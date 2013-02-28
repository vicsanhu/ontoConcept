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
package org.tencompetence.ldauthor.ui.views.inspector.method.act;

import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.roles.ILearnerModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * RolePart ListViewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: RolePartListViewer.java,v 1.5 2009/05/19 18:21:04 phillipus Exp $
 */
public class RolePartListViewer extends CheckboxTableViewer {
    
    private IActModel fActModel;
    
    public RolePartListViewer(Composite parent, int style) {
        super(new Table(parent, SWT.CHECK | SWT.FULL_SELECTION | style));
        
        // Content Provider
        IStructuredContentProvider contentProvider = new IStructuredContentProvider() {
            public Object[] getElements(Object inputElement) {
                // Display all Role Parts
                return fActModel.getRolePartsModel().getChildren().toArray();
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
                IRolePartModel rolePart = (IRolePartModel)element;
                IRoleModel role = rolePart.getRole();
                ILDModelObject component = rolePart.getComponent();

                String text;
                
                if(role != null && component != null) {
                    text = role.getTitle() + 
                    " " + //$NON-NLS-1$
                    Messages.RolePartListViewer_0 + 
                    " '" + //$NON-NLS-1$
                    ((ITitle)component).getTitle()
                    + "'"; //$NON-NLS-1$
                }
                else {
                    text = rolePart.getTitle();
                }
                
                return text;
            }
            
            @Override
            public Image getImage(Object element) {
                IRolePartModel rolePart = (IRolePartModel)element;
                IRoleModel role = rolePart.getRole();
                if(role instanceof ILearnerModel) {
                    return ImageFactory.getImage(ImageFactory.IMAGE_LEARNER_16);
                }
                return ImageFactory.getImage(ImageFactory.IMAGE_STAFF_16);
            }
        };
        
        setLabelProvider(labelProvider);
        
        // Sorter
        setSorter(new ViewerSorter());
        
        // Listen to selections
        addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                IRolePartModel rolePart = (IRolePartModel)event.getElement();
                selectRolePart(rolePart, event.getChecked());
            }
        });
    }

    /**
     * Set the Act
     * @param act
     */
    public void setActModel(IActModel actModel) {
        fActModel = actModel;
        setInput(actModel);
        setCheckedElements();
    }
    
    /**
     * Set the check marks on slected Plays
     */
    private void setCheckedElements() {
        // Clear ticked elements
        setCheckedElements(new Object[] {});
        
        List<IRolePartModel> list = fActModel.getCompleteActType().getRoleParts();
        for(IRolePartModel rolePart : list) {
            setChecked(rolePart, true);
        }
    }

    private void selectRolePart(IRolePartModel rolePart, boolean checked) {
        List<IRolePartModel> list = fActModel.getCompleteActType().getRoleParts();
        
        if(checked) {
            list.add(rolePart);
        }
        else {
            list.remove(rolePart);
        }

        fActModel.getLDModel().setDirty();
    }


}
