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
package org.tencompetence.ldauthor.ui.views.inspector.method.act;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.Section;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.ICompleteActType;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.common.StackComposite;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.ui.views.inspector.ldproperties.PropertyValueEditorComposite;
import org.tencompetence.ldauthor.ui.views.inspector.ldproperties.TimeLimitComposite;
import org.tencompetence.ldauthor.ui.views.inspector.notifications.OnCompletionNotificationsComposite;


/**
 * Inspector Section for Act Rule Completion
 * 
 * @author Phillip Beauvoir
 * @version $Id: CompleteActSection.java,v 1.12 2009/06/17 11:58:59 phillipus Exp $
 */
public class CompleteActSection extends AbstractScrolledInspectorSection {
    
    private IActModel fActModel;
    
    /*
     * Choice buttons
     */ 
    private Button fButtonNone, fButtonWhenRolePartsCompleted, fButtonTimeLimit, fButtonWhenPropertySet, fButtonWhenConditionTrue;

    private TimeLimitComposite fTimeLimitComposite;
    
    private PropertyValueEditorComposite fCompleteActPropertyEditor;
    private PropertyValueEditorComposite fOnCompleteActPropertyEditor;
    
    private OnCompletionNotificationsComposite fOnCompletionNotificationsComposite;
    
    private WhenConditionTrueComposite fWhenConditionTrueEditor;
    
    private StackComposite fStackComposite;
    
    private Section fAdvancedSection;
    
    private static boolean EXPANDED_STATE = false;
    
    private RolePartListViewer fRolePartListViewer;

    private boolean fIsUpdating;

    private SelectionAdapter fSelectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(fIsUpdating) {
                return;
            }
            if(fActModel == null) {
                return;
            }
            
            if(fButtonNone.getSelection()) {
                fStackComposite.setControl(null);
                fActModel.getCompleteActType().setChoice(ICompleteActType.COMPLETE_NONE);
            }
            else if(fButtonWhenRolePartsCompleted.getSelection()) {
                fStackComposite.setControl(fRolePartListViewer.getControl());
                fActModel.getCompleteActType().setChoice(ICompleteActType.COMPLETE_WHEN_ROLE_PART_COMPLETED);
            }
            else if(fButtonTimeLimit.getSelection()) {
                fStackComposite.setControl(fTimeLimitComposite);
                fActModel.getCompleteActType().setChoice(ICompleteActType.COMPLETE_TIME_LIMIT);
            }
            else if(fButtonWhenPropertySet.getSelection()) {
                fStackComposite.setControl(fCompleteActPropertyEditor);
                fActModel.getCompleteActType().setChoice(ICompleteActType.COMPLETE_WHEN_PROPERTY_SET);
            }
            else if(fButtonWhenConditionTrue.getSelection()) {
                fStackComposite.setControl(fWhenConditionTrueEditor);
                fActModel.getCompleteActType().setChoice(ICompleteActType.COMPLETE_WHEN_CONDITION_TRUE);
            }
            
