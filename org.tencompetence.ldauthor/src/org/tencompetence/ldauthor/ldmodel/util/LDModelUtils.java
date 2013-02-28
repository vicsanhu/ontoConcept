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
package org.tencompetence.ldauthor.ldmodel.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import org.eclipse.jface.preference.IPreferenceStore;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.environments.IConferenceModel;
import org.tencompetence.imsldmodel.environments.IIndexSearchModel;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.environments.IMonitorModel;
import org.tencompetence.imsldmodel.environments.ISendMailModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.Messages;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;
import org.tencompetence.ldauthor.qti.model.QTIUtils;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.StringUtils;



/**
 * LD Model Utils
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDModelUtils.java,v 1.13 2009/07/01 11:36:34 phillipus Exp $
 */
public class LDModelUtils {
    
    /*
     * Types of Resource Files
     */
    public static final int HTML_FILE = 0;
    public static final int XHTML_FILE = 1;
    
    /*
     * Default object names
     */
    public static final String DEFAULT_OVERVIEW_NAME = Messages.LDModelUtils_0;
    public static final String DEFAULT_COMPLETION_NAME = Messages.LDModelUtils_1;
    public static final String DEFAULT_PACKAGING_NAME = Messages.LDModelUtils_2;
    
    private static Hashtable<String, String> keyNames = new Hashtable<String, String>();
    
    static {
        keyNames.put(LDModelFactory.ROLES, Messages.LDModelUtils_6);
        keyNames.put(LDModelFactory.ROLE_PART, Messages.LDModelUtils_7);
        keyNames.put(LDModelFactory.ACTIVITIES, Messages.LDModelUtils_8);
        keyNames.put(LDModelFactory.LEARNING_ACTIVITY, Messages.LDModelUtils_9);
        keyNames.put(LDModelFactory.SUPPORT_ACTIVITY, Messages.LDModelUtils_10);
        keyNames.put(LDModelFactory.ACTIVITY_STRUCTURE, Messages.LDModelUtils_11);
        keyNames.put(LDModelFactory.RESOURCES, Messages.LDModelUtils_12);
        keyNames.put(LDModelFactory.RESOURCE, Messages.LDModelUtils_13);
        keyNames.put(LDModelFactory.CONFERENCE, Messages.LDModelUtils_14);
        keyNames.put(LDModelFactory.SEND_MAIL, Messages.LDModelUtils_15);
        keyNames.put(LDModelFactory.INDEX_SEARCH, Messages.LDModelUtils_16);
        keyNames.put(LDModelFactory.MONITOR, Messages.LDModelUtils_17);
        keyNames.put(LDModelFactory.LEARNING_OBJECT, Messages.LDModelUtils_18);
        keyNames.put(LDModelFactory.KNOWLEDGE_OBJECT, Messages.LDModelUtils_19);
        keyNames.put(LDModelFactory.TOOL_OBJECT, Messages.LDModelUtils_20);
        keyNames.put(LDModelFactory.TEST_OBJECT, Messages.LDModelUtils_21);
        keyNames.put(LDModelFactory.GLOBAL_PERSONAL_PROPERTY, Messages.LDModelUtils_22);
        keyNames.put(LDModelFactory.GLOBAL_PROPERTY, Messages.LDModelUtils_23);
        keyNames.put(LDModelFactory.LOCAL_PERSONAL_PROPERTY, Messages.LDModelUtils_24);
        keyNames.put(LDModelFactory.LOCAL_PROPERTY, Messages.LDModelUtils_25);
        keyNames.put(LDModelFactory.LOCAL_ROLE_PROPERTY, Messages.LDModelUtils_26);
        keyNames.put(LDModelFactory.PROPERTY_GROUP, Messages.LDModelUtils_27);
    }

    /**
     * Return the user's preferred name for an LD object.
     * These are set in Preferences or maybe set here
     * 
     * @param key One of the LD object names
     * @return A user name for an LD object stored in Preferences or the default one
     */
    public static String getUserObjectName(String key) {
        IPreferenceStore store = LDAuthorPlugin.getDefault().getPreferenceStore();
        String keyName = ILDAuthorPreferenceConstants.PREFS_LDOBJECT_USERNAME + key;
        String result = store.getString(keyName);
        if("".equals(result)) { //$NON-NLS-1$
            result = keyNames.get(key); // Might have it here
        }
        
        return result == null ? key : result;
    }
    
    // ===========================================================================
    // NEW LD OBJECT DEFAULTS AND RESOURCE FILES
    // ===========================================================================
    
