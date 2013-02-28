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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.tencompetence.ldauthor.LDAuthorApp;
import org.tencompetence.ldauthor.LDAuthorPlugin;

/**
 * Class used to initialize default preference values
 * 
 * @author Phillip Beauvoir
 * @version $Id: PreferenceInitializer.java,v 1.28 2009/10/30 14:18:10 phillipus Exp $
 */
public class PreferenceInitializer
extends AbstractPreferenceInitializer
implements ILDAuthorPreferenceConstants
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = LDAuthorPlugin.getDefault().getPreferenceStore();
		
        // Initialise here
        store.setDefault(PREFS_BROWSER_USE_EXTERNAL, "false"); //$NON-NLS-1$
        store.setDefault(PREFS_BROWSER_VIEW_RESTORE_URL, "false"); //$NON-NLS-1$
        store.setDefault(PREFS_BROWSER_VIEW_URL, "about:blank"); //$NON-NLS-1$
        
        store.setDefault(PREFS_PREVIEW_POPUP, "false"); //$NON-NLS-1$
        
        // Default user data folder
        store.setDefault(PREFS_USER_DATA_FOLDER, LDAuthorApp.WORKSPACE_FOLDER.getPath());

        store.setDefault(PREFS_CONNECTION_TOOL_STICKY, "true"); //$NON-NLS-1$
        
        // LD Objects
        store.setDefault(PREFS_LDOBJECT_METHOD, Messages.PreferenceInitializer_0); 
        store.setDefault(PREFS_LDOBJECT_PLAY, Messages.PreferenceInitializer_1); 
        store.setDefault(PREFS_LDOBJECT_PLAYS, Messages.PreferenceInitializer_2); 
        store.setDefault(PREFS_LDOBJECT_ACT, Messages.PreferenceInitializer_3); 
        store.setDefault(PREFS_LDOBJECT_ACTS, Messages.PreferenceInitializer_4); 
        store.setDefault(PREFS_LDOBJECT_LEARNER, Messages.PreferenceInitializer_5); 
        store.setDefault(PREFS_LDOBJECT_STAFF, Messages.PreferenceInitializer_6); 
        store.setDefault(PREFS_LDOBJECT_ENVIRONMENT, Messages.PreferenceInitializer_7); 
        store.setDefault(PREFS_LDOBJECT_ENVIRONMENTS, Messages.PreferenceInitializer_8); 
        
        // CC Server
        store.setDefault(PREFS_CC_SERVER, "http://coppercore.tencompetence.org:8080"); //$NON-NLS-1$
        store.setDefault(PREFS_CC_INCLUDE_RECOURSE_INFO, "false"); //$NON-NLS-1$
        store.setDefault(PREFS_CC_LOCAL_SERVER, "http://localhost:8080"); //$NON-NLS-1$
        store.setDefault(PREFS_CC_LOCAL_INCLUDE_RECOURSE_INFO, "false"); //$NON-NLS-1$
        
        // Editors
        store.setDefault(PREFS_EDITORS, ""); //$NON-NLS-1$
        store.setDefault(PREFS_USE_RTE, "true"); //$NON-NLS-1$
    }
}
