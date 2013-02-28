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
package org.tencompetence.imsldmodel.activities.impl;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.ICompleteActivityType;
import org.tencompetence.imsldmodel.expressions.IWhenPropertyValueIsSetType;
import org.tencompetence.imsldmodel.expressions.impl.WhenPropertyValueIsSetType;
import org.tencompetence.imsldmodel.types.impl.AbstractCompleteType;


/**
 * Complete Activity Type
 * 
 * @author Phillip Beauvoir
 * @version $Id: CompleteActivityType.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class CompleteActivityType
extends AbstractCompleteType
implements ICompleteActivityType {
    
    
    public CompleteActivityType(ILDModelObject owner) {
        super(owner);
    }

    public void fromJDOM(Element element) {
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.USER_CHOICE)) {
                setChoice(COMPLETE_USER_CHOICE);
            }
            else if(tag.equals(LDModelFactory.TIME_LIMIT)) {
                setChoice(COMPLETE_TIME_LIMIT);
                getTimeLimitType().fromJDOM(child);
            }
            else if(tag.equals(LDModelFactory.WHEN_PROPERTY_VALUE_IS_SET)) {
                setChoice(COMPLETE_WHEN_PROPERTY_SET);
                IWhenPropertyValueIsSetType type = new WhenPropertyValueIsSetType(getOwner().getLDModel());
                type.fromJDOM(child);
                getWhenPropertyValueIsSetTypes().add(type);
            }
        }
    }

    public Element toJDOM() {
        switch(getChoice()) {
            // No choice
            case COMPLETE_NONE:
                return null;
                
            // User Choice
            case COMPLETE_USER_CHOICE:
                Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
                Element child = new Element(LDModelFactory.USER_CHOICE, IMSLD_NAMESPACE_100_EMBEDDED);
                element.addContent(child);
                return element;
                
            // Time Limit
            case COMPLETE_TIME_LIMIT:
                element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
                child = getTimeLimitType().toJDOM();
                element.addContent(child);
                return element;
                
            // When Property Value is Set
            case COMPLETE_WHEN_PROPERTY_SET:
                // None set
                if(getWhenPropertyValueIsSetTypes().isEmpty()) {
                    return null;
                }
                // Else OK
                element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
                for(IWhenPropertyValueIsSetType type : getWhenPropertyValueIsSetTypes()) {
                    Element e = type.toJDOM();
                    element.addContent(e);
                }
                return element;
            
            default:
                return null;
        }
    }
    
    public String getTagName() {
        return LDModelFactory.COMPLETE_ACTIVITY;
    }


}
