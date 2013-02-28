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

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TreeItem;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.activities.IActivityRefModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureRefModel;
import org.tencompetence.imsldmodel.activities.IActivityType;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;



/**
 * Activities Tree DragDropHandler
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActivitiesTreeViewerDragDropHandler.java,v 1.12 2009/05/19 18:21:04 phillipus Exp $
 */
public class ActivitiesTreeViewerDragDropHandler {

    private ILDModel fLDModel;
    private StructuredViewer fViewer;
    
    private int fDragOperations = DND.DROP_COPY | DND.DROP_MOVE; 

    private Transfer[] fTransferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
    
    public ActivitiesTreeViewerDragDropHandler(StructuredViewer viewer) {
        fViewer = viewer;
        
        registerDragSupport();
        registerDropSupport();
    }
    
    public void setLDModel(ILDModel ldModel) {
        fLDModel = ldModel;
    }

    private void registerDragSupport() {
        fViewer.addDragSupport(fDragOperations, fTransferTypes, new DragSourceListener() {
            
            public void dragFinished(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(null);
            }

            public void dragSetData(DragSourceEvent event) {
                // For consistency set the data to the selection even though
                // the selection is provided by the LocalSelectionTransfer
                // to the drop target adapter.
                event.data = LocalSelectionTransfer.getTransfer().getSelection();
            }

            public void dragStart(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(fViewer.getSelection());
                event.doit = true;
            }
            
        });
    }
    
