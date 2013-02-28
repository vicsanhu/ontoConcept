package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class SimpleAssociableChoice extends BodyElement{
	
	private String fData = ""; // required
	private boolean fFixed = false; // option
	private String fShowHide = ""; // option
	private int fMatchMax = 1; // required

	//private AssessmentItem fAssessmentItem;
	//private ChoiceInteraction fChoiceInteraction;

	/** Creates a new instance of SimpleChoice */
	public SimpleAssociableChoice() {
		super.createId("SAC");
		//fAssessmentItem = anAssessmentItem;
		//fChoiceInteraction = aChoiceInteraction;
	}
/*
	public AssessmentItem getAssessmentItem() {
		return fAssessmentItem;
	}
*/
	public void setData(String data) {
		this.fData = data;
	}

	public String getData() {
		return fData;
	}

	public void setFixed(boolean aFixed) {
		this.fFixed = aFixed;
	}

	public boolean getFixed() {
		return fFixed;
	}
	
	public void setShowHide(String showHide) {
		fShowHide = showHide;
	}

	public String getShowHide() {
		return fShowHide;
	}

	public void setMatchMax(int matchMax) {
		this.fMatchMax = matchMax;
	}

	public int getMatchMax() {
		return fMatchMax;
	}


	public void fromJDOM(Element element) {

		fData = element.getText();

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.FIXED.equals(tag)) {
				fFixed = "true".equalsIgnoreCase(value);
			} else if (AssessmentElementFactory.SHOW_HIDE.equals(tag)) {
				fShowHide = value;
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
		
		if (!fShowHide.equalsIgnoreCase(""))
			aChoiceElement.setAttribute(AssessmentElementFactory.SHOW_HIDE, fShowHide);
		
		if (fMatchMax!=0)
			aChoiceElement.setAttribute(AssessmentElementFactory.MATCH_MAX, String.valueOf(fMatchMax));


		aChoiceElement.setText(getData());
		
		
		return aChoiceElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.SIMPLE_ASSOCIABLE_CHOICE;
	}

}