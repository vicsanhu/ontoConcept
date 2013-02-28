/*
 * Copyright (c) 2008, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.ui.editors;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;


/**
 * A delegate class that tracks opening, closing and activating LD Editors in order that the caller can be
 * activated in a context of no LD Editor or the default open LD Editor.
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDEditorContextDelegate.java,v 1.2 2008/11/04 12:48:05 phillipus Exp $
 */
public class LDEditorContextDelegate
implements IPartListener {

    private IWorkbenchWindow fWindow;
    protected ILDMultiPageEditor fCurrentLDEditor;
    
    public LDEditorContextDelegate(IWorkbenchWindow window) {
        fWindow = window;
        fWindow.getPartService().addPartListener(this);
    }
    
    /**
     * Check if there is at least one LD Editor open so that the caller has an LD Editor context to start with.
     */
    public void checkEditorOpen() {
        if(fWindow.getActivePage() != null) {
            // If there is one active use that
            IEditorPart editorPart = fWindow.getActivePage().getActiveEditor();
            if(editorPart instanceof ILDMultiPageEditor) {
                partActivated(editorPart);
                return;
            }
            
            // Else use brute force to find a default LD Editor
            IEditorReference[] editors = fWindow.getActivePage().getEditorReferences();
            for(IEditorReference editorReference : editors) {
                IWorkbenchPart part = editorReference.getPart(false);
                if(part instanceof ILDMultiPageEditor) {
                    partActivated(part);
                    return;
                }
            }
            
            // No, so none
            fCurrentLDEditor = null;
            setActiveEditor(null);
        }
    }
    
    /**
     * Over-ride this
     * @param editor The Active LD Editor which may be null
     */
    public void setActiveEditor(ILDMultiPageEditor editor) {
        // over-ride
    }
    
    public ILDMultiPageEditor getActiveEditor() {
        return fCurrentLDEditor;
    }
    
    public void dispose() {
        fWindow.getPartService().removePartListener(this);
    }

    // =================================================================================
    //                       PART LISTENER
    // =================================================================================

    public void partActivated(IWorkbenchPart part) {
        if(part instanceof ILDMultiPageEditor && fCurrentLDEditor != part) {
            fCurrentLDEditor = (ILDMultiPageEditor)part;
            setActiveEditor((ILDMultiPageEditor)part);
        }
    }

    public void partBroughtToTop(IWorkbenchPart part) {
    }

    public void partClosed(IWorkbenchPart part) {
        // If current active editor is closed
        if(part == fCurrentLDEditor) {
            checkEditorOpen();
        }
    }

    public void partDeactivated(IWorkbenchPart part) {
    }

    public void partOpened(IWorkbenchPart part) {
    }
}
