package org.tencompetence.qtieditor.rule;


import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.expression.Expression;


public class ResponseIf extends ResponseRule{

	private Expression fExpression = null; //required
	
	public ResponseIf(AbstractAssessment aAssessment) {
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
        Element aElement = new Element(getTagName(), getNamespace());

        if (fExpression!=null) {
        	Element aExpressionElement = toExpressionElement(fExpression);
			if (aExpressionElement != null)
				aElement.addContent(aExpressionElement);
        }

        for (int i = 0; i < fResponseRuleList.size(); i++) {
			AbstractRule aRule = ((AbstractRule)fResponseRuleList
					.getBasicElementAt(i));
			if (aRule instanceof SetOutcomeValue) {
				aElement.addContent(((SetOutcomeValue)aRule).toJDOM());
			} else if (aRule instanceof ResponseCondition) {
				aElement.addContent(((ResponseCondition)aRule).toJDOM());
			} else if (aRule instanceof ExitResponse) {
				aElement.addContent(((ExitResponse)aRule).toJDOM());
			}
		}

        return aElement;
	}

	public String getTagName() {
        return AssessmentElementFactory.RESPONSE_IF;
    }	
}

