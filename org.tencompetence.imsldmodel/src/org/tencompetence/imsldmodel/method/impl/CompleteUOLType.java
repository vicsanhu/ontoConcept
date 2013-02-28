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
package org.tencompetence.imsldmodel.method.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IWhenPropertyValueIsSetType;
import org.tencompetence.imsldmodel.expressions.impl.WhenPropertyValueIsSetType;
import org.tencompetence.imsldmodel.method.ICompleteUOLType;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.types.impl.AbstractCompleteType;


/**
 * CompleteUOLType
 * 
 * @author Phillip Beauvoir
 * @version $Id: CompleteUOLType.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class CompleteUOLType
extends AbstractCompleteType
implements ICompleteUOLType, PropertyChangeListener {
    
    private List<IPlayModel> fPlays;
    
    private List<IPlayModel> fUndoCache;
    
    public CompleteUOLType(ILDModelObject owner) {
        super(owner);
        owner.getLDModel().addPropertyChangeListener(this);
        fPlays = new ArrayList<IPlayModel>();
        fUndoCache = new ArrayList<IPlayModel>();
    }

    public void addPlay(IPlayModel play) {
        if(play != null && !fPlays.contains(play)) {
            fPlays.add(play);
        }
    }

    public void removePlay(IPlayModel play) {
        if(play != null) {
            fPlays.remove(play);
        }
    }

    public List<IPlayModel> getPlays() {
        return fPlays;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // Listen to Plays being removed
        if(evt.getNewValue() instanceof IPlayModel) {
            IPlayModel play = (IPlayModel)evt.getNewValue();
            if(fPlays.contains(play)) {
                if(ILDModelObjectContainer.PROPERTY_CHILD_REMOVED.equals(evt.getPropertyName())) {
                    removePlay(play);
                    fUndoCache.add(play);
                }
            }
            else if(ILDModelObjectContainer.PROPERTY_CHILD_ADDED.equals(evt.getPropertyName()) && fUndoCache.contains(play)) {
                addPlay(play);
                fUndoCache.remove(play);
            }
        }
    }

    public void fromJDOM(Element element) {
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.WHEN_PLAY_COMPLETED)) {
                setChoice(COMPLETE_WHEN_PLAY_COMPLETED);
                // Find Play and add it
                String ref = child.getAttributeValue(LDModelFactory.REF);
                IPlayModel play = (IPlayModel)getOwner().getLDModel().getModelObject(ref);
                addPlay(play);
            }
            else if(tag.equals(LDModelFactory.TIME_LIMIT)) {
                setChoice(COMPLETE_TIME_LIMIT);
                getTimeLimitType().fromJDOM(child);
            }
            else if(tag.equals(LDModelFactory.WHEN_PROPERTY_VALUE_IS_SET)) {
                setChoice(COMPLETE_WHEN_PROPERTY_SET);
                IWhenPropertyValueIsSetType type = new WhenPropertyValueIsSetType(getOwner().getLDModel());
                type.fromJDOM(child);
                getWhenPropertyValueIsSetTypes().add(type);
            }
        }
    }

    public Element toJDOM() {
        switch(getChoice()) {
            // No choice
            case COMPLETE_NONE:
                return null;
                
            // When Plays completed
            case COMPLETE_WHEN_PLAY_COMPLETED:
                if(fPlays.isEmpty()) {
                    return null; // No Plays
                }
                Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
                for(IPlayModel play : fPlays) {
                    Element child = new Element(LDModelFactory.WHEN_PLAY_COMPLETED, IMSLD_NAMESPACE_100_EMBEDDED);
                    child.setAttribute(LDModelFactory.REF, play.getIdentifier());
                    element.addContent(child);
                }
                return element;
                
            // Time Limit
            case COMPLETE_TIME_LIMIT:
                element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
                Element child = getTimeLimitType().toJDOM();
                element.addContent(child);
                return element;
                
            // When Property Value is Set
            case COMPLETE_WHEN_PROPERTY_SET:
                // None set
                if(getWhenPropertyValueIsSetTypes().isEmpty()) {
                    return null;
                }
                // Else OK
                element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
                for(IWhenPropertyValueIsSetType type : getWhenPropertyValueIsSetTypes()) {
                    Element e = type.toJDOM();
                    element.addContent(e);
                }
                return element;
            
            default:
                return null;
        }
    }
    
    public String getTagName() {
        return LDModelFactory.COMPLETE_UOL;
    }
}
