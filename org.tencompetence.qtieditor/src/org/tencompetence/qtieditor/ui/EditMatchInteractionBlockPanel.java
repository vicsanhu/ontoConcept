package org.tencompetence.qtieditor.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.tencompetence.qtieditor.elements.MatchInteraction;
import org.tencompetence.qtieditor.elements.InterpretationValue;
import org.tencompetence.qtieditor.elements.MapEntry;
import org.tencompetence.qtieditor.elements.Mapping;
import org.tencompetence.qtieditor.elements.SimpleAssociableChoice;
import org.tencompetence.qtieditor.elements.Value;
import org.tencompetence.qtieditor.elements.AssociationExtensionList;


public class EditMatchInteractionBlockPanel extends AbstractQuestionEditPanel {

	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	
	private Text fPromptText;
	private Button fNewAssociationButton;
	private Button fDeleteAssociationButton;
	private Button fShuffleButton;

	private Composite fEditMatchSetsPanel;
	private EditSimpleMatchSetPanel fSourcePanel;	
	private EditSimpleMatchSetPanel fTargetPanel;
	private Composite fEditAssociationsPanel;
	//private AssociationTreeTable fAssociationTreeTable;
	private AssociationExtensionsTableViewer fAssociationTable;

	private MatchInteraction fMatchInteraction;
	//private String fQuestionClass;

	private AssociationExtensionList fAssociationList = new AssociationExtensionList();
	
	public EditMatchInteractionBlockPanel(Composite parent,
			AssessmentItem aAssessmentItem,
			MatchInteraction aMatchInteraction, 
			AssessmentTestEditor editor) {

		super(parent, editor);
		fMatchInteraction = aMatchInteraction;
		fAssessmentItem = aAssessmentItem;
		init();
		AssessmentTestEditor.changeToContentColour(this);
	}

