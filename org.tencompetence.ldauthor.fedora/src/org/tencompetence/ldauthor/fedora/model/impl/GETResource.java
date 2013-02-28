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

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.tencompetence.ldauthor.fedora.model.ICategory;
import org.tencompetence.ldauthor.fedora.model.IComments;
import org.tencompetence.ldauthor.fedora.model.IDC;
import org.tencompetence.ldauthor.fedora.model.IGETResource;
import org.tencompetence.ldauthor.fedora.model.IOwner;
import org.tencompetence.ldauthor.fedora.model.IRating;
import org.tencompetence.ldauthor.fedora.model.ITag;


/**
 * A Resource from a GET request
 * 
 * @author Phillip Beauvoir
 * @version $Id: GETResource.java,v 1.1 2008/10/07 11:56:45 phillipus Exp $
 */
public class GETResource extends AbstractFedoraObject
implements IGETResource {
    
    private String fHref = ""; //$NON-NLS-1$
    private String fType = ""; //$NON-NLS-1$
    private IDC fDC;
    private ICategory fCategory;
    private IOwner fOwner;
    private List<ITag> fTags;
    private IRating fRating;
    private String fScore = ""; //$NON-NLS-1$
    private IComments fComments;
    
    public GETResource() {
        fDC = new DC();
        fCategory = new Category();
        fOwner = new Owner();
        fTags = new ArrayList<ITag>();
        fRating = new Rating();
        fComments = new Comments();
    }

    public ICategory getCategory() {
        return fCategory;
    }

    public IComments getComments() {
        return fComments;
    }

    public IDC getDC() {
        return fDC;
    }

    public String getHref() {
        return fHref;
    }

    public IOwner getOwner() {
        return fOwner;
    }

    public IRating getRating() {
        return fRating;
    }

    public String getScore() {
        return fScore;
    }

    public List<ITag> getTags() {
        return fTags;
    }

    public String getType() {
        return fType;
    }

    public void setHref(String href) {
        fHref = href;
    }

    public void setScore(String value) {
        fScore = value;
    }

    public void setType(String type) {
        fType = type;
    }

    public void fromJDOM(Element element) {
        if(element == null) {
            return;
        }
        
        Element canonical = element.getChild(CANONICAL);
        if(canonical == null) {
            return;
        }
        
        Element link = canonical.getChild(LINK);
        if(link != null) {
            fType = link.getAttributeValue(TYPE);
            fHref = link.getAttributeValue(HREF);
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
        
        Element taglist = canonical.getChild(TAGLIST);
        if(taglist != null) {
            for(Object o : taglist.getChildren(TAG)) {
                Element child = (Element)o;
                ITag tag = new Tag();
                tag.fromJDOM(child);
                fTags.add(tag);
            }
        }
        
        Element rating = canonical.getChild(RATING);
        if(rating != null) {
            fRating.fromJDOM(rating);
        }
        
        Element score = canonical.getChild(SCORE);
        if(score != null) {
            fScore = score.getValue();
        }
        
        Element comments = canonical.getChild(COMMENTS);
        if(comments != null) {
            fComments.fromJDOM(comments);
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
        
        Element category = fCategory.toJDOM();
        canonical.addContent(category);
        
        Element owner = fOwner.toJDOM();
        canonical.addContent(owner);
        
        Element taglist = new Element(TAGLIST);
        canonical.addContent(taglist);
        for(ITag tag : fTags) {
            Element etag = tag.toJDOM();
            taglist.addContent(etag);
        }
        
        Element rating = fRating.toJDOM();
        canonical.addContent(rating);
        
        Element score = new Element(SCORE);
        canonical.addContent(score);
        score.setText(fScore);
        
        Element comments = fComments.toJDOM();
        canonical.addContent(comments);
        
        return root;
    }
}
