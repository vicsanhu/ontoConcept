package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class TimeLimits extends BasicElement {

	private double fMinTime; // optional
	private double fMaxTime; // optional

	public TimeLimits() {
	}
	
	public TimeLimits(double minTime, double maxTime) {
		this.fMinTime = minTime;
		this.fMaxTime = maxTime;
	}

	public void setMinTime(double minTime) {
		this.fMinTime = minTime;
	}

	public double getMinTime() {
		return fMinTime;
	}

	public void setMaxTime(double maxTime) {
		this.fMaxTime = maxTime;
	}

	public double getMaxTime() {
		return fMaxTime;
	}

	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.MIN_TIME.equals(tag)) {
				fMinTime = Double.parseDouble(value);
			}
			if (AssessmentElementFactory.MAX_TIME.equals(tag)) {
				fMaxTime = Double.parseDouble(value);
			}
		}
	}

	public Element toJDOM() {
		Element aTimeLimits = new Element(getTagName(), getNamespace());

		if (fMinTime > 0)
			aTimeLimits.setAttribute(
					AssessmentElementFactory.MIN_TIME, String
							.valueOf(fMinTime));
		if (fMaxTime > 0)
			aTimeLimits.setAttribute(
					AssessmentElementFactory.MAX_TIME, String
							.valueOf(fMaxTime));

		return aTimeLimits;
	}

	public String getTagName() {
		return AssessmentElementFactory.TIME_LIMITS;
	}

}
