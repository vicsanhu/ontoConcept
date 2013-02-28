package org.tencompetence.qtieditor.ui;

 

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.qtieditor.elements.AssessmentTest;
import org.tencompetence.qtieditor.elements.TestResultReport;

public class ResultReportEditPanel extends AbstractQuestionEditPanel {

	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	private Button fScoreButton;
	private Button fCorrectButton;
	private Button fIncorrectButton;
	private Button fPresentedButton;	
	private Button fRespondedButton;
	private Button fSelectedButton;
	private Button fPercentageButton;

	private AssessmentTest fAassessmen;
	private TestResultReport fTestResultReport;
	
	private static String display = "   Would you like to present the candidate the ";
	
	public ResultReportEditPanel(Composite parent,
			AssessmentTest aAassessmen,
			AssessmentTestEditor editor) {

		super(parent, editor);
		fAassessmen = aAassessmen;
		fTestResultReport = aAassessmen.getTestResultReport();
		init();
	}

	private void init() {
		
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		setLayout(gl);
		
		Composite anEditPanel = toolkit.createComposite(this, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = false;
		anEditPanel.setLayoutData(gridData);
		
		anEditPanel.setLayout(new GridLayout());
		Label aLabel = toolkit.createLabel(anEditPanel, "  Which test results will be presented to the candidate?", SWT.NONE);
		aLabel.setFont(AssessmentTestEditor.fontBold9);
		
		createResultPanel(anEditPanel);
		EditTestFeedbackPanel aPanel = new EditTestFeedbackPanel(anEditPanel, fAassessmen, this, fTestEditor);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		aPanel.setLayoutData(gd);
		toolkit.adapt(aPanel);
		
		AssessmentTestEditor.changeToContentColour(this);
		setDirty(false);
	}

	
	private void createResultPanel(Composite editPanel){
		Composite buttonBar = toolkit.createComposite(editPanel, SWT.NONE);
		buttonBar.setLayout(new RowLayout(SWT.VERTICAL));
		
		
		fScoreButton = new Button(buttonBar, SWT.CHECK);
		fScoreButton.setText("score");
		fScoreButton.setSelection(fTestResultReport.getScore());
		fScoreButton.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				fTestResultReport.setScore(!fTestResultReport.getScore());
				setDirty(true);
			}
		});
		fScoreButton.pack();
		
		fCorrectButton = toolkit.createButton(buttonBar, display + "number of correct responses?", SWT.CHECK);
		fCorrectButton.setSelection(fTestResultReport.getNumberCorrect());
		fCorrectButton.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				fTestResultReport.setNumberCorrect(!fTestResultReport.getNumberCorrect());
				setDirty(true);
			}
		});
		fCorrectButton.pack();
				
		fIncorrectButton = toolkit.createButton(buttonBar, display + "number of incorrect responses?", SWT.CHECK);
		fIncorrectButton.setSelection(fTestResultReport.getNumberIncorrect());
		fIncorrectButton.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				fTestResultReport.setNumberIncorrect(!fTestResultReport.getNumberIncorrect());
				setDirty(true);
			}
		});
		fIncorrectButton.pack();
		
		fPresentedButton = toolkit.createButton(buttonBar, display + "number of presented questions?", SWT.CHECK);
		fPresentedButton.setSelection(fTestResultReport.getNumberPresented());
		fPresentedButton.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				fTestResultReport.setNumberPresented(!fTestResultReport.getNumberPresented());
				setDirty(true);
			}
		});
		fPresentedButton.pack();	
		
		fRespondedButton = toolkit.createButton(buttonBar, display + "number of responded questions?", SWT.CHECK);
		fRespondedButton.setSelection(fTestResultReport.getNumberResponded());
		fRespondedButton.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				fTestResultReport.setNumberResponded(!fTestResultReport.getNumberResponded());
				setDirty(true);
			}
		});
		fRespondedButton.pack();	
		
		fSelectedButton = toolkit.createButton(buttonBar, display + "number of selected questions?", SWT.CHECK);
		fSelectedButton.setSelection(fTestResultReport.getNumberSelected());
		fSelectedButton.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				fTestResultReport.setNumberSelected(!fTestResultReport.getNumberSelected());
				setDirty(true);
			}
		});
		fSelectedButton.pack();	
				
		fPercentageButton = toolkit.createButton(buttonBar, display + "overall percentage of correct answers?", SWT.CHECK);
		fPercentageButton.setSelection(fTestResultReport.getPercentage());
		fPercentageButton.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				fTestResultReport.setPercentage(!fTestResultReport.getPercentage());
				setDirty(true);
			}
		});
		fPercentageButton.pack();	
	}

}