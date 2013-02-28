package org.tencompetence.qtieditor.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.CCombo;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.qtieditor.elements.BasicElementList;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.HottextInteraction;
import org.tencompetence.qtieditor.elements.Hottext;
import org.tencompetence.qtieditor.elements.InterpretationValue;
import org.tencompetence.qtieditor.elements.SimpleChoice;
import org.tencompetence.qtieditor.elements.Value;
import org.tencompetence.qtieditor.elements.Mapping;
import org.tencompetence.qtieditor.elements.MapEntry;
import org.tencompetence.qtieditor.elements.ResponseDeclaration;

public class EditHottextInteractionBlockPanel extends AbstractQuestionEditPanel {
	
	private static final String CORRECT = "Correct";
	private static final String CORRECT_SCORE = "Correct   Score   ";
	
	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	private Button fCopyButton;
	private Button fCutButton;
	private Button fPasteButton;
	
	private CCombo fMaxChoiceCombo;
	private Button fEditButton;
	private Button fCreateButton;
	private Button fDeleteButton;
	//private Button fEditChoiceButton;
	private Button fResetButton;	
	private boolean isEditingStatus;
	
	private StyledText styledText;
	private HottextInteraction fHottextInteraction;
	
	private Composite fEditHottextPanel = null;
	private Label fCorrectScoreLabel = null;
	private Label[] fHottextLabel = null;
	private Text[] fHottextText = null;
	private StackComposite[] fCorrectStackPanel = null;
	private Composite[] fCorrectPanel = null;
	private Composite[] fCorrectScorePanel = null;
	private Button[] fCorrectRadioButton = null;
	private Button[] fCorrectCheckButton = null;
	private Text[] fMappingText = null;

	private int fHottextNumber = 0;
	private int fMaxHottextNumber;

	//when text is changed, the following variables are used for recovery
	private String originalString; //when text is changed, it is used for recovery
	private StyleRange[] originalStyleRanges;
	
	private Map<StyleRange, Hottext> map = new HashMap<StyleRange, Hottext>();

	
	public EditHottextInteractionBlockPanel(Composite parent,
			AssessmentItem aAssessmentItem,
			HottextInteraction aHottextInteraction,
			AssessmentTestEditor editor) {

		super(parent, editor);
		fHottextInteraction = aHottextInteraction;
		//fAssessmentItem = fHottextInteraction.getAssessmentItem();
		fAssessmentItem = aAssessmentItem;
		init();
	}

	private void init() {
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayout(gl);		
		
		createQuestionTitlePanel(this, toolkit, "Hot Text Question");
		createQuestionTextPanel();
		createHottextTextPanel();
		
		AssessmentTestEditor.changeToContentColour(this);
		setDirty(false);
	}
	
	private void createQuestionTextPanel() {
		Composite anEditPanel = toolkit.createComposite(this, SWT.NONE);
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = false;
		
		anEditPanel.setLayoutData(gridData);		
		anEditPanel.setLayout(new GridLayout());
		
		Label aLabel = toolkit.createLabel(anEditPanel, "Question Text", SWT.NONE);
		aLabel.setFont(AssessmentTestEditor.fontBold9);
		
		styledText = new StyledText(anEditPanel, SWT.MULTI | SWT.WRAP
				| SWT.BORDER | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 140;
		gd.widthHint = 460;
		styledText.setLayoutData(gd);
		
		createButtons(anEditPanel);

		originalString = fHottextInteraction.getData();
		styledText.setText(originalString);
		int size = fHottextInteraction.getHottextList().size();
		if (size==0) {
			setEditState(false);
		}
		else {
			for (int i = 0; i < size; i++) {
				Hottext aHottext = (Hottext) fHottextInteraction.getHottextList().getBasicElementAt(i);
				StyleRange style = new StyleRange(aHottext.getStart(), 
						aHottext.getLength(), null, null, SWT.BOLD);
				map.put(style, aHottext);
				styledText.setStyleRange(style);
			}
			setEditState(true);
			originalStyleRanges = styledText.getStyleRanges();
		}
		
		styledText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {				
				if (isEditingStatus) {
					styledText.setText(originalString);
					styledText.setStyleRanges(originalStyleRanges);				
				} else {
					setDirty(true);
				}
			}
		});
		
