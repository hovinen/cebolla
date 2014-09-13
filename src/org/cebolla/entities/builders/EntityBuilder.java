/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.entities.builders;

import java.util.List;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public interface EntityBuilder<EntityType> {
    EntityType build();

    List<?> buildEntityList();
}
