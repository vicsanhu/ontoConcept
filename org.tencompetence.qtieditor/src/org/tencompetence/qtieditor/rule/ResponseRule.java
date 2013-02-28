package org.tencompetence.qtieditor.rule;

import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.BasicElementList;

public abstract class ResponseRule extends AbstractRule{

	protected BasicElementList fResponseRuleList = new BasicElementList(); //required

	public BasicElementList getResponseRuleList() {
		return fResponseRuleList;
	}

	public void setResponseRuleList(BasicElementList list) {
		fResponseRuleList = list;
	}

	public void addResponseRule(ResponseRule aResponseRule) {
		fResponseRuleList.addElement(aResponseRule);
	}

	public void removeResponseRule(ResponseRule aResponseRule) {
		fResponseRuleList.removeElement(aResponseRule);
	}
	
	public void fromJDOM(Element element) {
		for (Object object : element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();

			if (tag.equals(AssessmentElementFactory.SET_OUTCOME_VALUE)) {
				SetOutcomeValue aResponseRule = new SetOutcomeValue(getAssessment());
				aResponseRule.fromJDOM((Element)child);
				fResponseRuleList.addElement(aResponseRule);
			} else if (tag.equals(AssessmentElementFactory.RESPONSE_CONDITION)) {
				ResponseCondition aResponseRule = new ResponseCondition(getAssessment());
				aResponseRule.fromJDOM((Element)child);
				fResponseRuleList.addElement(aResponseRule);
			} else if (tag.equals(AssessmentElementFactory.EXIT_RESPONSE)) {
				ExitResponse aResponseRule = new ExitResponse(getAssessment());
				aResponseRule.fromJDOM((Element)child);
				fResponseRuleList.addElement(aResponseRule);
			} 
		}		
	}

	public Element toJDOM() {
        Element aElement = new Element(getTagName(), getNamespace());

		for (int i = 0; i < fResponseRuleList.size(); i++) {
			AbstractRule aRule = ((AbstractRule)fResponseRuleList
					.getBasicElementAt(i));
			if (aRule instanceof SetOutcomeValue) {
				aElement.addContent(((SetOutcomeValue)aRule).toJDOM());
			} else if (aRule instanceof ResponseCondition) {
				aElement.addContent(((ResponseCondition)aRule).toJDOM());
			} else if (aRule instanceof ExitResponse) {
				aElement.addContent(((ExitResponse)aRule).toJDOM());
			}
		}

        return aElement;
	}
}
