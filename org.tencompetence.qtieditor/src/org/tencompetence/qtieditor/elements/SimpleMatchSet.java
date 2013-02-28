package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class SimpleMatchSet extends BasicElement{
	
	private BasicElementList fSimpleAssociableChoiceList = new BasicElementList();

	/** Creates a new instance of Mapping */
	public SimpleMatchSet() {
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
	
	public SimpleAssociableChoice getSimpleAssociableChoiceAt(int index) {
		return (SimpleAssociableChoice)fSimpleAssociableChoiceList.getBasicElementAt(index);
	}

	public SimpleAssociableChoice getSimpleAssociableChoiceByID(String id) {
		for (int i=0; i<size(); i++) {
			SimpleAssociableChoice aSimpleAssociableChoice = (SimpleAssociableChoice)fSimpleAssociableChoiceList.getBasicElementAt(i);
			if (aSimpleAssociableChoice.getId().equalsIgnoreCase(id)) {
				return aSimpleAssociableChoice;
			}
		}
		return null;
	}

	public void removeSimpleAssociableChoice(SimpleAssociableChoice aSimpleAssociableChoice) {
		fSimpleAssociableChoiceList.removeElement(aSimpleAssociableChoice);
	}
	
	public int size() {
		return fSimpleAssociableChoiceList.size();
	}

	public void fromJDOM(Element element) {

		for (Object object: element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();

			if (tag.equals(AssessmentElementFactory.SIMPLE_ASSOCIABLE_CHOICE)) {
				SimpleAssociableChoice aSimpleAssociableChoice = new SimpleAssociableChoice();
				aSimpleAssociableChoice.fromJDOM((Element)child);
				fSimpleAssociableChoiceList.addElement(aSimpleAssociableChoice);
			} 
		}
	}
	
	public Element toJDOM() {
		
		int size = size();
		if (size==0) return null;
		
		Element aElement = new Element(getTagName(), getNamespace());
		
		for (int i = 0; i < size(); i++) {
			Element aSimpleAssociableChoiceElement = ((SimpleAssociableChoice) fSimpleAssociableChoiceList.getBasicElementAt(i)).toJDOM();
			if (aSimpleAssociableChoiceElement != null)
				aElement.addContent(aSimpleAssociableChoiceElement);
		}
		return aElement ;
	}
	
	@Override
	protected String getTagName() {
		return AssessmentElementFactory.SIMPLE_MATCH_SET;
	}
}
