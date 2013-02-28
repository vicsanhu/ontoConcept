package org.tencompetence.qtieditor.elements;

import java.util.ArrayList;
import java.util.List;

import org.jdom.*;
import org.tencompetence.qtieditor.rule.SetOutcomeValue;
import org.tencompetence.qtieditor.rule.LookupOutcomeValue;
import org.tencompetence.qtieditor.serialization.AssessmentTestSerializer;

import org.tencompetence.qtieditor.expression.TestVariables;
import org.tencompetence.qtieditor.expression.NumberCorrect;
import org.tencompetence.qtieditor.expression.NumberIncorrect;
import org.tencompetence.qtieditor.expression.NumberResponded;
import org.tencompetence.qtieditor.expression.NumberPresented;
import org.tencompetence.qtieditor.expression.NumberSelected;
import org.tencompetence.qtieditor.expression.Sum;
import org.tencompetence.qtieditor.expression.Product;
import org.tencompetence.qtieditor.expression.Divide;
import org.tencompetence.qtieditor.expression.BaseValue;
import org.tencompetence.qtieditor.expression.IntegerToFloat;


public class AssessmentTest extends AbstractAssessment {

	// elements
	private TimeLimits fTimeLimits;
	private BasicElementList fTestPartList;
	private OutcomeProcessing fOutcomeProcessing;
	private BasicElementList fTestFeedbackList = new BasicElementList();
	//private BasicElementList fScoreReportList;
	private TestResultReport fTestResultReport = new TestResultReport();

	public AssessmentTest() {
		super.createId("AT");
		fOutcomeDeclarationList = new BasicElementList();
		//fTimeLimits = new TimeLimits();
		fTestPartList = new BasicElementList();
		fOutcomeProcessing = new OutcomeProcessing(this);
	}

	public AssessmentTest(String id) {
		fID = id;
		fOutcomeDeclarationList = new BasicElementList();
		//fTimeLimits = new TimeLimits();
		fTestPartList = new BasicElementList();
		fOutcomeProcessing = new OutcomeProcessing(this);		
	}

/*	
	public void createOutcomeForTest() {
		fOutcomeDeclarationList.addElement(new OutcomeDeclaration("SCORE", 
				AssessmentElementFactory.FLOAT, 
				AssessmentElementFactory.SINGLE,
				this));
	}	
*/	
	public void setTimeLimits(TimeLimits timeLimits) {
		fTimeLimits = timeLimits;
	}

	public TimeLimits getTimeLimits() {
		return fTimeLimits;
	}	

	public BasicElementList getTestPartList() {
		return fTestPartList;
	}

	public void addTestPart(TestPart aTestPart) {
		fTestPartList.addElement(aTestPart);
	}

	public void removeTestPart(TestPart aTestPart) {
		fTestPartList.removeElement(aTestPart);
	}

	public TestPart getFirstTestPart() {
		return (TestPart) fTestPartList.getBasicElementAt(0);
	}

	public TestPart getTestPartByID(String id) {
		TestPart aTestPart = null;
		for (int i = 0; i < fTestPartList.size(); i++) {
			aTestPart = (TestPart) fTestPartList.getBasicElementAt(i);
			if (aTestPart.getId().equals(id))
				return aTestPart;
		}
		return null;
	}
	

	public VariableDeclaration getVariableDeclarationById(String id) {
		return getOutcomeDeclarationByID(id);
	}

	public OutcomeProcessing getOutcomeProcessing() {
		return fOutcomeProcessing;
	}

