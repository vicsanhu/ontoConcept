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
package org.tencompetence.ldauthor.ui.expressions;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IPropertyRefValuePair;
import org.tencompetence.imsldmodel.expressions.IPropertyRefValuePairOwner;
import org.tencompetence.imsldmodel.expressions.IPropertyValueType;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.ldauthor.ui.common.StackComposite;


/**
 * Composite to choose between "None", "Langstring", "Property Ref" and "Calculate"
 * 
 * @author Phillip Beauvoir
 * @version $Id: PropertyValueChoiceComposite.java,v 1.5 2009/06/08 17:28:36 phillipus Exp $
 */
public class PropertyValueChoiceComposite extends Composite {

    private IPropertyRefValuePairOwner fPropertyRefValuePairOwner;
    private IPropertyRefValuePair fValuePair;
    private IPropertyValueType fPropertyValue;
    
    private ComboViewer fChoiceCombo;
    
    private StackComposite fStackComposite;
    private Text fText;
    
    private ExpressionValueControl fExpressionValueControl;
    
    private boolean fIsUpdating;

    public PropertyValueChoiceComposite(Composite parent, IPropertyRefValuePairOwner propertyRefValuePairOwner) {
        super(parent, SWT.NULL);
    
        fPropertyRefValuePairOwner = propertyRefValuePairOwner;
        fValuePair = fPropertyRefValuePairOwner.getPropertyRefValuePair();
        fPropertyValue = fValuePair.getPropertyValue();
        
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.marginRight = 5;
        setLayout(layout);
        
        //setBackground(new Color(null, 255, 120, 255));
        
        setLayoutData(new GridData(GridData.FILL, SWT.NULL, true, false));
        
        // Choice Combo
        fChoiceCombo = new ComboViewer(this, SWT.READ_ONLY);
        GridData gd = new GridData(GridData.FILL, GridData.CENTER, false, false);
        //gd.widthHint = 200;
        fChoiceCombo.getCombo().setLayoutData(gd);
        
        fChoiceCombo.setContentProvider(new IStructuredContentProvider() {
            public Object[] getElements(Object inputElement) {
                return new String[] {
                        "none", //$NON-NLS-1$
                        LDModelFactory.LANGSTRING,
                        LDModelFactory.CALCULATE,
                        LDModelFactory.PROPERTY_REF
                };
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        });
        
        fChoiceCombo.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                String choice = (String)element;
                if("none".equals(choice)) { //$NON-NLS-1$
                    return Messages.PropertyValueChoiceComposite_0;
                }
                if(LDModelFactory.LANGSTRING.equals(choice)) {
                    return Messages.PropertyValueChoiceComposite_1;
                }
                if(LDModelFactory.PROPERTY_REF.equals(choice)) {
                    return Messages.PropertyValueChoiceComposite_2;
                }
                if(LDModelFactory.CALCULATE.equals(choice)) {
                    return Messages.PropertyValueChoiceComposite_3;
                }
                
                return ""; //$NON-NLS-1$
            }
        });
        
        fChoiceCombo.setInput(""); //$NON-NLS-1$
        
        // Type of Choice 
        int choice = fPropertyValue.getChoice();
        switch(choice) {
            case IPropertyValueType.CHOICE_NONE:
                fChoiceCombo.setSelection(new StructuredSelection(Messages.PropertyValueChoiceComposite_4));
                break;

            case IPropertyValueType.CHOICE_LANGSTRING:
                fChoiceCombo.setSelection(new StructuredSelection(LDModelFactory.LANGSTRING));
                break;
            
            case IPropertyValueType.CHOICE_PROPERTY_REF:
                fChoiceCombo.setSelection(new StructuredSelection(LDModelFactory.PROPERTY_REF));
                break;

            case IPropertyValueType.CHOICE_CALCULATE:
                fChoiceCombo.setSelection(new StructuredSelection(LDModelFactory.CALCULATE));
                break;
            
            default:
                break;
        }

        // Selection Listener
        fChoiceCombo.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                String choice = (String)((StructuredSelection)event.getSelection()).getFirstElement();
                if("none".equals(choice)) { //$NON-NLS-1$
                    fPropertyValue.setChoice(IPropertyValueType.CHOICE_NONE);
                }
                else if(LDModelFactory.LANGSTRING.equals(choice)) {
                    fPropertyValue.setChoice(IPropertyValueType.CHOICE_LANGSTRING);
                }
                else if(LDModelFactory.PROPERTY_REF.equals(choice)) {
                    fPropertyValue.setChoice(IPropertyValueType.CHOICE_PROPERTY_REF);
                }
                else if(LDModelFactory.CALCULATE.equals(choice)) {
                    fPropertyValue.setChoice(IPropertyValueType.CHOICE_CALCULATE);
                }
                
                updateValueControl();
                fPropertyValue.getLDModel().setDirty();
            }
        });
        
        // Stack Composite
        fStackComposite = new StackComposite(this, SWT.NULL);
        gd = new GridData(SWT.FILL, SWT.NULL, true, false);
        fStackComposite.setLayoutData(gd);
        
        
        // We would like to lazily create these as needed, but the StackComposite
        // Needs to know the minumum sizes now
        fText = createTextControl();
        fExpressionValueControl = createExpressionValueControl();
        
        fIsUpdating = true;
        updateValueControl();
        fIsUpdating = false;
    }
    
    private void updateValueControl() {
        int choice = fPropertyValue.getChoice();
        switch(choice) {
            case IPropertyValueType.CHOICE_NONE:
                fStackComposite.setControl(null);
                break;

            case IPropertyValueType.CHOICE_LANGSTRING:
                String text = fPropertyValue.getLangStringType().getValue();
                fText.setText(text);
                fStackComposite.setControl(fText);
                break;
            
            case IPropertyValueType.CHOICE_PROPERTY_REF:
                // Show Properties
                Object[] items = fPropertyValue.getLDModel().getPropertiesModel().getPropertyTypes().toArray();
                fExpressionValueControl.setItems(items, fPropertyValue.getPropertyRef().getLDModelObject());
                fStackComposite.setControl(fExpressionValueControl);
                break;

            case IPropertyValueType.CHOICE_CALCULATE:
                fStackComposite.setControl(null);
                break;
            
            default:
                break;
        }
    }
    
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        fChoiceCombo.addSelectionChangedListener(listener);
    }
    
    /**
     * Create Text Control
     */
    private Text createTextControl() {
        Text text = new Text(fStackComposite, SWT.BORDER);
        text.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if(!fIsUpdating) {
                    String text = fText.getText();
                    fPropertyValue.getLangStringType().setValue(text);
                    fPropertyValue.getLDModel().setDirty();
                }
            }
        });
        
        return text;
    }
    
    /**
     * Create Control
     */
    private ExpressionValueControl createExpressionValueControl() {
        ExpressionValueControl expressionValueControl = new ExpressionValueControl(fStackComposite);
        expressionValueControl.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if(!fIsUpdating) {
                    Object value = ((StructuredSelection)event.getSelection()).getFirstElement();
                    fPropertyValue.setPropertyRef((IPropertyTypeModel)value);
                }
            }
        });
        
        return expressionValueControl;
    }
}
