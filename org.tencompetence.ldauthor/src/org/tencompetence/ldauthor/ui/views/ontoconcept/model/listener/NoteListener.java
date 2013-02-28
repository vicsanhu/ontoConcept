package org.tencompetence.ldauthor.ui.views.ontoconcept.model.listener;

import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Note;

/**
 * Used by {@link Note} to notify others when changes occur.
 */
public interface NoteListener
	extends OntoZestElementListener
{
	void textChanged(String text);
}
