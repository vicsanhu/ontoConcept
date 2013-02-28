package org.tencompetence.qtieditor.expression;

import org.jdom.Attribute;
import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.ResponseDeclaration;

public class MapResponse extends SimpleExpression{

	//private String fIdentifier;
	private ResponseDeclaration fResponseDeclaration;
	
	public MapResponse(AbstractAssessment aAssessment) {
		setAssessment(aAssessment);
		setType(mapResponse_index);
	}
/*
	public void setIdentifier(String id) {
		this.fIdentifier = id;
	}

	public String getIdentifier() {
		return fIdentifier;
	}
*/
	public void setResponseDeclaration(ResponseDeclaration aResponseDeclaration) {
		fResponseDeclaration = aResponseDeclaration;
	}

	public ResponseDeclaration getResponseDeclaration() {
		return fResponseDeclaration;
	}	


	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				setResponseDeclaration(((AssessmentItem)fAssessment).getResponseDeclarationByID(value));
			} 
		}
	}

	public Element toJDOM() {
		Element aVariableElement = new Element(getTagName(), (fAssessment instanceof AssessmentItem)? getNamespace(): getTestNamespace());

		if (getResponseDeclaration()!=null)
			aVariableElement.setAttribute(AssessmentElementFactory.IDENTIFIER, getResponseDeclaration().getId());

		return aVariableElement;
	}	
	
	protected String getTagName() {
		return AssessmentElementFactory.MAP_RESPONSE;
	}
}