            fActModel.getLDModel().setDirty();
        }
    };

    public CompleteActSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        composite.setLayout(new GridLayout(2, false));
        setContent(composite);
        
        Group group = factory.createGroup(composite, Messages.CompleteActSection_0 + LDModelUtils.getUserObjectName(LDModelFactory.ACT));
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        fButtonNone = factory.createButton(group, Messages.CompleteActSection_1, SWT.RADIO);
        fButtonNone.addSelectionListener(fSelectionListener);
        
        fButtonWhenRolePartsCompleted = factory.createButton(group, Messages.CompleteActSection_2, SWT.RADIO);
        fButtonWhenRolePartsCompleted.addSelectionListener(fSelectionListener);
        
        fButtonTimeLimit = factory.createButton(group, Messages.CompleteActSection_3, SWT.RADIO);
        fButtonTimeLimit.addSelectionListener(fSelectionListener);
        
        fButtonWhenPropertySet = factory.createButton(group, Messages.CompleteActSection_4, SWT.RADIO);
        fButtonWhenPropertySet.addSelectionListener(fSelectionListener);
        
        fButtonWhenConditionTrue = factory.createButton(group, Messages.CompleteActSection_5, SWT.RADIO);
        fButtonWhenConditionTrue.addSelectionListener(fSelectionListener);
        
        fStackComposite = new StackComposite(composite, SWT.NULL);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 150;
        fStackComposite.setLayoutData(gd);
        factory.adapt(fStackComposite);
        
        fTimeLimitComposite = new TimeLimitComposite(fStackComposite, SWT.NULL);
        fRolePartListViewer = new RolePartListViewer(fStackComposite, SWT.BORDER);
        fCompleteActPropertyEditor = new PropertyValueEditorComposite(fStackComposite, SWT.NULL);
        fWhenConditionTrueEditor = new WhenConditionTrueComposite(fStackComposite, SWT.NULL);
        
        // Advanced
        fAdvancedSection = factory.createSection(composite, Section.TITLE_BAR | Section.TWISTIE);
        fAdvancedSection.setText(Messages.CompleteActSection_6);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 2;
        fAdvancedSection.setLayoutData(gd);
        fAdvancedSection.setExpanded(EXPANDED_STATE);
        
        Composite compositeAdvanced = factory.createComposite(fAdvancedSection);
        compositeAdvanced.setLayout(new GridLayout());
        fAdvancedSection.setClient(compositeAdvanced);
        
        factory.createLabel(compositeAdvanced,
                Messages.CompleteActSection_7 +
                " " + //$NON-NLS-1$
                LDModelUtils.getUserObjectName(LDModelFactory.ACT) +
                " " + //$NON-NLS-1$
                Messages.CompleteActSection_8);
        
        fOnCompleteActPropertyEditor = new PropertyValueEditorComposite(compositeAdvanced, SWT.NULL);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 150;
        fOnCompleteActPropertyEditor.setLayoutData(gd);
        
        // On Completion Notifications
        factory.createLabel(compositeAdvanced, Messages.CompleteActSection_9);
        fOnCompletionNotificationsComposite = new OnCompletionNotificationsComposite(compositeAdvanced, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 200;
        fOnCompletionNotificationsComposite.setLayoutData(gd);
        
        fAdvancedSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(ExpansionEvent e) {
                doLayout();
                EXPANDED_STATE = e.getState();
            }
        });
        
        doLayout();
    }
    
    /**
     * Layout the components (we are using a scrolled Composite)
     */
    private void doLayout() {
        Composite composite = (Composite)getContent();
        composite.layout();
        composite.pack();
    }

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IActModel) {
            fActModel = (IActModel)element;
        }
        else {
            throw new RuntimeException("Should have been an Act Model"); //$NON-NLS-1$
        }

        refresh();
    }
    
    public void refresh() {
        if(fActModel == null) {
            return;
        }
        
        fIsUpdating = true;
        
        fButtonNone.setSelection(false);
        fButtonWhenRolePartsCompleted.setSelection(false);
        fButtonTimeLimit.setSelection(false);
        fButtonWhenPropertySet.setSelection(false);
        fButtonWhenConditionTrue.setSelection(false);

        // Choice
        int choice = fActModel.getCompleteActType().getChoice();
        switch(choice) {
            case ICompleteActType.COMPLETE_NONE:
                fButtonNone.setSelection(true);
                fStackComposite.setControl(null);
                break;

            case ICompleteActType.COMPLETE_WHEN_ROLE_PART_COMPLETED:
                fButtonWhenRolePartsCompleted.setSelection(true);
                fStackComposite.setControl(fRolePartListViewer.getControl());
                break;
            
            case ICompleteActType.COMPLETE_TIME_LIMIT:
                fButtonTimeLimit.setSelection(true);
                fStackComposite.setControl(fTimeLimitComposite);
                break;

            case ICompleteActType.COMPLETE_WHEN_PROPERTY_SET:
                fButtonWhenPropertySet.setSelection(true);
                fStackComposite.setControl(fCompleteActPropertyEditor);
                break;

            case ICompleteActType.COMPLETE_WHEN_CONDITION_TRUE:
                fButtonWhenConditionTrue.setSelection(true);
                fStackComposite.setControl(fWhenConditionTrueEditor);
                break;

            default:
                break;
        }
        
        // Time Limit
        fTimeLimitComposite.setTimeLimitType(fActModel.getCompleteActType().getTimeLimitType());
        
        // Properties 1
        fCompleteActPropertyEditor.setOwner(fActModel.getCompleteActType());

        // Properties 2
        fOnCompleteActPropertyEditor.setOwner(fActModel.getOnCompletionType());
       
        // Role Parts
        fRolePartListViewer.setActModel(fActModel);
        
        // Conditions Editor
        fWhenConditionTrueEditor.setActModel(fActModel);
        
        // Notifications
        fOnCompletionNotificationsComposite.setOnCompletionType(fActModel.getOnCompletionType());

        fIsUpdating = false;
    }
}
