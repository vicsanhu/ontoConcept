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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.tencompetence.ldauthor.organisermodel.IOrganiserContainer;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.wizards.ld.NewLDWizard;

/**
 * New LD Action
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewLDAction.java,v 1.9 2008/12/04 20:50:56 phillipus Exp $
 */
public class NewLDAction
extends Action
implements IWorkbenchAction
{
    
    IWorkbenchWindow fWindow;
    IOrganiserContainer fParent;
    
    public NewLDAction(IWorkbenchWindow window) {
        fWindow = window;
        setImageDescriptor(ImageFactory.getImageDescriptor(ImageFactory.ICON_NEWLD));
        setText(Messages.NewLDAction_0);
        setToolTipText(Messages.NewLDAction_1);
    }
    
    public void setParent(IOrganiserContainer parent) {
        fParent = parent;
    }

    @Override
    public void run() {
        IWorkbenchWizard wizard = new NewLDWizard();
        wizard.init(fWindow.getWorkbench(),
                fParent == null ? null : new StructuredSelection(fParent));
        WizardDialog dialog = new WizardDialog(fWindow.getShell(), wizard);
        dialog.open();
    }

    public void dispose() {
        fWindow = null;
        fParent = null;
    } 
}