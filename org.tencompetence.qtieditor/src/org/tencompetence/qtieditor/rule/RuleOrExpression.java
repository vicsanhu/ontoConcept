package org.tencompetence.qtieditor.rule;

import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.BasicElement;
import org.tencompetence.qtieditor.expression.*;

public abstract class RuleOrExpression extends BasicElement {

	protected AbstractAssessment fAssessment;
	
	public void setAssessment(AbstractAssessment aAssessment) {
		fAssessment = aAssessment;
	}

	public AbstractAssessment getAssessment() {
		return fAssessment;
	}
    
	public Expression createExpression(Element child, String tag, String value) {
				
		if (tag.equals(AssessmentElementFactory.VARIABLE)) {
			Variable aExpression = new Variable(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.TEST_VARIABLES)) {
			TestVariables aExpression = new TestVariables(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.INTEGER_TO_FLOAT)) {
			IntegerToFloat aExpression = new IntegerToFloat(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.BASE_VALUE)) {
			BaseValue aExpression = new BaseValue(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.MATCH)) {
			Match aExpression = new Match(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.NUMBER_CORRECT)) {
			NumberCorrect aExpression = new NumberCorrect(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.NUMBER_INCORRECT)) {
			NumberIncorrect aExpression = new NumberIncorrect(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.NUMBER_PRESENTED)) {
			NumberPresented aExpression = new NumberPresented(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.NUMBER_RESPONDED)) {
			NumberResponded aExpression = new NumberResponded(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.NUMBER_SELECTED)) {
			NumberSelected aExpression = new NumberSelected(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.SUM)) {
			Sum aExpression = new Sum(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.SUBTRACT)) {
			Subtract aExpression = new Subtract(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.PRODUCT)) {
			Product aExpression = new Product(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.DIVIDE)) {
			Divide aExpression = new Divide(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.CORRECT)) {
			Correct aExpression = new Correct(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.IS_NULL)) {
			IsNull aExpression = new IsNull(fAssessment);
			aExpression.fromJDOM(child);
			return aExpression;
		} else if (tag.equals(AssessmentElementFactory.UNDEFINED_EXPRESSION)) {
			UndefinedExpression aExpression = new UndefinedExpression();
			return aExpression;
		}
		return null;
	}
	
	public Element toExpressionElement(Expression operant) {
		int type_index = operant.getType();
		if (type_index == Expression.sum_index) {
			return ((Sum)operant).toJDOM();
		} else if (type_index == Expression.match_index) {
			return ((Match)operant).toJDOM();
		} else if (type_index == Expression.baseValue_index) {
			return ((BaseValue)operant).toJDOM();
		} else if (type_index == Expression.variable_index) {
			return ((Variable)operant).toJDOM();
		} else if (type_index == Expression.testVariables_index) {
			return ((TestVariables)operant).toJDOM();
		} else if (type_index == Expression.integerToFloat_index) {
			return ((IntegerToFloat)operant).toJDOM();
		} else if (type_index == Expression.numberCorrect_index) {
			return ((NumberCorrect)operant).toJDOM();
		} else if (type_index == Expression.numberIncorrect_index) {
			return ((NumberIncorrect)operant).toJDOM();
		} else if (type_index == Expression.numberPresented_index) {
			return ((NumberPresented)operant).toJDOM();
		} else if (type_index == Expression.numberResponded_index) {
			return ((NumberResponded)operant).toJDOM();
		} else if (type_index == Expression.numberSelected_index) {
			return ((NumberSelected)operant).toJDOM();
		} else if (type_index == Expression.subtract_index) {
				return ((Subtract)operant).toJDOM();			
		} else if (type_index == Expression.product_index) {
			return ((Product)operant).toJDOM();			
		} else if (type_index == Expression.divide_index) {
			return ((Divide)operant).toJDOM();			
		} else if (type_index == Expression.isNull_index) {
			return ((IsNull)operant).toJDOM();
		} else if (type_index == Expression.correct_index) {
			return ((Correct)operant).toJDOM();
		}
		return null;
	}
	
}
