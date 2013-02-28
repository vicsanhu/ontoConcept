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

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.internal.StringUtils;
import org.tencompetence.imsldmodel.types.IItemModelType;
import org.tencompetence.imsldmodel.types.IItemType;


/**
 * ItemModelType
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemModelType.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class ItemModelType
implements IItemModelType {

    private ILDModel fLDModel;
    
    private String fTagName;

    private String fTitle = ""; //$NON-NLS-1$
    private List<IItemType> fItemTypes;
    
    public ItemModelType(ILDModel ldModel, String tagName) {
        fLDModel = ldModel;
        fTagName = tagName;
        fItemTypes = new ArrayList<IItemType>();
    }
    
    public ILDModel getLDModel() {
        return fLDModel;
    }

    public List<IItemType> getItemTypes() {
        return fItemTypes;
    }
    
    public void addChildItem(IItemType item) {
        getItemTypes().add(item);
        item.setParent(this);
    }
    
    public boolean canAddChildItem() {
        return true;
    }
    
    public void removeChildItem(IItemType item) {
        getItemTypes().remove(item);
    }

    public String getTitle() {
        return fTitle;
    }

    public void setTitle(String title) {
        fTitle = title;
    }
    
    public int getMaximumAllowed() {
        return -1;
    }


    // ================================== XML JDOM PERSISTENCE =================================

    public void fromJDOM(Element element) {
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
        return fTagName;
    }

    public Element toJDOM() {
        // No Items
        if(fItemTypes.isEmpty()) {
            return null;
        }
        
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
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
