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
package org.tencompetence.ldauthor.ui.wizards.method;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.method.IPlaysModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * New Act Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewActWizardPage.java,v 1.5 2009/05/22 16:35:05 phillipus Exp $
 */
public class NewActWizardPage extends WizardPage {

    public static final String PAGE_NAME = "NewActWizardPage"; //$NON-NLS-1$
    
    private Text fNameText;
    
    private ListViewer fListViewer;
    
    private IPlaysModel fPlaysModel;
    
    /**
	 * Constructor
     * @param playsModel 
	 */
    public NewActWizardPage(IPlaysModel playsModel) {
		super(PAGE_NAME);
		setTitle(Messages.NewActWizardPage_0 + LDModelUtils.getUserObjectName(LDModelFactory.ACT));
		setDescription(Messages.NewActWizardPage_1);
        setImageDescriptor(ImageFactory.getEclipseImageDescriptor(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
        
        fPlaysModel = playsModel;
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
	    label.setText(Messages.NewActWizardPage_2);

	    fNameText = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
	    fNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    fNameText.addModifyListener(new ModifyListener() {
	        public void modifyText(ModifyEvent e) {
	            validateFields();
	        }
	    });
	    
	    // If PlaysModel is not null then show a Play Selector Control
	    if(fPlaysModel != null) {
	        label = new Label(fieldContainer, SWT.NULL);
	        label.setText(Messages.NewActWizardPage_3 + LDModelUtils.getUserObjectName(LDModelFactory.PLAY) + ":"); //$NON-NLS-1$

	        fListViewer = new ListViewer(container, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
	        fListViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	        
	        fListViewer.setContentProvider(new IStructuredContentProvider() {
	            public Object[] getElements(Object inputElement) {
	                // Display all Plays
	                return fPlaysModel.getChildren().toArray();
	            }

	            public void dispose() {
	            }

	            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	            }
	        });
	        
	        // Label Provider
	        fListViewer.setLabelProvider(new LabelProvider() {
	            @Override
	            public String getText(Object element) {
	                return ((ITitle)element).getTitle();
	            }
	        });
	        
	        fListViewer.setInput(fPlaysModel);
	        
	        fListViewer.setSelection(new StructuredSelection(fPlaysModel.getChildren().get(0)));
	    }

	    setPageComplete(false);
        setControl(container);
	}

    /**
     * @return The Act Title
     */
    public String getActTitle() {
        return fNameText.getText();
    }
    
    /**
     * @return The Play to add to or null
     */
    public IPlayModel getParentPlay() {
        if(fListViewer != null) {
            return (IPlayModel)((IStructuredSelection)fListViewer.getSelection()).getFirstElement();
        }
        
        return null;
    }
    
    private void validateFields() {
        String path = fNameText.getText();
        
        if(!StringUtils.isSetAfterTrim(path)) {
            updateStatus(Messages.NewActWizardPage_4);
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