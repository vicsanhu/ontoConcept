package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class ItemSessionControl extends BasicElement {
	
	protected int fMaxAttempts = 0; // option
	private boolean fShowFeedback = false; // option
	private boolean fAllowReview = false; // option

	public void setMaxAttempts(int i) {
		this.fMaxAttempts = i;
	}

	public int getMaxAttempts() {
		return fMaxAttempts;
	}

	public void setShowFeedback(boolean ShowFeedback) {
		this.fShowFeedback = ShowFeedback;
	}

	public boolean getShowFeedback() {
		return fShowFeedback;
	}

	public void setAllowReview(boolean AllowReview) {
		this.fAllowReview = AllowReview;
	}

	public boolean getAllowReview() {
		return fAllowReview;
	}


	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.SHOW_FEEDBACK.equals(tag)) {
				fShowFeedback = "true".equalsIgnoreCase(value);
			} 
			else if (AssessmentElementFactory.ALLOW_REVIEW.equals(tag)) {
				fAllowReview = "true".equalsIgnoreCase(value);
			} 
		}

	}

	public Element toJDOM() {
		Element aItemSessionControl = new Element(getTagName(), getNamespace());

		if (fShowFeedback)
			aItemSessionControl.setAttribute(AssessmentElementFactory.SHOW_FEEDBACK, "true");
		else if (fAllowReview)
			aItemSessionControl.setAttribute(AssessmentElementFactory.ALLOW_REVIEW, "true");


		return aItemSessionControl;
	}

	public String getTagName() {
		return AssessmentElementFactory.ITEM_SESSION_CONTROL;
	}
}
