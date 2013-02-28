/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.ldauthor.ui.dialogs;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;


/**
 * "Rename" Dialog
 * 
 * @author Phillip Beauvoir
 * @version $Id: RenameObjectDialog.java,v 1.1 2009/06/07 14:05:59 phillipus Exp $
 */
public class RenameObjectDialog {
	
    /**
     * Owner Shell
     */
    private Shell fShell;
    
    /**
     * The old name
     */
    private String fOldName;
    
    /**
     * The new name
     */
    private String fNewName;

    /**
	 * Constructor
	 * @param object The Object to rename
	 */
	public RenameObjectDialog(Shell shell, String oldName) {
	    fShell = shell;
	    fOldName = oldName;
	}
	
    /**
     * Throw up a dialog asking for a new name
     * @return True if the user entered valid input, false if cancelled
     */
    public boolean open() {
        String title = Messages.RenameObjectDialog_0 + "\"" +fOldName + "\"" ; //$NON-NLS-1$ //$NON-NLS-2$
        
        InputDialog dialog = new InputDialog(fShell,
                title,
                Messages.RenameObjectDialog_1,
                fOldName,
                new InputValidator());
        
        int code = dialog.open();
        
        if(code == Window.OK) {
            fNewName = dialog.getValue();
            
            // Didn't change it
            if(fNewName.equals(fOldName)) {
                return false;
            }

            return true;
        }
        
        return false;
    }
    
    /**
     * @return The New name, or the old name if not changed
     */
    public String getNewName() {
        return fNewName;
    }
    
    /**
     * Validate user input
     */
    protected class InputValidator implements IInputValidator {
        /* (non-Javadoc)
         * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
         */
        public String isValid(String newText) {
            if("".equals(newText.trim())) { //$NON-NLS-1$
                return Messages.RenameObjectDialog_2;
            }
            
            if(newText.equals(fOldName)) {
                return ""; //$NON-NLS-1$
            }
            
            if(newText.equalsIgnoreCase(fOldName)) {
                return null;
            }

            //if(object.getParent().containsChildByName(object.getClass(), newText)) {
            //    return "Name already exists.";
            //}

            return null;
        }
    }
}
