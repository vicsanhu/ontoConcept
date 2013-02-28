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
package org.tencompetence.ldauthor.ui.editors.ld;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.organisermodel.IOrganiserLD;
import org.tencompetence.ldauthor.organisermodel.IOrganiserObject;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserIndex;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.editors.IEnvironmentEditorPage;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.IMethodEditorPage;
import org.tencompetence.ldauthor.ui.editors.IOverviewEditorPage;
import org.tencompetence.ldauthor.ui.editors.IResourcesEditorPage;
import org.tencompetence.ldauthor.ui.editors.LDEditorInput;
import org.tencompetence.ldauthor.ui.editors.common.OverviewOutlinePage;
import org.tencompetence.ldauthor.ui.editors.environment.EnvironmentEditorPage;
import org.tencompetence.ldauthor.ui.editors.method.MethodEditorPage;
import org.tencompetence.ldauthor.ui.editors.overview.OverviewEditorPage;
import org.tencompetence.ldauthor.ui.editors.resources.FileTreeViewer;
import org.tencompetence.ldauthor.ui.editors.resources.ResourcesEditorPage;


/**
 * Multi-Page Editor containing sub-editors to which this will delegate
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDMultiPageEditor.java,v 1.73 2010/02/01 10:45:09 phillipus Exp $
 */
public class LDMultiPageEditor
extends MultiPageEditorPart
implements ILDMultiPageEditor, PropertyChangeListener {
    
    public static final String ID = LDAuthorPlugin.PLUGIN_ID + ".ldEditor"; //$NON-NLS-1$
    
    private IOverviewEditorPage fOverviewEditorPage;
    private IMethodEditorPage fRolePartEditorPage;
    private IEnvironmentEditorPage fEnvironmentEditorPage;
    private IResourcesEditorPage fResourcesEditorPage;
    
    
    public LDMultiPageEditor() {
        // Register us as a Organiser Model Listener
        OrganiserIndex.getInstance().addPropertyChangeListener(this);
    }
    
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
        
        // Listen to LD Model Dirty changes and pass onto Active Editor
        ((LDEditorInput)getEditorInput()).getModel().addPropertyChangeListener(this);

        // Set Part Name
        setPartName(input.getName());
    }
    
    @Override
    protected void createPages() {
        
        try {
            fOverviewEditorPage = new OverviewEditorPage();
            addPage(fOverviewEditorPage, getEditorInput());
            
            fRolePartEditorPage = new MethodEditorPage();
            addPage(fRolePartEditorPage, getEditorInput());
            
            fEnvironmentEditorPage = new EnvironmentEditorPage();
            addPage(fEnvironmentEditorPage, getEditorInput());
            
            fResourcesEditorPage = new ResourcesEditorPage();
            addPage(fResourcesEditorPage, getEditorInput());
        }
        catch(PartInitException e) {
            e.printStackTrace();
        }
        
        setPageText(OVERVIEW_TAB, LDModelUtils.DEFAULT_OVERVIEW_NAME);
        setPageImage(OVERVIEW_TAB, ImageFactory.getImage(ImageFactory.IMAGE_ACT_24));

        setPageText(METHOD_TAB, LDModelUtils.getUserObjectName(LDModelFactory.METHOD));
        setPageImage(METHOD_TAB, ImageFactory.getImage(ImageFactory.IMAGE_SEQUENCE_24));
        
        setPageText(ENVIRONMENTS_TAB, LDModelUtils.getUserObjectName(LDModelFactory.ENVIRONMENTS));
        setPageImage(ENVIRONMENTS_TAB, ImageFactory.getImage(ImageFactory.IMAGE_ENVIRONMENT_24));
        
        setPageText(RESOURCES_TAB, LDModelUtils.getUserObjectName(LDModelFactory.RESOURCES));
        setPageImage(RESOURCES_TAB, ImageFactory.getImage(ImageFactory.IMAGE_PACKAGE_24));
    }
    
    @Override
    protected void pageChange(int newPageIndex) {
        super.pageChange(newPageIndex);
        
        // Activate Files Refresh Timer
        if(newPageIndex == RESOURCES_TAB) {
            ((FileTreeViewer)fResourcesEditorPage.getFileTreeViewer()).startRefreshTimer();
        }
        else {
            ((FileTreeViewer)fResourcesEditorPage.getFileTreeViewer()).stopRefreshTimer();
        }
    }
    
    @Override
    public void setActivePage(int pageIndex) {
        super.setActivePage(pageIndex);
    }
    
    public IOverviewEditorPage getOverviewEditorPage() {
        return fOverviewEditorPage;
    }
    
    public IMethodEditorPage getMethodEditorPage() {
        return fRolePartEditorPage;
    }

    public IEnvironmentEditorPage getEnvironmentEditorPage() {
        return fEnvironmentEditorPage;
    }
    
    public IResourcesEditorPage getResourcesEditorPage() {
        return fResourcesEditorPage;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        /*
         * Return the singleton Outline Page
         */
        if(adapter == IContentOutlinePage.class) {
            return new OverviewOutlinePage();
        }

        /*
         * Return the backing model
         */
        else if(adapter == ILDModel.class) {
            return ((LDEditorInput)getEditorInput()).getModel();
        }
        
        /*
         * Return the Active editor
         */
        else if(adapter == ILDEditorPart.class) {
            return (getActiveEditor() instanceof ILDEditorPart) ? getActiveEditor() : null;
        }

        return super.getAdapter(adapter);
    }
    
    @Override
    public void doSave(IProgressMonitor monitor) {
        // Notify each sub-editor part
        for(int i = 0; i < getPageCount(); i++) {
            IEditorPart part = getEditor(i);
            part.doSave(monitor);
        }
        
        try {
            ((LDEditorInput)getEditorInput()).saveModel();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    // Need to access this so make it public
    @Override
    public IEditorPart getActiveEditor() {
        return super.getActiveEditor();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        // Unregister us as an Organiser Model Listener
        OrganiserIndex.getInstance().removePropertyChangeListener(this);

        // Unregister us as a LD Model Listener
        ((LDEditorInput)getEditorInput()).getModel().removePropertyChangeListener(this);
        
        // Dispose of LD
        ((LDEditorInput)getEditorInput()).getModel().dispose();
    }
    
  
    // =================================================================================
    //                       Property Change Support
    // =================================================================================

    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        
        /*
         * Entry was renamed in OrganiserIndex View so update Editor bar if we are editing
         * the object referred to in the OrganiserIndex Entry.
         */
        if(evt.getSource() instanceof IOrganiserLD) {
            if(IOrganiserObject.PROPERTY_NAME.equals(propertyName)) {
                if(((IOrganiserLD)evt.getSource()).getFile().equals(((LDEditorInput)getEditorInput()).getModel().getManifestFile())) {
                    setPartName((String)evt.getNewValue());
                }
            }
        }
        
        /*
         * LD Model - pass on Dirty events to Active Editor
         */
        if(evt.getSource() instanceof ILDModel) {
            if(ILDModel.PROPERTY_DIRTY.equals(propertyName)) {
                ILDEditorPart part = (ILDEditorPart)getActiveEditor();
                if(part != null) {
                    part.setDirty(true);
                }
            }
        }
    }
}
