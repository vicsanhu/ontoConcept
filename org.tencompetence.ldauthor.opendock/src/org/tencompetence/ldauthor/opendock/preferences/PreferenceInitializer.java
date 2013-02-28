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
package org.tencompetence.ldauthor.opendock.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.tencompetence.ldauthor.opendock.OpenDockPlugin;

/**
 * Class used to initialize default preference values
 * 
 * @author Phillip Beauvoir
 * @version $Id: PreferenceInitializer.java,v 1.4 2008/05/28 14:15:13 phillipus Exp $
 */
public class PreferenceInitializer
extends AbstractPreferenceInitializer
implements IOpenDockPreferenceConstants
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = OpenDockPlugin.getDefault().getPreferenceStore();
		
        // Initialise here
        store.setDefault(PREFS_OPENDOCK_REPOSITORY_URL, "http://demo.opendocument.net/Repository/server_interface/server.php"); //$NON-NLS-1$
        store.setDefault(PREFS_OPENDOCK_APP_ID, "id4835653c4cc3a6.64594436"); //$NON-NLS-1$
        store.setDefault(PREFS_OPENDOCK_USER_ID, "ff1dd51d1fa58645cf402605ee2bdd78"); //$NON-NLS-1$
        store.setDefault(PREFS_OPENDOCK_REPOSITORY_ID, "id47becf6b668aa3.19149389"); //$NON-NLS-1$
    }
}
