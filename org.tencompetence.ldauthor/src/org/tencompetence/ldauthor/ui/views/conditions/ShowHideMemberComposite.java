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
import org.tencompetence.imsldmodel.IIdentifier;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IClassType;
import org.tencompetence.imsldmodel.expressions.IShowHideType;
import org.tencompetence.imsldmodel.expressions.IUOLHrefType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.expressions.ExpressionValueControl;
import org.tencompetence.ldauthor.ui.expressions.ExpressionsAdapterFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Show/Hide Member Composite
 * 
 * @author Phillip Beauvoir
 * @version $Id: ShowHideMemberComposite.java,v 1.8 2009/06/16 19:56:44 phillipus Exp $
 */
public class ShowHideMemberComposite extends Composite {
    
    private IShowHideType fShowHideType;
    private ILDModelObject fElement;
    private String fType;
    
    private Label fTypeLabel;
    
    /**
     * The top ancestor that will need laying out when children are added or removed. Could be null.
     */
    private Composite fLayoutComposite;

    
    public ShowHideMemberComposite(Composite parent, IShowHideType showHideType) {
        super(parent, SWT.NULL);
        
        fShowHideType = showHideType;
        
        // Do this now
        fLayoutComposite = getLayoutParentComposite(parent);
        
        GridLayout layout = new GridLayout(3, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing = ExpressionsAdapterFactory.HORIZONTAL_SPACING;
        setLayout(layout);
        
        AppFormToolkit.getInstance().adapt(this);
        
        // Indent this
        GridData gd = new GridData(GridData.FILL, GridData.CENTER, true, true);
        gd.horizontalIndent = 25;
        setLayoutData(gd);
        
        fTypeLabel = AppFormToolkit.getInstance().createLabel(this, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL, GridData.CENTER, false, true);
        gd.widthHint = 120;
        fTypeLabel.setLayoutData(gd);
        
        Button removeButton =  AppFormToolkit.getInstance().createButton(this, null, SWT.NULL);
        removeButton.setImage(ImageFactory.getImage(ImageFactory.ICON_MINUS));
        gd = new GridData(GridData.END, SWT.CENTER, false, false);
        gd.heightHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        gd.widthHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        removeButton.setLayoutData(gd);
        
        /*
         * Remove this member element
         */
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(fElement != null) {
                    fShowHideType.removeMember(fElement);
                    fShowHideType.getLDModel().setDirty();
                }
                
                dispose();
                doLayout();
            }
        });
    }
    
    public void setElement(ILDModelObject element) {
        fElement = element;
        setType(element.getTagName());
    }

    public void setType(String type) {
        fType = type;
        
        fTypeLabel.setText(ConditionsAdapterFactory.getFriendlyName(type));
        
        if(LDModelFactory.CLASS.equals(type)) {
            showClassComposite();
        }
        else if(LDModelFactory.UOL_HREF.equals(type)) {
            showUOLHrefComposite();
        }
        else {
            showReferencedComponentComposite();
        }
    }

    private void showReferencedComponentComposite() {
        ExpressionValueControl valueControl = new ExpressionValueControl(this);
        valueControl.moveBelow(fTypeLabel);
        
        Object[] items = null;
        
        final ILDModel ldModel = fShowHideType.getLDModel();
        
        if(LDModelFactory.ITEM_REF.equals(fType)) {
            valueControl.setLDModelItems(ldModel, fElement);
        }
        else if(LDModelFactory.ENVIRONMENT_REF.equals(fType)) {
            items = ldModel.getEnvironmentsModel().getChildren().toArray();
        }
        else if(LDModelFactory.LEARNING_ACTIVITY_REF.equals(fType)) {
            items = ldModel.getActivitiesModel().getLearningActivitiesModel().getChildren().toArray();
        }
        else if(LDModelFactory.SUPPORT_ACTIVITY_REF.equals(fType)) {
            items = ldModel.getActivitiesModel().getSupportActivitiesModel().getChildren().toArray();
        }
        else if(LDModelFactory.ACTIVITY_STRUCTURE_REF.equals(fType)) {
            items = ldModel.getActivitiesModel().getActivityStructuresModel().getChildren().toArray();
        }
        else if(LDModelFactory.PLAY_REF.equals(fType)) {
            items = ldModel.getMethodModel().getPlaysModel().getChildren().toArray();
        }
        
        if(items != null) {
            valueControl.setItems(items, fElement);
        }
        
        valueControl.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                Object selected = ((StructuredSelection)event.getSelection()).getFirstElement();
                if(selected instanceof ILDModelObject) {
                    if(fElement instanceof ILDModelObjectReference) {
                        ((ILDModelObjectReference)fElement).setReferenceIdentifer(((IIdentifier)selected).getIdentifier());
                    }
                    else {
                        fElement = (ILDModelObject)selected;
                        fShowHideType.addMember((ILDModelObject)selected);
                    }
                    ldModel.setDirty();
                }
            }
        });
    }

    private void showUOLHrefComposite() {
        final Text text = AppFormToolkit.getInstance().createText(this, "", SWT.BORDER); //$NON-NLS-1$
        text.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, true));
        text.moveBelow(fTypeLabel);
        
        final IUOLHrefType uolHref = (IUOLHrefType)fElement;
        
        text.setText(StringUtils.safeString(uolHref.getHref()));
        
        text.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                uolHref.setHref(text.getText());
                uolHref.getLDModel().setDirty();
            }
        });
    }

    private void showClassComposite() {
        Composite comp = new Composite(this, SWT.BORDER);
        
        GridLayout layout = new GridLayout(3, false);
        layout.marginHeight = 2;
        layout.marginWidth = 2;
        layout.horizontalSpacing = ExpressionsAdapterFactory.HORIZONTAL_SPACING;
        comp.setLayout(layout);
        comp.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        Label label = new Label(comp, SWT.NULL);
        label.setText(Messages.ShowHideMemberComposite_0);
        final Text textClass = AppFormToolkit.getInstance().createText(comp, "", SWT.BORDER); //$NON-NLS-1$
        GridData gd = new GridData(GridData.FILL, SWT.CENTER, true, true);
        gd.horizontalSpan = 2;
        textClass.setLayoutData(gd);
        
        label = new Label(comp, SWT.NULL);
        label.setText(Messages.ShowHideMemberComposite_1);
        final Text textTitle = AppFormToolkit.getInstance().createText(comp, "", SWT.BORDER); //$NON-NLS-1$
        textTitle.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, true));
        
        final Button buttonWithControl = new Button(comp, SWT.CHECK);
        buttonWithControl.setText(Messages.ShowHideMemberComposite_2);
        
        final IClassType classType = (IClassType)fElement;
        
        textClass.setText(StringUtils.safeString(classType.getClassString()));
        textTitle.setText(StringUtils.safeString(classType.getTitle()));
        buttonWithControl.setSelection(classType.isWithControl());
        
        textClass.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                classType.setClassString(textClass.getText());
                classType.getLDModel().setDirty();
            }
        });
        
        textTitle.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                classType.setTitle(textTitle.getText());
                classType.getLDModel().setDirty();
            }
        });
        
        buttonWithControl.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                classType.setWithControl(buttonWithControl.getSelection());
                classType.getLDModel().setDirty();
            }
        });
        
        comp.moveBelow(fTypeLabel);
    }

    /**
     * Re-layout
     */
    private void doLayout() {
        if(fLayoutComposite != null) {
            fLayoutComposite.layout();
            fLayoutComposite.pack();
        }
    }
    
    private Composite getLayoutParentComposite(Composite parent) {
        while(parent != null && !"_top_control_".equals(parent.getData())) { //$NON-NLS-1$
            parent = parent.getParent();
        }
        return parent;
    }
}
