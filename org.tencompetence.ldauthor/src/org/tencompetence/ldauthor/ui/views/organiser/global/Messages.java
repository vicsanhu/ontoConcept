package org.tencompetence.ldauthor.ui.views.organiser.global;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.views.organiser.global.messages"; //$NON-NLS-1$

    public static String OrganiserComposite_0;

    public static String OrganiserComposite_1;

    public static String OrganiserComposite_2;

    public static String OrganiserComposite_3;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
