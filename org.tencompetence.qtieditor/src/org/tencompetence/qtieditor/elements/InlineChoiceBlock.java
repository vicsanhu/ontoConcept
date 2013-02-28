package org.tencompetence.qtieditor.elements;

import org.jdom.*;

import java.util.*;

public class InlineChoiceBlock extends InlineBlock {

	public InlineChoiceBlock(AssessmentItem anAssessmentItem) {
		// super.createId("IC");
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
				if (e.getName().equals(
						AssessmentElementFactory.INLINE_CHOICE_INTERACTION)) {

					InlineChoiceInteraction fInlineChoiceInteraction = new InlineChoiceInteraction(
							fAssessmentItem);
					fInlineChoiceInteraction.fromJDOM((Element) child);

					fInlineChoiceInteraction.setStart(textString.getText()
							.length() + 1);

					String correctResponse = fInlineChoiceInteraction
							.getCorrectChoice().getData();
					fInlineChoiceInteraction
							.setLength(correctResponse.length());
					if (!correctResponse.equals("")) {
						textString.append(" " + correctResponse); // unsure how
																	// to handle
																	// the
																	// whitespace
					} else {
						System.out.println("correct choice is empty string");
					}
					fInteractionList.addElement(fInlineChoiceInteraction);

				}
			} else if (child instanceof Text) {
				textString.append((Text) child);
			}
		}
		data = textString.getText();
	}

	public Element toJDOM() {
		Element aInlineChoiceBlockElement = new Element(getTagName(), getNamespace());

		if (fInteractionList.size() <= 0) {
			aInlineChoiceBlockElement.setText(data);
		}

		int lastEnd = 0;
		for (int i = 0; i < fInteractionList.size(); i++) {
			InlineChoiceInteraction aInlineChoiceInteraction = (InlineChoiceInteraction) fInteractionList
					.getBasicElementAt(i);

			if (aInlineChoiceInteraction != null) {
				Element aInlineChoiceInteractionElement = aInlineChoiceInteraction
						.toJDOM();
				int start = aInlineChoiceInteraction.getStart();
				if (start == lastEnd) {
					aInlineChoiceBlockElement
							.addContent(aInlineChoiceInteractionElement);
				} else {
					Text aText = new Text(data.substring(lastEnd, start - 1));
					aInlineChoiceBlockElement.addContent(aText);
					aInlineChoiceBlockElement
							.addContent(aInlineChoiceInteractionElement);
				}
				lastEnd = start + aInlineChoiceInteraction.getLength();
			}
		}
		if (lastEnd < data.length()) {
			Text aText = new Text(data.substring(lastEnd));
			aInlineChoiceBlockElement.addContent(aText);
		}
		return aInlineChoiceBlockElement;

	}

	public String getTagName() {
		return AssessmentElementFactory.P;
	}
}
