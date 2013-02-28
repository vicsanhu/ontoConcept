package org.tencompetence.qtieditor.elements;

public class AssociationExtension {

	private String fSource = ""; 
	private String fTarget = ""; 
	private boolean fCorrect = true;
	private double fMappedValue = 1.0;

	/** Creates a new instance of MapEntry */
	public AssociationExtension() {
	}

	public AssociationExtension(String aSource, String aTarget) {
		this.fSource = aSource;
		this.fTarget = aTarget;
	}

	public AssociationExtension(String aSource, String aTarget, double aMappedValue) {
		this.fSource = aSource;
		this.fTarget = aTarget;
		this.fMappedValue = aMappedValue;
	}

	public AssociationExtension(String aSource, String aTarget, boolean isCorrect, double aMappedValue) {
		this.fSource = aSource;
		this.fTarget = aTarget;
		this.fCorrect = isCorrect;
		this.fMappedValue = aMappedValue;
	}
	
	public void setSource(String aSource) {
		this.fSource = aSource;
	}

	public String getSource() {
		return fSource;
	}
	
	public void setTarget(String aTarget) {
		this.fTarget = aTarget;
	}

	public String getTarget() {
		return fTarget;
	}

	public void setCorrect(boolean isCorrect) {
		this.fCorrect = isCorrect;
	}

	public boolean getCorrect() {
		return fCorrect;
	}
	
	public void setMappedValue(double aMappedValue) {
		this.fMappedValue = aMappedValue;
	}

	public double getMappedValue() {
		return fMappedValue;
	}

}
