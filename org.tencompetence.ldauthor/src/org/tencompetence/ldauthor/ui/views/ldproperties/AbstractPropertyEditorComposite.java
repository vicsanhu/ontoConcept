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
package org.tencompetence.ldauthor.ui.views.ldproperties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;
import org.tencompetence.imsldmodel.properties.IDataType;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.StackComposite;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Composite for editing Level B Properties
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractPropertyEditorComposite.java,v 1.6 2009/06/29 10:50:41 phillipus Exp $
 */
public abstract class AbstractPropertyEditorComposite extends Composite {
    
    protected Section fSection;
    protected Composite fFieldsComposite;
    
    protected Text fTextTitle;
    protected Text fTextIdentifier;
    protected Combo fComboType;
    
    
    private StackComposite fStackComposite;
    protected Text fTextInitialValue;
    protected Combo fComboBoolean;
    
    protected RestrictionTypeEditorPanel fRestrictionTypeEditorPanel;
    
    protected boolean fIsUpdating;
    
    protected IPropertyTypeModel fLDProperty;
    
    /**
     * Text changes
     */
    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating || fLDProperty == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fTextTitle) {
                String s = fTextTitle.getText();
                fLDProperty.setTitle(s);
            }
            
            else if(control == fTextInitialValue) {
                String s = fTextInitialValue.getText();
                fLDProperty.setInitialValue(s);
            }

            fLDProperty.getLDModel().setDirty();
        }
    };
    
    private VerifyListener fVerifyListener = new VerifyListener() {
        public void verifyText(VerifyEvent e) {
            if(fIsUpdating || fLDProperty == null) {
                return;
            }
            
            String type = fComboType.getText();
            
            // If type is integer or real
            if("integer".equals(type) || "real".equals(type)) { //$NON-NLS-1$ //$NON-NLS-2$
                String oldText = fTextInitialValue.getText();
                String insertedText = e.text;
                String leftText = oldText.substring(0, e.start);
                String rightText = oldText.substring(e.end, oldText.length());
                String newText = leftText + insertedText + rightText;
                e.doit = checkString(newText, type);
            }
        }
        
        boolean checkString(String text, String type) {
            if("".equals(text)) { //$NON-NLS-1$
                return true;
            }
            
            // Allow '-' if first character
            if("-".equals(text)) { //$NON-NLS-1$
                return true;
            }
            
            // Integer
            if("integer".equals(type)) { //$NON-NLS-1$
                try {
                    new Integer(text);
                }
                catch(NumberFormatException ex) {
                    return false;
                }
                return true;
            }
            
            // Real
            else {
                try {
                    new Float(text);
                }
                catch(NumberFormatException ex) {
                    return false;
                }
                return true;
            }
        }
    };
    
    /**
     * Combo changes
     */
    private SelectionAdapter fSelectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(fIsUpdating || fLDProperty == null) {
                return;
            }
            
            Object control = e.getSource();
            
            // Type Combo
            if(control == fComboType) {
                String type = fComboType.getText();
                fLDProperty.getDataType().setType(type);
                
                // If we change to boolean type have a default value
                if("boolean".equals(type)) { //$NON-NLS-1$
                    fLDProperty.setInitialValue("true"); //$NON-NLS-1$
                    fComboBoolean.setText("true"); //$NON-NLS-1$
                }
                // If we change to real or integer types clear text
                else if("real".equals(type) || "integer".equals(type)) { //$NON-NLS-1$ //$NON-NLS-2$
                    fIsUpdating = true;
                    fTextInitialValue.setText(""); //$NON-NLS-1$
                    fIsUpdating = false;
                }
                // Else restore
                else {
                    fLDProperty.setInitialValue(fTextInitialValue.getText());
                }
                
                // Set panels
                setPanels();
            }
            
            // Boolean Combo
            if(control == fComboBoolean) {
                String value = fComboBoolean.getText();
                fLDProperty.setInitialValue(value);
            }
            
            fLDProperty.getLDModel().setDirty();
        }
    };


    public AbstractPropertyEditorComposite(Composite parent, int style) {
        super(parent, style);
        
        setLayout(new GridLayout());
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        fSection = AppFormToolkit.getInstance().createSection(this, Section.TITLE_BAR);
        fSection.setText(getTitle());
        fSection.setLayout(new GridLayout());
        fSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        ScrolledComposite scrolledClient = new ScrolledComposite(fSection, SWT.V_SCROLL);
        scrolledClient.setExpandHorizontal(true);
        scrolledClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        AppFormToolkit.getInstance().adapt(scrolledClient);
               
        fSection.setClient(scrolledClient);
       
        fFieldsComposite = AppFormToolkit.getInstance().createComposite(scrolledClient, SWT.NULL);
        fFieldsComposite.setLayout(new GridLayout(2, false));
        fFieldsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        scrolledClient.setContent(fFieldsComposite);
        
        // Other components
        insertUIComponents(fFieldsComposite);
        
        // Title
        AppFormToolkit.getInstance().createLabel(fFieldsComposite, Messages.AbstractPropertyEditorComposite_0);
        fTextTitle = new Text(fFieldsComposite, SWT.BORDER);
        fTextTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fTextTitle.addModifyListener(fModifyListener);
        
        // Identifier
        AppFormToolkit.getInstance().createLabel(fFieldsComposite, Messages.AbstractPropertyEditorComposite_1);
        fTextIdentifier = new Text(fFieldsComposite, SWT.BORDER | SWT.READ_ONLY);
        fTextIdentifier.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Other components
        insertUIComponents(fTextIdentifier);
        
        // Type
        AppFormToolkit.getInstance().createLabel(fFieldsComposite, Messages.AbstractPropertyEditorComposite_2);
        fComboType = new Combo(fFieldsComposite, SWT.READ_ONLY);
        fComboType.setItems(IDataType.DATA_TYPES);
        fComboType.addSelectionListener(fSelectionListener);
        fComboType.setVisibleItemCount(20);
        
        // Initial Value
        AppFormToolkit.getInstance().createLabel(fFieldsComposite, Messages.AbstractPropertyEditorComposite_3);

        // StackComposite
        fStackComposite = new StackComposite(fFieldsComposite, SWT.NONE);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        //gd.heightHint = 80;
        fStackComposite.setLayoutData(gd);
        
        // Text Field
        //fTextInitialValue = new Text(fStackComposite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        fTextInitialValue = new Text(fStackComposite, SWT.BORDER);
        fTextInitialValue.addModifyListener(fModifyListener);
        fTextInitialValue.addVerifyListener(fVerifyListener);
        
        // Boolean Combo
        fComboBoolean = new Combo(fStackComposite, SWT.READ_ONLY);
        fComboBoolean.setItems(new String[] { "true", "false" });  //$NON-NLS-1$//$NON-NLS-2$
        fComboBoolean.addSelectionListener(fSelectionListener);
        
        // Restriction Type Editor
        AppFormToolkit.getInstance().createLabel(fFieldsComposite, Messages.AbstractPropertyEditorComposite_4);
        fRestrictionTypeEditorPanel = new RestrictionTypeEditorPanel(fFieldsComposite, SWT.NULL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        gd.heightHint = 200;  // Needs to be this tall on Mac
        fRestrictionTypeEditorPanel.setLayoutData(gd);
        AppFormToolkit.getInstance().adapt(fRestrictionTypeEditorPanel);
        
        // Other components
        insertUIComponents(fRestrictionTypeEditorPanel);
        
        fFieldsComposite.layout();
        fFieldsComposite.pack();
    }
    
    public void setProperty(IPropertyTypeModel property) {
        fLDProperty = property;
        
        fIsUpdating = true;
        
        fTextTitle.setText(StringUtils.safeString(property.getTitle()));
        fTextIdentifier.setText(StringUtils.safeString(property.getIdentifier()));
        fComboType.setText(property.getDataType().getType());
        
        // Boolean or otherwise
        String type = fLDProperty.getDataType().getType();
        if("boolean".equals(type)) { //$NON-NLS-1$
            fComboBoolean.setText("true".equals(fLDProperty.getInitialValue()) ? "true" : "false");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
        }
        else {
            fTextInitialValue.setText(StringUtils.safeString(property.getInitialValue()));
        }
        
        fRestrictionTypeEditorPanel.setProperty(property);
        
        setPanels();
        
        fIsUpdating = false;
    }
    
    private void setPanels() {
        // If type is boolean, no enumerations are allowed and the combo box is shown
        String type = fLDProperty.getDataType().getType();
        if("boolean".equals(type)) { //$NON-NLS-1$
            fRestrictionTypeEditorPanel.setPanelEnabled(false);
            fStackComposite.setControl(fComboBoolean);
        }
        else {
            fRestrictionTypeEditorPanel.setPanelEnabled(true);
            fStackComposite.setControl(fTextInitialValue);
        }
    }
    
    protected abstract String getTitle();

    /**
     * Insert any additional UI components.
     */
    protected void insertUIComponents(Control previousControl) {
    }

}
