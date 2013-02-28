package org.tencompetence.qtieditor.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.MessageBox;

import org.eclipse.ui.forms.widgets.FormToolkit;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.AssociationExtension;
import org.tencompetence.qtieditor.elements.GapText;
import org.tencompetence.qtieditor.elements.GapMatchInteraction;
import org.tencompetence.qtieditor.elements.Gap;
import org.tencompetence.qtieditor.elements.BasicElementList;
import org.tencompetence.qtieditor.elements.InlineChoice;
import org.tencompetence.qtieditor.elements.InlineChoiceInteraction;
import org.tencompetence.qtieditor.elements.InterpretationValue;
import org.tencompetence.qtieditor.elements.MapEntry;
import org.tencompetence.qtieditor.elements.Mapping;
import org.tencompetence.qtieditor.elements.SimpleAssociableChoice;
import org.tencompetence.qtieditor.elements.Value;
import org.tencompetence.qtieditor.elements.AssociationExtensionList;

public class EditGapMatchInteractionBlockPanel extends AbstractQuestionEditPanel {
	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	private Button fCopyButton;
	private Button fCutButton;
	private Button fPasteButton;
	
	private Button fEditButton;
	private Button fCreateButton;
	private Button fDeleteButton;
	//private Button fEditChoiceButton;
	private Button fResetButton;	
	private boolean isEditingStatus;
	
	private Button fNewGapTextButton;
	private Button fShuffleButton;
	private Composite fEditChoicesPanel = null;
	private Label[] fGapTextLabel = null;
	private Text[] fGapTextText = null;
	private Button[] fSelectButton = null;
	private Button[] fRemoveButton = null;
	private Composite[] fMoveButtonComposite = null;
	private Button[] fUpButton = null;
	private Button[] fDownButton = null;
	private int fGapTextNumber = 0;
	private int fMaxGapTextNumber;
	
	private StyledText styledText;
	private GapMatchInteraction fGapMatchInteraction;
	
	private Composite fEditAssociationsPanel;
	private AssociationExtensionsTableViewer fAssociationTable;	
	private AssociationExtensionList fAssociationExtensionList = new AssociationExtensionList();
	
	//when text is changed, the following variables are used for recovery
	private String originalString; //when text is changed, it is used for recovery
	private StyleRange[] originalStyleRanges;
	
	private Map<StyleRange, Gap> map = new HashMap<StyleRange, Gap>();

	
	public EditGapMatchInteractionBlockPanel(Composite parent,
			AssessmentItem aAssessmentItem,
			GapMatchInteraction aGapMatchInteraction,
			AssessmentTestEditor editor) {

		super(parent, editor);
		fGapMatchInteraction = aGapMatchInteraction;
		//fAssessmentItem = fGapMatchInteraction.getAssessmentItem();
		fAssessmentItem = aAssessmentItem;
		init();
		AssessmentTestEditor.changeToContentColour(this);
	}

