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
package org.tencompetence.ldauthor.ui.wizards.resource;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.ldauthor.ExtensionContentHandler;
import org.tencompetence.ldauthor.ILDContentHandler;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * New Resource Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewResourceWizardPage.java,v 1.12 2009/06/30 09:29:27 phillipus Exp $
 */
public class NewResourceWizardPage extends WizardPage {

    public static final String PAGE_NAME = "NewResourceWizardPage"; //$NON-NLS-1$
    
    private Text fIdentifierText;
    private Text fFileURLText;
    private Text fDescriptionText;

    /*
     * Choice buttons
     */ 
    private Button fButtonURLFile, fButtonNew;
    
    /*
     * File type buttons
     */
    private Button fButtonHTML, fButtonXHTML;
    
    private Button fButtonBrowse;

    private ILDModel fLDModel;
    
    /**
	 * Constructor
     * @param model 
	 */
    public NewResourceWizardPage(ILDModel ldModel) {
		super(PAGE_NAME);
		setTitle(Messages.NewResourceWizardPage_0);
		setDescription(Messages.NewResourceWizardPage_1);
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
	    
	    Label label = new Label(fieldContainer, SWT.NULL);
	    label.setText(Messages.NewResourceWizardPage_2);

	    fIdentifierText = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
	    fIdentifierText.setText(LDModelUtils.getNextResourceIdentifier(fLDModel, "Resource")); //$NON-NLS-1$
	    fIdentifierText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    fIdentifierText.addModifyListener(new ModifyListener() {
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
	    group1.setText(Messages.NewResourceWizardPage_12);
	    
	    fButtonHTML = new Button(group1, SWT.RADIO);
	    fButtonHTML.setText("HTML"); //$NON-NLS-1$
	    fButtonHTML.setSelection(true);
	    
	    fButtonXHTML = new Button(group1, SWT.RADIO);
	    fButtonXHTML.setText("XHTML"); //$NON-NLS-1$
	    
	    // File or URL
	    Group group2 = new Group(container, SWT.NULL);
	    group2.setLayout(new GridLayout(3, false));
	    group2.setLayoutData(new GridData(GridData.FILL_BOTH));
	    group2.setText(Messages.NewResourceWizardPage_3);
	    
	    fButtonNew = new Button(group2, SWT.RADIO);
	    fButtonNew.setText(Messages.NewResourceWizardPage_6);
	    //gd = new GridData(SWT.FILL, SWT.TOP, false, true);
	    //fButtonNew.setLayoutData(gd);
	    fButtonNew.setSelection(true);
	    
        fDescriptionText = new Text(group2, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 150;
        gd.horizontalSpan = 2;
        fDescriptionText.setLayoutData(gd);
        
        fButtonURLFile = new Button(group2, SWT.RADIO);
        fButtonURLFile.setText(Messages.NewResourceWizardPage_4);
        fButtonURLFile.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fFileURLText.setEnabled(fButtonURLFile.getSelection());
                fButtonBrowse.setEnabled(fButtonURLFile.getSelection());
                fDescriptionText.setEnabled(!fButtonURLFile.getSelection());
                fButtonHTML.setEnabled(!fButtonURLFile.getSelection());
                fButtonXHTML.setEnabled(!fButtonURLFile.getSelection());
            }
        });
        
        fFileURLText = new Text(group2, SWT.BORDER);
        fFileURLText.setEnabled(false);
        fFileURLText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fButtonBrowse = new Button(group2, SWT.PUSH);
        fButtonBrowse.setText(Messages.NewResourceWizardPage_5);
        fButtonBrowse.setEnabled(false);
        fButtonBrowse.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleBrowse();
            }
        });
        
        setControl(container);
	}

    private void handleBrowse() {
        File rootFolder = fLDModel.getRootFolder();
        
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setText(Messages.NewResourceWizardPage_7);
        dialog.setFilterPath(rootFolder.getPath());  // force to rootFolder (doesn't work on Mac)
        
        String href = dialog.open();
        if(href == null) {
            return;
        }

        File file = new File(href);

        // If file is not in the root folder warn
        if(!checkFileInRootFolder(rootFolder, file)) {
            MessageDialog.openError(getShell(),
                    Messages.NewResourceWizardPage_8,
                    Messages.NewResourceWizardPage_9 +
                    rootFolder.getPath());
            return;
        }

        fFileURLText.setText(FileUtils.getRelativePath(file, rootFolder));
    }
    
    /**
     * Return true if file is in root folder
     * @param rootFolder
     * @param file
     * @return
     */
    private boolean checkFileInRootFolder(File rootFolder, File file) {
        while((file = file.getParentFile()) != null) {
            if(file.equals(rootFolder)) {
                return true;
            }
        }
        return false;
    }

    
    public IResourceModel getResource() {
        IResourceModel resource = (IResourceModel)LDModelFactory.createModelObject(LDModelFactory.RESOURCE, fLDModel);
        String id = StringUtils.getValidID(fIdentifierText.getText());
        resource.setIdentifier(id);
        
        // File / URL
        if(fButtonURLFile.getSelection()) {
            String href = fFileURLText.getText();
            resource.setHrefAndResourceFile(href);

            // If this is a content type added by an extension, set its type
            File rootFolder = fLDModel.getRootFolder();
            File file = new File(rootFolder, href);
            ILDContentHandler handler = ExtensionContentHandler.getInstance().getHandler(file);
            if(handler != null) {
                resource.setType(handler.getType());
            }
            // Else, try and set the type
            else {
                if(href.toLowerCase().endsWith(".xhtml") || href.toLowerCase().endsWith(".xml")) { //$NON-NLS-1$ //$NON-NLS-2$
                    resource.setType(IResourceModel.IMSLDCONTENT_TYPE);
                }
            }
        }
        // New HTML/XHTML File
        else {
            int type = fButtonHTML.getSelection() ? LDModelUtils.HTML_FILE : LDModelUtils.XHTML_FILE;
            LDModelUtils.setNewObjectDefaults(resource, fDescriptionText.getText(), type);
        }
        
        return resource;
    }
    
    private void validateFields() {
        String id = fIdentifierText.getText();
        
        if(!StringUtils.isSetAfterTrim(id)) {
            updateStatus(Messages.NewResourceWizardPage_10);
            return;
        }
        
        id = StringUtils.getValidID(id);
        
        if(!fLDModel.getResourcesModel().isValidResourceID(id)) {
            updateStatus(Messages.NewResourceWizardPage_11);
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