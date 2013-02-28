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
import java.beans.PropertyChangeListener;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelConnection;
import org.tencompetence.ldauthor.propertychange.IPropertyChangeSupport;
import org.tencompetence.ldauthor.ui.editors.common.commands.DeleteConnectionCommand;
import org.tencompetence.ldauthor.ui.editors.common.directedit.EditableLabel;
import org.tencompetence.ldauthor.ui.editors.common.directedit.ExtendedDirectEditManager;
import org.tencompetence.ldauthor.ui.editors.common.directedit.LabelCellEditorLocator;
import org.tencompetence.ldauthor.ui.editors.environment.figures.AbstractConnectionFigure;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;

/**
 * Abstract class for all implementations of Connection parts
 * 
 * @author Paul Sharples
 * @version $Id: AbstractLDConnectionEditPart.java,v 1.1 2008/11/20 13:46:36 phillipus Exp $
 */
public abstract class AbstractLDConnectionEditPart extends AbstractConnectionEditPart 
implements PropertyChangeListener {

    protected DirectEditManager fManager;
    
	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	@Override
    public void activate() {
		if (!isActive()) {
			super.activate();
			((IPropertyChangeSupport) getModel()).addPropertyChangeListener(this);
		}
	}
	
    /**
     * Upon deactivation, detach from the model element as a property change listener.
     */
    @Override
    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            ((IPropertyChangeSupport) getModel()).removePropertyChangeListener(this);
        }
    }

    /* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
    protected void createEditPolicies() {
		// Selection handle edit policy. 
		// Makes the connection show a feedback, when selected by the user.
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		
		// Allows the removal of the connection model element
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy() {
			@Override
            protected Command getDeleteCommand(GroupRequest request) {
				return new DeleteConnectionCommand((IGraphicalModelConnection)getModel());
			}
		});
		
        // Add a policy to handle directly editing the Relationship label
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new PartDirectEditRelationshipPolicy());
	}
		
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
	    refreshVisuals();
	}
	
    @Override
    public void performRequest(Request request) {
        if(request.getType() == RequestConstants.REQ_OPEN) {
            // Edit relationship label if we clicked on it
            if(((AbstractConnectionFigure)getFigure()).didClickRelationshipLabel(((LocationRequest)request).getLocation().getCopy())) {
                EditableLabel label = ((AbstractConnectionFigure)getFigure()).getRelationshipLabel();
                if(fManager == null) {
                    fManager = new ExtendedDirectEditManager(this, TextCellEditor.class, new LabelCellEditorLocator(label), label, null);
                }
                fManager.show();
            }
            else {
                handleEditRequest(request); 
            }
        }
    }

    protected void handleEditRequest(Request request) {
        InspectorManager.getInstance().showInspector();
    }

    @Override
    protected void refreshVisuals() {
        IGraphicalModelConnection model = (IGraphicalModelConnection)getModel();
        ((AbstractConnectionFigure)getFigure()).setRelationshipText(model.getRelationshipText());
    }
    
    /**
     * DirectEditCommand for relationship text
     */
    private class DirectEditCommand extends Command {

        private String oldName;
        private String newName;

        public DirectEditCommand(String name) {
            newName = name;
        }

        @Override
        public void execute() {
            IGraphicalModelConnection model = (IGraphicalModelConnection)getModel();
            oldName = model.getRelationshipText();
            model.setRelationshipText(newName);
        }
        
        @Override
        public String getLabel() {
            return Messages.AbstractLDConnectionEditPart_0;
        }

        @Override
        public void undo() {
            IGraphicalModelConnection model = (IGraphicalModelConnection)getModel();
            model.setRelationshipText(oldName);
        }
    }

    /**
     * Direct Edit Policy
     */
    private class PartDirectEditRelationshipPolicy extends DirectEditPolicy {

	    @Override
	    protected Command getDirectEditCommand(DirectEditRequest request) {
	        String name = (String)request.getCellEditor().getValue();
	        DirectEditCommand command = new DirectEditCommand(name);
	        return command;
	    }

	    @Override
	    protected void showCurrentEditValue(DirectEditRequest request) {
	        String value = (String)request.getCellEditor().getValue();
	        
	        if(getHostFigure() instanceof AbstractConnectionFigure) {
	            ((AbstractConnectionFigure)getHostFigure()).setRelationshipText(value);
	        }
	    }
	}
}
