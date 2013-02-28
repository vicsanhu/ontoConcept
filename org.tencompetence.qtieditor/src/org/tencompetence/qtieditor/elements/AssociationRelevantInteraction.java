package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public abstract class AssociationRelevantInteraction extends BlockInteraction {
	
	// attributes
	protected boolean fShuffle = false; // required
	protected int fMaxAssociations = 1; // required


	public void setShuffle(boolean shuffle) {
		fShuffle = shuffle;
	}

	public boolean getShuffle() {
		return fShuffle;
	}

	public void setMaxAssocations(int maxAssocations) {
		this.fMaxAssociations = maxAssocations;
	}

	public int getMaxAssocations() {
		return fMaxAssociations;
	}

	public void fromJDOM(Element element) {
		super.fromJDOM(element);
		
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.MAX_ASSOCIATIONS.equals(tag)) {
				fMaxAssociations = Integer.parseInt(value);
			} else if (AssessmentElementFactory.SHUFFLE.equals(tag)) {
				fShuffle = "true".equalsIgnoreCase(value);
			}
		}
	}
	
	public Element toJDOM() {
		Element aInteractionElement = super.toJDOM();

		aInteractionElement.setAttribute(AssessmentElementFactory.SHUFFLE, fShuffle ? "true" : "false");
		aInteractionElement.setAttribute(AssessmentElementFactory.MAX_ASSOCIATIONS, String.valueOf(fMaxAssociations));
		
		return aInteractionElement;
	}
}
