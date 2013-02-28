package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class OrderInteraction extends ChoiceRelevantInteraction {

	/** Creates a new instance of OrderInteraction */
	public OrderInteraction(AssessmentItem anAssessmentItem) { //used by fromJDOM()
		setAssessmentItem(anAssessmentItem);
	}

	public OrderInteraction(AssessmentItem anAssessmentItem, int choiceNumber) { // used by UI
		setAssessmentItem(anAssessmentItem);
		fShuffle = true; // default
		for (int i = 0; i < choiceNumber; i++) {
			fSimpleChoiceList.addElement(new SimpleChoice(fAssessmentItem, this));
		}
		ResponseDeclaration aResponseDeclaration = createResponseDeclaration();
		aResponseDeclaration.setAssociatedOutcomeDeclaration(createOutcomeForMatchCorrect(aResponseDeclaration));
	}
	
	private ResponseDeclaration createResponseDeclaration(){
		// create and include responseDeclaration
		ResponseDeclaration aResponseDeclaration = new ResponseDeclaration(fAssessmentItem, "RESPONSE");
		aResponseDeclaration.setBaseType(AssessmentElementFactory.IDENTIFIER);
		aResponseDeclaration.setCardinality(AssessmentElementFactory.ORDERED);
		setResponseDeclaration(aResponseDeclaration);
		fAssessmentItem.getResponseDeclarationList().addElement(aResponseDeclaration);
		return aResponseDeclaration;
	}
	
	public OutcomeDeclaration createOutcomeForMatchCorrect(ResponseDeclaration aResponseDeclaration) {
		//return new OutcomeDeclaration(aResponseDeclaration.getId()+".SCORE", 
		return new OutcomeDeclaration("SCORE", 
				AssessmentElementFactory.INTEGER, 
				AssessmentElementFactory.SINGLE,
				fAssessmentItem);
	}

	public String getTagName() {
		return AssessmentElementFactory.ORDER_INTERACTION;
	}
}

