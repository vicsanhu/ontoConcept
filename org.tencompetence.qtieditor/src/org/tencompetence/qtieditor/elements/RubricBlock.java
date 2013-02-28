package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class RubricBlock extends BodyElement {

	private String fView = ""; //required: default; 
	private String fData = ""; // optional

	public RubricBlock() {
		super.createId("RB");
	}

	public void setView(String aView) {
		this.fView = aView;
	}

	public String getView() {
		return fView;
	}

	public void setData(String data) {
		this.fData = data;
	}

	public String getData() {
		return fData;
	}
	
	public void fromJDOM(Element element) {

		fData = element.getText();

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.VIEW.equals(tag)) {
				fView = value;
			}
 		}
	}

	public Element toJDOM() {

		Element aRubricBlockElement = new Element(getTagName(), getNamespace());
		aRubricBlockElement.setText(getData());

		aRubricBlockElement.setAttribute(AssessmentElementFactory.VIEW, fView);
		return aRubricBlockElement;
	}
	
	public String getTagName() {
		return AssessmentElementFactory.RUBRIC_BLOCK;
	}	
	
}
