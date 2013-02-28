package org.tencompetence.qtieditor.expression;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class NumberResponded extends ItemSubset{

    public NumberResponded(AbstractAssessment aAssessment) {
        setType(numberResponded_index);
        setAssessment(aAssessment);
    }
	
	protected String getTagName() {
		return AssessmentElementFactory.NUMBER_RESPONDED;
	}
}