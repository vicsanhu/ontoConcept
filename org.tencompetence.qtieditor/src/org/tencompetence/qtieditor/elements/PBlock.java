package org.tencompetence.qtieditor.elements;

public class PBlock extends Block {

	private String fData = ""; // optional

	/** Creates a new instance of PBlock */
	public PBlock(AssessmentItem anAssessmentItem) {
		setAssessmentItem(anAssessmentItem);
		//super.createId("PB");
	}

	public void setData(String aData) {
		fData = aData;
	}

	public String getData() {
		return fData;
	}

	public String getTagName() {
		return AssessmentElementFactory.P;
	}

}
