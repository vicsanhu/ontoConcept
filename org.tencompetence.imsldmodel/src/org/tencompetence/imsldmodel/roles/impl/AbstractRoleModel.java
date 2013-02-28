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
package org.tencompetence.imsldmodel.roles.impl;

import java.util.UUID;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.impl.LDModelObjectContainer;
import org.tencompetence.imsldmodel.internal.StringUtils;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.IItemModelType;
import org.tencompetence.imsldmodel.types.impl.ItemModelType;

/**
 * Abstract Role Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractRoleModel.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public abstract class AbstractRoleModel
extends LDModelObjectContainer
implements IRoleModel {
    
    private String fID;
    private String fTitle;
    private String fHref;
    private int fCreateNew;
    private int fMatchPersons;
    private int fMinPersons;
    private int fMaxPersons;
    
    private ILDModelObjectContainer fParent;
    
    private IItemModelType fInformationModel;
    
    protected AbstractRoleModel(ILDModel ldModel) {
        super(ldModel);
        fInformationModel = new ItemModelType(ldModel, LDModelFactory.INFORMATION);
    }
    
    public String getIdentifier() {
        if(fID == null) {
            fID = "role-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }
    
    public void setIdentifier(String id) {
        fID = id;
    }
    
    public String getHref() {
        return fHref;
    }
    
    public void setHref(String href) {
        fHref = href;
    }
    
    public String getTitle() {
        return fTitle;
    }
    
    public void setTitle(String title) {
        String old = fTitle;
        fTitle = title;
        getLDModel().firePropertyChange(this, PROPERTY_NAME, old, title);
    }
    
    public int getCreateNew() {
        return fCreateNew;
    }

    public void setCreateNew(int s) {
        fCreateNew = s;
    }

    public int getMaxPersons() {
        return fMaxPersons;
    }

    public void setMaxPersons(int num) {
        fMaxPersons = num;
    }

    public int getMinPersons() {
        return fMinPersons;
    }

    public void setMinPersons(int num) {
        fMinPersons = num;
    }

    public int getMatchPersons() {
        return fMatchPersons;
    }

    public void setMatchPersons(int s) {
        fMatchPersons = s;
    }
    
    public IItemModelType getInformationModel() {
        return fInformationModel;
    }
    
    public ILDModelObjectContainer getParent() {
        return fParent;
    }
    
    public void setParent(ILDModelObjectContainer parent) {
        fParent = parent;
    }
    
    /**
     * Returns true if this role is referenced at all by any Role Part in the LD. Includes child Roles.
     */
    public boolean isReferencedByRolePart() {
        for(ILDModelObject play : getLDModel().getMethodModel().getPlaysModel().getChildren()) {
            for(ILDModelObject act : ((IPlayModel)play).getActsModel().getChildren()) {
                for(ILDModelObject rolePart : ((IActModel)act).getRolePartsModel().getChildren()) {
                    IRoleModel role = ((IRolePartModel)rolePart).getRole();
                    if(role == this) {
                        return true;
                    }
                }
            }
        }
        
        // Check child roles
        for(ILDModelObject childRole : getChildren()) {
            if(((IRoleModel)childRole).isReferencedByRolePart()) {
                return true;
            }
        }

        return false;
    }


    
    // ============================================ JDOM ========================================
    
    public void fromJDOM(Element element) {
        fID = element.getAttributeValue(LDModelFactory.IDENTIFIER);
        fHref = element.getAttributeValue(LDModelFactory.HREF);
        
        String s = element.getAttributeValue(LDModelFactory.CREATE_NEW);
        if(s != null) {
            for(int i = 0; i < CREATE_NEW_STRINGS.length; i++) {
                if(s.equals(CREATE_NEW_STRINGS[i])) {
                    fCreateNew = i;
                }
            }
        }

        s = element.getAttributeValue(LDModelFactory.MATCH_PERSONS);
        if(s != null) {
            for(int i = 0; i < MATCH_PERSONS_STRINGS.length; i++) {
                if(s.equals(MATCH_PERSONS_STRINGS[i])) {
                    fMatchPersons = i;
                }
            }
        }

        try {
            Attribute att = element.getAttribute(LDModelFactory.MIN_PERSONS);
            if(att != null) {
                fMinPersons = att.getIntValue();
            }
            att = element.getAttribute(LDModelFactory.MAX_PERSONS);
            if(att != null) {
                fMaxPersons = att.getIntValue();
            }
        }
        catch(DataConversionException ex) {
            ex.printStackTrace();
        }
        
        Element title = element.getChild(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        if(title != null) {
            fTitle = title.getText();
        }
        
        Element im = element.getChild(LDModelFactory.INFORMATION, IMSLD_NAMESPACE_100_EMBEDDED);
        if(im != null) {
            fInformationModel.fromJDOM(im);
        }
        
        for(Object child : element.getChildren(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED)) {
            IRoleModel childRole = (IRoleModel)LDModelFactory.createModelObject(getTagName(), getLDModel());
            childRole.fromJDOM((Element)child);
            addChild(childRole);
        }
    }
    
    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        element.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());
        if(StringUtils.isSet(fHref)) {
            element.setAttribute(LDModelFactory.HREF, fHref);
        }
        
        if(fCreateNew != 0) {
            element.setAttribute(LDModelFactory.CREATE_NEW, CREATE_NEW_STRINGS[fCreateNew]);
        }
        if(fMatchPersons != 0) {
            element.setAttribute(LDModelFactory.MATCH_PERSONS, MATCH_PERSONS_STRINGS[fMatchPersons]);
        }
        if(fMinPersons != 0) {
            element.setAttribute(LDModelFactory.MIN_PERSONS, "" + fMinPersons); //$NON-NLS-1$
        }
        if(fMaxPersons != 0) {
            element.setAttribute(LDModelFactory.MAX_PERSONS, "" + fMaxPersons); //$NON-NLS-1$
        }
        
        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        element.addContent(title);
        
        // Information
        Element im = fInformationModel.toJDOM();
        if(im != null) {
            element.addContent(im);
        }
        
        // Children
        for(ILDModelObject role : getChildren()) {
            Element child = role.toJDOM();
            element.addContent(child);
        }
        
        return element;
    }
    
}
