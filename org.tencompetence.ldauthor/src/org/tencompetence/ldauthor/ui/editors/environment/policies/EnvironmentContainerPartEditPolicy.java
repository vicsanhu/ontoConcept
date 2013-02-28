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
package org.tencompetence.ldauthor.ui.editors.environment.policies;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.TreeSelection;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObjectContainer;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalLearningObjectModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalLearningObjectModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.organisermodel.IOrganiserResource;
import org.tencompetence.ldauthor.ui.editors.common.commands.CreateObjectCommand;
import org.tencompetence.ldauthor.ui.editors.common.policies.PartEditPolicy;
import org.tencompetence.ldauthor.ui.editors.environment.dnd.NativeDropRequest;
import org.tencompetence.ldauthor.ui.editors.environment.figures.EnvironmentContainerFigure;

/**
 * Policy for Editing Environment ContainerParts
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentContainerPartEditPolicy.java,v 1.15 2009/05/22 16:35:05 phillipus Exp $
 */
public class EnvironmentContainerPartEditPolicy extends PartEditPolicy {
    
    @Override
    public EditPart getTargetEditPart(Request request) {
        if(NativeDropRequest.ID.equals(request.getType())) {
            return getHost();
        }
        return super.getTargetEditPart(request);
    }
    
    @Override
    public Command getCommand(Request request) {
        if(NativeDropRequest.ID.equals(request.getType())) {
            return getDropCommand((NativeDropRequest)request);
        }
        return super.getCommand(request);
    }

    /**
     * @param request
     * @return A command for when a native drop event occurs on an Environment Container
     */
    protected Command getDropCommand(NativeDropRequest request) {
        if(request.getData() instanceof TreeSelection) {
            CompoundCommand result = new CompoundCommand(Messages.EnvironmentContainerPartEditPolicy_0);
            
            // XY drop point
            Point pt = request.getDropLocation();
            ((GraphicalEditPart)getHost()).getContentPane().translateToRelative(pt);

            // This offset compensates for the height of the title bar.  Shouldn't have to do this!
            int offset = ((EnvironmentContainerFigure)((GraphicalEditPart)getHost()).getFigure()).getLabel().getSize().height;

            int x = 0;
            
            for(Object o : ((TreeSelection)request.getData()).toArray()) {
                if(o instanceof IOrganiserResource) {
                    IGraphicalModelObjectContainer gfxEnv = (IGraphicalModelObjectContainer)getHost().getModel();
                    IOrganiserResource organiserResource = (IOrganiserResource)o;

                    // Create new graphical Learning Object (without defaults)
                    IGraphicalLearningObjectModel gfxModel = new GraphicalLearningObjectModel(gfxEnv.getLDModel());
                    // gfxModel.createLDModelObject(); // Don't do it this way, becuase we don't want a default Resource
                    ILearningObjectModel lo = (ILearningObjectModel)LDModelFactory.createModelObject(LDModelFactory.LEARNING_OBJECT, gfxModel.getLDModel());
                    gfxModel.setLDModelObject(lo);

                    gfxModel.setBounds(new Rectangle(pt.x + x, pt.y - offset, -1, -1));
                    gfxModel.setName(organiserResource.getName());

                    // Add info from resource to Learning Object as an Item
                    ILearningObjectModel learningObject = (ILearningObjectModel)gfxModel.getLDModelObject();
                    IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, learningObject.getLDModel());
                    itemType.setTitle(organiserResource.getName());
                    learningObject.getItems().addChildItem(itemType);
                    
                    // New resource linked to Item
                    LDModelUtils.createNewResourceWithHref(itemType, organiserResource.getResourceLocation(), itemType.getTitle());

                    Command cmd = new CreateObjectCommand(gfxEnv, gfxModel);
                    result.add(cmd);
                    
                    x += 80;
                }
            }
            
            return result.unwrap();
        }

        return null;
    }

}