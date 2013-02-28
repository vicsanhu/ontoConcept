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
package org.tencompetence.ldauthor.ui.wizards.ld;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * Open LD Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: OpenLDWizardPage.java,v 1.2 2009/07/02 12:51:00 phillipus Exp $
 */
public class OpenLDWizardPage extends WizardPage {

    public static final String PAGE_NAME = "OpenLDWizardPage"; //$NON-NLS-1$
    
    private Text fFileTextField;
    private Text fNameTextField;

    private Button fTicked;
    
    private Button fButtonBrowse;

    private Label fNameLabel;
    
    /**
	 * Constructor
	 */
	public OpenLDWizardPage() {
		super(PAGE_NAME);
		setTitle(Messages.OpenLDWizardPage_0);
		setDescription(Messages.OpenLDWizardPage_1);
        //setImageDescriptor(ImageFactory.getImageDescriptor("An Image"));
	}

    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        
        /*
         * Fields
         */
        Composite fieldContainer = new Composite(container, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        GridLayout layout = new GridLayout(3, false);
        layout.marginWidth = 0;
        fieldContainer.setLayout(layout);

	    Label label = new Label(fieldContainer, SWT.NULL);
	    label.setText(Messages.OpenLDWizardPage_2);

	    fFileTextField = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
	    fFileTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    fFileTextField.addModifyListener(new ModifyListener() {
	        public void modifyText(ModifyEvent e) {
	            validateFields();
	        }
	    });
	    fFileTextField.setEnabled(false);

	    fButtonBrowse = new Button(fieldContainer, SWT.PUSH);
	    fButtonBrowse.setText(Messages.OpenLDWizardPage_3);
	    fButtonBrowse.addSelectionListener(new SelectionAdapter() {
	        @Override
            public void widgetSelected(SelectionEvent e) {
	            handleBrowse();
	        }
	    });
	    
	    fTicked = new Button(fieldContainer, SWT.CHECK);
	    fTicked.setText(Messages.OpenLDWizardPage_4);
	    fTicked.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fNameLabel.setEnabled(fTicked.getSelection());
                fNameTextField.setEnabled(fTicked.getSelection());
            }
        });
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    gd.horizontalSpan = 3;
	    fTicked.setLayoutData(gd);
	    
	    fNameLabel = new Label(fieldContainer, SWT.NULL);
	    fNameLabel.setText(Messages.OpenLDWizardPage_5);
	    fNameLabel.setEnabled(false);

        fNameTextField = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
        fNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fNameTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        fNameTextField.setEnabled(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fNameTextField.setLayoutData(gd);

        setPageComplete(false);
        setControl(container);
	}

    private void handleBrowse() {
        FileDialog dialog = new FileDialog(getShell(), SWT.NULL);
        dialog.setText(Messages.OpenLDWizardPage_6);
        
        // If on Mac have to use this
        if(Platform.getOS().equals(Platform.OS_MACOSX)) {
            dialog.setFilterExtensions(new String[] { "*.xml", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else {
            dialog.setFilterExtensions(new String[] { "imsmanifest.xml", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        dialog.setFilterNames(new String[] { Messages.OpenLDWizardPage_7, Messages.OpenLDWizardPage_8 });
        
        String path = dialog.open();
        if(path != null) {
            fFileTextField.setText(path);
        }
    }
    
    private void validateFields() {
        String s = fFileTextField.getText();
        File file = new File(s);
        
        if("".equals(s.trim()) || !file.exists() || !file.getName().toLowerCase().equals("imsmanifest.xml")) { //$NON-NLS-1$ //$NON-NLS-2$
            updateStatus(Messages.OpenLDWizardPage_9);
            return;
        }
        
        updateStatus(null);
    }
    
    public boolean isOrganiserEntrySelected() {
        return fTicked.getSelection();
    }
    
    /**
     * @return The text in the Name field
     */
    public String getNameText() {
        return fNameTextField.getText();
    }

    /**
     * @return The file field
     */
    public File getFile() {
        return new File(fFileTextField.getText());
    }

    /**
     * Update the page status
     * @param message
     */
    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

}