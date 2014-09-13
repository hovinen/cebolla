/* This file is part of Cebolla, licensed under the GNU GPL v3. See COPYING for details.
 * Copyright 2014 Bradford Hovinen <hovinen@gmail.com>
 */

package org.cebolla.rules;

/**
 * @author Bradford Hovinen <hovinen@gmail.com>
 */
public class TestBean {
    private TestRepository testRepository;

    private TestRepository2 testRepository2;

    private TestRepository3 testRepository3;

    public TestBean() {
    }

    public TestBean(TestRepository3 testRepository3) {
	this.testRepository3 = testRepository3;
    }

    public TestRepository getTestRepository() {
	return testRepository;
    }

    public TestRepository2 getTestRepository2() {
	return testRepository2;
    }

    public TestRepository3 getTestRepository3() {
	return testRepository3;
    }
}
