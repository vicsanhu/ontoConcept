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

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.ICalculateType;


/**
 * Calculate Type
 * Note: this type must have two and only two choice members
 * 
 * @author Phillip Beauvoir
 * @version $Id: CalculateType.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class CalculateType implements ICalculateType {
    
    protected ILDModel fLDModel;
    
    private String fType;
    
    protected List<ILDModelObject> fExpressionMemberTypes;

    public CalculateType(ILDModel model, String type) {
        fLDModel = model;
        fType = type;
        fExpressionMemberTypes = new ArrayList<ILDModelObject>();
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    public List<ILDModelObject> getExpressionMemberTypes() {
        return fExpressionMemberTypes;
    }
    
    public String[] getExpressionMemberTypeNames() {
        return CALCULATE_CHOICES;
    }
    
    public ILDModelObject addExpressionMemberType(String name) {
        ILDModelObject member = LDModelFactory.createModelObject(name, fLDModel);
        addExpressionMemberType(member);
        return member;
    }
    
    public void addExpressionMemberType(ILDModelObject member) {
        if(member != null) {
            // Can only add a maximum of two so set it as the last one
            if(fExpressionMemberTypes.size() == 2) {
                fExpressionMemberTypes.set(1, member);
            }
            else {
                fExpressionMemberTypes.add(member);
            }
            fLDModel.firePropertyChange(this, PROPERTY_MEMBER_ADDED, null, member);
        }
    }
    
    public void removeExpressionMemberType(ILDModelObject member) {
        if(member != null) {
            fExpressionMemberTypes.remove(member);
            fLDModel.firePropertyChange(this, PROPERTY_MEMBER_REMOVED, null, member);
        }
    }
    
    public void replaceExpressionMemberType(ILDModelObject oldMember, ILDModelObject newMember) {
        if(oldMember != null && newMember != null) {
            int index = fExpressionMemberTypes.indexOf(oldMember);
            if(index != -1) {
                fExpressionMemberTypes.set(index, newMember);
                fLDModel.firePropertyChange(this, PROPERTY_MEMBER_REPLACED, oldMember, newMember);
            }
        }
    }
    
    public boolean hasCorrectMemberAmount() {
        return fExpressionMemberTypes.size() == 2;
    }
    
    public boolean canAddExpressionMemberType(String name) {
        return fExpressionMemberTypes.size() < 2;
    }
    
    public String getType() {
        return fType;
    }

    public void setType(String type) {
        fType = type;
    }

    
    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            ILDModelObject member = addExpressionMemberType(tag);
            if(member != null) {
                member.fromJDOM(child);
            }
        }
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        for(ILDModelObject member : fExpressionMemberTypes) {
            Element child = member.toJDOM();
            if(child != null) {
                element.addContent(child);
            }
        }
        
        return element;
    }

    public String getTagName() {
        return fType;
    }

}
