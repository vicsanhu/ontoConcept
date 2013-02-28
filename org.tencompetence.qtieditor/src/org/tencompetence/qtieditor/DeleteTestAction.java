package org.tencompetence.qtieditor;

import java.io.File;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.resources.IResourcesModel;
import org.tencompetence.qtieditor.elements.AssessmentItemRef;
import org.tencompetence.qtieditor.elements.AssessmentSection;
import org.tencompetence.qtieditor.elements.AssessmentTest;
import org.tencompetence.qtieditor.elements.BasicElementList;
import org.tencompetence.qtieditor.elements.SectionPart;
import org.tencompetence.qtieditor.serialization.AssessmentTestSerializer;

public class DeleteTestAction {
	
    public static final String IMS_QTI_TYPE = "imsqti_test_xmlv2p1";
    
    public String getType() {
        return IMS_QTI_TYPE;
    }
    
    //delete a test file => cleaning work 
    public void deleteTest(File file, ILDModel ldModel) {
        if (file.exists()) {
        	String testPath = file.getAbsolutePath();        	
        	AssessmentTest fAssessmentTest = AssessmentTestSerializer.openModel(testPath);
        	
        	//remove corresponding resource
        	IResourcesModel resources = ldModel.getResourcesModel();
            IResourceModel testResource = resources.getResourceByHref(file.getName());
            if (testResource!=null)
            	resources.removeResource(testResource);
            
            //remove properties coupled with the test
            ldModel.getPropertiesModel();
        	
        	//remove all items
        	if (fAssessmentTest!=null) {
        		AssessmentSection fAssessmentSection = fAssessmentTest.getFirstTestPart().getFirstAssessmentSection();
        		BasicElementList aSectionPartList = fAssessmentSection.getSectionPartList();
        		for (int i = 0; i < aSectionPartList.size(); i++) {
        			SectionPart aSectionPart = (SectionPart) aSectionPartList
        					.getBasicElementAt(i);
        			if (aSectionPart instanceof AssessmentItemRef) {
        				String href = ((AssessmentItemRef) aSectionPart).getHref();
        				int index = href.indexOf("/");
        				if (index>0) {
        					String itemPath = file.getParent() + File.separator + href.substring(0, index-1) +
        					File.separator + href.substring(index+1);
        					AssessmentTestSerializer.removeFile(itemPath);
        				}
        			}
        		}        	
        	}

        	//remove test and the item folder
        	AssessmentTestSerializer.removeModel(testPath);
        }
    }    
}

