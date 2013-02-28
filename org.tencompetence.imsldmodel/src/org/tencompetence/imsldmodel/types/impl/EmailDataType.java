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
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.properties.IGlobalPropertyTypeModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.roles.impl.RoleRefModel;
import org.tencompetence.imsldmodel.types.IEmailDataType;


/**
 * EmailDataType
 * 
 * @author Phillip Beauvoir
 * @version $Id: EmailDataType.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class EmailDataType implements IEmailDataType {
    
    private ILDModel fLDModel;
    
    /**
     * Reference to a Role
     */
    private ILDModelObjectReference fRoleRef;
    
    /**
     * Level B property ref for email address
     */
    private String fEmailPropertyRef;

    /**
     * Level B property ref for user name
     */
    private String fUserNamePropertyRef;

    /**
     * Constructor
     * @param ldModel
     */
    public EmailDataType(ILDModel ldModel) {
        fLDModel = ldModel;
        fRoleRef = new RoleRefModel(ldModel);
    }
    
    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    public IRoleModel getRole() {
        return (IRoleModel)fRoleRef.getLDModelObject();
    }
    
    public void setRole(IRoleModel role) {
        if(role != null) {
            fRoleRef.setReferenceIdentifer(role.getIdentifier());
        }
    }
     
    public IGlobalPropertyTypeModel getEmailPropertyRef() {
        if(fEmailPropertyRef != null) {
            return (IGlobalPropertyTypeModel)fLDModel.getModelObject(fEmailPropertyRef);
        }
        return null;
    }
    
    public void setEmailPropertyRef(IGlobalPropertyTypeModel property) {
        if(property != null) {
            fEmailPropertyRef = property.getIdentifier();
        }
        else {
            fEmailPropertyRef = null;
        }
    }
    
    public IGlobalPropertyTypeModel getUserNamePropertyRef() {
        if(fUserNamePropertyRef != null) {
            return (IGlobalPropertyTypeModel)fLDModel.getModelObject(fUserNamePropertyRef);
        }
        return null;
    }
    
    public void setUsernamePropertyRef(IGlobalPropertyTypeModel property) {
        if(property != null) {
            fUserNamePropertyRef = property.getIdentifier();
        }
        else {
            fUserNamePropertyRef = null;
        }
    }

    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        fEmailPropertyRef = element.getAttributeValue(LDModelFactory.EMAIL_PROPERTY_REF);
        
        fUserNamePropertyRef = element.getAttributeValue(LDModelFactory.USERNAME_PROPERTY_REF);
        
        Element rolerefElement = element.getChild(LDModelFactory.ROLE_REF, IMSLD_NAMESPACE_100_EMBEDDED);
        if(rolerefElement != null) {
            fRoleRef.fromJDOM(rolerefElement);
        }
    }

    public String getTagName() {
        return LDModelFactory.EMAIL_DATA;
    }

    public Element toJDOM() {
        Element emaildata = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        IGlobalPropertyTypeModel property1 = getEmailPropertyRef();
        if(property1 != null) {
            emaildata.setAttribute(LDModelFactory.EMAIL_PROPERTY_REF, property1.getIdentifier());
        }
        
        IGlobalPropertyTypeModel property2 = getEmailPropertyRef();
        if(property2 != null) {
            emaildata.setAttribute(LDModelFactory.USERNAME_PROPERTY_REF, property2.getIdentifier());
        }
        
        Element roleref = fRoleRef.toJDOM();
        emaildata.addContent(roleref);
        
        return emaildata;
    }
}
