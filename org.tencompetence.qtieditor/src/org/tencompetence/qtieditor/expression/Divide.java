package org.tencompetence.qtieditor.expression;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class Divide extends Operator{

    public Divide(AbstractAssessment aAssessment) {
        setType(divide_index);
        setAssessment(aAssessment);
        
    }
    
	public void createEmptyOperant() {
		fOperandList.add(null);
		fOperandList.add(null);
	}

	protected String getTagName() {
		return AssessmentElementFactory.DIVIDE;
	}    
}

