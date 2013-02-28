package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;
import org.tencompetence.qtieditor.rule.BranchRule;
import org.tencompetence.qtieditor.rule.PreCondition;

public abstract class SectionPart extends TestElement {

	// attributes
	protected boolean fRequired = false; // optional, default
	protected boolean fFixed = false; // optional, default

	// elements
	protected BasicElementList fPreConditionList;
	protected BasicElementList fBranchRuleList;
	protected ItemSessionControl fItemSessionControl;
	protected TimeLimits fTimeLimits;

	public void setRequired(boolean aRequired) {
		fRequired = aRequired;
	}

	public boolean getRequired() {
		return fRequired;
	}

	public void setFixed(boolean aFixed) {
		fFixed = aFixed;
	}

	public boolean getFixed() {
		return fFixed;
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

	public BasicElementList getBranchRuleList() {
		return fBranchRuleList;
	}

	public void addBranchRule(BranchRule aBranchRule) {
		fBranchRuleList.addElement(aBranchRule);
	}

	public void removeBranchRule(BranchRule aBranchRule) {
		fBranchRuleList.removeElement(aBranchRule);
	}

	public void setItemSessionControl(ItemSessionControl aItemSessionControl) {
		this.fItemSessionControl = aItemSessionControl;
	}

	public ItemSessionControl getItemSessionControl() {
		return fItemSessionControl;
	}

	public void setTimeLimits(TimeLimits timeLimits) {
		fTimeLimits = timeLimits;
	}

	public TimeLimits getTimeLimits() {
		return fTimeLimits;
	}

	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.REQUIRED.equals(tag)) {
				fRequired = "true".equalsIgnoreCase(value);
			} else if (AssessmentElementFactory.FIXED.equals(tag)) {
				fFixed = "true".equalsIgnoreCase(value);
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
			} else if (tag.equals(AssessmentElementFactory.ITEM_SESSION_CONTROL)) {
				fItemSessionControl.fromJDOM((Element) child);
			} else if (tag.equals(AssessmentElementFactory.TIME_LIMITS)) {
				fTimeLimits.fromJDOM((Element) child);
			}
		}
	}

	public Element toJDOM() {

		Element aSectionPartElement = new Element(getTagName(), getTestNamespace());

		aSectionPartElement.setAttribute(AssessmentElementFactory.IDENTIFIER,
				getId());
		if (fRequired)
			aSectionPartElement.setAttribute(AssessmentElementFactory.REQUIRED, "true");
		if (fFixed) 
			aSectionPartElement.setAttribute(AssessmentElementFactory.FIXED, "true");

		for (int i = 0; i < fPreConditionList.size(); i++) {
			Element aPreCondition = ((PreCondition) fPreConditionList
					.getBasicElementAt(i)).toJDOM();
			if (aPreCondition != null)
				aSectionPartElement.addContent(aPreCondition);
		}

		for (int i = 0; i < fBranchRuleList.size(); i++) {
			Element aBranchRule = ((BranchRule) fBranchRuleList
					.getBasicElementAt(i)).toJDOM();
			if (aBranchRule != null)
				aSectionPartElement.addContent(aBranchRule);
		}

		if (fItemSessionControl != null) {
			Element aItemSessionControl = fItemSessionControl.toJDOM();
			aSectionPartElement.addContent(aItemSessionControl);
		}
		
		if (fTimeLimits != null) {
			Element timeLimits = fTimeLimits.toJDOM();
			aSectionPartElement.addContent(timeLimits);
		}

		return aSectionPartElement;
	}

}
