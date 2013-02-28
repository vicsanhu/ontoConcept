package org.tencompetence.ldauthor.graphicsmodel.environment.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.graphicsmodel.environment.impl.messages"; //$NON-NLS-1$

    public static String GraphicalEnvironmentConnection_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
