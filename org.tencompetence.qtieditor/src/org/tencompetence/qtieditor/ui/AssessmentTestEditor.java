package org.tencompetence.qtieditor.ui;

import java.io.IOException;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.qtieditor.elements.PBlock;
import org.tencompetence.qtieditor.elements.SectionPart;
import org.tencompetence.qtieditor.elements.TestPart;
import org.tencompetence.qtieditor.serialization.QTIEditorInput;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.AssessmentItemRef;
import org.tencompetence.qtieditor.elements.AssessmentSection;
import org.tencompetence.qtieditor.elements.AssessmentTest;
import org.tencompetence.qtieditor.elements.BasicElementList;
import org.tencompetence.qtieditor.elements.Block;
import org.tencompetence.qtieditor.elements.ChoiceInteraction;
import org.tencompetence.qtieditor.elements.ExtendedTextInteraction;
import org.tencompetence.qtieditor.elements.FillingInBlankBlock;
import org.tencompetence.qtieditor.elements.InlineChoiceBlock;
import org.tencompetence.qtieditor.elements.MatchInteraction;
import org.tencompetence.qtieditor.elements.AssociateInteraction;
import org.tencompetence.qtieditor.elements.OrderInteraction;
import org.tencompetence.qtieditor.elements.GapMatchInteraction;
import org.tencompetence.qtieditor.elements.HottextInteraction;
import org.tencompetence.qtieditor.elements.SliderInteraction;

public class AssessmentTestEditor extends EditorPart {

	public static final String ID = "org.tencompetence.qtieditor.editor"; // for
																			// plug-in

	private static final int EDIT_NO_ITEM = 1;
	private static final int EDIT_ONE_ITEM = 2;
	private static final int EDIT_FIRST_ITEM = 3;
	private static final int EDIT_MIDDLE_ITEM = 4;
	private static final int EDIT_LAST_ITEM = 5;

	private Text fTitleText;
	//private Button fSaveTestButton;
	//private Button fRemoveTestButton;
	private Button fQuestionExampleButton;
	private Button fNewQuestionButton;
	private Button fRemoveQuestionButton;
	private Button fReportResultButton;

	private CCombo fQuestionTypeCombo;
	private CCombo fChoiceNumberCombo;

	// item and interaction edit panels
	private Composite fEmptyEditPanel = null;
	private Composite fAssessmentTestEditorPanel = null;
	private StackComposite fQuestionStackPanel = null;

	// navigation
	private Button fFirstButton;
	private Button fPreviousButton;
	private Button fNextButton;
	private Button fLastButton;
	private Label fPositionNumberLabel;

	private int fCurrentPosition = -1;
	private int fQuestionCount = 0;
	private boolean fResultReportFlag = true;
	private boolean[] fButtonStates = new boolean[6];

	private AssessmentTest fAssessmentTest;
	private AssessmentSection fAssessmentSection;

	private int fQuestionChoiceNumber = 0;
	private BasicElementList fQuestionList = new BasicElementList();
	private Vector fQuestionEditPanelList = new Vector();

	private ScrolledComposite fScrolledComposite;
	private ResultReportEditPanel fResultReportEditPanel = null;
	private Composite parent;
	private QTIEditorInput qtiEditorInput;
		
	/**
	 * Dirty flag
	 */
	private boolean fDirty;

	/**
	 * Form
	 */
	private Form fForm;

	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	
	private static Color header_colour;
	private static Color controlbar_colour;
	private static Color content_colour;
	static Font fontBold12;
	static Font font9;
	static Font fontBold9;

	
	@Override
	public void doSave(IProgressMonitor monitor) {
		saveTest();
	}

