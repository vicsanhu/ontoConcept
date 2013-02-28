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
package org.tencompetence.ldauthor.ldmodel.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.environments.IConferenceModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IIndexSearchModel;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.environments.IMonitorModel;
import org.tencompetence.imsldmodel.environments.ISendMailModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IActsModel;
import org.tencompetence.imsldmodel.method.ICompleteActType;
import org.tencompetence.imsldmodel.method.ICompleteUOLType;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.method.IPlaysModel;
import org.tencompetence.imsldmodel.resources.IResourceFileModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.types.IEmailDataType;
import org.tencompetence.imsldmodel.types.IItemModelType;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorViewModelAdapter;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Checks the LD for missing components
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDChecker.java,v 1.25 2009/07/03 11:13:16 phillipus Exp $
 */
public class LDChecker {
    
    private IReCourseLDModel fLDModel;
    
    private List<ILDCheckerItem> fMainList;

    public LDChecker(IReCourseLDModel ldModel) {
        fLDModel = ldModel;
    }
    
    /**
     * Check all LD Objects.
     * @return A List of Checker Items
     */
    public List<ILDCheckerItem> check() {
        fMainList = new ArrayList<ILDCheckerItem>();
        
        // LD
        checkLD();
        
        // Plays
        checkPlays();
        
        // Activities
        checkActivities();
        
        // Environments
        checkEnvironments();
        
        // Roles
        checkRoles();
        
        // Resources
        checkResources();
        
        return fMainList;
    }
    
    private void checkLD() {
        // Ensure correct level
        fLDModel.ensureIsCorrectLevel();
        
        CheckerCategory category = new CheckerCategory(fLDModel, LDModelUtils.DEFAULT_OVERVIEW_NAME);
        fMainList.add(category);
        
        // Must have valid URI
        String uri = fLDModel.getURI();
        if(!StringUtils.isSetAfterTrim(uri)) {
            ErrorCheckItem item = new ErrorCheckItem(fLDModel, Messages.LDChecker_0);
            category.add(item);
        }
        
        // Learning Objectives Items
        checkItems(fLDModel.getLearningObjectives(),
                Messages.LDChecker_41, 
                fLDModel.getLearningObjectives(),
                category,
                Messages.LDChecker_40);
        
        // Prerequisites Items
        checkItems(fLDModel.getPrerequisites(),
                Messages.LDChecker_42,
                fLDModel.getPrerequisites(),
                category,
                Messages.LDChecker_39);

        // If Completion Rule is "When Play(s) completed", must have at least one Play
        ICompleteUOLType completeType = fLDModel.getMethodModel().getCompleteUOLType();
        if(completeType.getChoice() == 1 && completeType.getPlays().size() == 0) {
            ErrorCheckItem item = new ErrorCheckItem(completeType, Messages.LDChecker_1 + " " //$NON-NLS-1$
                    + LDModelUtils.getUserObjectName(LDModelFactory.PLAY) + Messages.LDChecker_2);
            category.add(item);
        }
        
        // Completion Rules Items
        checkItems(fLDModel.getMethodModel().getOnCompletionType().getFeedBackDescription(),
                Messages.LDChecker_43,
                fLDModel.getMethodModel().getOnCompletionType().getFeedBackDescription(),
                category,
                Messages.LDChecker_3);
    }
    
    private void checkPlays() {
        // Plays
        IPlaysModel playsModel = fLDModel.getMethodModel().getPlaysModel();
        List<ILDModelObject> plays = playsModel.getChildren();
        
        CheckerCategory category = new CheckerCategory(fLDModel.getMethodModel(), LDModelUtils.getUserObjectName(LDModelFactory.METHOD));
        fMainList.add(category);
        
        // At least one Play
        if(plays.size() == 0) {
            ErrorCheckItem item = new ErrorCheckItem(fLDModel.getMethodModel(), Messages.LDChecker_4 + " " //$NON-NLS-1$
                    + LDModelUtils.getUserObjectName(LDModelFactory.PLAY)
                    + Messages.LDChecker_5  + " " //$NON-NLS-1$
                    + LDModelUtils.getUserObjectName(LDModelFactory.METHOD));
            category.add(item);
        }
        
        for(ILDModelObject modelObject : plays) {
            checkPlay((IPlayModel)modelObject, category);
        }
    }
    
