package org.tencompetence.qtieditor;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.Logger;
import org.tencompetence.qtieditor.serialization.QTIEditorInput;
import org.tencompetence.qtieditor.ui.AssessmentTestEditor;
import org.tencompetence.qtieditor.ui.NewTestDialog;



/**
 * Handles Toolbar "New QTI Test"
 * 
 * @author Phillip Beauvoir
 * @version $Id: ToolbarContributionHandler.java,v 1.16 2009/05/19 18:21:33 phillipus Exp $
 */
public class ToolbarContributionHandler extends AbstractHandler {
	
	private File fRootFolder;
	private ILDModel ldModel;

	public Object execute(ExecutionEvent event) throws ExecutionException {
	    
	    IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
        if(page == null) {
            return null;
        }
        IEditorPart editor = page.getActiveEditor();
        if(editor == null) {
            return null;
        }

        ldModel = (ILDModel)editor.getAdapter(ILDModel.class);
        if(ldModel != null) {
        	fRootFolder = ldModel.getRootFolder();
        }
        else {
            return null;
        }		
        
        if(fRootFolder.exists()) {
            NewTestDialog dialog = new NewTestDialog(fRootFolder, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
            if(dialog.open() == Dialog.OK) {
                File newFile = dialog.getFile();
                if(newFile != null) {
                	
                	try {
                		newFile.mkdir(); //create a folder for containing all item files
                    }
                    catch(SecurityException ex) {
                    	Logger.logError("ToolbarContributionHandler: Error creating item folder: " + newFile.getAbsolutePath());
                    }

                	/*
                    String href = newFile.getName()+".xml";
                    IResourcesModel resources = ldModel.getResourcesModel();
                    IResourceModel testResource = LDModelFactory.createNewResourceWithHref(ldModel, href, newFile.getName());
                    testResource.setType("imsqti_test_xmlv2p1");
                    resources.addResource(testResource);
                    */
                    QTIEditorInput input = new QTIEditorInput(new File(newFile.getAbsolutePath()+".xml")); 
 
                    try {
                        page.openEditor(input, AssessmentTestEditor.ID);
                    }
                    catch(PartInitException ex) {
                        Logger.logError("ToolbarContributionHandler: Could not open the editor: " + AssessmentTestEditor.ID); //$NON-NLS-1$
                    }
                }
            }
        }

		return null;
	}

}
