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
package org.tencompetence.ldauthor;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.BaseNewWizardMenu;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.tencompetence.ldauthor.actions.OpenLDAction;
import org.tencompetence.ldauthor.actions.PackageLDAction;
import org.tencompetence.ldauthor.actions.PublishLDAction;
import org.tencompetence.ldauthor.actions.SaveAsTemplateAction;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.views.ViewManager;
import org.tencompetence.ldauthor.ui.views.IDM.IDMethod;
import org.tencompetence.ldauthor.ui.views.browser.BrowserView;
import org.tencompetence.ldauthor.ui.views.checker.CheckerView;
import org.tencompetence.ldauthor.ui.views.conditions.ConditionsView;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;
import org.tencompetence.ldauthor.ui.views.ldproperties.LDPropertiesView;
import org.tencompetence.ldauthor.ui.views.ontoconcept.ontoConcept;
import org.tencompetence.ldauthor.ui.views.ontoconcept.ontoZest;
import org.tencompetence.ldauthor.ui.views.organiser.OrganiserView;
import org.tencompetence.ldauthor.ui.views.preview.PreviewView;
import org.tencompetence.ldauthor.ui.views.publisher.PublisherView;


/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDAuthorActionBarAdvisor.java,v 1.48 2009/07/02 10:26:27 phillipus Exp $
 */
