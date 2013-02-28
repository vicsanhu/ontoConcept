package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class MapEntry extends BasicElement{

	private String fMapKey = ""; // required

	private double fMappedValue; // required

	/** Creates a new instance of MapEntry */
	public MapEntry() {
	}

	public MapEntry(String mapKey, double mappedValue) {
		this.fMapKey = mapKey;
		this.fMappedValue = mappedValue;
	}

	public void setMapKey(String mapKey) {
		this.fMapKey = mapKey;
	}

	public String getMapKey() {
		return fMapKey;
	}

	public void setMappedValue(double mappedValue) {
		this.fMappedValue = mappedValue;
	}

	public double getMappedValue() {
		return fMappedValue;
	}

	public String getMappedValueString() {
		return String.valueOf(fMappedValue);
	}
	
	public void fromJDOM(Element element) {


		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (tag.equals(AssessmentElementFactory.MAP_KEY)) {
				fMapKey = value;
			} else if (tag.equals(AssessmentElementFactory.MAPPED_VALUE)) {
				fMappedValue= Double.parseDouble(value);
			}
		}
	}
	
	public Element toJDOM() {
				
		Element aElement = new Element(getTagName(), getNamespace());

		aElement.setAttribute(AssessmentElementFactory.MAP_KEY, fMapKey);
		aElement.setAttribute(AssessmentElementFactory.MAPPED_VALUE, String.valueOf(fMappedValue));

		return aElement ;
	}	
	@Override
	protected String getTagName() {
		return AssessmentElementFactory.MAP_ENTRY;
	}
}
