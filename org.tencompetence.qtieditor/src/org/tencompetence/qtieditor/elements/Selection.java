package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class Selection extends BasicElement {

	private int fSelect = 0; // required
	private boolean fWithReplacement = false; // optional, default

	public void setSelect(int aSelect) {
		fSelect = aSelect;
	}

	public int getSelect() {
		return fSelect;
	}

	public void setWithReplacement(boolean aWithReplacement) {
		fWithReplacement = aWithReplacement;
	}

	public boolean getWithReplacement() {
		return fWithReplacement;
	}

	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.SELECT.equals(tag)) {
				fSelect = Integer.parseInt(value);
			} else if (AssessmentElementFactory.WITH_REPLACEMENT.equals(tag)) {
				fWithReplacement = "true".equalsIgnoreCase(value);
			}
		}
	}

	public Element toJDOM() {

		Element aSelectionElement = new Element(getTagName(), getTestNamespace());

		aSelectionElement.setAttribute(AssessmentElementFactory.SELECT, String
				.valueOf(fSelect));
		aSelectionElement.setAttribute(
				AssessmentElementFactory.WITH_REPLACEMENT,
				fWithReplacement ? "true" : "false");
		return aSelectionElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.SELECTION;
	}
}
