/*
 * Copyright (c) 2009, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.ui.views.conditions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IConditionType;
import org.tencompetence.imsldmodel.expressions.IConditionsType;
import org.tencompetence.imsldmodel.method.IConditionsModel;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.DropDownAction;
import org.tencompetence.ldauthor.ui.common.StackComposite;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.LDEditorContextDelegate;
import org.tencompetence.ldauthor.ui.views.IConditionsView;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorTitleComposite;


/**
 * Conditions View
 * 
 * @author Phillip Beauvoir
 * @version $Id: ConditionsView.java,v 1.20 2009/06/16 19:56:43 phillipus Exp $
 */
public class ConditionsView extends ViewPart
implements IContextProvider, IConditionsView, PropertyChangeListener{

    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".ldConditionsView"; //$NON-NLS-1$
    public static String HELP_ID = LDAuthorPlugin.PLUGIN_ID + ".ldConditionsViewHelp"; //$NON-NLS-1$
    
    private ILDModel fCurrentLDModel;

    private LDEditorContextDelegate fLDEditorContextDelegate;
    
    private InspectorTitleComposite fTitleBar;
    
    private ConditionsTreeViewer fTreeViewer;
    
    /**
     * Editor Stack Composite
     */
    private StackComposite fStackComposite;
    
    /*
     * Editor Panels
     */
    private Composite fBlankComposite;
    private ConditionsComposite fConditionsComposite;
    private ConditionComposite fConditionComposite;
    
    private DropDownAction fDropDown;
    
    private IAction fActionNewConditionsTypeAction;
    private IAction fActionNewConditionAction;
    private IAction fActionDelete;
    private IAction fActionSelectAll;
    private IAction fActionExpandAll;

    /*
     * Flag. Can be turned off when deleting large amounts of elements so that table update is not hindered
     */
    private boolean fNotifications = true;

    
    /**
     * Default constructor
     */
    public ConditionsView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);
        
        fTitleBar = new InspectorTitleComposite(parent);
        fTitleBar.setTitle(Messages.ConditionsView_0, null);
        GridData gd = new GridData(GridData.FILL, SWT.NULL, true, false);
        fTitleBar.setLayoutData(gd);
        
        SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        fTreeViewer = new ConditionsTreeViewer(sash);
        
        // Table selection listener
        fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Update actions
                updateActions(event.getSelection());
                // Select Editor Panel
                Object object = ((StructuredSelection)event.getSelection()).getFirstElement();
                updateEditorPanel(object);
            }
        });


        fStackComposite = new StackComposite(sash, SWT.NULL);
        AppFormToolkit.getInstance().adapt(fStackComposite);
        
        // Blank
        fBlankComposite = new Composite(fStackComposite, SWT.NULL);
        AppFormToolkit.getInstance().adapt(fBlankComposite);
        fStackComposite.setControl(fBlankComposite);
        
        sash.setWeights(new int[] { 30, 70 });
        
        makeActions();
        registerGlobalActions();
        makeLocalToolBarActions();
        hookContextMenu();
        
        // Editor Context Listener
        fLDEditorContextDelegate = new LDEditorContextDelegate(getSite().getWorkbenchWindow()) {
            @Override
            public void setActiveEditor(ILDMultiPageEditor editor) {
                if(fCurrentLDModel != null) {
                    fCurrentLDModel.removePropertyChangeListener(ConditionsView.this);
                }

                if(editor != null) {
                    fCurrentLDModel = (ILDModel)editor.getAdapter(ILDModel.class);
                    fCurrentLDModel.addPropertyChangeListener(ConditionsView.this);
                    fTitleBar.setTitle(Messages.ConditionsView_0 + 
                            " " + //$NON-NLS-1$
                            Messages.ConditionsView_1 +
                            " '" + //$NON-NLS-1$
                            editor.getTitle() +
                            "'", //$NON-NLS-1$
                            null); 
                    fTreeViewer.setInput(fCurrentLDModel);
                    fTreeViewer.expandAll();
                }
                else {
                    fCurrentLDModel = null;
                    fTitleBar.setTitle(Messages.ConditionsView_0, null);
                    fTreeViewer.setInput(null);
                }
                
                // Menu items
                fDropDown.setEnabled(fCurrentLDModel != null);
            }
        };
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        // If there is already an active page then process it
        fLDEditorContextDelegate.checkEditorOpen();
    }
    
    /**
     * Update the Editor Panel
     * @param object
     */
    private void updateEditorPanel(Object object) {
        // Conditions Set
        if(object instanceof IConditionsType) {
            if(fConditionsComposite == null) {
                fConditionsComposite = new ConditionsComposite(fStackComposite);
            }
            fConditionsComposite.setConditions((IConditionsType)object);
            fStackComposite.setControl(fConditionsComposite);
        }
        // If-Then-Else Condition
        else if(object instanceof IConditionType) {
            if(fConditionComposite == null) {
                ScrolledComposite scrollContainer = new ScrolledComposite(fStackComposite, SWT.V_SCROLL);
                scrollContainer.setExpandHorizontal(true);
                AppFormToolkit.getInstance().adapt(scrollContainer);
                fConditionComposite = new ConditionComposite(scrollContainer);
                scrollContainer.setContent(fConditionComposite);
            }
            
            fConditionComposite.setConditionType((IConditionType)object);
            fStackComposite.setControl(fConditionComposite.getParent());
            fConditionComposite.layout();
            fConditionComposite.pack();
        }
        
        // Blank Panel
        else {
            fStackComposite.setControl(fBlankComposite);
        }
    }
    
    /**
     * Register Global Action Handlers
     */
    private void registerGlobalActions() {
        IActionBars actionBars = getViewSite().getActionBars();
        
        // Register our interest in the global menu actions
        actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), fActionDelete);
        actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), fActionSelectAll);
    }
    
    /**
     * Make Local Toolbar items
     */
    private void makeLocalToolBarActions() {
        IActionBars bars = getViewSite().getActionBars();
        IToolBarManager manager = bars.getToolBarManager();

        manager.add(new Separator(IWorkbenchActionConstants.NEW_GROUP));
        manager.add(fDropDown);
        manager.add(new Separator());
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /**
     * Make local actions
     */
    private void makeActions() {
        fActionNewConditionsTypeAction = new Action(Messages.ConditionsView_2) {
            @Override
            public void run() {
                addNewConditionsType();
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.ECLIPSE_IMAGE_FOLDER),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        fActionNewConditionAction = new Action(Messages.ConditionsView_3) {
            @Override
            public void run() {
                addNewCondition();
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.ICON_CONDITION),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        fActionNewConditionAction.setEnabled(false);
        
        fDropDown = new DropDownAction(Messages.ConditionsView_4) {
            @Override
            public void run() {
                Menu menu = getMenu(getViewSite().getShell());
                menu.setVisible(true);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD);
            }

            @Override
            protected void addActions(Menu menu) {
                addActionToMenu(menu, fActionNewConditionsTypeAction);
                addActionToMenu(menu, fActionNewConditionAction);
            }
        };

        // Select All
        fActionSelectAll = new LDAuthorActionFactory.SelectAllAction() {
            @Override
            public void run() {
                fTreeViewer.getTree().selectAll();
            }
        };
        
        // Expand All
        fActionExpandAll = new Action(Messages.ConditionsView_5) {
            @Override
            public void run() {
                fTreeViewer.expandAll();
            }
        };
        
        // Delete
        fActionDelete = new LDAuthorActionFactory.DeleteAction() {
            @Override
            public void run() {
                handleDeleteAction();
            }
        };

        fDropDown.setEnabled(false);
    }
    
    /**
     * Add new Conditions Element
     */
    private void addNewConditionsType() {
        IConditionsType conditions = (IConditionsType)LDModelFactory.createModelObject(LDModelFactory.CONDITIONS, fCurrentLDModel);
        conditions.addNewCondition(); // Must have one If-Then-Else Condition
        fCurrentLDModel.getMethodModel().getConditionsModel().addChild(conditions);
        fTreeViewer.setSelection(new StructuredSelection(conditions), true);
        fTreeViewer.expandToLevel(conditions, 1);
        fCurrentLDModel.setDirty();
    }
    
    /**
     * Add new If-Then Condition to Conditions container
     */
    private void addNewCondition() {
        Object obj = ((IStructuredSelection)fTreeViewer.getSelection()).getFirstElement();
        
        IConditionsType parent = null;
        
        if(obj instanceof IConditionsType) {
            parent = (IConditionsType)obj;
        }
        else if(obj instanceof IConditionType) {
            parent = (IConditionsType)((IConditionType)obj).getParent();
        }
        
        if(parent != null) {
            IConditionType condition = parent.addNewCondition();
            fTreeViewer.refresh();
            fTreeViewer.setSelection(new StructuredSelection(condition), true);
            fCurrentLDModel.setDirty();
        }
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#LDPropertiesViewPopupMenu"); //$NON-NLS-1$
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
     * Fill the right-click menu for the Table
     * 
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        if(fCurrentLDModel == null) {
            return;
        }
        
        boolean isEmpty = fTreeViewer.getSelection().isEmpty();
        
        IMenuManager newMenu = new MenuManager(Messages.ConditionsView_6, "new");  //$NON-NLS-1$
        manager.add(newMenu);

        newMenu.add(fActionNewConditionsTypeAction);
        newMenu.add(fActionNewConditionAction);
        
        manager.add(new Separator());
        manager.add(fActionExpandAll);
        
        if(!isEmpty) {
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_START));
            manager.add(fActionDelete);
            manager.add(new Separator());
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_END));
        }
    }
    
    /**
     * Update the Local Actions depending on the selection 
     * @param selection
     */
    private void updateActions(ISelection selection) {
        //boolean isEmpty = selection.isEmpty();
        
        Object obj = ((IStructuredSelection)selection).getFirstElement();
        
        fActionDelete.setEnabled(obj instanceof IConditionsType || 
                obj instanceof IConditionType);
        
        fActionNewConditionAction.setEnabled(obj instanceof IConditionsType || 
                obj instanceof IConditionType);
    }

    
    /**
     * Delete Action
     */
    private void handleDeleteAction() {
        List<?> selected = ((IStructuredSelection)fTreeViewer.getSelection()).toList();
        
        if(!askUserDeleteResources(selected)) {
            return;
        }
        
        fNotifications = false;
        
        for(Object o : selected) {
            if(o instanceof IConditionsType) {
                IConditionsType condition = (IConditionsType)o;
                fCurrentLDModel.getMethodModel().getConditionsModel().removeChild(condition);
            }
            else if(o instanceof IConditionType) {
                IConditionType condition = (IConditionType)o;
                IConditionsType conditions = (IConditionsType)(condition.getParent());
                conditions.getConditions().remove(o);
            }
        }
        
        fTreeViewer.refresh();
        fCurrentLDModel.setDirty();
        
        fNotifications = true;
    }
    
    /**
     * Ask the user whether they wish to delete the given objects
     * 
     * @param objects The objects to delete
     * @return True if the user said yes, false if user says no or cancels
     */
    private boolean askUserDeleteResources(List<?> selected) {
        // Confirmation dialog
        return MessageDialog.openQuestion(
                Display.getDefault().getActiveShell(),
                "Delete", //$NON-NLS-1$
                selected.size() > 1 ?
                        Messages.ConditionsView_7
                        : 
                        Messages.ConditionsView_8);
    }
    
    @Override
    public void setFocus() {
        if(fTreeViewer != null) {
            fTreeViewer.getControl().setFocus();
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if(fNotifications) {
            if(evt.getSource() instanceof IConditionsType || evt.getSource() instanceof IConditionsModel) {
                fTreeViewer.refresh();
            }
        }
    }
    
    /**
     * Dispose of all objects
     */
    @Override
    public void dispose() {
        super.dispose();
        
        if(fCurrentLDModel != null) {
            fCurrentLDModel.removePropertyChangeListener(this);
            fCurrentLDModel = null;
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
        return Messages.ConditionsView_9;
    }
}
