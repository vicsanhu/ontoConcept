package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class GapText extends GapChoice {
	
	private String fData = ""; // required
	private BasicElementList fMatchedGapList = new BasicElementList();

	public GapText(AssessmentItem aAssessmentItem, GapMatchInteraction aGapMatchInteraction) {
		super.createId("GT");
		fAssessmentItem = aAssessmentItem;
		fGapMatchInteraction = aGapMatchInteraction;
	}

	public void setData(String data) {
		this.fData = data;
	}

	public String getData() {
		return fData;
	}

	public BasicElementList getMatchedGapList() {
		return fMatchedGapList;
	}

	public void setMatchedGapList(BasicElementList list) {
		fMatchedGapList = list;
	}

	public void addMatchedGap(Gap aGap) {
		fMatchedGapList.addElement(aGap);
	}

	public void addMatchedGapAt(int index, Gap aGap) {
		fMatchedGapList.addElementAt(index, aGap);
	}

	public void setMatchedGapAt(int index, Gap aGap) {
		fMatchedGapList.setElementAt(index, aGap);
	}
	
	public Gap getMatchedGapByID(String id) {
		for (int i = 0; i < fMatchedGapList.size(); i++) {
			Gap aGap  = (Gap) fMatchedGapList.getBasicElementAt(i);
			if (aGap.equals(id))
				return aGap;
		}
		return null;
	}
	
	public void removeMatchedGap(Gap aGap) {
		fMatchedGapList.removeElement(aGap);
	}

	public void fromJDOM(Element element) {

		super.fromJDOM(element);
		fData = element.getText();
	}

	public Element toJDOM() {
		Element aChoiceElement = super.toJDOM();
		aChoiceElement.setText(getData());
		return aChoiceElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.GAP_TEXT;
	}

}