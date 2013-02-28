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
package org.tencompetence.ldauthor.ui.views.organiser.activities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.activities.IActivitiesModel;
import org.tencompetence.imsldmodel.activities.IActivityRefModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureRefModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityRefModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityRefModel;
import org.tencompetence.ldauthor.qti.model.QTIUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Tree for Activities
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActivitiesTreeViewer.java,v 1.10 2009/05/19 18:21:04 phillipus Exp $
 */
public class ActivitiesTreeViewer
extends TreeViewer {
    
    private boolean fShowLearningActivities = true, fShowSupportActivities = true, fShowActivityStructures = true;
    
    /**
     * Constructor
     * @param parent
     * @param style
     */
    public ActivitiesTreeViewer(Composite parent, int style) {
        super(parent, style);
        
        setContentProvider(new LDTreeContentProvider());
        setLabelProvider(new LDTreeLabelProvider());
        
        /*
         * Sort top level activities alphabetically.  Activity Refs in Activity Structures maintain their own order
         */
        setSorter(new ViewerSorter() {
            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                if(e1 instanceof ILDModelObjectReference) {
                    return 0;
                }
                return super.compare(viewer, e1, e2);
            }
            
            @Override
            public int category(Object element) {
                if(element instanceof ILearningActivityModel) {
                    return 0;
                }
                if(element instanceof ISupportActivityModel) {
                    return 1;
                }
                if(element instanceof IActivityStructureModel) {
                    return 2;
                }
                return 0;
            }
        });
    }
    
    public void showLearningActivities(boolean show) {
        fShowLearningActivities = show;
        refresh();
    }
    
    public void showSupportActivities(boolean show) {
        fShowSupportActivities = show;
        refresh();
    }
    
    public void showActivityStructures(boolean show) {
        fShowActivityStructures = show;
        refresh();
    }
    
    /**
     * @return The currently selected ActivityStructure.
     *         If the selected node is an Activity Reference then return itself.
     *         Return null if there isn't an ActivityStructure selected or it doesn't have one as a parent.
     */
    public IActivityStructureModel getSelectedActivityStructure() {
        Object o = ((IStructuredSelection)getSelection()).getFirstElement();
        
        // Child other ref, find parent
        if((o instanceof ILearningActivityRefModel) || (o instanceof ISupportActivityRefModel)) {
            return (IActivityStructureModel)((IActivityRefModel)o).getParent();
        }

        // AS Ref
        if(o instanceof IActivityStructureRefModel) {
            return (IActivityStructureModel)((IActivityStructureRefModel)o).getLDModelObject();
        }
        
        // AS
        if(o instanceof IActivityStructureModel) {
            return (IActivityStructureModel)o;
        }
        
        return null;
    }

    
    /**
     * View Content Provider
     */
    private class LDTreeContentProvider implements ITreeContentProvider {
        
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
            if(parentElement instanceof ILDModel) {
                List<ILDModelObject> fullList = new ArrayList<ILDModelObject>();
                
                IActivitiesModel activities = ((ILDModel)parentElement).getActivitiesModel();
                
                if(fShowLearningActivities) {
                    fullList.addAll(activities.getLearningActivitiesModel().getChildren());
                }
                if(fShowSupportActivities) {
                    fullList.addAll(activities.getSupportActivitiesModel().getChildren());
                }
                if(fShowActivityStructures) {
                    fullList.addAll(activities.getActivityStructuresModel().getChildren());
                }
                
                return fullList.toArray();
            }
            
            if(parentElement instanceof ILDModelObjectReference) {
                parentElement = ((ILDModelObjectReference)parentElement).getLDModelObject();
            }
            
            if(parentElement instanceof IActivityStructureModel) {
                return ((IActivityStructureModel)parentElement).getActivityRefs().toArray();
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
            if(element instanceof ILDModelObjectReference) {
                element = ((ILDModelObjectReference)element).getLDModelObject();
            }
            
            if(element instanceof IActivityStructureModel) {
                return ((IActivityStructureModel)element).getActivityRefs().size() > 0;
            }
            
            return false;
        }
    }
    
    /**
     * View Label Provider
     */
    private class LDTreeLabelProvider extends LabelProvider {

        public LDTreeLabelProvider() {
        }
        
        @Override
        public String getText(Object element) {
            if(element instanceof ITitle) {
                return ((ITitle)element).getTitle();
            }
            return element.toString();
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
         */
        @Override
        public Image getImage(Object obj) {
            if(obj instanceof ILDModelObjectReference) {
                obj = ((ILDModelObjectReference)obj).getLDModelObject();
            }
            
            if(obj instanceof ILearningActivityModel) {
                // If activity has Item that points to QTI Resource
                ILearningActivityModel activity = (ILearningActivityModel)obj;
                if(QTIUtils.isQTITestActivity(activity)) {
                    return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_TEST_OBJECT_16);
                }
                return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_ACTIVITY_16);
            }
            if(obj instanceof ISupportActivityModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_SUPPORT_ACTIVITY_16);
            }
            if(obj instanceof IActivityStructureModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_ACTIVITY_STRUCTURE_16);
            }

            return null;
        }
    }
}
