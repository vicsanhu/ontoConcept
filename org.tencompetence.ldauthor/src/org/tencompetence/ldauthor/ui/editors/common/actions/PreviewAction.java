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
package org.tencompetence.ldauthor.ui.editors.common.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.EnvironmentContainerEditPart;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.LearningObjectEditPart;
import org.tencompetence.ldauthor.ui.views.ViewManager;
import org.tencompetence.ldauthor.ui.views.preview.PreviewView;

/**
 * Action to open Preview on an Edit Part.
 * 
 * This is based on the org.eclipse.gef.ui.actions.MatchSizeAction
 * 
 * @author Phillip Beauvoir
 * @version $Id: PreviewAction.java,v 1.6 2008/11/22 01:47:39 phillipus Exp $
 */
public class PreviewAction extends SelectionAction {

    public static final String ID = "previewAction"; //$NON-NLS-1$
    
    public PreviewAction(IWorkbenchPart part) {
        super(part);
        setText(Messages.PreviewAction_0);
        setId(ID);
        setToolTipText(Messages.PreviewAction_0);
    }

    @Override
    public void run() {
        ViewManager.showViewPart(PreviewView.ID, false);
    }

    @Override
    protected boolean calculateEnabled() {
        IWorkbenchPart part = getWorkbenchPart();
        
        ISelection selection = part.getSite().getSelectionProvider().getSelection();
        if(selection instanceof IStructuredSelection) {
            Object object = ((IStructuredSelection)selection).getFirstElement();
            return (object instanceof EnvironmentContainerEditPart)
                    || (object instanceof LearningObjectEditPart);
        }
        
        return false;
    }

}
