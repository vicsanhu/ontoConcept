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
package org.tencompetence.imsldmodel.types.impl;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.properties.ILocalPropertyModel;
import org.tencompetence.imsldmodel.types.IDurationType;
import org.tencompetence.imsldmodel.types.ITimeLimitType;


/**
 * TimeLimitType
 * 
 * @author Phillip Beauvoir
 * @version $Id: TimeLimitType.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class TimeLimitType implements ITimeLimitType {
    
    private ILDModelObject fOwner;
    
    /**
     * Duration Type
     */
    private IDurationType fDurationType;
    
    /**
     * Level B property ref
     */
    private String fPropertyRef;

    /**
     * Constructor
     * @param ldModel
     */
    public TimeLimitType(ILDModelObject owner) {
        fOwner = owner;
    }
    
    public ILDModelObject getOwner() {
        return fOwner;
    }
    
    public IDurationType getDurationType() {
        if(fDurationType == null) {
            fDurationType = new DurationType();
        }
        return fDurationType;
    }

    public ILocalPropertyModel getPropertyRef() {
        if(fPropertyRef != null) {
            return (ILocalPropertyModel)fOwner.getLDModel().getModelObject(fPropertyRef);
        }
        return null;
    }
    
    public void setPropertyRef(ILocalPropertyModel property) {
        if(property != null) {
            fPropertyRef = property.getIdentifier();
        }
        else {
            fPropertyRef = null;
        }
    }
    
    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        fPropertyRef = element.getAttributeValue(LDModelFactory.PROPERTY_REF);
        getDurationType().setDurationString(element.getText());
    }

    public String getTagName() {
        return LDModelFactory.TIME_LIMIT;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        ILocalPropertyModel property = getPropertyRef();
        if(property != null) {
            element.setAttribute(LDModelFactory.PROPERTY_REF, property.getIdentifier());
        }
        else {
            element.setText(getDurationType().getDurationString());
        }
        
        return element;
    }
}
