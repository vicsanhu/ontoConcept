package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class TextEntryInteraction extends InlineInteraction {

	private int fExpectedLength = 0; // optional

	/** Creates a new instance of TextEntryInteraction from UI*/
	public TextEntryInteraction(AssessmentItem anAssessmentItem, int start, int length) {
		super(anAssessmentItem);
		createResponseDeclaration();
		fStart = start;
		fLength = length;
	}
	
	/** Creates a new instance of TextEntryInteraction from DOM parser*/
	public TextEntryInteraction(AssessmentItem anAssessmentItem) {
		super(anAssessmentItem);
		// it is not necessary to create an associated response declaration
	}	

	public ResponseDeclaration createResponseDeclaration() {
		ResponseDeclaration aResponseDeclaration = new ResponseDeclaration(fAssessmentItem);
		setResponseDeclaration(aResponseDeclaration);
		fAssessmentItem.getResponseDeclarationList().addElement(aResponseDeclaration);
		aResponseDeclaration.setBaseType(AssessmentElementFactory.STRING);
		aResponseDeclaration.setCardinality(AssessmentElementFactory.SINGLE);
		aResponseDeclaration.setAssociatedOutcomeDeclaration(createOutcomeForTextEntry(aResponseDeclaration));
		return getResponseDeclaration();
	}

	
	public OutcomeDeclaration createOutcomeForTextEntry(ResponseDeclaration aResponseDeclaration) {
		return new OutcomeDeclaration(aResponseDeclaration.getId()+".SCORE",
				AssessmentElementFactory.INTEGER, 
				AssessmentElementFactory.SINGLE,
				fAssessmentItem);
	}
	
	public void setExpectedLength(int i) {
		this.fExpectedLength = i;
	}

	public int getExpectedLength() {
		return fExpectedLength;
	}

	public void fromJDOM(Element element) {
		super.fromJDOM(element);

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.EXPECTED_LENGTH.equals(tag)) {
				fExpectedLength = Integer.parseInt(value);
			}
		}
	}

	public Element toJDOM() {
		Element aTextEntryInteraction = super.toJDOM();

		if (fExpectedLength > 0)
			aTextEntryInteraction.setAttribute(
					AssessmentElementFactory.EXPECTED_LENGTH, String
							.valueOf(fExpectedLength));

		return aTextEntryInteraction;
	}

	public String getTagName() {
		return AssessmentElementFactory.TEXT_ENTRY_INTERACTION;
	}
}