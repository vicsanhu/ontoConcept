package org.tencompetence.qtieditor.elements;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;

public class AreaMapEntry extends BasicElement{

	private String fShape = ""; // required: circle, default, ellipse, poly, rect 
	private List<Coordination> fCoordinationList = new ArrayList<Coordination>(); // required
	private double fMappedValue; // required

	/** Creates a new instance of AreaMappingEntry */
	public AreaMapEntry() {
	}

	public void setShape(String shape) {
		this.fShape = shape;
	}

	public String getShape() {
		return fShape;
	}

	public List getCoordinationList() {
		return fCoordinationList;
	}

	public void setCoordinationList(List list) {
		fCoordinationList = list;
	}

	public void addCoordination(Coordination aCoordination) {
		if (!fCoordinationList.contains(aCoordination))
			fCoordinationList.add(aCoordination);
	}

	public void removeCoordination(Coordination aCoordination) {
		fCoordinationList.remove(aCoordination);
	}

	public void setMappedValue(double mappedValue) {
		this.fMappedValue = mappedValue;
	}

	public double getMappedValue() {
		return fMappedValue;
	}
	
	public void fromJDOM(Element element) {


		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (tag.equals(AssessmentElementFactory.SHAPE)) {
				fShape = value;
			} else if (tag.equals(AssessmentElementFactory.MAPPED_VALUE)) {
				fMappedValue = Double.parseDouble(value);
			}
		}

		for (Object object: element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();
			String value = child.getText();

			//toDo coordination list
		}
	}
	
	public Element toJDOM() {
				
		Element aElement = new Element(getTagName(), getNamespace());

		aElement.setAttribute(AssessmentElementFactory.SHAPE, fShape);
		aElement.setAttribute(AssessmentElementFactory.MAPPED_VALUE, String.valueOf(fMappedValue));
		/*
		for (int i = 0; i < fCoordinationList.size(); i++) {
			Element aCoordinationElement = ((Coordination) fCoordinationList.get(i)).toJDOM();
			if (aCoordinationElement != null)
				aElement.addContent(aCoordinationElement);
		}
		*/

		return aElement ;
	}

	@Override
	protected String getTagName() {
		// TODO Auto-generated method stub
		return AssessmentElementFactory.AREA_MAP_ENTRY;
	}
}
