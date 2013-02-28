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
package org.tencompetence.ldauthor.ldmodel.util;

import java.util.ArrayList;
import java.util.List;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.environments.IConferenceModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.environments.IMonitorModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;


/**
 * Enumerates and adds a description all the Item Types for an LD
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemTypeDescriptionEnumerator.java,v 1.3 2009/06/16 09:49:57 phillipus Exp $
 */
public class ItemTypeDescriptionEnumerator {

    private ILDModel fLDModel;
    
    public ItemTypeDescriptionEnumerator(ILDModel ldModel) {
        fLDModel = ldModel;
    }
    
    /**
     * @return All the Item Type details
     */
    public List<ItemTypeOwnerDetail> getAllItemTypeDetails() {
        return getAllItemTypeDetails(null);
    }
    
    /**
     * @return All the Item Type details for all ItemTypes that reference a given resource
     */
    public List<ItemTypeOwnerDetail> getAllItemTypeDetails(IResourceModel resource) {
        List<ItemTypeOwnerDetail> list = new ArrayList<ItemTypeOwnerDetail>();
        
        List<ItemTypeContainerDetail> sublist = new ArrayList<ItemTypeContainerDetail>();
        
        // UNIT OF LEARNING

        // Learning Objectives
        IItemTypeContainer itemTypeContainer = fLDModel.getLearningObjectives();
        if(hasItemReference(itemTypeContainer, resource)) {
            sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_1, itemTypeContainer));
        }
        
        // Prerequisites
        itemTypeContainer = fLDModel.getPrerequisites();
        if(hasItemReference(itemTypeContainer, resource)) {
            sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_0, itemTypeContainer));
        }
        
        // Feedback Description
        itemTypeContainer = fLDModel.getMethodModel().getOnCompletionType().getFeedBackDescription();
        if(hasItemReference(itemTypeContainer, resource)) {
            sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_2, itemTypeContainer));
        }
        
        if(!sublist.isEmpty()) {
            ItemTypeOwnerDetail ownerDetail = new ItemTypeOwnerDetail(fLDModel, Messages.ItemTypeEnumerator_3, sublist);
            list.add(ownerDetail);
        }
        
        // ROLES
        
        for(IRoleModel roleModel : fLDModel.getRolesModel().getOrderedRoles()) {
            // Role Information
            itemTypeContainer = roleModel.getInformationModel();
            if(hasItemReference(itemTypeContainer, resource)) {
                sublist = new ArrayList<ItemTypeContainerDetail>();
                sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_4, itemTypeContainer));
                ItemTypeOwnerDetail ownerDetail = new ItemTypeOwnerDetail(roleModel, roleModel.getTitle(), sublist);
                list.add(ownerDetail);
            }
        }
        
        // ACTIVITIES
        
        // Learning Activities
        for(ILDModelObject ldModel : fLDModel.getActivitiesModel().getLearningActivitiesModel().getChildren()) {
            if(ldModel instanceof ILearningActivityModel) {
                ILearningActivityModel la = (ILearningActivityModel)ldModel;
                
                sublist = new ArrayList<ItemTypeContainerDetail>();
                
                // Description
                itemTypeContainer = la.getDescriptionModel();
                if(hasItemReference(itemTypeContainer, resource)) {
                    sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_6, itemTypeContainer));
                }

                // Learning Objectives
                itemTypeContainer = la.getLearningObjectivesModel();
                if(hasItemReference(itemTypeContainer, resource)) {
                    sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_8, itemTypeContainer));
                }
                
                // Prerequisites
                itemTypeContainer = la.getPrerequisitesModel();
                if(hasItemReference(itemTypeContainer, resource)) {
                    sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_7, itemTypeContainer));
                }
                
                // Completion Feedback
                itemTypeContainer = la.getOnCompletionType().getFeedBackDescription();
                if(hasItemReference(itemTypeContainer, resource)) {
                    sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_5, itemTypeContainer));
                }

                if(!sublist.isEmpty()) {
                    ItemTypeOwnerDetail ownerDetail = new ItemTypeOwnerDetail(la, la.getTitle(), sublist);
                    list.add(ownerDetail);
                }
            }
        }
        
        // Support Activities
        for(ILDModelObject ldModel : fLDModel.getActivitiesModel().getSupportActivitiesModel().getChildren()) {
            if(ldModel instanceof ISupportActivityModel) {
                ISupportActivityModel sa = (ISupportActivityModel)ldModel;
                
                sublist = new ArrayList<ItemTypeContainerDetail>();
                
                // Description
                itemTypeContainer = sa.getDescriptionModel();
                if(hasItemReference(itemTypeContainer, resource)) {
                    sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_10, itemTypeContainer));
                }
                
                // Completion Feedback
                itemTypeContainer = sa.getOnCompletionType().getFeedBackDescription();
                if(hasItemReference(itemTypeContainer, resource)) {
                    sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_9, itemTypeContainer));
                }
                
                if(!sublist.isEmpty()) {
                    ItemTypeOwnerDetail ownerDetail = new ItemTypeOwnerDetail(sa, sa.getTitle(), sublist);
                    list.add(ownerDetail);
                }
            }
        }
        
        // Activity Structures
        for(ILDModelObject ldModel : fLDModel.getActivitiesModel().getActivityStructuresModel().getChildren()) {
            if(ldModel instanceof IActivityStructureModel) {
                IActivityStructureModel as = (IActivityStructureModel)ldModel;
                
                // Information
                itemTypeContainer = as.getInformationModel();
                if(hasItemReference(itemTypeContainer, resource)) {
                    sublist = new ArrayList<ItemTypeContainerDetail>();
                    sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_11, itemTypeContainer));
                    ItemTypeOwnerDetail ownerDetail = new ItemTypeOwnerDetail(as, as.getTitle(), sublist);
                    list.add(ownerDetail);
                }
            }
        }
        
        
        // ENVIRONMENTS
        
        for(ILDModelObject ldModel : fLDModel.getEnvironmentsModel().getChildren()) {
            if(ldModel instanceof IEnvironmentModel) {
                IEnvironmentModel env = (IEnvironmentModel)ldModel;
                String envName = " [" + env.getTitle() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
                for(ILDModelObject child : env.getChildren()) {
                    // Learning Object
                    if(child instanceof ILearningObjectModel) {
                        ILearningObjectModel lo = (ILearningObjectModel)child;
                        // Resources
                        itemTypeContainer = lo.getItems();
                        if(hasItemReference(itemTypeContainer, resource)) {
                            sublist = new ArrayList<ItemTypeContainerDetail>();
                            sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_12, itemTypeContainer));
                            ItemTypeOwnerDetail ownerDetail = new ItemTypeOwnerDetail(lo, lo.getTitle() + envName, sublist);
                            list.add(ownerDetail);
                        }
                    }
                    // Conference
                    else if(child instanceof IConferenceModel) {
                        IConferenceModel conf = (IConferenceModel)child;
                        // Resources
                        itemTypeContainer = conf.getItem();
                        if(hasItemReference(itemTypeContainer, resource)) {
                            sublist = new ArrayList<ItemTypeContainerDetail>();
                            sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_13, itemTypeContainer));
                            ItemTypeOwnerDetail ownerDetail = new ItemTypeOwnerDetail(conf, conf.getTitle() + envName, sublist);
                            list.add(ownerDetail);
                        }
                    }
                    // Monitor
                    else if(child instanceof IMonitorModel) {
                        IMonitorModel monitor = (IMonitorModel)child;
                        // Resources
                        itemTypeContainer = monitor.getItems();
                        if(hasItemReference(itemTypeContainer, resource)) {
                            sublist = new ArrayList<ItemTypeContainerDetail>();
                            sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_14, itemTypeContainer));
                            ItemTypeOwnerDetail ownerDetail = new ItemTypeOwnerDetail(monitor, monitor.getTitle() + envName, sublist);
                            list.add(ownerDetail);
                        }
                    }
                }
            }
        }
        
        // PLAYS & ACTS
        
        for(ILDModelObject ldModel : fLDModel.getMethodModel().getPlaysModel().getChildren()) {
            if(ldModel instanceof IPlayModel) {
                IPlayModel play = (IPlayModel)ldModel;
                // Completion Feedback
                itemTypeContainer = play.getOnCompletionType().getFeedBackDescription();
                if(hasItemReference(itemTypeContainer, resource)) {
                    sublist = new ArrayList<ItemTypeContainerDetail>();
                    sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_15, itemTypeContainer));
                    ItemTypeOwnerDetail ownerDetail = new ItemTypeOwnerDetail(play, play.getTitle(), sublist);
                    list.add(ownerDetail);
                }
                
                for(ILDModelObject child : play.getActsModel().getChildren()) {
                    if(child instanceof IActModel) {
                        IActModel act = (IActModel)child;
                        // Completion Feedback
                        itemTypeContainer = act.getOnCompletionType().getFeedBackDescription();
                        if(hasItemReference(itemTypeContainer, resource)) {
                            sublist = new ArrayList<ItemTypeContainerDetail>();
                            sublist.add(new ItemTypeContainerDetail(Messages.ItemTypeEnumerator_15, itemTypeContainer));
                            ItemTypeOwnerDetail ownerDetail = new ItemTypeOwnerDetail(act, act.getTitle(), sublist);
                            list.add(ownerDetail);
                        }
                    }
                }
            }
        }
        
        return list;
    }
    
    /**
     * Return true if IItemTypeContainer has a child item that references Resource
     * 
     * @param itemTypeContainer
     * @param identifier
     * @return
     */
    private boolean hasItemReference(IItemTypeContainer itemTypeContainer, IResourceModel resource) {
        if(resource == null) {
            return hasItems(itemTypeContainer);  // Calling this with null resource just returns child count
        }
        
        String identifier = resource.getIdentifier();
        
        for(IItemType itemType : itemTypeContainer.getItemTypes()) {
            if(identifier.equalsIgnoreCase(itemType.getIdentifierRef())) {
                return true;
            }
            if(hasItemReference(itemType, resource)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Return true if IItemTypeContainer has child Items
     * @param itemTypeContainer
     * @return 
     */
    private boolean hasItems(IItemTypeContainer itemTypeContainer) {
        return itemTypeContainer.getItemTypes().size() > 0;
    }
    
    
    // ====================================================================================================
    //                                              CLASSES
    // ====================================================================================================

    public static class ItemTypeOwnerDetail {
        private Object fOwner;
        private String fOwnerName;
        private List<ItemTypeContainerDetail> fItemContainerDetails;
        
        public ItemTypeOwnerDetail(Object owner, String ownerName, List<ItemTypeContainerDetail> itemContainerDetails) {
            fOwner = owner;
            fOwnerName = ownerName;
            fItemContainerDetails = itemContainerDetails;
            for(ItemTypeContainerDetail itemTypeContainerDetail : itemContainerDetails) {
                itemTypeContainerDetail.fParentOwnerDetail = this;
            }
        }
        
        public Object getOwner() {
            return fOwner;
        }
        
        public String getOwnerName() {
            return fOwnerName;
        }
        
        public List<ItemTypeContainerDetail> getItemTypeContainerDetails() {
            return fItemContainerDetails;
        }
        
        @Override
        public String toString() {
            return fOwnerName;
        }
    }
    
    public static class ItemTypeContainerDetail {
        private String fName;
        private IItemTypeContainer fItemTypeContainer;
        
        private ItemTypeOwnerDetail fParentOwnerDetail;
        
        public ItemTypeContainerDetail(String name, IItemTypeContainer itemTypeContainer) {
            fName = name;
            fItemTypeContainer = itemTypeContainer;
        }
        
        public String getName() {
            return fName;
        }
        
        public IItemTypeContainer getItemTypeContainer() {
            return fItemTypeContainer;
        }
        
        public ItemTypeOwnerDetail getParentOwnerDetail() {
            return fParentOwnerDetail;
        }
        
        @Override
        public String toString() {
            return fName;
        }
        
        /*
         * Need this for trees to equate nodes
         */
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof IItemTypeContainer) {
                return obj.equals(fItemTypeContainer);
            }
            
            return super.equals(obj);
        }
    }
    

}
