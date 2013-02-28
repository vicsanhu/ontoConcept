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
package org.tencompetence.ldauthor.opendock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.tencompetence.ldauthor.opendock.preferences.IOpenDockPreferenceConstants;
import org.tencompetence.ldauthor.opendock.ui.UploadZipWizard;
import org.tencompetence.ldauthor.organisermodel.IOrganiserLD;
import org.tencompetence.ldauthor.organisermodel.OrganiserModelFactory;
import org.tencompetence.ldauthor.ui.editors.EditorManager;
import org.tencompetence.ldauthor.utils.StringUtils;
import org.tencompetence.ldauthor.utils.ZipUtils;

/**
 * OpenDock Connection and Search Handler
 * 
 * @author Roy Cherian
 * @author Phillip Beauvoir
 * @version $Id: OpenDockHandler.java,v 1.10 2010/02/01 10:45:14 phillipus Exp $
 */
public class OpenDockHandler implements IOpenDockPreferenceConstants, IOpenDockConstants, IPropertyChangeListener {

    /**
     * Singleton instance
     */
    private static OpenDockHandler fInstance;

    /**
     * Flag for when the Handler is initialised
     */
    private boolean fInitialised = false;

    /**
     * Flag for when the Handler is connected
     */
    private boolean fConnected = false;

    private MessageDigest fMessageDigest;

    private Hashtable<String, String> fIdentity = new Hashtable<String, String>();

    private XmlRpcClient fXmlRpcClient;

    private String fOpenDockURL;
    private String fOpenDockRepositoryID;
    private String fOpenDockAppID;
    private String fOpenDockUserID;

    private List<Object> fNetworkList;
    private List<Object> fRepositoriesList;
    private List<Object> fContainerList;

    /**
     * @return The Singleton Instance
     */
    public static OpenDockHandler getInstance() {
        if(fInstance == null) {
            fInstance = new OpenDockHandler();
        }
        return fInstance;
    }


    /**
     * Private Constructor
     */
    private OpenDockHandler() {
        /*
         * Listen to Preference changes
         */
        OpenDockPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
    }

    /**
     * Load the Repository Parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean loadRepositoryParameters() {
        fNetworkList = new ArrayList<Object>();
        fRepositoriesList = new ArrayList<Object>();
        fContainerList = new ArrayList<Object>();
        
        loadPreferences();
        
        if(!fInitialised) {
            return false;
        }

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            fConnected = true;
            Date date = new Date();
            String time = Long.toString(date.getTime());

            String applcnID = time + fOpenDockAppID;
            applcnID = getMD5Cipher(applcnID.getBytes());

            String usrID = time + fOpenDockUserID;
            usrID = getMD5Cipher(usrID.getBytes());

            fIdentity.put(IDENTITY_TIME, time);
            fIdentity.put(IDENTITY_APPLICATION, applcnID);
            fIdentity.put(IDENTITY_USER, usrID);

            // http://designshare.opendocument.net/Repository/server_interface/server.php
            config.setServerURL(new URL(fOpenDockURL));

            fXmlRpcClient = new XmlRpcClient();
            fXmlRpcClient.setConfig(config);

            Object[] params = new Object[] { fIdentity };

            Object result = fXmlRpcClient.execute(METHOD_GET_LIST_NETWORKS, params); 
            if(result instanceof Object[]) {
                Object[] networks = (Object[]) result;
                for(int i = 0; i < networks.length; i++) {
                    fNetworkList.add(((HashMap) networks[i]).get(QUERY_NETWORK_ID));
                }
            }

            for (Iterator<Object> iter = fNetworkList.iterator(); iter.hasNext();) {
                int netID = Integer.parseInt(iter.next().toString());
                params = new Object[] { fIdentity, netID };
                result = fXmlRpcClient.execute(METHOD_GET_LIST_REPOSITORIES, params);
                if (result instanceof Object[]) {
                    Object[] repositories = (Object[]) result;
                    for (int i = 0; i < repositories.length; i++) {
                        fRepositoriesList.add(repositories[i]);
                    }
                }
            }

            for (Iterator<Object> iter = fRepositoriesList.iterator(); iter.hasNext();) {
                Map repoMap = (Map) iter.next();
                String repoID = (String) repoMap.get(QUERY_REPOSITORY_ID);
                int netID = ((Integer) repoMap.get(QUERY_NETWORK_ID)).intValue();
                params = new Object[] { fIdentity, netID, repoID };
                result = fXmlRpcClient.execute(METHOD_GET_LIST_CONTAINERS, params);
                if (result instanceof Object[]) {
                    Object[] containers = (Object[]) result;
                    for (int i = 0; i < containers.length; i++) {
                        fContainerList.add(containers[i]);
                    }
                }
            }
        }
        catch(Exception e) {
            fConnected = false;
            e.printStackTrace();
            MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.OpenDockHandler_0, e.getMessage());
            return false;
        }

        return true;
    }
    
    /**
     * Do a Search with the set parameters
     * 
     * @param content
     * @param author
     * @param licence
     * @param allTerms
     * @param matchExact
     * @param allFields
     * @return
     * @throws XmlRpcException
     */
    @SuppressWarnings("unchecked")
    public ArrayList<HashMap<String, String>> doSearch(UOL_SearchQueryInfo queryInfo) throws XmlRpcException {
        // Return the results in this Array
        ArrayList<HashMap<String, String>> resultLst = new ArrayList<HashMap<String, String>>();
        
        // Add identity to info
        queryInfo.identity = getIdentity();
        
        XmlRpcClient rpcClient = getXMLRPCClient();
        Object result = rpcClient.execute(METHOD_SEARCH_REPOSITORY, queryInfo.getParams());
        
        if(result instanceof HashMap) {
            Object containers = ((HashMap)result).get(QUERY_CONTAINERS);
            if(containers instanceof Object[]) {
                Object[] containerArray = (Object[])containers;
                // Container_type
                for(int i = 0; i < containerArray.length; i++) {
                    if((containerArray[i] instanceof HashMap)) {
                        HashMap container = (HashMap)containerArray[i];
                        // Only interested in UoLs, not directories or other container types
                        if(UOL.equals(container.get(QUERY_CONTAINER_TYPE))) {
                            resultLst.add(container);
                        }
                    }
                }
            }
        }
        
        return resultLst;
    }

