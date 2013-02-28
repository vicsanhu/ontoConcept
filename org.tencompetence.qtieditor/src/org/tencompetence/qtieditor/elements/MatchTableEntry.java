package org.tencompetence.qtieditor.elements;

public abstract class MatchTableEntry extends AbstractTableEntry {

	private int fSourceValue = 0; // required

	public void setSourceValue(int aSourceValue) {
		fSourceValue = aSourceValue;
	}

	public int getSourceValue() {
		return fSourceValue;
	}
}

