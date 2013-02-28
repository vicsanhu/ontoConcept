package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

import org.tencompetence.qtieditor.rule.*;

public class OutcomeProcessing extends BodyElement {

	// elements
	private String fTemplate = "";
	private BasicElementList fOutcomeRuleList;
	private AssessmentTest fAssessmentTest;

	/** Creates a new instance of OutcomeProcessing */
	public OutcomeProcessing(AssessmentTest anAssessmentTest) {
		super.createId("OP");
		fAssessmentTest = anAssessmentTest;
		fOutcomeRuleList = new BasicElementList();
	}

	public AssessmentTest getAssessmentTest() {
		return fAssessmentTest;
	}

	public void setTemplate(String aTemplate) {
		this.fTemplate = aTemplate;
	}

	public String getTemplate() {
		return fTemplate;
	}

	public BasicElementList getOutcomeRuleList() {
		return fOutcomeRuleList;
	}

	public void addOutcomeRule(OutcomeRule aOutcomeRule) {
		fOutcomeRuleList.addElement(aOutcomeRule);
	}

	public void removeOutcomeRule(OutcomeRule aOutcomeRule) {
		fOutcomeRuleList.removeElement(aOutcomeRule);
	}

	public void fromJDOM(Element element) {
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			}
		}
		
		for (Object object : element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();
			String value = child.getText();

			if (tag.equals(AssessmentElementFactory.OUTCOME_CONDITION)) {
				OutcomeCondition aOutcomeCondition = new OutcomeCondition(fAssessmentTest);
				aOutcomeCondition.fromJDOM((Element)child);
				fOutcomeRuleList.addElement(aOutcomeCondition);
			} 
			else if (tag.equals(AssessmentElementFactory.SET_OUTCOME_VALUE)) {
				SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(fAssessmentTest);
				aSetOutcomeValue.fromJDOM((Element)child);
				fOutcomeRuleList.addElement(aSetOutcomeValue);
			} 
			else if (tag.equals(AssessmentElementFactory.LOOKUP_OUTCOME_VALUE)) {
				LookupOutcomeValue aLookupOutcomeValue = new LookupOutcomeValue(fAssessmentTest);
				aLookupOutcomeValue.fromJDOM((Element)child);
				fOutcomeRuleList.addElement(aLookupOutcomeValue);
			} 
		}
	}

	public Element toJDOM() {

		Element aOutcomeProcessingElement = new Element(getTagName(), getTestNamespace());

		for (int i = 0; i < fOutcomeRuleList.size(); i++) {
			BasicElement aBasicElement = ((BasicElement) fOutcomeRuleList.getBasicElementAt(i));
			if (aBasicElement instanceof OutcomeCondition) {
				aOutcomeProcessingElement.addContent(((OutcomeCondition)aBasicElement).toJDOM());
			}
			else if (aBasicElement instanceof SetOutcomeValue) {
				aOutcomeProcessingElement.addContent(((SetOutcomeValue)aBasicElement).toJDOM());
			}
			else if (aBasicElement instanceof LookupOutcomeValue) {
				aOutcomeProcessingElement.addContent(((LookupOutcomeValue)aBasicElement).toJDOM());
			}
				
		}

		return aOutcomeProcessingElement;
	}
	
	public String getTagName() {
		return AssessmentElementFactory.OUTCOME_PROCESSING;
	}

}
