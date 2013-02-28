package org.tencompetence.qtieditor.expression;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class NumberSelected extends ItemSubset{

    public NumberSelected(AbstractAssessment aAssessment) {
        setType(numberSelected_index);
        setAssessment(aAssessment);
    }
	
	protected String getTagName() {
		return AssessmentElementFactory.NUMBER_SELECTED;
	}
}