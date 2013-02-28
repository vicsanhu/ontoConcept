/*
 * Copyright (c) 2009, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.qti.ui;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.wizard.Wizard;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.qti.model.QTIUtils;
import org.tencompetence.ldauthor.ui.editors.EditorManager;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * New QTI Test Activity Wizard - Adds Activity
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewQTITestActivityWizard.java,v 1.4 2009/05/19 18:21:04 phillipus Exp $
 */
public class NewQTITestActivityWizard
extends Wizard {
	
    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".NewQTITestActivityWizard"; //$NON-NLS-1$
    
    /**
     * The single Page
     */
    private NewQTITestActivityWizardPage fPage;
    
    private IActivityStructureModel fParentAS;
    
    private ILDModel fLDModel;
    
    /**
     * Default Constructor
     */
    public NewQTITestActivityWizard(ILDModel ldModel) {
		setWindowTitle(Messages.NewQTITestActivityWizard_0);
		fLDModel = ldModel;
	}
	
    /**
     * Default Constructor for adding a child Test Activity to Activity Structure
     */
    public NewQTITestActivityWizard(IActivityStructureModel as) {
        setWindowTitle(Messages.NewQTITestActivityWizard_0);
        fParentAS = as;
        fLDModel = as.getLDModel();
    }
    
    /**
	 * Add the page to the wizard
	 */
	@Override
    public void addPages() {
	    fPage = new NewQTITestActivityWizardPage(fLDModel);
		addPage(fPage);
	}
    
	@Override
    public boolean performFinish() {
	    String name = fPage.getTestName();
	    if(!StringUtils.isSet(name)) { 
            return false;
        }
	    
        File file = null;

        // Existing Test File
        if(fPage.isExistingTestSelected()) {
            IResourceModel resource = fPage.getSelectedTestResource();
            QTIUtils.addQTIResourceAsActivity(resource, name, fParentAS, fLDModel);
            file = new File(fLDModel.getRootFolder(), resource.getHref());
        }
        // New test File
        else {
            try {
                file = QTIUtils.createQTITestFile(name, fLDModel);
            }
            catch(IOException ex) {
                ex.printStackTrace();
                return false;
            }
            // Add as an Activity
            QTIUtils.addQTIFileAsActivity(file, name, fParentAS, fLDModel);
        }
        
        // Open the QTI Editor
        EditorManager.editFile(file, fLDModel);
	    
        return true;
	}
	
}