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

import java.util.List;

import org.jdom.Element;
import org.tencompetence.imsldmodel.IIdentifier;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.activities.impl.ActivityStructureRefModel;
import org.tencompetence.imsldmodel.activities.impl.LearningActivityRefModel;
import org.tencompetence.imsldmodel.activities.impl.SupportActivityRefModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.impl.EnvironmentRefModel;
import org.tencompetence.imsldmodel.expressions.IClassType;
import org.tencompetence.imsldmodel.expressions.IShowHideType;
import org.tencompetence.imsldmodel.expressions.IUOLHrefType;
import org.tencompetence.imsldmodel.impl.LDModelObjectArrayList;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.method.impl.PlayRefModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.impl.ItemRefModel;


/**
 * Show/Hide Type
 * 
 * @author Phillip Beauvoir
 * @version $Id: ShowHideType.java,v 1.6 2009/11/26 09:16:04 phillipus Exp $
 */
public class ShowHideType implements IShowHideType {
    
    private ILDModel fLDModel;
    
    private List<ILDModelObject> fComponentRefs;
    
    private String fType;

    public ShowHideType(ILDModel ldModel, String type) {
        fLDModel = ldModel;
        fType = type;
        fComponentRefs = new LDModelObjectArrayList();
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    public List<ILDModelObject> getMemberReferences() {
        return fComponentRefs;
    }
    
    public void addMember(ILDModelObject member) {
        if(member == null || fComponentRefs.contains(member)) {
            return;
        }
        
        if(member instanceof IClassType || member instanceof IUOLHrefType) {
            fComponentRefs.add(member);
            fLDModel.firePropertyChange(this, PROPERTY_MEMBER_ADDED, null, member);
            return;
        }
        
        // Convert LD object to ref object...
        ILDModelObjectReference ref = null;
        
        if(member instanceof IItemType) {
            ref = new ItemRefModel(fLDModel);
        }
        else if(member instanceof IEnvironmentModel) {
            ref = new EnvironmentRefModel(fLDModel);
        }
        else if(member instanceof ILearningActivityModel) {
            ref = new LearningActivityRefModel(fLDModel);
        }
        else if(member instanceof ISupportActivityModel) {
            ref = new SupportActivityRefModel(fLDModel);
        }
        else if(member instanceof IActivityStructureModel) {
            ref = new ActivityStructureRefModel(fLDModel);
        }
        else if(member instanceof IPlayModel) {
            ref = new PlayRefModel(fLDModel);
        }
                
        if(ref != null) {
            ((ILDModelObjectReference)ref).setReferenceIdentifer(((IIdentifier)member).getIdentifier());
            fComponentRefs.add(ref);
            fLDModel.firePropertyChange(this, PROPERTY_MEMBER_ADDED, null, ref);
        }
    }
    
    public void removeMember(ILDModelObject member) {
        if(member != null) {
            fComponentRefs.remove(member);
            fLDModel.firePropertyChange(this, PROPERTY_MEMBER_REMOVED, null, member);
        }
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
            
            ILDModelObject object = null;
            
            if(LDModelFactory.CLASS.equals(tag)) {
                object = new ClassType(fLDModel);
            }
            else if(LDModelFactory.ITEM_REF.equals(tag)) {
                object = new ItemRefModel(fLDModel);
            }
            else if(LDModelFactory.PLAY_REF.equals(tag)) {
                object = new PlayRefModel(fLDModel);
            }
            else if(LDModelFactory.ENVIRONMENT_REF.equals(tag)) {
                object = new EnvironmentRefModel(fLDModel);
            }
            else if(LDModelFactory.LEARNING_ACTIVITY_REF.equals(tag)) {
                object = new LearningActivityRefModel(fLDModel);
            }
            else if(LDModelFactory.SUPPORT_ACTIVITY_REF.equals(tag)) {
                object = new SupportActivityRefModel(fLDModel);
            }
            else if(LDModelFactory.ACTIVITY_STRUCTURE_REF.equals(tag)) {
                object = new ActivityStructureRefModel(fLDModel);
            }
            else if(LDModelFactory.UOL_HREF.equals(tag)) {
                object = new UOLHrefType(fLDModel);
            }
            
            if(object != null) {
                fComponentRefs.add(object);
                object.fromJDOM(child);
            }
        }
    }

    public String getTagName() {
        return fType;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        for(ILDModelObject ref : fComponentRefs) {
            Element child = ref.toJDOM();
            element.addContent(child);
        }
        
        return element;
    }

}
