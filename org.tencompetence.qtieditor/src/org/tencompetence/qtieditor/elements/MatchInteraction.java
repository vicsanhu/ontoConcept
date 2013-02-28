package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class MatchInteraction extends AssociationRelevantInteraction {

	// elements
	private SimpleMatchSet fSourceSimpleMatchSet = new SimpleMatchSet();
	private SimpleMatchSet fTargetSimpleMatchSet = new SimpleMatchSet();

	/** Creates a new instance of MatchInteraction*/
	public MatchInteraction(AssessmentItem anAssessmentItem, int choiceNumber) { //used by UI
		setAssessmentItem(anAssessmentItem);
		for (int i=0; i<choiceNumber; i++ ) {
			fSourceSimpleMatchSet.addSimpleAssociableChoice(new SimpleAssociableChoice());
			fTargetSimpleMatchSet.addSimpleAssociableChoice(new SimpleAssociableChoice());
		}	
		ResponseDeclaration aResponseDeclaration = createResponseDeclaration();
		aResponseDeclaration.setAssociatedOutcomeDeclaration(createOutcomeForMapResponse(aResponseDeclaration));
	}

	public MatchInteraction(AssessmentItem anAssessmentItem) { // used by fromJDOM()
		setAssessmentItem(anAssessmentItem);
	}
	
	private ResponseDeclaration createResponseDeclaration(){
		// create and include responseDeclaration
		ResponseDeclaration aResponseDeclaration = new ResponseDeclaration(fAssessmentItem, "RESPONSE");
		aResponseDeclaration.setBaseType(AssessmentElementFactory.DIRECTED_PAIR);
		aResponseDeclaration.setCardinality(AssessmentElementFactory.MULTIPLE);
		setResponseDeclaration(aResponseDeclaration);
		fAssessmentItem.getResponseDeclarationList().addElement(aResponseDeclaration);
		return aResponseDeclaration;
	}
	
	public OutcomeDeclaration createOutcomeForMapResponse(ResponseDeclaration aResponseDeclaration) {
		return new OutcomeDeclaration("SCORE", 
				AssessmentElementFactory.FLOAT, 
				AssessmentElementFactory.SINGLE,
				fAssessmentItem);
	}

	public SimpleMatchSet getSourceSimpleMatchSet() {
		return fSourceSimpleMatchSet;
	}

	public SimpleMatchSet getTargetSimpleMatchSet() {
		return fTargetSimpleMatchSet;
	}

	public void fromJDOM(Element element) {
		super.fromJDOM(element);

		boolean gotFirst = false;
		for (Object o : element.getChildren()) {
			Element child = (Element) o;
			String tag = child.getName();
			//String value = child.getText();

			if (tag.equals(AssessmentElementFactory.SIMPLE_MATCH_SET)) {
				if (!gotFirst) {
					fSourceSimpleMatchSet = new SimpleMatchSet();
					fSourceSimpleMatchSet.fromJDOM((Element) child);
					gotFirst = true;
				}
				else {
					fTargetSimpleMatchSet = new SimpleMatchSet();
					fTargetSimpleMatchSet.fromJDOM((Element) child);
				}
			} 
		}
	}
	
	public Element toJDOM() {
		Element aInteractionElement = super.toJDOM();
		
		aInteractionElement.addContent(fSourceSimpleMatchSet.toJDOM());
		aInteractionElement.addContent(fTargetSimpleMatchSet.toJDOM());

		return aInteractionElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.MATCH_INTERACTION;
	}
}
