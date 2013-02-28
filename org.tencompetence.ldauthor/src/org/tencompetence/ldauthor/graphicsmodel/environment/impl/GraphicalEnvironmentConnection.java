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
package org.tencompetence.ldauthor.graphicsmodel.environment.impl;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.ldauthor.graphicsmodel.EnvironmentEditorGraphicalModelFactory;
import org.tencompetence.ldauthor.graphicsmodel.ILDModelObjectOwner;
import org.tencompetence.ldauthor.graphicsmodel.impl.AbstractGraphicalModelConnection;

/**
 * Environement Connection
 * 
 * @author Phillip Beauvoir
 * @version $Id: GraphicalEnvironmentConnection.java,v 1.7 2009/05/19 18:21:04 phillipus Exp $
 */
public class GraphicalEnvironmentConnection
extends AbstractGraphicalModelConnection {

	/**
     * Default Constructor
     */
	public GraphicalEnvironmentConnection(ILDModel ldModel) {
	    super(ldModel);
		setRelationshipText(Messages.GraphicalEnvironmentConnection_0);
	}
	
    public String getTagName() {
        return EnvironmentEditorGraphicalModelFactory.ENVIRONMENT_CONNECTION;
    }
    
    @Override
    public void reconnect(boolean notifyLDModel) {
        super.reconnect(notifyLDModel);
        
        if(notifyLDModel) {
            addConnectionLDModel();
        }
    }
    
    @Override
    public void disconnect(boolean notifyLDModel) {
        super.disconnect(notifyLDModel);
        
        if(notifyLDModel) {
            removeConnectionLDModel();
        }
    }

    private void addConnectionLDModel() {
        /*
         * Add an Environment Ref in the LD Model on adding a target connection
         */
        
        IEnvironmentModel env = (IEnvironmentModel)((ILDModelObjectOwner)getTarget()).getLDModelObject();
        IEnvironmentModel parentEnvironment = (IEnvironmentModel)((ILDModelObjectOwner)getSource()).getLDModelObject();
        
        ILDModelObjectReference envRef = (ILDModelObjectReference)LDModelFactory.createModelObject(LDModelFactory.ENVIRONMENT_REF, getLDModel());
        envRef.setReferenceIdentifer(env.getIdentifier());
        parentEnvironment.addChild(envRef);
    }

    private void removeConnectionLDModel() {
        if(getTarget() == null) {
            return;  // Not connected
        }
        
        /*
         * Remove the Environment Ref on removing a target connection
         */
        IEnvironmentModel env = (IEnvironmentModel)((ILDModelObjectOwner)getTarget()).getLDModelObject();
        IEnvironmentModel parentEnvironment = (IEnvironmentModel)((ILDModelObjectOwner)getSource()).getLDModelObject();
        parentEnvironment.removeChild(env); // can use actual environment because environment equals() environment-ref
    }


}
