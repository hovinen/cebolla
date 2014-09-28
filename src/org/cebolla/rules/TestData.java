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
 * Specify test-data to be injected.
 * 
 * This rule must be included in the test-class in order that Cebolla
 * inject repositories in either the test-class or the class under
 * test.
 * 
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

    /**
     * Include the given entity among the test-data
     * 
     * The given entity is then injected to all fake repositories which
     * whose annotation includes a superclass or interface thereof among
     * its parameters.
     * 
     * @return this
     */
    public TestData with(Object entity) {
	if (testRunning) {
	    add(entity);
	} else {
	    toBeAdded.add(entity);
	}
	return this;
    }

    /**
     * Include all entities built by the given builder in the test-data
     * 
     * This invokes the builder and injects the resulting entity and all
     * dependencies thereof using the same method as the method @see with.
     * 
     * @return this
     */
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

    /**
     * Remove the given entity from the test-data
     * 
     * Removes the entity from all fake repositories into which it was
     * injected. This requires that all relevant fake repositories
     * include a method <code>remove</code> which does the actual
     * removal. The method must take exactly one parameter of a type
     * inherited by the entity to be removed.
     */
    public void remove(Object entity) {
	for (Object repository : register.getRepositoriesForDataClass(entity
		.getClass())) {
	    dataInjector.remove(repository, entity);
	}
    }

    /**
     * @see MethodRule.apply
     */
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
