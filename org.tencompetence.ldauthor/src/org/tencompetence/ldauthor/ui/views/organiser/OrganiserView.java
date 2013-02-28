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
package org.tencompetence.ldauthor.ui.views.organiser;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserIndex;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.SelectionProviderAdapter;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.LDEditorContextDelegate;
import org.tencompetence.ldauthor.ui.views.IOrganiserView;
import org.tencompetence.ldauthor.ui.views.organiser.activities.ActivitiesComposite;
import org.tencompetence.ldauthor.ui.views.organiser.environments.EnvironmentsComposite;
import org.tencompetence.ldauthor.ui.views.organiser.global.OrganiserComposite;
import org.tencompetence.ldauthor.ui.views.organiser.resources.ResourcesSimpleComposite;
import org.tencompetence.ldauthor.ui.views.organiser.roles.RolesComposite;

/**
 * Organiser View
 * 
 * @author Phillip Beauvoir
 * @version $Id: OrganiserView.java,v 1.61 2010/02/01 10:45:09 phillipus Exp $
 */
public class OrganiserView
extends ViewPart
implements IContextProvider, IOrganiserView
{

    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".organiserView"; //$NON-NLS-1$
    public static String HELP_ID = LDAuthorPlugin.PLUGIN_ID + ".organiserViewHelp"; //$NON-NLS-1$
    
    private OrganiserComposite fOrganiserComposite;
    private ActivitiesComposite fActivitiesComposite;
    private EnvironmentsComposite fEnvironmentsComposite;
    private RolesComposite fRolesComposite;
    private ResourcesSimpleComposite fResourcesComposite;
    
    private LDEditorContextDelegate fLDEditorContextDelegate;
    
    private CTabFolder folder;
    
    /**
     * Default constructor
     */
    public OrganiserView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);
        
        folder = new CTabFolder(parent, SWT.FLAT | SWT.BOTTOM);
        folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        folder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                IOrganiserComposite composite =  (IOrganiserComposite)((CTabItem)e.item).getControl();
                composite.updateFocus();
            }
        });
        
        // Local
        CTabItem tabItem = new CTabItem(folder, SWT.NONE);
        //tabItem.setText(Messages.OrganiserView_0);
        tabItem.setToolTipText(Messages.OrganiserView_1);
        tabItem.setImage(ImageFactory.getImage(ImageFactory.ICON_LD));
        fOrganiserComposite = new OrganiserComposite(this, folder, SWT.NULL);
        tabItem.setControl(fOrganiserComposite);
        
        // Activities
        tabItem = new CTabItem(folder, SWT.NONE);
        //tabItem.setText(Messages.OrganiserView_2);
        tabItem.setToolTipText(Messages.OrganiserView_3);
        tabItem.setImage(ImageFactory.getImage(ImageFactory.IMAGE_ACTIVITY_STRUCTURE_24));
        fActivitiesComposite = new ActivitiesComposite(this, folder, SWT.NULL);
        tabItem.setControl(fActivitiesComposite);
        
        // Environments
        tabItem = new CTabItem(folder, SWT.NONE);
        //tabItem.setText(Messages.OrganiserView_4);
        tabItem.setToolTipText(Messages.OrganiserView_5);
        tabItem.setImage(ImageFactory.getImage(ImageFactory.IMAGE_ENVIRONMENT_24));
        fEnvironmentsComposite = new EnvironmentsComposite(this, folder, SWT.NULL);
        tabItem.setControl(fEnvironmentsComposite);
        
        // Roles
        tabItem = new CTabItem(folder, SWT.NONE);
        //tabItem.setText(Messages.OrganiserView_6);
        tabItem.setToolTipText(Messages.OrganiserView_7);
        tabItem.setImage(ImageFactory.getImage(ImageFactory.IMAGE_LEARNER_24));
        fRolesComposite = new RolesComposite(this, folder, SWT.NULL);
        tabItem.setControl(fRolesComposite);

        // Resources
        tabItem = new CTabItem(folder, SWT.NONE);
        //tabItem.setText(Messages.OrganiserView_9);
        tabItem.setToolTipText(Messages.OrganiserView_10);
        tabItem.setImage(ImageFactory.getImage(ImageFactory.ICON_RESOURCE));
        fResourcesComposite = new ResourcesSimpleComposite(this, folder, SWT.NULL);
        tabItem.setControl(fResourcesComposite);
        
        folder.setSelection(0);
        fOrganiserComposite.updateFocus();
        
        // Register a master single selection provider used by all sub-composites
        // We have to do this because the TabbedPropertySheetPage registers itself with the View's single selection provider
        getSite().setSelectionProvider(new SelectionProviderAdapter());
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(fOrganiserComposite, HELP_ID);
        
        // Editor Context Listener
        fLDEditorContextDelegate = new LDEditorContextDelegate(getSite().getWorkbenchWindow()) {
            @Override
            public void setActiveEditor(ILDMultiPageEditor editor) {
                fActivitiesComposite.setActiveEditor(editor);
                fRolesComposite.setActiveEditor(editor);
                fEnvironmentsComposite.setActiveEditor(editor);
                fResourcesComposite.setActiveEditor(editor);
            }
        };
        
        // If there is already an active page then process it
        fLDEditorContextDelegate.checkEditorOpen();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        // We need this otherwise we get a "Prevented recursive attempt to activate part.." 
        // See http://dev.eclipse.org/newslists/news.eclipse.platform/msg54218.html
        folder.setFocus();
    }
    
    @SuppressWarnings("unchecked")
    public static IUndoContext getUndoContext(Class adapter) {
        if(adapter == OrganiserComposite.class) {
            return OrganiserComposite.getUndoContext();
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        /*
         * Return the focussed editor part
         */
        if(adapter == ILDEditorPart.class) {
            if(fLDEditorContextDelegate.getActiveEditor() != null) {
                return fLDEditorContextDelegate.getActiveEditor().getAdapter(ILDEditorPart.class);
            }
        }

        /*
         * Return the focussed multi-page editor if any
         */
        if(adapter == ILDMultiPageEditor.class) {
            return fLDEditorContextDelegate.getActiveEditor();
        }

        return super.getAdapter(adapter);
    }
    
    /**
     * Dispose of all objects
     */
    @Override
    public void dispose() {
        super.dispose();
        
        fOrganiserComposite.dispose();
        fActivitiesComposite.dispose();
        fEnvironmentsComposite.dispose();
        fRolesComposite.dispose();
        fResourcesComposite.dispose();
        
        // If Dirty, save...
        if(OrganiserIndex.getInstance().isDirty()) {
            OrganiserIndex.getInstance().save();
        }
        
        fLDEditorContextDelegate.dispose();
    }
    
    // =================================================================================
    //                       Contextual Help support
    // =================================================================================
    
    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getContextChangeMask()
     */
    public int getContextChangeMask() {
        return NONE;
    }

    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getContext(java.lang.Object)
     */
    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getSearchExpression(java.lang.Object)
     */
    public String getSearchExpression(Object target) {
        return Messages.OrganiserView_8;
    }
    
}