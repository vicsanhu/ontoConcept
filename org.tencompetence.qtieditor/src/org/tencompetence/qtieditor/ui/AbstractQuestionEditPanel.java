package org.tencompetence.qtieditor.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.qtieditor.elements.AssessmentItem;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.properties.IPropertiesModel;
import org.tencompetence.imsldmodel.properties.ILocalPersonalPropertyModel;


public abstract class AbstractQuestionEditPanel extends Composite {

	protected AssessmentTestEditor fTestEditor;
	protected AssessmentItem fAssessmentItem;
	protected ILDModel ldModel;
	protected IPropertiesModel fPropertiesModel;
	protected ILocalPersonalPropertyModel fScoreProperty;
	protected ILocalPersonalPropertyModel fAnswerProperty;
	protected final static int maximum = 10; // the maximum number of the choices
	protected boolean fDirty;
	
	protected AbstractQuestionEditPanel(Composite parent, AssessmentTestEditor editor) {
		super(parent, SWT.NONE);
		fTestEditor = editor;
		clean();
	}
	
	protected void createQuestionTitlePanel(Composite composite, FormToolkit toolkit, String questionType) {
		
		Composite questionTitlePanel = toolkit.createComposite(
				composite, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 78;
		questionTitlePanel.setLayoutData(gridData);

		FormLayout layout = new FormLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		questionTitlePanel.setLayout(layout);
		toolkit.paintBordersFor(questionTitlePanel);
		
		Label label = toolkit.createLabel(questionTitlePanel, questionType,
				SWT.LEFT_TO_RIGHT);
		FormData data1 = new FormData();
		data1.left = new FormAttachment(0, 3);
		data1.top = new FormAttachment(0, 5);
		label.setLayoutData(data1);
		label.setFont(AssessmentTestEditor.fontBold12);


		Label titleLabel = toolkit.createLabel(questionTitlePanel, "Question Title: ",
				SWT.LEFT_TO_RIGHT);
		//titleLabel.setFont(fontBold9);
		data1 = new FormData();
		data1.left = new FormAttachment(0, 5);
		data1.top = new FormAttachment(label, 10);
		titleLabel.setLayoutData(data1);
		titleLabel.setFont(AssessmentTestEditor.fontBold9);

		Text fTitleText = toolkit.createText(questionTitlePanel, "", SWT.BORDER);
		fTitleText.setToolTipText("Please input question title here");		
		if (fAssessmentItem.getTitle().equalsIgnoreCase(""))
			fTitleText.setSize(350, 18);
		else
			fTitleText.setText(fAssessmentItem.getTitle());
		data1 = new FormData();
		data1.left = new FormAttachment(0, 5);
		data1.right = new FormAttachment(100, -160);
		data1.top = new FormAttachment(titleLabel, 8);
		fTitleText.setLayoutData(data1);

		fTitleText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String title = ((Text) e.getSource()).getText();
				fAssessmentItem.setTitle(title);
					setDirty(true);
			}
		});

		ldModel = fTestEditor.getQTIEditorInput().getLDModel();
		fPropertiesModel = ldModel.getPropertiesModel();
		
		if (questionType.equalsIgnoreCase("Likert-scale Question") ||
			questionType.equalsIgnoreCase("Open Question")) {
			
			Button fAnswerButton = toolkit.createButton(questionTitlePanel,
					"Answer", SWT.CHECK);
			data1 = new FormData();
			data1.right = new FormAttachment(100, -8);
			data1.top = new FormAttachment(titleLabel, 10);
			fAnswerButton.setLayoutData(data1);
			fAnswerButton.setSelection(false);
			//fAnswerProperty = fPropertiesModel.getPropertyByID(fAssessmentItem.getId()+".ANSWER");
			//fAnswerButton.setSelection(fAnswerProperty!=null);
			fAnswerButton.addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					if (!((Button)e.getSource()).getSelection()) {
						fAnswerProperty = (ILocalPersonalPropertyModel)LDModelFactory.createModelObject(LDModelFactory.LOCAL_PERSONAL_PROPERTY, ldModel);
						fAnswerProperty.setTitle(fAssessmentItem.getTitle()+" (answer)");
						fAnswerProperty.setIdentifier(fAssessmentItem.getId()+".ANSWER");
						//fAnswerProperty.setDataType("String");
						//fPropertiesModel.createLocalPersonalProperty(fAnswerProperty);
						System.out.println("create a property for coupling an answer, for example:");
						System.out.println("<imsld:locpers-property identifier='AI-1693642b-749d-4dfb-88d0-7216510bb057.ANSWER'>");
						System.out.println("    <imsld:title>first question (answer)</imsld:title>");
						System.out.println("    <imsld:datatype datatype='string'/>");
						System.out.println("</imsld:locpers-property>");

					} else {
						if (fAnswerProperty!=null) {
							//fPropertiesModel.removeProperty(fAnswerProperty);
							System.out.println("delete a property bound with an answer");
						}
					}
					setDirty(true);
				}
			});
			fAnswerButton.pack();
		} else {
			Button fScoreButton = toolkit.createButton(questionTitlePanel,
					"Score", SWT.CHECK);
			data1 = new FormData();
			data1.right = new FormAttachment(100, -15);
			data1.top = new FormAttachment(titleLabel, 10);
			fScoreButton.setLayoutData(data1);
			fScoreButton.setSelection(false);
			//fScoreProperty = fPropertiesModel.getPropertyByID(fAssessmentItem.getId()+".SCORE");
			//fScoreButton.setSelection(fScoreProperty!=null);
			fScoreButton.addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					if (!((Button)e.getSource()).getSelection()) {
						fScoreProperty = (ILocalPersonalPropertyModel)LDModelFactory.createModelObject(LDModelFactory.LOCAL_PERSONAL_PROPERTY, ldModel);
						fScoreProperty.setTitle(fAssessmentItem.getTitle()+" (score)");
						fScoreProperty.setIdentifier(fAssessmentItem.getId()+".SCORE");
						//fScoreProperty.setDataType("real");
						//fPropertiesModel.createLocalPersonalProperty(fScoreProperty);
						System.out.println("create a property for coupling a score, for example:");
						System.out.println("<imsld:locpers-property identifier='AI-1693642b-749d-4dfb-88d0-7216510bb057.SCORE'>");
						System.out.println("    <imsld:title>first question (score)</imsld:title>");
						System.out.println("    <imsld:datatype datatype='real'/>");
						System.out.println("</imsld:locpers-property>");
					} else {
						if (fScoreProperty!=null) {
							//fPropertiesModel.removeProperty(fScoreProperty);
							System.out.println("delete a property bound with a score");
						}
					}
					setDirty(true);
				}
			});
			fScoreButton.pack();
		}
	}

	protected boolean isDirty() {
		return fDirty;
	}
	
	protected void setDirty(boolean dirty) {
        fDirty = dirty;
        fTestEditor.setDirty(dirty);
    }
	
	protected void clean() {
		fDirty = false;
    }
	
}
