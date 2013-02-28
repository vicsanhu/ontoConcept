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
package org.tencompetence.ldauthor.ui.editors.overview;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.SelectionProviderAdapter;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.IOverviewEditorPage;
import org.tencompetence.ldauthor.ui.editors.LDEditorInput;



/**
 * Overview Editor page
 * 
 * @author Phillip Beauvoir
 * @version $Id: OverviewEditorPage.java,v 1.35 2010/02/01 10:45:09 phillipus Exp $
 */
public class OverviewEditorPage
extends EditorPart
implements IContextProvider, IOverviewEditorPage
{

    public static String HELP_ID = LDAuthorPlugin.PLUGIN_ID + ".OverviewEditorHelp"; //$NON-NLS-1$
    
    /**
     * Form
     */
    private Form fForm;
    
    private OverviewSection fOverviewSection;

    /**
     * Dirty flag
     */
    private boolean fDirty;
    
    /**
     * Default Constructor
     */
    public OverviewEditorPage() {
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
        
        // Register a single selection provider as master proxy
        getEditorSite().setSelectionProvider(new SelectionProviderAdapter());
        
        /*
         * Form Body is main composite
         */
        Composite body = fForm.getBody();
        body.setLayout(new GridLayout());
        
        ScrolledComposite scrolledClient = new ScrolledComposite(body, SWT.V_SCROLL);
        scrolledClient.setExpandHorizontal(true);
        scrolledClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        AppFormToolkit.getInstance().adapt(scrolledClient);
        
        fOverviewSection = new OverviewSection(this, scrolledClient);
        AppFormToolkit.getInstance().adapt(fOverviewSection);
        scrolledClient.setContent(fOverviewSection);
        
        fOverviewSection.layout();
        fOverviewSection.pack();
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
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
        if(fOverviewSection != null) {
            fOverviewSection.setFocus();
        }
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
         * Return the multi-page editor
         */
        if(adapter == ILDMultiPageEditor.class) {
            return ((MultiPageEditorSite)getSite()).getMultiPageEditor();
        }

        return super.getAdapter(adapter);
    }

    public IAction getGlobalActionHandler(String actionId) {
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
        return Messages.OverviewEditorPage_0;
    }
}
