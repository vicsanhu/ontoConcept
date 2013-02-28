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
package org.tencompetence.ldauthor.ui.views.inspector.item;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;



/**
 * DragDropHandler for Item Type Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemTreeTableDragDropHandler.java,v 1.8 2009/06/15 12:58:41 phillipus Exp $
 */
public class ItemTreeTableDragDropHandler {
    
    private int fDragOperations = DND.DROP_COPY | DND.DROP_MOVE; 

    private Transfer[] fTransferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };

    private ItemModelTreeTable fViewer;
    
    public ItemTreeTableDragDropHandler(ItemModelTreeTable viewer) {
        fViewer = viewer;
        registerDragSupport();
        registerDropSupport();
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
                    Object type = getSelectionType();
                    if(type instanceof IItemType) {
                        doItemCopyOperation(event);
                    }
                    else if(type instanceof IResourceModel) {
                        doResourceCopyOperation(event);
                    }
                }
                else if((event.detail == DND.DROP_MOVE)) {
                    Object type = getSelectionType();
                    if(type instanceof IItemType) {
                        doItemMoveOperation(event);
                    }
                    else if(type instanceof IResourceModel) {
                        doResourceCopyOperation(event);
                    }
                }
            }

            public void dropAccept(DropTargetEvent event) {
            }
            
        });
    }
    
    /**
     * Copy Items
     * @param event
     */
    private void doItemCopyOperation(DropTargetEvent event) {
        // Copying is not supported yet
        doItemMoveOperation(event);
    }

    /**
     * Move Items
     * @param event
     */
    private void doItemMoveOperation(DropTargetEvent event) {
        IItemTypeContainer parent = getTargetParent(event);
        if(parent == null) {
            return;
        }
        
        /*
         * Don't allow if parent has maximum children allowed
         */
        if(!parent.canAddChildItem()) {
            return;
        }
        
        int index = getTargetPosition(event);

        // Gather together the Items that we can move
        List<IItemType> list = new ArrayList<IItemType>();
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object object : selection.toList()) {
            IItemType item = (IItemType)object;
            if(canMoveItem(item, parent)) {
                list.add(item);
            }
        }
        
        if(list.isEmpty()) {
            return;
        }
        
        // Move Items
        for(int i = 0; i < list.size(); i++) {
            IItemType item = list.get(i);
            int newIndex = index;
            // IF moving item to same parent AND new parent index > old parent index THEN index--
            if(item.getParent() == parent) {
                int oldIndex = item.getParent().getItemTypes().indexOf(item);
                if(newIndex > oldIndex) {
                    newIndex--;
                }
            }
            item.getParent().getItemTypes().remove(item);
            parent.getItemTypes().add(newIndex, item);
            item.setParent(parent);
        }
        
        ILDModel ldModel = fViewer.getItemModel().getLDModel();
        ldModel.setDirty();
        fViewer.refresh();
    }
    
    /**
     * Copy Resources to become Items
     * @param event
     */
    private void doResourceCopyOperation(DropTargetEvent event) {
        IItemTypeContainer parent = fViewer.getItemModel();
        ILDModel ldModel = parent.getLDModel();
        
        /*
         * Make Items out of Resources.
         * Ensure that the LD Models are the same (no copying from other LD Editor instances)
         */
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        
        if(selection.isEmpty()) {
            return;
        }
        
        boolean dirty = false;
        
        for(Object object : selection.toList()) {
            if(object instanceof IResourceModel) {
                IResourceModel resource = (IResourceModel)object;
                if(resource.getLDModel() == ldModel) {
                    if(!parent.canAddChildItem()) {
                        break;
                    }
                    IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, ldModel);
                    itemType.setTitle(resource.getHref());
                    itemType.setIdentifierRef(resource.getIdentifier());
                    parent.addChildItem(itemType);
                    dirty = true;
                }
            }
        }

        if(dirty) {
            ldModel.setDirty();
            fViewer.refresh();
        }
    }
    
    /**
     * Determine the target index position from the drop event
     * 
     * @param event
     * @return
     */
    private int getTargetPosition(DropTargetEvent event) {
        // Dropped on blank area so use the parent IItemTypeOwner container
        if(event.item == null) {
            return fViewer.getItemModel().getItemTypes().size();
        }
        
        IItemType dropItem = (IItemType)event.item.getData();
        
        // Dropped in-between nodes
        int feedback = getFeedbackType(event);
        if(feedback == DND.FEEDBACK_INSERT_BEFORE) {
            return dropItem.getParent().getItemTypes().indexOf(dropItem);
        }
        if(feedback == DND.FEEDBACK_INSERT_AFTER) {
            return dropItem.getParent().getItemTypes().indexOf(dropItem) + 1;
        }
        
        // Dropped on node
        return dropItem.getItemTypes().size();
    }

    /**
     * Determine the target parent from the drop event
     * 
     * @param event
     * @return
     */
    private IItemTypeContainer getTargetParent(DropTargetEvent event) {
        // Dropped on blank area so use the parent IItemTypeOwner container
        if(event.item == null) {
            return fViewer.getItemModel();
        }
        
        IItemType dropItem = (IItemType)event.item.getData();
        
        // If dropped in-between nodes get parent
        int feedback = getFeedbackType(event);
        if(feedback != DND.FEEDBACK_SELECT) {
            return dropItem.getParent();
        }
        
        return dropItem;
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
     * @return True if target is valid
     */
    private boolean isValidDropTarget(DropTargetEvent event) {
        IItemTypeContainer parent = getTargetParent(event);
        if(parent != null) {
            return parent.canAddChildItem();
        }
        
        return true;
    }
    
    /**
     * Figure out if we can move an Item
     * 
     * @param object
     * @param targetTreeItem
     * @return
     */
    private boolean canMoveItem(IItemType item, IItemTypeContainer parent) {
        if(item == parent) {  // Cannot drop onto itself
            return false;
        }
        
        // Check that target is not a descendant of the source
        while(parent instanceof IItemType && (parent = ((IItemType)parent).getParent()) != null) {
            if(parent == item) {
                return false;
            }
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
        return (object instanceof IItemType) || (object instanceof IResourceModel);
    }
    
    /**
     * @return The Class of objects to be dragged
     */
    private Object getSelectionType() {
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        return selection.getFirstElement();
    }
}
