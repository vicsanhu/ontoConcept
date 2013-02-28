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
package org.tencompetence.ldauthor.ui.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.tencompetence.ldauthor.utils.FileUtils;


/**
 * Sub-class the Browser component
 * 
 * @author Phillip Beauvoir
 * @version $Id: ExtendedBrowser.java,v 1.3 2009/06/30 18:03:29 phillipus Exp $
 */
public class ExtendedBrowser extends Browser {
    
    private String fRealURL;

    public ExtendedBrowser(Composite parent, int style) {
        super(parent, style);
    }

    @Override
    protected void checkSubclass () {
        // Over-ride to support this
    }
    
    @Override
    public boolean setUrl(String url) {
        fRealURL = url;
        
        // Windows uses IE and this does not display HTML content if the file extension is .xml
        if(isXHTML_Windows(url)) {
            return displayTempXHTMLFile(url);
        }
        
        return super.setUrl(url);
    }
    
    @Override
    public void refresh() {
        if(isXHTML_Windows(fRealURL)) {
            displayTempXHTMLFile(fRealURL);
        }
        else {
            super.refresh();
        }
    }
    
    /**
     * Display a temp xml file renamed to.html extension.
     * This is because IE renders by extension, not content.
     * @param url
     * @return
     */
    private boolean displayTempXHTMLFile(String url) {
        try {
            // make a temp copy of the file
            URL u = new URL(url);
            File f = new File(u.getFile());
            final File tmp = new File(f.getParent(), ".~tmp.html"); //$NON-NLS-1$
            FileUtils.copyFile(f, tmp, false);
            
            // Load it
            boolean result = super.setUrl(tmp.getAbsolutePath());
            
            // Delete it
            Display.getCurrent().asyncExec(new Runnable() {
                public void run() {
                    tmp.deleteOnExit(); // Just in case delete() fails
                    tmp.delete();
                }
            });

            return result;
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Windows uses IE and this does not display HTML content if the file extension is .xml or .xhtml
     * @param url
     * @return
     */
    private boolean isXHTML_Windows(String url) {
        if(Platform.getOS().equals(Platform.OS_WIN32) && url != null) {
            String url1 = url.toLowerCase();
            if(url1.startsWith("file:/") && (url1.endsWith(".xml") || url1.endsWith(".xhtml"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                try {
                    URL u = new URL(url1);
                    Object o = u.getContent();
                    if(o instanceof InputStream) {
                        String s = slurp((InputStream)o);
                        return s.indexOf("<html") > -1 || s.indexOf("<HTML") > -1;  //$NON-NLS-1$//$NON-NLS-2$
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        return false;
    }
    
    private String slurp(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for(int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        in.close();
        return out.toString();
    }
}
