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
package org.tencompetence.ldauthor.ui.views.organiser.global;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.ldauthor.organisermodel.IOrganiserContainer;
import org.tencompetence.ldauthor.organisermodel.IOrganiserObject;
import org.tencompetence.ldauthor.organisermodel.OrganiserImageFactory;


/**
 * Tree for Organiser View
 * 
 * @author Phillip Beauvoir
 * @version $Id: OrganiserTreeViewer.java,v 1.1 2008/03/08 20:19:48 phillipus Exp $
 */
public class OrganiserTreeViewer
extends TreeViewer {
    
    private Class<?>[] fFilter;
    
    /**
     * Constructor
     * @param parent
     * @param style
     */
    public OrganiserTreeViewer(Composite parent, int style) {
        super(parent, style);
        
        setSorter(new ViewerSorter() {
            @Override
            public int category(Object element) {
                if(element instanceof IOrganiserContainer) {
                    return 0;
                }
                return 1;
            }
        });

        setContentProvider(new OrganiserViewContentProvider());
        setLabelProvider(new OrganiserViewLabelProvider());
    }
    
    /**
     * @param filter Show only filter types
     */
    public void setFilter(Class<?>[] filter) {
        fFilter = filter;
    }
    
    /**
     * View Content Provider
     */
    private class OrganiserViewContentProvider implements ITreeContentProvider {
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose() {
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
         */
        public Object[] getChildren(Object parentElement) {
            if(parentElement instanceof IOrganiserContainer) {
                List<IOrganiserObject> list = ((IOrganiserContainer)parentElement).getChildren();
                
                if(fFilter != null) {
                    return getFilteredList(list).toArray();
                }
                else {
                    return list.toArray();
                }
            }
            
            return new Object[0];
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
         */
        public Object getParent(Object element) {
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
         */
        public boolean hasChildren(Object element) {
            if(element instanceof IOrganiserContainer) {
                List<IOrganiserObject> list = ((IOrganiserContainer)element).getChildren();

                if(fFilter != null) {
                    return !getFilteredList(list).isEmpty();
                }
                else {
                    return !list.isEmpty();
                }
            }
            
            return false;
        }
        
        private List<IOrganiserObject> getFilteredList(List<IOrganiserObject> list) {
            List<IOrganiserObject> newList = new ArrayList<IOrganiserObject>();
            
            for(IOrganiserObject organiserObject : list) {
                for(Class<?> filterObject : fFilter) {
                    if(filterObject.isInstance(organiserObject)) {
                        newList.add(organiserObject);
                    }
                }
            }
            
            return newList;
        }
    }
    
    /**
     * View Label Provider
     */
    private class OrganiserViewLabelProvider extends LabelProvider {

        public OrganiserViewLabelProvider() {
        }
        
        @Override
        public String getText(Object element) {
            return ((IOrganiserObject)element).getName();
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
         */
        @Override
        public Image getImage(Object obj) {
            return OrganiserImageFactory.getInstance().getImage((IOrganiserObject)obj);
        }
    }
}
