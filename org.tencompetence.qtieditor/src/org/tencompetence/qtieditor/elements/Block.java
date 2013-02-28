/**
 * 
 */
package org.tencompetence.qtieditor.elements;

/**
 * @author ymi
 * 
 */
public abstract class Block extends BodyElement {
	
	protected AssessmentItem fAssessmentItem;

	/*
	protected Block(AssessmentItem anAssessmentItem) {
		fAssessmentItem = anAssessmentItem;
	}
	*/

	public void setAssessmentItem(AssessmentItem anAssessmentItem) {
		fAssessmentItem = anAssessmentItem;
	}

	public AssessmentItem getAssessmentItem() {
		return fAssessmentItem;
	}

}