	public void setDirty(boolean dirty) {
		if (fDirty != dirty) {
			fDirty = dirty;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return fDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
		init();
	}

	private void init() {

		ProcessTest();
		createFonts();
		
		fForm = toolkit.createForm(parent);

		GridLayout gl = new GridLayout();
		fForm.getBody().setLayout(gl);
		
		header_colour = new Color(fForm.getDisplay(), 208,224,238);		
		controlbar_colour = new Color(fForm.getDisplay(), 179,205,228);
		content_colour = new Color(fForm.getDisplay(), 234,241,247);

		// create UI of the tool
		fScrolledComposite = new ScrolledComposite(fForm.getBody(),
				SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		fScrolledComposite.setLayoutData(gd);
		toolkit.adapt(fScrolledComposite);		
		
		fAssessmentTestEditorPanel = new Composite(fScrolledComposite, SWT.NONE);	
		toolkit.adapt(fAssessmentTestEditorPanel);

		gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		gl.marginWidth = 0;
		gl.marginHeight = 0;		
		fAssessmentTestEditorPanel.setLayout(gl);
		
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.grabExcessVerticalSpace = true;
		
		fAssessmentTestEditorPanel.setLayoutData(gd);
		fAssessmentTestEditorPanel.setBackground(content_colour);
		
		Composite testEditPanel = createTestEditPanel();
		changeBackGroundColour(testEditPanel, header_colour);
		gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gd.heightHint = 90;
		testEditPanel.setLayoutData(gd);
	   
		
		Composite navigationControlPanel =  createNavigationControlPanel();
		changeBackGroundColour(navigationControlPanel, controlbar_colour);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.heightHint = 80;		
		navigationControlPanel.setLayoutData(gd);
		

		fQuestionStackPanel = new StackComposite(fAssessmentTestEditorPanel, SWT.NONE);
		fQuestionStackPanel.setBackground(content_colour);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		fQuestionStackPanel.setLayoutData(gd);
		toolkit.adapt(fQuestionStackPanel);
	
		
		fEmptyEditPanel = toolkit.createComposite(fQuestionStackPanel, SWT.NONE);
		fEmptyEditPanel.setBackground(content_colour);
		gd = new GridData(SWT.LEFT, SWT.END, true, false);
		fEmptyEditPanel.setLayoutData(gd);
		
		fResultReportEditPanel = new ResultReportEditPanel(fQuestionStackPanel,
				fAssessmentTest, this);
		toolkit.adapt(fResultReportEditPanel);
		fResultReportEditPanel.setBackground(content_colour);
		gd = new GridData(SWT.LEFT, SWT.END, true, false);
		fResultReportEditPanel.setLayoutData(gd);
		
		createQuestionPanels();
		setQuestionControlPanel();
		fScrolledComposite.setContent(fAssessmentTestEditorPanel);
		toolkit.paintBordersFor(fAssessmentTestEditorPanel);
		// Set the minimum size
		fScrolledComposite.setMinSize(700,950);		
		
		// Expand both horizontally and vertically
		fScrolledComposite.setExpandHorizontal(true);
		fScrolledComposite.setExpandVertical(true);
		fAssessmentTestEditorPanel.layout();
		fAssessmentTestEditorPanel.pack();
		
		setDirty(false);
		updateQuestionPanel();
	}
	
	private void ProcessTest() {
		// get the test data
		qtiEditorInput = (QTIEditorInput) getEditorInput();

		fAssessmentTest = qtiEditorInput.getModel().getAssessmentTest();
		if (fAssessmentTest==null) {
			MessageBox mDialog = new MessageBox(
					fAssessmentTestEditorPanel.getShell(), SWT.OK);
			mDialog.setText("Message");
			mDialog.setMessage("The test can not be edited anymore!");
			mDialog.open();
			return; 		
		}
		qtiEditorInput.setTestID(fAssessmentTest.getId());
		if (fAssessmentTest.getTestPartList().size() == 0) {
			TestPart firstTestPart = new TestPart(fAssessmentTest);
			fAssessmentTest.addTestPart(firstTestPart);
			firstTestPart.addAssessmentSection(new AssessmentSection(
					fAssessmentTest));
		}
		fAssessmentSection = fAssessmentTest.getFirstTestPart()
				.getFirstAssessmentSection();
		BasicElementList aSectionPartList = fAssessmentSection
				.getSectionPartList();
		BasicElementList newSectionPartList = new BasicElementList();
		boolean unfound = false;
		
		for (int i = 0; i < aSectionPartList.size(); i++) {
			SectionPart aSectionPart = (SectionPart) aSectionPartList
					.getBasicElementAt(i);
			if (aSectionPart instanceof AssessmentItemRef) {
				String href = ((AssessmentItemRef) aSectionPart).getHref();				
				AssessmentItem aAssessmentItem = qtiEditorInput
						.loadAssessmentItem(href);
				if (aAssessmentItem != null) {
					fQuestionList.addElement(aAssessmentItem);
					newSectionPartList.addElement(aSectionPart);
				} else {
					unfound = true;
				}
			} else {
				// handle section in later version
			}
		}
		if (unfound) {
			if (!MessageDialog.openQuestion(
	                Display.getDefault().getActiveShell(),
	                "Question",
	                "Would you like to edit this test even though certain item file is missing?"))
				return;
		}
		fAssessmentSection.setSectionPartList(newSectionPartList);
	}

	private Composite createTestEditPanel() {
		Composite testEditPanel = toolkit.createComposite(
				fAssessmentTestEditorPanel, SWT.NONE);
		testEditPanel.setBackground(header_colour);
		FormLayout layout = new FormLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		testEditPanel.setLayout(layout);
		toolkit.paintBordersFor(testEditPanel);

		Label label = toolkit.createLabel(testEditPanel, "Questionnaire",
				SWT.LEFT_TO_RIGHT);		
		label.setFont(fontBold12);
		FormData data1 = new FormData();
		data1.left = new FormAttachment(0, 5);
		label.setLayoutData(data1);

		Label titleLabel = toolkit.createLabel(testEditPanel, "Title",
				SWT.LEFT_TO_RIGHT);
		titleLabel.setFont(fontBold9);
		data1 = new FormData();
		data1.left = new FormAttachment(0, 7);
		data1.top = new FormAttachment(label, 15);
		titleLabel.setLayoutData(data1);

		Text fTitleText = toolkit.createText(testEditPanel, "", SWT.BORDER);
		fTitleText.setToolTipText("Please input test title here");		
		if (fAssessmentTest.getTitle().equalsIgnoreCase(""))
			fTitleText.setSize(350, 18);
		else
			fTitleText.setText(fAssessmentTest.getTitle());
		data1 = new FormData();
		data1.left = new FormAttachment(0, 5);
		data1.right = new FormAttachment(100, -170); //50, 0
		data1.top = new FormAttachment(titleLabel, 5);
		fTitleText.setLayoutData(data1);

		fTitleText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String title = ((Text) e.getSource()).getText();
				fAssessmentTest.setTitle(title);
				setDirty(true);
			}
		});
		
