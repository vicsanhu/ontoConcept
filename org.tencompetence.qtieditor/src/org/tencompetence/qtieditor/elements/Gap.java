package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class Gap extends IdentifiedElement {

	private boolean fFixed = false; // option
	private int fStart = 0; 
	private int fLength = 0;
	private GapText fMatchedGapText = null;
	private String fData = "";

	private AssessmentItem fAssessmentItem;
	private GapMatchInteraction fGapMatchInteraction;

	/** Creates a new instance of Gap from UI*/
	public Gap(AssessmentItem aAssessmentItem, GapMatchInteraction aGapMatchInteraction, 
			String data, int start, int length) {
		super.createId("GAP");
		fAssessmentItem = aAssessmentItem;
		fGapMatchInteraction = aGapMatchInteraction;
		fStart = start;
		fLength = length;
		fData = data;
		//fAssessmentItem.getFirstResponseDeclaration().addResponseCorrectChoice(fMatchedGapText);		
	}

	/** Creates a new instance of Gap from DOM*/
	public Gap(AssessmentItem aAssessmentItem, GapMatchInteraction aGapMatchInteraction) {
		fAssessmentItem = aAssessmentItem;
		fGapMatchInteraction = aGapMatchInteraction;
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

	public void setGapMatchInteraction(GapMatchInteraction aGapMatchInteraction) {
		fGapMatchInteraction = aGapMatchInteraction;
	}

	public GapMatchInteraction getGapMatchInteraction() {
		return fGapMatchInteraction;
	}

	public void setMatchedGapText(GapText aMatchedGapText) {
		fMatchedGapText = aMatchedGapText;
	}

	public GapText getMatchedGapText() {
		return fMatchedGapText;
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
	}

	public Element toJDOM() {
		Element aGapElement = new Element(getTagName(), getNamespace());

		aGapElement.setAttribute(AssessmentElementFactory.IDENTIFIER, getId());
		if (fFixed) 
			aGapElement.setAttribute(AssessmentElementFactory.FIXED, "true");
		return aGapElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.GAP;
	}

}

