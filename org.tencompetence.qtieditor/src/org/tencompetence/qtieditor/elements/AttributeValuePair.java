package org.tencompetence.qtieditor.elements;

public class AttributeValuePair {

	private String name = "", value = "";

	public AttributeValuePair() {
	}

	public AttributeValuePair(String aName, String aValue) {
		name = aName;
		value = aValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String s) {
		this.name = s;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String s) {
		this.value = s;
	}

	public String createXML() {

		return " " + name + "=\"" + value + "\"";
	}
}
