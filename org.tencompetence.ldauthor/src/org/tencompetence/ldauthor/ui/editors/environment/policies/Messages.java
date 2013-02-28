package org.tencompetence.ldauthor.ui.editors.environment.policies;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.editors.environment.policies.messages"; //$NON-NLS-1$

    public static String EnvironmentContainerEditPolicy_0;

    public static String EnvironmentContainerPartEditPolicy_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
