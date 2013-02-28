package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class SimpleChoice extends BodyElement {

	private String fData = ""; // option
	private boolean fFixed = false; // option
	private String fShowHide = ""; // option
	private double fMappedValue = 0; // option
	private FeedbackInline fFeedbackInline = null;

	private AssessmentItem fAssessmentItem;
	private ChoiceRelevantInteraction fChoiceRelevantInteraction;

	/** Creates a new instance of SimpleChoice */
	public SimpleChoice(AssessmentItem anAssessmentItem,
			ChoiceRelevantInteraction aChoiceInteraction) {
		super.createId("SC");
		fAssessmentItem = anAssessmentItem;
		fChoiceRelevantInteraction = aChoiceInteraction;
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

	public void setMappedValue(double mappedValue) {
		this.fMappedValue = mappedValue;
	}

	public double getMappedValue() {
		return fMappedValue;
	}

	public void setChoiceInteraction(ChoiceRelevantInteraction aInteraction) {
		this.fChoiceRelevantInteraction = aInteraction;
	}

	public ChoiceRelevantInteraction getChoiceInteraction() {
		return fChoiceRelevantInteraction;
	}

	public void setFeedbackInline(FeedbackInline aFeedbackInline) {
		fFeedbackInline = aFeedbackInline;
	}

	public FeedbackInline getFeedbackInline() {
		return fFeedbackInline;
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
			} else if (AssessmentElementFactory.MAPPED_VALUE.equals(tag)) {
				fMappedValue = Double.parseDouble(value);
			}
		}

		for (Object object : element.getChildren()) {
			Element child = (Element) object;
			String tag = child.getName();
			// String value = child.getText();Element outcomeDeclaration =
			// ((OutcomeDeclaration)fOutcomeDeclarationList.getBasicElementAt(i)).toJDOM();

			if (tag.equals(AssessmentElementFactory.FEEDBACK_INLINE)) {
				fFeedbackInline = new FeedbackInline(fAssessmentItem, this);
				fFeedbackInline.fromJDOM((Element) child);
			}
		}
	}

	public Element toJDOM() {
		Element aSimpleChoice = new Element(getTagName(), getNamespace());

		aSimpleChoice
				.setAttribute(AssessmentElementFactory.IDENTIFIER, getId());

		if (fFixed)
			aSimpleChoice.setAttribute(AssessmentElementFactory.FIXED, "true");
		
		if (!fShowHide.equalsIgnoreCase(""))
			aSimpleChoice.setAttribute(AssessmentElementFactory.SHOW_HIDE, fShowHide);
		
		if (fMappedValue!=0)
			aSimpleChoice.setAttribute(AssessmentElementFactory.MAPPED_VALUE, String.valueOf(fMappedValue));


		aSimpleChoice.setText(getData());
		
		/* transform inline feedback into modal feedback, so ignore inline feedback
		if (fFeedbackInline != null) {
			Element aFeedbackInline = fFeedbackInline.toJDOM();
			if (aFeedbackInline != null)
				aSimpleChoice.addContent(aFeedbackInline);
		}
		*/
		
		return aSimpleChoice;
	}

	public String getTagName() {
		return AssessmentElementFactory.SIMPLE_CHOICE;
	}

}
