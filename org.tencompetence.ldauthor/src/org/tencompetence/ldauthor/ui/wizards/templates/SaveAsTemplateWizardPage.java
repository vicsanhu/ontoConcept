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
package org.tencompetence.ldauthor.ui.wizards.templates;

import java.io.IOException;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
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
import org.tencompetence.ldauthor.templates.ITemplateGroup;
import org.tencompetence.ldauthor.templates.LDTemplateManager;
import org.tencompetence.ldauthor.templates.UserTemplateGroup;
import org.tencompetence.ldauthor.ui.templates.CategoriesTableViewer;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Save As Template Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: SaveAsTemplateWizardPage.java,v 1.5 2009/12/10 14:48:27 phillipus Exp $
 */
public class SaveAsTemplateWizardPage extends WizardPage {

    public static final String PAGE_NAME = "SaveAsTemplateWizardPage"; //$NON-NLS-1$
    
    private Text fNameTextField;
    
    private Text fDescriptionTextField;

    private CategoriesTableViewer fCategoriesTableViewer;
    
    private LDTemplateManager fLDTemplateManager;
    
    private ITemplateGroup fSelectedTemplateGroup;
    
    private Button fNewGroupButton;
    
    /**
	 * Constructor
	 */
	public SaveAsTemplateWizardPage(LDTemplateManager templateManager) {
		super(PAGE_NAME);
		setTitle(Messages.SaveAsTemplateWizardPage_0);
		setDescription(Messages.SaveAsTemplateWizardPage_1);
        //setImageDescriptor(ImageFactory.getImageDescriptor("An Image"));
		
		fLDTemplateManager = templateManager;
	}

    public void createControl(Composite parent) {
        GridData gd;
        Label label;
        
	    Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        
        Composite fieldContainer = new Composite(container, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        fieldContainer.setLayout(layout);
	    
        label = new Label(fieldContainer, SWT.NULL);
        label.setText(Messages.SaveAsTemplateWizardPage_2);
        
        fNameTextField = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
        fNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fNameTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        label = new Label(fieldContainer, SWT.NULL);
        label.setText(Messages.SaveAsTemplateWizardPage_3);
        
        fDescriptionTextField = new Text(fieldContainer, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        fDescriptionTextField.setLayoutData(gd);

	    label = new Label(container, SWT.NULL);
	    label.setText(Messages.SaveAsTemplateWizardPage_4);

        Composite tableComp = new Composite(container, SWT.NULL);
        tableComp.setLayout(new TableColumnLayout());
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        tableComp.setLayoutData(gd);
        fCategoriesTableViewer = new CategoriesTableViewer(tableComp, SWT.BORDER);
        
        fCategoriesTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                fSelectedTemplateGroup = (ITemplateGroup)((IStructuredSelection)event.getSelection()).getFirstElement();
            }
        });
        
        fCategoriesTableViewer.setInput(fLDTemplateManager);
        
        fCategoriesTableViewer.setSelection(new StructuredSelection(fLDTemplateManager.getTemplateGroups().get(0)));
        
        fNewGroupButton = new Button(container, SWT.FLAT);
        fNewGroupButton.setText(Messages.SaveAsTemplateWizardPage_6);
        
        fNewGroupButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                InputDialog dialog = new InputDialog(getShell(),
                        Messages.SaveAsTemplateWizardPage_7,
                        Messages.SaveAsTemplateWizardPage_8,
                        "", //$NON-NLS-1$
                        null);
                
                if(dialog.open() == InputDialog.OK) {
                    String name = dialog.getValue();
                    if(StringUtils.isSetAfterTrim(name)) {
                        ITemplateGroup group = new UserTemplateGroup(name);
                        fLDTemplateManager.getTemplateGroups().add(group);
                        
                        try {
                            fLDTemplateManager.saveUserTemplatesManifest();
                        }
                        catch(IOException ex) {
                            ex.printStackTrace();
                        }
                        
                        fCategoriesTableViewer.refresh();
                    }
                }
            }
        });
        
	    setPageComplete(false);
        setControl(container);
	}

    private void validateFields() {
        String name = fNameTextField.getText();
        
        if(!StringUtils.isSetAfterTrim(name)) {
            updateStatus(Messages.SaveAsTemplateWizardPage_5);
            return;
        }
        
        updateStatus(null);
    }
    
    /**
     * Update the page status
     * @param message
     */
    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }
    
    /**
     * @return The Name for the template
     */
    public String getTemplateName() {
        return fNameTextField.getText();
    }
    
    /**
     * @return The Name for the template
     */
    public String getTemplateDescription() {
        return fDescriptionTextField.getText();
    }

    public ITemplateGroup getTemplateGroup() {
        return fSelectedTemplateGroup;
    }
}