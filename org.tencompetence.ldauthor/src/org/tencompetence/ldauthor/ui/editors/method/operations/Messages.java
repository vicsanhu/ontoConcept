package org.tencompetence.ldauthor.ui.editors.method.operations;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.editors.method.operations.messages"; //$NON-NLS-1$

    public static String DeleteActOperation_0;

    public static String DeleteActOperation_1;

    public static String DeletePlayOperation_0;

    public static String DeletePlayOperation_1;

    public static String MoveActOperation_0;

    public static String MovePlayOperation_0;

    public static String NewActOperation_0;

    public static String NewPlayOperation_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
