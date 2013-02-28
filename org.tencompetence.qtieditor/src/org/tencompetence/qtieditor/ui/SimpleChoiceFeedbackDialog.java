package org.tencompetence.qtieditor.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.FeedbackInline;
import org.tencompetence.qtieditor.elements.OutcomeDeclaration;
import org.tencompetence.qtieditor.elements.ResponseDeclaration;
import org.tencompetence.qtieditor.elements.SimpleChoice;

public class SimpleChoiceFeedbackDialog extends Dialog {

	private Text fFeedbackText;
	private Button fButton;

	private SimpleChoice fSimpleChoice;
	private EditChoiceInteractionPanel fEditChoiceInteractionPanel;

	public SimpleChoiceFeedbackDialog(Shell parentShell,
			SimpleChoice aSimpleChoice, Button aButton,
			EditChoiceInteractionPanel aPanel) {
		super(parentShell);
		fSimpleChoice = aSimpleChoice;
		fButton = aButton;
		fEditChoiceInteractionPanel = aPanel;
	}

	protected Control createDialogArea(Composite parent) {
		Composite controlArea = (Composite) super.createDialogArea(parent);

		GridLayout layout = (GridLayout) controlArea.getLayout();
		layout.numColumns = 1;

		Label aLabel = new Label(controlArea, SWT.RIGHT);
		aLabel.setText("Feedback: ");

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 60;
		gd.widthHint = 510;
		fFeedbackText = new Text(controlArea, SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		if (fSimpleChoice.getFeedbackInline() != null)
			fFeedbackText.setText(fSimpleChoice.getFeedbackInline().getData());
		fFeedbackText.setLayoutData(gd);

		return controlArea;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
	}
	
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			
			//when the responseDeclaration is constructed from xml and no modalFeedback is define,
			// the response's feedback Outcome is null. so create the feedback outcome 
			ResponseDeclaration aResponseDeclaration = fSimpleChoice.getChoiceInteraction().getResponseDeclaration();
			if (aResponseDeclaration.getFeedbackOutcomeDeclaration()==null) {
				OutcomeDeclaration aOutcomeDeclaration = new OutcomeDeclaration(aResponseDeclaration.getId()+".FEEDBACK", 
						AssessmentElementFactory.IDENTIFIER, 
						AssessmentElementFactory.SINGLE,
						fSimpleChoice.getAssessmentItem());
				aResponseDeclaration.setFeedbackOutcomeDeclaration(aOutcomeDeclaration);
			}
			if (fSimpleChoice.getFeedbackInline() == null) {
				FeedbackInline aFeedbackInline = new FeedbackInline(
						fSimpleChoice.getAssessmentItem(), 
						fSimpleChoice);
				fSimpleChoice.setFeedbackInline(aFeedbackInline);
			}
			fSimpleChoice.getFeedbackInline().setData(fFeedbackText.getText());
			// fSimpleChoice.setFixed(fixed);
			fButton.setToolTipText(fSimpleChoice.getFeedbackInline().getData());
			fEditChoiceInteractionPanel.setDirty(true);
			close();
		} else {
			super.buttonPressed(buttonId);
		}
	}
}
