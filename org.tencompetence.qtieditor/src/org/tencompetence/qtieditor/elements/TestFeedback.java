package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class TestFeedback extends FeedbackElement  {
	
	private String fAccess = "atEnd"; // required
	protected InterpolationTableEntry fInterpolationTableEntry; // it is not an attribute 
							//of testFeedback, just for edit feedback
	private AssessmentTest fAssessmentTest;
	private String fType = "SUMMARY";
	private TestResultReport fTestResultReport = null;

	public TestFeedback(AssessmentTest aAssessmentTest) {
		super.createId("TF");
		fAssessmentTest = aAssessmentTest;
		fInterpolationTableEntry = new InterpolationTableEntry();
		fTitle = "Summary Feedback";
	}

	public AssessmentTest getAssessmentTest() {
		return fAssessmentTest;
	}
	
	public void setAccess(String aAccess) {
		this.fAccess = aAccess;
	}

	public String getAccess() {
		return fAccess;
	}	

	
	public void setInterpolationTableEntry(InterpolationTableEntry aInterpolationTableEntry) {
		this.fInterpolationTableEntry = aInterpolationTableEntry;
	}

	public InterpolationTableEntry getInterpolationTableEntry() {
		return fInterpolationTableEntry;
	}
	
	
	public void setForResult(String aTitle, TestResultReport aTestResultReport) {
		fType = "RESULTS";
		fID = "outcomeValue";
		fTitle = aTitle;
		fOutcomeIdentifier = "outcomeIdentifier";
		fShowHide = "hide";
		fAccess = "atEnd";
		fData = "The test is now complete. The following table shows a breakdown of your scores:";
		fTestResultReport = aTestResultReport;
	}
	
	public void fromJDOM(Element element) {
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.TITLE.equals(tag)) {
				fTitle = value;
			} else if (AssessmentElementFactory.OUTCOME_IDENTIFIER.equals(tag)) {
				fOutcomeIdentifier = value;
			} else if (AssessmentElementFactory.SHOW_HIDE.equals(tag)) {
				fShowHide = value;
			} else if (AssessmentElementFactory.ACCESS.equals(tag)) {
				fAccess = value;
			}
		}
		
		for (Object object : element.getChildren()) {
			Element child = (Element) object;
			String tag = child.getName();
			String value = child.getText();

			if (tag.equals(AssessmentElementFactory.P)) {
				fData = value;
			} 
		}
		
		OutcomeDeclaration aOutcomeDeclaration = fAssessmentTest.getOutcomeDeclarationByID(fOutcomeIdentifier);
		if (aOutcomeDeclaration!=null) { //the result report feedback has no outcomeDeclaration, so ignore it
			InterpolationTableEntry aInterpolationTableEntry = aOutcomeDeclaration.getInterpolationTable().getTableEntryByTargetValue(fID);
			this.setInterpolationTableEntry(aInterpolationTableEntry);	
		}
	}
	

	public Element toJDOM() {
		Element aFeedbackElement = new Element(getTagName(), getTestNamespace());
		
		aFeedbackElement.setAttribute(AssessmentElementFactory.ACCESS, fAccess);

		aFeedbackElement.setAttribute(AssessmentElementFactory.SHOW_HIDE, fShowHide);

		aFeedbackElement.setAttribute(AssessmentElementFactory.OUTCOME_IDENTIFIER, fOutcomeIdentifier);

		aFeedbackElement.setAttribute(AssessmentElementFactory.IDENTIFIER, fID);
		
		if (!fTitle.equalsIgnoreCase(""))
			aFeedbackElement.setAttribute(AssessmentElementFactory.TITLE, fTitle);
		
		if (!fData.equals("")) {
			//aFeedbackElement.setText(getData());
			
			//in order to present feedback in a paragraph, create a <p> block
			Element p = new Element(AssessmentElementFactory.P, getTestNamespace());
			p.setText(getData());
			aFeedbackElement.addContent(p);
		}
		
		if (fType.equalsIgnoreCase("RESULTS") && (fTestResultReport!=null)) {
			Element table = new Element("table", getTestNamespace());
			Element tbody = new Element("tbody", getTestNamespace());
			
			if (fTestResultReport.getScore()) {
				Element tr = new Element("tr", getTestNamespace());
				Element td = new Element("td", getTestNamespace());
				td.setText("The total score:");
				tr.addContent(td);
				td = new Element("td", getTestNamespace());
				PrintedVariable aPrintedVariable = new PrintedVariable("SCORE");
				td.addContent(aPrintedVariable.toJDOM());
				tr.addContent(td);
				tbody.addContent(tr);
			}
			if (fTestResultReport.getNumberCorrect()) {
				Element tr = new Element("tr", getTestNamespace());
				Element td = new Element("td", getTestNamespace());
				td.setText("Number of correct responses:");
				tr.addContent(td);
				td = new Element("td", getTestNamespace());
				PrintedVariable aPrintedVariable = new PrintedVariable("NCORRECT");
				td.addContent(aPrintedVariable.toJDOM());
				tr.addContent(td);
				tbody.addContent(tr);
			}
			if (fTestResultReport.getNumberIncorrect()) {
				Element tr = new Element("tr", getTestNamespace());
				Element td = new Element("td", getTestNamespace());
				td.setText("Number of incorrect responses:");
				tr.addContent(td);
				td = new Element("td", getTestNamespace());
				PrintedVariable aPrintedVariable = new PrintedVariable("NINCORRECT");
				td.addContent(aPrintedVariable.toJDOM());
				tr.addContent(td);
				tbody.addContent(tr);
			}
			if (fTestResultReport.getNumberPresented()) {
				Element tr = new Element("tr", getTestNamespace());
				Element td = new Element("td", getTestNamespace());
				td.setText("Number of presented questions:");
				tr.addContent(td);
				td = new Element("td", getTestNamespace());
				PrintedVariable aPrintedVariable = new PrintedVariable("NPRESENTED");
				td.addContent(aPrintedVariable.toJDOM());
				tr.addContent(td);
				tbody.addContent(tr);
			}
			if (fTestResultReport.getNumberResponded()) {
				Element tr = new Element("tr", getTestNamespace());
				Element td = new Element("td", getTestNamespace());
				td.setText("Number of responded questions:");
				tr.addContent(td);
				td = new Element("td", getTestNamespace());
				PrintedVariable aPrintedVariable = new PrintedVariable("NRESPONDED");
				td.addContent(aPrintedVariable.toJDOM());
				tr.addContent(td);
				tbody.addContent(tr);
			}
			if (fTestResultReport.getNumberSelected()) {
				Element tr = new Element("tr", getTestNamespace());
				Element td = new Element("td", getTestNamespace());
				td.setText("Number of selected questions:");
				tr.addContent(td);
				td = new Element("td", getTestNamespace());
				PrintedVariable aPrintedVariable = new PrintedVariable("NSELECTED");
				td.addContent(aPrintedVariable.toJDOM());
				tr.addContent(td);
				tbody.addContent(tr);
			}
			if (fTestResultReport.getPercentage()) {
				Element tr = new Element("tr", getTestNamespace());
				Element td = new Element("td", getTestNamespace());
				Element b = new Element("b", getTestNamespace());
				b.setText("Overall percentage of correct answers:");
				td.addContent(b);
				tr.addContent(td);
				td = new Element("td", getTestNamespace());
				PrintedVariable aPrintedVariable = new PrintedVariable("PERCENT_CORRECT");
				aPrintedVariable.setFormat("%3.1f");
				td.addContent(aPrintedVariable.toJDOM());
				td.addContent("%");
				tr.addContent(td);
				tbody.addContent(tr);
			}
			table.addContent(tbody);
			aFeedbackElement.addContent(table);
		}
		
		return aFeedbackElement;
	}
	
	public String getTagName() {
		return AssessmentElementFactory.TEST_FEEDBACK;
	}

}
