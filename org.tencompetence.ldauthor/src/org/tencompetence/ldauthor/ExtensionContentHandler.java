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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;


/**
 * Extension Content Handler
 * 
 * @author Phillip Beauvoir
 * @version $Id: ExtensionContentHandler.java,v 1.2 2009/03/27 16:07:51 phillipus Exp $
 */
public final class ExtensionContentHandler {

    public static String CONTENT_HANDLER_EXTENSION_ID = LDAuthorPlugin.PLUGIN_ID + ".contenthandler"; //$NON-NLS-1$
    
    private static ExtensionContentHandler fInstance;
    
    private List<ILDContentHandler> fHandlers = new ArrayList<ILDContentHandler>();
    
    public static ExtensionContentHandler getInstance() {
        if(fInstance == null) {
            fInstance = new ExtensionContentHandler();
        }
        return fInstance;
    }
    
    public List<ILDContentHandler> getHandlers() {
        if(fHandlers.isEmpty()) {
            IExtensionRegistry registry = Platform.getExtensionRegistry();
            IExtensionPoint extensionPoint = registry.getExtensionPoint(CONTENT_HANDLER_EXTENSION_ID);
            
            if(extensionPoint == null) {
                return fHandlers;
            }
            
            IExtension[] extensions = extensionPoint.getExtensions();
            for(IExtension extension : extensions) {
                IConfigurationElement[] elements = extension.getConfigurationElements();
                for(IConfigurationElement configurationElement : elements) {
                    try {
                        ILDContentHandler handler = (ILDContentHandler)configurationElement.createExecutableExtension("class");//$NON-NLS-1$
                        fHandlers.add(handler);
                    } 
                    catch(CoreException ex) {
                        ex.printStackTrace();
                    } 
                }
            }
        }
        
        return fHandlers;
    }
    
    /**
     * @param file
     * @return A handler to handle a certain file type, or null
     */
    public ILDContentHandler getHandler(File file) {
        if(file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        
        for(ILDContentHandler handler : getHandlers()) {
            if(handler.isHandler(file)) {
                return handler;
            }
        }
        
        return null;
    }
    
    /**
     * @return All (if any) Handler types
     */
    public String[] getHandlerTypes() {
        List<ILDContentHandler> handlers = getHandlers();
        
        String[] s = new String[handlers.size()];
        
        for(int i = 0; i < s.length; i++) {
            s[i] = handlers.get(i).getType();
        }
        
        return s;
    }
}
