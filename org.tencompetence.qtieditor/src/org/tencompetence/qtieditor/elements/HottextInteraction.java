package org.tencompetence.qtieditor.elements;


import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Text;

public class HottextInteraction extends BlockInteraction{

	// elements
	private String data = ""; 
	private int fMaxChoices = 1; // required

	protected BasicElementList fHottextList = new BasicElementList();

	/** Creates a new instance of HottextInteraction */
	public HottextInteraction(AssessmentItem anAssessmentItem, int choiceNumber) { // used by UI
		setAssessmentItem(anAssessmentItem);
		ResponseDeclaration aResponseDeclaration = createResponseDeclaration();
		aResponseDeclaration.setBlockInteraction(this);
		aResponseDeclaration.setAssociatedOutcomeDeclaration(createOutcomeForMatchCorrect(aResponseDeclaration));
	}
	
	public HottextInteraction(AssessmentItem anAssessmentItem) { //used by fromJDOM()
		setAssessmentItem(anAssessmentItem);
	}

	private ResponseDeclaration createResponseDeclaration(){
		// create and include responseDeclaration
		ResponseDeclaration aResponseDeclaration = new ResponseDeclaration(fAssessmentItem, "RESPONSE");
		aResponseDeclaration.setBaseType(AssessmentElementFactory.IDENTIFIER);
		aResponseDeclaration.setCardinality(AssessmentElementFactory.SINGLE);
		setResponseDeclaration(aResponseDeclaration);
		fAssessmentItem.getResponseDeclarationList().addElement(aResponseDeclaration);
		return aResponseDeclaration;
	}
	
	public OutcomeDeclaration createOutcomeForMatchCorrect(ResponseDeclaration aResponseDeclaration) {
		//return new OutcomeDeclaration(aResponseDeclaration.getId()+".SCORE", 
		return new OutcomeDeclaration("SCORE", 
				AssessmentElementFactory.INTEGER, 
				AssessmentElementFactory.SINGLE,
				fAssessmentItem);
	}

	public void setData(String aString) {
		data = aString;
	}

	public void appandData(String addition) {
		data = data + addition;
	}
	
	public String getData() {
		return data;
	}	

	public void setMaxChoices(int maxChoices) {
		this.fMaxChoices = maxChoices;
	}

	public int getMaxChoices() {
		return fMaxChoices;
	}
	
	public BasicElementList getHottextList() {
		return fHottextList;
	}

	public void setHottextList(BasicElementList list) {
		fHottextList = list;
	}

	public void addHottext(Hottext aHottext) {
		fHottextList.addElement(aHottext);
	}

	public void addHottextAt(int index, Hottext aHottext) {
		fHottextList.addElementAt(index, aHottext);
	}

	public void setHottextAt(int index, Hottext aHottext) {
		fHottextList.setElementAt(index, aHottext);
	}
	
	public Hottext getHottextByID(String id) {
		for (int i = 0; i < fHottextList.size(); i++) {
			Hottext aHottext  = (Hottext) fHottextList.getBasicElementAt(i);
			if (aHottext.getId().equals(id))
				return aHottext;
		}
		return null;
	}
	
	public Hottext getHottextAt(int index) {
		return (Hottext)fHottextList.getBasicElementAt(index);
	}
	
	public void removeHottext(Hottext aHottext) {
		fHottextList.removeElement(aHottext);
	}
	
	public void fromJDOM(Element element) {
		super.fromJDOM(element);
		
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.MAX_CHOICES.equals(tag)) {
				fMaxChoices = Integer.parseInt(value);
			}
		}

		for (Object o : element.getChildren()) {
			Element child = (Element) o;
			String tag = child.getName();
			//String value = child.getText();

			if (tag.equals(AssessmentElementFactory.P)) {
				List grandChildren = child.getContent();
				Iterator iterator = grandChildren.iterator();
				Text textString = new Text("");
				while (iterator.hasNext()) {
					Object grandChild = iterator.next();

					if (grandChild instanceof Element) {
						Element e = (Element) grandChild;
						if (e.getName().equals(AssessmentElementFactory.HOTTEXT)) {

							Hottext aHottext = new Hottext(fAssessmentItem, this);
							aHottext.fromJDOM((Element) grandChild);
							aHottext.setStart(textString.getText().length() + 1);	
							aHottext.setLength(aHottext.getData().length());
							addHottext(aHottext);
							textString.append(" " + aHottext.getData());
						}
					} else if (grandChild instanceof Text) {
						textString.append((Text) grandChild);
					}
				}
				data = textString.getText();
			}
		}
		fAssessmentItem.getFirstResponseDeclaration().setBlockInteraction(this);
	}
	
	public Element toJDOM() {
		Element aInteractionElement = super.toJDOM();
		aInteractionElement.setAttribute(AssessmentElementFactory.MAX_CHOICES, String.valueOf(fMaxChoices));
		
		Element aBlockElement = new Element(AssessmentElementFactory.P, getNamespace());
		
		if (fHottextList.size() <= 0) {			
			aBlockElement.setText(data);
			aInteractionElement.addContent(aBlockElement);
			return aInteractionElement;
		}

		int lastEnd = 0;
		for (int i = 0; i < fHottextList.size(); i++) {
			Hottext aHottext = (Hottext) fHottextList.getBasicElementAt(i);

			if (aHottext != null) {
				Element aHottextElement = aHottext.toJDOM();
				int start = aHottext.getStart();
				if (start == lastEnd) {
					aBlockElement.addContent(aHottextElement);
				} else {
					Text aText = new Text(data.substring(lastEnd, start - 1));
					aBlockElement.addContent(aText);
					aBlockElement.addContent(aHottextElement);
				}
				lastEnd = start + aHottext.getLength();
			}
		}
		if (lastEnd < data.length()) {
			Text aText = new Text(data.substring(lastEnd));
			aBlockElement.addContent(aText);
		}
		aInteractionElement.addContent(aBlockElement);
		return aInteractionElement;
	}


	public String getTagName() {
		return AssessmentElementFactory.HOTTEXT_INTERACTION;
	}
}