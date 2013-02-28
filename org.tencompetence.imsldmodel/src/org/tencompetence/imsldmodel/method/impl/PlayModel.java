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

import java.util.UUID;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.method.IActsModel;
import org.tencompetence.imsldmodel.method.ICompletePlayType;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.types.IOnCompletionType;
import org.tencompetence.imsldmodel.types.impl.OnCompletionType;

/**
 * Play
 * 
 * @author Phillip Beauvoir
 * @version $Id: PlayModel.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class PlayModel
implements IPlayModel {
    
    private ILDModel fLDModel;
    
    private boolean fIsVisible = true;
    private String fTitle;
    private String fID;
    
    private IActsModel fActs;
    
    private ICompletePlayType fCompletePlayType;
    
    private IOnCompletionType fCompletionType;

    public PlayModel(ILDModel ldModel) {
        fActs = new ActsModel(ldModel);
        fLDModel = ldModel;
    }

    public void setLDModel(ILDModel ldModel) {
        fLDModel = ldModel;
    }
    
    public ILDModel getLDModel() {
        return fLDModel;
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "play-" + UUID.randomUUID().toString(); //$NON-NLS-1$
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

    public boolean isVisible() {
        return fIsVisible;
    }

    public void setIsVisible(boolean set) {
        fIsVisible = set;
    }
    
    public IActsModel getActsModel() {
        return fActs;
    }

    public ICompletePlayType getCompletePlayType() {
        if(fCompletePlayType == null) {
            fCompletePlayType = new CompletePlayType(this);
        }
        return fCompletePlayType;
    }

    public IOnCompletionType getOnCompletionType() {
        if(fCompletionType == null) {
            fCompletionType = new OnCompletionType(this);
        }
        return fCompletionType;
    }

    public void fromJDOM(Element element) {
        fID = element.getAttributeValue(LDModelFactory.IDENTIFIER);
        fIsVisible = !"false".equals(element.getAttributeValue(LDModelFactory.ISVISIBLE)); //$NON-NLS-1$
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.TITLE)) {
                fTitle = child.getText();
            }
            
            // Act
            if(tag.equals(LDModelFactory.ACT)) {
                ILDModelObject act = LDModelFactory.createModelObject(LDModelFactory.ACT, fLDModel);
                act.fromJDOM((Element)child); // This first, in order to set ID
                fActs.addChild(act);
            }
            
            // Complete Play
            else if(tag.equals(LDModelFactory.COMPLETE_PLAY)) {
                getCompletePlayType().fromJDOM((Element)child);
            }
            
            // Completion
            else if(tag.equals(LDModelFactory.ON_COMPLETION)) {
                getOnCompletionType().fromJDOM((Element)child);
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.PLAY;
    }

    public Element toJDOM() {
        Element play = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        play.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());
        // Is visible
        play.setAttribute(LDModelFactory.ISVISIBLE, "" + isVisible()); //$NON-NLS-1$

        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        play.addContent(title);
        
        // Acts
        for(ILDModelObject act : fActs.getChildren()) {
            Element actE = act.toJDOM();
            play.addContent(actE);
        }

        if(fCompletePlayType != null) {
            Element child = fCompletePlayType.toJDOM();
            if(child != null) {
                play.addContent(child);
            }
        }
        
        if(fCompletionType != null) {
            Element child = fCompletionType.toJDOM();
            if(child != null) {
                play.addContent(child);
            }
        }

        return play;
    }
}
