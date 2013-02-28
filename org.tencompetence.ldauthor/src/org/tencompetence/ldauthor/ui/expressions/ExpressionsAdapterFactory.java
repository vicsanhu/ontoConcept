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
package org.tencompetence.ldauthor.ui.expressions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityType;
import org.tencompetence.imsldmodel.expressions.ICalculateType;
import org.tencompetence.imsldmodel.expressions.ICompleteExpressionType;
import org.tencompetence.imsldmodel.expressions.IDateTimeActivityStartedType;
import org.tencompetence.imsldmodel.expressions.IExpressionChoice;
import org.tencompetence.imsldmodel.expressions.IExpressionLeafType;
import org.tencompetence.imsldmodel.expressions.IIsMemberOfRoleType;
import org.tencompetence.imsldmodel.expressions.INoValueType;
import org.tencompetence.imsldmodel.expressions.INotType;
import org.tencompetence.imsldmodel.expressions.IOperatorType;
import org.tencompetence.imsldmodel.expressions.IPropertyValueType;
import org.tencompetence.imsldmodel.expressions.ITimeUOLStartedType;
import org.tencompetence.imsldmodel.expressions.IUOLHrefType;
import org.tencompetence.imsldmodel.expressions.IUsersInRoleType;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.properties.IPropertyRefModel;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;


/**
 * Factory class for adapting Expression model types and widgets
 * 
 * @author Phillip Beauvoir
 * @version $Id: ExpressionsAdapterFactory.java,v 1.11 2009/06/14 08:24:42 phillipus Exp $
 */
public class ExpressionsAdapterFactory {
    
    /*
     * Plus/Minus button height/width - different on a Mac!
     */
    public static final int BUTTON_SIZE = Platform.getOS().equals(Platform.OS_MACOSX) ? 30 : 20;
    
    /*
     * Horizintal gaps between controls
     */
    public static final int HORIZONTAL_SPACING = Platform.getOS().equals(Platform.OS_MACOSX) ? 0 : 5;
    
    /*
     * The types of value an expression type can have
     */
    public static final int VALUE_TYPE_NONE = 0;
    public static final int VALUE_TYPE_LIST = 1;
    public static final int VALUE_TYPE_TEXT = 2;
    
    /*
     * Expression Choices for menu items. null denotes a menu separator
     */
    public static String[] CALCULATE_CHOICES = {
            LDModelFactory.IS_MEMBER_OF_ROLE,
            LDModelFactory.USERS_IN_ROLE,
            LDModelFactory.NO_VALUE,
            LDModelFactory.TIME_UOL_STARTED,
            LDModelFactory.DATETIME_ACTIVITY_STARTED,
            LDModelFactory.CURRENT_DATETIME,
            LDModelFactory.COMPLETE,
            LDModelFactory.COMPLETE_UOL,
            null,
            LDModelFactory.PROPERTY_REF,
            LDModelFactory.PROPERTY_VALUE,
            null,
            LDModelFactory.AND,
            LDModelFactory.OR,
            LDModelFactory.NOT,
            null,
            LDModelFactory.IS,
            LDModelFactory.IS_NOT,
            null,
            LDModelFactory.SUM,
            LDModelFactory.SUBTRACT,
            LDModelFactory.MULTIPLY,
            LDModelFactory.DIVIDE,
            LDModelFactory.GREATER_THAN,
            LDModelFactory.LESS_THAN,
        };
    
    public static String[] EXPRESSION_CHOICES = {
            LDModelFactory.IS_MEMBER_OF_ROLE,
            LDModelFactory.USERS_IN_ROLE,
            LDModelFactory.NO_VALUE,
            LDModelFactory.TIME_UOL_STARTED,
            LDModelFactory.DATETIME_ACTIVITY_STARTED,
            LDModelFactory.CURRENT_DATETIME,
            LDModelFactory.COMPLETE,
            LDModelFactory.COMPLETE_UOL,
            null,
            null,
            LDModelFactory.AND,
            LDModelFactory.OR,
            LDModelFactory.NOT,
            null,
            LDModelFactory.IS,
            LDModelFactory.IS_NOT,
            null,
            LDModelFactory.SUM,
            LDModelFactory.SUBTRACT,
            LDModelFactory.MULTIPLY,
            LDModelFactory.DIVIDE,
            LDModelFactory.GREATER_THAN,
            LDModelFactory.LESS_THAN,
        };
    
