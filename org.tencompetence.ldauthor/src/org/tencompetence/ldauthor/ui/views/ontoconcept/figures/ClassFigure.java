package org.tencompetence.ldauthor.ui.views.ontoconcept.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;

@SuppressWarnings("nls")
public class ClassFigure extends Figure {

	public static final Image MALE = new Image(Display.getCurrent(),
			ClassFigure.class.getResourceAsStream("male.png"));
	public static final Image FEMALE = new Image(Display.getCurrent(),
			ClassFigure.class.getResourceAsStream("female.png"));
	
	private final Label nameFigure;
	
	public ClassFigure(String name, Image image) {
		final ToolbarLayout layout = new ToolbarLayout();
		layout.setSpacing(1);
		setLayoutManager(layout);
		setPreferredSize(100, 100);
		setBorder(new CompoundBorder(
			new LineBorder(1),
			new MarginBorder(2, 2, 2, 2)));
		
		IFigure imageNameDates = new Figure();
		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 1;
		imageNameDates.setLayoutManager(gridLayout);
		add(imageNameDates);
		imageNameDates.add(new ImageFigure(image));
		
		IFigure nameDates = new Figure();
		nameDates.setLayoutManager(new ToolbarLayout());
		imageNameDates.add(nameDates, new GridData(GridData.FILL_HORIZONTAL));
		nameFigure = new Label(name);
		nameDates.add(nameFigure);
	}

	@Override
	public void paintFigure(Graphics graphics) {
		Rectangle r = getBounds();
		graphics.setBackgroundPattern(new Pattern(Display.getCurrent(), r.x,
				r.y, r.x + r.width, r.y + r.height, ColorConstants.white,
				ColorConstants.lightGray));
		graphics.fillRectangle(r);
	}

	public void setName(String newName) {
		nameFigure.setText(newName);
	}
}
