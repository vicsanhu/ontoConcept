package org.tencompetence.qtieditor.rule;

import org.jdom.Element;
import org.jdom.Attribute;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class ExitResponse extends ResponseRule{

	public ExitResponse(AbstractAssessment aAssessment) {
		setAssessment(aAssessment);
	}

	public void fromJDOM(Element element) {
	}

	public Element toJDOM() {
		Element aElement = new Element(getTagName(), getNamespace());
		
		return aElement;
	}
	
	public String getTagName() {
        return AssessmentElementFactory.EXIT_RESPONSE;
    }	
}
