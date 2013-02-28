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
package org.tencompetence.ldauthor.ui.editors.environment;

import org.eclipse.gef.EditPart;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalConferenceModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalEnvironmentModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalEnvironmentsModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalIndexSearchModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalLearningObjectModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalMonitorModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalSendMailModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalEnvironmentConnection;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.AbstractLDEditPartFactory;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.ConferenceEditPart;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.EnvironmentConnectionPart;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.EnvironmentContainerEditPart;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.EnvironmentDiagramPart;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.IndexSearchEditPart;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.LearningObjectEditPart;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.MonitorEditPart;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.SendMailEditPart;

/**
 * Factory for creating Environment Edit Parts
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentEditPartFactory.java,v 1.6 2008/11/20 13:46:36 phillipus Exp $
 */
public class EnvironmentEditPartFactory
extends AbstractLDEditPartFactory {

    @Override
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart child = null;
        
        // Environments Diagram
        if(model instanceof IGraphicalEnvironmentsModel) {
            child = new EnvironmentDiagramPart();
        }
        // Environment
        else if(model instanceof IGraphicalEnvironmentModel) {
            child = new EnvironmentContainerEditPart();
        }
        else if(model instanceof IGraphicalLearningObjectModel) {
            child = new LearningObjectEditPart();
        }
        else if(model instanceof IGraphicalMonitorModel) {
            child = new MonitorEditPart();
        }
        else if(model instanceof IGraphicalSendMailModel) {
            child = new SendMailEditPart();
        }
        else if(model instanceof IGraphicalConferenceModel) {
            child = new ConferenceEditPart();
        }
        else if(model instanceof IGraphicalIndexSearchModel) {
            child = new IndexSearchEditPart();
        }
        else if(model instanceof GraphicalEnvironmentConnection) {
            child = new EnvironmentConnectionPart();
        }
        
        else child = super.createEditPart(context, model);
        
        // Add the model to the EditPart
        setModel(child, model);
        
        return child;
    }

}
