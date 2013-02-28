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

import java.util.UUID;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.internal.StringUtils;
import org.tencompetence.imsldmodel.properties.IDataType;
import org.tencompetence.imsldmodel.properties.IRestrictionType;


/**
 * Abstract Local Property Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractLocalPropertyModel.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public abstract class AbstractLocalPropertyModel extends AbstractPropertyModel {
    
    private String fID;

    public AbstractLocalPropertyModel(ILDModel model) {
        super(model);
    }
    
    public String getIdentifier() {
        if(fID == null) {
            fID = "locprop-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public void setIdentifier(String id) {
        fID = id;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        element.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());
        
        // Title
        if(StringUtils.isSet(getTitle())) {
            Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
            title.setText(getTitle());
            element.addContent(title);
        }
        
        // Data Type - required
        Element dataType = getDataType().toJDOM();
        element.addContent(dataType);
        
        // Initial Value
        if(StringUtils.isSet(getInitialValue())) {
            Element initValue = new Element(LDModelFactory.INITIAL_VALUE, IMSLD_NAMESPACE_100_EMBEDDED);
            initValue.setText(getInitialValue());
            element.addContent(initValue);
        }
        
        // Restrictions
        for(IRestrictionType restrictionType : getRestrictionTypes()) {
            Element restriction = restrictionType.toJDOM();
            element.addContent(restriction);
        }
        
        return element;
    }

    public void fromJDOM(Element element) {
        fID = element.getAttributeValue(LDModelFactory.IDENTIFIER);
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.TITLE)) {
                setTitle(child.getText());
            }
            
            else if(tag.equals(LDModelFactory.DATATYPE)) {
                IDataType dataType = new DataType(this);
                dataType.fromJDOM(child);
                setDataType(dataType);
            }
            
            else if(tag.equals(LDModelFactory.INITIAL_VALUE)) {
                setInitialValue(child.getText());
            }
            
            else if(tag.equals(LDModelFactory.RESTRICTION)) {
                IRestrictionType restriction = new RestrictionType(getLDModel());
                restriction.fromJDOM(child);
                getRestrictionTypes().add(restriction);
            }
        }
    }
}
