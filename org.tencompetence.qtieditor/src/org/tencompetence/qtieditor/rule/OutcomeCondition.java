package org.tencompetence.qtieditor.rule;

import org.jdom.Element;
import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.BasicElementList;

public class OutcomeCondition extends OutcomeRule{

	private OutcomeIf fOutcomeIf = null ; // required
	private BasicElementList fOutcomeElseIfList = new BasicElementList(); // required
	private OutcomeElse fOutcomeElse = null; // option
	
	public OutcomeCondition(AbstractAssessment aAssessment) {
		setAssessment(aAssessment);
		fOutcomeIf = new OutcomeIf(aAssessment);
	}
	
	public void setOutcomeIf(OutcomeIf aOutcomeIf) {
		fOutcomeIf = aOutcomeIf;
	}
	
	public OutcomeIf getOutcomeIf() {
		return fOutcomeIf;
	}


	public BasicElementList getOutcomeElseIfList() {
		return fOutcomeElseIfList;
	}

	public void setOutcomeElseIfList(BasicElementList list) {
		fOutcomeElseIfList = list;
	}

	public void addOutcomeElseIf(OutcomeElseIf aOutcomeElseIf) {
		fOutcomeElseIfList.addElement(aOutcomeElseIf);
	}

	public void removeOutcomeElseIf(OutcomeElseIf aOutcomeElseIf) {
		fOutcomeElseIfList.removeElement(aOutcomeElseIf);
	}
	
	
	public void setOutcomeElse(OutcomeElse aOutcomeElse) {
		fOutcomeElse = aOutcomeElse;
	}
	
	public OutcomeElse getOutcomeElse() {
		return fOutcomeElse;
	}
	public void fromJDOM(Element element) {
		for (Object object : element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();
			//String value = child.getText();

			if (tag.equals(AssessmentElementFactory.OUTCOME_IF)) {
				fOutcomeIf.fromJDOM((Element)child);
			} else if (tag.equals(AssessmentElementFactory.OUTCOME_ELSE_IF)) {
				OutcomeElseIf aOutcomeElseIf = new OutcomeElseIf(getAssessment());
				aOutcomeElseIf.fromJDOM((Element)child);
				fOutcomeElseIfList.addElement(aOutcomeElseIf);
			} else if (tag.equals(AssessmentElementFactory.OUTCOME_ELSE)) {
				fOutcomeElse = new OutcomeElse(fAssessment);
				fOutcomeElse.fromJDOM((Element)child);
			}
		}		
	}

	public Element toJDOM() {
        Element aOutcomeConditionElement = new Element(getTagName(), getTestNamespace());

        
        if(fOutcomeIf != null) {
        	Element aOutcomeIfElement = fOutcomeIf.toJDOM();
            aOutcomeConditionElement.addContent(aOutcomeIfElement);
        }

		for (int i = 0; i < fOutcomeElseIfList.size(); i++) {
			Element aOutcomeElseIfElement = ((OutcomeElseIf)fOutcomeElseIfList
					.getBasicElementAt(i)).toJDOM();
			if (aOutcomeElseIfElement != null) {
				aOutcomeConditionElement.addContent(aOutcomeElseIfElement);
			}
		}

        if(fOutcomeElse != null) {
        	Element aOutcomeElseElement = fOutcomeElse.toJDOM();
            aOutcomeConditionElement.addContent(aOutcomeElseElement);
        }
        return aOutcomeConditionElement;
	}

	public String getTagName() {
        return AssessmentElementFactory.OUTCOME_CONDITION;
    }	
}
