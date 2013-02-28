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

import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.ILDModelObjectOwner;
import org.tencompetence.ldauthor.propertychange.IPropertyChangeSupport;

/**
 * Abstract LD Edit Part
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractLDEditPart.java,v 1.2 2009/05/19 18:21:09 phillipus Exp $
 */
public abstract class AbstractLDEditPart
extends AbstractGraphicalEditPart
implements ILDEditPart, PropertyChangeListener {

    @Override
    public void activate() {
        if(isActive()) {
            return;
        }
        
        super.activate();

        // Listen to changes in Graphics Model
        ((IPropertyChangeSupport)getModel()).addPropertyChangeListener(this);
        
        // Listen to changes in LD Model
        ((IGraphicalModelObject)getModel()).getLDModel().addPropertyChangeListener(this);
    }

    // override deactivate to deregister with the model
    @Override
    public void deactivate() {
        if(!isActive()) {
            return;
        }
        
        super.deactivate();
        
        // Remove Listener to changes in Graphics Model
        ((IPropertyChangeSupport)getModel()).removePropertyChangeListener(this);
        
        // Remove Listener to changes in LD Model
        ((IGraphicalModelObject)getModel()).getLDModel().removePropertyChangeListener(this);
    }

    @Override
    protected void refreshVisuals() {
        /*
         * We need to set the bounds in the LayoutManager.
         * Tells the parent part that this part and its figure are to be constrained to the given rectangle.
         */ 
        GraphicalEditPart parentEditPart = (GraphicalEditPart)getParent();

        IGraphicalModelObject object = (IGraphicalModelObject)getModel();
        Rectangle bounds = object.getBounds();
        
        if(parentEditPart.getFigure().getLayoutManager() instanceof XYLayout) {
            parentEditPart.setLayoutConstraint(this, getFigure(), bounds);
        }
        // Content Pane Figure needs laying out
        else if(parentEditPart.getContentPane().getLayoutManager() instanceof XYLayout) {
            parentEditPart.getContentPane().setConstraint(getFigure(), bounds);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        ILDModelObject ldModelObject = null;

        // If this is an LD Model Object owner we need to react to LD object model changes
        if(getModel() instanceof ILDModelObjectOwner) {
            ldModelObject = ((ILDModelObjectOwner)getModel()).getLDModelObject();
        }
        
        // React to both graphical and LD model changes
        if(evt.getSource() == getModel() || evt.getSource() == ldModelObject) {
            refreshVisuals();
        }
    }
}