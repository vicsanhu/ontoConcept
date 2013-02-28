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
package org.tencompetence.ldauthor.preferences.editors;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Dialog for ading new External Editor Entry in Preferences
 * 
 * @author Phillip Beauvoir
 * @version $Id: EditorEntryDialog.java,v 1.4 2009/06/26 18:02:10 phillipus Exp $
 */
public class EditorEntryDialog extends Dialog {

    /**
     * The Text Fields
     */
    private Text fExtensionTextField, fProgramPathTextField;
    
    /**
     * Browse Button
     */
    private Button fBrowseButton;
    
    /* 
     * Values
     */
    public String fExtension, fProgramPath;

    
    /**
     * Constuctor
     * @param parentShell
     */
    public EditorEntryDialog(Shell parentShell) {
        super(parentShell);
    }

    /**
     * Constuctor
     *
     * @param parentShell
     * @param extension
     * @param programPath
     */
    public EditorEntryDialog(Shell parentShell, String extension, String programPath) {
        super(parentShell);
        this.fExtension = extension;
        this.fProgramPath = programPath;
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if(buttonId == IDialogConstants.OK_ID) {
            fExtension = fExtensionTextField.getText();
            fProgramPath = fProgramPathTextField.getText();
        }
        
        super.buttonPressed(buttonId);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.EditorEntryDialog_0);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        
        GridLayout layout = new GridLayout(4, false);
        composite.setLayout(layout);
        
        GridData gd;
        
        Label label = new Label(composite, SWT.WRAP);
        label.setText(Messages.EditorEntryDialog_1);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 4;
        label.setLayoutData(gd);
        
        label = new Label(composite, SWT.WRAP);
        label.setText(Messages.EditorEntryDialog_2);
        
        fExtensionTextField = new Text(composite, SWT.BORDER | SWT.SINGLE);
        gd = new GridData();
        gd.horizontalSpan = 3;
        gd.widthHint = 80; // Stops size being set by length of text
        fExtensionTextField.setLayoutData(gd);
        if(fExtension != null) {
            fExtensionTextField.setText(fExtension);
        }
        
        label = new Label(composite, SWT.WRAP);
        label.setText(Messages.EditorEntryDialog_3);
        
        fProgramPathTextField = new Text(composite, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.widthHint = 300;  // Stops size being set by length of text
        fProgramPathTextField.setLayoutData(gd);
        if(fProgramPath != null) {
            fProgramPathTextField.setText(fProgramPath);
        }
        
        fBrowseButton = new Button(composite, SWT.FLAT);
        fBrowseButton.setText(Messages.EditorEntryDialog_4);
        fBrowseButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleBrowse();
            }
        });

        return composite;
    }

    /**
     * Browse button was pressed
     */
    protected void handleBrowse() {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setText(Messages.EditorEntryDialog_5);
        
        // Restore last path (minus any command options)
        String s = fProgramPathTextField.getText();
        if(StringUtils.isSet(s)) {
            int index = s.lastIndexOf(" -"); //$NON-NLS-1$
            if(index != -1) {
                s = s.substring(0, index);
            }
            dialog.setFileName(s);
        }
        
        String str = dialog.open();
        if(str != null) {
            fProgramPathTextField.setText(str);
        }
    }
}
