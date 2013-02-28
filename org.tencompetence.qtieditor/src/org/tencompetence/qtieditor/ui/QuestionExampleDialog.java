package org.tencompetence.qtieditor.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

import org.tencompetence.qtieditor.elements.AssessmentElementFactory;

public class QuestionExampleDialog extends Dialog {

	private String fQuestionClass;
	private Image fImage = null;

	public QuestionExampleDialog(Shell parentShell, String aClass) {
		super(parentShell);
		fQuestionClass = aClass;
		parentShell.setText("Example");
	}

	protected Control createDialogArea(Composite parent) {

		Composite controlArea = (Composite) super.createDialogArea(parent);
		String type = "undefined-type";
		
		if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.TEXT_BASED_MULTIPLE_CHOICE)) {
			fImage = ImageFactory.getImage(ImageFactory.EXAMPLE_MULTIPLE_CHOICE);
			type = "Multiple Choice";
		} else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.TEXT_BASED_MULTIPLE_RESPONSE)) {
			fImage = ImageFactory.getImage(ImageFactory.EXAMPLE_MULTIPLE_RESPONSE);
			type = "Multiple Response";
		} else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.YES_NO)) {
			fImage = ImageFactory.getImage(ImageFactory.EXAMPLE_YES_NO);
			type = "Yes or No";
		} else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.LIKERT)) {
			fImage = ImageFactory.getImage(ImageFactory.EXAMPLE_LIKERT_SCALE);
			type = "Likert Scale";
		} else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.OPEN)) {
			fImage = ImageFactory.getImage(ImageFactory.EXAMPLE_OPEN_QUESTION);
			type = "Open Question";
		} else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.FILL_IN_THE_BLANK)) {
			fImage = ImageFactory.getImage(ImageFactory.EXAMPLE_FILL_IN_THE_BLANK);
			type = "Fill in the Blank";
		} else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.INLINE_CHOICE)) {
			fImage = ImageFactory.getImage(ImageFactory.EXAMPLE_INLINE_CHOICE);
			type = "In-line Choice";
		} else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.ASSOCIATE)) {
			fImage = ImageFactory.getImage(ImageFactory.EXAMPLE_ASSOCIATE);
			type = "Associate";
		} else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.MATCH)) {
			fImage = ImageFactory.getImage(ImageFactory.EXAMPLE_MATCH);
			type = "Match";
		} else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.ORDER)) {
			fImage = ImageFactory.getImage(ImageFactory.EXAMPLE_ORDER);
			type = "Order";
		}
		
		Label label = new Label (controlArea, SWT.NONE);
		
		if (fImage!=null) {
			label.setText("A question with the type of '" + type + "' will be renderred as below.");
			label = new Label (controlArea, SWT.NONE);
			label.setText(" ");
			label = new Label (controlArea, SWT.NONE);
			label.setImage (fImage);
		} else {
			label.setText("No Example for this type of question.");
		}
		
		return controlArea;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
	}
	
	protected void buttonPressed(int buttonId) {
		/*
		if (buttonId == IDialogConstants.OK_ID) {
			close();
		} else {
			super.buttonPressed(buttonId);
		}
		*/
		super.buttonPressed(buttonId);
	}
}

