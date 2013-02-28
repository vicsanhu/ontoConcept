package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;
import org.tencompetence.ldauthor.Logger;

public class ItemBody extends BodyElement {

	// elements
	private String fQuestionClass = ""; // indicating question type
	private BasicElementList fBlockList;

	// parent
	private AssessmentItem fAssessmentItem;

	/** Creates a new instance of ItemBody */
	public ItemBody(AssessmentItem anAssessmentItem) {
		//super.createId("IB");
		fAssessmentItem = anAssessmentItem;
		fBlockList = new BasicElementList();
	}

	public AssessmentItem getAssessmentItem() {
		return fAssessmentItem;
	}

	public void setQuestionClass(String type) {
		this.fQuestionClass = type;
	}

	public String getQuestionClass() {
		return fQuestionClass;
	}

	public BasicElementList getBlockList() {
		return fBlockList;
	}

	public void setBlockList(BasicElementList list) {
		fBlockList = list;
	}

	public void addBlock(Block aBlock) {
		fBlockList.addElement(aBlock);
	}

	public void removeBlock(Block aBlock) {
		fBlockList.removeElement(aBlock);
	}

	public Block getFirstBlock() {
		return (Block)fBlockList.getBasicElementAt(0);
	}
	
	public void setFirstBlock(Block aBlock) {
		if (fBlockList.size()==0)
			fBlockList.addElement(aBlock);
		else
			fBlockList.setElementAt(0, aBlock);
	}
	
	public void fromJDOM(Element element) {
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (tag.equals(AssessmentElementFactory.CLASS)) {
				fQuestionClass = value;
			}
		}
		
