package org.tencompetence.qtieditor.elements;

import java.util.ArrayList;

import org.jdom.Attribute;
import org.jdom.Element;

public class OutcomeDeclaration extends VariableDeclaration {

	// attributes
	// private ArrayList viewList = new ArrayList(); //optional
	private String interpretation = null; // optional
	private String longInterpretation = null; // optional

	private double normalMaximun; // optional
	private double normalMinimun; // optional
	private double masteryValue; // optional

	// elements
	private InterpretationValue fDefaultValue = new InterpretationValue("defaultValue");

	private InterpolationTable fInterpolationTable = new InterpolationTable();

	private AbstractAssessment fAssessment;

	// these properties are coupled with this outcome
	//private ArrayList outcomePropertyPairList = new ArrayList(); 

	/** Creates a new instance of OutcomeDeclaration AssessmentItem anAssessmentItem*/
	public OutcomeDeclaration(AbstractAssessment anAssessment) {
		super.createId("OD");
		fAssessment = anAssessment;
	}

	public OutcomeDeclaration(String id, String aBaseType, String aCardinality, AbstractAssessment anAssessment) {
		setId(id);
		fBaseType = aBaseType;
		fCardinality = aCardinality;
		fAssessment = anAssessment;
		fAssessment.getOutcomeDeclarationList().addElement(this);
	}

	public OutcomeDeclaration(String aBaseType, String aCardinality, AbstractAssessment anAssessment) {
		super.createId("OD");
		fBaseType = aBaseType;
		fCardinality = aCardinality;
		fAssessment = anAssessment;
		fAssessment.getOutcomeDeclarationList().addElement(this);
	}
	
	public AbstractAssessment getAssessmentItem() {
		return fAssessment;
	}
	
	public InterpolationTable getInterpolationTable() {
		return fInterpolationTable;
	}

	public void fromJDOM(Element element) {
		super.fromJDOM(element);
				
		for (Object o : element.getChildren()) {
			Element child = (Element) o;
			String tag = child.getName();
			//String value = child.getText();

			if (tag.equals(AssessmentElementFactory.DEFAULT_VALUE)) {
				fDefaultValue.fromJDOM((Element)child);
			} 
			if (tag.equals(AssessmentElementFactory.INTERPOLATION_TABLE)) {
				fInterpolationTable.fromJDOM((Element)child);
			} 
		}
		
		if (fAssessment instanceof AssessmentTest) {
			AssessmentTest aAssessmentTest = (AssessmentTest)fAssessment;
			 TestResultReport aTestResultReport = aAssessmentTest.getTestResultReport();
			if (getId().equalsIgnoreCase("SCORE")) {
				aTestResultReport.setScore(true);
			}
			else if (getId().equalsIgnoreCase("NCORRECT")) {
				aTestResultReport.setNumberCorrect(true);
			}
			else if (getId().equalsIgnoreCase("NINCORRECT")) {
				aTestResultReport.setNumberIncorrect(true);
			}
			else if (getId().equalsIgnoreCase("NPRESENTED")) {
				aTestResultReport.setNumberPresented(true);
			}
			else if (getId().equalsIgnoreCase("NRESPONDED")) {
				aTestResultReport.setNumberResponded(true);
			}
			else if (getId().equalsIgnoreCase("NSELECTED")) {
				aTestResultReport.setNumberSelected(true);
			}
			else if (getId().equalsIgnoreCase("PERCENT_CORRECT")) {
				aTestResultReport.setPercentage(true);
			}
		}
		
	}
	
	public Element toJDOM() {
		
		Element aOutcomeDeclaration = new Element(getTagName(), 
				(fAssessment instanceof AssessmentItem)? getNamespace(): getTestNamespace());

		aOutcomeDeclaration.setAttribute(AssessmentElementFactory.IDENTIFIER, getId());
		aOutcomeDeclaration.setAttribute(AssessmentElementFactory.CARDINALITY, fCardinality);
		aOutcomeDeclaration.setAttribute(AssessmentElementFactory.BASE_TYPE, fBaseType);
		 
		Element aDefaultValue = fDefaultValue.toJDOM();
        if(aDefaultValue != null) {        	
        	aOutcomeDeclaration.addContent(aDefaultValue);
        }

		Element aInterpolationTable = fInterpolationTable.toJDOM();
        if(aInterpolationTable != null) {        	
        	aOutcomeDeclaration.addContent(aInterpolationTable);
        }

        return aOutcomeDeclaration;
	}

	public String getTagName() {
		return AssessmentElementFactory.OUTCOME_DECLARATION;
	}

}
