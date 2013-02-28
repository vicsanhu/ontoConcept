package org.tencompetence.ldauthor.ui.views.organiser.environments;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.views.organiser.environments.messages"; //$NON-NLS-1$

    public static String EnvironmentsComposite_0;

    public static String EnvironmentsComposite_1;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}