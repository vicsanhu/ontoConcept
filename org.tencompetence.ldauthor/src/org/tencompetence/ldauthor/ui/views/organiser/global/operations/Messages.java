package org.tencompetence.ldauthor.ui.views.organiser.global.operations;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.views.organiser.global.operations.messages"; //$NON-NLS-1$

    public static String CopyOrganiserEntriesOperation_0;

    public static String CopyOrganiserEntriesOperation_1;

    public static String DeleteOrganiserEntriesOperation_0;

    public static String DeleteOrganiserEntriesOperation_1;

    public static String DeleteOrganiserEntriesOperation_2;

    public static String MoveOrganiserEntriesOperation_0;

    public static String MoveOrganiserEntriesOperation_1;

    public static String NewOrganiserEntryOperation_0;

    public static String RenameOrganiserEntryOperation_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
