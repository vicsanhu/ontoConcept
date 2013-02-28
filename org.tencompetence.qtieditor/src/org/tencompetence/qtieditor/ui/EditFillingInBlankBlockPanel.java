package org.tencompetence.qtieditor.ui;

import java.util.HashMap;
import java.util.Map;

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
import org.tencompetence.qtieditor.elements.FillingInBlankBlock;
import org.tencompetence.qtieditor.elements.TextEntryInteraction;

public class EditFillingInBlankBlockPanel extends AbstractQuestionEditPanel {
	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	private Button fCopyButton;
	private Button fCutButton;
	private Button fPasteButton;
	
	private Button fEditButton;
	private Button fCreateBlankButton;
	private Button fDeleteBlankButton;
	
	private Button fResetButton;	
	private boolean isEditingBlanks;
	
	private StyledText styledText;
	private FillingInBlankBlock fFillingInBlankBlock;
	
	private String originalString;
	private StyleRange[] originalStyleRanges;
	
	private Map<StyleRange, TextEntryInteraction> map = new HashMap<StyleRange, TextEntryInteraction>();

	public EditFillingInBlankBlockPanel(Composite parent,
			AssessmentItem aAssessmentItem,
			FillingInBlankBlock aFillingInBlankBlock,
			AssessmentTestEditor editor) {

		super(parent, editor);
		fFillingInBlankBlock = aFillingInBlankBlock;
		//fAssessmentItem = fFillingInBlankBlock.getAssessmentItem();
		fAssessmentItem = aAssessmentItem;
		
		init();
		AssessmentTestEditor.changeToContentColour(this);
	}

