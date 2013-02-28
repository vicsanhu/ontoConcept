/*
 * Copyright (c) 2009, Consortium Board TENCompetence
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
package org.tencompetence.qtieditor.ui;

import java.io.File;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.organisermodel.IOrganiserFolder;
import org.tencompetence.ldauthor.organisermodel.IOrganiserLD;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserIndex;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.views.organiser.global.OrganiserTreeViewer;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * New QTI Test Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewQTITestWizardPage.java,v 1.4 2009/05/19 18:21:33 phillipus Exp $
 */
public class NewQTITestWizardPage extends WizardPage {

    public static final String PAGE_NAME = "NewQTITestWizardPage"; //$NON-NLS-1$
    
    /*
     * Text Controls
     */
    private Text fText;

    private ILDMultiPageEditor fCurrentLDEditor;
    
    private OrganiserTreeViewer fOrganiserTreeViewer;
    
    private File fFile;
    
    /**
	 * Constructor
     * @param currentLDEditor 
	 */
	public NewQTITestWizardPage(ILDMultiPageEditor currentLDEditor) {
		super(PAGE_NAME);
		setTitle("New QTI Test");
		setDescription("Create a new test. Please do not use any white-space in the name like 'A test'.");
        setImageDescriptor(ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_TEST_FOLDER));
        
        fCurrentLDEditor = currentLDEditor;
	}
	
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);
        
        GridData gd;
        
        // Name
        Label label = new Label(container, SWT.NULL);
        label.setText("Name:");
        
        fText = new Text(container, SWT.BORDER);
        fText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });

        // No LD Editor in Focus soo show the Organiser
        if(fCurrentLDEditor == null) {
            label = new Label(container, SWT.NULL);
            label.setText("Parent LD:");
            fOrganiserTreeViewer = new OrganiserTreeViewer(container, SWT.BORDER);
            fOrganiserTreeViewer.setFilter(new Class[] { IOrganiserLD.class, IOrganiserFolder.class } );
            gd = new GridData(SWT.FILL, SWT.FILL, true, true);
            gd.horizontalSpan = 2;
            fOrganiserTreeViewer.getControl().setLayoutData(gd);
            fOrganiserTreeViewer.setInput(OrganiserIndex.getInstance());

            fOrganiserTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
                public void selectionChanged(SelectionChangedEvent event) {
                    validateFields();
                }
            });
        }
        
	    setPageComplete(false);
        setControl(container);
	}
    
    private void validateFields() {
        fFile = null;
        
        String name = fText.getText();
        if(!StringUtils.isSetAfterTrim(name)) {
            updateStatus("A name must be given");
            return;
        }
        
        if(name.indexOf(" ")!=-1) {
        	updateStatus("Please do not input any white-space in the name!");
            return;
        }
        
        if(!name.endsWith(".xml")) {
            name += ".xml";
        }

        File folder = null;
        
        if(fCurrentLDEditor != null) {
            ILDModel ldModel = (ILDModel)fCurrentLDEditor.getAdapter(ILDModel.class);
            folder = ldModel.getRootFolder();
        }
        else {
            Object o = ((IStructuredSelection)fOrganiserTreeViewer.getSelection()).getFirstElement();
            if(o instanceof IOrganiserLD) {
                folder = ((IOrganiserLD)o).getFile().getParentFile();
            }
        }
        
        if(folder == null) { //$NON-NLS-1$
            updateStatus("Please select parent LD");
            return;
        }
        
        String fileName = FileUtils.getValidFileName(name);
        
        fFile = new File(folder, fileName);
        if(fFile.exists() && !fFile.isDirectory()) {
            updateStatus("File exists");
            fFile = null;
            return;
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

    public File getFile() {
        return fFile;
    }
}
