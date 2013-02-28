/*
 * Copyright (c) 2008, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.ui.views.inspector.item;

import java.io.File;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.ISharedImages;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.types.IItemModelType;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.editors.EditorManager;
import org.tencompetence.ldauthor.ui.wizards.item.NewItemWizard;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * ItemModelEditor
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemModelEditor.java,v 1.10 2009/06/15 12:58:41 phillipus Exp $
 */
public class ItemModelEditor extends Composite {
    
    /**
     * Item Type Owner Model
     */
    private IItemTypeContainer fItemModel;
    
    private ILDModel fLDModel;
    
    private IAction fActionShowViewer, fActionNewItem, fActionNewChildItem, fActionRemoveItem, fActionEditItem;
    
    private Text fItemModelTitleTextField;
    
    private ItemModelTreeTable fItemTreeTable;
    
    ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fLDModel != null) {
                fLDModel.setDirty();
            }
            if(fItemModel instanceof IItemModelType) {
                ((IItemModelType)fItemModel).setTitle(fItemModelTitleTextField.getText());
            }
        }
    };

    
    public ItemModelEditor(ItemModelComposite parent, int style) {
        super(parent, style);
        setup();
    }

    private void setup() {
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 3;
        setLayout(layout);
        
        makeActions();
                
        // Use a parent composite for the table with TreeColumnLayout.
        // This allows for auto-resizing of table columns
        Composite c1 = AppFormToolkit.getInstance().createComposite(this);
        c1.setLayout(new TreeColumnLayout());
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 2;
        c1.setLayoutData(gd);
        
        fItemTreeTable = new ItemModelTreeTable(this, c1);
        hookContextMenu(fItemTreeTable);
        
        /*
         * Listen to tree selections
         */
        fItemTreeTable.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                handleSelectionChanged(event.getSelection());
            }
        });
        
        // Double-click handler
        fItemTreeTable.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                editSelectedItem();
            }
        });
    }
    
    private void makeActions() {
        // Toolbar
        ToolBar toolBar = new ToolBar(this, SWT.FLAT);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd.horizontalSpan = 2;
        toolBar.setLayoutData(gd);
        AppFormToolkit.getInstance().adapt(toolBar);
        
        ToolBarManager toolBarmanager = new ToolBarManager(toolBar);
        
        // New Item
        fActionNewItem = new Action(Messages.ItemModelEditor_0) {
            @Override
            public void run() {
                addNewItem(fItemModel);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.ICON_ITEM),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };

        // New Child Item
        fActionNewChildItem = new Action(Messages.ItemModelEditor_1) {
            @Override
            public void run() {
                Object obj = ((IStructuredSelection)fItemTreeTable.getSelection()).getFirstElement();
                if(obj instanceof IItemTypeContainer) {
                    addNewItem((IItemTypeContainer)obj);
                }
            }

            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.ICON_CHILD_ITEM),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        // Remove
        fActionRemoveItem = new Action(Messages.ItemModelEditor_2) {
            @Override
            public void run() {
                deleteItems();
            }

            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getSharedImageDescriptor(ISharedImages.IMG_TOOL_DELETE);
            }
        };
        
        // Edit File
        fActionEditItem = new Action(Messages.ItemModelEditor_3) {
            @Override
            public void run() {
                editSelectedItem();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_EDIT);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionShowViewer = new Action(Messages.ItemModelEditor_4) {
            @Override
            public void run() {
                ((ItemModelComposite)getParent()).showViewer();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_VIEW);
            }

            @Override
            public String getToolTipText() {
                return getText();
            }
        };

        fActionNewChildItem.setEnabled(false);
        fActionRemoveItem.setEnabled(false);
        fActionEditItem.setEnabled(false);
        
        toolBarmanager.add(fActionEditItem);
        toolBarmanager.add(new Separator());
        toolBarmanager.add(fActionNewItem);
        toolBarmanager.add(fActionNewChildItem);
        toolBarmanager.add(fActionRemoveItem);
        toolBarmanager.add(new Separator());
        toolBarmanager.add(fActionShowViewer);

        toolBarmanager.update(true);
    }
    
    /**
     * Set the Items to display
     * 
     * @param model
     */
    public void setItemModel(IItemTypeContainer model) {
        fItemModel = model;
        fLDModel = fItemModel.getLDModel();
        
        // Title field is only valid for ItemModelType
        if(model instanceof IItemModelType) {
            // Title
            if(fItemModelTitleTextField == null) {
                AppFormToolkit.getInstance().createLabel(this, Messages.ItemModelEditor_5, SWT.NONE);
                fItemModelTitleTextField = AppFormToolkit.getInstance().createText(this, ""); //$NON-NLS-1$
                fItemModelTitleTextField.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false));
            }
            fItemModelTitleTextField.removeModifyListener(fModifyListener);
            fItemModelTitleTextField.setText(StringUtils.safeString(((IItemModelType)model).getTitle()));
            fItemModelTitleTextField.addModifyListener(fModifyListener);
        }
        
        fItemTreeTable.setItemModel(model);
        
        updateItemMenu();
    }
    
    /**
     * Hook into a right-click menu
     * @param itemTreeTable 
     */
    private void hookContextMenu(TreeViewer viewer) {
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
    }

    /**
     * Fill the right-click menu
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        updateItemMenu();
        
        manager.add(fActionNewItem);
        manager.add(fActionNewChildItem);
        manager.add(fActionEditItem);

        manager.add(new Separator());
        manager.add(fActionRemoveItem);
    }
    
    /**
     * Update the Add New Item menu depending on how many allowed
     */
    private void updateItemMenu() {
        fActionNewItem.setEnabled(fItemModel.canAddChildItem());
    }

    /**
     * Selection changed
     * @param selection
     */
    void handleSelectionChanged(ISelection selection) {
        boolean selected = !selection.isEmpty();
        IItemType itemType = (IItemType)((IStructuredSelection)selection).getFirstElement();
        
        // New Child
        fActionNewChildItem.setEnabled(selected);
        
        // Delete
        fActionRemoveItem.setEnabled(selected);
        
        // Edit
        fActionEditItem.setEnabled(selected && isEditableItem(itemType));
    }
    
    /**
     * Add a new Item
     * @param item
     */
    public boolean addNewItem(IItemTypeContainer parent) {
        NewItemWizard wizard = new NewItemWizard(fLDModel);
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
        if(dialog.open() != IDialogConstants.OK_ID) {
            return false;
        }

        IItemType itemType = wizard.getItem();
        
        parent.addChildItem(itemType);

        fItemTreeTable.refresh();
        fLDModel.setDirty();

        // If a child item, expand parent
        if(parent instanceof IItemType) {
            fItemTreeTable.expandToLevel(parent, 1);
        }
        
        updateItemMenu();

        //editElement(itemType, 0);
        
        return true;
    }

    /**
     * Delete an Item
     * @param item
     */
    private void deleteItems() {
        List<?> selected = ((IStructuredSelection)fItemTreeTable.getSelection()).toList();
        
        if(!askUserDeleteItems(selected.size() > 1)) {
            return;
        }
        
        for(Object o : selected) {
            if(o instanceof IItemType) {
                IItemType itemType = (IItemType)o;
                itemType.getParent().removeChildItem(itemType);
                itemType.dispose();
            }
        }
        
        fItemTreeTable.refresh();
        fLDModel.setDirty();
        updateItemMenu();
    }

    /**
     * Ask the user whether they wish to delete the given objects
     * 
     * @param objects The objects to delete
     * @return True if the user said yes, false if user says no or cancels
     */
    private boolean askUserDeleteItems(boolean plural) {
        // Confirmation dialog
        return MessageDialog.openQuestion(
                Display.getDefault().getActiveShell(),
                Messages.ItemModelEditor_6,
                plural ?
                        Messages.ItemModelEditor_7 
                        : 
                            Messages.ItemModelEditor_8);
    }
    
    private boolean isEditableItem(IItemType itemType) {
        if(itemType == null) {
            return false;
        }
        
        String href = getItemHREF(itemType);
        
        if(!StringUtils.isSet(href)) {
            return false;
        }
        
        File file = new File(fLDModel.getRootFolder(), href);
        
        return file.exists();
    }

    private String getItemHREF(IItemType itemType) {
        if(itemType != null) {
            IResourceModel resource = fLDModel.getResourcesModel().getResource(itemType);
            if(resource != null) {
                return resource.getHref();
            }
        }
        return null;
    }
    
    private void editSelectedItem() {
        IItemType itemType = (IItemType)((IStructuredSelection)fItemTreeTable.getSelection()).getFirstElement();
        if(itemType == null) {
            return;
        }

        String href = getItemHREF(itemType);

        if(!StringUtils.isSet(href)) {
            return;
        }

        // Open in an editor if possible
        File file = new File(fLDModel.getRootFolder(), href);
        EditorManager.editFile(file, fLDModel);
    }

}
