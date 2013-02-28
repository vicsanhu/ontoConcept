package org.tencompetence.ldauthor.ui.views.inspector;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.views.inspector.messages"; //$NON-NLS-1$

    public static String InspectorView_0;

    public static String InspectorWindow_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
