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
package org.tencompetence.imsldmodel.expressions.impl;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IExpressionType;
import org.tencompetence.imsldmodel.expressions.ILangStringType;
import org.tencompetence.imsldmodel.expressions.IPropertyValueType;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.imsldmodel.properties.impl.PropertyRefModel;


/**
 * PropertyValueType
 * 
 * @author Phillip Beauvoir
 * @version $Id: PropertyValueType.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class PropertyValueType implements IPropertyValueType {
    
    private ILDModel fLDModel;
    
    private int fChoice = CHOICE_NONE;
    
    private String fValue = ""; //$NON-NLS-1$
    
    private IExpressionType fCalculateType;
    
    private ILangStringType fLangStringType;
    
    private ILDModelObjectReference fPropertyRef;

    /**
     * Constructor
     * @param model
     */
    public PropertyValueType(ILDModel model) {
        fLDModel = model;
        fPropertyRef = new PropertyRefModel(model);
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    public int getChoice() {
        return fChoice;
    }

    public void setChoice(int choice) {
        fChoice = choice;
        if(fChoice != CHOICE_NONE) {
            fValue = ""; //$NON-NLS-1$
        }
    }

    public String getValue() {
        return fValue;
    }

    public void setValue(String value) {
        fValue = value;
    }

    public IExpressionType getCalculate() {
        if(fCalculateType == null) {
            fCalculateType = new ExpressionCalculateType(fLDModel);
        }
        return fCalculateType;
    }

    public ILangStringType getLangStringType() {
        if(fLangStringType == null) {
            fLangStringType = new LangStringType(fLDModel);
        }
        return fLangStringType;
    }

    public ILDModelObjectReference getPropertyRef() {
        if(fPropertyRef == null) {
            fPropertyRef = new PropertyRefModel(fLDModel);
        }
        return fPropertyRef;
    }
    
    public void setPropertyRef(IPropertyTypeModel property) {
        if(property != null) {
            fPropertyRef.setReferenceIdentifer(property.getIdentifier());
        }
    } 

    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        // Value
        setValue(element.getText().trim());  // Need to trim off whitespace as we are a container element as well

        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.LANGSTRING)) {
                setChoice(CHOICE_LANGSTRING);
                getLangStringType().fromJDOM(child);
            }
            else if(tag.equals(LDModelFactory.CALCULATE)) {
                setChoice(CHOICE_CALCULATE);
                getCalculate().fromJDOM(child);
            }
            else if(tag.equals(LDModelFactory.PROPERTY_REF)) {
                setChoice(CHOICE_PROPERTY_REF);
                getPropertyRef().fromJDOM(child);
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.PROPERTY_VALUE;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        element.setText(fValue);
        
        switch(getChoice()) {
            case CHOICE_LANGSTRING:
                Element child = getLangStringType().toJDOM();
                element.addContent(child);
                break;
                
            case CHOICE_CALCULATE:
                child = getCalculate().toJDOM();
                element.addContent(child);
                break;

            case CHOICE_PROPERTY_REF:
                child = getPropertyRef().toJDOM();
                element.addContent(child);
                break;
        }
        
        return element;
    }
}
