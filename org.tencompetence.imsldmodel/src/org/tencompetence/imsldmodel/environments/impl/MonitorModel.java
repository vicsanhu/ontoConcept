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
import java.util.UUID;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.IMonitorModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;
import org.tencompetence.imsldmodel.types.impl.ItemType;
import org.tencompetence.imsldmodel.types.impl.ItemTypeCollection;


/**
 * Monitor Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: MonitorModel.java,v 1.6 2009/11/26 09:16:04 phillipus Exp $
 */
public class MonitorModel extends AbstractServiceModel
implements IMonitorModel, PropertyChangeListener {
    
    private IRoleModel fRole;
    
    private String fTitle;

    private IItemTypeContainer fItems;
    
    public MonitorModel(ILDModel ldModel) {
        super(ldModel);
        ldModel.addPropertyChangeListener(this);
        fItems = new ItemTypeCollection(ldModel);
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "monitor-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public IRoleModel getRole() {
        return fRole;
    }

    public void setRole(IRoleModel role) {
        fRole = role;
    }

    public String getTitle() {
        return fTitle == null ? getLDModel().getObjectName(getTagName()) : fTitle;
    }

    public void setTitle(String title) {
        String old = fTitle;
        fTitle = title;
        getLDModel().firePropertyChange(this, PROPERTY_NAME, old, title);
    }

    public IItemTypeContainer getItems() {
        return fItems;
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        // Listen to Roles being removed
        String eventName = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        if(ILDModelObjectContainer.PROPERTY_CHILD_REMOVED.equals(eventName) && newValue instanceof IRoleModel) {
            if(newValue == fRole) {
                fRole = null;
            }
        }
    }

    // ====================================== JDOM SUPPORT ===========================================
    
    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);
        
        Element monitor = element.getChild(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        if(monitor != null) {
            for(Object o : monitor.getChildren()) {
                Element child = (Element)o;
                String tag = child.getName();
                
                if(tag.equals(LDModelFactory.ROLE_REF)) {
                    String ref = child.getAttributeValue(LDModelFactory.REF);
                    fRole = (IRoleModel)getLDModel().getModelObject(ref);
                }
                
                if(tag.equals(LDModelFactory.TITLE)) {
                    fTitle = child.getText();
                }
                
                else if(tag.equals(LDModelFactory.ITEM)) {
                    IItemType itemType = new ItemType(getLDModel());
                    fItems.addChildItem(itemType);
                    itemType.fromJDOM(child);
                }
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.MONITOR;
    }

    @Override
    public Element toJDOM() {
        Element service = super.toJDOM();
        
        Element monitor = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        service.addContent(monitor);
        
        if(fRole == null) {
            Element self = new Element("self", IMSLD_NAMESPACE_100_EMBEDDED); //$NON-NLS-1$
            monitor.addContent(self);
        }
        else {
            Element roleref = new Element(LDModelFactory.ROLE_REF, IMSLD_NAMESPACE_100_EMBEDDED);
            roleref.setAttribute(LDModelFactory.REF, fRole.getIdentifier());
            monitor.addContent(roleref);
        }
        
        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        monitor.addContent(title);

        for(IItemType itemType : fItems.getItemTypes()) {
            Element child = itemType.toJDOM();
            monitor.addContent(child);
        }
        
        return service;
    }

}
