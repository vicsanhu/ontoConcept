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
package org.tencompetence.ldauthor.ui.views.checker;

import java.util.List;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.method.IMethodModel;
import org.tencompetence.imsldmodel.resources.IResourceFileModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.ldmodel.util.LDChecker.ErrorCheckItem;
import org.tencompetence.ldauthor.ldmodel.util.LDChecker.ILDCheckerItem;
import org.tencompetence.ldauthor.ldmodel.util.LDChecker.WarningCheckItem;
import org.tencompetence.ldauthor.serialization.LDModelValidator;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.LDEditorContextDelegate;
import org.tencompetence.ldauthor.ui.views.ICheckerView;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;

/**
 * Checker View
 * 
 * @author Phillip Beauvoir
 * @version $Id: CheckerView.java,v 1.13 2009/07/03 10:20:19 phillipus Exp $
 */
public class CheckerView
extends ViewPart
implements IContextProvider, ICheckerView
{

    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".checkerView"; //$NON-NLS-1$
    public static String HELP_ID = LDAuthorPlugin.PLUGIN_ID + ".checkerViewHelp"; //$NON-NLS-1$
    
    /*
     * Actions
     */
    private IAction fActionRefresh;
    
    private CheckerTreeViewer fTreeViewer;
    
    private CheckerXMLErrorComposite fErrorViewer;
    
    private Section fSection;
    
    private IReCourseLDModel fLDModel;
    
    private LDEditorContextDelegate fLDEditorContextDelegate;

    /**
     * Default constructor
     */
    public CheckerView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        AppFormToolkit.getInstance().adapt(parent);
        parent.setLayout(new GridLayout());
        
        fSection = AppFormToolkit.getInstance().createSection(parent, Section.TITLE_BAR);
        fSection.setText(Messages.CheckerView_0);
        fSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        Composite client = AppFormToolkit.getInstance().createComposite(fSection);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);
        fSection.setClient(client);
        
        SashForm sashForm = new SashForm(client, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        makeActions();        
        registerGlobalActions();
        
        ToolBar toolBar = createToolBar(fSection);
        fSection.setTextClient(toolBar);
        
        // Use a parent composite for the table with TableColumnLayout.
        // This allows for auto-resizing of table columns
        Composite c1 = AppFormToolkit.getInstance().createComposite(sashForm);
        c1.setLayout(new TreeColumnLayout());
        c1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        fTreeViewer = new CheckerTreeViewer(c1, SWT.BORDER);
        
        fErrorViewer = new CheckerXMLErrorComposite(sashForm, SWT.BORDER);

        hookContextMenu();

        /*
         * Listen to Double-click Action
         */
        fTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                ILDMultiPageEditor editor = fLDEditorContextDelegate.getActiveEditor();
                
                if(editor == null) {
                    return;  // sanity check
                }
                
                ILDCheckerItem item = (ILDCheckerItem)((IStructuredSelection)event.getSelection()).getFirstElement();
                if(item instanceof ErrorCheckItem || item instanceof WarningCheckItem) {
                    Object component = item.getComponent();

                    // The LD - jump to main tab
                    if(component instanceof ILDModel) {
                        editor.setActivePage(ILDMultiPageEditor.OVERVIEW_TAB);
                    }
                    // Jump to Design/Method tab
                    else if(component instanceof IMethodModel) {
                        editor.setActivePage(ILDMultiPageEditor.METHOD_TAB);
                    }
                    // A Resource - jump to Resources tab
                    else if(component instanceof IResourceModel || component instanceof IResourceFileModel) {
                        editor.setActivePage(ILDMultiPageEditor.RESOURCES_TAB);
                        editor.getResourcesEditorPage().getResourcesTableViewer().setSelection(new StructuredSelection(component));
                    }
                    // Else open Inspector
                    else {
                        InspectorManager.getInstance().showInspector();
                        InspectorManager.getInstance().setInput(fLDEditorContextDelegate.getActiveEditor(), component);
                    }
                }
            }
        });
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(fTreeViewer.getControl(), HELP_ID);
        
        // Editor Context Listener
        fLDEditorContextDelegate = new LDEditorContextDelegate(getSite().getWorkbenchWindow()) {
            @Override
            public void setActiveEditor(ILDMultiPageEditor editor) {
                if(editor != null) {
                    setLDModel((IReCourseLDModel)editor.getAdapter(ILDModel.class), editor.getTitle());
                }
                else {
                    setLDModel(null, null);
                }
            }
        };
        
        // If there is already an active page then process it
        fLDEditorContextDelegate.checkEditorOpen();
    }
    
    /**
     * Make local actions
     */
    private void makeActions() {
        fActionRefresh = new LDAuthorActionFactory.RefreshAction() {
            private List<XMLException> errorList;

            @Override
            public void run() {
                if(fLDModel != null) {
                    // Update Tree
                    fTreeViewer.setLDModel(fLDModel);
                    
                    // Update XML Checker
                    BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                        public void run() {
                            LDModelValidator validator = new LDModelValidator(fLDModel);
                            try {
                                errorList = validator.validate();
                            }
                            catch(Exception ex) {
                                ex.printStackTrace();
                            }
                            
                            fErrorViewer.setInput(errorList);
                        }
                    });
                }
            }
        };
        
        fActionRefresh.setEnabled(false);
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        
        Menu menu = menuMgr.createContextMenu(fTreeViewer.getControl());
        fTreeViewer.getControl().setMenu(menu);
        
        getSite().registerContextMenu(menuMgr, fTreeViewer);
    }
    
    /**
     * Register Global Action Handlers
     */
    private void registerGlobalActions() {
        IActionBars actionBars = getViewSite().getActionBars();
        
        // Register our interest in the global menu actions
        actionBars.setGlobalActionHandler(ActionFactory.REFRESH.getId(), fActionRefresh);
    }
    
    private ToolBar createToolBar(Composite parent) {
        ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
        toolBar.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
        //fFormToolkit.adapt(toolBar);
        
        ToolBarManager toolBarmanager = new ToolBarManager(toolBar);

        toolBarmanager.add(fActionRefresh);
        
        toolBarmanager.update(true);
        
        return toolBar;
    }
    
    private void fillContextMenu(IMenuManager manager) {
        manager.add(fActionRefresh);
        
        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    @Override
    public void setFocus() {
        fTreeViewer.getControl().setFocus();
    }
    
    private void setLDModel(IReCourseLDModel ldModel, String title) {
        fLDModel = ldModel;
        
        if(ldModel != null) {
            fActionRefresh.setEnabled(true);
            fSection.setText(Messages.CheckerView_1 + title);
            fSection.layout();
        }
        else {
            fActionRefresh.setEnabled(false);
            fSection.setText(Messages.CheckerView_0);
        }
        
        fTreeViewer.setLDModel(fLDModel);
        // This is expensive so let user manually refresh
        fErrorViewer.setInput(null);
    }
    
    @Override
    public void dispose() {
        super.dispose();
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
        return Messages.CheckerView_2;
    }
}