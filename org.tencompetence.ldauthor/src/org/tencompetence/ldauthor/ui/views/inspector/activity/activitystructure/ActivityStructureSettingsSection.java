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
package org.tencompetence.ldauthor.ui.views.inspector.activity.activitystructure;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Inspector Section for Activity Structure Settings
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActivityStructureSettingsSection.java,v 1.8 2009/06/10 10:16:49 phillipus Exp $
 */
public class ActivityStructureSettingsSection extends AbstractScrolledInspectorSection {
    
    private Text fTextTitle;
    
    private Text fTextID;
    
    private Spinner fSpinnerNumberToSelect;
    
    private Combo fComboType, fComboSort;
    
    private boolean fIsUpdating;
    
    private IActivityStructureModel fActivityStructureModel;
    
    String[] TYPE_STRINGS_HUMAN = {
            Messages.ActivityStructureSettingsSection_10, 
            Messages.ActivityStructureSettingsSection_11 
    };
    
    String[] SORT_STRINGS_HUMAN = {
            Messages.ActivityStructureSettingsSection_12, 
            Messages.ActivityStructureSettingsSection_13, 
            Messages.ActivityStructureSettingsSection_14 
    };


    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fActivityStructureModel == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fTextTitle) {
                String s = fTextTitle.getText();
                fActivityStructureModel.setTitle(s);
            }
            
            else if(control == fSpinnerNumberToSelect) {
                fActivityStructureModel.setNumberToSelect(fSpinnerNumberToSelect.getSelection());
            }
            
            fActivityStructureModel.getLDModel().setDirty();
        }
    };
    
    private SelectionAdapter fSelectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fActivityStructureModel == null) {
                return;
            }
            
            Object control = e.getSource();
            if(control == fComboType) {
                int index = fComboType.getSelectionIndex();
                fActivityStructureModel.setStructureType(index);
                fSpinnerNumberToSelect.setEnabled(index != IActivityStructureModel.TYPE_SEQUENCE);
            }
            else if(control == fComboSort) {
                fActivityStructureModel.setSort(fComboSort.getSelectionIndex());
            }
            
            fActivityStructureModel.getLDModel().setDirty();
        }
    };

    public ActivityStructureSettingsSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        setContent(composite);
        
        Label label = factory.createLabel(composite, Messages.ActivityStructureSettingsSection_0);
        label.setToolTipText(Messages.ActivityStructureSettingsSection_1);
        fTextTitle = factory.createText(composite, ""); //$NON-NLS-1$
        GridData gd = new GridData(GridData.FILL_BOTH);
        fTextTitle.setLayoutData(gd);
        fTextTitle.addModifyListener(fModifyListener);

        label = factory.createLabel(composite, Messages.ActivityStructureSettingsSection_2);
        label.setToolTipText(Messages.ActivityStructureSettingsSection_3);
        fComboType = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        fComboType.setItems(TYPE_STRINGS_HUMAN);
        fComboType.addSelectionListener(fSelectionListener);
        
        label = factory.createLabel(composite, Messages.ActivityStructureSettingsSection_4);
        label.setToolTipText(Messages.ActivityStructureSettingsSection_5);
        fSpinnerNumberToSelect = new Spinner(composite, SWT.BORDER);
        fSpinnerNumberToSelect.addModifyListener(fModifyListener);

        label = factory.createLabel(composite, Messages.ActivityStructureSettingsSection_6);
        label.setToolTipText(Messages.ActivityStructureSettingsSection_7);
        fComboSort = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        fComboSort.setItems(SORT_STRINGS_HUMAN);
        fComboSort.addSelectionListener(fSelectionListener);
        
        label = factory.createLabel(composite, Messages.ActivityStructureSettingsSection_8);
        label.setToolTipText(Messages.ActivityStructureSettingsSection_9);
        fTextID = factory.createText(composite, "", SWT.READ_ONLY); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextID.setLayoutData(gd);
        
        composite.layout();
        composite.pack();
    }
    

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IActivityStructureModel) {
            fActivityStructureModel = (IActivityStructureModel)element;
        }
        else {
            throw new RuntimeException("Should have been an Activity Structure"); //$NON-NLS-1$
        }
        
        refresh();
    }
    
    public void refresh() {
        if(fActivityStructureModel == null) {
            return;
        }
        
        // Populate fields...
        fIsUpdating = true;
        
        fTextTitle.setText(StringUtils.safeString(fActivityStructureModel.getTitle()));
        
        int st = fActivityStructureModel.getStructureType();
        fComboType.setText(TYPE_STRINGS_HUMAN[st]);
        
        fSpinnerNumberToSelect.setEnabled(st != IActivityStructureModel.TYPE_SEQUENCE);
        
        int sort = fActivityStructureModel.getSort();
        fComboSort.setText(SORT_STRINGS_HUMAN[sort]);
        
        int max = fActivityStructureModel.getActivityRefs().size();
        fSpinnerNumberToSelect.setMaximum(max);
        fSpinnerNumberToSelect.setSelection(fActivityStructureModel.getNumberToSelect());
        
        fTextID.setText(StringUtils.safeString(fActivityStructureModel.getIdentifier()));
        
        fIsUpdating = false;
    }
}
