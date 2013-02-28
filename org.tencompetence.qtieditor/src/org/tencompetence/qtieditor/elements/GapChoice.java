package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public abstract class GapChoice extends BodyElement{
	
	protected boolean fFixed = false; // option
	protected int fMatchMax = 1; // required
	protected AssessmentItem fAssessmentItem;
	protected GapMatchInteraction fGapMatchInteraction;

	public void setFixed(boolean aFixed) {
		this.fFixed = aFixed;
	}

	public boolean getFixed() {
		return fFixed;
	}

	public void setMatchMax(int matchMax) {
		this.fMatchMax = matchMax;
	}

	public int getMatchMax() {
		return fMatchMax;
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

	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.FIXED.equals(tag)) {
				fFixed = "true".equalsIgnoreCase(value);
			} else if (AssessmentElementFactory.MATCH_MAX.equals(tag)) {
				fMatchMax = Integer.parseInt(value);
			}
		}
	}

	public Element toJDOM() {
		Element aChoiceElement = new Element(getTagName(), getNamespace());
		aChoiceElement
				.setAttribute(AssessmentElementFactory.IDENTIFIER, getId());
		if (fFixed)
			aChoiceElement.setAttribute(AssessmentElementFactory.FIXED, "true");
		if (fMatchMax!=0)
			aChoiceElement.setAttribute(AssessmentElementFactory.MATCH_MAX, String.valueOf(fMatchMax));		
		return aChoiceElement;
	}

}