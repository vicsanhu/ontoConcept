package org.tencompetence.qtieditor.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.ui.forms.widgets.FormToolkit;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.BasicElementList;
import org.tencompetence.qtieditor.elements.BasicElement;
import org.tencompetence.qtieditor.elements.InterpretationValue;
import org.tencompetence.qtieditor.elements.Mapping;
import org.tencompetence.qtieditor.elements.MapEntry;
import org.tencompetence.qtieditor.elements.SliderInteraction;
import org.tencompetence.qtieditor.elements.Value;

public class EditSliderInteractionBlockPanel extends AbstractQuestionEditPanel {

	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	private Text fPromptText;
	//private Composite fSliderControlPanel;
	private Text fLowerBoundText;
	private Text fUpperBoundText;
	private Text fStepText;
	private Scale fScale;
	private Button fDesignSliderButton;
	private Button fStepLabelButton;
	private CCombo fOrientationCombo;
	
	private Button fNewMapEntryButton;
	private Text fSelectionText;
	private Composite fEditMappingPanel = null;
	private Mapping fResponseMapping;

	private Label[] fMapEntryLabel = null;
	private Text[] fMapKeyText = null;
	private Button[] fCorrectButton = null;
	private Text[] fMappedValueText = null;
	private Button[] fRemoveButton = null;
	private Composite[] moveButtons = null;
	private Button[] fUpButton = null;
	private Button[] fDownButton = null;

	private int fMapEntryNumber = 0;
	private int fMaxMapEntryNumber;

	private SliderInteraction fSliderInteraction;

	public EditSliderInteractionBlockPanel(Composite parent,
			AssessmentItem aAssessmentItem,
			SliderInteraction aSliderInteraction, 
			AssessmentTestEditor editor) {

		super(parent, editor);
		fSliderInteraction = aSliderInteraction;
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
		
		createQuestionTitlePanel(this, toolkit, "Slider Question");
		createQuestionTextPanel();	
		createBoundsPanel();
		createSliderPanel();
		createSliderControlPanel();

		setDirty(false);		
		//updateUserInterface();

	}
	
