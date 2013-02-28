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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObjectContainer;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalEnvironmentModel;
import org.tencompetence.ldauthor.ui.editors.common.commands.CreateObjectCommand;
import org.tencompetence.ldauthor.ui.editors.common.policies.FreeFormDiagramLayoutEditPolicy;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.EnvironmentContainerEditPart;


/**
 * Policy for EnvironmentContainer
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentContainerLayoutPolicy.java,v 1.8 2008/11/20 13:46:36 phillipus Exp $
 */
public class EnvironmentContainerLayoutPolicy
extends FreeFormDiagramLayoutEditPolicy {
    
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        IGraphicalModelObject newObject = (IGraphicalModelObject)request.getNewObject();
        
        // Dont' allow certain types
        if(newObject == null || newObject instanceof IGraphicalEnvironmentModel) {
            return null;
        }
        
        newObject.setBounds((Rectangle)getConstraintFor(request));

        return new CreateObjectCommand((IGraphicalModelObjectContainer)getHost().getModel(), newObject);
    }

    @Override
    protected Command createAddCommand(EditPart child, Object constraint) {
        /*
         * This allows us to drag parts from one parent container to another.
         * This is the "add" counterpart to the "remove" Command in OrphanChildCommand
         */
        
        // Cannot embed Environments
        if(child instanceof EnvironmentContainerEditPart) {
            return null;
        }
        
        final IGraphicalModelObjectContainer gfxParent = (IGraphicalModelObjectContainer)getHost().getModel();
        final IGraphicalModelObject gfxChild = (IGraphicalModelObject)child.getModel();
        final Rectangle bounds = (Rectangle)constraint;
        if(bounds.x < 0) {
            bounds.x = 0;
        }
        if(bounds.y < 0) {
            bounds.y = 0;
        }
        
        Command cmd = new Command() {
            @Override
            public void execute() {
                gfxChild.setBounds(bounds);
                gfxParent.addChild(gfxChild, true);
            }
            
            @Override
            public void undo() {
                gfxParent.removeChild(gfxChild, true);
            }
            
            @Override
            public void redo() {
                execute();
            }
        };
        
        return cmd;
    }

}
