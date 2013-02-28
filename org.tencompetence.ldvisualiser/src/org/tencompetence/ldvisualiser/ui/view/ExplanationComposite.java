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
package org.tencompetence.ldvisualiser.ui.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureRefModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentRefModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.ICompleteActType;
import org.tencompetence.imsldmodel.method.ICompletePlayType;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldvisualiser.ui.view.ObjectAnalysis.RolePartMapping;


/**
 * Explanation Composite
 * 
 * @author Phillip Beauvoir
 * @version $Id: ExplanationComposite.java,v 1.10 2009/10/14 16:20:10 phillipus Exp $
 */
public class ExplanationComposite extends ScrolledComposite {
    
    private Composite fClient;
    private Label fMainLabel;
    private Label fTextLabel;
    
    String PLAY = LDModelUtils.getUserObjectName(LDModelFactory.PLAY);
    String PLAYS = LDModelUtils.getUserObjectName(LDModelFactory.PLAYS);
    String ACT = LDModelUtils.getUserObjectName(LDModelFactory.ACT);
    String ACTS = LDModelUtils.getUserObjectName(LDModelFactory.ACTS);
    String ACTIVITY_STRUCTURE = LDModelUtils.getUserObjectName(LDModelFactory.ACTIVITY_STRUCTURE);
    String ENVIRONMENT = LDModelUtils.getUserObjectName(LDModelFactory.ENVIRONMENT);
    
    String SPACE = " "; //$NON-NLS-1$
    String DOT = "."; //$NON-NLS-1$
    String DOT_SPACE = ". "; //$NON-NLS-1$
    String CR = "\n"; //$NON-NLS-1$
    
    private Object fContext;

    public ExplanationComposite(Composite parent, int style) {
        super(parent, SWT.V_SCROLL);
        
        setExpandHorizontal(true);
        
        fClient = new Composite(this, SWT.NULL);
        GridLayout layout = new GridLayout();
        //layout.marginHeight = 0;
        //layout.marginWidth = 0;
        fClient.setLayout(layout);
        
        GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
        setLayoutData(gd);
        
        setContent(fClient);
        
        AppFormToolkit.getInstance().adapt(this);
        AppFormToolkit.getInstance().adapt(fClient);
        
        fMainLabel = AppFormToolkit.getInstance().createHeaderLabel(fClient, "", SWT.NULL); //$NON-NLS-1$
        gd = new GridData(GridData.FILL, SWT.NULL, true, false);
        fMainLabel.setLayoutData(gd);
        
        fTextLabel = AppFormToolkit.getInstance().createLabel(fClient, "", SWT.WRAP); //$NON-NLS-1$
        gd = new GridData(GridData.FILL, SWT.NULL, true, false);
        fTextLabel.setLayoutData(gd);
        
        fClient.pack();
        fClient.layout();
    }

    public void setContext(Object context) {
        fContext = context;
        
        fMainLabel.setText(""); //$NON-NLS-1$
        fTextLabel.setText(""); //$NON-NLS-1$

        if(context == null) {
            return;
        }
        
        if(context instanceof ILDModel) {
            fMainLabel.setText(Messages.ExplanationComposite_0);
            explainUOL((ILDModel)context);
        }
        
        if(context instanceof IPlayModel) {
            explainPlay((IPlayModel)context);
        }
        
        if(context instanceof IActModel) {
            explainAct((IActModel)context);
        }
        
        if(context instanceof IActivityStructureRefModel) {
            context = ((IActivityStructureRefModel)context).getLDModelObject();
        }
        
        if(context instanceof IActivityStructureModel) {
            explainActivityStructure((IActivityStructureModel)context);
        }
        
        if(context instanceof RolePartMapping) {
            RolePartMapping mapping = (RolePartMapping)context;
            explainRolePartMapping(mapping);
            fMainLabel.setText(mapping.role.getTitle() + SPACE + Messages.ExplanationComposite_1 + SPACE +
                    ACT + SPACE + "'" + mapping.act.getTitle() + "'");  //$NON-NLS-1$//$NON-NLS-2$
        }
        
        if(context instanceof IEnvironmentRefModel) {
            context = ((IEnvironmentRefModel)context).getLDModelObject();
        }
        
        if(context instanceof IEnvironmentModel) {
            explainEnvironment((IEnvironmentModel)context);
        }
        
        // Title here
        if(context instanceof ITitle) {
            fMainLabel.setText(((ITitle)context).getTitle());
        }

        fClient.pack();
        fClient.layout();
    }
    
