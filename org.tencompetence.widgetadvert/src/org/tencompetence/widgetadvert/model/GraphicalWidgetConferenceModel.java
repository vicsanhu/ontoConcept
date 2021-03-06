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
package org.tencompetence.widgetadvert.model;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.IConferenceModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalConferenceModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalConferenceModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;



/**
 * Graphical Widget Conference Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: GraphicalWidgetConferenceModel.java,v 1.1 2009/09/24 17:15:35 phillipus Exp $
 */
public class GraphicalWidgetConferenceModel
extends GraphicalConferenceModel implements IGraphicalConferenceModel {
    
    private IWidgetAdvert fWidget;
	
    public GraphicalWidgetConferenceModel(ILDModel ldModel, IWidgetAdvert widget) {
        super(ldModel);
        fWidget = widget;
    }
    
    @Override
    public void createLDModelObject() {
        IConferenceModel conference = (IConferenceModel)LDModelFactory.createModelObject(LDModelFactory.CONFERENCE, getLDModel());
        setLDModelObject(conference);
        LDModelUtils.setNewObjectDefaults(conference);
        
        // Then add the details from the Widget
        conference.setTitle(fWidget.getWidgetTitle());
        conference.setParameters(fWidget.getWidgetParameter());
    }

}
