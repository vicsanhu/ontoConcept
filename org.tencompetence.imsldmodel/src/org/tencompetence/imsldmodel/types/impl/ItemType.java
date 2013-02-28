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
package org.tencompetence.imsldmodel.types.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.internal.StringUtils;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;


/**
 * Item Type
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemType.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class ItemType
implements IItemType, PropertyChangeListener {
    
    private ILDModel fLDModel;
    
    private String fID;
    
    private String fTitle = ""; //$NON-NLS-1$
    
    private String fParameters = ""; //$NON-NLS-1$
    
    private boolean fVisible = true;
    
    private String fIdentifierRef = ""; //$NON-NLS-1$
    
    private List<IItemType> fItemTypes;
    
    private IItemTypeContainer fParent;

    public ItemType(ILDModel ldModel) {
        fLDModel = ldModel;
        fLDModel.addPropertyChangeListener(this);
    }
    
    public ILDModel getLDModel() {
        return fLDModel;
    }
    
    public List<IItemType> getItemTypes() {
        if(fItemTypes == null) {
            fItemTypes = new ArrayList<IItemType>();
        }
        return fItemTypes;
    }
    
    public void addChildItem(IItemType item) {
        getItemTypes().add(item);
        item.setParent(this);
    }
    
    public void removeChildItem(IItemType item) {
        getItemTypes().remove(item);
    }
    
    public boolean canAddChildItem() {
        return true;
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "item-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public void setIdentifier(String id) {
        fID = id;
    }

    public String getTitle() {
        return fTitle;
    }

    public void setTitle(String title) {
        fTitle = title;
    }

    public String getParameters() {
        return fParameters;
    }

    public boolean isVisible() {
        return fVisible;
    }

    public void setIsVisible(boolean set) {
        fVisible = set;
    }

    public void setParameters(String s) {
        fParameters = s;
    }

    public String getIdentifierRef() {
        return fIdentifierRef;
    }

    public void setIdentifierRef(String ref) {
        fIdentifierRef = ref;
    }

    public IItemTypeContainer getParent() {
        return fParent;
    }

    public void setParent(IItemTypeContainer parent) {
        fParent = parent;
    }

    public int getMaximumAllowed() {
        return -1;
    }
    
    public void dispose() {
        // NOTE: If Undo/Redo is implemented for deleting Items, Property Chnage Listener need to be re-added or not removed
        fLDModel.removePropertyChangeListener(this);
        if(fItemTypes != null) {
            for(IItemType item : fItemTypes) {
                item.dispose();
            }
        }
    }
    
    /* 
     * When a Resource's identifer changes update the identifierref
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        
        if(IResourceModel.PROPERTY_RESOURCE_IDENTIFIER.equals(propertyName) && StringUtils.isSet(fIdentifierRef)) {
            String oldID = (String)evt.getOldValue();
            String newID = (String)evt.getNewValue();
            
            if(fIdentifierRef.equals(oldID)) {
                fIdentifierRef = newID;
            }
        }
    }


    // ================================== XML JDOM PERSISTENCE =================================
    
    public void fromJDOM(Element element) {
        fID = element.getAttributeValue(LDModelFactory.IDENTIFIER);
        fVisible = !"false".equals(element.getAttributeValue(LDModelFactory.ISVISIBLE)); //$NON-NLS-1$
        fParameters = element.getAttributeValue(LDModelFactory.PARAMETERS);
        fIdentifierRef = element.getAttributeValue(LDModelFactory.IDENTIFIER_REF);
        
        for(Object child : element.getChildren()) {
            String tag = ((Element)child).getName();
            String value = ((Element)child).getValue();
            
            if(tag.equals(LDModelFactory.TITLE)) {
                fTitle = value;
            }
            else if(tag.equals(LDModelFactory.ITEM)) {
                IItemType itemType = new ItemType(fLDModel);
                addChildItem(itemType);
                itemType.fromJDOM((Element)child);
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.ITEM;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        element.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());
        element.setAttribute(LDModelFactory.ISVISIBLE, fVisible ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$
        
        if(StringUtils.isSet(fParameters)) {
            element.setAttribute(LDModelFactory.PARAMETERS, fParameters);
        }
        
        if(StringUtils.isSet(fIdentifierRef)) {
            element.setAttribute(LDModelFactory.IDENTIFIER_REF, fIdentifierRef);
        }
        
        if(StringUtils.isSet(fTitle)) {
            Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
            title.setText(fTitle);
            element.addContent(title);
        }

        if(fItemTypes != null) {
            for(IItemType itemType : fItemTypes) {
                Element child = itemType.toJDOM();
                element.addContent(child);
            }
        }

        return element;
    }
}
