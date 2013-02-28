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
package org.tencompetence.ldauthor.ui.wizards.zip;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.serialization.LDModelValidator;
import org.tencompetence.ldauthor.ui.views.checker.CheckerTreeViewer;
import org.tencompetence.ldauthor.ui.views.checker.CheckerXMLErrorComposite;
import org.tencompetence.ldauthor.ui.views.checker.XMLException;


/**
 * Publish to CopperCore Wizard Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: PublishCopperCoreWizardPage.java,v 1.4 2009/06/30 10:24:23 phillipus Exp $
 */
public class PublishCopperCoreWizardPage extends WizardPage {

    public static final String PAGE_NAME = "PublishCopperCoreWizardPage"; //$NON-NLS-1$
    
    private IReCourseLDModel fLDModel;
    
    /**
	 * Constructor
	 */
	public PublishCopperCoreWizardPage(IReCourseLDModel ldModel) {
		super(PAGE_NAME);
		setTitle(Messages.PublishCopperCoreWizardPage_0);
		setDescription(Messages.PublishCopperCoreWizardPage_1);
        //setImageDescriptor(ImageFactory.getImageDescriptor("An Image"));
		
		fLDModel = ldModel;
	}

    public void createControl(Composite parent) {
	    Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        
        Label statusLabel = new Label(container, SWT.NULL);
        statusLabel.setText(Messages.PackageToZipWizardPage_10);
        
        SashForm sashForm = new SashForm(container, SWT.VERTICAL);
        GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
        // Stop growth!
        gd.widthHint = 600;
        gd.heightHint = 350;
        sashForm.setLayoutData(gd);
        
        CheckerTreeViewer checkerTreeViewer = new CheckerTreeViewer(sashForm, SWT.BORDER);
        checkerTreeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        checkerTreeViewer.setInput(fLDModel);
        
        CheckerXMLErrorComposite errorViewer = new CheckerXMLErrorComposite(sashForm, SWT.BORDER);
        
        LDModelValidator validator = new LDModelValidator(fLDModel);
        List<XMLException> errorList = null;
        try {
            errorList = validator.validate();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        errorViewer.setInput(errorList);
        
        setControl(container);
        
        setPageComplete(errorList != null && errorList.isEmpty());
	}

}