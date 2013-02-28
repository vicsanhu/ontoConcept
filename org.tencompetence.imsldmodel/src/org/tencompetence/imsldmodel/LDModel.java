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
package org.tencompetence.imsldmodel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.jdom.Attribute;
import org.jdom.Element;
import org.tencompetence.imsldmodel.activities.IActivitiesModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentsModel;
import org.tencompetence.imsldmodel.internal.StringUtils;
import org.tencompetence.imsldmodel.method.IMethodModel;
import org.tencompetence.imsldmodel.properties.IPropertiesModel;
import org.tencompetence.imsldmodel.resources.IResourcesModel;
import org.tencompetence.imsldmodel.roles.IRolesModel;
import org.tencompetence.imsldmodel.types.IItemModelType;


/**
 * Core LD Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDModel.java,v 1.7 2009/11/26 09:16:04 phillipus Exp $
 */
public class LDModel implements ILDModel {
    
    private String fManifestIdentifier;
    
    private String fID;
    private String fTitle = ""; //$NON-NLS-1$
    private String fURI;
    private String fVersion = "1.0.0"; //$NON-NLS-1$
    private String fLevel = "A"; //$NON-NLS-1$
    private boolean fSequenceUsed;

    private IItemModelType fLearningObjectives;
    
    private IItemModelType fPrerequisites;
    
    private IMethodModel fMethod;
    
    private IActivitiesModel fActivities;
    
    private IEnvironmentsModel fEnvironments;
    
    private IRolesModel fRoles;
    
    private IPropertiesModel fProperties;
    
    private IResourcesModel fResources;
    
    private File fManifestFile;
    
    private boolean fDoNotify = true;
    
    /*
     * Component ID Map
     */
    private HashMap<String, ILDModelObject> fIDMap = new HashMap<String, ILDModelObject>();

    /**
     * Property Change Listeners
     */
    private PropertyChangeSupport fChangelisteners;
    
    public LDModel() {
        this(null);
    }
    
    public LDModel(File manifestFile) {
        fManifestFile = manifestFile;
        
        fChangelisteners = new PropertyChangeSupport(this);
        
        fLearningObjectives = (IItemModelType)LDModelFactory.createModelObject(LDModelFactory.LEARNING_OBJECTIVES, this);
        fPrerequisites = (IItemModelType)LDModelFactory.createModelObject(LDModelFactory.PREREQUISITES, this);
        fMethod = (IMethodModel)LDModelFactory.createModelObject(LDModelFactory.METHOD, this);
        fActivities = (IActivitiesModel)LDModelFactory.createModelObject(LDModelFactory.ACTIVITIES, this);
        fEnvironments = (IEnvironmentsModel)LDModelFactory.createModelObject(LDModelFactory.ENVIRONMENTS, this);
        fRoles = (IRolesModel)LDModelFactory.createModelObject(LDModelFactory.ROLES, this);
        fProperties = (IPropertiesModel)LDModelFactory.createModelObject(LDModelFactory.PROPERTIES, this);
        fResources = (IResourcesModel)LDModelFactory.createModelObject(LDModelFactory.RESOURCES, this);
    }
    
