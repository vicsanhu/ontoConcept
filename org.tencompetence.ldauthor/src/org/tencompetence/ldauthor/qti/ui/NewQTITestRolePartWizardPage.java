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
package org.tencompetence.ldauthor.qti.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
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
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.qti.model.QTIUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * New QTI Test RolePart Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewQTITestRolePartWizardPage.java,v 1.5 2009/10/30 18:00:10 phillipus Exp $
 */
public class NewQTITestRolePartWizardPage extends WizardPage {

    public static final String PAGE_NAME = "NewQTITestRolePartWizardPage"; //$NON-NLS-1$
    
    private Text fNameText;

    private ILDModel fLDModel;
    
    private Button fUseExistingTest;
    
    private ListViewer fTestListViewer;
    
    private Button[] fRoleButtons;
    

    /**
	 * Constructor
	 */
    public NewQTITestRolePartWizardPage(ILDModel ldModel) {
		super(PAGE_NAME);
		
		setTitle(Messages.NewQTITestRolePartWizardPage_0);
		setDescription(Messages.NewQTITestRolePartWizardPage_1);
        setImageDescriptor(ImageFactory.getEclipseImageDescriptor(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
        
        fLDModel = ldModel;
	}

    public void createControl(Composite parent) {
        GridData gd;
	    Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        
        Composite fieldContainer = new Composite(container, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        fieldContainer.setLayout(layout);
        
        // Name
	    Label label = new Label(fieldContainer, SWT.NULL);
	    label.setText(Messages.NewQTITestRolePartWizardPage_2);

	    fNameText = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
	    fNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    fNameText.addModifyListener(new ModifyListener() {
	        public void modifyText(ModifyEvent e) {
	            validateFields();
	        }
	    });

        // Use existing test file
        fUseExistingTest = new Button(fieldContainer, SWT.CHECK);
        fUseExistingTest.setText(Messages.NewQTITestRolePartWizardPage_3);
        gd = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd.horizontalSpan = 2;
        fUseExistingTest.setLayoutData(gd);
        fUseExistingTest.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fTestListViewer.getControl().setEnabled(fUseExistingTest.getSelection());
                validateFields();
            }
        });
        
        // List of tests
        fTestListViewer = new ListViewer(fieldContainer, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 2;
        fTestListViewer.getControl().setLayoutData(gd);
        fTestListViewer.getControl().setEnabled(false);
        fTestListViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                validateFields();
            }
        });
        
        fTestListViewer.setContentProvider(new IStructuredContentProvider() {
            public Object[] getElements(Object inputElement) {
                // Display all Resources of QTI Type
                List<IResourceModel> list = QTIUtils.getQTIResources(fLDModel);
                fUseExistingTest.setEnabled(!list.isEmpty());
                return list.toArray();
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        });
        
        // Label Provider
        fTestListViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ((IResourceModel)element).getIdentifier();
            }
        });
        
        fTestListViewer.setInput(""); //$NON-NLS-1$

        // Show Roles as ticks
        Group group = new Group(fieldContainer, SWT.NULL);
        group.setText(Messages.NewQTITestRolePartWizardPage_4);
        group.setLayout(new GridLayout(2, true));
        gd = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd.horizontalSpan = 2;
        group.setLayoutData(gd);
        
        List<IRoleModel> roles = fLDModel.getRolesModel().getOrderedRoles();
        fRoleButtons = new Button[roles.size()];
        
        // Default Learner Role
        IRoleModel defaultRole = fLDModel.getRolesModel().getDefaultLearnerRole();
        
        // OK, try Default Staff Role
        if(defaultRole == null) {
            defaultRole = fLDModel.getRolesModel().getDefaultStaffRole();
        }
        
        for(int i = 0; i < roles.size(); i++) {
            IRoleModel role = roles.get(i);
            fRoleButtons[i] = new Button(group, SWT.CHECK);
            fRoleButtons[i].setText(role.getTitle());
            fRoleButtons[i].setData(role);
            fRoleButtons[i].addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    validateFields();
                }
            });
            // Set default role
            if(role == defaultRole) {
                fRoleButtons[i].setSelection(true);
            }
        }
        
        // If only one Role, then disable it
        if(fRoleButtons.length == 1) {
            fRoleButtons[0].setEnabled(false);
        }
	    
	    setPageComplete(false);
        setControl(container);
	}

    /**
     * @return The name of the Test
     */
    public String getTestName() {
        return fNameText.getText();
    }
    
    public List<IRoleModel> getRoles() {
        List<IRoleModel> list = new ArrayList<IRoleModel>();
        for(Button button : fRoleButtons) {
            if(button.getSelection()) {
                list.add((IRoleModel)button.getData());
            }
        }
        return list;
    }
    
    /**
     * @return Whether user has chosen to use existing Test Resource
     */
    public boolean isExistingTestSelected() {
        return fUseExistingTest.getSelection();
    }
    
    /**
     * @return The selected Test Resource or null if not selected
     */
    public IResourceModel getSelectedTestResource() {
        IStructuredSelection selection = (IStructuredSelection)fTestListViewer.getSelection();
        if(selection.isEmpty()) {
            return null;
        }
        
        return (IResourceModel)selection.getFirstElement();
    }
    
    private void validateFields() {
        String title = fNameText.getText();
        
        if(!StringUtils.isSetAfterTrim(title)) {
            updateStatus(Messages.NewQTITestRolePartWizardPage_5);
            return;
        }
        
        boolean selected = false;
        for(Button button : fRoleButtons) {
            if(button.getSelection()) {
                selected = true;
            }
        }
        if(!selected) {
            updateStatus(Messages.NewQTITestRolePartWizardPage_6);
            return;
        }
        
        if(fUseExistingTest.getSelection() && fTestListViewer.getSelection().isEmpty()) {
            updateStatus(Messages.NewQTITestRolePartWizardPage_7);
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