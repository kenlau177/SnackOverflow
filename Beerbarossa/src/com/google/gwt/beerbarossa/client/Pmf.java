package com.google.gwt.beerbarossa.client;
import javax.jdo.PersistenceManagerFactory;

import javax.jdo.JDOHelper;

public final class Pmf {
    private static final PersistenceManagerFactory pmfInstance = 
    		JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private Pmf() {
    }

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}