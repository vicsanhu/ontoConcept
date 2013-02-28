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
package org.tencompetence.ldauthor.ui.views.ldproperties;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.properties.ILocalRolePropertyModel;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;


/**
 * Composite for editing Level B Properties
 * 
 * @author Phillip Beauvoir
 * @version $Id: LocalRolePropertyEditorComposite.java,v 1.6 2009/05/19 18:21:04 phillipus Exp $
 */
public class LocalRolePropertyEditorComposite extends AbstractPropertyEditorComposite {
    
    private ComboViewer fComboRoles;
    
    public LocalRolePropertyEditorComposite(Composite parent, int style) {
        super(parent, style);
    }
    
    @Override
    protected void insertUIComponents(Control previousControl) {
        if(previousControl == fTextIdentifier) {
            AppFormToolkit.getInstance().createLabel(fFieldsComposite, Messages.LocalRolePropertyEditorComposite_0);
            fComboRoles = new ComboViewer(fFieldsComposite, SWT.READ_ONLY);
            fComboRoles.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            fComboRoles.setContentProvider(new RoleComboViewerProvider());
            fComboRoles.setLabelProvider(new RoleComboViewerLabelProvider());
           
            fComboRoles.getCombo().addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    if(fIsUpdating) {
                        return;
                    }
                    
                    if(fLDProperty == null) {
                        return;
                    }
                    
                    Object control = e.getSource();
                    if(control == fComboRoles.getControl()) {
                        StructuredSelection selection = (StructuredSelection)fComboRoles.getSelection();
                        if(!selection.isEmpty()) {
                            IRoleModel role = (IRoleModel)selection.getFirstElement();
                            ((ILocalRolePropertyModel)fLDProperty).setRole(role);
                        }
                    }
                    
                    fLDProperty.getLDModel().setDirty();
                }
            });
        }
    }               
    
    @Override
    public void setProperty(IPropertyTypeModel property) {
        super.setProperty(property);
        
        fIsUpdating = true;
        
        fComboRoles.setInput(property.getLDModel());
        
        IRoleModel role = ((ILocalRolePropertyModel)property).getRole();
        if(role != null) {
            fComboRoles.setSelection( new StructuredSelection(role) );
        }
        else {
            fComboRoles.setSelection(null);  // no selection
        }
        
        fIsUpdating = false;
    }
    
    @Override
    protected String getTitle() {
        return Messages.LocalRolePropertyEditorComposite_1;
    }
    
    // ===================================================================================
    //                           Combo Viewer Provider
    // ===================================================================================
    
    private class RoleComboViewerProvider
    implements IStructuredContentProvider {
        
        public Object[] getElements(Object input) {
            ILDModel ldModel = (ILDModel)input;
            return ldModel.getRolesModel().getOrderedRoles().toArray();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

    }
    
    private class RoleComboViewerLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            return ((IRoleModel)element).getTitle();
        }
    }

}
