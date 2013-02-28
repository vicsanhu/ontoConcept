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
package org.tencompetence.ldauthor.ui.editors.environment.figures;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.ui.editors.common.directedit.EditableLabel;

/**
 * Figure for an Icon type figure with main image and text at the bottom
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractIconTypeFigure.java,v 1.1 2008/11/20 13:46:36 phillipus Exp $
 */
public abstract class AbstractIconTypeFigure
extends Figure
implements IDefaultSizedFigure, IIconTypeFigure
{
    private EditableLabel fLabel;
    
    private Label fImageLabel;
    
 
    public AbstractIconTypeFigure(){
        setup();
    }
    
    /**
     * Set up the layout and main label
     */
    protected void setup() {
        BorderLayout layout = new BorderLayout();
        setLayoutManager(layout);
        add(getImageLabel(), BorderLayout.CENTER);
        add(getTextLabel(), BorderLayout.BOTTOM);
    }
    
    /* (non-Javadoc)
     * @see org.tencompetence.ldauthor.ui.editors.common.figures.IIconTypeFigure#getTextLabel()
     */
    public EditableLabel getTextLabel() {
        if(fLabel == null) {
            fLabel = new EditableLabel(""); //$NON-NLS-1$
            //fLabel.setToolTip(new Label("Double-click to edit"));
        }
        return fLabel;
    }
    
    /* (non-Javadoc)
     * @see org.tencompetence.ldauthor.ui.editors.common.figures.IIconTypeFigure#getImageLabel()
     */
    public Label getImageLabel() {
        if(fImageLabel == null) {
            fImageLabel = new Label(getDefaultImage());
            fImageLabel.setOpaque(true);
        }
        return fImageLabel;
    }
    
    /**
     * Set the Image for the Image label
     * 
     * @param image
     */
    public void setImage(Image image) {
        getImageLabel().setIcon(image);
    }
    
    /**
     * Refresh the visuals
     * @param object
     */
    public void refreshVisuals(IGraphicalModelObject object) {
        if(object != null) {
            setName(object.getName());

            // Image label tooltip
            if(getImageLabel().getToolTip() == null) {
                getImageLabel().setToolTip(new Label(getToolTipText(object)));
            }
            else {
                ((Label)getImageLabel().getToolTip()).setText(getToolTipText(object));
            }
        }
    }

    public void setName(String value) {
        getTextLabel().setText(value);
        
        // Text Label tooltip
        if(getTextLabel().getToolTip() == null) {
            getTextLabel().setToolTip(new Label(value));
        }
        else {
            ((Label)getTextLabel().getToolTip()).setText(value);
        }
    }

    public boolean didClickTextLabel(Point requestLoc) {
        EditableLabel nameLabel = getTextLabel();
        nameLabel.translateToRelative(requestLoc);
        return nameLabel.containsPoint(requestLoc);
    }
    
    /**
     * @param object
     * @return The Tooltip text for the main body
     */
    protected String getToolTipText(IGraphicalModelObject object) {
        return object == null ? null : object.getName();
    }
}