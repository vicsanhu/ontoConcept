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
package org.tencompetence.imsldmodel.resources.impl;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.IMSNamespaces;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.internal.StringUtils;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.resources.IResourcesModel;
import org.tencompetence.imsldmodel.types.IItemType;


/**
 * CP Resources
 * 
 * @author Phillip Beauvoir
 * @version $Id: Resources.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class Resources implements IResourcesModel {
    
    private ILDModel fLDModel;

    private List<IResourceModel> fResources = new ArrayList<IResourceModel>();
    
    private String fBase;
    
    public Resources(ILDModel ldModel) {
        fLDModel = ldModel;
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public String getBase() {
        return fBase;
    }

    public List<IResourceModel> getResources() {
        return fResources;
    }

    public void setBase(String url) {
        fBase = url;
    }
    
    public void addResource(IResourceModel resource) {
        if(resource != null && !fResources.contains(resource) && fResources.add(resource)) {
            fLDModel.firePropertyChange(this, IResourceModel.PROPERTY_RESOURCE_ADDED, null, resource);
        }
    }

    public void removeResource(IResourceModel resource) {
        if(resource != null && fResources.remove(resource)) {
            fLDModel.firePropertyChange(this, IResourceModel.PROPERTY_RESOURCE_REMOVED, null, resource);
        }
    }
    
    public IResourceModel getResourceByHref(String href) {
        if(!StringUtils.isSet(href)) {
            return null;
        }
        
        for(IResourceModel resource : fResources) {
            if(href.equalsIgnoreCase(resource.getHref())) {
                return resource;
            }
        }
        
        return null;
    }

    public IResourceModel getResourceByIdentifier(String identifier) {
        if(!StringUtils.isSet(identifier)) {
            return null;
        }
        
        for(IResourceModel resource : fResources) {
            if(identifier.equals(resource.getIdentifier())) {
                return resource;
            }
        }
        
        return null;
    }

    public IResourceModel getResource(IItemType itemType) {
        return getResourceByIdentifier(itemType.getIdentifierRef());
    }
    
    /**
     * Check if Resource ID is valid or in use
     * 
     * @param ID
     * @return
     */
    public boolean isValidResourceID(String id) {
        if(!StringUtils.isValidID(id)) {
            return false;
        }
        
        return getResourceByIdentifier(id) == null;
    }

    
    // =================================== JDOM =============================================

    public void fromJDOM(Element element) {
        fBase =  element.getAttributeValue(LDModelFactory.BASE);
        
        for(Object o : element.getChildren(LDModelFactory.RESOURCE, element.getNamespace())) {
            Element child = (Element)o;
            IResourceModel resource = new Resource(fLDModel);
            resource.fromJDOM(child);
            fResources.add(resource);
        }
    }

    public String getTagName() {
        return LDModelFactory.RESOURCES;
    }

    public Element toJDOM() {
        Element resources = new Element(getTagName(), IMSNamespaces.IMSCP_NAMESPACE_114);
        
        if(StringUtils.isSet(fBase)) {
            resources.setAttribute(LDModelFactory.BASE, fBase);
        }
        
        for(IResourceModel resource : fResources) {
            Element child = resource.toJDOM();
            if(child != null) {
                resources.addContent(child);
            }
        }
        
        return resources;
    }
}
