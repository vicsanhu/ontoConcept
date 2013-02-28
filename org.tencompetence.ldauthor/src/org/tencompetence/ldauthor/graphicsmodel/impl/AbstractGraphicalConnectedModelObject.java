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
package org.tencompetence.ldauthor.graphicsmodel.impl;

import java.util.ArrayList;
import java.util.List;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalConnectedModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelConnection;

/**
 * Base Model Object with Coonected Properties support
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractGraphicalConnectedModelObject.java,v 1.10 2009/05/19 18:21:05 phillipus Exp $
 */
public abstract class AbstractGraphicalConnectedModelObject
extends AbstractGraphicalModelObject
implements IGraphicalConnectedModelObject
{
    
    /** 
     * List of outgoing Connections
     */
    private List<IGraphicalModelConnection> fSourceConnections = new ArrayList<IGraphicalModelConnection>();
    
    /** 
     * List of incoming Connections
     */
    private List<IGraphicalModelConnection> fTargetConnections = new ArrayList<IGraphicalModelConnection>();
    
    protected AbstractGraphicalConnectedModelObject(ILDModel ldModel) {
        super(ldModel);
    }
    
    /**
     * Add an incoming or outgoing connection to this shape.
     * @param conn a non-null connection instance
     * @throws IllegalArgumentException if the connection is null or has not distinct endpoints
     */
    public void addConnection(IGraphicalModelConnection connection, boolean notifyLDModel) {
    	if(connection == null || connection.getSource() == connection.getTarget()) {
    		throw new IllegalArgumentException();
    	}
    	if(connection.getSource() == this) {
    		fSourceConnections.add(connection);
    		firePropertyChange(this, PROPERTY_SOURCE_CONNECTION_ADDED, null, connection);
    	} 
    	else if(connection.getTarget() == this) {
    		fTargetConnections.add(connection);
    		firePropertyChange(this, PROPERTY_TARGET_CONNECTION_ADDED, null, connection);
    	}
    }

    /**
     * Remove an incoming or outgoing connection from this shape.
     * @param conn a non-null connection instance
     * @throws IllegalArgumentException if the parameter is null
     */
    public void removeConnection(IGraphicalModelConnection connection, boolean notifyLDModel) {
        if (connection == null) {
            throw new IllegalArgumentException();
        }
        if (connection.getSource() == this) {
            fSourceConnections.remove(connection);
            firePropertyChange(this, PROPERTY_SOURCE_CONNECTION_REMOVED, null, connection);
        } 
        else if (connection.getTarget() == this) {
            fTargetConnections.remove(connection);
            firePropertyChange(this, PROPERTY_TARGET_CONNECTION_REMOVED, null, connection);
        }
    }

    /**
     * Return a List of outgoing Connections.
     */
    public List<IGraphicalModelConnection> getSourceConnections() {
    	return new ArrayList<IGraphicalModelConnection>(fSourceConnections);
    }

    /**
     * Return a List of incoming Connections.
     */
    public List<IGraphicalModelConnection> getTargetConnections() {
    	return new ArrayList<IGraphicalModelConnection>(fTargetConnections);
    }
    
    public boolean hasTargetConnection(IGraphicalConnectedModelObject targetObject) {
        for(IGraphicalModelConnection connection : getSourceConnections()) {
            if(targetObject == connection.getTarget()) {
                return true;
            }
        }
        return false;
    }
}
