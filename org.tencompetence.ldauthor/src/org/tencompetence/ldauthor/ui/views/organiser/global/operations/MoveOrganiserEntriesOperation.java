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
package org.tencompetence.ldauthor.ui.views.organiser.global.operations;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserIndex;


/**
 * Move Organiser entries Undoable Operation
 * 
 * @author Phillip Beauvoir
 * @version $Id: MoveOrganiserEntriesOperation.java,v 1.2 2008/04/24 10:14:55 phillipus Exp $
 */
public class MoveOrganiserEntriesOperation
extends AbstractOperation
{

    private List<MoveOrganiserEntryOperation> fList;
    
    public MoveOrganiserEntriesOperation(IUndoContext undoContext, List<MoveOrganiserEntryOperation> list) {
        super(""); //$NON-NLS-1$
        addContext(undoContext);
        fList = list;
    }
    
    @Override
    public String getLabel() {
        if(fList.size() == 1) {
            return Messages.MoveOrganiserEntriesOperation_0 + fList.get(0).getLabel();
        }
        return Messages.MoveOrganiserEntriesOperation_1;
    }
    
    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        for(MoveOrganiserEntryOperation operation : fList) {
            operation.execute(monitor, info);
        }
        OrganiserIndex.getInstance().save();
        return Status.OK_STATUS;
    }

    @Override
    public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        for(MoveOrganiserEntryOperation operation : fList) {
            operation.redo(monitor, info);
        }
        OrganiserIndex.getInstance().save();
        return Status.OK_STATUS;
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        for(MoveOrganiserEntryOperation operation : fList) {
            operation.undo(monitor, info);
        }       
        OrganiserIndex.getInstance().save();
        return Status.OK_STATUS;
    }

    @Override
    public void dispose() {
        fList = null;
    }
}