    public String getIdentifier() {
        if(fID == null) {
            fID = "ld-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }
    
    public void setIdentifier(String id) {
        fID = id;
    }
    
    public String getURI() {
        if(fURI == null) {
            fURI = "http://www.yourURI.here/" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fURI;
    }

    public void setURI(String uri) {
        fURI = uri;
    }

    public String getVersion() {
        return fVersion;
    }

    public void setVersion(String version) {
        fVersion = version;
    }
    
    public String getLevel() {
        if(fLevel != null) {
            return fLevel.toUpperCase();
        }
        return "A"; //$NON-NLS-1$
    }

    public void setLevel(String level) {
        String old = getLevel();
        fLevel = level;
        firePropertyChange(this, PROPERTY_LEVEL, old, level);
    }

    public boolean isSequencedUsed() {
        return fSequenceUsed;
    }

    public void setIsSequenceUsed(boolean set) {
        fSequenceUsed = set;
    }

    public String getTitle() {
        return fTitle;
    }

    public void setTitle(String title) {
        fTitle = title;
    }

    public IItemModelType getLearningObjectives() {
        return fLearningObjectives;
    }

    public IItemModelType getPrerequisites() {
        return fPrerequisites;
    }

    public IRolesModel getRolesModel() {
        return fRoles;
    }
    
    public IPropertiesModel getPropertiesModel() {
        return fProperties;
    }

    public IActivitiesModel getActivitiesModel() {
        return fActivities;
    }
    
    public IEnvironmentsModel getEnvironmentsModel() {
        return fEnvironments;
    }

    public IMethodModel getMethodModel() {
        return fMethod;
    }
    
    public IResourcesModel getResourcesModel() {
        return fResources;
    }
    
    public File getManifestFile() {
        return fManifestFile;
    }
    
    public void setManifestFile(File file) {
        fManifestFile = file;
    }
    
    public File getRootFolder() {
        return fManifestFile == null ? null : fManifestFile.getParentFile();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if(fChangelisteners == null || listener == null) {
            return;
        }
        
        // Stupid, but the only way to check if it is already registered
        for(PropertyChangeListener listener1 : fChangelisteners.getPropertyChangeListeners()) {
            if(listener1 == listener) {
                return;
            }
        }
        
        fChangelisteners.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if(fChangelisteners != null && listener != null) {
            fChangelisteners.removePropertyChangeListener(listener);
        }
    }

    public void firePropertyChange(Object source, String propName, Object oldValue, Object newValue) {
        if(fDoNotify && fChangelisteners != null) {
            PropertyChangeEvent evt = new PropertyChangeEvent(source, propName, oldValue, newValue);
            fChangelisteners.firePropertyChange(evt);
        }
    }

    public ILDModelObject getModelObject(String id) {
        if(!StringUtils.isSet(id)) {
            return null;
        }
        return fIDMap.get(id);
    }
    
    public void putModelObject(String id, ILDModelObject object) {
        if(StringUtils.isSet(id) && object != null) {
            fIDMap.put(id, object);
        }
    }

    public void removeModelObject(String id) {
        if(id != null) {
            fIDMap.remove(id);
        }
    }

    public String getManifestIdentifier() {
        if(fManifestIdentifier == null) {
            fManifestIdentifier = "manifest-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fManifestIdentifier;
    }

    public void setManifestIdentifier(String id) {
        fManifestIdentifier = id;
    }

    public void setNotifications(boolean set) {
        fDoNotify = set;
    }
    
    public boolean isNotifications() {
        return fDoNotify;
    }

    public void setDirty() {
        firePropertyChange(this, PROPERTY_DIRTY, false, true);
    }
    
    public void dispose() {
        // Tell listeners first
        firePropertyChange(this, PROPERTY_DISPOSED, false, true);
        
        // Remove Property Change Listeners
        for(PropertyChangeListener listener : fChangelisteners.getPropertyChangeListeners()) {
            fChangelisteners.removePropertyChangeListener(listener);
        }
        fChangelisteners = null;
        
        fIDMap.clear();
        fIDMap = null;
    }

    public String getObjectName(String objectTag) {
        // Insert code to return user-friendly name for object given its XML tag
        return objectTag;
    }

    public void setObjectName(String objectTag, String name) {
        // Insert (optional) code to set user-friendly name for object
    }

    
    // ================================== XML JDOM PERSISTENCE =================================

    public void fromJDOM(Element element) {
        // Stop notifications
        boolean oldNotify = fDoNotify;
        fDoNotify = false;
        
        for(Object attribute : element.getAttributes()) {
            String tag = ((Attribute)attribute).getName();
            String value = ((Attribute)attribute).getValue();
            
            if(LDModelFactory.IDENTIFIER.equals(tag)) {
                fID = value;
            }
            else if(LDModelFactory.VERSION.equals(tag)) {
                fVersion = value;
            }
            else if(LDModelFactory.URI.equals(tag)) {
                fURI = value;
            }
            else if(LDModelFactory.LEVEL.equals(tag)) {
                fLevel = value;
            }
            else if(LDModelFactory.SEQUENCE_USED.equals(tag)) {
                fSequenceUsed = "true".equalsIgnoreCase(value); //$NON-NLS-1$
            }
        }

        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            String value = child.getText();
            
            // Title
            if(tag.equals(LDModelFactory.TITLE)) {
                fTitle = value;
            }
            
            // Learning Objectives
            else if(tag.equals(LDModelFactory.LEARNING_OBJECTIVES)) {
                fLearningObjectives.fromJDOM((Element)child);
            }
            
            // Prerequisites
            else if(tag.equals(LDModelFactory.PREREQUISITES)) {
                fPrerequisites.fromJDOM((Element)child);
            }
            
            // Components
            else if(tag.equals(LDModelFactory.COMPONENTS)) {
                // Roles
                Element roles = child.getChild(LDModelFactory.ROLES, IMSLD_NAMESPACE_100_EMBEDDED);
                if(roles != null) {
                    fRoles.fromJDOM(roles);
                }
                
                // Properties
                Element properties = child.getChild(LDModelFactory.PROPERTIES, IMSLD_NAMESPACE_100_EMBEDDED);
                if(properties != null) {
                    fProperties.fromJDOM(properties);
                }
                
                // Activities
                Element activities = child.getChild(LDModelFactory.ACTIVITIES, IMSLD_NAMESPACE_100_EMBEDDED);
                if(activities != null) {
                    fActivities.fromJDOM(activities);
                }
                
                // Environments
                Element envs = child.getChild(LDModelFactory.ENVIRONMENTS, IMSLD_NAMESPACE_100_EMBEDDED);
                if(envs != null) {
                    fEnvironments.fromJDOM(envs);
                }
            }
            
            // Method
            else if(tag.equals(LDModelFactory.METHOD)) {
                fMethod.fromJDOM(child);
            }
        }
        
        // Restore notifications flag
        fDoNotify = oldNotify;
     }

    public Element toJDOM() {
        Element learningdesign = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        learningdesign.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());
        learningdesign.setAttribute(LDModelFactory.VERSION, fVersion);
        learningdesign.setAttribute(LDModelFactory.LEVEL, fLevel);
        learningdesign.setAttribute(LDModelFactory.URI, getURI());
        learningdesign.setAttribute(LDModelFactory.SEQUENCE_USED, fSequenceUsed ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$
        
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(fTitle);
        learningdesign.addContent(title);
        
        // Learning Objectives
        Element lo = fLearningObjectives.toJDOM();
        if(lo != null) {
            learningdesign.addContent(lo);
        }
        
        // Prerequisites
        Element prereq = fPrerequisites.toJDOM();
        if(prereq != null) {
            learningdesign.addContent(prereq);
        }
        
        // Components
        Element components = new Element(LDModelFactory.COMPONENTS, IMSLD_NAMESPACE_100_EMBEDDED);
        learningdesign.addContent(components);
        
        // Roles
        Element roles = fRoles.toJDOM();
        components.addContent(roles);
        
        // Properties
        Element properties = fProperties.toJDOM();
        if(properties != null) {
            components.addContent(properties);
        }
        
        // Activities
        Element activities = fActivities.toJDOM();
        if(activities != null) {
            components.addContent(activities);
        }
        
        // Environments
        Element envs = fEnvironments.toJDOM();
        if(envs != null) {
            components.addContent(envs);
        }

        // Method
        Element method = fMethod.toJDOM();
        learningdesign.addContent(method);
        
        return learningdesign;
    }

    public String getTagName() {
        return LDModelFactory.LEARNING_DESIGN;
    }
}
