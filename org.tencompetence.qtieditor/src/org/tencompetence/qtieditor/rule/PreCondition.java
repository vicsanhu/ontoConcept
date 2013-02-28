package org.tencompetence.qtieditor.rule;

import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.expression.Expression;

public class PreCondition extends AbstractRule {

	private Expression fExpression = null; //required

	public PreCondition(AbstractAssessment aAssessment) {
		setAssessment(aAssessment);
	}
	public void setExpression(Expression aExpression) {
		fExpression = aExpression;
	}
	
	public Expression getExpression() {
		return fExpression;
	}
	
	public void fromJDOM(Element element) {
		for (Object object : element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();
			String value = child.getText();

			Expression aExpression = createExpression(child, tag, value);
			if (aExpression != null) {
				fExpression = aExpression;
				break;
			}
		}
	}

	public Element toJDOM() {
		Element aElement = new Element(getTagName(), getTestNamespace());

        if (fExpression!=null) {
        	Element aExpressionElement = toExpressionElement(fExpression);
			if (aExpressionElement != null)
				aElement.addContent(aExpressionElement);
        }

        return aElement;
	}
	
	public String getTagName() {
        return AssessmentElementFactory.PRE_CONDITION;
    }	
}
