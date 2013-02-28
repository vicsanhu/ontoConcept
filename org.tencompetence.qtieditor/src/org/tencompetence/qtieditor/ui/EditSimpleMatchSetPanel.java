package org.tencompetence.qtieditor.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
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
import org.tencompetence.qtieditor.elements.SimpleAssociableChoice;
import org.tencompetence.qtieditor.elements.SimpleMatchSet;

public class EditSimpleMatchSetPanel extends Composite{

	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	private Composite fEditChoicesPanel = null;
	private EditMatchInteractionBlockPanel fEditMatchInteractionBlockPanel;

	private Button fNewChoiceButton;
	private Label[] fChoiceLabel = null;
	private Text[] fChoiceText = null;
	private Button[] fSelectButton = null;
	private Button[] fRemoveButton = null;
	private Composite[] moveButtons = null;
	private Button[] fUpButton = null;
	private Button[] fDownButton = null;
	private CCombo[] fMaxMtachCombo = null;
	
	private String[] choices = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
	
	private int fChoiceNumber = 0;
	private int fMaxChoiceNumber;
	private SimpleAssociableChoice fSelectedChoice = null;


	private SimpleMatchSet fSimpleMatchSet;
	private String fTitle; //"Source Match Set" and "Target Match Set"
	
	private AssessmentTestEditor fTestEditor;
	
	public EditSimpleMatchSetPanel(Composite parent, 
			SimpleMatchSet aSimpleMatchSet, 
			EditMatchInteractionBlockPanel aEditMatchInteractionBlockPanel, 
			AssessmentTestEditor editor,
			String title) {
		super(parent, SWT.BORDER);
		fTestEditor = editor;		
		fSimpleMatchSet = aSimpleMatchSet;
		fEditMatchInteractionBlockPanel = aEditMatchInteractionBlockPanel;
		fTitle = title;
		init();
		AssessmentTestEditor.changeToContentColour(this);
	}

	private void init() {
		
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;		
		setLayout(gl);	
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		setLayoutData(gridData);
		//setBackground(AssessmentTestEditor.getContentColour());

		Composite controlEditPanel =  toolkit.createComposite(this, SWT.NONE);		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 35;
		controlEditPanel.setLayoutData(gridData);
		
		FormLayout fLayout = new FormLayout();
		controlEditPanel.setLayout(fLayout);
		
		Label ansLabel = toolkit.createLabel(controlEditPanel, fTitle, SWT.NONE);
		ansLabel.setFont(AssessmentTestEditor.fontBold9);
		FormData gd1 = new FormData();
		gd1.left = new FormAttachment(0,3);
		gd1.top = new FormAttachment(0,10);
		ansLabel.setLayoutData(gd1);
			
		fNewChoiceButton = toolkit.createButton(controlEditPanel, "Add Choice", SWT.PUSH);		
		gd1 = new FormData();
		gd1.right = new FormAttachment(100, -5);
		gd1.bottom = new FormAttachment(ansLabel, 3, SWT.BOTTOM);
		fNewChoiceButton.setLayoutData(gd1);
		fNewChoiceButton.setToolTipText("Add associable choice");
		fNewChoiceButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				SimpleAssociableChoice aSimpleAssociableChoice= new SimpleAssociableChoice();
				fSimpleMatchSet.addSimpleAssociableChoice(aSimpleAssociableChoice);
				addChoicePanel(aSimpleAssociableChoice);
				if (fChoiceNumber >= fMaxChoiceNumber) {
					fNewChoiceButton.setEnabled(false);
				}
				fEditMatchInteractionBlockPanel.setDirty(true);
				AssessmentTestEditor.changeToContentColour(fEditChoicesPanel);
				fEditChoicesPanel.layout(true, true);
				fEditMatchInteractionBlockPanel.layout(true, true);
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
				
		fEditChoicesPanel.setLayout(new GridLayout(6, false));
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		
		Label aLabel = toolkit.createLabel(fEditChoicesPanel, "   ", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditChoicesPanel, "       ", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditChoicesPanel, "Choice Text", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditChoicesPanel, "Selected", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditChoicesPanel, "Max-match", SWT.CENTER);
		//aLabel.setLayoutData(gridData);
		aLabel = toolkit.createLabel(fEditChoicesPanel, " ", SWT.CENTER);
				
		fChoiceNumber = 0;
		fMaxChoiceNumber = AbstractQuestionEditPanel.maximum;
		
		fChoiceLabel = new Label[fMaxChoiceNumber];
		fChoiceText = new Text[fMaxChoiceNumber];
		fSelectButton = new Button[fMaxChoiceNumber];
		fMaxMtachCombo = new CCombo[fMaxChoiceNumber];
		fRemoveButton = new Button[fMaxChoiceNumber];
		
		moveButtons = new Composite[fMaxChoiceNumber]; 
		fUpButton = new Button[fMaxChoiceNumber];
		fDownButton = new Button[fMaxChoiceNumber];
		
		BasicElementList aList = fSimpleMatchSet.getSimpleAssociableChoiceList();
		for (int i = 0; i < aList.size(); i++) {
			addChoicePanel((SimpleAssociableChoice) aList.getBasicElementAt(i));
		}
		
		if (fChoiceNumber >= fMaxChoiceNumber) {
			fNewChoiceButton.setEnabled(false);
		}
	}
	
