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
package org.tencompetence.ldauthor.fedora.model.impl;

import org.jdom.Element;
import org.tencompetence.ldauthor.fedora.model.IComments;


/**
 * Comments
 * 
 * @author Phillip Beauvoir
 * @version $Id: Comments.java,v 1.1 2008/10/07 11:56:45 phillipus Exp $
 */
public class Comments extends AbstractFedoraObject
implements IComments {
    
    String fCount = ""; //$NON-NLS-1$

    public String getCount() {
        return fCount;
    }

    public void setCount(String value) {
        fCount = value;
    }

    public void fromJDOM(Element element) {
        if(element == null) {
            return;
        }
        
        Element count = element.getChild(COUNT);
        if(count != null) {
            fCount = count.getValue();
        }
    }

    public String getTagName() {
        return COMMENTS;
    }

    public Element toJDOM() {
        Element root = new Element(getTagName());
        
        Element count = new Element(COUNT);
        root.addContent(count);
        count.setText(fCount);
        
        return root;
    }
}