    private void registerDropSupport() {
        fViewer.addDropSupport(fDragOperations, fTransferTypes, new DropTargetListener() {
            int operations = DND.DROP_NONE;
            
            public void dragEnter(DropTargetEvent event) {
                operations = isValidSelection() ? event.detail : DND.DROP_NONE;
            }

            public void dragLeave(DropTargetEvent event) {
            }

            public void dragOperationChanged(DropTargetEvent event) {
                operations = isValidSelection() ? event.detail : DND.DROP_NONE;
            }

            public void dragOver(DropTargetEvent event) {
                event.detail = isValidDropTarget(event) ? operations : DND.DROP_NONE;
                
                if(operations == DND.DROP_NONE) {
                    event.feedback = DND.FEEDBACK_NONE;
                }
                else {
                    event.feedback = getFeedbackType(event);
                    event.feedback |= DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
                }
            }

            public void drop(DropTargetEvent event) {
                if(!LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)){
                    return;
                }
                
                if((event.detail == DND.DROP_COPY)) {
                    doDropOperation(event);
                }
                else if((event.detail == DND.DROP_MOVE)) {
                    doDropOperation(event);
                }
            }

            public void dropAccept(DropTargetEvent event) {
            }
            
        });
    }

    private void doDropOperation(DropTargetEvent event) {
        boolean move = event.detail == DND.DROP_MOVE;
        
        // Find Drop Target
        Object parent = getTargetParent(event);
        if(parent == null) {
            return;
        }
        
        // Get new Index position first
        int index = getTargetPosition(event);
        
        /*
         * Parent will be either the top-level LearningActivities, SupportActivities, ActivityStructures
         * or an ActivityStructure Ref.
         */
        
        // Objects to move/copy
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object o : selection.toArray()) {
            ILDModelObject ldObject = (ILDModelObject)o;
            
            if(canDropItem(ldObject, event)) {
                // Move/Copy Activity Ref in AS
                if(ldObject instanceof IActivityRefModel && parent instanceof IActivityStructureModel) {
                    moveActivityRef(ldObject, (IActivityStructureModel)parent, index, move);
                }
                
                // Move/Copy top Activity to AS - creates Activity Ref within AS
                else if(ldObject instanceof IActivityType && parent instanceof IActivityStructureModel) {
                    createActivityRef(ldObject, (IActivityStructureModel)parent, index);
                }
            }
        }
        
        fLDModel.setDirty();
    }
    
    /**
     * Create Activity Ref in AS
     * @param ldObject
     * @param newParent
     * @param index
     */
    private void createActivityRef(ILDModelObject ldObject, IActivityStructureModel newParent, int index) {
        if(newParent.containsActivity(ldObject)) {
            return;
        }
        
        if(newParent.equals(ldObject)) {
            return;
        }
        
        // Add
        IActivityRefModel ref = newParent.addActivity(ldObject, index);
        if(ref != null) {
            fViewer.setSelection(new StructuredSelection(ref));
        }
    }
    
    /**
     * Move/Copy an Activity Ref to same or new parent
     * 
     * @param ldObject
     * @param newParent
     * @param index
     */
    private void moveActivityRef(ILDModelObject ldObject, IActivityStructureModel newParent, int index, boolean move) {
        IActivityStructureModel oldParent = (IActivityStructureModel)((IActivityRefModel)ldObject).getParent();
        
        // If moving/copying to different parent, check if it already exists
        if(oldParent != newParent && newParent.containsActivity(ldObject)) {
            return;
        }
        
        // Don't move/copy AS to self
        if(ldObject instanceof IActivityStructureRefModel && newParent.equals(((ILDModelObjectReference)ldObject).getLDModelObject())) {
            return;
        }
        
        // Don't copy to same parent
        if(!move && oldParent == newParent) {
            return;
        }

        // Remove
        if(move) {
            oldParent.removeActivity(ldObject);
        }
        
        // Add
        IActivityRefModel ref = newParent.addActivity(ldObject, index);
        if(ref != null) {
            fViewer.setSelection(new StructuredSelection(ref));
        }
    }
    
    /**
     * Determine the feedback type for dropping
     * 
     * @param event
     * @return
     */
    private int getFeedbackType(DropTargetEvent event) {
        if(event.item == null) {
            return DND.FEEDBACK_NONE;
        }
        
        Rectangle rect = ((TreeItem)event.item).getBounds();
        Point pt = fViewer.getControl().toControl(event.x, event.y);
        if(pt.y < rect.y + 3) {
            return DND.FEEDBACK_INSERT_BEFORE;
        }
        if(pt.y > rect.y + rect.height - 3) {
            return DND.FEEDBACK_INSERT_AFTER;
        }
        
        return DND.FEEDBACK_SELECT;
    }
    
    /**
     * Determine the target parent from the drop event
     * 
     * @param event
     * @return
     */
    private Object getTargetParent(DropTargetEvent event) {
        if(event.item == null) {
            return null;
        }
        
        Object ldObjectDroppedOn = event.item.getData();
        Object parent = null;
        
        // LA
        if(ldObjectDroppedOn instanceof ILearningActivityModel) {
            parent = fLDModel.getActivitiesModel().getLearningActivitiesModel();
        }
        // SA
        else if(ldObjectDroppedOn instanceof ISupportActivityModel) {
            parent = fLDModel.getActivitiesModel().getSupportActivitiesModel();
        }
        // AS
        else if(ldObjectDroppedOn instanceof IActivityStructureModel) {
            parent = ldObjectDroppedOn;
        }
        // AS Ref
        else if(ldObjectDroppedOn instanceof IActivityStructureRefModel) {
            // If dropped in-between nodes get parent
            int feedback = getFeedbackType(event);
            if(feedback != DND.FEEDBACK_SELECT) {
                parent = ((IActivityStructureRefModel)ldObjectDroppedOn).getParent();
            }
            else {
                parent = ((IActivityStructureRefModel)ldObjectDroppedOn).getLDModelObject();
            }
        }
        // LA or SA Ref
        else if(ldObjectDroppedOn instanceof IActivityRefModel) {
            parent = ((IActivityRefModel)ldObjectDroppedOn).getParent();
        }
        
        if(parent == null) {
            System.err.println("Parent was null in ActivitiesTreeViewerDragDropHandler.getTargetParent()"); //$NON-NLS-1$
        }
        
        return parent;
    }

    
    /**
     * Determine the target index position from the drop event
     * 
     * @param event
     * @return
     */
    private int getTargetPosition(DropTargetEvent event) {
        if(event.item == null) {
            return 0;
        }
        
        ILDModelObject ldObjectDroppedOn = (ILDModelObject)event.item.getData();
        Object parent = getTargetParent(event);
        
        if(parent instanceof IActivityStructureModel) {
            if(parent == ldObjectDroppedOn) {
                return ((IActivityStructureModel)parent).getActivityRefs().size();
            }
            return ((IActivityStructureModel)parent).getActivityRefs().indexOf(ldObjectDroppedOn);
        }
        
        else if(parent instanceof ILDModelObjectContainer) {
            return ((ILDModelObjectContainer)parent).getChildren().indexOf(ldObjectDroppedOn);
        }
        
        // Else
        return 0;
    }

    /**
     * Figure out if we can move an Item
     */
    private boolean canDropItem(ILDModelObject object, DropTargetEvent event) {
        if(event.item == null) {
            return false;
        }
        
        Object parent = getTargetParent(event);
        Object ldObjectDroppedOn = event.item.getData();
        
        // Cannot drop onto itself
        if(object == parent) {  
            return false;
        }
        
        // Check that target is not a descendant AS of the source AS
        // TODO - check also child branches
        if((object instanceof IActivityStructureRefModel || object instanceof IActivityStructureModel)
                && ldObjectDroppedOn instanceof IActivityStructureRefModel) {
            
            TreeItem treeItem = ((TreeItem)event.item).getParentItem();
            while(treeItem != null) {
                if(object.equals(treeItem.getData())) {
                    return false;
                }
                treeItem = treeItem.getParentItem();
            }
        }
        
        return true;
    }
    
    /**
     * @return True if target is valid
     */
    private boolean isValidDropTarget(DropTargetEvent event) {
        //return event.item != null;
        
        Object parent = getTargetParent(event);
        
        // Cannot re-arrange top-level Activities
        if(parent instanceof ILDModelObjectContainer) {
            return false;
        }
        
        return true;
    }

    /**
     * @return True if the selection is full of valid objects
     */
    private boolean isValidSelection() {
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object object : selection.toList()) {
            if(!isSupportedType(object)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Return true if this object is supported for Drop support
     * 
     * @param object
     * @return
     */
    private boolean isSupportedType(Object object) {
        return (object instanceof IActivityType) || (object instanceof IActivityRefModel);
    }
}
