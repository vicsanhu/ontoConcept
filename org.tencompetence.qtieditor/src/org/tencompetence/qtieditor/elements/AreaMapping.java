package org.tencompetence.qtieditor.elements;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AbstractMapping;

public class AreaMapping extends AbstractMapping {

	private List<AreaMapEntry> fAreaMapEntryList = new ArrayList<AreaMapEntry>();

	/** Creates a new instance of Mapping */
	public AreaMapping() {
	}

	public List getAreaMapEntryList() {
		return fAreaMapEntryList;
	}

	public void setAreaMapEntryList(List list) {
		fAreaMapEntryList = list;
	}

	public void addAreaMapEntry(AreaMapEntry aAreaMapEntry) {
		if (!fAreaMapEntryList.contains(aAreaMapEntry))
			fAreaMapEntryList.add(aAreaMapEntry);
	}

	public void removeAreaMapEntry(AreaMapEntry aAreaMapEntry) {
		fAreaMapEntryList.remove(aAreaMapEntry);
	}

	public int size() {
		return fAreaMapEntryList.size();
	}
	
	public void fromJDOM(Element element) {


		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (tag.equals(AssessmentElementFactory.LOWER_BOUND)) {
				fLowerBound = Double.parseDouble(value);
			} else if (tag.equals(AssessmentElementFactory.UPPER_BOUND)) {
				fUpperBound = Double.parseDouble(value);
			} else if (tag.equals(AssessmentElementFactory.DEFAULT_VALUE)) {
				fDefaultValue = Double.parseDouble(value);
			}
		}

		for (Object object: element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();
			String value = child.getText();

			if (tag.equals(AssessmentElementFactory.MAP_ENTRY)) {
				AreaMapEntry aAreaMapEntry = new AreaMapEntry();
				aAreaMapEntry.fromJDOM((Element)child);
				fAreaMapEntryList.add(aAreaMapEntry);
			} 
		}
	}
	
	public Element toJDOM() {
		
		int size = size();
		if (size==0) return null;
		
		Element aElement = new Element(getTagName(), getNamespace());

		aElement.setAttribute(AssessmentElementFactory.LOWER_BOUND, String.valueOf(fLowerBound));
		aElement.setAttribute(AssessmentElementFactory.UPPER_BOUND, String.valueOf(fUpperBound));
		aElement.setAttribute(AssessmentElementFactory.DEFAULT_VALUE, String.valueOf(fDefaultValue));
		
		for (int i = 0; i < size(); i++) {
			Element aAreaMapEntryElement = ((AreaMapEntry) fAreaMapEntryList.get(i)).toJDOM();
			if (aAreaMapEntryElement != null)
				aElement.addContent(aAreaMapEntryElement);
		}

		return aElement ;
	}
	
	@Override
	protected String getTagName() {
		return AssessmentElementFactory.AREA_MAPPING;
	}}
