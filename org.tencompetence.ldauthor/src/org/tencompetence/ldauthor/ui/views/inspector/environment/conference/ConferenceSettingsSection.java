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
package org.tencompetence.ldauthor.ui.views.inspector.environment.conference;

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
import org.tencompetence.imsldmodel.environments.IConferenceModel;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Inspector Section for Conference Settings
 * 
 * @author Phillip Beauvoir
 * @version $Id: ConferenceSettingsSection.java,v 1.8 2009/06/10 10:16:49 phillipus Exp $
 */
public class ConferenceSettingsSection extends AbstractScrolledInspectorSection {
    
    private Text fTextTitle, fTextClass, fTextParameters;
    private Text fTextID;
    
    private Button fButtonVisible;
    
    private Combo fComboType;
    
    private boolean fIsUpdating;
    
    private IGraphicalModelObject fGraphicalModelObject;
    
    private IConferenceModel fConference;
    
    private static String[] TYPE_STRINGS_HUMAN = {
            Messages.ConferenceSettingsSection_12,
            Messages.ConferenceSettingsSection_13,
            Messages.ConferenceSettingsSection_14
    };

    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fConference == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fTextTitle) {
                String s = fTextTitle.getText();
                fConference.setTitle(s);
                if(fGraphicalModelObject != null) {
                    fGraphicalModelObject.setName(s);
                }
            }
            else if(control == fTextClass) {
                fConference.setClassString(fTextClass.getText());
            }
            else if(control == fTextParameters) {
                fConference.setParameters(fTextParameters.getText());
            }
            
            fConference.getLDModel().setDirty();
        }
    };
    
    private SelectionAdapter fSelectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fConference == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fButtonVisible) {
                fConference.setIsVisible(fButtonVisible.getSelection());
            }
            
            if(control == fComboType) {
                fConference.setType(fComboType.getSelectionIndex());
            }
            
            fConference.getLDModel().setDirty();
        }
    };

    
    public ConferenceSettingsSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        setContent(composite);
        
        Label label = factory.createLabel(composite, Messages.ConferenceSettingsSection_0);
        label.setToolTipText(Messages.ConferenceSettingsSection_1);
        fTextTitle = factory.createText(composite, ""); //$NON-NLS-1$
        GridData gd = new GridData(GridData.FILL_BOTH);
        fTextTitle.setLayoutData(gd);
        fTextTitle.addModifyListener(fModifyListener);

        label = factory.createLabel(composite, Messages.ConferenceSettingsSection_2);
        label.setToolTipText(Messages.ConferenceSettingsSection_3);
        fComboType = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        fComboType.setItems(TYPE_STRINGS_HUMAN);
        fComboType.addSelectionListener(fSelectionListener);
        
        fButtonVisible = factory.createButton(composite, Messages.ConferenceSettingsSection_4, SWT.CHECK);
        fButtonVisible.setToolTipText(Messages.ConferenceSettingsSection_5);
        fButtonVisible.addSelectionListener(fSelectionListener);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        fButtonVisible.setLayoutData(gd);

        label = factory.createLabel(composite, Messages.ConferenceSettingsSection_6);
        label.setToolTipText(Messages.ConferenceSettingsSection_7);
        fTextClass = factory.createText(composite, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextClass.setLayoutData(gd);
        fTextClass.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.ConferenceSettingsSection_8);
        label.setToolTipText(Messages.ConferenceSettingsSection_9);
        fTextParameters = factory.createText(composite, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextParameters.setLayoutData(gd);
        fTextParameters.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.ConferenceSettingsSection_10);
        label.setToolTipText(Messages.ConferenceSettingsSection_11);
        fTextID = factory.createText(composite, "", SWT.READ_ONLY); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextID.setLayoutData(gd);
        
        composite.layout();
        composite.pack();
    }
    

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IConferenceModel) {
            fConference = (IConferenceModel)element;
        }
        else {
            throw new RuntimeException("Should have been a Conference Part"); //$NON-NLS-1$
        }

        refresh();
    }
    
    public void refresh() {
        if(fConference == null) {
            return;
        }
        
        // Populate fields...
        fIsUpdating = true;
        
        String s = StringUtils.safeString(fConference.getTitle());
        fTextTitle.setText(s);

        s = StringUtils.safeString(fConference.getClassString());
        fTextClass.setText(s);

        s = StringUtils.safeString(fConference.getParameters());
        fTextParameters.setText(s);

        int index = fConference.getType();
        fComboType.setText(TYPE_STRINGS_HUMAN[index]);
        
        fButtonVisible.setSelection(fConference.isVisible());
        
        fTextID.setText(StringUtils.safeString(fConference.getIdentifier()));

        fIsUpdating = false;
    }
}
