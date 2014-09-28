package org.cebolla;

import org.reflections.Reflections;

/**
 * Global configuration for Cebolla
 * 
 * This class allows the developer to specify in which package to search to 
 * find fake repositories.
 * 
 * @author Bradford Hovinen <hovinen@gmail.com>
 *
 */
public class Cebolla {
    private static String fakeRepositoryPackage;
    private static Reflections reflections;

    static {
        fakeRepositoryPackage = System
                .getProperty("cebolla.repository.package");
    }

    /**
     * Specify that fake repositories are in the package whose name is given
     * or subpackages thereof.
     * 
     * Note: Does not check whether there is actually a package with the given 
     * name. If no such package exists, then no fake repositories will be found 
     * and the @see TestData rule will throw an error when attempting to inject
     * test-data.
     */
    public static void setFakeRepositoryPackage(String fakeRepositoryPackage) {
        Cebolla.fakeRepositoryPackage = fakeRepositoryPackage;
        reflections = null;
    }

    /**
     * Specify that fake repositories are in the given package or subpackages thereof.
     */
    public static void setFakeRepositoryPackage(Package fakeRepositoryPackage) {
        setFakeRepositoryPackage(fakeRepositoryPackage.getName());
    }

    /**
     * Create a Reflections-object based on the current configuration. For
     * internal use only.
     */
    public static Reflections createReflections() {
        if (reflections == null) {
            if (fakeRepositoryPackage == null) {
                reflections = new Reflections();
            } else {
                reflections = new Reflections(fakeRepositoryPackage);
            }
        }

        return reflections;
    }
}
