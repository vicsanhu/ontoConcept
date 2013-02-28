package org.tencompetence.qtieditor.ui;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;


public class BlankComposite extends Composite {
    
    public BlankComposite(Composite parent, int style) {
        super(parent, style);
        
        setLayout(new GridLayout());
        
        Section section = AppFormToolkit.getInstance().createSection(this, Section.TITLE_BAR);
        section.setText("");
        section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }
}
