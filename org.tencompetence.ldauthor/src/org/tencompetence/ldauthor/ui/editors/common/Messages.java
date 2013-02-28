package org.tencompetence.ldauthor.ui.editors.common;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.editors.common.messages"; //$NON-NLS-1$

    public static String AbstractLDEditorPalette_0;

    public static String LDGraphicalEditorActionBarContributor_0;

    public static String LDGraphicalEditorActionBarContributor_1;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
