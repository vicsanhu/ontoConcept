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
package org.tencompetence.imsldmodel.roles.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.impl.LDModelObjectContainer;
import org.tencompetence.imsldmodel.roles.ILearnerModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.roles.IRolesModel;
import org.tencompetence.imsldmodel.roles.IStaffModel;

/**
 * Roles
 * 
 * @author Phillip Beauvoir
 * @version $Id: RolesModel.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class RolesModel
extends LDModelObjectContainer
implements IRolesModel {
    
    public RolesModel(ILDModel model) {
        super(model);
    }
    
    public List<ILearnerModel> getLearners() {
        List<ILearnerModel> list = new ArrayList<ILearnerModel>();
        for(ILDModelObject object : getChildren()) {
            if(object instanceof ILearnerModel) {
                list.add((ILearnerModel)object);
            }
        }
        return list;
    }
    
    public List<IStaffModel> getStaff() {
        List<IStaffModel> list = new ArrayList<IStaffModel>();
        for(ILDModelObject object : getChildren()) {
            if(object instanceof IStaffModel) {
                list.add((IStaffModel)object);
            }
        }
        return list;
    }

    public ILearnerModel getDefaultLearnerRole() {
        List<ILearnerModel> list = getLearners();
        return list.isEmpty() ? null : list.get(0);
    }

    public IStaffModel getDefaultStaffRole() {
        List<IStaffModel> list = getStaff();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<IRoleModel> getOrderedRoles() {
        List<IRoleModel> list = new ArrayList<IRoleModel>();
        list.addAll(getOrderedLearners());
        list.addAll(getOrderedStaff());
        return list;
    }
    
    public List<ILearnerModel> getOrderedLearners() {
        List<ILearnerModel> list = new ArrayList<ILearnerModel>();
        _getLearnerFlatList(this, list);
        Collections.sort(list, new TitleComparator());
        return list;
    }
    
    // internal
    private void _getLearnerFlatList(ILDModelObjectContainer model, List<ILearnerModel> list) {
        for(ILDModelObject o : model.getChildren()) {
            if(o instanceof ILearnerModel) {
                list.add((ILearnerModel)o);
                _getLearnerFlatList((ILearnerModel)o, list);
            }
        }
    }

    public List<IStaffModel> getOrderedStaff() {
        List<IStaffModel> list = new ArrayList<IStaffModel>();
        _getStaffFlatList(this, list);
        Collections.sort(list, new TitleComparator());
        return list;
    }
    
    // internal
    private void _getStaffFlatList(ILDModelObjectContainer model, List<IStaffModel> list) {
        for(ILDModelObject o : model.getChildren()) {
            if(o instanceof IStaffModel) {
                list.add((IStaffModel)o);
                _getStaffFlatList((IStaffModel)o, list);
            }
        }
    }
    
    private static class TitleComparator implements Comparator<ITitle> {
        public int compare(ITitle o1, ITitle o2) {
            if(o1.getTitle() == null || o1.getTitle() == null) {
                return 0;
            }
            return o1.getTitle().compareToIgnoreCase(o2.getTitle());
        }
    }
    
    @Override
    public String toString() {
        return getLDModel().getObjectName(LDModelFactory.ROLES);
    }


    // ====================================== JDOM =====================================
    
    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        // Learners
        for(ILDModelObject object : getLearners()) {
            Element child = object.toJDOM();
            element.addContent(child);
        }
        
        // Staff
        for(ILDModelObject object : getStaff()) {
            Element child = object.toJDOM();
            element.addContent(child);
        }

        return element;
    }
    
    public void fromJDOM(Element element) {
        // Dynamically create objects from children
        for(Object child : element.getChildren()) {
            ILDModelObject object = LDModelFactory.createModelObject(((Element)child).getName(), getLDModel());
            if(object != null) {
                object.fromJDOM((Element)child); // This first, in order to set ID
                addChild(object);
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.ROLES;
    }
}
