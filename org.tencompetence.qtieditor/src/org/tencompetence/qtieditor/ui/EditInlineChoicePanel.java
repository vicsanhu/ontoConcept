package org.tencompetence.qtieditor.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
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
import org.tencompetence.qtieditor.elements.BasicElementList;
import org.tencompetence.qtieditor.elements.ChoiceInteraction;
import org.tencompetence.qtieditor.elements.InlineChoice;
import org.tencompetence.qtieditor.elements.InlineChoiceInteraction;
import org.tencompetence.qtieditor.elements.SimpleChoice;

public class EditInlineChoicePanel extends AbstractQuestionEditPanel{

	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	private Composite fEditChoicesPanel = null;
	private EditInlineChoiceBlockPanel fEditInlineChoiceBlockPanel;

	private Button fNewChoiceButton;
	//private Button fRemoveChoiceButton;
	private Button fShuffleButton;
	private Label[] fChoiceLabel = null;
	private Text[] fChoiceText = null;
	private Button[] fCorrectButton = null;
	private Button[] fRemoveButton = null;
	private Composite[] moveButtons = null;
	private Button[] fUpButton = null;
	private Button[] fDownButton = null;
	
	private int fChoiceNumber = 0;
	private int fMaxChoiceNumber;
	//private boolean fCanRemoveChoice = false;

	private InlineChoiceInteraction fChoiceInteraction;
	//private InlineChoice fOriginalCorrectChoice;
	
	//used in EditInlineChoiceBlockPanel
	public EditInlineChoicePanel(Composite parent, 
			InlineChoiceInteraction aInlineChoiceInteraction, 
			EditInlineChoiceBlockPanel aEditInlineChoiceBlockPanel, 
			AssessmentTestEditor editor) {
		super(parent, editor);
		fChoiceInteraction = aInlineChoiceInteraction;
		fEditInlineChoiceBlockPanel = aEditInlineChoiceBlockPanel;
		init();
		AssessmentTestEditor.changeToContentColour(this);
	}

	private void init() {
		
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayout(gl);		
		setBackground(AssessmentTestEditor.getContentColour());

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
		
/*
		fShuffleButton = toolkit.createButton(controlEditPanel, "Shuffle", SWT.CHECK);
		gd1 = new FormData();
		gd1.left = new FormAttachment(100, -200);
		gd1.top = new FormAttachment (ansLabel, 0, SWT.TOP);
		fShuffleButton.setLayoutData(gd1);
		
		if (fChoiceInteraction.getShuffle())
			fShuffleButton.setSelection(true);
		fShuffleButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				fChoiceInteraction.setShuffle(fShuffleButton.getSelection());
				fEditInlineChoiceBlockPanel.setDirty(true);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				fChoiceInteraction.setShuffle(fShuffleButton.getSelection());
				fEditInlineChoiceBlockPanel.setDirty(true);
			}
		});
*/		
		fChoiceInteraction.setShuffle(true);
		
		fNewChoiceButton = toolkit.createButton(controlEditPanel, "   Add Choice   ", SWT.PUSH);		
		gd1 = new FormData();
		gd1.right = new FormAttachment(100, -5);
		gd1.bottom = new FormAttachment(ansLabel, 3, SWT.BOTTOM);
		fNewChoiceButton.setLayoutData(gd1);
		fNewChoiceButton.setToolTipText("Add a choice");
		fNewChoiceButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				InlineChoice aInlineChoice= new InlineChoice(fChoiceInteraction.getAssessmentItem());
				fChoiceInteraction.addInlineChoice(aInlineChoice);
				addChoicePanel(aInlineChoice);
				if (fChoiceNumber >= fMaxChoiceNumber) {
					fNewChoiceButton.setEnabled(false);
				}
				fEditInlineChoiceBlockPanel.setDirty(true);
				AssessmentTestEditor.changeToContentColour(fEditChoicesPanel);
				fEditChoicesPanel.layout(true, true);
				fEditInlineChoiceBlockPanel.layout();
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
				
		fEditChoicesPanel.setLayout(new GridLayout(5, false));
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		
		Label aLabel = toolkit.createLabel(fEditChoicesPanel, "   ", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditChoicesPanel, "          ", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditChoicesPanel, "Choice Text", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditChoicesPanel, "Correct", SWT.CENTER);
		aLabel.setLayoutData(gridData);
		aLabel = toolkit.createLabel(fEditChoicesPanel, " ", SWT.CENTER);
				
		fChoiceNumber = 0;
		fMaxChoiceNumber = AbstractQuestionEditPanel.maximum;
		
		fChoiceLabel = new Label[fMaxChoiceNumber];
		fChoiceText = new Text[fMaxChoiceNumber];
		fCorrectButton = new Button[fMaxChoiceNumber];
		fRemoveButton = new Button[fMaxChoiceNumber];
		
		moveButtons = new Composite[fMaxChoiceNumber]; 
		fUpButton = new Button[fMaxChoiceNumber];
		fDownButton = new Button[fMaxChoiceNumber];
		
		BasicElementList aList = fChoiceInteraction.getInlineChoiceList();
		for (int i = 0; i < aList.size(); i++) {
			addChoicePanel((InlineChoice) aList.getBasicElementAt(i));
		}
		
		if (fChoiceNumber >= fMaxChoiceNumber) {
			fNewChoiceButton.setEnabled(false);
		}
	}
	
