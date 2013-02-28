package org.tencompetence.qtieditor.expression;

import org.jdom.Attribute;
import org.jdom.Element;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.VariableDeclaration;

public class TestVariables extends SimpleExpression{
	private String fVariableIdentifier;
	private String fWeightIdentifier;
	
	public TestVariables(AbstractAssessment aAssessment) {
		setAssessment(aAssessment);
		setType(testVariables_index);
	}

	public void setVariableIdentifier(String id) {
		fVariableIdentifier = id;
	}

	public String getVariableIdentifier() {
		return fVariableIdentifier;
	}

	
	public TestVariables(AbstractAssessment aAssessment, String aVariableIdentifier) {
		setAssessment(aAssessment);
		fVariableIdentifier = aVariableIdentifier;
		setType(testVariables_index);
	}
	
	
	public void setWeightIdentifier(String id) {
		this.fWeightIdentifier = id;
	}

	public String getWeightIdentifier() {
		return fWeightIdentifier;
	}

	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.VARIABLE_IDENTIFIER.equals(tag)) {
				fVariableIdentifier = value;
			} else if (AssessmentElementFactory.WEIGHT_IDENTIFIER.equals(tag)) {
				fWeightIdentifier = value;
			}
		}
	}

	public Element toJDOM() {
		Element aVariableElement = new Element(getTagName(), (fAssessment instanceof AssessmentItem)? getNamespace(): getTestNamespace());

		if (getVariableIdentifier()!=null)
			aVariableElement.setAttribute(AssessmentElementFactory.VARIABLE_IDENTIFIER, getVariableIdentifier());
		if (getWeightIdentifier()!=null)
			aVariableElement.setAttribute(AssessmentElementFactory.WEIGHT_IDENTIFIER, getWeightIdentifier());

		return aVariableElement;
	}	
	
	protected String getTagName() {
		return AssessmentElementFactory.TEST_VARIABLES;
	}

}
