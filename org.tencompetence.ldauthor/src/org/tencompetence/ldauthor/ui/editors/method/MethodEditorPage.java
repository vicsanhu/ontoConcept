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

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.operations.RedoActionHandler;
import org.eclipse.ui.operations.UndoActionHandler;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.SelectionProviderAdapter;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.IMethodEditorPage;
import org.tencompetence.ldauthor.ui.editors.LDEditorInput;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;



/**
 * Method Editor page.
 * 
 * This is the Method Editor but actually acts as Host for sub-composite panels
 * for Plays and Acts.
 * 
 * @author Phillip Beauvoir
 * @version $Id: MethodEditorPage.java,v 1.12 2010/02/01 10:45:09 phillipus Exp $
 */
public class MethodEditorPage
extends EditorPart
implements IContextProvider, IMethodEditorPage
{

    public static String HELP_ID = LDAuthorPlugin.PLUGIN_ID + ".MethodEditorHelp"; //$NON-NLS-1$
    
    /**
     * Form
     */
    private Form fForm;

    private PlaysComposite fPlaysComposite;
    
    private RolePartsComposite fRolePartsComposite;
    
    /*
     * Undo and redo stuff
     */
    private UndoActionHandler fUndoActionHandler;
    private RedoActionHandler fRedoActionHandler;
    private IUndoContext fUndoContext;

    
    /**
     * Dirty flag
     */
    private boolean fDirty;
    
    /**
     * Use a local Selection provider to co-ordinate local selections
     */
    private ActSelectionProvider fLocalSelectionProviderAdapter = new ActSelectionProvider();

    
    /**
     * Default Constructor
     */
    public MethodEditorPage() {
        initializeOperationHistory();
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
    }

    @Override
    public void createPartControl(Composite parent) {

        fForm = AppFormToolkit.getInstance().createForm(parent);
        AppFormToolkit.getInstance().paintBordersFor(fForm.getBody());
        
        /*
         * Form Body is main composite
         */
        Composite body = fForm.getBody();
        body.setLayout(new GridLayout());
        
        SashForm sash = new SashForm(body, SWT.HORIZONTAL);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        AppFormToolkit.getInstance().adapt(sash);
        
        fPlaysComposite = new PlaysComposite(this, sash, SWT.NULL);
        AppFormToolkit.getInstance().adapt(fPlaysComposite);
        
        fRolePartsComposite = new RolePartsComposite(this, sash, SWT.NULL);
        AppFormToolkit.getInstance().adapt(fRolePartsComposite);
        
        sash.setWeights(new int[] { 25, 75 });
        
        // Disable Inspector so that we don't select anything
        InspectorManager.getInstance().disable();

        // Select first Act
        fPlaysComposite.getPlayWidgetsHandler().selectDefaultAct();
        
        // Re-enable Inspector
        InspectorManager.getInstance().enable();
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        // Undo
        registerUndoActionHandlers();
        
        // Register a master single selection provider
        getEditorSite().setSelectionProvider(new SelectionProviderAdapter());
    }
    
    /**
     * Register Global Action Handlers
     */
    private void registerUndoActionHandlers() {
        fUndoActionHandler = new UndoActionHandler(getEditorSite(), fUndoContext);
        fRedoActionHandler = new RedoActionHandler(getEditorSite(), fUndoContext);

        IActionBars actionBars = getEditorSite().getActionBars();
        
        // Register our interest in the global menu actions
        actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), fUndoActionHandler);
        actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), fRedoActionHandler);
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        setDirty(false);
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isDirty() {
        return fDirty;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void setFocus() {
        fPlaysComposite.setFocus();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        /*
         * Return the backing model
         */
        if(adapter == ILDModel.class) {
            return ((LDEditorInput)getEditorInput()).getModel();
        }
        /*
         * Undo Context
         */
        if(adapter == IUndoContext.class) {
            return fUndoContext;
        }
        /*
         * Selection Adapter
         */
        if(adapter == ActSelectionProvider.class) {
            return fLocalSelectionProviderAdapter;
        }
        
        /*
         * Return the multi-page editor
         */
        if(adapter == ILDMultiPageEditor.class) {
            return ((MultiPageEditorSite)getSite()).getMultiPageEditor();
        }

        return super.getAdapter(adapter);
    }

    public IAction getGlobalActionHandler(String actionId) {
        if(actionId == ActionFactory.UNDO.getId()) {
            return fUndoActionHandler;
        }
        if(actionId == ActionFactory.REDO.getId()) {
            return fRedoActionHandler;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.tencompetence.ldauthor.ui.editors.ILDEditor#setDirty(boolean)
     */
    public void setDirty(boolean dirty) {
        if(fDirty != dirty) {
            fDirty = dirty;
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        // Dispose of undo stuff
        getOperationHistory().dispose(fUndoContext, true, true, true);
    }
    
    // =================================================================================
    //                       Contextual Help support
    // =================================================================================
    
    public int getContextChangeMask() {
        return NONE;
    }

    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    public String getSearchExpression(Object target) {
        return Messages.MethodEditorPage_0;
    }

    // =================================================================================
    //                       Undo/Redo stuff
    // =================================================================================

    /*
     * Initialize the workbench operation history for our undo context.
     */
    private void initializeOperationHistory() {
        // Create a unique undo context to represent this editor's undo history
        fUndoContext = new ObjectUndoContext(this);

        // Set the undo limit for this context (could put this in Prefs)
        int limit = 99;
        getOperationHistory().setLimit(fUndoContext, limit);
    }

    /*
     * Get the operation history from the workbench.
     */
    private IOperationHistory getOperationHistory() {
        return PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
    }

}