    /**
     * @param tagName
     * @return Friendly display name for xml tag
     */
    public static String getFriendlyName(String tagName) {
        if(tagName == null) {
            return ""; //$NON-NLS-1$
        }
        
        if(LDModelFactory.IS_MEMBER_OF_ROLE.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_0;
        }
        if(LDModelFactory.USERS_IN_ROLE.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_1;
        }
        if(LDModelFactory.NO_VALUE.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_2;
        }
        if(LDModelFactory.TIME_UOL_STARTED.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_3;
        }
        if(LDModelFactory.DATETIME_ACTIVITY_STARTED.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_4;
        }
        if(LDModelFactory.CURRENT_DATETIME.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_5;
        }
        if(LDModelFactory.COMPLETE.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_6;
        }
        if(LDModelFactory.COMPLETE_UOL.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_20;
        }
        if(LDModelFactory.AND.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_7;
        }
        if(LDModelFactory.OR.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_8;
        }
        if(LDModelFactory.NOT.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_9;
        }
        if(LDModelFactory.IS.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_10;
        }
        if(LDModelFactory.IS_NOT.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_11;
        }
        if(LDModelFactory.MULTIPLY.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_12;
        }
        if(LDModelFactory.DIVIDE.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_13;
        }
        if(LDModelFactory.SUM.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_14;
        }
        if(LDModelFactory.SUBTRACT.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_15;
        }
        if(LDModelFactory.LESS_THAN.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_16;
        }
        if(LDModelFactory.GREATER_THAN.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_17;
        }
        if(LDModelFactory.PROPERTY_REF.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_18;
        }
        if(LDModelFactory.PROPERTY_VALUE.equals(tagName)) {
            return Messages.ExpressionsAdapterFactory_19;
        }
        
        return tagName;
    }
    
    /**
     * @param ldModel
     * @param tagName
     * @return A list of objects related to the tag member type
     */
    public static Object[] getValueList(ILDModel ldModel, String tagName) {
        if(LDModelFactory.IS_MEMBER_OF_ROLE.equals(tagName) || LDModelFactory.USERS_IN_ROLE.equals(tagName)) {
            return ldModel.getRolesModel().getOrderedRoles().toArray();
        }
        if(LDModelFactory.NO_VALUE.equals(tagName) || LDModelFactory.PROPERTY_REF.equals(tagName) ||
                LDModelFactory.PROPERTY_REF.equals(tagName)) {
            return ldModel.getPropertiesModel().getPropertyTypes().toArray();
        }
        if(LDModelFactory.DATETIME_ACTIVITY_STARTED.equals(tagName)) {
            return ldModel.getActivitiesModel().getAllActivities().toArray();
        }
        if(LDModelFactory.COMPLETE.equals(tagName)) {
            List<ILDModelObject> list = new ArrayList<ILDModelObject>();
            
            // Plays
            for(ILDModelObject play : ldModel.getMethodModel().getPlaysModel().getChildren()) {
                list.add(play);
                // Acts
                for(ILDModelObject act : ((IPlayModel)play).getActsModel().getChildren()) {
                    list.add(act);
                    // Role Parts
                    for(ILDModelObject rolePart : ((IActModel)act).getRolePartsModel().getChildren()) {
                        list.add(rolePart);
                    }
                }
            }
            
            // Activities
            for(ILDModelObject activity : ldModel.getActivitiesModel().getAllActivities()) {
                list.add(activity);
            }
            
            return list.toArray();
        }
        
        return new Object[0];
    }
    
