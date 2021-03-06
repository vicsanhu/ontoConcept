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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.tencompetence.ldauthor.organisermodel.IOrganiserObject;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserIndex;
import org.tencompetence.ldauthor.ui.dialogs.RenameObjectDialog;


/**
 * Rename Organiser entry Undoable Operation
 * 
 * @author Phillip Beauvoir
 * @version $Id: RenameOrganiserEntryOperation.java,v 1.3 2009/06/07 14:05:59 phillipus Exp $
 */
public class RenameOrganiserEntryOperation
extends AbstractOperation
{

    private IOrganiserObject fObject;
    private String fOldName, fNewName;
    
    public RenameOrganiserEntryOperation(IUndoContext undoContext, IOrganiserObject object) {
        super(Messages.RenameOrganiserEntryOperation_0 + object.getName());
        addContext(undoContext);
        fObject = object;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        RenameObjectDialog dialog = new RenameObjectDialog(Display.getDefault().getActiveShell(), fObject.getName());
        if(dialog.open()) {
            String name = dialog.getNewName();
            if(name != null) {
                fOldName = fObject.getName();
                fNewName = name;
                fObject.setName(fNewName);
                OrganiserIndex.getInstance().save();
                return Status.OK_STATUS;
            }
        }

        return Status.CANCEL_STATUS;
    }
    
    @Override
    public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        fObject.setName(fNewName);
        OrganiserIndex.getInstance().save();
        return Status.OK_STATUS;
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        fObject.setName(fOldName);
        OrganiserIndex.getInstance().save();
        return Status.OK_STATUS;
    }

}
