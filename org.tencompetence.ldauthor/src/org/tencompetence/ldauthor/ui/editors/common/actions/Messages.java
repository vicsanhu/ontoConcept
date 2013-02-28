package org.tencompetence.ldauthor.ui.editors.common.actions;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.tencompetence.ldauthor.ui.editors.common.actions.messages"; //$NON-NLS-1$

    public static String DefaultEditPartSizeAction_0;

    public static String DefaultEditPartSizeAction_1;

    public static String DefaultEditPartSizeRetargetAction_0;

    public static String DefaultEditPartSizeRetargetAction_1;

    public static String PreviewAction_0;

    public static String PropertiesAction_0;

    public static String PropertiesRetargetAction_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
