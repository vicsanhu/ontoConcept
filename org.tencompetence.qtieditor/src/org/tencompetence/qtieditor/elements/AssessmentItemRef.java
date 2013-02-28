package org.tencompetence.qtieditor.elements;


import java.io.File;

import org.jdom.Attribute;
import org.jdom.Element;

import org.tencompetence.qtieditor.rule.BranchRule;
import org.tencompetence.qtieditor.rule.PreCondition;

public class AssessmentItemRef extends SectionPart {

	// attributes
	private String fHref = ""; // required

	// elements
	private ItemSessionControl fItemSessionControl = null;
	private BasicElementList fVariableMappingList;
	private BasicElementList fWeightList;
	private BasicElementList fTemplateDefaultList;

	public AssessmentItemRef(AssessmentTest anAssessmentTest) {
		super.createId("AIR");
		setAssessmentTest(anAssessmentTest);
		fPreConditionList = new BasicElementList();
		fBranchRuleList = new BasicElementList();
		fVariableMappingList = new BasicElementList();
		fWeightList = new BasicElementList();
		fTemplateDefaultList = new BasicElementList();

	}

	public AssessmentItemRef(AssessmentTest anAssessmentTest, String id) {
		fID = id;
		setAssessmentTest(anAssessmentTest);
		fPreConditionList = new BasicElementList();
		fBranchRuleList = new BasicElementList();
		fVariableMappingList = new BasicElementList();
		fWeightList = new BasicElementList();
		fTemplateDefaultList = new BasicElementList();

	}

	public void setHref(String aHref) {
		fHref = aHref;
	}

	public String getHref() {
		return fHref;
	}

	public BasicElementList getVariableMappingList() {
		return fVariableMappingList;
	}

	public void addVariableMapping(VariableMapping aVariableMapping) {
		fVariableMappingList.addElement(aVariableMapping);
	}

	public void removeVariableMapping(VariableMapping aVariableMapping) {
		fVariableMappingList.removeElement(aVariableMapping);
	}

	public BasicElementList getWeightList() {
		return fWeightList;
	}

	public void addWeight(Weight aWeight) {
		fWeightList.addElement(aWeight);
	}

	public void removeWeight(Weight aWeight) {
		fWeightList.removeElement(aWeight);
	}

	public BasicElementList getTemplateDefaultList() {
		return fTemplateDefaultList;
	}

	public void addTemplateDefault(TemplateDefault aTemplateDefault) {
		fTemplateDefaultList.addElement(aTemplateDefault);
	}

	public void removeTemplateDefault(TemplateDefault aTemplateDefault) {
		fTemplateDefaultList.removeElement(aTemplateDefault);
	}
	
	public void fromJDOM(Element element) {

		super.fromJDOM(element);

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.HREF.equals(tag)) {
				fHref = value;
			}
		}

		for (Object object : element.getChildren()) {
			Element child = (Element) object;
			String tag = child.getName();
			// String value = child.getText();

			if (tag.equals(AssessmentElementFactory.VARIABLE_MAPPING)) {
				VariableMapping aVariableMapping = new VariableMapping();
				aVariableMapping.fromJDOM((Element) child);
				fVariableMappingList.addElement(aVariableMapping);
			} else if (tag.equals(AssessmentElementFactory.WEIGHT)) {
				Weight aWeight = new Weight();
				aWeight.fromJDOM((Element) child);
				fWeightList.addElement(aWeight);
			} else if (tag.equals(AssessmentElementFactory.WEIGHT)) {
				TemplateDefault aTemplateDefault = new TemplateDefault();
				aTemplateDefault.fromJDOM((Element) child);
				fTemplateDefaultList.addElement(aTemplateDefault);
			}
		}
	}

	public Element toJDOM() {

		Element aAssessmentItemRefElement = new Element(getTagName(),
				getTestNamespace());

		aAssessmentItemRefElement.setAttribute(
				AssessmentElementFactory.IDENTIFIER, getId());
		if (fRequired)
			aAssessmentItemRefElement.setAttribute(AssessmentElementFactory.REQUIRED, "true");
		if (fFixed) 
			aAssessmentItemRefElement.setAttribute(AssessmentElementFactory.FIXED, "true");
		aAssessmentItemRefElement.setAttribute(AssessmentElementFactory.HREF,
				fHref);

		for (int i = 0; i < fPreConditionList.size(); i++) {
			Element aPreCondition = ((PreCondition) fPreConditionList
					.getBasicElementAt(i)).toJDOM();
			if (aPreCondition != null)
				aAssessmentItemRefElement.addContent(aPreCondition);
		}

		for (int i = 0; i < fBranchRuleList.size(); i++) {
			Element aBranchRule = ((BranchRule) fBranchRuleList
					.getBasicElementAt(i)).toJDOM();
			if (aBranchRule != null)
				aAssessmentItemRefElement.addContent(aBranchRule);
		}

		if (fItemSessionControl != null) {
			Element aItemSessionControl = fItemSessionControl.toJDOM();
			aAssessmentItemRefElement.addContent(aItemSessionControl);
		}

		if (fTimeLimits != null) {
			Element timeLimits = fTimeLimits.toJDOM();
			aAssessmentItemRefElement.addContent(timeLimits);
		}

		for (int i = 0; i < fVariableMappingList.size(); i++) {
			VariableMapping aVariableMapping = ((VariableMapping) fVariableMappingList
					.getBasicElementAt(i));
			Element aVariableMappingElement = aVariableMapping.toJDOM();
			aAssessmentItemRefElement.addContent(aVariableMappingElement);
		}

		for (int i = 0; i < fWeightList.size(); i++) {
			Weight aWeight = ((Weight) fWeightList.getBasicElementAt(i));
			Element aWeightElement = aWeight.toJDOM();
			aAssessmentItemRefElement.addContent(aWeightElement);
		}

		for (int i = 0; i < fTemplateDefaultList.size(); i++) {
			TemplateDefault aTemplateDefault = ((TemplateDefault) fTemplateDefaultList.getBasicElementAt(i));
			Element aTemplateDefaultElement = aTemplateDefault.toJDOM();
			aAssessmentItemRefElement.addContent(aTemplateDefaultElement);
		}

		return aAssessmentItemRefElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.ASSESSMENT_ITEM_REF;
	}
}
