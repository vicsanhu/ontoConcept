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
import org.tencompetence.ldauthor.fedora.model.ICategory;


/**
 * ResourceCategory
 * 
 * @author Phillip Beauvoir
 * @version $Id: Category.java,v 1.1 2008/10/07 11:56:45 phillipus Exp $
 */
public class Category extends AbstractFedoraObject
implements ICategory {

    private String fHref = ""; //$NON-NLS-1$
    private String fTitle = ""; //$NON-NLS-1$
    private String fType = CATEGORY;

    public String getHref() {
        return fHref;
    }

    public String getTitle() {
        return fTitle;
    }

    public String getType() {
        return fType;
    }

    public void setHref(String href) {
        fHref = href;
    }

    public void setTitle(String title) {
        fTitle = title;
    }

    public void setType(String type) {
        fType = type;
    }

    public void fromJDOM(Element element) {
        if(element == null) {
            return;
        }
        
        Element link = element.getChild(LINK);
        if(link != null) {
            fHref = link.getAttributeValue(HREF);
            fType = link.getAttributeValue(TYPE);
        }
        Element title = element.getChild(TITLE);
        if(title != null) {
            fTitle = title.getValue();
        }
    }

    public String getTagName() {
        return CATEGORY;
    }

    public Element toJDOM() {
        Element root = new Element(getTagName());
        
        Element link = new Element(LINK);
        root.addContent(link);
        link.setAttribute(TYPE, fType);
        link.setAttribute(HREF, fHref);
        
        Element title = new Element(TITLE);
        root.addContent(title);
        title.setText(fTitle);
        
        return root;
    }
}
