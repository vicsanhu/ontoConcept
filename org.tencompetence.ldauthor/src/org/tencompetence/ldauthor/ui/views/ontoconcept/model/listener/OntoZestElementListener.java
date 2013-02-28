package org.tencompetence.ldauthor.ui.views.ontoconcept.model.listener;

import org.tencompetence.ldauthor.ui.views.ontoconcept.model.GenealogyElement;

/**
 * Interface used by {@link GenealogyElement} to notify others when changes occur.
 */
public interface OntoZestElementListener
{
	void locationChanged(int x, int y);
	void sizeChanged(int width, int height);
}
