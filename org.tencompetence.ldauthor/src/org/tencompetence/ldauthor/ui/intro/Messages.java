package org.tencompetence.ldauthor.ui.intro;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.intro.messages"; //$NON-NLS-1$

    public static String LDAuthorIntroPart_0;

    public static String LDAuthorIntroPart_1;

    public static String LDAuthorIntroPart_2;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