		styledText.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				super.mouseUp(e);
				if (isEditingStatus) {
					int position = styledText.getCaretOffset();
					
					StyleRange[] styles = styledText.getStyleRanges();
					for (int i = 0; i < styles.length; i++) {
						int start = styles[i].start;
						int length = styles[i].length;
						if ((0 <= (position - start)) && ((position - start) <= length)) {							
							styledText.setSelection(start, start+length);
							fDeleteButton.setEnabled(true);
							fCreateButton.setEnabled(false);
							updateUserInterface();
							return;
						}
					}
					if (styledText.getSelectionCount()>0)
						fCreateButton.setEnabled(true);
					else 
						fCreateButton.setEnabled(false);
					fDeleteButton.setEnabled(false);
					updateUserInterface();
				}
			}			
		});
	}
	
	private void createButtons(Composite editPanel){
		Composite buttonBar = toolkit.createComposite(editPanel, SWT.NONE);
		buttonBar.setLayout(new RowLayout(SWT.HORIZONTAL));
				
		fCopyButton = toolkit.createButton(buttonBar, "       Copy      ", SWT.PUSH);
		fCopyButton.setEnabled(true);
		fCopyButton.setToolTipText("Copy");
		fCopyButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				styledText.copy();
			}
		});
		fCopyButton.pack();
		
		fCutButton = toolkit.createButton(buttonBar, "        Cut        ", SWT.PUSH);
		fCutButton.setEnabled(true);
		fCutButton.setToolTipText("Cut");
		fCutButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				styledText.cut();
				setDirty(true);
			}
		});
		fCutButton.pack();
		
		fPasteButton = toolkit.createButton(buttonBar, "      Paste      ", SWT.PUSH);
		fPasteButton.setEnabled(true);
		fPasteButton.setToolTipText("Paste");
		fPasteButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				styledText.paste();
				setDirty(true);
			}
		});
		fPasteButton.pack();	
		
		Label label = toolkit.createLabel(buttonBar, "       ", SWT.LEFT_TO_RIGHT);

		fEditButton = toolkit.createButton(buttonBar, "        Edit        ", SWT.PUSH);
		fEditButton.setEnabled(true);
		fEditButton.setToolTipText("Start to define inline choices");
		fEditButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				
				if (!MessageDialog.openQuestion(
		                Display.getDefault().getActiveShell(),
		                "Message",
		                "Are you sure that the text has been finalised?\n\n" +
		                "You can not change the text after you start to define inline choices,\n\n" +
		                "unless you reset the definitions of the inline choices."))
					return;								

				setEditState(true);
				originalString = styledText.getText();
				fHottextInteraction.setData(styledText.getText());
				originalStyleRanges = styledText.getStyleRanges();
			}
		});
		fEditButton.pack();	
		
		fCreateButton = toolkit.createButton(buttonBar, " Create Hot Text ", SWT.PUSH);
		fCreateButton.setEnabled(true);
		fCreateButton.setToolTipText("Select a string in the question text and define it as a gap");
		fCreateButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	fCreateButton.setEnabled(false);
            	createHottext();
            	AssessmentTestEditor.changeToContentColour(fEditHottextPanel);	
				setDirty(true);
				updateUserInterface();
			}
		});
		fCreateButton.pack();	
		
		fDeleteButton = toolkit.createButton(buttonBar, " Delete Hot Text ", SWT.PUSH);
		fDeleteButton.setEnabled(true);
		fDeleteButton.setToolTipText("Delete a selected inline choice");
		fDeleteButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	fDeleteButton.setEnabled(false);
				removeHottext();
				setDirty(true);
				updateUserInterface();
			}
		});
		fDeleteButton.pack();	
		
		fResetButton = toolkit.createButton(buttonBar, "      Reset     ", SWT.PUSH);
		fResetButton.setEnabled(true);
		fResetButton.setToolTipText("Remove all definitions of inline choices and edit the question text again");
		fResetButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				
				if (!MessageDialog.openQuestion(
		                Display.getDefault().getActiveShell(),
		                "Message",
		                "After you reset the choices, the definitions of all choices will be removed.\n\n" +
		                "Are you sure you want to reset the choices of this question?"))
					return;								

				reset();
				setDirty(true);
				updateUserInterface();
			}
		});
		fResetButton.pack();			
	}
	
	private void createHottext() {

		if (styledText.getSelectionCount() > 0) { //these is at minimum one gap defined
						
			Point selectionRange = styledText.getSelectionRange();
			int end = selectionRange.x+selectionRange.y;
						
			for (int i = 0; i < originalStyleRanges.length; i++) {
				int start = originalStyleRanges[i].start;
				int length = originalStyleRanges[i].length;
				if (selectionRange.x <= start+length){
					
					if (end < start) {//selection is in between two gaps, no overlap
						StyleRange style = new StyleRange(selectionRange.x, selectionRange.y, null, null, SWT.BOLD);
						styledText.setStyleRange(style);
						
						String correctAnswer = originalString.substring(selectionRange.x, end);
						Hottext aHottext = new Hottext (fAssessmentItem, fHottextInteraction, correctAnswer, selectionRange.x, selectionRange.y);
						fHottextInteraction.addHottextAt(i, aHottext);
						addHottextPanelAt(i, aHottext);
						map.put(style, aHottext);
					}	
					else if ((i+1<originalStyleRanges.length)&&(originalStyleRanges[i+1].start <= end)) { //cover multiple gaps, handled as the first one
						int count = 1;
						for (int j=i+2; j < originalStyleRanges.length; j++) {
							if (originalStyleRanges[j].start <= end) {
								count++;
							}
						}						
						end = Math.max(originalStyleRanges[i+count].start+originalStyleRanges[i+count].length, end);
						start = Math.min(start, selectionRange.x);
						length = end - start;
						StyleRange style = new StyleRange(start, length, null, null, SWT.BOLD);
						styledText.setStyleRange(style);
						Hottext aHottext = new Hottext (fAssessmentItem, fHottextInteraction, 
								originalString.substring(start, end), 
								start, length);
						fHottextInteraction.setHottextAt(i, aHottext);
						updateHottextPanelAt(i, aHottext);
						map.remove(originalStyleRanges[i]);
						map.put(style, aHottext);
						
						for (int j=i+1; j<=i+count; j++) {
							aHottext = (Hottext)map.get(originalStyleRanges[j]);
							fHottextInteraction.removeHottext(aHottext);
							removeHottextPanelAt(j, aHottext);
							map.remove(originalStyleRanges[j]);
						}
						styledText.setSelection(start, end);
					}
					else { //the selection and a gap overlap
						end = Math.max(start+length, end);
						start = Math.min(start, selectionRange.x);
						length = end - start;						
						
						StyleRange style = new StyleRange(start, length, null, null, SWT.BOLD);
						styledText.setStyleRange(style);
						
						Hottext aHottext = new Hottext (fAssessmentItem, fHottextInteraction, 
								originalString.substring(start, end), 
								start, length);
						fHottextInteraction.setHottextAt(i, aHottext);
						updateHottextPanelAt(i, aHottext);
						map.remove(originalStyleRanges[i]);
						map.put(style, aHottext);
						styledText.setSelection(start, end);
					}
					originalStyleRanges = styledText.getStyleRanges();						
					return;
				}
			}
			
			//create a blank after the last gap
			StyleRange style = new StyleRange(selectionRange.x, selectionRange.y, null, null, SWT.BOLD);
			styledText.setStyleRange(style);
			
			String correctAnswer = originalString.substring(selectionRange.x, end);
			Hottext aHottext = new Hottext (fAssessmentItem, fHottextInteraction, correctAnswer, selectionRange.x, selectionRange.y);
			fHottextInteraction.addHottext(aHottext);
			addHottextPanelAt(fHottextNumber, aHottext);
			map.put(style, aHottext);	
			originalStyleRanges = styledText.getStyleRanges();
		}
	}

	private void updateUserInterface() {
		fEditHottextPanel.layout();
		layout();
		fTestEditor.updateQuestionPanel();							
	}

	private void removeHottext() {
		
		boolean result = MessageDialog.openQuestion(
                Display.getDefault().getActiveShell(),
                "Delete", "Are you sure you want to delete this gap?");
        if (!result) 
        	return;
		
		styledText.setEnabled(false);
		int position = styledText.getCaretOffset();
		
		StyleRange[] styles = styledText.getStyleRanges();
		for (int i = 0; i < styles.length; i++) {
			int start = styles[i].start;
			int length = styles[i].length;
			if ((0 <= (position - start)) && ((position - start) <= length)) {
				
				Hottext aHottext = (Hottext)map.get(styles[i]);				
				fHottextInteraction.removeHottext(aHottext);
				removeHottextPanelAt(i, aHottext);
				map.remove(styles[i]);				
				StyleRange style = new StyleRange(start, length, null, null,
						SWT.NORMAL);
				styledText.setStyleRange(style);
				originalStyleRanges = styledText.getStyleRanges();
				break;
			}
		}
		styledText.setEnabled(true);
	}
	
	public void reset() {
		
		fHottextInteraction.getHottextList().clear();
		removeAllHottextPanel();
		map.clear();		
		originalString = styledText.getText();
		styledText.setStyleRange(new StyleRange(0, originalString.length(), null, null,
				SWT.NORMAL));		
		setEditState(false);		
	}

	private void setEditState(Boolean isEditing) {
		if (isEditing) {
			fCopyButton.setEnabled(false);
			fCutButton.setEnabled(false);
			fPasteButton.setEnabled(false);
			fEditButton.setEnabled(false);
			fCreateButton.setEnabled(false);
			fDeleteButton.setEnabled(false);
			fResetButton.setEnabled(true);
			isEditingStatus = true;
		}
		else {
			fCopyButton.setEnabled(true);
			fCutButton.setEnabled(true);
			fPasteButton.setEnabled(true);
			fEditButton.setEnabled(true);
			fCreateButton.setEnabled(false);
			fDeleteButton.setEnabled(false);
			fResetButton.setEnabled(false);
			isEditingStatus = false;
		}
	}
	
	public void modifyHottextString(Hottext updatedHottext, String newString) {
		
		String oldHottextString = updatedHottext.getData();
		int difference = newString.length() - oldHottextString.length();
		
		originalString = originalString.substring(0, updatedHottext.getStart()) +
			newString + 
			originalString.substring(updatedHottext.getStart()+updatedHottext.getLength());		
		styledText.setText(originalString);
				
		map.clear();
		
		int size = fHottextInteraction.getHottextList().size();
		for (int i = 0; i < size; i++) {
			Hottext aHottext = (Hottext)fHottextInteraction.getHottextList().getBasicElementAt(i);
			//handle all blanks before the changed correct choice
			if (aHottext!=updatedHottext) {
				StyleRange style = new StyleRange(aHottext.getStart(), 
						aHottext.getLength(), null, null, SWT.BOLD);
				map.put(style, aHottext);
				styledText.setStyleRange(style);
			}
			else {
				//handle the changed correct choice
				aHottext.setLength(aHottext.getLength()+difference);
				aHottext.setData(newString);
				StyleRange style = new StyleRange(aHottext.getStart(), 
						aHottext.getLength(), null, null, SWT.BOLD);
				map.put(style, aHottext);
				styledText.setStyleRange(style);
				
				//handle all Hottext texts after the changed Hottext
				for (int j=i+1; j< size; j++) {
					aHottext = (Hottext)fHottextInteraction.getHottextList().getBasicElementAt(j);
					aHottext.setStart(aHottext.getStart()+difference);
					style = new StyleRange(aHottext.getStart(), 
							aHottext.getLength(), null, null, SWT.BOLD);
					map.put(style, aHottext);
					styledText.setStyleRange(style);
				}
				originalStyleRanges = styledText.getStyleRanges();	
				styledText.redraw();
				return;
			}
		}
	}
	


	private void createHottextTextPanel( ){
		Composite controlEditPanel =  toolkit.createComposite(this, SWT.NONE);
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 35;
		controlEditPanel.setLayoutData(gridData);
		
		FormLayout fLayout = new FormLayout();
		controlEditPanel.setLayout(fLayout);
		
		Label ansLabel = toolkit.createLabel(controlEditPanel, "Hot Text List ", SWT.NONE);
		ansLabel.setFont(AssessmentTestEditor.fontBold9);
		FormData gd1 = new FormData();
		gd1.left = new FormAttachment(0,3);
		gd1.top = new FormAttachment(0,10);
		ansLabel.setLayoutData(gd1);
		
		Label aLabel = toolkit.createLabel(controlEditPanel, "Max Correct Choices: ", SWT.NONE);
		aLabel.setFont(AssessmentTestEditor.fontBold9);
		gd1 = new FormData();
		gd1.left = new FormAttachment(100, -250);
		gd1.top = new FormAttachment (ansLabel, 0, SWT.TOP);
		aLabel.setLayoutData(gd1);
		
		String[] choices = { "no restriction", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		fMaxChoiceCombo = new CCombo(controlEditPanel, SWT.READ_ONLY | SWT.FLAT | SWT.BORDER );
		fMaxChoiceCombo.setItems(choices);
		gd1 = new FormData();
		gd1.right = new FormAttachment(100, -5);
		gd1.bottom = new FormAttachment(ansLabel, 3, SWT.BOTTOM);
		fMaxChoiceCombo.setLayoutData(gd1);
		fMaxChoiceCombo.select(fHottextInteraction.getMaxChoices());
		fMaxChoiceCombo.setToolTipText("Please define the maximum number of correct choices.");
		toolkit.adapt(fMaxChoiceCombo);
		fMaxChoiceCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int oldNumber = fHottextInteraction.getMaxChoices();
				int newNumber = fMaxChoiceCombo.getSelectionIndex();
				
				if (oldNumber==newNumber)
					return;
				
				ResponseDeclaration aResponseDeclaration = fHottextInteraction.getResponseDeclaration();
				fHottextInteraction.setMaxChoices(newNumber);
				if (oldNumber==1) { 
					for (int index=0; index<fHottextNumber; index++) {
						fCorrectStackPanel[index].setControl(fCorrectScorePanel[index]);
						fCorrectCheckButton[index].setSelection(fCorrectRadioButton[index].getSelection());
						fMappingText[index].setText("");
						fCorrectStackPanel[index].layout();
					}
					fCorrectScoreLabel.setText(CORRECT_SCORE);
					aResponseDeclaration.setCardinality(AssessmentElementFactory.MULTIPLE);
					aResponseDeclaration.getAssociatedOutcomeDeclaration().setBaseType(AssessmentElementFactory.FLOAT);
				} else if (newNumber==1) {
					for (int index=0; index<fHottextNumber; index++) {
						fCorrectStackPanel[index].setControl(fCorrectPanel[index]);
						fCorrectRadioButton[index].setSelection(false);
						fCorrectStackPanel[index].layout();
					}
					fCorrectScoreLabel.setText(CORRECT);
					aResponseDeclaration.setCardinality(AssessmentElementFactory.SINGLE);
					aResponseDeclaration.getAssociatedOutcomeDeclaration().setBaseType(AssessmentElementFactory.INTEGER);
					aResponseDeclaration.getCorrectResponse().clear();
					aResponseDeclaration.getMapping().clear();
				} else {
					//still multiple choices
					return;
				}
				setDirty(true);
				updateUserInterface();
			}
		});
			
	    fEditHottextPanel = toolkit.createComposite(this, SWT.NONE);
	    gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL; 
		gridData.verticalAlignment = GridData.FILL; 
		gridData.grabExcessHorizontalSpace = true; 
		gridData.grabExcessVerticalSpace = false;
		fEditHottextPanel.setLayoutData(gridData);
						
		fEditHottextPanel.setLayout(new GridLayout(3, false));
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		
		aLabel = toolkit.createLabel(fEditHottextPanel, " ", SWT.CENTER);		
		aLabel = toolkit.createLabel(fEditHottextPanel, "Hot Text", SWT.CENTER);
		if (fHottextInteraction.getMaxChoices()==1) {
			fCorrectScoreLabel = toolkit.createLabel(fEditHottextPanel, CORRECT, SWT.CENTER);
			fCorrectScoreLabel.setLayoutData(gridData);
		}
		else 
			fCorrectScoreLabel = toolkit.createLabel(fEditHottextPanel, CORRECT_SCORE, SWT.CENTER);	
			
		
		fHottextNumber = 0;
		fMaxHottextNumber = AbstractQuestionEditPanel.maximum;
		
		fHottextLabel = new Label[fMaxHottextNumber];
		fHottextText = new Text[fMaxHottextNumber];
		fCorrectStackPanel = new StackComposite[fMaxHottextNumber];
		fCorrectPanel = new Composite[fMaxHottextNumber];
		fCorrectScorePanel = new Composite[fMaxHottextNumber];
		fCorrectRadioButton = new Button[fMaxHottextNumber];
		fCorrectCheckButton = new Button[fMaxHottextNumber];
		fMappingText = new Text[fMaxHottextNumber];
		
		BasicElementList aList = fHottextInteraction.getHottextList();
		for (int i = 0; i < aList.size(); i++) {
			addHottextPanelAt(i, (Hottext) aList.getBasicElementAt(i));
		}
		
		if (fHottextNumber >= fMaxHottextNumber) {
			fCreateButton.setEnabled(false);
		}

	}
	
	private void updateHottextPanelAt(int position, Hottext aHottext) {
		fHottextText[position].setText(aHottext.getData());
		InterpretationValue correctResponses = fHottextInteraction.getResponseDeclaration().getCorrectResponse();
		fCorrectRadioButton[position].setSelection(false);
		for (int i = 0; i < correctResponses.size(); i++) {
			if (correctResponses.getValueAt(i).getData().equals(aHottext.getId()))
				fCorrectRadioButton[position].setSelection(true);
		}
	}

	private void addHottextPanelAt(int position, Hottext aHottext) {
		fHottextNumber++;
		
		if (fHottextNumber < 10) {
			fHottextLabel[fHottextNumber-1] = toolkit.createLabel(fEditHottextPanel, "  " + fHottextNumber + ":", SWT.CENTER);
		} else {
			fHottextLabel[fHottextNumber-1] = toolkit.createLabel(fEditHottextPanel, fHottextNumber + ":", SWT.CENTER);
		}
		
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd2.heightHint = 30;
		gd2.widthHint = 450;
		fHottextText[fHottextNumber-1] = toolkit.createText(fEditHottextPanel, aHottext.getData(), SWT.WRAP
				| SWT.BORDER | SWT.V_SCROLL);
		fHottextText[fHottextNumber-1].setLayoutData(gd2);
		fHottextText[fHottextNumber-1].addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fHottextNumber; i++) {
					if (object == fHottextText[i])
						index = i;
				}
				if (index > -1) {
					Hottext updatedHottext = (Hottext)fHottextInteraction.getHottextList().getBasicElementAt(index);
					modifyHottextString(updatedHottext, fHottextText[index].getText());
					setDirty(true);
				}
			}
		});
		
		fCorrectStackPanel[fHottextNumber-1] = new StackComposite(fEditHottextPanel, SWT.NONE);
		AssessmentTestEditor.changeToContentColour(fCorrectStackPanel[fHottextNumber-1]);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		fCorrectStackPanel[fHottextNumber-1].setLayoutData(gd);
		toolkit.adapt(fCorrectStackPanel[fHottextNumber-1]);

		fCorrectScorePanel[fHottextNumber-1] = toolkit.createComposite(fCorrectStackPanel[fHottextNumber-1], SWT.NONE);		
		GridData gridData = new GridData();
		fCorrectScorePanel[fHottextNumber-1].setLayoutData(gridData);						
		fCorrectScorePanel[fHottextNumber-1].setLayout(new GridLayout(2, false));
		
		fCorrectCheckButton[fHottextNumber-1] = toolkit.createButton(fCorrectScorePanel[fHottextNumber-1], null, SWT.CHECK | SWT.CENTER);	
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		gd.verticalAlignment = GridData.CENTER;
		fCorrectCheckButton[fHottextNumber-1].setLayoutData(gd);
		InterpretationValue correctResponses = fHottextInteraction.getResponseDeclaration().getCorrectResponse();
		fCorrectCheckButton[fHottextNumber-1].setSelection(correctResponses.isIncluded(aHottext.getId()));
		fCorrectCheckButton[fHottextNumber-1].setText("   ");
		fCorrectCheckButton[fHottextNumber-1].addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fHottextNumber; i++) {
					if (object == fCorrectCheckButton[i])
						index = i;
				}
				if (index > -1) {					
					String choiceID = ((Hottext) fHottextInteraction
							.getHottextList()
							.getBasicElementAt(index)).getId();
					Value value = new Value(
							AssessmentElementFactory.IDENTIFIER,
							choiceID);
					InterpretationValue correctResponses = fHottextInteraction
							.getResponseDeclaration()
							.getCorrectResponse();

					if (correctResponses.size() == 0) {
						correctResponses.addValue(value);
						setDirty(true);
					} else {
						int found = -1;
						for (int i = 0; i < correctResponses.size(); i++) {
							if (correctResponses.getValueAt(i).getData().equals(choiceID)) {
								found = i;
							}
						}
						if (found == -1) {
							if (fHottextInteraction.getMaxChoices()==0) {
								correctResponses.addValue(value);
								setDirty(true);
							} else if (correctResponses.size()<fHottextInteraction.getMaxChoices()) {
								correctResponses.addValue(value);								
								setDirty(true);
							} else {
								MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
								mDialog.setText("Warning: inappropriate selection!");
								mDialog.setMessage("The maximum number of the correct choices is '"+fHottextInteraction.getMaxChoices()+ "'. You can not define an additional correct choice!");
								mDialog.open();
							}
						} else {
							correctResponses.removeValueAt(found);
							setDirty(true);
						}
					}
				}
			}
		});
		
		Mapping aMapping = fHottextInteraction.getResponseDeclaration().getMapping();
		fMappingText[fHottextNumber-1] = toolkit.createText(fCorrectScorePanel[fHottextNumber-1], "", SWT.BORDER);

		GridData gd3 = new GridData(SWT.NONE, SWT.NONE, true, false);
		gd3.heightHint = 15;
		gd3.widthHint = 25;		
		gd3.horizontalAlignment = SWT.CENTER;
		gd3.verticalAlignment = SWT.CENTER;
		fMappingText[fHottextNumber-1].setLayoutData(gd3);
		MapEntry aMapEntry = aMapping.getMapEntryByKey(aHottext.getId());
		if (aMapEntry!=null) {
			fMappingText[fHottextNumber-1].setText(aMapEntry.getMappedValueString());
		} else {
			fMappingText[fHottextNumber-1].setText("");
		}
		fMappingText[fHottextNumber-1].addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fHottextNumber; i++) {
					if (object == fMappingText[i])
						index = i;
				}
				if (index > -1) {
					try {
						if (fMappingText[index].getText().equalsIgnoreCase(""))
							return;
						
						double value = Double.valueOf(fMappingText[index].getText()).floatValue();
						String choiceID = ((Hottext) fHottextInteraction.getHottextList().getBasicElementAt(index)).getId();
						Mapping aMapping = fHottextInteraction.getResponseDeclaration().getMapping();
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

		fCorrectPanel[fHottextNumber-1] = toolkit.createComposite(fCorrectStackPanel[fHottextNumber-1], SWT.CENTER);
		/*
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalAlignment = GridData.CENTER;
		fCorrectPanel[fHottextNumber-1].setLayoutData(gridData);						
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 20;
		fCorrectPanel[fHottextNumber-1].setLayout(gl);
		 */

		gridData = new GridData();
		fCorrectPanel[fHottextNumber-1].setLayoutData(gridData);						
		fCorrectPanel[fHottextNumber-1].setLayout(new GridLayout(2, false));

		toolkit.createLabel(fCorrectPanel[fHottextNumber-1], "         ", SWT.CENTER);		
		
		fCorrectRadioButton[fHottextNumber-1] = toolkit.createButton(fCorrectPanel[fHottextNumber-1], null, SWT.RADIO | SWT.CENTER);	
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.horizontalAlignment = GridData.CENTER;
		fCorrectRadioButton[fHottextNumber-1].setLayoutData(gd);
		fCorrectRadioButton[fHottextNumber-1].setSelection(correctResponses.isIncluded(aHottext.getId()));
		fCorrectRadioButton[fHottextNumber-1].addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fHottextNumber; i++) {
					if (object == fCorrectRadioButton[i])
						index = i;
				}

				if (index > -1) {					
					resetCorrectRadioButtons();
					fCorrectRadioButton[index].setSelection(true);
					
					String choiceID = ((Hottext) fHottextInteraction
							.getHottextList()
							.getBasicElementAt(index)).getId();
					Value value = new Value(
							AssessmentElementFactory.IDENTIFIER,
							choiceID);
					InterpretationValue correctResponses = fHottextInteraction
							.getResponseDeclaration()
							.getCorrectResponse();

					if (correctResponses.size() == 0)
						correctResponses.addValue(value);
					else
						correctResponses.setValueAt(0, value);
					setDirty(true);
				}
			}
		});
		
		if (fHottextInteraction.getMaxChoices()==1) {
			fCorrectStackPanel[fHottextNumber-1].setControl(fCorrectPanel[fHottextNumber-1]);
		} else {
			fCorrectStackPanel[fHottextNumber-1].setControl(fCorrectScorePanel[fHottextNumber-1]);
		}
		
		if (position!=(fHottextNumber-1)) {
			String aHottextString = fHottextText[fHottextNumber-1].getText();
			boolean aRadioStatus = fCorrectRadioButton[fHottextNumber-1].getSelection();
			boolean aCheckStatus = fCorrectCheckButton[fHottextNumber-1].getSelection();
			String aMappingTextString = fMappingText[fHottextNumber-1].getText();
			
			for (int j = fHottextNumber - 2; j >= position; j--) {
				fHottextText[j+1].setText(fHottextText[j].getText());
				fCorrectRadioButton[j+1].setSelection(fCorrectRadioButton[j].getSelection());
				fCorrectCheckButton[j+1].setSelection(fCorrectCheckButton[j].getSelection());
				fMappingText[j+1].setText(fMappingText[j].getText());
			}
			fHottextText[position].setText(aHottextString);
			fCorrectRadioButton[position].setSelection(aRadioStatus);
			fCorrectCheckButton[position].setSelection(aCheckStatus);
			fMappingText[position].setText(aMappingTextString);
		} 
	}
	
	
	private void resetCorrectRadioButtons() {
		for (int i=0; i<fHottextNumber; i++) {
			fCorrectRadioButton[i].setSelection(false);			
		}
	}
	
	private void removeAllHottextPanel() {
		
		fHottextInteraction.getResponseDeclaration().getCorrectResponse().clear();
		fHottextInteraction.getResponseDeclaration().getMapping().clear();

		for (int i = 0; i < fHottextNumber; i++) {
			fHottextLabel[i].dispose();
			fHottextText[i].dispose();
			fCorrectStackPanel[i].dispose();
			fCorrectPanel[i].dispose();
			fCorrectScorePanel[i].dispose();
			fCorrectRadioButton[i].dispose();
			fCorrectCheckButton[i].dispose();
			fMappingText[i].dispose();
			fCorrectRadioButton[i].dispose();
		}
		fHottextNumber=0;		
	}

	private void removeHottextPanelAt(int i, Hottext aHottext) {

		fHottextInteraction.getResponseDeclaration().getCorrectResponse().removeValueData(aHottext.getId());
		fHottextInteraction.getResponseDeclaration().getMapping().removeMapEntryByKey(aHottext.getId());

		for (int j = i; j < fHottextNumber - 1; j++) {
			fHottextText[j].setText(fHottextText[j + 1].getText());
			fCorrectRadioButton[j].setSelection(fCorrectRadioButton[j + 1].getSelection());
			fCorrectCheckButton[j].setSelection(fCorrectCheckButton[j + 1].getSelection());
			fMappingText[j].setText(fMappingText[j + 1].getText());
		}
		fHottextLabel[fHottextNumber-1].dispose();
		fHottextText[fHottextNumber-1].dispose();
		fCorrectStackPanel[fHottextNumber-1].dispose();
		fCorrectPanel[fHottextNumber-1].dispose();
		fCorrectScorePanel[fHottextNumber-1].dispose();
		fCorrectRadioButton[fHottextNumber-1].dispose();
		fCorrectCheckButton[fHottextNumber-1].dispose();
		fMappingText[fHottextNumber-1].dispose();
		fCorrectRadioButton[fHottextNumber-1].dispose();
		
		fHottextNumber--;
	}
}

