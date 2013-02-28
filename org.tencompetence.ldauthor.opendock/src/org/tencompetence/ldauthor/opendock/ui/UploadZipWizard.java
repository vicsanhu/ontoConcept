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
package org.tencompetence.ldauthor.opendock.ui;

import java.io.File;

import org.eclipse.jface.wizard.Wizard;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.opendock.UOL_UploadInfo;


/**
 * Upload Zip Wizard
 * 
 * @author Phillip Beauvoir
 * @version $Id: UploadZipWizard.java,v 1.2 2008/04/25 11:46:13 phillipus Exp $
 */
public class UploadZipWizard
extends Wizard {
	
    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".UploadZipWizard"; //$NON-NLS-1$
    
    /**
     * The single Page
     */
    private UploadZipWizardPage fPage;
    
    private UOL_UploadInfo fUploadInfo;
    
    /**
     * Default Constructor
     * @param uploadInfo 
     */
    public UploadZipWizard(UOL_UploadInfo uploadInfo) {
        fUploadInfo = uploadInfo;
		setWindowTitle(Messages.UploadZipWizard_0);
	}
	
    /**
	 * Add the page to the wizard
	 */
	@Override
    public void addPages() {
	    fPage = new UploadZipWizardPage();
		addPage(fPage);
	}
	
	@Override
    public boolean performFinish() {
	    fUploadInfo.file = new File(fPage.fFileText.getText());
	    
	    fUploadInfo.metaTitle = fPage.fTitleText.getText();
	    fUploadInfo.metaShortDesc = fPage.fShortDescText.getText();
	    fUploadInfo.metaLongDesc = fPage.fLongDescText.getText();
        fUploadInfo.cc_Language = fPage.fLanguageCombo.getText();
	    fUploadInfo.cc_CopyRightHolder = fPage.fCopyrightHolderText.getText();
	    fUploadInfo.cc_CopyrightYear = fPage.fCopyrightYearText.getText();
	    
	    return true;
	}

}