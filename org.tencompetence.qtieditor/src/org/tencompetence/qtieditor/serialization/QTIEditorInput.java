package org.tencompetence.qtieditor.serialization;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.resources.IResourcesModel;
import org.tencompetence.imsldmodel.properties.IPropertiesModel;
import org.tencompetence.imsldmodel.properties.ILocalPropertyModel;
import org.eclipse.ui.IPersistableElement;

import org.tencompetence.qtieditor.elements.AssessmentItem;


/**
 * Editor Input for QTI Editor
 * 
 * @author Phillip Beauvoir
*/
public class QTIEditorInput 
implements IEditorInput
{
	  /**
     * The Model
     */
    private QTIModel fModel;
    
    private File qtiFile;
    private String testFolder;
    private String itemFolder;
    
    private IResourcesModel resources;
    private IResourceModel testResource;
    private ILDModel ldModel;
    private IPropertiesModel properties;
    private String fTestID;
    
    public QTIEditorInput(File aFile) {
		qtiFile = aFile;
		if (qtiFile == null) {
			throw new IllegalArgumentException("File cannot be null"); //$NON-NLS-1$
		} else {
			int index = qtiFile.getAbsolutePath().lastIndexOf(".");
			if ((index>-1) && (index<qtiFile.getAbsolutePath().length())) {
				testFolder = qtiFile.getParent() + File.separator;
				itemFolder = qtiFile.getAbsolutePath().substring(0, index)+ File.separator;
			}
		}
		
		//ldModel may be useful in the future
		IEditorPart ldEditor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		if (ldEditor instanceof ILDMultiPageEditor) {
			ldModel = (ILDModel) ldEditor.getAdapter(ILDModel.class);
			properties = ldModel.getPropertiesModel();
		}
		

	}

    /**
     * @return The Model
     */
    public QTIModel getModel() {
        if(fModel == null) {
            fModel = new QTIModel(this, qtiFile);          
        }
        return fModel;
    }
    
    /**
     * Save the Model
     * @throws IOException 
     */
    public void saveAssessmentTest() throws IOException {
    	AssessmentTestSerializer.saveModel(fModel.getAssessmentTest(), qtiFile.getAbsolutePath());
     }
    
    public void removeAssessmentTest() {
    	AssessmentTestSerializer.removeModel(qtiFile.getAbsolutePath());
    	/*
		IResourceModel resource = resources.getResourceByHref(getName()+".xml");
		resources.removeResource(resource);
		*/
     }
    
    public void createAssessmentItem(AssessmentItem aAssessmentItem) {
		AssessmentItemSerializer.saveModel(aAssessmentItem, itemFolder + aAssessmentItem.getId() + ".xml");
		/*
        IResourceModel itemResource = LDModelFactory.createNewResourceWithHref(ldModel, aHref, aAssessmentItem.getId());
        itemResource.setType("imsqti_item_xmlv2p0");
        resources.addResource(itemResource);
        ldModel.getResourcesModel();
        */
	}
    
    public void saveAssessmentItem(AssessmentItem aAssessmentItem) {
		AssessmentItemSerializer.saveModel(aAssessmentItem, itemFolder + aAssessmentItem.getId() + ".xml");
	}
    
    public AssessmentItem loadAssessmentItem(String href) {
		return AssessmentItemSerializer.openModel(testFolder + transformHref(href));
	}

    public void removeAssessmentItem(String href) {
		AssessmentItemSerializer.removeModel(testFolder + transformHref(href));
		/*
		IResourceModel resource = resources.getResourceByHref(aHref);
		resources.removeResource(resource);
		*/
	}
    
    private String transformHref(String href) {
    	String result = "";
    	int index = href.indexOf("/");
    	while (index!=-1) {
    		result = result + href.substring(0, index) + File.separator;
    		href = href.substring(index+1);
    		index = href.indexOf("/");
    	}
    	return result + href;
    }
	
    public boolean exists() {
        return true;
    }

    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    public String getName() {
        if (qtiFile != null) {
        	String name = qtiFile.getName();
        	int index = name.lastIndexOf(".");
        	return name.substring(0, index);
        }
        else
        	return "Test";
    }



    public ILDModel getLDModel( ) {
    	return ldModel;
    }
 
    public IPropertiesModel getProperties( ) {
    	return properties;
    }
    
    public void setResources(IResourcesModel aResources) {
    	resources = aResources;
    }

    public IResourcesModel getResources( ) {
    	return resources;
    }
    
    public void setTestResource(IResourceModel aResource) {
    	testResource = aResource;
    }

    public IResourceModel getTestResource( ) {
    	return testResource;
    }
    
    public IPersistableElement getPersistable() {
        return null;
    }

    public String getTestID() {
        return fTestID;
    }
    
    public void setTestID(String id) {
        fTestID = id;
    }
    
    public String getToolTipText() {
        return qtiFile.toString();
    }

    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if(adapter == QTIModel.class) {
            return getModel();
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        
        if(!(obj instanceof QTIEditorInput)) {
            return false;
        }
        
        return qtiFile.equals(((QTIEditorInput)obj).qtiFile);
    }
    
}