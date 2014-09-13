/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.injection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.cebolla.repositories.AbstractFakeRepository;

import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public class TestDataInjector {
    public void inject(Object repository, Object datum) {
	Method method = findMethod(repository, "add", datum.getClass());
	invokeMethod(method, repository, datum);
    }

    public void remove(Object repository, Object datum) {
	Method method = findMethod(repository, "remove", datum.getClass());
	invokeMethod(method, repository, datum);
    }

    private Method findMethod(Object target, String methodName,
	    Class<? extends Object> parameterClass) {
	Collection<Method> bestCandidates = new ArrayList<>();
	Collection<Method> candidates = new ArrayList<>();

	for (Method method : target.getClass().getMethods()) {
	    if (methodName.equals(method.getName())
		    && method.getParameterTypes().length == 1) {
		if (target instanceof AbstractFakeRepository
			&& method.getDeclaringClass() == AbstractFakeRepository.class) {
		    AbstractFakeRepository<?> amr = (AbstractFakeRepository<?>) target;
		    Class<?> entityType = amr.getEntityType();
		    if (!entityType.isAssignableFrom(parameterClass)) {
			continue;
		    }
		}

		if (method.getParameterTypes()[0] == parameterClass) {
		    bestCandidates.add(method);
		}

		if (method.getParameterTypes()[0]
			.isAssignableFrom(parameterClass)) {
		    candidates.add(method);
		}
	    }
	}

	if (bestCandidates.size() == 1) {
	    return Iterables.getOnlyElement(bestCandidates);
	}

	if (candidates.size() == 0) {
	    throw new RuntimeException("Could not find suitable " + methodName
		    + "()-method in " + target + " for adding object of type "
		    + parameterClass);
	} else if (candidates.size() > 1) {
	    throw new RuntimeException("Ambiguous " + methodName
		    + "()-methods in repository " + target
		    + " for object of type " + parameterClass + ": "
		    + candidates);
	} else {
	    return Iterables.getOnlyElement(candidates);
	}
    }

    private Object invokeMethod(Method method, Object object, Object parameter) {
	try {
	    return method.invoke(object, parameter);
	} catch (IllegalAccessException | IllegalArgumentException
		| InvocationTargetException e) {
	    throw Throwables.propagate(e);
	}
    }
}