    /**
     * @return Type of value  
     */
    public static int getValueType(ILDModelObject expessionType) {
        // Special case for Complete UOL
        if(expessionType instanceof ICompleteExpressionType) {
            return (((ICompleteExpressionType)expessionType).getComponentReference() instanceof IUOLHrefType) ?
                    VALUE_TYPE_TEXT : VALUE_TYPE_LIST;
        }

        String tagName = expessionType.getTagName();
        
        if(LDModelFactory.TIME_UOL_STARTED.equals(tagName)) {
            return VALUE_TYPE_TEXT;
        }
        if(LDModelFactory.PROPERTY_VALUE.equals(tagName)) {
            return VALUE_TYPE_TEXT;
        }
        if(LDModelFactory.CURRENT_DATETIME.equals(tagName)) {
            return VALUE_TYPE_NONE;
        }
        
        return VALUE_TYPE_LIST;
    }
    
    /**
     * Return a value of a given Expression type
     * @param expressionType
     * @return
     */
    public static Object getValue(ILDModelObject expressionType) {
        // Is member of Role
        if(expressionType instanceof IIsMemberOfRoleType) {
            IIsMemberOfRoleType valueType = (IIsMemberOfRoleType)expressionType; 
            return valueType.getLDModelObject();
        }
        // Users in Role
        else if(expressionType instanceof IUsersInRoleType) {
            IUsersInRoleType valueType = (IUsersInRoleType)expressionType; 
            return valueType.getRoleRef();
        }
        // No Value
        else if(expressionType instanceof INoValueType) {
            INoValueType valueType = (INoValueType)expressionType; 
            return valueType.getPropertyRef();
        }
        // Time UOL started
        else if(expressionType instanceof ITimeUOLStartedType) {
            ITimeUOLStartedType valueType = (ITimeUOLStartedType)expressionType; 
            return valueType.getURI();
        }
        // Datetime Activity started
        else if(expressionType instanceof IDateTimeActivityStartedType) {
            IDateTimeActivityStartedType valueType = (IDateTimeActivityStartedType)expressionType; 
            return valueType.getLDModelObject();
        }
        // Is Completed
        else if(expressionType instanceof ICompleteExpressionType) {
            ICompleteExpressionType valueType = (ICompleteExpressionType)expressionType;
            ILDModelObject ref = valueType.getComponentReference();
            if(ref instanceof ILDModelObjectReference) {
                return ((ILDModelObjectReference)ref).getLDModelObject();
            }
            else if(ref instanceof IUOLHrefType) {
                return ((IUOLHrefType)ref).getHref();
            }
        }
        // Property Ref
        else if(expressionType instanceof IPropertyRefModel) {
            IPropertyRefModel valueType = (IPropertyRefModel)expressionType; 
            return valueType.getLDModelObject();
        }
        // Property value
        else if(expressionType instanceof IPropertyValueType) {
            IPropertyValueType valueType = (IPropertyValueType)expressionType; 
            return valueType.getValue();
        }
        
        return null;
    }
    
    /**
     * Add a new Member type.
     * Use this method so that we can do some magic and avoid notifications...and stuff
     * @param parentType
     * @param type
     * @return
     */
    public static ILDModelObject addNewMemberType(IExpressionChoice parentType, String type) {
        ILDModelObject memberType = null;
        
        // Special case for Complete UOL type
        if(LDModelFactory.COMPLETE_UOL.equals(type)) {
            memberType = LDModelFactory.createModelObject(LDModelFactory.COMPLETE, parentType.getLDModel());
            // This will set the type to UOL HREF
            ((ICompleteExpressionType)memberType).setUOLHref("");  //$NON-NLS-1$
        }
        else {
            memberType = LDModelFactory.createModelObject(type, parentType.getLDModel());
        }
        
        // Can't have an empty "Not" type
        if(memberType instanceof INotType) {
            ((INotType)memberType).addExpressionMemberType(LDModelFactory.OR);
        }

        if(memberType != null) {
            parentType.addExpressionMemberType(memberType);
            parentType.getLDModel().setDirty();
        }
        
        return memberType;
    }
    
