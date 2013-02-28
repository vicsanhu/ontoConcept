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
import org.tencompetence.imsldmodel.environments.ISendMailModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.IEmailDataType;
import org.tencompetence.imsldmodel.types.impl.EmailDataType;


/**
 * SendMailModel
 * 
 * @author Phillip Beauvoir
 * @version $Id: SendMailModel.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class SendMailModel extends AbstractServiceModel
implements ISendMailModel, PropertyChangeListener {
    
    /**
     * Title
     */
    private String fTitle;
    
    /**
     * Select selction options
     */
    private int fSelect = 0;
    
    /**
     * Email Data types
     */
    private List<IEmailDataType> fEmailDataTypes = new ArrayList<IEmailDataType>();
    
    private List<IEmailDataType> fUndoCache = new ArrayList<IEmailDataType>();


    public SendMailModel(ILDModel ldModel) {
        super(ldModel);
        ldModel.addPropertyChangeListener(this);
    }
    
    public String getIdentifier() {
        if(fID == null) {
            fID = "sendmail-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public String getTitle() {
        return fTitle == null ? getLDModel().getObjectName(getTagName()) : fTitle;
    }

    public void setTitle(String title) {
        String old = fTitle;
        fTitle = title;
        getLDModel().firePropertyChange(this, PROPERTY_NAME, old, title);
    }

    public int getSelect() {
        return fSelect;
    }

    public void setSelect(int select) {
        fSelect = select;
    }

    public List<IEmailDataType> getEmailDataTypes() {
        return fEmailDataTypes;
    }
    
    public IEmailDataType addEmailDataType(IRoleModel role) {
        if(role == null) {
            return null;
        }
        
        // If we already have it, return that
        IEmailDataType emailData = getEmailDataType(role);
        if(emailData != null) {
            return emailData;
        }
        
        emailData = new EmailDataType(getLDModel());
        emailData.setRole(role);
        getEmailDataTypes().add(emailData);
        return emailData;
    } 
    
    public IEmailDataType getEmailDataType(IRoleModel role) {
        return getEmailDataType(role, getEmailDataTypes());
    }
    
    private IEmailDataType getEmailDataTypeFromUndoCache(IRoleModel role) {
        return getEmailDataType(role, fUndoCache);
    }
    
    private IEmailDataType getEmailDataType(IRoleModel role, List<IEmailDataType> list) {
        if(role == null) {
            return null;
        }
        
        for(IEmailDataType emailData : list) {
            if(role == emailData.getRole()) {
                return emailData;
            }
        }
        
        return null;
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        // Listen to Roles being removed
        if(evt.getNewValue() instanceof IRoleModel) {
            IRoleModel role = (IRoleModel)evt.getNewValue();
            if(ILDModelObjectContainer.PROPERTY_CHILD_REMOVED.equals(evt.getPropertyName())) {
                IEmailDataType emailData = getEmailDataType(role);
                if(emailData != null) {
                    getEmailDataTypes().remove(emailData);
                    fUndoCache.add(emailData);
                }
            }
            else if(ILDModelObjectContainer.PROPERTY_CHILD_ADDED.equals(evt.getPropertyName())) {
                IEmailDataType emailData = getEmailDataTypeFromUndoCache(role);
                if(emailData != null) {
                    getEmailDataTypes().add(emailData);
                    fUndoCache.remove(emailData);
                }
            }
        }
    }


    @Override
    public Element toJDOM() {
        Element service = super.toJDOM();
        
        Element sendmail = new Element(LDModelFactory.SEND_MAIL, IMSLD_NAMESPACE_100_EMBEDDED);
        service.addContent(sendmail);
        
        sendmail.setAttribute(LDModelFactory.SELECT, SELECT_STRINGS[fSelect]);
        
        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        sendmail.addContent(title);
        
        // Email Data
        for(IEmailDataType emailData : fEmailDataTypes) {
            Element child = emailData.toJDOM();
            sendmail.addContent(child);
        }
        
        return service;
    }
    
    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);

        Element sendmail = element.getChild(LDModelFactory.SEND_MAIL, IMSLD_NAMESPACE_100_EMBEDDED);
        if(sendmail != null) {
            
            String s = sendmail.getAttributeValue(LDModelFactory.SELECT);
            if(s != null) {
                for(int i = 0; i < SELECT_STRINGS.length; i++) {
                    if(s.equals(SELECT_STRINGS[i])) {
                        fSelect = i;
                    }
                }
            }
            
            for(Object o : sendmail.getChildren()) {
                Element child = (Element)o;
                String tag = child.getName();

                if(tag.equals(LDModelFactory.TITLE)) {
                    fTitle = child.getText();
                }

                else if(tag.equals(LDModelFactory.EMAIL_DATA)) {
                    IEmailDataType emailDataType = new EmailDataType(getLDModel());
                    fEmailDataTypes.add(emailDataType);
                    emailDataType.fromJDOM(child);
                }
            }
        }
    }
    
    public String getTagName() {
        return LDModelFactory.SEND_MAIL;
    }
}
