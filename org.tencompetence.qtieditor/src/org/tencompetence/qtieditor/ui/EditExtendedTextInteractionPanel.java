package org.tencompetence.qtieditor.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.ExtendedTextInteraction;

public class EditExtendedTextInteractionPanel extends AbstractQuestionEditPanel {

	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	private Text fPromptText;
	private Text fLineText;
	private Text fLengthText;

	private ExtendedTextInteraction fExtendedTextInteraction;

	public EditExtendedTextInteractionPanel(Composite parent, AssessmentItem aAssessmentItem, 
			ExtendedTextInteraction aExtendedTextInteraction, AssessmentTestEditor editor) {

		super(parent, editor);
		fExtendedTextInteraction = aExtendedTextInteraction;
		fAssessmentItem = aAssessmentItem; 
		init();
		AssessmentTestEditor.changeToContentColour(this);
	}

	public void init() {

		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayout(gl);
		
		createQuestionTitlePanel(this, toolkit, "Open Question");
		
		Composite promptEditPanel = toolkit.createComposite(this, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = false;
		promptEditPanel.setLayoutData(gridData);
		
		promptEditPanel.setLayout(new GridLayout());
		Label aLabel = toolkit.createLabel(promptEditPanel, "Question Text ", SWT.NONE);
		aLabel.setFont(AssessmentTestEditor.fontBold9);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 140; //500
		gd.widthHint = 460; //500
		fPromptText = toolkit.createText(promptEditPanel, fExtendedTextInteraction.getPrompt().getData(), SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		fPromptText.setLayoutData(gd);
		fPromptText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fExtendedTextInteraction.getPrompt().setData(fPromptText.getText());
				setDirty(true);
			}
		});
		
		/*
		
		Composite controlEditPanel = new Composite(this, SWT.CENTER);
		controlEditPanel.setLayout(new GridLayout(4, false));

		Label label = new Label(controlEditPanel , SWT.LEFT_TO_RIGHT);
		label.setText("Expected Lines: ");

		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 15;
		gd.widthHint = 60;
		fLineText = new Text(controlEditPanel , SWT.BORDER);
		fLineText.setText(String.valueOf(fExtendedTextInteraction.getExpectedLines()));
		fLineText.setLayoutData(gd);
		fLineText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					int number = Integer.parseInt(((Text) e.getSource()).getText());
					fExtendedTextInteraction.setExpectedLines(number);
				}
				catch (Exception event) {
					MessageBox mDialog = new MessageBox(fItemEditor.getParent().getShell(), SWT.OK);
				      mDialog.setMessage("Please input a number!");
				      mDialog.open();		
				}
			}
		});
		
		
		label = new Label(controlEditPanel , SWT.LEFT_TO_RIGHT);
		label.setText("    Expected number of characters: ");

		fLengthText = new Text(controlEditPanel , SWT.BORDER);
		fLengthText.setText(String.valueOf(fExtendedTextInteraction.getExpectedLength()));
		fLengthText.setLayoutData(gd);
		fLengthText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					int number = Integer.parseInt(((Text) e.getSource()).getText());
					fExtendedTextInteraction.setExpectedLength(number);
				}
				catch (Exception event) {
					MessageBox mDialog = new MessageBox(fItemEditor.getParent().getShell(), SWT.OK);
				      mDialog.setMessage("Please input a number!");
				      mDialog.open();		
				}
			}
		});
		*/
		setDirty(false);		
	}

	public String getHelpText() {
		return "This is a open question. Please define the expected lines or expected length.";
	}

}

