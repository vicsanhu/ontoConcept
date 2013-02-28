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
 * General Preferences Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: CopperCorePreferencePage.java,v 1.6 2009/12/14 11:16:53 phillipus Exp $
 */
public class CopperCorePreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, ILDAuthorPreferenceConstants
{
    public static String HELPID_PREFERENCES = LDAuthorPlugin.PLUGIN_ID + ".prefsCopperCoreHelp"; //$NON-NLS-1$
    
    private Text textServer, textUser, textPassword;
    private Button fButtonIncludeReCourseInfo;
    
    private Text textLocalServer, textLocalUser;
    private Button fButtonLocalIncludeReCourseInfo;

    /**
	 * Constructor
	 */
	public CopperCorePreferencePage() {
		setPreferenceStore(LDAuthorPlugin.getDefault().getPreferenceStore());
	}
	
    @Override
    protected Control createContents(Composite parent) {
        GridData gd;
        Label label;
        
        IPreferenceStore store = getPreferenceStore();
       
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        // Main Server
        
        Group maingroup = new Group(client, SWT.NULL);
        maingroup.setText(Messages.CopperCorePreferencePage_0);
        maingroup.setLayout(new GridLayout(2, false));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        maingroup.setLayoutData(gd);
        
        label = new Label(maingroup, SWT.NULL);
        label.setText(Messages.CopperCorePreferencePage_2);
        textServer = new Text(maingroup, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textServer.setLayoutData(gd);
        
        label = new Label(maingroup, SWT.NULL);
        label.setText(Messages.CopperCorePreferencePage_3);
        textUser = new Text(maingroup, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textUser.setLayoutData(gd);
        
        label = new Label(maingroup, SWT.NULL);
        label.setText(Messages.CopperCorePreferencePage_4);
        textPassword = new Text(maingroup, SWT.BORDER | SWT.PASSWORD);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textPassword.setLayoutData(gd);

        fButtonIncludeReCourseInfo = new Button(maingroup, SWT.CHECK);
        fButtonIncludeReCourseInfo.setText(Messages.CopperCorePreferencePage_1);
        fButtonIncludeReCourseInfo.setSelection(store.getBoolean(PREFS_CC_INCLUDE_RECOURSE_INFO));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fButtonIncludeReCourseInfo.setLayoutData(gd);
        
        textServer.setText(store.getString(PREFS_CC_SERVER));
        textUser.setText(CopperCorePrefsManager.getDecryptedString(PREFS_CC_USERNAME));
        textPassword.setText(CopperCorePrefsManager.getDecryptedString(PREFS_CC_PASSWORD));
        
        // Local Server
        
        Group localgroup = new Group(client, SWT.NULL);
        localgroup.setText(Messages.CopperCorePreferencePage_5);
        localgroup.setLayout(new GridLayout(2, false));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        localgroup.setLayoutData(gd);
        
        label = new Label(localgroup, SWT.NULL);
        label.setText(Messages.CopperCorePreferencePage_2);
        textLocalServer = new Text(localgroup, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textLocalServer.setLayoutData(gd);
        
        label = new Label(localgroup, SWT.NULL);
        label.setText(Messages.CopperCorePreferencePage_3);
        textLocalUser = new Text(localgroup, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        textLocalUser.setLayoutData(gd);
        
        fButtonLocalIncludeReCourseInfo = new Button(localgroup, SWT.CHECK);
        fButtonLocalIncludeReCourseInfo.setText(Messages.CopperCorePreferencePage_1);
        fButtonLocalIncludeReCourseInfo.setSelection(store.getBoolean(PREFS_CC_LOCAL_INCLUDE_RECOURSE_INFO));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fButtonLocalIncludeReCourseInfo.setLayoutData(gd);
        
        textLocalServer.setText(store.getString(PREFS_CC_LOCAL_SERVER));
        textLocalUser.setText(CopperCorePrefsManager.getDecryptedString(PREFS_CC_LOCAL_USERNAME));
        
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
        
        // Main
        store.setValue(PREFS_CC_SERVER, textServer.getText());
        CopperCorePrefsManager.storeUserPreferences(textUser.getText(), textPassword.getText());
        store.setValue(PREFS_CC_INCLUDE_RECOURSE_INFO, fButtonIncludeReCourseInfo.getSelection());

        // Local
        store.setValue(PREFS_CC_LOCAL_SERVER, textLocalServer.getText());
        store.setValue(PREFS_CC_LOCAL_USERNAME, textLocalUser.getText());
        store.setValue(PREFS_CC_LOCAL_INCLUDE_RECOURSE_INFO, fButtonLocalIncludeReCourseInfo.getSelection());

        return true;
    }

    @Override
    protected void performDefaults() {
        IPreferenceStore store = getPreferenceStore();
        
        // Main
        textServer.setText(store.getDefaultString(PREFS_CC_SERVER));
        textUser.setText(store.getDefaultString(PREFS_CC_USERNAME));
        textPassword.setText(store.getDefaultString(PREFS_CC_PASSWORD));
        fButtonIncludeReCourseInfo.setSelection(store.getDefaultBoolean(PREFS_CC_INCLUDE_RECOURSE_INFO));
        
        // Local
        textLocalServer.setText(store.getDefaultString(PREFS_CC_LOCAL_SERVER));
        textLocalUser.setText(store.getDefaultString(PREFS_CC_LOCAL_USERNAME));
        fButtonLocalIncludeReCourseInfo.setSelection(store.getDefaultBoolean(PREFS_CC_LOCAL_INCLUDE_RECOURSE_INFO));

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