package org.tencompetence.qtieditor.elements;

import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Text;

public class GapMatchInteraction extends BlockInteraction{

	// attributes
	protected boolean fShuffle = false; // required

	// elements
	protected String data = ""; // optional
	protected BasicElementList fGapList = new BasicElementList();
	protected BasicElementList fGapTextList = new BasicElementList();

	/** Creates a new instance of GapMatchInteraction */
	public GapMatchInteraction(AssessmentItem anAssessmentItem, int choiceNumber) { // used by UI
		setAssessmentItem(anAssessmentItem);
		fShuffle = true; // default
		for (int i = 0; i < choiceNumber; i++) {
			fGapTextList.addElement(new GapText(anAssessmentItem, this));
		}
		ResponseDeclaration aResponseDeclaration = createResponseDeclaration();
		aResponseDeclaration.setAssociatedOutcomeDeclaration(createOutcomeForMatchCorrect(aResponseDeclaration));
	}
	
	public GapMatchInteraction(AssessmentItem anAssessmentItem) { //used by fromJDOM()
		setAssessmentItem(anAssessmentItem);
	}

	private ResponseDeclaration createResponseDeclaration(){
		// create and include responseDeclaration
		ResponseDeclaration aResponseDeclaration = new ResponseDeclaration(fAssessmentItem, "RESPONSE");
		aResponseDeclaration.setBaseType(AssessmentElementFactory.DIRECTED_PAIR);
		aResponseDeclaration.setCardinality(AssessmentElementFactory.MULTIPLE);
		setResponseDeclaration(aResponseDeclaration);
		fAssessmentItem.getResponseDeclarationList().addElement(aResponseDeclaration);
		return aResponseDeclaration;
	}
	
	public OutcomeDeclaration createOutcomeForMatchCorrect(ResponseDeclaration aResponseDeclaration) {
		//return new OutcomeDeclaration(aResponseDeclaration.getId()+".SCORE", 
		return new OutcomeDeclaration("SCORE", 
				AssessmentElementFactory.FLOAT, 
				AssessmentElementFactory.SINGLE,
				fAssessmentItem);
	}

	public void setShuffle(boolean shuffle) {
		this.fShuffle = shuffle;
	}

	public boolean getShuffle() {
		return fShuffle;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void appandData(String addition) {
		this.data = data + addition;
	}
	
	public String getData() {
		return data;
	}
	
	public BasicElementList getGapList() {
		return fGapList;
	}

	public void setGapList(BasicElementList list) {
		fGapList = list;
	}

	public void addGap(Gap aGap) {
		fGapList.addElement(aGap);
	}

	public void addGapAt(int index, Gap aGap) {
		fGapList.addElementAt(index, aGap);
	}

	public void setGapAt(int index, Gap aGap) {
		fGapList.setElementAt(index, aGap);
	}
	
	public Gap getGapByID(String id) {
		for (int i = 0; i < fGapList.size(); i++) {
			Gap aGap  = (Gap) fGapList.getBasicElementAt(i);
			if (aGap.getId().equals(id))
				return aGap;
		}
		return null;
	}
	
	public Gap getGapAt(int index) {
		return (Gap)fGapList.getBasicElementAt(index);
	}
	
	public void removeGap(Gap aGap) {
		fGapList.removeElement(aGap);
	}
	
	public BasicElementList getGapTextList() {
		return fGapTextList;
	}

	public void setGapTextList(BasicElementList list) {
		fGapTextList = list;
	}

	public void addGapText(GapText aGapText) {
		fGapTextList.addElement(aGapText);
	}
	
	public GapText getGapTextByID(String id) {
		for (int i = 0; i < fGapTextList.size(); i++) {
			GapText aGapText  = (GapText) fGapTextList.getBasicElementAt(i);
			if (aGapText.getId().equals(id))
				return aGapText;
		}
		return null;
	}
	
	public GapText getGapTextAt(int index) {
		return (GapText)fGapTextList.getBasicElementAt(index);
	}

	public void removeGapText(GapText aGapText) {
		fGapTextList.removeElement(aGapText);
	}


	public void fromJDOM(Element element) {
		super.fromJDOM(element);
		
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.SHUFFLE.equals(tag)) {
				fShuffle = "true".equalsIgnoreCase(value);
			}
		}

