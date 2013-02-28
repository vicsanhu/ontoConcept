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
import org.tencompetence.ldauthor.graphicsmodel.environment.impl.GraphicalEnvironmentConnection;

/**
 * A command to create a connection between two Environments
 * 
 * @author Phillip Beauvoir
 * @version $Id: CreateEnvironmentConnectionCommand.java,v 1.3 2008/04/24 10:15:30 phillipus Exp $
 */
public class CreateEnvironmentConnectionCommand
extends AbstractEnvironmentConnectionCommand {

    private IGraphicalModelConnection fConnection;
    private IGraphicalConnectedModelObject fSource;
    private IGraphicalConnectedModelObject fTarget;

    /**
     * Instantiate a command that can create a connection between two shapes.
     * 
     * @param source the source endpoint (a non-null Shape instance)
     * @throws IllegalArgumentException if source is null
     */
    public CreateEnvironmentConnectionCommand(IGraphicalConnectedModelObject source) {
        if(source == null) {
            throw new IllegalArgumentException();
        }
        fSource = source;
    }

    /**
     * Set the target endpoint for the connection.
     * @param target that target endpoint (a non-null Shape instance)
     * @throws IllegalArgumentException if target is null
     */
    public void setTarget(IGraphicalConnectedModelObject target) {
        if(target == null) {
            throw new IllegalArgumentException();
        }
        fTarget = target;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#canExecute()
     */
    @Override
    public boolean canExecute() {
        // Check for common rules
        if(!canExecute(fSource, fTarget)) {
            return false;
        }

        if(fTarget != null) {

            // Return false if the source -> target connection exists already
            for(IGraphicalModelConnection connection : fSource.getSourceConnections()) {
                if(connection.getTarget().equals(fTarget)) {
                    return false;
                }
            }

            /*
             * Dissallow bi-directional connections. We want a one way flow only
             */
            for(IGraphicalModelConnection connection : fTarget.getSourceConnections()) {
                if(connection.getTarget().equals(fSource)) {
                    return false;
                }
            }

         }
        
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        fConnection = new GraphicalEnvironmentConnection(fSource.getLDModel());
        fConnection.connect(fSource, fTarget, true);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gef.commands.Command#redo()
     */
    @Override
    public void redo() {
        fConnection.reconnect(true);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gef.commands.Command#undo()
     */
    @Override
    public void undo() {
        fConnection.disconnect(true);
    }
    
    @Override
    public String getLabel() {
        return Messages.CreateEnvironmentConnectionCommand_0;
    }

}
