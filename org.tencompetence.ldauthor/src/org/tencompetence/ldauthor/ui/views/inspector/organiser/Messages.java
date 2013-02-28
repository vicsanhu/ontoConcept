package org.tencompetence.ldauthor.ui.views.inspector.organiser;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.views.inspector.organiser.messages"; //$NON-NLS-1$

    public static String OrganiserFolderPropertySection_0;

    public static String OrganiserLDEntryPropertySection_0;

    public static String OrganiserLDEntryPropertySection_1;

    public static String OrganiserResourceEntryPropertySection_0;

    public static String OrganiserResourceEntryPropertySection_1;

    public static String OrganiserResourceEntryPropertySection_2;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
