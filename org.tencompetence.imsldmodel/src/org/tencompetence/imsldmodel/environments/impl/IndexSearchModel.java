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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jdom.Element;
import org.tencompetence.imsldmodel.IIdentifier;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.IIndexSearchModel;


/**
 * Index Search Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: IndexSearchModel.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class IndexSearchModel extends AbstractServiceModel
implements IIndexSearchModel, PropertyChangeListener {
    
    private int fType = 0;
    
    private String fTitle;
    
    private List<String> fIndexClass = new ArrayList<String>();
    
    private List<String> fIndexElements = new ArrayList<String>();
    
    private List<String> fIndexTypeOfElements = new ArrayList<String>();
    
    private List<ILDModelObject> fUndoCache = new ArrayList<ILDModelObject>();

    public IndexSearchModel(ILDModel ldModel) {
        super(ldModel);
        ldModel.addPropertyChangeListener(this);
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "index-search-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public int getType() {
        return fType;
    }

    public void setType(int type) {
        fType = type;
    }

    public String getTitle() {
        return fTitle == null ? getLDModel().getObjectName(getTagName()) : fTitle;
    }

    public void setTitle(String title) {
        String old = fTitle;
        fTitle = title;
        getLDModel().firePropertyChange(this, PROPERTY_NAME, old, title);
    }

    public List<String> getIndexClasses() {
        return fIndexClass;
    }

    public List<String> getIndexElements() {
        return fIndexElements;
    }

    public List<String> getIndexTypeofElements() {
        return fIndexTypeOfElements;
    }
    
    public void setIndexClasses(List<String> list) {
        fIndexClass = list;
    }

    public void setIndexElements(List<String> list) {
        fIndexElements = list;
    }

    public void setIndexTypeofElements(List<String> list) {
        fIndexTypeOfElements = list;
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        // React to LD components removed and added for Index Element
        if(ILDModelObjectContainer.PROPERTY_CHILD_REMOVED.equals(evt.getPropertyName())) {
            ILDModelObject object = (ILDModelObject)evt.getNewValue();
            removeReference(object);
        }
        
        else if(ILDModelObjectContainer.PROPERTY_CHILD_ADDED.equals(evt.getPropertyName())) {
            ILDModelObject object = (ILDModelObject)evt.getNewValue();
            undoRemoveReference(object);
        }
    }
    
    private void removeReference(ILDModelObject object) {
        if((object instanceof IIdentifier) && hasReference((IIdentifier)object)) {
            fIndexElements.remove(((IIdentifier)object).getIdentifier());
            fUndoCache.add(object);
        }
        // If a container type was deleted also check its children
        if(object instanceof ILDModelObjectContainer) {
            for(ILDModelObject child : ((ILDModelObjectContainer)object).getChildren()) {
                removeReference(child);
            }
        }
    }
    
    private void undoRemoveReference(ILDModelObject object) {
        if((object instanceof IIdentifier) && fUndoCache.contains(object)) {
            fIndexElements.add(((IIdentifier)object).getIdentifier());
            fUndoCache.remove(object);
        }
        // If a container type was added also check its children
        if(object instanceof ILDModelObjectContainer) {
            for(ILDModelObject child : ((ILDModelObjectContainer)object).getChildren()) {
                undoRemoveReference(child);
            }
        }
    }

    private boolean hasReference(IIdentifier object) {
        for(String id : fIndexElements) {
            if(id.equals(object.getIdentifier())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Element toJDOM() {
        Element service = super.toJDOM();
        
        Element indexsearch = new Element(LDModelFactory.INDEX_SEARCH, IMSLD_NAMESPACE_100_EMBEDDED);
        service.addContent(indexsearch);
        
        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        indexsearch.addContent(title);
        
        // Index
        Element index = new Element(LDModelFactory.INDEX, IMSLD_NAMESPACE_100_EMBEDDED);
        
        // Index Class
        for(String s : fIndexClass) {
            Element indexClass = new Element(LDModelFactory.INDEX_CLASS, IMSLD_NAMESPACE_100_EMBEDDED);
            indexClass.setText(s);
            index.addContent(indexClass);
        }
        
        // Index Elements
        for(String s : fIndexElements) {
            Element indexElement = new Element(LDModelFactory.INDEX_ELEMENT, IMSLD_NAMESPACE_100_EMBEDDED);
            indexElement.setAttribute(LDModelFactory.INDEX, s);
            index.addContent(indexElement);
        }

        // Index Types of Element
        for(String s : fIndexTypeOfElements) {
            Element indexTypeElement = new Element(LDModelFactory.INDEX_TYPE_OF_ELEMENT, IMSLD_NAMESPACE_100_EMBEDDED);
            indexTypeElement.setText(s);
            index.addContent(indexTypeElement);
        }
        
        // If there is anything to add...
        if(!index.getChildren().isEmpty()) {
            indexsearch.addContent(index);
        }

        // Search Type
        Element search = new Element(LDModelFactory.SEARCH, IMSLD_NAMESPACE_100_EMBEDDED);
        indexsearch.addContent(search);
        search.setAttribute(LDModelFactory.SEARCH_TYPE, TYPE_STRINGS[fType]);
        
        return service;
    }

    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);

        Element indexsearch = element.getChild(LDModelFactory.INDEX_SEARCH, IMSLD_NAMESPACE_100_EMBEDDED);
        if(indexsearch != null) {
            for(Object o : indexsearch.getChildren()) {
                Element child = (Element)o;
                String tag = child.getName();

                if(tag.equals(LDModelFactory.TITLE)) {
                    fTitle = child.getText();
                }

                else if(tag.equals(LDModelFactory.INDEX)) {
                    for(Object object : child.getChildren(LDModelFactory.INDEX_CLASS, IMSLD_NAMESPACE_100_EMBEDDED)) {
                        fIndexClass.add(((Element)object).getText());
                    }
                    
                    for(Object object : child.getChildren(LDModelFactory.INDEX_ELEMENT, IMSLD_NAMESPACE_100_EMBEDDED)) {
                        String s = ((Element)object).getAttributeValue(LDModelFactory.INDEX);
                        if(s != null) {
                            fIndexElements.add(s);
                        }
                    }
                    
                    for(Object object : child.getChildren(LDModelFactory.INDEX_TYPE_OF_ELEMENT, IMSLD_NAMESPACE_100_EMBEDDED)) {
                        fIndexTypeOfElements.add(((Element)object).getText());
                    }
                }

                else if(tag.equals(LDModelFactory.SEARCH)) {
                    String s = child.getAttributeValue(LDModelFactory.SEARCH_TYPE);
                    if(s != null) {
                        for(int i = 0; i < TYPE_STRINGS.length; i++) {
                            if(s.equals(TYPE_STRINGS[i])) {
                                fType = i;
                            }
                        }
                    }
                }
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.INDEX_SEARCH;
    }
}
