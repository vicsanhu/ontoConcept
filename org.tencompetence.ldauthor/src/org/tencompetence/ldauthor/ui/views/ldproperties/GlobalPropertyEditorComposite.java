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
package org.tencompetence.ldauthor.ui.views.ldproperties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.properties.IGlobalPropertyTypeModel;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Composite for editing Level B Properties
 * 
 * @author Phillip Beauvoir
 * @version $Id: GlobalPropertyEditorComposite.java,v 1.6 2009/06/29 08:30:34 phillipus Exp $
 */
public class GlobalPropertyEditorComposite extends AbstractPropertyEditorComposite {
    
    /*
     * Choice buttons
     */
    private Button fButtonExisting, fButtonGlobalDefinition;

    /*
     * Text fields
     */
    private Text fTextHref, fTextURI;

    
    public GlobalPropertyEditorComposite(Composite parent, int style) {
        super(parent, style);
    }
    
    @Override
    protected void insertUIComponents(Control previousControl) {
        if(previousControl == fFieldsComposite) {
            fButtonGlobalDefinition = AppFormToolkit.getInstance().createButton(fFieldsComposite, Messages.GlobalPropertyEditorComposite_0, SWT.RADIO);
            GridData gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.horizontalSpan = 2;
            fButtonGlobalDefinition.setLayoutData(gd);
            
            fButtonGlobalDefinition.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    boolean isGlobalDefinition = fButtonGlobalDefinition.getSelection();
                    setPanelEnabled(isGlobalDefinition);
                    ((IGlobalPropertyTypeModel)fLDProperty).setIsExistingChoice(!isGlobalDefinition);
                    fLDProperty.getLDModel().setDirty();
                }
            });
        }
        
        if(previousControl == fRestrictionTypeEditorPanel) {
            fButtonExisting = AppFormToolkit.getInstance().createButton(fFieldsComposite, Messages.GlobalPropertyEditorComposite_1, SWT.RADIO);
            GridData gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.horizontalSpan = 2;
            fButtonExisting.setLayoutData(gd);
            
            AppFormToolkit.getInstance().createLabel(fFieldsComposite, Messages.GlobalPropertyEditorComposite_2);
            fTextHref = new Text(fFieldsComposite, SWT.BORDER);
            fTextHref.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            fTextHref.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    if(fIsUpdating) {
                        return;
                    }
                    
                    if(fLDProperty == null) {
                        return;
                    }
                    
                    String s = fTextHref.getText();
                    ((IGlobalPropertyTypeModel)fLDProperty).setExistingHREF(s);
                    
                    fLDProperty.getLDModel().setDirty();
                }
            });
        }
        
        if(previousControl == fTextIdentifier) {
            AppFormToolkit.getInstance().createLabel(fFieldsComposite, Messages.GlobalPropertyEditorComposite_3);
            fTextURI = new Text(fFieldsComposite, SWT.BORDER);
            fTextURI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            fTextURI.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    if(fIsUpdating) {
                        return;
                    }
                    
                    if(fLDProperty == null) {
                        return;
                    }
                    
                    String s = fTextURI.getText();
                    ((IGlobalPropertyTypeModel)fLDProperty).setGlobalDefinitionURI(s);
                    
                    fLDProperty.getLDModel().setDirty();
                }
            });
        }
        
    }
    
    @Override
    public void setProperty(IPropertyTypeModel property) {
        super.setProperty(property);
        
        fIsUpdating = true;
        
        fTextURI.setText(StringUtils.safeString(((IGlobalPropertyTypeModel)property).getGlobalDefinitionURI()));
        fTextHref.setText(StringUtils.safeString(((IGlobalPropertyTypeModel)property).getExistingHREF()));
        
        fButtonExisting.setSelection(((IGlobalPropertyTypeModel)property).isExistingChoice());
        fButtonGlobalDefinition.setSelection(!((IGlobalPropertyTypeModel)property).isExistingChoice());
        
        // Enable fields
        setPanelEnabled(!((IGlobalPropertyTypeModel)property).isExistingChoice());
        
        fIsUpdating = false;
    }

    /**
     * Set the main panel enabled
     * @param enabled
     */
    protected void setPanelEnabled(boolean enabled) {
        fTextHref.setEnabled(!enabled);
        
        fTextTitle.setEnabled(enabled);
        fTextURI.setEnabled(enabled);
        fComboType.setEnabled(enabled);
        fTextInitialValue.setEnabled(enabled);
        fComboBoolean.setEnabled(enabled);
        fRestrictionTypeEditorPanel.setPanelEnabled(enabled);
    }


    @Override
    protected String getTitle() {
        return Messages.GlobalPropertyEditorComposite_4;
    }
}
