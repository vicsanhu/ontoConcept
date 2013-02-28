package org.tencompetence.ldauthor.ui.views.ontoconcept.model.listener;

import org.tencompetence.ldauthor.ui.views.ontoconcept.model.Note;
import org.tencompetence.ldauthor.ui.views.ontoconcept.model.NoteContainer;

/**
 * Used by {@link NoteContainer} to notify others when changes occur.
 */
public interface NoteContainerListener
{
	void noteAdded(int index, Note n);
	void noteRemoved(Note n);
}
