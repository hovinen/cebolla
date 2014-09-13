/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.rules;

import org.cebolla.annotations.FakeRepository;
import org.cebolla.repositories.AbstractFakeRepositoryWithSimpleLookup;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
@FakeRepository({ TestEntity3.class })
public class FakeTestRepository3 extends
	AbstractFakeRepositoryWithSimpleLookup<String, TestEntity3> implements
	TestRepository3 {

}
