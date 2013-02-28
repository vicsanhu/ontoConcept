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
package org.tencompetence.ldauthor.ui.views.ldproperties;

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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
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
import org.eclipse.swt.dnd.Clipboard;
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
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.properties.IGlobalPersonalPropertyModel;
import org.tencompetence.imsldmodel.properties.IGlobalPropertyModel;
import org.tencompetence.imsldmodel.properties.ILocalPersonalPropertyModel;
import org.tencompetence.imsldmodel.properties.ILocalPropertyModel;
import org.tencompetence.imsldmodel.properties.ILocalRolePropertyModel;
import org.tencompetence.imsldmodel.properties.IPropertyGroupModel;
import org.tencompetence.imsldmodel.properties.IPropertyRefModel;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.DropDownAction;
import org.tencompetence.ldauthor.ui.common.StackComposite;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.LDEditorContextDelegate;
import org.tencompetence.ldauthor.ui.views.ILDPropertiesView;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorTitleComposite;

/**
 * Level B Properties View
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDPropertiesView.java,v 1.20 2009/06/10 13:12:40 phillipus Exp $
 */
public class LDPropertiesView
extends ViewPart
implements IContextProvider, ILDPropertiesView, PropertyChangeListener
{

    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".ldPropertiesView"; //$NON-NLS-1$
    public static String HELP_ID = LDAuthorPlugin.PLUGIN_ID + ".ldPropertiesViewHelp"; //$NON-NLS-1$
    
    private ILDModel fCurrentLDModel;
    
    private DropDownAction fDropDown;
    
    private IAction fActionShowCopyToClipboardDialog;
    
    private IAction fActionNewLocalPropertyAction;
    private IAction fActionNewLocalPersonalPropertyAction;
    private IAction fActionNewLocalRolePropertyAction;
    private IAction fActionNewGlobalPropertyAction;
    private IAction fActionNewGlobalPersonalPropertyAction;
    private IAction fActionNewPropertyGroupAction;
    
    private IAction fActionDelete;
    private IAction fActionSelectAll;
    
    private LDEditorContextDelegate fLDEditorContextDelegate;
    
    private InspectorTitleComposite fTitleBar;

    
    /**
     * Tree Table
     */
    private LDPropertiesTreeTable fTreeTable;
    
    /**
     * Editor Stack Composite
     */
    private StackComposite fStackComposite;
    
    /*
     * Editor Panels
     */
    private BlankComposite fBlankComposite;
    private LocalPropertyEditorComposite fLocalPropertyEditorComposite;
    private LocalPersonalPropertyEditorComposite fLocalPersonalPropertyEditorComposite;
    private LocalRolePropertyEditorComposite fLocalRolePropertyEditorComposite;
    private GlobalPropertyEditorComposite fGlobalPropertyEditorComposite;
    private GlobalPersonalPropertyEditorComposite fGlobalPersonalPropertyEditorComposite;
    private PropertyGroupEditorComposite fPropertyGroupEditorComposite;
    
    private PropertiesTableDragDropHandler fDragDropHandler;
    
    private Clipboard fClipboard = new Clipboard(null);
    
    /*
     * Flag. Can be turned off when deleting large amounts of elements so that table update is not hindered
     */
    private boolean fNotifications = true;
    
    /**
     * Default constructor
     */
    public LDPropertiesView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);
        
        fTitleBar = new InspectorTitleComposite(parent);
        fTitleBar.setTitle(Messages.LDPropertiesView_0, null);
        GridData gd = new GridData(GridData.FILL, SWT.NULL, true, false);
        fTitleBar.setLayoutData(gd);
        
        SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Use a parent composite for the table with TreeColumnLayout.
        // This allows for auto-resizing of table columns
        Composite composite = new Composite(sash, SWT.NULL);
        composite.setLayout(new TreeColumnLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        fTreeTable = new LDPropertiesTreeTable(composite);
        
        fStackComposite = new StackComposite(sash, SWT.NULL);
        AppFormToolkit.getInstance().adapt(fStackComposite);
        
        // Blank
        fBlankComposite = new BlankComposite(fStackComposite, SWT.NULL);
        AppFormToolkit.getInstance().adapt(fBlankComposite);
        fStackComposite.setControl(fBlankComposite);
        
        sash.setWeights(new int[] { 50, 50 });

        makeActions();
        registerGlobalActions();
        makeLocalToolBarActions();
        hookContextMenu();
        
        // Table selection listener
        fTreeTable.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Update actions
                updateActions(event.getSelection());
                // Select Editor Panel
                Object object = ((StructuredSelection)event.getSelection()).getFirstElement();
                updateEditorPanel(object);
            }
        });
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        // Editor Context Listener
        fLDEditorContextDelegate = new LDEditorContextDelegate(getSite().getWorkbenchWindow()) {
            @Override
            public void setActiveEditor(ILDMultiPageEditor editor) {
                if(fCurrentLDModel != null) {
                    fCurrentLDModel.removePropertyChangeListener(LDPropertiesView.this);
                }

                if(editor != null) {
                    fCurrentLDModel = (ILDModel)editor.getAdapter(ILDModel.class);
                    fCurrentLDModel.addPropertyChangeListener(LDPropertiesView.this);
                    fTreeTable.setInput(fCurrentLDModel);
                    fDragDropHandler.setLDModel(fCurrentLDModel);
                    fTitleBar.setTitle(Messages.LDPropertiesView_0 + " " + Messages.LDPropertiesView_9 + " '" + editor.getTitle() + "'", null);  //$NON-NLS-1$  //$NON-NLS-2$ //$NON-NLS-3$
                }
                else {
                    fCurrentLDModel = null;
                    fTreeTable.setInput(null);
                    fTitleBar.setTitle(Messages.LDPropertiesView_0, null);
                }

                // Menu items
                fDropDown.setEnabled(fCurrentLDModel != null);
            }
        };
        
        // DnD
        fDragDropHandler = new PropertiesTableDragDropHandler(fTreeTable);
        
        // If there is already an active page then process it
        fLDEditorContextDelegate.checkEditorOpen();
    }
    
    /**
     * Update the Editor Panel
     * @param object
     */
    private void updateEditorPanel(Object object) {
        if(object instanceof ILDModelObjectReference) {
            object = ((ILDModelObjectReference)object).getLDModelObject();
        }
        
        // Local Property
        if(object instanceof ILocalPropertyModel) {
            if(fLocalPropertyEditorComposite == null) {
                fLocalPropertyEditorComposite = new LocalPropertyEditorComposite(fStackComposite, SWT.NULL);
                AppFormToolkit.getInstance().adapt(fLocalPropertyEditorComposite);
            }
            fLocalPropertyEditorComposite.setProperty((IPropertyTypeModel)object);
            fStackComposite.setControl(fLocalPropertyEditorComposite);
        }
        
        // Local Personal Property
        else if(object instanceof ILocalPersonalPropertyModel) {
            if(fLocalPersonalPropertyEditorComposite == null) {
                fLocalPersonalPropertyEditorComposite = new LocalPersonalPropertyEditorComposite(fStackComposite, SWT.NULL);
                AppFormToolkit.getInstance().adapt(fLocalPersonalPropertyEditorComposite);
            }
            fLocalPersonalPropertyEditorComposite.setProperty((IPropertyTypeModel)object);
            fStackComposite.setControl(fLocalPersonalPropertyEditorComposite);
        }

        // Local Role Property
        else if(object instanceof ILocalRolePropertyModel) {
            if(fLocalRolePropertyEditorComposite == null) {
                fLocalRolePropertyEditorComposite = new LocalRolePropertyEditorComposite(fStackComposite, SWT.NULL);
                AppFormToolkit.getInstance().adapt(fLocalRolePropertyEditorComposite);
            }
            fLocalRolePropertyEditorComposite.setProperty((IPropertyTypeModel)object);
            fStackComposite.setControl(fLocalRolePropertyEditorComposite);
        }

        // Global Property
        else if(object instanceof IGlobalPropertyModel) {
            if(fGlobalPropertyEditorComposite == null) {
                fGlobalPropertyEditorComposite = new GlobalPropertyEditorComposite(fStackComposite, SWT.NULL);
                AppFormToolkit.getInstance().adapt(fGlobalPropertyEditorComposite);
            }
            fGlobalPropertyEditorComposite.setProperty((IPropertyTypeModel)object);
            fStackComposite.setControl(fGlobalPropertyEditorComposite);
        }
        
        // Global Personal Property
        else if(object instanceof IGlobalPersonalPropertyModel) {
            if(fGlobalPersonalPropertyEditorComposite == null) {
                fGlobalPersonalPropertyEditorComposite = new GlobalPersonalPropertyEditorComposite(fStackComposite, SWT.NULL);
                AppFormToolkit.getInstance().adapt(fGlobalPersonalPropertyEditorComposite);
            }
            fGlobalPersonalPropertyEditorComposite.setProperty((IPropertyTypeModel)object);
            fStackComposite.setControl(fGlobalPersonalPropertyEditorComposite);
        }
        
        // Property Group
        else if(object instanceof IPropertyGroupModel) {
            if(fPropertyGroupEditorComposite == null) {
                fPropertyGroupEditorComposite = new PropertyGroupEditorComposite(fStackComposite, SWT.NULL);
                AppFormToolkit.getInstance().adapt(fPropertyGroupEditorComposite);
            }
            fPropertyGroupEditorComposite.setPropertyGroup((IPropertyGroupModel)object);
            fStackComposite.setControl(fPropertyGroupEditorComposite);
        }

        // Blank Panel
        else {
            fStackComposite.setControl(fBlankComposite);
        }
    }

    @Override
    public void setFocus() {
        if(fTreeTable != null) {
            fTreeTable.getControl().setFocus();
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
        fActionNewLocalPropertyAction = new Action(Messages.LDPropertiesView_2) {
            @Override
            public void run() {
                addNewObject(LDModelFactory.createModelObject(LDModelFactory.LOCAL_PROPERTY, fCurrentLDModel));
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.ICON_PROPERTY),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        fActionNewLocalPersonalPropertyAction = new Action(Messages.LDPropertiesView_3) {
            @Override
            public void run() {
                addNewObject(LDModelFactory.createModelObject(LDModelFactory.LOCAL_PERSONAL_PROPERTY, fCurrentLDModel));
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.ICON_PROPERTY),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        fActionNewLocalRolePropertyAction = new Action(Messages.LDPropertiesView_4) {
            @Override
            public void run() {
                ILocalRolePropertyModel propertyModel = (ILocalRolePropertyModel)LDModelFactory.createModelObject(LDModelFactory.LOCAL_ROLE_PROPERTY, fCurrentLDModel);
                // Default Role
                propertyModel.setRole(propertyModel.getLDModel().getRolesModel().getDefaultLearnerRole());
                addNewObject(propertyModel);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.ICON_PROPERTY),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        fActionNewGlobalPropertyAction = new Action(Messages.LDPropertiesView_5) {
            @Override
            public void run() {
                addNewObject(LDModelFactory.createModelObject(LDModelFactory.GLOBAL_PROPERTY, fCurrentLDModel));
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.ICON_PROPERTY),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        fActionNewGlobalPersonalPropertyAction = new Action(Messages.LDPropertiesView_6) {
            @Override
            public void run() {
                addNewObject(LDModelFactory.createModelObject(LDModelFactory.GLOBAL_PERSONAL_PROPERTY, fCurrentLDModel));
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.ICON_PROPERTY),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        fActionNewPropertyGroupAction = new Action(Messages.LDPropertiesView_7) {
            @Override
            public void run() {
                addNewObject(LDModelFactory.createModelObject(LDModelFactory.PROPERTY_GROUP, fCurrentLDModel));
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
        
        fDropDown = new DropDownAction(Messages.LDPropertiesView_8) {
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
                addActionToMenu(menu, fActionNewLocalPropertyAction);
                addActionToMenu(menu, fActionNewLocalPersonalPropertyAction);
                addActionToMenu(menu, fActionNewLocalRolePropertyAction);
                addActionToMenu(menu, fActionNewGlobalPropertyAction);
                addActionToMenu(menu, fActionNewGlobalPersonalPropertyAction);
                addActionToMenu(menu, fActionNewPropertyGroupAction);
            }
        };

        // Select All
        fActionSelectAll = new LDAuthorActionFactory.SelectAllAction() {
            @Override
            public void run() {
                fTreeTable.getTree().selectAll();
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
        
        fActionShowCopyToClipboardDialog = new Action(Messages.LDPropertiesView_1) {
            @Override
            public void run() {
                ILDModelObject property = (ILDModelObject)((IStructuredSelection)fTreeTable.getSelection()).getFirstElement();
                Dialog dlg = new CopyPropertyToClipboardDialog(getSite().getShell(), property);
                dlg.open();
            }
        };
    }
    
    private void addNewObject(ILDModelObject object) {
        Object selected = ((IStructuredSelection)fTreeTable.getSelection()).getFirstElement();
        
        // Selected a Property
        if(selected instanceof IPropertyTypeModel || selected instanceof IPropertyGroupModel) {
            int index = fCurrentLDModel.getPropertiesModel().getChildren().indexOf(selected) + 1;
            fCurrentLDModel.getPropertiesModel().addChildAt(object, index);
        }
        // Else
        else {
            fCurrentLDModel.getPropertiesModel().addChild(object);
        }
        
        fTreeTable.setSelection(new StructuredSelection(object), true);
        fCurrentLDModel.setDirty();
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
        
        Menu menu = menuMgr.createContextMenu(fTreeTable.getControl());
        fTreeTable.getControl().setMenu(menu);
        
        getSite().registerContextMenu(menuMgr, fTreeTable);
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
        
        boolean isEmpty = fTreeTable.getSelection().isEmpty();
        
        IMenuManager newMenu = new MenuManager(Messages.LDPropertiesView_8, "new"); //$NON-NLS-1$
        manager.add(newMenu);

        newMenu.add(fActionNewLocalPropertyAction);
        newMenu.add(fActionNewLocalPersonalPropertyAction);
        newMenu.add(fActionNewLocalRolePropertyAction);
        newMenu.add(fActionNewGlobalPropertyAction);
        newMenu.add(fActionNewGlobalPersonalPropertyAction);
        newMenu.add(fActionNewPropertyGroupAction);
        
        manager.add(new Separator());
        
        
        if(!isEmpty) {
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_START));
            // TODO
            //manager.add(fActionCut);
            //manager.add(fActionCopy);
            //manager.add(fActionPaste);
            manager.add(fActionDelete);
            manager.add(new Separator());
            //manager.add(fActionRename);
            // Copy to Clipboard
            manager.add(fActionShowCopyToClipboardDialog);
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_END));
        }
    }
    
    /**
     * Update the Local Actions depending on the selection 
     * @param selection
     */
    private void updateActions(ISelection selection) {
        boolean isEmpty = selection.isEmpty();
        
        //Object obj = ((IStructuredSelection)selection).getFirstElement();
            
        fActionDelete.setEnabled(!isEmpty);
    }

    
    /**
     * Delete Action
     */
    private void handleDeleteAction() {
        List<?> selected = ((IStructuredSelection)fTreeTable.getSelection()).toList();
        
        if(!askUserDeleteResources(selected)) {
            return;
        }
        
        fNotifications = false;
        
        for(Object o : selected) {
            ILDModelObject ldObject = (ILDModelObject)o;
            // Delete child Property reference from Group
            if(ldObject instanceof IPropertyRefModel) {
                IPropertyGroupModel parent = (IPropertyGroupModel)((IPropertyRefModel)ldObject).getParent();
                if(parent != null) {
                    parent.removeProperty(ldObject);
                }
            }
            // Delete top level
            else {
                fCurrentLDModel.getPropertiesModel().removeChild(ldObject);
            }
        }
        
        fTreeTable.refresh();
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
                Messages.LDPropertiesView_10,
                selected.size() > 1 ?
                        Messages.LDPropertiesView_11 
                        : 
                        Messages.LDPropertiesView_12);
    }


    public void propertyChange(PropertyChangeEvent evt) {
        // Property updated - update the whole tree, as it could appear more than once
        if(fNotifications) {
            if(evt.getSource() instanceof IPropertyTypeModel || evt.getNewValue() instanceof IPropertyTypeModel ||
                    evt.getSource() instanceof IPropertyGroupModel || evt.getNewValue() instanceof IPropertyGroupModel) {
                fTreeTable.refresh();
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
        
        fClipboard.dispose();
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
        return Messages.LDPropertiesView_13;
    }
    
}