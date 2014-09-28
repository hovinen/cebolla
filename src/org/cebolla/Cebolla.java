package org.cebolla;

import org.reflections.Reflections;

public class Cebolla {
    private static String fakeRepositoryPackage;
    private static Reflections reflections;

    static {
        fakeRepositoryPackage = System
                .getProperty("cebolla.repository.package");
    }

    public static void setFakeRepositoryPackage(String fakeRepositoryPackage) {
        Cebolla.fakeRepositoryPackage = fakeRepositoryPackage;
        reflections = null;
    }

    public static void setFakeRepositoryPackage(Package fakeRepositoryPackage) {
        setFakeRepositoryPackage(fakeRepositoryPackage.getName());
    }

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
