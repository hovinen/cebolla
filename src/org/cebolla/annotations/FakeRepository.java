/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies to the data-injector the class is a fake repository
 * 
 * Annotate fake repositories with this annotation. As a parameter supply
 * a list of classes for which the fake repository accepts test-data. For
 * each such class there must be an method with the name <code>add</code>
 * taking exactly one parameter of a type from which the test data class
 * inherits. In case of ambiguity, an error is thrown.
 * 
 * @author Bradford Hovinen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FakeRepository {
    public Class<?>[] value();
}
