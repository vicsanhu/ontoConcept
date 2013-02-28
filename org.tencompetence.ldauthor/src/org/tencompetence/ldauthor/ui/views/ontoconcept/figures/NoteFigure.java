package org.tencompetence.ldauthor.ui.views.ontoconcept.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.tencompetence.ldauthor.ui.views.ontoconcept.borders.NoteBorder;

public class NoteFigure extends Label {

	public NoteFigure(String note) {
		super(note);
		setBorder(new NoteBorder());
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		graphics.setBackgroundColor(ColorConstants.white);
		Rectangle b = getBounds();
		final int fold = NoteBorder.FOLD;
		graphics.fillRectangle(b.x + fold, b.y, b.width - fold, fold);
		graphics.fillRectangle(b.x, b.y + fold, b.width, b.height - fold);
		super.paintFigure(graphics);
	}
}