	public void init() {
		
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayout(gl);
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		setLayoutData(gridData);
		
		createQuestionTitlePanel(this, toolkit, "Match Question");
		createQuestionTextPanel();
        createAssicationPanel();
		createMatchSetPanel();
		
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
		fPromptText = toolkit.createText(promptEditPanel, fMatchInteraction.getPrompt().getData(), SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		fPromptText.setLayoutData(gd);
		fPromptText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fMatchInteraction.getPrompt().setData(fPromptText.getText());
				setDirty(true);
			}
		});
	}
	
	private void createMatchSetPanel() {
		//create two match sets
		fEditMatchSetsPanel = toolkit.createComposite(this, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL; 
		gridData.grabExcessHorizontalSpace = true; 
		fEditMatchSetsPanel.setLayoutData(gridData);
		
		fEditMatchSetsPanel.setLayout(new GridLayout(2, false));	

		fSourcePanel = new EditSimpleMatchSetPanel(fEditMatchSetsPanel, 
				fMatchInteraction.getSourceSimpleMatchSet(), 
				this, fTestEditor, 
				AssessmentElementFactory.SOURCE_TITLE);
		
		
		fTargetPanel = new EditSimpleMatchSetPanel(fEditMatchSetsPanel, 
				fMatchInteraction.getTargetSimpleMatchSet(), 
				this, fTestEditor, 
				AssessmentElementFactory.TARGET_TITLE);
	}
	
	private void createAssicationPanel() {
		//create association table
		fEditAssociationsPanel = toolkit.createComposite(this, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		//gridData.grabExcessHorizontalSpace = true;
		//gridData.heightHint = 35;
		fEditAssociationsPanel.setSize(460, 100);
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
				setDirty(true);
				fEditMatchSetsPanel.layout();
				layout();
				fTestEditor.updateQuestionPanel();
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
				setDirty(true);
				fEditMatchSetsPanel.layout();
				layout();
				fTestEditor.updateQuestionPanel();
			}
		});
		
        Composite composite = new Composite(this, SWT.NULL);
        //composite.setLayout(new TreeColumnLayout());
        composite.setLayout(new TableColumnLayout());
        gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 100;
		gridData.widthHint = 700;
        composite.setLayoutData(gridData);
        fAssociationTable = new AssociationExtensionsTableViewer(composite, fMatchInteraction, this);
        
        InterpretationValue aInterpretationValue = fMatchInteraction.getResponseDeclaration().getCorrectResponse();
        for (int i=0; i<aInterpretationValue.size(); i++) {
        	String valueData = aInterpretationValue.getValueAt(i).getData();
        	int index = valueData.indexOf(" ");
        	if (index!=-1) {
        		String id = valueData.substring(0, index);
        		SimpleAssociableChoice sourceChoice = fMatchInteraction.getSourceSimpleMatchSet().getSimpleAssociableChoiceByID(id);
        		id = valueData.substring(index+1);
        		SimpleAssociableChoice targetChoice = fMatchInteraction.getTargetSimpleMatchSet().getSimpleAssociableChoiceByID(id);
        		AssociationExtension aAssociationExtension = new AssociationExtension(sourceChoice.getId(), targetChoice.getId());
        		fAssociationList.addAssociationExtension(aAssociationExtension);
        	}
        }
         
        Mapping aMapping = fMatchInteraction.getResponseDeclaration().getMapping();
        for (int i=0; i<aMapping.size(); i++) {
        	String valueData = aMapping.getMapEntryAt(i).getMapKey();        	
        	int index = valueData.indexOf(" ");
        	if (index!=-1) {
        		String id = valueData.substring(0, index);
        		SimpleAssociableChoice sourceChoice = fMatchInteraction.getSourceSimpleMatchSet().getSimpleAssociableChoiceByID(id);
        		id = valueData.substring(index+1);
        		SimpleAssociableChoice targetChoice = fMatchInteraction.getTargetSimpleMatchSet().getSimpleAssociableChoiceByID(id);
        		
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

	
	public MatchInteraction getMatchInteraction() {
		return fMatchInteraction;
	}
	
	public EditSimpleMatchSetPanel getSourcePanel() {
		return fSourcePanel;
	}
	
	public EditSimpleMatchSetPanel getTargetPanel() {
		return fTargetPanel;
	}
	
	public AssociationExtensionsTableViewer getAssociationTreeTable() {
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
    
    
    private void addNewAssociation() {        
        if (fSourcePanel.getSelectedChoice()!=null && (fTargetPanel.getSelectedChoice()!=null)) {
    		String sourceID = fSourcePanel.getSelectedChoice().getId();
    		String targetID = fTargetPanel.getSelectedChoice().getId();
    		AssociationExtension aAssociationExtension = fAssociationList.getAssociationExtensionBySourceIDTargetID(sourceID, targetID);
    		if (aAssociationExtension==null) {
    			
    			int count = fAssociationList.getReferenceNumber(sourceID, true);
    			if (count>=fSourcePanel.getSelectedChoice().getMatchMax()) {
    				MessageBox mDialog = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
    				mDialog.setText("Message");
    				mDialog.setMessage("The number of associations in which the selected choice is defined as a source is " + count + 
    						". If necessary, please change the value of Max-match of the selected choice.");
    				mDialog.open();
    				return;
    			}
    			count = fAssociationList.getReferenceNumber(targetID, false);
    			if (count>=fTargetPanel.getSelectedChoice().getMatchMax()) {
    				MessageBox mDialog = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
    				mDialog.setText("Message");
    				mDialog.setMessage("The number of associations in which the selected choice is defined as a target is " + count + 
    						". If necessary, please change the value of Max-match of the selected choice.");
    				mDialog.open();
    				return;
    			}
    			aAssociationExtension = new AssociationExtension(sourceID, targetID); 
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
    			Value aValue = new Value(AssessmentElementFactory.DIRECTED_PAIR, 
    					aAssociationExtension.getSource()+" "+aAssociationExtension.getTarget());
    			correctResponses.addValue(aValue);
    		}
    		MapEntry aMapEntry = new MapEntry(aAssociationExtension.getSource()+" "+
    				aAssociationExtension.getTarget(), aAssociationExtension.getMappedValue());
    		aMapping.addMapEntry(aMapEntry);
    	}
    	fMatchInteraction.setMaxAssocations(fAssociationList.size());
    	fMatchInteraction.getResponseDeclaration().setCorrectResponse(correctResponses);
    	fMatchInteraction.getResponseDeclaration().setMapping(aMapping);
    }
}

