/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.rules;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.cebolla.annotations.InjectFakeRepository;
import org.cebolla.annotations.InjectRepositories;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public class TestDataTest {
    private TestEntity entity = new TestEntity("entity1");
    private TestEntity2 entity2 = new TestEntity2("id");
    private TestEntity3 entity3 = new TestEntity3("id");
    private TestValueObject valueObject = new TestValueObject();

    @Rule
    public final TestData testData = new TestData()
    	.with(entity)
    	.with(entity3)
    	.with(valueObject);

    @InjectRepositories
    private TestBean testBean = new TestBean(new FakeTestRepository3());

    @InjectRepositories
    private TestBeanSubclass testBeanSubclass;

    @InjectRepositories
    private TestBean testBeanUninitialized;

    @InjectFakeRepository
    private FakeTestRepository fakeTestRepository;

    @InjectFakeRepository
    private FakeTestRepository2 fakeTestRepositoryAlreadyConstructed = new FakeTestRepository2()
	    .with(entity2);

    @Test
    public void rule_atTestStart_instantiatesClass() {
	assertNotNull(testBeanUninitialized);
    }

    @Test
    public void injectTestRepository_atTestStart_injectsFakeTestRepository() {
	assertThat(testBean.getTestRepository(),
		is(instanceOf(FakeTestRepository.class)));
    }

    @Test
    public void injectTestRepository_atTestStart_injectsInTestClass() {
	assertThat(testBean.getTestRepository(),
		is(sameInstance((TestRepository) fakeTestRepository)));
    }

    @Test
    public void fakeTestRepositoryLookup_afterTestStart_findsEntity() {
	assertThat(fakeTestRepository.lookup("entity1").orNull(),
		is(sameInstance(entity)));
    }

    @Test
    public void ruleAdd_duringTest_makesEntityAvailable() {
	TestEntity entity = new TestEntity("entity2");

	testData.with(entity);

	assertTrue(fakeTestRepository.lookup("entity2").isPresent());
    }

    @Test
    public void rule_afterTest_resetsFakeRepositories1() {
	TestEntity entity = new TestEntity("entity3");

	testData.with(entity);

	assertTrue(fakeTestRepository.lookup("entity3").isPresent());
	assertFalse(fakeTestRepository.lookup("entity4").isPresent());
    }

    @Test
    public void rule_afterTest_resetsFakeRepositories2() {
	TestEntity entity = new TestEntity("entity4");

	testData.with(entity);

	assertFalse(fakeTestRepository.lookup("entity3").isPresent());
	assertTrue(fakeTestRepository.lookup("entity4").isPresent());
    }

    @Test
    public void rule_repositoryAlreadyConstructed_usesConstructedRepository() {
	assertThat(fakeTestRepositoryAlreadyConstructed.lookup("id").orNull(),
		is(sameInstance(entity2)));
    }

    @Test
    public void rule_repositoryAlreadyConstructed_injectsUserSuppliedRepository() {
	assertThat(
		testBean.getTestRepository2(),
		is(sameInstance((TestRepository2) fakeTestRepositoryAlreadyConstructed)));
    }

    @Test
    public void rule_withValueObject_valueObjectInserted() {
	assertThat(fakeTestRepository.getValueObjectsAdded(),
		contains(valueObject));
    }

    @Test
    public void rule_beanAlreadyHasFakeRepository_dataAddedToFakeRepository() {
	assertThat(testBean.getTestRepository3().lookup("id").orNull(),
		is(sameInstance(entity3)));
    }

    @Test
    public void dataInjector_fieldInSuperclass_fieldInjected() {
	assertThat(testBeanSubclass.getTestRepository(),
		is(instanceOf(TestRepository.class)));
    }

    public static class TestBeanSubclass extends TestBean {

    }
}