    private void checkPlay(IPlayModel playModel, CheckerCategory parentCategory) {
        // Acts
        IActsModel actsModel = playModel.getActsModel();
        List<ILDModelObject> acts = actsModel.getChildren();
        
        CheckerCategory category = new CheckerCategory(playModel, playModel.getTitle());
        parentCategory.add(category);
        
        // At least one Act
        if(acts.size() == 0) {
            parentCategory.add(category);
            ErrorCheckItem item = new ErrorCheckItem(fLDModel.getMethodModel(), Messages.LDChecker_6 + " " //$NON-NLS-1$
                    + LDModelUtils.getUserObjectName(LDModelFactory.ACT)
                    + Messages.LDChecker_7 + " " //$NON-NLS-1$
                    + LDModelUtils.getUserObjectName(LDModelFactory.PLAY));
            category.add(item);
        }
        
        // Items
        checkItems(playModel,
                null, 
                playModel.getOnCompletionType().getFeedBackDescription(),
                category,
                Messages.LDChecker_8);

        for(ILDModelObject modelObject : playModel.getActsModel().getChildren()) {
            checkAct((IActModel)modelObject, category);
        }
    }

    private void checkAct(IActModel actModel, CheckerCategory parentCategory) {
        CheckerCategory category = new CheckerCategory(actModel, actModel.getTitle());
        parentCategory.add(category);
        
        // If Completion Rule is when RolePart completed, must have at least one RolePart
        ICompleteActType completeType = actModel.getCompleteActType();
        if(completeType.getChoice() == 1 && completeType.getRoleParts().size() == 0) {
            ErrorCheckItem item = new ErrorCheckItem(actModel, Messages.LDChecker_9 + " " //$NON-NLS-1$
                    + LDModelUtils.getUserObjectName(LDModelFactory.ROLE_PART)
                    + Messages.LDChecker_10);
            category.add(item);
        }
        
        // At least one RolePart
        if(actModel.getRolePartsModel().getChildren().size() == 0) {
            ErrorCheckItem item = new ErrorCheckItem(fLDModel.getMethodModel(), Messages.LDChecker_11 + " " //$NON-NLS-1$
                    + LDModelUtils.getUserObjectName(LDModelFactory.ROLE_PART)
                    + Messages.LDChecker_12 + " " //$NON-NLS-1$
                    + LDModelUtils.getUserObjectName(LDModelFactory.ACT));
            category.add(item);
        }
        
        // Items
        checkItems(actModel,
                null, 
                actModel.getOnCompletionType().getFeedBackDescription(),
                category,
                Messages.LDChecker_8);
    }

    private void checkActivities() {
        CheckerCategory category = new CheckerCategory(fLDModel.getActivitiesModel(), LDModelUtils.getUserObjectName(LDModelFactory.ACTIVITIES));
        fMainList.add(category);
        
        for(ILDModelObject modelObject : fLDModel.getActivitiesModel().getLearningActivitiesModel().getChildren()) {
            checkActivity((IActivityModel)modelObject, category);
        }
        
        for(ILDModelObject modelObject : fLDModel.getActivitiesModel().getSupportActivitiesModel().getChildren()) {
            checkActivity((IActivityModel)modelObject, category);
        }
        
        for(ILDModelObject modelObject : fLDModel.getActivitiesModel().getActivityStructuresModel().getChildren()) {
            checkActivityStructure((IActivityStructureModel)modelObject, category);
        }
    }
    
    private void checkActivity(IActivityModel activityModel, CheckerCategory parentCategory) {
        CheckerCategory category = new CheckerCategory(activityModel, activityModel.getTitle());
        parentCategory.add(category);

        IItemModelType itemModel = activityModel.getDescriptionModel();
        if(itemModel.getItemTypes().size() == 0) {
            ErrorCheckItem item = new ErrorCheckItem(activityModel, Messages.LDChecker_14);
            category.add(item);
        }
        
        // Items
        checkItems(activityModel,
                null, 
                activityModel.getOnCompletionType().getFeedBackDescription(),
                category,
                Messages.LDChecker_15);
        
        checkItems(activityModel,
                null, 
                activityModel.getDescriptionModel(),
                category,
                Messages.LDChecker_16);

        if(activityModel instanceof ILearningActivityModel) {
            checkItems(activityModel,
                    null, 
                    ((ILearningActivityModel)activityModel).getLearningObjectivesModel(),
                    category,
                    Messages.LDChecker_17);

            checkItems(activityModel,
                    null, 
                    ((ILearningActivityModel)activityModel).getPrerequisitesModel(),
                    category,
                    Messages.LDChecker_18);
        }
    }
    
