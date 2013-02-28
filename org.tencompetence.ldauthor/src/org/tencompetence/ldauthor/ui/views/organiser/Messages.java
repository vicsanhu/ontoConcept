package org.tencompetence.ldauthor.ui.views.organiser;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.views.organiser.messages"; //$NON-NLS-1$

    public static String OrganiserView_1;

    public static String OrganiserView_10;

    public static String OrganiserView_3;

    public static String OrganiserView_5;

    public static String OrganiserView_7;

    public static String OrganiserView_8;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
