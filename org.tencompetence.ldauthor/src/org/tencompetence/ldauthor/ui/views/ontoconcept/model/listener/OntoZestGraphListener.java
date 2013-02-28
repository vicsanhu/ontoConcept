package org.tencompetence.ldauthor.ui.views.ontoconcept.model.listener;

import org.tencompetence.ldauthor.ui.views.ontoconcept.model.GenealogyGraph;
import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Relation;
import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Class;

/**
 * Used by {@link GenealogyGraph} to notify others when changes occur.
 */
public interface OntoZestGraphListener
	extends NoteContainerListener
{
	void classAdded(Class p);
	void classRemoved(Class p);
	void relationAdded(Relation m);
	void relationRemoved(Relation m);
	void graphCleared();
}
