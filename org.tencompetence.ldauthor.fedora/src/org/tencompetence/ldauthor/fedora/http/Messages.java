package org.tencompetence.ldauthor.fedora.http;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.fedora.http.messages"; //$NON-NLS-1$

    public static String HTTPHandler_0;

    public static String HTTPHandler_1;

    public static String HTTPHandler_2;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
