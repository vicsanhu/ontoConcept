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
package org.tencompetence.ldauthor.ui.editors.environment.commands;

import org.tencompetence.ldauthor.graphicsmodel.IGraphicalConnectedModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelConnection;

/**
 * Reconnect Target Connection Command
 * 
 * @author Phillip Beauvoir
 * @version $Id: ReconnectTargetEnvironmentConnectionCommand.java,v 1.2 2008/04/24 10:15:30 phillipus Exp $
 */
public class ReconnectTargetEnvironmentConnectionCommand
extends AbstractEnvironmentConnectionCommand {

    private IGraphicalModelConnection fConnection;
    private IGraphicalConnectedModelObject fSource;
    private IGraphicalConnectedModelObject fNewTarget;
    private IGraphicalConnectedModelObject fOldTarget;

    public ReconnectTargetEnvironmentConnectionCommand(IGraphicalModelConnection connection, IGraphicalConnectedModelObject newTarget) {
        if(connection == null) {
            throw new IllegalArgumentException();
        }
        
        fConnection = connection;
        fSource = connection.getSource();
        fOldTarget = connection.getTarget();
        fNewTarget = newTarget;
    }

    @Override
    public boolean canExecute() {
        // If it's the same old target then OK
        if(fOldTarget == fNewTarget) {
            return true;
        }
        
        // Check for common rules
        if(!canExecute(fSource, fNewTarget)) {
            return false;
        }
        
        // Return false if the connection exists already
        for(IGraphicalModelConnection connection : fNewTarget.getTargetConnections()) {
            // Return false if a oldSource -> newTarget connection exists already
            // and it is a differenct instance that the connection-field
            if(connection.getSource().equals(fSource) && !connection.equals(fConnection)) {
                return false;
            }
        }

        /**
         * Dissallow bi-directional connections. We want a one way flow only
         */
        for(IGraphicalModelConnection connection : fNewTarget.getSourceConnections()) {
            if(connection.getTarget().equals(fSource)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Reconnect the connection to newSource (if setNewSource(...) was invoked
     * before) or newTarget (if setNewTarget(...) was invoked before).
     */
    @Override
    public void execute() {
        fConnection.connect(fSource, fNewTarget, true);
    }

    /**
     * Reconnect the connection to its original source and target endpoints.
     */
    @Override
    public void undo() {
        fConnection.connect(fSource, fOldTarget, true);
    }

    @Override
    public String getLabel() {
        return Messages.ReconnectTargetEnvironmentConnectionCommand_0;
    }
}
