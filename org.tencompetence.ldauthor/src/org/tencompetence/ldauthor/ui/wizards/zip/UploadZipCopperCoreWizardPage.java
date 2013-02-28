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
package org.tencompetence.ldauthor.ui.wizards.zip;

import java.io.File;

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
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Upload Zip file Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: UploadZipCopperCoreWizardPage.java,v 1.1 2008/10/16 18:17:27 phillipus Exp $
 */
public class UploadZipCopperCoreWizardPage extends WizardPage {

    public static final String PAGE_NAME = "UploadZipWizardPage"; //$NON-NLS-1$
    
    private Text fFileTextField;
    
    private Button fButtonBrowse;
    
    private String fFilePath;
    
    /**
	 * Constructor
	 */
	public UploadZipCopperCoreWizardPage() {
		super(PAGE_NAME);
		setTitle(Messages.UploadZipWizardPage_0);
		setDescription(Messages.UploadZipWizardPage_1);
        //setImageDescriptor(ImageFactory.getImageDescriptor("An Image"));
	}

    public void createControl(Composite parent) {
	    Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        
        Composite fieldContainer = new Composite(container, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        GridLayout layout = new GridLayout(3, false);
        layout.marginWidth = 0;
        fieldContainer.setLayout(layout);
	    
	    Label label = new Label(fieldContainer, SWT.NULL);
	    label.setText(Messages.UploadZipWizardPage_2);

	    fFileTextField = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
	    fFileTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    fFileTextField.addModifyListener(new ModifyListener() {
	        public void modifyText(ModifyEvent e) {
	            validateFields();
	        }
	    });

	    fButtonBrowse = new Button(fieldContainer, SWT.PUSH);
	    fButtonBrowse.setText(Messages.UploadZipWizardPage_3);
	    fButtonBrowse.addSelectionListener(new SelectionAdapter() {
	        @Override
            public void widgetSelected(SelectionEvent e) {
	            handleBrowse();
	        }
	    });
	    
	    if(fFilePath != null) {
	        fFileTextField.setText(fFilePath);
	    }
	    
	    validateFields();
        setControl(container);
	}

    private void handleBrowse() {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setText(Messages.UploadZipWizardPage_5);
        dialog.setFilterExtensions(new String[] { "*.zip", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
        dialog.setFilterNames(new String[] { Messages.UploadZipWizardPage_6, Messages.UploadZipWizardPage_7 });
        String path = dialog.open();
        if(path != null) {
            if(!path.toLowerCase().endsWith(".zip")) { //$NON-NLS-1$
                path += ".zip"; //$NON-NLS-1$
            }
            fFileTextField.setText(path);
        }
    }
    
    private void validateFields() {
        String path = fFileTextField.getText();
        
        if(!StringUtils.isSetAfterTrim(path)) {
            updateStatus(Messages.UploadZipWizardPage_8);
            return;
        }
        
        File file = new File(path);
        if(!file.exists()) {
            updateStatus(Messages.UploadZipWizardPage_9);
            return;
        }
        
        updateStatus(null);
    }
    
    /**
     * Update the page status
     * @param message
     */
    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }
    
    /**
     * @return The File path for the Zip
     */
    public String getFilePath() {
        return fFileTextField.getText();
    }
    
    public void setFilePath(String path) {
        fFilePath = path;
    }
}