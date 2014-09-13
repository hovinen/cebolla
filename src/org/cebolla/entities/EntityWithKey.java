/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.entities;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public interface EntityWithKey<Key> {
    Key getKey();
}
