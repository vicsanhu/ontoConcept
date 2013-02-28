package org.tencompetence.qtieditor.rule;

import org.jdom.Element;
import org.tencompetence.qtieditor.expression.Expression;
import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class ResponseElseIf extends ResponseRule{

	private Expression fExpression = null; //required

	public ResponseElseIf(AbstractAssessment aAssessment) {
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
		super.fromJDOM(element);
	}

	public Element toJDOM() {
		Element aElement = super.toJDOM();

        if (fExpression!=null) {
        	Element aExpressionElement = toExpressionElement(fExpression);
			if (aExpressionElement != null)
				aElement.addContent(aExpressionElement);
        }

        return aElement;
	}
	
	protected String getTagName() {
		return AssessmentElementFactory.RESPONSE_ELSE_IF;
	}
	
	
}
