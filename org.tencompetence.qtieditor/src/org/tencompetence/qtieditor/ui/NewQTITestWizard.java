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
package org.tencompetence.qtieditor.ui;

import java.io.File;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.Logger;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.qtieditor.Activator;
import org.tencompetence.qtieditor.serialization.QTIEditorInput;


/**
 * New QTI Test Wizard
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewQTITestWizard.java,v 1.3 2009/01/28 13:55:47 miaoyw Exp $
 */
public class NewQTITestWizard
extends Wizard
implements INewWizard
{
	public static String ID = Activator.PLUGIN_ID + ".NewQTITestWizard"; //$NON-NLS-1$
    
    private NewQTITestWizardPage fMainPage;

    private ILDMultiPageEditor fCurrentLDEditor;
    
    /**
     * Default Constructor
     */
    public NewQTITestWizard() {
		setWindowTitle("New QTI Test");
	}
	
    /**
	 * Add the page to the wizard
	 */
	@Override
    public void addPages() {
	    fMainPage = new NewQTITestWizardPage(fCurrentLDEditor);
		addPage(fMainPage);
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	@Override
    public boolean performFinish() {
	    File file = fMainPage.getFile();
	    if(file != null) {
	        QTIEditorInput input = new QTIEditorInput(file); 	        
	        
	        String folderPath = file.getAbsolutePath();
	        folderPath = folderPath.substring(0, folderPath.length()-4);
        	File itemFolder = new File(folderPath);
        	try {
        		itemFolder.mkdir(); //create a folder for containing all item files
            }
            catch(SecurityException ex) {
                 ex.printStackTrace();
            }	        
                                 
            try {
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(); 
                page.openEditor(input, AssessmentTestEditor.ID);
            }
            catch(PartInitException ex) {
                Logger.logError("NewQTIItemActionDelegate: Error opening the editor: " + AssessmentTestEditor.ID); //$NON-NLS-1$
            }
	    }
	    
        return true;
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	    if(workbench != null) {
	        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
	        if(window != null) {
	            checkEditorOpen(window);
	        }
	    }
	}
	
    /**
     * Check if there is at least one LD Editor open so that the wizard has an LD Editor context to start with.
     */
    private void checkEditorOpen(IWorkbenchWindow window) {
        if(window.getActivePage() != null) {
            // If there is one active use that
            IEditorPart editorPart = window.getActivePage().getActiveEditor();
            if(editorPart instanceof ILDMultiPageEditor) {
                fCurrentLDEditor = (ILDMultiPageEditor)editorPart;
                return;
            }
            
            // Else use brute force to find a default LD Editor
            IEditorReference[] editors = window.getActivePage().getEditorReferences();
            for(IEditorReference editorReference : editors) {
                IWorkbenchPart part = editorReference.getPart(false);
                if(part instanceof ILDMultiPageEditor) {
                    fCurrentLDEditor = (ILDMultiPageEditor)editorPart;
                    return;
                }
            }
            
            // No, so none
            fCurrentLDEditor = null;
        }
    }

}