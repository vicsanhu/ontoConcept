package org.tencompetence.ldauthor.fedora.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.fedora.preferences.messages"; //$NON-NLS-1$

    public static String ConnectionPreferencePage_0;

    public static String ConnectionPreferencePage_1;

    public static String ConnectionPreferencePage_2;

    public static String ConnectionPreferencePage_3;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
