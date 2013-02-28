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
package org.tencompetence.ldauthor.graphicsmodel;

import org.eclipse.gef.requests.CreationFactory;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalConferenceModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalEnvironmentConnection;
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalEnvironmentModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalIndexSearchModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalLearningObjectModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalMonitorModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalSendMailModel;
import org.tencompetence.ldauthor.graphicsmodel.other.impl.GraphicalNoteModel;


/**
 * Graphical Model Factory for creating objects from the Palette in teh Environment Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentEditorGraphicalModelFactory.java,v 1.3 2009/09/24 17:15:34 phillipus Exp $
 */
public class EnvironmentEditorGraphicalModelFactory implements CreationFactory {
    
    protected String fName;
    protected ILDModel fLDModel;
    
    public static final String ICON = "icon"; //$NON-NLS-1$
    public static final String NOTE = "note"; //$NON-NLS-1$
    public static final String ENVIRONMENT_CONNECTION = "environment-connection"; //$NON-NLS-1$
    
    public EnvironmentEditorGraphicalModelFactory(String name, ILDModel ldModel) {
        fName = name;
        fLDModel = ldModel;
    }
    
    public Object getNewObject() {
        // Environments
        if(fName.equals(LDModelFactory.ENVIRONMENT)) {
            return new GraphicalEnvironmentModel(fLDModel);
        }
        else if(fName.equals(LDModelFactory.CONFERENCE)) {
            return new GraphicalConferenceModel(fLDModel);
        }
        else if(fName.equals(LDModelFactory.INDEX_SEARCH)) {
            return new GraphicalIndexSearchModel(fLDModel);
        }
        else if(fName.equals(LDModelFactory.MONITOR)) {
            return new GraphicalMonitorModel(fLDModel);
        }
        else if(fName.equals(LDModelFactory.SEND_MAIL)) {
            return new GraphicalSendMailModel(fLDModel);
        }
        else if(fName.equals(LDModelFactory.LEARNING_OBJECT)) {
            return new GraphicalLearningObjectModel(fLDModel);
        }
        else if(fName.equals(LDModelFactory.LEARNING_OBJECT_KNOWLEDGE_OBJECT)) {
            return new GraphicalLearningObjectModel(fLDModel, ILearningObjectModel.TYPE_KNOWLEDGE_OBJECT);
        }
        else if(fName.equals(LDModelFactory.LEARNING_OBJECT_TEST_OBJECT)) {
            return new GraphicalLearningObjectModel(fLDModel, ILearningObjectModel.TYPE_TEST_OBJECT);
        }
        else if(fName.equals(LDModelFactory.LEARNING_OBJECT_TOOL_OBJECT)) {
            return new GraphicalLearningObjectModel(fLDModel, ILearningObjectModel.TYPE_TOOL_OBJECT);
        }
        // Connections
        else if(fName.equals(ENVIRONMENT_CONNECTION)) {
            return new GraphicalEnvironmentConnection(fLDModel);
        }
        // Other
        else if(fName.equals(NOTE)) {
            return new GraphicalNoteModel(fLDModel);
        }
        
        return null;
    }

    public Object getObjectType() {
        return fName;
    }
}
