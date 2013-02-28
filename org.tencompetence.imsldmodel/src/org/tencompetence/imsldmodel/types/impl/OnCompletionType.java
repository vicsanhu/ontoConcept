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

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IChangePropertyValueType;
import org.tencompetence.imsldmodel.expressions.impl.ChangePropertyValueType;
import org.tencompetence.imsldmodel.types.INotificationType;
import org.tencompetence.imsldmodel.types.IOnCompletionType;


/**
 * OnCompletion Type
 * 
 * @author Phillip Beauvoir
 * @version $Id: OnCompletionType.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class OnCompletionType
implements IOnCompletionType {

    private ILDModelObject fParent;
    
    private ItemModelType fFeedBackDescription;
    
    private List<IChangePropertyValueType> fChangePropertyValueTypes;
    
    private List<INotificationType> fNotificationTypes;
    
    public OnCompletionType(ILDModelObject parent) {
        fParent = parent;
        fFeedBackDescription = new ItemModelType(getLDModel(), LDModelFactory.FEEDBACK_DESCRIPTION);
    }

    public ILDModel getLDModel() {
        return fParent.getLDModel();
    }

    public ILDModelObject getParent() {
        return fParent;
    }

    public ItemModelType getFeedBackDescription() {
        return fFeedBackDescription;
    }

    public List<IChangePropertyValueType> getChangePropertyValueTypes() {
        if(fChangePropertyValueTypes == null) {
            fChangePropertyValueTypes = new ArrayList<IChangePropertyValueType>();
        }
        return fChangePropertyValueTypes;
    }

    public List<INotificationType> getNotificationTypes() {
        if(fNotificationTypes == null) {
            fNotificationTypes = new ArrayList<INotificationType>();
        }
        return fNotificationTypes;
    }


    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.FEEDBACK_DESCRIPTION)) {
                getFeedBackDescription().fromJDOM(child);
            }
            else if(tag.equals(LDModelFactory.CHANGE_PROPERTY_VALUE)) {
                IChangePropertyValueType type = new ChangePropertyValueType(getLDModel());
                type.fromJDOM(child);
                getChangePropertyValueTypes().add(type);
            }
            else if(tag.equals(LDModelFactory.NOTIFICATION)) {
                INotificationType type = new NotificationType(getLDModel());
                type.fromJDOM(child);
                getNotificationTypes().add(type);
            }
        }
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        Element feedback = fFeedBackDescription.toJDOM();
        if(feedback != null) {
            element.addContent(feedback);
        }

        if(fChangePropertyValueTypes != null) {
            for(IChangePropertyValueType type : fChangePropertyValueTypes) {
                Element child = type.toJDOM();
                element.addContent(child);
            }
        }
        
        if(fNotificationTypes != null) {
            for(INotificationType type : fNotificationTypes) {
                Element child = type.toJDOM();
                element.addContent(child);
            }
        }
        
        return element.getChildren().isEmpty() ? null : element;
    }

    public String getTagName() {
        return LDModelFactory.ON_COMPLETION;
    }
}
