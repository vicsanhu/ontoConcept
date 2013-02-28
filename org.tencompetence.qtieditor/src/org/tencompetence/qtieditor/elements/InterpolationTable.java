package org.tencompetence.qtieditor.elements;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;

public class InterpolationTable extends LookupTable{
	
	private List<InterpolationTableEntry> fInterpolationTableEntryList = new ArrayList<InterpolationTableEntry>();

	/** Creates a new instance of InterpolationTable */
	public InterpolationTable() {
	}
	
	public List getInterpolationTableEntryList() {
		return fInterpolationTableEntryList;
	}

	public void setInterpolationTableEntryList(List list) {
		fInterpolationTableEntryList = list;
	}

	public void addInterpolationTableEntry(InterpolationTableEntry aInterpolationTableEntry) {
		if (!fInterpolationTableEntryList.contains(aInterpolationTableEntry))
			fInterpolationTableEntryList.add(aInterpolationTableEntry);
	}

	public void removeInterpolationTableEntry(InterpolationTableEntry aInterpolationTableEntry) {
		fInterpolationTableEntryList.remove(aInterpolationTableEntry);
	}
	

	public InterpolationTableEntry getTableEntryByTargetValue(String id) {
		InterpolationTableEntry aInterpolationTableEntry = null;
		for (int i = 0; i < fInterpolationTableEntryList.size(); i++) {
			aInterpolationTableEntry = (InterpolationTableEntry) fInterpolationTableEntryList.get(i);
			if (aInterpolationTableEntry.getTargetValue().equals(id))
				return aInterpolationTableEntry;
		}
		return null;
	}
	
	public int size() {
		return fInterpolationTableEntryList.size();
	}

	public void fromJDOM(Element element) {

		for (Object object: element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();
			String value = child.getText();

			if (tag.equals(AssessmentElementFactory.INTERPOLATION_TABLE_ENTRY)) {
				InterpolationTableEntry aInterpolationTableEntry = new InterpolationTableEntry();
				aInterpolationTableEntry.fromJDOM((Element)child);
				fInterpolationTableEntryList.add(aInterpolationTableEntry);
			} 
		}
	}
	
	public Element toJDOM() {
		
		int size = size();
		if (size==0) return null;
		
		Element aElement = new Element(getTagName(), getTestNamespace());
		
		for (int i = 0; i < size(); i++) {
			Element aInterpolationTableEntryElement = ((InterpolationTableEntry) fInterpolationTableEntryList.get(i)).toJDOM();
			if (aInterpolationTableEntryElement != null)
				aElement.addContent(aInterpolationTableEntryElement);
		}
		return aElement ;
	}
	
	@Override
	protected String getTagName() {
		return AssessmentElementFactory.INTERPOLATION_TABLE;
	}
}