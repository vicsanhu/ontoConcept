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
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.ldauthor.ExtensionContentHandler;
import org.tencompetence.ldauthor.ILDContentHandler;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.organisermodel.IOrganiserResource;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Resources Viewer Drag and Drop Handler
 * 
 * @author Phillip Beauvoir
 * @version $Id: ResourcesTableViewerDragDropHandler.java,v 1.15 2009/05/22 16:35:05 phillipus Exp $
 */
public class ResourcesTableViewerDragDropHandler {
    
    private int fDragOperations = DND.DROP_COPY | DND.DROP_MOVE; 

    private StructuredViewer fViewer;
    
    private ILDModel fLDModel;

    public ResourcesTableViewerDragDropHandler(StructuredViewer viewer, ILDModel ldModel) {
        fViewer = viewer;
        fLDModel = ldModel;
        
        //registerDragSupport();
        registerDropSupport();
    }
    
    @SuppressWarnings("unused")
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
                addResources();
            }

            public void dropAccept(DropTargetEvent event) {
            }
            
        });
    }

    /**
     * Add Resources from a DND operation
     */
    private void addResources() {
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object o : selection.toList()) {
            // Dropped File
            if(o instanceof File) {
                File file = (File)o;
                if(!file.isDirectory()) {
                    addResource(file);
                }
            }
            
            // Organiser Resource
            else if(o instanceof IOrganiserResource) {
                IOrganiserResource organiserResource = (IOrganiserResource)o;
                String href = organiserResource.getResourceLocation();
                if(StringUtils.isSet(href)) {
                    File file = new File(href);
                    if(file.exists() && !file.isDirectory()) {
                        // TODO - Copy file over?
                        addResource(file);
                    }
                    else {
                        addResource(href);
                    }
                }
            }
        }
    }
    
    
    /**
     * Add a Resource with file
     * 
     * @param href
     */
    private IResourceModel addResource(File file) {
        String href = FileUtils.getRelativePath(file, fLDModel.getRootFolder());
        
        IResourceModel resource = LDModelUtils.createNewResourceWithHref(fLDModel, href, file.getName());

        // If this is a content type added by an extension, set its type
        ILDContentHandler handler = ExtensionContentHandler.getInstance().getHandler(file);
        if(handler != null) {
            resource.setType(handler.getType());
        }
        
        return resource;
    }
    
    /**
     * Add a resource with a non-file (web)
     * 
     * @param href
     * @return
     */
    private IResourceModel addResource(String href) {
        IResourceModel resource = LDModelUtils.createNewResourceWithHref(fLDModel, href, href);
        
        return resource;
    }

    private boolean isValidSelection() {
        IStructuredSelection selection = (IStructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
        for(Object object : selection.toList()) {
            if(!(object instanceof File) && !(object instanceof IOrganiserResource)) {
                return false;
            }
        }
        return true;
    }
}
