package org.tencompetence.ldauthor.ui.editors.text;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.editors.text.messages"; //$NON-NLS-1$

    public static String SimpleTextEditor_0;

    public static String SimpleTextEditor_1;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
