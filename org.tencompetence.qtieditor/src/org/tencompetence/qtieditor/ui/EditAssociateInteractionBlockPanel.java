package org.tencompetence.qtieditor.ui;


import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import org.tencompetence.qtieditor.elements.AssociationExtension;
import org.tencompetence.qtieditor.elements.AssociateInteraction;
import org.tencompetence.qtieditor.elements.BasicElement;
import org.tencompetence.qtieditor.elements.BasicElementList;
import org.tencompetence.qtieditor.elements.ChoiceInteraction;
import org.tencompetence.qtieditor.elements.InterpretationValue;
import org.tencompetence.qtieditor.elements.MapEntry;
import org.tencompetence.qtieditor.elements.Mapping;
import org.tencompetence.qtieditor.elements.SimpleAssociableChoice;
import org.tencompetence.qtieditor.elements.Value;
import org.tencompetence.qtieditor.elements.AssociationExtensionList;


public class EditAssociateInteractionBlockPanel extends AbstractQuestionEditPanel {

	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	
	private Text fPromptText;
	private Button fNewAssociationButton;
	private Button fDeleteAssociationButton;
	private Button fNewChoiceButton;
	//private Button fRemoveChoiceButton;
	private Button fShuffleButton;

	private int fChoiceNumber = 0;
	private int fMaxChoiceNumber;
	private Composite fEditAssociateCompositeListPanel;
	//private EditAssociateListPanel fAssociateListPanel;	
	private Composite fEditAssociationsPanel;
	private EditAssociateRowPanel[] fAssociateComposites = null;
	private AssociationExtensionsTableViewer fAssociationTable;
	private AssociationExtensionList fAssociationList = new AssociationExtensionList();

	private AssociateInteraction fAssociateInteraction;
	
	public EditAssociateInteractionBlockPanel(Composite parent,
			AssessmentItem aAssessmentItem,
			AssociateInteraction aAssociateInteraction, 
			AssessmentTestEditor editor) {

		super(parent, editor);
		fAssociateInteraction = aAssociateInteraction;
		fAssessmentItem = aAssessmentItem;
		init();
		AssessmentTestEditor.changeToContentColour(this);
	}

