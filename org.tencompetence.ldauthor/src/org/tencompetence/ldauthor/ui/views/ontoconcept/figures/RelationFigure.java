package org.tencompetence.ldauthor.ui.views.ontoconcept.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.tencompetence.ldauthor.ui.views.ontoconcept.anchors.RelationAnchor;

/**
 * A custom figure for the GenealogyView visually linking related people
 */
public class RelationFigure extends PolygonShape {
	public static final int RADIUS = 26;
	private static final PointList ARROWHEAD = new PointList(new int[] { 0, 0,
			-2, 2, -2, 0, -2, -2, 0, 0 });
	
	private final Label typeRelationFigure;

	public RelationFigure(String string) {
		Rectangle r = new Rectangle(0, 0, 50, 50);
		setStart(r.getTop());
		addPoint(r.getTop());
		addPoint(r.getLeft());
		addPoint(r.getBottom());
		addPoint(r.getRight());
		addPoint(r.getTop());
		setEnd(r.getTop());
		setFill(true);
		setBackgroundColor(ColorConstants.lightGray);
		// Add 1 to include width of the border
		// otherwise the diamond's right and bottom tips are missing 1 pixel
		setPreferredSize(r.getSize().expand(1, 1));

		setLayoutManager(new StackLayout());
		typeRelationFigure = new Label(string) {
			@Override
			public boolean containsPoint(int x, int y) {
				return false;
			}
		};
		add(typeRelationFigure);
	}

	public void setTypeRelation(String typeRelation) {
		typeRelationFigure.setText(typeRelation);
	}

	/**
	 * Connect a "parent" to the receiver
	 * 
	 * @param figure
	 *            the parent
	 * @return the connection
	 */
	public PolylineConnection addParent(IFigure figure) {
		PolylineConnection connection = new PolylineConnection();
		connection.setSourceAnchor(new ChopboxAnchor(figure));
		connection.setTargetAnchor(new RelationAnchor(this));

		// Add an arrowhead decoration
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(ARROWHEAD);
		decoration.setBackgroundColor(ColorConstants.darkGray);
		connection.setTargetDecoration(decoration);

		return connection;
	}

	/**
	 * Connect a "child" to the receiver
	 * 
	 * @param figure
	 *            the child
	 * @return the connection
	 */
	public PolylineConnection addChild(IFigure figure) {
		PolylineConnection connection = new PolylineConnection();
		connection.setSourceAnchor(new RelationAnchor(this));
		connection.setTargetAnchor(new ChopboxAnchor(figure));

		// Add an arrowhead decoration
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(ARROWHEAD);
		decoration.setBackgroundColor(ColorConstants.white);
		connection.setTargetDecoration(decoration);

		return connection;
	}
}