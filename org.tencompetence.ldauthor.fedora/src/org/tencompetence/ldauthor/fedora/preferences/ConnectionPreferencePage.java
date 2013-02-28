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
package org.tencompetence.ldauthor.fedora.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.fedora.FedoraPlugin;

/**
 * Connection Preferences Page
 * 
 * @author Paul Sharples
 * @author Phil Beauvoir
 * @version $Id: ConnectionPreferencePage.java,v 1.2 2008/10/15 17:05:04 phillipus Exp $
 */
public class ConnectionPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IFedoraPreferenceConstants
{
    public static String HELPID_PREFERENCES = FedoraPlugin.PLUGIN_ID + ".prefsConnectionHelp"; //$NON-NLS-1$
    
    private Text textServer, textUser, textPassword;
    
	/**
	 * Constructor
	 */
	public ConnectionPreferencePage() {
		setPreferenceStore(FedoraPlugin.getDefault().getPreferenceStore());
	}
	

	@Override
    protected Control createContents(Composite parent) {
        GridData gd;
        Label label;
        
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        Group maingroup = new Group(client, SWT.NULL);
        maingroup.setText(Messages.ConnectionPreferencePage_0);
        maingroup.setLayout(new GridLayout(2, false));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        maingroup.setLayoutData(gd);
        
        label = new Label(maingroup, SWT.NULL);
        label.setText(Messages.ConnectionPreferencePage_1);
        textServer = new Text(maingroup, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textServer.setLayoutData(gd);
        
        label = new Label(maingroup, SWT.NULL);
        label.setText(Messages.ConnectionPreferencePage_2);
        textUser = new Text(maingroup, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textUser.setLayoutData(gd);
        
        label = new Label(maingroup, SWT.NULL);
        label.setText(Messages.ConnectionPreferencePage_3);
        textPassword = new Text(maingroup, SWT.BORDER | SWT.PASSWORD);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textPassword.setLayoutData(gd);


        IPreferenceStore store = getPreferenceStore();
        textServer.setText(store.getString(PREFS_FEDORA_SERVER_URL));
        textUser.setText(ConnectionsPrefsManager.getDecryptedString(PREFS_FEDORA_USERNAME));
        textPassword.setText(ConnectionsPrefsManager.getDecryptedString(PREFS_FEDORA_PASSWORD));
        
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), HELPID_PREFERENCES);
        
        return client;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
    @Override
	public boolean performOk() {
        IPreferenceStore store = getPreferenceStore();
        
        store.setValue(PREFS_FEDORA_SERVER_URL, textServer.getText());
        
        ConnectionsPrefsManager.storeUserPreferences(textUser.getText(), textPassword.getText());

        return true;
    }
    
    @Override
	protected void performDefaults() {
        IPreferenceStore store = getPreferenceStore();
        
        textServer.setText(store.getDefaultString(PREFS_FEDORA_SERVER_URL));
        textUser.setText(store.getDefaultString(PREFS_FEDORA_USERNAME));
        textPassword.setText(store.getDefaultString(PREFS_FEDORA_PASSWORD));
        
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