package org.tencompetence.qtieditor.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.Gap;
import org.tencompetence.qtieditor.elements.GapText;
import org.tencompetence.qtieditor.elements.InlineChoice;
import org.tencompetence.qtieditor.elements.InlineChoiceBlock;
import org.tencompetence.qtieditor.elements.InlineChoiceInteraction;

public class EditInlineChoiceBlockPanel extends AbstractQuestionEditPanel {
	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	private Button fCopyButton;
	private Button fCutButton;
	private Button fPasteButton;
	
	private Button fEditButton;
	private Button fChoiceButton;
	private Button fDeleteButton;
	//private Button fEditChoiceButton;
	private Button fResetButton;	
	private boolean isEditingChoices;
	
	private Vector fInlineChoiceEditPanelList = new Vector();
	private StackComposite fInlineChoiceEditStackPanel = null;
	private Composite fEmptyEditPanel = null;
	
	private StyledText styledText;
	private InlineChoiceBlock fInlineChoiceBlock;
	
	//when text is changed, the following variables are used for recovery
	private String originalString; //when text is changed, it is used for recovery
	private StyleRange[] originalStyleRanges;
	
	private Map<StyleRange, InlineChoiceInteraction> map = new HashMap<StyleRange, InlineChoiceInteraction>();

	
	public EditInlineChoiceBlockPanel(Composite parent,
			AssessmentItem aAssessmentItem,
			InlineChoiceBlock aInlineChoiceBlock,
			AssessmentTestEditor editor) {

		super(parent, editor);
		fInlineChoiceBlock = aInlineChoiceBlock;
		//fAssessmentItem = fInlineChoiceBlock.getAssessmentItem();
		fAssessmentItem = aAssessmentItem;
		init();
		AssessmentTestEditor.changeToContentColour(this);
	}

	private void init() {
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayout(gl);		
		
		createQuestionTitlePanel(this, toolkit, "Inline-choice Question");
		createQuestionTextPanel();
		
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

		fInlineChoiceEditStackPanel = new StackComposite(anEditPanel, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		fInlineChoiceEditStackPanel.setLayoutData(gd);
		toolkit.adapt(fInlineChoiceEditStackPanel);
		fEmptyEditPanel = toolkit.createComposite(fInlineChoiceEditStackPanel, SWT.NONE);

		originalString = fInlineChoiceBlock.getData();
		styledText.setText(originalString);
		int size = fInlineChoiceBlock.getInteractionList().size();
		if (size==0) {
			setEditState(false);
		}
		else {
			for (int i = 0; i < size; i++) {
				InlineChoiceInteraction aInlineChoiceInteraction = 
					(InlineChoiceInteraction) fInlineChoiceBlock.getInteractionList().getBasicElementAt(i);
				StyleRange style = new StyleRange(aInlineChoiceInteraction.getStart(), 
						aInlineChoiceInteraction.getLength(), null, null, SWT.BOLD);
				map.put(style, aInlineChoiceInteraction);
				styledText.setStyleRange(style);
				EditInlineChoicePanel aPanel = new EditInlineChoicePanel(fInlineChoiceEditStackPanel, aInlineChoiceInteraction, this, fTestEditor);
				fInlineChoiceEditPanelList.add(aPanel);
			}
			setEditState(true);
			originalStyleRanges = styledText.getStyleRanges();
		}
		
		styledText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {				
				if (isEditingChoices) {
					styledText.setText(originalString);
					styledText.setStyleRanges(originalStyleRanges);				
				} else 
					setDirty(true);
			}
		});
		
