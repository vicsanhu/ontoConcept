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
package org.tencompetence.ldauthor.utils;


/**
 * Some useful Browser Utilities
 *
 * @author Phillip Beauvoir
 * @version $Id: BrowserUtils.java,v 1.5 2009/07/22 15:29:02 phillipus Exp $
 */
public final class BrowserUtils  {
    
    /**
     * List of prefixes that the Browser will display OK
     */
    public static String[] BROWSER_PREFIXES = {
        "www", //$NON-NLS-1$
        "http", //$NON-NLS-1$
    };

    /**
     * List of file extensions that the Browser will display OK in the Browser itself.
     * Other files might be launched in a separate window, which is no good for viewing.
     */
    public static String[] BROWSER_EXTENSIONS = {
        ".htm", //$NON-NLS-1$
        ".html", //$NON-NLS-1$
        ".xml", //$NON-NLS-1$
        ".xhtml", //$NON-NLS-1$
        ".gif", //$NON-NLS-1$
        ".png", //$NON-NLS-1$
        ".jpg", //$NON-NLS-1$
        ".txt", //$NON-NLS-1$
        //".pdf", //$NON-NLS-1$
        ".swf" //$NON-NLS-1$
    };
	
    /**
     * @param url
     * @return True if url is OK to display in a Browser component and not open up an external application
     */
    public static boolean isBrowserURL(String url) {
        if(url == null) {
            return false;
        }
        
        url = url.toLowerCase();
        
        for(int i = 0; i < BROWSER_PREFIXES.length; i++) {
            if(url.startsWith(BROWSER_PREFIXES[i])) {
                return true;
            }
        }
        
        for(int i = 0; i < BROWSER_EXTENSIONS.length; i++) {
            if(url.endsWith(BROWSER_EXTENSIONS[i])) {
                return true;
            }
        }
        
        return false;
    }
    

}

