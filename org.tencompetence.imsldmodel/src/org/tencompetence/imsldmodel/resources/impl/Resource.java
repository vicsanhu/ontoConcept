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
import org.tencompetence.imsldmodel.resources.IDependencyModel;
import org.tencompetence.imsldmodel.resources.IResourceFileModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.resources.IResourcesModel;


/**
 * CP Resource
 * 
 * @author Phillip Beauvoir
 * @version $Id: Resource.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class Resource implements IResourceModel {
    
    private ILDModel fLDModel;
    
    private List<IResourceFileModel> fFiles = new ArrayList<IResourceFileModel>();
    
    private List<IDependencyModel> fDependencies = new ArrayList<IDependencyModel>();

    private String fID, fBase, fHref;
    
    private String fType = WEBCONTENT_TYPE;
    
    /**
     * Constructor
     * 
     * @param ldModel
     */
    public Resource(ILDModel ldModel) {
        fLDModel = ldModel;
    }
    
    public ILDModel getLDModel() {
        return fLDModel;
    }

    public String getBase() {
        return fBase;
    }

    public List<IResourceFileModel> getFiles() {
        return fFiles;
    }

    public String getHref() {
        return fHref;
    }

    public String getType() {
        return fType;
    }

    public void setHref(String href) {
        // CopperCore Kludge...
        if(href != null && href.toLowerCase().startsWith("www.")) { //$NON-NLS-1$
            href = "http://" + href; //$NON-NLS-1$
        }
        
        String old = fHref;
        fHref = href;
        fLDModel.firePropertyChange(this, PROPERTY_RESOURCE_HREF, old, href);
    }
    
    public void setHrefAndResourceFile(String href) {
        // Remove old default File entry corrsponding to Resource's HREF if it exists
        IResourceFileModel fileModel = getResourceFileByHref(fHref);
        if(fileModel != null) {
            getFiles().remove(fileModel);
        }
        
        addDefaultResourceFile(href);
        setHref(href);
    }

    public void setType(String type) {
        String old = fType;
        fType = type;
        fLDModel.firePropertyChange(this, PROPERTY_RESOURCE_TYPE, old, type);
    }

    public void setBase(String url) {
        String old = fBase;
        fBase = url;
        fLDModel.firePropertyChange(this, PROPERTY_RESOURCE_BASE, old, url);
    }

    public String getIdentifier() {
        return fID;
    }

    public void setIdentifier(String id) {
        String old = fID;
        fID = id;
        fLDModel.firePropertyChange(this, PROPERTY_RESOURCE_IDENTIFIER, old, id);
    }

    public List<IDependencyModel> getDependencies() {
        return fDependencies;
    }
    
    public String getFullyQualifiedHref() {
        IResourcesModel resources = fLDModel.getResourcesModel();
        String href = StringUtils.safeString(resources.getBase());
        href += StringUtils.safeString(getBase());
        href += StringUtils.safeString(getHref());
        return href;
    }

    public IResourceFileModel getResourceFileByHref(String href) {
        if(!StringUtils.isSet(href)) {
            return null;
        }
        
        for(IResourceFileModel file : fFiles) {
            if(href.equalsIgnoreCase(file.getHref())) {
                return file;
            }
        }
        
        return null;
    }
    
    /**
     * Add a default IResourceFileModel entry given that this Resource has an existing href
     * 
     * @return The IResourceFileModel, or null if this Resource's href is invalid or null
     */
    private IResourceFileModel addDefaultResourceFile(String href) {
        if(!StringUtils.isSetAfterTrim(href)) {
            return null;
        }
        
        // Don't add non-local files
        if(href.toLowerCase().startsWith("www.") || href.toLowerCase().startsWith("http")) { //$NON-NLS-1$ //$NON-NLS-2$
            return null;
        }
        
        IResourceFileModel resourceFile = null;
        
        // If there isn't one already set...
        if(getResourceFileByHref(href) == null) {
            resourceFile = new ResourceFile(getLDModel());
            resourceFile.setHref(href);
            getFiles().add(resourceFile);
        }
        
        return resourceFile;
    }

    // =================================== JDOM =============================================

    public void fromJDOM(Element element) {
        fID =  element.getAttributeValue(LDModelFactory.IDENTIFIER);
        fType =  element.getAttributeValue(LDModelFactory.TYPE);
        fBase =  element.getAttributeValue(LDModelFactory.BASE);
        fHref =  element.getAttributeValue(LDModelFactory.HREF);
        if(fHref != null) {
            fHref = fHref.replaceAll("%20", " "); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        for(Object o : element.getChildren(LDModelFactory.FILE, element.getNamespace())) {
            Element child = (Element)o;
            IResourceFileModel file = new ResourceFile(fLDModel);
            file.fromJDOM(child);
            fFiles.add(file);
        }
        
        for(Object o : element.getChildren(LDModelFactory.DEPENDENCY, element.getNamespace())) {
            Element child = (Element)o;
            IDependencyModel dependency = new Dependency();
            dependency.fromJDOM(child);
            fDependencies.add(dependency);
        }
    }

    public String getTagName() {
        return LDModelFactory.RESOURCE;
    }

    public Element toJDOM() {
        Element resource = new Element(getTagName(), IMSNamespaces.IMSCP_NAMESPACE_114);
        
        resource.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());
        
        if(StringUtils.isSet(fType)) {
            resource.setAttribute(LDModelFactory.TYPE, fType);
        }
        if(StringUtils.isSet(fBase)) {
            resource.setAttribute(LDModelFactory.BASE, fBase);
        }
        if(StringUtils.isSet(fHref)) {
            // Convert spaces to %20 
            String href = fHref.replaceAll(" ", "%20"); //$NON-NLS-1$ //$NON-NLS-2$
            resource.setAttribute(LDModelFactory.HREF, href);
        }
        
        for(IResourceFileModel file : fFiles) {
            Element child = file.toJDOM();
            if(child != null) {
                resource.addContent(child);
            }
        }
        
        for(IDependencyModel dependency : fDependencies) {
            Element child = dependency.toJDOM();
            if(child != null) {
                resource.addContent(child);
            }
        }
        
        return resource;
    }
}
