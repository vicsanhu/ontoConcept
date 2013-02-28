package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

import org.tencompetence.qtieditor.rule.OutcomeCondition;
import org.tencompetence.qtieditor.rule.ResponseCondition;
import org.tencompetence.qtieditor.rule.ResponseRule;
import org.tencompetence.qtieditor.rule.SetOutcomeValue;

public class ResponseProcessing extends BasicElement {

	// elements
	private String fTemplate = "";
	private BasicElementList fResponseRuleList = new BasicElementList();
	private AssessmentItem fAssessmentItem;

	/** Creates a new instance of ResponseProcessing */
	public ResponseProcessing(AssessmentItem anAssessmentItem) {
		fAssessmentItem = anAssessmentItem;
		//fResponseRuleList.addElement(null);
	}

	public AssessmentItem getAssessmentItem() {
		return fAssessmentItem;
	}

	public void setTemplate(String aTemplate) {
		this.fTemplate = aTemplate;
	}

	public String getTemplate() {
		return fTemplate;
	}

	public BasicElementList getResponseRuleList() {
		return fResponseRuleList;
	}

	public void addResponseRule(ResponseRule aResponseRule) {
		fResponseRuleList.addElement(aResponseRule);
	}

	public void setResponseRuleAt(int index, ResponseRule aResponseRule) {
		fResponseRuleList.setElementAt(index, aResponseRule);
	}
	
	public void removeResponseRule(ResponseRule aResponseRule) {
		fResponseRuleList.removeElement(aResponseRule);
	}

	public void fromJDOM(Element element) {
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.TEMPLATE.equals(tag)) {
				fTemplate = value;
			}
		}
		
		for (Object object : element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();
			String value = child.getText();

			if (tag.equals(AssessmentElementFactory.RESPONSE_CONDITION)) {
				ResponseCondition aResponseCondition = new ResponseCondition(fAssessmentItem);
				aResponseCondition.fromJDOM((Element)child);
				fResponseRuleList.addElement(aResponseCondition);
			} 
			else if (tag.equals(AssessmentElementFactory.SET_OUTCOME_VALUE)) {
				SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(fAssessmentItem);
				aSetOutcomeValue.fromJDOM((Element)child);
				fResponseRuleList.addElement(aSetOutcomeValue);
			} 
		}
	}

	public Element toJDOM() {

		if (fResponseRuleList.size()==0) return null;
		
		Element aResponseProcessingElement = new Element(getTagName(), getNamespace());

		for (int i = 0; i < fResponseRuleList.size(); i++) {
			BasicElement aBasicElement = ((BasicElement) fResponseRuleList.getBasicElementAt(i));
			if (aBasicElement instanceof ResponseCondition) {
				aResponseProcessingElement.addContent(((ResponseCondition)aBasicElement).toJDOM());
			}
			else if (aBasicElement instanceof SetOutcomeValue) {
				aResponseProcessingElement.addContent(((SetOutcomeValue)aBasicElement).toJDOM());
			}
		}

		return aResponseProcessingElement;
	}
		
	public String getTagName() {
		return AssessmentElementFactory.RESPONSE_PROCESSING;
	}

}
