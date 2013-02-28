package org.tencompetence.qtieditor.expression;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;

public abstract class ItemSubset extends Expression {

	protected String fSectionIdentifier = ""; //option
	//protected List<String> fIncludeCategoryList = new ArrayList<String>(); //option
	//protected List<String> fExcludeCategoryList = new ArrayList<String>(); //option
	
	public void setSectionIdentifier(String aSectionIdentifier) {
		fSectionIdentifier = aSectionIdentifier;
	}

	public String getSectionIdentifier() {
		return fSectionIdentifier;
	}	
	
	public void fromJDOM(Element element) {

		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.SECTION_IDENTIFIER.equals(tag)) {
				fSectionIdentifier = value;
			}
		}
	}

	public Element toJDOM() {
		Element aElement = new Element(getTagName(), (fAssessment instanceof AssessmentItem)? getNamespace(): getTestNamespace());

		if (fSectionIdentifier!=null && (!fSectionIdentifier.equalsIgnoreCase("")))
			aElement.setAttribute(AssessmentElementFactory.SECTION_IDENTIFIER, fSectionIdentifier);

		return aElement;
	}	
}
