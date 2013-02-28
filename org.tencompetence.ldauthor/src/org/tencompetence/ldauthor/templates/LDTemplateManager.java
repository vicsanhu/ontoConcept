/*
 * Copyright (c) 2007, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.templates;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.tencompetence.jdom.JDOMXMLUtils;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;
import org.tencompetence.ldauthor.templates.impl.ld.BasicLDTemplate;
import org.tencompetence.ldauthor.templates.impl.ld.ClonedLDTemplate;
import org.tencompetence.ldauthor.templates.impl.ld.EmptyLDTemplate;
import org.tencompetence.ldauthor.templates.impl.ld.InbuiltLDTemplate;
import org.tencompetence.ldauthor.templates.impl.ld.UserLDTemplate;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.StringUtils;



/**
 * LD Template Manager
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDTemplateManager.java,v 1.9 2010/01/06 15:34:25 phillipus Exp $
 */
public class LDTemplateManager implements ILDTemplateXMLTags {

    private HashMap<String, ITemplateGroup> fMap = new HashMap<String, ITemplateGroup>();
    
    private List<ITemplateGroup> fTemplateGroups;
    
    static final String TEMPLATES_FOLDER = "templates"; //$NON-NLS-1$
    static final String TEMPLATES_LD_FOLDER = "ld"; //$NON-NLS-1$
    static final String TEMPLATES_FILE = "templates.xml"; //$NON-NLS-1$
        
    
    /**
     * @return The User Templates Folder
     */
    public static File getUserTemplatesFolder() {
        IPreferenceStore store = LDAuthorPlugin.getDefault().getPreferenceStore();
        
        String path = store.getString(ILDAuthorPreferenceConstants.PREFS_USER_DATA_FOLDER);
        if(!StringUtils.isSet(path)) {
            path = store.getDefaultString(ILDAuthorPreferenceConstants.PREFS_USER_DATA_FOLDER);
        }
        
        File file = new File(path, TEMPLATES_FOLDER);
        file.mkdirs();
        
        return file;
    }
    
    /**
     * @return The User Templates manifest file
     */
    public static File getUserTemplatesManifest() {
        return new File(getUserTemplatesFolder(), TEMPLATES_FILE);
    }
    
    /**
     * @return The In-built Templates Folder
     */
    public static File getInbuiltTemplatesFolder() {
        return new File(LDAuthorPlugin.getDefault().getTemplatesFolder(), TEMPLATES_LD_FOLDER);
    }

    /**
     * @return The Inbuilt Templates manifest file
     */
    public static File getInbuiltTemplatesManifest() {
        return new File(getInbuiltTemplatesFolder(), TEMPLATES_FILE);
    }


