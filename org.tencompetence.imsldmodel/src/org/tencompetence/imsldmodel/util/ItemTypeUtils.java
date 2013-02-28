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
package org.tencompetence.imsldmodel.util;

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
import org.tencompetence.imsldmodel.internal.StringUtils;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;


/**
 * Utility methods for Item types in the LD
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemTypeUtils.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class ItemTypeUtils {

    /**
     * Return true if resource is referenced by at least one Item
     * 
     * @param resource
     * @return
     */
    public static boolean isReferencedByItem(IResourceModel resource) {
        String identifier = resource.getIdentifier();
        
        if(!StringUtils.isSetAfterTrim(identifier)) {
            return false;
        }
        
        for(IItemType itemType : getAllItemTypes(resource.getLDModel())) {
            if(identifier.equalsIgnoreCase(itemType.getIdentifierRef())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * @return A flat list of all Item Types in the LD
     */
    public static List<IItemType> getAllItemTypes(ILDModel ldModel) {
        List<IItemType> list = new ArrayList<IItemType>();
        
        // LD
        addItemTypes(ldModel.getPrerequisites(), list);
        addItemTypes(ldModel.getLearningObjectives(), list);
        
        // Roles
        for(IRoleModel roleModel : ldModel.getRolesModel().getOrderedRoles()) {
            addItemTypes(roleModel.getInformationModel(), list);
        }
        
        // Learning Activities
        for(ILDModelObject object : ldModel.getActivitiesModel().getLearningActivitiesModel().getChildren()) {
            ILearningActivityModel la = (ILearningActivityModel)object;
            addItemTypes(la.getOnCompletionType().getFeedBackDescription(), list);
            addItemTypes(la.getDescriptionModel(), list);
            addItemTypes(la.getPrerequisitesModel(), list);
            addItemTypes(la.getLearningObjectivesModel(), list);
        }
        
        // Support Activities
        for(ILDModelObject object : ldModel.getActivitiesModel().getSupportActivitiesModel().getChildren()) {
            ISupportActivityModel sa = (ISupportActivityModel)object;
            addItemTypes(sa.getOnCompletionType().getFeedBackDescription(), list);
            addItemTypes(sa.getDescriptionModel(), list);
        }
        
        // Activity Structures
        for(ILDModelObject object : ldModel.getActivitiesModel().getActivityStructuresModel().getChildren()) {
            IActivityStructureModel as = (IActivityStructureModel)object;
            addItemTypes(as.getInformationModel(), list);
        }
        
        // Environments
        for(ILDModelObject object : ldModel.getEnvironmentsModel().getChildren()) {
            IEnvironmentModel env = (IEnvironmentModel)object;
            for(ILDModelObject child : env.getChildren()) {
                // Learning Object
                if(child instanceof ILearningObjectModel) {
                    ILearningObjectModel lo = (ILearningObjectModel)child;
                    addItemTypes(lo.getItems(), list);
                }
                // Conference
                else if(child instanceof IConferenceModel) {
                    IConferenceModel conf = (IConferenceModel)child;
                    addItemTypes(conf.getItem(), list);
                }
                // Monitor
                else if(child instanceof IMonitorModel) {
                    IMonitorModel monitor = (IMonitorModel)child;
                    addItemTypes(monitor.getItems(), list);
                }
            }
        }
        
        // Method
        addItemTypes(ldModel.getMethodModel().getOnCompletionType().getFeedBackDescription(), list);

        // Plays, Acts
        for(ILDModelObject object : ldModel.getMethodModel().getPlaysModel().getChildren()) {
            IPlayModel play = (IPlayModel)object;
            addItemTypes(play.getOnCompletionType().getFeedBackDescription(), list);

            for(ILDModelObject child : play.getActsModel().getChildren()) {
                IActModel act = (IActModel)child;
                addItemTypes(act.getOnCompletionType().getFeedBackDescription(), list);
            }
        }
        
        return list;
    }
    
    private static void addItemTypes(IItemTypeContainer itemTypeContainer, List<IItemType> list) {
        for(IItemType itemType : itemTypeContainer.getItemTypes()) {
            list.add(itemType);
            addItemTypes(itemType, list);
        }
    }
    
 }
