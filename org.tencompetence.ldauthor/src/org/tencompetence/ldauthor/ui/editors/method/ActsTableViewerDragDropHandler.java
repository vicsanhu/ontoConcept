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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.method.operations.MoveActOperation;



/**
 * ActsTableViewerDragDropHandler
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActsTableViewerDragDropHandler.java,v 1.8 2009/05/19 18:21:01 phillipus Exp $
 */
public class ActsTableViewerDragDropHandler {

    private ILDEditorPart fEditor;
    private StructuredViewer fViewer;
    
    private int fDragOperations = DND.DROP_MOVE; 

    private Transfer[] fTransferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
    
    public ActsTableViewerDragDropHandler(ILDEditorPart editor, StructuredViewer viewer) {
        fEditor = editor;
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
                event.detail = getDropTarget(event) != null ? operations : DND.DROP_NONE;
                
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
                    doMoveOperation(event);
                }
                else if((event.detail == DND.DROP_MOVE)) {
                    doMoveOperation(event);
                }
            }

            public void dropAccept(DropTargetEvent event) {
            }
            
        });
    }

    private void doMoveOperation(DropTargetEvent event) {
        // Source Act
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        IActModel dragSourceAct = (IActModel)selection.getFirstElement();
        
        // Find Drop Target
        Object dropTarget = getDropTarget(event);
        
        // Same source and target, so leave it
        if(dragSourceAct == dropTarget) {
            return;
        }
        
        int index = 0;
        ILDModelObjectContainer target = null;
        
        // Dropped on an Act
        if(dropTarget instanceof IActModel) {
            IActModel dropTargetAct = (IActModel)dropTarget;
            target = dropTargetAct.getParent();
            index = target.getChildren().indexOf(dropTargetAct);
        }
        // Dropped on Play - add it to end of ActsModel
        else if(dropTarget instanceof IPlayModel) {
            target = ((IPlayModel)dropTarget).getActsModel();
            index = target.getChildren().size();
        }
        
        try {
            getOperationHistory().execute(
                    new MoveActOperation(fEditor, dragSourceAct, target, index),
                    null,
                    null);
        }
        catch(ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the Drop target
     */
    private Object getDropTarget(DropTargetEvent event) {
        // If event.item is null then it's not dropped on an Act, so must be dropped on Play
        if(event.item == null) {
            return fViewer.getInput();  // This is the target Play we dropped on
        }
        
        // Else we dropped on an Act
        else {
            return event.item.getData();
        }
    }

    private boolean isValidSelection() {
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        Object object = selection.getFirstElement();
        return object instanceof IActModel;
    }

    /**
     * Determine the feedback type for dropping
     * 
     * @param event
     * @return
     */
    private int getFeedbackType(DropTargetEvent event) {
        return DND.FEEDBACK_SELECT;
        //return DND.FEEDBACK_NONE;
    }

    /*
     * Get the undo/redo operation history from the workbench.
     */
    private IOperationHistory getOperationHistory() {
        return PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
    }

}
