package org.tencompetence.ldauthor.ui.views.ontoconcept.model;

import java.util.*;

import org.tencompetence.ldauthor.ui.views.ontoconcept.model.listener.NoteListener;

@SuppressWarnings("nls")
public final class Note extends GenealogyElement
{
	private String text = "";
	private final Collection<NoteListener> listeners = new HashSet<NoteListener>();
	
	public Note() {
	}

	public Note(String text) {
		setText(text);
	}

	public String getText() {
		return text;
	}
	
	public boolean setText(String newText) {
		if (newText == null)
			newText = "";
		if (text.equals(newText))
			return false;
		text = newText;
		for (NoteListener l : listeners)
			l.textChanged(text);
		return true;
	}

	//============================================================
	// Listeners
	
	public void addNoteListener(NoteListener l) {
		listeners.add(l);
	}
	
	public void removeNoteListener(NoteListener l) {
		listeners.remove(l);
	}

	//============================================================
	// GenealogyElement
	
	@Override
	protected void fireLocationChanged(int newX, int newY) {
		for (NoteListener l : listeners)
			l.locationChanged(newX, newY);
	}

	@Override
	protected void fireSizeChanged(int newWidth, int newHeight) {
		for (NoteListener l : listeners)
			l.sizeChanged(newWidth, newHeight);
	}
}
