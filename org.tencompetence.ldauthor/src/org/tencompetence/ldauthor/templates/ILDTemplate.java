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
package org.tencompetence.ldauthor.templates;

import java.io.File;

import org.tencompetence.ldauthor.LDAuthorException;


/**
 * Interface for LD Template
 * 
 * @author Phillip Beauvoir
 * @version $Id: ILDTemplate.java,v 1.2 2008/12/05 00:25:02 phillipus Exp $
 */
public interface ILDTemplate extends ITemplate {

    /**
     * Set the Manifest File and create the LD
     * @param manifestFile
     * @throws LDAuthorException 
     */
    void create(File manifestFile) throws LDAuthorException;

    /**
     * Set the title of the LD (if any)
     * @param title
     */
    void setTitle(String title);
    
    /**
     * @return title
     */ 
    String getTitle();

    /**
     * Set the Learning Objectives of the LD (if any)
     * @param learningObjectives
     */
    void setLearningObjectives(String learningObjectives);
    
    /**
     * @return Learning Objectives
     */
    String getLearningObjectives();

    /**
     * Set the Prerequisites of the LD (if any)
     * @param prereqs
     */
    void setPrerequisites(String prereqs);

    /**
     * @return Prerequisites
     */
    String getPrerequisites();
}
