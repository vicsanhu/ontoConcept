package org.tencompetence.ldauthor.ui.views.ontoconcept;

import java.util.ArrayList;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.*;

import org.tencompetence.ldauthor.ui.views.ontoconcept.model.*;
import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Class;

/**
 * Content provider for {@link GenealogyZestView}. The getElements(...) method returns the
 * elements in the diagram and the getConnectedTo(...) method returns to which elements
 * the specified element is connected. This content provider is best for models that do
 * not have elements representing the connections between other objects.
 * <p>
 * Also see {@link GenealogyZestContentProvider2} and
 * {@link GenealogyZestContentProvider3}
 */
class GenealogyZestContentProvider
	implements IGraphEntityContentProvider, INestedContentProvider
{
	/**
	 * Adjust the receiver based upon new input
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/**
	 * Answer the people and marriages to be displayed
	 */
	public Object[] getElements(Object input) {
		ArrayList<Object> results = new ArrayList<Object>();
		if (input instanceof GenealogyGraph) {
			GenealogyGraph graph = (GenealogyGraph) input;
			results.addAll(graph.getPeople());
			results.addAll(graph.getMarriages());
		}
		return results.toArray();
	}

	/**
	 * Given a person or a marriage, answer to which other people or marriages that object
	 * is connected.
	 */
	public Object[] getConnectedTo(Object element) {
		ArrayList<Object> results = new ArrayList<Object>();
		if (element instanceof Class) {
			Class p = (Class) element;
			if (p.getMarriage() != null)
				results.add(p.getMarriage());
		}
		if (element instanceof Relation) {
			Relation m = (Relation) element;
			results.addAll(m.getOffspring());
		}
		return results.toArray();
	}

	/**
	 * Answer <code>true</code> if the specified model element has children.
	 */
	public boolean hasChildren(Object element) {
		/*if (element instanceof Class)
			return true;*/
		return false;
	}

	/**
	 * Answer the child model elements of the specified model element. This method is only called
	 * if the hasChildren method returns true for the specified element.
	 */
	public Object[] getChildren(Object element) {
		if (element instanceof Class)
			return ((Class) element).getNotes().toArray();
		return null;
	}

	/**
	 * Cleanup the receiver for proper garbage collection by discarding any cached content
	 * and unhooking any listeners.
	 */
	public void dispose() {
	}
}