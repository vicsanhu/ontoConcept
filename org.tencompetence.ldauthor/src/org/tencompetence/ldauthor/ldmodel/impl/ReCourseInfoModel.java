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

import java.util.Date;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.IMSNamespaces;
import org.tencompetence.ldauthor.graphicsmodel.IImageBank;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalEnvironmentsModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalEnvironmentsModel;
import org.tencompetence.ldauthor.graphicsmodel.impl.ImageBank;
import org.tencompetence.ldauthor.ldmodel.IOverviewModel;
import org.tencompetence.ldauthor.ldmodel.IReCourseInfoModel;
import org.tencompetence.ldauthor.serialization.ILDAuthorNamespaces;


/**
 * Model representing the additional ReCourse Info
 * 
 * @author Phillip Beauvoir
 * @version $Id: ReCourseInfoModel.java,v 1.2 2009/05/19 18:21:05 phillipus Exp $
 */
public class ReCourseInfoModel
implements IReCourseInfoModel, IMSNamespaces, ILDAuthorNamespaces
{
    
    private IOverviewModel fOverview;
    
    private IGraphicalEnvironmentsModel fEnvironmentsModel;
    
    private Date fDateCreated;
    
    private IImageBank fImageBank;
    
    public ReCourseInfoModel(ILDModel ldModel) {
        fImageBank = new ImageBank(ldModel);
        fOverview = new OverviewModel(ldModel);
        fEnvironmentsModel = new GraphicalEnvironmentsModel(ldModel);
    }

    public IOverviewModel getOverviewModel() {
        return fOverview;
    }

    public IGraphicalEnvironmentsModel getGraphicalEnvironmentsModel() {
        return fEnvironmentsModel;
    }

    public Date getDateCreated() {
        if(fDateCreated == null) {
            fDateCreated = new Date();
        }
        return fDateCreated;
    }
    
    public void setDateCreated(Date date) {
        fDateCreated = date;
    }
    
    public IImageBank getImageBank() {
        return fImageBank;
    }


    // ================================== XML JDOM PERSISTENCE =================================
    
    public Element toJDOM() {
        Element element = new Element(getTagName(), LDAUTHOR_NAMESPACE_EMBEDDED);
        
        // Date created
        element.setAttribute(DATE_CREATED_ELEMENT, String.valueOf(getDateCreated().getTime()));
        
        // Image Bank
        Element child = fImageBank.toJDOM();
        element.addContent(child);
        
        // Overview
        child = fOverview.toJDOM();
        element.addContent(child);
        
        // Environments
        child = fEnvironmentsModel.toJDOM();
        element.addContent(child);

        return element;
    }
    
    public void fromJDOM(Element element) {
        // Date created
        Attribute att = element.getAttribute(DATE_CREATED_ELEMENT);
        if(att != null) {
            try {
                Long dateValue = att.getLongValue();
                fDateCreated = new Date(dateValue);
            }
            catch(DataConversionException ex) {
                ex.printStackTrace();
            }
        }
        
        for(Object child : element.getChildren()) {
            String tag = ((Element)child).getName();
            
            // Image Bank
            if(tag.equals(fImageBank.getTagName())) {
                fImageBank.fromJDOM((Element)child);
            }
            // Overview
            if(tag.equals(fOverview.getTagName())) {
                fOverview.fromJDOM((Element)child);
            }
            // Environments
            else if(tag.equals(fEnvironmentsModel.getTagName())) {
                fEnvironmentsModel.fromJDOM((Element)child);
            }
        }
    }

    public String getTagName() {
        return ROOT_ELEMENT;
    }
    
    public void reconcile() {
        fEnvironmentsModel.reconcile();
    }

}
