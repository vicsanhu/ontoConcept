package org.tencompetence.qtieditor.expression;

import org.jdom.Attribute;
import org.jdom.Element;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;

public class BaseValue extends SimpleExpression{

	// attributes
	private String fBaseType = null; // at last one
	private String fData = null;

	/** Creates a new instance of BaseValue */
	public BaseValue(AbstractAssessment aAssessment) {
		setAssessment(aAssessment);
		setType(baseValue_index);
	}

	public BaseValue(AbstractAssessment aAssessment, String baseType, String data) {
		setAssessment(aAssessment);
		this.fBaseType = baseType;
		this.fData = data;
	}

	public void setBaseType(String baseType) {
		this.fBaseType = baseType;
	}

	public String getBaseType() {
		return fBaseType;
	}

	public void setData(String data) {
		this.fData = data;
	}

	public String getData() {
		return fData;
	}
	
	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.BASE_TYPE.equals(tag)) {
				fBaseType = value;
			} 
		}
		fData = element.getText();
	}

	public Element toJDOM() {
		Element aBaseValueElement = new Element(getTagName(), (fAssessment instanceof AssessmentItem)? getNamespace(): getTestNamespace());
		if (fBaseType!=null)
			aBaseValueElement.setAttribute(AssessmentElementFactory.BASE_TYPE, fBaseType );
		if (fData!=null)
			aBaseValueElement.setText(getData());
		
		return aBaseValueElement;
	}	
	
	protected String getTagName() {
		return AssessmentElementFactory.BASE_VALUE;
	}
}