	private void addChoicePanel(InlineChoice aInlineChoice) {
		fChoiceNumber++;
		
		if (fChoiceNumber < 10) {
			fChoiceLabel[fChoiceNumber - 1] = toolkit.createLabel(fEditChoicesPanel, 
					"  " + fChoiceNumber + ":", SWT.CENTER);
		} else {
			fChoiceLabel[fChoiceNumber - 1] = toolkit.createLabel(fEditChoicesPanel, 
					fChoiceNumber + ":", SWT.CENTER);
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
		
		GridData gd2 = new GridData(GridData.FILL_BOTH);
		gd2.heightHint = 30;
		gd2.widthHint = 450; 
		fChoiceText[fChoiceNumber - 1] = toolkit.createText(fEditChoicesPanel, 
				aInlineChoice.getData(), SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
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
					InlineChoice anInlineChoice = (InlineChoice) fChoiceInteraction.getInlineChoiceList()
							.getBasicElementAt(index);
					
					if (fChoiceInteraction.getCorrectChoice()==anInlineChoice) {
						fEditInlineChoiceBlockPanel.updateInteractionCorrectChoice(fChoiceInteraction, fChoiceText[index].getText());
					}
					anInlineChoice.setData(fChoiceText[index].getText());
					fEditInlineChoiceBlockPanel.setDirty(true);
				}
			}
		});

		fCorrectButton[fChoiceNumber - 1] = toolkit.createButton(fEditChoicesPanel,
					null, SWT.RADIO | SWT.CENTER);
		// if this choice is correct, select it
		if (fChoiceInteraction.getCorrectChoice()==aInlineChoice)
			fCorrectButton[fChoiceNumber - 1].setSelection(true);
		else
			fCorrectButton[fChoiceNumber - 1].setSelection(false);
		
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		fCorrectButton[fChoiceNumber - 1].setLayoutData(gd);

		// if this button is clicked, include/exclude this choice in the correct
		// choice list of the response-declaration
		fCorrectButton[fChoiceNumber - 1].addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fChoiceNumber; i++) {
					if (object == fCorrectButton[i])
						index = i;
				}

				if (index > -1) {
					InlineChoice choice = ((InlineChoice) fChoiceInteraction
							.getInlineChoiceList().getBasicElementAt(index));
					
					if (choice!=fChoiceInteraction.getCorrectChoice()) {
						if (choice.getData().equalsIgnoreCase("")) {
							MessageBox mDialog = new MessageBox(getShell(), SWT.OK); //in wizard, using fItemEditor.getShell()
						    mDialog.setMessage("It is not allowed to define an empty string as a correct choice.");
						    mDialog.open();
						    return;
						}
						else {
							fEditInlineChoiceBlockPanel.updateInteractionCorrectChoice(fChoiceInteraction, choice.getData());
							fChoiceInteraction.getResponseDeclaration().setFirstCorrectResponse(AssessmentElementFactory.IDENTIFIER, choice.getId());
							fEditInlineChoiceBlockPanel.setDirty(true);
						}	
					}
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

		InlineChoice thisChoice = (InlineChoice) fChoiceInteraction
			.getInlineChoiceList().getBasicElementAt(i);
		String thisChoiceText = fChoiceText[i].getText();
		boolean isThisChoiceCorrect = fCorrectButton[i].getSelection();
		if (isUp) {
			// note: the choice has to be changed before the text exchange
			fChoiceInteraction.getInlineChoiceList().setElementAt(i, fChoiceInteraction.getInlineChoiceList().getBasicElementAt(i-1));
			fChoiceInteraction.getInlineChoiceList().setElementAt(i-1, thisChoice);
		
			fChoiceText[i].setText(fChoiceText[i-1].getText());
			fChoiceText[i-1].setText(thisChoiceText);
			
			fCorrectButton[i].setSelection(fCorrectButton[i-1].getSelection());
			fCorrectButton[i-1].setSelection(isThisChoiceCorrect);
			
		} else {
			fChoiceInteraction.getInlineChoiceList().setElementAt(i, fChoiceInteraction.getInlineChoiceList().getBasicElementAt(i+1));
			fChoiceInteraction.getInlineChoiceList().setElementAt(i+1, thisChoice);

			fChoiceText[i].setText(fChoiceText[i+1].getText());
			fChoiceText[i+1].setText(thisChoiceText);
			
			fCorrectButton[i].setSelection(fCorrectButton[i+1].getSelection());
			fCorrectButton[i+1].setSelection(isThisChoiceCorrect);			
		}

		fEditInlineChoiceBlockPanel.setDirty(true);
	}
	
	private void removeChoiceAt(int i) {

		InlineChoice correctChoice = fChoiceInteraction.getCorrectChoice();
		InlineChoice thisChoice = (InlineChoice) fChoiceInteraction
			.getInlineChoiceList().getBasicElementAt(i); 
		if (correctChoice==thisChoice) {
			MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
		    mDialog.setMessage("It is not allowed to remove a correct choice.");
		    mDialog.open();
		    return;
		}
		fChoiceInteraction.getInlineChoiceList().removeElementAt(i);

		if (fCorrectButton[i].getSelection()) {
			fCorrectButton[i].setSelection(false);
		}
		for (int j = i; j < fChoiceNumber - 1; j++) {
			fChoiceText[j].setText(fChoiceText[j + 1].getText());
			if (fCorrectButton[j + 1].getSelection()) {
				fCorrectButton[j].setSelection(true);
				fCorrectButton[j + 1].setSelection(false);
			}			
		}

		fChoiceLabel[fChoiceNumber - 1].dispose();
		fChoiceText[fChoiceNumber - 1].dispose();
		fCorrectButton[fChoiceNumber - 1].dispose();
		fRemoveButton[fChoiceNumber - 1].dispose();
		
		fUpButton[fChoiceNumber - 1].dispose();
		fDownButton[fChoiceNumber - 1].dispose();
		moveButtons[fChoiceNumber - 1].dispose();
		
		fChoiceNumber--;
	}
	
	public void updateCorrectChoice(String choiceString) {
		InlineChoice anInlineChoice = fChoiceInteraction.getCorrectChoice();
		for (int i=0; i<fChoiceInteraction.getInlineChoiceList().size(); i++) {
			if (anInlineChoice==fChoiceInteraction.getInlineChoiceList().getBasicElementAt(i)) {
				fChoiceText[i].setText(choiceString);
			}
		}
	}
	

}
