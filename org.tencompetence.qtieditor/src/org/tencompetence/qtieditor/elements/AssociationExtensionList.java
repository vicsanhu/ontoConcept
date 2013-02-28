package org.tencompetence.qtieditor.elements;

import java.util.ArrayList;
import java.util.List;


public class AssociationExtensionList{
	
	private List<AssociationExtension> fAssociationExtensionList = new ArrayList<AssociationExtension>();

	/** Creates a new instance of AssociationExtensionList */
	public AssociationExtensionList() {
	}
	
	public List getAssociationExtensionList() {
		return fAssociationExtensionList;
	}

	public void setAssociationExtensionList(List list) {
		fAssociationExtensionList = list;
	}

	public void addAssociationExtension(AssociationExtension aAssociationExtension) {
		if (!fAssociationExtensionList.contains(aAssociationExtension))
			fAssociationExtensionList.add(aAssociationExtension);
	}
	
	public boolean addAssociationExtension(String sourceID, String targetID) {
		for (int i=0; i<size(); i++) {
			AssociationExtension aAssociationExtension = (AssociationExtension)fAssociationExtensionList.get(i);
			if ((aAssociationExtension.getSource().equalsIgnoreCase(sourceID))&&
					(aAssociationExtension.getTarget().equalsIgnoreCase(targetID)))	{
				return false;
			}
		}
		AssociationExtension aAssociationExtension = new AssociationExtension(sourceID, targetID);
		fAssociationExtensionList.add(aAssociationExtension);
		return true;
	}
	
	public AssociationExtension getAssociationExtensionAt(int index) {
		if (index>=size()) return null;
		else if (index<0) return null;
		return (AssociationExtension)fAssociationExtensionList.get(index);
	}

	public AssociationExtension getAssociationExtensionBySourceIDTargetID(String sourceID, String targetID) {
		for (int i=0; i<size(); i++) {
			AssociationExtension aAssociationExtension = (AssociationExtension)fAssociationExtensionList.get(i);
			if ((aAssociationExtension.getSource().equalsIgnoreCase(sourceID))&&
					(aAssociationExtension.getTarget().equalsIgnoreCase(targetID)))	{
				return aAssociationExtension;
			}
		}
		return null;
	}

	public int getAssociationIndexBySourceIDTargetID(String sourceID, String targetID) {
		for (int i=0; i<size(); i++) {
			AssociationExtension aAssociationExtension = (AssociationExtension)fAssociationExtensionList.get(i);
			if ((aAssociationExtension.getSource().equalsIgnoreCase(sourceID))&&
					(aAssociationExtension.getTarget().equalsIgnoreCase(targetID)))	{
				return i;
			}
		}
		return -1;
	}

	public int getReferenceNumber(String id, boolean isSource) {
		int count = 0;
		for (int i=0; i<size(); i++) {
			AssociationExtension aAssociationExtension = (AssociationExtension)fAssociationExtensionList.get(i);
			if (isSource) {
				if (aAssociationExtension.getSource().equalsIgnoreCase(id)) 
					count++;
			} else {
				if (aAssociationExtension.getTarget().equalsIgnoreCase(id))
					count++;
			}
		}
		return count;
	}
	
	public boolean isChoiceUsedInAnyAssociationExtension(String fTitle, String choiceID) {
		if (fTitle.equalsIgnoreCase(AssessmentElementFactory.SOURCE_TITLE)) {
			for (int i=0; i<size(); i++) {
				AssociationExtension aAssociationExtension = (AssociationExtension)fAssociationExtensionList.get(i);
				if (aAssociationExtension.getSource().equalsIgnoreCase(choiceID))	{
					return true;
				}
			}
		} else if (fTitle.equalsIgnoreCase(AssessmentElementFactory.TARGET_TITLE)) {
			for (int i=0; i<size(); i++) {
				AssociationExtension aAssociationExtension = (AssociationExtension)fAssociationExtensionList.get(i);
				if (aAssociationExtension.getTarget().equalsIgnoreCase(choiceID))	{
					return true;
				}
			}
		}
		return false;
	}
	
