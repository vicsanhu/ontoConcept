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
package org.tencompetence.ldauthor.ui.views.inspector.notifications;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.imsldmodel.types.INotificationType;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.views.inspector.common.EmailDataTypeTableViewer;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * NotificationEditorComposite
 * 
 * @author Phillip Beauvoir
 * @version $Id: NotificationEditorComposite.java,v 1.5 2009/06/19 10:14:48 phillipus Exp $
 */
public class NotificationEditorComposite extends Composite
implements PropertyChangeListener {
    
    private Text fTextSubject;
    private ComboViewer fActivityComboViewer;
    private EmailDataTypeTableViewer fEmailDataTypeTableViewer;

    private INotificationType fNotificationType;
    
    private ILDModel fCurrentLDModel;
    
    private boolean fIsUpdating;
    
    public NotificationEditorComposite(Composite parent) {
        super(parent, SWT.NULL);
        
        setLayout(new GridLayout(2, false));
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        // Subject
        AppFormToolkit.getInstance().createLabel(this, Messages.NotificationEditorComposite_0);
        fTextSubject = AppFormToolkit.getInstance().createText(this, "", SWT.BORDER); //$NON-NLS-1$
        fTextSubject.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fTextSubject.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if(!fIsUpdating && fNotificationType != null) {
                    fNotificationType.setSubject(fTextSubject.getText());
                    fNotificationType.getLDModel().setDirty();
                }
            }
        });
        
        // Activity
        AppFormToolkit.getInstance().createLabel(this, Messages.NotificationEditorComposite_1);
        fActivityComboViewer = new ComboViewer(this, SWT.READ_ONLY);
        fActivityComboViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fActivityComboViewer.setContentProvider(new ActivityComboViewerProvider());
        fActivityComboViewer.setLabelProvider(new ActivityComboViewerLabelProvider());
        
        fActivityComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if(!fIsUpdating && fNotificationType != null) {
                    Object selected = ((IStructuredSelection)event.getSelection()).getFirstElement();
                    if(selected instanceof IActivityModel) {
                        fNotificationType.setActivityReference((IActivityModel)selected);
                    }
                    else {
                        fNotificationType.setActivityReference(null);
                    }
                    fNotificationType.getLDModel().setDirty();
                }
            }
        });
        
        // Email data
        AppFormToolkit.getInstance().createLabel(this, Messages.NotificationEditorComposite_2);
        
        Composite tableComp = new Composite(this, SWT.NULL);
        tableComp.setLayout(new TableColumnLayout());
        GridData gd = new GridData(GridData.FILL_BOTH);
        tableComp.setLayoutData(gd);
        fEmailDataTypeTableViewer = new EmailDataTypeTableViewer(tableComp);
        
        // Dispose listener
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if(fCurrentLDModel != null) {
                    fCurrentLDModel.removePropertyChangeListener(NotificationEditorComposite.this);
                }
            }
        });
    }

    public void setNotificationType(INotificationType notificationType) {
        if(fNotificationType == notificationType) {
            return;
        }
        
        ILDModel newLDModel = notificationType.getLDModel();
        
        // Remove previous Model listener if any
        if(fCurrentLDModel != newLDModel) {
            if(fCurrentLDModel != null) {
                fCurrentLDModel.removePropertyChangeListener(this);
            }
            
            // Add new one
            newLDModel.addPropertyChangeListener(this);

            // New LD Model
            fCurrentLDModel = newLDModel;
        }

        fNotificationType = notificationType;
        
        fIsUpdating = true;
        
        fTextSubject.setText(StringUtils.safeString(fNotificationType.getSubject()));
        
        fActivityComboViewer.setInput(fNotificationType.getLDModel());
        
        IActivityModel activity = fNotificationType.getActivityReference();
        if(activity != null) {
            fActivityComboViewer.setSelection(new StructuredSelection(activity));
        }
        else {
            fActivityComboViewer.setSelection(new StructuredSelection("")); //$NON-NLS-1$
        }
        
        fEmailDataTypeTableViewer.setEmailDataTypeOwner(fNotificationType);
        
        fIsUpdating = false;
    }
    
    /* 
     * When a Property changes update
     */
    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        Object newValue = evt.getNewValue();
        
        // Activity change
        if(source instanceof IActivityModel || newValue instanceof IActivityModel) {
            fActivityComboViewer.refresh();
        }
    }


    // ===================================================================================
    //                           Combo Viewer Provider
    // ===================================================================================
    
    private class ActivityComboViewerProvider
    implements IStructuredContentProvider {
        
        public Object[] getElements(Object input) {
            List<Object> list = new ArrayList<Object>();
            
            // No Activity
            list.add(""); //$NON-NLS-1$

            list.addAll(((ILDModel)input).getActivitiesModel().getLearningAndSupportActivities());

            return list.toArray();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

    }
    
    private class ActivityComboViewerLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            if(element instanceof String) {
                return Messages.NotificationEditorComposite_3;
            }
            return ((IActivityModel)element).getTitle();
        }
    }
}
