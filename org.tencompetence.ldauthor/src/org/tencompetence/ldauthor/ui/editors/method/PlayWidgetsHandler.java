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
package org.tencompetence.ldauthor.ui.editors.method;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.LDEditorInput;
import org.tencompetence.ldauthor.ui.editors.method.operations.DeletePlayOperation;
import org.tencompetence.ldauthor.ui.editors.method.operations.MovePlayOperation;
import org.tencompetence.ldauthor.ui.editors.method.operations.NewPlayOperation;
import org.tencompetence.ldauthor.ui.wizards.method.NewPlayWizard;


/**
 * Handles sync of Play Widgets and Plays LD model
 * 
 * @author Phillip Beauvoir
 * @version $Id: PlayWidgetsHandler.java,v 1.11 2009/05/19 18:21:01 phillipus Exp $
 */
public class PlayWidgetsHandler {

    private ILDEditorPart fEditor;
    private ILDModel fLDModel;
    
    private Composite fClient;
    
    public PlayWidgetsHandler(ILDEditorPart editor, Composite parent) {
        fEditor = editor;
        fLDModel = ((LDEditorInput)editor.getEditorInput()).getModel();
        
        Color backgroundColor;
        if(Platform.getOS().equals(Platform.OS_LINUX)) {
            backgroundColor = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
        }
        else {
            backgroundColor = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);
        }
        
        ScrolledComposite scrolledClient = new ScrolledComposite(parent, SWT.V_SCROLL);
        scrolledClient.setExpandHorizontal(true);
        AppFormToolkit.getInstance().adapt(scrolledClient);
        scrolledClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        scrolledClient.setBackground(backgroundColor);

        fClient = new Composite(scrolledClient, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 3;
        layout.marginWidth = 3;
        layout.verticalSpacing = 4;
        fClient.setLayout(layout);
        fClient.setBackground(backgroundColor);
        
        scrolledClient.setContent(fClient);
        
        addPlayItems();
    }
    
    /**
     * Add a new Play to the LD model and create a new PlayWidget
     */
    public void addNewPlay() {
        NewPlayWizard wizard = new NewPlayWizard();
        WizardDialog dialog = new WizardDialog(fEditor.getSite().getShell(), wizard);
        if(dialog.open() == IDialogConstants.OK_ID) {
            try {
                getOperationHistory().execute(
                        new NewPlayOperation(this, wizard.getPlayTitle()),
                        null,
                        null);
            }
            catch(ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Delete a Play
     * @param play
     */
    public void deletePlay(IPlayModel play) {
        try {
            getOperationHistory().execute(
                    new DeletePlayOperation(this, play),
                    null,
                    null);
        }
        catch(ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Move a Play either up (-1) or down (+1)
     * @param play
     * @param offset
     */
    public void movePlay(IPlayModel play, int offset) {
        try {
            getOperationHistory().execute(
                    new MovePlayOperation(this, play, offset),
                    null,
                    null);
        }
        catch(ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Add the Play Items from the LD model
     */
    private void addPlayItems() {
        for(ILDModelObject objPlay : fLDModel.getMethodModel().getPlaysModel().getChildren()) {
            new PlayWidget(this, (IPlayModel)objPlay);
        }
        
        layout();
    }
    
    /**
     * Select the default (first) Act
     */
    public void selectDefaultAct() {
        if(fClient.getChildren().length > 0) {
            PlayWidget widget = (PlayWidget)fClient.getChildren()[0];
            List<ILDModelObject> acts = widget.getPlay().getActsModel().getChildren();
            if(acts.size() > 0) {
                widget.getTableViewer().setSelection(new StructuredSelection(acts.get(0)));
            }
        }
    }
    
    public void layout() {
        /*
         * This is the only combination of commands that refreshes the Plays client area
         * successfully on all 3 tested platforms. Ubuntu is the worst culprit.
         */
        fClient.layout();
        fClient.pack();
    }
    
    public Composite getComposite() {
        return fClient;
    }
    
    /**
     * @return The Editor Part this Handler belongs to
     */
    public ILDEditorPart getEditor() {
        return fEditor;
    }
    
    /*
     * Get the undo/redo operation history from the workbench.
     */
    private IOperationHistory getOperationHistory() {
        return PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
    }

    /**
     * Find a Play Widget based on Play
     * We do this because the widget may have been deleted and a new one created
     * @param play
     * @return
     */
    public PlayWidget findPlayWidget(IPlayModel play) {
        for(Control child : fClient.getChildren()) {
            PlayWidget playWidget = (PlayWidget)child;
            if(play == playWidget.getPlay()) {
                return playWidget;
            }
        }

        return null;
    }

}
