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
package org.tencompetence.ldauthor.ui.views.inspector.activity;

import java.util.List;

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
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentRefOwner;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Environment List Viewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentListViewer.java,v 1.3 2009/05/19 18:21:05 phillipus Exp $
 */
public class EnvironmentListViewer extends CheckboxTableViewer {
    
    private IEnvironmentRefOwner fModel;
    
    public EnvironmentListViewer(Composite parent, int style) {
        super(new Table(parent, SWT.CHECK | SWT.FULL_SELECTION | style));
        
        // Content Provider
        IStructuredContentProvider contentProvider = new IStructuredContentProvider() {
            public Object[] getElements(Object inputElement) {
                // Display all Environments
                return fModel.getLDModel().getEnvironmentsModel().getChildren().toArray();
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
                return ImageFactory.getImage(ImageFactory.IMAGE_ENVIRONMENT_16);
            }
        };
        
        setLabelProvider(labelProvider);
        
        // Listen to selections
        addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                IEnvironmentModel env = (IEnvironmentModel)event.getElement();
                selectEnvironment(env, event.getChecked());
            }
        });
    }

    /**
     * Set the Activity Model
     * @param model
     */
    public void setModel(IEnvironmentRefOwner model) {
        fModel = model;
        setInput(model);
        setCheckedElements();
    }
    
    /**
     * Set the check marks on slected Environments
     */
    private void setCheckedElements() {
        // Clear ticked elements
        setCheckedElements(new Object[] {});
        
        List<ILDModelObjectReference> list = fModel.getEnvironmentRefs();
        for(ILDModelObjectReference ref : list) {
            setChecked(ref.getLDModelObject(), true);
        }
    }

    private void selectEnvironment(IEnvironmentModel env, boolean checked) {
        if(checked) {
            fModel.addEnvironmentRef(env);
        }
        else {
            fModel.removeEnvironmentRef(env);
        }

        fModel.getLDModel().setDirty();
    }


}
