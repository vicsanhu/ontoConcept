package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class Stylesheet extends BasicElement{

	private String fHref = null; // required
	private String fMimeType = null; // required
	private String fMedia = null; // optional
	private String fTitle = null; // optional

	/** Creates a new instance of Stylesheet */
	public Stylesheet() {
	}

	public void setHref(String href) {
		this.fHref = href;
	}

	public String getHrefe() {
		return fHref;
	}

	public void setMIMEType(String type) {
		this.fMimeType = type;
	}

	public String getMIMEType() {
		return fMimeType;
	}

	public void setMedia(String media) {
		this.fMedia = media;
	}

	public String getMedia() {
		return fMedia;
	}

	public void setTitle(String title) {
		this.fTitle = title;
	}

	public String getTitle() {
		return fTitle;
	}
	
	public void fromJDOM(Element element) {

		fMedia = element.getText();

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.HREF.equals(tag)) {
				fHref = value;
			} else if (AssessmentElementFactory.TYPE.equals(tag)) {
				fMimeType = value;
			} else if (AssessmentElementFactory.MEDIA.equals(tag)) {
				fMedia = value;
			} else if (AssessmentElementFactory.TITLE.equals(tag)) {
				fTitle = value;
			}
 		}
	}

	public Element toJDOM() {

		Element aRubricBlockElement = new Element(getTagName(), getNamespace());
		aRubricBlockElement.setText(getMedia());

		aRubricBlockElement.setAttribute(AssessmentElementFactory.HREF, fHref);
		aRubricBlockElement.setAttribute(AssessmentElementFactory.TYPE, fMimeType);
		aRubricBlockElement.setAttribute(AssessmentElementFactory.MEDIA, fMedia);
		aRubricBlockElement.setAttribute(AssessmentElementFactory.TITLE, fTitle);
		
		return aRubricBlockElement;
	}
	
	public String getTagName() {
		return AssessmentElementFactory.STYLE_SHEET;
	}	

}
