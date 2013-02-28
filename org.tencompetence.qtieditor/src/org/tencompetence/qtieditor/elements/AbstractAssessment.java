package org.tencompetence.qtieditor.elements;

public abstract class AbstractAssessment extends IdentifiedElement {

	// attributes
	protected String fTitle = ""; // required
	protected String fToolName = ""; // optional
	protected String fToolVersion = ""; // optional

	protected String XMLString; 
	protected BasicElementList fOutcomeDeclarationList;

	public void setTitle(String title) {
		fTitle = title;
	}

	public String getTitle() {
		return fTitle;
	}

	public void setToolName(String toolName) {
		fToolName = toolName;
	}

	public String getToolName() {
		return fToolName;
	}

	public void setToolVersion(String toolVersion) {
		fToolVersion = toolVersion;
	}

	public String getToolVersion() {
		return fToolVersion;
	}
	
	public void setXMLString(String aXMLString) {
		this.XMLString = aXMLString;
	}

	public String getXMLString() {
		return XMLString;
	}

	
	public BasicElementList getOutcomeDeclarationList() {
		return fOutcomeDeclarationList;
	}

	public void addOutcomeDeclaration(OutcomeDeclaration aOutcomeDeclaration) {
		fOutcomeDeclarationList.addElement(aOutcomeDeclaration);
	}

	public void removeOutcomeDeclaration(OutcomeDeclaration aOutcomeDeclaration) {
		fOutcomeDeclarationList.removeElement(aOutcomeDeclaration);
	}

	public OutcomeDeclaration getFirstOutcomeDeclaration() {
		return (OutcomeDeclaration)fOutcomeDeclarationList.getBasicElementAt(0);
	}
	
	public OutcomeDeclaration getOutcomeDeclarationByID(String id) {
		OutcomeDeclaration aOutcomeDeclaration = null;
		for (int i = 0; i < fOutcomeDeclarationList.size(); i++) {
			aOutcomeDeclaration = (OutcomeDeclaration)fOutcomeDeclarationList.getBasicElementAt(i);
			if (aOutcomeDeclaration.getId().equals(id))
				return aOutcomeDeclaration;
		}
		return null;
	}
	
	public abstract VariableDeclaration getVariableDeclarationById(String id);

}
