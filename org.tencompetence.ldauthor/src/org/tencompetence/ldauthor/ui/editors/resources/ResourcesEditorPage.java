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
package org.tencompetence.ldauthor.ui.editors.resources;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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
import org.tencompetence.ldauthor.ui.editors.IResourcesEditorPage;
import org.tencompetence.ldauthor.ui.editors.LDEditorInput;


/**
 * Resources Editor Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: ResourcesEditorPage.java,v 1.22 2010/02/01 10:45:09 phillipus Exp $
 */
public class ResourcesEditorPage extends EditorPart 
implements IContextProvider, IResourcesEditorPage {

    public static String HELP_ID = LDAuthorPlugin.PLUGIN_ID + ".resourcesEditorHelp"; //$NON-NLS-1$
    
    /**
     * Form
     */
    private Form fForm;
    
    private FilesComposite fFilesComposite;
    
    private ResourcesComposite fResourcesComposite;
    
    /**
     * Dirty flag
     */
    private boolean fDirty;

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
        
        fResourcesComposite = new ResourcesComposite(this, sash, SWT.NULL);
        AppFormToolkit.getInstance().adapt(fResourcesComposite);
        
        fFilesComposite = new FilesComposite(this, sash, SWT.NULL);
        AppFormToolkit.getInstance().adapt(fFilesComposite);
        
        sash.setWeights(new int[] { 70, 30 });
        
        // Register a master single selection provider
        getEditorSite().setSelectionProvider(new SelectionProviderAdapter());
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    public void setFocus() {
        fForm.setFocus();
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        setDirty(false);
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public boolean isDirty() {
        return fDirty;
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

    /**
     * @return The Resources Section TableViewer
     */
    public TableViewer getResourcesTableViewer() {
        return fResourcesComposite.getTableViewer();
    }

    /**
     * @return The Files Section TreeViewer
     */
    public TreeViewer getFileTreeViewer() {
        return fFilesComposite.getTreeViewer();
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
        return Messages.ResourcesEditorPage_0;
    }

}
