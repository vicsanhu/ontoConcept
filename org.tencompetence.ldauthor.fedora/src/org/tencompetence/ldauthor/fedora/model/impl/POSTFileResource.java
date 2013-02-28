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
import org.tencompetence.ldauthor.fedora.model.IDC;
import org.tencompetence.ldauthor.fedora.model.IOwner;
import org.tencompetence.ldauthor.fedora.model.IPOSTResource;


/**
 * Fedora File Resource to POST to server
 * 
 * @author Phillip Beauvoir
 * @version $Id: POSTFileResource.java,v 1.1 2008/10/07 11:56:45 phillipus Exp $
 */
public class POSTFileResource extends AbstractFedoraObject
implements IPOSTResource {
    
    private String fHref = ""; //$NON-NLS-1$
    private String fType = RESOURCE_FILE_TYPE;
    
    private IDC fDC;
    private ICategory fCategory;
    private IOwner fOwner;
    
    public POSTFileResource(String guid) {
        fHref = guid;
        
        fDC = new DC();
        fCategory = new Category();
        fOwner = new Owner();
    }

    public IDC getDC() {
        return fDC;
    }

    public String getHref() {
        return fHref;
    }

    public String getType() {
        return fType;
    }

    public IOwner getOwner() {
        return fOwner;
    }

    public ICategory getCategory() {
        return fCategory;
    }
    
    public void fromJDOM(Element element) {
        if(element == null) {
            return;
        }
        
        Element canonical = element.getChild(CANONICAL);
        if(canonical != null) {
            Element link = canonical.getChild(LINK);
            if(link != null) {
                fHref = link.getAttributeValue(HREF);
                fType = link.getAttributeValue(TYPE);
            }
            
            Element dc = canonical.getChild(DC);
            if(dc != null) {
                fDC.fromJDOM(dc);
            }
            
            Element category = canonical.getChild(CATEGORY);
            if(category != null) {
                fCategory.fromJDOM(category);
            }

            Element owner = canonical.getChild(OWNER);
            if(owner != null) {
                fOwner.fromJDOM(owner);
            }
        }
     
    }

    public String getTagName() {
        return RESOURCE;
    }

    public Element toJDOM() {
        Element root = new Element(getTagName());
        
        Element canonical = new Element(CANONICAL);
        root.addContent(canonical);
        
        Element link = new Element(LINK);
        canonical.addContent(link);
        link.setAttribute(TYPE, fType);
        link.setAttribute(HREF, fHref);
        
        Element dc = fDC.toJDOM();
        canonical.addContent(dc);
        
        // TODO - Category
        // Element category = fCategory.toJDOM();
        // canonical.addContent(category);
        
        Element owner = fOwner.toJDOM();
        canonical.addContent(owner);
        
        return root;
    }
}
