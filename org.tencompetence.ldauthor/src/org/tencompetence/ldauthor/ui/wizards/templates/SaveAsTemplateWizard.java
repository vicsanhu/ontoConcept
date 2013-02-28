/*
 * Copyright (c) 2008, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.ui.wizards.templates;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.templates.ITemplateGroup;
import org.tencompetence.ldauthor.templates.LDTemplateManager;
import org.tencompetence.ldauthor.templates.impl.ld.UserLDTemplate;
import org.tencompetence.ldauthor.utils.FileUtils;


/**
 * Save As Template Zip Wizard
 * 
 * @author Phillip Beauvoir
 * @version $Id: SaveAsTemplateWizard.java,v 1.4 2009/12/10 14:48:27 phillipus Exp $
 */
public class SaveAsTemplateWizard
extends Wizard {
	
    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".SaveAsTemplateWizard"; //$NON-NLS-1$
    
    /**
     * The single Page
     */
    private SaveAsTemplateWizardPage fPage;
    
    private ILDModel fLDModel;
    
    private LDTemplateManager fLDTemplateManager;
    
    /**
     * Default Constructor
     * @param title 
     */
    public SaveAsTemplateWizard(ILDModel ldModel, String title) {
		setWindowTitle(Messages.SaveAsTemplateWizard_0 + " - " + title); //$NON-NLS-1$
		fLDModel = ldModel;
		fLDTemplateManager = new LDTemplateManager();
	}
	
    /**
	 * Add the page to the wizard
	 */
	@Override
    public void addPages() {
	    fPage = new SaveAsTemplateWizardPage(fLDTemplateManager);
		addPage(fPage);
	}
    
	@Override
    public boolean performFinish() {
	    String name = fPage.getTemplateName();
	    String description = fPage.getTemplateDescription();
	    ITemplateGroup group = fPage.getTemplateGroup();
	    
	    UserLDTemplate template = new UserLDTemplate();
	    template.setName(name);
	    template.setDescription(description);
	    template.setLocation(UUID.randomUUID().toString());
	    
	    group.addTemplate(template);
	    
	    try {
            fLDTemplateManager.saveUserTemplatesManifest();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        // Copy Files
        
        File sourceFolder = fLDModel.getRootFolder();
        
        File targetFolder = template.getSourceFolder();
        targetFolder.mkdirs();
        
        try {
            FileUtils.copyFolder(sourceFolder, targetFolder);
        }
        catch(IOException ex) {
            MessageDialog.openError(getShell(), Messages.SaveAsTemplateWizard_1, ex.getMessage());
        }
	    
        return true;
	}
	
}