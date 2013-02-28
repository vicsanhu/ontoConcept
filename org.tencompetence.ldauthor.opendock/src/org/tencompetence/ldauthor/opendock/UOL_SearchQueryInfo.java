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

import java.util.ArrayList;
import java.util.Hashtable;


/**
 * Info structure for querying a UoL
 * 
 * @author Phillip Beauvoir
 * @version $Id: UOL_SearchQueryInfo.java,v 1.3 2008/04/25 11:46:13 phillipus Exp $
 */
public class UOL_SearchQueryInfo implements IOpenDockConstants {
    
    int netID = 1;  // Default network

    Hashtable<String, String> identity;
    
    // TODO - Page requests of 50 at a time
    public int maxResults = 1000;  // max number of results
    public int maxDisplay = 1000;  // max to display in page
    public int displayOffset = 0;     // offset
    
    public boolean matchAllTerms = false;
    public boolean matchExact = false;
    public boolean matchAllFields = false;
    
    public String content = ""; //$NON-NLS-1$
    public String author = ""; //$NON-NLS-1$
    public String licence = ""; //$NON-NLS-1$
    
    public UOL_SearchQueryInfo() {
    }
    
    public Object[] getParams() {
        if(identity == null) {
            throw new RuntimeException("Identity cannot be null, must be set."); //$NON-NLS-1$
        }
        
        Hashtable<String, String> param4 = new Hashtable<String, String>();
        param4.put(PARAM_CONTENT, content);
        param4.put(PARAM_AUTHOR, author);
        param4.put(PARAM_LICENSE, licence);

        Object param5 = new ArrayList<Object>();

        return new Object[] { identity, netID, "", "", param4, param5, maxResults, maxDisplay, displayOffset, matchAllTerms, matchExact, matchAllFields }; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
