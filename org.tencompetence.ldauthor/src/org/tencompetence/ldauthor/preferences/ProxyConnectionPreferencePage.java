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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.LDAuthorPlugin;

/**
 * Proxy Connection Preferences Page
 * 
 * @author Paul Sharples
 * @author Phil Beauvoir
 * @version $Id: ProxyConnectionPreferencePage.java,v 1.1 2008/10/02 11:25:03 phillipus Exp $
 */
public class ProxyConnectionPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, ILDAuthorPreferenceConstants
{
    public static String HELPID_PREFERENCES = LDAuthorPlugin.PLUGIN_ID + ".prefsProxyConnectionHelp"; //$NON-NLS-1$
    
    private Text textHTTPProxyHost, textHTTPProxyPort, textHTTPProxyUser, textHTTPProxyPassword;
    
    private Button fNTLMAuthButton;
    
	/**
	 * Constructor
	 */
	public ProxyConnectionPreferencePage() {
		setPreferenceStore(LDAuthorPlugin.getDefault().getPreferenceStore());
	}
	

	@Override
    protected Control createContents(Composite parent) {
        GridData gd;
        Label label;
        
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        Group group = new Group(client, SWT.NULL);
        group.setText(Messages.ProxyConnectionPreferencePage_0);
        group.setLayout(new GridLayout(2, false));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        
        label = new Label(group, SWT.NULL);
        label.setText(Messages.ProxyConnectionPreferencePage_1);
        textHTTPProxyHost = new Text(group, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textHTTPProxyHost.setLayoutData(gd);
        
        label = new Label(group, SWT.NULL);
        label.setText(Messages.ProxyConnectionPreferencePage_2);
        textHTTPProxyPort = new Text(group, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textHTTPProxyPort.setLayoutData(gd);
        
        label = new Label(group, SWT.NULL);
        label.setText(Messages.ProxyConnectionPreferencePage_3);
        textHTTPProxyUser = new Text(group, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textHTTPProxyUser.setLayoutData(gd);
        
        label = new Label(group, SWT.NULL);
        label.setText(Messages.ProxyConnectionPreferencePage_4);
        textHTTPProxyPassword = new Text(group, SWT.BORDER | SWT.PASSWORD);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textHTTPProxyPassword.setLayoutData(gd);
        
        label = new Label(group, SWT.NULL);
        label.setText(Messages.ProxyConnectionPreferencePage_5);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        label.setLayoutData(gd);
        
        
        
        Group advGroup = new Group(client, SWT.NULL);
        advGroup.setText(Messages.ProxyConnectionPreferencePage_6);        
        GridLayout subLayout = new GridLayout();
        subLayout.numColumns = 2;
        advGroup.setLayout(subLayout);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        advGroup.setLayoutData(gd);
        
        label = new Label(advGroup, SWT.NULL);
        label.setText(Messages.ProxyConnectionPreferencePage_7);        
        fNTLMAuthButton = new Button(advGroup, SWT.CHECK | SWT.LEFT);   
        IPreferenceStore store = getPreferenceStore();
        
        
        textHTTPProxyHost.setText(store.getString(PREFS_HTTP_PROXY_HOST));
        textHTTPProxyPort.setText(store.getString(PREFS_HTTP_PROXY_PORT));
        textHTTPProxyUser.setText(ProxyConnectionPrefsManager.getDecryptedString(PREFS_HTTP_PROXY_USERNAME));
        textHTTPProxyPassword.setText(ProxyConnectionPrefsManager.getDecryptedString(PREFS_HTTP_PROXY_PASSWORD));
        
        fNTLMAuthButton.setSelection(store.getBoolean(PREFS_USE_HTTP_PROXY_NTLM));
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), HELPID_PREFERENCES);
        
        return client;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
    @Override
	public boolean performOk() {
        //IPreferenceStore store = getPreferenceStore();
        
        String proxyHost = textHTTPProxyHost.getText();
        String proxyPort = textHTTPProxyPort.getText();
        String proxyUser = textHTTPProxyUser.getText();
        String proxyPassword = textHTTPProxyPassword.getText();
        boolean useNTLNAuth = fNTLMAuthButton.getSelection();
        
        ProxyConnectionPrefsManager.storeHttpProxyPreferences(proxyHost, proxyPort, proxyUser, proxyPassword, useNTLNAuth);
        ProxyConnectionPrefsManager.setHttpProxySettings();
        
        return true;
    }
    
    @Override
	protected void performDefaults() {
        IPreferenceStore store = getPreferenceStore();
        
        textHTTPProxyHost.setText(store.getDefaultString(PREFS_HTTP_PROXY_HOST));
        textHTTPProxyPort.setText(store.getDefaultString(PREFS_HTTP_PROXY_PORT));
        textHTTPProxyUser.setText(store.getDefaultString(PREFS_HTTP_PROXY_USERNAME));
        textHTTPProxyPassword.setText(store.getDefaultString(PREFS_HTTP_PROXY_PASSWORD));
        fNTLMAuthButton.setSelection(store.getDefaultBoolean(PREFS_USE_HTTP_PROXY_NTLM));
                
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