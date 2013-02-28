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
package org.tencompetence.ldauthor.graphicsmodel;

import java.util.List;



/**
 * Core Model Object with graphical properties and connectionsfor the Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: IGraphicalConnectedModelObject.java,v 1.6 2008/04/24 10:15:21 phillipus Exp $
 */
public interface IGraphicalConnectedModelObject
extends IGraphicalModelObject
{
    /*
     * This group of Property names should all start with "Property.Source_xxxx" or "Property.Target_xxxx" in order
     * that receivers can test the type of change.
     */
    String PROPERTY_SOURCE = "Property.SourceConnection"; //$NON-NLS-1$
    String PROPERTY_TARGET = "Property.TargetConnection"; //$NON-NLS-1$
    String PROPERTY_SOURCE_CONNECTION_ADDED = "Property.SourceConnection_added"; //$NON-NLS-1$
    String PROPERTY_SOURCE_CONNECTION_REMOVED = "Property.SourceConnection_removed"; //$NON-NLS-1$
    String PROPERTY_TARGET_CONNECTION_ADDED = "Property.TargetConnection_added"; //$NON-NLS-1$
    String PROPERTY_TARGET_CONNECTION_REMOVED = "Property.TargetConnection_removed"; //$NON-NLS-1$
    
    String getIdentifier();
    
    /**
     * Be able to report on source connections
     * @return
     */
    List<IGraphicalModelConnection> getSourceConnections();
    
    /**
     * Be able to report on target connections
     * @return
     */
    List<IGraphicalModelConnection> getTargetConnections();
    
    /**
     * this object can have connections added
     * @param connection
     * @param notifyLDModel If this is true notify the LD Model
     */
    void addConnection(IGraphicalModelConnection connection, boolean notifyLDModel);
    
    /**
     * Remove connections
     * @param connection
     * @param notifyLDModel If this is true notify the LD Model
     */
    void removeConnection(IGraphicalModelConnection connection, boolean notifyLDModel);
    
    /**
     * @param target
     * @return True if this object has a target connection with the targetObject
     */
    boolean hasTargetConnection(IGraphicalConnectedModelObject targetObject);
}