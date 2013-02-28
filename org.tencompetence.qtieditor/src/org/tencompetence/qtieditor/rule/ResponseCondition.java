package org.tencompetence.qtieditor.rule;


import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.BasicElementList;

public class ResponseCondition extends ResponseRule{

	private ResponseIf fResponseIf = null ; // required
	private BasicElementList fResponseElseIfList = new BasicElementList(); // required
	private ResponseElse fResponseElse = null; // option
	
	public ResponseCondition(AbstractAssessment aAssessment) {
		setAssessment(aAssessment);
		fResponseIf = new ResponseIf(aAssessment);
	}
	
	public void setResponseIf(ResponseIf aResponseIf) {
		fResponseIf = aResponseIf;
	}
	
	public ResponseIf getResponseIf() {
		return fResponseIf;
	}


	public BasicElementList getResponseElseIfList() {
		return fResponseElseIfList;
	}

	public void setResponseElseIfList(BasicElementList list) {
		fResponseElseIfList = list;
	}

	public void addResponseElseIf(ResponseElseIf aResponseElseIf) {
		fResponseElseIfList.addElement(aResponseElseIf);
	}

	public void removeResponseElseIf(ResponseElseIf aResponseElseIf) {
		fResponseElseIfList.removeElement(aResponseElseIf);
	}
	
	
	public void setResponseElse(ResponseElse aResponseElse) {
		fResponseElse = aResponseElse;
	}
	
	public ResponseElse getResponseElse() {
		return fResponseElse;
	}
	public void fromJDOM(Element element) {
		for (Object object : element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();
			//String value = child.getText();

			if (tag.equals(AssessmentElementFactory.RESPONSE_IF)) {
				fResponseIf.fromJDOM((Element)child);
			} else if (tag.equals(AssessmentElementFactory.RESPONSE_ELSE_IF)) {
				ResponseElseIf aResponseElseIf = new ResponseElseIf(getAssessment());
				aResponseElseIf.fromJDOM((Element)child);
				fResponseElseIfList.addElement(aResponseElseIf);
			} else if (tag.equals(AssessmentElementFactory.RESPONSE_ELSE)) {
				fResponseElse = new ResponseElse(fAssessment);
				fResponseElse.fromJDOM((Element)child);
			}
		}		
	}

	public Element toJDOM() {
        Element aResponseConditionElement = new Element(getTagName(), getNamespace());

        
        if(fResponseIf != null) {
        	Element aResponseIfElement = fResponseIf.toJDOM();
            aResponseConditionElement.addContent(aResponseIfElement);
        }

		for (int i = 0; i < fResponseElseIfList.size(); i++) {
			Element aResponseElseIfElement = ((ResponseElseIf)fResponseElseIfList
					.getBasicElementAt(i)).toJDOM();
			if (aResponseElseIfElement != null) {
				aResponseConditionElement.addContent(aResponseElseIfElement);
			}
		}

        if(fResponseElse != null) {
        	Element aResponseElseElement = fResponseElse.toJDOM();
            aResponseConditionElement.addContent(aResponseElseElement);
        }
        return aResponseConditionElement;
	}

	public String getTagName() {
        return AssessmentElementFactory.RESPONSE_CONDITION;
    }	
}
