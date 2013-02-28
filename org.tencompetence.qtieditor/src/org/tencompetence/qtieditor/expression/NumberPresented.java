package org.tencompetence.qtieditor.expression;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class NumberPresented extends ItemSubset{

    public NumberPresented(AbstractAssessment aAssessment) {
        setType(numberPresented_index);
        setAssessment(aAssessment);
    }
	
	protected String getTagName() {
		return AssessmentElementFactory.NUMBER_PRESENTED;
	}
}