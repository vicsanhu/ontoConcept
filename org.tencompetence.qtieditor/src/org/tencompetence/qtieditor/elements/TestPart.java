package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;
import org.tencompetence.qtieditor.rule.BranchRule;
import org.tencompetence.qtieditor.rule.PreCondition;

public class TestPart extends TestElement {

	// attributes
	private String fNavigationMode = NavigationMode.NONLINEAR; // required: linear
															// or nonlinear
	private String fSubmissionMode = SubmissionMode.SIMULTANEOUS; // required:
																// individual or
																// simultaneous

	// elements
	private BasicElementList fPreConditionList;
	private BasicElementList fBranchRuleList;
	private BasicElementList fAssessmentSectionList;
	private ItemSessionControl fItemSessionControl = null;
	private TimeLimits fTimeLimits;
	private BasicElementList fTestFeedbackList;

	public TestPart(AssessmentTest anAssessmentTest) {
		super.createId("TP");
		setAssessmentTest(anAssessmentTest);
		fPreConditionList = new BasicElementList();
		fBranchRuleList = new BasicElementList();
		//fTimeLimits = new TimeLimits();
		fAssessmentSectionList = new BasicElementList();
		fTestFeedbackList = new BasicElementList();

	}
/*
	public TestPart(AssessmentTest anAssessmentTest, String id) {
		fID = id;
		setAssessmentTest(anAssessmentTest);
		fPreConditionList = new BasicElementList();
		fBranchRuleList = new BasicElementList();
		//fTimeLimits = new TimeLimits();
		fAssessmentSectionList = new BasicElementList();
		fTestFeedbackList = new BasicElementList();
	}
*/
	public void setToolName(String toolName) {
		fNavigationMode = toolName;
	}

	public String getToolName() {
		return fNavigationMode;
	}

	public void setToolVersion(String toolVersion) {
		fSubmissionMode = toolVersion;
	}

	public String getToolVersion() {
		return fSubmissionMode;
	}

	public void setTimeLimits(TimeLimits timeLimits) {
		fTimeLimits = timeLimits;
	}

	public TimeLimits getTimeLimits() {
		return fTimeLimits;
	}

	public BasicElementList getPreConditionList() {
		return fPreConditionList;
	}

	public void addPreCondition(PreCondition aPreCondition) {
		fPreConditionList.addElement(aPreCondition);
	}

	public void removePreCondition(PreCondition aPreCondition) {
		fPreConditionList.removeElement(aPreCondition);
	}

	public PreCondition getFirstPreCondition() {
		return (PreCondition) fPreConditionList.getBasicElementAt(0);
	}

	public BasicElementList getBranchRuleList() {
		return fBranchRuleList;
	}

	public void addBranchRule(BranchRule aBranchRule) {
		fBranchRuleList.addElement(aBranchRule);
	}

	public void removeBranchRule(BranchRule aBranchRule) {
		fBranchRuleList.removeElement(aBranchRule);
	}

	public BranchRule getFirstBranchRule() {
		return (BranchRule) fBranchRuleList.getBasicElementAt(0);
	}

	public BasicElementList getAssessmentSectionList() {
		return fAssessmentSectionList;
	}

	public void addAssessmentSection(AssessmentSection aAssessmentSection) {
		fAssessmentSectionList.addElement(aAssessmentSection);
	}

	public void removeAssessmentSection(AssessmentSection aAssessmentSection) {
		fAssessmentSectionList.removeElement(aAssessmentSection);
	}

	public AssessmentSection getFirstAssessmentSection() {
		return (AssessmentSection) fAssessmentSectionList.getBasicElementAt(0);
	}

	public AssessmentSection getAssessmentSectionByID(String id) {
		AssessmentSection aAssessmentSection = null;
		for (int i = 0; i < fAssessmentSectionList.size(); i++) {
			aAssessmentSection = (AssessmentSection) fAssessmentSectionList
					.getBasicElementAt(i);
			if (aAssessmentSection.getId().equals(id))
				return aAssessmentSection;
		}
		return null;
	}

	public void setItemSessionControl(ItemSessionControl aItemSessionControl) {
		this.fItemSessionControl = aItemSessionControl;
	}

	public ItemSessionControl getItemSessionControl() {
		return fItemSessionControl;
	}