    public LDTemplateManager() {
        fTemplateGroups = new ArrayList<ITemplateGroup>();
        
        // Read Inbuilt Templates
        try {
            readInbuiltTemplatesManifest();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        // Read User Templates
        try {
            readUserTemplatesManifest();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Read in the inbuilt templates descriptor
     * @throws IOException
     * @throws JDOMException
     */
    private void readInbuiltTemplatesManifest() throws IOException, JDOMException {
        File file = getInbuiltTemplatesManifest();
        
        if(!file.exists()) {
            return;
        }
        
        Document doc = JDOMXMLUtils.readXMLFile(file);
        Element rootElement = doc.getRootElement();
        
        // In-built groups
        for(Object child : rootElement.getChildren(XMLTAG_GROUP)) {
            Element groupElement = (Element)child;
            String id = groupElement.getAttributeValue(XMLTAG_ID);
            String name = groupElement.getAttributeValue(XMLTAG_NAME);
            if(id != null && name != null) {
                // New group
                TemplateGroup group = new TemplateGroup();
                group.setID(id);
                group.setName(name);
                fTemplateGroups.add(group);
                fMap.put(id, group);
                
                // In built basic types
                if(XMLTAG_INBUILT_GROUP_1.equals(id)) {
                    group.addTemplate(new EmptyLDTemplate());
                    group.addTemplate(new BasicLDTemplate());
                    group.addTemplate(new ClonedLDTemplate());
                }
                
                // Templates in group
                for(Object element : groupElement.getChildren(XMLTAG_TEMPLATE)) {
                    Element templateElement = (Element)element;
                    InbuiltLDTemplate template = new InbuiltLDTemplate();
                    template.fromJDOM(templateElement);
                    group.addTemplate(template);
                }
            }
        }
    }
    
    /**
     * @return All the LD Template Groups
     */
    public List<ITemplateGroup> getTemplateGroups() {
        return fTemplateGroups;
    }
    
    /**
     * Delete a User Template
     * @param template
     * @throws IOException
     */
    public void deleteTemplate(UserLDTemplate template) throws IOException {
        boolean deleted = false;
        
        ITemplateGroup group = getParentGroup(template);
        if(group != null) {
            deleted = group.removeTemplate(template);
        }
        
        if(deleted) {
            saveUserTemplatesManifest();
            
            // Delete physical files
            File folder = template.getSourceFolder();
            if(folder.exists() && folder.isDirectory()) {
                FileUtils.deleteFolder(folder);
            }
        }
    }
    
    public ITemplateGroup getParentGroup(ITemplate template) {
        for(ITemplateGroup group : fTemplateGroups) {
            if(group.getTemplates().contains(template)) {
                return group;
            }
        }
        return null;
    }
    
    
    public void readUserTemplatesManifest() throws IOException, JDOMException {
        File file = getUserTemplatesManifest();
        
        if(!file.exists()) {
            return;
        }
        
        Document doc = JDOMXMLUtils.readXMLFile(file);
        Element rootElement = doc.getRootElement();
        
        // User groups
        for(Object child : rootElement.getChildren(XMLTAG_USERGROUP)) {
            Element groupElement = (Element)child;
            UserTemplateGroup group = new UserTemplateGroup(groupElement.getAttributeValue(XMLTAG_NAME));
            fTemplateGroups.add(group);
            for(Object element : groupElement.getChildren(XMLTAG_TEMPLATE)) {
                Element templateElement = (Element)element;
                UserLDTemplate template = new UserLDTemplate();
                template.fromJDOM(templateElement);
                group.addTemplate(template);
            }
        }
        
        // In-built groups containing user templates
        for(Object child : rootElement.getChildren(XMLTAG_GROUP)) {
            Element groupElement = (Element)child;
            String id = groupElement.getAttributeValue(XMLTAG_ID);
            if(id != null) {
                ITemplateGroup group = fMap.get(id);
                if(group != null) {
                    for(Object element : groupElement.getChildren(XMLTAG_TEMPLATE)) {
                        Element templateElement = (Element)element;
                        UserLDTemplate template = new UserLDTemplate();
                        template.fromJDOM(templateElement);
                        group.addTemplate(template);
                    }
                }
            }
        }
    }
    
    public void saveUserTemplatesManifest() throws IOException {
        Document doc = new Document();
        
        /*
         * Add Comments
         */
        Comment comment = new Comment("ReCourse Templates Manifest"); //$NON-NLS-1$
        doc.addContent(comment);
        comment = new Comment("Modified - " + new Date().toString()); //$NON-NLS-1$
        doc.addContent(comment);
        
        Element root = new Element(XMLTAG_MANIFEST);
        doc.setRootElement(root);
        
        for(ITemplateGroup group : fTemplateGroups) {
            Element groupElement;
            
            // Add User templates to user groups
            if(group instanceof UserTemplateGroup) {
                groupElement = new Element(XMLTAG_USERGROUP);
                groupElement.setAttribute(XMLTAG_NAME, group.getName());
            }
            // Add User templates to in-built groups
            else {
                groupElement = new Element(XMLTAG_GROUP);
                groupElement.setAttribute(XMLTAG_ID, group.getID());
            }
            
            for(ITemplate template : group.getTemplates()) {
                if(template instanceof UserLDTemplate) {
                    Element child = ((UserLDTemplate)template).toJDOM();
                    groupElement.addContent(child);
                }
            }
            
            root.addContent(groupElement);
        }
        
        JDOMXMLUtils.write2XMLFile(doc, getUserTemplatesManifest());
    }

}
