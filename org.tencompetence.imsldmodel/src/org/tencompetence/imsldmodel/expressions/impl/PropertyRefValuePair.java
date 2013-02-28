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

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.expressions.IPropertyRefValuePair;
import org.tencompetence.imsldmodel.expressions.IPropertyValueType;
import org.tencompetence.imsldmodel.properties.IPropertyRefModel;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.imsldmodel.properties.impl.PropertyRefModel;


/**
 * Property Ref / Value Pair
 * 
 * @author Phillip Beauvoir
 * @version $Id: PropertyRefValuePair.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class PropertyRefValuePair implements IPropertyRefValuePair {
    
    private IPropertyRefModel fPropertyRef;
    
    private IPropertyValueType fPropertyValue;
    
    public PropertyRefValuePair(ILDModel ldModel) {
        fPropertyRef = new PropertyRefModel(ldModel);
        fPropertyValue = new PropertyValueType(ldModel);
    }
    
    public IPropertyRefModel getPropertyRef() {
        return fPropertyRef;
    }

    public void setPropertyRef(IPropertyTypeModel property) {
        if(property != null) {
            fPropertyRef.setReferenceIdentifer(property.getIdentifier());
        }
    }

    public IPropertyValueType getPropertyValue() {
        return fPropertyValue;
    }
}
