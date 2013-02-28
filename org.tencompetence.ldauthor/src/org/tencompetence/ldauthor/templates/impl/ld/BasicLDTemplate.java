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
package org.tencompetence.ldauthor.templates.impl.ld;

import java.io.File;

import org.eclipse.swt.graphics.Image;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.roles.ILearnerModel;
import org.tencompetence.imsldmodel.roles.IStaffModel;
import org.tencompetence.ldauthor.LDAuthorException;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.serialization.LDModelSerializer;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Basic LD Template
 * 
 * @author Phillip Beauvoir
 * @version $Id: BasicLDTemplate.java,v 1.5 2009/06/30 08:21:25 phillipus Exp $
 */
public class BasicLDTemplate extends AbstractLDTemplate {
    
    public BasicLDTemplate() {
        super();
        setDescription(Messages.BasicLDTemplate_0);
        setName(Messages.BasicLDTemplate_1);
    }

    public Image getImage() {
        return ImageFactory.getImage(ImageFactory.IMAGE_APP_48);
    }
    
    @Override
    public void create(File manifestFile) throws LDAuthorException {
        super.create(manifestFile);
        
        addDefaultTitleLOsAndPrereqs();

        // Learner
        ILearnerModel learner = (ILearnerModel)LDModelFactory.createModelObject(LDModelFactory.LEARNER, fLDModel);
        fLDModel.getRolesModel().addChild(learner);
        
        // Staff
        IStaffModel staff = (IStaffModel)LDModelFactory.createModelObject(LDModelFactory.STAFF, fLDModel);
        fLDModel.getRolesModel().addChild(staff);
        
        // Learning Activity
        ILearningActivityModel la = (ILearningActivityModel)LDModelFactory.createModelObject(LDModelFactory.LEARNING_ACTIVITY, fLDModel);
        fLDModel.getActivitiesModel().getLearningActivitiesModel().addChild(la);
        String laDescription = Messages.BasicLDTemplate_2;
        LDModelUtils.setNewObjectDefaults(la, laDescription, LDModelUtils.HTML_FILE);

        // Support Activity
        ISupportActivityModel sa = (ISupportActivityModel)LDModelFactory.createModelObject(LDModelFactory.SUPPORT_ACTIVITY, fLDModel);
        fLDModel.getActivitiesModel().getSupportActivitiesModel().addChild(sa);
        String saDescription = Messages.BasicLDTemplate_2;
        LDModelUtils.setNewObjectDefaults(sa, saDescription, LDModelUtils.HTML_FILE);
        
        // Environment
        IEnvironmentModel env = (IEnvironmentModel)LDModelFactory.createModelObject(LDModelFactory.ENVIRONMENT, fLDModel);
        fLDModel.getEnvironmentsModel().addChild(env);
        
        // Learning Object
        ILearningObjectModel lo = (ILearningObjectModel)LDModelFactory.createModelObject(LDModelFactory.LEARNING_OBJECT, fLDModel);
        env.addChild(lo);
        LDModelUtils.setNewObjectDefaults(lo);
        
        // One Play
        IPlayModel play = (IPlayModel)LDModelFactory.createModelObject(LDModelFactory.PLAY, fLDModel);
        fLDModel.getMethodModel().getPlaysModel().addChild(play);
        
        // One Act
        IActModel act = (IActModel)LDModelFactory.createModelObject(LDModelFactory.ACT, fLDModel);
        play.getActsModel().addChild(act);
        
        // Role Parts
        IRolePartModel rolePart1 = (IRolePartModel)LDModelFactory.createModelObject(LDModelFactory.ROLE_PART, fLDModel);
        rolePart1.setRole(learner);
        rolePart1.setComponent(la);
        act.getRolePartsModel().addChild(rolePart1);
        
        IRolePartModel rolePart2 = (IRolePartModel)LDModelFactory.createModelObject(LDModelFactory.ROLE_PART, fLDModel);
        rolePart2.setRole(staff);
        rolePart2.setComponent(sa);
        act.getRolePartsModel().addChild(rolePart2);

        try {
            LDModelSerializer serialiser = new LDModelSerializer(fLDModel);
            serialiser.saveModel();
        }
        catch(Exception ex) {
            throw new LDAuthorException(ex);
        }

    }

}
