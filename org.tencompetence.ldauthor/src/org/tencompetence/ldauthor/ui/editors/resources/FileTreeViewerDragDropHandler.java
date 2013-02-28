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
package org.tencompetence.ldauthor.ui.editors.resources;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.utils.FileUtils;


/**
 * File Viewer Drag and Drop Handler
 * 
 * @author Phillip Beauvoir
 * @version $Id: FileTreeViewerDragDropHandler.java,v 1.10 2009/05/19 18:21:04 phillipus Exp $
 */
public class FileTreeViewerDragDropHandler {
    
    private int fDragOperations = DND.DROP_COPY | DND.DROP_MOVE; 

    private ILDEditorPart fEditor;
    private ILDModel fLDModel;
    
    private StructuredViewer fViewer;
    
    public FileTreeViewerDragDropHandler(StructuredViewer viewer, ILDEditorPart editor) {
        fViewer = viewer;
        fEditor = editor;
        
        fLDModel = (ILDModel)fEditor.getAdapter(ILDModel.class);
        
        registerDragSupport();
        registerDropSupport();
    }
    
    private void registerDragSupport() {
        Transfer[] dragTransferTypes = new Transfer[] {
                LocalSelectionTransfer.getTransfer()
        };
        
        fViewer.addDragSupport(fDragOperations, dragTransferTypes, new DragSourceListener() {
            
            public void dragFinished(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(null);
            }

            public void dragSetData(DragSourceEvent event) {
                event.data = LocalSelectionTransfer.getTransfer().getSelection();
            }

            public void dragStart(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(fViewer.getSelection());
                event.doit = true;
            }
            
        });
    }
    
    private void registerDropSupport() {
        Transfer[] dropTransferTypes = new Transfer[] {
              FileTransfer.getInstance(),
              LocalSelectionTransfer.getTransfer()
        };
        
        fViewer.addDropSupport(fDragOperations, dropTransferTypes, new DropTargetListener() {
            int operations = DND.DROP_NONE;
            
            public void dragEnter(DropTargetEvent event) {
                operations = isValidSelection(event) ? event.detail : DND.DROP_NONE;
                //operations = event.detail;
            }

            public void dragLeave(DropTargetEvent event) {
            }

            public void dragOperationChanged(DropTargetEvent event) {
                operations = isValidSelection(event) ? event.detail : DND.DROP_NONE;
                //operations = event.detail;
            }

            public void dragOver(DropTargetEvent event) {
                event.detail = operations;
                if(operations == DND.DROP_NONE) {
                    event.feedback = DND.FEEDBACK_NONE;
                }
                else {
                    event.feedback |= DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
                }
            }

            public void drop(DropTargetEvent event) {
                // Internal drop
                if(event.data instanceof ITreeSelection) {
                    internalFilesDropped(event);
                }

                // Files
                else if(event.data instanceof String[]) {
                    externalFilesDropped(event);
                }
            }

            public void dropAccept(DropTargetEvent event) {
            }
            
        });
    }

    private void internalFilesDropped(DropTargetEvent event) {
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        File[] files = new File[selection.size()];
        List<?> list = selection.toList();
        list.toArray(files);
        shiftFiles(event, files, event.detail == DND.DROP_COPY);
    }
    
    private void externalFilesDropped(DropTargetEvent event) {
        String[] strings = (String[])event.data;
        File[] files = new File[strings.length];
        for(int i = 0; i < files.length; i++) {
            files[i] = new File(strings[i]);
        }
        shiftFiles(event, files, true);
    }
    
    private void shiftFiles(DropTargetEvent event, final File[] files, final boolean doCopy) {
        if(files == null || files.length == 0) {
            return;
        }
        
        File targetParentFolder = null;
        
        // Dropped on no node so use LD root folder
        if(event.item == null) {
            targetParentFolder = fLDModel.getRootFolder();
        }
        
        // Dropped on Tree node
        else if(event.item instanceof TreeItem) {
            TreeItem treeItem = (TreeItem)event.item;
            targetParentFolder = (File)treeItem.getData();
            if(!targetParentFolder.isDirectory()) {
                targetParentFolder = targetParentFolder.getParentFile();
            }
        }
        
        if(targetParentFolder != null && targetParentFolder.exists() && targetParentFolder.isDirectory()) {
            
            // Few files require no Progress monitor
            if(FileUtils.getFileSize(files) < (5 * 1024 * 1024)) {
                try {
                    if(doCopy) {
                        FileUtils.copyFiles(files, targetParentFolder, null);
                    }
                    else {
                        moveFiles(files, targetParentFolder, null);
                    }
                }
                catch(IOException ex) {
                    MessageDialog.openError(fViewer.getControl().getShell(), Messages.FileTreeViewerDragDropHandler_0, ex.getMessage());
                }
                
                fViewer.refresh();
                return;
            }
            
            // Else use a Progress monitor
            
            final ProgressMonitorDialog monitor = new ProgressMonitorDialog(fViewer.getControl().getShell());
            final File destFolder = targetParentFolder;
            
            IRunnableWithProgress runnable = new IRunnableWithProgress() {
                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    try {
                        if(doCopy) {
                            FileUtils.copyFiles(files, destFolder, monitor);
                        }
                        else {
                            moveFiles(files, destFolder, monitor);
                        }
                    }
                    catch(final IOException ex) {
                        Display.getDefault().asyncExec(new Runnable() {
                            public void run() {
                                if(monitor != null) {
                                    monitor.done();
                                }
                                MessageDialog.openError(fViewer.getControl().getShell(), Messages.FileTreeViewerDragDropHandler_0, ex.getMessage());
                            }
                        });
                    }
                }
            };
            
            try {
                monitor.run(true, true, runnable);
            }
            catch(InvocationTargetException ex) {
                ex.printStackTrace();
            }
            catch(InterruptedException ex) {
                ex.printStackTrace();
            }
            
            fViewer.refresh();
        }
    }
    
    private void moveFiles(File[] files, File destFolder, IProgressMonitor monitor) throws IOException {
        FileChangeHelper fileHelper = new FileChangeHelper(fLDModel);
        fileHelper.moveFiles(files, destFolder, monitor);
    }
    
    private boolean isValidSelection(DropTargetEvent event) {
        if(FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
            return true;
        }
        
        else if(LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
          IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
          if(selection != null) {
              for(Object object : selection.toList()) {
                  if(object instanceof File) {
                      return true;
                  }
              }
          }
        }

        return false;
    }

}
