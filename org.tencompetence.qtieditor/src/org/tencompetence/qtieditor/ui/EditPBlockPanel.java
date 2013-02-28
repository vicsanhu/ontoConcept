package org.tencompetence.qtieditor.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.qtieditor.elements.PBlock;

public class EditPBlockPanel extends AbstractQuestionEditPanel {

	private static FormToolkit toolkit = AppFormToolkit.getInstance();
	private Text fPBlockText;
	private PBlock aPBlock;

	public EditPBlockPanel(Composite parent, PBlock aBlock, AssessmentTestEditor editor) {

		super(parent, editor);
		aPBlock = aBlock;

		this.setLayout(new RowLayout(SWT.VERTICAL | SWT.V_SCROLL));

		Composite fPBlockEditPanel = AppFormToolkit.getInstance().createComposite(this, SWT.NONE);
		fPBlockEditPanel.setLayout(new GridLayout());

		Label aLabel = toolkit.createLabel(fPBlockEditPanel, "Text Block: ", SWT.NONE);

		GridData gd1 = new GridData(GridData.FILL_BOTH);
		gd1.heightHint = 240;
		gd1.widthHint = 510;
		gd1.verticalIndent = 2;
		fPBlockText = toolkit.createText(fPBlockEditPanel, 
				aPBlock.getData(), SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		//fPBlockText.setText(aPBlock.getData());
		fPBlockText.setLayoutData(gd1);
		fPBlockText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				aPBlock.setData(fPBlockText.getText());
				setDirty(true);
			}
		});
		setDirty(false);
		AssessmentTestEditor.changeToContentColour(this);
	}
}
