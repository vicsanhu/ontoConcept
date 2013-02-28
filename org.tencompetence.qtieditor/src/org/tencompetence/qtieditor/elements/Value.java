package org.tencompetence.qtieditor.elements;

public class Value {

	// attributes
	private String fFieldIdentifier = null; // optional
	private String fBaseType = "string"; 
	private String fData = "";

	/** Creates a new instance of Value */
	public Value() {
	}

	public Value(String baseType, String data) {
		this.fBaseType = baseType;
		this.fData = data;
	}

	public void setFieldIdentifier(String fieldIdentifier) {
		this.fFieldIdentifier = fieldIdentifier;
	}

	public String getFieldIdentifier() {
		return fFieldIdentifier;
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
}
