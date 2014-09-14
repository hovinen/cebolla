/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.injection;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.cebolla.annotations.FakeRepository;
import org.cebolla.annotations.InjectFakeRepository;
import org.cebolla.annotations.InjectRepositories;
import org.reflections.Reflections;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public class FakeRepositoryDiscoverer {
    private static final Reflections REFLECTIONS = new Reflections();

    private final RepositoryRegister register;

    public FakeRepositoryDiscoverer(RepositoryRegister register) {
	this.register = register;
    }

    public void scanTarget(Object target) {
	try {
	    for (Field field : target.getClass().getDeclaredFields()) {
		if (field.isAnnotationPresent(InjectFakeRepository.class)) {
		    registerFakeIfPresent(target, field);
		} else if (field.isAnnotationPresent(InjectRepositories.class)) {
		    scanMembers(target, field);
		}
	    }
	} catch (SecurityException | IllegalArgumentException
		| IllegalAccessException e) {
	    Throwables.propagate(e);
	}
    }

    private void scanMembers(Object testClassInstance, Field testClassField)
	    throws IllegalAccessException {
	testClassField.setAccessible(true);
	Object target = testClassField.get(testClassInstance);

	for (Field field : testClassField.getType().getDeclaredFields()) {
	    if (field.getType().isAnnotationPresent(FakeRepository.class)) {
		registerFakeIfPresent(target, field);
	    }
	}
    }

    private void registerFakeIfPresent(Object target, Field field)
	    throws IllegalAccessException {
	field.setAccessible(true);

	Object fakeRepository = field.get(target);

	if (fakeRepository != null) {
	    register.addFakeRepository(fakeRepository);
	}
    }

    public Optional<Object> instantiateFakeRepository(
	    Class<?> repositoryInterface) {
	try {
	    Class<?> repositoryClass = getRepositoryClass(repositoryInterface);

	    if (repositoryClass == null) {
		return Optional.absent();
	    } else {
		Object repositoryInstance = repositoryClass.newInstance();
		register.addFakeRepository(repositoryInstance);
		return Optional.of(repositoryInstance);
	    }
	} catch (InstantiationException | IllegalAccessException e) {
	    throw Throwables.propagate(e);
	}
    }

    @SuppressWarnings("unchecked")
    private Class<?> getRepositoryClass(Class<?> repositoryInterface) {
	if (repositoryInterface.isAnnotationPresent(FakeRepository.class)) {
	    return repositoryInterface;
	}

	Set<Class<?>> candidates = (Set<Class<?>>) (Object) REFLECTIONS
		.getSubTypesOf(repositoryInterface);
	Set<Class<?>> filteredCandidates = new HashSet<>();
	for (Class<?> candidate : candidates) {
	    if (candidate.isAnnotationPresent(FakeRepository.class)) {
		filteredCandidates.add(candidate);
	    }
	}

	if (filteredCandidates.size() == 0) {
	    return null;
	} else if (filteredCandidates.size() > 1) {
	    throw new RuntimeException(
		    "Ambiguous fake-repositories for interface "
			    + repositoryInterface + ": " + filteredCandidates);
	} else {
	    return Iterables.getOnlyElement(filteredCandidates);
	}
    }
}
