package org.tencompetence.qtieditor.expression;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class NumberIncorrect extends ItemSubset{

    public NumberIncorrect(AbstractAssessment aAssessment) {
        setType(numberIncorrect_index);
        setAssessment(aAssessment);
    }
	
	protected String getTagName() {
		return AssessmentElementFactory.NUMBER_INCORRECT;
	}
}
