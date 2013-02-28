package org.tencompetence.qtieditor.elements;

public abstract class TestElement extends IdentifiedElement {

	protected AssessmentTest fAssessmentTest;

	public void setAssessmentTest(AssessmentTest aAssessmentTest) {
		fAssessmentTest = aAssessmentTest;
	}
	public AssessmentTest getAssessmentTest() {
		return fAssessmentTest;
	}
}
