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

import org.eclipse.jface.wizard.Wizard;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.utils.CopperCoreUtils;


/**
 * Publish to CopperCore Wizard
 * 
 * @author Phillip Beauvoir
 * @version $Id: PublishCopperCoreWizard.java,v 1.2 2009/05/19 18:21:04 phillipus Exp $
 */
public class PublishCopperCoreWizard
extends Wizard {
	
    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".PublishCopperCoreWizard"; //$NON-NLS-1$
    
    /**
     * The single Page
     */
    private PublishCopperCoreWizardPage fPage;
    
    private IReCourseLDModel fLDModel;
    
    /**
     * Default Constructor
     * @param title 
     */
    public PublishCopperCoreWizard(IReCourseLDModel ldModel, String title) {
		setWindowTitle(Messages.PublishCopperCoreWizard_0 + " - " + title); //$NON-NLS-1$
		fLDModel = ldModel;
	}
	
    /**
	 * Add the page to the wizard
	 */
	@Override
    public void addPages() {
	    fPage = new PublishCopperCoreWizardPage(fLDModel);
		addPage(fPage);
	}
    
	@Override
    public boolean performFinish() {
	    CopperCoreUtils.publish(fLDModel);
        return true;
	}
	
}