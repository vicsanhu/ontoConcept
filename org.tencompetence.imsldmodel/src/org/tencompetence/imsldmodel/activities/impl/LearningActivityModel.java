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

import java.util.UUID;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.types.IItemModelType;
import org.tencompetence.imsldmodel.types.impl.ItemModelType;


/**
 * Learning Activity Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: LearningActivityModel.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class LearningActivityModel extends AbstractActivityModel
implements ILearningActivityModel {
    
    private IItemModelType fLearningObjectivesModel;
    
    private IItemModelType fPrerequisitesModel;
    
    public LearningActivityModel(ILDModel ldModel) {
        super(ldModel);
        fLearningObjectivesModel = new ItemModelType(ldModel, LDModelFactory.LEARNING_OBJECTIVES);
        fPrerequisitesModel = new ItemModelType(ldModel, LDModelFactory.PREREQUISITES);
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "la-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public String getTitle() {
        return fTitle == null ? getLDModel().getObjectName(getTagName()) : fTitle;
    }

    public IItemModelType getLearningObjectivesModel() {
        return fLearningObjectivesModel;
    }

    public IItemModelType getPrerequisitesModel() {
        return fPrerequisitesModel;
    }

    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.LEARNING_OBJECTIVES)) {
                fLearningObjectivesModel.fromJDOM(child);
            }
            
            else if(tag.equals(LDModelFactory.PREREQUISITES)) {
                fPrerequisitesModel.fromJDOM(child);
            }
        }
    }

    @Override
    public Element toJDOM() {
        Element learningActivity = super.toJDOM();

        Element lo = fLearningObjectivesModel.toJDOM();
        if(lo != null) {
            learningActivity.addContent(lo);
        }
        
        Element prereq = fPrerequisitesModel.toJDOM();
        if(prereq != null) {
            learningActivity.addContent(prereq);
        }
        
        for(ILDModelObjectReference ref : fEnvironmentRefs) {
            Element e = ref.toJDOM();
            learningActivity.addContent(e);
        }
        
        Element description = fDescriptionModel.toJDOM();
        if(description != null) {
            learningActivity.addContent(description);
        }
        
        if(fCompleteActivityType != null) {
            Element child = fCompleteActivityType.toJDOM();
            if(child != null) {
                learningActivity.addContent(child);
            }
        }
        
        if(fCompletionType != null) {
            Element child = fCompletionType.toJDOM();
            if(child != null) {
                learningActivity.addContent(child);
            }
        }
        
        return learningActivity;
    }

    public String getTagName() {
        return LDModelFactory.LEARNING_ACTIVITY;
    }
}
