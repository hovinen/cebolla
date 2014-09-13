/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.rules;

import org.cebolla.entities.EntityWithKey;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public class TestEntity3 implements EntityWithKey<String> {
    private final String id;

    public TestEntity3(String id) {
	this.id = id;
    }

    @Override
    public String getKey() {
	return id;
    }
}
