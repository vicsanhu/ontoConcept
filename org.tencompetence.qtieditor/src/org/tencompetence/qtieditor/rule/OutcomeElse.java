package org.tencompetence.qtieditor.rule;

import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class OutcomeElse extends OutcomeRule{

	public OutcomeElse(AbstractAssessment aAssessment) {
		setAssessment(aAssessment);
	}
	
	public void fromJDOM(Element element) {
		super.fromJDOM(element);
	}

	public Element toJDOM() {
		return super.toJDOM();
	}	
	protected String getTagName() {
		return AssessmentElementFactory.OUTCOME_ELSE;
	}
}

