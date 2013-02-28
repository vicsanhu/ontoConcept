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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalConnectedModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelConnection;
import org.tencompetence.ldauthor.ui.editors.environment.commands.CreateEnvironmentConnectionCommand;
import org.tencompetence.ldauthor.ui.editors.environment.commands.ReconnectSourceEnvironmentConnectionCommand;
import org.tencompetence.ldauthor.ui.editors.environment.commands.ReconnectTargetEnvironmentConnectionCommand;

/**
 * ConnectionPolicy is a policy which allows a component to be connected
 * to another component via a line (connection)
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentConnectionPolicy.java,v 1.1 2007/10/13 12:20:19 phillipus Exp $
 */
public class EnvironmentConnectionPolicy extends GraphicalNodeEditPolicy {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
     */
    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        CreateEnvironmentConnectionCommand cmd = (CreateEnvironmentConnectionCommand)request.getStartCommand();
        cmd.setTarget((IGraphicalConnectedModelObject)getHost().getModel());
        return cmd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCreateCommand(org.eclipse.gef.requests.CreateConnectionRequest)
     */
    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        IGraphicalConnectedModelObject source = (IGraphicalConnectedModelObject)getHost().getModel();
        Command cmd = new CreateEnvironmentConnectionCommand(source);
        request.setStartCommand(cmd);
        return cmd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
     */
    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        IGraphicalModelConnection connection = (IGraphicalModelConnection)request.getConnectionEditPart().getModel();
        IGraphicalConnectedModelObject newSource = (IGraphicalConnectedModelObject)getHost().getModel();
        return new ReconnectSourceEnvironmentConnectionCommand(connection, newSource);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
     */
    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        IGraphicalModelConnection connection = (IGraphicalModelConnection)request.getConnectionEditPart().getModel();
        IGraphicalConnectedModelObject newTarget = (IGraphicalConnectedModelObject)getHost().getModel();
        return new ReconnectTargetEnvironmentConnectionCommand(connection, newTarget);
    }

}
