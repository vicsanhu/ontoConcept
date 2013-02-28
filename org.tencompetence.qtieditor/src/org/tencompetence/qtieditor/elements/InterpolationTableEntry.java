package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class InterpolationTableEntry extends AbstractTableEntry {

	protected double fSourceValue = 0.0; // required
	protected boolean fIncludeBoundary = true; // option
	protected int fProficiencyLevel = 0; // this is not a QTI-specified attribute 

	public InterpolationTableEntry() {
	}
	
	public void setSourceValue(double aSourceValue) {
		this.fSourceValue = aSourceValue;
	}

	public double getSourceValue() {
		return fSourceValue;
	}
	
	public void setIncludeBoundary(boolean aValue) {
		this.fIncludeBoundary = aValue;
	}

	public boolean getIncludeBoundary() {
		return fIncludeBoundary;
	}
	
	public void setProficiencyLevel(int aProficiencyLevel) {
		this.fProficiencyLevel = aProficiencyLevel;
	}

	public int getProficiencyLevel() {
		return fProficiencyLevel;
	}
	
	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (tag.equals(AssessmentElementFactory.SOURCE_VALUE)) {
				fSourceValue = Double.parseDouble(value);
			} else if (tag.equals(AssessmentElementFactory.TARGET_VALUE)) {
				fTargetValue = value;
			} else if (tag.equals(AssessmentElementFactory.INCLUDE_BOUNDARY)) {
				fIncludeBoundary = "true".equalsIgnoreCase(value);
			} else if (tag.equals(AssessmentElementFactory.PROFICIENCY_LEVEL)) {
				fProficiencyLevel = Integer.parseInt(value);
			}
		}
	}
	
	
	public Element toJDOM() {
				
		Element aElement = new Element(getTagName(), getTestNamespace());

		aElement.setAttribute(AssessmentElementFactory.SOURCE_VALUE, String.valueOf(fSourceValue));
		aElement.setAttribute(AssessmentElementFactory.TARGET_VALUE, fTargetValue);		
		if (!fIncludeBoundary)
			aElement.setAttribute(AssessmentElementFactory.INCLUDE_BOUNDARY, "false");
		if (fProficiencyLevel>0)
			aElement.setAttribute(AssessmentElementFactory.PROFICIENCY_LEVEL, String.valueOf(fProficiencyLevel));
		
		return aElement ;
	}

	protected String getTagName() {
		return AssessmentElementFactory.INTERPOLATION_TABLE_ENTRY;
	}

}