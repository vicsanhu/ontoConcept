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
package org.tencompetence.ldauthor.ui.wizards.ld;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.ldauthor.templates.ILDTemplate;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


public class NewLDWizardPageOverview
extends WizardPage {
    
    public static final String PAGE_NAME = "NewLDWizardPageOverview"; //$NON-NLS-1$
    
    private Text fTextTitle;
    private Text fTextLearningObjectives;
    private Text fTextPrerequisites;
    
    private ILDTemplate fTemplate;

    public NewLDWizardPageOverview() {
        super(PAGE_NAME);
        
        setTitle(Messages.NewLDWizardPageOverview_0);
        setDescription(Messages.NewLDWizardPageOverview_1);
        setImageDescriptor(ImageFactory.getEclipseImageDescriptor(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
    }

    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);
        
        GridData gdFillHorizontal = new GridData(GridData.FILL_HORIZONTAL);
        
        GridData gdFillHorizontal2 = new GridData(GridData.FILL_HORIZONTAL);
        gdFillHorizontal2.horizontalSpan = 2;
        
        GridData gdFillBoth2 = new GridData(GridData.FILL_BOTH);
        gdFillBoth2.horizontalSpan = 2;
        
        // Title
        Label label = new Label(container, SWT.NULL);
        label.setText(Messages.NewLDWizardPageOverview_2);
        fTextTitle = new Text(container, SWT.BORDER);
        fTextTitle.setLayoutData(gdFillHorizontal);
        fTextTitle.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                fTemplate.setTitle(fTextTitle.getText());
            }
        });
        
        label = new Label(container, SWT.NULL);
        label.setText(Messages.NewLDWizardPageOverview_3);
        label.setLayoutData(gdFillHorizontal2);
        fTextLearningObjectives = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        fTextLearningObjectives.setLayoutData(gdFillBoth2);
        fTextLearningObjectives.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                fTemplate.setLearningObjectives(fTextLearningObjectives.getText());
            }
        });
        
        label = new Label(container, SWT.NULL);
        label.setText(Messages.NewLDWizardPageOverview_4);
        label.setLayoutData(gdFillHorizontal2);
        fTextPrerequisites = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        fTextPrerequisites.setLayoutData(gdFillBoth2);
        fTextPrerequisites.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                fTemplate.setPrerequisites(fTextPrerequisites.getText());
            }
        });
        
        setPageComplete(true); // This page is optional
        setControl(container);
    }
    
    public String getUOLTitle() {
        return fTextTitle.getText();
    }

    public String getLearningObjectives() {
        return fTextLearningObjectives.getText();
    }

    public String getPrerequisites() {
        return fTextPrerequisites.getText();
    }

    public void setTemplate(ILDTemplate template) {
        // Do this anyways!
        fTemplate = template;

        if(fTextTitle == null) {
            return; // Page not created yet
        }
        
        fTextTitle.setText(StringUtils.safeString(template.getTitle()));
        fTextLearningObjectives.setText(StringUtils.safeString(template.getLearningObjectives()));
        fTextPrerequisites.setText(StringUtils.safeString(template.getPrerequisites()));
    }
}
