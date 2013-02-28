/*
 * Copyright (c) 2008, Consortium Board TENCompetence
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

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.organisermodel.IOrganiserFolder;
import org.tencompetence.ldauthor.organisermodel.IOrganiserLD;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserIndex;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;
import org.tencompetence.ldauthor.templates.ILDTemplate;
import org.tencompetence.ldauthor.templates.impl.ld.ClonedLDTemplate;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.views.organiser.global.OrganiserTreeViewer;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Wizard Page to get file Location and Name and Copied LD if required
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewLDWizardPageLocation.java,v 1.4 2008/12/11 15:19:35 phillipus Exp $
 */
public class NewLDWizardPageLocation
extends WizardPage {
    
    public static final String PAGE_NAME = "NewLDWizardPageLocation"; //$NON-NLS-1$
    
    //private static String fLocation = System.getProperty("user.home"); //$NON-NLS-1$
    
    private Text fNameTextField;
    private Text fFolderTextField;
    private Button fButtonBrowse;
    
    private Composite fOrganiserComposite;
    private OrganiserTreeViewer fOrganiserTreeViewer;
    
    private boolean fClonedType;

    public NewLDWizardPageLocation() {
        super(PAGE_NAME);
        
        setTitle(Messages.NewLDWizardPageLocation_0);
        setDescription(Messages.NewLDWizardPageLocation_1);
        setImageDescriptor(ImageFactory.getEclipseImageDescriptor(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
    }

    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout(3, false);
        container.setLayout(layout);
        
        Label label = new Label(container, SWT.NULL);
        label.setText(Messages.NewLDWizardPageLocation_2);

        fNameTextField = new Text(container, SWT.BORDER | SWT.SINGLE);
        fNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fNameTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fNameTextField.setLayoutData(gd);
        
        label = new Label(container, SWT.NULL);
        label.setText(Messages.NewLDWizardPage_2);

        fFolderTextField = new Text(container, SWT.BORDER | SWT.SINGLE);
        fFolderTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        String lastFolder = LDAuthorPlugin.getDefault().getPreferenceStore().getString(ILDAuthorPreferenceConstants.PREFS_LAST_FOLDER);
        if(!StringUtils.isSet(lastFolder)) {
            lastFolder = LDAuthorPlugin.getDefault().getPreferenceStore().getString(ILDAuthorPreferenceConstants.PREFS_USER_DATA_FOLDER);
        }
        fFolderTextField.setText(lastFolder);
        
        fFolderTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });

        fButtonBrowse = new Button(container, SWT.PUSH);
        fButtonBrowse.setText(Messages.NewLDWizardPage_3);
        fButtonBrowse.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleBrowse();
            }
        });
        
        fOrganiserComposite = new Composite(container, SWT.NULL);
        fOrganiserComposite.setLayout(new GridLayout());
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        fOrganiserComposite.setLayoutData(gd);
        fOrganiserComposite.setVisible(false);
        
        label = new Label(fOrganiserComposite, SWT.NULL);
        label.setText(Messages.NewLDWizardPageLocation_3);
        
        fOrganiserTreeViewer = new OrganiserTreeViewer(fOrganiserComposite, SWT.BORDER);
        fOrganiserTreeViewer.setFilter(new Class[] { IOrganiserLD.class, IOrganiserFolder.class } );
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 3;
        fOrganiserTreeViewer.getControl().setLayoutData(gd);
        fOrganiserTreeViewer.setInput(OrganiserIndex.getInstance());
        
        fOrganiserTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                validateFields();
            }
        });

        setPageComplete(false);
        setControl(container);
    }
    
    public void setTemplate(ILDTemplate template) {
        if(fOrganiserComposite == null) {
            return; // Not created page yet
        }
        
        fClonedType = template instanceof ClonedLDTemplate;
        fOrganiserComposite.setVisible(fClonedType);
    }

    private void handleBrowse() {
        DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.NULL);
        dialog.setText(Messages.NewLDWizardPageLocation_8);
        dialog.setMessage(Messages.NewLDWizardPage_8);
        String lastFolder = LDAuthorPlugin.getDefault().getPreferenceStore().getString(ILDAuthorPreferenceConstants.PREFS_LAST_FOLDER);
        dialog.setFilterPath(lastFolder);

        String path = dialog.open();
        if(path != null) {
            fFolderTextField.setText(path);
            LDAuthorPlugin.getDefault().getPreferenceStore().setValue(ILDAuthorPreferenceConstants.PREFS_LAST_FOLDER, path);
        }
    }
    
    private void validateFields() {
        String name = getNameText();

        if(!StringUtils.isSetAfterTrim(name)) {
            updateStatus(Messages.NewLDWizardPageLocation_4);
            return;
        }
        
        String folder = fFolderTextField.getText();
        File file = new File(folder);
        
        if("".equals(folder.trim()) || !file.isDirectory()) { //$NON-NLS-1$
            updateStatus(Messages.NewLDWizardPage_9);
            return;
        }
        
        String fileName = FileUtils.getValidFileName(name);
        
        File fullFile = new File(file, fileName);
        if(fullFile.exists() && !fullFile.isDirectory()) {
            updateStatus(Messages.NewLDWizardPageLocation_5);
            return;
        }
        
        if(fClonedType) {
            if(getCopiedFolder() == null) {
                updateStatus(Messages.NewLDWizardPageLocation_6);
                return;
            }
        }
        
        // OK
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
    
    @Override
    public boolean canFlipToNextPage() {
        return super.canFlipToNextPage() && !fClonedType;
    }

    /**
     * @return The text in the Name field
     */
    public String getNameText() {
        return fNameTextField.getText();
    }

    /**
     * @return The Folder for the LD
     */
    public File getFolder() {
        File folder = new File(fFolderTextField.getText());
        String fileName = FileUtils.getValidFileName(getNameText());
        return new File(folder, fileName);
    }

    /**
     * @return The Copied Folder (if selected)
     */
    public File getCopiedFolder() {
        Object o = ((IStructuredSelection)fOrganiserTreeViewer.getSelection()).getFirstElement();
        if(o instanceof IOrganiserLD) {
            return ((IOrganiserLD)o).getFile().getParentFile();
        }
        
        return null;
    }

}
