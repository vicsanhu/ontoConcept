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
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.ICompleteActType;
import org.tencompetence.imsldmodel.method.IRolePartsModel;
import org.tencompetence.imsldmodel.types.IOnCompletionType;
import org.tencompetence.imsldmodel.types.impl.OnCompletionType;

/**
 * Act
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActModel.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class ActModel
implements IActModel {
    
    private ILDModel fLDModel;
    
    private ILDModelObjectContainer fParent;
    
    private String fTitle;
    private String fID;

    private IRolePartsModel fRoleParts;
    
    private ICompleteActType fCompleteActType;
    
    private IOnCompletionType fCompletionType;

    public ActModel(ILDModel ldModel) {
        fLDModel = ldModel;
        fRoleParts = new RolePartsModel(ldModel);
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "act-" + UUID.randomUUID().toString(); //$NON-NLS-1$
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

    public ICompleteActType getCompleteActType() {
        if(fCompleteActType == null) {
            fCompleteActType = new CompleteActType(this);
        }
        return fCompleteActType;
    }

    public IOnCompletionType getOnCompletionType() {
        if(fCompletionType == null) {
            fCompletionType = new OnCompletionType(this);
        }
        return fCompletionType;
    }

    public IRolePartsModel getRolePartsModel() {
        return fRoleParts;
    }
    
    public ILDModelObjectContainer getParent() {
        return fParent;
    }
    
    public void setParent(ILDModelObjectContainer parent) {
        fParent = parent;
    }

    public void fromJDOM(Element element) {
        fID = element.getAttributeValue(LDModelFactory.IDENTIFIER);
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.TITLE)) {
                fTitle = child.getText();
            }
            
            // Role Part
            if(tag.equals(LDModelFactory.ROLE_PART)) {
                ILDModelObject rolePart = LDModelFactory.createModelObject(LDModelFactory.ROLE_PART, fLDModel);
                rolePart.fromJDOM(child); // This first, in order to set ID
                fRoleParts.addChild(rolePart);
            }
            
            // Complete Act
            else if(tag.equals(LDModelFactory.COMPLETE_ACT)) {
                getCompleteActType().fromJDOM(child);
            }
            
            // Completion
            else if(tag.equals(LDModelFactory.ON_COMPLETION)) {
                getOnCompletionType().fromJDOM(child);
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.ACT;
    }

    public Element toJDOM() {
        Element act = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        act.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());

        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        act.addContent(title);
        
        // Role Parts
        for(ILDModelObject rolePart : fRoleParts.getChildren()) {
            Element rolepartE = rolePart.toJDOM();
            act.addContent(rolepartE);
        }

        if(fCompleteActType != null) {
            Element child = fCompleteActType.toJDOM();
            if(child != null) {
                act.addContent(child);
            }
        }
        
        if(fCompletionType != null) {
            Element child = fCompletionType.toJDOM();
            if(child != null) {
                act.addContent(child);
            }
        }

        return act;
    }
}
