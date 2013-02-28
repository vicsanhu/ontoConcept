package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class Weight extends IdentifiedElement {

	private double fValue; // required
	
	public Weight() {
		super.createId("WT");
	}

	public void setValue(double aValue) {
		this.fValue = aValue;
	}

	public double getValue() {
		return fValue;
	}

	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.VALUE.equals(tag)) {
				fValue = Double.parseDouble(value);
			}
		}
	}

	public Element toJDOM() {
		Element aWeightElement = new Element(getTagName(), getNamespace());

		aWeightElement.setAttribute(AssessmentElementFactory.IDENTIFIER, getId());
		aWeightElement.setAttribute(AssessmentElementFactory.VALUE, String.valueOf(fValue));

		return aWeightElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.WEIGHT;
	}

}