	public void init() {
		
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayout(gl);
		
		createQuestionTitlePanel(this, toolkit, "Associate Question");
		createQuestionTextPanel();
		createAssicationPanel();
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
		gd.heightHint = 100; 
		gd.widthHint = 760; 
		fPromptText = toolkit.createText(promptEditPanel, fAssociateInteraction.getPrompt().getData(), SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		fPromptText.setLayoutData(gd);
		fPromptText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fAssociateInteraction.getPrompt().setData(fPromptText.getText());
				setDirty(true);
			}
		});
	}
	
	private void createAssicationPanel() {
		fEditAssociationsPanel = toolkit.createComposite(this, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		//gridData.grabExcessHorizontalSpace = true;
		//gridData.heightHint = 35;
		fEditAssociationsPanel.setSize(760, 100);
		fEditAssociationsPanel.setLayoutData(gridData);
		
		FormLayout fLayout = new FormLayout();
		fEditAssociationsPanel.setLayout(fLayout);
		
		Label ansLabel = toolkit.createLabel(fEditAssociationsPanel, "Association List", SWT.NONE);
		ansLabel.setFont(AssessmentTestEditor.fontBold9);
		FormData gd1 = new FormData();
		gd1.left = new FormAttachment(0,3);
		gd1.top = new FormAttachment(0,10);
		ansLabel.setLayoutData(gd1);
		
		fDeleteAssociationButton = toolkit.createButton(fEditAssociationsPanel, "Delete Association", SWT.PUSH);				
		gd1 = new FormData();
		gd1.right = new FormAttachment(100, -270);
		gd1.bottom = new FormAttachment(ansLabel, 3, SWT.BOTTOM);
		fDeleteAssociationButton.setLayoutData(gd1);
		fDeleteAssociationButton.setToolTipText("delete an association");
		fDeleteAssociationButton.setEnabled(false);
		fDeleteAssociationButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	
            	deleteAssociation();
            	updateAssociateComposites();
			}
		});
				
		fNewAssociationButton = toolkit.createButton(fEditAssociationsPanel, "Add Association", SWT.PUSH);				
		gd1 = new FormData();
		gd1.right = new FormAttachment(100, -5);
		gd1.bottom = new FormAttachment(ansLabel, 3, SWT.BOTTOM);
		fNewAssociationButton.setLayoutData(gd1);
		fNewAssociationButton.setToolTipText("Add an association");
		fNewAssociationButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	
            	addNewAssociation();
            	fAssociationTable.refresh();
            	updateAssociateComposites();
            }
		});
		
        Composite composite = new Composite(this, SWT.NULL);
        composite.setLayout(new TableColumnLayout());
        gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.heightHint = 100;
		gridData.widthHint = 700;
        composite.setLayoutData(gridData);
        fAssociationTable = new AssociationExtensionsTableViewer(composite, fAssociateInteraction, this);
        
        InterpretationValue aInterpretationValue = fAssociateInteraction.getResponseDeclaration().getCorrectResponse();
        for (int i=0; i<aInterpretationValue.size(); i++) {
        	String valueData = aInterpretationValue.getValueAt(i).getData();
        	int index = valueData.indexOf(" ");
        	if (index!=-1) {
        		String id = valueData.substring(0, index);
        		SimpleAssociableChoice sourceChoice = fAssociateInteraction.getSimpleAssociableChoiceByID(id);
        		id = valueData.substring(index+1);
        		SimpleAssociableChoice targetChoice = fAssociateInteraction.getSimpleAssociableChoiceByID(id);
        		AssociationExtension aAssociationExtension = new AssociationExtension(sourceChoice.getId(), targetChoice.getId());
        		fAssociationList.addAssociationExtension(aAssociationExtension);
        	}
        }
         
        Mapping aMapping = fAssociateInteraction.getResponseDeclaration().getMapping();
        for (int i=0; i<aMapping.size(); i++) {
        	String valueData = aMapping.getMapEntryAt(i).getMapKey();        	
        	int index = valueData.indexOf(" ");
        	if (index!=-1) {
        		String id = valueData.substring(0, index);
        		SimpleAssociableChoice sourceChoice = fAssociateInteraction.getSimpleAssociableChoiceByID(id);
        		id = valueData.substring(index+1);
        		SimpleAssociableChoice targetChoice = fAssociateInteraction.getSimpleAssociableChoiceByID(id);
        		
        		AssociationExtension aAssociationExtension = fAssociationList.
        			getAssociationExtensionBySourceIDTargetID(sourceChoice.getId(), targetChoice.getId());
        		if (aAssociationExtension==null) {
        			aAssociationExtension = new AssociationExtension(sourceChoice.getId(), 
        					targetChoice.getId(), false, aMapping.getMapEntryAt(i).getMappedValue()); 
        			fAssociationList.addAssociationExtension(aAssociationExtension);
        		} else {
        			aAssociationExtension.setMappedValue(aMapping.getMapEntryAt(i).getMappedValue());
        		}
        	}	
        }

        fAssociationTable.setInput(fAssociationList);

        fAssociationTable.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Update actions
                updateActions(event.getSelection());
                // Select Editor Panel
                Object object = ((StructuredSelection)event.getSelection()).getFirstElement();
                updateEditorPanel(object);
            }
        });
	}
	
	private void createChoicesPanel() {
		Composite controlEditPanel = toolkit.createComposite(this, SWT.NONE);		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 50;
		controlEditPanel.setLayoutData(gridData);
		
		FormLayout fLayout = new FormLayout();
		controlEditPanel.setLayout(fLayout);
		
		Label ansLabel = toolkit.createLabel(controlEditPanel, "Choices", SWT.NONE);
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
		fShuffleButton.setToolTipText("Render the choices in this order or randomly");		
		if (fAssociateInteraction.getShuffle())
			fShuffleButton.setSelection(true);
		fShuffleButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				fAssociateInteraction
						.setShuffle(fShuffleButton.getSelection());
				setDirty(true);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				fAssociateInteraction
						.setShuffle(fShuffleButton.getSelection());
				setDirty(true);
			}
		});
			
		fNewChoiceButton = toolkit.createButton(controlEditPanel, "Add Choice", SWT.PUSH);		
		gd1 = new FormData();
		gd1.right = new FormAttachment(100, -5);
		gd1.bottom = new FormAttachment(ansLabel, 3, SWT.BOTTOM);
		fNewChoiceButton.setLayoutData(gd1);
		fNewChoiceButton.setToolTipText("Add Choice");
		fNewChoiceButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	addChoice();
        		AssessmentTestEditor.changeToContentColour(fEditAssociateCompositeListPanel);
        		updateAssociateComposites();
			}
		});
		
		
		Label aChoiceLabel = toolkit.createLabel(controlEditPanel, "Choice text", SWT.NONE);
		gd1 = new FormData();
		gd1.left = new FormAttachment(0,60);
		gd1.top = new FormAttachment(0,35);
		aChoiceLabel.setLayoutData(gd1);

		Label aLabel = toolkit.createLabel(controlEditPanel, "Source", SWT.NONE);
		gd1 = new FormData();
		gd1.left = new FormAttachment(100, -160);
		gd1.top = new FormAttachment (aChoiceLabel, 0, SWT.TOP);
		aLabel.setLayoutData(gd1);
		
		aLabel = toolkit.createLabel(controlEditPanel, "Target", SWT.NONE);
		gd1 = new FormData();
		gd1.left = new FormAttachment(100, -120);
		gd1.top = new FormAttachment (aChoiceLabel, 0, SWT.TOP);
		aLabel.setLayoutData(gd1);
		
		aLabel = toolkit.createLabel(controlEditPanel, "Score", SWT.NONE);
		gd1 = new FormData();
		gd1.left = new FormAttachment(100, -70);
		gd1.top = new FormAttachment (aChoiceLabel, 0, SWT.TOP);
		aLabel.setLayoutData(gd1);

		fEditAssociateCompositeListPanel = toolkit.createComposite(this, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL; 
		gridData.grabExcessHorizontalSpace = true; 
		fEditAssociateCompositeListPanel.setLayoutData(gridData);
		fEditAssociateCompositeListPanel.setLayout(new GridLayout(1, false));	
		
		fMaxChoiceNumber = AbstractQuestionEditPanel.maximum;
		fAssociateComposites = new EditAssociateRowPanel[fMaxChoiceNumber]; 
		
		BasicElementList aList = fAssociateInteraction.getSimpleAssociableChoiceList();
		fChoiceNumber = aList.size();

		if (fChoiceNumber>=fMaxChoiceNumber) {
			fChoiceNumber = fMaxChoiceNumber;
			fNewChoiceButton.setEnabled(false);
		}
		//only handle the max number of choice
		for (int i = 0; i < fChoiceNumber; i++) {
			fAssociateComposites[i] = new EditAssociateRowPanel(fEditAssociateCompositeListPanel, 
					fAssociateInteraction, (SimpleAssociableChoice)aList.getBasicElementAt(i), this, i);
		}
	}
	
	private void addChoice() {
		SimpleAssociableChoice aSimpleAssociableChoice= new SimpleAssociableChoice();
		fAssociateInteraction.addSimpleAssociableChoice(aSimpleAssociableChoice);
		fAssociateComposites[fChoiceNumber] = new EditAssociateRowPanel(fEditAssociateCompositeListPanel, 
				fAssociateInteraction, aSimpleAssociableChoice, this, fChoiceNumber);

		fChoiceNumber++;
		if (fChoiceNumber >= fMaxChoiceNumber) {
			fNewChoiceButton.setEnabled(false);
		}
	}
	
	private void updateAssociateComposites() {
		setDirty(true);
		fEditAssociateCompositeListPanel.layout();
		layout();
		fTestEditor.updateQuestionPanel();

	}
	
	public void moveUp(int position) {
		if (position == 0) {
			MessageBox mDialog = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);					
			mDialog.setText("Message:");
			mDialog.setMessage("The moved choice is the first one and it can not be moved up anymore!");
			mDialog.open();
			return;
		} 
		BasicElementList aList = fAssociateInteraction.getSimpleAssociableChoiceList();
		BasicElement tempElement = aList.getBasicElementAt(position-1);
		boolean tempSource = fAssociateComposites[position-1].getSourceButtonSelection();
		boolean tempTarget = fAssociateComposites[position-1].getTargetButtonSelection();
		aList.setElementAt(position-1, aList.getBasicElementAt(position));
		aList.setElementAt(position, tempElement);
		
		fAssociateComposites[position-1].setPanelForSimpleAssociableChoice((SimpleAssociableChoice)aList.getBasicElementAt(position-1),
				fAssociateComposites[position].getSourceButtonSelection(),
				fAssociateComposites[position].getTargetButtonSelection());
		fAssociateComposites[position].setPanelForSimpleAssociableChoice((SimpleAssociableChoice)aList.getBasicElementAt(position),
				tempSource,
				tempTarget);	
		fAssociateComposites[position-1].layout();
		fAssociateComposites[position].layout();
		updateAssociateComposites();
	}
	
	public void moveDown(int position) {
		if (position == fChoiceNumber-1) {// the last one
			MessageBox mDialog = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);					
			mDialog.setText("Message:");
			mDialog.setMessage("The selected choice is the last one and it can not be moved down anymore!");
			mDialog.open();
			return;
		}
		BasicElementList aList = fAssociateInteraction.getSimpleAssociableChoiceList();
		BasicElement tempElement = aList.getBasicElementAt(position+1);
		boolean tempSource = fAssociateComposites[position+1].getSourceButtonSelection();
		boolean tempTarget = fAssociateComposites[position+1].getTargetButtonSelection();
		aList.setElementAt(position+1, aList.getBasicElementAt(position));
		aList.setElementAt(position, tempElement);
		
		fAssociateComposites[position+1].setPanelForSimpleAssociableChoice((SimpleAssociableChoice)aList.getBasicElementAt(position+1),
				fAssociateComposites[position].getSourceButtonSelection(),
				fAssociateComposites[position].getTargetButtonSelection());
		fAssociateComposites[position].setPanelForSimpleAssociableChoice((SimpleAssociableChoice)aList.getBasicElementAt(position),
				tempSource,
				tempTarget);	
		fAssociateComposites[position+1].layout();
		fAssociateComposites[position].layout();
		updateAssociateComposites();
	}

	public void selectedSourceAt(int position) {
		for (int i = 0; i < fChoiceNumber; i++) {
			if (i!=position) {
				fAssociateComposites[i].setSelectSourceButton(false);
				fAssociateComposites[i].layout();
			}
		}
		updateAssociateComposites();
	}

	public void selectedTargetAt(int position) {
		for (int i = 0; i < fChoiceNumber; i++) {
			if (i!=position) {
				fAssociateComposites[i].setSelectTargetButton(false);
				fAssociateComposites[i].layout();
			}
		}
		updateAssociateComposites();
	}
	
	public void removeAssociateAt(int i) {
		String id = fAssociateInteraction.getSimpleAssociableChoiceAt(i).getId();
		boolean result = getAssociationList().removeRelaventAssociationsByID(AssessmentElementFactory.ASSOCIATE, id);
		if (result)
			getAssociationTableView().setInput(getAssociationList());
		
		BasicElementList aList = fAssociateInteraction.getSimpleAssociableChoiceList();		
		aList.removeElementAt(i);

		for (int j = i; j < fChoiceNumber - 1; j++) {
			fAssociateComposites[j].setPanelForSimpleAssociableChoice((SimpleAssociableChoice)aList.getBasicElementAt(j),
					fAssociateComposites[j+1].getSourceButtonSelection(),
					fAssociateComposites[j+1].getTargetButtonSelection());
			fAssociateComposites[i].layout();
		}

		fAssociateComposites[fChoiceNumber - 1].dispose();		
		fChoiceNumber--;
		fNewChoiceButton.setEnabled(true);
		updateAssociateComposites();		
	}
	
    private void addNewAssociation() {
    	/*
    	if (fMaxChoiceNumber == fChoiceNumber) {// full 
			MessageBox mDialog = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);					
			mDialog.setText("Message:");
			mDialog.setMessage("The maximun of choices is "+fMaxChoiceNumber+ 
					". You can not define new choice anymore.");
			mDialog.open();
			return;
		}
		*/
    	int aSourcePosition = -1; 
    	int aTargetPosition = -1;
    	
		BasicElementList aList = fAssociateInteraction.getSimpleAssociableChoiceList();
		for (int i = 0; i < fChoiceNumber; i++) {
			if (fAssociateComposites[i].getSourceButtonSelection())
				aSourcePosition = i;
			if (fAssociateComposites[i].getTargetButtonSelection())
				aTargetPosition = i;
		}
		
        if (aSourcePosition != -1 && aTargetPosition != -1) {
    		String sourceID = fAssociateInteraction.getSimpleAssociableChoiceAt(aSourcePosition).getId();
    		String targetID = fAssociateInteraction.getSimpleAssociableChoiceAt(aTargetPosition).getId();
    		
       		if ((fAssociationList.getAssociationExtensionBySourceIDTargetID(sourceID, targetID)==null) &&
    			(fAssociationList.getAssociationExtensionBySourceIDTargetID(targetID, sourceID)==null)) {    			
    			int count = fAssociationList.getReferenceNumber(sourceID, true) +
    						fAssociationList.getReferenceNumber(sourceID, false);
    			if (count>=fAssociateInteraction.getSimpleAssociableChoiceAt(aSourcePosition).getMatchMax()) {
    				MessageBox mDialog = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
    				mDialog.setText("Message");
    				mDialog.setMessage("The number of associations in which the selected choice is defined as a source is " + count + 
    						". If necessary, please change the value of Max-match of the selected choice.");
    				mDialog.open();
    				return;
    			}
    			count = fAssociationList.getReferenceNumber(targetID, false) +
    					fAssociationList.getReferenceNumber(targetID, true);
    			if (count>=fAssociateInteraction.getSimpleAssociableChoiceAt(aTargetPosition).getMatchMax()) {
    				MessageBox mDialog = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
    				mDialog.setText("Message");
    				mDialog.setMessage("The number of associations in which the selected choice is defined as a target is " + count + 
    						". If necessary, please change the value of Max-match of the selected choice.");
    				mDialog.open();
    				return;
    			}
    			AssociationExtension aAssociationExtension = new AssociationExtension(sourceID, targetID); 
    			fAssociationList.addAssociationExtension(aAssociationExtension);
    			fAssociationTable.setInput(fAssociationList);        		
        		fAssociationTable.setSelection(new StructuredSelection(aAssociationExtension), true);
    		} else {
    			MessageBox mDialog = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
    			mDialog.setText("Message");
    			mDialog.setMessage("This association has already been defined!");
    			mDialog.open();
    			return;
    		}    		
    	} else {
    		MessageBox mDialog = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
			mDialog.setText("Message");
			mDialog.setMessage("Please select one choice from source set and another one from target set!");
			mDialog.open();
			return;
    	}

    }
    
    private void deleteAssociation() {
        Object selected = ((IStructuredSelection)fAssociationTable.getSelection()).getFirstElement();
        
        boolean result = MessageDialog.openQuestion(
                Display.getDefault().getActiveShell(),
                "Delete", "Are you sure you want to delete this association?");
        if (!result) 
        	return;
        
        if (selected instanceof AssociationExtension) {
        	AssociationExtension aAssociationExtension = (AssociationExtension)selected;
        	fAssociationList.removeAssociationExtension(aAssociationExtension);
        	fAssociationTable.setInput(fAssociationList);
        	fAssociationTable.refresh();
        }
    }
    	    
    public void setResponseCorrectAndMapping() {
    	InterpretationValue correctResponses = new InterpretationValue(AssessmentElementFactory.CORRECT_RESPONSE);
    	Mapping aMapping = new Mapping();
    	for (int i=0; i<fAssociationList.size(); i++) {
    		AssociationExtension aAssociationExtension = fAssociationList.getAssociationExtensionAt(i);
    		if (aAssociationExtension.getCorrect()) {
    			Value aValue = new Value(AssessmentElementFactory.PAIR, 
    					aAssociationExtension.getSource()+" "+aAssociationExtension.getTarget());
    			correctResponses.addValue(aValue);
    		}
    		MapEntry aMapEntry = new MapEntry(aAssociationExtension.getSource()+" "+
    				aAssociationExtension.getTarget(), aAssociationExtension.getMappedValue());
    		aMapping.addMapEntry(aMapEntry);
    	}
    	fAssociateInteraction.setMaxAssocations(fAssociationList.size());
    	fAssociateInteraction.getResponseDeclaration().setCorrectResponse(correctResponses);
    	fAssociateInteraction.getResponseDeclaration().setMapping(aMapping);
    	
    }
        
    public AssociateInteraction getAssociateInteraction() {
		return fAssociateInteraction;
	}
    
	/*	
	public EditAssociateListPanel getSourcePanel() {
		return fAssociateListPanel;
	}
	*/
    
	public AssociationExtensionsTableViewer getAssociationTableView() {
		return fAssociationTable;
	}

	
	public AssociationExtensionList getAssociationList() {
		return fAssociationList;
	}
	
    private void updateEditorPanel(Object object) {
        // 
        if(object instanceof Value) {
            //sourceTest TargetText change
         }
    }
    
    private void updateActions(ISelection selection) {
        boolean isEmpty = selection.isEmpty();        
        fDeleteAssociationButton.setEnabled(!isEmpty);
    }	
    	     
}
