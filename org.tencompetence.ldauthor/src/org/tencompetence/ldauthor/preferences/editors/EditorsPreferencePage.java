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
package org.tencompetence.ldauthor.preferences.editors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;

/**
 * Set External Editor Prefs Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: EditorsPreferencePage.java,v 1.3 2008/05/29 19:09:20 phillipus Exp $
 */
public class EditorsPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, ILDAuthorPreferenceConstants
{
    public static String HELPID_PREFERENCES = LDAuthorPlugin.PLUGIN_ID + ".prefsEditorsHelp"; //$NON-NLS-1$
    
    private Button fButtonRTE;
    
    /**
     * The Selection Panel
     */
    private EditorSelectionPanel fSelectionPanel;
    
	/**
	 * Constructor
	 */
	public EditorsPreferencePage() {
	    setPreferenceStore(LDAuthorPlugin.getDefault().getPreferenceStore());
	}
	
    @Override
    protected Control createContents(Composite parent) {
        IPreferenceStore store = getPreferenceStore();

        fButtonRTE = new Button(parent, SWT.CHECK);
        fButtonRTE.setText(Messages.EditorsPreferencePage_1);
        fButtonRTE.setSelection(store.getBoolean(PREFS_USE_RTE));
        
        new Label(parent, SWT.NULL);
        
        Label label = new Label(parent, SWT.NULL);
        label.setText(Messages.EditorsPreferencePage_0);

        fSelectionPanel = new EditorSelectionPanel(parent, SWT.NULL);
        
        String data = store.getString(PREFS_EDITORS);
        fSelectionPanel.setPreferencesData(data);

        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), HELPID_PREFERENCES);
        
        return fSelectionPanel;
    }
    
    public void init(IWorkbench workbench) {
        //IPreferenceStore store = getPreferenceStore();
    }

    @Override
    protected void performDefaults() {
        IPreferenceStore store = getPreferenceStore();
        
        fButtonRTE.setSelection(store.getDefaultBoolean(PREFS_USE_RTE));
        fSelectionPanel.setPreferencesData(store.getDefaultString(PREFS_EDITORS));
        
        super.performDefaults();
    }

    @Override
    public boolean performOk() {
        IPreferenceStore store = getPreferenceStore();
        
        store.setValue(PREFS_USE_RTE, fButtonRTE.getSelection());
        store.setValue(PREFS_EDITORS, fSelectionPanel.getPreferencesData());
        
        return true;
    }
}