		for (Object o : element.getChildren()) {
			Element child = (Element) o;
			String tag = child.getName();
			//String value = child.getText();

			if (tag.equals(AssessmentElementFactory.GAP_TEXT)) {
				GapText aGapText = new GapText(fAssessmentItem, this);
				aGapText.fromJDOM((Element) child);
				fGapTextList.addElement(aGapText);
			} else if (tag.equals(AssessmentElementFactory.P)) {
				List grandChildren = child.getContent();
				Iterator iterator = grandChildren.iterator();
				Text textString = new Text("");
				while (iterator.hasNext()) {
					Object grandChild = iterator.next();

					if (grandChild instanceof Element) {
						Element e = (Element) grandChild;
						if (e.getName().equals(AssessmentElementFactory.GAP)) {

							Gap aGap = new Gap(fAssessmentItem, this);
							aGap.fromJDOM((Element) grandChild);

							aGap.setStart(textString.getText().length() + 1);
							
							InterpretationValue aInterpretationValue = getResponseDeclaration().getCorrectResponse();
					        for (int i=0; i<aInterpretationValue.size(); i++) {
					        	String valueData = aInterpretationValue.getValueAt(i).getData();
					        	int index = valueData.indexOf(" ");
					        	if (index!=-1) {
					        		String targetID = valueData.substring(index+1);
					        		if (targetID.equals(aGap.getId())) {
					        			String sourceID = valueData.substring(0, index);
						        		GapText aGapText = getGapTextByID(sourceID);
						        		aGap.setData(aGapText.getData());
						        		aGap.setMatchedGapText(aGapText);
										aGap.setLength(aGapText.getData().length());
										if (!aGapText.getData().equals("")) {
											textString.append(" " + aGapText.getData());
										} else {
											System.out.println("correct choice is an empty string");
										}
										fGapList.addElement(aGap);
					        		}
					        	}
					        }
						}
					} else if (grandChild instanceof Text) {
						textString.append((Text) grandChild);
					}
				}
				data = textString.getText();
			}
		}
	}
	
	public Element toJDOM() {
		Element aInteractionElement = super.toJDOM();

		aInteractionElement.setAttribute(AssessmentElementFactory.SHUFFLE, fShuffle ? "true" : "false");
		
		for (int i = 0; i < fGapTextList.size(); i++) {
			Element aGapTextElement  = ((GapText) fGapTextList.getBasicElementAt(i)).toJDOM();
			if (aGapTextElement != null)
				aInteractionElement.addContent(aGapTextElement);
		}
		
		Element aBlockElement = new Element(AssessmentElementFactory.P, getNamespace());
		
		if (fGapList.size() <= 0) {			
			aBlockElement.setText(data);
			aInteractionElement.addContent(aBlockElement);
			return aInteractionElement;
		}

		int lastEnd = 0;
		for (int i = 0; i < fGapList.size(); i++) {
			Gap aGap = (Gap) fGapList.getBasicElementAt(i);

			if (aGap != null) {
				Element aGapElement = aGap.toJDOM();
				int start = aGap.getStart();
				if (start == lastEnd) {
					aBlockElement.addContent(aGapElement);
				} else {
					Text aText = new Text(data.substring(lastEnd, start - 1));
					aBlockElement.addContent(aText);
					aBlockElement.addContent(aGapElement);
				}
				lastEnd = start + aGap.getLength();
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
		return AssessmentElementFactory.GAP_MATCH_INTERACTION;
	}
}