    /**
     * Add further Item details to the Item
     * 
     * @param item
     * @throws XmlRpcException 
     */
    @SuppressWarnings("unchecked")
    public void addItemDetails(HashMap item) throws XmlRpcException {
        if(item != null && item.get("__ITEM_DETAILS__") == null) { //$NON-NLS-1$
            Hashtable<String, Object> pluginSearchParameters = new Hashtable<String, Object>();
            
            pluginSearchParameters.put("type", QUERY_CONTAINER); //$NON-NLS-1$
            
            Object obj = item.get(QUERY_NETWORK_ID);
            pluginSearchParameters.put("netid", obj == null ? "" : obj); //$NON-NLS-1$ //$NON-NLS-2$
            
            obj = item.get(QUERY_REPOSITORY_ID);
            pluginSearchParameters.put("repid", obj == null ? "" : obj); //$NON-NLS-1$ //$NON-NLS-2$
            
            obj = item.get(QUERY_CONTAINER_ID);
            pluginSearchParameters.put("cid", obj == null ? "" : obj); //$NON-NLS-1$ //$NON-NLS-2$
            
            pluginSearchParameters.put("itid", ""); //$NON-NLS-1$ //$NON-NLS-2$
            pluginSearchParameters.put("name", ""); //$NON-NLS-1$ //$NON-NLS-2$
            
            Object[] params = new Object[] { getIdentity(), pluginSearchParameters, new ArrayList() };
            
            Object result = getXMLRPCClient().execute(METHOD_GET_PLUGIN_DETAILS, params);
            
            if(result != null) {
                HashMap mapResult = (HashMap)result;
                Set keys = mapResult.keySet();
                Iterator iter = keys.iterator();
                while(iter.hasNext()) {
                    Object key = iter.next();
                    item.put(key, mapResult.get(key));
                }
            }
            
            // We'll do a little trick here by adding our own marker to indicate that this has been done
            item.put("__ITEM_DETAILS__", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    
    /**
     * Download a given item with dialog
     * @param item
     */
    public void downloadItemWithDialog(final HashMap<String, String> item) {
        
        // Ask for destination folder
        DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell(), SWT.NULL);
        dialog.setText(Messages.OpenDockHandler_1);
        String path = dialog.open();
        if(path == null) {
            return;
        }
        
        final File targetFolder = new File(path);
        
        IRunnableWithProgress operation = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                try {
                    downloadItem(item, targetFolder, monitor);
                }
                catch(Exception ex) {
                    throw new InterruptedException(ex.getMessage());
                }
            }
        };

        try {
            ProgressMonitorDialog progress = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
            progress.run(true, true, operation);
            MessageDialog.openInformation(Display.getCurrent().getActiveShell(), Messages.OpenDockHandler_2, Messages.OpenDockHandler_3);
        }
        catch(final Exception ex) {
            ex.printStackTrace();
            Display.getCurrent().asyncExec(new Runnable() {
                public void run() {
                    MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.OpenDockHandler_4, ex.getMessage());
                }
            });
            return;
        }
        
        // Add entry to Organiser
        File manifestFile = new File(targetFolder, "imsmanifest.xml"); //$NON-NLS-1$
        String title = StringUtils.safeString(item.get(QUERY_META_TITLE)) + " " + Messages.OpenDockHandler_5; //$NON-NLS-1$
        IOrganiserLD organiserEntry = OrganiserModelFactory.createOrganiserLD(title, manifestFile);
        OrganiserModelFactory.getOrganiserIndex().addChild(organiserEntry);

