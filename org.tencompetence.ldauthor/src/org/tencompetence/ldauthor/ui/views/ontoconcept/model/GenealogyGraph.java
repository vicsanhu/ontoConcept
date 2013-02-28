package org.tencompetence.ldauthor.ui.views.ontoconcept.model;

import java.util.*;

import org.tencompetence.ldauthor.ui.views.ontoconcept.model.listener.OntoZestGraphListener;

/**
 * The root model object representing a genealogy graph and directly or indirectly
 * containing all other model objects.
 */
public class GenealogyGraph
	implements NoteContainer
{
	private final Collection<Class> people = new HashSet<Class>();
	private final Collection<Relation> marriages = new HashSet<Relation>();
	private final List<Note> notes = new ArrayList<Note>();
	private final Collection<OntoZestGraphListener> listeners = new HashSet<OntoZestGraphListener>();
	
	/**
	 * Discard all elements so that new information can be loaded
	 */
	public void clear() {
		people.clear();
		marriages.clear();
		notes.clear();
		for (OntoZestGraphListener l : listeners)
			l.graphCleared();
	}

	//============================================================
	// People

	public Collection<Class> getPeople() {
		return people;
	}

	public boolean addPerson(Class p) {
		if (p == null || !people.add(p))
			return false;
		for (OntoZestGraphListener l : listeners)
			l.classAdded(p);
		return true;
	}

	public boolean removePerson(Class p) {
		if (!people.remove(p))
			return false;
		for (OntoZestGraphListener l : listeners)
			l.classRemoved(p);
		return true;
	}

	//============================================================
	// Marriages

	public Collection<Relation> getMarriages() {
		return marriages;
	}

	public boolean addMarriage(Relation m) {
		if (m == null || !marriages.add(m))
			return false;
		for (OntoZestGraphListener l : listeners)
			l.relationAdded(m);
		return true;
	}

	public boolean removeMarriage(Relation m) {
		if (!marriages.remove(m))
			return false;
		for (OntoZestGraphListener l : listeners)
			l.relationRemoved(m);
		return true;
	}

	//============================================================
	// Notes
	
	public List<Note> getNotes() {
		return notes;
	}

	public boolean addNote(Note n) {
		return addNote(notes.size(), n);
	}

	public boolean addNote(int index, Note n) {
		if (n == null || notes.contains(n))
			return false;
		notes.add(index, n);
		for (OntoZestGraphListener l : listeners)
			l.noteAdded(index, n);
		return true;
	}

	public boolean removeNote(Note n) {
		if (n == null || !notes.remove(n))
			return false;
		for (OntoZestGraphListener l : listeners)
			l.noteRemoved(n);
		return true;
	}

	//============================================================
	// Listeners
	
	public void addGenealogyGraphListener(OntoZestGraphListener l) {
		listeners.add(l);
	}
	
	public void removeGenealogyGraphListener(OntoZestGraphListener l) {
		listeners.remove(l);
	}
}
