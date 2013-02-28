/*
 * Copyright (c) 2009, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.preferences.templates;

import java.io.IOException;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;
import org.tencompetence.ldauthor.templates.ITemplateGroup;
import org.tencompetence.ldauthor.templates.LDTemplateManager;
import org.tencompetence.ldauthor.templates.UserTemplateGroup;
import org.tencompetence.ldauthor.templates.impl.ld.UserLDTemplate;
import org.tencompetence.ldauthor.utils.StringUtils;

/**
 * Templates Prefs Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: TemplatesPreferencePage.java,v 1.6 2010/01/06 15:34:25 phillipus Exp $
 */
public class TemplatesPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, ILDAuthorPreferenceConstants
{
    public static String HELPID_PREFERENCES = LDAuthorPlugin.PLUGIN_ID + ".prefsTemplatesHelp"; //$NON-NLS-1$
    
    private TemplatesTreeViewer fTreeViewer;
    private TemplatesEditPanel fEditPanel;
    
    private LDTemplateManager fLDTemplateManager;
    
    private boolean fIsDirty;
    
    private Button fButtonDelete;
    
	/**
	 * Constructor
	 */
	public TemplatesPreferencePage() {
	    setPreferenceStore(LDAuthorPlugin.getDefault().getPreferenceStore());
	    
	    fLDTemplateManager = new LDTemplateManager();
	}
	
    @Override
    protected Control createContents(Composite parent) {
        SashForm sash = new SashForm(parent, SWT.VERTICAL);
        
        Composite client = new Composite(sash, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);
        
        fTreeViewer = new TemplatesTreeViewer(client, SWT.BORDER, fLDTemplateManager);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 180;
        fTreeViewer.getControl().setLayoutData(gd);
        
        fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                Object o = ((IStructuredSelection)event.getSelection()).getFirstElement();
                fEditPanel.setDisplay(o);
                updateButtons(o);
            }
        });
        
        createButtonBar(client);
        
        fEditPanel = new TemplatesEditPanel(sash, SWT.BORDER, fLDTemplateManager, fTreeViewer);
        
        //sash.setWeights(new int[] { 60, 40 });
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), HELPID_PREFERENCES);
        
        return sash;
    }
    
    /**
     * Create the Button bar
     * @param parent
     * @return The Bar
     */
    private Composite createButtonBar(Composite parent) {
        Composite client = new Composite(parent, SWT.NULL);
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);
        
        GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        client.setLayoutData(gd);

        // Delete
        fButtonDelete = new Button(client, SWT.PUSH);
        fButtonDelete.setText(Messages.TemplatesPreferencePage_0);
        fButtonDelete.setEnabled(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fButtonDelete.setLayoutData(gd);
        fButtonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleDeleteAction();
            }
        });
        
        // Add a new Category
        Button buttonNewCategory = new Button(client, SWT.PUSH);
        buttonNewCategory.setText(Messages.TemplatesPreferencePage_1);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        buttonNewCategory.setLayoutData(gd);
        buttonNewCategory.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                InputDialog dialog = new InputDialog(getShell(),
                        Messages.TemplatesPreferencePage_2,
                        Messages.TemplatesPreferencePage_3,
                        "", //$NON-NLS-1$
                        null);
                
                if(dialog.open() == InputDialog.OK) {
                    String name = dialog.getValue();
                    if(StringUtils.isSetAfterTrim(name)) {
                        ITemplateGroup group = new UserTemplateGroup(name);
                        fLDTemplateManager.getTemplateGroups().add(group);
                        fEditPanel.updateCombo();
                        fTreeViewer.refresh();
                        fIsDirty = true;
                    }
                }
            }
        });
        
        return client;
    }

    private void updateButtons(Object object) {
        if(object instanceof UserLDTemplate) {
            fButtonDelete.setEnabled(true);
        }
        else if(object instanceof UserTemplateGroup) {
            fButtonDelete.setEnabled(((UserTemplateGroup)object).getTemplates().isEmpty());
        }
        else {
            fButtonDelete.setEnabled(false);
        }
    }

    private void handleDeleteAction() {
        Object object = ((IStructuredSelection)fTreeViewer.getSelection()).getFirstElement();
        if(object instanceof UserLDTemplate) {
            UserLDTemplate template = (UserLDTemplate)object;
            
            if(!askUserDeleteConfirm(template.getName())) {
                return;
            }
            
            try {
                fLDTemplateManager.deleteTemplate(template);
            }
            catch(IOException ex) {
                MessageDialog.openError(getControl().getShell(), Messages.TemplatesTreeViewer_0, ex.getMessage());
            }
            
            fTreeViewer.refresh();
        }
        
        if(object instanceof UserTemplateGroup) {
            UserTemplateGroup group = (UserTemplateGroup)object;
            
            if(!askUserDeleteConfirm(group.getName())) {
                return;
            }
            
            fLDTemplateManager.getTemplateGroups().remove(group);
            
            fTreeViewer.refresh();
            
            fIsDirty = true;
        }
    }
    
    private boolean askUserDeleteConfirm(String name) {
        // Confirmation dialog
        return MessageDialog.openQuestion(
                Display.getDefault().getActiveShell(),
                Messages.TemplatesTreeViewer_1,
                Messages.TemplatesTreeViewer_2 +
                " '" + name + "'?");  //$NON-NLS-1$//$NON-NLS-2$

    }
    

    
    public void init(IWorkbench workbench) {
        //IPreferenceStore store = getPreferenceStore();
    }

    @Override
    protected void performDefaults() {
        //IPreferenceStore store = getPreferenceStore();
        
        super.performDefaults();
    }

    @Override
    public boolean performOk() {
        if(fIsDirty || fEditPanel.isDirty()) {
            try {
                fLDTemplateManager.saveUserTemplatesManifest();
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return true;
    }
}