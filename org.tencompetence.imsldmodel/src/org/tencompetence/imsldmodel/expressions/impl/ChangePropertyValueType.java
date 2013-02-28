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
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IChangePropertyValueType;
import org.tencompetence.imsldmodel.expressions.IPropertyRefValuePair;


/**
 * ChangePropertyValue Type
 * This allows unbounded (with minumum of one) pairs of Property Value / Refs
 * But I think this is a mistake. I think there should only be one Property Ref/Value Pair.
 * 
 * @author Phillip Beauvoir
 * @version $Id: ChangePropertyValueType.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class ChangePropertyValueType implements IChangePropertyValueType {
    
    private ILDModel fLDModel;
    
    // This is a mistake I think
    // private List<IPropertyRefValuePair> fPropertyRefValuePairs;
    
    private IPropertyRefValuePair fPropertyRefValuePair;
    
    public ChangePropertyValueType(ILDModel ldModel) {
        fLDModel = ldModel;
        fPropertyRefValuePair = new PropertyRefValuePair(ldModel);
        //fPropertyRefValuePairs = new ArrayList<IPropertyRefValuePair>();
    }
    
    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    public IPropertyRefValuePair getPropertyRefValuePair() {
        return fPropertyRefValuePair;
    }

    // Mistake?
    //public List<IPropertyRefValuePair> getPropertyRefValuePairs() {
    //    return fPropertyRefValuePairs;
    //}
    
    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        /* Mistake?
        // Ensure we have matching pairs
        List<?> property_refs = element.getChildren(LDModelFactory.PROPERTY_REF, IMSLD_NAMESPACE_100_EMBEDDED);
        List<?> property_values = element.getChildren(LDModelFactory.PROPERTY_VALUE, IMSLD_NAMESPACE_100_EMBEDDED);
        
        if(property_refs != null && property_values != null && property_refs.size() == property_values.size()) {
            for(int i = 0; i < property_refs.size(); i++) {
                IPropertyRefValuePair refvalue = new PropertyRefValuePair(fLDModel);
                refvalue.getPropertyRef().fromJDOM((Element)property_refs.get(i));
                refvalue.getPropertyValue().fromJDOM((Element)property_values.get(i));
                fPropertyRefValuePairs.add(refvalue);
            }
        }
        */
        
        Element property_ref = element.getChild(LDModelFactory.PROPERTY_REF, IMSLD_NAMESPACE_100_EMBEDDED);
        if(property_ref != null) {
            fPropertyRefValuePair.getPropertyRef().fromJDOM(property_ref);
        }
        
        Element property_value = element.getChild(LDModelFactory.PROPERTY_VALUE, IMSLD_NAMESPACE_100_EMBEDDED);
        if(property_value != null) {
            fPropertyRefValuePair.getPropertyValue().fromJDOM(property_value);
        }
    }

    public String getTagName() {
        return LDModelFactory.CHANGE_PROPERTY_VALUE;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        /* Mistake?
        for(IPropertyRefValuePair refvalue : fPropertyRefValuePairs) {
            Element property_ref = refvalue.getPropertyRef().toJDOM();
            element.addContent(property_ref);
            
            Element property_value = refvalue.getPropertyValue().toJDOM();
            element.addContent(property_value);
        }
        */
        
        Element property_ref = fPropertyRefValuePair.getPropertyRef().toJDOM();
        element.addContent(property_ref);
        
        Element property_value = fPropertyRefValuePair.getPropertyValue().toJDOM();
        element.addContent(property_value);
        
        return element;
    }
}
