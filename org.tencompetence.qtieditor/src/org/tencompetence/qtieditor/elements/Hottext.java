package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class Hottext extends IdentifiedElement {

	private boolean fFixed = false; // option
	private int fStart = 0; 
	private int fLength = 0;
	private String fData = "";

	private AssessmentItem fAssessmentItem;
	private HottextInteraction fHottextInteraction;

	/** Creates a new instance of Hottext from UI*/
	public Hottext(AssessmentItem aAssessmentItem, HottextInteraction aHottextInteraction, 
			String data, int start, int length) {
		super.createId("HT");
		fAssessmentItem = aAssessmentItem;
		fHottextInteraction = aHottextInteraction;
		fStart = start;
		fLength = length;
		fData = data;
	}

	/** Creates a new instance of Hottext from DOM*/
	public Hottext(AssessmentItem aAssessmentItem, HottextInteraction aHottextInteraction) {
		fAssessmentItem = aAssessmentItem;
		fHottextInteraction = aHottextInteraction;
	}

	public void setFixed(boolean fixed) {
		this.fFixed = fixed;
	}

	public boolean getFixed() {
		return fFixed;
	}

	public void setStart(int i) {
		this.fStart = i;
	}

	public int getStart() {
		return fStart;
	}
	
	public void setLength(int i) {
		this.fLength = i;
	}

	public int getLength() {
		return fLength;
	}

	public void setData(String data) {
		this.fData = data;
	}

	public String getData() {
		return fData;
	}

	public void setAssessmentItem(AssessmentItem anAssessmentItem) {
		fAssessmentItem = anAssessmentItem;
	}

	public AssessmentItem getAssessmentItem() {
		return fAssessmentItem;
	}

	public void setHottextInteraction(HottextInteraction aHottextInteraction) {
		fHottextInteraction = aHottextInteraction;
	}

	public HottextInteraction getHottextInteraction() {
		return fHottextInteraction;
	}
	
	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.FIXED.equals(tag)) {
				fFixed = "true".equalsIgnoreCase(value);
			} 
		}
		fData = element.getText();
	}

	public Element toJDOM() {
		Element aHottextElement = new Element(getTagName(), getNamespace());

		aHottextElement.setAttribute(AssessmentElementFactory.IDENTIFIER, getId());
		if (fFixed) 
			aHottextElement.setAttribute(AssessmentElementFactory.FIXED, "true");
		
		aHottextElement.setText(getData());
		return aHottextElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.HOTTEXT;
	}

}

