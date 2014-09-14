/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.rules.entities;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public interface EntityRepositoryWithSimpleLookup<Key, EntityType> {
    Optional<EntityType> lookup(Key key);

    ImmutableList<EntityType> fetchAll();
}
