package org.tencompetence.qtieditor.elements;

public class TestResultReport {
	
	private boolean fScore = true; 
	
	private boolean fNumberCorrect = false; 
	
	private boolean fNumberIncorrect = false; 
	
	private boolean fNumberResponded = false; 
	
	private boolean fNumberPresented = false; 
	
	private boolean fNumberSelected = false; 
	
	private boolean fPercentage = false; 
	
	public TestResultReport() {
	}
	
	public boolean hasAnyResult() {
		return fScore || fNumberCorrect || fNumberIncorrect || fNumberResponded 
			|| fNumberPresented || fNumberSelected || fPercentage ;
	}

	public void setNumberSelected(boolean aNumberSelected) {
		this.fNumberSelected = aNumberSelected;
	}

	public boolean getNumberSelected() {
		return fNumberSelected;
	}	

	public void setNumberPresented(boolean aNumberPresented) {
		this.fNumberPresented = aNumberPresented;
	}

	public boolean getNumberPresented() {
		return fNumberPresented;
	}

	public void setNumberResponded(boolean aNumberResponded) {
		this.fNumberResponded = aNumberResponded;
	}

	public boolean getNumberResponded() {
		return fNumberResponded;
	}	

	public void setScore(boolean aScore) {
		this.fScore = aScore;
	}

	public boolean getScore() {
		return fScore;
	}
	
	public void setNumberIncorrect(boolean aNumberIncorrect) {
		this.fNumberIncorrect = aNumberIncorrect;
	}

	public boolean getNumberIncorrect() {
		return fNumberIncorrect;
	}	

	public void setNumberCorrect(boolean aNumberCorrect) {
		this.fNumberCorrect = aNumberCorrect;
	}

	public boolean getNumberCorrect() {
		return fNumberCorrect;
	}

	public void setPercentage(boolean aPercentage) {
		this.fPercentage = aPercentage;
	}

	public boolean getPercentage() {
		return fPercentage;
	}
	
	
}