    /**
     * Object Defaults for a Learning Object
     * @param lo
     */
    public static void setNewObjectDefaults(ILearningObjectModel lo) {
        ILDModel ldModel = lo.getLDModel();
        
        // If a Test Type, Add QTI File
        if(lo.getType() == ILearningObjectModel.TYPE_TEST_OBJECT) {
            QTIUtils.addQTITestToLearningObject(lo);
        }
        else {
            // Create Resource File
            File tgtFile = writeDescriptionFile(ldModel, lo.getTitle(), Messages.LDModelUtils_28, Messages.LDModelUtils_3, HTML_FILE);
            
            // Add one Resource Item for Description
            IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, ldModel);
            itemType.setTitle(Messages.LDModelUtils_5);
            lo.getItems().addChildItem(itemType);
            createNewResourceWithHref(itemType, tgtFile.getName(), lo.getTitle());
        }
    }
    
    /**
     * Object Defaults for a SendMail type
     * @param sendMail
     * @param fileType
     */
    public static void setNewObjectDefaults(ISendMailModel sendMail) {
        ILDModel ldModel = sendMail.getLDModel();
        
        // Add Default Role for Mail recipient
        IRoleModel learner = ldModel.getRolesModel().getDefaultLearnerRole();
        if(learner != null) {
            sendMail.addEmailDataType(learner);
        }
    }
    
    /**
     * Object Defaults for an IndexSearch type
     * @param indexSearch
     */
    public static void setNewObjectDefaults(IIndexSearchModel indexSearch) {
        // Add one class
        indexSearch.getIndexClasses().add("class"); //$NON-NLS-1$
    }
    
    /**
     * Object Defaults for a Conference
     * @param conference
     * @param fileType HTML or XHTML
     */
    public static void setNewObjectDefaults(IConferenceModel conference) {
        ILDModel ldModel = conference.getLDModel();
        
        // One Participant
        IRoleModel learner = ldModel.getRolesModel().getDefaultLearnerRole();
        if(learner != null) {
            conference.addParticipant(learner);
        }
        
        /*
         * Widget particular rule:
         * If the conference contains parameter "widget=vote", then it must have either a "moderator" or "conference-manager"
         */
        String params = conference.getParameters();
        if(params != null && params.toLowerCase().indexOf("widget=vote") != -1) { //$NON-NLS-1$
            IRoleModel staff = ldModel.getRolesModel().getDefaultStaffRole();
            if(staff != null) {
                conference.setManager(staff);
            }
        }
        
        // Create Resource File
        File tgtFile = writeDescriptionFile(ldModel, conference.getTitle(), Messages.LDModelUtils_28, Messages.LDModelUtils_3, HTML_FILE);
        
        // Add one Resource Item for Description
        IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, ldModel);
        itemType.setTitle(Messages.LDModelUtils_5);
        conference.getItem().addChildItem(itemType);
        createNewResourceWithHref(itemType, tgtFile.getName(), conference.getTitle());
    }
    
    /**
     * Object Defaults for a Monitor
     * @param conference
     * @param fileType HTML or XHTML
     */
    public static void setNewObjectDefaults(IMonitorModel monitor) {
        ILDModel ldModel = monitor.getLDModel();
        
        // Create Resource File
        File tgtFile = writeDescriptionFile(ldModel, monitor.getTitle(), Messages.LDModelUtils_28, Messages.LDModelUtils_3, XHTML_FILE);
        
        // Add one Resource Item
        IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, ldModel);
        itemType.setTitle(Messages.LDModelUtils_5);
        monitor.getItems().addChildItem(itemType);
        createNewResourceWithHref(itemType, tgtFile.getName(), monitor.getTitle());
    }
    
    /**
     * Object Defaults for a Learning / Support Activity
     * @param activity
     * @param description
     * @param fileType HTML or XHTML
     */
    public static void setNewObjectDefaults(IActivityModel activity, String description, int fileType) {
        description = normaliseDescriptionText(description);
        
        ILDModel ldModel = activity.getLDModel();
        
        // Create Resource File
        File tgtFile = writeDescriptionFile(ldModel, activity.getTitle(), Messages.LDModelUtils_4,
                StringUtils.isSet(description) ? description : Messages.LDModelUtils_3, fileType);
        
        // Add one Resource Item for Description
        IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, ldModel);
        itemType.setTitle(Messages.LDModelUtils_28);
        activity.getDescriptionModel().addChildItem(itemType);
        createNewResourceWithHref(itemType, tgtFile.getName(), activity.getTitle());
    }
    
    /**
     * Object Defaults for an Activity Structure
     * @param as
     * @param description
     * @param fileType HTML or XHTML
     */
    public static void setNewObjectDefaults(IActivityStructureModel as, String description, int fileType) {
        description = normaliseDescriptionText(description);
        
        ILDModel ldModel = as.getLDModel();
        
        // Create Information File if description is set (optional)
        if(StringUtils.isSet(description)) {
            File tgtFile = writeDescriptionFile(ldModel, as.getTitle(), Messages.LDModelUtils_4,
                    StringUtils.isSet(description) ? description : Messages.LDModelUtils_3, fileType);
            IItemType itemType = (IItemType)LDModelFactory.createModelObject(LDModelFactory.ITEM, ldModel);
            itemType.setTitle(Messages.LDModelUtils_28);
            as.getInformationModel().addChildItem(itemType);
            createNewResourceWithHref(itemType, tgtFile.getName(), as.getTitle());
        }
    }

    /**
     * Object Defaults for an Item
     * @param itemType
     * @param description
     * @param fileType HTML or XHTML
     */
    public static void setNewObjectDefaults(IItemType itemType, String description, int fileType) {
        description = normaliseDescriptionText(description);
        
        ILDModel ldModel = itemType.getLDModel();
        
        // Create Resource
        File tgtFile = writeDescriptionFile(ldModel, itemType.getTitle(), Messages.LDModelUtils_4,
                StringUtils.isSet(description) ? description : Messages.LDModelUtils_3, fileType);
        IResourceModel resource = createNewResourceWithHref(itemType, tgtFile.getName(), itemType.getTitle());
        // XHTML = imsldcontent type
        if(fileType == XHTML_FILE) {
            resource.setType(IResourceModel.IMSLDCONTENT_TYPE);
        }
    }

    /**
     * Object Defaults for a Resource
     * @param resource
     * @param description
     * @param fileType HTML or XHTML
     */
    public static void setNewObjectDefaults(IResourceModel resource, String description, int fileType) {
        description = normaliseDescriptionText(description);

        ILDModel ldModel = resource.getLDModel();
        
        File tgtFile = null;
        
        // Create Resource File - we do this slightly differently
        if(fileType == HTML_FILE) {
            tgtFile = new File(ldModel.getRootFolder(), resource.getIdentifier() + ".html"); //$NON-NLS-1$
            if(!tgtFile.exists()) { // Don't over-write if it exists!
                writeHTMLDescriptionFile(tgtFile, Messages.LDModelUtils_4, StringUtils.isSet(description) ? description : Messages.LDModelUtils_3);
            }
        }
        else {
            tgtFile = new File(ldModel.getRootFolder(), resource.getIdentifier() + ".xhtml"); //$NON-NLS-1$
            if(!tgtFile.exists()) { // Don't over-write if it exists!
                writeXHTMLDescriptionFile(tgtFile, Messages.LDModelUtils_4, StringUtils.isSet(description) ? description : Messages.LDModelUtils_3);
            }
            // XHTML = imsldcontent type
            resource.setType(IResourceModel.IMSLDCONTENT_TYPE);
        }
        
        resource.setHrefAndResourceFile(tgtFile.getName());
    }
    
    /**
     * Make the description text suitable for HTML
     * @param text
     * @return
     */
    private static String normaliseDescriptionText(String text) {
        if(text != null) {
            text.trim();
            // Note ensure the tags are well-formed for XHTML!
            text = text.replaceAll(System.getProperty("line.separator"), "<br/>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return text;
    }
    
    /**
     * Write a Description File
     * @param ldModel
     * @param suggestedFileName 
     * @param title The Title in the <title> part
     * @param text The Text to use as the description
     * @param fileType HTML_FILE or XHTML_FILE
     * @return
     */
    private static File writeDescriptionFile(ILDModel ldModel, String suggestedFileName,
            String title, String text, int fileType) {
        
        File file = null;
        
        if(fileType == HTML_FILE) {
            file = getNextValidFileName(ldModel.getRootFolder(), suggestedFileName + ".html"); //$NON-NLS-1$
            writeHTMLDescriptionFile(file, title, text);
        }
        else if(fileType == XHTML_FILE) {
            file = getNextValidFileName(ldModel.getRootFolder(), suggestedFileName + ".xhtml"); //$NON-NLS-1$
            writeXHTMLDescriptionFile(file, title, text);
        }
        else {
            throw new IllegalArgumentException("Should be HTML_FILE or XHTML_FILE type"); //$NON-NLS-1$
        }
        
        return file;
    }
    
    /**
     * Write a HTML Description file
     * @param file
     * @param title
     * @param text
     */
    private static void writeHTMLDescriptionFile(File file, String title, String text) {
        String s = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n"; //$NON-NLS-1$
        s += "<html>\n"; //$NON-NLS-1$
        s += " <head>\n"; //$NON-NLS-1$
        s += "  <title>" + title + "</title>\n"; //$NON-NLS-1$ //$NON-NLS-2$
        s += " </head>\n"; //$NON-NLS-1$
        s += " <body style=\"font-family:Verdana; font-size:10pt;\">\n"; //$NON-NLS-1$
        s += "  <p>" + text + "</p>\n"; //$NON-NLS-1$ //$NON-NLS-2$
        s += " </body>\n"; //$NON-NLS-1$
        s += "</html>"; //$NON-NLS-1$
        
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(s);
            fw.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Write a XHTML Description file
     * @param file
     * @param title
     * @param text
     */
    private static void writeXHTMLDescriptionFile(File file, String title, String text) {
        String s = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"; //$NON-NLS-1$
        s += "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"; //$NON-NLS-1$
        s += " <head>\n"; //$NON-NLS-1$
        s += "  <meta content=\"text/html; charset=ISO-8859-1\" http-equiv=\"content-type\" />\n"; //$NON-NLS-1$
        s += "  <title>" + title + "</title>\n"; //$NON-NLS-1$ //$NON-NLS-2$
        s += " </head>\n"; //$NON-NLS-1$
        s += " <body style=\"font-family:Verdana; font-size:10pt;\">\n"; //$NON-NLS-1$
        s += "  <p>" + text + "</p>\n"; //$NON-NLS-1$ //$NON-NLS-2$
        s += " </body>\n"; //$NON-NLS-1$
        s += "</html>"; //$NON-NLS-1$
        
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(s);
            fw.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Create and add a new Resource linked to an IItemType with a HREF
     * The IItemType's identifierref will reference this Resource
     * A <file> entry will also be made
     * It will be added to the <resources> element
     * 
     * @param itemType
     * @param href
     * @param idHint The Resource identifier hint
     * @return
     */
    public static IResourceModel createNewResourceWithHref(IItemType itemType, String href, String idHint) {
        IResourceModel resource = createNewResourceWithHref(itemType.getLDModel(), href, idHint);
        itemType.setIdentifierRef(resource.getIdentifier());
        return resource;
    }
    
    /**
     * Create and add a new Resource with a HREF
     * A <file> entry will also be made
     * It will be added to the <resources> element
     * @param ldModel
     * @param href
     * @param idHint The Resource identifier hint
     * @return
     */
    public static IResourceModel createNewResourceWithHref(ILDModel ldModel, String href, String idHint) {
        IResourceModel resource = (IResourceModel)LDModelFactory.createModelObject(LDModelFactory.RESOURCE, ldModel);
        // Create a nice ID
        String id = getNextResourceIdentifier(ldModel, idHint);
        resource.setIdentifier(id);
        
        // Set href and file entry
        resource.setHrefAndResourceFile(href);
        
        // Try and set the type
        if(href.toLowerCase().endsWith(".xhtml") || href.toLowerCase().endsWith(".xml")) { //$NON-NLS-1$ //$NON-NLS-2$
            resource.setType(IResourceModel.IMSLDCONTENT_TYPE);
        }
        
        ldModel.getResourcesModel().addResource(resource);
        
        return resource;
    }
    
    /**
     * @param ldModel
     * @param idHint The identifier hint it should be based on. If it's invalid "resource" will be used
     * @return The next available valid Resource identifier, suffixing "-n" if needed
     */
    public static synchronized String getNextResourceIdentifier(ILDModel ldModel, String idHint) {
        int index = 1;
        
        idHint = StringUtils.getValidID(idHint);
        if(!StringUtils.isValidID(idHint)) { // We tried our best!
            idHint = "Resource";   //$NON-NLS-1$
        }
        
        String id = idHint;
        
        while(ldModel.getResourcesModel().getResourceByIdentifier(id) != null) {
            id = idHint + "-" + index++; //$NON-NLS-1$
        }
        
        return id;
    }
    
    /**
     * @param parentFolder
     * @param fileNameHint The disired fileName
     * @return The next available File Name
     */
    public static synchronized File getNextValidFileName(File parentFolder, String suggestedFileName) {
        int index = 1;
        
        suggestedFileName = FileUtils.getValidFileName(suggestedFileName);

        String name = suggestedFileName.substring(0, suggestedFileName.lastIndexOf('.'));
        String ext = suggestedFileName.substring(suggestedFileName.lastIndexOf('.'));

        File file = null;
        
        do {
            file = new File(parentFolder, suggestedFileName);
            suggestedFileName = name + "-" + index++ + ext; //$NON-NLS-1$
        }
        while(file.exists());
        
        return file;
    }
    
    /**
     * Return a useful name for a Role Part constructed from the Role Name and Component
     * @param rolePart
     * @return
     */
    public static String createRolePartTitle(IRolePartModel rolePart) {
        String title;
        
        IRoleModel role = rolePart.getRole();
        ILDModelObject component = rolePart.getComponent();
        
        if(role != null && component != null) {
            title = role.getTitle() + " " + Messages.LDModelUtils_29 + " '" + ((ITitle)component).getTitle() + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        else {
            title = rolePart.getTitle();
        }
        
        return title;
    }
}
