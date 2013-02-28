package org.tencompetence.qtieditor.serialization;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.tencompetence.jdom.JDOMXMLUtils;
import org.tencompetence.ldauthor.Logger;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.eclipse.swt.widgets.MessageBox;

public class AssessmentItemSerializer implements INamespaces {

   public AssessmentItemSerializer() {
    }

    public final static void handleNamespace(Element root){
    	//root.addNamespaceDeclaration(IMSQTI_NAMESPACE_2_0_EMBEDDED);
    	root.addNamespaceDeclaration(XSI_NAMESPACE);
        root.setAttribute(XSI_SCHEMALOCATION, IMSQTI_SCHEMALOCATION_2_0, XSI_NAMESPACE);    	
    }
    
    
    public static void saveModel(AssessmentItem fAssessmentItem, String saveTarget) {
    	Document doc = new Document();
		Comment comment = new Comment("This file was created with the TENCompetence QTI Assessment Item Editor on "+ new Date().toString());
        doc.addContent(comment);
                
        Element root = fAssessmentItem.toJDOM();
		doc.setRootElement(root);
        
        File file = new File(saveTarget);
        
        try {
        	JDOMXMLUtils.write2XMLFile(doc, file);
        }
        catch(IOException ex) {
        	MessageBox mDialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OK);
			mDialog.setText("Error Report");
			mDialog.setMessage("Error: saving an item xml file at: " + saveTarget);
			mDialog.open();
        	Logger.logError("AssessmentItemSerializer: Error saving an item xml file at: " + saveTarget); //$NON-NLS-1$
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
			mDialog.setMessage("Error: saving an item xml file at: " + saveTarget);
			mDialog.open();
        	Logger.logError("AssessmentItemSerializer: Error saving an item xml file at: " + saveTarget); //$NON-NLS-1$
        }
        
        if(bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
            	MessageBox mDialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OK);
            	mDialog.setText("Error Report");
    			mDialog.setMessage("Error: closing an item xml file at: " + saveTarget);
    			mDialog.open();
            	Logger.logError("AssessmentItemSerializer: Error closing an item xml file at: " + saveTarget); //$NON-NLS-1$
            }
        }
          */
   	
    }
    
    public static void removeModel(String path) {
    	File file = new File(path);
    	if (file.exists())
    		file.delete();
    }
    
    public static  void saveModel(AssessmentItem fAssessmentItem) {
    	FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		String saveTarget = dialog.open();
		saveModel(fAssessmentItem, saveTarget);
	}

    public static AssessmentItem openModel(String saveTarget) {
    	Document aJDOM = null;
		
		if (saveTarget != null) {
			SAXBuilder aSAXBuilder = new SAXBuilder();
			try {
				aJDOM = aSAXBuilder.build(new File(saveTarget));
			}
			catch (IOException event2) {
	        	MessageBox mDialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OK);
				mDialog.setText("Error Report");
				mDialog.setMessage("Error: opening an item xml file at: " + saveTarget);
				mDialog.open();
	        	Logger.logError("AssessmentItemSerializer: Error opening an item xml file at: " + saveTarget); //$NON-NLS-1$
			}
			catch (JDOMException event1) {
				MessageBox mDialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OK);
				mDialog.setText("Error Report");
				mDialog.setMessage("Error: parsing an item xml file at: " + saveTarget);
				mDialog.open();
	        	Logger.logError("AssessmentItemSerializer: Error parsing an item xml file at: " + saveTarget); //$NON-NLS-1$
			}
			
			if (aJDOM != null) {
				
				AssessmentItem newAssessmentItem = new AssessmentItem(aJDOM.getRootElement().getAttributeValue(AssessmentElementFactory.IDENTIFIER));
				newAssessmentItem.fromJDOM(aJDOM.getRootElement());
				return newAssessmentItem;
			}
		} 
		
		return null;
    }
    
    public static AssessmentItem openModel() {
    	FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OPEN);
		String saveTarget = dialog.open();
		return openModel(saveTarget);
		
    }
    
    
}
