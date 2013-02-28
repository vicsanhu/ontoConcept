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
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.expressions.IChangePropertyValueType;
import org.tencompetence.imsldmodel.expressions.IPropertyValueType;
import org.tencompetence.imsldmodel.expressions.IThenType;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.expressions.ExpressionValueControl;
import org.tencompetence.ldauthor.ui.expressions.ExpressionsAdapterFactory;
import org.tencompetence.ldauthor.ui.views.inspector.ldproperties.AdvancedPropertyValueEditorDialog;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Change Property Value Composite
 * 
 * @author Phillip Beauvoir
 * @version $Id: ChangePropertyValueComposite.java,v 1.6 2009/07/02 11:33:56 phillipus Exp $
 */
public class ChangePropertyValueComposite extends Composite {
    
    private IThenType fThenType;
    private IChangePropertyValueType fMemberType;
    
    private ExpressionValueControl fpropertyRefControl;
    private Text fPropertyValueText;
    private Button fAdvancedButton;
    
    public ChangePropertyValueComposite(Composite parent) {
        super(parent, SWT.NULL);
        
        GridLayout layout = new GridLayout(3, false);
        layout.marginHeight = 5;
        layout.marginWidth = 0;
        layout.horizontalSpacing = ExpressionsAdapterFactory.HORIZONTAL_SPACING;
        setLayout(layout);
        
        AppFormToolkit.getInstance().adapt(this);
        
        GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
        setLayoutData(gd);
        
        Label l = AppFormToolkit.getInstance().createLabel(this, Messages.ChangePropertyValueComposite_0);
        gd = new GridData(GridData.FILL, GridData.CENTER, false, true);
        gd.widthHint = 50;
        l.setLayoutData(gd);
        
        Composite comp = new Composite(this, SWT.BORDER);
        GridLayout layout2 = new GridLayout(3, false);
        layout2.marginHeight = 2;
        layout2.marginWidth = 2;
        layout2.horizontalSpacing = ExpressionsAdapterFactory.HORIZONTAL_SPACING;
        comp.setLayout(layout2);
        comp.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        Label label = new Label(comp, SWT.NULL);
        label.setText(Messages.ChangePropertyValueComposite_1);
        fpropertyRefControl = new ExpressionValueControl(comp);
        ((GridData)fpropertyRefControl.getLayoutData()).horizontalSpan = 2;
        
        label = new Label(comp, SWT.NULL);
        label.setText(Messages.ChangePropertyValueComposite_2);
        fPropertyValueText = AppFormToolkit.getInstance().createText(comp, "", SWT.BORDER); //$NON-NLS-1$
        gd = new GridData(GridData.FILL, GridData.CENTER, true, true);
        fPropertyValueText.setLayoutData(gd);
        
        fAdvancedButton = new Button(comp, SWT.PUSH);
        fAdvancedButton.setText(Messages.ChangePropertyValueComposite_3);
        
        fAdvancedButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                AdvancedPropertyValueEditorDialog dialog = new AdvancedPropertyValueEditorDialog(getShell(), fMemberType);
                dialog.open();
                updateAdvancedButton();
                fAdvancedButton.getParent().layout();
            }
        });
        
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
                fThenType.removeMember(fMemberType);
                fMemberType.getLDModel().setDirty();
            }
        });
    }

    public void setChangePropertyValueType(IThenType thenType, IChangePropertyValueType memberType) {
        fThenType = thenType;
        fMemberType = memberType;
        
        final ILDModel ldModel = thenType.getLDModel();
        
        ILDModelObject selected = fMemberType.getPropertyRefValuePair().getPropertyRef().getLDModelObject();
        fpropertyRefControl.setItems(ldModel.getPropertiesModel().getPropertyTypes().toArray(), selected);
        
        fpropertyRefControl.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                Object selected = ((StructuredSelection)event.getSelection()).getFirstElement();
                if(selected instanceof IPropertyTypeModel) {
                    fMemberType.getPropertyRefValuePair().setPropertyRef((IPropertyTypeModel)selected);
                    ldModel.setDirty();
                }
            }
        });
        
        String value = StringUtils.safeString(fMemberType.getPropertyRefValuePair().getPropertyValue().getValue());
        fPropertyValueText.setText(value);
        
        fPropertyValueText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                fMemberType.getPropertyRefValuePair().getPropertyValue().setValue(fPropertyValueText.getText());
                ldModel.setDirty();
            }
        });
        
        updateAdvancedButton();
    }

    private void updateAdvancedButton() {
        boolean isSimple = fMemberType.getPropertyRefValuePair().getPropertyValue().getChoice() == IPropertyValueType.CHOICE_NONE;
        if(!isSimple) {
            fAdvancedButton.setImage(ImageFactory.getImage(ImageFactory.ICON_TICK));
            fPropertyValueText.setEnabled(false);
        }
        else {
            fAdvancedButton.setImage(null);
            fPropertyValueText.setEnabled(true);
        }
    }
}