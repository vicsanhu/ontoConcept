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

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.ILDModelObjectOwner;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.editors.common.directedit.EditableLabel;

/**
 * Figure for a Environment
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentContainerFigure.java,v 1.10 2009/05/22 16:35:05 phillipus Exp $
 */
public class EnvironmentContainerFigure
extends Figure
implements IGraphicalModelObjectFigure, IDefaultSizedFigure
{
    
    private Figure fMainFigure;
    
    private EditableLabel fLabel;
    
    private Label fTipLabel;
    
    private boolean fUseTransparency;
    
    private int fTransparency = 100;
    
    public EnvironmentContainerFigure(boolean useTransparency, int transparencylevel){
        fUseTransparency = useTransparency;
        setTransparency(transparencylevel);
        setup();
    }
    
    public EnvironmentContainerFigure(){
        setup();
    }
    
    protected void setup() {
        BorderLayout layout = new BorderLayout();
        setLayoutManager(layout);

        setBorder(new LineBorder(ColorConstants.black, 1));
        //setOpaque(true);
        
        add(getLabel(), BorderLayout.TOP);
        
        ScrollPane sp = new ScrollPane() {
            @Override
            public boolean isOpaque() {
                return !fUseTransparency;
            }
        };
        
        sp.setContents(getMainFigure());
        
        //sp.setBackgroundColor(ColorConstants.blue);
        
        add(sp, BorderLayout.CENTER);
        
        // Useful tips
        String tipLong = Messages.EnvironmentContainerFigure_0;
        String tipShort = Messages.EnvironmentContainerFigure_1;
        getMainFigure().setToolTip(new Label(tipLong));
        
        fTipLabel = new Label(tipShort);
        fTipLabel.setOpaque(true);
        add(fTipLabel, BorderLayout.BOTTOM);
    }
    
    public void setName(String value) {
        getLabel().setText(value);
    }
    
    /**
    * Sets the alpha to the given value.  Values may range from 0 to 255.  A value
    * of 0 is completely transparent.
    * 
    * @param alpha an alpha value (0-255)
    */
    public void setTransparency(int level) {
        if(level < 0) { 
            level = 0;
        }
        if(level > 255) { 
            level = 255;
        }
            
        fTransparency = level;
    }

    public void refreshVisuals(IGraphicalModelObject object) {
        IEnvironmentModel env = (IEnvironmentModel)((ILDModelObjectOwner)object).getLDModelObject();
        
        // Show tip label if there are no child components
        fTipLabel.setVisible(env.getChildren().isEmpty());
        
        setName(object.getName());
    }

    public IFigure getMainFigure() {
        if(fMainFigure == null) {
            fMainFigure = new Figure() {
                @Override
                protected boolean useLocalCoordinates() {
                    return true;
                }
                
                @Override
                protected void paintFigure(Graphics graphics) {
                    if(fUseTransparency) {
                        graphics.setAntialias(SWT.ON);
                        graphics.setAlpha(fTransparency);
                    }
                    super.paintFigure(graphics);
                }
            };
            fMainFigure.setLayoutManager(new XYLayout());
            fMainFigure.setBorder(new CompartmentFigureBorder());
            fMainFigure.setOpaque(true);
        }
        
        return fMainFigure;
    }

    public EditableLabel getLabel() {
        if(fLabel == null) {
            fLabel = new EditableLabel(LDModelUtils.getUserObjectName(LDModelFactory.ENVIRONMENT));
            fLabel.setToolTip(new Label(Messages.EnvironmentContainerFigure_2));
            fLabel.setIcon(ImageFactory.getImage(ImageFactory.IMAGE_ENVIRONMENT_16));
            
            fLabel.setBackgroundColor(new Color(null, 163, 196, 253));
            fLabel.setOpaque(true);
        }
        return fLabel;
    }

    /**
     * @param requestLoc
     * @return True if requestLoc is in the Main Label
     */
    public boolean didClickMainLabel(Point requestLoc) {
        EditableLabel nameLabel = getLabel();
        nameLabel.translateToRelative(requestLoc);
        // Left and Right  portions not included
        int offset = nameLabel.getBounds().width / 4;
        Rectangle insets = nameLabel.getBounds().getCopy().crop(new Insets(0, offset, 0, offset));
        return insets.contains(requestLoc);
        //return nameLabel.containsPoint(requestLoc);
    }

    public Dimension getDefaultSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize(int w, int h) {
        if(getMainFigure().getChildren().isEmpty()) {
            return new Dimension(150, 100);
        }
        else {
            return super.getPreferredSize(w, h);
        }
    }
    
    @Override
    protected boolean useLocalCoordinates() {
        return true;
    }
    
    static class CompartmentFigureBorder extends AbstractBorder {

        public Insets getInsets(IFigure figure) {
            return new Insets(2, 2, 2, 2);
        }

        public void paint(IFigure figure, Graphics graphics, Insets insets) {
            graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
        }
    }
}