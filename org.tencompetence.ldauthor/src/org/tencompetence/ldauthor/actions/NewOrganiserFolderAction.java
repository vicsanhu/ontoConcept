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
package org.tencompetence.ldauthor.actions;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.tencompetence.ldauthor.organisermodel.IOrganiserContainer;
import org.tencompetence.ldauthor.organisermodel.IOrganiserFolder;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserFolder;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserIndex;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.dialogs.NewOrganiserFolderDialog;
import org.tencompetence.ldauthor.ui.views.organiser.OrganiserView;
import org.tencompetence.ldauthor.ui.views.organiser.global.OrganiserComposite;
import org.tencompetence.ldauthor.ui.views.organiser.global.operations.NewOrganiserEntryOperation;

/**
 * New Organiser Folder Action
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewOrganiserFolderAction.java,v 1.6 2009/06/07 14:05:59 phillipus Exp $
 */
public class NewOrganiserFolderAction
extends Action
implements IWorkbenchAction
{
    
    private IWorkbenchWindow fWindow;
    private IOrganiserContainer fParent;
    
    public NewOrganiserFolderAction(IWorkbenchWindow window) {
        fWindow = window;
        setImageDescriptor(ImageFactory.getImageDescriptor(ImageFactory.ICON_NEWFOLDER));
        setText(Messages.NewOrganiserFolderAction_0);
        setToolTipText(Messages.NewOrganiserFolderAction_1);
    }
    
    public void setParent(IOrganiserContainer parent) {
        fParent = parent;
    }
    
    @Override
    public void run() {
        if(fParent == null) {
            fParent = OrganiserIndex.getInstance();
        }
        
        NewOrganiserFolderDialog dialog = new NewOrganiserFolderDialog(fWindow.getShell());
        if(dialog.open() == Dialog.OK) {
            IOrganiserFolder folder = new OrganiserFolder(dialog.getName());
            
            // The Organiser View has a static Undo Context so we need to show it
            IUndoContext undoContext = OrganiserView.getUndoContext(OrganiserComposite.class);
            try {
                getOperationHistory().execute(
                        new NewOrganiserEntryOperation(undoContext, fParent, folder),
                        null,
                        null);
            }
            catch(ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private IOperationHistory getOperationHistory() {
        return PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
    }
    
    public void dispose() {
        fWindow = null;
        fParent = null;
    } 
}