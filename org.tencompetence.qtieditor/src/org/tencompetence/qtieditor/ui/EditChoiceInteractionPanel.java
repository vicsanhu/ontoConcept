package org.tencompetence.qtieditor.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.tencompetence.qtieditor.elements.ChoiceInteraction;
import org.tencompetence.qtieditor.elements.InterpretationValue;
import org.tencompetence.qtieditor.elements.MapEntry;
import org.tencompetence.qtieditor.elements.Mapping;
import org.tencompetence.qtieditor.elements.SimpleChoice;
import org.tencompetence.qtieditor.elements.Value;

public class EditChoiceInteractionPanel extends AbstractQuestionEditPanel {

	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	
	private Text fPromptText;
	private Button fNewChoiceButton;
	//private Button fRemoveChoiceButton;
	private Button fShuffleButton;

	private Composite fEditChoicesPanel = null;

	private Label[] fChoiceLabel = null;
	private Text[] fChoiceText = null;
	private Button[] fCorrectButton = null;
	private Text[] fMappingText = null;
	private Button[] fFeedbackButton = null;
	private Button[] fRemoveButton = null;
	private Composite[] moveButtons = null;
	private Button[] fUpButton = null;
	private Button[] fDownButton = null;

	private int fChoiceNumber = 0;
	private int fMaxChoiceNumber;

	private ChoiceInteraction fChoiceInteraction;
	//private EditChoiceInteractionPanel fEditChoiceInteractionPanel;
	private String fQuestionClass;
	
	public EditChoiceInteractionPanel(Composite parent, AssessmentItem aAssessmentItem, 
			ChoiceInteraction aChoiceInteraction, AssessmentTestEditor editor) {

		super(parent, editor);
		fChoiceInteraction = aChoiceInteraction;
		fAssessmentItem = aAssessmentItem; 
		init();
		AssessmentTestEditor.changeToContentColour(this);
	}

