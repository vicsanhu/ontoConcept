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
package org.tencompetence.ldauthor.ui.editors.browser;


/**
 * Editor Input for Browser Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: BrowserEditorOrganiserResourceInput.java,v 1.3 2008/04/24 10:15:29 phillipus Exp $
 */
public class BrowserEditorOrganiserResourceInput
extends BrowserEditorInput {
    
    /**
     * Name
     */
    private String fName;
    
    /**
     * Location
     */
    private String fLocation;
    
	public BrowserEditorOrganiserResourceInput() {
	}

	/**
	 * @param name
	 * @param location
	 */
	public BrowserEditorOrganiserResourceInput(String name, String location) {
	    if(location == null) {
	        throw new NullPointerException("Location was null"); //$NON-NLS-1$
	    }
	    
	    fName = name;
	    fLocation = location;
    }

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        
        if(!(obj instanceof BrowserEditorInput)) {
            return false;
        }
        
        if(fLocation == null) {
            return false;
        }
        
        return fLocation.equalsIgnoreCase(((BrowserEditorInput)obj).getURL());
	}
	
	/*
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
	    return fName;
	}
	
	/*
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
	    return fLocation == null ? "" : fLocation; //$NON-NLS-1$
	}

    @Override
    public String getURL() {
        return fLocation;
    }
}
