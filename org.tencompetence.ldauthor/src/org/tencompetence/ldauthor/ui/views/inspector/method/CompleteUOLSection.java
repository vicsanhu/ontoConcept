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
package org.tencompetence.ldauthor.ui.views.inspector.method;

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
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.method.ICompleteUOLType;
import org.tencompetence.imsldmodel.method.IMethodModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.common.StackComposite;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.ui.views.inspector.ldproperties.PropertyValueEditorComposite;
import org.tencompetence.ldauthor.ui.views.inspector.ldproperties.TimeLimitComposite;
import org.tencompetence.ldauthor.ui.views.inspector.notifications.OnCompletionNotificationsComposite;


/**
 * Inspector Section for UOL Completion
 * 
 * @author Phillip Beauvoir
 * @version $Id: CompleteUOLSection.java,v 1.21 2009/06/17 11:58:59 phillipus Exp $
 */
public class CompleteUOLSection extends AbstractScrolledInspectorSection {
    
    private ILDModel fLDModel;
    private IMethodModel fMethodModel;
    
    /*
     * Choice buttons
     */ 
    private Button fButtonNone, fButtonWhenPlaysCompleted, fButtonTimeLimit, fButtonWhenPropertySet;

    private TimeLimitComposite fTimeLimitComposite;
    
    private PlayListViewer fPlayListViewer;
    
    private PropertyValueEditorComposite fCompleteUOLPropertyComposite;
    private PropertyValueEditorComposite fOnCompletionUOLPropertyComposite;

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
            
            if(fButtonNone.getSelection()) {
                fStackComposite.setControl(null);
                fMethodModel.getCompleteUOLType().setChoice(ICompleteUOLType.COMPLETE_NONE);
            }
            else if(fButtonWhenPlaysCompleted.getSelection()) {
                fStackComposite.setControl(fPlayListViewer.getControl());
                fMethodModel.getCompleteUOLType().setChoice(ICompleteUOLType.COMPLETE_WHEN_PLAY_COMPLETED);
            }
            else if(fButtonTimeLimit.getSelection()) {
                fStackComposite.setControl(fTimeLimitComposite);
                fMethodModel.getCompleteUOLType().setChoice(ICompleteUOLType.COMPLETE_TIME_LIMIT);
            }
            else if(fButtonWhenPropertySet.getSelection()) {
                fStackComposite.setControl(fCompleteUOLPropertyComposite);
                fMethodModel.getCompleteUOLType().setChoice(ICompleteUOLType.COMPLETE_WHEN_PROPERTY_SET);
            }
            
            fLDModel.setDirty();
        }
    };

    public CompleteUOLSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        composite.setLayout(new GridLayout(2, false));
        setContent(composite);
        
        Group group = factory.createGroup(composite, Messages.CompleteUOLPropertySection_0);
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        fButtonNone = factory.createButton(group, Messages.CompleteUOLPropertySection_1, SWT.RADIO);
        fButtonNone.addSelectionListener(fSelectionListener);
        
        fButtonWhenPlaysCompleted = factory.createButton(group,
                Messages.CompleteUOLPropertySection_2 
                + " "  //$NON-NLS-1$
                + LDModelUtils.getUserObjectName(LDModelFactory.PLAYS)
                + " "  //$NON-NLS-1$
                + Messages.CompleteUOLPropertySection_3, SWT.RADIO);
        fButtonWhenPlaysCompleted.addSelectionListener(fSelectionListener);
        
        fButtonTimeLimit = factory.createButton(group, Messages.CompleteUOLPropertySection_4, SWT.RADIO);
        fButtonTimeLimit.addSelectionListener(fSelectionListener);
        
        fButtonWhenPropertySet = factory.createButton(group, Messages.CompleteUOLSection_0, SWT.RADIO);
        fButtonWhenPropertySet.addSelectionListener(fSelectionListener);
        
        fStackComposite = new StackComposite(composite, SWT.NULL);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 150;
        fStackComposite.setLayoutData(gd);
        factory.adapt(fStackComposite);
        
        fTimeLimitComposite = new TimeLimitComposite(fStackComposite, SWT.NULL);
        fPlayListViewer = new PlayListViewer(fStackComposite, SWT.BORDER);
        fCompleteUOLPropertyComposite = new PropertyValueEditorComposite(fStackComposite, SWT.NULL);
        
        // Advanced
        fAdvancedSection = factory.createSection(composite, Section.TITLE_BAR | Section.TWISTIE);
        fAdvancedSection.setText(Messages.CompleteUOLSection_1);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 2;
        fAdvancedSection.setLayoutData(gd);
        fAdvancedSection.setExpanded(EXPANDED_STATE);
        
        Composite compositeAdvanced = factory.createComposite(fAdvancedSection);
        compositeAdvanced.setLayout(new GridLayout());
        fAdvancedSection.setClient(compositeAdvanced);
        
        // On Completion Change Property Values
        factory.createLabel(compositeAdvanced, Messages.CompleteUOLSection_2);
        fOnCompletionUOLPropertyComposite = new PropertyValueEditorComposite(compositeAdvanced, SWT.NULL);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 150;
        fOnCompletionUOLPropertyComposite.setLayoutData(gd);
        
        // On Completion Notifications
        factory.createLabel(compositeAdvanced, Messages.CompleteUOLSection_3);
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
        
        fMethodModel = (IMethodModel)((ICompleteUOLType)element).getOwner();
        fLDModel = fMethodModel.getLDModel();
        
        refresh();
    }
    
    public void refresh() {
        fIsUpdating = true;
        
        fButtonNone.setSelection(false);
        fButtonWhenPlaysCompleted.setSelection(false);
        fButtonTimeLimit.setSelection(false);
        fButtonWhenPropertySet.setSelection(false);
        
        // Choice
        int choice = fMethodModel.getCompleteUOLType().getChoice();
        switch(choice) {
            case ICompleteUOLType.COMPLETE_NONE:
                fButtonNone.setSelection(true);
                fStackComposite.setControl(null);
                break;

            case ICompleteUOLType.COMPLETE_WHEN_PLAY_COMPLETED:
                fButtonWhenPlaysCompleted.setSelection(true);
                fStackComposite.setControl(fPlayListViewer.getControl());
                break;
            
            case ICompleteUOLType.COMPLETE_TIME_LIMIT:
                fButtonTimeLimit.setSelection(true);
                fStackComposite.setControl(fTimeLimitComposite);
                break;

            case ICompleteUOLType.COMPLETE_WHEN_PROPERTY_SET:
                fButtonWhenPropertySet.setSelection(true);
                fStackComposite.setControl(fCompleteUOLPropertyComposite);
                break;

            default:
                break;
        }
        
        // Time Limit
        fTimeLimitComposite.setTimeLimitType(fMethodModel.getCompleteUOLType().getTimeLimitType());
        
        // Plays
        fPlayListViewer.setLDModel(fLDModel);
        
        // Properties 1
        fCompleteUOLPropertyComposite.setOwner(fMethodModel.getCompleteUOLType());

        // Properties 2
        fOnCompletionUOLPropertyComposite.setOwner(fMethodModel.getOnCompletionType());
        
        // Notifications
        fOnCompletionNotificationsComposite.setOnCompletionType(fMethodModel.getOnCompletionType());

        fIsUpdating = false;
    }
}
