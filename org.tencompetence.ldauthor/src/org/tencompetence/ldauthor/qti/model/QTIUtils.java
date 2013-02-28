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
package org.tencompetence.ldauthor.qti.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.IMSNamespaces;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivitiesModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.properties.ILocalPersonalPropertyModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.jdom.JDOMXMLUtils;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * QTI Utils used to bridge to the QTI Module
 * 
 * @author Phillip Beauvoir
 * @version $Id: QTIUtils.java,v 1.6 2009/06/16 10:01:00 phillipus Exp $
 */
public class QTIUtils {

    public static Namespace QTI_NAMESPACE_2_1 = Namespace.getNamespace("http://www.imsglobal.org/xsd/imsqti_v2p1"); //$NON-NLS-1$
    public static String QTI_SCHEMALOCATION_2_1 = "http://www.imsglobal.org/xsd/imsqti_v2p1 imsqti_v2p1.xsd"; //$NON-NLS-1$
    public static final String IMS_QTI_TYPE = "imsqti_test_xmlv2p1"; //$NON-NLS-1$
    
    private static final String FRIENDLY_QTI_TEXT = Messages.QTIUtils_0;
    
    /**
     * Create a new, basic, blank QTI file for this model
     * 
     * @param name The proposed name of the file, this will be normalised
     * @return The newly created file
     * @throws IOException
     */
    public static File createQTITestFile(String name, ILDModel ldModel) throws IOException {
        File rootFolder = ldModel.getRootFolder();
        
        // Normalise the file name
        String fileName = FileUtils.getValidFileName(name);
        
        File file = new File(rootFolder, fileName + ".xml"); //$NON-NLS-1$
        
        Document doc = new Document();
        Element root = new Element("assessmentTest", QTI_NAMESPACE_2_1); //$NON-NLS-1$
        doc.setRootElement(root);
        
        root.addNamespaceDeclaration(IMSNamespaces.XSI_NAMESPACE);
        root.setAttribute(IMSNamespaces.XSI_SCHEMALOCATION, QTI_SCHEMALOCATION_2_1, IMSNamespaces.XSI_NAMESPACE);
        
        String identifier = "AT-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        root.setAttribute("identifier", identifier); //$NON-NLS-1$
        
        JDOMXMLUtils.write2XMLFile(doc, file);
        
        // Have to write a foldername with the same file name - this is crazy!!
        // What if the folder name already exists?
        File folder = new File(rootFolder, fileName);
        folder.mkdirs();
        
        return file;
    }

    /**
     * Create a new Learning Activity with newly created Item and Resource that points to the QTI File
     * Add a QTI Test File as an Activity with an Item that points to a newly created Resource that points to the QTI File
     * @param file The QTI Test File
     * @param name The Name of the Activity
     * @param parentAS The Parent Activity Structure if adding as child or null if not
     * @param ldModel LD Model
     * @return
     */
    public static ILearningActivityModel addQTIFileAsActivity(File file, String name, IActivityStructureModel parentAS, ILDModel ldModel) {
        // Create a new Resource for this File
        IResourceModel resource = addQTIFileAsResource(file, name, ldModel);
        return addQTIResourceAsActivity(resource, name, parentAS, ldModel);
    }
    
    /**
     * Add a QTI File as a Resource
     * @param file
     * @param name
     * @param ldModel
     * @return
     */
    public static IResourceModel addQTIFileAsResource(File file, String name, ILDModel ldModel) {
        IResourceModel resource = LDModelUtils.createNewResourceWithHref(ldModel, file.getName(), name);
        resource.setType(IMS_QTI_TYPE);
        return resource;
    }
    
    /**
     * Create a new Learning Activity with newly created Item that points to the given, existing QTI Resource
     * @param resource The QTI Resource
     * @param name The Name of the Activity
     * @param parentAS The Parent Activity Structure if adding as child
     * @param ldModel LD Model
     * @return
     */
    public static ILearningActivityModel addQTIResourceAsActivity(IResourceModel resource, String name, IActivityStructureModel parentAS, ILDModel ldModel) {
        // Create an Activity
        ILearningActivityModel activity = (ILearningActivityModel)LDModelFactory.createModelObject(LDModelFactory.LEARNING_ACTIVITY, ldModel);
        activity.setTitle(name);
        
        // Create Item pointing to the Resource
        IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, ldModel);
        itemType.setTitle("QTI Test"); //$NON-NLS-1$
        itemType.setIdentifierRef(resource.getIdentifier());
        activity.getDescriptionModel().addChildItem(itemType);
        
        // Add the Activity to Activities Model
        IActivitiesModel activitiesModel = ldModel.getActivitiesModel();
        activitiesModel.getLearningActivitiesModel().addChild(activity);
        
        // Then add a ref to it if we are adding to Activity Structure
        if(parentAS != null) {
            parentAS.addActivity(activity, -1);
        }
        
