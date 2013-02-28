package org.tencompetence.qtieditor.elements;

public abstract class AbstractTableEntry extends BasicElement {

	protected String fTargetValue = ""; // required

	public void setTargetValue(String aTargetValue) {
		this.fTargetValue = aTargetValue;
	}

	public String getTargetValue() {
		return fTargetValue;
	}

}
