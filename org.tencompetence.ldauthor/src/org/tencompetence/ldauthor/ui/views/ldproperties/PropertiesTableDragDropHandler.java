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
import org.tencompetence.imsldmodel.properties.IPropertyGroupModel;
import org.tencompetence.imsldmodel.properties.IPropertyGroupRefModel;
import org.tencompetence.imsldmodel.properties.IPropertyRefModel;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;


/**
 * PropertiesTableDragDropHandler
 * 
 * @author Phillip Beauvoir
 * @version $Id: PropertiesTableDragDropHandler.java,v 1.4 2009/05/19 18:21:04 phillipus Exp $
 */
public class PropertiesTableDragDropHandler {

    private ILDModel fLDModel;
    private StructuredViewer fViewer;
    
    private int fDragOperations = DND.DROP_COPY | DND.DROP_MOVE; 

    private Transfer[] fTransferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
    
    public PropertiesTableDragDropHandler(StructuredViewer viewer) {
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

        // Objects to move/copy
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object o : selection.toArray()) {
            ILDModelObject ldObject = (ILDModelObject)o;
            
            if(canDropItem(ldObject, event)) {
                // Move a top level Property
                if(ldObject instanceof IPropertyTypeModel && parent == fLDModel.getPropertiesModel()) {
                    moveProperty(ldObject, index);
                }
                // Move a top level Group
                else if(ldObject instanceof IPropertyGroupModel && parent == fLDModel.getPropertiesModel()) {
                    moveProperty(ldObject, index);
                }
                // Create a Property Ref
                else if(ldObject instanceof IPropertyTypeModel && parent instanceof IPropertyGroupModel) {
                    createPropertyRef(ldObject, (IPropertyGroupModel)parent, index);
                }
                // Create a Property Group Ref
                else if(ldObject instanceof IPropertyGroupModel && parent instanceof IPropertyGroupModel) {
                    createPropertyRef(ldObject, (IPropertyGroupModel)parent, index);
                }
                // Move/Copy a Property Ref
                else if(ldObject instanceof IPropertyRefModel && parent instanceof IPropertyGroupModel) {
                    movePropertyRef((IPropertyRefModel)ldObject, (IPropertyGroupModel)parent, index, move);
                }
            }
        }
        
        fLDModel.setDirty();
    }
    
    /**
     * Move a top level Property or Group
     * @param property
     * @param index
     */
    private void moveProperty(ILDModelObject ldObject, int index) {
        // IF moving property AND new index > old index THEN index--
        int oldIndex = fLDModel.getPropertiesModel().getChildren().indexOf(ldObject);
        if(index > oldIndex) {
            index--;
        }
        
        fLDModel.getPropertiesModel().moveChild(ldObject, index);
        fViewer.setSelection(new StructuredSelection(ldObject));
    }
    
    /**
     * Move/Copy a Property Ref to same or new parent
     * @param property
     * @param parent
     * @param index
     * @param move
     */
    private void movePropertyRef(IPropertyRefModel propertyRef, IPropertyGroupModel newParent, int index, boolean move) {
        IPropertyGroupModel oldParent = (IPropertyGroupModel)((IPropertyRefModel)propertyRef).getParent();
        
        // If moving/copying to different parent, check if it already exists
        if(oldParent != newParent && newParent.containsProperty(propertyRef)) {
            return;
        }
        
        // Don't move/copy Group to self
        if(propertyRef instanceof IPropertyGroupRefModel && newParent.equals(((ILDModelObjectReference)propertyRef).getLDModelObject())) {
            return;
        }
        
        // Don't copy to same parent
        if(!move && oldParent == newParent) {
            return;
        }

        // Remove
        if(move) {
            oldParent.removeProperty(propertyRef);
        }
        
        // Add
        IPropertyRefModel ref = newParent.addProperty(propertyRef, index);
        if(ref != null) {
            fViewer.setSelection(new StructuredSelection(ref));
        }
    }
    
    private void createPropertyRef(ILDModelObject ldObject, IPropertyGroupModel newParent, int index) {
        if(newParent.containsProperty(ldObject)) {
            return;
        }
        
        if(newParent.equals(ldObject)) {
            return;
        }
        
        // Add
        IPropertyRefModel ref = newParent.addProperty(ldObject, index);
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
            return fLDModel.getPropertiesModel();
        }
        
        Object ldObjectDroppedOn = event.item.getData();
        Object parent = null; // default
        
        if(ldObjectDroppedOn instanceof IPropertyTypeModel) {
            parent = fLDModel.getPropertiesModel();
        }
        
        else if(ldObjectDroppedOn instanceof IPropertyGroupModel) {
            // If dropped in-between nodes get parent
            int feedback = getFeedbackType(event);
            if(feedback != DND.FEEDBACK_SELECT) {
                parent = fLDModel.getPropertiesModel();
            }
            else {
                parent = ldObjectDroppedOn;
            }
        }
        
        else if(ldObjectDroppedOn instanceof IPropertyRefModel) {
            if(ldObjectDroppedOn instanceof IPropertyGroupRefModel) {
                // If dropped in-between nodes get parent
                int feedback = getFeedbackType(event);
                if(feedback != DND.FEEDBACK_SELECT) {
                    parent = ((IPropertyRefModel)ldObjectDroppedOn).getParent();
                }
                else {
                    parent = ((IPropertyRefModel)ldObjectDroppedOn).getLDModelObject();
                }
            }
            else {
                parent = ((IPropertyRefModel)ldObjectDroppedOn).getParent();
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
        // Dropped on blank area so use the parent 
        if(event.item == null) {
            return fLDModel.getPropertiesModel().getChildren().size();
        }
        
        ILDModelObject ldObjectDroppedOn = (ILDModelObject)event.item.getData();
        Object parent = getTargetParent(event);
        
        if(parent instanceof IPropertyGroupModel) {
            if(parent == ldObjectDroppedOn) {
                return ((IPropertyGroupModel)parent).getPropertyRefs().size();
            }
            return ((IPropertyGroupModel)parent).getPropertyRefs().indexOf(ldObjectDroppedOn);
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
            return true;
        }
        
        Object parent = getTargetParent(event);
        Object ldObjectDroppedOn = event.item.getData();
        
        // Cannot drop onto itself
        if(object == parent) {  
            return false;
        }
        
        // Check that target is not a descendant Group of the source Group
        // TODO - check also child branches
        if((object instanceof IPropertyGroupModel || object instanceof IPropertyGroupRefModel)
                && ldObjectDroppedOn instanceof IPropertyGroupRefModel) {
            
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
        return (object instanceof IPropertyTypeModel) || (object instanceof IPropertyRefModel) || (object instanceof IPropertyGroupModel);
    }
}