	public void init() {
		
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayout(gl);
		
		fQuestionClass = fChoiceInteraction.getAssessmentItem().getItemBody().getQuestionClass();
		String questionType = " ";
		if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.TEXT_BASED_MULTIPLE_CHOICE))  {
			questionType = "Multiple-choice";
		} else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.TEXT_BASED_MULTIPLE_RESPONSE))  {
			questionType = "Multiple-response";
		} else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.YES_NO))  {
			questionType = "Yes-no";
		} else if (fQuestionClass.equalsIgnoreCase(AssessmentElementFactory.LIKERT))  {
			questionType = "Likert-scale";
		}
		createQuestionTitlePanel(this, toolkit, questionType+" Question");
		createQuestionTextPanel();
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
		Label aLabel = toolkit.createLabel(promptEditPanel, "Question Text", SWT.NONE);
		aLabel.setFont(AssessmentTestEditor.fontBold9);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 140; 
		gd.widthHint = 460; 
		fPromptText = toolkit.createText(promptEditPanel, fChoiceInteraction.getPrompt().getData(), SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		fPromptText.setLayoutData(gd);
		fPromptText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fChoiceInteraction.getPrompt().setData(fPromptText.getText());
				setDirty(true);
			}
		});
	}
	
	private void createChoicesPanel() {
		Composite controlEditPanel =  toolkit.createComposite(this, SWT.NONE);		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 35;
		controlEditPanel.setLayoutData(gridData);
		
		FormLayout fLayout = new FormLayout();
		controlEditPanel.setLayout(fLayout);
		
		Label ansLabel = toolkit.createLabel(controlEditPanel, "Answer ", SWT.NONE);
		ansLabel.setFont(AssessmentTestEditor.fontBold9);
		FormData gd1 = new FormData();
		gd1.left = new FormAttachment(0,3);
		gd1.top = new FormAttachment(0,10);
		ansLabel.setLayoutData(gd1);
		

		if (!fQuestionClass.equals(AssessmentElementFactory.LIKERT)) {
			fShuffleButton = toolkit.createButton(controlEditPanel, "Shuffle", SWT.CHECK);
			gd1 = new FormData();
			gd1.left = new FormAttachment(100, -200);
			gd1.top = new FormAttachment (ansLabel, 0, SWT.TOP);
			fShuffleButton.setLayoutData(gd1);
			fShuffleButton.setToolTipText("Render the choices in this order or randomly");
			
			if (fChoiceInteraction.getShuffle())
				fShuffleButton.setSelection(true);
			fShuffleButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					fChoiceInteraction
							.setShuffle(fShuffleButton.getSelection());
					setDirty(true);
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					fChoiceInteraction
							.setShuffle(fShuffleButton.getSelection());
					setDirty(true);
				}
			});
		}

		
		fNewChoiceButton = toolkit.createButton(controlEditPanel, "Add Choice", SWT.PUSH);
				
		gd1 = new FormData();
		gd1.right = new FormAttachment(100, -5);
		gd1.bottom = new FormAttachment(ansLabel, 3, SWT.BOTTOM);
		fNewChoiceButton.setLayoutData(gd1);
		fNewChoiceButton.setToolTipText("Add a choice");
		fNewChoiceButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				SimpleChoice aSimpleChoice = new SimpleChoice(
						fChoiceInteraction.getAssessmentItem(),
						fChoiceInteraction);
				fChoiceInteraction.addSimpleChoice(aSimpleChoice);
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
			
	    fEditChoicesPanel = toolkit.createComposite(this, SWT.NONE);
	    gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL; 
		gridData.verticalAlignment = GridData.FILL; 
		gridData.grabExcessHorizontalSpace = true; 
		gridData.grabExcessVerticalSpace = false;
		fEditChoicesPanel.setLayoutData(gridData);
				
		
		if (!fQuestionClass.equals(AssessmentElementFactory.LIKERT)) {
			fEditChoicesPanel.setLayout(new GridLayout(7, false));
		} else {
			fEditChoicesPanel.setLayout(new GridLayout(4, false));
		}
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		
		
		Label aLabel = toolkit.createLabel(fEditChoicesPanel, " ", SWT.CENTER);		
		aLabel = toolkit.createLabel(fEditChoicesPanel, " ", SWT.CENTER);		
		aLabel = toolkit.createLabel(fEditChoicesPanel, "Choice Text", SWT.CENTER);
		
		if (!fQuestionClass.equals(AssessmentElementFactory.LIKERT)) {
			aLabel = toolkit.createLabel(fEditChoicesPanel,"Correct", SWT.CENTER);
			aLabel = toolkit.createLabel(fEditChoicesPanel, "  Score  ", SWT.CENTER);
			aLabel.setLayoutData(gridData);
			aLabel = toolkit.createLabel(fEditChoicesPanel, " ", SWT.CENTER);
		}
		
		aLabel = toolkit.createLabel(fEditChoicesPanel, " ", SWT.CENTER);
		
		fChoiceNumber = 0;
		fMaxChoiceNumber = AbstractQuestionEditPanel.maximum;
		
		fChoiceLabel = new Label[fMaxChoiceNumber];
		fRemoveButton = new Button[fMaxChoiceNumber];
		fChoiceText = new Text[fMaxChoiceNumber];
		fCorrectButton = new Button[fMaxChoiceNumber];
		fMappingText = new Text[fMaxChoiceNumber];
		fFeedbackButton = new Button[fMaxChoiceNumber];
		
		moveButtons = new Composite[fMaxChoiceNumber]; 
		fUpButton = new Button[fMaxChoiceNumber];
		fDownButton = new Button[fMaxChoiceNumber];
		
		BasicElementList aList = fChoiceInteraction.getSimpleChoiceList();
		for (int i = 0; i < aList.size(); i++) {
			addChoicePanel((SimpleChoice) aList.getBasicElementAt(i));
		}

		if (fChoiceNumber >= fMaxChoiceNumber) {
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
		
		moveButtons[fChoiceNumber - 1] = toolkit.createComposite(fEditChoicesPanel, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.verticalSpacing = 0;
		gl.marginHeight = 0;
		moveButtons[fChoiceNumber - 1].setLayout(gl);

		fUpButton[fChoiceNumber - 1] = toolkit.createButton(moveButtons[fChoiceNumber - 1], null, SWT.ARROW | SWT.UP);
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

		fDownButton[fChoiceNumber - 1] = toolkit.createButton(moveButtons[fChoiceNumber - 1], null, SWT.ARROW | SWT.DOWN);
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
		
		//GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false);
		GridData gd2 = new GridData(SWT.FILL, SWT.NONE, true, false);
		gd2.heightHint = 30;
		gd2.widthHint = 400;
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
					((SimpleChoice) fChoiceInteraction.getSimpleChoiceList()
							.getBasicElementAt(index))
							.setData(fChoiceText[index].getText());
					setDirty(true);
					//fCanRemoveChoice = false;
				}
			}
		});

		if (!fQuestionClass.equals(AssessmentElementFactory.LIKERT)) {

			if (fChoiceInteraction.getResponseDeclaration().getCardinality()
					.equals(AssessmentElementFactory.MULTIPLE)) {
				fCorrectButton[fChoiceNumber - 1] = toolkit.createButton(
						fEditChoicesPanel, null, SWT.CHECK);

			} else {
				fCorrectButton[fChoiceNumber - 1] = toolkit.createButton(
						fEditChoicesPanel, null, SWT.RADIO);
			}
			
			GridData gd = new GridData();
			gd.horizontalAlignment = GridData.CENTER;
			fCorrectButton[fChoiceNumber - 1].setLayoutData(gd);
			
			
			// if this choice is correct, select it
			InterpretationValue correctResponses = fChoiceInteraction
					.getResponseDeclaration().getCorrectResponse();
			fCorrectButton[fChoiceNumber - 1].setSelection(false);
			for (int i = 0; i < correctResponses.size(); i++) {
				if (correctResponses.getValueAt(i).getData().equals(
						aSimpleChoice.getId()))
					fCorrectButton[fChoiceNumber - 1].setSelection(true);
			}
			
			// if this button is clicked, include/exclude this choice in the
			// correct choice list of the response-declaration
			fCorrectButton[fChoiceNumber - 1]
					.addSelectionListener(new SelectionAdapter() {
			            public void widgetSelected(SelectionEvent e) {
							Object object = e.getSource();
							int index = -1;
							for (int i = 0; i < fChoiceNumber; i++) {
								if (object == fCorrectButton[i])
									index = i;
							}

							if (index > -1) {
								String choiceID = ((SimpleChoice) fChoiceInteraction
										.getSimpleChoiceList()
										.getBasicElementAt(index)).getId();
								Value value = new Value(
										AssessmentElementFactory.IDENTIFIER,
										choiceID);
								InterpretationValue correctResponses = fChoiceInteraction
										.getResponseDeclaration()
										.getCorrectResponse();

								if (fChoiceInteraction
										.getAssessmentItem()
										.getItemBody()
										.getQuestionClass()
										.equalsIgnoreCase(
												AssessmentElementFactory.TEXT_BASED_MULTIPLE_RESPONSE)) {
									if (correctResponses.size() == 0) {
										correctResponses.addValue(value);
									} else {
										int found = -1;
										for (int i = 0; i < correctResponses.size(); i++) {
											if (correctResponses.getValueAt(i).getData().equals(choiceID)) {
												found = i;
											}
										}
										if (found == -1) {
											correctResponses.addValue(value);
										} else {
											correctResponses.removeValueAt(found);
										}
									}
								} else { // for multiple choice, yes or no,
									if (correctResponses.size() == 0)
										correctResponses.addValue(value);
									else
										correctResponses.setValueAt(0, value);
								}
								setDirty(true);
							}
						}
					});
			fCorrectButton[fChoiceNumber - 1].pack();

			Mapping aMapping = fChoiceInteraction.getResponseDeclaration()
					.getMapping();
			fMappingText[fChoiceNumber - 1] = toolkit.createText(fEditChoicesPanel,
					"", SWT.BORDER);

			GridData gd3 = new GridData();
			gd3.heightHint = 15;
			gd3.widthHint = 25;		
			fMappingText[fChoiceNumber - 1].setLayoutData(gd3);

			for (int i = 0; i < aMapping.size(); i++) {
				MapEntry aMapEntry = aMapping.getMapEntryByKey(aSimpleChoice
						.getId());
				if (aMapEntry != null) {
					fMappingText[fChoiceNumber - 1].setText(aMapEntry
							.getMappedValueString());
					break;
				}
			}

			fMappingText[fChoiceNumber - 1].addModifyListener(new ModifyListener() {
						public void modifyText(ModifyEvent e) {
							Object object = e.getSource();
							int index = -1;
							for (int i = 0; i < fChoiceNumber; i++) {
								if (object == fMappingText[i])
									index = i;
							}
							if (index > -1) {
								// double value = Double.parseDouble(fMappingEntry[index].getText());
								try {
									if (fMappingText[index].getText().equalsIgnoreCase(""))
										return;
									
									double value = Double.valueOf(fMappingText[index].getText()).floatValue();
									String choiceID = ((SimpleChoice) fChoiceInteraction.getSimpleChoiceList().getBasicElementAt(index)).getId();
									Mapping aMapping = fChoiceInteraction.getResponseDeclaration().getMapping();
									aMapping.updateOrAddMapEntry(choiceID, fMappingText[index].getText());
									setDirty(true);
								} catch (Exception event) {
									if ("-".equalsIgnoreCase(fMappingText[index].getText()) || 
											("+".equalsIgnoreCase(fMappingText[index].getText())))
										return;
									MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
									mDialog.setText("Warning: Wrong input!");
									mDialog.setMessage("You input a non-number: '"+fMappingText[index].getText()+ "'. Please input a number!");
									mDialog.open();
									fMappingText[index].setText("");
								}
							}
						}
					});

			fFeedbackButton[fChoiceNumber - 1] = toolkit.createButton(
					fEditChoicesPanel, "Add feedback", SWT.PUSH | SWT.CENTER);
			if (fChoiceNumber == 1) {
				fFeedbackButton[fChoiceNumber - 1].setSelection(true);
			}
			
			if (((SimpleChoice) fChoiceInteraction.getSimpleChoiceList()
					.getBasicElementAt(fChoiceNumber - 1)).getFeedbackInline() == null)
				fFeedbackButton[fChoiceNumber - 1]
						.setToolTipText("The feedback hasn't been defined. Click the button to define the feedback.");
			else
				fFeedbackButton[fChoiceNumber - 1]
						.setToolTipText(((SimpleChoice) fChoiceInteraction
								.getSimpleChoiceList().getBasicElementAt(
										fChoiceNumber - 1)).getFeedbackInline()
								.getData());
			fFeedbackButton[fChoiceNumber - 1]
					.addSelectionListener(new SelectionAdapter() {
			            public void widgetSelected(SelectionEvent e) {
							Object object = e.getSource();
							int index = -1;
							for (int i = 0; i < fChoiceNumber; i++) {
								if (object == fFeedbackButton[i])
									index = i;
							}
							if (index > -1) {
								openChoiceFeedBackDialog(object, index);
							}
						}
					});
			fFeedbackButton[fChoiceNumber - 1].pack();
		}
		
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

		SimpleChoice thisChoice = (SimpleChoice) fChoiceInteraction.getSimpleChoiceList().getBasicElementAt(i);
		String thisChoiceText = fChoiceText[i].getText();
		boolean isThisChoiceCorrect = fCorrectButton[i].getSelection();
		if (isUp) {
			// note: the choice has to be changed before the text exchange
			fChoiceInteraction.getSimpleChoiceList().setElementAt(i, fChoiceInteraction.getSimpleChoiceList().getBasicElementAt(i-1));
			fChoiceInteraction.getSimpleChoiceList().setElementAt(i-1, thisChoice);
		
			fChoiceText[i].setText(fChoiceText[i-1].getText());
			fChoiceText[i-1].setText(thisChoiceText);
			
			if (!fQuestionClass.equals(AssessmentElementFactory.LIKERT)) {
				fCorrectButton[i].setSelection(fCorrectButton[i-1].getSelection());
				fCorrectButton[i-1].setSelection(isThisChoiceCorrect);
				
				String thisMappingText = fMappingText[i].getText();
				fMappingText[i].setText(fMappingText[i-1].getText());
				fMappingText[i-1].setText(thisMappingText);
				
				//the choices have already exchanged
				fFeedbackButton[i].setToolTipText(((SimpleChoice) fChoiceInteraction
						.getSimpleChoiceList().getBasicElementAt(i)).getFeedbackInline().getData());
				fFeedbackButton[i-1].setToolTipText(thisChoice.getFeedbackInline().getData());
			}
			
		} else {
			fChoiceInteraction.getSimpleChoiceList().setElementAt(i, fChoiceInteraction.getSimpleChoiceList().getBasicElementAt(i+1));
			fChoiceInteraction.getSimpleChoiceList().setElementAt(i+1, thisChoice);

			fChoiceText[i].setText(fChoiceText[i+1].getText());
			fChoiceText[i+1].setText(thisChoiceText);
			
			if (!fQuestionClass.equals(AssessmentElementFactory.LIKERT)) {
				fCorrectButton[i].setSelection(fCorrectButton[i+1].getSelection());
				fCorrectButton[i+1].setSelection(isThisChoiceCorrect);		
				
				String thisMappingText = fMappingText[i].getText();
				fMappingText[i].setText(fMappingText[i+1].getText());
				fMappingText[i+1].setText(thisMappingText);
				
				//the choices have already exchanged
				fFeedbackButton[i].setToolTipText(((SimpleChoice) fChoiceInteraction
						.getSimpleChoiceList().getBasicElementAt(i)).getFeedbackInline().getData());
				fFeedbackButton[i+1].setToolTipText(thisChoice.getFeedbackInline().getData());
			}
		}
	
		fEditChoicesPanel.layout();
		layout();
		fTestEditor.updateQuestionPanel();
		setDirty(true);
	}
	
	private void removeChoiceAt(int i) {

		//handling correct choice
		if (!fQuestionClass.equals(AssessmentElementFactory.LIKERT)) {
			String choiceID = ((SimpleChoice) fChoiceInteraction
					.getSimpleChoiceList().getBasicElementAt(i)).getId();
			InterpretationValue correctResponses = fChoiceInteraction
					.getResponseDeclaration().getCorrectResponse();
			for (int j = 0; j < correctResponses.size(); j++) {
				// if the removed choice is a correct answer
				if (correctResponses.getValueAt(j).getData().equals(choiceID))
					correctResponses.removeValueAt(j);
			}

			Mapping aMapping = fChoiceInteraction.getResponseDeclaration()
					.getMapping();
			aMapping.removeMapEntryByKey(choiceID);
			
			if (fCorrectButton[i].getSelection()) {
				fCorrectButton[i].setSelection(false);
			}
		}
		
		// remove the choice
		fChoiceInteraction.getSimpleChoiceList().removeElementAt(i);

		// update the UI
		for (int j = i; j < fChoiceNumber - 1; j++) {
			fChoiceText[j].setText(fChoiceText[j + 1].getText());
			if (!fQuestionClass.equals(AssessmentElementFactory.LIKERT)) {
				fMappingText[j].setText(fMappingText[j + 1].getText());
				if (fCorrectButton[j + 1].getSelection()) {
					fCorrectButton[j].setSelection(true);
					fCorrectButton[j + 1].setSelection(false);
				}
				fFeedbackButton[j].setToolTipText(((SimpleChoice) fChoiceInteraction
					.getSimpleChoiceList().getBasicElementAt(j)).getFeedbackInline().getData());
			}
		}

		fChoiceLabel[fChoiceNumber - 1].dispose();
		fChoiceText[fChoiceNumber - 1].dispose();
		if (!fQuestionClass.equals(AssessmentElementFactory.LIKERT)) {
			fCorrectButton[fChoiceNumber - 1].dispose();
			fMappingText[fChoiceNumber - 1].dispose();
			fFeedbackButton[fChoiceNumber - 1].dispose();
		}
		fRemoveButton[fChoiceNumber - 1].dispose();

		fUpButton[fChoiceNumber - 1].dispose();
		fDownButton[fChoiceNumber - 1].dispose();
		moveButtons[fChoiceNumber - 1].dispose();

		fChoiceNumber--;
		fEditChoicesPanel.layout();
		layout();
		fTestEditor.updateQuestionPanel();
	}

	private void openChoiceFeedBackDialog(Object object, int index) {
		SimpleChoiceFeedbackDialog dialog = new SimpleChoiceFeedbackDialog(
				getShell(),
				(SimpleChoice) fChoiceInteraction
						.getSimpleChoiceList()
						.getBasicElementAt(index),
				(Button) object,
				this);
		dialog.open();
	}

}
