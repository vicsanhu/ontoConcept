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
package org.tencompetence.ldauthor.ui.editors.environment.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalConnectedModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelConnection;
import org.tencompetence.ldauthor.graphicsmodel.ILDModelObjectOwner;

/**
 * Abstract LD Edit Part with connections
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractConnectedLDEditPart.java,v 1.2 2009/05/19 18:21:09 phillipus Exp $
 */
public abstract class AbstractConnectedLDEditPart
extends AbstractLDEditPart
implements NodeEditPart {

    /*
     * (non-Javadoc)
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
     */
    @Override
    protected List<IGraphicalModelConnection> getModelSourceConnections() {    	
    	return ((IGraphicalConnectedModelObject)getModel()).getSourceConnections();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
     */
    @Override
    protected List<IGraphicalModelConnection> getModelTargetConnections() {
    	return ((IGraphicalConnectedModelObject)getModel()).getTargetConnections();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
     */
    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
    	return getConnectionAnchor();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
     */
    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
    	return getConnectionAnchor();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
     */
    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
    	return getConnectionAnchor();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
     */
    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
    	return getConnectionAnchor();
    }
    
    /**
     * @return The overall connection anchor to use
     */
    protected abstract ConnectionAnchor getConnectionAnchor();
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ILDModelObject ldModelObject = null;

        // If this is an LD Model Object owner we need to react to LD object model changes
        if(getModel() instanceof ILDModelObjectOwner) {
            ldModelObject = ((ILDModelObjectOwner)getModel()).getLDModelObject();
        }
        
        // React to both graphical and LD model changes
        if(evt.getSource() == getModel() || evt.getSource() == ldModelObject) {
            String prop = evt.getPropertyName();

            if(prop.startsWith(IGraphicalConnectedModelObject.PROPERTY_SOURCE)) {
                refreshSourceConnections();
            } 
            else if(prop.startsWith(IGraphicalConnectedModelObject.PROPERTY_TARGET)) {
                refreshTargetConnections();
            }
            else {
                refreshVisuals();
            }
        }
    }
}