package org.tencompetence.widgetadvert;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.widgetadvert.messages"; //$NON-NLS-1$

    public static String IWidgetConstants_0;

    public static String IWidgetConstants_1;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
