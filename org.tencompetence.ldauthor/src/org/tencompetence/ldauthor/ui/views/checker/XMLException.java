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
package org.tencompetence.ldauthor.ui.views.checker;

import org.xml.sax.SAXException;


/**
 * XMLException
 * 
 * @author Phillip Beauvoir
 * @version $Id: XMLException.java,v 1.2 2009/06/29 19:17:53 phillipus Exp $
 */
public class XMLException {
    
    public static final int ERROR = 0;
    public static final int FATAL_ERROR = 1;
    public static final int WARNING = 2;
    
    private int level;
    private SAXException saxException;

    public XMLException(int level, SAXException saxException) {
        this.level = level;
        this.saxException = saxException;
    }
    
    public int getLevel() {
        return level;
    }
    
    public SAXException getSAXException() {
        return saxException;
    }
    
    public String getType() {
        switch(level) {
            case ERROR:
                return Messages.XMLException_0;

            case FATAL_ERROR:
                return Messages.XMLException_1;
                
            case WARNING:
                return Messages.XMLException_2;
        }
        
        return ""; //$NON-NLS-1$
    }
}
