package org.tencompetence.qtieditor.expression;

import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;

public class Match extends Expression{

    public Match(AbstractAssessment aAssessment) {
        setType(match_index);
        setAssessment(aAssessment);
        
    }
    
	public void createEmptyOperant() {
		fOperandList.add(null);
		fOperandList.add(null);
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
		Element aMatchElement = new Element(getTagName(), (fAssessment instanceof AssessmentItem)? getNamespace(): getTestNamespace());

        for (int i = 0; i< fOperandList.size(); i++) {
        	Expression aOperand = (Expression)fOperandList.get(i);
            
            if (aOperand!=null) {
            	Element aOperandElement = toExpressionElement(aOperand);
    			if (aOperandElement != null)
    				aMatchElement.addContent(aOperandElement);
            } else {
            	Element aOperandElement = toExpressionElement(new UndefinedExpression());
    			if (aOperandElement != null)
    				aMatchElement.addContent(aOperandElement);           	
            }
        }
		return aMatchElement;
	}

	protected String getTagName() {
		return AssessmentElementFactory.MATCH;
	}    
}