    private void explainUOL(ILDModel ldModel) {
        int numPlays = ldModel.getMethodModel().getPlaysModel().getChildren().size();
        
        String text = Messages.ExplanationComposite_2 + DOT_SPACE;
        
        text += Messages.ExplanationComposite_3 + SPACE + numPlays + SPACE + (numPlays != 1 ? PLAYS : PLAY) + DOT_SPACE;
        
        if(numPlays > 1) {
            text += Messages.ExplanationComposite_4 + SPACE + PLAY + SPACE + Messages.ExplanationComposite_5 + DOT;
        }
        
        fTextLabel.setText(text);
    }
    
    private void explainPlay(IPlayModel play) {
        int numActs = play.getActsModel().getChildren().size();
        int completeChoice = play.getCompletePlayType().getChoice();
        
        String lastActName = null;
        if(numActs > 0) {
            lastActName = ((IActModel)play.getActsModel().getChildren().get(numActs - 1)).getTitle();
        }
        
        String text = Messages.ExplanationComposite_6 + SPACE + PLAY + SPACE;
        
        text += Messages.ExplanationComposite_7 + SPACE + numActs + SPACE + (numActs != 1 ? ACTS : ACT) + DOT_SPACE + CR;
        
        text += Messages.ExplanationComposite_8 + SPACE + PLAY + SPACE + Messages.ExplanationComposite_9 + SPACE;
        
        switch(completeChoice) {
            case ICompletePlayType.COMPLETE_NONE:
                text += Messages.ExplanationComposite_10 + DOT_SPACE;
                break;
                
            case ICompletePlayType.COMPLETE_WHEN_LAST_ACT_COMPLETED:
                text += Messages.ExplanationComposite_11 + SPACE + ACT;
                if(lastActName != null) {
                    text += ", '" + lastActName + "',"; //$NON-NLS-1$ //$NON-NLS-2$
                }
                text += SPACE + Messages.ExplanationComposite_14 + DOT;
                break; 
                
            case ICompletePlayType.COMPLETE_TIME_LIMIT:
                text += Messages.ExplanationComposite_15 + DOT;
                break;
                
            case ICompletePlayType.COMPLETE_WHEN_PROPERTY_SET:
                text += Messages.ExplanationComposite_16 + DOT;
                break; 
        }
        
        fTextLabel.setText(text);
    }
    
