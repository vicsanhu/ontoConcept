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
import org.tencompetence.ldauthor.fedora.model.IDC;



/**
 * FedoraDC
 * 
 * @author Phillip Beauvoir
 * @version $Id: DC.java,v 1.1 2008/10/07 11:56:45 phillipus Exp $
 */
public class DC extends AbstractFedoraObject
implements IDC {
    
    private String fTitle = ""; //$NON-NLS-1$
    private String fDescription = ""; //$NON-NLS-1$
    private String fIdentifier = ""; //$NON-NLS-1$
    private String fCreator = ""; //$NON-NLS-1$
    private String fFormat = ""; //$NON-NLS-1$
    private String fSubject = ""; //$NON-NLS-1$
    private String fType = ""; //$NON-NLS-1$
    private String fSource = ""; //$NON-NLS-1$
    private String fCoverage = ""; //$NON-NLS-1$
    private String fPublisher = ""; //$NON-NLS-1$
    private String fContributor = ""; //$NON-NLS-1$
    private String fRights = ""; //$NON-NLS-1$
    private String fRelation = ""; //$NON-NLS-1$
    private String fDate = ""; //$NON-NLS-1$
    private String fLanguage = ""; //$NON-NLS-1$



    public void setTitle(String title) {
        fTitle = title;
    }

    public String getTitle() {
        return fTitle;
    }

    public String getDescription() {
        return fDescription;
    }
    
    public void setDescription(String description) {
        fDescription = description;
    }
    
    public String getIdentifier() {
        return fIdentifier;
    }
    
    public void setIdentifier(String identifier) {
        fIdentifier = identifier;
    }

    public String getCreator() {
        return fCreator;
    }

    public void setCreator(String creator) {
        fCreator = creator;
    }
    
    public String getFormat() {
        return fFormat;
    }

    public void setFormat(String format) {
        fFormat = format;
    }
    
    public String getSubject() {
        return fSubject;
    }

    public void setSubject(String subject) {
        fSubject = subject;
    }

    public String getType() {
        return fType;
    }

    public void setType(String type) {
        fType = type;
    }

    public String getSource() {
        return fSource;
    }

    public void setSource(String source) {
        fSource = source;
    }

    public String getCoverage() {
        return fCoverage;
    }
    
    public void setCoverage(String coverage) {
        fCoverage = coverage;
    }
    
    public String getPublisher() {
        return fPublisher;
    }

    public void setPublisher(String publisher) {
        fPublisher = publisher;
    }
    
    public String getContributor() {
        return fContributor;
    }
    
    public void setContributor(String contributor) {
        fContributor = contributor;
    }

    public String getRights() {
        return fRights;
    }
    
    public void setRights(String rights) {
        fRights = rights;
    }
    
    public String getRelation() {
        return fRelation;
    }
    
    public void setRelation(String relation) {
        fRelation = relation;
    }
    
    public String getDate() {
        return fDate;
    }
    
    public void setDate(String date) {
        fDate = date;
    }
    
    public String getLanguage() {
        return fLanguage;
    }

    public void setLanguage(String language) {
        fLanguage = language;
    }

    public void fromJDOM(Element element) {
        if(element == null) {
            return;
        }
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            String value = child.getText();
            
            if(TITLE.equals(tag)) {
                fTitle = value;
            }
            else if(IDENTIFIER.equals(tag)) {
                fIdentifier = value;
            }
            else if(DESCRIPTION.equals(tag)) {
                fDescription = value;
            }
            else if(SUBJECT.equals(tag)) {
                fSubject = value;
            }
            else if(TYPE.equals(tag)) {
                fType = value;
            }
            else if(SOURCE.equals(tag)) {
                fSource = value;
            }
            else if(COVERAGE.equals(tag)) {
                fCoverage = value;
            }
            else if(CREATOR.equals(tag)) {
                fCreator = value;
            }
            else if(PUBLISHER.equals(tag)) {
                fPublisher = value;
            }
            else if(CONTRIBUTOR.equals(tag)) {
                fContributor = value;
            }
            else if(RIGHTS.equals(tag)) {
                fRights = value;
            }
            else if(RELATION.equals(tag)) {
                fRelation = value;
            }
            else if(DATE.equals(tag)) {
                fDate = value;
            }
            else if(FORMAT.equals(tag)) {
                fFormat = value;
            }
            else if(LANGUAGE.equals(tag)) {
                fLanguage = value;
            }
        }
    }

    public String getTagName() {
        return DC;
    }

    public Element toJDOM() {
        Element root = new Element(getTagName());
        
        Element child = new Element(TITLE);
        root.addContent(child);
        child.setText(fTitle);
        
        child = new Element(IDENTIFIER);
        root.addContent(child);
        child.setText(fIdentifier);

        child = new Element(DESCRIPTION);
        root.addContent(child);
        child.setText(fDescription);
        
        child = new Element(SUBJECT);
        root.addContent(child);
        child.setText(fSubject);
        
        child = new Element(TYPE);
        root.addContent(child);
        child.setText(fType);
        
        child = new Element(SOURCE);
        root.addContent(child);
        child.setText(fSource);
        
        child = new Element(COVERAGE);
        root.addContent(child);
        child.setText(fCoverage);
        
        child = new Element(CREATOR);
        root.addContent(child);
        child.setText(fCreator);
        
        child = new Element(PUBLISHER);
        root.addContent(child);
        child.setText(fPublisher);
        
        child = new Element(CONTRIBUTOR);
        root.addContent(child);
        child.setText(fContributor);
        
        child = new Element(RIGHTS);
        root.addContent(child);
        child.setText(fRights);
        
        child = new Element(RELATION);
        root.addContent(child);
        child.setText(fRelation);
        
        child = new Element(DATE);
        root.addContent(child);
        child.setText(fDate);
        
        child = new Element(FORMAT);
        root.addContent(child);
        child.setText(fFormat);
        
        child = new Element(LANGUAGE);
        root.addContent(child);
        child.setText(fLanguage);
               
        return root;
    }
}
