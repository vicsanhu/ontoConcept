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
package org.tencompetence.ldauthor.ui.editors.common.policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObjectContainer;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.ui.editors.common.commands.CreateObjectCommand;
import org.tencompetence.ldauthor.ui.editors.common.commands.SetConstraintObjectCommand;
import org.tencompetence.ldauthor.ui.editors.environment.editparts.ILDEditPart;

/**
 * XYLayoutEditPolicy is a policy that constrains figures by a location and size
 * This extends it with the capability to change the location
 * 
 * @author Phillip Beauvoir
 * @version $Id: FreeFormDiagramLayoutEditPolicy.java,v 1.12 2008/11/20 13:46:36 phillipus Exp $
 */
public class FreeFormDiagramLayoutEditPolicy
extends XYLayoutEditPolicy {
    
    /* (non-Javadoc)
     * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.requests.ChangeBoundsRequest, org.eclipse.gef.EditPart, java.lang.Object)
     */
    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
        if(child instanceof ILDEditPart && constraint instanceof Rectangle) {
            // Return a command that can move and/or resize a Shape
            Command command = new SetConstraintObjectCommand((IGraphicalModelObject)child.getModel(),
                    request,
                    (Rectangle)constraint);
            return command;
        }
        
        return super.createChangeConstraintCommand(request, child, constraint);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
     */
    @Override
    protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
     */
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        IGraphicalModelObject newObject = (IGraphicalModelObject)request.getNewObject();
        
        // We might get a null object from the CreationFactory, for example Environment Refs
        if(newObject == null) {
            return null;
        }
        
        newObject.setBounds((Rectangle)getConstraintFor(request));

        return new CreateObjectCommand((IGraphicalModelObjectContainer)getHost().getModel(), newObject);
    }

}