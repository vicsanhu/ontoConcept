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
package org.tencompetence.ldauthor.ui.wizards.ld;

import java.io.File;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.INewWizard;
import org.tencompetence.ldauthor.LDAuthorException;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.organisermodel.IOrganiserLD;
import org.tencompetence.ldauthor.organisermodel.OrganiserModelFactory;
import org.tencompetence.ldauthor.templates.ILDTemplate;
import org.tencompetence.ldauthor.templates.LDTemplateManager;
import org.tencompetence.ldauthor.templates.impl.ld.ClonedLDTemplate;
import org.tencompetence.ldauthor.ui.editors.EditorManager;
import org.tencompetence.ldauthor.ui.views.organiser.OrganiserView;
import org.tencompetence.ldauthor.ui.views.organiser.global.OrganiserComposite;
import org.tencompetence.ldauthor.ui.views.organiser.global.operations.NewOrganiserEntryOperation;
import org.tencompetence.ldauthor.ui.wizards.organiser.AbstractOrganiserWizard;


/**
 * New LD Wizard
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewLDWizard.java,v 1.7 2008/12/15 12:00:32 phillipus Exp $
 */
public class NewLDWizard
extends AbstractOrganiserWizard
implements INewWizard
{
	public static String ID = LDAuthorPlugin.PLUGIN_ID + ".NewLDWizard"; //$NON-NLS-1$
    
    private NewLDWizardPage fMainPage;
    private NewLDWizardPageLocation fLocationPage;
    private NewLDWizardPageOverview fOverviewPage;
    
    private LDTemplateManager fTemplateManager;

    /**
     * Default Constructor
     */
    public NewLDWizard() {
		setWindowTitle(Messages.NewLDWizard_0);
		fTemplateManager = new LDTemplateManager();
	}
	
    /**
	 * Add the page to the wizard
	 */
	@Override
    public void addPages() {
	    fMainPage = new NewLDWizardPage(fTemplateManager);
		addPage(fMainPage);
		
		fLocationPage = new NewLDWizardPageLocation();
        addPage(fLocationPage);
		
		fOverviewPage = new NewLDWizardPageOverview();
		addPage(fOverviewPage);
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	@Override
    public boolean performFinish() {
        // Get template
        ILDTemplate template = fMainPage.getSelectedTemplate();
        
        if(template == null) {
            return false;
        }
	    
	    // Create Root folder
	    File targetFolder = fLocationPage.getFolder();
        targetFolder.mkdirs();

        // Manifest file
        File manifestFile = new File(targetFolder, "imsmanifest.xml"); //$NON-NLS-1$
	    
        // Special case
        if(template instanceof ClonedLDTemplate) {
            File copiedLD = fLocationPage.getCopiedFolder();
            ((ClonedLDTemplate)template).setCopiedFolder(copiedLD);
        }
        
        // Then create it
        try {
            template.create(manifestFile);
        }
        catch(LDAuthorException ex) {
            MessageDialog.openError(getShell(), Messages.NewLDWizard_1, ex.getMessage());
        }

        // Add an Organiser Entry
        IOrganiserLD organiserEntry = OrganiserModelFactory.createOrganiserLD(fLocationPage.getNameText(), manifestFile);
        
        // The Organiser View has a static Undo Context so we need to use it
        IUndoContext undoContext = OrganiserView.getUndoContext(OrganiserComposite.class);
        try {
            getOperationHistory().execute(
                    new NewOrganiserEntryOperation(undoContext, fParent, organiserEntry),
                    null,
                    null);
        }
        catch(ExecutionException e) {
            e.printStackTrace();
        }
        
        // Open the Editor
        EditorManager.openLDEditor(organiserEntry.getName(), organiserEntry.getFile());
	    
        return true;
	}
}