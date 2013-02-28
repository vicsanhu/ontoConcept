/*
 * Copyright (c) 2009, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.ui.views.conditions;

import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IShowHideType;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;


/**
 * Factory class for adapting Condition model types
 * 
 * @author Phillip Beauvoir
 * @version $Id: ConditionsAdapterFactory.java,v 1.5 2009/06/16 22:56:32 phillipus Exp $
 */
public class ConditionsAdapterFactory {
    
    /*
     * Show/Hide Choices for menu items. null denotes a menu separator
     */
    public static String[] SHOW_HIDE_CHOICES = {
            LDModelFactory.CLASS,
            LDModelFactory.ITEM_REF,
            LDModelFactory.ENVIRONMENT_REF,
            LDModelFactory.LEARNING_ACTIVITY_REF,
            LDModelFactory.SUPPORT_ACTIVITY_REF,
            LDModelFactory.ACTIVITY_STRUCTURE_REF,
            LDModelFactory.PLAY_REF,
            LDModelFactory.UOL_HREF
        };

    
    /**
     * @param tagName
     * @return Friendly display name for xml tag
     */
    public static String getFriendlyName(String tagName) {
        if(tagName == null) {
            return ""; //$NON-NLS-1$
        }
        
        if(LDModelFactory.SHOW.equals(tagName)) {
            return Messages.ConditionsAdapterFactory_0;
        }
        if(LDModelFactory.HIDE.equals(tagName)) {
            return Messages.ConditionsAdapterFactory_1;
        }
        if(LDModelFactory.CHANGE_PROPERTY_VALUE.equals(tagName)) {
            return Messages.ConditionsAdapterFactory_2;
        }
        if(LDModelFactory.NOTIFICATION.equals(tagName)) {
            return Messages.ConditionsAdapterFactory_3;
        }
        
        if(LDModelFactory.CLASS.equals(tagName)) {
            return Messages.ConditionsAdapterFactory_4;
        }
        if(LDModelFactory.ITEM_REF.equals(tagName)) {
            return Messages.ConditionsAdapterFactory_5;
        }
        if(LDModelFactory.ENVIRONMENT_REF.equals(tagName)) {
            return LDModelUtils.getUserObjectName(LDModelFactory.ENVIRONMENT);
        }
        if(LDModelFactory.LEARNING_ACTIVITY_REF.equals(tagName)) {
            return LDModelUtils.getUserObjectName(LDModelFactory.LEARNING_ACTIVITY);
        }
        if(LDModelFactory.SUPPORT_ACTIVITY_REF.equals(tagName)) {
            return LDModelUtils.getUserObjectName(LDModelFactory.SUPPORT_ACTIVITY);
        }
        if(LDModelFactory.ACTIVITY_STRUCTURE_REF.equals(tagName)) {
            return LDModelUtils.getUserObjectName(LDModelFactory.ACTIVITY_STRUCTURE);
        }
        if(LDModelFactory.PLAY_REF.equals(tagName)) {
            return LDModelUtils.getUserObjectName(LDModelFactory.PLAY);
        }
        if(LDModelFactory.UOL_HREF.equals(tagName)) {
            return Messages.ConditionsAdapterFactory_6;
        }
        
        if(LDModelFactory.ELSE.equals(tagName)) {
            return Messages.ConditionsAdapterFactory_7;
        }
        if("elseif".equals(tagName)) { //$NON-NLS-1$
            return Messages.ConditionsAdapterFactory_8;
        }
        
        return tagName;
    }

    
    /**
     * Add a new Member type to Show/Hide type.
     */
    public static ILDModelObject addNewMemberType(IShowHideType parentType, String type) {
        ILDModelObject memberType = LDModelFactory.createModelObject(type, parentType.getLDModel());
        
        if(memberType != null) {
            parentType.addMember(memberType);
            parentType.getLDModel().setDirty();
        }
        
        return memberType;
    }

}
