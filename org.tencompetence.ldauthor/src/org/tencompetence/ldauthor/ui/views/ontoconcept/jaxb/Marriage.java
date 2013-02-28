package org.tencompetence.ldauthor.ui.views.ontoconcept.jaxb;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "marriage")
@XmlType(propOrder = { "typeRelation", "superclassId", "offspring"})
public class Marriage {
	private String typeRelation;
	private String superclassId;
	private ArrayList<Offspring> offspring;
	
	@XmlAttribute(name="typeRelation")
	public String getTypeRelation(){
		return typeRelation;
	}
	
	public void setTypeRelation(String typeRelation) {
		this.typeRelation = typeRelation;
	}
	
	@XmlAttribute(name="superclassId")
	public String getSuperclassId() {
		return superclassId;
	}
	public void setSuperclassId(String superclassId) {
		this.superclassId = superclassId;
	}
	
	public ArrayList<Offspring> getoffspring() {
		return offspring;
	}
	
	public void setoffspring(ArrayList<Offspring> offspring) {
		this.offspring = offspring;
	}
	
}
