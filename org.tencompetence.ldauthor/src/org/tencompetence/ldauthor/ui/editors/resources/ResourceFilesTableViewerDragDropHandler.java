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

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.resources.IResourceFileModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.utils.FileUtils;


/**
 * Resource Files Viewer Drag and Drop Handler
 * 
 * @author Phillip Beauvoir
 * @version $Id: ResourceFilesTableViewerDragDropHandler.java,v 1.8 2009/05/19 18:21:04 phillipus Exp $
 */
public class ResourceFilesTableViewerDragDropHandler {
    
    private int fDragOperations = DND.DROP_COPY | DND.DROP_MOVE; 

    private StructuredViewer fViewer;
    
    private ILDEditorPart fEditor;
    
    private IResourceModel fResource;

    public ResourceFilesTableViewerDragDropHandler(StructuredViewer viewer, ILDEditorPart editor) {
        fViewer = viewer;
        fEditor = editor;
        
        registerDropSupport();
    }
    
    private void registerDropSupport() {
        Transfer[] dropTransferTypes = new Transfer[] {
              LocalSelectionTransfer.getTransfer()
        };
        
        fViewer.addDropSupport(fDragOperations, dropTransferTypes, new DropTargetListener() {
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
                event.detail = operations;
                event.feedback = DND.FEEDBACK_NONE;
            }

            public void drop(DropTargetEvent event) {
                if(fResource != null) {
                    addResourceFiles();
                }
            }

            public void dropAccept(DropTargetEvent event) {
            }
            
        });
    }
    
    public void setResource(IResourceModel resource) {
        fResource = resource;
    }

    /**
     * Add Resource Files from DND
     */
    private void addResourceFiles() {
        ILDModel ldModel = (ILDModel)fEditor.getAdapter(ILDModel.class);
        
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object o : selection.toList()) {
            if(o instanceof File) {
                File file = (File)o;
                if(!file.isDirectory()) {
                    String href = FileUtils.getRelativePath(file, ldModel.getRootFolder());
                    if(fResource.getResourceFileByHref(href) == null) {
                        IResourceFileModel resourceFile = (IResourceFileModel)LDModelFactory.createModelObject(LDModelFactory.FILE, ldModel);
                        fResource.getFiles().add(resourceFile);
                        resourceFile.setHref(href); // This will fire a change event...
                    }
                }
            }
        }
    }

    private boolean isValidSelection() {
        if(fResource == null) {
            return false;
        }
        
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object object : selection.toList()) {
            if(!(object instanceof File)) {
                return false;
            }
        }
        return true;
    }
}
