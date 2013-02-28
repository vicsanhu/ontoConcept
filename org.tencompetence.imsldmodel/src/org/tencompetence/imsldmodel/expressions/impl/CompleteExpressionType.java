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
import org.tencompetence.imsldmodel.expressions.ICompleteExpressionType;
import org.tencompetence.imsldmodel.expressions.IUOLHrefType;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.method.impl.ActRefModel;
import org.tencompetence.imsldmodel.method.impl.PlayRefModel;
import org.tencompetence.imsldmodel.method.impl.RolePartRefModel;


/**
 * CompleteExpressionType
 * 
 * @author Phillip Beauvoir
 * @version $Id: CompleteExpressionType.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class CompleteExpressionType implements ICompleteExpressionType {

    private ILDModel fLDModel;
    
    private ILDModelObject fComponentRef;
    
    /**
     * Constructor
     * @param model
     */
    public CompleteExpressionType(ILDModel model) {
        fLDModel = model;
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    public ILDModelObject getComponentReference() {
        return fComponentRef;
    }
    
    public String[] getReferenceTypeNames() {
        return COMPLETE_CHOICES;
    }
    
    public void setComponent(ILDModelObject object) {
        // Convert LD object to ref object...
        if(object instanceof IPlayModel) {
            fComponentRef = new PlayRefModel(getLDModel());
        }
        else if(object instanceof IActModel) {
            fComponentRef = new ActRefModel(getLDModel());
        }
        else if(object instanceof IRolePartModel) {
            fComponentRef = new RolePartRefModel(getLDModel());
        }
        else if(object instanceof ILearningActivityModel) {
            fComponentRef = new LearningActivityRefModel(getLDModel());
        }
        else if(object instanceof ISupportActivityModel) {
            fComponentRef = new SupportActivityRefModel(getLDModel());
        }
        else if(object instanceof IActivityStructureModel) {
            fComponentRef = new ActivityStructureRefModel(getLDModel());
        }
                
        if(fComponentRef != null) {
            ((ILDModelObjectReference)fComponentRef).setReferenceIdentifer(((IIdentifier)object).getIdentifier());
        }
    }
    
    public void setUOLHref(String href) {
        if(!(fComponentRef instanceof IUOLHrefType)) {
            fComponentRef = new UOLHrefType(getLDModel());
        }
        ((IUOLHrefType)fComponentRef).setHref(href);
    }
    
    
    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.PLAY_REF)) {
                fComponentRef = new PlayRefModel(fLDModel);
            }
            else if(tag.equals(LDModelFactory.ACT_REF)) {
                fComponentRef = new ActRefModel(fLDModel);
            }
            else if(tag.equals(LDModelFactory.ROLEPART_REF)) {
                fComponentRef = new RolePartRefModel(fLDModel);
            }
            else if(tag.equals(LDModelFactory.LEARNING_ACTIVITY_REF)) {
                fComponentRef = new LearningActivityRefModel(fLDModel);
            }
            else if(tag.equals(LDModelFactory.SUPPORT_ACTIVITY_REF)) {
                fComponentRef = new SupportActivityRefModel(fLDModel);
            }
            else if(tag.equals(LDModelFactory.ACTIVITY_STRUCTURE_REF)) {
                fComponentRef = new ActivityStructureRefModel(fLDModel);
            }
            else if(tag.equals(LDModelFactory.UOL_HREF)) {
                fComponentRef = new UOLHrefType(getLDModel());
            }
            
            if(fComponentRef != null) {
                fComponentRef.fromJDOM(child);
            }
        }
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        if(fComponentRef != null) {
            Element child = fComponentRef.toJDOM();
            element.addContent(child);
        }
        
        return element;
    }

    public String getTagName() {
        return LDModelFactory.COMPLETE;
    }
}
