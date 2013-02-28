package org.tencompetence.qtieditor.rule;

import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.BasicElementList;

public abstract class OutcomeRule extends AbstractRule{

	protected BasicElementList fOutcomeRuleList = new BasicElementList(); //required

	public BasicElementList getOutcomeRuleList() {
		return fOutcomeRuleList;
	}

	public void setOutcomeRuleList(BasicElementList list) {
		fOutcomeRuleList = list;
	}

	public void addOutcomeRule(OutcomeRule aOutcomeRule) {
		fOutcomeRuleList.addElement(aOutcomeRule);
	}

	public void removeOutcomeRule(OutcomeRule aOutcomeRule) {
		fOutcomeRuleList.removeElement(aOutcomeRule);
	}
	
	public void fromJDOM(Element element) {
		for (Object object : element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();

			if (tag.equals(AssessmentElementFactory.SET_OUTCOME_VALUE)) {
				SetOutcomeValue aOutcomeRule = new SetOutcomeValue(getAssessment());
				aOutcomeRule.fromJDOM((Element)child);
				fOutcomeRuleList.addElement(aOutcomeRule);
			} else if (tag.equals(AssessmentElementFactory.OUTCOME_CONDITION)) {
				OutcomeCondition aOutcomeRule = new OutcomeCondition(getAssessment());
				aOutcomeRule.fromJDOM((Element)child);
				fOutcomeRuleList.addElement(aOutcomeRule);
			} else if (tag.equals(AssessmentElementFactory.EXIT_TEST)) {
				ExitTest aOutcomeRule = new ExitTest(getAssessment());
				aOutcomeRule.fromJDOM((Element)child);
				fOutcomeRuleList.addElement(aOutcomeRule);
			} 
		}		
	}

	public Element toJDOM() {
        Element aElement = new Element(getTagName(), getTestNamespace());

		for (int i = 0; i < fOutcomeRuleList.size(); i++) {
			AbstractRule aRule = ((AbstractRule)fOutcomeRuleList
					.getBasicElementAt(i));
			if (aRule instanceof SetOutcomeValue) {
				aElement.addContent(((SetOutcomeValue)aRule).toJDOM());
			} else if (aRule instanceof OutcomeCondition) {
				aElement.addContent(((OutcomeCondition)aRule).toJDOM());
			} else if (aRule instanceof ExitTest) {
				aElement.addContent(((ExitTest)aRule).toJDOM());
			}
		}

        return aElement;
	}
}
