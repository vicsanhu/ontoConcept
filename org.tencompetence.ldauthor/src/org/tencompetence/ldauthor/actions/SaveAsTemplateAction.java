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
package org.tencompetence.ldauthor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.LDEditorContextDelegate;
import org.tencompetence.ldauthor.ui.wizards.templates.SaveAsTemplateWizard;

/**
 * Save As Template Action
 * 
 * @author Phillip Beauvoir
 * @version $Id: SaveAsTemplateAction.java,v 1.2 2009/05/19 18:21:05 phillipus Exp $
 */
public class SaveAsTemplateAction
extends Action
implements IWorkbenchAction
{
    
    public static final ActionFactory INSTANCE = new ActionFactory("org.tencompetence.ldauthor.actions.saveastemplate") { //$NON-NLS-1$

        @Override
        public IWorkbenchAction create(IWorkbenchWindow window) {
            if (window == null) {
                throw new IllegalArgumentException();
            }
            IWorkbenchAction action = new SaveAsTemplateAction(window);
            action.setId(getId());
            return action;
        }
    };
    
    private LDEditorContextDelegate fLDEditorContextDelegate;

    private ILDModel fLDModel;
    private String fTitle;
    
    public SaveAsTemplateAction(IWorkbenchWindow window) {
        setText(Messages.SaveAsTemplateAction_0);
        setToolTipText(Messages.SaveAsTemplateAction_0);
        setEnabled(false);
        
        // Editor Context Listener
        fLDEditorContextDelegate = new LDEditorContextDelegate(window) {
            @Override
            public void setActiveEditor(ILDMultiPageEditor editor) {
                if(editor != null) {
                    fLDModel = ((ILDModel)editor.getAdapter(ILDModel.class));
                    fTitle = editor.getTitle();
                    setEnabled(fLDModel != null);
                }
                else {
                    fLDModel = null;
                    setEnabled(false);
                }
            }
        };
    }
    
    @Override
    public void run() {
        Wizard wizard = new SaveAsTemplateWizard(fLDModel, fTitle);
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
        dialog.open();

    }
    
    public void dispose() {
        fLDEditorContextDelegate.dispose();
    } 
}