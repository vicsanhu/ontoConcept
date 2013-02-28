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
package org.tencompetence.ldauthor.ui.editors.method;

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
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityRefModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureRefModel;
import org.tencompetence.imsldmodel.activities.IActivityType;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.method.IRolePartsModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.method.RolePartsTreeModelAdapter.ASChildTreeItem;
import org.tencompetence.ldauthor.ui.editors.method.RolePartsTreeModelAdapter.FoldedRolePart;



/**
 * RolePartsTableViewerDragDropHandler
 * 
 * @author Phillip Beauvoir
 * @version $Id: RolePartsTreeTableViewerDragDropHandler.java,v 1.15 2009/10/30 18:00:10 phillipus Exp $
 */
public class RolePartsTreeTableViewerDragDropHandler {

    private ILDEditorPart fEditor;
    private StructuredViewer fViewer;
    private RolePartsTreeModelAdapter fAdapter;
    
    private int fDragOperations = DND.DROP_COPY | DND.DROP_MOVE; 

    private Transfer[] fTransferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
    
    public RolePartsTreeTableViewerDragDropHandler(ILDEditorPart editor, StructuredViewer viewer) {
        fEditor = editor;
        fViewer = viewer;
        
        registerDragSupport();
        registerDropSupport();
    }
    
    public void setAct(RolePartsTreeModelAdapter adapter) {
        fAdapter = adapter;
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
                operations = isValidDragSelection() ? event.detail : DND.DROP_NONE;
            }

            public void dragLeave(DropTargetEvent event) {
            }

            public void dragOperationChanged(DropTargetEvent event) {
                operations = isValidDragSelection() ? event.detail : DND.DROP_NONE;
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

    /**
     * Perform the drop operation
     * @param event
     */
    private void doDropOperation(DropTargetEvent event) {
        if(fAdapter == null || fAdapter.getAct() == null) {
            return;
        }
        
        boolean move = event.detail == DND.DROP_MOVE;
        
        // Find Drop Target
        Object parent = getTargetParent(event);
        if(parent == null) {
            return;
        }
        
        // Get new Index position first
        int index = getTargetPosition(event);

        /*
         * Parent will be either the top-level RoleParts Model or an ActivityStructure Ref.
         */

        // Objects to move/copy
        // If it is a LA, SA, AS, Env or Activity Ref then it was dragged from elsewhere (the Organiser)
        // If it is an ASChildTreeItem then it is an internal DnD operation
        // If it is a FoldedRolePart we are re-ordering them
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object obj : selection.toArray()) {
            // Role Parts Model is parent
            if(parent instanceof IRolePartsModel) {
                if(obj instanceof ASChildTreeItem) {
                    IActivityRefModel ldRef = (IActivityRefModel)((ASChildTreeItem)obj).getComponentRef();
                    boolean result = addNewRolePart((ILDModelObject)ldRef);
                    if(move && result) {
                        ldRef.getParent().removeActivity(ldRef);
                    }
                }
                
                if(obj instanceof IActivityType || obj instanceof IActivityRefModel || obj instanceof IEnvironmentModel) {
                    addNewRolePart((ILDModelObject)obj);
                }
                
                if(obj instanceof FoldedRolePart && canDropItem(obj, event)) {
                    moveRolePart((FoldedRolePart)obj, index);
                }
            }
            
            // AS Parent
            else if(parent instanceof IActivityStructureModel) {
                IActivityStructureModel as = (IActivityStructureModel)parent;
                
                // Move/Copy AS Child Item internally
                if(obj instanceof ASChildTreeItem) {
                    IActivityRefModel activityRef = (IActivityRefModel)((ASChildTreeItem)obj).getComponentRef();
                    if(canDropItem(activityRef, event)) {
                        moveActivityRef(activityRef, as, index, move);
                    }
                }
                
                // Move/Copy Folded Role Part member to AS (not Environment)
                else if(obj instanceof FoldedRolePart) {
                    FoldedRolePart foldedRolePart = (FoldedRolePart)obj;
                    ILDModelObject component = foldedRolePart.getComponent();
                    if(component != as && component instanceof IActivityType && !as.containsActivity(component)) {
                        as.addActivity(component, index);
                        if(move) {
                            fAdapter.deleteFoldedRolePart(foldedRolePart);
                        }
                    }
                    fViewer.refresh();
                }
                
                // Dragged Activity or ref from Organiser to AS - copy and create new Activity Ref
                else if(obj instanceof IActivityType || obj instanceof IActivityRefModel) {
                    createActivityRef((ILDModelObject)obj, (IActivityStructureModel)parent, index);
                }
            }
        }

        fEditor.setDirty(true);
    }
    
