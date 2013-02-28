package org.tencompetence.qtieditor.serialization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import org.tencompetence.jdom.JDOMXMLUtils;
import org.tencompetence.ldauthor.Logger;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentTest;

public class AssessmentTestSerializer implements INamespaces {

   public AssessmentTestSerializer() {
    }

    public final static void handleNamespace(Element root){
    	//root.addNamespaceDeclaration(IMSQTI_NAMESPACE_2_0_EMBEDDED);
    	root.addNamespaceDeclaration(XSI_NAMESPACE);
        root.setAttribute(XSI_SCHEMALOCATION, IMSQTI_SCHEMALOCATION_2_1, XSI_NAMESPACE);    	
    }
    
    
    public static void saveModel(AssessmentTest fAssessmentTest, String saveTarget) {
    	Document doc = new Document();
		Comment comment = new Comment("This file was created with the TENCompetence QTI Assessment Test Editor on "+ new Date().toString());
        doc.addContent(comment);
                
        Element root = fAssessmentTest.toJDOM();
		doc.setRootElement(root);
		
        File file = new File(saveTarget);
        
        try {
        	JDOMXMLUtils.write2XMLFile(doc, file);
        }
        catch(IOException ex) {
        	MessageBox mDialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OK);
			mDialog.setText("Error Report");
			mDialog.setMessage("Error: saving a test xml file at: " + saveTarget);
			mDialog.open();
        	Logger.logError("AssessmentTestSerializer: Error saving a test at: " + saveTarget); //$NON-NLS-1$
        }
        /*
        
		XMLOutputter outputter = new XMLOutputter();
		String xml = outputter.outputString(doc);
        BufferedWriter bw = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter isw = new OutputStreamWriter(fos, "UTF-8"); //$NON-NLS-1$
            bw = new BufferedWriter(isw);
            bw.write(xml);
        }
        catch(IOException ex) {
        	MessageBox mDialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OK);
			mDialog.setText("Error Report");
			mDialog.setMessage("Error: saving a test xml file at: " + saveTarget);
			mDialog.open();
        	Logger.logError("AssessmentTestSerializer: Error saving a test at: " + saveTarget); //$NON-NLS-1$
        }
        
        if(bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
            	MessageBox mDialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OK);
    			mDialog.setText("Error Report");
    			mDialog.setMessage("Error: closing a test xml file at: " + saveTarget);
    			mDialog.open();
    			Logger.logError("AssessmentTestSerializer: Error closing a test xml file at: " + saveTarget); //$NON-NLS-1$
            }
        }
        */
    }
    
    public static void saveModel(AssessmentTest fAssessmentTest) {
    	FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		String saveTarget = dialog.open();
		saveModel(fAssessmentTest, saveTarget);		
	}
    
    
    public static void removeModel(String path) {
    	File file = new File(path);
    	if (file.exists())
    		file.delete();
    	
    	// remove test folder
    	int index = path.lastIndexOf(".");
    	String folderName = path.substring(0, index);
            
    	file = new File(folderName);
    	if (file.exists()&&file.isDirectory())
    		file.delete();
 
    }

    public static void removeFile(String path) {
    	File file = new File(path);
    	if (file.exists())
    		file.delete();
    }
    
    public static AssessmentTest openModel(String saveTarget) {
    	Document aJDOM = null;
		
		if (saveTarget != null) {
			SAXBuilder aSAXBuilder = new SAXBuilder();
			try {
				aJDOM = aSAXBuilder.build(new File(saveTarget));
			}
			catch (IOException event2) {
				MessageBox mDialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OK);
				mDialog.setText("Error Report");
				mDialog.setMessage("Error: opening a test xml file at: " + saveTarget);
				mDialog.open();
				Logger.logError("AssessmentTestSerializer: Error opening a test xml file at: " + saveTarget); //$NON-NLS-1$
			}
			catch (JDOMException event1) {
				MessageBox mDialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OK);
				mDialog.setText("Error Report");
				mDialog.setMessage("Error: parsing a test xml file from: " + saveTarget);
				mDialog.open();
				Logger.logError("AssessmentTestSerializer: Error parsing a test xml file from: " + saveTarget); //$NON-NLS-1$
			}
			
			if (aJDOM != null) {
				AssessmentTest newAssessmentTest = new AssessmentTest(aJDOM.getRootElement().getAttributeValue(AssessmentElementFactory.IDENTIFIER));
				newAssessmentTest.fromJDOM(aJDOM.getRootElement());
				return newAssessmentTest;
			}
		} 
		
		return null;
    }
    
    public static AssessmentTest openModel() {
    	FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OPEN);
		String saveTarget = dialog.open();
		return openModel(saveTarget);
		
    }
    
}
 
