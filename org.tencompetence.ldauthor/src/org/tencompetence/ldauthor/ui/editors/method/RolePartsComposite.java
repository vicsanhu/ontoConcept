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
package org.tencompetence.ldauthor.ui.editors.method;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.widgets.Section;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityRefModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.roles.ILearnerModel;
import org.tencompetence.imsldmodel.roles.IStaffModel;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;
import org.tencompetence.ldauthor.qti.ui.NewQTITestActivityWizard;
import org.tencompetence.ldauthor.qti.ui.NewQTITestRolePartWizard;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.dialogs.RenameObjectDialog;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.method.RolePartsTreeModelAdapter.ASChildTreeItem;
import org.tencompetence.ldauthor.ui.editors.method.RolePartsTreeModelAdapter.FoldedRolePart;
import org.tencompetence.ldauthor.ui.views.ViewManager;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;
import org.tencompetence.ldauthor.ui.views.preview.PreviewPopup;
import org.tencompetence.ldauthor.ui.views.preview.PreviewView;
import org.tencompetence.ldauthor.ui.wizards.activity.NewActivityWizard;
import org.tencompetence.ldauthor.ui.wizards.activity.NewRolePartWizard;
import org.tencompetence.ldauthor.ui.wizards.role.NewRoleWizard;


/**
 * Composite holding Role Parts
 * 
 * @author Phillip Beauvoir
 * @version $Id: RolePartsComposite.java,v 1.42 2009/06/07 14:05:59 phillipus Exp $
 */
public class RolePartsComposite extends Composite {
    
    private ILDEditorPart fEditor;
    
    private Section fSection;
    
    private RolePartsTreeModelAdapter fRolePartsTreeModelAdapter;
    private RolePartsTreeTableViewer fTreeViewer;
    
    private Button fNewButton;
    
    private IAction fActionNewMainLearningActivity;
    private IAction fActionNewMainTestActivity;
    private IAction fActionNewMainSupportActivity;
    private IAction fActionNewMainActivityStructure;
    
    private IAction fActionNewChildLearningActivity;
    private IAction fActionNewChildTestActivity;
    private IAction fActionNewChildSupportActivity;
    private IAction fActionNewChildActivityStructure;
    
    private IAction fActionNewLearnerRole;
    private IAction fActionNewStaffRole;
    
    private IAction fActionShowProperties;
    private IAction fActionShowPreview;
    private IAction fActionCut;
    private IAction fActionCopy;
    private IAction fActionPaste;
    private IAction fActionDelete;
    private IAction fActionSelectAll;
    private IAction fActionRename;
    

