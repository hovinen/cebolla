/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.injection;

import java.lang.reflect.Field;

import org.cebolla.annotations.InjectFakeRepository;
import org.cebolla.annotations.InjectRepositories;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;

/**
 * @author Bradford Hovinen
 */
public class FakeRepositoryInjector {
    private final RepositoryRegister register;
    private final FakeRepositoryDiscoverer discoverer;

    public FakeRepositoryInjector(RepositoryRegister register,
	    FakeRepositoryDiscoverer discoverer) {
	this.register = register;
	this.discoverer = discoverer;
    }

    public void scanTarget(Object target) {
	try {
	    Class<? extends Object> testClass = target.getClass();
	    injectFieldsOfTestClass(target, testClass);
	} catch (SecurityException | IllegalArgumentException
		| IllegalAccessException e) {
	    Throwables.propagate(e);
	}
    }

    private void injectFieldsOfTestClass(Object target,
	    Class<? extends Object> testClass) throws IllegalAccessException {
	for (Field field : testClass.getDeclaredFields()) {
	    if (field.isAnnotationPresent(InjectFakeRepository.class)) {
		if (!injectFakeRepository(target, field)) {
		    throw new RuntimeException(
			    "Could not find candidate to inject to field of type "
				    + field.getType() + " in target "
				    + testClass);
		}
	    } else if (field.isAnnotationPresent(InjectRepositories.class)) {
		scanMembers(target, field);
	    }
	}

	if (testClass.getSuperclass() != null) {
	    injectFieldsOfTestClass(target, testClass.getSuperclass());
	}
    }

    private void scanMembers(Object testClassInstance, Field testClassField)
	    throws IllegalAccessException {
	testClassField.setAccessible(true);
	Object target = testClassField.get(testClassInstance);

	if (target == null) {
	    target = instantiateTestClass(testClassInstance, testClassField);
	}

	injectFieldsOfClassUnderTest(target, testClassField.getType());
    }

    private void injectFieldsOfClassUnderTest(Object target,
	    Class<?> classUnderTest) {
	for (Field field : classUnderTest.getDeclaredFields()) {
	    injectFakeRepository(target, field);
	}

	if (classUnderTest.getSuperclass() != null) {
	    injectFieldsOfClassUnderTest(target, classUnderTest.getSuperclass());
	}
    }

    private Object instantiateTestClass(Object testClassInstance,
	    Field testClassField) {
	try {
	    Object newInstance = testClassField.getType().newInstance();
	    testClassField.set(testClassInstance, newInstance);
	    return newInstance;
	} catch (InstantiationException | IllegalAccessException
		| IllegalArgumentException e) {
	    throw Throwables.propagate(e);
	}
    }

    private boolean injectFakeRepository(Object target, Field field) {
	try {
	    field.setAccessible(true);

	    Optional<Object> fakeRepository = register
		    .getRepositoryForInjection(field.getType());

	    if (!fakeRepository.isPresent()) {
		fakeRepository = discoverer.instantiateFakeRepository(field
			.getType());
	    }

	    if (fakeRepository.isPresent()) {
		field.set(target, fakeRepository.get());
		return true;
	    } else {
		return false;
	    }
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw Throwables.propagate(e);
	}
    }

}
