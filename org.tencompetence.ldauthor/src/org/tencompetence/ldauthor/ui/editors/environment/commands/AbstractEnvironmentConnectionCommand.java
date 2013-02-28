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

import org.eclipse.gef.commands.Command;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalConnectedModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelConnection;

/**
 * Abstract Connection Command
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractEnvironmentConnectionCommand.java,v 1.1 2007/10/13 12:20:19 phillipus Exp $
 */
public abstract class AbstractEnvironmentConnectionCommand
extends Command {

    /**
     * Determine if source object is allowed to be connected to target object
     * 
     * @param source
     * @param target
     * @return True if can execute
     */
    protected boolean canExecute(IGraphicalConnectedModelObject source, IGraphicalConnectedModelObject target) {

        if(target != null) {
            // Disallow source -> source connections
            if(source.equals(target)) {
                return false;
            }
            
            /*
             * Dissallow a connection to or from a different type
             */
            if(target.getClass() != source.getClass()) {
                return false;
            }
            
            // Make sure a cycle cannot be created
            if(checkCycles(source, target)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Checking for cycles.  Return true if there is a cycle.
     */
    protected boolean checkCycles(IGraphicalConnectedModelObject sourceToCheck, IGraphicalConnectedModelObject targetToCheck) {
        for(IGraphicalModelConnection connection : sourceToCheck.getTargetConnections()) {
            if(connection.getSource().equals(targetToCheck)) {
                return true;
            }
            if(checkCycles(connection.getSource(), targetToCheck)) {
                return true;
            }
        }
        return false;
    }
}