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
package org.tencompetence.ldauthor.ui.views.inspector.activity.supportactivity;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.roles.ILearnerModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Role Selection Viewer for Support Activity
 * 
 * @author Phillip Beauvoir
 * @version $Id: RoleSelectionViewer.java,v 1.4 2009/05/19 18:21:09 phillipus Exp $
 */
public class RoleSelectionViewer extends CheckboxTableViewer {
    
    
    private ISupportActivityModel fSupportActivity;
    
    public RoleSelectionViewer(Composite parent, int style) {
        super(new Table(parent, SWT.CHECK | SWT.FULL_SELECTION | style));
        
        // Content Provider
        IStructuredContentProvider contentProvider = new IStructuredContentProvider() {
            public Object[] getElements(Object inputElement) {
                // Display all Roles
                return fSupportActivity.getLDModel().getRolesModel().getOrderedRoles().toArray();
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        };

        setContentProvider(contentProvider);
        
        // Label Provider
        LabelProvider labelProvider = new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ((ITitle)element).getTitle();
            }
            
            @Override
            public Image getImage(Object element) {
                if(element instanceof ILearnerModel) {
                    return ImageFactory.getImage(ImageFactory.IMAGE_LEARNER_16);
                }
                else {
                    return ImageFactory.getImage(ImageFactory.IMAGE_STAFF_16);
                }
            }
        };
        
        setLabelProvider(labelProvider);
        
        // Listen to selections
        addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                IRoleModel role = (IRoleModel)event.getElement();
                selectRole(role, event.getChecked());
            }
        });
    }

    /**
     * Set the Support Activity Model
     * @param model
     */
    public void setSupportActivity(ISupportActivityModel model) {
        fSupportActivity = model;
        setInput(model.getLDModel().getRolesModel());
        setCheckedElements();
    }
    
    /**
     * Set the check marks on selected Roles
     */
    private void setCheckedElements() {
        // Clear ticked elements
        setCheckedElements(new Object[] {});
        
        for(ILDModelObjectReference ref : fSupportActivity.getRoleRefs()) {
            setChecked(ref.getLDModelObject(), true);
        }
    }

    private void selectRole(IRoleModel role, boolean checked) {
        if(checked) {
            fSupportActivity.addRoleRef(role);
        }
        else {
            fSupportActivity.removeRoleRef(role);
        }

        fSupportActivity.getLDModel().setDirty();
    }

}
