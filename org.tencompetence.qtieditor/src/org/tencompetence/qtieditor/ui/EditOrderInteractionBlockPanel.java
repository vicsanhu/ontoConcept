package org.tencompetence.qtieditor.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.BasicElementList;
import org.tencompetence.qtieditor.elements.OrderInteraction;
import org.tencompetence.qtieditor.elements.InterpretationValue;
import org.tencompetence.qtieditor.elements.MapEntry;
import org.tencompetence.qtieditor.elements.Mapping;
import org.tencompetence.qtieditor.elements.SimpleChoice;
import org.tencompetence.qtieditor.elements.Value;

public class EditOrderInteractionBlockPanel extends AbstractQuestionEditPanel {

	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	
	private Text fPromptText;
	private Button fNewChoiceButton;
	private Button fCorrectOrderButton;
	private Button fShuffleButton;

	private Composite fEditChoicesPanel = null;

	private Label[] fChoiceLabel = null;
	private Text[] fChoiceText = null;
	private Button[] fRemoveButton = null;
	private Composite[] moveButtonComposite = null;
	private Button[] fUpButton = null;
	private Button[] fDownButton = null;

	private int fChoiceNumber = 0;
	private int fMaxChoiceNumber;

	private OrderInteraction fOrderInteraction;
	//private EditOrderInteractionPanel fEditOrderInteractionPanel;
	private String fQuestionClass;
	
	public EditOrderInteractionBlockPanel(Composite parent,
			AssessmentItem aAssessmentItem,
			OrderInteraction aOrderInteraction, 
			AssessmentTestEditor editor) {

		super(parent, editor);
		fOrderInteraction = aOrderInteraction;
		fAssessmentItem = aAssessmentItem;
		init();
		AssessmentTestEditor.changeToContentColour(this);
	}

	public void init() {
		
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayout(gl);
		
		createQuestionTitlePanel(this, toolkit, "Order Question");
		createQuestionTextPanel();
		createControlPanel();
		createChoicesPanel();
				    
		setDirty(false);
	}
	
