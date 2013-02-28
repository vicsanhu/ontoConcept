package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class ExtendedTextInteraction extends BlockInteraction {

	// attributes
	private int fMaxStrings = 1; // optional
	private int fMinStrings = 1; // optional
	private int fExpectedLines = 0; // optional
	private int fExpectedLength = 0; // optional
	
	/** Creates a new instance of ExtendedTextInteraction from UI*/
	public ExtendedTextInteraction(AssessmentItem anAssessmentItem) {
		setAssessmentItem(anAssessmentItem);		
		createResponseDeclaration();
	}
	
	/** Creates a new instance of ExtendedTextInteraction from DOM parser*/
	public ExtendedTextInteraction(AssessmentItem anAssessmentItem, String forDOM) {
		setAssessmentItem(anAssessmentItem);	
		//no need to create a response and outcome anymore
	}

	private ResponseDeclaration createResponseDeclaration(){
		ResponseDeclaration aResponseDeclaration = new ResponseDeclaration(fAssessmentItem, "RESPONSE");
		aResponseDeclaration.setBaseType(AssessmentElementFactory.STRING);
		aResponseDeclaration.setCardinality(AssessmentElementFactory.SINGLE);
		setResponseDeclaration(aResponseDeclaration);
		fAssessmentItem.getResponseDeclarationList().addElement(aResponseDeclaration);
		aResponseDeclaration.setAssociatedOutcomeDeclaration(createOutcomeForExtendedText(aResponseDeclaration));
		return aResponseDeclaration;
	}
	
	public OutcomeDeclaration createOutcomeForExtendedText(ResponseDeclaration aResponseDeclaration) {
		return new //OutcomeDeclaration(aResponseDeclaration.getId()+".EXTENDED_TEXT", 
				OutcomeDeclaration("ANSWER", 
				AssessmentElementFactory.STRING, 
				AssessmentElementFactory.SINGLE,
				fAssessmentItem);
	}
	
	public void setMaxStrings(int aMaxStrings) {
		this.fMaxStrings = aMaxStrings;
	}

	public int getMaxStrings() {
		return fMaxStrings;
	}

	public void setMinStrings(int aMinStrings) {
		this.fMinStrings = aMinStrings;
	}

	public int getMinStrings() {
		return fMinStrings;
	}
	
	public void setExpectedLines(int i) {
		this.fExpectedLines = i;
	}

	public int getExpectedLines() {
		return fExpectedLines;
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

			if (AssessmentElementFactory.MAX_STRINGS.equals(tag)) {
				fMaxStrings= Integer.parseInt(value);
			} else if (AssessmentElementFactory.MIN_STRINGS.equals(tag)) {
				fMinStrings= Integer.parseInt(value);
			} else if (AssessmentElementFactory.EXPECTED_LINES.equals(tag)) {
				fExpectedLines= Integer.parseInt(value);
			} else if (AssessmentElementFactory.EXPECTED_LENGTH.equals(tag)) {
				fExpectedLength = Integer.parseInt(value);
			}
		}
	}
	
	public Element toJDOM() {
		Element aExtendedTextInteraction = super.toJDOM();

		if (fMaxStrings > 1)
			aExtendedTextInteraction.setAttribute(AssessmentElementFactory.MAX_STRINGS, String.valueOf(fMaxStrings));
		if (fMinStrings != 1)
			aExtendedTextInteraction.setAttribute(AssessmentElementFactory.MIN_STRINGS, String.valueOf(fMinStrings));
		if (fExpectedLines > 0)
			aExtendedTextInteraction.setAttribute(AssessmentElementFactory.EXPECTED_LINES, String.valueOf(fExpectedLines));
		if (fExpectedLength > 0)
			aExtendedTextInteraction.setAttribute(AssessmentElementFactory.EXPECTED_LENGTH, String.valueOf(fExpectedLength));
		
		return aExtendedTextInteraction;
	}
	public String getTagName() {
		return AssessmentElementFactory.EXTENDED_TEXT_INTERACTION;
	}
}
