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
package org.tencompetence.ldauthor.ui.views.ldproperties;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.IIdentifier;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.properties.IPropertyGroupModel;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * "New File" Dialog
 * 
 * @author Phillip Beauvoir
 * @version $Id: CopyPropertyToClipboardDialog.java,v 1.5 2009/06/22 13:48:29 phillipus Exp $
 */
public class CopyPropertyToClipboardDialog
extends TitleAreaDialog {
	
    /*
     * Text Controls
     */
    private Text fText;

    private Clipboard fClipboard = new Clipboard(null);
    
    private ILDModelObject fProperty;
    
    private Button fViewButton, fSetButton;
    
    private Button fPropertyOfSelfButton, fPropertyOfSupportedPersonButton;
    
    private Button fViewValueButton, fViewTitleValueButton;
    
    private SelectionAdapter fSelectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            updateTextBox();
        }
    };
    
    /**
	 * Constructor
     * @param parentShell
	 */
	public CopyPropertyToClipboardDialog(Shell parentShell, ILDModelObject property) {
        super(parentShell);
        fProperty = property;
	}
	
    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.CopyPropertyToClipboardDialog_0);
        shell.setImage(ImageFactory.getImage(ImageFactory.ICON_PROPERTY));
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(Messages.CopyPropertyToClipboardDialog_1);
        setMessage(Messages.CopyPropertyToClipboardDialog_2);
        
        Composite composite = new Composite((Composite)super.createDialogArea(parent), SWT.NULL);

        GridLayout layout = new GridLayout(2, true);
        layout.marginWidth = 10;
        layout.marginTop = 5;
        composite.setLayout(layout);
        
        GridData gd = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gd);
        
        Group group1 = new Group(composite, SWT.NULL);
        group1.setLayout(new GridLayout());
        group1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        group1.setText(Messages.CopyPropertyToClipboardDialog_3);
        
        fViewButton = new Button(group1, SWT.RADIO);
        fViewButton.setSelection(true);
        fViewButton.addSelectionListener(fSelectionListener);

        fSetButton = new Button(group1, SWT.RADIO);
        fSetButton.addSelectionListener(fSelectionListener);
        
        Object selectedProperty = getProperty();
        
        if(selectedProperty instanceof IPropertyGroupModel) {
            fViewButton.setText(Messages.CopyPropertyToClipboardDialog_4);
            fSetButton.setText(Messages.CopyPropertyToClipboardDialog_5);
        }
        else {
            fViewButton.setText(Messages.CopyPropertyToClipboardDialog_6);
            fSetButton.setText(Messages.CopyPropertyToClipboardDialog_7);
        }
        
        Group group2 = new Group(composite, SWT.NULL);
        group2.setLayout(new GridLayout());
        group2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        group2.setText(Messages.CopyPropertyToClipboardDialog_8);
        
        fPropertyOfSelfButton = new Button(group2, SWT.RADIO);
        fPropertyOfSelfButton.setSelection(true);
        fPropertyOfSelfButton.addSelectionListener(fSelectionListener);
        fPropertyOfSelfButton.setText(Messages.CopyPropertyToClipboardDialog_9);
        
        fPropertyOfSupportedPersonButton = new Button(group2, SWT.RADIO);
        fPropertyOfSupportedPersonButton.addSelectionListener(fSelectionListener);
        fPropertyOfSupportedPersonButton.setText(Messages.CopyPropertyToClipboardDialog_10);
        
        Group group3 = new Group(composite, SWT.NULL);
        group3.setLayout(new GridLayout());
        group3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        group3.setText(Messages.CopyPropertyToClipboardDialog_11);
        
        fViewValueButton = new Button(group3, SWT.RADIO);
        fViewValueButton.setSelection(true);
        fViewValueButton.addSelectionListener(fSelectionListener);
        fViewValueButton.setText(Messages.CopyPropertyToClipboardDialog_12);
        
        fViewTitleValueButton = new Button(group3, SWT.RADIO);
        fViewTitleValueButton.addSelectionListener(fSelectionListener);
        fViewTitleValueButton.setText(Messages.CopyPropertyToClipboardDialog_13);
        
        fText = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        fText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 80;
        gd.widthHint = 120;
        gd.horizontalSpan = 2;
        fText.setLayoutData(gd);
        
        updateTextBox();
        
        return composite;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, Messages.CopyPropertyToClipboardDialog_14, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        // Copy to clipboard
        fClipboard.setContents(new String[] { fText.getText() },
                new Transfer[] { TextTransfer.getInstance() } );
        
        fClipboard.dispose();

        super.okPressed();
    }

    @Override
    protected void cancelPressed() {
        super.cancelPressed();
        fClipboard.dispose();
    }
    
    /**
     * @return The actual Property or Property Group we are dealing with
     */
    private Object getProperty() {
        if(fProperty instanceof ILDModelObjectReference) {
            return ((ILDModelObjectReference)fProperty).getLDModelObject();
        }
        return fProperty;
    }
    
    /**
     * Update the text box
     */
    private void updateTextBox() {
        String txt;
        
        Object selectedProperty = getProperty();
        
        if(fViewButton.getSelection()) {
            txt = "<view-property"; //$NON-NLS-1$
        }
        else {
            txt = "<set-property"; //$NON-NLS-1$
        }
        
        if(selectedProperty instanceof IPropertyGroupModel) {
            txt += "-group"; //$NON-NLS-1$
        }
        
        txt += " xmlns=\"http://www.imsglobal.org/xsd/imsld_v1p0\" ref=\""; //$NON-NLS-1$
        
        String id = ""; //$NON-NLS-1$
        if(selectedProperty instanceof IIdentifier) {
            id = ((IIdentifier)selectedProperty).getIdentifier();
        }
        
        txt += id + "\""; //$NON-NLS-1$
        
        if(fPropertyOfSelfButton.getSelection()) {
            txt += " property-of=\"self\""; //$NON-NLS-1$
        }
        else {
            txt += " property-of=\"supported-person\""; //$NON-NLS-1$
        }
        
        if(fViewValueButton.getSelection()) {
            txt += " view=\"value\""; //$NON-NLS-1$
        }
        else {
            txt += " view=\"title-value\""; //$NON-NLS-1$
        }
        
        txt += "/>"; //$NON-NLS-1$
        
        fText.setText(txt);
    }
}
