/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.ldauthor.ui.wizards.organiser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * New Resource Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewOrganiserResourceWizardPage.java,v 1.2 2008/11/20 16:37:13 phillipus Exp $
 */
public class NewOrganiserResourceWizardPage extends AbstractNewObjectWizardPage {

    public static final String PAGE_NAME = "NewResourceWizardPage"; //$NON-NLS-1$
    
    /**
     * The Text controls
     */
    private Text fTypeTextField, fLocationTextField;

    
    /**
	 * Constructor
	 */
	public NewOrganiserResourceWizardPage() {
		super(PAGE_NAME);
		setTitle(Messages.NewResourceWizardPage_0);
		setDescription(Messages.NewResourceWizardPage_1);
		setImageDescriptor(ImageFactory.getEclipseImageDescriptor(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
	}

	@Override
	public void createControl(Composite parent) {
	    super.createControl(parent);
	    
        Label label = new Label(fFieldContainer, SWT.NULL);
        label.setText(Messages.NewResourceWizardPage_2);
        fLocationTextField = new Text(fFieldContainer, SWT.BORDER | SWT.SINGLE);
        fLocationTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fLocationTextField.setLayoutData(gd);

        label = new Label(fFieldContainer, SWT.NULL);
        label.setText(Messages.NewResourceWizardPage_3);
        fTypeTextField = new Text(fFieldContainer, SWT.BORDER | SWT.SINGLE);
        fTypeTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fTypeTextField.setLayoutData(gd);

	}
	
    /**
     * @return The text in the Location field
     */
    public String getLocationText() {
        return fLocationTextField.getText();
    }

    /**
     * @return The text in the Type field
     */
    public String getTypeText() {
        return fTypeTextField.getText();
    }

    @Override
    protected void validateFields() {
        String name = getNameText();
        
        if(!StringUtils.isSetAfterTrim(name)) {
            updateStatus(Messages.AbstractNewObjectWizardPage_1);
            return;
        }
        
        // OK
        updateStatus(null);
    }
}