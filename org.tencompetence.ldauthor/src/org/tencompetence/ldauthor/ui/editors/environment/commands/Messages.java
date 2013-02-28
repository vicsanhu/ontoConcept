package org.tencompetence.ldauthor.ui.editors.environment.commands;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.editors.environment.commands.messages"; //$NON-NLS-1$

    public static String CreateEnvironmentConnectionCommand_0;

    public static String OrphanEnvironmentChildCommand_0;

    public static String ReconnectSourceEnvironmentConnectionCommand_0;

    public static String ReconnectTargetEnvironmentConnectionCommand_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
