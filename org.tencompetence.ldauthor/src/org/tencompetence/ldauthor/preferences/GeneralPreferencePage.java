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
package org.tencompetence.ldauthor.preferences;

import java.io.File;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.LDAuthorPlugin;

/**
 * General Preferences Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: GeneralPreferencePage.java,v 1.18 2008/11/22 01:47:39 phillipus Exp $
 */
public class GeneralPreferencePage
extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage, ILDAuthorPreferenceConstants
{
    public static String HELPID_PREFERENCES = LDAuthorPlugin.PLUGIN_ID + ".prefsGeneralHelp"; //$NON-NLS-1$
    
    
	/**
	 * Constructor
	 */
	public GeneralPreferencePage() {
		super(FLAT);
		setPreferenceStore(LDAuthorPlugin.getDefault().getPreferenceStore());
		//setDescription("General Preferences.");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	@Override
    public void createFieldEditors() {
        addField(new DirectoryFieldEditor(PREFS_USER_DATA_FOLDER,
                Messages.GeneralPreferencePage_0,
                getFieldEditorParent()));
        
        addField(new BooleanFieldEditor(PREFS_CONNECTION_TOOL_STICKY,
                Messages.GeneralPreferencePage_1,
                getFieldEditorParent()));
        
        addField(new BooleanFieldEditor(PREFS_BROWSER_USE_EXTERNAL,
                Messages.GeneralPreferencePage_2,
                getFieldEditorParent()));
        
        addField(new BooleanFieldEditor(PREFS_PREVIEW_POPUP,
                Messages.GeneralPreferencePage_3,
                getFieldEditorParent()));
        
        /*
         * Register Help Context
         */ 
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), HELPID_PREFERENCES);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        super.performOk();
        
        // IPreferenceStore store = getPreferenceStore();
        
        // Do some stuff here
        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        // Make sure default folder exists, else it complains
        IPreferenceStore store = getPreferenceStore();
        String path = store.getDefaultString(ILDAuthorPreferenceConstants.PREFS_USER_DATA_FOLDER);
        File file = new File(path);
        file.mkdirs();
        
        super.performDefaults();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }
}