	private void init() {
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayout(gl);		
		
		createQuestionTitlePanel(this, toolkit, "Gap-match Question");
		createQuestionTextPanel();
		createAssicationPanel();
		createGapTextPanel();

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

		originalString = fGapMatchInteraction.getData();
		styledText.setText(originalString);
		int size = fGapMatchInteraction.getGapList().size();
		if (size==0) {
			setEditState(false);
		}
		else {
			for (int i = 0; i < size; i++) {
				Gap aGap = (Gap) fGapMatchInteraction.getGapList().getBasicElementAt(i);
				StyleRange style = new StyleRange(aGap.getStart(), 
						aGap.getLength(), null, null, SWT.BOLD);
				map.put(style, aGap);
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
					setDirty(true);
				} else {
					//setDirty(true);
				}
			}
		});
		
		styledText.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				super.mouseUp(e);
				if (isEditingStatus) {
					styledText.setEnabled(false);
					int position = styledText.getCaretOffset();
					
					StyleRange[] styles = styledText.getStyleRanges();
					for (int i = 0; i < styles.length; i++) {
						int start = styles[i].start;
						int length = styles[i].length;
						if ((0 <= (position - start)) && ((position - start) <= length)) {							
							Gap aGap = (Gap)map.get(styles[i]);
							styledText.setSelection(start, start+length);
							styledText.setEnabled(true);
							fDeleteButton.setEnabled(true);
							fCreateButton.setEnabled(false);
							
							for (int j=0; j<fGapMatchInteraction.getGapTextList().size(); j++) {
								GapText aGapText = (GapText)fGapMatchInteraction.getGapTextList().getBasicElementAt(j);
								if ((aGapText==aGap.getMatchedGapText())) {									
									resetSelectButtons();
									fSelectButton[j].setSelection(true);
									
									int index = fAssociationExtensionList.getAssociationIndexBySourceIDTargetID(aGapText.getId(), aGap.getId());
									if (index!= -1) {
										fAssociationTable.getTable().setSelection(index);
										updateUserInterface();
									}	
									return;
								}
							}							
						}
					}
										
					resetSelectButtons();
					if (styledText.getSelectionCount()>0)
						fCreateButton.setEnabled(true);
					else 
						fCreateButton.setEnabled(false);
					fDeleteButton.setEnabled(false);
					styledText.setEnabled(true);
					fAssociationTable.getTable().deselectAll();
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
				fGapMatchInteraction.setData(styledText.getText());
				originalStyleRanges = styledText.getStyleRanges();
			}
		});
		fEditButton.pack();	
		
		fCreateButton = toolkit.createButton(buttonBar, " Create Gap ", SWT.PUSH);
		fCreateButton.setEnabled(true);
		fCreateButton.setToolTipText("Select a string in the question text and define it as a gap");
		fCreateButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	fCreateButton.setEnabled(false);
            	createGap();
				setDirty(true);
				fAssociationTable.setInput(fAssociationExtensionList);
	        	fAssociationTable.refresh();
				updateUserInterface();
			}
		});
		fCreateButton.pack();	
		
		fDeleteButton = toolkit.createButton(buttonBar, " Delete Gap ", SWT.PUSH);
		fDeleteButton.setEnabled(true);
		fDeleteButton.setToolTipText("Delete a selected inline choice");
		fDeleteButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	fDeleteButton.setEnabled(false);
				removeGap();
				setDirty(true);
				fAssociationTable.setInput(fAssociationExtensionList);
	        	fAssociationTable.refresh();
				updateUserInterface();
			}
		});
		fDeleteButton.pack();	
		
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
				updateUserInterface();
			}
		});
		fResetButton.pack();			
	}
	
	
	private void createAssicationPanel() {
		//create association table
		fEditAssociationsPanel = toolkit.createComposite(this, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		fEditAssociationsPanel.setSize(460, 100);
		fEditAssociationsPanel.setLayoutData(gridData);
		
		FormLayout fLayout = new FormLayout();
		fEditAssociationsPanel.setLayout(fLayout);
		
		Label ansLabel = toolkit.createLabel(fEditAssociationsPanel, "Gap Match List", SWT.NONE);
		ansLabel.setFont(AssessmentTestEditor.fontBold9);
		FormData gd1 = new FormData();
		gd1.left = new FormAttachment(0,3);
		gd1.top = new FormAttachment(0,10);
		ansLabel.setLayoutData(gd1);

		
		Composite composite = new Composite(this, SWT.NULL);
        composite.setLayout(new TableColumnLayout());
        gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.heightHint = 100;
		gridData.widthHint = 700;
        composite.setLayoutData(gridData);
        
        fAssociationTable = new AssociationExtensionsTableViewer(composite, fGapMatchInteraction, this);
        
        InterpretationValue aInterpretationValue = fGapMatchInteraction.getResponseDeclaration().getCorrectResponse();
        for (int i=0; i<aInterpretationValue.size(); i++) {
        	String valueData = aInterpretationValue.getValueAt(i).getData();
        	int index = valueData.indexOf(" ");
        	if (index!=-1) {
        		String sourceID = valueData.substring(0, index);
        		GapText aGapText = fGapMatchInteraction.getGapTextByID(sourceID);
        		String targetID = valueData.substring(index+1);
        		Gap aGap = fGapMatchInteraction.getGapByID(targetID);
        		if (aGapText!=null&&aGap!=null) {
        			AssociationExtension aAssociationExtension = new AssociationExtension(sourceID, targetID);
        			fAssociationExtensionList.addAssociationExtension(aAssociationExtension);
        		}
        	}
        }
         
        Mapping aMapping = fGapMatchInteraction.getResponseDeclaration().getMapping();
        for (int i=0; i<aMapping.size(); i++) {
        	String valueData = aMapping.getMapEntryAt(i).getMapKey();        	
        	int index = valueData.indexOf(" ");
        	if (index!=-1) {
        		String sourceID = valueData.substring(0, index);
        		GapText aGapText = fGapMatchInteraction.getGapTextByID(sourceID);
        		String targetID = valueData.substring(index+1);
        		Gap aGap = fGapMatchInteraction.getGapByID(targetID);
        		if (aGapText!=null&&aGap!=null) {
        			AssociationExtension aAssociationExtension = fAssociationExtensionList.getAssociationExtensionBySourceIDTargetID(sourceID, targetID);
        			if (aAssociationExtension==null) {
        				aAssociationExtension = new AssociationExtension(sourceID, targetID, false, aMapping.getMapEntryAt(i).getMappedValue()); 
        				fAssociationExtensionList.addAssociationExtension(aAssociationExtension);
        			} else {
        				aAssociationExtension.setMappedValue(aMapping.getMapEntryAt(i).getMappedValue());
        			}
        		}
        	}	
        }

        fAssociationTable.setInput(fAssociationExtensionList);

        fAssociationTable.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Select Editor Panel
                Object object = ((StructuredSelection)event.getSelection()).getFirstElement();
                updateEditorPanel(object);
            }
        });
	}


	private void createGap() {

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
						Gap aGap = new Gap (fAssessmentItem, fGapMatchInteraction, correctAnswer, selectionRange.x, selectionRange.y);
						fGapMatchInteraction.addGapAt(i, aGap);
						addGapTextPanelForGap(aGap);						
						map.put(style, aGap);
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
						Gap aGap = (Gap) map.get(originalStyleRanges[i]);
						fAssociationExtensionList.removeAssociationExtensionBySourceIDTargetID(aGap.getMatchedGapText().getId(), aGap.getId());
												
						aGap = new Gap (fAssessmentItem, fGapMatchInteraction, 
								originalString.substring(start, end), 
								start, length);
						fGapMatchInteraction.setGapAt(i, aGap);
						addGapTextPanelForGap(aGap);
						map.remove(originalStyleRanges[i]);
						map.put(style, aGap);
						
						for (int j=i+1; j<=i+count; j++) {
							aGap = (Gap)map.get(originalStyleRanges[j]);
							fAssociationExtensionList.removeAssociationExtensionBySourceIDTargetID(aGap.getMatchedGapText().getId(), aGap.getId());
							fGapMatchInteraction.removeGap(aGap);
							map.remove(originalStyleRanges[j]);
						}
						styledText.setSelection(start, end);
					}
					else { //the selection and a gap overlap
						end = Math.max(start+length, end);
						start = Math.min(start, selectionRange.x);
						length = end - start;
						Gap aGap = (Gap) map.get(originalStyleRanges[i]);
						fAssociationExtensionList.removeAssociationExtensionBySourceIDTargetID(aGap.getMatchedGapText().getId(), aGap.getId());
						
						map.remove(originalStyleRanges[i]);
						
						StyleRange style = new StyleRange(start, length, null, null, SWT.BOLD);
						styledText.setStyleRange(style);
						
						aGap = new Gap (fAssessmentItem, fGapMatchInteraction, 
								originalString.substring(start, end), 
								start, length);
						fGapMatchInteraction.setGapAt(i, aGap);
						addGapTextPanelForGap(aGap);
						map.put(style, aGap);
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
			Gap aGap = new Gap (fAssessmentItem, fGapMatchInteraction, correctAnswer, selectionRange.x, selectionRange.y);
			fGapMatchInteraction.addGap(aGap);
			addGapTextPanelForGap(aGap);			
			map.put(style, aGap);			
			originalStyleRanges = styledText.getStyleRanges();
		}
	}

	private void addGapTextPanelForGap(Gap aGap) {
		GapText aGapText = null;
		int selectedNumber = -1;
		for (int j=0; j<fGapMatchInteraction.getGapTextList().size(); j++) {
			aGapText = (GapText)fGapMatchInteraction.getGapTextList().getBasicElementAt(j);
			if ((aGapText).getData().equals(aGap.getData())) {
				selectedNumber = j;
				break;
			}
		}
		if (selectedNumber != -1) {
			// a proper gap text exists
			aGap.setMatchedGapText(aGapText);
			resetSelectButtons();
			fSelectButton[selectedNumber].setSelection(true);				
		} else {
			aGapText = new GapText(fAssessmentItem, fGapMatchInteraction);	
			aGapText.setData(aGap.getData());
			aGap.setMatchedGapText(aGapText);
			fGapMatchInteraction.addGapText(aGapText);
			addChoicePanel(aGapText);
			resetSelectButtons();
			fSelectButton[fGapTextNumber-1].setSelection(true);			
			AssessmentTestEditor.changeToContentColour(fEditChoicesPanel);	
		}
				
		if (fGapTextNumber >= fMaxGapTextNumber) {
			fNewGapTextButton.setEnabled(false);
		}		
		AssociationExtension aAssociationExtension = new AssociationExtension(aGapText.getId(), aGap.getId()); 
		fAssociationExtensionList.addAssociationExtension(aAssociationExtension);
		fAssociationTable.setSelection(new StructuredSelection(aAssociationExtension), true);
	}
	
	private void updateUserInterface() {
		fEditChoicesPanel.layout();							
		layout();
		fTestEditor.updateQuestionPanel();							
	}

	private void removeGap() {
		
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
				
				Gap aGap = (Gap)map.get(styles[i]);
				fAssociationExtensionList.removeAssociationExtensionBySourceIDTargetID(aGap.getMatchedGapText().getId(), aGap.getId());
				
				fGapMatchInteraction.removeGap(aGap);
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
	
	private void resetSelectButtons() {
		for (int i=0; i<fGapTextNumber; i++) {
			fSelectButton[i].setSelection(false);			
		}
	}

	public void reset() {
		
		//remove original response declarations
		styledText.setEnabled(false);
		
		removeAllGapTextPanel();
		fGapMatchInteraction.getGapTextList().clear();		
		fGapMatchInteraction.getGapList().clear();
		map.clear();
		fAssociationExtensionList.clear();
		fAssociationTable.setInput(fAssociationExtensionList);
    	fAssociationTable.refresh();
		
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
	
	public void updateGapTextAt(GapText updatedGapText) {
				
		styledText.setEnabled(false);
		for (int i=0; i<fAssociationExtensionList.size(); i++) {
			AssociationExtension aAssociationExtension = fAssociationExtensionList.getAssociationExtensionAt(i);
			if (aAssociationExtension.getSource().equals(updatedGapText.getId()))	{
				Gap aGap = fGapMatchInteraction.getGapByID(aAssociationExtension.getTarget());
				if (aGap!=null) {
					updateGapMatchText(aGap, updatedGapText.getData());
				}
			}
		}
		fGapMatchInteraction.setData(originalString);
		styledText.setEnabled(true);
	}
	
	public void updateGapMatchText(Gap updatedGap, String newGapTextString) {
		
		String oldGapTextString = updatedGap.getData();
		int difference = newGapTextString.length() - oldGapTextString.length();
		
		originalString = originalString.substring(0, updatedGap.getStart()) +
			newGapTextString + 
			originalString.substring(updatedGap.getStart()+updatedGap.getLength());		
		styledText.setText(originalString);
				
		map.clear();
		
		int size = fGapMatchInteraction.getGapList().size();
		for (int i = 0; i < size; i++) {
			Gap aGap = (Gap)fGapMatchInteraction.getGapList().getBasicElementAt(i);
			//handle all blanks before the changed correct choice
			if (aGap!=updatedGap) {
				StyleRange style = new StyleRange(aGap.getStart(), 
						aGap.getLength(), null, null, SWT.BOLD);
				map.put(style, aGap);
				styledText.setStyleRange(style);
			}
			else {
				//handle the changed correct choice
				aGap.setLength(aGap.getLength()+difference);
				aGap.setData(newGapTextString);
				StyleRange style = new StyleRange(aGap.getStart(), 
						aGap.getLength(), null, null, SWT.BOLD);
				map.put(style, aGap);
				styledText.setStyleRange(style);
				
				//handle all gap texts after the changed gap
				for (int j=i+1; j< size; j++) {
					aGap = (Gap)fGapMatchInteraction.getGapList().getBasicElementAt(j);
					aGap.setStart(aGap.getStart()+difference);
					style = new StyleRange(aGap.getStart(), 
							aGap.getLength(), null, null, SWT.BOLD);
					map.put(style, aGap);
					styledText.setStyleRange(style);
				}
				originalStyleRanges = styledText.getStyleRanges();	
				styledText.redraw();
				return;
			}
		}
	}
	
	private void createGapTextPanel( ){
		Composite controlEditPanel =  toolkit.createComposite(this, SWT.NONE);
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 35;
		controlEditPanel.setLayoutData(gridData);
		
		FormLayout fLayout = new FormLayout();
		controlEditPanel.setLayout(fLayout);
		
		Label ansLabel = toolkit.createLabel(controlEditPanel, "Gap Text ", SWT.NONE);
		ansLabel.setFont(AssessmentTestEditor.fontBold9);
		FormData gd1 = new FormData();
		gd1.left = new FormAttachment(0,3);
		gd1.top = new FormAttachment(0,10);
		ansLabel.setLayoutData(gd1);

		fShuffleButton = toolkit.createButton(controlEditPanel, "Shuffle", SWT.CHECK);
		gd1 = new FormData();
		gd1.left = new FormAttachment(100, -200);
		gd1.top = new FormAttachment (ansLabel, 0, SWT.TOP);
		fShuffleButton.setLayoutData(gd1);
		fShuffleButton.setToolTipText("Render the gap text in this order or randomly");
		
		if (fGapMatchInteraction.getShuffle())
			fShuffleButton.setSelection(true);
		fShuffleButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				fGapMatchInteraction
						.setShuffle(fShuffleButton.getSelection());
				setDirty(true);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				fGapMatchInteraction
						.setShuffle(fShuffleButton.getSelection());
				setDirty(true);
			}
		});

		
		fNewGapTextButton = toolkit.createButton(controlEditPanel, "Add Gap Text", SWT.PUSH);
				
		gd1 = new FormData();
		gd1.right = new FormAttachment(100, -5);
		gd1.bottom = new FormAttachment(ansLabel, 3, SWT.BOTTOM);
		fNewGapTextButton.setLayoutData(gd1);
		fNewGapTextButton.setToolTipText("Add a gap text");
		fNewGapTextButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				GapText aGapText = new GapText(
						fGapMatchInteraction.getAssessmentItem(),
						fGapMatchInteraction);
				fGapMatchInteraction.addGapText(aGapText);
				addChoicePanel(aGapText);
				if (fGapTextNumber >= fMaxGapTextNumber) {
					fNewGapTextButton.setEnabled(false);
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
						
		fEditChoicesPanel.setLayout(new GridLayout(5, false));
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		
		Label aLabel = toolkit.createLabel(fEditChoicesPanel, " ", SWT.CENTER);		
		aLabel = toolkit.createLabel(fEditChoicesPanel, " ", SWT.CENTER);		
		aLabel = toolkit.createLabel(fEditChoicesPanel, "Gap Text", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditChoicesPanel, "Selected", SWT.CENTER);		
		aLabel = toolkit.createLabel(fEditChoicesPanel, " ", SWT.CENTER);		
		
		fGapTextNumber = 0;
		fMaxGapTextNumber = AbstractQuestionEditPanel.maximum;
		
		fGapTextLabel = new Label[fMaxGapTextNumber];
		fRemoveButton = new Button[fMaxGapTextNumber];
		fGapTextText = new Text[fMaxGapTextNumber];
		fSelectButton = new Button[fMaxGapTextNumber];
		
		fMoveButtonComposite = new Composite[fMaxGapTextNumber]; 
		fUpButton = new Button[fMaxGapTextNumber];
		fDownButton = new Button[fMaxGapTextNumber];
		
		BasicElementList aList = fGapMatchInteraction.getGapTextList();
		for (int i = 0; i < aList.size(); i++) {
			addChoicePanel((GapText) aList.getBasicElementAt(i));
		}

		if (fGapTextNumber >= fMaxGapTextNumber) {
			fNewGapTextButton.setEnabled(false);
		}
	}
	
	private void addChoicePanel(GapText aGapText) {
		fGapTextNumber++;
		
		if (fGapTextNumber < 10) {
			fGapTextLabel[fGapTextNumber - 1] = toolkit.createLabel(fEditChoicesPanel, "  " + fGapTextNumber + ":",
					SWT.CENTER);
		} else {
			fGapTextLabel[fGapTextNumber - 1] = toolkit.createLabel(fEditChoicesPanel, fGapTextNumber + ":",
				SWT.CENTER);
		}
		
		fMoveButtonComposite[fGapTextNumber - 1] = toolkit.createComposite(fEditChoicesPanel, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.verticalSpacing = 0;
		gl.marginHeight = 0;
		fMoveButtonComposite[fGapTextNumber - 1].setLayout(gl);

		fUpButton[fGapTextNumber - 1] = toolkit.createButton(fMoveButtonComposite[fGapTextNumber - 1], null, SWT.ARROW | SWT.UP);
		fUpButton[fGapTextNumber - 1].setToolTipText("Move this choice up");
		fUpButton[fGapTextNumber - 1].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fGapTextNumber; i++) {
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
		fUpButton[fGapTextNumber - 1].pack();

		fDownButton[fGapTextNumber - 1] = toolkit.createButton(fMoveButtonComposite[fGapTextNumber - 1], null, SWT.ARROW | SWT.DOWN);
		fDownButton[fGapTextNumber - 1].setToolTipText("Move this choice down");
		fDownButton[fGapTextNumber - 1].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fGapTextNumber; i++) {
					if (object == fDownButton[i])
						index = i;
				}
				if (index == fGapTextNumber -1 ) {
					// on the bottom 
					MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
				    mDialog.setMessage("This choice is on the bottom already.");
				    mDialog.open();
				}
				else if ((index >= 0) && (index < fGapTextNumber -1)) {
					changeOrderBetween(index, false);
				}				
			}
		});
		fDownButton[fGapTextNumber - 1].pack();
		
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd2.heightHint = 30;
		gd2.widthHint = 450;
		fGapTextText[fGapTextNumber - 1] = toolkit.createText(fEditChoicesPanel, aGapText.getData(), SWT.WRAP
				| SWT.BORDER | SWT.V_SCROLL);
		fGapTextText[fGapTextNumber - 1].setLayoutData(gd2);
		fGapTextText[fGapTextNumber - 1].addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fGapTextNumber; i++) {
					if (object == fGapTextText[i])
						index = i;
				}
				if (index > -1) {
					GapText updatedGapText = (GapText)fGapMatchInteraction.getGapTextList().getBasicElementAt(index);
					updatedGapText.setData(fGapTextText[index].getText());

					updateGapTextAt(updatedGapText);
					
					boolean result = fAssociationExtensionList.isChoiceUsedInAnyAssociationExtension(AssessmentElementFactory.SOURCE_TITLE, updatedGapText.getId());
					if (result)
						fAssociationTable.setInput(fAssociationExtensionList);

					setDirty(true);
				}
			}
		});
		
		fSelectButton[fGapTextNumber - 1] = toolkit.createButton(fEditChoicesPanel,
				null, SWT.RADIO | SWT.CENTER);	
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		fSelectButton[fGapTextNumber - 1].setLayoutData(gd);
		fSelectButton[fGapTextNumber - 1].addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fGapTextNumber; i++) {
					if (object == fSelectButton[i])
						index = i;
				}

				if (index > -1) {					
					GapText currentlySelectedGapText = (GapText)fGapMatchInteraction.getGapTextList().getBasicElementAt(index);	
					/*
					if (fSelectedGapText!=currentlySelectedGapText) {
						if (currentlySelectedGapText.getData().equalsIgnoreCase("")) {
							MessageBox mDialog = new MessageBox(getShell(), SWT.OK); //in wizard, using fItemEditor.getShell()
						    mDialog.setMessage("It is not allowed to define an empty string as a gap.");
						    mDialog.open();
						    return;
						}
						else {
							//updateInteractionCorrectChoice(currentlySelectedGapText, currentlySelectedGapText.getData());
							setDirty(true);
						}	
					}
					*/
				}
			}
		});
		
		fRemoveButton[fGapTextNumber - 1] = toolkit.createButton(fEditChoicesPanel, null, SWT.PUSH | SWT.CENTER);
		Image anImage = ImageFactory.getImage(ImageFactory.ICON_DELETE);
		if (anImage != null) 
			fRemoveButton[fGapTextNumber - 1].setImage(anImage);
		else 
			fRemoveButton[fGapTextNumber - 1].setText("Delete Choice");

		fRemoveButton[fGapTextNumber - 1].setToolTipText("Delete this choice");
		fRemoveButton[fGapTextNumber - 1]
						.addSelectionListener(new SelectionAdapter() {
				            public void widgetSelected(SelectionEvent e) {
								
								if (!MessageDialog.openQuestion(
						                Display.getDefault().getActiveShell(),
						                "Delete",
						                "Are you sure you want to delete this choice?"))
									return;								
								
								Object object = e.getSource();
								int index = -1;
								for (int i = 0; i < fGapTextNumber; i++) {
									if (object == fRemoveButton[i])
										index = i;
								}
								if (index > -1) {
									GapText aGapText = (GapText) fGapMatchInteraction.getGapTextList().getBasicElementAt(index);
									
									for (int j = 0; j < fGapMatchInteraction.getGapList().size(); j++) {
										if (aGapText==fGapMatchInteraction.getGapAt(j).getMatchedGapText()) {
											MessageBox mDialog = new MessageBox(getShell(), SWT.OK); //in wizard, using fItemEditor.getShell()
									    	mDialog.setMessage("It is not allowed to remove this gap-text, because it is matched with a gap '"+fGapMatchInteraction.getGapAt(j).getData()+"'.");
									    	mDialog.open();
									    	return ;
										}
									}
									
									removeGapTextPanelAt(index);
									fNewGapTextButton.setEnabled(true);
									setDirty(true);
									updateUserInterface();
								}
							}
						});
		fRemoveButton[fGapTextNumber - 1].pack();
	}

	private void changeOrderBetween(int i, boolean isUp) {

		GapText thisChoice = (GapText) fGapMatchInteraction.getGapTextList().getBasicElementAt(i);
		String thisChoiceText = fGapTextText[i].getText();
		if (isUp) {
			// note: the choice has to be changed before the text exchange
			fGapMatchInteraction.getGapTextList().setElementAt(i, fGapMatchInteraction.getGapTextList().getBasicElementAt(i-1));
			fGapMatchInteraction.getGapTextList().setElementAt(i-1, thisChoice);
		
			fGapTextText[i].setText(fGapTextText[i-1].getText());
			fGapTextText[i-1].setText(thisChoiceText);			
		} else {
			fGapMatchInteraction.getGapTextList().setElementAt(i, fGapMatchInteraction.getGapTextList().getBasicElementAt(i+1));
			fGapMatchInteraction.getGapTextList().setElementAt(i+1, thisChoice);

			fGapTextText[i].setText(fGapTextText[i+1].getText());
			fGapTextText[i+1].setText(thisChoiceText);
		}
	
		fEditChoicesPanel.layout();
		layout();
		fTestEditor.updateQuestionPanel();
		setDirty(true);
	}
	
	private void removeAllGapTextPanel() {
		for (int i = 0; i < fGapTextNumber; i++) {
			fGapTextLabel[i].dispose();
			fGapTextText[i].dispose();
			fSelectButton[i].dispose();
			fRemoveButton[i].dispose();

			fUpButton[i].dispose();
			fDownButton[i].dispose();
			fMoveButtonComposite[i].dispose();
		}
		fGapTextNumber=0;		
	}
	
	private void removeGapTextPanelAt(int i) {

		fGapMatchInteraction.getGapTextList().removeElementAt(i);

		// update the UI
		for (int j = i; j < fGapTextNumber - 1; j++) {
			fGapTextText[j].setText(fGapTextText[j + 1].getText());
			fSelectButton[j].setSelection(fSelectButton[j + 1].getSelection());
		}

		fGapTextLabel[fGapTextNumber - 1].dispose();
		fGapTextText[fGapTextNumber - 1].dispose();
		fSelectButton[fGapTextNumber - 1].dispose();
		fRemoveButton[fGapTextNumber - 1].dispose();

		fUpButton[fGapTextNumber - 1].dispose();
		fDownButton[fGapTextNumber - 1].dispose();
		fMoveButtonComposite[fGapTextNumber - 1].dispose();

		fGapTextNumber--;
	}
	
	
	public AssociationExtensionsTableViewer getAssociationTreeTable() {
		return fAssociationTable;
	}
	
	public AssociationExtensionList getAssociationList() {
		return fAssociationExtensionList;
	}
	
    private void updateEditorPanel(Object object) {
        // 
        if(object instanceof Value) {
            //sourceTest TargetText change
         }
    }
    
    public void setResponseCorrectAndMapping() {
    	InterpretationValue correctResponses = new InterpretationValue(AssessmentElementFactory.CORRECT_RESPONSE);
    	Mapping aMapping = new Mapping();
    	for (int i=0; i<fAssociationExtensionList.size(); i++) {
    		AssociationExtension aAssociationExtension = fAssociationExtensionList.getAssociationExtensionAt(i);
    		if (aAssociationExtension.getCorrect()) {
    			Value aValue = new Value(AssessmentElementFactory.DIRECTED_PAIR, 
    					aAssociationExtension.getSource()+" "+aAssociationExtension.getTarget());
    			correctResponses.addValue(aValue);
    		}
    		MapEntry aMapEntry = new MapEntry(aAssociationExtension.getSource()+" "+
    				aAssociationExtension.getTarget(), aAssociationExtension.getMappedValue());
    		aMapping.addMapEntry(aMapEntry);
    	}
    	fGapMatchInteraction.getResponseDeclaration().setCorrectResponse(correctResponses);
    	fGapMatchInteraction.getResponseDeclaration().setMapping(aMapping);
    }

	
}
