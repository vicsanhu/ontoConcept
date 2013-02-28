package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public abstract class FeedbackElement extends IdentifiedElement {

	protected String fData = ""; // required
	
	protected String fOutcomeIdentifier = ""; // required
	protected String fShowHide = "show"; // required
	protected String fTitle = ""; // option

	public void setData(String data) {
		fData = data;
	}

	public String getData() {
		return fData;
	}

	public void setOutcomeIdentifier(String id) {
		fOutcomeIdentifier = id;
	}

	public String getOutcomeIdentifier() {
		return fOutcomeIdentifier;
	}

	public void setShowHide(String showHide) {
		fShowHide = showHide;
	}

	public String getShowHide() {
		return fShowHide;
	}

	public void setTitle(String title) {
		fTitle = title;
	}

	public String getTitle() {
		return fTitle;
	}

}
