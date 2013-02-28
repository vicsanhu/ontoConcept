package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

import org.tencompetence.qtieditor.elements.AssessmentElementFactory;		
import org.tencompetence.qtieditor.elements.BasicElement;

public class VariableMapping extends BasicElement {

	private String fSourceIdentifier = ""; // required
	private String fTargetIdentifier = ""; // required

	public void setSourceIdentifier(String aSourceIdentifier) {
		this.fSourceIdentifier = aSourceIdentifier;
	}

	public String getSourceIdentifier() {
		return fSourceIdentifier;
	}

	public void setTargetIdentifier(String aTargetIdentifier) {
		this.fTargetIdentifier = aTargetIdentifier;
	}

	public String getTargetIdentifier() {
		return fTargetIdentifier;
	}

	
	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.SOURCE_IDENTIFIER.equals(tag)) {
				fSourceIdentifier = value;
			} else if (AssessmentElementFactory.TARGET_IDENTIFIER.equals(tag)) {
				fTargetIdentifier = value;
			}
		}

	}

	public Element toJDOM() {
		Element aVariableMapping = new Element(getTagName(), getNamespace());

		aVariableMapping.setAttribute(AssessmentElementFactory.SOURCE_IDENTIFIER, getSourceIdentifier());
		aVariableMapping.setAttribute(AssessmentElementFactory.TARGET_IDENTIFIER, getTargetIdentifier());
		
		return aVariableMapping;
	}

	public String getTagName() {
        return AssessmentElementFactory.VARIABLE_MAPPING;
    }	
}
