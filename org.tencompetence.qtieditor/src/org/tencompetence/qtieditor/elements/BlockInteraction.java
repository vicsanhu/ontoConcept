package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Element;

public abstract class BlockInteraction extends Block {

	// Description: The group of interactions which have inherent structure of
	// their own.

	// block interaction Type
	public final static String ASSOCIATE_INTERACTION = "associateInteraction";
	public final static String CHOICE_INTERACTION = "choiceInteraction";
	public final static String DRAWING_INTERACTION = "drawingInteraction";
	public final static String EXTENDED_TEXT_INTERACTION = "extendedTextInteraction";
	public final static String GAP_MATCH_INTERACTION = "gapMatchInteraction";
	public final static String GRAPHIC_ASSOCIATE_INTERACTION = "graphicAssociateInteraction";
	public final static String GRAPHIC_GAP_MATCH_INTERACTION = "graphicGapMatchInteraction";
	public final static String GRAPHIC_ORDER_INTERACTION = "graphicOrderInteraction";
	public final static String HOTSPOT_INTERACTION = "hotspotInteraction";
	public final static String HOT_TEXT_INTERACTION = "hottextInteraction";
	public final static String MATCH_INTERACTION = "matchInteraction";
	public final static String MEDIA_INTERACTION = "mediaInteraction";
	public final static String ORDER_INTERACTION = "orderInteraction";
	public final static String SELECT_POINT_INTERACTION = "selectPointInteraction";
	public final static String SLIDER_INTERACTION = "sliderInteraction";
	public final static String UPLOAD_INTERACTION = "uploadInteraction";

	protected ResponseDeclaration fAssociatedResponseDeclaration; // required
	protected Prompt fPrompt = new Prompt();


	public void setResponseDeclaration(ResponseDeclaration aResponseDeclaration) {
		this.fAssociatedResponseDeclaration = aResponseDeclaration;
	}

	public ResponseDeclaration getResponseDeclaration() {
		return fAssociatedResponseDeclaration;
	}

	public String getResponseIdentifier() {
		return fAssociatedResponseDeclaration.getId();
	}

	public void setPrompt(Prompt prompt) {
		this.fPrompt = prompt;
	}
	
	public Prompt getPrompt() {
		return fPrompt;
	}
	
	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.RESPONSE_IDENTIFIER.equals(tag)) {
				fAssociatedResponseDeclaration = fAssessmentItem.getResponseDeclarationByID(value);
			} 
		}

		for (Object object : element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();
			String value = child.getText();

			if (tag.equals(AssessmentElementFactory.PROMPT)) {
				fPrompt.setData(value);
			} 
		}
	}

	public Element toJDOM() {
		Element aBlockInteraction = new Element(getTagName(), getNamespace());

		aBlockInteraction.setAttribute(AssessmentElementFactory.RESPONSE_IDENTIFIER, fAssociatedResponseDeclaration.getId());
		
		Element aPromptElement  = new Element(AssessmentElementFactory.PROMPT, getNamespace());
		if (!fPrompt.getData().equals("")) {
			aPromptElement.setText(fPrompt.getData());
			aBlockInteraction.addContent(aPromptElement);
		}

		return aBlockInteraction;
	}
	

}
