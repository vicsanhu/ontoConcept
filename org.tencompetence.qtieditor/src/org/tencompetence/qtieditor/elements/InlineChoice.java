package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class InlineChoice extends IdentifiedElement {

	private String fData = ""; // option
	private boolean fFixed = false; // option
	private String fShowHide = ""; // option

	private AssessmentItem fAssessmentItem;

	/** Creates a new instance of InlineChoice */
	public InlineChoice(AssessmentItem anAssessmentItem) {
		super.createId("IC");
		fAssessmentItem = anAssessmentItem;
	}

	public AssessmentItem getAssessmentItem() {
		return fAssessmentItem;
	}

	public void setData(String data) {
		this.fData = data;
	}

	public String getData() {
		return fData;
	}

	public void setFixed(boolean fixed) {
		this.fFixed = fixed;
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

	/*
	public void setInlineChoiceInteraction(InlineChoiceInteraction aInlineChoiceInteraction) {
		this.fInlineChoiceInteraction = aInlineChoiceInteraction;
	}

	public InlineChoiceInteraction getInlineChoiceInteraction() {
		return fInlineChoiceInteraction;
	}
*/
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
			}
		}
	}

	public Element toJDOM() {
		Element aInlineChoice = new Element(getTagName(), getNamespace());

		aInlineChoice.setAttribute(AssessmentElementFactory.IDENTIFIER, getId());
		if (fFixed) 
			aInlineChoice.setAttribute(AssessmentElementFactory.FIXED, "true");
		if (!fShowHide.equalsIgnoreCase(""))
			aInlineChoice.setAttribute(AssessmentElementFactory.SHOW_HIDE, fShowHide);

		aInlineChoice.setText(getData());

		return aInlineChoice;
	}

	public String getTagName() {
		return AssessmentElementFactory.INLINE_CHOICE;
	}

}
