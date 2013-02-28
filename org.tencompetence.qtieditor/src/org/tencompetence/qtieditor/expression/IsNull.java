package org.tencompetence.qtieditor.expression;

import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;

public class IsNull extends Expression{

    public IsNull(AbstractAssessment aAssessment) {
        setType(isNull_index);
        setAssessment(aAssessment);
    }
    
	public void createEmptyOperant() {
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
		Element aIsNullElement = new Element(getTagName(), (fAssessment instanceof AssessmentItem)? getNamespace(): getTestNamespace());

        for (int i = 0; i< fOperandList.size(); i++) {
        	Expression aOperand = (Expression)fOperandList.get(i);
            
            if (aOperand!=null) {
            	Element aOperandElement = toExpressionElement(aOperand);
    			if (aOperandElement != null)
    				aIsNullElement.addContent(aOperandElement);
            } else {
            	Element aOperandElement = toExpressionElement(new UndefinedExpression());
    			if (aOperandElement != null)
    				aIsNullElement.addContent(aOperandElement);
            }
        }
		return aIsNullElement;
	}

	protected String getTagName() {
		return AssessmentElementFactory.IS_NULL;
	}    
}