	private void createQuestionTextPanel() {
		Composite promptEditPanel = toolkit.createComposite(this, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;		
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = false;
		promptEditPanel.setLayoutData(gridData);
		
		promptEditPanel.setLayout(new GridLayout());
		fQuestionClass = fOrderInteraction.getAssessmentItem().getItemBody().getQuestionClass();
		Label aLabel = toolkit.createLabel(promptEditPanel, "Question Text", SWT.NONE);
		aLabel.setFont(AssessmentTestEditor.fontBold9);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 140; 
		gd.widthHint = 460; 
		fPromptText = toolkit.createText(promptEditPanel, fOrderInteraction.getPrompt().getData(), SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		fPromptText.setLayoutData(gd);
		fPromptText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fOrderInteraction.getPrompt().setData(fPromptText.getText());
				setDirty(true);
			}
		});		
	}
	
	private void createControlPanel() {
		Composite controlEditPanel =  toolkit.createComposite(this, SWT.NONE);
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 35;
		controlEditPanel.setLayoutData(gridData);
		
		FormLayout fLayout = new FormLayout();
		controlEditPanel.setLayout(fLayout);
		
		Label ansLabel = toolkit.createLabel(controlEditPanel, "Choices ", SWT.NONE);
		ansLabel.setFont(AssessmentTestEditor.fontBold9);
		FormData gd1 = new FormData();
		gd1.left = new FormAttachment(0,3);
		gd1.top = new FormAttachment(0,10);
		ansLabel.setLayoutData(gd1);
		
		fCorrectOrderButton = toolkit.createButton(controlEditPanel, "Correct Order", SWT.PUSH);
		gd1 = new FormData();
		gd1.left = new FormAttachment(100, -400);
		gd1.top = new FormAttachment (ansLabel, 0, SWT.TOP);
		fCorrectOrderButton.setLayoutData(gd1);
		fCorrectOrderButton.setToolTipText("Define the correct order as it is");
		fCorrectOrderButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	InterpretationValue correctOrder = new InterpretationValue(AssessmentElementFactory.CORRECT_RESPONSE);
            	BasicElementList aList = fOrderInteraction.getSimpleChoiceList();
            	for (int i=0; i<fOrderInteraction.getSimpleChoiceList().size(); i++) {
            		correctOrder.addValue(new Value("identifier", ((SimpleChoice)aList.getBasicElementAt(i)).getId()));
            	}
            	fOrderInteraction.getResponseDeclaration().setCorrectResponse(correctOrder);
            	MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
			    mDialog.setMessage("The correct order has been saved already.");
			    mDialog.open();
				setDirty(true);
			}
		});

		fShuffleButton = toolkit.createButton(controlEditPanel, "Shuffle", SWT.CHECK);
		gd1 = new FormData();
		gd1.left = new FormAttachment(100, -200);
		gd1.top = new FormAttachment (ansLabel, 0, SWT.TOP);
		fShuffleButton.setLayoutData(gd1);
		fShuffleButton.setToolTipText("Render the choices in this order or randomly");
		
		if (fOrderInteraction.getShuffle())
			fShuffleButton.setSelection(true);
		fShuffleButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				fOrderInteraction
						.setShuffle(fShuffleButton.getSelection());
				setDirty(true);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				fOrderInteraction
						.setShuffle(fShuffleButton.getSelection());
				setDirty(true);
			}
		});

		
		fNewChoiceButton = toolkit.createButton(controlEditPanel, "Add Choice", SWT.PUSH);
				
		gd1 = new FormData();
		gd1.right = new FormAttachment(100, -5);
		gd1.bottom = new FormAttachment(ansLabel, 3, SWT.BOTTOM);
		fNewChoiceButton.setLayoutData(gd1);
		fNewChoiceButton.setToolTipText("Add a choice");

		fNewChoiceButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				SimpleChoice aSimpleChoice = new SimpleChoice(
						fOrderInteraction.getAssessmentItem(),
						fOrderInteraction);
				fOrderInteraction.addSimpleChoice(aSimpleChoice);
				addChoicePanel(aSimpleChoice);
				if (fChoiceNumber >= fMaxChoiceNumber) {
					fNewChoiceButton.setEnabled(false);
				}
				setDirty(true);
				AssessmentTestEditor.changeToContentColour(fEditChoicesPanel);
				
				fEditChoicesPanel.layout();
				
				layout();
				fTestEditor.updateQuestionPanel();
			}
		});
	}
	
	private void createChoicesPanel () {
		fEditChoicesPanel = toolkit.createComposite(this, SWT.NONE);
	    GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL; 
		gridData.verticalAlignment = GridData.FILL; 
		gridData.grabExcessHorizontalSpace = true; 
		gridData.grabExcessVerticalSpace = false;
		fEditChoicesPanel.setLayoutData(gridData);				
		
		fEditChoicesPanel.setLayout(new GridLayout(4, false));
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		
		Label aLabel = toolkit.createLabel(fEditChoicesPanel, " ", SWT.CENTER);		
		aLabel = toolkit.createLabel(fEditChoicesPanel, " ", SWT.CENTER);		
		aLabel = toolkit.createLabel(fEditChoicesPanel, "Choice Text", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditChoicesPanel, " ", SWT.CENTER);		
		
		fChoiceNumber = 0;
		fMaxChoiceNumber = AbstractQuestionEditPanel.maximum;
		
		fChoiceLabel = new Label[fMaxChoiceNumber];
		fRemoveButton = new Button[fMaxChoiceNumber];
		fChoiceText = new Text[fMaxChoiceNumber];
		
		moveButtonComposite = new Composite[fMaxChoiceNumber]; 
		fUpButton = new Button[fMaxChoiceNumber];
		fDownButton = new Button[fMaxChoiceNumber];
		
		BasicElementList aList = fOrderInteraction.getSimpleChoiceList();
		for (int i = 0; i < aList.size(); i++) {
			addChoicePanel((SimpleChoice) aList.getBasicElementAt(i));
		}

		if (fChoiceNumber == fMaxChoiceNumber) {
			fNewChoiceButton.setEnabled(false);
		}
	}

	private void addChoicePanel(SimpleChoice aSimpleChoice) {
		fChoiceNumber++;
		
		if (fChoiceNumber < 10) {
			fChoiceLabel[fChoiceNumber - 1] = toolkit.createLabel(fEditChoicesPanel, "  " + fChoiceNumber + ":",
					SWT.CENTER);
		} else {
			fChoiceLabel[fChoiceNumber - 1] = toolkit.createLabel(fEditChoicesPanel, fChoiceNumber + ":",
				SWT.CENTER);
		}
		
		moveButtonComposite[fChoiceNumber - 1] = toolkit.createComposite(fEditChoicesPanel, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.verticalSpacing = 0;
		gl.marginHeight = 0;
		moveButtonComposite[fChoiceNumber - 1].setLayout(gl);

		fUpButton[fChoiceNumber - 1] = toolkit.createButton(moveButtonComposite[fChoiceNumber - 1], null, SWT.ARROW | SWT.UP);
		fUpButton[fChoiceNumber - 1].setToolTipText("Move this choice up");
		fUpButton[fChoiceNumber - 1].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fChoiceNumber; i++) {
					if (object == fUpButton[i])
						index = i;
				}
				if (index == 0 ) {
					// on the top
					MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
				    mDialog.setMessage("This choice is on the top already.");
				    mDialog.open();
				}
				else if (index > 0) {
					changeOrderBetween(index, true);
				}				
			}
		});
		fUpButton[fChoiceNumber - 1].pack();

		fDownButton[fChoiceNumber - 1] = toolkit.createButton(moveButtonComposite[fChoiceNumber - 1], null, SWT.ARROW | SWT.DOWN);
		fDownButton[fChoiceNumber - 1].setToolTipText("Move this choice down");
		fDownButton[fChoiceNumber - 1].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fChoiceNumber; i++) {
					if (object == fDownButton[i])
						index = i;
				}
				if (index == fChoiceNumber -1 ) {
					// on the bottom 
					MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
				    mDialog.setMessage("This choice is on the bottom already.");
				    mDialog.open();
				}
				else if ((index >= 0) && (index < fChoiceNumber -1)) {
					changeOrderBetween(index, false);
				}				
			}
		});
		fDownButton[fChoiceNumber - 1].pack();
		
		GridData gd2 = new GridData(SWT.FILL, SWT.NONE, true, false);
		gd2.heightHint = 30;
		gd2.widthHint = 450;
		fChoiceText[fChoiceNumber - 1] = toolkit.createText(fEditChoicesPanel, aSimpleChoice.getData(), SWT.WRAP
				| SWT.BORDER | SWT.V_SCROLL);
		fChoiceText[fChoiceNumber - 1].setLayoutData(gd2);
		fChoiceText[fChoiceNumber - 1].addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fChoiceNumber; i++) {
					if (object == fChoiceText[i])
						index = i;
				}
				if (index > -1) {
					((SimpleChoice) fOrderInteraction.getSimpleChoiceList()
							.getBasicElementAt(index))
							.setData(fChoiceText[index].getText());
					setDirty(true);
					//fCanRemoveChoice = false;
				}
			}
		});
		
		fRemoveButton[fChoiceNumber - 1] = toolkit.createButton(fEditChoicesPanel, null, SWT.PUSH | SWT.CENTER);
		Image anImage = ImageFactory.getImage(ImageFactory.ICON_DELETE);
		if (anImage != null) 
			fRemoveButton[fChoiceNumber - 1].setImage(anImage);
		else 
			fRemoveButton[fChoiceNumber - 1].setText("Delete Choice");

		fRemoveButton[fChoiceNumber - 1].setToolTipText("Delete this choice");
		fRemoveButton[fChoiceNumber - 1]
						.addSelectionListener(new SelectionAdapter() {
				            public void widgetSelected(SelectionEvent e) {
								
								if (!MessageDialog.openQuestion(
						                Display.getDefault().getActiveShell(),
						                "Delete",
						                "Are you sure you want to delete this choice?"))
									return;								
								
								Object object = e.getSource();
								int index = -1;
								for (int i = 0; i < fChoiceNumber; i++) {
									if (object == fRemoveButton[i])
										index = i;
								}
								if (index > -1) {
									removeChoiceAt(index);
									fNewChoiceButton.setEnabled(true);
									setDirty(true);
								}
							}
						});
		fRemoveButton[fChoiceNumber - 1].pack();
	}

	private void changeOrderBetween(int i, boolean isUp) {

		SimpleChoice thisChoice = (SimpleChoice) fOrderInteraction.getSimpleChoiceList().getBasicElementAt(i);
		String thisChoiceText = fChoiceText[i].getText();
		if (isUp) {
			// note: the choice has to be changed before the text exchange
			fOrderInteraction.getSimpleChoiceList().setElementAt(i, fOrderInteraction.getSimpleChoiceList().getBasicElementAt(i-1));
			fOrderInteraction.getSimpleChoiceList().setElementAt(i-1, thisChoice);
		
			fChoiceText[i].setText(fChoiceText[i-1].getText());
			fChoiceText[i-1].setText(thisChoiceText);			
		} else {
			fOrderInteraction.getSimpleChoiceList().setElementAt(i, fOrderInteraction.getSimpleChoiceList().getBasicElementAt(i+1));
			fOrderInteraction.getSimpleChoiceList().setElementAt(i+1, thisChoice);

			fChoiceText[i].setText(fChoiceText[i+1].getText());
			fChoiceText[i+1].setText(thisChoiceText);
		}
	
		fEditChoicesPanel.layout();
		layout();
		fTestEditor.updateQuestionPanel();
		setDirty(true);
	}
	
	private void removeChoiceAt(int i) {

		//handling correct choice
		if (!fQuestionClass.equals(AssessmentElementFactory.LIKERT)) {
			String choiceID = ((SimpleChoice) fOrderInteraction
					.getSimpleChoiceList().getBasicElementAt(i)).getId();
			InterpretationValue correctResponses = fOrderInteraction
					.getResponseDeclaration().getCorrectResponse();
			for (int j = 0; j < correctResponses.size(); j++) {
				// if the removed choice is a correct answer
				if (correctResponses.getValueAt(j).getData().equals(choiceID))
					correctResponses.removeValueAt(j);
			}

			Mapping aMapping = fOrderInteraction.getResponseDeclaration()
					.getMapping();
			aMapping.removeMapEntryByKey(choiceID);
		}
		
		// remove the choice
		fOrderInteraction.getSimpleChoiceList().removeElementAt(i);

		// update the UI
		for (int j = i; j < fChoiceNumber - 1; j++) {
			fChoiceText[j].setText(fChoiceText[j + 1].getText());
		}

		fChoiceLabel[fChoiceNumber - 1].dispose();
		fChoiceText[fChoiceNumber - 1].dispose();
		fRemoveButton[fChoiceNumber - 1].dispose();

		fUpButton[fChoiceNumber - 1].dispose();
		fDownButton[fChoiceNumber - 1].dispose();
		moveButtonComposite[fChoiceNumber - 1].dispose();

		fChoiceNumber--;
		fEditChoicesPanel.layout();
		layout();
		fTestEditor.updateQuestionPanel();
	}

}
