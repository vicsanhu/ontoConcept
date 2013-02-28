package org.tencompetence.ldauthor.organisermodel.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.organisermodel.impl.messages"; //$NON-NLS-1$

    public static String OrganiserIndex_0;

    public static String OrganiserIndex_1;

    public static String OrganiserIndex_2;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
