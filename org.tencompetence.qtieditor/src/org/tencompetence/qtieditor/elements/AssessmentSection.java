package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;
import org.tencompetence.qtieditor.rule.BranchRule;
import org.tencompetence.qtieditor.rule.PreCondition;

public class AssessmentSection extends SectionPart {

	// attributes
	private String fTitle = ""; // required
	private boolean fVisible = true; // required, default
	private boolean fKeepTogether = true; // optional, default

	// elements
	private ItemSessionControl fItemSessionControl = null;
	private Selection fSelection;
	private Ordering fOrdering;
	private BasicElementList fRubricBlockList;
	private BasicElementList fSectionPartList;

	public AssessmentSection(AssessmentTest anAssessmentTest) {
		super.createId("AS");
		setAssessmentTest(anAssessmentTest);
		init();
	}

	public AssessmentSection(AssessmentTest anAssessmentTest, String id) {
		fID = id;
		setAssessmentTest(anAssessmentTest);
		init();
	}

	private void init() {
		fPreConditionList = new BasicElementList();
		fBranchRuleList = new BasicElementList();
		fSelection = new Selection();
		fOrdering = new Ordering();
		fRubricBlockList = new BasicElementList();
		fSectionPartList = new BasicElementList();
	}

	public void setTitle(String aTitle) {
		fTitle = aTitle;
	}

	public String getTitle() {
		return fTitle;
	}

	public void setVisible(boolean aVisible) {
		fVisible = aVisible;
	}

	public boolean getVisible() {
		return fVisible;
	}

	public void setKeepTogether(boolean aKeepTogether) {
		fKeepTogether = aKeepTogether;
	}

	public boolean getKeepTogether() {
		return fKeepTogether;
	}

	public void setSelection(Selection aSelection) {
		fSelection = aSelection;
	}

	public Selection getSelection() {
		return fSelection;
	}

	public void setOrdering(Ordering aOrdering) {
		fOrdering = aOrdering;
	}

	public Ordering getOrdering() {
		return fOrdering;
	}

	public BasicElementList getRubricBlockList() {
		return fRubricBlockList;
	}

	public void addRubricBlock(RubricBlock aRubricBlock) {
		fRubricBlockList.addElement(aRubricBlock);
	}

	public void removeRubricBlock(RubricBlock aRubricBlock) {
		fRubricBlockList.removeElement(aRubricBlock);
	}

	public BasicElementList getSectionPartList() {
		return fSectionPartList;
	}

	public void setSectionPartList(BasicElementList aSectionPartList) {
		fSectionPartList = aSectionPartList;
	}
	
	public void addSectionPart(SectionPart aSectionPart) {
		fSectionPartList.addElement(aSectionPart);
	}

	public void addSectionPartAt(int index, SectionPart aSectionPart) {
		fSectionPartList.addElementAt(index, aSectionPart);
	}

	public void removeSectionPart(SectionPart aSectionPart) {
		fSectionPartList.removeElement(aSectionPart);
	}

	public void removeSectionPartAt(int index) {
		fSectionPartList.removeElementAt(index);
	}

	public SectionPart getSectionPartAt(int index) {
		return (SectionPart)fSectionPartList.getBasicElementAt(index);
	}

	public SectionPart getSectionPartByID(String id) {
		SectionPart aSectionPart = null;
		for (int i = 0; i < fSectionPartList.size(); i++) {
			aSectionPart = (SectionPart) fSectionPartList.getBasicElementAt(i);
			if (aSectionPart.getId().equals(id))
				return aSectionPart;
		}
		return null;
	}

	public void setItemSessionControl(ItemSessionControl aItemSessionControl) {
		this.fItemSessionControl = aItemSessionControl;
	}

	public ItemSessionControl getItemSessionControl() {
		return fItemSessionControl;
	}

