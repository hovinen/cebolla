package org.cebolla;

import org.reflections.Reflections;

public class Cebolla {
    private static Package fakeRepositoryPackage;
    private static Reflections reflections;

    public static void setFakeRepositoryPackage(Package fakeRepositoryPackage) {
	Cebolla.fakeRepositoryPackage = fakeRepositoryPackage;
	reflections = null;
    }

    public static Reflections createReflections() {
	if (reflections == null) {
	    if (fakeRepositoryPackage == null) {
	        reflections = new Reflections();
	    } else {
	        reflections = new Reflections(fakeRepositoryPackage.getName());
	    }
	}

	return reflections;
    }
}
