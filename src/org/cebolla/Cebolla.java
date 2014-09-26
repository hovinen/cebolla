package org.cebolla;

import org.reflections.Reflections;

public class Cebolla {
    private static Package fakeRepositoryPackage;

    public static void setFakeRepositoryPackage(Package fakeRepositoryPackage) {
	Cebolla.fakeRepositoryPackage = fakeRepositoryPackage;
    }

    public static Reflections createReflections() {
	if (fakeRepositoryPackage == null) {
	    return new Reflections();
	} else {
	    return new Reflections(fakeRepositoryPackage.getName());
	}
    }
}
