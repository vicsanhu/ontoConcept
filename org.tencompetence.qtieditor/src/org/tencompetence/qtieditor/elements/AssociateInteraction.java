package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class AssociateInteraction extends AssociationRelevantInteraction {


	// elements
	private BasicElementList fSimpleAssociableChoiceList = new BasicElementList();

	/** Creates a new instance of AssociateInteraction*/
	public AssociateInteraction(AssessmentItem anAssessmentItem, int choiceNumber) { //used by UI
		setAssessmentItem(anAssessmentItem);
		for (int i=0; i<choiceNumber; i++ ) {
			fSimpleAssociableChoiceList.addElement(new SimpleAssociableChoice());
		}
		ResponseDeclaration aResponseDeclaration = createResponseDeclaration();
		aResponseDeclaration.setAssociatedOutcomeDeclaration(createOutcomeForMapResponse(aResponseDeclaration));
	}

	public AssociateInteraction(AssessmentItem anAssessmentItem) { // used by fromJDOM()
		setAssessmentItem(anAssessmentItem);
	}
	
	private ResponseDeclaration createResponseDeclaration(){
		// create and include responseDeclaration
		ResponseDeclaration aResponseDeclaration = new ResponseDeclaration(fAssessmentItem, "RESPONSE");
		aResponseDeclaration.setBaseType(AssessmentElementFactory.PAIR);
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

	public BasicElementList getSimpleAssociableChoiceList() {
		return fSimpleAssociableChoiceList;
	}

	public void setSimpleAssociableChoiceList(BasicElementList list) {
		fSimpleAssociableChoiceList = list;
	}

	public void addSimpleAssociableChoice(SimpleAssociableChoice aSimpleAssociableChoice) {
		fSimpleAssociableChoiceList.addElement(aSimpleAssociableChoice);
	}

	public void removeSimpleAssociableChoice(SimpleAssociableChoice aSimpleAssociableChoice) {
		fSimpleAssociableChoiceList.removeElement(aSimpleAssociableChoice);
	}

	public SimpleAssociableChoice getSimpleAssociableChoiceAt(int index) {
		return (SimpleAssociableChoice)fSimpleAssociableChoiceList.getBasicElementAt(index);
	}
	
	public SimpleAssociableChoice getSimpleAssociableChoiceByID(String id) {
		for (int i=0; i<fSimpleAssociableChoiceList.size(); i++) {
			SimpleAssociableChoice aSimpleAssociableChoice = (SimpleAssociableChoice)fSimpleAssociableChoiceList.getBasicElementAt(i);
			if (aSimpleAssociableChoice.getId().equalsIgnoreCase(id)) {
				return aSimpleAssociableChoice;
			}
		}
		return null;
	}
	
	public void fromJDOM(Element element) {
		super.fromJDOM(element);

		for (Object o : element.getChildren()) {
			Element child = (Element) o;
			String tag = child.getName();
			//String value = child.getText();

			if (tag.equals(AssessmentElementFactory.SIMPLE_ASSOCIABLE_CHOICE)) {
				SimpleAssociableChoice aSimpleAssociableChoice = new SimpleAssociableChoice();
				aSimpleAssociableChoice.fromJDOM((Element) child);
				fSimpleAssociableChoiceList.addElement(aSimpleAssociableChoice);
			} 
		}
	}
	
	public Element toJDOM() {
		Element aInteractionElement = super.toJDOM();
		
		for (int i = 0; i < fSimpleAssociableChoiceList.size(); i++) {
			Element aSimpleAssociableChoice  = ((SimpleAssociableChoice) fSimpleAssociableChoiceList.getBasicElementAt(i)).toJDOM();
			if (aSimpleAssociableChoice != null)
				aInteractionElement.addContent(aSimpleAssociableChoice);
		}
		return aInteractionElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.ASSOCIATE_INTERACTION;
	}
}

