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
package org.tencompetence.ldauthor.fedora.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.LDEditorContextDelegate;


/**
 * Action to Upload a LD to Fedora
 * 
 * We have to implement IActionDelegate2 so that we get an init(IAction action) which is called
 * because our plugin implements EarlyStartup
 * 
 * @author Phillip Beauvoir
 * @version $Id: UploadAction.java,v 1.6 2009/06/30 10:24:26 phillipus Exp $
 */
public class UploadAction
implements IWorkbenchWindowActionDelegate, IActionDelegate2 {
    
    private IAction fAction;
    private ILDMultiPageEditor fEditor;
    private String fTitle;
    
    private LDEditorContextDelegate fLDEditorContextDelegate;
    
    public UploadAction() {
    }

    public void dispose() {
        fLDEditorContextDelegate.dispose();
        fAction = null;
        fEditor = null;
    }

    public void init(IWorkbenchWindow window) {
        // New LD Editor Context listener delegate
        fLDEditorContextDelegate = new LDEditorContextDelegate(window) {
            @Override
            public void setActiveEditor(ILDMultiPageEditor editor) {
                fEditor = editor;
                if(fEditor != null) {
                    fTitle = fEditor.getTitle();
                    fAction.setEnabled(true);
                }
                else {
                    fAction.setEnabled(false);
                }
            }
        };
    }

    public void run(IAction action) {
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    // This is triggered by EarlyStartup
    public void init(IAction action) {
        fAction = action;
        action.setEnabled(false);
    }

    public void runWithEvent(IAction action, Event event) {
        if(fEditor == null) {
            return;
        }
        
        // Does it need saving?
        if(fEditor.isDirty()) {
            MessageDialog dialog = new MessageDialog(fEditor.getSite().getShell(),
                    Messages.UploadAction_0,
                    null,
                    Messages.UploadAction_1,
                    MessageDialog.QUESTION,
                    new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL },
                    0);
            int response = dialog.open();
            switch(response) {
                case 0:
                    fEditor.doSave(null);
                    break;
                    
                case 1:
                    break;
                    
                case 2:
                    return;
            }
        }
        
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            public void run() {
                IReCourseLDModel ldModel = ((IReCourseLDModel)fEditor.getAdapter(ILDModel.class));
                Wizard wizard = new UploadWizard(ldModel, fTitle);
                WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
                dialog.open();
            }
        });
    }
}
