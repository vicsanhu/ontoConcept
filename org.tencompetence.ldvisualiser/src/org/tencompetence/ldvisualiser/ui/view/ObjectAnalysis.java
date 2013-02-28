/*
 * Copyright (c) 2009, Consortium Board TENCompetence
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
package org.tencompetence.ldvisualiser.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.imsldmodel.activities.IActivityRefModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureRefModel;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.environments.IServiceModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;


/**
 * Analysis rules and utils
 * 
 * @author Phillip Beauvoir
 * @version $Id: ObjectAnalysis.java,v 1.8 2009/06/25 08:27:05 phillipus Exp $
 */
public class ObjectAnalysis {

    /**
     * Return true if child has child nodes
     * @param parentContext
     * @param childObject
     * @return
     */
    public static boolean hasChildNodes(Object parentContext, Object childObject) {
        /*
         * Play
         */
        if(childObject instanceof IPlayModel) {
            IPlayModel play = (IPlayModel)childObject;
            return play.getActsModel().getChildren().size() > 0;
        }
        
        /*
         * Act
         */
        if(childObject instanceof IActModel) {
            IActModel act = (IActModel)childObject;
            return act.getRolePartsModel().getChildren().size() > 0;
        }
        
        /*
         * Role in Role Part (Act)
         */
        if(childObject instanceof IRoleModel && parentContext instanceof IActModel) {
            return true;
        }
        
        /*
         * Role in Role Part (RolePartMapping)
         */
        if(childObject instanceof IRoleModel && parentContext instanceof RolePartMapping) {
            return true;
        }
        
        /*
         * Activity Structure Ref
         */
        if(childObject instanceof IActivityStructureRefModel) {
            childObject = ((IActivityStructureRefModel)childObject).getLDModelObject();
        }
        
        /*
         * Activity Structure
         */
        if(childObject instanceof IActivityStructureModel) {
            IActivityStructureModel as = (IActivityStructureModel)childObject;
            return as.getActivityRefs().size() > 0;
        }
        
        return false;
    }

    /**
     * @param currentObject
     * @param selectedElement
     * @return True if is a leaf
     */
    public static boolean isLeaf(Object currentObject, Object selectedElement) {
        if(selectedElement instanceof ILDModelObjectReference) {
            selectedElement = ((ILDModelObjectReference)selectedElement).getLDModelObject();
        }
        
        if(selectedElement instanceof IActivityModel) {
            return true;
        }
        
        if(selectedElement instanceof IRoleModel) {
            return true;
        }
        
        if(selectedElement instanceof IServiceModel) {
            return true;
        }
        
        if(selectedElement instanceof ILearningObjectModel) {
            return true;
        }
        
        return false;
    }
    
    /**
     * If an Activity Ref is a child of an Activity Structure which is a sequence type
     * then return the next connected node or null if not in a sequence
     * @param as
     * @param activityRef
     * @return
     */
    public static ILDModelObjectReference calculateNextActivityStructureSequenceConnection(IActivityRefModel activityRef) {
        IActivityStructureModel as = activityRef.getParent();
        if(as != null && as.getStructureType() == IActivityStructureModel.TYPE_SEQUENCE) {
            List<ILDModelObjectReference> refs = as.getActivityRefs();
            int pos = refs.indexOf(activityRef);
            if(pos < refs.size() - 1) {
                return refs.get(pos + 1);
            }
        }
        
        return null;
    }
    
    /**
     * @param act
     * @return The next Act in sequence after act or null if the last one
     */
    public static IActModel calculateNextActConnection(IActModel act) {
    	if(act != null ) {
            List<ILDModelObject> acts = act.getParent().getChildren();
            int pos = acts.indexOf(act);
            if(pos < acts.size() - 1) {
                return (IActModel) acts.get(pos + 1);
            }
        }
        
        return null;
    }
    
    /**
     * @param act
     * @return A list of the Roles in all RoleParts in an Act
     */
    public static List<IRoleModel> getRolesInRoleParts(IActModel act) {
        List<IRoleModel> list = new ArrayList<IRoleModel>();
        
        for(ILDModelObject child : act.getRolePartsModel().getChildren()) {
            IRolePartModel rolePart = (IRolePartModel)child;
            IRoleModel role = rolePart.getRole();
            if(!list.contains(role)) {
                list.add(role);
            }
        }
        
        return list;
    }
    
    /**
     * Calculate and return RoleParts as a map of Roles to Components done in a Role Part.
     * Key is Role, Element is a list of Components the Role does in the Act.
     * @param act
     * @return
     */
    public static List<RolePartMapping> getRolePartMappings(IActModel act) {
        List<RolePartMapping> list = new ArrayList<RolePartMapping>();
        
        for(ILDModelObject child : act.getRolePartsModel().getChildren()) {
            IRolePartModel rolePart = (IRolePartModel)child;
            IRoleModel role = rolePart.getRole();
            ILDModelObject component = rolePart.getComponent();
            
            if(role == null || component == null) {
                continue;
            }
            
            RolePartMapping mapping = new RolePartMapping(act, role);

            if(list.contains(mapping)) {
                int index = list.indexOf(mapping);
                mapping = list.get(index);
                mapping.componentList.add(component);
            }
            else {
                mapping.componentList.add(component);
                list.add(mapping);
            }
            
        }
        
        return list;
    }
    
    /**
     * Maps a Role in an Act's RoleParts to a single entity
     * 
     */
    public static class RolePartMapping {
        IActModel act;
        IRoleModel role;
        List<ILDModelObject> componentList;
        
        RolePartMapping(IActModel act, IRoleModel role) {
            this.act = act;
            this.role = role;
            componentList = new ArrayList<ILDModelObject>();
        }
        
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof RolePartMapping) {
                return ((RolePartMapping)obj).role.equals(this.role);
            }
            
            return super.equals(obj);
        }
        
        @Override
        public int hashCode() {
            // Need this for equality
            return act.hashCode();
        }
        
        @Override
        public String toString() {
            return "Role Mapping " + act.getTitle() + ": " + role.getTitle(); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
}