    /**
     * Move a Folded Role Part
     * @param foldedRolePart
     * @param index
     */
    private void moveRolePart(FoldedRolePart foldedRolePart, int index) {
        fAdapter.moveFoldedRolePart(foldedRolePart, index);
        fViewer.refresh();
    }

    /**
     * Add a new Role Part
     * @param obj
     */
    private boolean addNewRolePart(ILDModelObject obj) {
        if(fAdapter == null || fAdapter.getAct() == null) {
            return false;
        }
        
        IActModel act = fAdapter.getAct();

        IRolePartsModel rolePartsModel = act.getRolePartsModel();
        
        // Default Role
        IRoleModel role = null;
        
        // Use Staff Role if Support Activity
        if(obj instanceof ISupportActivityModel) {
            //This might return null...
            role = act.getLDModel().getRolesModel().getDefaultStaffRole();
        }
        // OK, try default learner
        if(role == null) {
            role = act.getLDModel().getRolesModel().getDefaultLearnerRole();
        }
        // Last resort
        if(role == null) {
            role = act.getLDModel().getRolesModel().getDefaultStaffRole();
        }
        // Forget it...
        if(role == null) {
            return false;
        }

        // No duplicates
        if(rolePartsModel.contains(role, obj)) {
            return false;
        }
        
        IRolePartModel rolePart = (IRolePartModel)LDModelFactory.createModelObject(LDModelFactory.ROLE_PART, act.getLDModel());
        
        if(obj instanceof ILDModelObjectReference) {
            obj = ((ILDModelObjectReference)obj).getLDModelObject();
        }
        
        rolePart.setComponent(obj);
        rolePart.setRole(role);
        rolePartsModel.addChild(rolePart);
        
        return true;
    }
    
