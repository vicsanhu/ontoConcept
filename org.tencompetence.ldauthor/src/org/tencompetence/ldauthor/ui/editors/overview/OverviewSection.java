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
package org.tencompetence.ldauthor.ui.editors.overview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.ldmodel.IOverviewModel;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorViewModelAdapter;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Overview Section
 * 
 * @author Phillip Beauvoir
 * @version $Id: OverviewSection.java,v 1.18 2009/06/18 18:13:36 phillipus Exp $
 */
public class OverviewSection extends Composite implements PropertyChangeListener {

    private ILDEditorPart fEditor;
    private IReCourseLDModel fLDModel;
    private IOverviewModel fOverviewModel;
    
    private Text fTitleText, fURIText, fVersionText, fDescriptionText, fTagsText;
    private Text fAuthorText, fSubjectText;
    private Combo fComboLevel;
    
    private boolean fIsUpdating;
    
    /**
     * Text Field modify listener
     */
    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fTitleText) {
                fLDModel.setTitle(fTitleText.getText());
            }
            else if(control == fURIText) {
                fLDModel.setURI(fURIText.getText());
            }
            else if(control == fVersionText) {
                fLDModel.setVersion(fVersionText.getText());
            }
            else if(control == fDescriptionText) {
                fOverviewModel.setDescription(fDescriptionText.getText());
            }
            else if(control == fTagsText) {
                fOverviewModel.setTags(fTagsText.getText());
            }
            else if(control == fAuthorText) {
                fOverviewModel.setAuthor(fAuthorText.getText());
            }
            else if(control == fSubjectText) {
                fOverviewModel.setSubject(fSubjectText.getText());
            }
            
            fLDModel.setDirty();
        }
    };
    
    private SelectionAdapter fSelectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            Object control = e.getSource();
            if(control == fComboLevel) {
                fLDModel.setLevel(fComboLevel.getText());
                fLDModel.setDirty();
            }
        }
    };

    public OverviewSection(ILDEditorPart editor, Composite parent) {
        super(parent, SWT.NULL);
        
        fEditor = editor;
        
        fLDModel = (IReCourseLDModel)editor.getAdapter(ILDModel.class);
        fOverviewModel = fLDModel.getReCourseInfoModel().getOverviewModel();
        
        // Listen to level changes to update Combo box
        fLDModel.addPropertyChangeListener(this);
        
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                fLDModel.removePropertyChangeListener(OverviewSection.this);
            }
        });
        
        setup();
        
        populateFields();
    }
    
    private void setup() {
        setLayout(new GridLayout());
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        Composite fieldsComposite = AppFormToolkit.getInstance().createComposite(this);
        fieldsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        GridLayout layout = new GridLayout(6, false);
        fieldsComposite.setLayout(layout);
        
        // Needed on Ubuntu
        AppFormToolkit.getInstance().paintBordersFor(fieldsComposite);
        
        AppFormToolkit.getInstance().createLabel(fieldsComposite, Messages.OverviewSection_0, SWT.NONE);
        fTitleText = AppFormToolkit.getInstance().createText(fieldsComposite, ""); //$NON-NLS-1$
        GridData gd = new GridData(SWT.FILL, SWT.NULL, true, false);
        gd.horizontalSpan = 5;
        fTitleText.setLayoutData(gd);
        
        AppFormToolkit.getInstance().createLabel(fieldsComposite, Messages.OverviewSection_1, SWT.NONE);
        fURIText = AppFormToolkit.getInstance().createText(fieldsComposite, ""); //$NON-NLS-1$
        fURIText.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false));

        AppFormToolkit.getInstance().createLabel(fieldsComposite, Messages.OverviewSection_2, SWT.NONE);
        fVersionText = AppFormToolkit.getInstance().createText(fieldsComposite, ""); //$NON-NLS-1$
        gd = new GridData(SWT.FILL, SWT.NULL, false, false);
        gd.widthHint = 60;
        fVersionText.setLayoutData(gd);
        
        AppFormToolkit.getInstance().createLabel(fieldsComposite, Messages.OverviewSection_3, SWT.NONE);
        fComboLevel = new Combo(fieldsComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        fComboLevel.setItems( new String[] {"A", "B", "C"} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        gd = new GridData(SWT.FILL, SWT.NULL, false, false);
        fComboLevel.setLayoutData(gd);

        AppFormToolkit.getInstance().createLabel(fieldsComposite, Messages.OverviewSection_4, SWT.NONE);
        fTagsText = AppFormToolkit.getInstance().createText(fieldsComposite, ""); //$NON-NLS-1$
        gd = new GridData(SWT.FILL, SWT.NULL, true, false);
        gd.horizontalSpan = 5;
        fTagsText.setLayoutData(gd);
        
        AppFormToolkit.getInstance().createLabel(fieldsComposite, Messages.OverviewSection_5, SWT.NONE);
        fAuthorText = AppFormToolkit.getInstance().createText(fieldsComposite, ""); //$NON-NLS-1$
        gd = new GridData(SWT.FILL, SWT.NULL, true, false);
        gd.horizontalSpan = 5;
        fAuthorText.setLayoutData(gd);
        
        AppFormToolkit.getInstance().createLabel(fieldsComposite, Messages.OverviewSection_6, SWT.NONE);
        fSubjectText = AppFormToolkit.getInstance().createText(fieldsComposite, ""); //$NON-NLS-1$
        gd = new GridData(SWT.FILL, SWT.NULL, true, false);
        gd.horizontalSpan = 5;
        fSubjectText.setLayoutData(gd);

        AppFormToolkit.getInstance().createLabel(fieldsComposite, Messages.OverviewSection_7, SWT.NONE);
        
        fDescriptionText = AppFormToolkit.getInstance().createText(fieldsComposite, "", SWT.MULTI | SWT.WRAP | SWT.V_SCROLL); //$NON-NLS-1$
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 120;
        gd.horizontalSpan = 5;
        fDescriptionText.setLayoutData(gd);
        
        // Modify listeners
        fTitleText.addModifyListener(fModifyListener);
        fURIText.addModifyListener(fModifyListener);
        fVersionText.addModifyListener(fModifyListener);
        fDescriptionText.addModifyListener(fModifyListener);
        fTagsText.addModifyListener(fModifyListener);
        fAuthorText.addModifyListener(fModifyListener);
        fSubjectText.addModifyListener(fModifyListener);
        fComboLevel.addSelectionListener(fSelectionListener);
        
        
        Hyperlink link = AppFormToolkit.getInstance().createHyperlink(this, Messages.OverviewEditorPage_3, SWT.NULL);
        link.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent e) {
                doLearningObjectives();
            }
        });
        link.setToolTipText(Messages.OverviewEditorPage_4);
        
        link = AppFormToolkit.getInstance().createHyperlink(this, Messages.OverviewEditorPage_1, SWT.NULL);
        link.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent e) {
                doPrerequisites();
            }
        });
        link.setToolTipText(Messages.OverviewEditorPage_2);
        
        link = AppFormToolkit.getInstance().createHyperlink(this, Messages.OverviewEditorPage_5, SWT.NULL);
        link.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent e) {
                doCompletion();
            }
        });
        link.setToolTipText(Messages.OverviewEditorPage_5);

    }

    private void populateFields() {
        fIsUpdating = true;
        
        fTitleText.setText(StringUtils.safeString(fLDModel.getTitle()));
        fURIText.setText(StringUtils.safeString(fLDModel.getURI()));
        fVersionText.setText(StringUtils.safeString(fLDModel.getVersion()));
        fDescriptionText.setText(StringUtils.safeString(fOverviewModel.getDescription()));
        fTagsText.setText(StringUtils.safeString(fOverviewModel.getTags()));
        fAuthorText.setText(StringUtils.safeString(fOverviewModel.getAuthor()));
        fSubjectText.setText(StringUtils.safeString(fOverviewModel.getSubject()));
        fComboLevel.setText(fLDModel.getLevel());

        fIsUpdating = false;
    }

    private void doPrerequisites() {
        InspectorViewModelAdapter adapter = new InspectorViewModelAdapter(fLDModel.getPrerequisites(), 
                Messages.OverviewEditorPage_2);
        
        InspectorManager.getInstance().setInput(fEditor, adapter);
        InspectorManager.getInstance().showInspector();
    }
    
    private void doLearningObjectives() {
        InspectorViewModelAdapter adapter = new InspectorViewModelAdapter(fLDModel.getLearningObjectives(), 
            Messages.OverviewEditorPage_4);
        
        InspectorManager.getInstance().setInput(fEditor, adapter);
        InspectorManager.getInstance().showInspector();
    }
    
    private void doCompletion() {
        InspectorViewModelAdapter adapter = new InspectorViewModelAdapter(fLDModel.getMethodModel().getCompleteUOLType(), 
            Messages.OverviewEditorPage_5);
        
        InspectorManager.getInstance().setInput(fEditor, adapter);
        InspectorManager.getInstance().showInspector();
    }
    

    public void propertyChange(PropertyChangeEvent evt) {
        // Update Level Combo
        if(evt.getPropertyName() == ILDModel.PROPERTY_LEVEL) {
            fIsUpdating = true;
            fComboLevel.setText(fLDModel.getLevel());
            fIsUpdating = false;
        }
    }


}
