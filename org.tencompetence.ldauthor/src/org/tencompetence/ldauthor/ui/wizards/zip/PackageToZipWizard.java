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
package org.tencompetence.ldauthor.ui.wizards.zip;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.serialization.LDModelSerializer;


/**
 * Package to Zip Wizard
 * 
 * @author Phillip Beauvoir
 * @version $Id: PackageToZipWizard.java,v 1.9 2009/05/19 18:21:04 phillipus Exp $
 */
public class PackageToZipWizard
extends Wizard {
	
    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".PackageToZipWizard"; //$NON-NLS-1$
    
    /**
     * The single Page
     */
    private PackageToZipWizardPage fPage;
    
    private IReCourseLDModel fLDModel;
    
    /**
     * Default Constructor
     * @param title 
     */
    public PackageToZipWizard(IReCourseLDModel ldModel, String title) {
		setWindowTitle(Messages.PackageToZipWizard_0 + " - " + title); //$NON-NLS-1$
		fLDModel = ldModel;
	}
	
    /**
	 * Add the page to the wizard
	 */
	@Override
    public void addPages() {
	    fPage = new PackageToZipWizardPage(fLDModel);
		addPage(fPage);
	}
    
	@Override
    public boolean performFinish() {
	    String str = fPage.getFilePath();
	    if(!str.toLowerCase().endsWith(".zip")) { //$NON-NLS-1$
	        str += ".zip"; //$NON-NLS-1$
	    }
	    
	    final File file = new File(str);
	    
	    try {
	        LDModelSerializer serialiser = new LDModelSerializer(fLDModel);
	        serialiser.createZipFile(file, fPage.includeReCourseInfo());
	    }
	    catch(final Exception ex) {
	        ex.printStackTrace();
	        
	        Display.getDefault().asyncExec(new Runnable() {
	            public void run() {
	                MessageDialog.openError(getShell(), Messages.PackageToZipWizard_0, ex.getMessage());
	            }
	        });
	        
	        return true;
	    }
	    
	    Display.getDefault().asyncExec(new Runnable() {
	        public void run() {
	            MessageDialog.openInformation(getShell(), Messages.PackageToZipWizard_0, Messages.PackageToZipWizard_1);
	        }
	    });

        return true;
	}
	
}