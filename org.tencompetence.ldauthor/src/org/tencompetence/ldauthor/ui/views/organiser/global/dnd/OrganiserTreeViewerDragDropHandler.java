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
package org.tencompetence.ldauthor.ui.views.organiser.global.dnd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.organisermodel.IOrganiserContainer;
import org.tencompetence.ldauthor.organisermodel.IOrganiserFolder;
import org.tencompetence.ldauthor.organisermodel.IOrganiserObject;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserIndex;
import org.tencompetence.ldauthor.ui.views.organiser.global.operations.CopyOrganiserEntriesOperation;
import org.tencompetence.ldauthor.ui.views.organiser.global.operations.CopyOrganiserEntryOperation;
import org.tencompetence.ldauthor.ui.views.organiser.global.operations.MoveOrganiserEntriesOperation;
import org.tencompetence.ldauthor.ui.views.organiser.global.operations.MoveOrganiserEntryOperation;


/**
 * Organiser Tree View Tree Drag and Drop Handler
 * 
 * @author Phillip Beauvoir
 * @version $Id: OrganiserTreeViewerDragDropHandler.java,v 1.1 2008/03/08 20:19:48 phillipus Exp $
 */
public class OrganiserTreeViewerDragDropHandler {
    
    private int fDragOperations = DND.DROP_COPY | DND.DROP_MOVE; 

