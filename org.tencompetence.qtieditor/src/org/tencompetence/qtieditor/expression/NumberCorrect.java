package org.tencompetence.qtieditor.expression;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class NumberCorrect extends ItemSubset{

    public NumberCorrect(AbstractAssessment aAssessment) {
        setType(numberCorrect_index);
        setAssessment(aAssessment);
    }
	
	protected String getTagName() {
		return AssessmentElementFactory.NUMBER_CORRECT;
	}
}
