package org.tencompetence.qtieditor.elements;

public abstract class LookupTable extends BasicElement {

	private String fDefaultValue = ""; //option


	public void setDefaultValue(String aDefaultValue) {
		this.fDefaultValue = aDefaultValue;
	}

	public String getDefaultValue() {
		return fDefaultValue;
	}
}
