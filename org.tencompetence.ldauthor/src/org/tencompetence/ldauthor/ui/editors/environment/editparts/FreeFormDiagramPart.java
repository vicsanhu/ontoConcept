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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.swt.graphics.Image;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObjectContainer;
import org.tencompetence.ldauthor.propertychange.IPropertyChangeSupport;
import org.tencompetence.ldauthor.ui.editors.common.policies.FreeFormDiagramLayoutEditPolicy;

/**
 * Root, top-level Diagram Part that has a FreeformLayer Figure
 * 
 * @author Phillip Beauvoir
 * @version $Id: FreeFormDiagramPart.java,v 1.3 2010/02/01 10:45:09 phillipus Exp $
 */
public class FreeFormDiagramPart
extends AbstractGraphicalEditPart
implements PropertyChangeListener {
    
    private Image fBackgroundImage;
    
    public void setBackGroundImage(Image image) {
        fBackgroundImage = image;
    }

    @Override
    protected List<?> getModelChildren() {
        return ((IGraphicalModelObjectContainer)getModel()).getChildren();
    }

    @Override
    public void activate() {
        if(isActive()) {
            return;
        }
        super.activate();
        ((IPropertyChangeSupport)getModel()).addPropertyChangeListener(this);
    }

    @Override
    public void deactivate() {
        if(!isActive()) {
            return;
        }
        super.deactivate();
        ((IPropertyChangeSupport)getModel()).removePropertyChangeListener(this);
    }

    @Override
    protected IFigure createFigure() {
        FreeformLayer layer = new FreeformLayer() {
            @Override
            public void paintFigure(Graphics graphics) {
                if(fBackgroundImage != null) {
                    graphics.drawImage(fBackgroundImage, 0, 0);
                }
                super.paintFigure(graphics);
            }
        };
        
        layer.setLayoutManager(new FreeformLayout());
        
        // Use manhattan lines
        ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
        // This one is an interesting router
        // cLayer.setConnectionRouter(new ShortestPathConnectionRouter(figure));
        cLayer.setConnectionRouter(new ManhattanConnectionRouter());

        return layer;
    }

    @Override
    protected void createEditPolicies() {
        // Install a custom layout policy that handles dragging things around
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new FreeFormDiagramLayoutEditPolicy());
    }

    public void propertyChange(PropertyChangeEvent event) {
        String prop = event.getPropertyName();
        if(prop.startsWith(IGraphicalModelObjectContainer.PROPERTY_CHILD)) {
            refreshChildren();
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if(adapter == SnapToHelper.class) {
            List<SnapToHelper> snapStrategies = new ArrayList<SnapToHelper>();
            
            Boolean val = (Boolean)getViewer().getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
            if(val != null && val.booleanValue()) {
                snapStrategies.add(new SnapToGuides(this));
            }
            
            // Snap to Geometry if the Ruler Guide is showing
            val = (Boolean)getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
            if(val != null && val.booleanValue()) {
                snapStrategies.add(new SnapToGeometry(this));
            }
            
            // Snap to Grid
            val = (Boolean)getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
            if(val != null && val.booleanValue()) {
                snapStrategies.add(new SnapToGrid(this));
            }

            if(snapStrategies.size() == 0) {
                return null;
            }
            
            if(snapStrategies.size() == 1) {
                return snapStrategies.get(0);
            }

            SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
            for(int i = 0; i < snapStrategies.size(); i++) {
                ss[i] = snapStrategies.get(i);
            }
            return new CompoundSnapToHelper(ss);
        }
        return super.getAdapter(adapter);
    }
    
    @Override
    public String toString() {
        return ""; //$NON-NLS-1$
    }

}