    private Transfer[] fTransferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };

    private TreeViewer fViewer;
    private IUndoContext fUndoContext;

    public OrganiserTreeViewerDragDropHandler(TreeViewer viewer, IUndoContext undoContext) {
        fViewer = viewer;
        fUndoContext = undoContext;
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
                    event.feedback |= DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
                }
            }

            public void drop(DropTargetEvent event) {
                if(!LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)){
                    return;
                }
                
                // Local Operation
                if(isLocalOperation()) {
                    if((event.detail == DND.DROP_COPY)) {
                        doLocalCopyOperation(event);
                    }
                    else if((event.detail == DND.DROP_MOVE)) {
                        doLocalMoveOperation(event);
                    }
                }
                else {
                    doExternalCopyOperation(event);
                }
            }

            public void dropAccept(DropTargetEvent event) {
            }
            
        });
    }
    
    /**
     * Copy non-Organiser objects to Organiser
     * @param event
     */
    private void doExternalCopyOperation(DropTargetEvent event) {
        // Ascertain target parent container
        IOrganiserContainer targetParent = getTargetParent(event.item);
        
        List<CopyOrganiserEntryOperation> copyOperationsList = new ArrayList<CopyOrganiserEntryOperation>();
        
        // Iterate thru dropped source objects
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object object : selection.toList()) {
            // Get correct Organiser entry
            IOrganiserObject newEntry = OrganiserObjectFactory.getInstance().createOrganiserObject(object);
            if(newEntry == null) {
                continue;
            }

            // Add to list of Copy operations
            copyOperationsList.add(new CopyOrganiserEntryOperation(targetParent, newEntry, false));
        }
        
        // Execute as undoable operation
        if(copyOperationsList.size() > 0) {
            try {
                getOperationHistory().execute(
                        new CopyOrganiserEntriesOperation(fUndoContext, copyOperationsList),
                        null,
                        null);
            }
            catch(ExecutionException e) {
                e.printStackTrace();
            }
            
            // Refresh, open, and select target folder
            refreshTargetNode(targetParent);
        }
    }

    /**
     * Copy Local things
     * @param event
     */
    private void doLocalCopyOperation(DropTargetEvent event) {
        // Ascertain target parent container
        IOrganiserContainer targetParent = getTargetParent(event.item);
        
        List<CopyOrganiserEntryOperation> copyOperationsList = new ArrayList<CopyOrganiserEntryOperation>();
        
        // Iterate thru dropped source objects
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object o : selection.toList()) {
            IOrganiserObject object = (IOrganiserObject)o;

            // If can drop it and not already in target parent
            if(canDropOrganiserObject(object, (TreeItem)event.item) && !targetParent.getChildren().contains(object)) {
                // Add to list of Copy operations
                copyOperationsList.add(new CopyOrganiserEntryOperation(targetParent, object, true));
            }
        }
        
        // Execute as undoable operation
        if(copyOperationsList.size() > 0) {
            try {
                getOperationHistory().execute(
                        new CopyOrganiserEntriesOperation(fUndoContext, copyOperationsList),
                        null,
                        null);
            }
            catch(ExecutionException e) {
                e.printStackTrace();
            }
            
            // Refresh, open, and select target folder
            refreshTargetNode(targetParent);
        }
    }

    /**
     * Local Move
     * @param event
     */
    private void doLocalMoveOperation(DropTargetEvent event) {
        // Ascertain target parent container
        IOrganiserContainer targetParent = getTargetParent(event.item);

        List<MoveOrganiserEntryOperation> moveOperationsList = new ArrayList<MoveOrganiserEntryOperation>();
        
        // Iterate thru dropped source objects
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object o : selection.toList()) {
            IOrganiserObject object = (IOrganiserObject)o;

            // If can drop it and not already in target parent
            if(canDropOrganiserObject(object, (TreeItem)event.item) && !targetParent.getChildren().contains(object)) {
                // Add to list of Move operations
                moveOperationsList.add(new MoveOrganiserEntryOperation(targetParent, object));
            }
        }
        
        // Execute as undoable operation
        if(moveOperationsList.size() > 0) {
            try {
                getOperationHistory().execute(
                        new MoveOrganiserEntriesOperation(fUndoContext, moveOperationsList),
                        null,
                        null);
            }
            catch(ExecutionException e) {
                e.printStackTrace();
            }
            
            // Refresh, open, and select target folder
            refreshTargetNode(targetParent);
        }
    }
    
    /**
     * Refresh the dropped target node
     * 
     * @param targetParent
     */
    private void refreshTargetNode(IOrganiserContainer targetParent) {
        fViewer.refresh(targetParent);
        if(targetParent != OrganiserIndex.getInstance()) {
            fViewer.expandToLevel(targetParent, AbstractTreeViewer.ALL_LEVELS);
            fViewer.setSelection(new StructuredSelection(targetParent));
        }
    }
    
    /**
     * Figure out if we can drop an IOrganiserObject onto a TreeItem
     * 
     * @param object
     * @param targetTreeItem
     * @return
     */
    private boolean canDropOrganiserObject(IOrganiserObject object, TreeItem targetTreeItem) {
        if(targetTreeItem == null) {  // Root tree
            return true;
        }
        
        if(object == targetTreeItem.getData()) {  // Cannot drop onto itself
            return false;
        }
        
        // If moving a folder check that target folder is not a descendant of the source folder
        if(object instanceof IOrganiserFolder) {
            while((targetTreeItem = targetTreeItem.getParentItem()) != null) {
                if(targetTreeItem.getData() == object) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * @return True if target is valid
     */
    private boolean isValidDropTarget(DropTargetEvent event) {
        // True if the parent is a container
        return getTargetParent(event.item) != null;
    }
    
    /**
     * @return True if the selection is full of valid objects
     */
    private boolean isValidSelection() {
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object object : selection.toList()) {
            if(!OrganiserObjectFactory.getInstance().isSupportedType(object)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * @return True if this is a local copy/move operation
     */
    private boolean isLocalOperation() {
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        return selection.getFirstElement() instanceof IOrganiserObject;
    }
    
    /**
     * @param widget
     * @return The correct target parent container or null if widget is a leaf
     */
    private IOrganiserContainer getTargetParent(Widget widget) {
        if(widget instanceof TreeItem) {
            IOrganiserObject targetObject = (IOrganiserObject)widget.getData();
            if(targetObject instanceof IOrganiserContainer) {  // ok
                return (IOrganiserContainer)targetObject;
            }
            else {
                return null; // a leaf node
            }
        }
        
        return OrganiserIndex.getInstance();  // root
    }
    
    /*
     * Get the operation history from the workbench.
     */
    private IOperationHistory getOperationHistory() {
        return PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
    }

}