	private void createQuestionTextPanel(){
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
		gd.heightHint = 140; 
		gd.widthHint = 460; 
		fPromptText = toolkit.createText(promptEditPanel, fSliderInteraction.getPrompt().getData(), SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		fPromptText.setLayoutData(gd);
		fPromptText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fSliderInteraction.getPrompt().setData(fPromptText.getText());
				setDirty(true);
			}
		});
	}
	
	private void createBoundsPanel() {

		Composite fEditBoundsPanel =  toolkit.createComposite(this, SWT.NONE);		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 75;
		fEditBoundsPanel.setLayoutData(gridData);

		FormLayout fLayout = new FormLayout();
		fEditBoundsPanel.setLayout(fLayout);
		
		Label aSliderLabel = toolkit.createLabel(fEditBoundsPanel, "Slider Definition", SWT.NONE);
		aSliderLabel.setFont(AssessmentTestEditor.fontBold9);
		FormData aFormData = new FormData();
		aFormData.left = new FormAttachment(0,3);
		aFormData.top = new FormAttachment(0,5);
		aSliderLabel.setLayoutData(aFormData);
		
		Label aLowerBoundLabel = toolkit.createLabel(fEditBoundsPanel, "Lower Bound ", SWT.NONE);
		aFormData = new FormData();
		aFormData.left = new FormAttachment(0,5);
		aFormData.top = new FormAttachment(0,30);
		aLowerBoundLabel.setLayoutData(aFormData);
		
		Label aStepLabel = toolkit.createLabel(fEditBoundsPanel, "Step", SWT.NONE);
		aFormData = new FormData();
		aFormData.left = new FormAttachment(50, -10);
		aFormData.bottom = new FormAttachment(aLowerBoundLabel, 0, SWT.BOTTOM);
		aStepLabel.setLayoutData(aFormData);
		
		Label aUpperBoundLabel = toolkit.createLabel(fEditBoundsPanel, "Upper Bound", SWT.NONE);
		aFormData = new FormData();
		aFormData.right = new FormAttachment(100, -5);
		aFormData.bottom = new FormAttachment(aLowerBoundLabel, 0, SWT.BOTTOM);
		aUpperBoundLabel.setLayoutData(aFormData);

		
		fLowerBoundText = toolkit.createText(fEditBoundsPanel, String.valueOf(fSliderInteraction.getLowerBound()), SWT.BORDER);
		aFormData = new FormData();
		aFormData.left = new FormAttachment(0,5);
		aFormData.top = new FormAttachment(0,50);
		aFormData.height = 15; 
		aFormData.width = 30; 
		fLowerBoundText.setLayoutData(aFormData);
		fLowerBoundText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					if (fLowerBoundText.getText().equalsIgnoreCase(""))
						return;
					
					Integer value = Integer.valueOf(fLowerBoundText.getText());
					System.out.println(fLowerBoundText.getText());
					//fSliderInteraction.setLowerBound(value);
					setDirty(true);
				} catch (Exception event) {
					if ("-".equalsIgnoreCase(fLowerBoundText.getText()) || 
							("+".equalsIgnoreCase(fLowerBoundText.getText())))
						return;
					MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
					mDialog.setText("Warning: Wrong input!");
					mDialog.setMessage("You input a non-integer: '"+fLowerBoundText.getText()+ "'. Please input an integer!");
					mDialog.open();
					fLowerBoundText.setText("");
				}
				setDirty(true);
			}
		});

		
		fStepText = toolkit.createText(fEditBoundsPanel, String.valueOf(fSliderInteraction.getStep()), SWT.BORDER);
		aFormData = new FormData();
		aFormData.left = new FormAttachment(50, -20);
		aFormData.bottom = new FormAttachment(fLowerBoundText, 0, SWT.BOTTOM);
		aFormData.height = 15; 
		aFormData.width = 30; 
		fStepText.setLayoutData(aFormData);
		fStepText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					if (fStepText.getText().equalsIgnoreCase(""))
						return;
					
					Integer value = Integer.valueOf(fStepText.getText());
					System.out.println(fStepText.getText());
					//fSliderInteraction.setStep(value);
					setDirty(true);
				} catch (Exception event) {
					if ("-".equalsIgnoreCase(fStepText.getText()) || 
							("+".equalsIgnoreCase(fStepText.getText())))
						return;
					MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
					mDialog.setText("Warning: Wrong input!");
					mDialog.setMessage("You input a non-integer: '"+fStepText.getText()+ "'. Please input an integer!");
					mDialog.open();
					fStepText.setText("");
				}
				setDirty(true);
			}
		});


		fUpperBoundText = toolkit.createText(fEditBoundsPanel, String.valueOf(fSliderInteraction.getUpperBound()), SWT.BORDER);
		aFormData = new FormData();
		aFormData.right = new FormAttachment(100, -5);
		aFormData.bottom = new FormAttachment(fLowerBoundText, 0, SWT.BOTTOM);
		aFormData.height = 15; 
		aFormData.width = 30; 
		fUpperBoundText.setLayoutData(aFormData);
		fUpperBoundText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				try {
					if (fUpperBoundText.getText().equalsIgnoreCase(""))
						return;
					
					Integer value = Integer.valueOf(fUpperBoundText.getText());
					System.out.println(fUpperBoundText.getText());
					//fSliderInteraction.setUpperBound(value);
					setDirty(true);
				} catch (Exception event) {
					if ("-".equalsIgnoreCase(fUpperBoundText.getText()) || 
							("+".equalsIgnoreCase(fUpperBoundText.getText())))
						return;
					MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
					mDialog.setText("Warning: Wrong input!");
					mDialog.setMessage("You input a non-integer: '"+fUpperBoundText.getText()+ "'. Please input an integer!");
					mDialog.open();
					fUpperBoundText.setText("");
				}
			}
		});
	}
	
	private void createSliderPanel() {
		Composite sliderEditPanel = toolkit.createComposite(this, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL; 
		gridData.verticalAlignment = GridData.FILL; 
		gridData.grabExcessHorizontalSpace = true; 
		gridData.grabExcessVerticalSpace = false;
		sliderEditPanel.setLayoutData(gridData);		
		sliderEditPanel.setLayout(new GridLayout());

		fScale = new Scale (sliderEditPanel, SWT.HORIZONTAL | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;		
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = false;
		gridData.heightHint = 40; 
		gridData.widthHint = 460; 
		fScale.setLayoutData(gridData);
		fScale.setMinimum (fSliderInteraction.getLowerBound());
		fScale.setMaximum (fSliderInteraction.getUpperBound());
		fScale.setPageIncrement (fSliderInteraction.getStep());
		fScale.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				super.mouseUp(e);
				fSelectionText.setText(String.valueOf(fScale.getSelection()));
				updateUserInterface();
			}			
		});		
	}
	
	private void createSliderControlPanel() {

		Composite fSliderControlPanel =  toolkit.createComposite(this, SWT.NONE);		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 65;
		fSliderControlPanel.setLayoutData(gridData);

		FormLayout aFormLayout = new FormLayout();
		fSliderControlPanel.setLayout(aFormLayout);		

		fStepLabelButton = toolkit.createButton(fSliderControlPanel, "Show Step Label", SWT.CHECK);
		FormData aFormData = new FormData();
		aFormData.left = new FormAttachment(0,3);
		aFormData.top = new FormAttachment(0,5);
		fStepLabelButton.setLayoutData(aFormData);
		fStepLabelButton.setToolTipText("Render the choices in this order or randomly");			
		if (fSliderInteraction.getStepLabel())
			fStepLabelButton.setSelection(true);
		fStepLabelButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					fSliderInteraction.setStepLabel(fStepLabelButton.getSelection());
					setDirty(true);
				}
		});
		
		
		Label aLabel = toolkit.createLabel(fSliderControlPanel, "Orientation", SWT.NONE);
		aFormData = new FormData();
		aFormData.right = new FormAttachment(50, -2);
		aFormData.bottom = new FormAttachment(fStepLabelButton, 0, SWT.BOTTOM);
		aLabel.setLayoutData(aFormData);

		String[] Orientations = { AssessmentElementFactory.HORIZONTAL, AssessmentElementFactory.VERTICAL };

		fOrientationCombo = new CCombo(fSliderControlPanel, SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER);
		fOrientationCombo.setItems(Orientations);
		fOrientationCombo.setEnabled(true);
		aFormData = new FormData();
		aFormData.left = new FormAttachment(50, 2);
		aFormData.bottom = new FormAttachment(fStepLabelButton, 5, SWT.BOTTOM);
		fOrientationCombo.setToolTipText("Please select whether the slider should be rendered horizentally or veritically.");
		fOrientationCombo.setLayoutData(aFormData);
		if (fSliderInteraction.getOrientation().equals(AssessmentElementFactory.HORIZONTAL)) {
			fOrientationCombo.select(0);
		} else if (fSliderInteraction.getOrientation().equals(AssessmentElementFactory.VERTICAL)) {
			fOrientationCombo.select(1);
		}
		toolkit.adapt(fOrientationCombo);
		fOrientationCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int index = fOrientationCombo.getSelectionIndex();
				switch (index) {
				case 0: {
					fSliderInteraction.setOrientation(AssessmentElementFactory.HORIZONTAL);
					break;
				}
				case 1: {
					fSliderInteraction.setOrientation(AssessmentElementFactory.VERTICAL);
					break;
				}
				default:
					// ignore everything else
				}
			}
		});

		fDesignSliderButton = toolkit.createButton(fSliderControlPanel, "Design Slider", SWT.PUSH);
		aFormData = new FormData();
		aFormData.right = new FormAttachment(100, -5);
		aFormData.bottom = new FormAttachment(fStepLabelButton, 5, SWT.BOTTOM);
		fDesignSliderButton.setLayoutData(aFormData);
		fDesignSliderButton.setToolTipText("If you want to re-design the slider, " +
				"please modify the values of lower bound and upper bound, " +
				"and select set-step-label and orientation. " +
				"The click this button. The slider will be redrawn according to the modified values.");
		
		fDesignSliderButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	try {
                	if (fLowerBoundText.getText().equals("")) {
                		MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
    					mDialog.setText("Warning: Wrong input!");
    					mDialog.setMessage("You should input an integer for specifying the lower bound!");
    					mDialog.open();
    					return;
                	} 
                	Integer min = Integer.valueOf(fLowerBoundText.getText());
                	
                	if (fUpperBoundText.getText().equals("")) {
                		MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
    					mDialog.setText("Warning: Wrong input!");
    					mDialog.setMessage("You should input an integer for specifying the upper bound!");
    					mDialog.open();
    					return;
                	} 
                	Integer max = Integer.valueOf(fUpperBoundText.getText());

    				if (min>max) {
    					MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
    					mDialog.setText("Warning: Wrong input!");
    					mDialog.setMessage("The value of upper bound should be lager than The value of lower bound!");
    					mDialog.open();
    					return;
    				}
    				
    				if (fStepText.getText().equals("")) {
    					MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
    					mDialog.setText("Warning: Wrong input!");
    					mDialog.setMessage("You should input an integer for specifying the step!");
    					mDialog.open();
    					return;
    				}
    				Integer step = Integer.valueOf(fStepText.getText());

                	fSliderInteraction.setLowerBound(min);
                	fScale.setMinimum (min);            	
    				fSliderInteraction.setUpperBound(max);
    				fScale.setMaximum (max);    				
    				fSliderInteraction.setStep(step);
            		fScale.setPageIncrement(step);

    				setDirty(true);
    				fScale.redraw();				
    				layout();
    				fTestEditor.updateQuestionPanel();
            	} catch (Exception event) {
					if ("-".equalsIgnoreCase(fUpperBoundText.getText()) || 
							("+".equalsIgnoreCase(fUpperBoundText.getText())))
						return;
					MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
					mDialog.setText("Warning: Wrong input!");
					mDialog.setMessage("You input a non-integer: '"+fUpperBoundText.getText()+ "'. Please input an integer!");
					mDialog.open();
					fUpperBoundText.setText("");
				}
			}
		});
		
		
		fResponseMapping = fSliderInteraction.getResponseDeclaration().getMapping();

		Label mappingLabel = toolkit.createLabel(fSliderControlPanel, "Mapping", SWT.NONE);
		mappingLabel.setFont(AssessmentTestEditor.fontBold9);
		FormData gd1 = new FormData();
		gd1.left = new FormAttachment(0,3);
		gd1.top = new FormAttachment(0,40);
		mappingLabel.setLayoutData(gd1);
		
		
		aLabel = toolkit.createLabel(fSliderControlPanel, "Selection", SWT.NONE);
		aFormData = new FormData();
		aFormData.right = new FormAttachment(50, -2);
		aFormData.bottom = new FormAttachment(mappingLabel, 0, SWT.BOTTOM);
		aLabel.setLayoutData(aFormData);

		fSelectionText = toolkit.createText(fSliderControlPanel, "", SWT.BORDER);
		aFormData = new FormData();
		aFormData.left = new FormAttachment(50, 2);
		aFormData.bottom = new FormAttachment(mappingLabel, 5, SWT.BOTTOM);
		aFormData.height = 15; 
		aFormData.width = 30; 
		fSelectionText.setText("0");
		fSelectionText.setLayoutData(aFormData);
		fSelectionText.setEditable(false);

		fNewMapEntryButton = toolkit.createButton(fSliderControlPanel, "Add Map Entry", SWT.PUSH);				
		gd1 = new FormData();
		gd1.right = new FormAttachment(100, -5);
		gd1.bottom = new FormAttachment(mappingLabel, 5, SWT.BOTTOM);
		fNewMapEntryButton.setLayoutData(gd1);
		fNewMapEntryButton.setToolTipText("Add a map entry");
		fNewMapEntryButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				String selectionString = fSelectionText.getText();
				if (fResponseMapping.getMapEntryByKey(selectionString)!=null) {
					MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
					mDialog.setText("Message");
					mDialog.setMessage("This map entry has been defined.");
					mDialog.open();
					return;
				}
				
				MapEntry aMapEntry = new MapEntry(selectionString, 0);
				addMapEntryPanel(aMapEntry);
				fResponseMapping.addMapEntry(aMapEntry);
				if (fMapEntryNumber >= fMaxMapEntryNumber) {
					fNewMapEntryButton.setEnabled(false);
				}
				setDirty(true);
				AssessmentTestEditor.changeToContentColour(fEditMappingPanel);				
				updateUserInterface();
			}
		});
		
			
		fEditMappingPanel = toolkit.createComposite(this, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL; 
		gridData.verticalAlignment = GridData.FILL; 
		gridData.grabExcessHorizontalSpace = true; 
		gridData.grabExcessVerticalSpace = false;
		fEditMappingPanel.setLayoutData(gridData);		
		fEditMappingPanel.setLayout(new GridLayout(6, false));
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;	
		
		aLabel = toolkit.createLabel(fEditMappingPanel, " ", SWT.CENTER);		
		aLabel = toolkit.createLabel(fEditMappingPanel, " ", SWT.CENTER);		
		aLabel = toolkit.createLabel(fEditMappingPanel, "Map Entries", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditMappingPanel,"Correct", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditMappingPanel, "  Score  ", SWT.CENTER);
		aLabel.setLayoutData(gridData);	
		aLabel = toolkit.createLabel(fEditMappingPanel, " ", SWT.CENTER);
		
		fMapEntryNumber = 0;
		fMaxMapEntryNumber = AbstractQuestionEditPanel.maximum;
		
		fMapEntryLabel = new Label[fMaxMapEntryNumber];
		fRemoveButton = new Button[fMaxMapEntryNumber];
		fMapKeyText = new Text[fMaxMapEntryNumber];
		fCorrectButton = new Button[fMaxMapEntryNumber];
		fMappedValueText = new Text[fMaxMapEntryNumber];
		
		moveButtons = new Composite[fMaxMapEntryNumber]; 
		fUpButton = new Button[fMaxMapEntryNumber];
		fDownButton = new Button[fMaxMapEntryNumber];
		
		for (int i = 0; i < fResponseMapping.size(); i++) {
			addMapEntryPanel(fResponseMapping.getMapEntryAt(i));
		}

		if (fMapEntryNumber >= fMaxMapEntryNumber) {
			fNewMapEntryButton.setEnabled(false);
		}
	}

	private void addMapEntryPanel(MapEntry aMapEntry) {
		fMapEntryNumber++;
		
		if (fMapEntryNumber < 10) {
			fMapEntryLabel[fMapEntryNumber - 1] = toolkit.createLabel(fEditMappingPanel, "  " + fMapEntryNumber + ":",
					SWT.CENTER);
		} else {
			fMapEntryLabel[fMapEntryNumber - 1] = toolkit.createLabel(fEditMappingPanel, fMapEntryNumber + ":",
				SWT.CENTER);
		}

		moveButtons[fMapEntryNumber - 1] = toolkit.createComposite(fEditMappingPanel, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.verticalSpacing = 0;
		gl.marginHeight = 0;
		moveButtons[fMapEntryNumber - 1].setLayout(gl);		
		
		fUpButton[fMapEntryNumber - 1] = toolkit.createButton(moveButtons[fMapEntryNumber - 1], null, SWT.ARROW | SWT.UP);
		fUpButton[fMapEntryNumber - 1].setToolTipText("Move this choice up");
		fUpButton[fMapEntryNumber - 1].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fMapEntryNumber; i++) {
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
		fUpButton[fMapEntryNumber - 1].pack();

		fDownButton[fMapEntryNumber - 1] = toolkit.createButton(moveButtons[fMapEntryNumber - 1], null, SWT.ARROW | SWT.DOWN);
		fDownButton[fMapEntryNumber - 1].setToolTipText("Move this choice down");
		fDownButton[fMapEntryNumber - 1].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fMapEntryNumber; i++) {
					if (object == fDownButton[i])
						index = i;
				}
				if (index == fMapEntryNumber -1 ) {
					// on the bottom 
					MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
				    mDialog.setMessage("This choice is on the bottom already.");
				    mDialog.open();
				}
				else if ((index >= 0) && (index < fMapEntryNumber -1)) {
					changeOrderBetween(index, false);
				}				
			}
		});
		fDownButton[fMapEntryNumber - 1].pack();
		
		GridData gd2 = new GridData(SWT.FILL, SWT.NONE, true, false);
		gd2.heightHint = 15;
		gd2.widthHint = 450;
		gd2.verticalAlignment = SWT.CENTER;
		fMapKeyText[fMapEntryNumber - 1] = toolkit.createText(fEditMappingPanel, aMapEntry.getMappedValueString(), SWT.WRAP
				| SWT.BORDER | SWT.V_SCROLL);
		fMapKeyText[fMapEntryNumber - 1].setLayoutData(gd2);
		fMapKeyText[fMapEntryNumber - 1].setEditable(false);
		fMapKeyText[fMapEntryNumber - 1].setText(aMapEntry.getMapKey());

		fCorrectButton[fMapEntryNumber - 1] = toolkit.createButton(fEditMappingPanel, null, SWT.RADIO);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		fCorrectButton[fMapEntryNumber - 1].setLayoutData(gd);
		
		
		// if this choice is correct, select it
		InterpretationValue correctResponses = fSliderInteraction
				.getResponseDeclaration().getCorrectResponse();
		fCorrectButton[fMapEntryNumber - 1].setSelection(false);
		for (int i = 0; i < correctResponses.size(); i++) {
			if (correctResponses.getValueAt(i).getData().equals(aMapEntry.getMapKey()))
				fCorrectButton[fMapEntryNumber - 1].setSelection(true);
		}
		fCorrectButton[fMapEntryNumber - 1]
				.addSelectionListener(new SelectionAdapter() {
		            public void widgetSelected(SelectionEvent e) {
						Object object = e.getSource();
						int index = -1;
						for (int i = 0; i < fMapEntryNumber; i++) {
							if (object == fCorrectButton[i])
								index = i;
						}

						if (index > -1) {
							Value value = new Value(
									AssessmentElementFactory.INTEGER,
									String.valueOf(fMapKeyText[index].getText()));
							InterpretationValue correctResponses = fSliderInteraction
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
		fCorrectButton[fMapEntryNumber - 1].pack();

		fMappedValueText[fMapEntryNumber - 1] = toolkit.createText(fEditMappingPanel,
				"", SWT.BORDER);
		GridData gd3 = new GridData();
		gd3.heightHint = 15;
		gd3.widthHint = 25;		
		fMappedValueText[fMapEntryNumber - 1].setLayoutData(gd3);
		fMappedValueText[fMapEntryNumber - 1].setText(aMapEntry.getMappedValueString());
		fMappedValueText[fMapEntryNumber - 1].addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						Object object = e.getSource();
						int index = -1;
						for (int i = 0; i < fMapEntryNumber; i++) {
							if (object == fMappedValueText[i])
								index = i;
						}
						if (index > -1) {
							// double value = Double.parseDouble(fMappingEntry[index].getText());
							try {
								if (fMappedValueText[index].getText().equalsIgnoreCase(""))
									return;
								
								float value = Double.valueOf(fMappedValueText[index].getText()).floatValue();
								fResponseMapping.updateOrAddMapEntry(fMapKeyText[index].getText(), fMappedValueText[index].getText());
								setDirty(true);
							} catch (Exception event) {
								if ("-".equalsIgnoreCase(fMappedValueText[index].getText()) || 
										("+".equalsIgnoreCase(fMappedValueText[index].getText())))
									return;
								MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
								mDialog.setText("Warning: Wrong input!");
								mDialog.setMessage("You input a non-number: '"+fMappedValueText[index].getText()+ "'. Please input a number!");
								mDialog.open();
								fMappedValueText[index].setText("");
							}
						}
					}
				});
			
		
		fRemoveButton[fMapEntryNumber - 1] = toolkit.createButton(fEditMappingPanel, null, SWT.PUSH | SWT.CENTER);
		Image anImage = ImageFactory.getImage(ImageFactory.ICON_DELETE);
		if (anImage != null) 
			fRemoveButton[fMapEntryNumber - 1].setImage(anImage);
		else 
			fRemoveButton[fMapEntryNumber - 1].setText("Delete Choice");

		fRemoveButton[fMapEntryNumber - 1].setToolTipText("Delete this choice");
		fRemoveButton[fMapEntryNumber - 1]
						.addSelectionListener(new SelectionAdapter() {
				            public void widgetSelected(SelectionEvent e) {
								
								if (!MessageDialog.openQuestion(
										getShell(),
						                "Delete",
						                "Are you sure you want to delete this choice?"))
									return;								
								
								Object object = e.getSource();
								int index = -1;
								for (int i = 0; i < fMapEntryNumber; i++) {
									if (object == fRemoveButton[i])
										index = i;
								}
								if (index > -1) {									
									removeMapEntryPanelAt(index, fResponseMapping.getMapEntryByKey(fMapKeyText[index].getText()));
									fNewMapEntryButton.setEnabled(true);
									setDirty(true);
									updateUserInterface();
								}
							}
						});
		fRemoveButton[fMapEntryNumber - 1].pack();
	}

	private void changeOrderBetween(int i, boolean isUp) {

		MapEntry thisMapEntry = fResponseMapping.getMapEntryAt(i);
		String thisChoiceText = fMapKeyText[i].getText();
		boolean isThisChoiceCorrect = fCorrectButton[i].getSelection();
		if (isUp) {
			// note: the choice has to be changed before the text exchange
			fResponseMapping.setMapEntryAt(i, fResponseMapping.getMapEntryAt(i-1));
			fResponseMapping.setMapEntryAt(i-1, thisMapEntry);
		
			fMapKeyText[i].setText(fMapKeyText[i-1].getText());
			fMapKeyText[i-1].setText(thisChoiceText);
			
			fCorrectButton[i].setSelection(fCorrectButton[i-1].getSelection());
			fCorrectButton[i-1].setSelection(isThisChoiceCorrect);
			
			String thisMappingText = fMappedValueText[i].getText();
			fMappedValueText[i].setText(fMappedValueText[i-1].getText());
			fMappedValueText[i-1].setText(thisMappingText);
			
		} else {
			fResponseMapping.setMapEntryAt(i, fResponseMapping.getMapEntryAt(i+1));
			fResponseMapping.setMapEntryAt(i+1, thisMapEntry);

			fMapKeyText[i].setText(fMapKeyText[i+1].getText());
			fMapKeyText[i+1].setText(thisChoiceText);
			
			fCorrectButton[i].setSelection(fCorrectButton[i+1].getSelection());
			fCorrectButton[i+1].setSelection(isThisChoiceCorrect);		
			
			String thisMappingText = fMappedValueText[i].getText();
			fMappedValueText[i].setText(fMappedValueText[i+1].getText());
			fMappedValueText[i+1].setText(thisMappingText);
		}
	
		updateUserInterface();
		setDirty(true);
	}
	
	private void removeMapEntryPanelAt(int i, MapEntry aMapEntry) {

		InterpretationValue correctResponses = fSliderInteraction.getResponseDeclaration().getCorrectResponse();
		for (int j = 0; j < correctResponses.size(); j++) {
			if (correctResponses.getValueAt(j).getData().equals(aMapEntry.getMapKey()))
				correctResponses.removeValueAt(j);
		}

		Mapping aMapping = fSliderInteraction.getResponseDeclaration().getMapping();
		aMapping.removeMapEntry(aMapEntry);
		
		if (fCorrectButton[i].getSelection()) {
			fCorrectButton[i].setSelection(false);
		}
			
		// update the UI
		for (int j = i; j < fMapEntryNumber - 1; j++) {
			fMapKeyText[j].setText(fMapKeyText[j + 1].getText());
			fMappedValueText[j].setText(fMappedValueText[j + 1].getText());
			if (fCorrectButton[j + 1].getSelection()) {
				fCorrectButton[j].setSelection(true);
				fCorrectButton[j + 1].setSelection(false);
			}
		}

		fMapEntryLabel[fMapEntryNumber - 1].dispose();
		fMapKeyText[fMapEntryNumber - 1].dispose();
		fCorrectButton[fMapEntryNumber - 1].dispose();
		fMappedValueText[fMapEntryNumber - 1].dispose();
		fRemoveButton[fMapEntryNumber - 1].dispose();

		fUpButton[fMapEntryNumber - 1].dispose();
		fDownButton[fMapEntryNumber - 1].dispose();
		moveButtons[fMapEntryNumber - 1].dispose();

		fMapEntryNumber--;
	}

	private void updateUserInterface() {
		fEditMappingPanel.layout();
		layout();
		fTestEditor.updateQuestionPanel();							
	}
}


