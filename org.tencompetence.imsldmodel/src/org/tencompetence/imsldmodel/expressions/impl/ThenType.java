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
import org.tencompetence.imsldmodel.expressions.IChangePropertyValueType;
import org.tencompetence.imsldmodel.expressions.IShowHideType;
import org.tencompetence.imsldmodel.expressions.IThenType;
import org.tencompetence.imsldmodel.types.INotificationType;


/**
 * Then Type
 * 
 * @author Phillip Beauvoir
 * @version $Id: ThenType.java,v 1.8 2009/11/26 09:16:04 phillipus Exp $
 */
public class ThenType implements IThenType {
    
    private ILDModel fLDModel;
    
    private List<ILDModelObject> fMembers;

    public ThenType(ILDModel model) {
        fLDModel = model;
        fMembers = new ArrayList<ILDModelObject>();
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    public List<ILDModelObject> getMembers() {
        return fMembers;
    }

    public List<IShowHideType> getShowHideTypes() {
        List<IShowHideType> list = new ArrayList<IShowHideType>();
        for(ILDModelObject member : fMembers) {
            if(member instanceof IShowHideType) {
                list.add((IShowHideType)member);
            }
        }
        return list;
    }

    public List<IChangePropertyValueType> getChangePropertyValueTypes() {
        List<IChangePropertyValueType> list = new ArrayList<IChangePropertyValueType>();
        for(ILDModelObject member : fMembers) {
            if(member instanceof IChangePropertyValueType) {
                list.add((IChangePropertyValueType)member);
            }
        }
        return list;
    }

    public List<INotificationType> getNotificationTypes() {
        List<INotificationType> list = new ArrayList<INotificationType>();
        for(ILDModelObject member : fMembers) {
            if(member instanceof INotificationType) {
                list.add((INotificationType)member);
            }
        }
        return list;
    }

    public ILDModelObject addMember(String name) {
        ILDModelObject member = LDModelFactory.createModelObject(name, fLDModel);
        addMember(member);
        return member;
    }

    public void addMember(ILDModelObject member) {
        if(member != null) {
            fMembers.add(member);
            fLDModel.firePropertyChange(this, PROPERTY_MEMBER_ADDED, null, member);
        }
    }
    
    public boolean hasCorrectMemberAmount() {
        return fMembers.size() > 0;
    }

    public void removeMember(ILDModelObject member) {
        if(member != null) {
            fMembers.remove(member);
            fLDModel.firePropertyChange(this, PROPERTY_MEMBER_REMOVED, null, member);
        }
    }
    
    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            ILDModelObject member = addMember(tag);
            if(member != null) {
                member.fromJDOM(child);
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.THEN;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        for(ILDModelObject member : getShowHideTypes()) {
            Element child = member.toJDOM();
            if(child != null) {
                element.addContent(child);
            }
        }

        for(ILDModelObject member : getChangePropertyValueTypes()) {
            Element child = member.toJDOM();
            if(child != null) {
                element.addContent(child);
            }
        }

        for(ILDModelObject member : getNotificationTypes()) {
            Element child = member.toJDOM();
            if(child != null) {
                element.addContent(child);
            }
        }

        return element;
    }

}
