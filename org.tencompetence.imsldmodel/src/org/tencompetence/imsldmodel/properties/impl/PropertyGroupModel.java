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
package org.tencompetence.imsldmodel.properties.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.UUID;

import org.jdom.Element;
import org.tencompetence.imsldmodel.IIdentifier;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.impl.LDModelObjectReferenceArrayList;
import org.tencompetence.imsldmodel.properties.IPropertyGroupModel;
import org.tencompetence.imsldmodel.properties.IPropertyRefModel;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;


/**
 * Property Group
 * 
 * @author Phillip Beauvoir
 * @version $Id: PropertyGroupModel.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class PropertyGroupModel implements IPropertyGroupModel, PropertyChangeListener {
    
    private ILDModel fLDModel;
    
    private String fID;
    private String fTitle;
    
    private List<ILDModelObjectReference> fPropertyRefs = new LDModelObjectReferenceArrayList();

    public PropertyGroupModel(ILDModel ldModel) {
        fLDModel = ldModel;
        ldModel.addPropertyChangeListener(this);
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "propgroup-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public void setIdentifier(String id) {
        fID = id;
    }

    public String getTitle() {
        return fTitle == null ? getLDModel().getObjectName(getTagName()) : fTitle;
    }

    public void setTitle(String title) {
        String old = fTitle;
        fTitle = title;
        getLDModel().firePropertyChange(this, PROPERTY_NAME, old, title);
    }
    
    // ====================================== PROPERTY REF ACCESS SUPPORT ===========================================

    public List<ILDModelObjectReference> getPropertyRefs() {
        return fPropertyRefs;
    }

    public boolean containsProperty(ILDModelObject child) {
        return fPropertyRefs.contains(child);
    }

    public IPropertyRefModel addProperty(ILDModelObject child, int index) {
        // Convert LD object to ref object...
        
        IPropertyRefModel ref = null;
        
        // If this is a ref, make a new ref (don't re-use the same ref)
        if(child instanceof IPropertyRefModel) {
            child = ((IPropertyRefModel)child).getLDModelObject();
        }
        
        if(child instanceof IPropertyTypeModel) {
            ref = new PropertyRefModel(getLDModel());
        }
        else if(child instanceof IPropertyGroupModel) {
            ref = new PropertyGroupRefModel(getLDModel());
        }
        
        if(ref != null) {
            ref.setReferenceIdentifer(((IIdentifier)child).getIdentifier());
            ref.setParent(this);            
            
            if(index == -1) {
                fPropertyRefs.add(ref);
            }
            else {
                if(index >= fPropertyRefs.size()) {
                    fPropertyRefs.add(ref);
                }
                else {
                    fPropertyRefs.add(index, ref);
                }
            }
            
            // Fire property change
            fLDModel.firePropertyChange(this, PROPERTY_PROPERTY_REFS_CHANGED, null, fPropertyRefs);
        }
        
        return ref;
    }
    
    public void removeProperty(ILDModelObject child) {
        fPropertyRefs.remove(child);
        
        // Fire property change
        fLDModel.firePropertyChange(this, PROPERTY_PROPERTY_REFS_CHANGED, null, fPropertyRefs);
    }

    // ====================================== PROPERTY CHANGE SUPPORT ===========================================

    public void propertyChange(PropertyChangeEvent evt) {
        // An object was removed - check if it was a Property that we reference...
        if(evt.getPropertyName().equals(ILDModelObjectContainer.PROPERTY_CHILD_REMOVED)) {
            ILDModelObject object =  (ILDModelObject)evt.getNewValue();
            
            // Property removed
            if(fPropertyRefs.contains(object)) {
                removeProperty(object);
            }
        }
    }        
    
    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        fID = element.getAttributeValue(LDModelFactory.IDENTIFIER);
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.TITLE)) {
                fTitle = child.getText();
            }
            
            else if(tag.equals(LDModelFactory.PROPERTY_REF)) {
                IPropertyRefModel ref = new PropertyRefModel(fLDModel);
                ref.setParent(this);
                ref.fromJDOM(child);
                fPropertyRefs.add(ref);
            }
            
            else if(tag.equals(LDModelFactory.PROPERTY_GROUP_REF)) {
                IPropertyRefModel ref = new PropertyGroupRefModel(fLDModel);
                ref.setParent(this);
                ref.fromJDOM(child);
                fPropertyRefs.add(ref);
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.PROPERTY_GROUP;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        element.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());
        
        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        element.addContent(title);
        
        // Property Refs
        for(ILDModelObjectReference ref : fPropertyRefs) {
            Element e = ref.toJDOM();
            element.addContent(e);
        }

        return element;
    }

}
