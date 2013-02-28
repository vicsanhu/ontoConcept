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
package org.tencompetence.imsldmodel;

import java.io.File;

import org.tencompetence.imsldmodel.activities.IActivitiesModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentsModel;
import org.tencompetence.imsldmodel.method.IMethodModel;
import org.tencompetence.imsldmodel.properties.IPropertiesModel;
import org.tencompetence.imsldmodel.resources.IResourcesModel;
import org.tencompetence.imsldmodel.roles.IRolesModel;
import org.tencompetence.imsldmodel.types.IItemModelType;

/**
 * Description
 * 
 * @author Phillip Beauvoir
 * @version $Id: ILDModel.java,v 1.8 2009/11/26 09:16:04 phillipus Exp $
 */
public interface ILDModel
extends IIdentifier, IPropertyChangeSupport, ILDModelJDOMPersistence, IMSNamespaces {
    
    String PROPERTY_DISPOSED = "property.disposed"; //$NON-NLS-1$
    String PROPERTY_DIRTY = "property.dirty"; //$NON-NLS-1$
    String PROPERTY_LEVEL = "property.level"; //$NON-NLS-1$

    /**
     * @return The URI of the manifest
     */
    String getURI();
    
    /**
     * Set the URI of the manifest
     * @param uri
     */
    void setURI(String uri);
    
    /**
     * @return The version of the manifest
     */
    String getVersion();
    
    /**
     * Set the version of the manifest
     * @param version
     */
    void setVersion(String version);
    
    /**
     * @return The level of the manifest, "A", "B" or "C"
     */
    String getLevel();
    
    /**
     * Set the level of the manifest, "A", "B" or "C"
     * @param level
     */
    void setLevel(String level);
    
    /**
     * @return Whether manifest is sequenced
     */ 
    boolean isSequencedUsed();
    
    /**
     * @param Set whether manifest is sequenced
     */
    void setIsSequenceUsed(boolean set);

    /**
     * @return The Title of the Manifest
     */
    String getTitle();
    
    /**
     * Set the title of the manifest
     * @param title
     */
    void setTitle(String title);

    /**
     * @return The Prerequisites
     */
    IItemModelType getPrerequisites();
    
    /**
     * @return The Learning Objectives
     */
    IItemModelType getLearningObjectives();

    /**
     * @return The Roles Model
     */
    IRolesModel getRolesModel();

    /**
     * @return The Properties Model
     */
    IPropertiesModel getPropertiesModel();
    
    /**
     * @return The Activities Model
     */
    IActivitiesModel getActivitiesModel();
    
    /**
     * @return The Environments Model
     */
    IEnvironmentsModel getEnvironmentsModel();

    /**
     * @return The Method Model
     */
    IMethodModel getMethodModel();
    
    /**
     * @return The Resources Model
     */
    IResourcesModel getResourcesModel();
    
    /**
     * @param id
     * @return An LD Model object from the lookup table given its identifier, or null
     */
    ILDModelObject getModelObject(String id);
    
    /**
     * Put an LD Model object in the lookup table given its identifier
     * @param id
     * @param object
     */
    void putModelObject(String id, ILDModelObject object);
    
    /**
     * Remove an LD Model object from the lookup table given its identifier
     * @param id
     */
    void removeModelObject(String id);
    
    /**
     * @return The ID of the manifest
     */
    String getManifestIdentifier();
    
    /**
     * Set the ID of the manifest
     * @param id
     */
    void setManifestIdentifier(String id);

    /**
     * @return The Manifest File or null if there isn't one
     */
    File getManifestFile();
    
    /**
     * Set the manifest file
     * @param file
     */
    void setManifestFile(File file);
    
    /**
     * @return The Folder of the Manifest or null if there is no manifest
     */
    File getRootFolder();
    
    /**
     * Turn Property change notifications off or on
     * @param set
     */
    void setNotifications(boolean set);
    
    /**
     * @return True if model Property change notifications are on
     */
    boolean isNotifications();
    
    /**
     * Set the LD Model as dirty (true)
     * This will fire a notification to listeners (usually just the Editor to signify that the model needs saving)
     */
    void setDirty();
    
    /**
     * Dispose of the model
     */
    void dispose();
    
    /**
     * Set an LD object name to a human-readable one
     * @param objectTag The XML tag of the object
     * @param name The human-readable name
     */
    void setObjectName(String objectTag, String name);
    
    /**
     * @param objectTag
     * @return A human-readable name of an LD object
     */
    String getObjectName(String objectTag);
}