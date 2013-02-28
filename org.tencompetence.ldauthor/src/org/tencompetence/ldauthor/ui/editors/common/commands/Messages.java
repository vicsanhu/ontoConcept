package org.tencompetence.ldauthor.ui.editors.common.commands;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.editors.common.commands.messages"; //$NON-NLS-1$

    public static String CreateObjectCommand_0;

    public static String DeleteConnectionCommand_0;

    public static String DeleteObjectCommand_0;

    public static String MoveObjectCommand_0;

    public static String RenameObjectCommand_0;

    public static String SetConstraintObjectCommand_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