    private void checkActivityStructure(IActivityStructureModel activityModel, CheckerCategory parentCategory) {
        CheckerCategory category = new CheckerCategory(activityModel, activityModel.getTitle());
        parentCategory.add(category);

        if(activityModel.getActivityRefs().size() == 0) {
            ErrorCheckItem item = new ErrorCheckItem(activityModel.getLDModel().getMethodModel(), Messages.LDChecker_19);
            category.add(item);
        }
        
        // Items
        checkItems(activityModel,
                null, 
                activityModel.getInformationModel(),
                category,
                Messages.LDChecker_20);
    }

    private void checkEnvironments() {
        CheckerCategory category = new CheckerCategory(fLDModel.getEnvironmentsModel(), LDModelUtils.getUserObjectName(LDModelFactory.ENVIRONMENTS));
        fMainList.add(category);
        
        for(ILDModelObject modelObject : fLDModel.getEnvironmentsModel().getChildren()) {
            CheckerCategory envCategory = new CheckerCategory(modelObject, ((IEnvironmentModel)modelObject).getTitle());
            category.add(envCategory);
            checkEnvironment((IEnvironmentModel)modelObject, envCategory);
        }
    }
    
    private void checkEnvironment(IEnvironmentModel envModel, CheckerCategory parentCategory) {
        for(ILDModelObject object : envModel.getChildren()) {
            if(object instanceof ILearningObjectModel) {
                checkLearningObject((ILearningObjectModel)object, parentCategory);
            }
            if(object instanceof IConferenceModel) {
                checkConference((IConferenceModel)object, parentCategory);
            }
            if(object instanceof ISendMailModel) {
                checkSendMail((ISendMailModel)object, parentCategory);
            }
            if(object instanceof IIndexSearchModel) {
                checkIndexSearch((IIndexSearchModel)object, parentCategory);
            }
            if(object instanceof IMonitorModel) {
                checkMonitor((IMonitorModel)object, parentCategory);
            }
        }
    }
    
    private void checkLearningObject(ILearningObjectModel loModel, CheckerCategory parentCategory) {
        CheckerCategory category = new CheckerCategory(loModel, loModel.getTitle());
        parentCategory.add(category);

        if(loModel.getItems().getItemTypes().size() == 0) {
            ErrorCheckItem item = new ErrorCheckItem(loModel, Messages.LDChecker_21);
            category.add(item);
        }

        // Items
        checkItems(loModel,
                null, 
                loModel.getItems(),
                category,
                Messages.LDChecker_22);
    }
    
    private void checkConference(IConferenceModel conferenceModel, CheckerCategory parentCategory) {
        CheckerCategory category = new CheckerCategory(conferenceModel, conferenceModel.getTitle());
        parentCategory.add(category);

        if(conferenceModel.getParticipants().size() == 0) {
            ErrorCheckItem item = new ErrorCheckItem(conferenceModel, Messages.LDChecker_23);
            category.add(item);
        }
        
        /*
         * Widget particular rule:
         * If the conference contains parameter "widget=vote", then it must have either a "moderator" or "conference-manager"
         */
        String params = conferenceModel.getParameters();
        if(params != null && params.toLowerCase().indexOf("widget=vote") != -1) { //$NON-NLS-1$
            if(conferenceModel.getModerator() == null && conferenceModel.getManager() == null) {
                ErrorCheckItem item = new ErrorCheckItem(conferenceModel, Messages.LDChecker_34);
                category.add(item);
            }
        }
        
        if(conferenceModel.getItem().getItemTypes().size() == 0) {
            ErrorCheckItem item = new ErrorCheckItem(conferenceModel, Messages.LDChecker_24);
            category.add(item);
        }
        
        // Items
        checkItems(conferenceModel,
                null, 
                conferenceModel.getItem(),
                category,
                Messages.LDChecker_25);
    }

    private void checkSendMail(ISendMailModel sendmailModel, CheckerCategory parentCategory) {
        CheckerCategory category = new CheckerCategory(sendmailModel, sendmailModel.getTitle());
        parentCategory.add(category);

        if(sendmailModel.getEmailDataTypes().size() == 0) {
            ErrorCheckItem item = new ErrorCheckItem(sendmailModel, Messages.LDChecker_26);
            category.add(item);
        }
        // If not Level A, Email Data types must have an email property ref
        else if(!sendmailModel.getLDModel().getLevel().equals("A")) { //$NON-NLS-1$
            for(IEmailDataType emailData : sendmailModel.getEmailDataTypes()) {
                if(emailData.getEmailPropertyRef() == null) {
                    ErrorCheckItem item = new ErrorCheckItem(sendmailModel, emailData.getRole().getTitle() +
                            " " + Messages.LDChecker_44); //$NON-NLS-1$
                    category.add(item);
                }
            }
        }
    }
    
