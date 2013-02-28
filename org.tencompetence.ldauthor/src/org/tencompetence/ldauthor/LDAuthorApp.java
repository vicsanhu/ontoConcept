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

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.utils.PlatformUtils;



/**
 * The main application class for standalone RCP operation.
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDAuthorApp.java,v 1.10 2008/06/23 21:41:45 phillipus Exp $
 */
public class LDAuthorApp
implements IApplication {

    /**
     * ID of the Application
     */
    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".app"; //$NON-NLS-1$
    
    public static File WORKSPACE_FOLDER = new File(PlatformUtils.getApplicationDataFolder(), "ReCourse"); //$NON-NLS-1$

	/**
	 * Constructor
	 */
	public LDAuthorApp() {
	}
	
	public Object start(IApplicationContext context) throws Exception {
	    /*
         * Set Workbench data location.
         * You have to set it before the Workbench starts.
         * 
	     * File.toURL() is deprecated in Java 6
	     * and File.toURI().toURL() causes %20 characters in the URL such as C:\Documents%20and%20Settings
	     */
	    setWorkbenchDataLocation(new URL("file", "", WORKSPACE_FOLDER.getPath())); //$NON-NLS-1$//$NON-NLS-2$
        
        // GO!!!!
        
	    Display display = PlatformUI.createDisplay();
	    
	    try {
	        int code = PlatformUI.createAndRunWorkbench(display, new LDAuthorWorkbenchAdvisor());
	        // Exit the application with an appropriate return code
	        return code == PlatformUI.RETURN_RESTART ? EXIT_RESTART : EXIT_OK;
	    }
	    finally {
	        if(display != null) {
	            display.dispose();
	        }
	    }
	}
	
    public void stop() {
    
    }

    /**
	 * Set the file location of the data store.<p>
	 * We want to do this for an RCP standalone app but not when this is running as a plugin
	 * becauae the location of the Workbench instance will be set already.
	 * This has to be done before the Workbench starts.
	 * Note that the launch configuration in the Eclipse IDE should have "@noDefault"
     * as the workspace for the launch configuration.
	 * 
	 * @param url A url to a folder
     * @throws IOException 
     * @throws IllegalStateException 
	 */
	public static void setWorkbenchDataLocation(URL url) throws IllegalStateException, IOException {
        /*
         * Ascertain the existing location. If it is null, we can set it.
         * When run from the Eclipse IDE, setting the workspace location in the launch configuration
         * to "@noDefault" will set it as null.
         */
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc == null) {
            Logger.logError(
                    "Instance Location is null, cannot set it in setWorkbenchDataLocation(URL)", //$NON-NLS-1$
                    null);
        }
        else if(!instanceLoc.isSet()) {
            instanceLoc.release();
            /*
             * If this is set to true, you can't run another instance
             * of the app with this workspace open
             */ 
            instanceLoc.set(url, false);  
        }
	}
}
