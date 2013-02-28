package org.tencompetence.qtieditor.expression;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class IntegerToFloat extends Operator {

    public IntegerToFloat(AbstractAssessment aAssessment) {
        setType(integerToFloat_index);
        setAssessment(aAssessment);
        fOperandList.add(null);
    }
    
	protected String getTagName() {
		return AssessmentElementFactory.INTEGER_TO_FLOAT;
	}    
}    
    
