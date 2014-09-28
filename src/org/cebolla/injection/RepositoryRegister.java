/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.injection;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cebolla.annotations.FakeRepository;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * @author Bradford Hovinen
 */
public class RepositoryRegister {
    private Map<Class<?>, Collection<Object>> repositories = new HashMap<>();

    public ImmutableCollection<?> getRepositoriesForDataClass(Class<?> dataClass) {
	ImmutableSet.Builder<Object> result = ImmutableSet.builder();

	if (repositories.containsKey(dataClass)) {
	    result.addAll(repositories.get(dataClass));
	}

	for (Class<?> iface : dataClass.getInterfaces()) {
	    result.addAll(getRepositoriesForDataClass(iface));
	}

	if (dataClass.getSuperclass() != null) {
	    result.addAll(getRepositoriesForDataClass(dataClass.getSuperclass()));
	}

	return result.build();
    }

    public Optional<Object> getRepositoryForInjection(Class<?> targetClass) {
	Set<Object> candidates = new HashSet<>();

	for (Collection<?> collection : repositories.values()) {
	    for (Object repository : collection) {
		if (targetClass.isAssignableFrom(repository.getClass())) {
		    candidates.add(repository);
		}
	    }
	}

	if (candidates.isEmpty()) {
	    return Optional.absent();
	} else if (candidates.size() > 1) {
	    throw new RuntimeException(
		    "Ambiguous fake repository for target-class " + targetClass
			    + ": " + candidates);
	} else {
	    return Optional.of(Iterables.getOnlyElement(candidates));
	}
    }

    public void addFakeRepository(Object fakeRepository) {
	if (!fakeRepository.getClass()
		.isAnnotationPresent(FakeRepository.class)) {
	    throw new RuntimeException("Class " + fakeRepository.getClass()
		    + " is not annotated with @"
		    + FakeRepository.class.getSimpleName());
	}

	FakeRepository annotation = fakeRepository.getClass().getAnnotation(
		FakeRepository.class);

	for (Class<?> datumClass : annotation.value()) {
	    if (repositories.containsKey(datumClass)) {
		repositories.get(datumClass).add(fakeRepository);
	    } else {
		repositories.put(datumClass, Sets.newHashSet(fakeRepository));
	    }
	}
    }
}
