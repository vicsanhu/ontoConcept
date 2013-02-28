package org.tencompetence.qtieditor.rule;

import org.jdom.Attribute;
import org.jdom.Element;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.BasicElementList;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;		
import org.tencompetence.qtieditor.elements.BasicElement;
import org.tencompetence.qtieditor.elements.FeedbackInline;
import org.tencompetence.qtieditor.expression.Expression;

public class BranchRule extends AbstractRule {

	private String fTarget = ""; // required
	private Expression fExpression = null; //required

	public BranchRule(AbstractAssessment aAssessment) {
		setAssessment(aAssessment);
	}

	public void setTarget(String aTarget) {
		this.fTarget = aTarget;
	}

	public String getTarget() {
		return fTarget;
	}
	
	public void setExpression(Expression aExpression) {
		fExpression = aExpression;
	}
	
	public Expression getExpression() {
		return fExpression;
	}

	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.TARGET.equals(tag)) {
				fTarget = value;
			}
		}

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

		aElement.setAttribute(AssessmentElementFactory.TARGET, getTarget());

        if (fExpression!=null) {
        	Element aExpressionElement = toExpressionElement(fExpression);
			if (aExpressionElement != null)
				aElement.addContent(aExpressionElement);
        }
		return aElement;
	}

	public String getTagName() {
        return AssessmentElementFactory.BRANCH_RULE;
    }	
}
