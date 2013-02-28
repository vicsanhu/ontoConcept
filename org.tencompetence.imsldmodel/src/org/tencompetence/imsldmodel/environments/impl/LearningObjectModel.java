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
package org.tencompetence.imsldmodel.environments.impl;

import java.util.UUID;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.internal.StringUtils;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;
import org.tencompetence.imsldmodel.types.impl.ItemType;
import org.tencompetence.imsldmodel.types.impl.ItemTypeCollection;


/**
 * Description
 * 
 * @author Phillip Beauvoir
 * @version $Id: LearningObjectModel.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class LearningObjectModel implements ILearningObjectModel {
    
    private ILDModel fLDModel;
    
    private String fID;
    private boolean fIsVisible = true;
    private String fClass, fParameters;
    int fType = TYPE_NONE;

    private String fTitle;
    
    private IItemTypeContainer fItems;
    
    public LearningObjectModel(ILDModel ldModel) {
        fLDModel = ldModel;
        fItems = new ItemTypeCollection(ldModel);
    }
    
    public ILDModel getLDModel() {
        return fLDModel;
    }

    public String getTitle() {
        if(fTitle == null) {
            switch(fType) {
                case TYPE_NONE:
                    return getLDModel().getObjectName(getTagName());
                    
                default:
                    return getLDModel().getObjectName(TYPE_STRINGS[fType]);
            }
        }
        
        return fTitle;
    }

    public void setTitle(String title) {
        String old = fTitle;
        fTitle = title;
        getLDModel().firePropertyChange(this, PROPERTY_NAME, old, title);
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "lo-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public void setIdentifier(String id) {
        fID = id;
    }

    public boolean isVisible() {
        return fIsVisible;
    }

    public void setIsVisible(boolean set) {
        fIsVisible = set;
    }

    public String getClassType() {
        return fClass;
    }

    public IItemTypeContainer getItems() {
        return fItems;
    }

    public String getParameters() {
        return fParameters;
    }

    public int getType() {
        return fType;
    }

    public void setClassType(String s) {
        fClass = s;
    }

    public void setParameters(String s) {
        fParameters = s;
    }

    public void setType(int type) {
        int old = fType;
        fType = type;
        getLDModel().firePropertyChange(this, PROPERTY_TYPE, old, fType);
    }

    public void fromJDOM(Element element) {
        fID = element.getAttributeValue(LDModelFactory.IDENTIFIER);
        fIsVisible = !"false".equals(element.getAttributeValue(LDModelFactory.ISVISIBLE)); //$NON-NLS-1$
        fClass = element.getAttributeValue(LDModelFactory.CLASS);
        fParameters = element.getAttributeValue(LDModelFactory.PARAMETERS);
        
        String s = element.getAttributeValue(LDModelFactory.TYPE);
        if(s != null) {
            for(int i = 0; i < TYPE_STRINGS.length; i++) {
                if(s.equals(TYPE_STRINGS[i])) {
                    fType = i;
                }
            }
        }
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.TITLE)) {
                fTitle = child.getText();
            }
            
            else if(tag.equals(LDModelFactory.ITEM)) {
                IItemType itemType = new ItemType(fLDModel);
                fItems.addChildItem(itemType);
                itemType.fromJDOM(child);
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.LEARNING_OBJECT;
    }

    public Element toJDOM() {
        Element learningObject = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);

        learningObject.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());
        learningObject.setAttribute(LDModelFactory.ISVISIBLE, "" + isVisible()); //$NON-NLS-1$
        
        if(StringUtils.isSet(fClass)) {
            learningObject.setAttribute(LDModelFactory.CLASS, fClass);
        }

        if(StringUtils.isSet(fParameters)) {
            learningObject.setAttribute(LDModelFactory.PARAMETERS, fParameters);
        }

        if(fType != 0) {
            learningObject.setAttribute(LDModelFactory.TYPE, TYPE_STRINGS[fType]);
        }

        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        learningObject.addContent(title);

        for(IItemType itemType : fItems.getItemTypes()) {
            Element child = itemType.toJDOM();
            learningObject.addContent(child);
        }
        
        return learningObject;
    }
}
