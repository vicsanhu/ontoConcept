package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class ScoreReport extends FeedbackElement  {
	
	private String fAccess = null; // required

	public ScoreReport() {
		super.createId("SR");
	}

	public void setAccess(String aAccess) {
		this.fAccess = aAccess;
	}

	public String getAccess() {
		return fAccess;
	}	
/*
	public void fromJDOM(Element element) {
		super.fromJDOM(element);

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.ACCESS.equals(tag)) {
				fAccess = value;
			}
		}
	}
	
	public Element toJDOM() {
		Element aTestFeedback = super.toJDOM();
		aTestFeedback.setAttribute(AssessmentElementFactory.ACCESS, getAccess());

		return aTestFeedback;
	}
*/	
	public String getTagName() {
		return AssessmentElementFactory.ACCESS;
	}

}