    private void explainAct(IActModel act) {
        List<RolePartMapping> rolePartMappings = ObjectAnalysis.getRolePartMappings(act);
        
        String text = Messages.ExplanationComposite_17 + SPACE + ACT + SPACE + Messages.ExplanationComposite_18 + SPACE +
            rolePartMappings.size() + SPACE + (rolePartMappings.size() != 1 ? Messages.ExplanationComposite_19 : Messages.ExplanationComposite_20) + ":" + CR;  //$NON-NLS-1$
        
        for(RolePartMapping mapping : rolePartMappings) {
            IRoleModel role = mapping.role;
            
            text += " - " + role.getTitle() + SPACE + Messages.ExplanationComposite_23 + SPACE; //$NON-NLS-1$
            
            Iterator<ILDModelObject> componentList = mapping.componentList.iterator();
            while(componentList.hasNext()) {
                ILDModelObject modelObject = (ILDModelObject)componentList.next();
                
                //String componentType = LDModelUtils.getUserObjectName(modelObject.getTagName());
                
                text += "'" + ((ITitle)modelObject).getTitle() + "'"; //$NON-NLS-1$ //$NON-NLS-2$
                if(componentList.hasNext()) {
                    text += ", "; //$NON-NLS-1$
                }
                else {
                    text += DOT;
                }
            }
            
            text += CR;
        }
        
        
        text += CR;
        text += Messages.ExplanationComposite_27 + SPACE + ACT + SPACE + Messages.ExplanationComposite_28 + SPACE;
        
        int completeChoice = act.getCompleteActType().getChoice();
        switch(completeChoice) {
            case ICompleteActType.COMPLETE_NONE:
                text += Messages.ExplanationComposite_29 + DOT_SPACE;
                break;
                
            case ICompleteActType.COMPLETE_WHEN_ROLE_PART_COMPLETED:
                HashMap<IRoleModel, List<ILDModelObject>> completedMap = new HashMap<IRoleModel, List<ILDModelObject>>();
                
                List<IRolePartModel> completeRoleParts = act.getCompleteActType().getRoleParts();
                for(IRolePartModel rolePart : completeRoleParts) {
                    IRoleModel role = rolePart.getRole();
                    ILDModelObject component = rolePart.getComponent();
                    
                    if(role == null || component == null) {
                        continue;
                    }
                    
                    List<ILDModelObject> componentList = completedMap.get(role);
                    if(componentList == null) {
                        componentList = new ArrayList<ILDModelObject>();
                        completedMap.put(role, componentList);
                    }
                    
                    componentList.add(component);
                }
                
                text += Messages.ExplanationComposite_30 + CR;
                
                for(IRoleModel role : completedMap.keySet()) {
                    text += " - " + role.getTitle() + SPACE + Messages.ExplanationComposite_32 + SPACE; //$NON-NLS-1$
                    
                    Iterator<ILDModelObject> componentList = completedMap.get(role).iterator();
                    while(componentList.hasNext()) {
                        ILDModelObject modelObject = (ILDModelObject)componentList.next();
                        
                        //String componentType = LDModelUtils.getUserObjectName(modelObject.getTagName());
                        
                        text += "'" + ((ITitle)modelObject).getTitle() + "'"; //$NON-NLS-1$ //$NON-NLS-2$
                        if(componentList.hasNext()) {
                            text += ", "; //$NON-NLS-1$
                        }
                        else {
                            text += DOT;
                        }
                    }
                    
                    text += CR;
                }
                
                break; 
                
            case ICompleteActType.COMPLETE_TIME_LIMIT:
                text += Messages.ExplanationComposite_36 + DOT;
                break;
                
            case ICompleteActType.COMPLETE_WHEN_PROPERTY_SET:
                text += Messages.ExplanationComposite_37 + DOT;
                break;
                
            case ICompleteActType.COMPLETE_WHEN_CONDITION_TRUE:
                text += Messages.ExplanationComposite_38 + DOT;
                break;
        }

        fTextLabel.setText(text);
    }
    
    private void explainActivityStructure(IActivityStructureModel as) {
        String text = Messages.ExplanationComposite_39 + SPACE + ACTIVITY_STRUCTURE + SPACE;
        
        int numChildren = as.getActivityRefs().size();
        
        int type = as.getStructureType();
        switch(type) {
            case IActivityStructureModel.TYPE_SEQUENCE:
                text += Messages.ExplanationComposite_40 + SPACE + numChildren + SPACE + Messages.ExplanationComposite_41 + DOT;
                break;

            case IActivityStructureModel.TYPE_SELECTION:
                int numToSelect = as.getNumberToSelect();
                if(numToSelect == 0) {
                    numToSelect = numChildren;
                }
                text += Messages.ExplanationComposite_42 + SPACE + numToSelect + SPACE + Messages.ExplanationComposite_43 + SPACE + numChildren + SPACE + Messages.ExplanationComposite_44 + DOT;
                break;
        }
        
        fTextLabel.setText(text);
    }
    
    private void explainRolePartMapping(RolePartMapping mapping) {
        IRoleModel role = mapping.role;
        
        String text = Messages.ExplanationComposite_45 + SPACE + role.getTitle() + SPACE + Messages.ExplanationComposite_46 +
            SPACE + "'" + mapping.act.getTitle() + "':" + CR; //$NON-NLS-1$ //$NON-NLS-2$
        
        for(ILDModelObject modelObject : mapping.componentList) {
            text += " - '" + ((ITitle)modelObject).getTitle() + "'" + CR; //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        fTextLabel.setText(text);
    }
    
    private void explainEnvironment(IEnvironmentModel env) {
        String text = Messages.ExplanationComposite_51 + SPACE + ENVIRONMENT + SPACE + Messages.ExplanationComposite_52 + DOT;
        
        fTextLabel.setText(text);
    }

    public void refresh() {
        setContext(fContext);
    }
}
