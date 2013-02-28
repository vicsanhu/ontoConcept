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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.imsldmodel.expressions.IThenType;
import org.tencompetence.imsldmodel.types.INotificationType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.expressions.ExpressionValueControl;
import org.tencompetence.ldauthor.ui.expressions.ExpressionsAdapterFactory;
import org.tencompetence.ldauthor.ui.views.inspector.common.EmailDataTypeTableViewer;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Notification Composite
 * 
 * @author Phillip Beauvoir
 * @version $Id: NotificationComposite.java,v 1.6 2009/06/19 10:14:48 phillipus Exp $
 */
public class NotificationComposite extends Composite {
    
    private IThenType fThenType;
    private INotificationType fNotificationType;
    
    private Text fSubjectText;
    private ExpressionValueControl fActivityRefControl;
    private EmailDataTypeTableViewer fEmailDataTypeTableViewer;
    
    public NotificationComposite(Composite parent) {
        super(parent, SWT.NULL);
        
        GridLayout layout = new GridLayout(3, false);
        layout.marginHeight = 5;
        layout.marginWidth = 0;
        layout.horizontalSpacing = ExpressionsAdapterFactory.HORIZONTAL_SPACING;
        setLayout(layout);
        
        AppFormToolkit.getInstance().adapt(this);
        
        GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
        setLayoutData(gd);
        
        Label l = AppFormToolkit.getInstance().createLabel(this, Messages.NotificationComposite_0);
        gd = new GridData(GridData.FILL, GridData.CENTER, false, true);
        gd.widthHint = 50;
        l.setLayoutData(gd);
        
        Composite comp = new Composite(this, SWT.BORDER);
        GridLayout layout2 = new GridLayout(2, false);
        layout2.marginHeight = 2;
        layout2.marginWidth = 2;
        layout2.horizontalSpacing = ExpressionsAdapterFactory.HORIZONTAL_SPACING;
        comp.setLayout(layout2);
        comp.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        Label label = new Label(comp, SWT.NULL);
        label.setText(Messages.NotificationComposite_1);
        fSubjectText = AppFormToolkit.getInstance().createText(comp, "", SWT.BORDER); //$NON-NLS-1$
        gd = new GridData(GridData.FILL, GridData.CENTER, true, true);
        fSubjectText.setLayoutData(gd);
        
        label = new Label(comp, SWT.NULL);
        label.setText(Messages.NotificationComposite_2);
        fActivityRefControl = new ExpressionValueControl(comp);
        
        label = new Label(comp, SWT.NULL);
        label.setText(Messages.NotificationComposite_3);
        Composite tableComp = new Composite(comp, SWT.NULL);
        tableComp.setLayout(new TableColumnLayout());
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 100;
        tableComp.setLayoutData(gd);
        fEmailDataTypeTableViewer = new EmailDataTypeTableViewer(tableComp);
        
        Button removeButton = AppFormToolkit.getInstance().createButton(this, null, SWT.NULL);
        removeButton.setImage(ImageFactory.getImage(ImageFactory.ICON_MINUS));
        gd = new GridData(GridData.END, SWT.CENTER, false, false);
        gd.heightHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        gd.widthHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        removeButton.setLayoutData(gd);
        
        /*
         * Remove this object
         */
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dispose(); // This first
                fThenType.removeMember(fNotificationType);
                fNotificationType.getLDModel().setDirty();
            }
        });
    }

    public void setNotificatioType(IThenType thenType, INotificationType notificationType) {
        fThenType = thenType;
        fNotificationType = notificationType;
        
        final ILDModel ldModel = thenType.getLDModel();
        
        String subject = StringUtils.safeString(fNotificationType.getSubject());
        fSubjectText.setText(subject);
        
        fSubjectText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                fNotificationType.setSubject(fSubjectText.getText());
                ldModel.setDirty();
            }
        });
        
        Object selected = fNotificationType.getActivityReference();
        if(selected == null) {
            selected = Messages.NotificationComposite_4;
        }
        
        List<Object> list = new ArrayList<Object>();
        
        // No Activity
        list.add(Messages.NotificationComposite_4);
        list.addAll(ldModel.getActivitiesModel().getLearningAndSupportActivities());
        fActivityRefControl.setItems(list.toArray(), selected);
        
        fActivityRefControl.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                Object selected = ((StructuredSelection)event.getSelection()).getFirstElement();
                if(selected instanceof IActivityModel) {
                    fNotificationType.setActivityReference((IActivityModel)selected);
                }
                else {
                    fNotificationType.setActivityReference(null);
                }
                
                ldModel.setDirty();
            }
        });
        
        fEmailDataTypeTableViewer.setEmailDataTypeOwner(fNotificationType);
    }

}