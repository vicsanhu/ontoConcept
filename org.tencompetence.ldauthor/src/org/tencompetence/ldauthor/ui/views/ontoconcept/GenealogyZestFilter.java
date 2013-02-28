package org.tencompetence.ldauthor.ui.views.ontoconcept;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Class;

/**
 * Filter the {@link GenealogyZestView}'s content based upon the specified gender.
 */
class GenealogyZestFilter extends ViewerFilter
{
	private final String name;

	GenealogyZestFilter(String string) {
		this.name = string;
	}

	@Override
	public boolean select(Viewer viewer, Object parent, Object element) {
		if (element instanceof Class) {
			Class p = (Class) element;
			return p.getName().toLowerCase().contains(name);
		}
		return true;
	}
}