    /**
     * Move/Copy an Activity Ref to same or new parent
     * 
     * @param ldObject
     * @param newParent
     * @param index
     */
    private void moveActivityRef(IActivityRefModel ldObject, IActivityStructureModel newParent, int index, boolean move) {
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
     * Determine the target parent from the drop event
     * 
     * @param event
     * @return
     */
    private Object getTargetParent(DropTargetEvent event) {
        if(fAdapter == null || fAdapter.getAct() == null) {
            return null;
        }
        
        IActModel act = fAdapter.getAct();
        
        // Dropped on blank area == RoleParts Model
        if(event.item == null) {
            return act.getRolePartsModel();
        }
        
        Object droppedOnObject = event.item.getData();
        Object parent = null;
        
        int feedback = getFeedbackType(event);

        // Folded RolePart
        if(droppedOnObject instanceof FoldedRolePart) {
            ILDModelObject ldObject = ((FoldedRolePart)droppedOnObject).getComponent();
            // If dropped directly on AS, use that
            if(ldObject instanceof IActivityStructureModel && feedback == DND.FEEDBACK_SELECT) {
                return ldObject;
            }
            // Else Role Parts Model
            else {
                return act.getRolePartsModel();
            }
        }
        // AS Child Item
        else if(droppedOnObject instanceof ASChildTreeItem) {
            ILDModelObjectReference activityRef = ((ASChildTreeItem)droppedOnObject).getComponentRef();
            // AS Ref
            if(activityRef instanceof IActivityStructureRefModel) {
                // If dropped in-between nodes get parent
                if(feedback != DND.FEEDBACK_SELECT) {
                    parent = ((IActivityStructureRefModel)activityRef).getParent();
                }
                else {
                    parent = ((IActivityStructureRefModel)activityRef).getLDModelObject();
                }
            }
            // LA or SA Ref
            else if(activityRef instanceof IActivityRefModel) {
                parent = ((IActivityRefModel)activityRef).getParent();
            }
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
        
        Object droppedOnObject = event.item.getData();
        
        int feedback = getFeedbackType(event);
        
        // Folded RolePart
        if(droppedOnObject instanceof FoldedRolePart) {
            // Determine if we dropped in-between nodes
            if(feedback == DND.FEEDBACK_INSERT_BEFORE) {
                return fAdapter.getFoldedRoleParts().indexOf(droppedOnObject);
            }
            if(feedback == DND.FEEDBACK_INSERT_AFTER) {
                return fAdapter.getFoldedRoleParts().indexOf(droppedOnObject) + 1;
            }
            
            // Else dropped on it
            droppedOnObject = ((FoldedRolePart)droppedOnObject).getComponent();
        }
        
        // AS Child Item
        else if(droppedOnObject instanceof ASChildTreeItem) {
            droppedOnObject = ((ASChildTreeItem)droppedOnObject).getComponentRef();
        }
        
        Object parent = getTargetParent(event);
        
        if(parent instanceof IActivityStructureModel) {
            if(parent == droppedOnObject) {
                return ((IActivityStructureModel)parent).getActivityRefs().size();
            }
            return ((IActivityStructureModel)parent).getActivityRefs().indexOf(droppedOnObject);
        }
        
        // Else
        return 0;
    }

    /**
     * Figure out if we can drop an Item
     */
    private boolean canDropItem(Object object, DropTargetEvent event) {
        if(event.item == null) {
            return false;
        }
        
        Object parent = getTargetParent(event);
        Object droppedOnObject = event.item.getData();
        
        // Folded RolePart
        if(droppedOnObject instanceof FoldedRolePart) {
            droppedOnObject = ((FoldedRolePart)droppedOnObject).getComponent();
        }
        // AS Child Item
        else if(droppedOnObject instanceof ASChildTreeItem) {
            droppedOnObject = ((ASChildTreeItem)droppedOnObject).getComponentRef();
        }
        
        // Cannot drop onto itself
        if(object == parent) {  
            return false;
        }
        
        // Check that target is not a descendant AS of the source AS
        // TODO - check also child branches
        if((object instanceof IActivityStructureRefModel || object instanceof IActivityStructureModel)
                && droppedOnObject instanceof IActivityStructureRefModel) {
            
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
        if(fAdapter == null || fAdapter.getAct() == null) {
            return false;
        }
        
        // Test on first object in selection
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        Object draggedObject = selection.getFirstElement();
        
        // Dropped on blank area
        if(event.item == null) {
            return true;
        }
        
        Object droppedOnObject = event.item.getData();

        int feedback = getFeedbackType(event);

        if(draggedObject instanceof FoldedRolePart) {
            draggedObject = ((FoldedRolePart)draggedObject).getComponent();
        }
        
        if(draggedObject instanceof IEnvironmentModel) {
            return (droppedOnObject instanceof FoldedRolePart) && (feedback != DND.FEEDBACK_SELECT);
        }

        if(droppedOnObject instanceof FoldedRolePart) {
            // Dragged onto FoldedRolePart - only allow IActivityStructureModel
            ILDModelObject component = ((FoldedRolePart)droppedOnObject).getComponent();
            if(!(component instanceof IActivityStructureModel)) {
                return feedback != DND.FEEDBACK_SELECT;
            }
        }

        return true;
    }
    
    /**
     * @return True if the selection is full of valid objects
     */
    private boolean isValidDragSelection() {
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object object : selection.toList()) {
            if(!isSupportedDragType(object)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Return true if object is supported for a Drag action
     * 
     * @param object
     * @return
     */
    private boolean isSupportedDragType(Object object) {
        return (object instanceof IActivityType) || (object instanceof IActivityRefModel) || (object instanceof IEnvironmentModel)
            || (object instanceof ASChildTreeItem) || (object instanceof FoldedRolePart);
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

}