	public void fromJDOM(Element element) {

		super.fromJDOM(element);

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.TITLE.equals(tag)) {
				fTitle = value;
			} else if (AssessmentElementFactory.VISIBLE.equals(tag)) {
				fVisible = "true".equalsIgnoreCase(value);
			} else if (AssessmentElementFactory.KEEP_TOGETHER.equals(tag)) {
				fKeepTogether = "true".equalsIgnoreCase(value);
			}
		}

		for (Object object : element.getChildren()) {
			Element child = (Element) object;
			String tag = child.getName();
			// String value = child.getText();

			if (tag.equals(AssessmentElementFactory.ITEM_SESSION_CONTROL)) {
				fItemSessionControl.fromJDOM((Element) child);
			} else if (tag.equals(AssessmentElementFactory.SELECTION)) {
				fSelection.fromJDOM((Element) child);
			} else if (tag.equals(AssessmentElementFactory.ORDERING)) {
				fOrdering.fromJDOM((Element) child);
			} else if (tag.equals(AssessmentElementFactory.RUBRIC_BLOCK)) {
				RubricBlock aRubricBlock = new RubricBlock();
				aRubricBlock.fromJDOM((Element) child);
				fRubricBlockList.addElement(aRubricBlock);
			} else if (tag.equals(AssessmentElementFactory.ASSESSMENT_SECTION)) {
				AssessmentSection aAssessmentSection = new AssessmentSection(getAssessmentTest());
				aAssessmentSection.fromJDOM((Element) child);
				fSectionPartList.addElement(aAssessmentSection);
			} else if (tag.equals(AssessmentElementFactory.ASSESSMENT_ITEM_REF)) {
				AssessmentItemRef aAssessmentItemRef = new AssessmentItemRef(getAssessmentTest());
				aAssessmentItemRef.fromJDOM((Element) child);
				fSectionPartList.addElement(aAssessmentItemRef);
			}
		}
	}

	public Element toJDOM() {

		Element aAssessmentSectionElement = new Element(getTagName(),
				getTestNamespace());

		aAssessmentSectionElement.setAttribute(
				AssessmentElementFactory.IDENTIFIER, getId());
		aAssessmentSectionElement
				.setAttribute(AssessmentElementFactory.REQUIRED,
						fRequired ? "true" : "false");
		aAssessmentSectionElement.setAttribute(AssessmentElementFactory.FIXED,
				fFixed ? "true" : "false");
		aAssessmentSectionElement.setAttribute(AssessmentElementFactory.TITLE,
				fTitle);
		aAssessmentSectionElement.setAttribute(
				AssessmentElementFactory.VISIBLE, fVisible ? "true" : "false");
		aAssessmentSectionElement.setAttribute(
				AssessmentElementFactory.KEEP_TOGETHER, fKeepTogether ? "true"
						: "false");

		for (int i = 0; i < fPreConditionList.size(); i++) {
			Element aPreCondition = ((PreCondition) fPreConditionList
					.getBasicElementAt(i)).toJDOM();
			if (aPreCondition != null)
				aAssessmentSectionElement.addContent(aPreCondition);
		}

		for (int i = 0; i < fBranchRuleList.size(); i++) {
			Element aBranchRule = ((BranchRule) fBranchRuleList
					.getBasicElementAt(i)).toJDOM();
			if (aBranchRule != null)
				aAssessmentSectionElement.addContent(aBranchRule);
		}

		if (fItemSessionControl != null) {
			Element aItemSessionControl = fItemSessionControl.toJDOM();
			aAssessmentSectionElement.addContent(aItemSessionControl);
		}

		if (fTimeLimits != null) {
			Element timeLimits = fTimeLimits.toJDOM();
			aAssessmentSectionElement.addContent(timeLimits);
		}

		if (fSelection != null) {
			Element aSelectionElement = fSelection.toJDOM();
			aAssessmentSectionElement.addContent(aSelectionElement);
		}

		if (fOrdering != null) {
			Element aOrderingElement = fOrdering.toJDOM();
			aAssessmentSectionElement.addContent(aOrderingElement);
		}

		for (int i = 0; i < fRubricBlockList.size(); i++) {
			RubricBlock aRubricBlock = ((RubricBlock) fRubricBlockList
					.getBasicElementAt(i));
			Element aRubricBlockElement = aRubricBlock.toJDOM();
			aAssessmentSectionElement.addContent(aRubricBlockElement);
		}

		for (int i = 0; i < fSectionPartList.size(); i++) {
			SectionPart aSectionPart = ((SectionPart) fSectionPartList
					.getBasicElementAt(i));
			if (aSectionPart instanceof AssessmentItemRef) {
				Element aAssessmentItemRefElement = ((AssessmentItemRef) aSectionPart)
						.toJDOM();
				aAssessmentSectionElement.addContent(aAssessmentItemRefElement);
			} else if (aSectionPart instanceof AssessmentSection) {
				Element anotherAssessmentSectionElement = ((AssessmentSection) aSectionPart)
						.toJDOM();
				aAssessmentSectionElement
						.addContent(anotherAssessmentSectionElement);
			}
		}

		return aAssessmentSectionElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.ASSESSMENT_SECTION;
	}
}
