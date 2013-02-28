//------------------------------------------------------------------------------
// Copyright (c) 2005, 2006 IBM Corporation and others.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// Contributors:
// IBM Corporation - initial implementation
//------------------------------------------------------------------------------
package org.eclipse.epf.richtext.actions;

import org.eclipse.epf.richtext.IRichText;
import org.eclipse.epf.richtext.RichTextCommand;
import org.eclipse.epf.richtext.RichTextImages;
import org.eclipse.epf.richtext.RichTextResources;
import org.eclipse.epf.richtext.actions.RichTextAction;
import org.eclipse.epf.richtext.dialogs.AddLinkDialog;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;


/**
 * This is a patched version of the add link action that passed both the link
 * and the name. This method calls a patched version of the addLink JScript
 * function.
 * 
 * @author Hubert Vogten
 * 
 * @version $Revision: 1.2 $, $Date: 2008/06/06 13:49:48 $, $Author: hubertvogten $
 */
public class AddLinkAction extends RichTextAction {

	/**
	 * Creates a new instance.
	 */
	public AddLinkAction(IRichText richText) {
		super(richText, IAction.AS_PUSH_BUTTON);
		setImageDescriptor(RichTextImages.IMG_DESC_ADD_LINK);
		setDisabledImageDescriptor(RichTextImages.DISABLED_IMG_DESC_ADD_LINK);
		setToolTipText(RichTextResources.addLinkAction_toolTipText);
	}

	/**
	 * Executes the action.
	 * 
	 * @param richText
	 *            a rich text control
	 */
	public void execute(IRichText richText) {
		if (richText != null) {
			AddLinkDialog dialog = new AddLinkDialog(Display.getCurrent().getActiveShell(), richText.getBasePath());
			dialog.open();
			if (dialog.getReturnCode() == Window.OK) {
				String linkURL = dialog.getLink().getURL();
				String linkName = dialog.getLink().getName();
				if (linkURL.length() > 0) {
					richText.executeCommand(RichTextCommand.ADD_LINK, new String[] { linkURL, linkName });
				}
			}
		}
	}

	public boolean disableInSourceMode() {
		return false;
	}
}

