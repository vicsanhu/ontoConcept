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
package org.tencompetence.ldauthor.opendock.ui;

import java.io.File;
import java.util.Locale;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Upload Zip file Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: UploadZipWizardPage.java,v 1.3 2008/04/25 11:46:13 phillipus Exp $
 */
public class UploadZipWizardPage extends WizardPage {

    public static final String PAGE_NAME = "UploadZipWizardPage"; //$NON-NLS-1$
    
    Text fFileText;
    Text fTitleText;
    Text fShortDescText;
    Text fLongDescText;
    Combo fLanguageCombo;
    Text fCopyrightHolderText;
    Text fCopyrightYearText;

    private Button fButtonBrowse;
    
    /**
	 * Constructor
     * @param uploadInfo 
	 */
	public UploadZipWizardPage() {
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

	    fFileText = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
	    fFileText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    fFileText.addModifyListener(new ModifyListener() {
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
	    
	    Group group = new Group(container, SWT.NULL);
	    group.setText(Messages.UploadZipWizardPage_4);
	    group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    group.setLayout(new GridLayout(2, false));
	    
	    // Title
	    label = new Label(group, SWT.NULL);
        label.setText(Messages.UploadZipWizardPage_5);
        fTitleText = new Text(group, SWT.BORDER | SWT.SINGLE);
        fTitleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Short Desc
        label = new Label(group, SWT.NULL);
        label.setText(Messages.UploadZipWizardPage_6);
        fShortDescText = new Text(group, SWT.BORDER | SWT.SINGLE);
        fShortDescText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Long Desc
        label = new Label(group, SWT.NULL);
        label.setText(Messages.UploadZipWizardPage_7);
        fLongDescText = new Text(group, SWT.BORDER | SWT.MULTI);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 100;
        fLongDescText.setLayoutData(gd);
        
        // Language
        label = new Label(group, SWT.NULL);
        label.setText(Messages.UploadZipWizardPage_8);
        fLanguageCombo = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
        Locale[] locales = Locale.getAvailableLocales();
        String[] items = new String[locales.length];
        for(int i = 0; i < locales.length; i++) {
            items[i] = locales[i].toString();
        }
        fLanguageCombo.setItems(items);
        fLanguageCombo.setText(Locale.getDefault().toString());
        fLanguageCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Copyright holder
        label = new Label(group, SWT.NULL);
        label.setText(Messages.UploadZipWizardPage_9);
        fCopyrightHolderText = new Text(group, SWT.BORDER | SWT.SINGLE);
        fCopyrightHolderText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Copyright year
        label = new Label(group, SWT.NULL);
        label.setText(Messages.UploadZipWizardPage_10);
        fCopyrightYearText = new Text(group, SWT.BORDER | SWT.SINGLE);
        fCopyrightYearText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    
	    validateFields();
        setControl(container);
	}

    private void handleBrowse() {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setText(Messages.UploadZipWizardPage_11);
        dialog.setFilterExtensions(new String[] { "*.zip", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
        dialog.setFilterNames(new String[] { Messages.UploadZipWizardPage_12, Messages.UploadZipWizardPage_13 });
        String path = dialog.open();
        if(path != null) {
            if(!path.toLowerCase().endsWith(".zip")) { //$NON-NLS-1$
                path += ".zip"; //$NON-NLS-1$
            }
            fFileText.setText(path);
        }
    }
    
    private void validateFields() {
        String path = fFileText.getText();
        
        if(!StringUtils.isSetAfterTrim(path)) {
            updateStatus(Messages.UploadZipWizardPage_14);
            return;
        }
        
        File file = new File(path);
        if(!file.exists()) {
            updateStatus(Messages.UploadZipWizardPage_15);
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
}