/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify that fake repositories should be injected into the instance
 * 
 * Annotate a field in the test-class to indicate that into the given instance
 * fake repositories should be injected wherever possible.
 * 
 * If the field is null when the rule runs, Cebolla attempts to instantiate it
 * using a parameterless constructor. If that fails, then Cebolla throws an
 * error.
 * 
 * Cebolla scans the class for possible injection points and injects fake
 * repositories as it finds them. Exactly one instance of each fake repository
 * is injected at all locations. The same instance is also injected to points
 * annotated in the test-class with @see InjectFakeRepository.
 * 
 * @author Bradford Hovinen
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectRepositories {

}
