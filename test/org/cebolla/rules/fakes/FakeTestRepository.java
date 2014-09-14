/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.rules.fakes;

import java.util.ArrayList;
import java.util.List;

import org.cebolla.annotations.FakeRepository;
import org.cebolla.repositories.AbstractFakeRepositoryWithSimpleLookup;
import org.cebolla.rules.entities.TestEntity;
import org.cebolla.rules.entities.TestValueObject;
import org.cebolla.rules.repositories.TestRepository;

import com.google.common.collect.ImmutableList;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
@FakeRepository({ TestEntity.class, TestValueObject.class })
public class FakeTestRepository extends
	AbstractFakeRepositoryWithSimpleLookup<String, TestEntity>
	implements TestRepository {
    private final List<TestValueObject> valueObjectsAdded = new ArrayList<>();

    public void add(TestValueObject valueObject) {
	valueObjectsAdded.add(valueObject);
    }

    public void add(Integer integer) {
	throw new RuntimeException("Should not be called");
    }

    public ImmutableList<TestValueObject> getValueObjectsAdded() {
	return ImmutableList.copyOf(valueObjectsAdded);
    }
}
