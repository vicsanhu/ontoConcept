package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class InlineChoiceInteraction extends InlineInteraction {

	private boolean fShuffle = true; // optional	
	private BasicElementList fInlineChoiceList = new BasicElementList();	

	public InlineChoiceInteraction(AssessmentItem anAssessmentItem, String correctAnswer, int start, int length) {
		super(anAssessmentItem);
		fStart = start;
		fLength = length;
		InlineChoice anInlineChoice = new InlineChoice(fAssessmentItem);
		fInlineChoiceList.addElement(anInlineChoice);
		anInlineChoice.setData(correctAnswer);
		
		createResponseDeclaration();
		setResponseCorrectChoice(anInlineChoice);
	}

	public InlineChoiceInteraction(AssessmentItem anAssessmentItem) {
		super(anAssessmentItem);
		// it is not necessary to create an associated response declaration 
	}	

	public ResponseDeclaration createResponseDeclaration() {
		ResponseDeclaration aResponseDeclaration = new ResponseDeclaration(fAssessmentItem);
		setResponseDeclaration(aResponseDeclaration);
		fAssessmentItem.getResponseDeclarationList().addElement(aResponseDeclaration);
		aResponseDeclaration.setBaseType(AssessmentElementFactory.IDENTIFIER);
		aResponseDeclaration.setCardinality(AssessmentElementFactory.SINGLE);
		aResponseDeclaration.setAssociatedOutcomeDeclaration(createOutcomeForInlineChoices(aResponseDeclaration));
		return aResponseDeclaration;
	}
	
	
	public OutcomeDeclaration createOutcomeForInlineChoices(ResponseDeclaration aResponseDeclaration) {
		return new OutcomeDeclaration(aResponseDeclaration.getId()+".SCORE",
				AssessmentElementFactory.INTEGER, 
				AssessmentElementFactory.SINGLE,
				fAssessmentItem);
	}
	
	public void setResponseCorrectChoice(InlineChoice anInlineChoice){
		getResponseDeclaration().setFirstCorrectResponse(AssessmentElementFactory.IDENTIFIER, anInlineChoice.getId());		
	}
	
	public String getResponseCorrectChoiceId(){
		return getResponseDeclaration().getFirstCorrectResponse();		
	}
	
	public void setShuffle(boolean shuffle) {
		this.fShuffle = shuffle;
	}

	public boolean getShuffle() {
		return fShuffle;
	}

	public BasicElementList getInlineChoiceList() {
		return fInlineChoiceList;
	}

	public void setInlineChoiceList(BasicElementList list) {
		fInlineChoiceList = list;
	}

	public InlineChoice getCorrectChoice() {
		InlineChoice aChoice = null;
		String correctChoiceID = getResponseCorrectChoiceId();
		if (correctChoiceID.equalsIgnoreCase("")) 
			return null;
		
		for (int i=0; i<fInlineChoiceList.size(); i++) {
			aChoice = (InlineChoice) fInlineChoiceList.getBasicElementAt(i);
			if (aChoice.getId().equalsIgnoreCase(correctChoiceID)) 
				return aChoice;
		}
		return null;
	}

	public void addInlineChoice(InlineChoice aInlineChoice) {
		fInlineChoiceList.addElement(aInlineChoice);
	}

	public void removeInlineChoice(InlineChoice aInlineChoice) {
		fInlineChoiceList.removeElement(aInlineChoice);
	}
	
	public void fromJDOM(Element element) {
		super.fromJDOM(element);

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.SHUFFLE.equals(tag)) {
				fShuffle = "true".equalsIgnoreCase(value);
			}
		}
		
		for (Object o : element.getChildren()) {
			Element child = (Element) o;
			String tag = child.getName();
			//String value = child.getText();

			if (tag.equals(AssessmentElementFactory.INLINE_CHOICE)) {
				InlineChoice aInlineChoice = new InlineChoice(fAssessmentItem);
				aInlineChoice.fromJDOM((Element) child);
				fInlineChoiceList.addElement(aInlineChoice);
			} 
		}
	}

	public Element toJDOM() {
		Element aInlineChoiceInteraction = super.toJDOM();

		aInlineChoiceInteraction.setAttribute(AssessmentElementFactory.SHUFFLE, fShuffle ? "true": "false");

		for (int i = 0; i < fInlineChoiceList.size(); i++) {
			Element aInlineChoice  = ((InlineChoice) fInlineChoiceList.getBasicElementAt(i)).toJDOM();
			if (aInlineChoice != null)
				aInlineChoiceInteraction.addContent(aInlineChoice);
		}

		return aInlineChoiceInteraction;
	}

	public String getTagName() {
		return AssessmentElementFactory.INLINE_CHOICE_INTERACTION;
	}
}
