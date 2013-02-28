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
package org.tencompetence.ldauthor.ui.wizards.role;

import org.eclipse.jface.wizard.Wizard;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.roles.ILearnerModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;


/**
 * New Role Wizard
 * 
 * @author Phillip Beauvoir
 * @version $Id: NewRoleWizard.java,v 1.6 2009/05/19 18:21:09 phillipus Exp $
 */
public class NewRoleWizard
extends Wizard {
	
    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".NewRoleWizard"; //$NON-NLS-1$
    
    /**
     * The single Page
     */
    private NewRoleWizardPage fPage;
    
    private ILDModel fLDModel;
    
    private Class<?> fType;
    
    /**
     * Default Constructor
     */
    public NewRoleWizard(ILDModel ldModel, Class<?> type) {
        setWindowTitle(Messages.NewRoleWizard_0);
        fLDModel = ldModel;
        fType = type;
    }
        
    /**
	 * Add the page to the wizard
	 */
	@Override
    public void addPages() {
	    fPage = new NewRoleWizardPage(fLDModel, fType);
		addPage(fPage);
	}
    
	@Override
    public boolean performFinish() {
	    IRoleModel role;
	    
        if(fType == ILearnerModel.class) {
            role = (IRoleModel)LDModelFactory.createModelObject(LDModelFactory.LEARNER, fLDModel);
        }
        else {
            role = (IRoleModel)LDModelFactory.createModelObject(LDModelFactory.STAFF, fLDModel);
        }

        role.setTitle(fPage.getRoleTitle());
        role.setMinPersons(fPage.getRoleMinPersons());
        role.setMaxPersons(fPage.getRoleMaxPersons());
        role.setCreateNew(fPage.getRoleCreateNew());
        role.setMatchPersons(fPage.getRoleMatchPersons());
        
        ILDModelObjectContainer parent = fPage.getRoleParent();
        parent.addChild(role);
        
        fLDModel.setDirty();
	    
        return true;
	}
}