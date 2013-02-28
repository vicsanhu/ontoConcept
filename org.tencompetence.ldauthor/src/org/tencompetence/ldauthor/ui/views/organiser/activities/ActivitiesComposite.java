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
package org.tencompetence.ldauthor.ui.views.organiser.activities;

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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDecoration;
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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.activities.IActivityRefModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityType;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.qti.ui.NewQTITestActivityWizard;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.DropDownAction;
import org.tencompetence.ldauthor.ui.dialogs.RenameObjectDialog;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.views.IOrganiserView;
import org.tencompetence.ldauthor.ui.views.ViewManager;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;
import org.tencompetence.ldauthor.ui.views.organiser.IOrganiserComposite;
import org.tencompetence.ldauthor.ui.views.preview.PreviewView;
import org.tencompetence.ldauthor.ui.wizards.activity.NewActivityWizard;


/**
 * ActivitiesComposite
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActivitiesComposite.java,v 1.44 2009/06/07 14:05:59 phillipus Exp $
 */
public class ActivitiesComposite extends Composite implements IOrganiserComposite, PropertyChangeListener {
    
    private IOrganiserView fViewPart;
    
    private DropDownAction fDropDown;
    
    private IAction fActionNewMainLearningActivity;
    private IAction fActionNewMainTestActivity;
    private IAction fActionNewMainSupportActivity;
    private IAction fActionNewMainActivityStructure;
    
    private IAction fActionNewChildLearningActivity;
    private IAction fActionNewChildTestActivity;
    private IAction fActionNewChildSupportActivity;
    private IAction fActionNewChildActivityStructure;

    
    private IAction fActionShowInspector;
    private IAction fActionShowPreview;
    private IAction fActionCut;
    private IAction fActionCopy;
    private IAction fActionPaste;
    private IAction fActionDelete;
    private IAction fActionSelectAll;
    private IAction fActionRename;
    
    private IAction fActionToggleLearningActivities;
    private IAction fActionToggleSupportActivities;
    private IAction fActionToggleActivityStructures;
    
    private ILDModel fCurrentLDModel;
    
    /*
     * Flag. Can be turned off when deleting large amounts of elements so that table update is not hindered
     */
    private boolean fNotifications = true;
    
    /**
     * The Tree Viewer
     */
    private ActivitiesTreeViewer fTreeViewer;
    