public class LDAuthorActionBarAdvisor
extends ActionBarAdvisor {
    private IWorkbenchAction fActionClose;
    private IWorkbenchAction fActionCloseAll;
    private IWorkbenchAction fActionQuit;
    private IWorkbenchAction fActionAbout;
    private IWorkbenchAction fActionUndo;
    private IWorkbenchAction fActionRedo;
    private IWorkbenchAction fActionCut;
    private IWorkbenchAction fActionCopy;
    private IWorkbenchAction fActionPaste;
    private IWorkbenchAction fActionDelete;
    private IWorkbenchAction fActionSelectAll;
    private IWorkbenchAction fActionFind;
    private IWorkbenchAction fActionRename;
    private IWorkbenchAction fActionSave;
    private IWorkbenchAction fActionSaveAsTemplate;
    private IWorkbenchAction fActionSaveAll;
    private IWorkbenchAction fActionNewWizardDropDown;
    private IWorkbenchAction fActionResetPerspective;
    private IWorkbenchAction fActionOpen;
    private IWorkbenchAction fActionRefresh;
    
    private IAction fShowOrganiserView;
    private IAction fShowInspectorView;
    private IAction fShowPreView;
    private IAction fShowCheckerView;
    private IAction fShowPublisherView;
    private IAction fShowBrowserView;
    private IAction fShowLDPropertiesView;
    private IAction fShowConditionsView;
    private IAction fShowIDmethodView;
    private IAction fShowontoConceptView;
    private IAction fShowontoZestView;
    private IWorkbenchAction fActionPackageLD;
    private IWorkbenchAction fActionPublishLD;
    
    private BaseNewWizardMenu fNewWizardMenu;
    
    // Save some Workbench Images for re-use
    public static ImageDescriptor NEWWIZ_IMAGEDESCRIPTOR;
    
    
    /**
     * Constructor
     * @param configurer
     */
    public LDAuthorActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.ActionBarAdvisor#makeActions(org.eclipse.ui.IWorkbenchWindow)
     */
    @Override
    protected void makeActions(final IWorkbenchWindow window) {
        // New Wizard drop-down
        fActionNewWizardDropDown = ActionFactory.NEW_WIZARD_DROP_DOWN.create(window);
        register(fActionNewWizardDropDown);
        // Re-use this, it's a bit of a kludge but there is restricted access to it
        NEWWIZ_IMAGEDESCRIPTOR = fActionNewWizardDropDown.getImageDescriptor();
        
        // New... Wizard menu
        fNewWizardMenu = new BaseNewWizardMenu(window, null);
        
        // Open
        fActionOpen = new OpenLDAction(window);

        // Close
        fActionClose = ActionFactory.CLOSE.create(window);
        register(fActionClose);

        // Close All
        fActionCloseAll = ActionFactory.CLOSE_ALL.create(window);
        register(fActionCloseAll);

        // Save
        fActionSave = ActionFactory.SAVE.create(window);
        register(fActionSave);

        // Save As Template
        fActionSaveAsTemplate = SaveAsTemplateAction.INSTANCE.create(window);
        register(fActionSaveAsTemplate);

        // Save All
        fActionSaveAll = ActionFactory.SAVE_ALL.create(window);
        register(fActionSaveAll);
        
        // Undo
        fActionUndo = ActionFactory.UNDO.create(window);
        register(fActionUndo);

        // Redo
        fActionRedo = ActionFactory.REDO.create(window);
        register(fActionRedo);
        
        // Cut
        fActionCut = ActionFactory.CUT.create(window);
        register(fActionCut);
        
        // Copy
        fActionCopy = ActionFactory.COPY.create(window);
        register(fActionCopy);
        
        // Paste
        fActionPaste = ActionFactory.PASTE.create(window);
        register(fActionPaste);
        
        // Delete
        fActionDelete = ActionFactory.DELETE.create(window);
        register(fActionDelete);
        
        // Select All
        fActionSelectAll = ActionFactory.SELECT_ALL.create(window);
        register(fActionSelectAll);
        
        // Find
        fActionFind = ActionFactory.FIND.create(window);
        register(fActionFind);
        
        // Rename
        fActionRename = ActionFactory.RENAME.create(window);
        register(fActionRename);
        
        // Quit
        fActionQuit = ActionFactory.QUIT.create(window);
        register(fActionQuit);
        
        // About
        fActionAbout = ActionFactory.ABOUT.create(window);
        register(fActionAbout);

        // Reset Perspective
        fActionResetPerspective = ActionFactory.RESET_PERSPECTIVE.create(window);
        fActionResetPerspective.setText(Messages.LDAuthorActionBarAdvisor_0);
        register(fActionResetPerspective);
        
        // Refresh
        fActionRefresh = ActionFactory.REFRESH.create(window);
        register(fActionRefresh);
        
        // Package
        fActionPackageLD = new PackageLDAction(window);
        register(fActionPackageLD);
        
        // Publish
        fActionPublishLD = new PublishLDAction(window);
        register(fActionPublishLD);

        // Show/Hide Organiser View
        fShowOrganiserView = new Action(Messages.LDAuthorActionBarAdvisor_1) {
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public void run() {
                ViewManager.toggleViewPart(OrganiserView.ID, false);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_ORGANISER);
            }
        };
        
        // Show/Hide Inspector View
        fShowInspectorView = new Action(Messages.LDAuthorActionBarAdvisor_2) {
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public void run() {
                InspectorManager.getInstance().toggleInspector();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_INSPECTOR);
            }
        };
        
        // Show/Hide Checker View
        fShowCheckerView = new Action(Messages.LDAuthorActionBarAdvisor_3) {
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public void run() {
                ViewManager.toggleViewPart(CheckerView.ID, false);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_TICK);
            }
        };
        
        // Show/Hide Publisher View
        fShowPublisherView = new Action(Messages.LDAuthorActionBarAdvisor_4) {
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public void run() {
                ViewManager.toggleViewPart(PublisherView.ID, true);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_PUBLISHER);
            }
        };
        
        // Show/Hide PreView
        fShowPreView = new Action(Messages.LDAuthorActionBarAdvisor_13) {
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public void run() {
                ViewManager.toggleViewPart(PreviewView.ID, false);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_VIEW);
            }
        };
        
        // Show/Hide Browser 
        fShowBrowserView = new Action(Messages.LDAuthorActionBarAdvisor_14) {
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public void run() {
                ViewManager.toggleViewPart(BrowserView.ID, false);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_BROWSER);
            }
        };
        
        // Show/Hide LD Properties View 
        fShowLDPropertiesView = new Action(Messages.LDAuthorActionBarAdvisor_15) {
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public void run() {
                ViewManager.toggleViewPart(LDPropertiesView.ID, false);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_PROPERTY);
            }
        };
        
        // Show/Hide LD Conditions View 
        fShowConditionsView = new Action(Messages.LDAuthorActionBarAdvisor_16) {
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public void run() {
                ViewManager.toggleViewPart(ConditionsView.ID, false);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_CONDITION);
            }
        };
        
       // Show/Hide ID methods View 
       fShowIDmethodView = new Action(Messages.LDAuthorActionBarAdvisor_18) {
            
        	@Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            
            public void run() {
                ViewManager.toggleViewPart(IDMethod.ID, false);
            }
            
           @Override
           public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_IDMethod);
            }
        };
     
        //Show/Hide ontoConcept View
        fShowontoConceptView = new Action(Messages.LDAuthorActionBarAdvisor_19){
        	@Override
			public String getToolTipText() {
        		return getText();
        	}
        	@Override
			public void run() {
        		ViewManager.toggleViewPart(ontoConcept.ID, false);
        	}
        	@Override
			public ImageDescriptor getImageDescriptor() {
        		return ImageFactory.getImageDescriptor(ImageFactory.ICON_ONTOCONCEPT);
        	}
        };
        
        //Show/Hide ontoZestView
        fShowontoZestView = new Action(Messages.LDAuthorActionBarAdvisor_20){
        	@Override
			public String getToolTipText() {
        		return getText();
        	}
        	@Override
			public void run() {
        		ViewManager.toggleViewPart(ontoZest.ID, false);
        	}
        	@Override
			public ImageDescriptor getImageDescriptor() {
        		return ImageFactory.getImageDescriptor(ImageFactory.ICON_ONTOZEST);
        	}
        };
        
     
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillMenuBar(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    protected void fillMenuBar(IMenuManager menuBar) {
        // File
        menuBar.add(createFileMenu());
        
        // Edit
        menuBar.add(createEditMenu());
        
        // Tools
        menuBar.add(createToolsMenu());
        
        // Window
        menuBar.add(createWindowMenu());

        // Help
        menuBar.add(createHelpMenu());
   
        // IDmethod
        menuBar.add(createIDmethodMenu());
        
        // ontoConcept
        menuBar.add(createontoConceptMenu());
        
        // ontoZest
        menuBar.add(createontoZestMenu());
           
    }
    
	/**
     * Create the File menu
     * @return
     */
    private MenuManager createFileMenu() {
        MenuManager menu = new MenuManager(Messages.LDAuthorActionBarAdvisor_5, IWorkbenchActionConstants.M_FILE);
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
        
        IMenuManager newMenu = new MenuManager(Messages.LDAuthorActionBarAdvisor_6, "new"); //$NON-NLS-1$
        newMenu.add(new Separator("new")); //$NON-NLS-1$
        newMenu.add(fNewWizardMenu);
        newMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        menu.add(newMenu);

        menu.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
        menu.add(fActionOpen);
        menu.add(new Separator());

        menu.add(fActionClose);
        menu.add(fActionCloseAll);
        menu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
        menu.add(new Separator());

        menu.add(fActionSave);
        menu.add(fActionSaveAsTemplate);
        menu.add(fActionSaveAll);
        menu.add(new Separator());
        menu.add(fActionPackageLD);
        menu.add(fActionPublishLD);
        menu.add(new GroupMarker(IWorkbenchActionConstants.SAVE_EXT));
        menu.add(new Separator());
        
        menu.add(fActionRefresh);
        menu.add(new Separator());
        
        menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        
        // Not needed on a Mac
        if(!Platform.getOS().equals(Platform.OS_MACOSX)) {
        	menu.add(fActionQuit);
        }
        
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
        
        return menu;
    }

    /**
     * Create the Editor menu
     * @return
     */
    private MenuManager createEditMenu() {
        IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
        
        MenuManager menu = new MenuManager(Messages.LDAuthorActionBarAdvisor_7, IWorkbenchActionConstants.M_EDIT);
        menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_START));

        menu.add(fActionUndo);
        menu.add(fActionRedo);
        menu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
        menu.add(new Separator());

        menu.add(fActionCut);
        menu.add(fActionCopy);
        menu.add(fActionPaste);
        menu.add(fActionDelete);
        menu.add(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));
        menu.add(new Separator());

        menu.add(fActionSelectAll);
        menu.add(new Separator());
        
        menu.add(fActionFind);
        menu.add(new GroupMarker(IWorkbenchActionConstants.FIND_EXT));
        menu.add(new Separator());
        
        menu.add(fActionRename);

        menu.add(new GroupMarker(IWorkbenchActionConstants.ADD_EXT));

        menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
        
        /*
         * On a Mac, Eclipse adds a "Preferences" menu item under the application menu bar.
         * However, it does nothing unless you add the Preferences menu item manually elsewhere.
         * See - http://dev.eclipse.org/newslists/news.eclipse.platform.rcp/msg30749.html
         * 
         */
    	IWorkbenchAction preferenceAction = ActionFactory.PREFERENCES.create(window);
    	ActionContributionItem item = new ActionContributionItem(preferenceAction);
    	item.setVisible(!Platform.getOS().equals(Platform.OS_MACOSX));
    	
    	menu.add(new Separator());
    	menu.add(item);

        menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        return menu;
    }

    /**
     * Create the Tools Menu
     * @return
     */
    private MenuManager createToolsMenu() {
        MenuManager menu = new MenuManager(Messages.LDAuthorActionBarAdvisor_8, "tools"); //$NON-NLS-1$
        
        //menu.add(_actionFooBar);
        menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        
        return menu;
    }
    
    /**
     * Create Window Menu
     * @return
     */
    private MenuManager createWindowMenu() {
        IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();

        MenuManager menu = new MenuManager(Messages.LDAuthorActionBarAdvisor_9, IWorkbenchActionConstants.M_WINDOW);

        //MenuManager perspectiveMenu = new MenuManager(Messages.LDAuthorActionBarAdvisor_10, "openPerspective"); //$NON-NLS-1$
        //IContributionItem perspectiveList = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
        //perspectiveMenu.add(perspectiveList);
        //menu.add(perspectiveMenu);
        
        //MenuManager showViewMenu = new MenuManager(Messages.LDAuthorActionBarAdvisor_11);
        //menu.add(showViewMenu);

        //IContributionItem viewList = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
        //showViewMenu.add(viewList);

        //menu.add(new Separator("PerspectiveMenu")); //$NON-NLS-1$
        
        menu.add(fShowOrganiserView);
        menu.add(fShowInspectorView);
        menu.add(fShowPreView);
        menu.add(fShowCheckerView);
        menu.add(fShowPublisherView);
        menu.add(fShowBrowserView);
        menu.add(fShowLDPropertiesView);
        menu.add(fShowConditionsView);
        menu.add(fShowIDmethodView);
        
        //Agrega la accion de ontoConcept al menu Window
        menu.add(fShowontoConceptView);
        
        //Agrega la accion de ontoZest al menu Window
        menu.add(fShowontoZestView);
        
        menu.add(new GroupMarker("ldauthor_views_additions")); //$NON-NLS-1$
        
        menu.add(new Separator());

        menu.add(fActionResetPerspective);

        menu.add(new Separator("nav")); //$NON-NLS-1$

        MenuManager navigationMenu = new MenuManager(Messages.LDAuthorActionBarAdvisor_17);
        menu.add(navigationMenu);

        IAction a = ActionFactory.NEXT_EDITOR.create(window);
        register(a);
        navigationMenu.add(a);

        a = ActionFactory.PREVIOUS_EDITOR.create(window);
        register(a);
        navigationMenu.add(a);

        navigationMenu.add(new Separator());

        a = ActionFactory.NEXT_PART.create(window);
        register(a);
        navigationMenu.add(a);

        a = ActionFactory.PREVIOUS_PART.create(window);
        register(a);
        navigationMenu.add(a);

        menu.add(ContributionItemFactory.OPEN_WINDOWS.create(window));

        menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

        
        return menu;
    }

    /**
     * Create the Help menu
     * @return
     */
    private MenuManager createHelpMenu() {
        IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
        
        MenuManager menu = new MenuManager(Messages.LDAuthorActionBarAdvisor_12, IWorkbenchActionConstants.M_HELP);
        
        menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));
        
        menu.add(ActionFactory.INTRO.create(window));
        menu.add(new Separator());
        
        menu.add(ActionFactory.HELP_CONTENTS.create(window));
        menu.add(ActionFactory.HELP_SEARCH.create(window));
        menu.add(ActionFactory.DYNAMIC_HELP.create(window));
        
        menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
        
        /*
         * On a Mac, Eclipse adds an "About" menu item under the application menu bar.
         * However, it does nothing unless you add the About menu item manually elsewhere.
         * See - http://dev.eclipse.org/newslists/news.eclipse.platform.rcp/msg30749.html
         * 
         */
    	ActionContributionItem item = new ActionContributionItem(fActionAbout);
    	item.setVisible(!Platform.getOS().equals(Platform.OS_MACOSX));
    	
    	menu.add(new Separator());
    	menu.add(item);

        return menu;
    }

    private MenuManager createIDmethodMenu() {
        IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
        
        MenuManager menu = new MenuManager(Messages.LDAuthorActionBarAdvisor_18, "IDMethod");//$NON-NLS-1$
        
        /*
        menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));
        
        menu.add(ActionFactory.INTRO.create(window));
        menu.add(new Separator());
        
        menu.add(ActionFactory.HELP_CONTENTS.create(window));
        menu.add(ActionFactory.HELP_SEARCH.create(window));
        menu.add(ActionFactory.DYNAMIC_HELP.create(window));
        
        menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
        */
        /*
         * On a Mac, Eclipse adds an "About" menu item under the application menu bar.
         * However, it does nothing unless you add the About menu item manually elsewhere.
         * See - http://dev.eclipse.org/newslists/news.eclipse.platform.rcp/msg30749.html
         * 
         */
    	/*
        ActionContributionItem item = new ActionContributionItem(fActionAbout);
    	item.setVisible(!Platform.getOS().equals(Platform.OS_MACOSX));
    	
    	menu.add(new Separator());
    	menu.add(item);
      */
        return menu;
    }
    
    private MenuManager createontoConceptMenu() {
		// TODO Auto-generated method stub
    	IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
    	MenuManager menu = new MenuManager(Messages.LDAuthorActionBarAdvisor_19, "ontoConcept"); //$NON-NLS-1$
		return menu;
	}
    
    private MenuManager createontoZestMenu() {
		// TODO Auto-generated method stub
    	IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
    	MenuManager menu = new MenuManager(Messages.LDAuthorActionBarAdvisor_20, "ontoZest"); //$NON-NLS-1$
		return menu;
	}
    
  
    
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface.action.ICoolBarManager)
     */
    @Override
    protected void fillCoolBar(ICoolBarManager coolBarManager) {
        ToolBarManager toolBarFile = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBarManager.add(new ToolBarContributionItem(toolBarFile, "toolbar_file")); //$NON-NLS-1$
        
        toolBarFile.add(new GroupMarker("start")); //$NON-NLS-1$
        toolBarFile.add(fActionNewWizardDropDown);
        toolBarFile.add(fActionOpen);
        toolBarFile.add(fActionSave);
        toolBarFile.add(new GroupMarker("end")); //$NON-NLS-1$
        
        IToolBarManager toolBarEdit = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBarManager.add(new ToolBarContributionItem(toolBarEdit, "toolbar_edit")); //$NON-NLS-1$
        
        toolBarEdit.add(new GroupMarker("start")); //$NON-NLS-1$
        toolBarEdit.add(fActionUndo);
        toolBarEdit.add(fActionRedo);
        toolBarEdit.add(fActionDelete);
        toolBarEdit.add(new GroupMarker("end")); //$NON-NLS-1$
        
        //toolBarEdit.add(fActionCut);
        //toolBarEdit.add(fActionCopy);
        //toolBarEdit.add(fActionPaste);
        //toolBarEdit.add(fActionDelete);
        
        //toolBarEdit.add(new Separator());
        
        IToolBarManager toolBarViews = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBarManager.add(new ToolBarContributionItem(toolBarViews, "toolbar_views")); //$NON-NLS-1$
        
        toolBarViews.add(new GroupMarker("start")); //$NON-NLS-1$
        toolBarViews.add(fShowOrganiserView);
        toolBarViews.add(fShowInspectorView);
        toolBarViews.add(fShowPreView);
        toolBarViews.add(fShowCheckerView);
        toolBarViews.add(fShowPublisherView);
        toolBarViews.add(new Separator());
        toolBarViews.add(fShowLDPropertiesView);
        toolBarViews.add(fShowConditionsView);
        toolBarViews.add(new Separator());
        // menu ID Method
        toolBarViews.add(fShowIDmethodView);
        
        //Agrega a ontoConcept al menu de navegacion 
        toolBarViews.add(fShowontoConceptView);
        toolBarViews.add(new Separator());
        
        //Agrega a ontoZest al menu de navegacion 
        toolBarViews.add(fShowontoZestView);
        toolBarViews.add(new Separator());

        
        toolBarViews.add(new GroupMarker("end")); //$NON-NLS-1$
    }

    /**
     * Dispose of these
     */
    @Override
    public void dispose() {
        super.dispose();
        fNewWizardMenu.dispose();
    }
}
