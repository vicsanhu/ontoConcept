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

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.LDAuthorPlugin;

/**
 * Strings Preferences Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: StringsPreferencePage.java,v 1.4 2008/04/24 10:14:52 phillipus Exp $
 */
public class StringsPreferencePage
extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage, ILDAuthorPreferenceConstants
{
    public static String HELPID_PREFERENCES = LDAuthorPlugin.PLUGIN_ID + ".prefsStringsHelp"; //$NON-NLS-1$
    
	/**
	 * Constructor
	 */
	public StringsPreferencePage() {
	    super(GRID);
		setPreferenceStore(LDAuthorPlugin.getDefault().getPreferenceStore());
		setDescription(Messages.StringsPreferencePage_0);
	}
	
	@Override
	public void createFieldEditors() {
	    addField(new StringFieldEditor(PREFS_LDOBJECT_METHOD,
                Messages.StringsPreferencePage_1,
                getFieldEditorParent()));
	    
	    addField(new StringFieldEditor(PREFS_LDOBJECT_PLAY,
                Messages.StringsPreferencePage_2,
                getFieldEditorParent()));
	    
	    addField(new StringFieldEditor(PREFS_LDOBJECT_PLAYS,
                Messages.StringsPreferencePage_3,
                getFieldEditorParent()));
	    
	    addField(new StringFieldEditor(PREFS_LDOBJECT_ACT,
                Messages.StringsPreferencePage_4,
                getFieldEditorParent()));
        
        addField(new StringFieldEditor(PREFS_LDOBJECT_ACTS,
                Messages.StringsPreferencePage_5,
                getFieldEditorParent()));
        
        addField(new StringFieldEditor(PREFS_LDOBJECT_ENVIRONMENT,
                Messages.StringsPreferencePage_6,
                getFieldEditorParent()));
        
        addField(new StringFieldEditor(PREFS_LDOBJECT_ENVIRONMENTS,
                Messages.StringsPreferencePage_7,
                getFieldEditorParent()));
        
        addField(new StringFieldEditor(PREFS_LDOBJECT_LEARNER,
                Messages.StringsPreferencePage_8,
                getFieldEditorParent()));
        
        addField(new StringFieldEditor(PREFS_LDOBJECT_STAFF,
                Messages.StringsPreferencePage_9,
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }
}