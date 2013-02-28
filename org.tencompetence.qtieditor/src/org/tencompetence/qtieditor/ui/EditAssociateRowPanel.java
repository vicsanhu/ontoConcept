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
	import org.tencompetence.qtieditor.elements.AssociateInteraction;

	public class EditAssociateRowPanel extends Composite{

		private static FormToolkit toolkit = AppFormToolkit.getInstance();
		//private Composite fEditChoicesPanel = null;
		private EditAssociateInteractionBlockPanel fEditAssociateInteractionBlockPanel;

		private Label fChoiceLabel = null;
		private Text fChoiceText = null;
		private Button fSelectSourceButton = null;
		private Button fSelectTargetButton = null;
		private Button fRemoveButton = null;
		private Composite fMoveButtonsComposite = null;
		private Button fUpButton = null;
		private Button fDownButton = null;
		private CCombo fMaxMtachCombo = null;		
		private String[] fMatchMax = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		
		private int fChoiceNumber = 0;
		private SimpleAssociableChoice fSimpleAssociableChoice = null;

		private AssociateInteraction fAssociateInteraction;	
		//private AssessmentTestEditor fTestEditor;
		
		public EditAssociateRowPanel(Composite parent, 
				AssociateInteraction aAssociateInteraction,
				SimpleAssociableChoice afSimpleAssociableChoice, 
				EditAssociateInteractionBlockPanel aEditAssociateInteractionBlockPanel, 
				int number) {
			super(parent, SWT.NONE);
			fAssociateInteraction = aAssociateInteraction;		
			fSimpleAssociableChoice = afSimpleAssociableChoice;
			fEditAssociateInteractionBlockPanel = aEditAssociateInteractionBlockPanel;
			fChoiceNumber = number;
			init();
			AssessmentTestEditor.changeToContentColour(this);
		}

		private void init() {
			
			GridLayout gl = new GridLayout();
			gl.horizontalSpacing = 0;
			gl.verticalSpacing = 0;
			gl.marginWidth = 0;
			gl.marginHeight = 0;
			setLayout(gl);		
			//setBackground(AssessmentTestEditor.getContentColour());
			
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL; 
			//gridData.verticalAlignment = GridData.FILL; 
			gridData.grabExcessHorizontalSpace = true; 
			//gridData.grabExcessVerticalSpace = false;
			setLayoutData(gridData);
			
			Composite fEditChoicesPanel = toolkit.createComposite(this,  SWT.NONE);
			gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL; 
			//gridData.verticalAlignment = GridData.FILL; 
			gridData.grabExcessHorizontalSpace = true; 
			//gridData.grabExcessVerticalSpace = false;
			fEditChoicesPanel.setLayoutData(gridData);	
			gl = new GridLayout(8, false);
			gl.marginWidth = 0;
			gl.marginHeight = 0;
			fEditChoicesPanel.setLayout(gl);
			
			gridData = new GridData();
			gridData.horizontalAlignment = GridData.CENTER;
			
			String choiceLabel = String.valueOf(fChoiceNumber+1);
			if (fChoiceNumber < 10) {
				fChoiceLabel = toolkit.createLabel(fEditChoicesPanel, 
						"  " + choiceLabel + ":", SWT.CENTER);
			} else {
				fChoiceLabel = toolkit.createLabel(fEditChoicesPanel, 
						choiceLabel + ":", SWT.CENTER);
			}
			
			fMoveButtonsComposite = toolkit.createComposite(fEditChoicesPanel, SWT.NONE);			
			gl = new GridLayout();
			gl.verticalSpacing = 0;
			gl.marginHeight = 0;
			fMoveButtonsComposite.setLayout(gl);

			fUpButton = toolkit.createButton(fMoveButtonsComposite, null, SWT.ARROW | SWT.UP);
			fUpButton.setToolTipText("Move this choice up");
			fUpButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					fEditAssociateInteractionBlockPanel.moveUp(fChoiceNumber);
				}
			});
			fUpButton.pack();

			fDownButton = toolkit.createButton(fMoveButtonsComposite, null, SWT.ARROW | SWT.DOWN);
			fDownButton.setToolTipText("Move this choice down");
			fDownButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					fEditAssociateInteractionBlockPanel.moveDown(fChoiceNumber);
				}
			});
			fDownButton.pack();
			
			GridData gd2 = new GridData(SWT.FILL, SWT.NONE, true, false);
			gd2.heightHint = 30;
			gd2.widthHint = 400;
			fChoiceText = toolkit.createText(fEditChoicesPanel, 
					fSimpleAssociableChoice.getData(), SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
			fChoiceText.setLayoutData(gd2);
			fChoiceText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					fSimpleAssociableChoice.setData(fChoiceText.getText());
					fEditAssociateInteractionBlockPanel.setDirty(true);	
				}
			});
			
			Label aLabel = toolkit.createLabel(fEditChoicesPanel, 
					" ", SWT.CENTER);

			fSelectSourceButton = toolkit.createButton(fEditChoicesPanel,
						null, SWT.RADIO | SWT.CENTER);		
			GridData gd = new GridData();
			gd.horizontalAlignment = GridData.CENTER;
			fSelectSourceButton.setLayoutData(gd);
			fSelectSourceButton.setText("     ");
			fSelectSourceButton.setSelection(false);
			fSelectSourceButton.addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					fEditAssociateInteractionBlockPanel.selectedSourceAt(fChoiceNumber);
				}
			});
			
			fSelectTargetButton = toolkit.createButton(fEditChoicesPanel,
						null, SWT.RADIO | SWT.CENTER);		
			gd = new GridData();
			gd.horizontalAlignment = GridData.CENTER;
			fSelectTargetButton.setLayoutData(gd);
			fSelectTargetButton.setText("  ");
			fSelectTargetButton.setSelection(false);
			fSelectTargetButton.addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					fEditAssociateInteractionBlockPanel.selectedTargetAt(fChoiceNumber);
				}
			});

			fMaxMtachCombo = new CCombo(fEditChoicesPanel, SWT.READ_ONLY
					| SWT.FLAT | SWT.BORDER );
			fMaxMtachCombo.setItems(fMatchMax);
			GridData gd3 = new GridData();
			gd3.heightHint = 15;
			gd3.widthHint = 35;		
			fMaxMtachCombo.setLayoutData(gd3);
			fMaxMtachCombo.select(fSimpleAssociableChoice.getMatchMax()-1);
			fMaxMtachCombo.setToolTipText("Please define the maximum of the matches.");
			toolkit.adapt(fMaxMtachCombo);
			fMaxMtachCombo.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					fSimpleAssociableChoice.setMatchMax(fMaxMtachCombo.getSelectionIndex() + 1);
					fEditAssociateInteractionBlockPanel.setDirty(true);	
				}
			});
			
			fRemoveButton = toolkit.createButton(fEditChoicesPanel, null, SWT.PUSH | SWT.CENTER);		
			Image anImage = ImageFactory.getImage(ImageFactory.ICON_DELETE);
			if (anImage != null) 
				fRemoveButton.setImage(anImage);
			else 
				fRemoveButton.setText("Delete Choice");

			fRemoveButton.setToolTipText("Delete this choice");
			fRemoveButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if (!MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "Delete",
							   "Are you sure you want to delete this choice? " +
							   "Removing this choice results in removing relevant associations."))
							return;
									
						fEditAssociateInteractionBlockPanel.removeAssociateAt(fChoiceNumber);
					}
			});
			fRemoveButton.pack();
			
			
			fEditChoicesPanel.layout();
			layout();
			fEditAssociateInteractionBlockPanel.layout();
		}
		
		public void setPanelForSimpleAssociableChoice(SimpleAssociableChoice afSimpleAssociableChoice, 
				boolean hasSourceSelected,
				boolean hasTargetSelected) {
			fSimpleAssociableChoice = afSimpleAssociableChoice;
			fChoiceText.setText(fSimpleAssociableChoice.getData());
			fSelectSourceButton.setSelection(hasSourceSelected);
			fSelectTargetButton.setSelection(hasTargetSelected);
			fMaxMtachCombo.select(fSimpleAssociableChoice.getMatchMax()-1);
		}
		
		public void setSelectSourceButton(boolean hasSelected) {
			fSelectSourceButton.setSelection(hasSelected);
		}
		
		public void setSelectTargetButton(boolean hasSelected) {
			fSelectTargetButton.setSelection(hasSelected);
		}
		
		public boolean getSourceButtonSelection() {
			return fSelectSourceButton.getSelection();
		}
		
		public boolean getTargetButtonSelection() {
			return fSelectTargetButton.getSelection();
		}

	}

	
	
	
	
	
	

