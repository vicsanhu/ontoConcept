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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.ui.INewWizard;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.organisermodel.IOrganiserResource;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserResource;
import org.tencompetence.ldauthor.ui.views.organiser.OrganiserView;
import org.tencompetence.ldauthor.ui.views.organiser.global.OrganiserComposite;
import org.tencompetence.ldauthor.ui.views.organiser.global.operations.NewOrganiserEntryOperation;


/**
 * New Organiser Resource Wizard
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewOrganiserResourceWizard.java,v 1.3 2008/11/19 21:56:43 phillipus Exp $
 */
public class NewOrganiserResourceWizard
extends AbstractOrganiserWizard
implements INewWizard
{
	public static String ID = LDAuthorPlugin.PLUGIN_ID + ".NewOrganiserResourceWizard"; //$NON-NLS-1$
    
    /**
     * The single Page
     */
    private NewOrganiserResourceWizardPage fPage;
    
    /**
     * Default Constructor
     */
    public NewOrganiserResourceWizard() {
		setWindowTitle(Messages.NewResourceWizard_0);
	}
	
    /**
	 * Add the page to the wizard
	 */
	@Override
    public void addPages() {
	    fPage = new NewOrganiserResourceWizardPage();
		addPage(fPage);
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	@Override
    public boolean performFinish() {
        IOrganiserResource organiserEntry = new OrganiserResource(fPage.getNameText());
        organiserEntry.setResourceLocation(fPage.getLocationText());
        organiserEntry.setResourceType(fPage.getTypeText());
        
        // The Organiser View has a static Undo Context so we need to show it
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

        return true;
	}
	
}