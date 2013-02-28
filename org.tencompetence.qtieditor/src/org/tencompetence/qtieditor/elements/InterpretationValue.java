package org.tencompetence.qtieditor.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import org.jdom.Element;
import org.tencompetence.qtieditor.serialization.INamespaces;

public class InterpretationValue {

	// attributes
	private String fInterpretation = null; // optional

	// elements
	private List<Value> fValueList = new ArrayList<Value>(); // at last one
	
	private String fContainerName = ""; 

	/** Creates a new instance of InterpretationValue */
	public InterpretationValue() {
	}

	public InterpretationValue(String name) {
		fContainerName = name;
	}
	
	public List getValueList() {
		return fValueList;
	}

	public void setInterpretation(String interpretation) {
		this.fInterpretation = interpretation;
	}

	public String getInterpretation() {
		return fInterpretation;
	}

	public void addValue(Value value) {
		if (!fValueList.contains(value))
			fValueList.add(value);
	}

	public void setValueAt(int i, Value value) {
		if (fValueList.size()>=i)
			fValueList.set(i, value);
	}
	
	public boolean removeAll(List c) {
		return fValueList.removeAll(c);
	}
	
	public boolean removeValue(Value value) {
		return fValueList.remove(value);
	}
	
	public void removeValueData(String aString) {
		for (int i=0; i<fValueList.size(); i++) {
			if (((Value)fValueList.get(i)).getData().equals(aString)) {
				fValueList.remove(i);
			}
		}
	}
	
	public boolean isIncluded(String aString) {
		for (int i=0; i<fValueList.size(); i++) {
			if (((Value)fValueList.get(i)).getData().equals(aString)) {
				return true;
			}
		}
		return false;
	}
	
	public void removeValueAt(int i) {
		if (fValueList.size()>=i)
			fValueList.remove(i);
	}

	public Value getValueAt(int i) {
		if (fValueList.size()>=i)
			return (Value) fValueList.get(i);
		else 
			return null;
	}

	public int size() {
		return fValueList.size();
	}

	public void clear() {
		fValueList.clear();
	}
	
	public void fromJDOM(Element element) {

		for (Object object: element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();
			String value = child.getText();

			if (tag.equals(AssessmentElementFactory.VALUE)) {
				Value aValue = new Value();
				aValue.setData(value);
				fValueList.add(aValue);
			} 
		}
	}
	
	public Element toJDOM() {
		
		int size = size();
		if (size==0) return null;
		
		Element aInterpretationValue = new Element(fContainerName, INamespaces.IMSQTI_NAMESPACE_2_0);
		
		for (int i = 0; i < size; i++) {
			Element aValue = new Element(AssessmentElementFactory.VALUE, INamespaces.IMSQTI_NAMESPACE_2_0);
			aValue.setText(((Value)fValueList.get(i)).getData());
			aInterpretationValue.addContent(aValue);
		}

		return aInterpretationValue;
	}

}