	private void addChoicePanel(SimpleAssociableChoice aSimpleAssociableChoice) {
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
					fEditMatchInteractionBlockPanel.setDirty(true);
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
					fEditMatchInteractionBlockPanel.setDirty(true);
				}				
			}
		});
		fDownButton[fChoiceNumber - 1].pack();
		
		GridData gd2 = new GridData(SWT.FILL, SWT.NONE, true, false);
		gd2.heightHint = 20;
		gd2.widthHint = 100; 
		fChoiceText[fChoiceNumber - 1] = toolkit.createText(fEditChoicesPanel, 
				aSimpleAssociableChoice.getData(), SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
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
					SimpleAssociableChoice anSimpleAssociableChoice = (SimpleAssociableChoice) fSimpleMatchSet.getSimpleAssociableChoiceList()
							.getBasicElementAt(index);					
					anSimpleAssociableChoice.setData(fChoiceText[index].getText());
					
					String id = anSimpleAssociableChoice.getId();
					boolean result = fEditMatchInteractionBlockPanel.getAssociationList().isChoiceUsedInAnyAssociationExtension(fTitle, id);
					if (result)
						fEditMatchInteractionBlockPanel.getAssociationTreeTable().setInput(fEditMatchInteractionBlockPanel.getAssociationList());
					
					fEditMatchInteractionBlockPanel.setDirty(true);
				}
			}
		});

		fSelectButton[fChoiceNumber - 1] = toolkit.createButton(fEditChoicesPanel,
					null, SWT.RADIO | SWT.CENTER);
		/*
		 *  check if this choice has been defined in any association
		 *
		if (fSimpleMatchSet.getCorrectChoice()==aSimpleAssociableChoice)
			fCorrectButton[fChoiceNumber - 1].setSelection(true);
		else
			fCorrectButton[fChoiceNumber - 1].setSelection(false);
		*/
		
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		fSelectButton[fChoiceNumber - 1].setLayoutData(gd);

		// if this button is clicked, include/exclude this choice in the correct
		// choice list of the response-declaration
		fSelectButton[fChoiceNumber - 1].addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fChoiceNumber; i++) {
					if (object == fSelectButton[i])
						index = i;
				}

				if (index > -1) {
					fSelectedChoice = fSimpleMatchSet.getSimpleAssociableChoiceAt(index);
				}
			}
		});
		
		fMaxMtachCombo[fChoiceNumber - 1] = new CCombo(fEditChoicesPanel, SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER );
		fMaxMtachCombo[fChoiceNumber - 1].setItems(choices);
		GridData gd3 = new GridData();
		gd3.heightHint = 15;
		gd3.widthHint = 35;		
		fMaxMtachCombo[fChoiceNumber - 1].setLayoutData(gd3);
		fMaxMtachCombo[fChoiceNumber - 1].select(aSimpleAssociableChoice.getMatchMax()-1);
		fMaxMtachCombo[fChoiceNumber - 1].setToolTipText("Please define the maximum of the matches.");
		toolkit.adapt(fMaxMtachCombo[fChoiceNumber - 1]);
		fMaxMtachCombo[fChoiceNumber - 1].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fChoiceNumber; i++) {
					if (object == fMaxMtachCombo[i])
						index = i;
				}
				if (index > -1) {
					SimpleAssociableChoice anSimpleAssociableChoice = (SimpleAssociableChoice) fSimpleMatchSet.getSimpleAssociableChoiceList()
							.getBasicElementAt(index);					
					anSimpleAssociableChoice.setMatchMax(fMaxMtachCombo[index].getSelectionIndex() + 1);
					
					fEditMatchInteractionBlockPanel.setDirty(true);
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
						                "Are you sure you want to delete this choice? Removing this choice results in removing relevant associations."))
									return;
								
								Object object = e.getSource();
								int index = -1;
								for (int i = 0; i < fChoiceNumber; i++) {
									if (object == fRemoveButton[i])
										index = i;
								}
								if (index > -1) {									
									String id = ((SimpleAssociableChoice)fSimpleMatchSet.getSimpleAssociableChoiceList().getBasicElementAt(index)).getId();
									boolean result = fEditMatchInteractionBlockPanel.getAssociationList().removeRelaventAssociationsByID(fTitle, id);
									if (result)
										fEditMatchInteractionBlockPanel.getAssociationTreeTable().setInput(fEditMatchInteractionBlockPanel.getAssociationList());
									
									removeChoiceAt(index);
									fNewChoiceButton.setEnabled(true);
									fEditMatchInteractionBlockPanel.setDirty(true);									
									layout();
									fEditMatchInteractionBlockPanel.layout();
								}
							}
		});
		fRemoveButton[fChoiceNumber - 1].pack();
	}
	

	private void changeOrderBetween(int i, boolean isUp) {

		SimpleAssociableChoice thisChoice = (SimpleAssociableChoice) fSimpleMatchSet
			.getSimpleAssociableChoiceList().getBasicElementAt(i);
		String thisChoiceText = fChoiceText[i].getText();
		boolean isThisChoiceCorrect = fSelectButton[i].getSelection();
		if (isUp) {
			// note: the choice has to be changed before the text exchange
			fSimpleMatchSet.getSimpleAssociableChoiceList().setElementAt(i, fSimpleMatchSet.getSimpleAssociableChoiceList().getBasicElementAt(i-1));
			fSimpleMatchSet.getSimpleAssociableChoiceList().setElementAt(i-1, thisChoice);
		
			fChoiceText[i].setText(fChoiceText[i-1].getText());
			fChoiceText[i-1].setText(thisChoiceText);
			
			fSelectButton[i].setSelection(fSelectButton[i-1].getSelection());
			fSelectButton[i-1].setSelection(isThisChoiceCorrect);
			
		} else {
			fSimpleMatchSet.getSimpleAssociableChoiceList().setElementAt(i, fSimpleMatchSet.getSimpleAssociableChoiceList().getBasicElementAt(i+1));
			fSimpleMatchSet.getSimpleAssociableChoiceList().setElementAt(i+1, thisChoice);

			fChoiceText[i].setText(fChoiceText[i+1].getText());
			fChoiceText[i+1].setText(thisChoiceText);
			
			fSelectButton[i].setSelection(fSelectButton[i+1].getSelection());
			fSelectButton[i+1].setSelection(isThisChoiceCorrect);			
		}

		fEditMatchInteractionBlockPanel.setDirty(true);
	}
	
	private void removeChoiceAt(int i) {

		/* check whether this choice has been defined in any association

		SimpleAssociableChoice correctChoice = fSimpleMatchSet.getCorrectChoice();		
		SimpleAssociableChoice thisChoice = (SimpleAssociableChoice) fSimpleMatchSet
			.getSimpleAssociableChoiceList().getBasicElementAt(i); 
		if (correctChoice==thisChoice) {
			MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
		    mDialog.setMessage("It is not allowed to remove a correct choice.");
		    mDialog.open();
		    return;
		}
		*/
		
		fSimpleMatchSet.getSimpleAssociableChoiceList().removeElementAt(i);

		if (fSelectButton[i].getSelection()) {
			fSelectButton[i].setSelection(false);
		}
		for (int j = i; j < fChoiceNumber - 1; j++) {
			fChoiceText[j].setText(fChoiceText[j + 1].getText());
			fMaxMtachCombo[j].select(fMaxMtachCombo[j + 1].getSelectionIndex());
			if (fSelectButton[j + 1].getSelection()) {
				fSelectButton[j].setSelection(true);
				fSelectButton[j + 1].setSelection(false);
			}			
		}

		fChoiceLabel[fChoiceNumber - 1].dispose();
		fChoiceText[fChoiceNumber - 1].dispose();
		fSelectButton[fChoiceNumber - 1].dispose();
		fMaxMtachCombo[fChoiceNumber - 1].dispose();
		fRemoveButton[fChoiceNumber - 1].dispose();
		
		fUpButton[fChoiceNumber - 1].dispose();
		fDownButton[fChoiceNumber - 1].dispose();
		moveButtons[fChoiceNumber - 1].dispose();
		
		fChoiceNumber--;
	}
	
	public SimpleMatchSet getSimpleMatchSet() {
		return fSimpleMatchSet;
	}
	
	public SimpleAssociableChoice getSelectedChoice() {
		return fSelectedChoice;
	}
}