    private void checkIndexSearch(IIndexSearchModel indexsearchModel, CheckerCategory parentCategory) {
        if(indexsearchModel.getIndexElements().size() == 0 && indexsearchModel.getIndexClasses().size() == 0 &&
                indexsearchModel.getIndexTypeofElements().size() == 0) {
            ErrorCheckItem item = new ErrorCheckItem(indexsearchModel, indexsearchModel.getTitle() + Messages.LDChecker_27);
            parentCategory.add(item);
        }
    }
    
    private void checkMonitor(IMonitorModel monitorModel, CheckerCategory parentCategory) {
        CheckerCategory category = new CheckerCategory(monitorModel, monitorModel.getTitle());
        parentCategory.add(category);

        if(monitorModel.getItems().getItemTypes().size() == 0) {
            ErrorCheckItem item = new ErrorCheckItem(monitorModel, Messages.LDChecker_21);
            category.add(item);
        }
        
        // Items
        checkItems(monitorModel,
                null, 
                monitorModel.getItems(),
                category,
                Messages.LDChecker_28);
    }
    
    private void checkRoles() {
        CheckerCategory category = new CheckerCategory(fLDModel.getRolesModel(), LDModelUtils.getUserObjectName(LDModelFactory.ROLES));
        fMainList.add(category);
        
        boolean hasLearner = fLDModel.getRolesModel().getDefaultLearnerRole() != null;
        
        // At least one Learner
        if(!hasLearner) {
            ErrorCheckItem item = new ErrorCheckItem(fLDModel.getMethodModel(), Messages.LDChecker_29 + " " //$NON-NLS-1$
                    + LDModelUtils.getUserObjectName(LDModelFactory.LEARNER) + Messages.LDChecker_30);
            category.add(item);
        }
        
        // Items
        for(IRoleModel roleModel : fLDModel.getRolesModel().getOrderedRoles()) {
            CheckerCategory roleCategory = new CheckerCategory(roleModel, roleModel.getTitle());
            category.add(roleCategory);
            checkItems(roleModel,
                    null, 
                    roleModel.getInformationModel(),
                    category,
                    Messages.LDChecker_31);
        }
    }
    
    private void checkResources() {
        CheckerCategory category = new CheckerCategory(fLDModel.getResourcesModel(), LDModelUtils.getUserObjectName(LDModelFactory.RESOURCES));
        fMainList.add(category);
        
        for(IResourceModel resourceModel : fLDModel.getResourcesModel().getResources()) {
            String href = resourceModel.getHref();
            String resourceID = resourceModel.getIdentifier() + " - "; //$NON-NLS-1$
            
            // Missing HREF
            if(!StringUtils.isSetAfterTrim(href) ) {
                String message = resourceID + Messages.LDChecker_32;
                WarningCheckItem item = new WarningCheckItem(resourceModel, message);
                category.add(item);
            }
            // URL-type HREF
            else if(href.toLowerCase().startsWith("http") || href.toLowerCase().startsWith("www")) { //$NON-NLS-1$ //$NON-NLS-2$
                if(!resourceModel.getFiles().isEmpty()) {
                    String message = resourceID + Messages.LDChecker_38;
                    WarningCheckItem item = new WarningCheckItem(resourceModel, message);
                    category.add(item);
                }
            }
            // If file HREF does it exist?
            else {
                File file = new File(fLDModel.getRootFolder(), href);
                if(!file.exists()) {
                    String message = resourceID + Messages.LDChecker_36  + " " + href; //$NON-NLS-1$
                    WarningCheckItem item = new WarningCheckItem(resourceModel, message);
                    category.add(item);
                }

                // Does it have a default File entry
                IResourceFileModel fileResource = resourceModel.getResourceFileByHref(href);
                if(fileResource == null) {
                    String message = resourceID + Messages.LDChecker_35;
                    WarningCheckItem item = new WarningCheckItem(resourceModel, message);
                    category.add(item);
                }
            }
            
            // Resource Files
            for(IResourceFileModel fileModel : resourceModel.getFiles()) {
                String hrefFile = fileModel.getHref();
                
                // Does it have a HREF?
                if(!StringUtils.isSetAfterTrim(hrefFile) ) {
                    String message = resourceID + Messages.LDChecker_32;
                    WarningCheckItem item = new WarningCheckItem(resourceModel, message);
                    category.add(item);
                }
                // If file HREF does it exist?
                else { 
                    File file = new File(fLDModel.getRootFolder(), hrefFile);
                    if(!file.exists()) {
                        String message = resourceID + Messages.LDChecker_37  + " " + hrefFile; //$NON-NLS-1$
                        WarningCheckItem item = new WarningCheckItem(resourceModel, message);
                        category.add(item);
                    }
                }
            }
        }
    }
    
