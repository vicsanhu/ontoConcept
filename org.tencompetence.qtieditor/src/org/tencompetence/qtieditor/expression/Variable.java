package org.tencompetence.qtieditor.expression;

import org.jdom.Attribute;
import org.jdom.Element;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.VariableDeclaration;

public class Variable extends SimpleExpression{

	//private String fIdentifier;
	private VariableDeclaration fVariableDeclaration;
	private String fWeightIdentifier;
	
	public Variable(AbstractAssessment aAssessment) {
		setAssessment(aAssessment);
		setType(variable_index);
	}
/*
	public void setIdentifier(String id) {
		this.fIdentifier = id;
	}

	public String getIdentifier() {
		return fIdentifier;
	}
*/
	
	public Variable(AbstractAssessment aAssessment, VariableDeclaration aVariableDeclaration) {
		setAssessment(aAssessment);
		fVariableDeclaration = aVariableDeclaration;
		setType(variable_index);
	}
	
	public void setVariableDeclaration(VariableDeclaration aVariableDeclaration) {
		fVariableDeclaration = aVariableDeclaration;
	}

	public VariableDeclaration getVariableDeclaration() {
		return fVariableDeclaration;
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

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				setVariableDeclaration(fAssessment.getVariableDeclarationById(value));
			} else if (AssessmentElementFactory.WEIGHT_IDENTIFIER.equals(tag)) {
				fWeightIdentifier = value;
			}
		}
	}

	public Element toJDOM() {
		Element aVariableElement = new Element(getTagName(), (fAssessment instanceof AssessmentItem)? getNamespace(): getTestNamespace());

		if (getVariableDeclaration()!=null)
			aVariableElement.setAttribute(AssessmentElementFactory.IDENTIFIER, getVariableDeclaration().getId());
		if (getWeightIdentifier()!=null)
			aVariableElement.setAttribute(AssessmentElementFactory.WEIGHT_IDENTIFIER, getWeightIdentifier());

		return aVariableElement;
	}	
	
	protected String getTagName() {
		return AssessmentElementFactory.VARIABLE;
	}

}
