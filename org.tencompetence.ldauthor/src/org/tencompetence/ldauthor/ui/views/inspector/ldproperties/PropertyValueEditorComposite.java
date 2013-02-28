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
package org.tencompetence.ldauthor.ui.views.inspector.ldproperties;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IChangePropertyValueType;
import org.tencompetence.imsldmodel.expressions.IPropertyRefValuePairOwner;
import org.tencompetence.imsldmodel.expressions.IWhenPropertyValueIsSetType;
import org.tencompetence.imsldmodel.types.ICompleteType;
import org.tencompetence.imsldmodel.types.IOnCompletionType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.views.ViewManager;
import org.tencompetence.ldauthor.ui.views.ldproperties.LDPropertiesView;


/**
 * Composite for editing "When a Property Value is set" condition for completion of UOL, Play, Act and Activity
 * 
 * @author Phillip Beauvoir
 * @version $Id: PropertyValueEditorComposite.java,v 1.6 2009/06/16 20:31:29 phillipus Exp $
 */
public class PropertyValueEditorComposite extends Composite {
    
    private PropertyValueTableViewer fTableViewer;
    
    private IAction fActionAddProperty;
    private IAction fActionDelete;
    private IAction fActionShowAdvancedEditor;
    private IAction fActionShowPropertiesView;
    
    /*
     * Owner could be ICompleteType or IOnCompletionType
     */
    private Object fOwner;

    public PropertyValueEditorComposite(Composite parent, int style) {
        super(parent, style);
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        
        Composite client = new Composite(this, SWT.NULL);
        client.setLayout(new TableColumnLayout());
        client.setLayoutData(new GridData(GridData.FILL_BOTH));
        fTableViewer = new PropertyValueTableViewer(client);
        
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                ISelection selection = event.getSelection();
                updateMenu(selection);
            }
        });
        
        makeActions();
        hookContextMenu();
    }
    
    private void makeActions() {
        // New Item
        fActionAddProperty = new Action(Messages.PropertyValueEditorComposite_0) {
            @Override
            public void run() {
                addProperty();
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

        fActionShowPropertiesView = new Action(Messages.PropertyValueEditorComposite_1) {
            @Override
            public void run() {
                ViewManager.showViewPart(LDPropertiesView.ID, false);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionShowAdvancedEditor = new Action(Messages.PropertyValueEditorComposite_2) {
            @Override
            public void run() {
                IPropertyRefValuePairOwner item = (IPropertyRefValuePairOwner)((IStructuredSelection)fTableViewer.getSelection()).getFirstElement();
                if(item != null) {
                    editAdvancedPropertyValueEditor(item);
                }
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionDelete = new Action(Messages.PropertyValueEditorComposite_3) {
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
        
        fActionShowAdvancedEditor.setEnabled(false);
        fActionDelete.setEnabled(false);
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
        
        Menu menu = menuMgr.createContextMenu(fTableViewer.getControl());
        fTableViewer.getControl().setMenu(menu);
    }
    
    /**
     * Fill the right-click menu
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        manager.add(fActionAddProperty);
        manager.add(fActionDelete);
        manager.add(new Separator());
        manager.add(fActionShowAdvancedEditor);
        manager.add(fActionShowPropertiesView);
    }
    
    private void updateMenu(ISelection selection) {
        fActionDelete.setEnabled(!selection.isEmpty());
        fActionShowAdvancedEditor.setEnabled(!selection.isEmpty());
    }

    /**
     * Add a new property Value Item
     */
    private void addProperty() {
        if(fOwner instanceof ICompleteType) {
            ICompleteType completeType = (ICompleteType)fOwner;
            IWhenPropertyValueIsSetType type = (IWhenPropertyValueIsSetType)LDModelFactory.createModelObject(LDModelFactory.WHEN_PROPERTY_VALUE_IS_SET, completeType.getOwner().getLDModel());
            completeType.getWhenPropertyValueIsSetTypes().add(type);
            completeType.getOwner().getLDModel().setDirty();
            fTableViewer.refresh();
            fTableViewer.editElement(type, 0);
        }
        
        else if(fOwner instanceof IOnCompletionType) {
            IOnCompletionType completeType = (IOnCompletionType)fOwner;
            IChangePropertyValueType type = (IChangePropertyValueType)LDModelFactory.createModelObject(LDModelFactory.CHANGE_PROPERTY_VALUE, completeType.getParent().getLDModel());
            completeType.getChangePropertyValueTypes().add(type);
            completeType.getParent().getLDModel().setDirty();
            fTableViewer.refresh();
            fTableViewer.editElement(type, 0);
        }
    }
    
    /**
     * Delete Properties
     */
    private void deleteItems() {
        List<?> selected = ((IStructuredSelection)fTableViewer.getSelection()).toList();
        
        if(!askUserDeleteItems(selected.size() > 1)) {
            return;
        }
        
        if(fOwner instanceof ICompleteType) {
            ICompleteType completeType = (ICompleteType)fOwner;
            for(Object o : selected) {
                IPropertyRefValuePairOwner item = (IPropertyRefValuePairOwner)o;
                completeType.getWhenPropertyValueIsSetTypes().remove(item);
            }
            completeType.getOwner().getLDModel().setDirty();
        }
        
        else if(fOwner instanceof IOnCompletionType) {
            IOnCompletionType completeType = (IOnCompletionType)fOwner;
            for(Object o : selected) {
                IPropertyRefValuePairOwner item = (IPropertyRefValuePairOwner)o;
                completeType.getChangePropertyValueTypes().remove(item);
            }
            completeType.getParent().getLDModel().setDirty();
        }
        
        fTableViewer.refresh();
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
                Messages.PropertyValueEditorComposite_3,
                plural ?
                        Messages.PropertyValueEditorComposite_4 
                        : 
                        Messages.PropertyValueEditorComposite_5);
    }

    /**
     * Edit using the advanced Property editor
     */
    private void editAdvancedPropertyValueEditor(IPropertyRefValuePairOwner propertyRefValuePairOwner) {
        AdvancedPropertyValueEditorDialog dialog = new AdvancedPropertyValueEditorDialog(getShell(), propertyRefValuePairOwner);
        dialog.open();
        fTableViewer.refresh();
    }

    public void setOwner(Object owner) {
        fOwner = owner;
        fTableViewer.setOwner(owner);
    }
}