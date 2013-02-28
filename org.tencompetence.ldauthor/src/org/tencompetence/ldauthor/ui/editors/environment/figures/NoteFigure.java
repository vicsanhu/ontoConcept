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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.graphics.Color;

/**
 * Note Figure
 * 
 * @author Phillip Beauvoir
 * @version $Id: NoteFigure.java,v 1.1 2008/11/20 13:46:36 phillipus Exp $
 */
public class NoteFigure
extends Figure
implements IDefaultSizedFigure
{
    
    private static Color NOTE_COLOR = new Color(null, 255, 255, 181);

    private TextFlow nameFlow;

    public NoteFigure() {
        setToolTip(new Label(Messages.NoteFigure_0));
        setBorder(new CompoundBorder(new LineBorder() {

            @Override
            public void paint(IFigure figure, Graphics graphics, Insets insets) {
                tempRect.setBounds(getPaintRectangle(figure, insets));
                if(getWidth() % 2 == 1) {
                    tempRect.width--;
                    tempRect.height--;
                }
                tempRect.shrink(getWidth() / 2, getWidth() / 2);
                graphics.setLineWidth(getWidth());

                if(getColor() != null) {
                    graphics.setForegroundColor(getColor());
                }

                PointList list = new PointList();
                list.addPoint(tempRect.x, tempRect.y);
                list.addPoint(tempRect.x + tempRect.width - 2, tempRect.y);
                list.addPoint(tempRect.x + tempRect.width - 2, tempRect.y + tempRect.height - 12);
                list.addPoint(tempRect.x + tempRect.width - 12, tempRect.y + tempRect.height - 2);
                list.addPoint(tempRect.x, tempRect.y + tempRect.height - 2);
                graphics.drawPolygon(list);
            }
        }, new MarginBorder(3)));
        
        ToolbarLayout layout = new ToolbarLayout();
        setLayoutManager(layout);

        FlowPage page = new FlowPage();
        page.setForegroundColor(ColorConstants.black);
        setBackgroundColor(NOTE_COLOR);
        BlockFlow block = new BlockFlow();
        nameFlow = new TextFlow();
        nameFlow.setLayoutManager(new ParagraphTextLayout(nameFlow, ParagraphTextLayout.WORD_WRAP_SOFT));
        block.add(nameFlow);
        page.add(block);
        setOpaque(true);
        add(page);
        setSize(100, 20);
    }

    /**
     * Over-ride this to give us a bigger figure
     * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
     */
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        Dimension prefSize = super.getPreferredSize(wHint, hHint);
        prefSize.union(getDefaultSize());
        return prefSize;
    }

    public Dimension getDefaultSize() {
        return new Dimension(150, 60);
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        org.eclipse.draw2d.geometry.Rectangle tempRect = getBounds().getCopy();
        PointList list = new PointList();
        graphics.setBackgroundColor(ColorConstants.gray);
        list.addPoint(tempRect.x, tempRect.y);
        list.addPoint(tempRect.x + tempRect.width, tempRect.y + 2);
        list.addPoint(tempRect.x + tempRect.width, tempRect.y + tempRect.height - 12);
        list.addPoint(tempRect.x + tempRect.width - 12, tempRect.y + tempRect.height);
        list.addPoint(tempRect.x + 2, tempRect.y + tempRect.height);
        graphics.fillPolygon(list);
        list.removeAllPoints();
        list.addPoint(tempRect.x, tempRect.y);
        list.addPoint(tempRect.x + tempRect.width - 3, tempRect.y);
        list.addPoint(tempRect.x + tempRect.width - 3, tempRect.y + tempRect.height - 13);
        list.addPoint(tempRect.x + tempRect.width - 13, tempRect.y + tempRect.height - 3);
        list.addPoint(tempRect.x, tempRect.y + tempRect.height - 3);
        graphics.setBackgroundColor(NOTE_COLOR);
        graphics.fillPolygon(list);
    }

    public void setText(String text) {
        nameFlow.setText(text);
    }
}
