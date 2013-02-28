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
package org.tencompetence.ldauthor.ui.editors.common.commands;

import java.util.ArrayList;
import java.util.List;

import org.tencompetence.ldauthor.graphicsmodel.IGraphicalConnectedModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelConnection;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObjectContainer;

/**
 * Command for deleting an Object
 * 
 * @author Paul Sharples
 * @author Phillip Beauvoir
 * @version $Id: DeleteConnectedObjectCommand.java,v 1.9 2007/11/22 13:36:21 phillipus Exp $
 */
public class DeleteConnectedObjectCommand
extends DeleteObjectCommand {

    private List<IGraphicalModelConnection> fSourceConnections = new ArrayList<IGraphicalModelConnection>();
    private List<IGraphicalModelConnection> fTargetConnections = new ArrayList<IGraphicalModelConnection>();
	
    public DeleteConnectedObjectCommand(IGraphicalModelObjectContainer parent, IGraphicalConnectedModelObject object) {
        super(parent, object);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
    	// Store a copy of incoming & outgoing connections before proceeding 
    	fSourceConnections.addAll(((IGraphicalConnectedModelObject)getModelObject()).getSourceConnections());
    	fTargetConnections.addAll(((IGraphicalConnectedModelObject)getModelObject()).getTargetConnections());
        
        // Remove the child
        super.execute();
        
        // And Remove the connections
        removeConnections(fSourceConnections);
        removeConnections(fTargetConnections);
    }
    
    @Override
    public void undo() {
        super.undo();
        
        // Reconnect connections
        addConnections(fSourceConnections);
        addConnections(fTargetConnections);
        
        // Unstore connections
        fSourceConnections.clear();
        fTargetConnections.clear();
    }
       
    /**
     * Reconnects a List of Connections with their previous endpoints.
     * @param connections a non-null List of connections
     */
    private void addConnections(List<IGraphicalModelConnection> connections) {
        for(IGraphicalModelConnection conn : connections) {
            conn.reconnect(true);
        }
    }
    
    /**
     * Disconnects a List of Connections from their endpoints.
     * @param connections a non-null List of connections
     */
    private void removeConnections(List<IGraphicalModelConnection> connections) {
        for(IGraphicalModelConnection conn : connections) {
            conn.disconnect(true);
        }
    }
}