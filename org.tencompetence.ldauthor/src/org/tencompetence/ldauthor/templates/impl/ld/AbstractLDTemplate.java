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

import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.LDAuthorException;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.ldmodel.impl.ReCourseLDModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.templates.ILDTemplate;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Abstract LD Template
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractLDTemplate.java,v 1.5 2009/06/30 08:21:25 phillipus Exp $
 */
public abstract class AbstractLDTemplate implements ILDTemplate {
    
    protected IReCourseLDModel fLDModel;
    
    protected String fTitle = ""; //$NON-NLS-1$
    protected String fPrerequisites = ""; //$NON-NLS-1$
    protected String fLearningObjectives = ""; //$NON-NLS-1$
    
    private String fName, fDescription;
    
    protected AbstractLDTemplate() {
        fTitle = getName();
    }
    
    public void create(File manifestFile) throws LDAuthorException {
        fLDModel = new ReCourseLDModel(manifestFile);
    }
    
    protected void addDefaultTitleLOsAndPrereqs() {
        // Title
        if(StringUtils.isSetAfterTrim(fTitle)) {
            fLDModel.setTitle(fTitle);
        }
        
        // Learning Objectives
        if(StringUtils.isSetAfterTrim(fLearningObjectives)) {
            IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, fLDModel);
            itemType.setTitle(Messages.AbstractLDTemplate_0);
            fLDModel.getLearningObjectives().addChildItem(itemType);
            LDModelUtils.setNewObjectDefaults(itemType, fLearningObjectives, LDModelUtils.HTML_FILE);
        }

        // Prerequisites
        if(StringUtils.isSetAfterTrim(fPrerequisites)) {
            IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, fLDModel);
            itemType.setTitle(Messages.AbstractLDTemplate_1);
            fLDModel.getPrerequisites().addChildItem(itemType);
            LDModelUtils.setNewObjectDefaults(itemType, fPrerequisites, LDModelUtils.HTML_FILE);
        }
    }

    public String getLearningObjectives() {
        return fLearningObjectives;
    }
    
    public void setLearningObjectives(String learningObjectives) {
        fLearningObjectives = learningObjectives;
    }

    public String getPrerequisites() {
        return fPrerequisites;
    }
    
    public void setPrerequisites(String prereqs) {
        fPrerequisites = prereqs;
    }
    
    public String getTitle() {
        return fTitle;
    }

    public void setTitle(String title) {
        fTitle = title;
    }

    public void setDescription(String description) {
        fDescription = description;
    }

    public void setName(String name) {
        fName = name;
    }

    public String getName() {
        return fName;
    }
    
    public String getDescription() {
        return fDescription;
    }
}
