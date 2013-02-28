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

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivitiesModel;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityType;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.impl.LDModelObjectContainer;


/**
 * Activities Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActivitiesModel.java,v 1.6 2009/11/26 09:16:04 phillipus Exp $
 */
public class ActivitiesModel implements IActivitiesModel {
    
    private ILDModel fLDModel;
    
    private ILDModelObjectContainer fLearningActivitiesModel;
    private ILDModelObjectContainer fSupportActivitiesModel;
    private ILDModelObjectContainer fActivityStructuresModel;

    public ActivitiesModel(ILDModel ldModel) {
        fLDModel = ldModel;
        
        fLearningActivitiesModel = new LDModelObjectContainer(ldModel);
        fSupportActivitiesModel = new LDModelObjectContainer(ldModel);
        fActivityStructuresModel = new LDModelObjectContainer(ldModel);
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public ILDModelObjectContainer getLearningActivitiesModel() {
        return fLearningActivitiesModel;
    }

    public ILDModelObjectContainer getSupportActivitiesModel() {
        return fSupportActivitiesModel;
    }

    public ILDModelObjectContainer getActivityStructuresModel() {
        return fActivityStructuresModel;
    }

    public List<IActivityType> getAllActivities() {
        List<IActivityType> list = new ArrayList<IActivityType>();
        
        for(ILDModelObject activity : getLearningActivitiesModel().getChildren()) {
            list.add((IActivityType)activity);
        }
        
        for(ILDModelObject activity : getSupportActivitiesModel().getChildren()) {
            list.add((IActivityType)activity);
        }
        
        for(ILDModelObject activity : getActivityStructuresModel().getChildren()) {
            list.add((IActivityType)activity);
        }

        return list;
    }
    
    public List<IActivityModel> getLearningAndSupportActivities() {
        List<IActivityModel> list = new ArrayList<IActivityModel>();
        
        for(ILDModelObject activity : getLearningActivitiesModel().getChildren()) {
            list.add((IActivityModel)activity);
        }
        
        for(ILDModelObject activity : getSupportActivitiesModel().getChildren()) {
            list.add((IActivityModel)activity);
        }

        return list;
    }

    @Override
    public String toString() {
        return getLDModel().getObjectName(getTagName());
    }
    
    // ====================================== JDOM ============================
    
    public void fromJDOM(Element element) {
        for(Object object : element.getChildren()) {
            Element child = (Element)object;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.LEARNING_ACTIVITY)) {
                ILearningActivityModel learningActivity = new LearningActivityModel(fLDModel);
                learningActivity.fromJDOM(child); // This first, in order to set ID
                fLearningActivitiesModel.addChild(learningActivity);
            }
            else if(tag.equals(LDModelFactory.SUPPORT_ACTIVITY)) {
                ISupportActivityModel supportActivity = new SupportActivityModel(fLDModel);
                supportActivity.fromJDOM(child); // This first, in order to set ID
                fSupportActivitiesModel.addChild(supportActivity);
            }
            else if(tag.equals(LDModelFactory.ACTIVITY_STRUCTURE)) {
                IActivityStructureModel activityStructure = new ActivityStructureModel(fLDModel);
                activityStructure.fromJDOM(child); // This first, in order to set ID
                fActivityStructuresModel.addChild(activityStructure);
            }
        }
    }

    public Element toJDOM() {
        Element activites = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        for(ILDModelObject object : fLearningActivitiesModel.getChildren()) {
            Element child = object.toJDOM();
            activites.addContent(child);
        }
        
        for(ILDModelObject object : fSupportActivitiesModel.getChildren()) {
            Element child = object.toJDOM();
            activites.addContent(child);
        }

        for(ILDModelObject object : fActivityStructuresModel.getChildren()) {
            Element child = object.toJDOM();
            activites.addContent(child);
        }

        return activites.getChildren().size() == 0 ? null : activites;
    }

    public String getTagName() {
        return LDModelFactory.ACTIVITIES;
    }
}
