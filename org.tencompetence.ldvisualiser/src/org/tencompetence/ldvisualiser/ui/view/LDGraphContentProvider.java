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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.IParentable;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.imsldmodel.activities.IActivityRefModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureRefModel;
import org.tencompetence.imsldmodel.activities.IActivityType;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentRefModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldvisualiser.ui.view.ObjectAnalysis.RolePartMapping;

/**
 * Graph Content Provider
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDGraphContentProvider.java,v 1.20 2009/06/30 22:06:57 phillipus Exp $
 */
public class LDGraphContentProvider implements IGraphEntityContentProvider {
    
    private Object fInput;

    /* 
     * Return the top level elements to display in the graph.
     * You must also return any child elements if you want to see their child nodes
     * i.e each element only goes one level deep
     */
    public Object[] getElements(Object input) {
        fInput = input;
        
        /*
         * Graph is focussed on LD Model - return Plays and Acts
         */
        if(input instanceof ILDModel) {
            ILDModel ldModel = (ILDModel)input;
            
            List<Object> list = new ArrayList<Object>();
            
            for(Object play : ldModel.getMethodModel().getPlaysModel().getChildren()) {
                list.add(play);
                for(Object act : ((IPlayModel)play).getActsModel().getChildren()) {
                    list.add(act);
                }
            }
            
            return list.toArray();
        }
        
        /*
         * Graph is focussed on Play - return Acts
         */
        if(input instanceof IPlayModel) {
            IPlayModel playModel = (IPlayModel)input;
            return playModel.getActsModel().getChildren().toArray();
        }
        
        /*
         * Graph is focussed on Act - return Roles (and any Role parents) and Components in the RoleParts,
         *                            and Activity Structure children
         */
        if(input instanceof IActModel) {
            IActModel actModel = (IActModel)input;
            
            List<Object> list = new ArrayList<Object>();
            
            for(Object rolePart : actModel.getRolePartsModel().getChildren()) {
                // Add Role
                IRoleModel role = ((IRolePartModel)rolePart).getRole();
                list.add(role);
                // Add Component
                ILDModelObject component = ((IRolePartModel)rolePart).getComponent();
                list.add(component);
                
                // If it's an Activity Structure Sequence add its children so we can show their interconnections
                if(component instanceof IActivityStructureModel) {
                    IActivityStructureModel as = (IActivityStructureModel)component;
                    for(Object ref : as.getActivityRefs()) {
                        list.add(ref);
                    }
                }
                
                // Add Parent Roles
                Object parent = role;
                while((parent = ((IParentable)parent).getParent()) instanceof IRoleModel) {
                    list.add(parent);
                }
            }
            
            return list.toArray();
        }
        
        /*
         * Activity Structure Ref - convert to actual object
         */
        if(input instanceof IActivityStructureRefModel) {
            input = ((IActivityStructureRefModel)input).getLDModelObject();
        }
        
        /*
         * Graph is focussed on Activity Structure - return its child Activity refs
         */
        if(input instanceof IActivityStructureModel) {
            IActivityStructureModel as = (IActivityStructureModel)input;
            return as.getActivityRefs().toArray();
        }
        
        /*
         * Graph is focussed on RolePart Mapping - return Roles (and any Role parents) and Components in the RolePart,
         *                                         and Activity Structure children 
         */
        if(input instanceof RolePartMapping) {
            RolePartMapping mapping = (RolePartMapping)input;
            
            List<Object> list = new ArrayList<Object>();
            list.add(mapping.role);
            list.addAll(mapping.componentList);
            
            for(Object component : mapping.componentList) {
                // If it's an Activity Structure Sequence add its children so we can show their interconnections
                if(component instanceof IActivityStructureModel) {
                    IActivityStructureModel as = (IActivityStructureModel)component;
                    for(Object ref : as.getActivityRefs()) {
                        list.add(ref);
                    }
                }
            }
            
            // Add Parent Roles
            Object parent = mapping.role;
            while((parent = ((IParentable)parent).getParent()) instanceof IRoleModel) {
                list.add(parent);
            }
            
            return list.toArray();
        }
        
        /*
         * Environment Ref - convert to actual object
         */
        if(input instanceof IEnvironmentRefModel) {
            input = ((IEnvironmentRefModel)input).getLDModelObject();
        }
        
        /*
         * Graph is focussed on Environment - return children
         */
        if(input instanceof IEnvironmentModel) {
            IEnvironmentModel env = (IEnvironmentModel)input;
            return env.getChildren().toArray();
        }
        
        
        return new Object[0];
    }
    
