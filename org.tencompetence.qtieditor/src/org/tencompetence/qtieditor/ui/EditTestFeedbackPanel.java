package org.tencompetence.qtieditor.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.ui.forms.widgets.FormToolkit;

import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.qtieditor.elements.AssessmentTest;
import org.tencompetence.qtieditor.elements.BasicElementList;
import org.tencompetence.qtieditor.elements.TestFeedback;

public class EditTestFeedbackPanel extends AbstractQuestionEditPanel{

	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	private Composite fEditFeedbacksPanel = null;
	private ResultReportEditPanel fResultReportEditPanel;

	private Button fNewFeedbackButton;
	private Button fOrderFeedbackButton;
	private Label[] fFeedbackLabel = null;
	private Text[] fFeedbackText = null;
	private Text[] fPointText = null;
	private Button[] fIncludeBoundaryButton = null;
	private Button[] fRemoveButton = null;
	private Label[] fProficiencyLevelLabel = null;
	private Text[] fProficiencyLevelText = null;
	private CCombo[] fProficiencyLevelCombo = null;
	
	private String[] choices = { "  ", "1", "2", "3", "4", "5", "6", "7", "8" };
	
	private int fFeedbackNumber = 0;
	private int fMaxFeedbackNumber;
	
	private AssessmentTest fAssessmentTest;
	
	public EditTestFeedbackPanel(Composite parent, 
			AssessmentTest aAssessmentTest , 
			ResultReportEditPanel aResultReportEditPanel, 
			AssessmentTestEditor editor) {
		super(parent, editor);
		fAssessmentTest = aAssessmentTest;
		fResultReportEditPanel = aResultReportEditPanel;
		init();
	}

