package org.tencompetence.qtieditor.expression;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class Product extends Operator {

    public Product(AbstractAssessment aAssessment) {
        setType(product_index);
        setAssessment(aAssessment);
    }
    
    public Product(AbstractAssessment aAssessment, int number) {
        for (int i = 0; i< number; i++) {
        	fOperandList.add(null);
        }
        setType(product_index);
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
		return AssessmentElementFactory.PRODUCT;
	}    

   
}