        // Open the Editor
        EditorManager.openLDEditor(organiserEntry.getName(), organiserEntry.getFile());
    }
    
    /**
     * Download an item as a zip file, unzip to target folder
     * @throws XmlRpcException 
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
    public void downloadItem(HashMap<String, String> item, File targetFolder, IProgressMonitor monitor) throws XmlRpcException, IOException {
        XmlRpcClient rpcClient = getXMLRPCClient();
        Hashtable<String, String> identity = getIdentity();

        int netID = Integer.parseInt(item.get(QUERY_NETWORK_ID));
        String repoID = item.get(QUERY_REPOSITORY_ID);
        String contID = item.get(QUERY_CONTAINER_ID);
        String itemID = item.get(QUERY_ITEM_ID);

        Object[] params = new Object[] { identity, netID, repoID, contID, itemID };

        Object result = rpcClient.execute(METHOD_DOWNLOAD_ELEMENT, params);
        
        if(monitor != null && monitor.isCanceled()) {
            return;
        }

        if(result instanceof HashMap) {
            Object data = ((HashMap)result).get("data"); //$NON-NLS-1$
            
            if(data == null) {
                return;
            }

            // Download to temp file
            File tmpFile = File.createTempFile("odn", ".zip"); //$NON-NLS-1$ //$NON-NLS-2$
            tmpFile.deleteOnExit();

            FileOutputStream fout = new FileOutputStream(tmpFile);
            fout.write(((byte[])data), 0, ((byte[])data).length);
            fout.close();
            
            // Unzip to target folder
            ZipUtils.unpackZip(tmpFile, targetFolder);
        }
    }

    /**
     * Load stored network details from Preferences
     */
    private void loadPreferences() {
        IPreferenceStore store = OpenDockPlugin.getDefault().getPreferenceStore();

        fOpenDockURL = store.getString(PREFS_OPENDOCK_REPOSITORY_URL);
        fOpenDockRepositoryID = store.getString(PREFS_OPENDOCK_REPOSITORY_ID);
        fOpenDockAppID = store.getString(PREFS_OPENDOCK_APP_ID);
        fOpenDockUserID = store.getString(PREFS_OPENDOCK_USER_ID);

        // Check there are valid settings
        fInitialised = StringUtils.isSet(fOpenDockURL) &&
            StringUtils.isSet(fOpenDockAppID) && StringUtils.isSet(fOpenDockUserID);
    }

    /**
     * 
     * @return
     */
    public Hashtable<String, String> getIdentity(){
        return fIdentity;
    }

    private String getMD5Cipher(byte[] defaultBytes) {
        MessageDigest algorithm = getMD5Algorithm();
        algorithm.reset();
        algorithm.update(defaultBytes);

        byte messageDigest[] = algorithm.digest();

        StringBuffer hexString = new StringBuffer();
        
        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    private MessageDigest getMD5Algorithm(){
        if(fMessageDigest == null){
            try{
                fMessageDigest = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
            }
            catch(NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        }
        return fMessageDigest;
    }

    public XmlRpcClient getXMLRPCClient() {
        return fXmlRpcClient;
    }

    public List<Object> getContainerLst() {
        return fContainerList;
    }

    public List<Object> getRepositoriesList() {
        return fRepositoriesList;
    }

    public boolean isConnected() {
        return fConnected;
    }

    public void setConnected(boolean connected) {
        fConnected = connected;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent event) {
        // If user changes these Prefs, then reload them
        if(event.getProperty().startsWith(IOpenDockPreferenceConstants.PREFS_OPENDOCK)) {
            setConnected(false);
        }
    }

    
    // ==============================================================================================

    public void uploadUOLWithWizard() {
        final UOL_UploadInfo uploadInfo = new UOL_UploadInfo();
        
        UploadZipWizard wizard = new UploadZipWizard(uploadInfo);
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
        if(dialog.open() != Window.OK) {
            return;
        }
        
        if(uploadInfo.file == null || !uploadInfo.file.exists()) {
            return;
        }
        
        // Add extra info
        uploadInfo.identity = getIdentity();
        uploadInfo.repositoryID = fOpenDockRepositoryID;
        
        IRunnableWithProgress operation = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                try {
                    XmlRpcClient rpcClient = getXMLRPCClient();
                    rpcClient.execute(METHOD_UPLOAD_ELEMENT, uploadInfo.getParams());
                }
                catch(Exception ex) {
                    throw new InterruptedException(ex.getMessage());
                }
            }
        };

        try {
            ProgressMonitorDialog progress = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
            progress.run(true, true, operation);
            MessageDialog.openInformation(Display.getCurrent().getActiveShell(), Messages.OpenDockHandler_6, Messages.OpenDockHandler_7);
        }
        catch(final Exception ex) {
            ex.printStackTrace();
            Display.getCurrent().asyncExec(new Runnable() {
                public void run() {
                    MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.OpenDockHandler_8, ex.getMessage());
                }
            });
        }
    }
 }
