package org.tencompetence.ldauthor.ui.views.ontoconcept.model;

import java.util.*;

import org.tencompetence.ldauthor.ui.views.ontoconcept.model.listener.ClassListener;

/**
 * A person in the {@link GenealogyGraph}
 */

@SuppressWarnings("nls")
public class Class extends GenealogyElement
	implements NoteContainer
{
	public enum Gender {
		MALE, FEMALE
	};

	private String name = "";
	private final Gender gender;
	private Relation marriage;
	private Relation parentsMarriage;
	private final List<Note> notes = new ArrayList<Note>();
	private final Collection<ClassListener> listeners = new HashSet<ClassListener>();

	public Class(Gender gender) {
		this.gender = gender;
	}

	public Gender getGender() {
		return gender;
	}

	//============================================================
	// Name

	public String getName() {
		return name;
	}

	public boolean setName(String newName) {
		if (newName == null)
			newName = "";
		if (name.equals(newName))
			return false;
		name = newName;
		for (ClassListener l : listeners)
			l.nameChanged(newName);
		return true;
	}

	//============================================================
	// Birth and Death


	//============================================================
	// Marriage and Offspring

	/**
	 * Answer the marriage for which this person is a husband or wife or <code>null</code>
	 * if this person is not currently a husband or wife
	 */
	public Relation getMarriage() {
		return marriage;
	}

	/**
	 * Set the marriage for which this person is a husband or wife or <code>null</code> if
	 * this person is not currently a husband or wife
	 */
	public boolean setMarriage(Relation newMarriage) {
		if (marriage == newMarriage)
			return false;
		if (marriage != null) {
			Relation oldMarriage = marriage;
			marriage = null;
			if (getGender() == Gender.MALE)
				oldMarriage.setSuperClass(null);
			else
				oldMarriage.setSubClass(null);
		}
		marriage = newMarriage;
		if (marriage != null) {
			if (getGender() == Gender.MALE)
				marriage.setSuperClass(this);
			else
				marriage.setSubClass(this);
		}
		for (ClassListener l : listeners)
			l.marriageChanged(marriage);
		return true;
	}

	/**
	 * Answer the marriage for which this person is an offspring or <code>null</code> if
	 * this person is not currently recorded as an offspring.
	 */
	public Relation getParentsMarriage() {
		return parentsMarriage;
	}

	/**
	 * Set the marriage for which this person is an offspring or <code>null</code> if this
	 * person is not currently recorded as an offspring.
	 */
	public boolean setParentsMarriage(Relation newParentMarriage) {
		if (parentsMarriage == newParentMarriage)
			return false;
	
		parentsMarriage = newParentMarriage;
		if (parentsMarriage != null)
			parentsMarriage.addOffspring(this);
		for (ClassListener l : listeners)
			l.parentsMarriageChanged(marriage);
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
		for (ClassListener l : listeners)
			l.noteAdded(index, n);
		return true;
	}

	public boolean removeNote(Note n) {
		if (n == null || !notes.remove(n))
			return false;
		for (ClassListener l : listeners)
			l.noteRemoved(n);
		return true;
	}

	//============================================================
	// Listeners
	
	public void addPersonListener(ClassListener l) {
		listeners.add(l);
	}
	
	public void removePersonListener(ClassListener l) {
		listeners.remove(l);
	}
	
	//============================================================
	// GenealogyElement

	@Override
	protected void fireLocationChanged(int newX, int newY) {
		for (ClassListener l : listeners)
			l.locationChanged(newX, newY);
	}

	@Override
	protected void fireSizeChanged(int newWidth, int newHeight) {
		for (ClassListener l : listeners)
			l.sizeChanged(newWidth, newHeight);
	}
}
