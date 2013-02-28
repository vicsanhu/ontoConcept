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
package org.tencompetence.ldauthor;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDAuthorPlugin.java,v 1.20 2009/06/03 17:07:31 phillipus Exp $
 */
public class LDAuthorPlugin
extends AbstractUIPlugin {
    /**
     * ID of the plug-in
     */
    public static String PLUGIN_ID = "org.tencompetence.ldauthor"; //$NON-NLS-1$

    /**
	 * The File location of this plugin folder
	 */
	private static File fPluginFolder;

    /**
     * The shared instance
     */
    private static LDAuthorPlugin fInstance;

    /**
     * The constructor.
     */
    public LDAuthorPlugin() {
        fInstance = this;
    }

    /**
     * This method is called upon plug-in activation
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
     * This method is called when the plug-in is stopped
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        // super must be *last*
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     */
    public static LDAuthorPlugin getDefault() {
        return fInstance;
    }

    /**
     * @return The User data folder
     */ 
    public File getUserDataFolder() {
        String path = getPreferenceStore().getString(ILDAuthorPreferenceConstants.PREFS_USER_DATA_FOLDER);
        return new File(path);
    }
    
    /**
     * @return The assets folder
     */
    public File getAssetsFolder() {
        return new File(getPluginFolder(), "assets"); //$NON-NLS-1$
    }
    
    /**
     * @return The schema folder
     */
    public File getSchemaFolder() {
        return new File(getAssetsFolder(), "schema"); //$NON-NLS-1$
    }
    
    /**
     * @return The Templates folder
     */
    public File getTemplatesFolder() {
        return new File(getAssetsFolder(), "templates"); //$NON-NLS-1$
    }
    
    /**
     * @return The Workspace folder
     */
    public File getWorkspaceFolder() {
        /*
         * Get Data Folder.  Try for one set by a user system property first, otherwise
         * use the workbench instance data location
         */
        String strFolder = System.getProperty("org.tencompetence.ldauthor.workspaceFolder"); //$NON-NLS-1$
        if(strFolder != null) {
            return new File(strFolder);
        }
        
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc == null) {
            Logger.logWarning("Instance Location is null. Using user.home"); //$NON-NLS-1$
            return new File(System.getProperty("user.home")); //$NON-NLS-1$
        }
        else {
            URL url = instanceLoc.getURL();
            if(url != null) {
                return new File(url.getPath());
            }
            else {
                return new File(System.getProperty("user.home")); //$NON-NLS-1$
            }
        }
    }
    
    /**
	 * @return The File Location of this plugin
	 */
	public File getPluginFolder() {
        if(fPluginFolder == null) {
            URL url = getBundle().getEntry("/"); //$NON-NLS-1$
            try {
                url = FileLocator.resolve(url);
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
            fPluginFolder = new File(url.getPath());
        }
        
        return fPluginFolder;
	}
    
    /**
     * @param key A string key beginning with "%"
     * @return A Resource String from the plugin.properties file
     */
    public String getResourceString(String key) {
        return Platform.getResourceString(Platform.getBundle(PLUGIN_ID), key);
    }
    
    /**
     * @return The plugin id
     */
    public String getId(){
        return getBundle().getSymbolicName();
    }
}