		styledText.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				super.mouseUp(e);
				if (isEditingChoices) {
					styledText.setEnabled(false);
					int position = styledText.getCaretOffset();
					
					StyleRange[] styles = styledText.getStyleRanges();
					for (int i = 0; i < styles.length; i++) {
						int start = styles[i].start;
						int length = styles[i].length;
						if ((0 <= (position - start)) && ((position - start) <= length)) {							
							styledText.setSelection(start, start+length);
							fDeleteButton.setEnabled(true);
							fChoiceButton.setEnabled(false);
							fInlineChoiceEditStackPanel.setControl((Composite) fInlineChoiceEditPanelList.get(i));
							styledText.setEnabled(true);
							fTestEditor.updateQuestionPanel();
							return;
						}
					}
					fInlineChoiceEditStackPanel.setControl(fEmptyEditPanel);
					if (styledText.getSelectionCount()>0)
						fChoiceButton.setEnabled(true);
					else 
						fChoiceButton.setEnabled(false);
					fDeleteButton.setEnabled(false);
					styledText.setEnabled(true);
					fTestEditor.updateQuestionPanel();
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
				fInlineChoiceBlock.setData(styledText.getText());
				originalStyleRanges = styledText.getStyleRanges();
			}
		});
		fEditButton.pack();	
		
		fChoiceButton = toolkit.createButton(buttonBar, "Create Choice", SWT.PUSH);
		fChoiceButton.setEnabled(true);
		fChoiceButton.setToolTipText("Select a string in the question text and define it as an inline choice");
		fChoiceButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	fChoiceButton.setEnabled(false);
            	createChoice();
				setDirty(true);
			}
		});
		fChoiceButton.pack();	
		
		fDeleteButton = toolkit.createButton(buttonBar, "Delete Choice", SWT.PUSH);
		fDeleteButton.setEnabled(true);
		fDeleteButton.setToolTipText("Delete a selected inline choice");
		fDeleteButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	fDeleteButton.setEnabled(false);
            	remove();
				setDirty(true);
			}
		});
		fDeleteButton.pack();	