    /* 
     * Return all elements that are connected to entity
     */
    public Object[] getConnectedTo(Object entity) {
        //System.out.println(entity);
        
        /*
         * Entity is Play - link to Acts
         */
        if(entity instanceof IPlayModel) {
        	List<ILDModelObject> acts = ((IPlayModel)entity).getActsModel().getChildren();
        	
        	// Just link to first Act - should we do this?
        	if(!acts.isEmpty()) {
        		return new Object[] { acts.get(0) };
        	}
        	
        	// Link to all Acts
            return acts.toArray();
        }
        
        /*
         * Entity is Act - link Acts tgether in sequence and each Act to its RolePart mappings
         */
        if(entity instanceof IActModel) {
        	List<Object> list = new ArrayList<Object>();
        	
        	// Role Part mappings
        	list.addAll(ObjectAnalysis.getRolePartMappings((IActModel)entity));
        	
        	// Next Act in sequence - should we do this?
        	list.add(ObjectAnalysis.calculateNextActConnection((IActModel) entity));
        	
        	return list.toArray();
        }
        
        /*
         * Entity is a Role in an Act - link to all RolePart components for the Role and any Role parents
         */
        if(entity instanceof IRoleModel && fInput instanceof IActModel) {
            List<Object> list = new ArrayList<Object>();
            IRoleModel role = (IRoleModel)entity;
            
            // Role Part components
            for(Object rolepart : ((IActModel)fInput).getRolePartsModel().getChildren()) {
                IRolePartModel rolePart = (IRolePartModel)rolepart;
                if(rolePart.getRole() == role) {
                    list.add(rolePart.getComponent());
                }
            }
            
            // Show Parent Roles
            if(role.getParent() instanceof IRoleModel) {
                list.add(role.getParent());
            }
            
            return list.toArray();
        }
        
        /*
         * Entity is a Role in a RolePart Mapping - link to all RolePart components for the Role and any Role parents
         */
        if(entity instanceof IRoleModel && fInput instanceof RolePartMapping) {
            List<Object> list = new ArrayList<Object>();
            IRoleModel role = (IRoleModel)entity;
            
            RolePartMapping mapping = (RolePartMapping)fInput;
            
            // Role Part components links - but only for the Role in question, not parent roles
            if(entity == mapping.role) {
                list.addAll(mapping.componentList);
            }
            
            // Show Parent Roles
            if(role.getParent() instanceof IRoleModel) {
                list.add(role.getParent());
            }

            return list.toArray();
        }
        
        /*
         * Entity is an Activity Structure - link to all children if choice type, or next child if sequence type,
         *                                   and its Environment refs
         */
        if(entity instanceof IActivityStructureModel) {
            List<Object> list = new ArrayList<Object>();
            IActivityStructureModel as = (IActivityStructureModel)entity;
            
            /*
             * If a sequence type, return the next child node
             */
            if(as.getStructureType() == IActivityStructureModel.TYPE_SEQUENCE && !as.getActivityRefs().isEmpty()) {
                list.add(as.getActivityRefs().get(0));
            }
            /*
             * Else all children
             */
            else {
                for(ILDModelObjectReference ref : as.getActivityRefs()) {
                    list.add(ref);
                }
            }
            
            // Environments
            for(ILDModelObjectReference envref : as.getEnvironmentRefs()) {
                list.add(envref.getLDModelObject());
            }
            
            return list.toArray();
        }
        
        /*
         * Entity is a Learning Activity, Support Activity - link to its Environment refs
         */
        if(entity instanceof IActivityModel) {
            List<Object> list = new ArrayList<Object>();
            IActivityType activity = (IActivityType)entity;
            for(ILDModelObjectReference envref : activity.getEnvironmentRefs()) {
                list.add(envref.getLDModelObject());
             }
            return list.toArray();
        }
        
        /*
         * Entity is an Environment - link to its children
         */
        if(entity instanceof IEnvironmentModel) {
            List<Object> list = new ArrayList<Object>();
            IEnvironmentModel env = (IEnvironmentModel)entity;
            for(ILDModelObject child : env.getChildren()) {
                // Link to real Environment, not ref
                if(child instanceof IEnvironmentRefModel) {
                    list.add(((IEnvironmentRefModel)child).getLDModelObject());
                }
            }
            return list.toArray();
        }
        
        /*
         * Activity Structure child Activity ref
         */
        if(entity instanceof IActivityRefModel) {
            List<Object> list = new ArrayList<Object>();
            
            // If it's a child of a sequence type AS
            IActivityRefModel activityref = (IActivityRefModel)entity;
            ILDModelObjectReference nextObject = ObjectAnalysis.calculateNextActivityStructureSequenceConnection(activityref);
            if(nextObject != null) {
                list.add(nextObject);
            }
            
            IActivityType activity = (IActivityType)activityref.getLDModelObject();
            
            // Environments
            for(ILDModelObjectReference envref : activity.getEnvironmentRefs()) {
                list.add(envref.getLDModelObject());
            }
            
            return list.toArray();
        }
        
        return null;
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

}