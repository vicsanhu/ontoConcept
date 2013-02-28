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
package org.tencompetence.imsldmodel.properties.impl;

import java.util.ArrayList;
import java.util.List;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.properties.IDataType;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.imsldmodel.properties.IRestrictionType;


/**
 * Properties Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractPropertyModel.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public abstract class AbstractPropertyModel implements IPropertyTypeModel {
    
    private ILDModel fLDModel;
    
    protected IDataType fDataType = new DataType(this);
    
    private String fInitialValue = ""; //$NON-NLS-1$
    
    private List<IRestrictionType> fRestrictionTypes = new ArrayList<IRestrictionType>();

    public AbstractPropertyModel(ILDModel model) {
        fLDModel = model;
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    public IDataType getDataType() {
        return fDataType;
    }
    
    public void setDataType(IDataType type) {
        fDataType = type;
    }
    
    public List<IRestrictionType> getRestrictionTypes() {
        return fRestrictionTypes;
    }
    
    public String getInitialValue() {
        return fInitialValue;
    }
    
    public void setInitialValue(String value) {
        String old = fInitialValue;
        fInitialValue = value;
        getLDModel().firePropertyChange(this, PROPERTY_INITIAL_VALUE, old, value);
    }

}