/*
		fEditChoiceButton = toolkit.createButton(buttonBar, "   Edit Choice  ", SWT.PUSH);
		fEditChoiceButton.setEnabled(true);
		fEditChoiceButton.setToolTipText("Edit a selected inline choice");
		fEditChoiceButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				editChoices();
			}
		});
		fEditChoiceButton.pack();	
*/
		
		
		fResetButton = toolkit.createButton(buttonBar, "Reset Choices", SWT.PUSH);
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
			}
		});
		fResetButton.pack();	
		
	}

	private void createChoice() {

		if (styledText.getSelectionCount() > 0) {
						
			Point selectionRange = styledText.getSelectionRange();
			int end = selectionRange.x+selectionRange.y;
						
			for (int i = 0; i < originalStyleRanges.length; i++) {
				int start = originalStyleRanges[i].start;
				int length = originalStyleRanges[i].length;
				if (selectionRange.x <= start+length){
					
					if (end < start) {//no intersection
						StyleRange style = new StyleRange(selectionRange.x, selectionRange.y, null, null, SWT.BOLD);
						styledText.setStyleRange(style);
						
						String correctAnswer = originalString.substring(selectionRange.x, end);
						InlineChoiceInteraction aInlineChoiceInteraction = new InlineChoiceInteraction (fAssessmentItem, correctAnswer, selectionRange.x, selectionRange.y);
						fInlineChoiceBlock.addInteractionAt(i, aInlineChoiceInteraction);
						EditInlineChoicePanel aPanel = new EditInlineChoicePanel(fInlineChoiceEditStackPanel, aInlineChoiceInteraction, this, fTestEditor);
						fInlineChoiceEditPanelList.add(i, aPanel);
						fInlineChoiceEditStackPanel.setControl(aPanel);
						map.put(style, aInlineChoiceInteraction);
					}	
					else if ((i+1<originalStyleRanges.length)&&(originalStyleRanges[i+1].start <= end)) { //intersect with multiple blanks
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
						
						String correctAnswer = originalString.substring(start, end);
						InlineChoiceInteraction aInlineChoiceInteraction = (InlineChoiceInteraction) map.get(originalStyleRanges[i]);
						aInlineChoiceInteraction.setStart(start); 
						aInlineChoiceInteraction.setLength(length); 
						InlineChoice anInlineChoice = aInlineChoiceInteraction.getCorrectChoice();
						if (anInlineChoice!=null) 
							anInlineChoice.setData(correctAnswer);
						EditInlineChoicePanel aPanel = (EditInlineChoicePanel) fInlineChoiceEditPanelList.get(i);
						aPanel.updateCorrectChoice(correctAnswer);
						fInlineChoiceEditStackPanel.setControl(aPanel);
						map.remove(originalStyleRanges[i]);
						map.put(style, aInlineChoiceInteraction);
						
						for (int j=i+1; j<=i+count; j++) {
							aInlineChoiceInteraction = (InlineChoiceInteraction)map.get(originalStyleRanges[j]);
							fAssessmentItem.removeResponseDeclaration(aInlineChoiceInteraction
									.getResponseDeclaration());
							fInlineChoiceBlock.removeInteraction(aInlineChoiceInteraction);
							fInlineChoiceEditPanelList.removeElementAt(j);
							map.remove(originalStyleRanges[j]);
						}
						styledText.setSelection(start, end);
					}
					else { 
						end = Math.max(start+length, end);
						start = Math.min(start, selectionRange.x);
						length = end - start;
						InlineChoiceInteraction aInlineChoiceInteraction = (InlineChoiceInteraction) map.get(originalStyleRanges[i]);
						map.remove(originalStyleRanges[i]);
						
						StyleRange style = new StyleRange(start, length, null, null, SWT.BOLD);
						styledText.setStyleRange(style);
						
						String correctAnswer = originalString.substring(start, end);
						aInlineChoiceInteraction.setStart(start); 
						aInlineChoiceInteraction.setLength(length); 
						InlineChoice anInlineChoice = aInlineChoiceInteraction.getCorrectChoice();
						if (anInlineChoice!=null) 
							anInlineChoice.setData(correctAnswer);
						EditInlineChoicePanel aPanel = (EditInlineChoicePanel) fInlineChoiceEditPanelList.get(i);
						aPanel.updateCorrectChoice(correctAnswer);
						fInlineChoiceEditStackPanel.setControl(aPanel);
						map.put(style, aInlineChoiceInteraction);
						styledText.setSelection(start, end);
					}
					originalStyleRanges = styledText.getStyleRanges();	
					fTestEditor.updateQuestionPanel();
					return;
				}
			}
			
			//create a blank after the last one
			StyleRange style = new StyleRange(selectionRange.x, selectionRange.y, null, null, SWT.BOLD);
			styledText.setStyleRange(style);
			
			String correctAnswer = originalString.substring(selectionRange.x, end);
			InlineChoiceInteraction aInlineChoiceInteraction = new InlineChoiceInteraction (fAssessmentItem, correctAnswer, selectionRange.x, selectionRange.y);
			fInlineChoiceBlock.addInteraction(aInlineChoiceInteraction);
			EditInlineChoicePanel aPanel = new EditInlineChoicePanel(fInlineChoiceEditStackPanel, aInlineChoiceInteraction, this, fTestEditor);
			fInlineChoiceEditPanelList.add(aPanel);
			fInlineChoiceEditStackPanel.setControl(aPanel);
			map.put(style, aInlineChoiceInteraction);
			originalStyleRanges = styledText.getStyleRanges();
			fTestEditor.updateQuestionPanel();
		}
	}

	private void remove() {
		
		styledText.setEnabled(false);
		int position = styledText.getCaretOffset();
		
		StyleRange[] styles = styledText.getStyleRanges();
		for (int i = 0; i < styles.length; i++) {
			int start = styles[i].start;
			int length = styles[i].length;
			if ((0 <= (position - start)) && ((position - start) <= length)) {
				
				InlineChoiceInteraction aInlineChoiceInteraction = (InlineChoiceInteraction)map.get(styles[i]);
				fAssessmentItem.removeResponseDeclaration(aInlineChoiceInteraction
						.getResponseDeclaration());
				fInlineChoiceBlock.removeInteraction(aInlineChoiceInteraction);
				fInlineChoiceEditStackPanel.setControl(fEmptyEditPanel);
				fInlineChoiceEditPanelList.removeElementAt(i);
				map.remove(styles[i]);
				
				StyleRange style = new StyleRange(start, length, null, null,
						SWT.NORMAL);
				styledText.setStyleRange(style);
				originalStyleRanges = styledText.getStyleRanges();
				break;
			}
		}
		styledText.setEnabled(true);
		fTestEditor.updateQuestionPanel();
	}

	public void reset() {
		
		//remove original response declarations
		styledText.setEnabled(false);
		for (int i = 0; i < fInlineChoiceBlock.getInteractionList().size(); i++) {
			InlineChoiceInteraction aInlineChoiceInteraction = (InlineChoiceInteraction) fInlineChoiceBlock
					.getInteractionList().getBasicElementAt(i);
			fAssessmentItem.removeResponseDeclaration(aInlineChoiceInteraction
					.getResponseDeclaration());
			
		}
		fInlineChoiceBlock.getInteractionList().clear();
		fInlineChoiceEditPanelList.clear();
		fInlineChoiceEditStackPanel.setControl(fEmptyEditPanel);
		map.clear();
		
		originalString = styledText.getText();
		styledText.setStyleRange(new StyleRange(0, originalString.length(), null, null,
				SWT.NORMAL));
		
		styledText.setEnabled(true);
		setEditState(false);
		fTestEditor.updateQuestionPanel();
	}

	private void setEditState(Boolean isEditing) {
		if (isEditing) {
			fCopyButton.setEnabled(false);
			fCutButton.setEnabled(false);
			fPasteButton.setEnabled(false);
			fEditButton.setEnabled(false);
			//fChoiceButton.setEnabled(true);
			//fDeleteButton.setEnabled(true);
			//fEditChoiceButton.setEnabled(true);
			fResetButton.setEnabled(true);
			isEditingChoices = true;
		}
		else {
			fCopyButton.setEnabled(true);
			fCutButton.setEnabled(true);
			fPasteButton.setEnabled(true);
			fEditButton.setEnabled(true);
			fChoiceButton.setEnabled(false);
			fDeleteButton.setEnabled(false);
			//fEditChoiceButton.setEnabled(false);
			fResetButton.setEnabled(false);
			isEditingChoices = false;
		}
	}
	
	public void updateInteractionCorrectChoice(InlineChoiceInteraction anInlineChoiceInteraction, String newCorrectChoice) {
		
		styledText.setEnabled(false);
		
		String oldCorrectChoice = anInlineChoiceInteraction.getCorrectChoice().getData();
		int difference = newCorrectChoice.length() - oldCorrectChoice.length();
		
		originalString = originalString.substring(0, anInlineChoiceInteraction.getStart()) +
			newCorrectChoice + 
			originalString.substring(anInlineChoiceInteraction.getStart()+anInlineChoiceInteraction.getLength());		
		styledText.setText(originalString);
		fInlineChoiceBlock.setData(originalString);
		
		map.clear();
		
		int size = fInlineChoiceBlock.getInteractionList().size();
		for (int i = 0; i < size; i++) {
			InlineChoiceInteraction anInteraction = (InlineChoiceInteraction)fInlineChoiceBlock.getInteractionList().getBasicElementAt(i);
			//handle all blanks before the changed correct choice
			if (anInteraction!=anInlineChoiceInteraction) {
				StyleRange style = new StyleRange(anInteraction.getStart(), 
						anInteraction.getLength(), null, null, SWT.BOLD);
				map.put(style, anInteraction);
				styledText.setStyleRange(style);
			}
			else {
				//handle the changed correct choice
				anInteraction.setLength(anInteraction.getLength()+difference);
				StyleRange style = new StyleRange(anInteraction.getStart(), 
						anInteraction.getLength(), null, null, SWT.BOLD);
				map.put(style, anInteraction);
				styledText.setStyleRange(style);
				
				//handle all blanks after the changed correct choice
				for (int j=i+1; j< size; j++) {
					anInteraction = (InlineChoiceInteraction)fInlineChoiceBlock.getInteractionList().getBasicElementAt(j);
					anInteraction.setStart(anInteraction.getStart()+difference);
					style = new StyleRange(anInteraction.getStart(), 
							anInteraction.getLength(), null, null, SWT.BOLD);
					map.put(style, anInteraction);
					styledText.setStyleRange(style);
				}
				
				styledText.setEnabled(true);
				originalStyleRanges = styledText.getStyleRanges();
				styledText.redraw();
				return;

			}
		}
	}

}