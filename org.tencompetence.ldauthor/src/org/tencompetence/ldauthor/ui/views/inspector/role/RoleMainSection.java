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
package org.tencompetence.ldauthor.ui.views.inspector.role;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.IParentable;
import org.tencompetence.imsldmodel.roles.ILearnerModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.roles.IStaffModel;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Role Main Section
 * 
 * @author Phillip Beauvoir
 * @version $Id: RoleMainSection.java,v 1.10 2009/06/10 10:16:49 phillipus Exp $
 */
public class RoleMainSection extends AbstractScrolledInspectorSection {
    
    private Text fTextTitle, fTextHref;
    
    private Text fTextID;
    
    private Spinner fSpinnerMinPersons, fSpinnerMaxPersons;
    
    private Combo fComboCreateNew, fComboMatchPersons;
    
    private ComboViewer fComboInheritsFrom;
    
    private boolean fIsUpdating;
    
    private IRoleModel fRoleModel;
    
    private String[] CREATE_NEW_STRINGS_HUMAN = {
            Messages.RoleMainSection_0,
            Messages.RoleMainSection_1,
            Messages.RoleMainSection_2
    };
    
    private String[] MATCH_PERSONS_STRINGS_HUMAN = {
            Messages.RoleMainSection_3,
            Messages.RoleMainSection_4,
            Messages.RoleMainSection_5
    };

    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fRoleModel == null) {
                return;
            }
            
            Object control = e.getSource();
            if(control == fTextTitle) {
                String s = fTextTitle.getText();
                fRoleModel.setTitle(s);
            }
            else if(control == fTextHref) {
                fRoleModel.setHref(fTextHref.getText());
            }
            else if(control == fSpinnerMinPersons) {
                fRoleModel.setMinPersons(fSpinnerMinPersons.getSelection());
                checkNumbers();
            }
            else if(control == fSpinnerMaxPersons) {
                fRoleModel.setMaxPersons(fSpinnerMaxPersons.getSelection());
                checkNumbers();
            }
            
            fRoleModel.getLDModel().setDirty();
        }
    };
    
    private SelectionAdapter fSelectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fRoleModel == null) {
                return;
            }
            
            Object control = e.getSource();
            if(control == fComboCreateNew) {
                fRoleModel.setCreateNew(fComboCreateNew.getSelectionIndex());
            }
            else if(control == fComboMatchPersons) {
                fRoleModel.setMatchPersons(fComboMatchPersons.getSelectionIndex());
            }
            
            // Move Role to new Role Parent
            else if(control == fComboInheritsFrom.getControl()) {
                ILDModelObjectContainer newParent = (ILDModelObjectContainer)((StructuredSelection)fComboInheritsFrom.getSelection()).getFirstElement();
                ILDModelObjectContainer oldParent = fRoleModel.getParent();
                if(newParent == fRoleModel) {
                    // itself, so parent is RolesModel
                    newParent = fRoleModel.getLDModel().getRolesModel();
                }
                // same parent
                if(newParent == oldParent) {
                    return;
                }

                // move it.  
                fRoleModel.getLDModel().setNotifications(false); // we don't need notifications
                oldParent.removeChild(fRoleModel);
                newParent.addChild(fRoleModel);
                fRoleModel.getLDModel().setNotifications(true);
            }
            
            fRoleModel.getLDModel().setDirty();
        }
    };

    
    public RoleMainSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        setContent(composite);
        
        GridData gd;
        
        Label label = factory.createLabel(composite, Messages.RoleMainPropertySection_0);
        label.setToolTipText(Messages.RoleMainPropertySection_1);
        fTextTitle = factory.createText(composite, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextTitle.setLayoutData(gd);
        fTextTitle.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.RoleMainPropertySection_2);
        label.setToolTipText(Messages.RoleMainPropertySection_3);
        fTextHref = factory.createText(composite, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextHref.setLayoutData(gd);
        fTextHref.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.RoleMainPropertySection_4);
        label.setToolTipText(Messages.RoleMainPropertySection_5);
        fComboInheritsFrom = new ComboViewer(composite, SWT.READ_ONLY);
        fComboInheritsFrom.setContentProvider(new RoleComboViewerProvider());
        fComboInheritsFrom.setLabelProvider(new RoleComboViewerLabelProvider());
        fComboInheritsFrom.getCombo().addSelectionListener(fSelectionListener);
        gd = new GridData(SWT.NULL, SWT.FILL, false, true);
        gd.widthHint = 200;
        fComboInheritsFrom.getCombo().setLayoutData(gd);
        
        label = factory.createLabel(composite, Messages.RoleMainPropertySection_6);
        label.setToolTipText(Messages.RoleMainPropertySection_7);
        fSpinnerMinPersons = new Spinner(composite, SWT.BORDER);
        fSpinnerMinPersons.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.RoleMainPropertySection_8);
        label.setToolTipText(Messages.RoleMainPropertySection_9);
        fSpinnerMaxPersons = new Spinner(composite, SWT.BORDER);
        fSpinnerMaxPersons.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.RoleMainPropertySection_10);
        label.setToolTipText(Messages.RoleMainPropertySection_11);
        fComboCreateNew = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        fComboCreateNew.setItems(CREATE_NEW_STRINGS_HUMAN);
        fComboCreateNew.addSelectionListener(fSelectionListener);
        
        label = factory.createLabel(composite, Messages.RoleMainPropertySection_12);
        label.setToolTipText(Messages.RoleMainPropertySection_13);
        fComboMatchPersons = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        fComboMatchPersons.setItems(MATCH_PERSONS_STRINGS_HUMAN);
        fComboMatchPersons.addSelectionListener(fSelectionListener);
        
        label = factory.createLabel(composite, Messages.RoleMainPropertySection_14);
        label.setToolTipText(Messages.RoleMainPropertySection_15);
        fTextID = factory.createText(composite, "", SWT.READ_ONLY); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextID.setLayoutData(gd);

        composite.layout();
        composite.pack();
    }
    
    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IRoleModel) {
            fRoleModel = (IRoleModel)element;
        }
        else {
            throw new RuntimeException("Should have been a Role"); //$NON-NLS-1$
        }
        
        refresh();
    }
    
    public void refresh() {
        if(fRoleModel == null) {
            return;
        }
        
        // Populate fields...
        fIsUpdating = true;
        
        String s = StringUtils.safeString(fRoleModel.getTitle());
        fTextTitle.setText(s);

        s = StringUtils.safeString(fRoleModel.getHref());
        fTextHref.setText(s);
        
        fComboInheritsFrom.setInput(fRoleModel);
        if(fRoleModel.getParent() instanceof IRoleModel) {
            fComboInheritsFrom.setSelection( new StructuredSelection(fRoleModel.getParent()) );
        }
        else {
            fComboInheritsFrom.setSelection( new StructuredSelection(fRoleModel) );
        }

        int min = fRoleModel.getMinPersons();
        fSpinnerMinPersons.setSelection(min);
        
        int max = fRoleModel.getMaxPersons();
        fSpinnerMaxPersons.setSelection(max);
       
        int index = fRoleModel.getCreateNew();
        fComboCreateNew.setText(CREATE_NEW_STRINGS_HUMAN[index]);

        index = fRoleModel.getMatchPersons();
        fComboMatchPersons.setText(MATCH_PERSONS_STRINGS_HUMAN[index]);
        
        fTextID.setText(StringUtils.safeString(fRoleModel.getIdentifier()));
        
        fIsUpdating = false;
    }

    
    /**
     * Check Min/Max numbers makes sense
     */
    private void checkNumbers() {
        int min = fSpinnerMinPersons.getSelection();
        int max = fSpinnerMaxPersons.getSelection();
        
        if(min > max) {
            fSpinnerMaxPersons.setSelection(min);
        }
    }

    // ===================================================================================
    //                           Combo Viewer Provider
    // ===================================================================================
    
    private class RoleComboViewerProvider
    implements IStructuredContentProvider {
        
        public Object[] getElements(Object input) {
            IRoleModel role = (IRoleModel)input;
            
            List<Object> list = new ArrayList<Object>();

            if(role instanceof ILearnerModel) {
                for(IRoleModel learner : role.getLDModel().getRolesModel().getOrderedLearners()) {
                    if(!isDescendant(role, learner)) {
                        list.add(learner);
                    }
                }
            }
            
            if(role instanceof IStaffModel) {
                for(IRoleModel staff : role.getLDModel().getRolesModel().getOrderedStaff()) {
                    if(!isDescendant(role, staff)) {
                        list.add(staff);
                    }
                }
            }
            
            return list.toArray();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        /**
         * @param parent
         * @param obj
         * @return True if obj is a child or descendant of parent
         */
        private boolean isDescendant(ILDModelObjectContainer parent, IParentable obj) {
            ILDModelObjectContainer p = obj.getParent();
            while(p != null) {
                if(p == parent) {
                    return true;
                }
                p = (p instanceof IParentable) ? ((IParentable)p).getParent() : null;
            }
            
            return false;
        }
    }
    
    private class RoleComboViewerLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            return ((IRoleModel)element).getTitle();
        }
    }
}