        // Create Level B Property from identifier of QTI File pointed to by resource
        addLevelBProperty(resource, ldModel);
        
        ldModel.setDirty();
        
        return activity;
    }
    
    /**
     * Add a QTI Test File, Item, and Resource to Learning Object
     * 
     * @param lo
     */
    public static void addQTITestToLearningObject(ILearningObjectModel lo) {
        ILDModel ldModel = lo.getLDModel();
        
        File tgtFile = LDModelUtils.getNextValidFileName(ldModel.getRootFolder(), lo.getTitle() + ".xml"); //$NON-NLS-1$
        String name = FileUtils.getFileNameWithoutExtension(tgtFile);
        
        File file = null;

        try {
            file = createQTITestFile(name, ldModel);
        }
        catch(IOException ex) {
            ex.printStackTrace();
            return;
        }
        
        IResourceModel resource = addQTIFileAsResource(file, name, ldModel);
        IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, ldModel);
        itemType.setTitle("QTI Test"); //$NON-NLS-1$
        itemType.setIdentifierRef(resource.getIdentifier());
        lo.getItems().addChildItem(itemType);

        // Create Level B Property from identifier of QTI File pointed to by resource
        addLevelBProperty(file, ldModel);
    }
    
    /**
     * Add a Level B Property to LD Model for QTI Test file referenced by Resource
     * @param resource
     * @param ldModel
     */
    public static void addLevelBProperty(IResourceModel resource, ILDModel ldModel) {
        String href = resource.getHref();
        if(!StringUtils.isSet(href)) {
            return;
        }
        File file = new File(ldModel.getRootFolder(), href);
        addLevelBProperty(file, ldModel); 
    }

    /**
     * Add a Level B Property to LD Model for QTI Test file
     * @param file
     * @param ldModel
     */
    public static void addLevelBProperty(File file, ILDModel ldModel) {
        String identifier = null;
        try {
            identifier = getQTIFileIdentifier(file);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        if(identifier != null) {
            // Check if we already have one
            boolean exists = ldModel.getPropertiesModel().hasChild(identifier + ".SCORE"); //$NON-NLS-1$
            if(!exists) {
                ILocalPersonalPropertyModel property = (ILocalPersonalPropertyModel)LDModelFactory.createModelObject(LDModelFactory.LOCAL_PERSONAL_PROPERTY, ldModel);
                property.setIdentifier(identifier + ".SCORE"); //$NON-NLS-1$
                property.getDataType().setType("real"); //$NON-NLS-1$
                property.setInitialValue("0"); //$NON-NLS-1$
                property.setTitle("QTI Test"); //$NON-NLS-1$
                ldModel.getPropertiesModel().addChild(property);
                ldModel.setDirty();
            }
        }
        else {
            System.err.println("Could not get identifier in QTI File"); //$NON-NLS-1$
        }
    }

    /**
     * @param file
     * @return The identifier of the QTI File XML
     * @throws JDOMException 
     * @throws IOException 
     */
    public static String getQTIFileIdentifier(File file) throws IOException, JDOMException {
        if(file == null || !file.exists()) {
            throw new IOException("File is null or does not exist"); //$NON-NLS-1$
        }
        
        Document doc = JDOMXMLUtils.readXMLFile(file);
        if(doc.hasRootElement()) {
            Element root = doc.getRootElement();
            return root.getAttributeValue("identifier"); //$NON-NLS-1$
        }
        return null;
    }

    /**
     * @param activity
     * @return True if activity contains 1 item pointing to QTI Test Resource
     */
    public static boolean isQTITestActivity(ILearningActivityModel activity) {
        List<IItemType> items = activity.getDescriptionModel().getItemTypes();
        if(!items.isEmpty()) {
            IItemType item = items.get(0);
            IResourceModel resource = activity.getLDModel().getResourcesModel().getResource(item);
            if(resource != null) {
                return IMS_QTI_TYPE.equals(resource.getType());
            }
        }
        
        return false;
    }
    
    /**
     * @param resource
     * @return True if Resource Type is QTI type. 
     *         This is not an expensive way of doing it.
     */
    public static boolean isQTITestResource(IResourceModel resource) {
        return IMS_QTI_TYPE.equals(resource.getType());
    }
    
    /**
     * @param ldModel
     * @return A list of Resources that are of type imsqti_test_xmlv2p1
     */
    public static List<IResourceModel> getQTIResources(ILDModel ldModel) {
        List<IResourceModel> list = new ArrayList<IResourceModel>();
        
        for(IResourceModel resource : ldModel.getResourcesModel().getResources()) {
            if(IMS_QTI_TYPE.equals(resource.getType())) {
                list.add(resource);
            }
        }
        
        return list;
    }
    
    /**
     * @return A display String to substitute for a QTI Test displayed in a Browser
     */
    public static String getFriendlyDisplayForQTITest() {
        return FRIENDLY_QTI_TEXT;
    }
}
