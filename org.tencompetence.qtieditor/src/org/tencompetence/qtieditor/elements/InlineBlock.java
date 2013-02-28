package org.tencompetence.qtieditor.elements;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.Text;

public abstract class InlineBlock extends Block {
	
	protected String data = ""; // optional
	protected BasicElementList fInteractionList = new BasicElementList();;

	/** Creates a new instance of FillingInBlankBlock 
	public InlineBlock(AssessmentItem anAssessmentItem) {
		//super.createId("FIB");
		fInteractionList = new BasicElementList();
	}*/

	public void setData(String data) {
		this.data = data;
	}

	public void appandData(String addition) {
		this.data = data + addition;
	}
	
	public String getData() {
		return data;
	}
	
	public BasicElementList getInteractionList() {
		return fInteractionList;
	}

	public void setInteractionList(BasicElementList list) {
		fInteractionList = list;
	}

	public void addInteraction(InlineInteraction aInlineInteraction) {
		fInteractionList.addElement(aInlineInteraction);
	}

	public void addInteractionAt(int index, InlineInteraction aInlineInteraction) {
		fInteractionList.addElementAt(index, aInlineInteraction);
	}
	public void removeInteraction(InlineInteraction aInlineInteraction) {
		fInteractionList.removeElement(aInlineInteraction);
	}
	
}	