	public void setOutcomeProcessing(OutcomeProcessing aOutcomeProcessing) {
		fOutcomeProcessing = aOutcomeProcessing;
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

/*
	public BasicElementList getScoreReportList() {
		return fScoreReportList;
	}

	public void setScoreReportList(BasicElementList list) {
		fScoreReportList = list;
	}

	 public void addScoreReport(ScoreReport aScoreReport) {
		 fScoreReportList.addElement(aScoreReport);
	 }
	  
	 public void removeScoreReport(ScoreReport aScoreReport) {
		 fScoreReportList.removeElement(aScoreReport); 
	 }
*/	 
	 
	public TestResultReport getTestResultReport() {
			return fTestResultReport;
	}	

	public OutcomeDeclaration createOutcomeForSummary() {
			return new OutcomeDeclaration("SUMMARY", 
					AssessmentElementFactory.IDENTIFIER, 
					AssessmentElementFactory.SINGLE,
					this);
	}

	public OutcomeDeclaration createOutcomeResult(String aType) {
			return new OutcomeDeclaration(aType, 
					AssessmentElementFactory.FLOAT, 
					AssessmentElementFactory.SINGLE,
					this);
	}

	public void handleOutcomeProcessing(){
		fOutcomeDeclarationList.clear();
		fOutcomeProcessing.getOutcomeRuleList().clear();
		
    	TestVariables aVariable = new TestVariables(this);
		aVariable.setVariableIdentifier("SCORE");
		Sum score = new Sum(this);
		score.addOperand(aVariable);

		//total score
		if (fTestResultReport.getScore()) {
			SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
			OutcomeDeclaration aOutcomeDeclaration = createOutcomeResult("SCORE");
	    	aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
	    	aSetOutcomeValue.setExpression(score);
	    	fOutcomeDeclarationList.addElement(aOutcomeDeclaration);
			fOutcomeProcessing.getOutcomeRuleList().addElement(aSetOutcomeValue);
		}
				
		//number correct
		if (fTestResultReport.getNumberCorrect()) {
			SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
			OutcomeDeclaration aOutcomeDeclaration = createOutcomeResult("NCORRECT");
	    	aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
	    	IntegerToFloat aIntegerToFloat = new IntegerToFloat(this);
	    	NumberCorrect aNumberCorrect = new NumberCorrect(this);
	    	aIntegerToFloat.setFirstOperand(aNumberCorrect);
	    	aSetOutcomeValue.setExpression(aIntegerToFloat);
	    	fOutcomeDeclarationList.addElement(aOutcomeDeclaration);
			fOutcomeProcessing.getOutcomeRuleList().addElement(aSetOutcomeValue);
		}
		
		//number incorrect
		if (fTestResultReport.getNumberIncorrect()) {
			SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
			OutcomeDeclaration aOutcomeDeclaration = createOutcomeResult("NINCORRECT");
	    	aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
	    	IntegerToFloat aIntegerToFloat = new IntegerToFloat(this);
	    	aIntegerToFloat.setFirstOperand(new NumberIncorrect(this));
	    	aSetOutcomeValue.setExpression(aIntegerToFloat);
	    	fOutcomeDeclarationList.addElement(aOutcomeDeclaration);
			fOutcomeProcessing.getOutcomeRuleList().addElement(aSetOutcomeValue);
		}
		
		//number presented
		if (fTestResultReport.getNumberPresented()) {
			SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
			OutcomeDeclaration aOutcomeDeclaration = createOutcomeResult("NPRESENTED");
	    	aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
	    	IntegerToFloat aIntegerToFloat = new IntegerToFloat(this);
	    	aIntegerToFloat.setFirstOperand(new NumberPresented(this));
	    	aSetOutcomeValue.setExpression(aIntegerToFloat);
	    	fOutcomeDeclarationList.addElement(aOutcomeDeclaration);
			fOutcomeProcessing.getOutcomeRuleList().addElement(aSetOutcomeValue);
		}
		
		//number responded
		if (fTestResultReport.getNumberResponded()) {
			SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
			OutcomeDeclaration aOutcomeDeclaration = createOutcomeResult("NRESPONDED");
	    	aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
	    	IntegerToFloat aIntegerToFloat = new IntegerToFloat(this);
	    	aIntegerToFloat.setFirstOperand(new NumberResponded(this));
	    	aSetOutcomeValue.setExpression(aIntegerToFloat);
	    	fOutcomeDeclarationList.addElement(aOutcomeDeclaration);
			fOutcomeProcessing.getOutcomeRuleList().addElement(aSetOutcomeValue);
		}
		
		//number selected
		if (fTestResultReport.getNumberSelected()) {
			SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
			OutcomeDeclaration aOutcomeDeclaration = createOutcomeResult("NSELECTED");
	    	aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
	    	IntegerToFloat aIntegerToFloat = new IntegerToFloat(this);
	    	aIntegerToFloat.setFirstOperand(new NumberSelected(this));
	    	aSetOutcomeValue.setExpression(aIntegerToFloat);
	    	fOutcomeDeclarationList.addElement(aOutcomeDeclaration);
			fOutcomeProcessing.getOutcomeRuleList().addElement(aSetOutcomeValue);
		}
		
		//percentage of correct answers
		if (fTestResultReport.getPercentage()) {
			SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
			OutcomeDeclaration aOutcomeDeclaration = createOutcomeResult("PERCENT_CORRECT");
	    	aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
	    	
	    	Divide aDivide = new Divide(this);
	    	aDivide.addOperand(new NumberCorrect(this));
			aDivide.addOperand(new NumberPresented(this));
			BaseValue aBaseValue = new BaseValue(this);
			aBaseValue.setBaseType(AssessmentElementFactory.FLOAT);
			aBaseValue.setData("100.0");

			Product aProduct = new Product(this);
			aProduct.addOperand(aBaseValue);
			aProduct.addOperand(aDivide);
	    	
	    	aSetOutcomeValue.setExpression(aProduct);
	    	fOutcomeDeclarationList.addElement(aOutcomeDeclaration);
			fOutcomeProcessing.getOutcomeRuleList().addElement(aSetOutcomeValue);
			
			
		}
		
		//Lookup Outcome
		if (fTestFeedbackList.size()>0) {
			OutcomeDeclaration anOutcomeDeclaration = createOutcomeForSummary();
			
			// making the interpolation table entries in a sequence from large to small
			for (int i = fTestFeedbackList.size()-1; i>0; i-- ) {
				for (int j = 0; j<i; j++ ) {
					TestFeedback preceed = (TestFeedback)fTestFeedbackList.getBasicElementAt(j);
					TestFeedback succeed = (TestFeedback)fTestFeedbackList.getBasicElementAt(j+1);
					if (preceed.getInterpolationTableEntry().getSourceValue()<
							succeed.getInterpolationTableEntry().getSourceValue()) {
						fTestFeedbackList.setElementAt(j, succeed);
						fTestFeedbackList.setElementAt(j+1, preceed);
					} else if (preceed.getInterpolationTableEntry().getSourceValue()==
							succeed.getInterpolationTableEntry().getSourceValue()) {
						if (!preceed.getInterpolationTableEntry().getIncludeBoundary() &&
								succeed.getInterpolationTableEntry().getIncludeBoundary()) {
							fTestFeedbackList.setElementAt(j, succeed);
							fTestFeedbackList.setElementAt(j+1, preceed);
						}							
					}						
				}
			}

			for (int i=0; i<fTestFeedbackList.size(); i++) {
				TestFeedback anTestFeedback = (TestFeedback)fTestFeedbackList.getBasicElementAt(i);
				anTestFeedback.setOutcomeIdentifier(anOutcomeDeclaration.getId());
				anOutcomeDeclaration.getInterpolationTable()
					.addInterpolationTableEntry(anTestFeedback.getInterpolationTableEntry());
			}
			LookupOutcomeValue aLookupOutcomeValue = new LookupOutcomeValue(this);
	    	aLookupOutcomeValue.setOutcomeDeclaration(anOutcomeDeclaration);	    
	    	aLookupOutcomeValue.setExpression(score);
	    	fOutcomeDeclarationList.addElement(anOutcomeDeclaration);
			fOutcomeProcessing.getOutcomeRuleList().addElement(aLookupOutcomeValue);
		}
				
	}
	
	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.TITLE.equals(tag)) {
				fTitle = value;
			/* 
			} else if(AssessmentElementFactory.TOOL_NAME.equals(tag)) {
				fToolName = value; 
			} else if(AssessmentElementFactory.TOOL_VERSION.equals(tag)) {
				fToolVersion = value; 
			*/
			}
		}

		for (Object object : element.getChildren()) {
			Element child = (Element) object;
			String tag = child.getName();
			// String value = child.getText();

			if (tag.equals(AssessmentElementFactory.OUTCOME_DECLARATION)) {
				OutcomeDeclaration aOutcomeDeclaration = new OutcomeDeclaration(this);
				aOutcomeDeclaration.fromJDOM((Element) child);
				fOutcomeDeclarationList.addElement(aOutcomeDeclaration);
			} else if (tag.equals(AssessmentElementFactory.TIME_LIMITS)) {
				fTimeLimits.fromJDOM((Element) child);
			} else if (tag.equals(AssessmentElementFactory.TEST_PART)) {
				TestPart aTestPart = new TestPart(this);
				aTestPart.fromJDOM((Element) child);
				fTestPartList.addElement(aTestPart);
			} else if (tag.equals(AssessmentElementFactory.OUTCOME_PROCESSING)) {
				fOutcomeProcessing.fromJDOM((Element) child);
			 } else if (tag.equals(AssessmentElementFactory.TEST_FEEDBACK)) {
				TestFeedback aTestFeedback = new TestFeedback(this);
				aTestFeedback.fromJDOM((Element) child);
				
				//exclude the one that shows the table
				if (!aTestFeedback.getId().equalsIgnoreCase("outcomeValue"))
					fTestFeedbackList.addElement(aTestFeedback);
			/*
			} else if (tag.equals(AssessmentElementFactory.SCORE_REPORT)) {
				ScoreReport aScoreReport = new ScoreReport();
				aScoreReport.fromJDOM((Element) child);
				fScoreReportList.addElement(aScoreReport);	
			*/			
			}
		}
	}

	public Element toJDOM() {

		Element aTestElement = new Element(getTagName(), getTestNamespace());

		AssessmentTestSerializer.handleNamespace(aTestElement);
		
		handleOutcomeProcessing();

		aTestElement.setAttribute(AssessmentElementFactory.IDENTIFIER, getId());
		aTestElement.setAttribute(AssessmentElementFactory.TITLE, fTitle);
		// test.setAttribute(AssessmentElementFactory.TOOL_NAME, fToolName);
		// test.setAttribute(AssessmentElementFactory.TOOL_VERSION, fToolVersion);

		for (int i = 0; i < fOutcomeDeclarationList.size(); i++) {
			Element outcomeDeclaration = ((OutcomeDeclaration) fOutcomeDeclarationList
					.getBasicElementAt(i)).toJDOM();
			if (outcomeDeclaration != null)
				aTestElement.addContent(outcomeDeclaration);
		}
		
		if (fTimeLimits != null) {
			Element timeLimits = fTimeLimits.toJDOM();
			aTestElement.addContent(timeLimits);
		}

		for (int i = 0; i < fTestPartList.size(); i++) {
			Element testPart = ((TestPart) fTestPartList.getBasicElementAt(i))
					.toJDOM();
			if (testPart != null)
				aTestElement.addContent(testPart);
		}

		Element outcomeProcessing = fOutcomeProcessing.toJDOM();
		if (outcomeProcessing != null) {
			aTestElement.addContent(outcomeProcessing);
		}
		
		for (int i = 0; i < fTestFeedbackList.size(); i++) {
			Element testFeedback = ((TestFeedback) fTestFeedbackList
					.getBasicElementAt(i)).toJDOM();
			if (testFeedback != null)
				aTestElement.addContent(testFeedback);
		}
		
		// create a special testFeedback, which will never be added in the list
		if (fTestResultReport.hasAnyResult()) {
			TestFeedback aTestFeedback = new TestFeedback(this);
			aTestFeedback.setForResult("Detailed Breakdown", fTestResultReport);
			Element testFeedbackElement = aTestFeedback.toJDOM();
			if (testFeedbackElement != null)
				aTestElement.addContent(testFeedbackElement);
		}
		
/* V2P1 the first draft

		for (int i = 0; i < fScoreReportList.size(); i++) {
			Element ScoreReport = ((ScoreReport) fScoreReportList
					.getBasicElementAt(i)).toJDOM();
			if (ScoreReport != null)
				aTestElement.addContent(ScoreReport);
		}
*/
		return aTestElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.ASSESSMENT_TEST;
	}

}