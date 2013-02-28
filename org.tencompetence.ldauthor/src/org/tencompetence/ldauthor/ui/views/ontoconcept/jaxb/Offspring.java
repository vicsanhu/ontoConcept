package org.tencompetence.ldauthor.ui.views.ontoconcept.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "offspring")
public class Offspring {
	private String childId;
	
	@XmlAttribute(name="childId")
	public String getChildId() {
		return childId;
	}
	
	public void setChildId(String childId) {
		this.childId = childId;
	}
}
