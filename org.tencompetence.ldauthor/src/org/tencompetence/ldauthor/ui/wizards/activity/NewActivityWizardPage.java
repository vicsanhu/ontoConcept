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
package org.tencompetence.ldauthor.ui.wizards.activity;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityType;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * New Activity Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewActivityWizardPage.java,v 1.14 2009/06/30 17:27:04 phillipus Exp $
 */
public class NewActivityWizardPage extends WizardPage {

    public static final String PAGE_NAME = "NewActivityWizardPage"; //$NON-NLS-1$
    
    private Text fNameText;
    private Text fDescriptionText;
    private Text fURLText;
    private Text fFileText;

    private ILDModel fLDModel;
    private int fType;
    
    /*
     * File type buttons
     */
    private Button fButtonHTML, fButtonXHTML;

    
    /*
     * Choice buttons
     */ 
    private Button fButtonNewText, fButtonNewURL, fButtonFile, fButtonFileChooser;


    /**
	 * Constructor
	 */
    public NewActivityWizardPage(ILDModel ldModel, int type) {
		super(PAGE_NAME);
		
		fType = type;
		
		switch(type) {
		    case NewActivityWizard.LEARNING_ACTIVITY:
		        setTitle(Messages.NewActivityWizardPage_4);
		        break;
		        
		    case NewActivityWizard.SUPPORT_ACTIVITY:
                setTitle(Messages.NewActivityWizardPage_5);
                break;
                
		    case NewActivityWizard.ACTIVITY_STRUCTURE:
                setTitle(Messages.NewActivityWizardPage_6);
                break;
		}
		
		setDescription(Messages.NewActivityWizardPage_1);
        setImageDescriptor(ImageFactory.getEclipseImageDescriptor(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
        
        fLDModel = ldModel;
	}

    public void createControl(Composite parent) {
	    Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        
        Composite fieldContainer = new Composite(container, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        fieldContainer.setLayout(layout);
	    
        // Name
        
	    Label label = new Label(fieldContainer, SWT.NULL);
	    label.setText(Messages.NewActivityWizardPage_2);

	    fNameText = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
	    fNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    fNameText.addModifyListener(new ModifyListener() {
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
        group1.setText(Messages.NewActivityWizardPage_14);
        
        fButtonHTML = new Button(group1, SWT.RADIO);
        fButtonHTML.setText("HTML"); //$NON-NLS-1$
        fButtonHTML.setSelection(true);
        
        fButtonXHTML = new Button(group1, SWT.RADIO);
        fButtonXHTML.setText("XHTML"); //$NON-NLS-1$

	    
        // Description or URL
        Group group2 = new Group(fieldContainer, SWT.NULL);
        group2.setLayout(new GridLayout(3, false));
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 2;
        group2.setLayoutData(gd);
        group2.setText(Messages.NewActivityWizardPage_8);
        
        // New Resource Text
        fButtonNewText = new Button(group2, SWT.RADIO);
        fButtonNewText.setText(Messages.NewActivityWizardPage_9);
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
	    gd.heightHint = 100;
	    gd.horizontalSpan = 2;
	    fDescriptionText.setLayoutData(gd);

        // New Resource URL
        fButtonNewURL = new Button(group2, SWT.RADIO);
        fButtonNewURL.setText(Messages.NewActivityWizardPage_10);
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
        fButtonFile.setText(Messages.NewActivityWizardPage_12);
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
        fButtonFileChooser.setText(Messages.NewActivityWizardPage_13);
        fButtonFileChooser.setEnabled(false);
        fButtonFileChooser.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                chooseFile();
                validateFields();
            }
        });

	    setPageComplete(false);
        setControl(container);
	}

    /**
     * @return The Activity
     */
    public IActivityType getActivity() {
        IActivityType activity = null;
        
        switch(fType) {
            case NewActivityWizard.LEARNING_ACTIVITY:
                activity = (IActivityType)LDModelFactory.createModelObject(LDModelFactory.LEARNING_ACTIVITY, fLDModel);
                break;
                
            case NewActivityWizard.SUPPORT_ACTIVITY:
                activity = (IActivityType)LDModelFactory.createModelObject(LDModelFactory.SUPPORT_ACTIVITY, fLDModel);
                break;
                
            case NewActivityWizard.ACTIVITY_STRUCTURE:
                activity = (IActivityType)LDModelFactory.createModelObject(LDModelFactory.ACTIVITY_STRUCTURE, fLDModel);
                break;
        }
        
        activity.setTitle(fNameText.getText());
        
        // New Resource text
        if(fButtonNewText.getSelection()) {
            int type = fButtonHTML.getSelection() ? LDModelUtils.HTML_FILE : LDModelUtils.XHTML_FILE;
 
            if(activity instanceof IActivityModel) {
                LDModelUtils.setNewObjectDefaults((IActivityModel)activity, fDescriptionText.getText(), type);
            }
            else if(activity instanceof IActivityStructureModel) {
                LDModelUtils.setNewObjectDefaults((IActivityStructureModel)activity, fDescriptionText.getText(), type);
            }
        }
        
        // Resource URL
        else if(fButtonNewURL.getSelection()) {
            IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, fLDModel);
            itemType.setTitle(Messages.NewActivityWizardPage_11);
            
            if(activity instanceof IActivityModel) {
                ((IActivityModel)activity).getDescriptionModel().addChildItem(itemType);
            }
            else {
                ((IActivityStructureModel)activity).getInformationModel().addChildItem(itemType);
            }
            
            LDModelUtils.createNewResourceWithHref(itemType, fURLText.getText(), fNameText.getText());
        }
        
        // File
        else if(fButtonFile.getSelection()) {
            IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, fLDModel);
            itemType.setTitle(Messages.NewRolePartWizardPage_13);
            
            // Create Resource or re-use one
            String href = fFileText.getText();
            IResourceModel resource = fLDModel.getResourcesModel().getResourceByHref(href);
            if(resource == null) {
                resource = LDModelUtils.createNewResourceWithHref(itemType, href, href);
            }
            else {
                itemType.setIdentifierRef(resource.getIdentifier());
            }
            
            // Description
            if(activity instanceof IActivityModel) {
                ((IActivityModel)activity).getDescriptionModel().addChildItem(itemType);
            }
            else {
                ((IActivityStructureModel)activity).getInformationModel().addChildItem(itemType);
            }
        }
        
        return activity;
    }
    
    private void chooseFile() {
        File rootFolder = fLDModel.getRootFolder();
        
        File file = FileUtils.selectFileFromRootFolder(rootFolder, getShell());
        if(file != null) {
            fFileText.setText(FileUtils.getRelativePath(file, rootFolder));
        }
    }
    
    private void validateFields() {
        String title = fNameText.getText();
        
        if(!StringUtils.isSetAfterTrim(title)) {
            updateStatus(Messages.NewActivityWizardPage_7);
            return;
        }
        
        if(fButtonFile.getSelection() && !StringUtils.isSet(fFileText.getText())) {
            updateStatus(Messages.NewActivityWizardPage_16);
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