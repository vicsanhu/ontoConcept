package org.tencompetence.qtieditor.expression;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class Subtract extends Operator{

    public Subtract(AbstractAssessment aAssessment) {
        setType(subtract_index);
        setAssessment(aAssessment);
    }
    
	public void createEmptyOperant() {
		fOperandList.add(null);
		fOperandList.add(null);
	}

	protected String getTagName() {
		return AssessmentElementFactory.SUBTRACT;
	}    
}
