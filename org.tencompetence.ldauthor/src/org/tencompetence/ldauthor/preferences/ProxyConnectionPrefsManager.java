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

import java.util.Properties;

import net.sourceforge.blowfishj.BlowfishEasy;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.Logger;


/**
 * Convenience class to set Proxy Connections settings based on user Prefs
 * 
 * @author Paul Sharples 
 * @author Phillip Beauvoir
 * @version $Id: ProxyConnectionPrefsManager.java,v 1.1 2008/10/02 11:25:03 phillipus Exp $
 */
public class ProxyConnectionPrefsManager implements ILDAuthorPreferenceConstants {
    
    /**
     * Simple password encrypter
     */
    private static BlowfishEasy fBlowFish;
    
    private static boolean fIsProxySet, fUseNTLMAuthentication;
    
    /**
     * Store the HTTP Proxy settings in the Preference Store encrypted
     * 
     * @param proxyHost
     * @param proxyPort
     * @param proxyUser
     * @param proxyPassword
     */
    public static void storeHttpProxyPreferences(String proxyHost, String proxyPort, String proxyUserName, String proxyPassword, boolean useNTLMAuth) {
        proxyHost = proxyHost.trim();
        proxyPort = proxyPort.trim();
        proxyUserName = proxyUserName.trim();
        proxyPassword = proxyPassword.trim();
        
        IPreferenceStore store = LDAuthorPlugin.getDefault().getPreferenceStore();

        // TODO - what's the minimum info needed to set the proxy - just the hostname or the other values too?
        // just flag it for now if the hostname is filled in
        checkProxySettings(proxyHost, useNTLMAuth); 
                        
        store.setValue(PREFS_HTTP_PROXY_HOST, proxyHost);
        store.setValue(PREFS_HTTP_PROXY_PORT, proxyPort);
        
        // Encrypt username and password
        if(proxyUserName.length() > 0) {
            proxyUserName = getBlowfishEasy().encryptString(proxyUserName);
        }
        if(proxyPassword.length() > 0) {
            proxyPassword = getBlowfishEasy().encryptString(proxyPassword);
        }
        store.setValue(PREFS_HTTP_PROXY_USERNAME, proxyUserName);
        store.setValue(PREFS_HTTP_PROXY_PASSWORD, proxyPassword);
        
        store.setValue(PREFS_USE_HTTP_PROXY_NTLM, useNTLMAuth);
    }

    /**
     * TODO - whats the minimum info needed to set the proxy - just the hostname or the other values too?
     * just flag it for now if the hostname is filled in
     */
    protected static void checkProxySettings(String hostname, boolean isNTLMSet){
    	if(hostname != null) {
    		fIsProxySet = (hostname.length() > 0 ? true : false);
    	}
    	fUseNTLMAuthentication = isNTLMSet;
    }
    
    /**
     * Set the global HTTP Proxy settings from User Prefs Store
     */
    public static void setHttpProxySettings() {
        IPreferenceStore store = LDAuthorPlugin.getDefault().getPreferenceStore();
        Properties props = System.getProperties();        
        String hostname = store.getString(PREFS_HTTP_PROXY_HOST); 
        boolean useNTLM = store.getBoolean(PREFS_USE_HTTP_PROXY_NTLM);
        props.put("http.proxyHost", hostname);//$NON-NLS-1$
        props.put("http.proxyPort", store.getString(PREFS_HTTP_PROXY_PORT));//$NON-NLS-1$
        props.put("http.proxyUserName", getDecryptedString(PREFS_HTTP_PROXY_USERNAME));//$NON-NLS-1$
        props.put("http.proxyPassword", getDecryptedString(PREFS_HTTP_PROXY_PASSWORD));//$NON-NLS-1$       
        props.put("http.proxyUseNTLM", useNTLM);//$NON-NLS-1$
        checkProxySettings(hostname, useNTLM);
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
    
    public static boolean proxyServerSet(){
    	return fIsProxySet;
    }
    
    public static boolean useNTLMAuthentication(){
    	return fUseNTLMAuthentication;
    }
      
    public static void setProxySettings(HttpClient client) {
    	if(proxyServerSet()){
    		IPreferenceStore store = LDAuthorPlugin.getDefault().getPreferenceStore();
    		String hostname = store.getString(PREFS_HTTP_PROXY_HOST);	        
    		int port;
    		try{
    			port = Integer.parseInt(store.getString(PREFS_HTTP_PROXY_PORT));
    		} 
    		catch(NumberFormatException ex){
    			// TODO - the user entered a hostname, but either left the port empty (default)
    			// or possible the user had non numeric data in there & it failed.
    			port=8080; // default for now if not specified
    		}
    		String username =  getDecryptedString(PREFS_HTTP_PROXY_USERNAME);
    		String password =  getDecryptedString(PREFS_HTTP_PROXY_PASSWORD);
    		HostConfiguration hConf= client.getHostConfiguration();
    		hConf.setProxy(hostname, port);
    		AuthScope scopeProxy = new AuthScope(hostname,port , AuthScope.ANY_REALM);
    		// 			
    		if(fUseNTLMAuthentication){								  
    			String domain = System.getenv("USERDOMAIN");    //$NON-NLS-1$
    			String computerName = System.getenv("COMPUTERNAME");//$NON-NLS-1$
    			if (domain!=null && computerName!=null){
    				NTCredentials userNTLMCredentials = new NTCredentials(username, password, computerName, domain);
    				client.getState().setProxyCredentials(scopeProxy, userNTLMCredentials);
    			}
    			else {
    				Logger.showErrorDialog("Failed to retrieve system domain/computer name for NTLM Authentication"); //$NON-NLS-1$
    			}
    		}
    		else{
    			client.getState().setProxyCredentials(scopeProxy, new UsernamePasswordCredentials(username, password));
    		}
    	}  
    }

}
