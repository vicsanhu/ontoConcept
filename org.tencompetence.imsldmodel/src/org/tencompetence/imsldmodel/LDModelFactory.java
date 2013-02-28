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
package org.tencompetence.imsldmodel;

import org.tencompetence.imsldmodel.activities.impl.ActivitiesModel;
import org.tencompetence.imsldmodel.activities.impl.ActivityStructureModel;
import org.tencompetence.imsldmodel.activities.impl.ActivityStructureRefModel;
import org.tencompetence.imsldmodel.activities.impl.LearningActivityModel;
import org.tencompetence.imsldmodel.activities.impl.LearningActivityRefModel;
import org.tencompetence.imsldmodel.activities.impl.SupportActivityModel;
import org.tencompetence.imsldmodel.activities.impl.SupportActivityRefModel;
import org.tencompetence.imsldmodel.environments.impl.ConferenceModel;
import org.tencompetence.imsldmodel.environments.impl.EnvironmentModel;
import org.tencompetence.imsldmodel.environments.impl.EnvironmentRefModel;
import org.tencompetence.imsldmodel.environments.impl.EnvironmentsModel;
import org.tencompetence.imsldmodel.environments.impl.IndexSearchModel;
import org.tencompetence.imsldmodel.environments.impl.LearningObjectModel;
import org.tencompetence.imsldmodel.environments.impl.MonitorModel;
import org.tencompetence.imsldmodel.environments.impl.SendMailModel;
import org.tencompetence.imsldmodel.expressions.impl.CalculateType;
import org.tencompetence.imsldmodel.expressions.impl.ChangePropertyValueType;
import org.tencompetence.imsldmodel.expressions.impl.ClassType;
import org.tencompetence.imsldmodel.expressions.impl.CompleteExpressionType;
import org.tencompetence.imsldmodel.expressions.impl.ConditionsType;
import org.tencompetence.imsldmodel.expressions.impl.CurrentDateTimeType;
import org.tencompetence.imsldmodel.expressions.impl.DateTimeActivityStartedType;
import org.tencompetence.imsldmodel.expressions.impl.IsMemberOfRoleType;
import org.tencompetence.imsldmodel.expressions.impl.NoValueType;
import org.tencompetence.imsldmodel.expressions.impl.NotType;
import org.tencompetence.imsldmodel.expressions.impl.OperatorType;
import org.tencompetence.imsldmodel.expressions.impl.PropertyValueType;
import org.tencompetence.imsldmodel.expressions.impl.ShowHideType;
import org.tencompetence.imsldmodel.expressions.impl.TimeUOLStartedType;
import org.tencompetence.imsldmodel.expressions.impl.UOLHrefType;
import org.tencompetence.imsldmodel.expressions.impl.UsersInRoleType;
import org.tencompetence.imsldmodel.expressions.impl.WhenPropertyValueIsSetType;
import org.tencompetence.imsldmodel.method.impl.ActModel;
import org.tencompetence.imsldmodel.method.impl.ActRefModel;
import org.tencompetence.imsldmodel.method.impl.MethodModel;
import org.tencompetence.imsldmodel.method.impl.PlayModel;
import org.tencompetence.imsldmodel.method.impl.PlayRefModel;
import org.tencompetence.imsldmodel.method.impl.RolePartModel;
import org.tencompetence.imsldmodel.method.impl.RolePartRefModel;
import org.tencompetence.imsldmodel.properties.impl.GlobalPersonalPropertyModel;
import org.tencompetence.imsldmodel.properties.impl.GlobalPropertyModel;
import org.tencompetence.imsldmodel.properties.impl.LocalPersonalPropertyModel;
import org.tencompetence.imsldmodel.properties.impl.LocalPropertyModel;
import org.tencompetence.imsldmodel.properties.impl.LocalRolePropertyModel;
import org.tencompetence.imsldmodel.properties.impl.PropertiesModel;
import org.tencompetence.imsldmodel.properties.impl.PropertyGroupModel;
import org.tencompetence.imsldmodel.properties.impl.PropertyRefModel;
import org.tencompetence.imsldmodel.properties.impl.RestrictionType;
import org.tencompetence.imsldmodel.resources.impl.Resource;
import org.tencompetence.imsldmodel.resources.impl.ResourceFile;
import org.tencompetence.imsldmodel.resources.impl.Resources;
import org.tencompetence.imsldmodel.roles.impl.LearnerRoleModel;
import org.tencompetence.imsldmodel.roles.impl.RolesModel;
import org.tencompetence.imsldmodel.roles.impl.StaffRoleModel;
import org.tencompetence.imsldmodel.types.impl.ItemModelType;
import org.tencompetence.imsldmodel.types.impl.ItemRefModel;
import org.tencompetence.imsldmodel.types.impl.ItemType;
import org.tencompetence.imsldmodel.types.impl.NotificationType;




