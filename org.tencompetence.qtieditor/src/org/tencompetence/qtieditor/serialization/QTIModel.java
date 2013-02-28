package org.tencompetence.qtieditor.serialization;

import java.io.File;

import org.tencompetence.qtieditor.elements.AssessmentTest;
import org.tencompetence.ldauthor.Logger;

public class QTIModel {
    
    private AssessmentTest fAssessmentTest;
    
    private File fQTIFile;
    
      
    public QTIModel(QTIEditorInput input, File qtiFile) {
    	fQTIFile = qtiFile;
    	if(qtiFile.exists()){
    		fAssessmentTest = AssessmentTestSerializer.openModel(qtiFile.getAbsolutePath());
    	}else {
    		fAssessmentTest = new AssessmentTest();
    		AssessmentTestSerializer.saveModel(fAssessmentTest, fQTIFile.getAbsolutePath());
    		
    		/* 
    		 * the itemFoler will be created in wizard
    		String addr = fQTIFile.getParent() + File.separator + fAssessmentTest.getId();
         	try {
        		File itemFolder = new File(addr);
         		itemFolder.mkdir();
            }
            catch(SecurityException ex) {
            	Logger.logError("QTIModel: Error creating a folder at: " + addr); //$NON-NLS-1$
            }   
            */    	
    	}
    }
    
    public AssessmentTest getAssessmentTest() {
		return fAssessmentTest;
	}
  
    public void setAssessmentTest(AssessmentTest aAssessmentTest) {
		this.fAssessmentTest = aAssessmentTest;
	}

}
