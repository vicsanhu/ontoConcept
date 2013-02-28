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
package org.tencompetence.ldauthor.ui.editors.common;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.graphicsmodel.impl.GraphicalEmptyModel;
import org.tencompetence.ldauthor.ui.editors.LDEditorInput;
import org.tencompetence.ldauthor.ui.editors.ld.LDMultiPageEditor;


/**
 * Abstract Graphical Editor that exists as an editor part of a Multi-part Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractLDGraphicalEditorPage.java,v 1.15 2010/02/01 10:45:09 phillipus Exp $
 */
public abstract class AbstractLDGraphicalEditorPage
extends AbstractLDGraphicalEditor {
    
    /*
     * Have to over-ride this so we can update actions as this editor will be in a Multipage Editor.
     */ 
    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if(part instanceof LDMultiPageEditor && ((LDMultiPageEditor)part).getActiveEditor() == this) {
            updateActions(getSelectionActions());
        }
    }
    
    /**
     * Set a null model so that the editor is blank and disabled
     */
    protected void setEmptyModel() {
        getGraphicalViewer().getControl().setEnabled(false);
        setModel(new GraphicalEmptyModel(null));
        getGraphicalViewer().setContents(getModel());
    }
    
    @Override
    public void doSave(IProgressMonitor monitor) {
        // Mark the save location
        getCommandStack().markSaveLocation();
        setDirty(false);  // Have to do this
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class type) {
        /*
         * Return the backing model
         */
        if(type == ILDModel.class) {
            return ((LDEditorInput)getEditorInput()).getModel();
        }
        
        return super.getAdapter(type);
    }
}
