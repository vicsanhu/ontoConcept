package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class Ordering extends BasicElement{

	private boolean fShuffle = true; // required, default


	public void setShuffle(boolean aShuffle) {
		fShuffle = aShuffle;
	}

	public boolean getShuffle() {
		return fShuffle;
	}

	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.SHUFFLE.equals(tag)) {
				fShuffle = "true".equalsIgnoreCase(value);
			}
		}
	}

	public Element toJDOM() {

		Element aOrderingElement = new Element(getTagName(), getTestNamespace());

		aOrderingElement.setAttribute(
				AssessmentElementFactory.SHUFFLE,
				fShuffle ? "true" : "false");
		return aOrderingElement;
	}

	public String getTagName() {
		return AssessmentElementFactory.ORDERING;
	}
}