	public BasicElementList getTestFeedbackList() {
		return fTestFeedbackList;
	}

	public void setTestFeedbackList(BasicElementList list) {
		fTestFeedbackList = list;
	}

	public void addTestFeedback(TestFeedback aTestFeedback) {
		fTestFeedbackList.addElement(aTestFeedback);
	}

	public void removeTestFeedback(TestFeedback aTestFeedback) {
		fTestFeedbackList.removeElement(aTestFeedback);
	}

	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.NAVIGATION_MODE.equals(tag)) {
				fNavigationMode = value;
			} else if (AssessmentElementFactory.SUBMISSION_MODE.equals(tag)) {
				fSubmissionMode = value;
			}
		}

		for (Object object : element.getChildren()) {
			Element child = (Element) object;
			String tag = child.getName();
			// String value = child.getText();

			if (tag.equals(AssessmentElementFactory.PRE_CONDITION)) {
				PreCondition aPreCondition = new PreCondition(getAssessmentTest());
				aPreCondition.fromJDOM((Element) child);
				fPreConditionList.addElement(aPreCondition);
			} else if (tag.equals(AssessmentElementFactory.BRANCH_RULE)) {
				BranchRule aBranchRule = new BranchRule(getAssessmentTest());
				aBranchRule.fromJDOM((Element) child);
				fBranchRuleList.addElement(aBranchRule);
			} else if (tag
					.equals(AssessmentElementFactory.ITEM_SESSION_CONTROL)) {
				fItemSessionControl.fromJDOM((Element) child);
			} else if (tag.equals(AssessmentElementFactory.TIME_LIMITS)) {
				fTimeLimits.fromJDOM((Element) child);
			} else if (tag.equals(AssessmentElementFactory.ASSESSMENT_SECTION)) {
				AssessmentSection aAssessmentSection = new AssessmentSection(getAssessmentTest());
				aAssessmentSection.fromJDOM((Element) child);
				fAssessmentSectionList.addElement(aAssessmentSection);
			} else if (tag.equals(AssessmentElementFactory.TEST_FEEDBACK)) {
				/* test feedback need refer to the unit where to give feedbacks
				 * test is defined as the unit. it will be the next step to support section
				TestFeedback aTestFeedback = new TestFeedback();
				aTestFeedback.fromJDOM((Element) child);
				fTestFeedbackList.addElement(aTestFeedback);
				*/
			}
		}
	}

	public Element toJDOM() {

		Element aElement = new Element(getTagName(), getTestNamespace());

		aElement.setAttribute(AssessmentElementFactory.IDENTIFIER, getId());
		aElement.setAttribute(AssessmentElementFactory.NAVIGATION_MODE,
				fNavigationMode);
		aElement.setAttribute(AssessmentElementFactory.SUBMISSION_MODE,
				fSubmissionMode);

		for (int i = 0; i < fPreConditionList.size(); i++) {
			Element aPreCondition = ((PreCondition) fPreConditionList
					.getBasicElementAt(i)).toJDOM();
			if (aPreCondition != null)
				aElement.addContent(aPreCondition);
		}

		for (int i = 0; i < fBranchRuleList.size(); i++) {
			Element aBranchRule = ((BranchRule) fBranchRuleList
					.getBasicElementAt(i)).toJDOM();
			if (aBranchRule != null)
				aElement.addContent(aBranchRule);
		}

		if (fTimeLimits != null) {
			Element timeLimits = fTimeLimits.toJDOM();
			aElement.addContent(timeLimits);
		}

		for (int i = 0; i < fAssessmentSectionList.size(); i++) {
			Element aAssessmentSection = ((AssessmentSection) fAssessmentSectionList
					.getBasicElementAt(i)).toJDOM();
			if (aAssessmentSection != null)
				aElement.addContent(aAssessmentSection);
		}

		if (fItemSessionControl != null) {
			Element aItemSessionControl = fItemSessionControl.toJDOM();
			aElement.addContent(aItemSessionControl);
		}

		for (int i = 0; i < fTestFeedbackList.size(); i++) {
			Element testFeedback = ((TestFeedback) fTestFeedbackList
					.getBasicElementAt(i)).toJDOM();
			if (testFeedback != null)
				aElement.addContent(testFeedback);
		}

		return aElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.TEST_PART;
	}

}