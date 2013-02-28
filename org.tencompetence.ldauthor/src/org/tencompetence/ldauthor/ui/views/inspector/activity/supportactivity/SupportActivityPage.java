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
package org.tencompetence.ldauthor.ui.views.inspector.activity.supportactivity;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractTabbedInspectorPage;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.ui.views.inspector.activity.ActivityDescriptionSection;
import org.tencompetence.ldauthor.ui.views.inspector.activity.ActivityEnvironmentsSection;
import org.tencompetence.ldauthor.ui.views.inspector.activity.ActivityFeedbackSection;
import org.tencompetence.ldauthor.ui.views.inspector.activity.ActivitySettingsSection;
import org.tencompetence.ldauthor.ui.views.inspector.activity.CompleteActivitySection;


/**
 * Inspector Support Activity Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: SupportActivityPage.java,v 1.4 2009/06/10 10:16:49 phillipus Exp $
 */
public class SupportActivityPage extends AbstractTabbedInspectorPage {
    
    private ActivityDescriptionSection fDescriptionSection;
    private ActivityEnvironmentsSection fEnvironmentsSection;
    private SupportActivityRolesSection fRolesSection;
    private CompleteActivitySection fCompletionSection;
    private ActivityFeedbackSection fFeedbackSection;
    private ActivitySettingsSection fSettingsSection;
    
    private static final int TAB_DESCRIPTION = 0;
    private static final int TAB_ENVIRONMENTS = 1;
    private static final int TAB_ROLES = 2;
    private static final int TAB_COMPLETION_RULE = 3;
    private static final int TAB_COMPLETION_FEEDBACK = 4;
    private static final int TAB_SETTINGS = 5;

    public SupportActivityPage(Composite parent) {
        super(parent);
        
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        CTabItem item = factory.createTabItem(getFolder(), SWT.NULL);
        item.setText(Messages.SupportActivityPage_0);
        
        fDescriptionSection = new ActivityDescriptionSection(getFolder());
        fDescriptionSection.createControls();
        item.setControl(fDescriptionSection);
        
        item = factory.createTabItem(getFolder(), SWT.NULL);
        item.setText(Messages.SupportActivityPage_1);
        
        item = factory.createTabItem(getFolder(), SWT.NULL);
        item.setText(Messages.SupportActivityPage_2);
        
        item = factory.createTabItem(getFolder(), SWT.NULL);
        item.setText(Messages.SupportActivityPage_3);
        
        item = factory.createTabItem(getFolder(), SWT.NULL);
        item.setText(Messages.SupportActivityPage_4);
        
        item = factory.createTabItem(getFolder(), SWT.NULL);
        item.setText(Messages.SupportActivityPage_5);
        
        getFolder().setSelection(TAB_DESCRIPTION);
    }

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(fDescriptionSection != null) {
            fDescriptionSection.setInput(provider, element);
        }
        
        if(fEnvironmentsSection != null) {
            fEnvironmentsSection.setInput(provider, element);
        }
        
        if(fRolesSection != null) {
            fRolesSection.setInput(provider, element);
        }
        
        if(fCompletionSection != null) {
            fCompletionSection.setInput(provider, element);
        }
        
        if(fFeedbackSection != null) {
            fFeedbackSection.setInput(provider, element);
        }
        
        if(fSettingsSection != null) {
            fSettingsSection.setInput(provider, element);
        }
    }

    @Override
    protected void createTabSection(CTabItem tabItem) {
        // Lazily create the other tabs
        switch(getFolder().indexOf(tabItem)) {
            case TAB_ENVIRONMENTS:
                if(fEnvironmentsSection == null) {
                    fEnvironmentsSection = new ActivityEnvironmentsSection(getFolder());
                    fEnvironmentsSection.createControls();
                    tabItem.setControl(fEnvironmentsSection);
                    fEnvironmentsSection.setInput(getInspectorProvider(), getElement());
                }
                break;

            case TAB_ROLES:
                if(fRolesSection == null) {
                    fRolesSection = new SupportActivityRolesSection(getFolder());
                    fRolesSection.createControls();
                    tabItem.setControl(fRolesSection);
                    fRolesSection.setInput(getInspectorProvider(), getElement());
                }
                break;

            case TAB_COMPLETION_RULE:
                if(fCompletionSection == null) {
                    fCompletionSection = new CompleteActivitySection(getFolder());
                    fCompletionSection.createControls();
                    tabItem.setControl(fCompletionSection);
                    fCompletionSection.setInput(getInspectorProvider(), getElement());
                }
                break;

            case TAB_COMPLETION_FEEDBACK:
                if(fFeedbackSection == null) {
                    fFeedbackSection = new ActivityFeedbackSection(getFolder());
                    fFeedbackSection.createControls();
                    tabItem.setControl(fFeedbackSection);
                    fFeedbackSection.setInput(getInspectorProvider(), getElement());
                }
                break;

            case TAB_SETTINGS:
                if(fSettingsSection == null) {
                    fSettingsSection = new ActivitySettingsSection(getFolder());
                    fSettingsSection.createControls();
                    tabItem.setControl(fSettingsSection);
                    fSettingsSection.setInput(getInspectorProvider(), getElement());
                }
                break;

            default:
                break;
        }
        
    }
}
