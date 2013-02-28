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
package org.tencompetence.ldauthor.ui.wizards.activity;

import org.eclipse.jface.wizard.Wizard;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.activities.IActivitiesModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityType;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;


/**
 * New RolePart Wizard - Adds Activity and Role
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewRolePartWizard.java,v 1.7 2009/05/19 18:21:06 phillipus Exp $
 */
public class NewRolePartWizard
extends Wizard {
	
    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".NewRolePartWizard"; //$NON-NLS-1$
    
    public static final int LEARNING_ACTIVITY = 1;
    public static final int SUPPORT_ACTIVITY = 2;
    public static final int ACTIVITY_STRUCTURE =3;
    
    /**
     * The single Page
     */
    private NewRolePartWizardPage fPage;
    
    private IActModel fActModel;
    
    private int fType;
        
    /**
     * Default Constructor
     */
    public NewRolePartWizard(IActModel actModel, int type) {
		setWindowTitle(Messages.NewRolePartWizard_1);
		fActModel = actModel;
		fType = type;
	}
	
    /**
	 * Add the page to the wizard
	 */
	@Override
    public void addPages() {
	    fPage = new NewRolePartWizardPage(fActModel.getLDModel(), fType);
		addPage(fPage);
	}
    
	@Override
    public boolean performFinish() {
	    IActivityType activity = fPage.getActivity();
	    if(activity == null) { 
	        return false;
	    }
	    
	    ILDModel ldModel = fActModel.getLDModel();

        // First, add it to Activities Model
	    IActivitiesModel activitiesModel = ldModel.getActivitiesModel();
	    if(activity instanceof ILearningActivityModel) {
	        activitiesModel.getLearningActivitiesModel().addChild(activity);
	    }
	    if(activity instanceof ISupportActivityModel) {
	        activitiesModel.getSupportActivitiesModel().addChild(activity);
	    }
	    if(activity instanceof IActivityStructureModel) {
	        activitiesModel.getActivityStructuresModel().addChild(activity);
	    }
	    
	    // Add Role Parts
	    for(IRoleModel role : fPage.getRoles()) {
	        IRolePartModel rolePart = (IRolePartModel)LDModelFactory.createModelObject(LDModelFactory.ROLE_PART, ldModel);
	        rolePart.setRole(role);
	        rolePart.setComponent(activity);
	        fActModel.getRolePartsModel().addChild(rolePart);
        }
	    
	    ldModel.setDirty();
	    
        return true;
	}
}