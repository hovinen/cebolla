/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectFakeRepository {

}
