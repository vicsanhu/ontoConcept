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
package org.tencompetence.ldauthor.ui.views.inspector.environment.sendmail;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.environments.ISendMailModel;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.ui.views.inspector.common.EmailDataTypeTableViewer;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Inspector Section for SendMail Settings
 * 
 * @author Phillip Beauvoir
 * @version $Id: SendMailSettingsSection.java,v 1.10 2009/06/17 11:58:59 phillipus Exp $
 */
public class SendMailSettingsSection extends AbstractScrolledInspectorSection {
    
    private Text fTextTitle, fTextClass, fTextParameters;
    
    private Text fTextID;
    
    private Button fButtonVisible;
    
    private Combo fComboSelect;
    
    private EmailDataTypeTableViewer fRolesTable;
    
    private boolean fIsUpdating;
    
    private ISendMailModel fSendMailModel;
    
    private static String[] SELECT_STRINGS_HUMAN = {
            Messages.SendMailSettingsSection_12, 
            Messages.SendMailSettingsSection_13 
    };
    
    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fSendMailModel == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fTextTitle) {
                String s = fTextTitle.getText();
                fSendMailModel.setTitle(s);
            }
            else if(control == fTextClass) {
                fSendMailModel.setClassString(fTextClass.getText());
            }
            else if(control == fTextParameters) {
                fSendMailModel.setParameters(fTextParameters.getText());
            }
            
            fSendMailModel.getLDModel().setDirty();
        }
    };
    
    private SelectionAdapter fSelectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fSendMailModel == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fButtonVisible) {
                fSendMailModel.setIsVisible(fButtonVisible.getSelection());
            }
            
            if(control == fComboSelect) {
                fSendMailModel.setSelect(fComboSelect.getSelectionIndex());
            }
            
            fSendMailModel.getLDModel().setDirty();
        }
    };
    
    public SendMailSettingsSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        setContent(composite);
        
        Label label = factory.createLabel(composite, Messages.SendMailSettingsSection_0);
        label.setToolTipText(Messages.SendMailSettingsSection_1);
        fTextTitle = factory.createText(composite, ""); //$NON-NLS-1$
        GridData gd = new GridData(GridData.FILL_BOTH);
        fTextTitle.setLayoutData(gd);
        fTextTitle.addModifyListener(fModifyListener);

        label = factory.createLabel(composite, Messages.SendMailSettingsSection_2);
        label.setToolTipText(Messages.SendMailSettingsSection_3);
        fComboSelect = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        fComboSelect.setItems(SELECT_STRINGS_HUMAN);
        fComboSelect.addSelectionListener(fSelectionListener);
        
        Composite c = factory.createComposite(composite);
        c.setLayout(new TableColumnLayout());
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        gd.heightHint = 150;
        c.setLayoutData(gd);
        fRolesTable = new EmailDataTypeTableViewer(c);
        
        fButtonVisible = factory.createButton(composite, Messages.SendMailSettingsSection_4, SWT.CHECK);
        fButtonVisible.setToolTipText(Messages.SendMailSettingsSection_5);
        fButtonVisible.addSelectionListener(fSelectionListener);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        fButtonVisible.setLayoutData(gd);

        label = factory.createLabel(composite, Messages.SendMailSettingsSection_6);
        label.setToolTipText(Messages.SendMailSettingsSection_7);
        fTextClass = factory.createText(composite, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextClass.setLayoutData(gd);
        fTextClass.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.SendMailSettingsSection_8);
        label.setToolTipText(Messages.SendMailSettingsSection_9);
        fTextParameters = factory.createText(composite, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextParameters.setLayoutData(gd);
        fTextParameters.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.SendMailSettingsSection_10);
        label.setToolTipText(Messages.SendMailSettingsSection_11);
        fTextID = factory.createText(composite, "", SWT.READ_ONLY); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextID.setLayoutData(gd);
        
        composite.layout();
        composite.pack();
    }
    
    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof ISendMailModel) {
            fSendMailModel = (ISendMailModel)element;
        }
        else {
            throw new RuntimeException("Should have been a Send Mail Part"); //$NON-NLS-1$
        }

        refresh();
    }
    
    public void refresh() {
        if(fSendMailModel == null) {
            return;
        }
        
        // Populate fields...
        fIsUpdating = true;
        
        fRolesTable.setEmailDataTypeOwner(fSendMailModel);
        
        String s = StringUtils.safeString(fSendMailModel.getTitle());
        fTextTitle.setText(s);

        s = StringUtils.safeString(fSendMailModel.getClassString());
        fTextClass.setText(s);

        s = StringUtils.safeString(fSendMailModel.getParameters());
        fTextParameters.setText(s);

        int index = fSendMailModel.getSelect();
        fComboSelect.setText(SELECT_STRINGS_HUMAN[index]);
        
        fButtonVisible.setSelection(fSendMailModel.isVisible());
        
        fTextID.setText(StringUtils.safeString(fSendMailModel.getIdentifier()));

        fIsUpdating = false;
    }
}
