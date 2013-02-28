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
package org.tencompetence.imsldmodel.method.impl;

import java.util.UUID;

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
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.roles.impl.RoleRefModel;


/**
 * RolePartModel
 * 
 * @author Phillip Beauvoir
 * @version $Id: RolePartModel.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class RolePartModel
implements IRolePartModel {

    private ILDModel fLDModel;
    
    private String fTitle;
    private String fID;
    
    /**
     * Reference to a Role
     */
    private ILDModelObjectReference fRoleRef;
    
    /**
     * Reference to a component
     */
    private ILDModelObjectReference fComponentRef;

    /**
     * Constructor
     * @param ldModel
     */
    public RolePartModel(ILDModel ldModel) {
        fLDModel = ldModel;
        fRoleRef = new RoleRefModel(ldModel);
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public ILDModelObject getComponent() {
        if(fComponentRef != null) {
            return fComponentRef.getLDModelObject();
        }
        return null;
    }

    public void setComponent(ILDModelObject object) {
        // Convert LD object to ref object...
        
        if(object instanceof ILearningActivityModel) {
            fComponentRef = new LearningActivityRefModel(getLDModel());
        }
        else if(object instanceof ISupportActivityModel) {
            fComponentRef = new SupportActivityRefModel(getLDModel());
        }
        else if(object instanceof IActivityStructureModel) {
            fComponentRef = new ActivityStructureRefModel(getLDModel());
        }
        else if(object instanceof IEnvironmentModel) {
            fComponentRef = new EnvironmentRefModel(getLDModel());
        }
        
        if(fComponentRef != null) {
            fComponentRef.setReferenceIdentifer(((IIdentifier)object).getIdentifier());
        }
    }

    public IRoleModel getRole() {
        return (IRoleModel)fRoleRef.getLDModelObject();
    }

    public void setRole(IRoleModel role) {
        if(role != null) {
            fRoleRef.setReferenceIdentifer(role.getIdentifier());
        }
    }

    public String getTitle() {
        return fTitle == null ? getLDModel().getObjectName(getTagName()) : fTitle;
    }

    public void setTitle(String title) {
        fTitle = title;
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "rolepart-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public void setIdentifier(String id) {
        fID = id;
    }

    public void fromJDOM(Element element) {
        fID = element.getAttributeValue(LDModelFactory.IDENTIFIER);
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            // Title
            if(tag.equals(LDModelFactory.TITLE)) {
                fTitle = child.getText();
            }
            
            // Role Ref
            else if(tag.equals(LDModelFactory.ROLE_REF)) {
                fRoleRef.fromJDOM(child);
            }
            
            // Component
            else if(tag.equals(LDModelFactory.ENVIRONMENT_REF)) {
                fComponentRef = new EnvironmentRefModel(fLDModel);
                fComponentRef.fromJDOM(child);
            }

            else if(tag.equals(LDModelFactory.LEARNING_ACTIVITY_REF)) {
                fComponentRef = new LearningActivityRefModel(fLDModel);
                fComponentRef.fromJDOM(child);
            }
            
            else if(tag.equals(LDModelFactory.SUPPORT_ACTIVITY_REF)) {
                fComponentRef = new SupportActivityRefModel(fLDModel);
                fComponentRef.fromJDOM(child);
            }
            
            else if(tag.equals(LDModelFactory.ACTIVITY_STRUCTURE_REF)) {
                fComponentRef = new ActivityStructureRefModel(fLDModel);
                fComponentRef.fromJDOM(child);
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.ROLE_PART;
    }

    public Element toJDOM() {
        Element rolepart = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        rolepart.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());
        
        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        rolepart.addContent(title);

        // Role Ref
        Element roleref = fRoleRef.toJDOM();
        rolepart.addContent(roleref);
        
        // Component
        if(fComponentRef != null) {
            Element e = fComponentRef.toJDOM();
            rolepart.addContent(e);
        }
        
        return rolepart;
    }

}
