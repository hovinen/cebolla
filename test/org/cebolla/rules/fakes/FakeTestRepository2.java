/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.rules.fakes;

import org.cebolla.annotations.FakeRepository;
import org.cebolla.repositories.AbstractFakeRepositoryWithSimpleLookup;
import org.cebolla.rules.entities.TestEntity2;
import org.cebolla.rules.repositories.TestRepository2;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
@FakeRepository(TestEntity2.class)
public class FakeTestRepository2 extends AbstractFakeRepositoryWithSimpleLookup<String, TestEntity2> implements TestRepository2 {
    public FakeTestRepository2 with(TestEntity2 entity) {
        add(entity);
        return this;
    }
}
