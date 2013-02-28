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
package org.tencompetence.ldauthor.ui.views.organiser.roles;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.roles.ILearnerModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.roles.IStaffModel;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.DropDownAction;
import org.tencompetence.ldauthor.ui.dialogs.RenameObjectDialog;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.views.IOrganiserView;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;
import org.tencompetence.ldauthor.ui.views.organiser.IOrganiserComposite;
import org.tencompetence.ldauthor.ui.wizards.role.NewRoleWizard;


/**
 * RolesComposite
 * 
 * @author Phillip Beauvoir
 * @version $Id: RolesComposite.java,v 1.23 2009/06/07 14:05:59 phillipus Exp $
 */
public class RolesComposite extends Composite implements IOrganiserComposite, PropertyChangeListener {
    
    private IOrganiserView fViewPart;
    
    private DropDownAction fDropDown;
    private IAction fActionNewLearnerRole;
    private IAction fActionNewStaffRole;
    
    private IAction fActionRename;
    private IAction fActionShowInspector;
    private IAction fActionDelete;
    private IAction fActionSelectAll;

    private ILDModel fCurrentLDModel;
    
    private RolesTableViewer fTableViewer;
    
    public RolesComposite(IOrganiserView viewPart, Composite parent, int style) {
        super(parent, style);
        
        setLayout(new TableColumnLayout());
        
        fViewPart = viewPart;
        
        makeActions();
        
        fTableViewer = new RolesTableViewer(this, SWT.MULTI);
        fTableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Update the Inspector
                Object o = ((StructuredSelection)event.getSelection()).getFirstElement();
                InspectorManager.getInstance().setInput(fViewPart, o);
                
                // And the selection provider
                fViewPart.getSite().getSelectionProvider().setSelection(event.getSelection());
                
                // Update actions
                updateActions(event.getSelection());
            }
        });
        
        // Double-click opens the Inspector View
        fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                InspectorManager.getInstance().showInspector();
            }
        });

        hookContextMenu();
    }
    
    /**
     * Make local actions
     */
    private void makeActions() {
        fActionNewLearnerRole = new Action(Messages.RolesComposite_0) {
            @Override
            public void run() {
                doNewRoleAction(ILearnerModel.class);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.IMAGE_LEARNER_16);
            }
        };
        
        fActionNewStaffRole = new Action(Messages.RolesComposite_1) {
            @Override
            public void run() {
                doNewRoleAction(IStaffModel.class);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.IMAGE_STAFF_16);
            }
        };
        
        fDropDown = new DropDownAction(Messages.RolesComposite_2) {
            @Override
            public void run() {
                Menu menu = getMenu(getShell());
                menu.setVisible(true);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD);
            }

            @Override
            protected void addActions(Menu menu) {
                addActionToMenu(menu, fActionNewLearnerRole);
                addActionToMenu(menu, fActionNewStaffRole);
            }
        };
        fDropDown.setEnabled(false);
        
        // Delete
        fActionDelete = new LDAuthorActionFactory.DeleteAction() {
            @Override
            public void run() {
                doDeleteAction();
            }
        };
        fActionDelete.setEnabled(false);
        
        // Select All
        fActionSelectAll = new LDAuthorActionFactory.SelectAllAction() {
            @Override
            public void run() {
                fTableViewer.getTable().selectAll();
                updateActions(fTableViewer.getSelection());
            }
        };
        
        // Properties
        fActionShowInspector = new Action(Messages.RolesComposite_3) {
            @Override
            public void run() {
                InspectorManager.getInstance().showInspector();
            }
        };
        
        // Rename
        fActionRename = new LDAuthorActionFactory.RenameAction() {
            @Override
            public void run() {
                renameRole();
            }
        };
        fActionRename.setEnabled(false);
    }
    
    /**
     * Hook into a right-click menu on the Table
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#RolesPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);

        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });

        Menu menu = menuMgr.createContextMenu(fTableViewer.getControl());
        fTableViewer.getControl().setMenu(menu);

        fViewPart.getSite().registerContextMenu(menuMgr, fTableViewer);
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
        
        boolean isEmpty = fTableViewer.getSelection().isEmpty();
        
        IMenuManager newMenu = new MenuManager(Messages.RolesComposite_4, "new"); //$NON-NLS-1$
        manager.add(newMenu);

        newMenu.add(fActionNewLearnerRole);
        newMenu.add(fActionNewStaffRole);
        newMenu.add(new Separator());
        
        if(!isEmpty) {
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_START));
            manager.add(fActionDelete);
            manager.add(new Separator());
            manager.add(fActionRename);
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_END));
            manager.add(fActionShowInspector);
        }

        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /**
     * Update the Local Actions depending on the selection 
     * @param selection
     */
    private void updateActions(ISelection selection) {
        boolean isEmpty = selection.isEmpty();
        
        //Object obj = ((IStructuredSelection)selection).getFirstElement();

        fActionDelete.setEnabled(!isEmpty);
        fActionRename.setEnabled(!isEmpty);
        fActionShowInspector.setEnabled(!isEmpty);
    }

    
    /**
     * Register Global Action Handlers
     */
    private void registerGlobalActions() {
        IActionBars actionBars = fViewPart.getViewSite().getActionBars();
        actionBars.clearGlobalActionHandlers();
        
        // Register our interest in the global menu actions
        actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), fActionDelete);
        actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), fActionSelectAll);
        actionBars.setGlobalActionHandler(ActionFactory.RENAME.getId(), fActionRename);
    }
    
    /**
     * Make Local Toolbar items
     */
    private void makeLocalToolBarActions() {
        IActionBars bars = fViewPart.getViewSite().getActionBars();
        IToolBarManager manager = bars.getToolBarManager();
        manager.removeAll();

        manager.add(new Separator(IWorkbenchActionConstants.NEW_GROUP));
        manager.add(fDropDown);
        manager.add(new Separator());
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        
        bars.updateActionBars();
    }

    public void updateFocus() {
        updateActions(fTableViewer.getSelection());
        registerGlobalActions();
        makeLocalToolBarActions();
        
        fTableViewer.getControl().setFocus();
        
        // Have to do this if you want to update the Inspector
        Object o = ((StructuredSelection)fTableViewer.getSelection()).getFirstElement();
        InspectorManager.getInstance().setInput(fViewPart, o);
    }
    
    public void setActiveEditor(ILDMultiPageEditor editor) {
        if(fCurrentLDModel != null) {
            fCurrentLDModel.removePropertyChangeListener(this);
        }
        
        if(editor != null) {
            fCurrentLDModel = (ILDModel)editor.getAdapter(ILDModel.class);
            fCurrentLDModel.addPropertyChangeListener(this);
            fTableViewer.setInput(fCurrentLDModel);
            fDropDown.setEnabled(true);
        }
        else {
            fCurrentLDModel = null;
            fTableViewer.setInput(null);
            fDropDown.setEnabled(false);
        }
    }
    
    /**
     * Rename
     */
    private void renameRole() {
        IRoleModel selected = (IRoleModel)((StructuredSelection)fTableViewer.getSelection()).getFirstElement();
        if(selected != null) {
            RenameObjectDialog dialog = new RenameObjectDialog(Display.getDefault().getActiveShell(), selected.getTitle());
            if(dialog.open()) {
                String name = dialog.getNewName();
                if(name != null) {
                    selected.setTitle(name);
                    fCurrentLDModel.setDirty();
                }
            }
        }
    }
    
    /**
     * Delete selected objects
     */
    private void doDeleteAction() {
        List<?> selected = ((IStructuredSelection)fTableViewer.getSelection()).toList();
        
        if(!askUserDeleteResources()) {
            return;
        }
        
        for(Object o : selected) {
            IRoleModel role = (IRoleModel)o;
            // Do not delete if referenced by Role Part
            if(role.isReferencedByRolePart()) {
                MessageDialog.openError(
                        Display.getDefault().getActiveShell(),
                        Messages.RolesComposite_5,
                        Messages.RolesComposite_6 + role.getTitle() + Messages.RolesComposite_7);
            }
            else {
                role.getParent().removeChild(role);
            }
        }
        
        fTableViewer.refresh();
        fCurrentLDModel.setDirty();
    }
    
    private void doNewRoleAction(Class<?> type) {
        Wizard wizard = new NewRoleWizard(fCurrentLDModel, type);
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
        if(dialog.open() == IDialogConstants.OK_ID) {
            fCurrentLDModel.setDirty();
        }
    }

    /**
     * Ask the user whether they wish to delete the given objects
     * @return True if the user said yes, false if user says no or cancels
     */
    private boolean askUserDeleteResources() {
        // Confirmation dialog
        return MessageDialog.openQuestion(
                Display.getDefault().getActiveShell(),
                Messages.RolesComposite_8,
                Messages.RolesComposite_9);
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        // Role updated - update the whole table
        if(evt.getSource() instanceof IRoleModel || evt.getNewValue() instanceof IRoleModel) {
            fTableViewer.refresh();
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
    }

}
