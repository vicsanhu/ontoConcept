package org.tencompetence.ldauthor.ui.views.ontoconcept.model;

import java.util.*;

import org.tencompetence.ldauthor.ui.views.ontoconcept.model.listener.RelationListener;

public final class Relation extends GenealogyElement
{
	private String typeRelation;
	private Class superClass;
	private Class subClass;
	private final Collection<Class> offspring = new HashSet<Class>();
	private final Collection<RelationListener> listeners = new HashSet<RelationListener>();

	public Relation() {
	}

	public Relation(String year) {
		setTypeRelation(year);
	}

	public String getTypeRelation() {
		return typeRelation;
	}

	public boolean setTypeRelation(String newTypeRelation) {
		if (typeRelation == newTypeRelation)
			return false;
		typeRelation = newTypeRelation;
		for (RelationListener l : listeners)
			l.typeRelationChanged(typeRelation);
		return false;
	}

	public Class getSuperClass() {
		return superClass;
	}
	
	public boolean setSuperClass(Class newSuperClass) {
		if (newSuperClass == null)
			return false;
		superClass = newSuperClass;
		if (superClass != null)
			superClass.setMarriage(this);
		for (RelationListener l : listeners)
			l.superClassChanged(superClass);
		return true;
	}
	
	public Class getSubClass() {
		return subClass;
	}
	
	public boolean setSubClass(Class newSubClass) {
		if (newSubClass == null)
			return false;
		subClass = newSubClass;
		if (subClass != null)
			subClass.setMarriage(this);
		for (RelationListener l : listeners)
			l.subClassChanged(subClass);
		return true;
	}

	//============================================================
	// Offspring
	
	public Collection<Class> getOffspring() {
		return offspring;
	}
	
	public boolean addOffspring(Class p) {
		if (p == null || !offspring.add(p))
			return false;
		p.setParentsMarriage(this);
		for (RelationListener l : listeners)
			l.offspringAdded(p);
		return true;
	}
	
	public boolean removeOffspring(Class p) {
		if (!offspring.remove(p))
			return false;
		p.setParentsMarriage(null);
		for (RelationListener l : listeners)
			l.offspringRemoved(p);
		return true;
	}

	//============================================================
	// Listeners
	
	public void addMarriageListener(RelationListener l) {
		listeners.add(l);
	}
	
	public void removeMarriageListener(RelationListener l) {
		listeners.remove(l);
	}

	//============================================================
	// GenealogyElement
	
	@Override
	protected void fireLocationChanged(int newX, int newY) {
		for (RelationListener l : listeners)
			l.locationChanged(newX, newY);
	}

	@Override
	protected void fireSizeChanged(int newWidth, int newHeight) {
		for (RelationListener l : listeners)
			l.sizeChanged(newWidth, newHeight);
	}
}
