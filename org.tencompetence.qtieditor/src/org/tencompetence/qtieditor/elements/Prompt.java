package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class Prompt extends BodyElement {

	private String data = ""; // optional

	/** Creates a new instance of Prompt */
	public Prompt() {
		//super.createId("PT");
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public String getTagName() {
		return AssessmentElementFactory.PROMPT;
	}
}
