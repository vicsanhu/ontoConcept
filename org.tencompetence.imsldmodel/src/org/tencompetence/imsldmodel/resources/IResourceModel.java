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
package org.tencompetence.imsldmodel.resources;

import java.util.List;

import org.tencompetence.imsldmodel.IIdentifier;
import org.tencompetence.imsldmodel.ILDModelJDOMPersistence;
import org.tencompetence.imsldmodel.ILDModelObject;

/**
 * Resource
 * 
 * @author Phillip Beauvoir
 * @version $Id: IResourceModel.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public interface IResourceModel
extends IIdentifier, ILDModelJDOMPersistence, ILDModelObject {
    
    String PROPERTY_RESOURCE_IDENTIFIER = "Property.Resource.ID"; //$NON-NLS-1$
    String PROPERTY_RESOURCE_HREF = "Property.Resource.Href"; //$NON-NLS-1$
    String PROPERTY_RESOURCE_ADDED = "Property.Resource.added"; //$NON-NLS-1$
    String PROPERTY_RESOURCE_REMOVED = "Property.Resource.removed"; //$NON-NLS-1$
    String PROPERTY_RESOURCE_FILE = "Property.Resource.File"; //$NON-NLS-1$
    String PROPERTY_RESOURCE_TYPE = "Property.Resource.Type"; //$NON-NLS-1$
    String PROPERTY_RESOURCE_BASE = "Property.Resource.Base"; //$NON-NLS-1$
    
    String WEBCONTENT_TYPE = "webcontent"; //$NON-NLS-1$
    String IMSLDCONTENT_TYPE = "imsldcontent"; //$NON-NLS-1$
    
    String[] RESOURCE_TYPES = {
            WEBCONTENT_TYPE,
            IMSLDCONTENT_TYPE
    };

    String getType();
    void setType(String type);

    String getBase();
    void setBase(String url);
    
    String getHref();
    void setHref(String href);
    
    /**
     * Set the HREF and add a default <file> entry.
     * If HREF is already set and a corresponding <file> entry exists it will be removed
     * @param href
     */
    void setHrefAndResourceFile(String href);
    
    List<IResourceFileModel> getFiles();
    
    List<IDependencyModel> getDependencies();
    
    /**
     * @return A full HREF with all "base" prefixes
     */
    String getFullyQualifiedHref();

    /**
     * @param href
     * @return
     */
    IResourceFileModel getResourceFileByHref(String href);
}