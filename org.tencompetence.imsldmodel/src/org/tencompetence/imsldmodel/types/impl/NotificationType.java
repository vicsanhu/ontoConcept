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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.activities.impl.LearningActivityRefModel;
import org.tencompetence.imsldmodel.activities.impl.SupportActivityRefModel;
import org.tencompetence.imsldmodel.internal.StringUtils;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.IEmailDataType;
import org.tencompetence.imsldmodel.types.INotificationType;


/**
 * Notification Type
 * 
 * @author Phillip Beauvoir
 * @version $Id: NotificationType.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class NotificationType implements INotificationType, PropertyChangeListener {
    
    private ILDModel fLDModel;
    
    /**
     * Email Data types
     */
    private List<IEmailDataType> fEmailDataTypes = new ArrayList<IEmailDataType>();
    private List<IEmailDataType> fUndoCache = new ArrayList<IEmailDataType>();
    
    private ILDModelObjectReference fActivityReference;
    
    private String fSubject;

    public NotificationType(ILDModel ldModel) {
        fLDModel = ldModel;
        ldModel.addPropertyChangeListener(this);
    }

    public ILDModel getLDModel() {
        return fLDModel;
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

    public List<IEmailDataType> getEmailDataTypes() {
        return fEmailDataTypes;
    }
    
    public IActivityModel getActivityReference() {
        if(fActivityReference != null) {
            return (IActivityModel)fActivityReference.getLDModelObject();
        }
        return null;
    }
    
    public void setActivityReference(IActivityModel activity) {
        ILDModelObjectReference old = fActivityReference;
        
        if(activity instanceof ILearningActivityModel) {
            fActivityReference = new LearningActivityRefModel(fLDModel);
            fActivityReference.setReferenceIdentifer(activity.getIdentifier());
        }
        else if(activity instanceof ISupportActivityModel) {
            fActivityReference = new SupportActivityRefModel(fLDModel);
            fActivityReference.setReferenceIdentifer(activity.getIdentifier());
        }
        else {
            fActivityReference = null;
        }
        
        getLDModel().firePropertyChange(this, PROPERTY_ACTIVITY, old, activity);
    }
    
    public String getSubject() {
        return fSubject;
    }
    
    public void setSubject(String subject) {
        String old = fSubject;
        fSubject = subject;
        getLDModel().firePropertyChange(this, PROPERTY_SUBJECT, old, subject);
    }

    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.EMAIL_DATA)) {
                IEmailDataType emailDataType = new EmailDataType(getLDModel());
                fEmailDataTypes.add(emailDataType);
                emailDataType.fromJDOM(child);
            }
            else if(tag.equals(LDModelFactory.LEARNING_ACTIVITY_REF)) {
                fActivityReference = new LearningActivityRefModel(fLDModel);
                fActivityReference.fromJDOM(child);
            }
            else if(tag.equals(LDModelFactory.SUPPORT_ACTIVITY_REF)) {
                fActivityReference = new SupportActivityRefModel(fLDModel);
                fActivityReference.fromJDOM(child);
            }
            else if(tag.equals(LDModelFactory.SUBJECT)) {
                fSubject = child.getText();
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.NOTIFICATION;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        // Email Data
        for(IEmailDataType emailData : fEmailDataTypes) {
            Element child = emailData.toJDOM();
            element.addContent(child);
        }

        // Ref
        if(fActivityReference != null) {
            Element child = fActivityReference.toJDOM();
            element.addContent(child);
        }
        
        // Subject
        if(StringUtils.isSet(fSubject)) {
            Element subject = new Element(LDModelFactory.SUBJECT, IMSLD_NAMESPACE_100_EMBEDDED);
            subject.setText(fSubject);
            element.addContent(subject);
        }
        
        return element;
    }
}
