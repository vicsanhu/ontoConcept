package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class ModalFeedback extends FeedbackElement {

	//private AssessmentItem fAssessmentItem;

	/** Creates a new instance of ModalFeedback */
	public ModalFeedback() {
	}
	
	public ModalFeedback(String aOutcomeDeclarationId,
			String aSimpleChoiceId,
			String aShowHide,
			String aData) {
		
		setOutcomeIdentifier(aOutcomeDeclarationId);
		super.setId(aSimpleChoiceId);
		setShowHide(aShowHide);
		setData(aData);
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
		
		for (Object object : element.getChildren()) {
			Element child = (Element) object;
			String tag = child.getName();
			String value = child.getText();

			if (tag.equals(AssessmentElementFactory.P)) {
				fData = value;
			} 
		}
	}

	public Element toJDOM() {
		Element aFeedbackElement = new Element(getTagName(), getNamespace());
		
		aFeedbackElement.setAttribute(AssessmentElementFactory.OUTCOME_IDENTIFIER, fOutcomeIdentifier);

		aFeedbackElement.setAttribute(AssessmentElementFactory.IDENTIFIER, fID);
		
		if (!fTitle.equalsIgnoreCase(""))
			aFeedbackElement.setAttribute(AssessmentElementFactory.TITLE, fTitle);
		
		if (!fShowHide.equalsIgnoreCase(""))
			aFeedbackElement.setAttribute(AssessmentElementFactory.SHOW_HIDE, fShowHide);

		if (!fData.equals("")) {
			Element p = new Element(AssessmentElementFactory.P, getNamespace());
			p.setText(getData());
			aFeedbackElement.addContent(p);
		}

		return aFeedbackElement;
	}
	
	public String getTagName() {
		return AssessmentElementFactory.MODAL_FEEDBACK;
	}
}
