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
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.IConferenceModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;
import org.tencompetence.imsldmodel.types.impl.ItemType;
import org.tencompetence.imsldmodel.types.impl.ItemTypeCollection;


/**
 * Conference Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: ConferenceModel.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class ConferenceModel extends AbstractServiceModel
implements IConferenceModel, PropertyChangeListener {

    /**
     * Title
     */
    private String fTitle;
    
    /**
     * Type selection options
     */
    private int fType = 0;
    
    private IRoleModel fManager;
    
    private IRoleModel fModerator;
    
    private List<IRoleModel> fParticipants = new ArrayList<IRoleModel>();
    
    private List<IRoleModel> fObservers = new ArrayList<IRoleModel>();
    
    private List<IRoleModel> fObserversUndoCache = new ArrayList<IRoleModel>();
    private List<IRoleModel> fParticipantsUndoCache = new ArrayList<IRoleModel>();
    
    private IItemTypeContainer fItem;
    

    public ConferenceModel(ILDModel ldModel) {
        super(ldModel);
        ldModel.addPropertyChangeListener(this);
        fItem = new ItemTypeCollection(ldModel, 1);
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "conference-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public IRoleModel getManager() {
        return fManager;
    }

    public IRoleModel getModerator() {
        return fModerator;
    }

    public List<IRoleModel> getObservers() {
        return fObservers;
    }

    public List<IRoleModel> getParticipants() {
        return fParticipants;
    }

    public int getType() {
        return fType;
    }

    public void setManager(IRoleModel role) {
        fManager = role;
    }

    public void setModerator(IRoleModel role) {
        fModerator = role;
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

    public IItemTypeContainer getItem() {
        return fItem;
    }
    
    public void addParticipant(IRoleModel role) {
        if(role != null && !fParticipants.contains(role)) {
            fParticipants.add(role);
        }
    }

    public void removeParticipant(IRoleModel role) {
        if(role != null) {
            fParticipants.remove(role);
        }
    }

    public void addObserver(IRoleModel role) {
        if(role != null && !fObservers.contains(role)) {
            fObservers.add(role);
        }
    }

    public void removeObserver(IRoleModel role) {
        if(role != null) {
            fObservers.remove(role);
        }
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        // Listen to Roles being removed
        if(evt.getNewValue() instanceof IRoleModel) {
            IRoleModel role = (IRoleModel)evt.getNewValue();
            if(ILDModelObjectContainer.PROPERTY_CHILD_REMOVED.equals(evt.getPropertyName())) {
                if(fParticipants.contains(role)) {
                    fParticipants.remove(role);
                    fParticipantsUndoCache.add(role);
                }
                if(fObservers.contains(role)) {
                    fObservers.remove(role);
                    fObserversUndoCache.add(role);
                }
                if(fManager == role) {
                    fManager = null;
                }
                if(fModerator == role) {
                    fModerator = null;
                }
            }
            else if(ILDModelObjectContainer.PROPERTY_CHILD_ADDED.equals(evt.getPropertyName())) {
                if(fParticipantsUndoCache.contains(role)) {
                    fParticipants.add(role);
                    fParticipantsUndoCache.remove(role);
                }
                if(fObserversUndoCache.contains(role)) {
                    fObservers.add(role);
                    fObserversUndoCache.remove(role);
                }
            }
        }
    }


    @Override
    public Element toJDOM() {
        Element service = super.toJDOM();
        
        Element conference = new Element(LDModelFactory.CONFERENCE, IMSLD_NAMESPACE_100_EMBEDDED);
        service.addContent(conference);
        
        // Type
        conference.setAttribute(LDModelFactory.CONFERENCE_TYPE, TYPE_STRINGS[fType]);
        
        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        conference.addContent(title);
        
        // Participants
        for(IRoleModel role : fParticipants) {
            Element child = new Element(LDModelFactory.PARTICIPANT, IMSLD_NAMESPACE_100_EMBEDDED);
            child.setAttribute(LDModelFactory.ROLE_REF, role.getIdentifier());
            conference.addContent(child);
        }
        
        // Observers
        for(IRoleModel role : fObservers) {
            Element child = new Element(LDModelFactory.OBSERVER, IMSLD_NAMESPACE_100_EMBEDDED);
            child.setAttribute(LDModelFactory.ROLE_REF, role.getIdentifier());
            conference.addContent(child);
        }
        
        // Conference Manager
        if(fManager != null) {
            Element child = new Element(LDModelFactory.CONFERENCE_MANAGER, IMSLD_NAMESPACE_100_EMBEDDED);
            child.setAttribute(LDModelFactory.ROLE_REF, fManager.getIdentifier());
            conference.addContent(child);
        }

        // Moderator
        if(fModerator != null) {
            Element child = new Element(LDModelFactory.MODERATOR, IMSLD_NAMESPACE_100_EMBEDDED);
            child.setAttribute(LDModelFactory.ROLE_REF, fModerator.getIdentifier());
            conference.addContent(child);
        }
        
        // Item
        for(IItemType itemType : fItem.getItemTypes()) {
            Element child = itemType.toJDOM();
            conference.addContent(child);
        }

        return service;
    }
    
    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);

        Element conference = element.getChild(LDModelFactory.CONFERENCE, IMSLD_NAMESPACE_100_EMBEDDED);
        if(conference != null) {
            
            String s = conference.getAttributeValue(LDModelFactory.CONFERENCE_TYPE);
            if(s != null) {
                for(int i = 0; i < TYPE_STRINGS.length; i++) {
                    if(s.equals(TYPE_STRINGS[i])) {
                        fType = i;
                    }
                }
            }
            
            for(Object o : conference.getChildren()) {
                Element child = (Element)o;
                String tag = child.getName();

                if(tag.equals(LDModelFactory.TITLE)) {
                    fTitle = child.getText();
                }

                else if(tag.equals(LDModelFactory.PARTICIPANT)) {
                    String ref = child.getAttributeValue(LDModelFactory.ROLE_REF);
                    IRoleModel role = (IRoleModel)getLDModel().getModelObject(ref);
                    addParticipant(role);
                }

                else if(tag.equals(LDModelFactory.OBSERVER)) {
                    String ref = child.getAttributeValue(LDModelFactory.ROLE_REF);
                    IRoleModel role = (IRoleModel)getLDModel().getModelObject(ref);
                    addObserver(role);
                }

                else if(tag.equals(LDModelFactory.CONFERENCE_MANAGER)) {
                    String ref = child.getAttributeValue(LDModelFactory.ROLE_REF);
                    fManager = (IRoleModel)getLDModel().getModelObject(ref);
                }

                else if(tag.equals(LDModelFactory.MODERATOR)) {
                    String ref = child.getAttributeValue(LDModelFactory.ROLE_REF);
                    fModerator = (IRoleModel)getLDModel().getModelObject(ref);
                }

                else if(tag.equals(LDModelFactory.ITEM)) {
                    IItemType itemType = new ItemType(getLDModel());
                    fItem.addChildItem(itemType);
                    itemType.fromJDOM(child);
                }
            }
        }
    }
    
    public String getTagName() {
        return LDModelFactory.CONFERENCE;
    }
}
