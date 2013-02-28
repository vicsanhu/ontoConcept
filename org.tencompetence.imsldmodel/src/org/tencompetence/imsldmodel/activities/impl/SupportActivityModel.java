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
package org.tencompetence.imsldmodel.activities.impl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.impl.LDModelObjectReferenceArrayList;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.roles.impl.RoleRefModel;


/**
 * Support Activity Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: SupportActivityModel.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class SupportActivityModel extends AbstractActivityModel
implements ISupportActivityModel {
    
    private List<ILDModelObjectReference> fRoleRefs = new LDModelObjectReferenceArrayList();
    
    private List<IRoleModel> fUndoCache = new ArrayList<IRoleModel>();

    public SupportActivityModel(ILDModel ldModel) {
        super(ldModel);
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "sa-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public String getTitle() {
        return fTitle == null ? getLDModel().getObjectName(getTagName()) : fTitle;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Listen to Roles being removed
        if(evt.getNewValue() instanceof IRoleModel) {
            IRoleModel role = (IRoleModel)evt.getNewValue();
            if(ILDModelObjectContainer.PROPERTY_CHILD_REMOVED.equals(evt.getPropertyName())) {
                if(fRoleRefs.contains(role)) {
                    removeRoleRef(role);
                    fUndoCache.add(role);
                }
            }
            else if(ILDModelObjectContainer.PROPERTY_CHILD_ADDED.equals(evt.getPropertyName())) {
                if(fUndoCache.contains(role)) {
                    addRoleRef(role);
                    fUndoCache.remove(role);
                }
            }
        }
        else {
            super.propertyChange(evt);
        }
    }

    // ====================================== ROLE REF ACCESS SUPPORT ===========================================

    public List<ILDModelObjectReference> getRoleRefs() {
        return fRoleRefs;
    }

    public void addRoleRef(IRoleModel role) {
        // Convert LD object to ref object...
        if(!fRoleRefs.contains(role)) {
            ILDModelObjectReference ref = new RoleRefModel(role.getLDModel());
            ref.setReferenceIdentifer(role.getIdentifier());
            fRoleRefs.add(ref);
        }
    }

    public void removeRoleRef(IRoleModel role) {
        fRoleRefs.remove(role);
    }
    
    // ====================================== JDOM SUPPORT ===========================================
    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.ROLE_REF)) {
                ILDModelObjectReference ref = new RoleRefModel(getLDModel());
                ref.fromJDOM(child);
                fRoleRefs.add(ref);
            }
        }
    }

    @Override
    public Element toJDOM() {
        Element supportActivity = super.toJDOM();

        for(ILDModelObjectReference ref : fRoleRefs) {
            Element e = ref.toJDOM();
            supportActivity.addContent(e);
        }
        
        for(ILDModelObjectReference ref : fEnvironmentRefs) {
            Element e = ref.toJDOM();
            supportActivity.addContent(e);
        }
        
        Element description = fDescriptionModel.toJDOM();
        if(description != null) {
            supportActivity.addContent(description);
        }
        
        if(fCompleteActivityType != null) {
            Element child = fCompleteActivityType.toJDOM();
            if(child != null) {
                supportActivity.addContent(child);
            }
        }
        
        if(fCompletionType != null) {
            Element child = fCompletionType.toJDOM();
            if(child != null) {
                supportActivity.addContent(child);
            }
        }
        
        return supportActivity;
    }

    public String getTagName() {
        return LDModelFactory.SUPPORT_ACTIVITY;
    }
}
