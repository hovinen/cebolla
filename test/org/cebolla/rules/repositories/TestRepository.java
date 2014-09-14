/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.rules.repositories;

import org.cebolla.rules.entities.EntityRepositoryWithSimpleLookup;
import org.cebolla.rules.entities.TestEntity;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public interface TestRepository extends EntityRepositoryWithSimpleLookup<String, TestEntity> {
}