    /**
     * Check Items for invalid Resource References
     */
    /**
     * @param owner The owner object to jump to
     * @param ownerText The text to display in the Inspector or null
     * @param itemTypeContainer
     * @param parentCategory
     * @param errorLabel
     */
    private void checkItems(Object owner, String ownerText, IItemTypeContainer itemTypeContainer,
            CheckerCategory parentCategory, String errorLabel) {
        
        if(ownerText != null) {
            owner = new InspectorViewModelAdapter(owner, ownerText);
        }
        
        List<IItemType> list = new ArrayList<IItemType>();
        
        collectInvalidReferencedItems(itemTypeContainer, list);
        
        if(list.size() > 0) {
            ErrorCheckItem item = new ErrorCheckItem(owner, errorLabel + " - " + list.size() + Messages.LDChecker_33); //$NON-NLS-1$
            parentCategory.add(item);
        }
    }

    private void collectInvalidReferencedItems(IItemTypeContainer itemTypeContainer, List<IItemType> list) {
        for(IItemType itemType : itemTypeContainer.getItemTypes()) {
            String idref = itemType.getIdentifierRef();
            if(StringUtils.isSetAfterTrim(idref) && fLDModel.getResourcesModel().getResourceByIdentifier(idref) == null) {
                list.add(itemType);
            }
            collectInvalidReferencedItems(itemType, list);
        }
    }
    

    
    // ============================================================================================================
    //                                              CLASSES
    // ============================================================================================================

    public static interface ILDCheckerItem {
        Object getComponent();
        String getComment();
    }

    /**
     * Denotes an Error in the LD
     */
    public static class ErrorCheckItem implements ILDCheckerItem {
        private Object fComponent;
        private String fComment;
        
        public ErrorCheckItem(Object component, String comment) {
            fComponent = component;
            fComment = comment;
        }
        
        public Object getComponent() {
            return fComponent;
        }
        
        public String getComment() {
            return fComment;
        }
        
        @Override
        public String toString() {
            return fComment;
        }
    }
    
    /**
     * Denotes a Warning in the LD
     */
    public static class WarningCheckItem implements ILDCheckerItem {
        private Object fComponent;
        private String fComment;
        
        public WarningCheckItem(Object component, String comment) {
            fComponent = component;
            fComment = comment;
        }
        
        public Object getComponent() {
            return fComponent;
        }
        
        public String getComment() {
            return fComment;
        }
        
        @Override
        public String toString() {
            return fComment;
        }
    }
    
    /**
     * Denotes a Category in the LD
     */
    public static class CheckerCategory implements ILDCheckerItem {
        private Object fComponent;
        private String fComment;
        private List<ILDCheckerItem> fSubList = new ArrayList<ILDCheckerItem>();
        private boolean fHasErrors;
        private boolean fHasWarnings;
        
        public CheckerCategory(Object component, String comment) {
            fComponent = component;
            fComment = comment;
        }
        
        public void add(ILDCheckerItem item) {
            if(!fSubList.contains(item)) {
                fSubList.add(item);
                if(item instanceof ErrorCheckItem) {
                    fHasErrors = true;
                }
                if(item instanceof WarningCheckItem) {
                    fHasWarnings = true;
                }
            }
        }

        public Object getComponent() {
            return fComponent;
        }
        
        public String getComment() {
            return fComment;
        }
        
        public List<ILDCheckerItem> getList() {
            return fSubList;
        }
        
        public boolean hasErrors() {
            for(ILDCheckerItem item : fSubList) {
                if(item instanceof CheckerCategory) {
                    fHasErrors |= ((CheckerCategory)item).hasErrors();
                }
            }
            return fHasErrors;
        }
        
        public boolean hasWarnings() {
            for(ILDCheckerItem item : fSubList) {
                if(item instanceof CheckerCategory) {
                    fHasWarnings |= ((CheckerCategory)item).hasWarnings();
                }
            }
            return fHasWarnings;
        }
        
        @Override
        public String toString() {
            return fComment;
        }
    }
}