    private ActivitiesTreeViewerDragDropHandler fDragDropHandler;
 
    
    public ActivitiesComposite(IOrganiserView viewPart, Composite parent, int style) {
        super(parent, style);
        
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        setLayout(layout);
        
        fViewPart = viewPart;
        
        // Create the Tree Viewer first    
        fTreeViewer = new ActivitiesTreeViewer(this, SWT.MULTI);
        fTreeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        
        fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Pass on selections to the Inspector
                Object o = ((StructuredSelection)event.getSelection()).getFirstElement();
                InspectorManager.getInstance().setInput(fViewPart, o);
                
                // And the selection provider
                fViewPart.getSite().getSelectionProvider().setSelection(event.getSelection());

                // Update actions
                updateActions(event.getSelection());
            }
        });
        
        // Double-click opens the Properties View
        fTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                InspectorManager.getInstance().showInspector();
            }
        });

        // DnD
        fDragDropHandler = new ActivitiesTreeViewerDragDropHandler(fTreeViewer);
        
        makeActions();
        hookContextMenu();
    }
    
    /**
     * Make local actions
     */
    private void makeActions() {
        fActionNewMainLearningActivity = new Action(Messages.ActivitiesComposite_6) {
            @Override
            public void run() {
                doNewActivityAction(NewActivityWizard.LEARNING_ACTIVITY);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_ACTIVITY_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        fActionNewMainTestActivity = new Action(Messages.ActivitiesComposite_25) {
            @Override
            public void run() {
                doNewQTITestActivityAction();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_TEST_OBJECT_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        fActionNewMainSupportActivity = new Action(Messages.ActivitiesComposite_9) {
            @Override
            public void run() {
                doNewActivityAction(NewActivityWizard.SUPPORT_ACTIVITY);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_SUPPORT_ACTIVITY_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        fActionNewMainActivityStructure = new Action(Messages.ActivitiesComposite_10) {
            @Override
            public void run() {
                doNewActivityAction(NewActivityWizard.ACTIVITY_STRUCTURE);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_ACTIVITY_STRUCTURE_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        fActionNewChildLearningActivity = new Action(Messages.ActivitiesComposite_21) {
            @Override
            public void run() {
                doNewChildActivityAction(NewActivityWizard.LEARNING_ACTIVITY);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_ACTIVITY_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        fActionNewChildLearningActivity.setEnabled(false);
        
        fActionNewChildTestActivity = new Action(Messages.ActivitiesComposite_26) {
            @Override
            public void run() {
                doNewChildQTITestActivityAction();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_TEST_OBJECT_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        fActionNewChildTestActivity.setEnabled(false);
        
        fActionNewChildSupportActivity = new Action(Messages.ActivitiesComposite_22) {
            @Override
            public void run() {
                doNewChildActivityAction(NewActivityWizard.SUPPORT_ACTIVITY);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_SUPPORT_ACTIVITY_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        fActionNewChildSupportActivity.setEnabled(false);
        
        fActionNewChildActivityStructure = new Action(Messages.ActivitiesComposite_23) {
            @Override
            public void run() {
                doNewChildActivityAction(NewActivityWizard.ACTIVITY_STRUCTURE);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_ACTIVITY_STRUCTURE_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        fActionNewChildActivityStructure.setEnabled(false);
        
        fDropDown = new DropDownAction(Messages.ActivitiesComposite_2) {
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
                addActionToMenu(menu, fActionNewMainLearningActivity);
                addActionToMenu(menu, fActionNewMainTestActivity);
                addActionToMenu(menu, fActionNewMainSupportActivity);
                addActionToMenu(menu, fActionNewMainActivityStructure);
                IActivityStructureModel as = fTreeViewer.getSelectedActivityStructure();
                if(as != null) {
                    new MenuItem(menu, SWT.SEPARATOR);
                    addActionToMenu(menu, fActionNewChildLearningActivity);
                    addActionToMenu(menu, fActionNewChildTestActivity);
                    addActionToMenu(menu, fActionNewChildSupportActivity);
                    addActionToMenu(menu, fActionNewChildActivityStructure);
                }
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
        
        // Cut
        fActionCut = new LDAuthorActionFactory.CutAction() {
            @Override
            public void run() {
                //handleCutAction();
            }
        };
        fActionCut.setEnabled(false);

        // Copy
        fActionCopy = new LDAuthorActionFactory.CopyAction() {
            @Override
            public void run() {
                //handleCopyAction();
            }
        };
        fActionCopy.setEnabled(false);

        // Paste
        fActionPaste = new LDAuthorActionFactory.PasteAction() {
            @Override
            public void run() {
                //handlePasteAction();
            }
        };
        fActionPaste.setEnabled(false);
        
        // Select All
        fActionSelectAll = new LDAuthorActionFactory.SelectAllAction() {
            @Override
            public void run() {
                fTreeViewer.getTree().selectAll();
                updateActions(fTreeViewer.getSelection());
            }
        };
        
        // Inspector
        fActionShowInspector = new Action(Messages.ActivitiesComposite_3) {
            @Override
            public void run() {
                InspectorManager.getInstance().showInspector();
            }
        };

        // Preview
        fActionShowPreview = new Action(Messages.ActivitiesComposite_20) {
            @Override
            public void run() {
                ViewManager.showViewPart(PreviewView.ID, false);
            }
        };
        
        // Rename
        fActionRename = new LDAuthorActionFactory.RenameAction() {
            @Override
            public void run() {
                renameActivity();
            }
        };
        fActionRename.setEnabled(false);

        fActionToggleLearningActivities = new Action(Messages.ActivitiesComposite_4, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                setText(isChecked() ? Messages.ActivitiesComposite_5 : Messages.ActivitiesComposite_4);
                fTreeViewer.showLearningActivities(!isChecked());
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_ACTIVITY_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_SLASH), IDecoration.TOP_LEFT);
            }
        };
        fActionToggleLearningActivities.setChecked(false);
        
        fActionToggleSupportActivities = new Action(Messages.ActivitiesComposite_7, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                setText(isChecked() ? Messages.ActivitiesComposite_8 : Messages.ActivitiesComposite_7);
                fTreeViewer.showSupportActivities(!isChecked());
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_SUPPORT_ACTIVITY_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_SLASH), IDecoration.TOP_LEFT);
            }
        };
        fActionToggleSupportActivities.setChecked(false);
        
        fActionToggleActivityStructures = new Action(Messages.ActivitiesComposite_12, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                setText(isChecked() ? Messages.ActivitiesComposite_11 : Messages.ActivitiesComposite_12);
                fTreeViewer.showActivityStructures(!isChecked());
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_ACTIVITY_STRUCTURE_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_SLASH), IDecoration.TOP_LEFT);
            }
        };
        fActionToggleActivityStructures.setChecked(false);
    }
    
    /**
     * Register Global Action Handlers
     */
    private void registerGlobalActions() {
        IActionBars actionBars = fViewPart.getViewSite().getActionBars();
        actionBars.clearGlobalActionHandlers();
        
        // Register our interest in the global menu actions
        actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), fActionCut);
        actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), fActionCopy);
        actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), fActionPaste);
        actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), fActionDelete);
        actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), fActionSelectAll);
        actionBars.setGlobalActionHandler(ActionFactory.RENAME.getId(), fActionRename);
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#ActivitiesOrganiserPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        
        Menu menu = menuMgr.createContextMenu(fTreeViewer.getControl());
        fTreeViewer.getControl().setMenu(menu);
        
        fViewPart.getSite().registerContextMenu(menuMgr, fTreeViewer);
    }

    /**
     * Update the Local Actions depending on the selection 
     * @param selection
     */
    private void updateActions(ISelection selection) {
        boolean isEmpty = selection.isEmpty();
        
        //Object obj = ((IStructuredSelection)selection).getFirstElement();

        // Have we selected an ActivityStructure or have one as a parent?
        // This gives us the option to add the Activity to that rather than as a top level Role Part
        IActivityStructureModel as = fTreeViewer.getSelectedActivityStructure();
        fActionNewChildLearningActivity.setEnabled(as != null);
        fActionNewChildTestActivity.setEnabled(as != null);
        fActionNewChildSupportActivity.setEnabled(as != null);
        fActionNewChildActivityStructure.setEnabled(as != null);
        
        fActionDelete.setEnabled(!isEmpty);
        fActionCut.setEnabled(!isEmpty);
        fActionCopy.setEnabled(!isEmpty);
        fActionRename.setEnabled(!isEmpty);
        fActionPaste.setEnabled(false);
        
        fActionShowInspector.setEnabled(!isEmpty);
        fActionShowPreview.setEnabled(!isEmpty);
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
        
        IActivityStructureModel as = fTreeViewer.getSelectedActivityStructure();
        
        IMenuManager newMenu = new MenuManager(Messages.ActivitiesComposite_13, "new"); //$NON-NLS-1$
        manager.add(newMenu);

        newMenu.add(fActionNewMainLearningActivity);
        newMenu.add(fActionNewMainTestActivity);
        newMenu.add(fActionNewMainSupportActivity);
        newMenu.add(fActionNewMainActivityStructure);
        if(as != null) {
            newMenu.add(new Separator());
            newMenu.add(fActionNewChildLearningActivity);
            newMenu.add(fActionNewChildTestActivity);
            newMenu.add(fActionNewChildSupportActivity);
            newMenu.add(fActionNewChildActivityStructure);
        }
        manager.add(new Separator());
        
        if(!isEmpty) {
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_START));
            // TODO
            //manager.add(fActionCut);
            //manager.add(fActionCopy);
            //manager.add(fActionPaste);
            manager.add(fActionDelete);
            manager.add(new Separator());
            manager.add(fActionRename);
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_END));
            manager.add(fActionShowInspector);
            manager.add(fActionShowPreview);
        }

        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
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
        manager.add(fActionToggleLearningActivities);
        manager.add(fActionToggleSupportActivities);
        manager.add(fActionToggleActivityStructures);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        
        bars.updateActionBars();
    }

    public void updateFocus() {
        updateActions(fTreeViewer.getSelection());
        registerGlobalActions();
        makeLocalToolBarActions();
        
        fTreeViewer.getControl().setFocus();
        
        // Have to do this to update the Inspector
        Object o = ((StructuredSelection)fTreeViewer.getSelection()).getFirstElement();
        InspectorManager.getInstance().setInput(fViewPart, o);
    }
    
    /**
     * Rename
     */
    private void renameActivity() {
        ILDModelObject selected = (ILDModelObject)((StructuredSelection)fTreeViewer.getSelection()).getFirstElement();
        if(selected != null) {
            if(selected instanceof IActivityRefModel) {
                selected = ((IActivityRefModel)selected).getLDModelObject();
            }
            RenameObjectDialog dialog = new RenameObjectDialog(Display.getDefault().getActiveShell(), ((ITitle)selected).getTitle());
            if(dialog.open()) {
                String name = dialog.getNewName();
                if(name != null) {
                    ((ITitle)selected).setTitle(name);
                    fCurrentLDModel.setDirty();
                }
            }
        }
    }
    
    /**
     * New Top level Activity
     */
    private void doNewActivityAction(int type) {
        Wizard wizard = new NewActivityWizard(fCurrentLDModel, type);
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
        dialog.open();
    }
    
    /**
     * Creates a Child Activity of a parent AS
     */
    private void doNewChildActivityAction(int type) {
        IActivityStructureModel as = fTreeViewer.getSelectedActivityStructure();
        if(as == null) {
            throw new RuntimeException("Activity Structure was null"); //$NON-NLS-1$
        }
        
        Wizard wizard = new NewActivityWizard(as, type);
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
        dialog.open();
    }

    /**
     * New Top level QTI Test Activity
     */
    private void doNewQTITestActivityAction() {
        Wizard wizard = new NewQTITestActivityWizard(fCurrentLDModel);
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
        dialog.open();
    }

    /**
     * Creates a Child Test Activity of a parent AS
     */
    private void doNewChildQTITestActivityAction() {
        IActivityStructureModel as = fTreeViewer.getSelectedActivityStructure();
        if(as == null) {
            throw new RuntimeException("Activity Structure was null"); //$NON-NLS-1$
        }
        
        Wizard wizard = new NewQTITestActivityWizard(as);
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
        dialog.open();
    }

    /**
     * Delete selected objects
     */
    private void doDeleteAction() {
        List<?> selected = ((IStructuredSelection)fTreeViewer.getSelection()).toList();
        
        if(!askUserDeleteResources(selected)) {
            return;
        }
        
        fNotifications = false;
        
        for(Object o : selected) {
            ILDModelObject ldObject = (ILDModelObject)o;
            // Delete child Activity reference from AS
            if(ldObject instanceof IActivityRefModel) {
                IActivityStructureModel asParent = (IActivityStructureModel)((IActivityRefModel)ldObject).getParent();
                if(asParent != null) {
                    asParent.removeActivity(ldObject);
                }
            }
            // Delete top level
            else {
                // Do not delete if referenced by Role Part
                if(fCurrentLDModel.getMethodModel().getPlaysModel().isComponentReferencedByRolePart(ldObject)) {
                    boolean response = MessageDialog.openQuestion(
                            Display.getDefault().getActiveShell(),
                            Messages.ActivitiesComposite_14,
                            Messages.ActivitiesComposite_15 +
                            ((ITitle)ldObject).getTitle() +
                            Messages.ActivitiesComposite_16
                            + "\n" + //$NON-NLS-1$
                            Messages.ActivitiesComposite_24
                            );
                    if(!response) {
                        break;
                    }
                }
                else {
                    if(ldObject instanceof ILearningActivityModel) {
                        fCurrentLDModel.getActivitiesModel().getLearningActivitiesModel().removeChild(ldObject);
                    }
                    else if(ldObject instanceof ISupportActivityModel) {
                        fCurrentLDModel.getActivitiesModel().getSupportActivitiesModel().removeChild(ldObject);
                    }
                    else if(ldObject instanceof IActivityStructureModel) {
                        fCurrentLDModel.getActivitiesModel().getActivityStructuresModel().removeChild(ldObject);
                    }
                }
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
                Messages.ActivitiesComposite_17,
                selected.size() > 1 ?
                        Messages.ActivitiesComposite_18 
                        : 
                        Messages.ActivitiesComposite_19);
    }

    public void setActiveEditor(ILDMultiPageEditor editor) {
        if(fCurrentLDModel != null) {
            fCurrentLDModel.removePropertyChangeListener(this);
        }
        
        if(editor != null) {
            fCurrentLDModel = (ILDModel)editor.getAdapter(ILDModel.class);
            fCurrentLDModel.addPropertyChangeListener(this);
            fTreeViewer.setInput(fCurrentLDModel);
            fDragDropHandler.setLDModel(fCurrentLDModel);
            fDropDown.setEnabled(true);
        }
        else {
            fCurrentLDModel = null;
            fTreeViewer.setInput(null);
            fDropDown.setEnabled(false);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // Activity updated - update the whole tree, as it could appear more than once
        if(fNotifications) {
            if(evt.getSource() instanceof IActivityType || evt.getNewValue() instanceof IActivityType) {
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
    }
}
