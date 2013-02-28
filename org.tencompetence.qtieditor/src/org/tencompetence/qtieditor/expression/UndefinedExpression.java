package org.tencompetence.qtieditor.expression;

import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class UndefinedExpression extends Expression{

	public UndefinedExpression() {
		setType(empty_index);
	}
	
	public void fromJDOM(Element element) {
	}

	public Element toJDOM() {
		return new Element(getTagName(), getNamespace());
	}
	@Override
	protected String getTagName() {
		return AssessmentElementFactory.UNDEFINED_EXPRESSION;
	}
}
