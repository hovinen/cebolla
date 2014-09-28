/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify that a fake repository should be injected into the given field.
 * 
 * Annotate a field in the test-class with this annotation to indicate that
 * Cebolla should inject an instance of the corresponding fake repository
 * to that field during initialisation.
 * 
 * The fake repository is determined by searching all classes annotated
 * with @see FakeRepository and whose type inherits from that of the field.
 * In case of ambiguity an exception is thrown.
 * 
 * The same instance of the fake repository is injected as into the class
 * under test with @see InjectRepositories.
 * 
 * @author Bradford Hovinen
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectFakeRepository {

}
