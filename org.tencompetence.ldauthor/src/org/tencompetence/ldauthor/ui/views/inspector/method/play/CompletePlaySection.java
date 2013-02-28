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
package org.tencompetence.ldauthor.ui.views.inspector.method.play;

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
import org.tencompetence.imsldmodel.method.ICompletePlayType;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.common.StackComposite;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.ui.views.inspector.ldproperties.PropertyValueEditorComposite;
import org.tencompetence.ldauthor.ui.views.inspector.ldproperties.TimeLimitComposite;
import org.tencompetence.ldauthor.ui.views.inspector.notifications.OnCompletionNotificationsComposite;


/**
 * Inspector Section for Play Rule Completion
 * 
 * @author Phillip Beauvoir
 * @version $Id: CompletePlaySection.java,v 1.12 2009/06/17 11:58:59 phillipus Exp $
 */
public class CompletePlaySection extends AbstractScrolledInspectorSection {
    
    private IPlayModel fPlay;
    
    /*
     * Choice buttons
     */ 
    private Button fButtonNone, fButtonWhenLastActCompleted, fButtonTimeLimit, fButtonWhenPropertySet;

    private TimeLimitComposite fTimeLimitComposite;
    
    private PropertyValueEditorComposite fCompletePlayPropertyEditor;
    private PropertyValueEditorComposite fOnCompletePlayPropertyEditor;
    
    private OnCompletionNotificationsComposite fOnCompletionNotificationsComposite;
    
    private StackComposite fStackComposite;
    
    private Section fAdvancedSection;
    
    private static boolean EXPANDED_STATE = false;
    
    private boolean fIsUpdating;

    private SelectionAdapter fSelectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fPlay == null) {
                return;
            }
            
            if(fButtonNone.getSelection()) {
                fStackComposite.setControl(null);
                fPlay.getCompletePlayType().setChoice(ICompletePlayType.COMPLETE_NONE);
            }
            else if(fButtonWhenLastActCompleted.getSelection()) {
                fStackComposite.setControl(null);
                fPlay.getCompletePlayType().setChoice(ICompletePlayType.COMPLETE_WHEN_LAST_ACT_COMPLETED);
            }
            else if(fButtonTimeLimit.getSelection()) {
                fStackComposite.setControl(fTimeLimitComposite);
                fPlay.getCompletePlayType().setChoice(ICompletePlayType.COMPLETE_TIME_LIMIT);
            }
            else if(fButtonWhenPropertySet.getSelection()) {
                fStackComposite.setControl(fCompletePlayPropertyEditor);
                fPlay.getCompletePlayType().setChoice(ICompletePlayType.COMPLETE_WHEN_PROPERTY_SET);
            }
            
            fPlay.getLDModel().setDirty();
        }
    };

    public CompletePlaySection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        composite.setLayout(new GridLayout(2, false));
        setContent(composite);
        
        Group group = factory.createGroup(composite, Messages.CompletePlaySection_0 + LDModelUtils.getUserObjectName(LDModelFactory.PLAY));
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        fButtonNone = factory.createButton(group, Messages.CompletePlaySection_1, SWT.RADIO);
        fButtonNone.addSelectionListener(fSelectionListener);
        
        fButtonWhenLastActCompleted = factory.createButton(group,
                Messages.CompletePlaySection_2
                + " " //$NON-NLS-1$
                + LDModelUtils.getUserObjectName(LDModelFactory.ACT)
                + " " //$NON-NLS-1$
                + Messages.CompletePlaySection_3,
                SWT.RADIO);
        fButtonWhenLastActCompleted.addSelectionListener(fSelectionListener);
        
        fButtonTimeLimit = factory.createButton(group, Messages.CompletePlaySection_4, SWT.RADIO);
        fButtonTimeLimit.addSelectionListener(fSelectionListener);
        
        fButtonWhenPropertySet = factory.createButton(group, Messages.CompletePlaySection_5, SWT.RADIO);
        fButtonWhenPropertySet.addSelectionListener(fSelectionListener);
        
        fStackComposite = new StackComposite(composite, SWT.NULL);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 150;
        fStackComposite.setLayoutData(gd);
        factory.adapt(fStackComposite);
        
        fTimeLimitComposite = new TimeLimitComposite(fStackComposite, SWT.NULL);
        fCompletePlayPropertyEditor = new PropertyValueEditorComposite(fStackComposite, SWT.NULL);
        
        // Advanced
        fAdvancedSection = factory.createSection(composite, Section.TITLE_BAR | Section.TWISTIE);
        fAdvancedSection.setText(Messages.CompletePlaySection_6);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 2;
        fAdvancedSection.setLayoutData(gd);
        fAdvancedSection.setExpanded(EXPANDED_STATE);
        
        Composite compositeAdvanced = factory.createComposite(fAdvancedSection);
        compositeAdvanced.setLayout(new GridLayout());
        fAdvancedSection.setClient(compositeAdvanced);
        
        factory.createLabel(compositeAdvanced,
                Messages.CompletePlaySection_7 +
                " " + //$NON-NLS-1$
                LDModelUtils.getUserObjectName(LDModelFactory.PLAY) +
                " " + //$NON-NLS-1$
                Messages.CompletePlaySection_8);
        
        fOnCompletePlayPropertyEditor = new PropertyValueEditorComposite(compositeAdvanced, SWT.NULL);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 150;
        fOnCompletePlayPropertyEditor.setLayoutData(gd);
        
        // On Completion Notifications
        factory.createLabel(compositeAdvanced, Messages.CompletePlaySection_9);
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
        
        if(element instanceof IPlayModel) {
            fPlay = (IPlayModel)element;
        }
        else {
            throw new RuntimeException("Should have been a Play Model Object"); //$NON-NLS-1$
        }
        
        refresh();
    }
    
    public void refresh() {
        if(fPlay == null) {
            return;
        }
        
        fIsUpdating = true;
        
        fButtonNone.setSelection(false);
        fButtonWhenLastActCompleted.setSelection(false);
        fButtonTimeLimit.setSelection(false);
        fButtonWhenPropertySet.setSelection(false);
        
        // Choice
        int choice = fPlay.getCompletePlayType().getChoice();
        switch(choice) {
            case ICompletePlayType.COMPLETE_NONE:
                fButtonNone.setSelection(true);
                fStackComposite.setControl(null);
                break;

            case ICompletePlayType.COMPLETE_WHEN_LAST_ACT_COMPLETED:
                fButtonWhenLastActCompleted.setSelection(true);
                fStackComposite.setControl(null);
                break;
            
            case ICompletePlayType.COMPLETE_TIME_LIMIT:
                fButtonTimeLimit.setSelection(true);
                fStackComposite.setControl(fTimeLimitComposite);
                break;

            case ICompletePlayType.COMPLETE_WHEN_PROPERTY_SET:
                fButtonWhenPropertySet.setSelection(true);
                fStackComposite.setControl(fCompletePlayPropertyEditor);
                break;

            default:
                break;
        }
        
        // Time Limit
        fTimeLimitComposite.setTimeLimitType(fPlay.getCompletePlayType().getTimeLimitType());
        
        // Properties 1
        fCompletePlayPropertyEditor.setOwner(fPlay.getCompletePlayType());

        // Properties 2
        fOnCompletePlayPropertyEditor.setOwner(fPlay.getOnCompletionType());
        
        // Notifications
        fOnCompletionNotificationsComposite.setOnCompletionType(fPlay.getOnCompletionType());
        
        fIsUpdating = false;
    }
}
