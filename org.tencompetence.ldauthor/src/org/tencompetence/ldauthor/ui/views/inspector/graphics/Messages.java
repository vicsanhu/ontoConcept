package org.tencompetence.ldauthor.ui.views.inspector.graphics;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.views.inspector.graphics.messages"; //$NON-NLS-1$

    public static String ConnectionPropertySection_0;

    public static String ConnectionPropertySection_1;

    public static String ConnectionPropertySection_2;

    public static String ConnectionPropertySection_3;
    
    public static String NotePropertySection_0;

    public static String NotePropertySection_1;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
