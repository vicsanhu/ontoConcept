package org.tencompetence.ldauthor.templates;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.templates.messages"; //$NON-NLS-1$

    public static String LDTemplateManager_0;

    public static String LDTemplateManager_1;

    public static String LDTemplateManager_2;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
