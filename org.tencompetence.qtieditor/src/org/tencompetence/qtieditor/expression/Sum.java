package org.tencompetence.qtieditor.expression;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class Sum extends Operator {

    public Sum(AbstractAssessment aAssessment) {
        setType(sum_index);
        setAssessment(aAssessment);
    }
    
    public Sum(AbstractAssessment aAssessment, int number) {
        for (int i = 0; i< number; i++) {
        	fOperandList.add(null);
        }
        setType(sum_index);
        setAssessment(aAssessment);
    }
    
    public void addOperand(Expression aOperand) {
    	fOperandList.add(aOperand);
    }
    
    public void setOperandAt(int index, Expression aOperand) {
    	fOperandList.set(index, aOperand);
    }
    
    public void removeOperand(Expression aOperand) {
    	fOperandList.remove(aOperand);
    }
    
    public void removeOperandAt(int index) {
    	fOperandList.remove(index);
    }

	protected String getTagName() {
		return AssessmentElementFactory.SUM;
	}    
}