		fReportResultButton = toolkit.createButton(testEditPanel,
				"Report Results", SWT.PUSH);
		//fReportResultButton.setBackground(header_colour);
		fReportResultButton.setEnabled(true);
		fReportResultButton.setToolTipText("Define the report of test results");
		data1 = new FormData();
		data1.right = new FormAttachment(100, -5);
		data1.top = new FormAttachment(titleLabel, 5);
		fReportResultButton.setLayoutData(data1);
		fReportResultButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	if (fResultReportFlag) {
					fQuestionStackPanel.setControl(fResultReportEditPanel);					
				} else if (fCurrentPosition>0) {
					fQuestionStackPanel.setControl((Composite) fQuestionEditPanelList
							.get(fCurrentPosition-1));			
				} else {
					fQuestionStackPanel.setControl(fEmptyEditPanel);
				}
				changeResultReportButton();
				updateQuestionPanel();
            }
        });


		/*		
		fRemoveTestButton = toolkit.createButton(testEditPanel, "Delete Test",
				SWT.PUSH);
		fRemoveTestButton.setEnabled(true);
		fRemoveTestButton.setToolTipText("Delete the currently edited test and associated questions");		
		data1 = new FormData();
		data1.right = new FormAttachment(100, -5);
		data1.bottom = new FormAttachment(titleLabel, 0, SWT.BOTTOM);
		fRemoveTestButton.setLayoutData(data1);
		fRemoveTestButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				
				if (!MessageDialog.openQuestion(
		                Display.getDefault().getActiveShell(),
		                "Delete",
		                "Are you sure you want to delete this test?"))
					return;
				
				BasicElementList aSectionPartList = fAssessmentSection
						.getSectionPartList();
				for (int i = 0; i < aSectionPartList.size(); i++) {
					SectionPart aSectionPart = (SectionPart) aSectionPartList
							.getBasicElementAt(i);
					if (aSectionPart instanceof AssessmentItemRef) {
						String href = ((AssessmentItemRef) aSectionPart).getHref();
						
						qtiEditorInput.removeAssessmentItem(href);
					}
				}

				try {
					qtiEditorInput.removeAssessmentTest();
				} catch (IOException event) {
					System.err.println("something wrong when removing an assessmentTest!");
				}
				setDirty(false);
			}
		});

		fSaveTestButton = toolkit.createButton(testEditPanel, "Save Test",
				SWT.PUSH);
		//fSaveTestButton.setBackground(header_colour);
		fSaveTestButton.setEnabled(true);
		fSaveTestButton.setToolTipText("Save the currently edited test and associated questions");
		data1 = new FormData();
		data1.right = new FormAttachment(100, -5);
		data1.left = new FormAttachment(fRemoveTestButton, 0, SWT.LEFT);
		data1.top = new FormAttachment(fReportResultButton, 5, SWT.TOP);
		fSaveTestButton.setLayoutData(data1);

		fSaveTestButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				saveTest();
			}
		});
		*/
		
		return testEditPanel;
	}

	private Composite createNavigationControlPanel() {
		Composite navigationEditPanel = toolkit
				.createComposite(fAssessmentTestEditorPanel, SWT.NONE);
		navigationEditPanel.setBackground(controlbar_colour);
		FormLayout layout = new FormLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		navigationEditPanel.setLayout(layout);

		Label qLabel = toolkit.createLabel(navigationEditPanel,"Question", SWT.CENTER);		
		qLabel.setFont(fontBold9);
		
		//qLabel.setBackground(controlbar_colour);
		FormData data1 = new FormData();
		data1.left = new FormAttachment(8, 0);
		data1.top = new FormAttachment(0, 15);
		qLabel.setLayoutData(data1);
		
		fFirstButton = toolkit.createButton(navigationEditPanel, null,
				SWT.ARROW | SWT.UP);
		data1 = new FormData();
		data1.left = new FormAttachment(0, 5);
		data1.top = new FormAttachment(qLabel, 12, SWT.BOTTOM);
		fFirstButton.setLayoutData(data1);
		fFirstButton.setToolTipText("Go to beginning of the test");
		fFirstButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				saveEditedQuestion();
				fCurrentPosition = 1;
				setNavigationBarState(EDIT_FIRST_ITEM);
				fPositionNumberLabel.setText(createPositionNumber());

				fQuestionStackPanel
						.setControl((Composite) fQuestionEditPanelList
								.get(fCurrentPosition - 1));
				setQuestionTypeUI(((AssessmentItem) fQuestionList
						.getBasicElementAt(fCurrentPosition - 1)).getItemBody()
						.getQuestionClass());
				saveTest();
				updateQuestionPanel();
			}
		});

		fPreviousButton = toolkit.createButton(navigationEditPanel, null,
				SWT.ARROW | SWT.LEFT);
		data1 = new FormData();
		data1.left = new FormAttachment(fFirstButton, 10);
		data1.top = new FormAttachment(qLabel, 12, SWT.BOTTOM);
		fPreviousButton.setLayoutData(data1);
		fPreviousButton.setToolTipText("Go to the previous questoin.");
		fPreviousButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				saveEditedQuestion();
				if (fCurrentPosition == 2) {
					setNavigationBarState(EDIT_FIRST_ITEM);
				} else {
					setNavigationBarState(EDIT_MIDDLE_ITEM);
				}
				fCurrentPosition = fCurrentPosition - 1;
				fPositionNumberLabel.setText(createPositionNumber());
				fQuestionStackPanel
						.setControl((Composite) fQuestionEditPanelList
								.get(fCurrentPosition - 1));
				setQuestionTypeUI(((AssessmentItem) fQuestionList
						.getBasicElementAt(fCurrentPosition - 1)).getItemBody()
						.getQuestionClass());
				saveTest();
				updateQuestionPanel();
			}
		});

		fPositionNumberLabel = toolkit.createLabel(navigationEditPanel,
				"0 of 0", SWT.CENTER);
		fPositionNumberLabel.setFont(fontBold9);
		
		data1 = new FormData();
		data1.left = new FormAttachment(qLabel, 0, SWT.LEFT);
		data1.right = new FormAttachment(qLabel, 0, SWT.RIGHT);
		data1.top = new FormAttachment(qLabel, 12, SWT.BOTTOM);
		fPositionNumberLabel.setLayoutData(data1);

		fNextButton = toolkit.createButton(navigationEditPanel, null, SWT.ARROW
				| SWT.RIGHT);
		data1 = new FormData();
		data1.left = new FormAttachment(qLabel, 22, SWT.RIGHT); //original 30
		data1.top = new FormAttachment(qLabel, 12, SWT.BOTTOM);
		fNextButton.setLayoutData(data1);
		fNextButton.setToolTipText("Go to the next questoin.");		
		fNextButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				saveEditedQuestion();
				if (fCurrentPosition == fQuestionCount - 1) {
					setNavigationBarState(EDIT_LAST_ITEM);
				} else {
					setNavigationBarState(EDIT_MIDDLE_ITEM);
				}
				fCurrentPosition = fCurrentPosition + 1;
				fPositionNumberLabel.setText(createPositionNumber());
				fQuestionStackPanel
						.setControl((Composite) fQuestionEditPanelList
								.get(fCurrentPosition - 1));
				setQuestionTypeUI(((AssessmentItem) fQuestionList
						.getBasicElementAt(fCurrentPosition - 1)).getItemBody()
						.getQuestionClass());
				saveTest();
				updateQuestionPanel();
			}
		});

		fLastButton = toolkit.createButton(navigationEditPanel, null, SWT.ARROW
				| SWT.DOWN );
		data1 = new FormData();
		data1.left = new FormAttachment(fNextButton, 10);
		data1.top = new FormAttachment(qLabel, 12, SWT.BOTTOM);
		fLastButton.setLayoutData(data1);
		fLastButton.setToolTipText("Go to the end of this test");
		fLastButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				saveEditedQuestion();
				fCurrentPosition = fQuestionCount;
				setNavigationBarState(EDIT_LAST_ITEM);
				fPositionNumberLabel.setText(createPositionNumber());
				fQuestionStackPanel
						.setControl((Composite) fQuestionEditPanelList
								.get(fCurrentPosition - 1));
				setQuestionTypeUI(((AssessmentItem) fQuestionList
						.getBasicElementAt(fCurrentPosition - 1)).getItemBody()
						.getQuestionClass());
				saveTest();
				updateQuestionPanel();
			}
		});

		setNavigationBarState(EDIT_NO_ITEM);

		fRemoveQuestionButton = toolkit.createButton(navigationEditPanel,
				"Delete Question", SWT.PUSH);
		
		data1 = new FormData();
		data1.left = new FormAttachment(24, 20); 
		data1.top = new FormAttachment(qLabel, 10, SWT.BOTTOM);
		fRemoveQuestionButton.setLayoutData(data1);
		fRemoveQuestionButton.setToolTipText("Delete the currently edited question");
		fRemoveQuestionButton.setEnabled(false);
		fRemoveQuestionButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				if (fQuestionCount > 0) {
					
					if (!MessageDialog.openQuestion(
			                Display.getDefault().getActiveShell(),
			                "Delete",
			                "Are you sure you want to delete this question?"))
						return;
					
					SectionPart aSectionPart = (SectionPart) fAssessmentSection
							.getSectionPartAt(fCurrentPosition - 1);
					if ((aSectionPart != null)
							&& (aSectionPart instanceof AssessmentItemRef)) {
						qtiEditorInput
								.removeAssessmentItem(((AssessmentItemRef) aSectionPart)
										.getHref());
						fQuestionList.removeElementAt(fCurrentPosition - 1);
						fAssessmentSection
								.removeSectionPartAt(fCurrentPosition - 1);
						fQuestionEditPanelList
								.removeElementAt(fCurrentPosition - 1);
					} else {
						// handle an assessment section
					}

					fQuestionCount--;

					if (fQuestionCount == 0) {
						setNavigationBarState(EDIT_NO_ITEM);
						fCurrentPosition = 0;
					} else if (fQuestionCount == 1) {
						setNavigationBarState(EDIT_ONE_ITEM);
						if (fCurrentPosition == 2) {
							fCurrentPosition = 1;
						}
					} else if (fQuestionCount == 2) {
						if (fCurrentPosition == 3) {
							setNavigationBarState(EDIT_LAST_ITEM);
							fCurrentPosition = fQuestionCount;
						} else if (fCurrentPosition == 2) {
							setNavigationBarState(EDIT_FIRST_ITEM);
							fCurrentPosition = 1;
						} else if (fCurrentPosition == 1) {
							setNavigationBarState(EDIT_FIRST_ITEM);
						}
					} else if (fQuestionCount >= 3) {
						if (fCurrentPosition == fQuestionCount + 1) {
							setNavigationBarState(EDIT_LAST_ITEM);
							fCurrentPosition = fQuestionCount;
						} else if (fCurrentPosition == 2) {
							setNavigationBarState(EDIT_FIRST_ITEM);
							fCurrentPosition = 1;
						} else if (fCurrentPosition == 1) {
							setNavigationBarState(EDIT_FIRST_ITEM);
						} else {
							setNavigationBarState(EDIT_MIDDLE_ITEM);
							fCurrentPosition = fCurrentPosition - 1;
						}
					}
					fPositionNumberLabel.setText(createPositionNumber());

					if (fQuestionCount > 0) {
						fQuestionStackPanel
								.setControl((Composite) fQuestionEditPanelList
										.get(fCurrentPosition - 1));
						setQuestionTypeUI(((AssessmentItem) fQuestionList
								.getBasicElementAt(fCurrentPosition - 1))
								.getItemBody().getQuestionClass());

					} else {
						fQuestionStackPanel.setControl(fEmptyEditPanel);
						fRemoveQuestionButton.setEnabled(false);
						fQuestionTypeCombo.select(0);
					}
					
					fAssessmentSection.getSelection().setSelect(fQuestionCount);
					saveTest();
					updateQuestionPanel();
				}
			}
		});

		Label tlabel = toolkit.createLabel(navigationEditPanel,
				"Question type", SWT.NONE);
		tlabel.setFont(fontBold9);
		data1 = new FormData();
		data1.left = new FormAttachment(fRemoveQuestionButton, 50, SWT.RIGHT); //30
		data1.top = new FormAttachment(0, 15);
		tlabel.setLayoutData(data1);

		String[] questionTypes = { "", "Multiple Choice", "Multiple Response",
				"Likert Scale", "Yes or No", "Open Question",
				"Fill in the Blank", "In-line Choice", 
				"Match", "Associate", "Order", "Gap Match", "Hot Text", "Slider" };

		fQuestionTypeCombo = new CCombo(navigationEditPanel, SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER);
		fQuestionTypeCombo.setItems(questionTypes);
		fQuestionTypeCombo.setEnabled(true);
		data1 = new FormData();
		data1.left = new FormAttachment(tlabel, 0, SWT.LEFT);
		data1.top = new FormAttachment(qLabel, 12, SWT.BOTTOM);
		fQuestionTypeCombo.setToolTipText("Please choose a question type which you want to create.");
		fQuestionTypeCombo.setLayoutData(data1);
		toolkit.adapt(fQuestionTypeCombo);

		fQuestionTypeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int index = fQuestionTypeCombo.getSelectionIndex();
				switch (index) {
				case 0: {
					fChoiceNumberCombo.setEnabled(false);
					fChoiceNumberCombo.select(0);
					fQuestionChoiceNumber = 1;
					break;
				}
				case AssessmentItem.TBMC: {
					fChoiceNumberCombo.setEnabled(true);
					fChoiceNumberCombo.select(3);
					fQuestionChoiceNumber = 4;
					break;
				}
				case AssessmentItem.TBMR: {
					fChoiceNumberCombo.setEnabled(true);
					fChoiceNumberCombo.select(3);
					fQuestionChoiceNumber = 4;
					break;
				}
				case AssessmentItem.LS: {
					fChoiceNumberCombo.setEnabled(true);
					fChoiceNumberCombo.select(4);
					fQuestionChoiceNumber = 5;
					break;
				}
				case AssessmentItem.YN: {
					fChoiceNumberCombo.setEnabled(false);
					fChoiceNumberCombo.select(1);
					fQuestionChoiceNumber = 2;
					break;
				}
				case AssessmentItem.OPEN: {
					fChoiceNumberCombo.setEnabled(false);
					fChoiceNumberCombo.select(0);
					fQuestionChoiceNumber = 1;
					break;
				}
				case AssessmentItem.FIB: {
					fChoiceNumberCombo.setEnabled(false);
					fChoiceNumberCombo.select(0);
					fQuestionChoiceNumber = 1;
					break;
				}
				case AssessmentItem.IC: {
					fChoiceNumberCombo.setEnabled(false);
					fChoiceNumberCombo.select(0);
					fQuestionChoiceNumber = 1;
					break;
				}
				case AssessmentItem.MATCH: {
					fChoiceNumberCombo.setEnabled(true);
					fChoiceNumberCombo.select(3);
					fQuestionChoiceNumber = 4;
					break;
				}
				case AssessmentItem.ASSOCIATE: {
					fChoiceNumberCombo.setEnabled(true);
					fChoiceNumberCombo.select(3);
					fQuestionChoiceNumber = 4;
					break;
				}
				case AssessmentItem.ORDER: {
					fChoiceNumberCombo.setEnabled(true);
					fChoiceNumberCombo.select(3);
					fQuestionChoiceNumber = 4;
					break;
				}
				case AssessmentItem.GM: {
					fChoiceNumberCombo.setEnabled(false);
					fChoiceNumberCombo.select(0);
					fQuestionChoiceNumber = 1;
					break;
				}
				case AssessmentItem.HOTTEXT: {
					fChoiceNumberCombo.setEnabled(false);
					fChoiceNumberCombo.select(0);
					fQuestionChoiceNumber = 1;
					break;
				}
				case AssessmentItem.SLIDER: {
					fChoiceNumberCombo.setEnabled(false);
					fChoiceNumberCombo.select(0);
					fQuestionChoiceNumber = 1;
					break;
				}
				default:
					// ignore everything else
				}
			}
		});
		
				
		fQuestionExampleButton = toolkit.createButton(navigationEditPanel, null, SWT.PUSH);
		Image anImage = ImageFactory.getImage(ImageFactory.ICON_INFO);
		if (anImage != null) 
			fQuestionExampleButton.setImage(anImage);
		else 
			fQuestionExampleButton.setText("See Example");
		fQuestionExampleButton.setEnabled(true);
		fQuestionExampleButton.setToolTipText("See an example of the selected type of question.");
		data1 = new FormData();
		data1.left = new FormAttachment(fQuestionTypeCombo, 10);
		data1.top = new FormAttachment(qLabel, 10, SWT.BOTTOM);
		fQuestionExampleButton.setLayoutData(data1);
		fQuestionExampleButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {

				String questionClass = "";
				if (fQuestionTypeCombo.getSelectionIndex() == 1) {
					questionClass = AssessmentElementFactory.TEXT_BASED_MULTIPLE_CHOICE;
				} else if (fQuestionTypeCombo.getSelectionIndex() == 2) {
					questionClass = AssessmentElementFactory.TEXT_BASED_MULTIPLE_RESPONSE;
				} else if (fQuestionTypeCombo.getSelectionIndex() == 3) {
					questionClass = AssessmentElementFactory.LIKERT;
				} else if (fQuestionTypeCombo.getSelectionIndex() == 4) {
					questionClass = AssessmentElementFactory.YES_NO;
				} else if (fQuestionTypeCombo.getSelectionIndex() == 5) {
					questionClass = AssessmentElementFactory.OPEN;
				} else if (fQuestionTypeCombo.getSelectionIndex() == 6) {
					questionClass = AssessmentElementFactory.FILL_IN_THE_BLANK;
				} else if (fQuestionTypeCombo.getSelectionIndex() == 7) {
					questionClass = AssessmentElementFactory.INLINE_CHOICE;
				} else if (fQuestionTypeCombo.getSelectionIndex() == 8) {
					questionClass = AssessmentElementFactory.MATCH;
				} else if (fQuestionTypeCombo.getSelectionIndex() == 9) {
					questionClass = AssessmentElementFactory.ASSOCIATE;
				} else if (fQuestionTypeCombo.getSelectionIndex() == 10) {
					questionClass = AssessmentElementFactory.ORDER;
				} else if (fQuestionTypeCombo.getSelectionIndex() == 11) {
					questionClass = AssessmentElementFactory.GAP_MATCH;
				} else if (fQuestionTypeCombo.getSelectionIndex() == 12) {
					questionClass = AssessmentElementFactory.HOT_TEXT;
				} else if (fQuestionTypeCombo.getSelectionIndex() == 13) {
					questionClass = AssessmentElementFactory.SLIDER;
				}

				if (!questionClass.equalsIgnoreCase("")) {
					QuestionExampleDialog dialog = new QuestionExampleDialog(fScrolledComposite.getShell(), questionClass);
					dialog.open();
				} else {
					MessageBox mDialog = new MessageBox(
							fAssessmentTestEditorPanel.getShell(), SWT.OK);
					mDialog.setText("Message");
					mDialog.setMessage("Please select a question type before showing an example!");
					mDialog.open();
				}
					
			}
		});

		Label clabel = toolkit.createLabel(navigationEditPanel,
				"Number of choices", SWT.LEFT_TO_RIGHT);
		clabel.setFont(fontBold9);
		data1 = new FormData();
		data1.left = new FormAttachment(fQuestionExampleButton, 15, SWT.RIGHT);
		data1.top = new FormAttachment(0, 15);
		clabel.setLayoutData(data1);
		
		clabel.setLayoutData(data1);

		String[] choices = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

		fChoiceNumberCombo = new CCombo(navigationEditPanel, SWT.READ_ONLY
				| SWT.FLAT | SWT.BORDER );
		fChoiceNumberCombo.setItems(choices);
		data1 = new FormData();
		data1.left = new FormAttachment(clabel, 15, SWT.LEFT);
		data1.top = new FormAttachment(qLabel, 12, SWT.BOTTOM);
		fChoiceNumberCombo.setLayoutData(data1);
		fChoiceNumberCombo.setToolTipText("Please define how many choices of the new question.");
		toolkit.adapt(fChoiceNumberCombo);
		fChoiceNumberCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				fQuestionChoiceNumber = fChoiceNumberCombo.getSelectionIndex() + 1;
			}
		});

		fNewQuestionButton = toolkit.createButton(navigationEditPanel,
				"Add Question", SWT.PUSH);
		fNewQuestionButton.setEnabled(true);
		fNewQuestionButton.setToolTipText("Add a new question following the currently edited question.");
		data1 = new FormData();
		data1.right = new FormAttachment(100, -5);
		data1.top = new FormAttachment(qLabel, 10, SWT.BOTTOM);		
		fNewQuestionButton.setLayoutData(data1);
		fNewQuestionButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
				if (fQuestionTypeCombo.getSelectionIndex() <= 0) {
					MessageBox mDialog = new MessageBox(
							fAssessmentTestEditorPanel.getShell(), SWT.OK);
					mDialog.setText("Message");
					mDialog.setMessage("Please select a question type before creating a new question!");
					mDialog.open();
					return;
				}

				saveEditedQuestion();

				fQuestionCount++;

				if (fQuestionCount == 1) {
					fCurrentPosition = 1;
					setNavigationBarState(EDIT_ONE_ITEM);
				} else if (fQuestionCount == 2) {
					fCurrentPosition = 2;
					setNavigationBarState(EDIT_LAST_ITEM);
				} else if (fQuestionCount >= 3) {
					if (fCurrentPosition == fQuestionCount - 1) {
						fCurrentPosition = fQuestionCount;
						setNavigationBarState(EDIT_LAST_ITEM);
					} else {
						fCurrentPosition = fCurrentPosition + 1;
						setNavigationBarState(EDIT_MIDDLE_ITEM);
					}
				}
				fPositionNumberLabel.setText(createPositionNumber());

				AssessmentItem anAssessmentItem = new AssessmentItem();
				Block aBlock = null;
				// use switch case default form
				if (fQuestionTypeCombo.getSelectionIndex() == 1) {
					aBlock = new ChoiceInteraction(anAssessmentItem,
							AssessmentItem.TBMC, fQuestionChoiceNumber);
					anAssessmentItem
							.getItemBody()
							.setQuestionClass(
									AssessmentElementFactory.TEXT_BASED_MULTIPLE_CHOICE);
				} else if (fQuestionTypeCombo.getSelectionIndex() == 2) {
					aBlock = new ChoiceInteraction(anAssessmentItem,
							AssessmentItem.TBMR, fQuestionChoiceNumber);
					anAssessmentItem
							.getItemBody()
							.setQuestionClass(
									AssessmentElementFactory.TEXT_BASED_MULTIPLE_RESPONSE);
				} else if (fQuestionTypeCombo.getSelectionIndex() == 3) {
					aBlock = new ChoiceInteraction(anAssessmentItem,
							AssessmentItem.LS, fQuestionChoiceNumber);
					anAssessmentItem.getItemBody().setQuestionClass(
							AssessmentElementFactory.LIKERT);
				} else if (fQuestionTypeCombo.getSelectionIndex() == 4) {
					aBlock = new ChoiceInteraction(anAssessmentItem,
							AssessmentItem.YN, 2);
					anAssessmentItem.getItemBody().setQuestionClass(
							AssessmentElementFactory.YES_NO);
				} else if (fQuestionTypeCombo.getSelectionIndex() == 5) {
					aBlock = new ExtendedTextInteraction(anAssessmentItem);
					anAssessmentItem.getItemBody().setQuestionClass(
							AssessmentElementFactory.OPEN);
				} else if (fQuestionTypeCombo.getSelectionIndex() == 6) {
					aBlock = new FillingInBlankBlock(anAssessmentItem);
					anAssessmentItem.getItemBody().setQuestionClass(
							AssessmentElementFactory.FILL_IN_THE_BLANK);
				} else if (fQuestionTypeCombo.getSelectionIndex() == 7) {
					aBlock = new InlineChoiceBlock(anAssessmentItem);
					anAssessmentItem.getItemBody().setQuestionClass(
							AssessmentElementFactory.INLINE_CHOICE);
				} else if (fQuestionTypeCombo.getSelectionIndex() == 8) {
					aBlock = new MatchInteraction(anAssessmentItem, fQuestionChoiceNumber);
					anAssessmentItem.getItemBody().setQuestionClass(
							AssessmentElementFactory.MATCH);
				} else if (fQuestionTypeCombo.getSelectionIndex() == 9) {
					aBlock = new AssociateInteraction(anAssessmentItem, fQuestionChoiceNumber);
					anAssessmentItem.getItemBody().setQuestionClass(
							AssessmentElementFactory.ASSOCIATE);
				} else if (fQuestionTypeCombo.getSelectionIndex() == 10) {
					aBlock = new OrderInteraction(anAssessmentItem, fQuestionChoiceNumber);
					anAssessmentItem.getItemBody().setQuestionClass(
							AssessmentElementFactory.ORDER);
				} else if (fQuestionTypeCombo.getSelectionIndex() == 11) {
					aBlock = new GapMatchInteraction(anAssessmentItem, 0);
					anAssessmentItem.getItemBody().setQuestionClass(
							AssessmentElementFactory.GAP_MATCH);
				} else if (fQuestionTypeCombo.getSelectionIndex() == 12) {
					aBlock = new HottextInteraction(anAssessmentItem, 0);
					anAssessmentItem.getItemBody().setQuestionClass(
							AssessmentElementFactory.HOT_TEXT);
				} else if (fQuestionTypeCombo.getSelectionIndex() == 13) {
					aBlock = new SliderInteraction(anAssessmentItem);
					anAssessmentItem.getItemBody().setQuestionClass(
							AssessmentElementFactory.SLIDER);
				}				

				anAssessmentItem.getItemBody().setFirstBlock(aBlock);
				qtiEditorInput.createAssessmentItem(anAssessmentItem);
				AssessmentItemRef aAssessmentItemRef = new AssessmentItemRef(
						fAssessmentTest);
				aAssessmentItemRef.setHref(qtiEditorInput.getName() + "/"
						+ anAssessmentItem.getId() + ".xml");
				fAssessmentSection.addSectionPartAt(fCurrentPosition - 1,
						aAssessmentItemRef);
				fAssessmentSection.getSelection().setSelect(fQuestionCount);

				fQuestionList.addElementAt(fCurrentPosition - 1,
						anAssessmentItem);
				fQuestionStackPanel.setControl(addQuestionPanel(
						fCurrentPosition - 1, anAssessmentItem, aBlock));
				fRemoveQuestionButton.setEnabled(true);
				
				saveTest();
				updateQuestionPanel();
			}
		});
		return navigationEditPanel;

	}

	private String createPositionNumber() {
		return String.valueOf(fCurrentPosition) + " of "
				+ String.valueOf(fQuestionCount);
	}

	private void setQuestionTypeUI(String questionType) {
		if (questionType
				.equals(AssessmentElementFactory.TEXT_BASED_MULTIPLE_CHOICE)) {
			fChoiceNumberCombo.setEnabled(true);
			fChoiceNumberCombo.select(3);
			fQuestionChoiceNumber = 4;
			fQuestionTypeCombo.select(1);
		} else if (questionType
				.equals(AssessmentElementFactory.TEXT_BASED_MULTIPLE_RESPONSE)) {
			fChoiceNumberCombo.setEnabled(true);
			fChoiceNumberCombo.select(3);
			fQuestionChoiceNumber = 4;
			fQuestionTypeCombo.select(2);
		} else if (questionType.equals(AssessmentElementFactory.LIKERT)) {
			fChoiceNumberCombo.setEnabled(true);
			fChoiceNumberCombo.select(4);
			fQuestionChoiceNumber = 5;
			fQuestionTypeCombo.select(3);
		} else if (questionType.equals(AssessmentElementFactory.YES_NO)) {
			fChoiceNumberCombo.setEnabled(false);
			fChoiceNumberCombo.select(1);
			fQuestionChoiceNumber = 2;
			fQuestionTypeCombo.select(4);
		} else if (questionType.equals(AssessmentElementFactory.OPEN)) {
			fChoiceNumberCombo.setEnabled(false);
			fChoiceNumberCombo.select(0);
			fQuestionChoiceNumber = 1;
			fQuestionTypeCombo.select(5);
		} else if (questionType
				.equals(AssessmentElementFactory.FILL_IN_THE_BLANK)) {
			fChoiceNumberCombo.setEnabled(false);
			fChoiceNumberCombo.select(0);
			fQuestionChoiceNumber = 1;
			fQuestionTypeCombo.select(6);
		} else if (questionType.equals(AssessmentElementFactory.INLINE_CHOICE)) {
			fChoiceNumberCombo.setEnabled(false);
			fChoiceNumberCombo.select(0);
			fQuestionChoiceNumber = 1;
			fQuestionTypeCombo.select(7);
		} else if (questionType.equals(AssessmentElementFactory.MATCH)) {
			fChoiceNumberCombo.setEnabled(true);
			fChoiceNumberCombo.select(3);
			fQuestionChoiceNumber = 4;
			fQuestionTypeCombo.select(8);
		} else if (questionType.equals(AssessmentElementFactory.ASSOCIATE)) {
			fChoiceNumberCombo.setEnabled(true);
			fChoiceNumberCombo.select(3);
			fQuestionChoiceNumber = 4;
			fQuestionTypeCombo.select(9);
		} else if (questionType.equals(AssessmentElementFactory.ORDER)) {
			fChoiceNumberCombo.setEnabled(true);
			fChoiceNumberCombo.select(3);
			fQuestionChoiceNumber = 4;
			fQuestionTypeCombo.select(10);
		} else if (questionType.equals(AssessmentElementFactory.GAP_MATCH)) {
			fChoiceNumberCombo.setEnabled(false);
			fChoiceNumberCombo.select(0);
			fQuestionChoiceNumber = 1;
			fQuestionTypeCombo.select(11);
		} else if (questionType.equals(AssessmentElementFactory.HOT_TEXT)) {
			fChoiceNumberCombo.setEnabled(false);
			fChoiceNumberCombo.select(0);
			fQuestionChoiceNumber = 1;
			fQuestionTypeCombo.select(12);
		} else if (questionType.equals(AssessmentElementFactory.SLIDER)) {
			fChoiceNumberCombo.setEnabled(false);
			fChoiceNumberCombo.select(0);
			fQuestionChoiceNumber = 1;
			fQuestionTypeCombo.select(13);
		} else {
			fQuestionTypeCombo.select(0);
			fChoiceNumberCombo.setEnabled(false);
			fChoiceNumberCombo.select(0);
			fQuestionChoiceNumber = 1;
		}

		fNewQuestionButton.setEnabled(true);
	}


	private void setQuestionControlPanel() {
		// set navigation information and buttons
		fQuestionCount = fQuestionList.size();
		if (fQuestionCount == 0) {
			fCurrentPosition = 0;
			setNavigationBarState(EDIT_NO_ITEM);
			fRemoveQuestionButton.setEnabled(false);
		} else if (fQuestionCount == 1) {
			fCurrentPosition = 1;
			setNavigationBarState(EDIT_ONE_ITEM);
			fRemoveQuestionButton.setEnabled(true);
			setQuestionTypeUI(((AssessmentItem) fQuestionList
					.getBasicElementAt(0)).getItemBody().getQuestionClass());

		} else if (fQuestionCount >= 2) {
			fCurrentPosition = 1;
			setNavigationBarState(EDIT_FIRST_ITEM);
			fRemoveQuestionButton.setEnabled(true);
			setQuestionTypeUI(((AssessmentItem) fQuestionList
					.getBasicElementAt(0)).getItemBody().getQuestionClass());
		}

		fPositionNumberLabel.setText(createPositionNumber());

		// re-construct the interaction panel list
		fQuestionEditPanelList.removeAllElements();
		createQuestionPanels();

		// display the first block
		if (fQuestionCount > 0) {
			fQuestionStackPanel.setControl((Composite) fQuestionEditPanelList
					.get(0));
		} else {
			fQuestionStackPanel.setControl(fEmptyEditPanel);
		}

		fAssessmentSection.getSelection().setSelect(fQuestionCount);
	}

	private void createQuestionPanels() {
		int size = fQuestionList.size();
		for (int i = 0; i < size; i++) {
			AssessmentItem aAssessmentItem = (AssessmentItem) fQuestionList
					.getBasicElementAt(i);
			addQuestionPanel(i, aAssessmentItem, aAssessmentItem.getItemBody().getFirstBlock());
		}

		// show the first block for the new item or the empty one if there is no
		// block
		if (fQuestionList.size() == 0)
			fQuestionStackPanel.setControl(fEmptyEditPanel);
		else {
			fQuestionStackPanel.setControl((Composite) fQuestionEditPanelList
					.get(0));
		}
	}

	private Composite addQuestionPanel(int index, AssessmentItem aAssessmentItem, Block aBlock) {
		Composite aPanel = null;
		if (aBlock == null) {
			return fEmptyEditPanel;
		}
		if (aBlock instanceof ChoiceInteraction) {
			aPanel = new EditChoiceInteractionPanel(fQuestionStackPanel, aAssessmentItem, 
					(ChoiceInteraction) aBlock, this);
		} else if (aBlock instanceof ExtendedTextInteraction) {
			aPanel = new EditExtendedTextInteractionPanel(fQuestionStackPanel, aAssessmentItem, 
					(ExtendedTextInteraction) aBlock, this);
		} else if (aBlock instanceof FillingInBlankBlock) {
			aPanel = new EditFillingInBlankBlockPanel(fQuestionStackPanel, aAssessmentItem,
					(FillingInBlankBlock) aBlock, this);
		} else if (aBlock instanceof InlineChoiceBlock) {
			aPanel = new EditInlineChoiceBlockPanel(fQuestionStackPanel, aAssessmentItem,
					(InlineChoiceBlock) aBlock, this);
		} else if (aBlock instanceof PBlock) {
			aPanel = new EditPBlockPanel(fQuestionStackPanel, (PBlock) aBlock,
					this);
		} else if (aBlock instanceof MatchInteraction) {
			aPanel = new EditMatchInteractionBlockPanel(fQuestionStackPanel, aAssessmentItem,
			(MatchInteraction) aBlock, this);
		} else if (aBlock instanceof AssociateInteraction) {
			aPanel = new EditAssociateInteractionBlockPanel(fQuestionStackPanel, aAssessmentItem,
			(AssociateInteraction) aBlock, this);
		} else if (aBlock instanceof OrderInteraction) {
				aPanel = new EditOrderInteractionBlockPanel(fQuestionStackPanel, aAssessmentItem,
				(OrderInteraction) aBlock, this);
		} else if (aBlock instanceof GapMatchInteraction) {
			aPanel = new EditGapMatchInteractionBlockPanel(fQuestionStackPanel, aAssessmentItem,
			(GapMatchInteraction)aBlock, this);
		} else if (aBlock instanceof HottextInteraction) {
			aPanel = new EditHottextInteractionBlockPanel(fQuestionStackPanel, aAssessmentItem,
			(HottextInteraction)aBlock, this);
		} else if (aBlock instanceof SliderInteraction) {
			aPanel = new EditSliderInteractionBlockPanel(fQuestionStackPanel, aAssessmentItem,
			(SliderInteraction)aBlock, this);
		} else {
			return fEmptyEditPanel;
		}
		
		if (aPanel!=null) {
			fQuestionEditPanelList.add(index, aPanel);
			toolkit.adapt(aPanel);
			aPanel.setBackground(content_colour);
		}
		return aPanel;
	}

	public void updateQuestionPanel() {		
		fAssessmentTestEditorPanel.layout(true, true);
		fScrolledComposite.layout();
		fForm.layout();
	}

	public Composite getParent() {
		return parent;
	}

	private void saveEditedQuestion() {
		if (fQuestionCount <= 0 || fCurrentPosition <= 0) 			
			return;
			
		AbstractQuestionEditPanel aPanel = (AbstractQuestionEditPanel) fQuestionEditPanelList
				.get(fCurrentPosition - 1);
		if (aPanel.isDirty()) {
			if (aPanel instanceof EditMatchInteractionBlockPanel) 
				((EditMatchInteractionBlockPanel)aPanel).setResponseCorrectAndMapping();
			if (aPanel instanceof EditAssociateInteractionBlockPanel) 
				((EditAssociateInteractionBlockPanel)aPanel).setResponseCorrectAndMapping();
			if (aPanel instanceof EditGapMatchInteractionBlockPanel) 
				((EditGapMatchInteractionBlockPanel)aPanel).setResponseCorrectAndMapping();			
			
			AssessmentItem aAssessmentItem = ((AssessmentItem) fQuestionList
					.getBasicElementAt(fCurrentPosition - 1));
			qtiEditorInput.saveAssessmentItem(aAssessmentItem);
			aPanel.clean();
		}
	}
	
	private void saveTest() {
		try {
			qtiEditorInput.saveAssessmentTest();
			saveEditedQuestion();
		} catch (IOException event) {
			System.err.println("something wrong on save an assessmentTest as a XML file!");
		}
		setDirty(false);
	}

	public void setNavigationBarState(int state) {
		if (state == EDIT_NO_ITEM) {
			fFirstButton.setEnabled(false);
			fPreviousButton.setEnabled(false);
			fPositionNumberLabel.setEnabled(false);
			fNextButton.setEnabled(false);
			fLastButton.setEnabled(false);
		} else if (state == EDIT_ONE_ITEM) {
			fFirstButton.setEnabled(false);
			fPreviousButton.setEnabled(false);
			fPositionNumberLabel.setEnabled(true);
			fNextButton.setEnabled(false);
			fLastButton.setEnabled(false);
		} else if (state == EDIT_FIRST_ITEM) {
			fFirstButton.setEnabled(false);
			fPreviousButton.setEnabled(false);
			fPositionNumberLabel.setEnabled(true);
			fNextButton.setEnabled(true);
			fLastButton.setEnabled(true);
		} else if (state == EDIT_MIDDLE_ITEM) {
			fFirstButton.setEnabled(true);
			fPreviousButton.setEnabled(true);
			fPositionNumberLabel.setEnabled(true);
			fNextButton.setEnabled(true);
			fLastButton.setEnabled(true);
		} else if (state == EDIT_LAST_ITEM) {
			fFirstButton.setEnabled(true);
			fPreviousButton.setEnabled(true);
			fPositionNumberLabel.setEnabled(true);
			fNextButton.setEnabled(false);
			fLastButton.setEnabled(false);
		}
	}
	
	private void changeResultReportButton() {
		if (fResultReportFlag) {
			fReportResultButton.setText("Edit Questions");
			fRemoveQuestionButton.setEnabled(false);
			fNewQuestionButton.setEnabled(false);
			fQuestionExampleButton.setEnabled(false);
			fPositionNumberLabel.setEnabled(false);
			
			fButtonStates[0] = fQuestionTypeCombo.getEnabled();
			fButtonStates[1] = fChoiceNumberCombo.getEnabled();
			fButtonStates[2] = fFirstButton.getEnabled();
			fButtonStates[3] = fPreviousButton.getEnabled();
			fButtonStates[4] = fNextButton.getEnabled();
			fButtonStates[5] = fLastButton.getEnabled();
			
			fQuestionTypeCombo.setEnabled(false);
			fChoiceNumberCombo.setEnabled(false);
			fFirstButton.setEnabled(false);
			fPreviousButton.setEnabled(false);
			fNextButton.setEnabled(false);
			fLastButton.setEnabled(false);

		} else {
			fReportResultButton.setText("Report Results");
			if (fQuestionCount>0) {
				fRemoveQuestionButton.setEnabled(true);
			}
			fNewQuestionButton.setEnabled(true);
			fQuestionExampleButton.setEnabled(true);
			fPositionNumberLabel.setEnabled(true);
			
			fQuestionTypeCombo.setEnabled(fButtonStates[0]);
			fChoiceNumberCombo.setEnabled(fButtonStates[1]);
			fFirstButton.setEnabled(fButtonStates[2]);
			fPreviousButton.setEnabled(fButtonStates[3]);
			fNextButton.setEnabled(fButtonStates[4]);
			fLastButton.setEnabled(fButtonStates[5]);
		}
		fResultReportFlag = !fResultReportFlag;
		saveTest();
	}
	
	
	public QTIEditorInput getQTIEditorInput() {
		return qtiEditorInput;
	}

	public static void changeToContentColour(Composite container) {
		Control[] children = container.getChildren();
		for (Control aChild : children) {
			if (!(aChild instanceof Text) && !(aChild instanceof StyledText))
				aChild.setBackground(content_colour);
			if (aChild instanceof Composite) {
				changeToContentColour((Composite) aChild);
			}
		}
	}
	public static void changeBackGroundColour(Composite container, Color colour) {
		Control[] children = container.getChildren();
		for (Control aChild : children) {
			if (!(aChild instanceof Text) && !(aChild instanceof StyledText)
					&& !(aChild instanceof CCombo)){
				aChild.setBackground(colour);
			}
			if (aChild instanceof Composite) {
				changeBackGroundColour((Composite) aChild, colour);
			}
		}
	}
	
	public static Color getContentColour() {
		return content_colour;
	}
	
	private void createFonts() {
		if(fontBold12 == null){
		 fontBold12 = new Font(getSite().getShell().getDisplay(),"fontBold12", 12, SWT.BOLD);
		 font9 = new Font(getSite().getShell().getDisplay(),"font9", 9, SWT.NORMAL);
		 fontBold9 = new Font(getSite().getShell().getDisplay(),"fontBold9", 9, SWT.BOLD);
		}
	}
}