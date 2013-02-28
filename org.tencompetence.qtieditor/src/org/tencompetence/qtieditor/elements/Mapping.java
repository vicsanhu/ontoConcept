package org.tencompetence.qtieditor.elements;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;

public class Mapping extends AbstractMapping {
	
	private List<MapEntry> fMapEntryList = new ArrayList<MapEntry>();

	/** Creates a new instance of Mapping */
	public Mapping() {
	}
	
	public List getMapEntryList() {
		return fMapEntryList;
	}

	public void setMapEntryList(List list) {
		fMapEntryList = list;
	}

	public void addMapEntry(MapEntry aMapEntry) {
		if (!fMapEntryList.contains(aMapEntry))
			fMapEntryList.add(aMapEntry);
	}

	public MapEntry getMapEntryAt(int index) {
		if (index>=size()) return null;
		return (MapEntry)fMapEntryList.get(index);
	}	

	public void setMapEntryAt(int index, MapEntry aMapEntry) {
		if (index<0||index>=size()) 
			return;
		fMapEntryList.set(index, aMapEntry);
	}

	public MapEntry getMapEntryByKey(String aMapKey) {
		for (int i=0; i<size(); i++) {
			MapEntry aMapEntry = (MapEntry)fMapEntryList.get(i);
			if (aMapEntry.getMapKey().equalsIgnoreCase(aMapKey)) {
				return aMapEntry;
			}
		}
		return null;
	}
	
	public void updateOrAddMapEntry(String aMapKey, String valueString) {
		
		for (int i=0; i<size(); i++) {
			MapEntry aMapEntry = (MapEntry)fMapEntryList.get(i);
			if (aMapEntry.getMapKey().equalsIgnoreCase(aMapKey)) {
				if (valueString.equalsIgnoreCase("")) {
					fMapEntryList.remove(aMapEntry);
				} else {
					aMapEntry.setMappedValue(Double.parseDouble(valueString));
				}
				return;
			}
		}
		fMapEntryList.add(new MapEntry(aMapKey, Double.parseDouble(valueString)));
	}

	public void removeMapEntryByKey(String aMapKey) {
		for (int i=0; i<size(); i++) {
			MapEntry aMapEntry = (MapEntry)fMapEntryList.get(i);
			if (aMapEntry.getMapKey().equalsIgnoreCase(aMapKey)) {
				fMapEntryList.remove(aMapEntry);
				return;
			}
		}
	}

	public void removeMapEntry(MapEntry aMapEntry) {
		fMapEntryList.remove(aMapEntry);
	}
	
	public int size() {
		return fMapEntryList.size();
	}
	
	public void clear() {
		fMapEntryList.clear();
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
				MapEntry aMapEntry = new MapEntry();
				aMapEntry.fromJDOM((Element)child);
				fMapEntryList.add(aMapEntry);
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
			Element aMapEntryElement = ((MapEntry) fMapEntryList.get(i)).toJDOM();
			if (aMapEntryElement != null)
				aElement.addContent(aMapEntryElement);
		}
		return aElement ;
	}
	
	@Override
	protected String getTagName() {
		return AssessmentElementFactory.MAPPING;
	}
}
