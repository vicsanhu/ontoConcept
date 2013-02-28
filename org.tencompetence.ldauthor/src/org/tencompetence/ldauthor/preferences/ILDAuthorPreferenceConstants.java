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

import org.tencompetence.imsldmodel.LDModelFactory;

/**
 * Constant definitions for plug-in preferences
 * 
 * @author Phillip Beauvoir
 * @version $Id: ILDAuthorPreferenceConstants.java,v 1.26 2009/10/30 14:18:10 phillipus Exp $
 */
public interface ILDAuthorPreferenceConstants {
    
    String PREFS_BROWSER_VIEW_RESTORE_URL = "browser.ViewRestoreURL"; //$NON-NLS-1$
    String PREFS_BROWSER_VIEW_URL = "browser.ViewURL"; //$NON-NLS-1$
    String PREFS_BROWSER_USE_EXTERNAL = "browser.External"; //$NON-NLS-1$
    String PREFS_DEFAULT_TAB = "defaultTab"; //$NON-NLS-1$
    String PREFS_USER_DATA_FOLDER = "userDataFolder"; //$NON-NLS-1$
    String PREFS_LAST_FOLDER = "lastFolder"; //$NON-NLS-1$
    
    String PREFS_CC_SERVER = "CCServer"; //$NON-NLS-1$
    String PREFS_CC_USERNAME = "CCUser"; //$NON-NLS-1$
    String PREFS_CC_PASSWORD = "CCPassword"; //$NON-NLS-1$
    String PREFS_CC_INCLUDE_RECOURSE_INFO = "CCReCourseInfo"; //$NON-NLS-1$
    
    String PREFS_CC_LOCAL_SERVER = "CCLocalServer"; //$NON-NLS-1$
    String PREFS_CC_LOCAL_USERNAME = "CCLocalUser"; //$NON-NLS-1$
    String PREFS_CC_LOCAL_INCLUDE_RECOURSE_INFO = "CCLocalReCourseInfo"; //$NON-NLS-1$

    String PREFS_CONNECTION_TOOL_STICKY = "connectionToolSticky"; //$NON-NLS-1$
	
    String PREFS_LDOBJECT_USERNAME = "ldObjectUserName_"; //$NON-NLS-1$
    String PREFS_LDOBJECT_METHOD = PREFS_LDOBJECT_USERNAME + LDModelFactory.METHOD;
    String PREFS_LDOBJECT_PLAY = PREFS_LDOBJECT_USERNAME + LDModelFactory.PLAY;
    String PREFS_LDOBJECT_PLAYS = PREFS_LDOBJECT_USERNAME + LDModelFactory.PLAYS;
    String PREFS_LDOBJECT_ACT = PREFS_LDOBJECT_USERNAME + LDModelFactory.ACT;
    String PREFS_LDOBJECT_ACTS = PREFS_LDOBJECT_USERNAME + LDModelFactory.ACTS;
    String PREFS_LDOBJECT_LEARNER = PREFS_LDOBJECT_USERNAME + LDModelFactory.LEARNER;
    String PREFS_LDOBJECT_STAFF = PREFS_LDOBJECT_USERNAME + LDModelFactory.STAFF;
    String PREFS_LDOBJECT_ENVIRONMENT = PREFS_LDOBJECT_USERNAME + LDModelFactory.ENVIRONMENT;
    String PREFS_LDOBJECT_ENVIRONMENTS = PREFS_LDOBJECT_USERNAME + LDModelFactory.ENVIRONMENTS;
    
    String PREFS_PREVIEW_POPUP = "previewPopup"; //$NON-NLS-1$
    
    String PREFS_EDITORS = "externalEditors"; //$NON-NLS-1$
    
    String PREFS_USE_RTE = "useRTE"; //$NON-NLS-1$
    
    String PREFS_HTTP_PROXY_HOST = "httpProxyHost";//$NON-NLS-1$
    String PREFS_HTTP_PROXY_PORT = "httpProxyPort";//$NON-NLS-1$
    String PREFS_HTTP_PROXY_USERNAME = "httpProxyUser";//$NON-NLS-1$
    String PREFS_HTTP_PROXY_PASSWORD = "httpProxyPassword";//$NON-NLS-1$
    
    String PREFS_USE_HTTP_PROXY_NTLM = "httpProxyUseNTLM"; //$NON-NLS-1$        
    String PREFS_HTTP_PROXY_NTLM_DOMAIN = "httpProxyNTLMDomain";//$NON-NLS-1$
    String PREFS_HTTP_PROXY_NTLM_COMPUTERNAME = "httpProxyNTLMComputerName";//$NON-NLS-1$

}
