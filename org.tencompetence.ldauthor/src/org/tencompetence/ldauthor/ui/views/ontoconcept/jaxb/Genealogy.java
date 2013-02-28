package org.tencompetence.ldauthor.ui.views.ontoconcept.jaxb;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "genealogy")
@XmlType(propOrder = { "person", "marriage"})
public class Genealogy {
	private ArrayList<Person> person;
	private ArrayList<Marriage> marriage;
	
	public ArrayList<Person> getPerson() {
		return person;
	}
	
	public void setPerson(ArrayList<Person> person) {
		this.person = person;
	}
	
	public ArrayList<Marriage> getMarriage() {
		return marriage;
	} 
	
	public void setMarriage(ArrayList<Marriage> marriage) {
		this.marriage = marriage;
	}
}
