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
package org.tencompetence.ldauthor.ui.views.inspector.environment.learningobject;

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
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Inspector Section for Learning Object Settings
 * 
 * @author Phillip Beauvoir
 * @version $Id: LearningObjectSettingsSection.java,v 1.8 2009/06/10 10:16:49 phillipus Exp $
 */
public class LearningObjectSettingsSection extends AbstractScrolledInspectorSection {
    
    private Text fTextTitle, fTextClass, fTextParameters;
    
    private Text fTextID;
    
    private Button fButtonVisible;
    
    private Combo fComboType;
    
    private boolean fIsUpdating;
    
    private ILearningObjectModel fLearningObjectModel;
    
    private static String[] TYPE_STRINGS_HUMAN = {
        Messages.LearningObjectSettingsSection_12, 
        Messages.LearningObjectSettingsSection_13,
        Messages.LearningObjectSettingsSection_14,
        Messages.LearningObjectSettingsSection_15
    };

    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fLearningObjectModel == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fTextTitle) {
                String s = fTextTitle.getText();
                fLearningObjectModel.setTitle(s);
            }
            else if(control == fTextClass) {
                fLearningObjectModel.setClassType(fTextClass.getText());
            }
            else if(control == fTextParameters) {
                fLearningObjectModel.setParameters(fTextParameters.getText());
            }
            
            fLearningObjectModel.getLDModel().setDirty();
        }
    };
    
    private SelectionAdapter fSelectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fLearningObjectModel == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fButtonVisible) {
                fLearningObjectModel.setIsVisible(fButtonVisible.getSelection());
            }
            
            if(control == fComboType) {
                fLearningObjectModel.setType(fComboType.getSelectionIndex());
            }
            
            fLearningObjectModel.getLDModel().setDirty();
        }
    };

    
    public LearningObjectSettingsSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        setContent(composite);
        
        Label label = factory.createLabel(composite, Messages.LearningObjectSettingsSection_0);
        label.setToolTipText(Messages.LearningObjectSettingsSection_1);
        fTextTitle = factory.createText(composite, ""); //$NON-NLS-1$
        GridData gd = new GridData(GridData.FILL_BOTH);
        fTextTitle.setLayoutData(gd);
        fTextTitle.addModifyListener(fModifyListener);

        label = factory.createLabel(composite, Messages.LearningObjectSettingsSection_2);
        label.setToolTipText(Messages.LearningObjectSettingsSection_3);
        fComboType = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        fComboType.setItems(TYPE_STRINGS_HUMAN);
        fComboType.addSelectionListener(fSelectionListener);
        
        fButtonVisible = factory.createButton(composite, Messages.LearningObjectSettingsSection_4, SWT.CHECK);
        fButtonVisible.setToolTipText(Messages.LearningObjectSettingsSection_5);
        fButtonVisible.addSelectionListener(fSelectionListener);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        fButtonVisible.setLayoutData(gd);

        label = factory.createLabel(composite, Messages.LearningObjectSettingsSection_6);
        label.setToolTipText(Messages.LearningObjectSettingsSection_7);
        fTextClass = factory.createText(composite, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextClass.setLayoutData(gd);
        fTextClass.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.LearningObjectSettingsSection_8);
        label.setToolTipText(Messages.LearningObjectSettingsSection_9);
        fTextParameters = factory.createText(composite, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextParameters.setLayoutData(gd);
        fTextParameters.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.LearningObjectSettingsSection_10);
        label.setToolTipText(Messages.LearningObjectSettingsSection_11);
        fTextID = factory.createText(composite, "", SWT.READ_ONLY); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextID.setLayoutData(gd);
        
        composite.layout();
        composite.pack();
    }
    

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof ILearningObjectModel) {
            fLearningObjectModel = (ILearningObjectModel)element;
        }
        else {
            throw new RuntimeException("Should have been a Learning Object Part"); //$NON-NLS-1$
        }

        refresh();
    }
    
    public void refresh() {
        if(fLearningObjectModel == null) {
            return;
        }
        
        // Populate fields...
        fIsUpdating = true;
        
        String s = StringUtils.safeString(fLearningObjectModel.getTitle());
        fTextTitle.setText(s);

        s = StringUtils.safeString(fLearningObjectModel.getClassType());
        fTextClass.setText(s);

        s = StringUtils.safeString(fLearningObjectModel.getParameters());
        fTextParameters.setText(s);

        int index = fLearningObjectModel.getType();
        fComboType.setText(TYPE_STRINGS_HUMAN[index]);
        
        fButtonVisible.setSelection(fLearningObjectModel.isVisible());
        
        fTextID.setText(StringUtils.safeString(fLearningObjectModel.getIdentifier()));

        fIsUpdating = false;
    }
}
