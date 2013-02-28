package org.tencompetence.ldauthor;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.tencompetence.ldauthor.ui.views.IDM.IDMethod;

public class PerspectiveFactoryID implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		// TODO Auto-generated method stub

		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		
		layout.addStandaloneView(IDMethod.ID,  false, IPageLayout.LEFT, 1.0f, editorArea);

		
		
	}

}
