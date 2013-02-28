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
package org.tencompetence.ldauthor.ui.views.inspector.environment.indexsearch;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.IIdentifier;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IIndexSearchModel;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * IndexElementTreeViewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: IndexElementTreeViewer.java,v 1.3 2009/06/16 09:49:46 phillipus Exp $
 */
public class IndexElementTreeViewer extends CheckboxTreeViewer {
    
    private List<String> fSelected;
    private ILDModel fLDModel;
    
    public IndexElementTreeViewer(Composite parent, int style) {
        super(parent, style);
        
        setContentProvider(new ListContentProvider());
        
        // Label Provider
        LabelProvider labelProvider = new LabelProvider() {
            @Override
            public String getText(Object element) {
                if(element instanceof ITitle) {
                    return ((ITitle)element).getTitle();
                }
                
                return element.toString();
            }
            
            @Override
            public Image getImage(Object element) {
                if(element instanceof ILDModelObject) {
                    return ImageFactory.getIconLDType((ILDModelObject)element);
                }
                return null;
            }
        };
        
        setLabelProvider(labelProvider);
        
        // Listen to selections
        addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                createSelectedList();
                getTree().notifyListeners(SWT.Modify, null);
            }
        });

    }
    
    public List<String> getItems() {
        return fSelected;
    }

    public void setIndexSearch(IIndexSearchModel indexSearch) {
        fSelected = indexSearch.getIndexElements();
        fLDModel = indexSearch.getLDModel();
        setInput(""); //$NON-NLS-1$
        
        for(String string : fSelected) {
            ILDModelObject object = fLDModel.getModelObject(string);
            if(object != null) {
                setChecked(object, true);
            }
        }
    }
    
    private void createSelectedList() {
        fSelected.clear();
        
        for(Object object : getCheckedElements()) {
            if(object instanceof IIdentifier) {
                fSelected.add(((IIdentifier)object).getIdentifier());
            }
        }
    }
    
    // ===================================================================================
    //                                Content Provider
    // ===================================================================================

    /**
     * Content Provider
     */
    class ListContentProvider implements ITreeContentProvider {
        
        public Object[] getChildren(Object parentElement) {
            if(parentElement instanceof ILDModelObjectContainer) {
                return removeRefs((ILDModelObjectContainer)parentElement);
            }
            return null;
        }

        public Object getParent(Object element) {
            // Need to have parents for the checked thing to work
            for(ILDModelObject environment : fLDModel.getEnvironmentsModel().getChildren()) {
                for(Object child : ((IEnvironmentModel)environment).getChildren()) {
                    if(element == child) {
                        return environment;
                    }
                }
            }
            return null;
        }

        public boolean hasChildren(Object element) {
            if(element instanceof ILDModelObjectContainer) {
                return removeRefs((ILDModelObjectContainer)element).length > 0;
            }
            return false;
        }

        public Object[] getElements(Object inputElement) {
            List<Object> list = new ArrayList<Object>();
            
            for(Object object : fLDModel.getActivitiesModel().getLearningActivitiesModel().getChildren()) {
                list.add(object);
            }
            
            for(Object object : fLDModel.getActivitiesModel().getSupportActivitiesModel().getChildren()) {
                list.add(object);
            }
            
            for(Object object : fLDModel.getActivitiesModel().getActivityStructuresModel().getChildren()) {
                list.add(object);
            }
            
            for(Object object : fLDModel.getEnvironmentsModel().getChildren()) {
                list.add(object);
            }
            
            return list.toArray();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
        
        /**
         * Remove Refs from an object.  We don't want to show these.
         * @return a list with Refs removed
         */
        private Object[] removeRefs(ILDModelObjectContainer env) {
            List<ILDModelObject> list = new ArrayList<ILDModelObject>();
            
            for(ILDModelObject child : env.getChildren()) {
                if(!(child instanceof ILDModelObjectReference)) {
                    list.add(child);
                }
            }
            
            return list.toArray();
        }
    }
}
