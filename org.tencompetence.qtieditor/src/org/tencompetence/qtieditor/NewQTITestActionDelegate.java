package org.tencompetence.qtieditor;

import java.io.File;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.Logger;
import org.tencompetence.qtieditor.serialization.QTIEditorInput;
import org.tencompetence.qtieditor.ui.AssessmentTestEditor;
import org.tencompetence.qtieditor.ui.NewTestDialog;

/**
 * Handles Action "New QTI Test"
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewQTITestActionDelegate.java,v 1.5 2009/05/19 18:21:33 phillipus Exp $
 */
public class NewQTITestActionDelegate implements IEditorActionDelegate {
	
	private String rootPath = "";
	
    public NewQTITestActionDelegate() {
    }

    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        if(targetEditor == null) {
            return;
        }
        
        ILDModel ldModel = (ILDModel)targetEditor.getAdapter(ILDModel.class);
    	if(ldModel != null) {
            rootPath = ldModel.getRootFolder().getAbsolutePath();
    	}
    }

    public void run(IAction action) {
    	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();	
	
        File parent = new File(rootPath);

        if(parent != null) {
            if(!parent.isDirectory()) 
            	parent = parent.getParentFile();
        } else
        	return;
        
        if(parent.exists()) {
        	NewTestDialog dialog = new NewTestDialog(parent, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
            if(dialog.open() == Dialog.OK) {
                File newFile = dialog.getFile();
                if(newFile != null) {
                	
                	try {
                		newFile.mkdir(); //create a folder for containing all item files
                    }
                    catch(SecurityException ex) {
                    	Logger.logError("NewQTITestActionDelegate: Error creating item folder: " + newFile.getAbsolutePath());
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
                    	Logger.logError("NewQTITestActionDelegate: Error opening the editor: " + AssessmentTestEditor.ID); //$NON-NLS-1$
                    }
                }
            }
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

}