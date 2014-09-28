/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.entities.builders;

import java.util.List;

/**
 * Builder for an entity
 * 
 * This interface allows the creation of entities with dependencies. It
 * is used by @see TestData to inject the entity with all of its
 * dependencies in one command so that the developer need not do so by
 * hand.
 * 
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public interface EntityBuilder<EntityType> {
    /**
     * Build the entity itself
     */
    EntityType build();

    /**
     * Build the entity along with all of its dependencies
     * 
     * @return a list including the entity and all dependencies
     */
    List<?> buildEntityList();
}
