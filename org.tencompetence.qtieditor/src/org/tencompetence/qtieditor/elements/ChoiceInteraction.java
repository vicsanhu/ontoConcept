package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class ChoiceInteraction extends ChoiceRelevantInteraction {

	protected int fMaxChoices = 1; // required
	protected int fMinChoices = 0; // optional

	private int fType = -1; // -1: undefined; 1:TBMC; 2:TBMR; 3:LS; 4:YN 

	/** Creates a new instance of ChoiceInteraction */
	public ChoiceInteraction(AssessmentItem anAssessmentItem) { //used by fromJDOM()
		setAssessmentItem(anAssessmentItem);
		fSimpleChoiceList = new BasicElementList();
	}

	public ChoiceInteraction(AssessmentItem anAssessmentItem, int type,
			int choiceNumber) { // used by UI
		setAssessmentItem(anAssessmentItem);
		fSimpleChoiceList = new BasicElementList();
		
		//because usually an outcome is associated with a response, 
		//when a response id removed the outcome should be removed together
		ResponseDeclaration aResponseDeclaration = createResponseDeclaration();
		aResponseDeclaration.setFeedbackOutcomeDeclaration(createFeedbackOutcomeDeclaration(aResponseDeclaration));
		
		fType = type;
		if (fType == AssessmentItem.TBMC) {
			createMultipleChoiceInteraction(choiceNumber);
			aResponseDeclaration.setAssociatedOutcomeDeclaration(createOutcomeForMapResponse(aResponseDeclaration));
		}
		else if (fType == AssessmentItem.TBMR) {
			createMultipleResponseInteraction(choiceNumber);
			aResponseDeclaration.setAssociatedOutcomeDeclaration(createOutcomeForMapResponse(aResponseDeclaration));
		}
		else if (fType == AssessmentItem.LS) {
			createLikertScaleInteraction(choiceNumber);
			aResponseDeclaration.setAssociatedOutcomeDeclaration(createAnswerOutcome(aResponseDeclaration));
		}
		else if (fType == AssessmentItem.YN) {
			createYesAndNoInteraction();
			aResponseDeclaration.setAssociatedOutcomeDeclaration(createOutcomeForMapResponse(aResponseDeclaration));
		}
	}
	
	private ResponseDeclaration createResponseDeclaration(){
		// create and include responseDeclaration
		ResponseDeclaration aResponseDeclaration = new ResponseDeclaration(fAssessmentItem, "RESPONSE");
		aResponseDeclaration.setBaseType(AssessmentElementFactory.IDENTIFIER);
		setResponseDeclaration(aResponseDeclaration);
		fAssessmentItem.getResponseDeclarationList().addElement(aResponseDeclaration);
		return aResponseDeclaration;
	}
	
	public OutcomeDeclaration createFeedbackOutcomeDeclaration(ResponseDeclaration aResponseDeclaration)  {
		return new OutcomeDeclaration(aResponseDeclaration.getId()+".FEEDBACK", 
				AssessmentElementFactory.IDENTIFIER, 
				AssessmentElementFactory.SINGLE,
				fAssessmentItem);
	}
	
	public OutcomeDeclaration createOutcomeForMatchCorrect(ResponseDeclaration aResponseDeclaration) {
		//return new OutcomeDeclaration(aResponseDeclaration.getId()+".SCORE", 
		return new OutcomeDeclaration("SCORE", 
				AssessmentElementFactory.INTEGER, 
				AssessmentElementFactory.SINGLE,
				fAssessmentItem);
	}
	
	public OutcomeDeclaration createOutcomeForMapResponse(ResponseDeclaration aResponseDeclaration) {
		return new OutcomeDeclaration("SCORE", 
				AssessmentElementFactory.FLOAT, 
				AssessmentElementFactory.SINGLE,
				fAssessmentItem);
	}
	
	public OutcomeDeclaration createAnswerOutcome(ResponseDeclaration aResponseDeclaration) {
		return new OutcomeDeclaration("ANSWER", 
				AssessmentElementFactory.STRING, 
				AssessmentElementFactory.SINGLE,
				fAssessmentItem);
	}

	private void createMultipleChoiceInteraction(int choiceNumber) {
		fMaxChoices = 1;
		for (int i = 0; i < choiceNumber; i++) {
			fSimpleChoiceList.addElement(new SimpleChoice(fAssessmentItem, this));
		}
		getResponseDeclaration().setCardinality(AssessmentElementFactory.SINGLE);
	}

	private void createMultipleResponseInteraction(int choiceNumber) {
		fMaxChoices = 10;
		for (int i = 0; i < choiceNumber; i++) {
			fSimpleChoiceList.addElement(new SimpleChoice(fAssessmentItem, this));
		}
		getResponseDeclaration().setCardinality(AssessmentElementFactory.MULTIPLE);
	}

	private void createLikertScaleInteraction(int choiceNumber) {
		fMaxChoices = 1;
		SimpleChoice aSimpleChoice;
		for (int i = 0; i < choiceNumber; i++) {
			aSimpleChoice = new SimpleChoice(fAssessmentItem, this);
			aSimpleChoice.setData("" + (i + 1));
			fSimpleChoiceList.addElement(aSimpleChoice);
		}
		getResponseDeclaration().setCardinality(AssessmentElementFactory.SINGLE);
	}

	private void createYesAndNoInteraction() {
		fMaxChoices = 1;
		SimpleChoice aSimpleChoice = new SimpleChoice(fAssessmentItem, this);
		aSimpleChoice.setData("Yes");
		fSimpleChoiceList.addElement(aSimpleChoice);

		aSimpleChoice = new SimpleChoice(fAssessmentItem, this);
		aSimpleChoice.setData("No");
		fSimpleChoiceList.addElement(aSimpleChoice);
		getResponseDeclaration().setCardinality(AssessmentElementFactory.SINGLE);
	}

	public void setMaxChoices(int maxChoices) {
		this.fMaxChoices = maxChoices;
	}

	public int getMaxChoices() {
		return fMaxChoices;
	}

	public void fromJDOM(Element element) {
		super.fromJDOM(element);
		
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.MAX_CHOICES.equals(tag)) {
				fMaxChoices = Integer.parseInt(value);
			}
		}
	}
	
	public Element toJDOM() {
		Element aChoiceInteraction = super.toJDOM();
		aChoiceInteraction.setAttribute(AssessmentElementFactory.MAX_CHOICES, String.valueOf(fMaxChoices));
		return aChoiceInteraction;
	}

	public String getTagName() {
		return AssessmentElementFactory.CHOICE_INTERACTION;
	}
}
