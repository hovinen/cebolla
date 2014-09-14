/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.rules.entities;

import org.cebolla.entities.EntityWithKey;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public class TestEntity implements EntityWithKey<String>, TestEntityInterface {
    private final String id;

    public TestEntity(String id) {
	this.id = id;
    }

    @Override
    public String getKey() {
	return id;
    }
}