    /**
     * Update the Expression Type and value
     * @param expressionType
     * @param value
     */
    public static void updateExpressionValue(ILDModelObject expressionType, Object value) {
        // Is member of role
        if(expressionType instanceof IIsMemberOfRoleType && value instanceof IRoleModel) {
            IIsMemberOfRoleType type = (IIsMemberOfRoleType)expressionType;
            IRoleModel role = (IRoleModel)value;
            type.setReferenceIdentifer(role.getIdentifier());
        }
        
        // Users in Role
        else if(expressionType instanceof IUsersInRoleType && value instanceof IRoleModel) {
            IUsersInRoleType type = (IUsersInRoleType)expressionType;
            type.setRoleRef((IRoleModel)value);
        }
        
        // No value
        else if(expressionType instanceof INoValueType && value instanceof IPropertyTypeModel) {
            INoValueType type = (INoValueType)expressionType;
            type.setPropertyRef((IPropertyTypeModel)value);
        }
        
        // Time UOL started
        else if(expressionType instanceof ITimeUOLStartedType && value instanceof String) {
            ITimeUOLStartedType type = (ITimeUOLStartedType)expressionType;
            type.setURI((String)value);
        }
        
        // Datetime Activity started
        else if(expressionType instanceof IDateTimeActivityStartedType && value instanceof IActivityType) {
            IDateTimeActivityStartedType type = (IDateTimeActivityStartedType)expressionType;
            IActivityType activity = (IActivityType)value;
            type.setReferenceIdentifer(activity.getIdentifier());
        }
        
        // Complete
        else if(expressionType instanceof ICompleteExpressionType) {
            ICompleteExpressionType type = (ICompleteExpressionType)expressionType;
            if(value instanceof String) {
                // UOL Completed Href
                type.setUOLHref((String)value);
            }
            else {
                type.setComponent((ILDModelObject)value);
            }
        }
        // Property Ref
        else if(expressionType instanceof IPropertyRefModel && value instanceof IPropertyTypeModel) {
            IPropertyRefModel type = (IPropertyRefModel)expressionType; 
            IPropertyTypeModel property = (IPropertyTypeModel)value;
            type.setReferenceIdentifer((property.getIdentifier()));
        }
        // Property value
        else if(expressionType instanceof IPropertyValueType && value instanceof String) {
            IPropertyValueType type = (IPropertyValueType)expressionType;
            type.setChoice(IPropertyValueType.CHOICE_NONE);
            type.setValue((String)value);
        }
    }
    
    /**
     * @param object
     * @return The correct type of editing panel
     */
    public static IExpressionComposite createExpressionComposite(IExpressionChoice parentType, ILDModelObject memberType, Composite parent) {
        IExpressionComposite composite = null;
        
        /*
         * A "Not" type requires that we dig in and find the underlying child type
         */
        if(memberType instanceof INotType) {
            INotType notType = (INotType)memberType;
            List<ILDModelObject> members = notType.getExpressionMemberTypes();
            
            // Oops, no members
            if(members.isEmpty()) {
                return null;
            }
            
            // One child member
            ILDModelObject child = members.get(0);
            
            composite = createExpressionComposite(child, parent);
        }
        else {
            composite = createExpressionComposite(memberType, parent);
        }
        
        if(composite != null) {
            composite.setExpressionType(parentType, memberType);
        }
        
        return composite;
    }
    
    private static IExpressionComposite createExpressionComposite(ILDModelObject memberType, Composite parent) {
        if(memberType instanceof IOperatorType) {
            return new OperatorComposite(parent);
        }
        
        if(memberType instanceof ICalculateType) {
            return new CalculateComposite(parent);
        }
        
        if(memberType instanceof IExpressionLeafType) {
            return new ExpressionLeafNodeComposite(parent);
        }
        
        return null;
    }
}
