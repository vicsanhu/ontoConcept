package org.tencompetence.qtieditor.expression;

import org.jdom.Element;
import java.util.ArrayList;
import java.util.List;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.rule.AbstractRule;
import org.tencompetence.qtieditor.rule.RuleOrExpression;

public abstract class Expression extends RuleOrExpression {

    public final static int empty_index = 0;
    public final static int and_index = 1;
    public final static int anyN_index = 2;
    public final static int baseValue_index = 3;
    public final static int contains_index = 4;
    public final static int correct_index = 5;
    public final static int customOperator_index = 6;
    public final static int default_index = 7;
    public final static int delete_index = 8;
    public final static int divide_index = 9;
    public final static int durationGTE_index = 10;
    public final static int durationLT_index = 11;
    public final static int equal_index = 12;
    public final static int equalRounded_index = 13;
    public final static int fieldValue_index = 14;
    public final static int gt_index = 15;
    public final static int gte_index = 16;
    public final static int index_index = 17;
    public final static int insindexe_index = 18;
    public final static int integerDivindexe_index = 19;
    public final static int integerModulus_index = 20;
    public final static int integerToFloat_index = 21;
    public final static int isNull_index = 22;
    public final static int lt_index = 23;
    public final static int lte_index = 24;
    public final static int mapResponse_index = 25;
    public final static int mapResponsePoint_index = 26;
    public final static int match_index = 27;
    public final static int member_index = 28;
    public final static int multiple_index = 29;
    public final static int not_index = 30;
    public final static int null_index = 31;
    public final static int numberCorrect_index = 32;
    public final static int numberIncorrect_index = 33;
    public final static int numberPresented_index = 34;
    public final static int numberResponded_index = 35;
    public final static int numberSelected_index = 36;
    public final static int or_index = 37;
    public final static int ordered_index = 38;
    public final static int patternMatch_index = 39;
    public final static int power_index = 40;
    public final static int product_index = 41;
    public final static int random_index = 42;
    public final static int randomFloat_index = 43;
    public final static int randomInteger_index = 44;
    public final static int round_index = 45;
    public final static int stringMatch_index = 46;
    public final static int substring_index = 47;
    public final static int subtract_index = 48;
    public final static int sum_index = 49;
    public final static int testVariables_index = 50;
    public final static int truncate_index = 51;
    public final static int variable_index = 52;
    
    protected int expType = -1;
    protected Expression parentExpression;
    protected AbstractRule parentRule;    
    
	protected List<Expression> fOperandList = new ArrayList<Expression>();
	
	
    public int getType(){
        return expType;
    }
    
    public void setType(int aType){
        expType = aType;
    }
    
    public Expression getParentExpression() {
        return parentExpression;
    }
    
    public void setParentExpression(Expression parent){
        parentExpression = parent;
    }
    
    public AbstractRule getParentRule() {
        return parentRule;
    }
    
    public void setParentRule(AbstractRule parent){
        parentRule = parent;
    }
	    
    public void addOperand(Expression operand) {
        fOperandList.add(operand);
    }
    
    public void setOperandAt(int index, Expression operand) {
        if ((index > -1) || (fOperandList.size()>index)) {
            fOperandList.set(index, operand);
        }
        else {
            System.err.println("An expression has not operant at " + index);
        }
    }
    
    public Expression getOperandAt(int index) {
        if ((index > -1) || (fOperandList.size()>index)) {
            return (Expression)fOperandList.get(index);
        }
        else {
        	System.err.println("An expression has not operant at " + index);
            return null;
        }
    }
    
    public Expression getFirstOperand(){
        return getOperandAt(0);
    }
    
    public void setFirstOperand(Expression operand){
        setOperandAt(0, operand);
    }
    
    public Expression getSecondOperand(){
        return getOperandAt(1);
    }
    
    public void setSecondOperand(Expression operand){
        setOperandAt(1, operand);
    }
    
    public int getSize(){
        return fOperandList.size();
    }
    
    public void setSize(int aSize){
        for (int i = 0; i< aSize; i++) {
            fOperandList.add(null);
        }
    }
	
	public void clear() {
    	fOperandList.clear();
    }
        
    public void removeRelations() {
        for (int i=0; i<fOperandList.size(); i++) {
        	Expression op = (Expression)fOperandList.get(i);
            if (op!=null) op.removeRelations();
        }
    }
	
    
	public void fromJDOM(Element element) {

		for (Object object : element.getChildren()) {
			Element child = (Element) object;
			String tag = child.getName();
			String value = child.getText();

			fOperandList.add(createExpression(child, tag, value));
		}
	}

	public Element toJDOM() {
		Element aElement = new Element(getTagName(), (fAssessment instanceof AssessmentItem)? getNamespace(): getTestNamespace());

        for (int i = 0; i< fOperandList.size(); i++) {
        	Expression aOperand = (Expression)fOperandList.get(i);
            
            if (aOperand!=null) {
            	Element aOperandElement = toExpressionElement(aOperand);
    			if (aOperandElement != null)
    				aElement.addContent(aOperandElement);
            } else {
            	Element aOperandElement = toExpressionElement(new UndefinedExpression());
    			if (aOperandElement != null)
    				aElement.addContent(aOperandElement);
            }
        }
		return aElement;
	}	

}
