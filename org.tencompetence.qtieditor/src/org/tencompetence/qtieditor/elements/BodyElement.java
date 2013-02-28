package org.tencompetence.qtieditor.elements;

import java.util.ArrayList;
import java.util.List;

public abstract class BodyElement extends IdentifiedElement {

	// attributes
	private List<String> fStyleclassList = new ArrayList<String>(); // optional
	private String fLang = ""; // optional
	private String fLabel = ""; // optional

	public void setLabel(String label) {
		this.fLabel = label;
	}

	public String getLabel() {
		return fLabel;
	}

	public void addStyleclass(String aStyleclass) {
		if (!fStyleclassList.contains(aStyleclass))
			fStyleclassList.add(aStyleclass);
	}

	public void removeStyleclass(String aStyleclass) {
		fStyleclassList.remove(aStyleclass);
	}

	public void setLang(String lang) {
		this.fLang = lang;
	}

	public String getLang() {
		return fLang;
	}
	
}
