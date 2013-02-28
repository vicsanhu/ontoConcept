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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.method.ActSelectionProvider;
import org.tencompetence.ldauthor.ui.editors.method.PlayWidget;
import org.tencompetence.ldauthor.ui.editors.method.PlayWidgetsHandler;


/**
 * DeletePlayOperation
 * 
 * @author Phillip Beauvoir
 * @version $Id: DeletePlayOperation.java,v 1.9 2009/05/22 16:35:05 phillipus Exp $
 */
public class DeletePlayOperation extends AbstractOperation {
    
    private PlayWidgetsHandler fHandler;
    private ILDEditorPart fEditor;
    private IPlayModel fPlay;
    
    private int index;

    public DeletePlayOperation(PlayWidgetsHandler handler, IPlayModel play) {
        super(Messages.DeletePlayOperation_0 + " " + LDModelUtils.getUserObjectName(LDModelFactory.PLAY)); //$NON-NLS-1$
        
        fHandler = handler;
        fEditor = handler.getEditor();
        fPlay = play;
        
        addContext((IUndoContext)fEditor.getAdapter(IUndoContext.class));
    }
    
    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if(!askUserDeletePlay(fPlay)) {
            return Status.CANCEL_STATUS;
        }
        
        return deletePlay();
    }

    @Override
    public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        return deletePlay();
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        fPlay.getLDModel().getMethodModel().getPlaysModel().addChildAt(fPlay, index);

        new PlayWidget(fHandler, fPlay, index);
        fHandler.layout();

        fPlay.getLDModel().setDirty();
        return Status.OK_STATUS;
    }

    private IStatus deletePlay() {
        PlayWidget playWidget = fHandler.findPlayWidget(fPlay);
        
        if(playWidget == null) {
            // Shouldn't happen!
            System.err.println("Couldn't find Play in DeletePlayOperation.deletePlay"); //$NON-NLS-1$
            return new Status(Status.ERROR, LDAuthorPlugin.getDefault().getId(), 1, "", null); //$NON-NLS-1$
        }
        
        index = fPlay.getLDModel().getMethodModel().getPlaysModel().getChildren().indexOf(fPlay);

        fPlay.getLDModel().getMethodModel().getPlaysModel().removeChild(fPlay);
        playWidget.dispose();
        fHandler.layout();

        // De-select Play
        fEditor.getEditorSite().getSelectionProvider().setSelection(StructuredSelection.EMPTY);
        
        // And de-select an Act
        ActSelectionProvider selectionHandler = (ActSelectionProvider)fEditor.getAdapter(ActSelectionProvider.class);
        selectionHandler.setSelection(StructuredSelection.EMPTY);

        fPlay.getLDModel().setDirty();

        return Status.OK_STATUS;
    }

    /**
     * Ask the user whether they wish to delete the given object
     * 
     * @param object The object to delete
     * @return True if the user said yes, false if user says no or cancels
     */
    private boolean askUserDeletePlay(IPlayModel play) {
        // Confirmation dialog
        return MessageDialog.openQuestion(
                Display.getDefault().getActiveShell(),
                Messages.DeletePlayOperation_0,
                Messages.DeletePlayOperation_1 + play.getTitle() + "'?"); //$NON-NLS-1$
    }
}
