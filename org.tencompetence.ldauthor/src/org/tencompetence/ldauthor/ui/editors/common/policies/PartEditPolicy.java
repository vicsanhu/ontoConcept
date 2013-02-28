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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalConnectedModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObjectContainer;
import org.tencompetence.ldauthor.ui.editors.common.commands.DeleteConnectedObjectCommand;
import org.tencompetence.ldauthor.ui.editors.common.commands.DeleteObjectCommand;

/**
 * Policy for Editing Parts
 * 
 * @author Phillip Beauvoir
 * @version $Id: PartEditPolicy.java,v 1.9 2007/11/22 13:36:25 phillipus Exp $
 */
public class PartEditPolicy extends ComponentEditPolicy {

    @Override
    protected Command createDeleteCommand(GroupRequest request) {
        IGraphicalModelObjectContainer parent = (IGraphicalModelObjectContainer)getHost().getParent().getModel();
        IGraphicalModelObject object = (IGraphicalModelObject)getHost().getModel();
        
        Command command;
        
        /*
         * If the object to be deleted has connections then use a different Delete command
         */
        
        if(object instanceof IGraphicalConnectedModelObject) {
            command = new DeleteConnectedObjectCommand(parent, (IGraphicalConnectedModelObject)object);
        }
        else {
            command = new DeleteObjectCommand(parent, object);
        }
        
        return command;
    }
}