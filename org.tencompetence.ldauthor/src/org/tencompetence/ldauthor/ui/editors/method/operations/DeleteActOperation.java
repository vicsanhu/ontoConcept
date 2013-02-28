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
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;


/**
 * Delete Act Operation
 * 
 * @author Phillip Beauvoir
 * @version $Id: DeleteActOperation.java,v 1.5 2009/05/22 16:35:05 phillipus Exp $
 */
public class DeleteActOperation extends AbstractOperation {
    
    private ILDEditorPart fEditor;
    private ILDModelObjectContainer fActsModel;
    private IActModel fAct;
    private int fIndex;

    public DeleteActOperation(ILDEditorPart ldEditor, IActModel act) {
        super(Messages.DeleteActOperation_0 + " " + LDModelUtils.getUserObjectName(LDModelFactory.ACT)); //$NON-NLS-1$
        
        fEditor = ldEditor;
        fAct = act;
        
        addContext((IUndoContext)fEditor.getAdapter(IUndoContext.class));
    }
    
    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if(!askUserDeleteAct(fAct)) {
            return Status.CANCEL_STATUS;
        }
        
        deleteAct();
        
        return Status.OK_STATUS;
    }

    @Override
    public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        deleteAct();
        return Status.OK_STATUS;
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        fActsModel.addChildAt(fAct, fIndex);
        return Status.OK_STATUS;
    }
    
    private void deleteAct() {
        fIndex = fAct.getParent().getChildren().indexOf(fAct);
        fActsModel = fAct.getParent();
        fActsModel.removeChild(fAct);
        fEditor.getEditorSite().getSelectionProvider().setSelection(StructuredSelection.EMPTY);
        fAct.getLDModel().setDirty();
    }
    
    /**
     * Ask the user whether they wish to delete the given object
     * 
     * @param act The object to delete
     * @return True if the user said yes, false if user says no or cancels
     */
    private boolean askUserDeleteAct(IActModel act) {
        // Confirmation dialog
        return MessageDialog.openQuestion(
                Display.getDefault().getActiveShell(),
                Messages.DeleteActOperation_0,
                Messages.DeleteActOperation_1 + act.getTitle() + "'?"); //$NON-NLS-1$
    }
    

}