		for (Object object : element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();
			String value = child.getText();

			if (tag.equals(AssessmentElementFactory.P)) {
				if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.FILL_IN_THE_BLANK)) {
					FillingInBlankBlock aFillingInBlankBlock = new FillingInBlankBlock(fAssessmentItem);
					aFillingInBlankBlock.fromJDOM((Element)child);
					fBlockList.addElement(aFillingInBlankBlock);					
				}
				else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.INLINE_CHOICE)) {
					InlineChoiceBlock aInlineChoiceBlock = new InlineChoiceBlock(fAssessmentItem);
					aInlineChoiceBlock.fromJDOM((Element)child);
					fBlockList.addElement(aInlineChoiceBlock);
				}
					
				/*	
				if ( child.getChildren(AssessmentElementFactory.TEXT_ENTRY_INTERACTION, getNamespace()).size() > 0 ) {
					FillingInBlankBlock aFillingInBlankBlock = new FillingInBlankBlock(fAssessmentItem);
					aFillingInBlankBlock.fromJDOM((Element)child);
					fBlockList.addElement(aFillingInBlankBlock);
				} else if (child.getChildren(AssessmentElementFactory.INLINE_CHOICE_INTERACTION, getNamespace()).size() > 0 ) {
					InlineChoiceBlock aInlineChoiceBlock = new InlineChoiceBlock(fAssessmentItem);
					aInlineChoiceBlock.fromJDOM((Element)child);
					fBlockList.addElement(aInlineChoiceBlock);
				} else {
					PBlock p = new PBlock(fAssessmentItem);
					p.setData(value);
					fBlockList.addElement(p);
				}	
				*/			
			} 
			else if (tag.equals(AssessmentElementFactory.CHOICE_INTERACTION)) {
				ChoiceInteraction aChoiceInteraction = new ChoiceInteraction(fAssessmentItem);
				aChoiceInteraction.fromJDOM((Element)child);
				fBlockList.addElement(aChoiceInteraction);
			} 
			else if (tag.equals(AssessmentElementFactory.EXTENDED_TEXT_INTERACTION)) {
				ExtendedTextInteraction aExtendedTextInteraction = new ExtendedTextInteraction(fAssessmentItem, "notCreateOutcome");
				aExtendedTextInteraction.fromJDOM((Element)child);
				fBlockList.addElement(aExtendedTextInteraction);
			} 
			else if (tag.equals(AssessmentElementFactory.MATCH_INTERACTION)) {
				MatchInteraction aMatchInteraction = new MatchInteraction(fAssessmentItem);
				aMatchInteraction.fromJDOM((Element)child);
				fBlockList.addElement(aMatchInteraction);
			} 
			else if (tag.equals(AssessmentElementFactory.ASSOCIATE_INTERACTION)) {
				AssociateInteraction aAssociateInteraction = new AssociateInteraction(fAssessmentItem);
				aAssociateInteraction.fromJDOM((Element)child);
				fBlockList.addElement(aAssociateInteraction);
			} 
			else if (tag.equals(AssessmentElementFactory.ORDER_INTERACTION)) {
				OrderInteraction aOrderInteraction = new OrderInteraction(fAssessmentItem);
				aOrderInteraction.fromJDOM((Element)child);
				fBlockList.addElement(aOrderInteraction);
			} 
			else if (tag.equals(AssessmentElementFactory.GAP_MATCH_INTERACTION)) {
				GapMatchInteraction aGapMatchInteraction = new GapMatchInteraction(fAssessmentItem);
				aGapMatchInteraction.fromJDOM((Element)child);
				fBlockList.addElement(aGapMatchInteraction);
			} 
			else if (tag.equals(AssessmentElementFactory.HOTTEXT_INTERACTION)) {
				HottextInteraction aHottextInteraction = new HottextInteraction(fAssessmentItem);
				aHottextInteraction.fromJDOM((Element)child);
				fBlockList.addElement(aHottextInteraction);
			} 
			else if (tag.equals(AssessmentElementFactory.SLIDER_INTERACTION)) {
				SliderInteraction aSliderInteraction = new SliderInteraction(fAssessmentItem, "notCreateOutcome");
				aSliderInteraction.fromJDOM((Element)child);
				fBlockList.addElement(aSliderInteraction);
			} 
			else {
				Logger.logError("ItemBody: the editor can not handle the interaction: " + tag +".");
			}
		}
	}

	public Element toJDOM() {
		Element aItemBody = new Element(getTagName(), getNamespace());

		if (!fQuestionClass.equals(""))
			aItemBody.setAttribute(AssessmentElementFactory.CLASS, fQuestionClass);
		
		for (int i = 0; i < fBlockList.size(); i++) {
			Block aBlock = (Block)fBlockList.getBasicElementAt(i);
			if (aBlock instanceof PBlock) {
				Element p = new Element(AssessmentElementFactory.P, getNamespace());
				p.setText(((PBlock)aBlock).getData());
				aItemBody.addContent(p);
			}
			else if (aBlock instanceof ChoiceInteraction) {
				Element aChoiceInteraction = ((ChoiceInteraction)aBlock).toJDOM();
				if (aChoiceInteraction != null)
					aItemBody.addContent(aChoiceInteraction);
			}
			else if (aBlock instanceof ExtendedTextInteraction) {
				Element aExtendedTextInteraction = ((ExtendedTextInteraction)aBlock).toJDOM();
				if (aExtendedTextInteraction != null)
					aItemBody.addContent(aExtendedTextInteraction);
			}
			else if (aBlock instanceof FillingInBlankBlock) {
				Element aFillingInBlankBlock = ((FillingInBlankBlock)aBlock).toJDOM();
				if (aFillingInBlankBlock != null)
					aItemBody.addContent(aFillingInBlankBlock);
			}
			else if (aBlock instanceof InlineChoiceBlock) {
				Element aInlineChoiceBlock = ((InlineChoiceBlock)aBlock).toJDOM();
				if (aInlineChoiceBlock != null)
					aItemBody.addContent(aInlineChoiceBlock);
			}
			else if (aBlock instanceof MatchInteraction) {
				Element aMatchInteraction = ((MatchInteraction)aBlock).toJDOM();
				if (aMatchInteraction != null)
					aItemBody.addContent(aMatchInteraction);
			}
			else if (aBlock instanceof AssociateInteraction) {
				Element aAssociateInteraction = ((AssociateInteraction)aBlock).toJDOM();
				if (aAssociateInteraction != null)
					aItemBody.addContent(aAssociateInteraction);
			}
			else if (aBlock instanceof OrderInteraction) {
				Element aOrderInteraction = ((OrderInteraction)aBlock).toJDOM();
				if (aOrderInteraction != null)
					aItemBody.addContent(aOrderInteraction);
			}
			else if (aBlock instanceof GapMatchInteraction) {
				Element aGapMatchInteraction = ((GapMatchInteraction)aBlock).toJDOM();
				if (aGapMatchInteraction != null)
					aItemBody.addContent(aGapMatchInteraction);
			}
			else if (aBlock instanceof HottextInteraction) {
				Element aHottextInteraction = ((HottextInteraction)aBlock).toJDOM();
				if (aHottextInteraction != null)
					aItemBody.addContent(aHottextInteraction);
			}
			else if (aBlock instanceof SliderInteraction) {
				Element aSliderInteraction = ((SliderInteraction)aBlock).toJDOM();
				if (aSliderInteraction != null)
					aItemBody.addContent(aSliderInteraction);
			} 
			else {
				Logger.logError("ItemBody: the editor can not handle the interaction: " + aBlock.getClass().getName() +".");
			}
		}
		return aItemBody;
	}
	
	public String getTagName() {
		return AssessmentElementFactory.ITEM_BODY;
	}

}