    public RolePartsComposite(ILDEditorPart editor, Composite parent, int style) {
        super(parent, style);
        
        fEditor = editor;
        
        createActions();
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        
        fSection = AppFormToolkit.getInstance().createSection(this, Section.TITLE_BAR);
        fSection.setText(Messages.RolePartsComposite_0);
        fSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        Composite toolBar = createToolBar(fSection);
        fSection.setTextClient(toolBar);
        
        Composite client = AppFormToolkit.getInstance().createComposite(fSection);
        client.setLayout(new TreeColumnLayout());
        fSection.setClient(client);

        fTreeViewer = new RolePartsTreeTableViewer(editor, client, SWT.BORDER);
        fRolePartsTreeModelAdapter = new RolePartsTreeModelAdapter(editor, fTreeViewer);
        
        // Listen to local Act selection/de-selection
        ActSelectionProvider selectionHandler = (ActSelectionProvider)fEditor.getAdapter(ActSelectionProvider.class);
        selectionHandler.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IActModel act = (IActModel)((StructuredSelection)event.getSelection()).getFirstElement();
                setAct(act);
            }
        });
        
        // Double-click opens the Inspector View
        fTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                InspectorManager.getInstance().showInspector();
            }
        });
        
        // Tree Selections
        fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Update Actions
                updateActions(event.getSelection());
            }
        });
        
        // Enable hover preview popup if set in Prefs
        hookPopups();
        
        registerGlobalActions();
        hookContextMenu();
    }
    
    /**
     * Set the Act model
     * @param act
     */
    public void setAct(IActModel act) {
        if(act == fRolePartsTreeModelAdapter.getAct()) {
            return; // No need to update if same
        }
        
        // We can enable these now
        fNewButton.setEnabled(act != null);
        fActionNewMainLearningActivity.setEnabled(act != null);
        fActionNewMainTestActivity.setEnabled(act != null);
        fActionNewMainSupportActivity.setEnabled(act != null);
        fActionNewMainActivityStructure.setEnabled(act != null);
        fActionNewLearnerRole.setEnabled(act != null);
        fActionNewStaffRole.setEnabled(act != null);
        
        // Update text
        fSection.setText(act != null ? Messages.RolePartsComposite_2 + " " + act.getTitle() : Messages.RolePartsComposite_3); //$NON-NLS-1$
        fSection.layout();  // update for text

        fRolePartsTreeModelAdapter.setAct(act);
        fTreeViewer.setInput(fRolePartsTreeModelAdapter);
    }

    private Composite createToolBar(Composite parent) {
        Composite c = new Composite(parent, SWT.NULL);
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        c.setLayout(layout);

        fNewButton = new Button(c, SWT.PUSH);
        fNewButton.setText(Messages.RolePartsComposite_4);
        fNewButton.setToolTipText(Messages.RolePartsComposite_5);
        fNewButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD).createImage());
        fNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                IActivityStructureModel as = fTreeViewer.getSelectedActivityStructure();
                
                MenuManager menuManager = new MenuManager();
                menuManager.add(fActionNewMainLearningActivity);
                menuManager.add(fActionNewMainTestActivity);
                menuManager.add(fActionNewMainSupportActivity);
                menuManager.add(fActionNewMainActivityStructure);
                if(as != null) {
                    menuManager.add(new Separator());
                    menuManager.add(fActionNewChildLearningActivity);
                    menuManager.add(fActionNewChildTestActivity);
                    menuManager.add(fActionNewChildSupportActivity);
                    menuManager.add(fActionNewChildActivityStructure);
                }
                menuManager.add(new Separator());
                menuManager.add(fActionNewLearnerRole);
                menuManager.add(fActionNewStaffRole);
                Menu menu = menuManager.createContextMenu(fNewButton.getShell());
                menu.setVisible(true);
            }
        });
        
        return c;
    }

    /**
     * Actions
     */
    private void createActions() {
        fActionNewMainLearningActivity = new Action(Messages.RolePartsComposite_16) {
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
        fActionNewMainLearningActivity.setEnabled(false);
        
        fActionNewMainTestActivity = new Action(Messages.RolePartsComposite_22) {
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
        fActionNewMainTestActivity.setEnabled(false);
        
        fActionNewMainSupportActivity = new Action(Messages.RolePartsComposite_17) {
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
        fActionNewMainSupportActivity.setEnabled(false);
        
        fActionNewMainActivityStructure = new Action(Messages.RolePartsComposite_18) {
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
        fActionNewMainActivityStructure.setEnabled(false);
        
        fActionNewChildLearningActivity = new Action(Messages.RolePartsComposite_19) {
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
        
        fActionNewChildTestActivity = new Action(Messages.RolePartsComposite_23) {
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

        fActionNewChildSupportActivity = new Action(Messages.RolePartsComposite_20) {
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
        
        fActionNewChildActivityStructure = new Action(Messages.RolePartsComposite_21) {
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
        
        fActionNewLearnerRole = new Action(LDModelUtils.getUserObjectName(LDModelFactory.LEARNER) + " " + Messages.RolePartsComposite_8) { //$NON-NLS-1$
            @Override
            public void run() {
                doNewRoleAction(ILearnerModel.class);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_LEARNER_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        fActionNewLearnerRole.setEnabled(false);
        
        fActionNewStaffRole = new Action(LDModelUtils.getUserObjectName(LDModelFactory.STAFF) + " " + Messages.RolePartsComposite_9) { //$NON-NLS-1$
            @Override
            public void run() {
                doNewRoleAction(IStaffModel.class);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_STAFF_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        fActionNewStaffRole.setEnabled(false);
        
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
        
        // Properties
        fActionShowProperties = new Action(Messages.RolePartsComposite_10) {
            @Override
            public void run() {
                InspectorManager.getInstance().showInspector();
            }
        };

        // Preview
        fActionShowPreview = new Action(Messages.RolePartsComposite_15) {
            @Override
            public void run() {
                ViewManager.showViewPart(PreviewView.ID, false);
            }
        };
        
        // Rename
        fActionRename = new LDAuthorActionFactory.RenameAction() {
            @Override
            public void run() {
                renameComponent();
            }
        };
        fActionRename.setEnabled(false);
    }
    
    /**
     * Hook into Preview Popups
     */
    private void hookPopups() {
        fTreeViewer.getControl().addMouseTrackListener(new MouseTrackAdapter() {
            PreviewPopup popup;

            @Override
            public void mouseHover(MouseEvent event) {
                // As per user Prefs
                if(!LDAuthorPlugin.getDefault().getPreferenceStore().getBoolean(ILDAuthorPreferenceConstants.PREFS_PREVIEW_POPUP)) {
                    return;
                }

                Point pt = new Point(event.x, event.y);

                Widget widget = event.widget;
                if(widget instanceof Tree) {
                    Tree w = (Tree) widget;
                    widget = w.getItem(pt);
                }

                if(widget == null) {
                    return;
                }

                Object object = widget.getData();
                if(object == null) {
                    return;
                }

                if(object instanceof FoldedRolePart) {
                    object = ((FoldedRolePart)object).getComponent();
                }
                else if(object instanceof ASChildTreeItem) {
                    object = ((ASChildTreeItem)object).getComponentRef().getLDModelObject();
                }

                if(popup != null && popup.getShell() != null && !popup.getShell().isDisposed()) {
                    popup.close();
                }

                popup = new PreviewPopup(getShell(), fEditor, object);
                popup.create();
                popup.open();
            }
        });
    }
    
    /**
     * Hook into a right-click menu on the Table
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#RolePartsPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);

        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });

        Menu menu = menuMgr.createContextMenu(fTreeViewer.getControl());
        fTreeViewer.getControl().setMenu(menu);

        fEditor.getSite().registerContextMenu(menuMgr, fTreeViewer);
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
        fActionPaste.setEnabled(false);
        fActionRename.setEnabled(!isEmpty);
        
        fActionShowProperties.setEnabled(!isEmpty);
        fActionShowPreview.setEnabled(!isEmpty);
    }

    /**
     * Fill the right-click menu for the Table
     * 
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        boolean isEmpty = fTreeViewer.getSelection().isEmpty();
        
        IActivityStructureModel as = fTreeViewer.getSelectedActivityStructure();
        
        IMenuManager newMenu = new MenuManager(Messages.RolePartsComposite_11, "new"); //$NON-NLS-1$
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
        newMenu.add(new Separator());
        newMenu.add(fActionNewLearnerRole);
        newMenu.add(fActionNewStaffRole);
        newMenu.add(new Separator());
        
        if(!isEmpty) {
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_START));
            // TODO
            //manager.add(fActionCut);
            //manager.add(fActionCopy);
            //manager.add(fActionPaste);
            manager.add(fActionDelete);
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_END));
            manager.add(fActionRename);
            manager.add(new Separator());
            manager.add(fActionShowProperties);
            manager.add(fActionShowPreview);
        }

        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /**
     * Register Global Actions on focus events
     */
    private void registerGlobalActions() {
        final IActionBars bars = fEditor.getEditorSite().getActionBars();
        
        fTreeViewer.getControl().addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                register(true);
            }
            
            public void focusLost(FocusEvent e) {
                register(false);
            }
            
            // Only de-register specific actions (not undo/redo!)
            private void register(boolean hasFocus) {
                bars.setGlobalActionHandler(ActionFactory.PROPERTIES.getId(), hasFocus ? fActionShowProperties : null);
                bars.setGlobalActionHandler(ActionFactory.CUT.getId(), hasFocus ? fActionCut : null);
                bars.setGlobalActionHandler(ActionFactory.COPY.getId(), hasFocus ? fActionCopy : null);
                bars.setGlobalActionHandler(ActionFactory.PASTE.getId(), hasFocus ? fActionPaste : null);
                bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), hasFocus ? fActionDelete : null);
                bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), hasFocus ? fActionSelectAll : null);
                bars.setGlobalActionHandler(ActionFactory.RENAME.getId(), hasFocus ? fActionRename : null);
                bars.updateActionBars();
            }
        });
    }

    /**
     * New Top level Activity creates a Role Part
     */
    private void doNewActivityAction(int type) {
        // Make sure we are referencing an Act
        IActModel act = fRolePartsTreeModelAdapter.getAct();
        if(act == null) {
            throw new RuntimeException("Act was null"); //$NON-NLS-1$
        }
        
        Wizard wizard = new NewRolePartWizard(act, type);
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
     * New Top level QTI Test Activity creates a Role Part
     */
    private void doNewQTITestActivityAction() {
        // Make sure we are referencing an Act
        IActModel act = fRolePartsTreeModelAdapter.getAct();
        if(act == null) {
            throw new RuntimeException("Act was null"); //$NON-NLS-1$
        }
        
        Wizard wizard = new NewQTITestRolePartWizard(act);
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

    private void doNewRoleAction(Class<?> type) {
        Wizard wizard = new NewRoleWizard((ILDModel)fEditor.getAdapter(ILDModel.class), type);
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
        
        for(Object o : selected) {
            // Delete Role Part
            if(o instanceof FoldedRolePart) {
                FoldedRolePart foldedRolePart = (FoldedRolePart)o;
                fRolePartsTreeModelAdapter.deleteFoldedRolePart(foldedRolePart);
            }
            // Delete child Activity reference from AS
            else if(o instanceof ASChildTreeItem) {
                IActivityRefModel ref = (IActivityRefModel)((ASChildTreeItem)o).getComponentRef();
                IActivityStructureModel asParent = (IActivityStructureModel)ref.getParent();
                if(asParent != null) {
                    asParent.removeActivity(ref);
                }
            }
        }
        
        fTreeViewer.refresh();
        fEditor.setDirty(true);
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
                Messages.RolePartsComposite_12,
                selected.size() > 1 ?
                        Messages.RolePartsComposite_13 
                        : 
                        Messages.RolePartsComposite_14);
    }

    /**
     * Rename selected component
     */
    private void renameComponent() {
        ILDModelObject component = null;
        
        Object obj = ((IStructuredSelection)fTreeViewer.getSelection()).getFirstElement();
        if(obj instanceof FoldedRolePart) {
            component = ((FoldedRolePart)obj).getComponent();
        }
        else if(obj instanceof ASChildTreeItem) {
            component = ((ASChildTreeItem)obj).getComponentRef().getLDModelObject();
        }
        
        if(component == null) {
            System.err.println("Component was null in renameComponent()"); //$NON-NLS-1$
            return;
        }
        
        RenameObjectDialog dialog = new RenameObjectDialog(Display.getDefault().getActiveShell(), ((ITitle)component).getTitle());
        if(dialog.open()) {
            String name = dialog.getNewName();
            if(name != null) {
                ((ITitle)component).setTitle(name);
                component.getLDModel().setDirty();
            }
        }
    }

}
