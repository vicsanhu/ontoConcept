package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public abstract class ChoiceRelevantInteraction extends BlockInteraction {

	// attributes
	protected boolean fShuffle = false; // required

	// elements
	protected BasicElementList fSimpleChoiceList = new BasicElementList();


	public void setShuffle(boolean shuffle) {
		this.fShuffle = shuffle;
	}

	public boolean getShuffle() {
		return fShuffle;
	}

	public BasicElementList getSimpleChoiceList() {
		return fSimpleChoiceList;
	}

	public void setSimpleChoiceList(BasicElementList list) {
		fSimpleChoiceList = list;
	}

	public void addSimpleChoice(SimpleChoice aSimpleChoice) {
		fSimpleChoiceList.addElement(aSimpleChoice);
	}

	public void removeSimpleChoice(SimpleChoice aSimpleChoice) {
		fSimpleChoiceList.removeElement(aSimpleChoice);
	}
	

	public void fromJDOM(Element element) {
		super.fromJDOM(element);
		
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.SHUFFLE.equals(tag)) {
				fShuffle = "true".equalsIgnoreCase(value);
			}
		}

		for (Object o : element.getChildren()) {
			Element child = (Element) o;
			String tag = child.getName();
			//String value = child.getText();

			if (tag.equals(AssessmentElementFactory.SIMPLE_CHOICE)) {
				SimpleChoice aSimpleChoice = new SimpleChoice(fAssessmentItem, this);
				aSimpleChoice.fromJDOM((Element) child);
				fSimpleChoiceList.addElement(aSimpleChoice);
			} 
		}
	}
	
	public Element toJDOM() {
		Element aInteraction = super.toJDOM();

		aInteraction.setAttribute(AssessmentElementFactory.SHUFFLE, fShuffle ? "true" : "false");
		
		for (int i = 0; i < fSimpleChoiceList.size(); i++) {
			Element aSimpleChoice  = ((SimpleChoice) fSimpleChoiceList.getBasicElementAt(i)).toJDOM();
			if (aSimpleChoice != null)
				aInteraction.addContent(aSimpleChoice);
		}
		return aInteraction;
	}

}
