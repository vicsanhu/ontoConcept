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
import org.tencompetence.imsldmodel.activities.IActivitiesModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityType;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;


/**
 * New Activity Wizard
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewActivityWizard.java,v 1.6 2009/05/19 18:21:06 phillipus Exp $
 */
public class NewActivityWizard
extends Wizard {
	
    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".NewActivityWizard"; //$NON-NLS-1$
    
    public static final int LEARNING_ACTIVITY = 1;
    public static final int SUPPORT_ACTIVITY = 2;
    public static final int ACTIVITY_STRUCTURE =3;
    
    /**
     * The single Page
     */
    private NewActivityWizardPage fPage;
    
    private IActivityStructureModel fParentAS;
    
    private ILDModel fLDModel;
    
    private int fType;
    
    /**
     * Default Constructor
     */
    public NewActivityWizard(ILDModel ldModel, int type) {
        setWindowTitle(Messages.NewActivityWizard_0);
        fLDModel = ldModel;
        fType = type;
    }
        
    /**
     * Default Constructor for adding a child Activity
     */
    public NewActivityWizard(IActivityStructureModel as, int type) {
		setWindowTitle(Messages.NewActivityWizard_0);
		fParentAS = as;
		fLDModel = as.getLDModel();
		fType = type;
	}
	
    /**
	 * Add the page to the wizard
	 */
	@Override
    public void addPages() {
	    fPage = new NewActivityWizardPage(fLDModel, fType);
		addPage(fPage);
	}
    
	@Override
    public boolean performFinish() {
	    IActivityType activity = fPage.getActivity();
	    if(activity == null) { 
	        return false;
	    }
	    
        // Add it to Activities Model
	    IActivitiesModel activitiesModel = fLDModel.getActivitiesModel();
	    if(activity instanceof ILearningActivityModel) {
	        activitiesModel.getLearningActivitiesModel().addChild(activity);
	    }
	    if(activity instanceof ISupportActivityModel) {
	        activitiesModel.getSupportActivitiesModel().addChild(activity);
	    }
	    if(activity instanceof IActivityStructureModel) {
	        activitiesModel.getActivityStructuresModel().addChild(activity);
	    }
	    
	    // Then add a ref to it if we are adding to AS
	    if(fParentAS != null) {
	        fParentAS.addActivity(activity, -1);
	    }
	    
	    fLDModel.setDirty();
	    
        return true;
	}
}