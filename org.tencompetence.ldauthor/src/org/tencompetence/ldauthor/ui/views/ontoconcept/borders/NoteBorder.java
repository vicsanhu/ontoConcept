package org.tencompetence.ldauthor.ui.views.ontoconcept.borders;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.SWT;

/**
 * A border with the left top corder "folded" over.
 */
public class NoteBorder extends AbstractBorder {
	public static final int FOLD = 10;

	public Insets getInsets(IFigure figure) {
		return new Insets(1, 2 + FOLD, 2, 2); // top, left, bottom, right
	}

	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		Rectangle r = figure.getBounds().getCopy();
		r.crop(insets);
		graphics.setLineWidth(1);
		// solid long edges around border
		graphics.drawLine(r.x + FOLD, r.y, r.x + r.width - 1, r.y);
		graphics.drawLine(r.x, r.y + FOLD, r.x, r.y + r.height - 1);
		graphics.drawLine(r.x + r.width - 1, r.y, r.x + r.width - 1, r.y + r.height - 1);
		graphics.drawLine(r.x, r.y + r.height - 1, r.x + r.width - 1, r.y + r.height - 1);
		// solid short edges
		graphics.drawLine(r.x + FOLD, r.y, r.x + FOLD, r.y + FOLD);
		graphics.drawLine(r.x, r.y + FOLD, r.x + FOLD, r.y + FOLD);
		// gray small triangle
		graphics.setBackgroundColor(ColorConstants.lightGray);
		graphics.fillPolygon(new int[] { r.x, r.y + FOLD, r.x + FOLD, r.y,
				r.x + FOLD, r.y + FOLD });
		// dotted short diagonal line
		graphics.setLineStyle(SWT.LINE_DOT);
		graphics.drawLine(r.x, r.y + FOLD, r.x + FOLD, r.y);
	}
}