	public void updateAssociationExtensionCorrect(String sourceID, String targetID, boolean isCorrect) {
		
		for (int i=0; i<size(); i++) {
			AssociationExtension aAssociationExtension = (AssociationExtension)fAssociationExtensionList.get(i);
			if ((aAssociationExtension.getSource().equalsIgnoreCase(sourceID))&&
					(aAssociationExtension.getTarget().equalsIgnoreCase(targetID)))
				aAssociationExtension.setCorrect(isCorrect);				
		}
	}
	
	public void updateAssociationExtensionValue(String sourceID, String targetID, String valueString) {
		
		for (int i=0; i<size(); i++) {
			AssociationExtension aAssociationExtension = (AssociationExtension)fAssociationExtensionList.get(i);
			if ((aAssociationExtension.getSource().equalsIgnoreCase(sourceID))&&
					(aAssociationExtension.getTarget().equalsIgnoreCase(targetID)))	{
				if (valueString.equalsIgnoreCase("")) {
					fAssociationExtensionList.remove(aAssociationExtension);
				} else {
					aAssociationExtension.setMappedValue(Double.parseDouble(valueString));
				}
				return;
			}
		}
	}

	public void removeAssociationExtensionBySourceIDTargetID(String sourceID, String targetID) {
		for (int i=0; i<size(); i++) {
			AssociationExtension aAssociationExtension = (AssociationExtension)fAssociationExtensionList.get(i);
			if ((aAssociationExtension.getSource().equalsIgnoreCase(sourceID))&&
					(aAssociationExtension.getTarget().equalsIgnoreCase(targetID)))	{
				fAssociationExtensionList.remove(aAssociationExtension);
				return;
			}
		}
	}

	public void removeAssociationExtension(AssociationExtension aAssociationExtension) {
		fAssociationExtensionList.remove(aAssociationExtension);
	}
	
	public boolean removeRelaventAssociationsByID(String fTitle, String sourceIDOrTargetID) {
		
		List<AssociationExtension> resultAssociationExtensionList = new ArrayList<AssociationExtension>();
		
		boolean result = false;
		
		if (fTitle.equalsIgnoreCase(AssessmentElementFactory.SOURCE_TITLE)) {
			for (int i=0; i<size(); i++) {
				AssociationExtension aAssociationExtension = (AssociationExtension)fAssociationExtensionList.get(i);
				if (!aAssociationExtension.getSource().equalsIgnoreCase(sourceIDOrTargetID))	{
					resultAssociationExtensionList.add(aAssociationExtension);
				}
			}
		} else if (fTitle.equalsIgnoreCase(AssessmentElementFactory.TARGET_TITLE)) {
			for (int i=0; i<size(); i++) {
				AssociationExtension aAssociationExtension = (AssociationExtension)fAssociationExtensionList.get(i);
				if (!aAssociationExtension.getTarget().equalsIgnoreCase(sourceIDOrTargetID))	{
					resultAssociationExtensionList.add(aAssociationExtension);
				}
			}
		} else if (fTitle.equalsIgnoreCase(AssessmentElementFactory.ASSOCIATE)) {
			for (int i=0; i<size(); i++) {
				AssociationExtension aAssociationExtension = (AssociationExtension)fAssociationExtensionList.get(i);
				if ((!aAssociationExtension.getSource().equalsIgnoreCase(sourceIDOrTargetID) && 
						(!aAssociationExtension.getTarget().equalsIgnoreCase(sourceIDOrTargetID))))	{
					resultAssociationExtensionList.add(aAssociationExtension);
				}
			}
		}
		
		if (size()!=resultAssociationExtensionList.size()) 
			result = true;
		if (result)
			fAssociationExtensionList = resultAssociationExtensionList;			
		return result;
	}
	
	public int size() {
		return fAssociationExtensionList.size();
	}
	
	public void clear() {
		fAssociationExtensionList.clear();
	}

}

