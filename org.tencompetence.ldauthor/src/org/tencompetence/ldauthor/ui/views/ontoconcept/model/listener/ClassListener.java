package org.tencompetence.ldauthor.ui.views.ontoconcept.model.listener;

import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Relation;
import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Class;

/**
 * Used by {@link Class} to notify others when changes occur.
 */
public interface ClassListener
	extends NoteContainerListener, OntoZestElementListener
{
	void nameChanged(String newName);
	void marriageChanged(Relation marriage);
	void parentsMarriageChanged(Relation marriage);
}
