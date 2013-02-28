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

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * "Rename File" Dialog
 * 
 * @author Phillip Beauvoir
 * @version $Id: RenameFileDialog.java,v 1.1 2009/06/07 14:05:59 phillipus Exp $
 */
public class RenameFileDialog
extends TitleAreaDialog {
	
    /*
     * Text Controls
     */
    private Text fText;

    /**
     * The file
     */
    private File fFile;
    
    /**
     * The renamed file
     */
    private File fRenamedFile;
    

	public RenameFileDialog(File file, Shell parentShell) {
        super(parentShell);
        setTitleImage(ImageFactory.getImage(ImageFactory.IMAGE_FOLDER));
        fFile = file;
	}
	
    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.RenameFileDialog_0);
        shell.setImage(ImageFactory.getImage("file")); //$NON-NLS-1$
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(Messages.RenameFileDialog_0);
        setMessage(fFile.getName());
        
        Composite composite = new Composite((Composite)super.createDialogArea(parent), SWT.NULL);

        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 10;
        layout.marginTop = 5;
        composite.setLayout(layout);
        
        GridData gd = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gd);
        
        // Name
        Label label = new Label(composite, SWT.NULL);
        label.setText(Messages.RenameFileDialog_1);
        
        fText = new Text(composite, SWT.BORDER);
        fText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fText.setText(fFile.getName());
        
        fText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateInput();
            }
        });
        
        return composite;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
    }

    /**
     * @return The renamed file or null
     */
    public File getRenamedFile() {
        return fRenamedFile;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        fRenamedFile = new File(fFile.getParentFile(), fText.getText());
        super.okPressed();
    }

    /**
     * Validate this input
     */
    private void validateInput() {
        String errorMessage = isValid(fText.getText());
        setErrorMessage(errorMessage);
        getButton(IDialogConstants.OK_ID).setEnabled(errorMessage == null);
    }

    /**
     * Validate user input
     */
    private String isValid(String newText) {
        if("".equals(newText.trim())) { //$NON-NLS-1$
            return Messages.RenameFileDialog_2;
        }
        
        // This will ensure non-legal filename characters are disallowed
        IStatus result = ResourcesPlugin.getWorkspace().validateName(newText, IResource.FILE);
        if(!result.isOK())  {
            return Messages.RenameFileDialog_3;
        }
        
        if(newText.equals(fFile.getName())) {
            return Messages.RenameFileDialog_4;
        }
        
        if(newText.equalsIgnoreCase(fFile.getName())) {
            return null;
        }

        File newfile = new File(fFile.getParent(), newText);
        if(newfile.exists()) {
            return Messages.RenameFileDialog_5;
        }
        
        return null;
    }
}
