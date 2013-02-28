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

import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObjectContainer;
import org.tencompetence.ldauthor.ui.editors.common.directedit.EditableLabel;
import org.tencompetence.ldauthor.ui.editors.common.directedit.ExtendedDirectEditManager;
import org.tencompetence.ldauthor.ui.editors.common.directedit.LabelCellEditorLocator;
import org.tencompetence.ldauthor.ui.editors.common.policies.PartDirectEditTitlePolicy;
import org.tencompetence.ldauthor.ui.editors.environment.figures.EnvironmentContainerFigure;
import org.tencompetence.ldauthor.ui.editors.environment.figures.IGraphicalModelObjectFigure;
import org.tencompetence.ldauthor.ui.editors.environment.policies.ContainerHighlightEditPolicy;
import org.tencompetence.ldauthor.ui.editors.environment.policies.EnvironmentConnectionPolicy;
import org.tencompetence.ldauthor.ui.editors.environment.policies.EnvironmentContainerEditPolicy;
import org.tencompetence.ldauthor.ui.editors.environment.policies.EnvironmentContainerLayoutPolicy;
import org.tencompetence.ldauthor.ui.editors.environment.policies.EnvironmentContainerPartEditPolicy;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;

/**
 * Environment Container Edit Part
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentContainerEditPart.java,v 1.1 2008/11/20 13:46:36 phillipus Exp $
 */
public class EnvironmentContainerEditPart
extends AbstractConnectedLDEditPart
implements IEnvironmentEditPart {
    
    private DirectEditManager fManager;
    
    private ConnectionAnchor fAnchor;

    public EnvironmentContainerEditPart() {
    }

    @Override
    protected List<?> getModelChildren() {
        return ((IGraphicalModelObjectContainer)getModel()).getChildren();
    }

    @Override
    protected void createEditPolicies() {
        // Add a policy to handle directly editing the Parts (for example, directly renaming a part)
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new PartDirectEditTitlePolicy());

        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new EnvironmentContainerPartEditPolicy());
        
        // Install a custom layout policy that handles dragging things around and creating new objects
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new EnvironmentContainerLayoutPolicy());
        
        // Allow roles to be joined together
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new EnvironmentConnectionPolicy());
        
        // Orphaning
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new EnvironmentContainerEditPolicy());
        
        // Selection Feedback
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ContainerHighlightEditPolicy());
    }
    
    /** 
     * Edit Requests are handled here
     */
    @Override
    public void performRequest(Request request) {
        if(request.getType() == RequestConstants.REQ_OPEN) {
            // Edit name label if we clicked on it
            if(((EnvironmentContainerFigure)getFigure()).didClickMainLabel(((LocationRequest)request).getLocation().getCopy())) {
                EditableLabel nameLabel = ((EnvironmentContainerFigure)getFigure()).getLabel();
                if(fManager == null) {
                    fManager = new ExtendedDirectEditManager(this, TextCellEditor.class, new LabelCellEditorLocator(nameLabel), nameLabel, null);
                }
                fManager.show();
            }
            else {
                handleEditRequest(request); 
            }
        }
    }
    
    @Override
    public IFigure getContentPane() {
        return ((EnvironmentContainerFigure)getFigure()).getMainFigure();
    }

    @Override
    protected IFigure createFigure() {
        return new EnvironmentContainerFigure();
    }
    
    protected void handleEditRequest(Request request) {
        InspectorManager.getInstance().showInspector();
    }

    @Override
    protected ConnectionAnchor getConnectionAnchor() {
        if(fAnchor == null) {
            fAnchor = new ChopboxAnchor(getFigure());
        }
        return fAnchor;
    }

    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        
        // Refresh the figure
        ((IGraphicalModelObjectFigure)getFigure()).refreshVisuals((IGraphicalModelObject)getModel());

        // Must refresh children components
        refreshChildren();
    }
}