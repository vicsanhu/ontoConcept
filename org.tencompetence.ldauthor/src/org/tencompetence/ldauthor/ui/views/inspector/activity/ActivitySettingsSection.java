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
package org.tencompetence.ldauthor.ui.views.inspector.activity;

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
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Inspector Section for Activity Settings
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActivitySettingsSection.java,v 1.6 2009/06/10 10:16:49 phillipus Exp $
 */
public class ActivitySettingsSection extends AbstractScrolledInspectorSection {
    
    private Text fTextTitle, fTextParameters;
    
    private Text fTextID;
    
    private Button fButtonVisible;
    
    private boolean fIsUpdating;
    
    private IActivityModel fActivityModel;
    
    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fActivityModel == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fTextTitle) {
                String s = fTextTitle.getText();
                fActivityModel.setTitle(s);
            }
            else if(control == fTextParameters) {
                fActivityModel.setParameters(fTextParameters.getText());
            }
            
            fActivityModel.getLDModel().setDirty();
        }
    };
    
    private SelectionAdapter fSelectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fActivityModel == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fButtonVisible) {
                fActivityModel.setIsVisible(fButtonVisible.getSelection());
            }
            
            fActivityModel.getLDModel().setDirty();
        }
    };


    public ActivitySettingsSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        setContent(composite);
        
        Label label = factory.createLabel(composite, Messages.ActivitySettingsSection_0);
        label.setToolTipText(Messages.ActivitySettingsSection_1);
        fTextTitle = factory.createText(composite, ""); //$NON-NLS-1$
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        fTextTitle.setLayoutData(gd);
        fTextTitle.addModifyListener(fModifyListener);

        fButtonVisible = factory.createButton(composite, Messages.ActivitySettingsSection_2, SWT.CHECK);
        fButtonVisible.setToolTipText(Messages.ActivitySettingsSection_3);
        fButtonVisible.addSelectionListener(fSelectionListener);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 2;
        fButtonVisible.setLayoutData(gd);

        label = factory.createLabel(composite, Messages.ActivitySettingsSection_4);
        label.setToolTipText(Messages.ActivitySettingsSection_5);
        fTextParameters = factory.createText(composite, ""); //$NON-NLS-1$
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        fTextParameters.setLayoutData(gd);
        fTextParameters.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.ActivitySettingsSection_6);
        label.setToolTipText(Messages.ActivitySettingsSection_7);
        fTextID = factory.createText(composite, "", SWT.READ_ONLY); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextID.setLayoutData(gd);
        
        composite.layout();
        composite.pack();
    }
    

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IActivityModel) {
            fActivityModel = (IActivityModel)element;
        }
        else {
            throw new RuntimeException("Should have been an Activity"); //$NON-NLS-1$
        }
        
        refresh();
    }
    
    public void refresh() {
        if(fActivityModel == null) {
            return;
        }
        
        // Populate fields...
        fIsUpdating = true;
        
        String s = StringUtils.safeString(fActivityModel.getTitle());
        fTextTitle.setText(s);

        s = StringUtils.safeString(fActivityModel.getParameters());
        fTextParameters.setText(s);

        fButtonVisible.setSelection(fActivityModel.isVisible());
        
        fTextID.setText(StringUtils.safeString(fActivityModel.getIdentifier()));

        fIsUpdating = false;
    }
}
