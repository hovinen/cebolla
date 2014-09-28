/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.repositories;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;

/**
 * Base class for fake repositories
 * 
 * This is a convenient base class to implement fake repositories. Fake
 * repositories can inherit from it to reduce boilerplate.
 * 
 * @author Bradford Hovinen
 */
public class AbstractFakeRepository<EntityType> {
    protected static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
	@Override
	public boolean apply(Object input) {
	    return true;
	}
    };

    private final List<EntityType> entities = new ArrayList<>();

    /**
     * Add an entity to the repository.
     */
    public void add(EntityType entity) {
	entities.add(entity);
    }

    /**
     * Remove the given entry from the repository.
     * 
     * If the entity is not present, take no action.
     */
    public void remove(EntityType entity) {
	entities.remove(entity);
    }

    /**
     * Filter all existing entries in the repository with the given predicate
     * and return the filtered entries in an @see java.lang.Iterable.
     */
    public Iterable<EntityType> filter(
	    Predicate<? super EntityType> predicate) {
	return Iterables.filter(entities, predicate);
    }

    /**
     * For internal use only
     */
    @SuppressWarnings("serial")
    public Class<?> getEntityType() {
	TypeToken<AbstractFakeRepository<EntityType>> token = new TypeToken<AbstractFakeRepository<EntityType>>(
		getClass()) {
	};
	Invokable<AbstractFakeRepository<EntityType>, Object> invokable = token.method(getAddMethod());
	return invokable.getParameters().get(0).getType().getRawType();
    }

    private Method getAddMethod() {
	try {
	    for (Method method : getClass().getMethods()) {
		if (method.getDeclaringClass() == AbstractFakeRepository.class
			&& "add".equals(method.getName())) {
		    return method;
		}
	    }

	    throw new RuntimeException("Could not find add-method");
	} catch (SecurityException e) {
	    throw Throwables.propagate(e);
	}
    }

    /**
     * Return all entries in the repository
     */
    public ImmutableList<EntityType> fetchAll() {
	return ImmutableList.copyOf(filter(ALWAYS_TRUE));
    }
}
