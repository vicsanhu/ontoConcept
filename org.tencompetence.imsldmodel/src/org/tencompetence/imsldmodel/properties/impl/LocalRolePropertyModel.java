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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.properties.ILocalRolePropertyModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;


/**
 * Local Property Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: LocalRolePropertyModel.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class LocalRolePropertyModel extends AbstractLocalPropertyModel
implements ILocalRolePropertyModel, PropertyChangeListener {
    
    private String fTitle;
    
    private IRoleModel fRole;

    public LocalRolePropertyModel(ILDModel model) {
        super(model);
        model.addPropertyChangeListener(this);
    }
    
    public String getTitle() {
        return fTitle == null ? getLDModel().getObjectName(getTagName()) : fTitle;
    }

    public void setTitle(String title) {
        String old = fTitle;
        fTitle = title;
        getLDModel().firePropertyChange(this, PROPERTY_NAME, old, title);
    }

    public String getTagName() {
        return LDModelFactory.LOCAL_ROLE_PROPERTY;
    }

    public IRoleModel getRole() {
        return fRole;
    }

    public void setRole(IRoleModel role) {
        fRole = role;
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        // Listen to Roles being removed
        if(evt.getNewValue() instanceof IRoleModel) {
            if(ILDModelObjectContainer.PROPERTY_CHILD_REMOVED.equals(evt.getPropertyName())) {
                IRoleModel role = (IRoleModel)evt.getNewValue();
                if(fRole == role) {
                    fRole = null;
                }
            }
        }
    }
    
    @Override
    public Element toJDOM() {
        Element element = super.toJDOM();
        
        // Role
        if(fRole != null) {
            Element roleref = new Element(LDModelFactory.ROLE_REF, IMSLD_NAMESPACE_100_EMBEDDED);
            roleref.setAttribute(LDModelFactory.REF, fRole.getIdentifier());
            
            // Insert Role ref just after <title> element, or at position 0 if no title
            int index = element.getChild(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED) == null ? 0 : 1;
            element.addContent(index, roleref);
        }
        
        return element;
    }
    
    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);
        
        // Role
        Element roleref = element.getChild(LDModelFactory.ROLE_REF, IMSLD_NAMESPACE_100_EMBEDDED);
        if(roleref != null) {
            String ref = roleref.getAttributeValue(LDModelFactory.REF);
            if(ref != null) {
                fRole = (IRoleModel)getLDModel().getModelObject(ref);
            }
        }
    }
}
