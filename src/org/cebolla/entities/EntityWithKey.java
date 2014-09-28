/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.entities;

/**
 * This interface should be implemented by any entity used in @see AbstractFakeRepositoryWithSimpleLookup
 * 
 * It specifies that an entity has a key with which one can look it up.
 * 
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public interface EntityWithKey<Key> {
    /**
     * Return the key for the entity
     */
    Key getKey();
}