	private void init() {
		
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayout(gl);
		
		createQuestionTitlePanel(this, toolkit, "Fill-in-the-blank Question");
		
		Composite fEditPanel = toolkit.createComposite(this, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = false;
		
		fEditPanel.setLayoutData(gridData);
		
		fEditPanel.setLayout(new GridLayout());
		//fEditPanel.setBackground(content_colour);
		Label aLabel = toolkit.createLabel(fEditPanel, "Question Text", SWT.NONE);
		aLabel.setFont(AssessmentTestEditor.fontBold9);
		//aLabel.setBackground(content_colour);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 140; //500
		gd.widthHint = 460; //510
		styledText = new StyledText(fEditPanel, SWT.MULTI | SWT.WRAP
				| SWT.BORDER | SWT.V_SCROLL);
		styledText.setLayoutData(gd);
		
		createButtons(fEditPanel);

		originalString = fFillingInBlankBlock.getData();
		styledText.setText(originalString);
		int size = fFillingInBlankBlock.getInteractionList().size();
		if (size==0) {
			setEditState(false);
		}
		else {
			for (int i = 0; i < size; i++) {
				TextEntryInteraction aTextEntryInteraction = 
					(TextEntryInteraction) fFillingInBlankBlock.getInteractionList().getBasicElementAt(i);
				StyleRange style = new StyleRange(aTextEntryInteraction.getStart(), 
						aTextEntryInteraction.getLength(), null, null, SWT.BOLD);
				map.put(style, aTextEntryInteraction);
				styledText.setStyleRange(style);
			}
			setEditState(true);
			originalStyleRanges = styledText.getStyleRanges();
		}
		
		styledText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {				
				if (isEditingBlanks) {				
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
				if (isEditingBlanks) {
					styledText.setEnabled(false);
					int position = styledText.getCaretOffset();
					
					StyleRange[] styles = styledText.getStyleRanges();
					for (int i = 0; i < styles.length; i++) {
						int start = styles[i].start;
						int length = styles[i].length;
						if ((0 <= (position - start)) && ((position - start) <= length)) {							
							styledText.setSelection(start, start+length);
							fDeleteBlankButton.setEnabled(true);
							fCreateBlankButton.setEnabled(false);
							styledText.setEnabled(true);
							fTestEditor.updateQuestionPanel();
							return;
						}
					}
					if (styledText.getSelectionCount()>0)
						fCreateBlankButton.setEnabled(true);
					else 
						fCreateBlankButton.setEnabled(false);
					fDeleteBlankButton.setEnabled(false);
					styledText.setEnabled(true);
					fTestEditor.updateQuestionPanel();
				}
			}			
		});
		
		setDirty(false);
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
		
		toolkit.createLabel(buttonBar, "       ", SWT.LEFT_TO_RIGHT);
		
		fEditButton = toolkit.createButton(buttonBar, "        Edit        ", SWT.PUSH);
		fEditButton.setEnabled(true);
		fEditButton.setToolTipText("Start to define blanks");
		fEditButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				
				if (!MessageDialog.openQuestion(
		                Display.getDefault().getActiveShell(),
		                "Message",
		                "Are you sure that the text has been finalised?\n\n"+
		                "You can not change the text after you start to define blanks,\n\n"
		                +"unless you reset the definitions of the blanks."))
					return;								

				setEditState(true);
				originalString = styledText.getText();
				originalStyleRanges = styledText.getStyleRanges();
				fFillingInBlankBlock.setData(styledText.getText());
			}
		});
		fEditButton.pack();	
		
		fCreateBlankButton = toolkit.createButton(buttonBar, " Create Blank ", SWT.PUSH);
		fCreateBlankButton.setEnabled(true);
		fCreateBlankButton.setToolTipText("Select a string in the question text and define it as a blank");
		fCreateBlankButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	fCreateBlankButton.setEnabled(false);
				createBlank();
				setDirty(true);
			}
		});
		fCreateBlankButton.pack();	
		
		fDeleteBlankButton = toolkit.createButton(buttonBar, " Delete Blank ", SWT.PUSH);
		fDeleteBlankButton.setEnabled(true);
		fDeleteBlankButton.setToolTipText("Delete a selected blank");
		fDeleteBlankButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	fDeleteBlankButton.setEnabled(false);
				deleteBlank();
				setDirty(true);
			}
		});
		fDeleteBlankButton.pack();	
		
		
		fResetButton = toolkit.createButton(buttonBar, " Reset Blanks ", SWT.PUSH);
		fResetButton.setEnabled(true);
		fResetButton.setToolTipText("Remove all definitions of blanks and edit the question text again");
		fResetButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				
				if (!MessageDialog.openQuestion(
		                Display.getDefault().getActiveShell(),
		                "Message",
		                "After you reset the blanks, the definitions of all blanks will be removed.\n\n" +
		                "Are you sure you want to reset the blanks of this question?"))
					return;								

				reset();
				setDirty(true);
			}
		});
		fResetButton.pack();	
		
	}

	private void createBlank() {

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
						TextEntryInteraction aTextEntryInteraction = new TextEntryInteraction (fAssessmentItem, selectionRange.x, selectionRange.y);
						aTextEntryInteraction.getResponseDeclaration().setFirstCorrectResponse("string", correctAnswer);
						fFillingInBlankBlock.addInteractionAt(i, aTextEntryInteraction);
						map.put(style, aTextEntryInteraction);
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
						TextEntryInteraction aTextEntryInteraction = (TextEntryInteraction) map.get(originalStyleRanges[i]);
						aTextEntryInteraction.setStart(start); 
						aTextEntryInteraction.setLength(length); 
						aTextEntryInteraction.getResponseDeclaration()
								.setFirstCorrectResponse("string", correctAnswer);	
						map.remove(originalStyleRanges[i]);
						map.put(style, aTextEntryInteraction);
						
						for (int j=i+1; j<=i+count; j++) {
							aTextEntryInteraction = (TextEntryInteraction)map.get(originalStyleRanges[j]);
							fAssessmentItem.removeResponseDeclaration(aTextEntryInteraction
									.getResponseDeclaration());
							fFillingInBlankBlock.removeInteraction(aTextEntryInteraction);
							map.remove(originalStyleRanges[j]);
						}
						styledText.setSelection(start, end);
					}
					else { //intersect with one blank
						end = Math.max(start+length, end);
						start = Math.min(start, selectionRange.x);
						length = end - start;
						TextEntryInteraction aTextEntryInteraction = (TextEntryInteraction) map.get(originalStyleRanges[i]);
						map.remove(originalStyleRanges[i]);
						
						StyleRange style = new StyleRange(start, length, null, null, SWT.BOLD);

						styledText.setStyleRange(style);
						
						String correctAnswer = originalString.substring(start, end);
						aTextEntryInteraction.setStart(start); 
						aTextEntryInteraction.setLength(length); 
						aTextEntryInteraction.getResponseDeclaration()
								.setFirstCorrectResponse("string", correctAnswer);
						
						map.put(style, aTextEntryInteraction);
						styledText.setSelection(start, end);
					}
					originalStyleRanges = styledText.getStyleRanges();						
					return;
				}
			}
			
			//create a blank after the last one
			StyleRange style = new StyleRange(selectionRange.x, selectionRange.y, null, null, SWT.BOLD);
			styledText.setStyleRange(style);
			
			String correctAnswer = originalString.substring(selectionRange.x, end);
			TextEntryInteraction aTextEntryInteraction = new TextEntryInteraction (fAssessmentItem, selectionRange.x, selectionRange.y);
			aTextEntryInteraction.getResponseDeclaration().setFirstCorrectResponse("string", correctAnswer);
			fFillingInBlankBlock.addInteraction(aTextEntryInteraction);
			map.put(style, aTextEntryInteraction);
			originalStyleRanges = styledText.getStyleRanges();
		}
	}

	private void deleteBlank() {
		
		styledText.setEnabled(false);
		int position = styledText.getCaretOffset();
		
		StyleRange[] styles = styledText.getStyleRanges();
		for (int i = 0; i < styles.length; i++) {
			int start = styles[i].start;
			int length = styles[i].length;
			if ((0 <= (position - start)) && ((position - start) <= length)) {
				
				TextEntryInteraction aTextEntryInteraction = (TextEntryInteraction)map.get(styles[i]);
				fAssessmentItem.removeResponseDeclaration(aTextEntryInteraction
						.getResponseDeclaration());
				fFillingInBlankBlock.removeInteraction(aTextEntryInteraction);
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
		
		//remove original response declarations
		styledText.setEnabled(false);
		for (int i = 0; i < fFillingInBlankBlock.getInteractionList().size(); i++) {
			TextEntryInteraction aTextEntryInteraction = (TextEntryInteraction) fFillingInBlankBlock
					.getInteractionList().getBasicElementAt(i);
			fAssessmentItem.removeResponseDeclaration(aTextEntryInteraction
					.getResponseDeclaration());
		}
		fFillingInBlankBlock.getInteractionList().clear();
		map.clear();
		
		originalString = styledText.getText();
		styledText.setStyleRange(new StyleRange(0, originalString.length(), null, null,
				SWT.NORMAL));
		
		styledText.setEnabled(true);				
		setEditState(false);
	}

	private void setEditState(Boolean isEditing) {
		if (isEditing) {
			fCopyButton.setEnabled(false);
			fCutButton.setEnabled(false);
			fPasteButton.setEnabled(false);
			fEditButton.setEnabled(false);
			//fBlankButton.setEnabled(true);
			//fRecoverButton.setEnabled(true);
			fResetButton.setEnabled(true);
			isEditingBlanks = true;
		}
		else {
			fCopyButton.setEnabled(true);
			fCutButton.setEnabled(true);
			fPasteButton.setEnabled(true);
			fEditButton.setEnabled(true);
			fCreateBlankButton.setEnabled(false);
			fDeleteBlankButton.setEnabled(false);
			fResetButton.setEnabled(false);
			isEditingBlanks = false;
		}
	}
	
	private void print() {
		StyleRange[] styles = styledText.getStyleRanges();
		for (int i = 0; i < styles.length; i++) {
			System.out.println(styles[i]);
		}
		System.out.println();
	}

}
