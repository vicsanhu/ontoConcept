package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

import org.tencompetence.qtieditor.elements.SimpleChoice;

public class FeedbackInline extends FeedbackElement {

	private AssessmentItem fAssessmentItem;
	private SimpleChoice fSimpleChoice;
	
	public FeedbackInline(AssessmentItem anAssessmentItem, SimpleChoice aSimpleChoice) {
		setOutcomeIdentifier(aSimpleChoice.getChoiceInteraction().getResponseDeclaration().getFeedbackOutcomeDeclaration().getId());
		fSimpleChoice = aSimpleChoice;
		setId(aSimpleChoice.getId());
		fAssessmentItem = anAssessmentItem;
	}

	public AssessmentItem getAssessmentItem() {
		return fAssessmentItem;
	}


	public void fromJDOM(Element element) {
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.TITLE.equals(tag)) {
				fTitle = value;
			} else if (AssessmentElementFactory.OUTCOME_IDENTIFIER.equals(tag)) {
				fOutcomeIdentifier = value;
			} else if (AssessmentElementFactory.SHOW_HIDE.equals(tag)) {
				fShowHide = value;
			}
		}
		fData = element.getText();
	}

	public Element toJDOM() {
		Element aFeedbackElement = new Element(getTagName(), getNamespace());
		
		aFeedbackElement.setAttribute(AssessmentElementFactory.OUTCOME_IDENTIFIER, fOutcomeIdentifier);

		aFeedbackElement.setAttribute(AssessmentElementFactory.IDENTIFIER, fID);
		
		if (!fTitle.equalsIgnoreCase(""))
			aFeedbackElement.setAttribute(AssessmentElementFactory.TITLE, fTitle);
		
		if (!fShowHide.equalsIgnoreCase(""))
			aFeedbackElement.setAttribute(AssessmentElementFactory.SHOW_HIDE, fShowHide);

		if (!fData.equals(""))
			aFeedbackElement.setText(getData());

		return aFeedbackElement;
	}
	
	public String getTagName() {
		return AssessmentElementFactory.FEEDBACK_INLINE;
	}

}
