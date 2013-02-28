package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Element;

public abstract class InlineInteraction extends BodyElement {

	// inline interaction Type
	//public final static String INLINE_CHOICE_INTERACTION = "associateInteraction";
	//public final static String TEXT_ENTRY_INTERACTION = "extendedTextInteraction";

	protected int fStart = 0; 
	protected int fLength = 0;

	protected ResponseDeclaration fAssociatedResponseDeclaration; // required

	protected AssessmentItem fAssessmentItem;

	protected InlineInteraction(AssessmentItem anAssessmentItem) {
		fAssessmentItem = anAssessmentItem;
	}

	public AssessmentItem getAssessmentItem() {
		return fAssessmentItem;
	}

	public void setStart(int i) {
		this.fStart = i;
	}

	public int getStart() {
		return fStart;
	}
	
	public void setLength(int i) {
		this.fLength = i;
	}

	public int getLength() {
		return fLength;
	}

	public void setResponseDeclaration(ResponseDeclaration aResponseDeclaration) {
		this.fAssociatedResponseDeclaration = aResponseDeclaration;
	}

	public ResponseDeclaration getResponseDeclaration() {
		return fAssociatedResponseDeclaration;
	}

	public String getResponseIdentifier() {
		return fAssociatedResponseDeclaration.getId();
	}
	
	public void fromJDOM(Element element) {
		
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.RESPONSE_IDENTIFIER.equals(tag)) {
				fAssociatedResponseDeclaration = fAssessmentItem.getResponseDeclarationByID(value);
			} 
		}
	}

	public Element toJDOM() {
		Element aInlineInteraction = new Element(getTagName(), getNamespace());
		aInlineInteraction.setAttribute(AssessmentElementFactory.RESPONSE_IDENTIFIER, fAssociatedResponseDeclaration.getId());
		return aInlineInteraction;
	}
}

