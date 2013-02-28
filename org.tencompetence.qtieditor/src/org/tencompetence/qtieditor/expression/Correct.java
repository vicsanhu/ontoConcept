package org.tencompetence.qtieditor.expression;

import org.jdom.Attribute;
import org.jdom.Element;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.VariableDeclaration;

public class Correct extends SimpleExpression{

	//private String fIdentifier;
	private VariableDeclaration fVariableDeclaration;
	
	public Correct(AbstractAssessment aAssessment) {
		setAssessment(aAssessment);
		setType(correct_index);
	}
/*
	public void setIdentifier(String id) {
		this.fIdentifier = id;
	}

	public String getIdentifier() {
		return fIdentifier;
	}
*/
	public void setVariableDeclaration(VariableDeclaration aVariableDeclaration) {
		fVariableDeclaration = aVariableDeclaration;
	}

	public VariableDeclaration getVariableDeclaration() {
		return fVariableDeclaration;
	}	
	

	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				setVariableDeclaration(fAssessment.getVariableDeclarationById(value));
			} 
		}
	}

	public Element toJDOM() {
		Element aCorrectElement = new Element(getTagName(), (fAssessment instanceof AssessmentItem)? getNamespace(): getTestNamespace());

		if (getVariableDeclaration()!=null)
			aCorrectElement.setAttribute(AssessmentElementFactory.IDENTIFIER, getVariableDeclaration().getId());

		return aCorrectElement;
	}	
	
	protected String getTagName() {
		return AssessmentElementFactory.CORRECT;
	}

}

