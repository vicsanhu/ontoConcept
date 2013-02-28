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
package org.tencompetence.ldauthor.ldmodel.impl;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.IMSNamespaces;
import org.tencompetence.ldauthor.ldmodel.IOverviewModel;
import org.tencompetence.ldauthor.ldmodel.IReCourseInfoModel;
import org.tencompetence.ldauthor.serialization.ILDAuthorNamespaces;


public class OverviewModel
implements IOverviewModel, IMSNamespaces, ILDAuthorNamespaces {
    
    private String fDescription = ""; //$NON-NLS-1$
    private String fTags = ""; //$NON-NLS-1$;
    private String fAuthor = ""; //$NON-NLS-1$;
    private String fSubject = ""; //$NON-NLS-1$;

    public OverviewModel(ILDModel model) {
        
    }

    public String getDescription() {
        return fDescription;
    }

    public String getTags() {
        return fTags;
    }

    public void setDescription(String description) {
        fDescription = description;
    }

    public void setTags(String tags) {
        fTags = tags;
    }
    
    public String getAuthor() {
        return fAuthor;
    }

    public String getSubject() {
        return fSubject;
    }

    public void setAuthor(String author) {
        fAuthor = author;
    }

    public void setSubject(String subject) {
        fSubject = subject;
    }


    // ================================== XML JDOM PERSISTENCE =================================

    public void fromJDOM(Element element) {
        for(Object child : element.getChildren()) {
            String tag = ((Element)child).getName();
            String value = ((Element)child).getText();
            
            if(tag.equals(DESCRIPTION)) {
                fDescription = value;
            }
            else if(tag.equals(TAGS)) {
                fTags = value;
            }
            else if(tag.equals(AUTHOR)) {
                fAuthor = value;
            }
            else if(tag.equals(SUBJECT)) {
                fSubject = value;
            }
        }
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), LDAUTHOR_NAMESPACE_EMBEDDED);
        
        Element child = new Element(DESCRIPTION, LDAUTHOR_NAMESPACE_EMBEDDED);
        child.setText(fDescription);
        element.addContent(child);
        
        child = new Element(TAGS, LDAUTHOR_NAMESPACE_EMBEDDED);
        child.setText(fTags);
        element.addContent(child);
        
        child = new Element(AUTHOR, LDAUTHOR_NAMESPACE_EMBEDDED);
        child.setText(fAuthor);
        element.addContent(child);

        child = new Element(SUBJECT, LDAUTHOR_NAMESPACE_EMBEDDED);
        child.setText(fSubject);
        element.addContent(child);

        return element;
    }

    public String getTagName() {
        return IReCourseInfoModel.OVERVIEW_ELEMENT;
    }
}
