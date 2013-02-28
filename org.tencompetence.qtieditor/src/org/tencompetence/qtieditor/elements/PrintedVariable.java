package org.tencompetence.qtieditor.elements;

import org.jdom.Element;

public class PrintedVariable extends IdentifiedElement {
	
	// attributes
	private String fFormat = ""; // optional

	public PrintedVariable (String id) {
		setId(id);
	}

	public void setFormat(String aFormat) {
		fFormat = aFormat;
	}

	public String getFormat() {
		return fFormat;
	}

	public Element toJDOM() {
		Element aElement = new Element(getTagName(), getTestNamespace());

		aElement.setAttribute(AssessmentElementFactory.IDENTIFIER, getId());
		if (!fFormat.equalsIgnoreCase(""))
			aElement.setAttribute(AssessmentElementFactory.FORMAT, fFormat);
		
		return aElement;
	}
	
	protected String getTagName() {
		return AssessmentElementFactory.PRINTED_VARIABLE;
	}
}
