package org.tencompetence.ldauthor.ui.views.ontoconcept.model.listener;

import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Relation;
import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Class;

/**
 * Used by {@link Relation} to notify others when changes occur.
 */
public interface RelationListener
	extends OntoZestElementListener
{
	void typeRelationChanged(String typeRelation);
	void superClassChanged(Class husband);
	void subClassChanged(Class wife);
	void offspringAdded(Class p);
	void offspringRemoved(Class p);
}
