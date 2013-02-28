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
package org.tencompetence.ldauthor.ui.editors.method.operations;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.StructuredSelection;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.method.PlayWidget;
import org.tencompetence.ldauthor.ui.editors.method.PlayWidgetsHandler;


/**
 * NewPlayOperation
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewPlayOperation.java,v 1.10 2009/05/22 16:35:05 phillipus Exp $
 */
public class NewPlayOperation extends AbstractOperation {
    
    private PlayWidgetsHandler fHandler;
    private ILDEditorPart fEditor;
    private ILDModel fLDModel;

    private IPlayModel fNewPlay;

    public NewPlayOperation(PlayWidgetsHandler handler, String title) {
        super(Messages.NewPlayOperation_0 + " " + LDModelUtils.getUserObjectName(LDModelFactory.PLAY)); //$NON-NLS-1$
        
        fHandler = handler;
        fEditor = handler.getEditor();
        fLDModel = (ILDModel)fEditor.getAdapter(ILDModel.class);
        
        fNewPlay = (IPlayModel)LDModelFactory.createModelObject(LDModelFactory.PLAY, fLDModel);
        fNewPlay.setTitle(title);
        
        // Add a new default Act
        IActModel newAct = (IActModel)LDModelFactory.createModelObject(LDModelFactory.ACT, fLDModel);
        newAct.setTitle(LDModelUtils.getUserObjectName(LDModelFactory.ACT) + " 1"); //$NON-NLS-1$
        fNewPlay.getActsModel().addChild(newAct);
        
        addContext((IUndoContext)fEditor.getAdapter(IUndoContext.class));
    }
    
    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        fLDModel.getMethodModel().getPlaysModel().addChild(fNewPlay);

        new PlayWidget(fHandler, fNewPlay);
        fHandler.layout();

        fLDModel.setDirty();

        return Status.OK_STATUS;
    }

    @Override
    public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        return execute(monitor, info);
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        PlayWidget playWidget = fHandler.findPlayWidget(fNewPlay);
        
        if(playWidget == null) {
            // Shouldn't happen!
            System.err.println("Couldn't find Play in NewPlayOperation.undo"); //$NON-NLS-1$
            return new Status(Status.ERROR, LDAuthorPlugin.getDefault().getId(), 1, "", null); //$NON-NLS-1$
        }

        fNewPlay.getLDModel().getMethodModel().getPlaysModel().removeChild(fNewPlay);
        playWidget.dispose();
        fHandler.layout();

        fEditor.getEditorSite().getSelectionProvider().setSelection(StructuredSelection.EMPTY);
        fLDModel.setDirty();

        return Status.OK_STATUS;
    }
}
