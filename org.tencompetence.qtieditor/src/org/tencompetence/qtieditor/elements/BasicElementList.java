package org.tencompetence.qtieditor.elements;

import java.util.ArrayList;
import java.util.List;

// for the convenience to create XML element
public class BasicElementList {

	private List<BasicElement> fDefinedElementList = new ArrayList<BasicElement>();

	/** Creates a new instance of DefinedElementModel */
	public BasicElementList() {
	}

	/**
	 * handle definedElements
	 */
	/*
	 * 	public void setDefinedElementList(ArrayList aList) {
		this.definedElements = aList;
	}

	public ArrayList getDefinedElementList() {
		return definedElements;
	}
*/
	public BasicElement getBasicElementAt(int i) {
		return (BasicElement) fDefinedElementList.get(i);
	}

	public void addElement(BasicElement basicElement) {
		if (!fDefinedElementList.contains(basicElement))
			fDefinedElementList.add(basicElement);
	}

	public void addElementAt(int index, BasicElement basicElement) {
		if (index <= size())
			fDefinedElementList.add(index, basicElement);
	}
	
	public void setElementAt(int index, BasicElement basicElement) {
		if (index < size())
			fDefinedElementList.set(index, basicElement);
	}

	public void removeElement(BasicElement basicElement) {
		fDefinedElementList.remove(basicElement);
	}

	public void removeElementAt(int index) {
		fDefinedElementList.remove(index);
	}

	public void clear() {
		fDefinedElementList.clear();
	}

	public int size() {
		return fDefinedElementList.size();
	}
}