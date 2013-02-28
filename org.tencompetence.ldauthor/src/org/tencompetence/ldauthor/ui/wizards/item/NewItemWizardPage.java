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
package org.tencompetence.ldauthor.ui.wizards.item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * New Item Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewItemWizardPage.java,v 1.12 2009/06/30 09:29:27 phillipus Exp $
 */
public class NewItemWizardPage extends WizardPage {

    public static final String PAGE_NAME = "NewItemWizardPage"; //$NON-NLS-1$
    
    private ILDModel fLDModel;
    
    private Text fTitleText;
    private Text fDescriptionText;
    private Text fURLText;
    private Text fFileText;
    
    /*
     * File type buttons
     */
    private Button fButtonHTML, fButtonXHTML;


    /*
     * Choice buttons
     */ 
    private Button fButtonNewText, fButtonNewURL, fButtonBlank, fButtonExisting, fButtonFile, fButtonFileChooser;

    private Combo fCombo;
    private boolean fComboPopulated;

    /**
	 * Constructor
     * @param model 
	 */
    public NewItemWizardPage(ILDModel ldModel) {
		super(PAGE_NAME);
		setTitle(Messages.NewItemWizardPage_0);
		setDescription(Messages.NewItemWizardPage_1);
        setImageDescriptor(ImageFactory.getEclipseImageDescriptor(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
        
        fLDModel = ldModel;
	}

    public void createControl(Composite parent) {
	    Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        
        Composite fieldContainer = new Composite(container, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        fieldContainer.setLayout(layout);
	    
	    Label label = new Label(fieldContainer, SWT.NULL);
	    label.setText(Messages.NewItemWizardPage_2);

	    fTitleText = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
	    fTitleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    fTitleText.addModifyListener(new ModifyListener() {
	        public void modifyText(ModifyEvent e) {
	            validateFields();
	        }
	    });
	    
        // HTML or XHTML
        Group group1 = new Group(fieldContainer, SWT.NULL);
        group1.setLayout(new GridLayout(2, false));
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        group1.setLayoutData(gd);
        group1.setText(Messages.NewItemWizardPage_11);
        
        fButtonHTML = new Button(group1, SWT.RADIO);
        fButtonHTML.setText("HTML"); //$NON-NLS-1$
        fButtonHTML.setSelection(true);
        
        fButtonXHTML = new Button(group1, SWT.RADIO);
        fButtonXHTML.setText("XHTML"); //$NON-NLS-1$
        
        // File or URL
	    Group group2 = new Group(container, SWT.NULL);
	    group2.setLayout(new GridLayout(3, false));
	    group2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    group2.setText(Messages.NewItemWizardPage_6);
	    
	    // New Resource Text
	    fButtonNewText = new Button(group2, SWT.RADIO);
	    //gd = new GridData(SWT.FILL, SWT.TOP, false, true);
	    //fButtonNewText.setLayoutData(gd);
	    fButtonNewText.setText(Messages.NewItemWizardPage_3);
	    fButtonNewText.setSelection(true);
	    fButtonNewText.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fDescriptionText.setEnabled(fButtonNewText.getSelection());
                fButtonHTML.setEnabled(fButtonNewText.getSelection());
                fButtonXHTML.setEnabled(fButtonNewText.getSelection());
                validateFields();
            }
        });
        
        fDescriptionText = new Text(group2, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 150;
        gd.horizontalSpan = 2;
        fDescriptionText.setLayoutData(gd);
        
        // New Resource URL
        fButtonNewURL = new Button(group2, SWT.RADIO);
        fButtonNewURL.setText(Messages.NewItemWizardPage_8);
        fButtonNewURL.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fURLText.setEnabled(fButtonNewURL.getSelection());
            }
        });
        
        fURLText = new Text(group2, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fURLText.setLayoutData(gd);
        fURLText.setText("http://"); //$NON-NLS-1$
        fURLText.setEnabled(false);
        
        // Reference a File (and create a new Resource)
        fButtonFile = new Button(group2, SWT.RADIO);
        fButtonFile.setText(Messages.NewItemWizardPage_9);
        fButtonFile.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fButtonFileChooser.setEnabled(fButtonFile.getSelection());
                validateFields();
            }
        });
        
        fFileText = new Text(group2, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fFileText.setLayoutData(gd);
        fFileText.setEnabled(false);
        
        fButtonFileChooser = new Button(group2, SWT.PUSH);
        fButtonFileChooser.setText(Messages.NewItemWizardPage_10);
        fButtonFileChooser.setEnabled(false);
        fButtonFileChooser.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                chooseFile();
                validateFields();
            }
        });

        // Existing Resource
        fButtonExisting = new Button(group2, SWT.RADIO);
        fButtonExisting.setText(Messages.NewItemWizardPage_4);
        fButtonExisting.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fCombo.setEnabled(fButtonExisting.getSelection());
                if(!fComboPopulated) {
                    populateResourceCombo();
                }
            }
        });
        
        fCombo = new Combo(group2, SWT.READ_ONLY);
        fCombo.setVisibleItemCount(12);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fCombo.setLayoutData(gd);
        fCombo.setEnabled(false);
        
        // No Resource ref
        fButtonBlank = new Button(group2, SWT.RADIO);
        fButtonBlank.setText(Messages.NewItemWizardPage_7);

	    setPageComplete(false);
        setControl(container);
	}

    /**
     * Populate the Resource combo with Resource identifiers
     */
    private void populateResourceCombo() {
        List<String> list = new ArrayList<String>();
        
        for(IResourceModel resource : fLDModel.getResourcesModel().getResources()) {
            if(StringUtils.isSet(resource.getIdentifier())) {
                list.add(resource.getIdentifier());
            }
        }
        
        if(list.isEmpty()) {
            return;
        }
        
        String[] s = new String[list.size()];
        list.toArray(s);
        
        fCombo.setItems(s);
        
        fCombo.setText(s[0]);
        
        fComboPopulated = true;
    }

    public IItemType getItem() {
        IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, fLDModel);
        
        itemType.setTitle(fTitleText.getText());
        
        // New Resource Text File
        if(fButtonNewText.getSelection()) {
            int type = fButtonHTML.getSelection() ? LDModelUtils.HTML_FILE : LDModelUtils.XHTML_FILE;
            LDModelUtils.setNewObjectDefaults(itemType, fDescriptionText.getText(), type);
        }
        // New Resource URL
        else if(fButtonNewURL.getSelection()) {
            LDModelUtils.createNewResourceWithHref(itemType, fURLText.getText(), fTitleText.getText());
        }
        // Use Existing Resource
        else if(fButtonExisting.getSelection()) {
            itemType.setIdentifierRef(fCombo.getText());
        }
        // Existing File
        else if(fButtonFile.getSelection()) {
            // Create Resource or re-use one
            String href = fFileText.getText();
            IResourceModel resource = fLDModel.getResourcesModel().getResourceByHref(href);
            // New one needed
            if(resource == null) {
                resource = LDModelUtils.createNewResourceWithHref(itemType, href, href);
            }
            // Re-use one
            else {
                itemType.setIdentifierRef(resource.getIdentifier());
            }
        }
        
        return itemType;
    }
    
    private void chooseFile() {
        File rootFolder = fLDModel.getRootFolder();
        
        File file = FileUtils.selectFileFromRootFolder(rootFolder, getShell());
        if(file != null) {
            fFileText.setText(FileUtils.getRelativePath(file, rootFolder));
        }
    }
    
    private void validateFields() {
        String title = fTitleText.getText();
        
        if(!StringUtils.isSetAfterTrim(title)) {
            updateStatus(Messages.NewItemWizardPage_5);
            return;
        }
        
        if(fButtonFile.getSelection() && !StringUtils.isSet(fFileText.getText())) {
            updateStatus(Messages.NewItemWizardPage_13);
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