package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public abstract class VariableDeclaration extends IdentifiedElement {

	// attributes
	protected String fCardinality = ""; // required
	protected String fBaseType = ""; // optional

	public void setCardinality(String cardinality) {
		this.fCardinality = cardinality;
	}

	public String getCardinality() {
		return fCardinality;
	}

	public void setBaseType(String baseType) {
		this.fBaseType = baseType;
	}

	public String getBaseType() {
		return fBaseType;
	}

	public void fromJDOM(Element element) {
		
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			}
			else if (AssessmentElementFactory.CARDINALITY.equals(tag)) {
				fCardinality = value;
			}
			else if (AssessmentElementFactory.BASE_TYPE.equals(tag)) {
				fBaseType = value;
			}
		}
	}

	
}
