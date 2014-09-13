/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.rules;

import java.util.ArrayList;
import java.util.List;

import org.cebolla.entities.builders.EntityBuilder;
import org.cebolla.injection.FakeRepositoryDiscoverer;
import org.cebolla.injection.FakeRepositoryInjector;
import org.cebolla.injection.RepositoryRegister;
import org.cebolla.injection.TestDataInjector;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public class TestData implements MethodRule {
    private RepositoryRegister register = new RepositoryRegister();
    private FakeRepositoryDiscoverer discoverer = new FakeRepositoryDiscoverer(
	    register);
    private FakeRepositoryInjector repositoryInjector = new FakeRepositoryInjector(
	    register, discoverer);
    private TestDataInjector dataInjector = new TestDataInjector();

    private final List<Object> toBeAdded = new ArrayList<>();
    private boolean testRunning = false;

    public TestData with(Object entity) {
	if (testRunning) {
	    add(entity);
	} else {
	    toBeAdded.add(entity);
	}
	return this;
    }

    public <Value> TestData with(EntityBuilder<Value> builder) {
	for (Object entity : builder.buildEntityList()) {
	    with(entity);
	}
	return this;
    }

    private void add(Object entity) {
	for (Object repository : register.getRepositoriesForDataClass(entity
		.getClass())) {
	    dataInjector.inject(repository, entity);
	}
    }

    public void remove(Object entity) {
	for (Object repository : register.getRepositoriesForDataClass(entity
		.getClass())) {
	    dataInjector.remove(repository, entity);
	}
    }

    @Override
    public Statement apply(final Statement statement, FrameworkMethod method,
	    final Object target) {
	return new Statement() {

	    @Override
	    public void evaluate() throws Throwable {
		discoverer.scanTarget(target);
		repositoryInjector.scanTarget(target);
		addAll();
		testRunning = true;
		statement.evaluate();
		testRunning = false;
	    }

	};
    }

    private void addAll() {
	for (Object entity : toBeAdded) {
	    add(entity);
	}
    }
}