/**
 * LDModelFactory
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDModelFactory.java,v 1.10 2009/11/26 09:16:04 phillipus Exp $
 */
public class LDModelFactory {
    
    public static final String ACT = "act"; //$NON-NLS-1$
    public static final String ACT_REF = "act-ref"; //$NON-NLS-1$
    public static final String ACTS = "acts"; //$NON-NLS-1$
    public static final String ACTIVITIES = "activities"; //$NON-NLS-1$
    public static final String ACTIVITY_DESCRIPTION = "activity-description"; //$NON-NLS-1$
    public static final String ACTIVITY_STRUCTURE = "activity-structure"; //$NON-NLS-1$
    public static final String ACTIVITY_STRUCTURE_REF = "activity-structure-ref"; //$NON-NLS-1$
    public static final String AND = "and"; //$NON-NLS-1$
    public static final String BASE = "base"; //$NON-NLS-1$
    public static final String CALCULATE = "calculate"; //$NON-NLS-1$
    public static final String CHANGE_PROPERTY_VALUE = "change-property-value"; //$NON-NLS-1$
    public static final String CLASS = "class"; //$NON-NLS-1$
    public static final String COMPLETE = "complete"; //$NON-NLS-1$
    public static final String COMPLETE_ACT = "complete-act"; //$NON-NLS-1$
    public static final String COMPLETE_ACTIVITY = "complete-activity"; //$NON-NLS-1$
    public static final String COMPLETE_PLAY = "complete-play"; //$NON-NLS-1$
    public static final String COMPLETE_UOL = "complete-unit-of-learning"; //$NON-NLS-1$
    public static final String COMPONENTS = "components"; //$NON-NLS-1$
    public static final String CONDITIONS = "conditions"; //$NON-NLS-1$
    public static final String CONFERENCE = "conference"; //$NON-NLS-1$
    public static final String CONFERENCE_MANAGER = "conference-manager"; //$NON-NLS-1$
    public static final String CONFERENCE_TYPE = "conference-type"; //$NON-NLS-1$
    public static final String CREATE_NEW = "create-new"; //$NON-NLS-1$
    public static final String CURRENT_DATETIME = "current-datetime"; //$NON-NLS-1$
    public static final String DATATYPE = "datatype"; //$NON-NLS-1$
    public static final String DATETIME_ACTIVITY_STARTED = "datetime-activity-started"; //$NON-NLS-1$
    public static final String DEPENDENCY = "dependency"; //$NON-NLS-1$
    public static final String DIVIDE = "divide"; //$NON-NLS-1$
    public static final String ELSE = "else"; //$NON-NLS-1$
    public static final String EMAIL_DATA = "email-data"; //$NON-NLS-1$
    public static final String EMAIL_PROPERTY_REF = "email-property-ref"; //$NON-NLS-1$
    public static final String ENVIRONMENT = "environment"; //$NON-NLS-1$
    public static final String ENVIRONMENT_REF = "environment-ref"; //$NON-NLS-1$
    public static final String ENVIRONMENTS = "environments"; //$NON-NLS-1$
    public static final String EXISTING = "existing"; //$NON-NLS-1$
    public static final String EXPRESSION = "expression"; //$NON-NLS-1$
    public static final String FEEDBACK_DESCRIPTION = "feedback-description"; //$NON-NLS-1$
    public static final String FILE = "file"; //$NON-NLS-1$
    public static final String GLOBAL_DEFINITION = "global-definition"; //$NON-NLS-1$
    public static final String GLOBAL_PROPERTY = "glob-property"; //$NON-NLS-1$
    public static final String GLOBAL_PERSONAL_PROPERTY = "globpers-property"; //$NON-NLS-1$
    public static final String GREATER_THAN = "greater-than"; //$NON-NLS-1$
    public static final String HIDE = "hide"; //$NON-NLS-1$
    public static final String HREF = "href"; //$NON-NLS-1$
    public static final String IDENTIFIER = "identifier"; //$NON-NLS-1$
    public static final String IDENTIFIER_REF = "identifierref"; //$NON-NLS-1$
    public static final String IF = "if"; //$NON-NLS-1$
    public static final String INFORMATION = "information"; //$NON-NLS-1$
    public static final String ISVISIBLE = "isvisible"; //$NON-NLS-1$
    public static final String ITEM = "item"; //$NON-NLS-1$
    public static final String INDEX = "index"; //$NON-NLS-1$
    public static final String INDEX_CLASS = "index-class"; //$NON-NLS-1$
    public static final String INDEX_ELEMENT = "index-element"; //$NON-NLS-1$
    public static final String INDEX_TYPE_OF_ELEMENT = "index-type-of-element"; //$NON-NLS-1$
    public static final String INDEX_SEARCH = "index-search"; //$NON-NLS-1$
    public static final String INITIAL_VALUE = "initial-value"; //$NON-NLS-1$
    public static final String IS = "is"; //$NON-NLS-1$
    public static final String IS_MEMBER_OF_ROLE = "is-member-of-role"; //$NON-NLS-1$
    public static final String IS_NOT = "is-not"; //$NON-NLS-1$
    public static final String ITEM_REF = "item-ref"; //$NON-NLS-1$
    public static final String KNOWLEDGE_OBJECT = "knowledge-object"; //$NON-NLS-1$
    public static final String LANGSTRING = "langstring"; //$NON-NLS-1$
    public static final String LEARNER = "learner"; //$NON-NLS-1$
    public static final String LEARNING_ACTIVITY = "learning-activity"; //$NON-NLS-1$
    public static final String LEARNING_ACTIVITY_REF = "learning-activity-ref"; //$NON-NLS-1$
    public static final String LEARNING_DESIGN = "learning-design"; //$NON-NLS-1$
    public static final String LEARNING_OBJECT = "learning-object"; //$NON-NLS-1$
    public static final String LEARNING_OBJECT_KNOWLEDGE_OBJECT = "learning-object-knowledge"; //$NON-NLS-1$
    public static final String LEARNING_OBJECT_TOOL_OBJECT = "learning-object-tool"; //$NON-NLS-1$
    public static final String LEARNING_OBJECT_TEST_OBJECT = "learning-object-test"; //$NON-NLS-1$
    public static final String LEARNING_OBJECTIVES = "learning-objectives"; //$NON-NLS-1$
    public static final String LESS_THAN = "less-than"; //$NON-NLS-1$
    public static final String LEVEL = "level"; //$NON-NLS-1$
    public static final String LOCAL_PROPERTY = "loc-property"; //$NON-NLS-1$
    public static final String LOCAL_PERSONAL_PROPERTY = "locpers-property"; //$NON-NLS-1$
    public static final String LOCAL_ROLE_PROPERTY = "locrole-property"; //$NON-NLS-1$
    public static final String LOM = "lom"; //$NON-NLS-1$
    public static final String MATCH_PERSONS = "match-persons"; //$NON-NLS-1$
    public static final String METADATA = "metadata"; //$NON-NLS-1$
    public static final String METHOD = "method"; //$NON-NLS-1$
    public static final String MIN_PERSONS = "min-persons"; //$NON-NLS-1$
    public static final String MAX_PERSONS = "max-persons"; //$NON-NLS-1$
    public static final String MODERATOR = "moderator"; //$NON-NLS-1$
    public static final String MONITOR = "monitor"; //$NON-NLS-1$
    public static final String MULTIPLY = "multiply"; //$NON-NLS-1$
    public static final String NOT = "not"; //$NON-NLS-1$
    public static final String NOTIFICATION = "notification"; //$NON-NLS-1$
    public static final String NO_VALUE = "no-value"; //$NON-NLS-1$
    public static final String NUMBER_TO_SELECT = "number-to-select"; //$NON-NLS-1$
    public static final String OBSERVER = "observer"; //$NON-NLS-1$
    public static final String ON_COMPLETION = "on-completion"; //$NON-NLS-1$
    public static final String OR = "or"; //$NON-NLS-1$
    public static final String PARAMETERS = "parameters"; //$NON-NLS-1$
    public static final String PARTICIPANT = "participant"; //$NON-NLS-1$
    public static final String PLAY = "play"; //$NON-NLS-1$
    public static final String PLAY_REF = "play-ref"; //$NON-NLS-1$
    public static final String PLAYS = "plays"; //$NON-NLS-1$
    public static final String PREREQUISITES = "prerequisites"; //$NON-NLS-1$
    public static final String PROPERTIES = "properties"; //$NON-NLS-1$
    public static final String PROPERTY_GROUP = "property-group"; //$NON-NLS-1$
    public static final String PROPERTY_REF = "property-ref"; //$NON-NLS-1$
    public static final String PROPERTY_GROUP_REF = "property-group-ref"; //$NON-NLS-1$
    public static final String PROPERTY_VALUE = "property-value"; //$NON-NLS-1$
    public static final String RESOURCES = "resources"; //$NON-NLS-1$
    public static final String RESOURCE = "resource"; //$NON-NLS-1$
    public static final String RESTRICTION = "restriction"; //$NON-NLS-1$
    public static final String RESTRICTION_TYPE = "restriction-type"; //$NON-NLS-1$
    public static final String ROLES = "roles"; //$NON-NLS-1$
    public static final String ROLE_PART = "role-part"; //$NON-NLS-1$
    public static final String ROLE_REF = "role-ref"; //$NON-NLS-1$
    public static final String ROLEPART_REF = "role-part-ref"; //$NON-NLS-1$
    public static final String REF = "ref"; //$NON-NLS-1$
    public static final String SEARCH = "search"; //$NON-NLS-1$
    public static final String SEARCH_TYPE = "search-type"; //$NON-NLS-1$
    public static final String SELECT = "select"; //$NON-NLS-1$
    public static final String SEND_MAIL = "send-mail"; //$NON-NLS-1$
    public static final String SEQUENCE_USED = "sequence-used"; //$NON-NLS-1$
    public static final String SERVICE = "service"; //$NON-NLS-1$
    public static final String SHOW = "show"; //$NON-NLS-1$
    public static final String SORT = "sort"; //$NON-NLS-1$
    public static final String STAFF = "staff"; //$NON-NLS-1$
    public static final String STRUCTURE_TYPE = "structure-type"; //$NON-NLS-1$
    public static final String SUBJECT = "subject"; //$NON-NLS-1$
    public static final String SUBTRACT = "subtract"; //$NON-NLS-1$
    public static final String SUM = "sum"; //$NON-NLS-1$
    public static final String SUPPORT_ACTIVITY = "support-activity"; //$NON-NLS-1$
    public static final String SUPPORT_ACTIVITY_REF = "support-activity-ref"; //$NON-NLS-1$
    public static final String TEST_OBJECT = "test-object"; //$NON-NLS-1$
    public static final String THEN = "then"; //$NON-NLS-1$
    public static final String TIME_LIMIT = "time-limit"; //$NON-NLS-1$
    public static final String TIME_UOL_STARTED = "time-unit-of-learning-started"; //$NON-NLS-1$
    public static final String TOOL_OBJECT = "tool-object"; //$NON-NLS-1$
    public static final String TYPE = "type"; //$NON-NLS-1$
    public static final String TITLE = "title"; //$NON-NLS-1$
    public static final String UOL_HREF = "unit-of-learning-href"; //$NON-NLS-1$
    public static final String UOL_URI = "unit-of-learning-uri"; //$NON-NLS-1$
    public static final String URI = "uri"; //$NON-NLS-1$
    public static final String USER_CHOICE = "user-choice"; //$NON-NLS-1$
    public static final String USERNAME_PROPERTY_REF = "username-property-ref"; //$NON-NLS-1$
    public static final String USERS_IN_ROLE = "users-in-role"; //$NON-NLS-1$
    public static final String VERSION = "version"; //$NON-NLS-1$
    public static final String WHEN_CONDITION_TRUE = "when-condition-true"; //$NON-NLS-1$
    public static final String WHEN_LAST_ACT_COMPLETED = "when-last-act-completed"; //$NON-NLS-1$
    public static final String WHEN_PLAY_COMPLETED = "when-play-completed"; //$NON-NLS-1$
    public static final String WHEN_PROPERTY_VALUE_IS_SET = "when-property-value-is-set"; //$NON-NLS-1$
    public static final String WHEN_ROLE_PART_COMPLETED = "when-role-part-completed"; //$NON-NLS-1$
    public static final String WITH_CONTROL = "with-control"; //$NON-NLS-1$
    

    
    /**
     * factory method for creating new LD objects
     * @param name XML tag name for object
     * @param ldModel Parent LD Model
     * @return A new LD Object
     */
    public static ILDModelObject createModelObject(String name, ILDModel ldModel) {
        if(name == null || ldModel == null) {
            return null;
        }
        
        // Top level
        if(name.equals(LEARNING_OBJECTIVES)) {
            return new ItemModelType(ldModel, name);
        }
        else if(name.equals(PREREQUISITES)) {
            return new ItemModelType(ldModel, name);
        }
        
        // Roles
        else if(name.equals(ROLES)) {
            return new RolesModel(ldModel);
        }
        else if(name.equals(LEARNER)) {
            return new LearnerRoleModel(ldModel);
        }
        else if(name.equals(STAFF)) {
            return new StaffRoleModel(ldModel);
        }
        
        // Environments
        else if(name.equals(ENVIRONMENTS)) {
            return new EnvironmentsModel(ldModel);
        }
        else if(name.equals(ENVIRONMENT)) {
            return new EnvironmentModel(ldModel);
        }
        else if(name.equals(ENVIRONMENT_REF)) {
            return new EnvironmentRefModel(ldModel);
        }
        else if(name.equals(LEARNING_OBJECT)) {
            return new LearningObjectModel(ldModel);
        }
        else if(name.equals(SEND_MAIL)) {
            return new SendMailModel(ldModel);
        }
        else if(name.equals(CONFERENCE)) {
            return new ConferenceModel(ldModel);
        }
        else if(name.equals(MONITOR)) {
            return new MonitorModel(ldModel);
        }
        else if(name.equals(INDEX_SEARCH)) {
            return new IndexSearchModel(ldModel);
        }
        
        // Method
        else if(name.equals(METHOD)) {
            return new MethodModel(ldModel);
        }
        else if(name.equals(PLAY)) {
            return new PlayModel(ldModel);
        }
        else if(name.equals(ACT)) {
            return new ActModel(ldModel);
        }
        else if(name.equals(ROLE_PART)) {
            return new RolePartModel(ldModel);
        }
        else if(name.equals(PLAY_REF)) {
            return new PlayRefModel(ldModel);
        }
        else if(name.equals(ACT_REF)) {
            return new ActRefModel(ldModel);
        }
        else if(name.equals(ROLEPART_REF)) {
            return new RolePartRefModel(ldModel);
        }
        
        // Activities
        else if(name.equals(ACTIVITIES)) {
            return new ActivitiesModel(ldModel);
        }
        else if(name.equals(LEARNING_ACTIVITY)) {
            return new LearningActivityModel(ldModel);
        }
        else if(name.equals(SUPPORT_ACTIVITY)) {
            return new SupportActivityModel(ldModel);
        }
        else if(name.equals(ACTIVITY_STRUCTURE)) {
            return new ActivityStructureModel(ldModel);
        }
        else if(name.equals(LEARNING_ACTIVITY_REF)) {
            return new LearningActivityRefModel(ldModel);
        }
        else if(name.equals(SUPPORT_ACTIVITY_REF)) {
            return new SupportActivityRefModel(ldModel);
        }
        else if(name.equals(ACTIVITY_STRUCTURE_REF)) {
            return new ActivityStructureRefModel(ldModel);
        }
        
        // Properties
        else if(name.equals(PROPERTIES)) {
            return new PropertiesModel(ldModel);
        }
        else if(name.equals(LOCAL_PROPERTY)) {
            return new LocalPropertyModel(ldModel);
        }
        else if(name.equals(LOCAL_PERSONAL_PROPERTY)) {
            return new LocalPersonalPropertyModel(ldModel);
        }
        else if(name.equals(LOCAL_ROLE_PROPERTY)) {
            return new LocalRolePropertyModel(ldModel);
        }
        else if(name.equals(GLOBAL_PROPERTY)) {
            return new GlobalPropertyModel(ldModel);
        }
        else if(name.equals(GLOBAL_PERSONAL_PROPERTY)) {
            return new GlobalPersonalPropertyModel(ldModel);
        }
        else if(name.equals(PROPERTY_GROUP)) {
            return new PropertyGroupModel(ldModel);
        }
        
        // Expression Types
        else if(name.equals(CONDITIONS)) {
            return new ConditionsType(ldModel);
        }
        else if(name.equals(PROPERTY_REF)) {
            return new PropertyRefModel(ldModel);
        }
        else if(name.equals(PROPERTY_VALUE)) {
            return new PropertyValueType(ldModel);
        }
        else if(name.equals(IS_MEMBER_OF_ROLE)) {
            return new IsMemberOfRoleType(ldModel);
        }
        else if(name.equals(IS)) {
            return new CalculateType(ldModel, name);
        }
        else if(name.equals(IS_NOT)) {
            return new CalculateType(ldModel, name);
        }
        else if(name.equals(AND)) {
            return new OperatorType(ldModel, name);
        }
        else if(name.equals(OR)) {
            return new OperatorType(ldModel, name);
        }
        else if(name.equals(SUM)) {
            return new CalculateType(ldModel, name);
        }
        else if(name.equals(SUBTRACT)) {
            return new CalculateType(ldModel, name);
        }
        else if(name.equals(MULTIPLY)) {
            return new CalculateType(ldModel, name);
        }
        else if(name.equals(DIVIDE)) {
            return new CalculateType(ldModel, name);
        }
        else if(name.equals(GREATER_THAN)) {
            return new CalculateType(ldModel, name);
        }
        else if(name.equals(LESS_THAN)) {
            return new CalculateType(ldModel, name);
        }
        else if(name.equals(USERS_IN_ROLE)) {
            return new UsersInRoleType(ldModel);
        }
        else if(name.equals(NO_VALUE)) {
            return new NoValueType(ldModel);
        }
        else if(name.equals(TIME_UOL_STARTED)) {
            return new TimeUOLStartedType(ldModel);
        }
        else if(name.equals(DATETIME_ACTIVITY_STARTED)) {
            return new DateTimeActivityStartedType(ldModel);
        }
        else if(name.equals(CURRENT_DATETIME)) {
            return new CurrentDateTimeType(ldModel);
        }
        else if(name.equals(COMPLETE)) {
            return new CompleteExpressionType(ldModel);
        }
        else if(name.equals(NOT)) {
            return new NotType(ldModel);
        }
        else if(name.equals(UOL_HREF)) {
            return new UOLHrefType(ldModel);
        }
        else if(name.equals(SHOW)) {
            return new ShowHideType(ldModel, name);
        }
        else if(name.equals(HIDE)) {
            return new ShowHideType(ldModel, name);
        }
        else if(name.equals(CHANGE_PROPERTY_VALUE)) {
            return new ChangePropertyValueType(ldModel);
        }
        else if(name.equals(NOTIFICATION)) {
            return new NotificationType(ldModel);
        }
        else if(name.equals(CLASS)) {
            return new ClassType(ldModel);
        }
        else if(name.equals(ITEM_REF)) {
            return new ItemRefModel(ldModel);
        }
        else if(name.equals(WHEN_PROPERTY_VALUE_IS_SET)) {
            return new WhenPropertyValueIsSetType(ldModel);
        }
        else if(name.equals(RESTRICTION)) {
            return new RestrictionType(ldModel);
        }
        
        // Types
        else if(name.equals(ITEM)) {
            return new ItemType(ldModel);
        }
        else if(name.equals(RESOURCES)) {
            return new Resources(ldModel);
        }
        else if(name.equals(RESOURCE)) {
            return new Resource(ldModel);
        }
        else if(name.equals(FILE)) {
            return new ResourceFile(ldModel);
        }
        
        return null;
    }
    
 }
