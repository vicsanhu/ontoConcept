package org.tencompetence.qtieditor.elements;

import org.jdom.*;
import java.util.*;

public class FillingInBlankBlock extends InlineBlock {

	/** Creates a new instance of FillingInBlankBlock */
	public FillingInBlankBlock(AssessmentItem anAssessmentItem) {
		//super.createId("FIB");
		setAssessmentItem(anAssessmentItem);
	}
	
	public void fromJDOM(Element element) {
		
	      List children = element.getContent();
	      Iterator iterator = children.iterator();
	      Text textString = new Text(""); 
	      while (iterator.hasNext()) {
	        Object child = iterator.next();
	        
	        if (child instanceof Element) {
	            Element e = (Element) child;
	            
	            if(e.getName().equals(AssessmentElementFactory.TEXT_ENTRY_INTERACTION)) {
	            	TextEntryInteraction fTextEntryInteraction = new TextEntryInteraction(fAssessmentItem);
					fTextEntryInteraction.fromJDOM((Element)child);
					
					fTextEntryInteraction.setStart(textString.getText().length()+1);
					
					String correctResponse = fTextEntryInteraction.getResponseDeclaration().getFirstCorrectResponse();
					fTextEntryInteraction.setLength(correctResponse.length());
					if (!correctResponse.equals("")) {
						textString.append(" " + correctResponse); //unsure how to handle the whitespace
					} else {
						System.out.println("correct choice is empty string");
					}
					fInteractionList.addElement(fTextEntryInteraction);
	            }
	        }
	        else if (child instanceof Text) {
	            textString.append((Text) child);
	        }
	      }
	      data = textString.getText();
	}

	public Element toJDOM() {
		Element aFillingInBlankBlock = new Element(getTagName(), getNamespace());

		if (fInteractionList.size() <= 0) {
			aFillingInBlankBlock.setText(data);
		}
		
		int lastEnd = 0;
		for (int i = 0; i < fInteractionList.size(); i++) {
			TextEntryInteraction aTextEntryInteraction  = (TextEntryInteraction) fInteractionList.getBasicElementAt(i);
			
			if (aTextEntryInteraction != null) {
				Element aTextEntryInteractionElement  = aTextEntryInteraction.toJDOM();
				int start = aTextEntryInteraction.getStart();
				if (start==lastEnd) {
					aFillingInBlankBlock.addContent(aTextEntryInteractionElement);
				}
				else {
					Text aText = new Text(data.substring(lastEnd, start-1));
					aFillingInBlankBlock.addContent(aText);
					aFillingInBlankBlock.addContent(aTextEntryInteractionElement);
				}
				lastEnd = start + aTextEntryInteraction.getLength();
			}
		}
		if (lastEnd < data.length()) {
			Text aText = new Text(data.substring(lastEnd));
			aFillingInBlankBlock.addContent(aText);
		}
		return aFillingInBlankBlock;
	}

	public String getTagName() {
		return AssessmentElementFactory.P;
	}
}