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
package org.tencompetence.ldauthor.ui.views.inspector.notifications;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.INotificationType;
import org.tencompetence.imsldmodel.types.IOnCompletionType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.StackComposite;


/**
 * Composite for Notifications
 * 
 * @author Phillip Beauvoir
 * @version $Id: OnCompletionNotificationsComposite.java,v 1.6 2009/06/19 10:14:48 phillipus Exp $
 */
public class OnCompletionNotificationsComposite extends Composite
implements PropertyChangeListener {
    
    private NotificationsTable fNotificationsTable;
    
    private NotificationEditorComposite fNotificationEditorComposite;
    
    /**
     * Editor Stack Composite
     */
    private StackComposite fStackComposite;
    
    private IAction fActionNewNotificationAction;
    private IAction fActionDelete;

    private IOnCompletionType fOnCompletionType;

    public OnCompletionNotificationsComposite(Composite parent, int style) {
        super(parent, style);
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        
        SashForm sash = new SashForm(this, SWT.HORIZONTAL);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Use a parent composite for the table with TableColumnLayout.
        // This allows for auto-resizing of table columns
        Composite composite = new Composite(sash, SWT.NULL);
        composite.setLayout(new TableColumnLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        fNotificationsTable = new NotificationsTable(composite);

        fStackComposite = new StackComposite(sash, SWT.NULL);

        sash.setWeights(new int[] { 40, 60 });
        
        fNotificationsTable.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                Object object = ((StructuredSelection)event.getSelection()).getFirstElement();
                updateMenu(object);
                updateEditorPanel(object);
            }
        });
        
        makeActions();
        hookContextMenu();
        
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if(fOnCompletionType != null) {
                    fOnCompletionType.getLDModel().removePropertyChangeListener(OnCompletionNotificationsComposite.this);
                }
            }
        });
    }

    public void setOnCompletionType(IOnCompletionType onCompletionType) {
        if(fOnCompletionType == onCompletionType) {
            return;
        }
        
        if(fOnCompletionType != null) {
            fOnCompletionType.getLDModel().removePropertyChangeListener(this);
        }
        
        fOnCompletionType = onCompletionType;
        fNotificationsTable.setInput(onCompletionType);
        
        fOnCompletionType.getLDModel().addPropertyChangeListener(this);
    }
    
    /**
     * Update the Editor Panel
     * @param object
     */
    private void updateEditorPanel(Object object) {
        if(object instanceof INotificationType) {
            if(fNotificationEditorComposite == null) {
                fNotificationEditorComposite = new NotificationEditorComposite(fStackComposite);
            }
            fStackComposite.setControl(fNotificationEditorComposite);
            fNotificationEditorComposite.setNotificationType((INotificationType)object);
        }
        else {
            fStackComposite.setControl(null);
        }
    }
    
    private void addNotification() {
        INotificationType item = (INotificationType)LDModelFactory.createModelObject(LDModelFactory.NOTIFICATION, fOnCompletionType.getLDModel());
        fOnCompletionType.getNotificationTypes().add(item);
        
        // Ensure there's one Email Data Type
        IRoleModel role = fOnCompletionType.getLDModel().getRolesModel().getDefaultLearnerRole();
        if(role == null) {
            role = fOnCompletionType.getLDModel().getRolesModel().getDefaultStaffRole();
        }
        if(role != null) {
            item.addEmailDataType(role);
        }
        
        fOnCompletionType.getLDModel().setDirty();
        fNotificationsTable.refresh();
        
        fNotificationsTable.setSelection(new StructuredSelection(item));
    }
    
    /**
     * Delete Properties
     */
    private void deleteItems() {
        List<?> selected = ((IStructuredSelection)fNotificationsTable.getSelection()).toList();
        
        if(!askUserDeleteItems(selected.size() > 1)) {
            return;
        }
        
        for(Object o : selected) {
            INotificationType item = (INotificationType)o;
            fOnCompletionType.getNotificationTypes().remove(item);
        }
        fOnCompletionType.getLDModel().setDirty();

        fNotificationsTable.refresh();
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
                Messages.OnCompletionNotificationsComposite_0,
                plural ?
                        Messages.OnCompletionNotificationsComposite_1 
                        : 
                        Messages.OnCompletionNotificationsComposite_2);
    }

    
    private void makeActions() {
        // New Item
        fActionNewNotificationAction = new Action(Messages.OnCompletionNotificationsComposite_3) {
            @Override
            public void run() {
                addNotification();
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.ICON_GO),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };

        fActionDelete = new Action(Messages.OnCompletionNotificationsComposite_0) {
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
        
        Menu menu = menuMgr.createContextMenu(fNotificationsTable.getControl());
        fNotificationsTable.getControl().setMenu(menu);
    }
    
    /**
     * Fill the right-click menu
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        manager.add(fActionNewNotificationAction);
        manager.add(fActionDelete);
        manager.add(new Separator());
    }
    
    private void updateMenu(Object object) {
        fActionDelete.setEnabled(object != null);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        if(source instanceof INotificationType) {
            fNotificationsTable.update(evt.getSource(), null);
        }
        if(source instanceof IActivityModel) {
            fNotificationsTable.refresh();
        }
    }
}
