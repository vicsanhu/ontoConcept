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
import org.tencompetence.imsldmodel.expressions.IWhenConditionTrueType;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.roles.impl.RoleRefModel;


/**
 * WhenConditionTrueType Type
 * 
 * @author Phillip Beauvoir
 * @version $Id: WhenConditionTrueType.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class WhenConditionTrueType implements IWhenConditionTrueType {
    
    private ILDModel fLDModel;
    
    private ILDModelObjectReference fRoleRef;
    
    private IExpressionType fExpression;
    
    /**
     * Constructor
     * @param model
     */
    public WhenConditionTrueType(ILDModel model) {
        fLDModel = model;
        fRoleRef = new RoleRefModel(model);
        fExpression = new ExpressionType(fLDModel);
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    public IRoleModel getRoleRef() {
        return (IRoleModel)fRoleRef.getLDModelObject();
    }
    
    public void setRoleRef(IRoleModel role) {
        if(role != null) {
            fRoleRef.setReferenceIdentifer(role.getIdentifier());
        }
    } 

    public IExpressionType getExpression() {
        return fExpression;
    }

    
    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.ROLE_REF)) {
                fRoleRef.fromJDOM(child);
            }
            else if(tag.equals(LDModelFactory.EXPRESSION)) {
                fExpression.fromJDOM(child);
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.WHEN_CONDITION_TRUE;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        Element roleref = fRoleRef.toJDOM();
        element.addContent(roleref);
        
        Element expression = fExpression.toJDOM();
        element.addContent(expression);
        
        return element;
    }
}
