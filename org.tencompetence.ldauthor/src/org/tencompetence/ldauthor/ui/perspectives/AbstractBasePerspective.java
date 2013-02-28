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
package org.tencompetence.ldauthor.ui.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.tencompetence.ldauthor.LDAuthorPlugin;


/**
 * Base Plex Perspective
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractBasePerspective.java,v 1.6 2009/11/16 14:29:44 phillipus Exp $
 */
public abstract class AbstractBasePerspective
implements IPerspectiveFactory
{
    /*
     * Folder Layouts
     */
    //IPlaceholderFolderLayout folderLayoutRight;
    IFolderLayout folderLayoutRight;
    IPlaceholderFolderLayout folderLayoutBottom;
    
    static String FOLDER_RIGHT = "folderRight"; //$NON-NLS-1$
    static String FOLDER_BOTTOM = "folderBottom"; //$NON-NLS-1$
    
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
     */
    public void createInitialLayout(IPageLayout layout) {
        // Show Editor area
        layout.setEditorAreaVisible(true);
        
        addFolderLayouts(layout);
        addAllPlaceHolders(layout);
        addAllPerspectiveShortcuts(layout);
    }
    
    /**
     * Add all registered View shortcuts
     * @param layout
     */
    protected void addAllViewShortcuts(IPageLayout layout) {
        IViewDescriptor[] descriptors = PlatformUI.getWorkbench().getViewRegistry().getViews();
        for(int i = 0; i < descriptors.length; i++) {
            IViewDescriptor descriptor = descriptors[i];
            if(descriptor.getId().startsWith(LDAuthorPlugin.PLUGIN_ID)) {
                layout.addShowViewShortcut(descriptor.getId());
            }
        }
    }

    /**
     * Add all registered Perspective shortcuts except the current Perspective
     * @param layout
     */
    protected void addAllPerspectiveShortcuts(IPageLayout layout) {
        IPerspectiveDescriptor[] descriptors = PlatformUI.getWorkbench().getPerspectiveRegistry().getPerspectives();
        for(int i = 0; i < descriptors.length; i++) {
            IPerspectiveDescriptor descriptor = descriptors[i];
            if(!descriptor.getId().equals(getID())) {
                layout.addPerspectiveShortcut(descriptor.getId());
            }
        }
    }
    
    /**
     * Add all registered "New" wizard shortcuts
     * @param layout
     */
    protected void addAllNewWizardShortcuts(IPageLayout layout) {
        IWizardCategory[] categories = PlatformUI.getWorkbench().getNewWizardRegistry().getRootCategory().getCategories();
        for(int i = 0; i < categories.length; i++) {
            IWizardCategory category = categories[i];
            IWizardDescriptor[] descriptors = category.getWizards();
            for(int j = 0; j < descriptors.length; j++) {
                IWizardDescriptor descriptor = descriptors[j];
                layout.addNewWizardShortcut(descriptor.getId());
            }
        }
    }

    /**
     * Add the main folder layout areas
     * @param layout
     */
    protected void addFolderLayouts(IPageLayout layout) {
        //folderLayoutLeft = layout.createFolder(FOLDER_LEFT, IPageLayout.LEFT, .23f, IPageLayout.ID_EDITOR_AREA);
        
        //folderLayoutRight = layout.createPlaceholderFolder(FOLDER_RIGHT, IPageLayout.RIGHT, .77f, IPageLayout.ID_EDITOR_AREA);
        folderLayoutRight = layout.createFolder(FOLDER_RIGHT, IPageLayout.RIGHT, .77f, IPageLayout.ID_EDITOR_AREA);
        
        folderLayoutBottom = layout.createPlaceholderFolder(FOLDER_BOTTOM, IPageLayout.BOTTOM, .6f, IPageLayout.ID_EDITOR_AREA);
    }
    
    /**
     * Add all the main Tree views
     * @param layout
     */
    protected void addAllTreeViews(IPageLayout layout) {
        if(folderLayoutRight == null) {
            addFolderLayouts(layout);
        }
    }
    
    /**
     * Add all the place holders for the views
     * @param layout
     */
    protected void addAllPlaceHolders(IPageLayout layout) {
        if(folderLayoutRight == null) {
            addFolderLayouts(layout);
        }
    }
    
    /**
     * @return The Perspective ID
     */
    public abstract String getID();
}
