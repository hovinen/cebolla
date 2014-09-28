Cebolla 0.1
Copyright 2014 Bradford Hovinen <hovinen@gmail.com>

Cebolla is a framework for injecting test-data into repositories. It consists of:
 - a JUnit-rule with which to specify test-data, and
 - a set of annotations with which to identify repositories.

Introduction
============

Say you have an application with some business-logic. The logic works with domain-objects (also known as entities) which are managed by some sort of repository, as in the following example.

    class MyDomainObject {
        private final String id;
        private final int property;
        private final String otherProperty;

        ...
    }

    interface MyDomainObjectRepository {
        public Optional<MyDomainObject> lookup(String id);
        public List<MyDomainObject> findAllWithOtherPropertyEqualTo(String value);
        public void persist(MyDomainObject obj);
    }

The repository is the gateway from the business-logic to the persistence-layer. So you may have some RDBMS-based implementation of the repository:

    class MyDomainObjectDatabaseRepository implements MyDomainObjectRepository {
        public Optional<MyDomainObject> lookup(String id) {
            // Run database-query select <fields> from <table> where id = :id; pack results in MyDomainObject if found; otherwise return Optional.absent
        }

        public List<MyDomainObject> findAllWithOtherPropertyEqualTo(String value) {
            // ...
        }

        public void persist(MyDomainObject obj) {
            // ...
        }
    }

Let's say your business-logic is in a class like the following:

    class MyBusinessLogic {
        @Inject
        private MyDomainObjectRepository repository;

        public doSomething(String domainObjectProperty) {
            List<MyDomainObject> objects = repository.findAllWithOtherPropertyEqualTo(domainObjectProperty);
            ...
            MyDomainObject newObject = ...
            ...
            repository.persist(newObject);
        }
    }

Now you want to unit-test your business-logic. One way to do this is to use a mocking-framework such as Mockito:

    class MyBusinessLogicTest {
        @Mock
        private MyDomainObjectRepository mockRepository;

        @InjectMocks
        private MyBusinessLogic businessLogic;

        @Before
        public void setup() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void doSomething_whenPropertyIsPresent_doesTheRightThing() {
            List<MyDomainObject> myObjects = ...
            when(mockRepository.findAllWithOtherPropertyEqualTo("foobar")).thenReturn(myObjects);

            businessLogic.doSomething("foobar");

            verify(repository.persist(...));
        }
    }

This approach has a few drawbacks:
 - Maintaining the stubs is a pain, especially as the repository becomes more complex.
 - Stubs are hard to debug -- if a stub is wrongly configured, then it just returns null, and it can be hard to track why.
 - Sharing test-data between test-cases can be a real nightmare.
 - Perhaps most importantly, the test must know how the repository is accessed, violating the encapsulation of the class under test. This leads to fragile tests.

A cleaner approach would be to implement a fake-repository which implements the same interface:

    class MyDomainObjectFakeRepository implements MyDomainObjectRepository {
        private final List<MyDomainObject> objects = new ArrayList<>();

        public void add(MyDomainObject object) {
            objects.add(object);
        }

        @Override
        public Optional<MyDomainObject> lookup(String id) {
            for(MyDomainObject candidate : objects) {
                if(Objects.equal(id, candidate.getId())) {
                    return Optional.of(candidate);
                }
            }

            return Optional.absent();
        }
    }

With this we don't need to manage the stubs individually any more. We just add the test-data to the repository and the methods return the correct responses. If a key is not found, the repository behaves as its database-cousin would: it returns Optional.absent() respectively an empty list, not null as the mock would.

Cebolla is a small framework which takes this to the next level. With an annotation you can mark a repository as a fake to be injected into the class under test. You then specify which test-data the repository accepts:

    @FakeRepository(MyDomainObject.class)
    class MyDomainObjectFakeRepository extends AbstractFakeRepositoryWithSimpleLookup<MyDomainObject> implements MyDomainObjectRepository {
        // lookup already in superclass

        @Override
        public List<MyDomainObject> findAllWithOtherPropertyEqualTo(String value) {
            return filter(new Predicate() { ... });
        }

        public void assertThatObjectWasPersisted(Matcher<MyDomainObject> object) {
            ...
        }
    }

A simple implementation with a lookup-operation is provided so that you don't need to implement as much yourself.

Test-data are specified with a JUnit-rule:

    class MyBusinessLogicTest {
        @Rule
        public TestData testData = new TestData().with(new MyDomainObject(...));

        @InjectRepositories
        private MyBusinessLogic businessLogic;

        @InjectFakeRepository
        private MyDomainObjectFakeRepository repository;

        @Test
        public void doSomething_whenPropertyIsPresent_doesTheRightThing() {
            businessLogic.doSomething("foobar");

            repository.assertThatObjectWasPersisted(...);
        }
    }

The annotation @InjectRepositories tells the rule to inject fake repositories into the appropriate fields in the given class -- typically the class under test.

With the annotation @InjectFakeRepository one can inject the fake repository into the test-class itself in order to do asserts on it. This is useful when verifying that an operation with a side-effect was invoked, such as when persisting a domain-object.

One can also add test-data in the test-method itself so that they are only available in that method:

    class MyBusinessLogicTest {
        @Rule
        public TestData testData = new TestData().with(new MyDomainObject(...));

        ...

        @Test
        public void doSomething_whenPropertyIsPresent_doesTheRightThing() {
            MyDomainObject object1 = ...
            MyDomainObject object2 = ...

            testData.with(object1).with(object2);

            ...
        }
    }

Repositories taking multiple domain-objects

A fake repository can support multiple domain-objects. Just add them to the annotation as a list:

    @FakeRepository( { MyDomainObject.class, MyOtherDomainObject.class } )
    class MyDomainObjectFakeRepository extends AbstractFakeRepositoryWithSimpleLookup<MyDomainObject> implements MyDomainObjectRepository {
        ...
    }

It is then necessary to define a method called add for each of the other classes:

    public void add(MyOtherDomainObject object) {
        ...
    }

The base-class AbstractFakeRepositoryWithSimpleLookup handles only the type whose parameter is given. The developer of the repository must then handle the remaining add-methods as well as any associated query-methods herself.

It is genenerally recommended not to have more than one domain object for each repository. Having more than one domain-object for a given repository may, however, make sense when the two types are closely related.

Finding fake repositories
=========================

By default, Cebolla searches the entire classpath for classes with the annotation @FakeRepository, then checks the given list of classes for which domain-objects the fake repository accepts and injects the data with the method add. Searching the whole classpath is often quite slow and not really necessary in most cases. Thus Cebolla offers the ability to restrict the search to subpackages of a given package. One can specify this via a static method:

    Cebolla.setFakeRepositoryPackage(<package name>);

or via the property cebolla.repository.package:

    -Dcebolla.repository.package=<package name>

Future work
===========

* Add constructor-based injection

