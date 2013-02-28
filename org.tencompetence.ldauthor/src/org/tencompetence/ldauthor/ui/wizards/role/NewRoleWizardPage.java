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
package org.tencompetence.ldauthor.ui.wizards.role;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.roles.ILearnerModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * New Role Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewRoleWizardPage.java,v 1.9 2009/05/19 20:23:04 phillipus Exp $
 */
public class NewRoleWizardPage extends WizardPage {

    public static final String PAGE_NAME = "NewRoleWizardPage"; //$NON-NLS-1$
    
    private Text fNameText;
    
    private ComboViewer fComboInheritsFrom;
    
    private Spinner fSpinnerMinPersons, fSpinnerMaxPersons;
    
    private Combo fComboCreateNew, fComboMatchPersons;

    
    private ILDModel fLDModel;
    
    private Class<?> fType;
    
    private String[] CREATE_NEW_STRINGS_HUMAN = {
            Messages.NewRoleWizardPage_11,
            Messages.NewRoleWizardPage_12,
            Messages.NewRoleWizardPage_13
    };
    
    private String[] MATCH_PERSONS_STRINGS_HUMAN = {
            Messages.NewRoleWizardPage_14,
            Messages.NewRoleWizardPage_15,
            Messages.NewRoleWizardPage_16
    };

    // "Self" Role - bogus role signifying "self" option
    private static String SELF_ROLE = Messages.NewRoleWizardPage_5;
    
    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            Object control = e.getSource();
            if(control == fSpinnerMinPersons || control == fSpinnerMaxPersons) {
                int min = fSpinnerMinPersons.getSelection();
                int max = fSpinnerMaxPersons.getSelection();
                
                if(min > max) {
                    fSpinnerMaxPersons.setSelection(min);
                }
            }
        }
    };
    
    /**
	 * Constructor
	 */
    public NewRoleWizardPage(ILDModel ldModel, Class<?> type) {
		super(PAGE_NAME);
		fLDModel = ldModel;
		fType = type;
		setTitle((type == ILearnerModel.class) ? Messages.NewRoleWizardPage_0 : Messages.NewRoleWizardPage_1);
		setDescription(Messages.NewRoleWizardPage_2);
		
		if(fType == ILearnerModel.class) {
		    setImageDescriptor(ImageFactory.getImageDescriptor(ImageFactory.IMAGE_NEW_LEARNER_WIZBAN));
		}
		else {
		    setImageDescriptor(ImageFactory.getImageDescriptor(ImageFactory.IMAGE_NEW_STAFF_WIZBAN));
		}
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
	    label.setText(Messages.NewRoleWizardPage_3);

	    fNameText = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
	    fNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    fNameText.addModifyListener(new ModifyListener() {
	        public void modifyText(ModifyEvent e) {
	            validateFields();
	        }
	    });
	    
        new Label(fieldContainer, SWT.NULL);
        new Label(fieldContainer, SWT.NULL);
        
	    label = new Label(fieldContainer, SWT.NULL);
        label.setText(Messages.NewRoleWizardPage_6);
        
        fComboInheritsFrom = new ComboViewer(fieldContainer, SWT.READ_ONLY);
        fComboInheritsFrom.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fComboInheritsFrom.setContentProvider(new RoleComboViewerProvider());
        fComboInheritsFrom.setLabelProvider(new RoleComboViewerLabelProvider());
        fComboInheritsFrom.setInput(fLDModel);
        fComboInheritsFrom.setSelection( new StructuredSelection(SELF_ROLE) );
        
        label = new Label(fieldContainer, SWT.NULL);
        label.setText(Messages.NewRoleWizardPage_7);
        fSpinnerMinPersons = new Spinner(fieldContainer, SWT.BORDER);
        fSpinnerMinPersons.addModifyListener(fModifyListener);
        
        label = new Label(fieldContainer, SWT.NULL);
        label.setText(Messages.NewRoleWizardPage_8);
        fSpinnerMaxPersons = new Spinner(fieldContainer, SWT.BORDER);
        fSpinnerMaxPersons.addModifyListener(fModifyListener);
        
        label = new Label(fieldContainer, SWT.NULL);
        label.setText(Messages.NewRoleWizardPage_9);
        fComboCreateNew = new Combo(fieldContainer, SWT.DROP_DOWN | SWT.READ_ONLY);
        fComboCreateNew.setItems(CREATE_NEW_STRINGS_HUMAN);
        fComboCreateNew.setText(CREATE_NEW_STRINGS_HUMAN[0]);
        
        label = new Label(fieldContainer, SWT.NULL);
        label.setText(Messages.NewRoleWizardPage_10);
        fComboMatchPersons = new Combo(fieldContainer, SWT.DROP_DOWN | SWT.READ_ONLY);
        fComboMatchPersons.setItems(MATCH_PERSONS_STRINGS_HUMAN);
        fComboMatchPersons.setText(MATCH_PERSONS_STRINGS_HUMAN[0]);

	    setPageComplete(false);
        setControl(container);
	}

    /**
     * @return The Role's Title
     */
    public String getRoleTitle() {
        return fNameText.getText();
    }
    
    /**
     * @return The Role's parent
     */
    public ILDModelObjectContainer getRoleParent() {
        Object parent = ((StructuredSelection)fComboInheritsFrom.getSelection()).getFirstElement();
        
        // No inheritance
        if(parent == SELF_ROLE) {
            return fLDModel.getRolesModel();
        }
        
        return (ILDModelObjectContainer)parent;
    }
    
    public int getRoleMinPersons() {
        return fSpinnerMinPersons.getSelection();
    }
    
    public int getRoleMaxPersons() {
        return fSpinnerMaxPersons.getSelection();
    }
    
    public int getRoleCreateNew() {
        return fComboCreateNew.getSelectionIndex();
    }
    
    public int getRoleMatchPersons() {
        return fComboMatchPersons.getSelectionIndex();
    }
    
    private void validateFields() {
        String path = fNameText.getText();
        
        if(!StringUtils.isSetAfterTrim(path)) {
            updateStatus(Messages.NewRoleWizardPage_4);
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
    
    // ===================================================================================
    //                           Combo Viewer Provider
    // ===================================================================================
    
    private class RoleComboViewerProvider
    implements IStructuredContentProvider {
        
        public Object[] getElements(Object input) {
            List<Object> list = new ArrayList<Object>();
            
            // No inheritance
            list.add(SELF_ROLE);

            if(fType == ILearnerModel.class) {
                for(IRoleModel learner : fLDModel.getRolesModel().getOrderedLearners()) {
                    list.add(learner);
                }
            }
            else {
                for(IRoleModel staff : fLDModel.getRolesModel().getOrderedStaff()) {
                    list.add(staff);
                }
            }

            return list.toArray();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

    }
    
    private class RoleComboViewerLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            if(element instanceof String) {
                return (String)element;
            }
            return ((IRoleModel)element).getTitle();
        }
    }

}