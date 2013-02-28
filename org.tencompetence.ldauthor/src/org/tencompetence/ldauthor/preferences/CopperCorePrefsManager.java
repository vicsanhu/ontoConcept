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

import net.sourceforge.blowfishj.BlowfishEasy;

import org.eclipse.jface.preference.IPreferenceStore;
import org.tencompetence.ldauthor.LDAuthorPlugin;


/**
 * Convenience class to set Connections settings based on user Prefs
 * 
 * @author Paul Sharples 
 * @author Phillip Beauvoir
 * @version $Id: CopperCorePrefsManager.java,v 1.1 2008/10/15 17:04:54 phillipus Exp $
 */
public class CopperCorePrefsManager implements ILDAuthorPreferenceConstants {
    
    /**
     * Simple password encrypter
     */
    private static BlowfishEasy fBlowFish;
    
    /**
     * Store the User settings in the Preference Store encrypted
     * 
     * @param proxyHost
     * @param proxyPort
     * @param proxyUser
     * @param proxyPassword
     */
    public static void storeUserPreferences(String userName, String password) {
        IPreferenceStore store = LDAuthorPlugin.getDefault().getPreferenceStore();

        // Encrypt username and password
        if(userName.length() > 0) {
            userName = getBlowfishEasy().encryptString(userName);
        }
        if(password.length() > 0) {
            password = getBlowfishEasy().encryptString(password);
        }
        store.setValue(PREFS_CC_USERNAME, userName);
        store.setValue(PREFS_CC_PASSWORD, password);
    }

    /**
     * @return A decrypted String in Prefs
     */
    public static String getDecryptedString(String name) {
        IPreferenceStore store = LDAuthorPlugin.getDefault().getPreferenceStore();
        
        String s = store.getString(name);
        if(s != null) {
            s = getBlowfishEasy().decryptString(s);
        }
        
        return s == null ? "" : s;//$NON-NLS-1$
    }

    private static BlowfishEasy getBlowfishEasy() {
        if(fBlowFish == null) {
            // TODO - Use uniqe ID for this - user name?
            fBlowFish = new BlowfishEasy(new char[] { 126, 112, 108, 101, 120, 50, 48, 48, 53, 126 });
        }
        
        return fBlowFish;
    }
    
}