	private void init() {
		
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayout(gl);
		
		Composite controlEditPanel = toolkit.createComposite(this, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = false;
		controlEditPanel.setLayoutData(gridData);
		
		FormLayout fLayout = new FormLayout();
		controlEditPanel.setLayout(fLayout);
		
		Label aLabel = toolkit.createLabel(controlEditPanel, "Feedback", SWT.NONE);
		aLabel.setFont(AssessmentTestEditor.fontBold9);
		FormData gd1 = new FormData();
		gd1.left = new FormAttachment(0,3);
		gd1.top = new FormAttachment(0,10);
		aLabel.setLayoutData(gd1);
				
		fOrderFeedbackButton = toolkit.createButton(controlEditPanel, "    Ordering    ", SWT.PUSH);
		gd1 = new FormData();
		gd1.left = new FormAttachment(100, -270); 
		gd1.top = new FormAttachment (aLabel, 0, SWT.TOP);
		fOrderFeedbackButton.setLayoutData(gd1);
		fOrderFeedbackButton.setEnabled(false);
		fOrderFeedbackButton.setToolTipText("Re-order the feedbacks according to their points and check whether there is reduplication");
		fOrderFeedbackButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				BasicElementList testFeedbackList = fAssessmentTest.getTestFeedbackList();				
				for (int i = testFeedbackList.size()-1; i>0; i-- ) {
					for (int j = 0; j<i; j++ ) {
						TestFeedback preceed = (TestFeedback)testFeedbackList.getBasicElementAt(j);
						TestFeedback succeed = (TestFeedback)testFeedbackList.getBasicElementAt(j+1);
						if (preceed.getInterpolationTableEntry().getSourceValue()<
								succeed.getInterpolationTableEntry().getSourceValue()) {
							testFeedbackList.setElementAt(j, succeed);
							testFeedbackList.setElementAt(j+1, preceed);
						} else if (preceed.getInterpolationTableEntry().getSourceValue()==
								succeed.getInterpolationTableEntry().getSourceValue()) {
							if (!preceed.getInterpolationTableEntry().getIncludeBoundary() &&
									succeed.getInterpolationTableEntry().getIncludeBoundary()) {
								testFeedbackList.setElementAt(j, succeed);
								testFeedbackList.setElementAt(j+1, preceed);
							}							
						}						
					}
				}
				
				String message = "";
				int count = 0;
				for (int i = 0; i < testFeedbackList.size()-1; i++ ) {
					TestFeedback preceed = (TestFeedback)testFeedbackList.getBasicElementAt(i);
					TestFeedback succeed = (TestFeedback)testFeedbackList.getBasicElementAt(i+1);
					if (preceed.getInterpolationTableEntry().getSourceValue()==
						succeed.getInterpolationTableEntry().getSourceValue()) {
						if (preceed.getInterpolationTableEntry().getIncludeBoundary() ==
							succeed.getInterpolationTableEntry().getIncludeBoundary()) {
							count++;
							message = message + "("+(i+1)+" and "+(i+2)+"), " ;
						}
					}											
				}
				
				for (int i = 0; i < fFeedbackNumber; i++) {
					TestFeedback aTestFeedback = (TestFeedback)testFeedbackList.getBasicElementAt(i);
					fPointText[i].setText(String.valueOf(aTestFeedback.getInterpolationTableEntry().getSourceValue()));
					fFeedbackText[i].setText(aTestFeedback.getData());
					fProficiencyLevelCombo[i].select(aTestFeedback.getInterpolationTableEntry().getProficiencyLevel());
					
					if (((TestFeedback)testFeedbackList
							.getBasicElementAt(i)).getInterpolationTableEntry().getIncludeBoundary())
						fIncludeBoundaryButton[i].setSelection(true);
					else
						fIncludeBoundaryButton[i].setSelection(false);
				}
				
				fEditFeedbacksPanel.layout(true, true);
				fTestEditor.updateQuestionPanel();

				MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
				if (count==0) {
					mDialog.setText("Message");
					if (fFeedbackNumber > 1) {
						mDialog.setMessage("The entries in the interpolation table is now in order.");
					} else {
						mDialog.setMessage("There is only one entry defined.");
					}
				} else if (count==1) {
					mDialog.setText("Warning: replicatedly defined feedback entries");
					mDialog.setMessage("There is a reduplication: " + message + "please check it.");
				} else if (count>1) {
					mDialog.setText("Warning: replicatedly defined feedback entries");
					mDialog.setMessage("There are " + count + " reduplications: " + message + "please check it.");
				}
				mDialog.open();
			}
		});
		fOrderFeedbackButton.pack();

		fNewFeedbackButton = toolkit.createButton(controlEditPanel, "Add Feedback", SWT.PUSH);
		gd1 = new FormData();
		gd1.right = new FormAttachment(100, -3);
		gd1.top = new FormAttachment(aLabel, 0, SWT.TOP);
		fNewFeedbackButton.setLayoutData(gd1);
		fNewFeedbackButton.setToolTipText("Add a feedback");
		fNewFeedbackButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				TestFeedback aTestFeedback = new TestFeedback(fAssessmentTest);
				aTestFeedback.getInterpolationTableEntry().setTargetValue(aTestFeedback.getId());
				fAssessmentTest.addTestFeedback(aTestFeedback);
				addFeedbackPanel(aTestFeedback);
				if (fFeedbackNumber >= 2) {
					fOrderFeedbackButton.setEnabled(true);
				}
				if (fFeedbackNumber >= fMaxFeedbackNumber) {
					fNewFeedbackButton.setEnabled(false);
				}
				fResultReportEditPanel.setDirty(true);
				fEditFeedbacksPanel.layout(true, true);
				fTestEditor.updateQuestionPanel();
			}
		});
		fNewFeedbackButton.pack();
			
		fEditFeedbacksPanel = toolkit.createComposite(this, SWT.NONE);
	    gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL; 
		gridData.verticalAlignment = GridData.FILL; 
		gridData.grabExcessHorizontalSpace = true; 
		gridData.grabExcessVerticalSpace = false;
		fEditFeedbacksPanel.setLayoutData(gridData);
				
		fEditFeedbacksPanel.setLayout(new GridLayout(7, false));
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		
		aLabel = toolkit.createLabel(fEditFeedbacksPanel, "No.", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditFeedbacksPanel, " Point ", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditFeedbacksPanel, "Include Boundary", SWT.CENTER);
		aLabel = toolkit.createLabel(fEditFeedbacksPanel, "Feedback Text", SWT.CENTER);
		toolkit.createLabel(fEditFeedbacksPanel, " ", SWT.CENTER);
		toolkit.createLabel(fEditFeedbacksPanel, "EQF Level", SWT.CENTER);
		toolkit.createLabel(fEditFeedbacksPanel, " ", SWT.CENTER);
		
		fFeedbackNumber = 0;
		fMaxFeedbackNumber = 10;
		
		fFeedbackLabel = new Label[fMaxFeedbackNumber];
		fPointText = new Text[fMaxFeedbackNumber];
		fFeedbackText =new Text[fMaxFeedbackNumber];
		fIncludeBoundaryButton = new Button[fMaxFeedbackNumber];
		fRemoveButton = new Button[fMaxFeedbackNumber];
		fProficiencyLevelLabel = new Label[fMaxFeedbackNumber];		
		fProficiencyLevelText = new Text[fMaxFeedbackNumber];
		fProficiencyLevelCombo = new CCombo[fMaxFeedbackNumber];
		
		BasicElementList aList = fAssessmentTest.getTestFeedbackList();
		for (int i = 0; i < aList.size(); i++) {
			addFeedbackPanel((TestFeedback) aList.getBasicElementAt(i));
		}
		
		if (fFeedbackNumber >= 2) {
			fOrderFeedbackButton.setEnabled(true);
		}
		
		fEditFeedbacksPanel.layout();		
		fResultReportEditPanel.layout();
		layout();
	}
	
	private void addFeedbackPanel(TestFeedback aTestFeedback) {
		
		fFeedbackNumber++;
		fFeedbackLabel[fFeedbackNumber - 1] = toolkit.createLabel(fEditFeedbacksPanel,
				fFeedbackNumber + ": If the score is larger than", SWT.CENTER);

		fPointText[fFeedbackNumber - 1] = toolkit.createText(fEditFeedbacksPanel, 
				String.valueOf(aTestFeedback.getInterpolationTableEntry().getSourceValue()), SWT.BORDER);
		GridData gd3 = new GridData(SWT.NONE, SWT.NONE, true, false);
		gd3.heightHint = 15;
		gd3.widthHint = 25;		
		gd3.horizontalAlignment = SWT.CENTER;
		gd3.verticalAlignment = SWT.CENTER;
		fPointText[fFeedbackNumber - 1].setLayoutData(gd3);
		fPointText[fFeedbackNumber - 1].addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fFeedbackNumber; i++) {
					if (object == fPointText[i])
						index = i;
				}
				if (index > -1) {
					try {
						//Integer.valueOf(getText()).intValue();
						if (fPointText[index].getText().equalsIgnoreCase("")) 
							return;
						double value = Double.valueOf(fPointText[index].getText()).floatValue();
						TestFeedback selectedTestFeedback = ((TestFeedback) fAssessmentTest
								.getTestFeedbackList().getBasicElementAt(index));						
						selectedTestFeedback.getInterpolationTableEntry().setSourceValue(value);	
						fResultReportEditPanel.setDirty(true);
					} catch (Exception event) {
						if ("-".equalsIgnoreCase(fPointText[index].getText()) || 
								("+".equalsIgnoreCase(fPointText[index].getText())))
							return;
						MessageBox mDialog = new MessageBox(getShell(), SWT.OK);
						mDialog.setText("Warning: Wrong input!");
						mDialog.setMessage("You input a non-number: '"+fPointText[index].getText()+ "'. Please input a number!");
						mDialog.open();
						fPointText[index].setText("");
						fResultReportEditPanel.setDirty(true);
					}				
				}
			}
		});

		fIncludeBoundaryButton[fFeedbackNumber - 1] = toolkit.createButton(fEditFeedbacksPanel,
				" then present the feedback: ", SWT.CHECK | SWT.CENTER);
		if (aTestFeedback.getInterpolationTableEntry().getIncludeBoundary())
			fIncludeBoundaryButton[fFeedbackNumber - 1].setSelection(true);
		else
			fIncludeBoundaryButton[fFeedbackNumber - 1].setSelection(false);

		fIncludeBoundaryButton[fFeedbackNumber - 1].addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fFeedbackNumber; i++) {
					if (object == fIncludeBoundaryButton[i])
						index = i;
				}

				if (index > -1) {
					TestFeedback selectedTestFeedback = ((TestFeedback) fAssessmentTest
							.getTestFeedbackList().getBasicElementAt(index));
					
					boolean current = selectedTestFeedback.getInterpolationTableEntry().getIncludeBoundary();
					
					if (current) {
						selectedTestFeedback.getInterpolationTableEntry().setIncludeBoundary(false);
					}
					else {
						selectedTestFeedback.getInterpolationTableEntry().setIncludeBoundary(true);
					}
					fResultReportEditPanel.setDirty(true);
				}
			}
		});

		GridData gd2 = new GridData(GridData.FILL_BOTH);
		gd2.heightHint = 30;
		gd2.widthHint = 350;
		fFeedbackText[fFeedbackNumber - 1] = toolkit.createText(fEditFeedbacksPanel, 
				aTestFeedback.getData(), SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		fFeedbackText[fFeedbackNumber - 1].setLayoutData(gd2);
		fFeedbackText[fFeedbackNumber - 1].addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fFeedbackNumber; i++) {
					if (object == fFeedbackText[i])
						index = i;
				}
				if (index > -1) {
					TestFeedback selectedTestFeedback = (TestFeedback) fAssessmentTest.getTestFeedbackList()
							.getBasicElementAt(index);
					
					selectedTestFeedback.setData(fFeedbackText[index].getText());
					fResultReportEditPanel.setDirty(true);
				}
			}
		});
		
		
		fProficiencyLevelLabel[fFeedbackNumber - 1] = toolkit.createLabel(fEditFeedbacksPanel,
				" and assign the EQF level as: ", SWT.CENTER);
		
		fProficiencyLevelCombo[fFeedbackNumber - 1] = new CCombo(fEditFeedbacksPanel, SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER );
		fProficiencyLevelCombo[fFeedbackNumber - 1].setItems(choices);
		fProficiencyLevelCombo[fFeedbackNumber - 1].select(aTestFeedback.getInterpolationTableEntry().getProficiencyLevel());

		gd3 = new GridData(GridData.FILL_BOTH);
		gd3.heightHint = 15;
		gd3.widthHint = 30;		
		gd3.horizontalAlignment = SWT.CENTER;
		gd3.verticalAlignment = SWT.CENTER;
		fProficiencyLevelCombo[fFeedbackNumber - 1].setLayoutData(gd3);
		toolkit.adapt(fProficiencyLevelCombo[fFeedbackNumber - 1]);
		fProficiencyLevelCombo[fFeedbackNumber - 1].addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Object object = e.getSource();
				int index = -1;
				for (int i = 0; i < fFeedbackNumber; i++) {
					if (object == fProficiencyLevelCombo[i])
						index = i;
				}
				if (index > -1) {
					TestFeedback selectedTestFeedback = ((TestFeedback) fAssessmentTest
							.getTestFeedbackList().getBasicElementAt(index));						
					selectedTestFeedback.getInterpolationTableEntry().setProficiencyLevel(fProficiencyLevelCombo[index].getSelectionIndex());
					fResultReportEditPanel.setDirty(true);
				}
			}
		});
		
		
		fRemoveButton[fFeedbackNumber - 1] = toolkit.createButton(fEditFeedbacksPanel, null, SWT.PUSH | SWT.CENTER);		
		Image anImage = ImageFactory.getImage(ImageFactory.ICON_DELETE);
		if (anImage != null) 
			fRemoveButton[fFeedbackNumber - 1].setImage(anImage);
		else 
			fRemoveButton[fFeedbackNumber - 1].setText("Delete Feedback");
		
		fRemoveButton[fFeedbackNumber - 1].setToolTipText("Delete this feedback");
		fRemoveButton[fFeedbackNumber - 1].addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
								
								if (!MessageDialog.openQuestion(
						                Display.getDefault().getActiveShell(),
						                "Delete",
						                "Are you sure you want to delete this feedback?"))
									return;
								
								Object object = e.getSource();
								int index = -1;
								for (int i = 0; i < fFeedbackNumber; i++) {
									if (object == fRemoveButton[i])
										index = i;
								}
								if (index > -1) {
									removeFeedbackAt(index);
									fNewFeedbackButton.setEnabled(true);
									if (fFeedbackNumber < 2) {
										fOrderFeedbackButton.setEnabled(false);
									}
									setDirty(true);
								}
							}
						});
		fRemoveButton[fFeedbackNumber - 1].pack();
		
		AssessmentTestEditor.changeToContentColour(this);
	}

	private void removeFeedbackAt(int i) {

		fAssessmentTest.getTestFeedbackList().removeElementAt(i);

		if (fIncludeBoundaryButton[i].getSelection()) {
			fIncludeBoundaryButton[i].setSelection(false);
		}
		for (int j = i; j < fFeedbackNumber - 1; j++) {
			fPointText[j].setText(fPointText[j + 1].getText());
			fFeedbackText[j].setText(fFeedbackText[j + 1].getText());
			if (fIncludeBoundaryButton[j + 1].getSelection()) {
				fIncludeBoundaryButton[j].setSelection(true);
				fIncludeBoundaryButton[j + 1].setSelection(false);
			}
			fProficiencyLevelCombo[j].select(fProficiencyLevelCombo[j+1].getSelectionIndex());
		}

		fFeedbackLabel[fFeedbackNumber - 1].dispose();
		fPointText[fFeedbackNumber - 1].dispose();
		fFeedbackText[fFeedbackNumber - 1].dispose();
		fIncludeBoundaryButton[fFeedbackNumber - 1].dispose();
		fProficiencyLevelLabel[fFeedbackNumber - 1].dispose();
		fProficiencyLevelCombo[fFeedbackNumber - 1].dispose();
		fRemoveButton[fFeedbackNumber - 1].dispose();

		fFeedbackNumber--;
	}
	

}

