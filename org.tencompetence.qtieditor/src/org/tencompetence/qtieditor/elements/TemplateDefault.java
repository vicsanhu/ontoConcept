package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

import org.tencompetence.qtieditor.elements.BasicElementList;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;		
import org.tencompetence.qtieditor.elements.BasicElement;
import org.tencompetence.qtieditor.expression.Expression;

public class TemplateDefault extends BasicElement {

	private String fTemplateIdentifier = ""; // required
	private BasicElementList fExpressionList = new BasicElementList(); // required

	public TemplateDefault() {

	}


	public void setTemplateIdentifier(String aTemplateIdentifier) {
		this.fTemplateIdentifier = aTemplateIdentifier;
	}

	public String getTemplateIdentifier() {
		return fTemplateIdentifier;
	}


	public BasicElementList getExpressionList() {
		return fExpressionList;
	}

	public void addExpression(Expression aExpression) {
		fExpressionList.addElement(aExpression);
	}

	public void removeExpression(Expression aExpression) {
		fExpressionList.removeElement(aExpression);
	}

	public Expression getFirstExpression() {
		return (Expression) fExpressionList.getBasicElementAt(0);
	}


	
	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.TEMPLATE_IDENTIFIER.equals(tag)) {
				fTemplateIdentifier = value;
			}
		}

		for (Object object : element.getChildren()) {
			Element child = (Element) object;
			String tag = child.getName();
			// String value = child.getText();
/*
			if (tag.equals(AssessmentElementFactory.any expression)) {
				Expression aExpression = new Expression();
				aExpression.fromJDOM((Element) child);
				fExpressionList.addElement(aExpression);
			}
*/
		}
	}

	public Element toJDOM() {
		Element aTemplateDefault = new Element(getTagName(), getNamespace());

		aTemplateDefault.setAttribute(AssessmentElementFactory.TEMPLATE_IDENTIFIER, getTemplateIdentifier());

		for (int i = 0; i < fExpressionList.size(); i++) {
			Expression aExpression = ((Expression) fExpressionList.getBasicElementAt(i));
/*			
			if (aExpression instanceof XXXXX){
				Element aExpressionElement = ((XXXXX)aExpression).toJDOM();
				if (aExpressionElement != null)
					aTemplateDefault.addContent(aExpressionElement);
			}
*/
		}
		
		return aTemplateDefault;
	}

	public String getTagName() {
        return AssessmentElementFactory.TEMPLATE_DEFAULT;
    }	
}
