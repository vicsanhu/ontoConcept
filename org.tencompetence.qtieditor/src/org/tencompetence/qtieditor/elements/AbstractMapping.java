package org.tencompetence.qtieditor.elements;

public abstract class AbstractMapping extends BasicElement {

	protected double fLowerBound = 0.0; // optional
	protected double fUpperBound = 4.0; // optional
	protected double fDefaultValue = 0.0; // required

	public void setLowerBound(double aLowerBound) {
		this.fLowerBound = aLowerBound;
	}

	public double getLowerBound() {
		return fLowerBound;
	}

	public void setUpperBound(double aUpperBound) {
		this.fUpperBound = aUpperBound;
	}

	public double getUpperBound() {
		return fUpperBound;
	}

	public void setDefaultValue(double aDefaultValue) {
		this.fDefaultValue = aDefaultValue;
	}

	public double getDefaultValue() {
		return fDefaultValue;
	}
}
