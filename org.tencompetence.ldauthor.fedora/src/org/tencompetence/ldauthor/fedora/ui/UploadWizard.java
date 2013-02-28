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
package org.tencompetence.ldauthor.fedora.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.tencompetence.ldauthor.fedora.FedoraPlugin;
import org.tencompetence.ldauthor.fedora.IFedoraHandler;
import org.tencompetence.ldauthor.fedora.http.ICancelMonitor;
import org.tencompetence.ldauthor.fedora.http.ICancelable;
import org.tencompetence.ldauthor.fedora.impl.FedoraHandlerImpl;
import org.tencompetence.ldauthor.fedora.preferences.ConnectionsPrefsManager;
import org.tencompetence.ldauthor.fedora.preferences.IFedoraPreferenceConstants;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;


/**
 * Upload to Fedora Wizard
 * 
 * @author Phillip Beauvoir
 * @version $Id: UploadWizard.java,v 1.2 2009/05/19 18:21:31 phillipus Exp $
 */
public class UploadWizard
extends Wizard {
	
    public static String ID = FedoraPlugin.PLUGIN_ID + ".UploadWizard"; //$NON-NLS-1$
    
    /**
     * The single Page
     */
    private UploadWizardPage fPage;
    
    private IReCourseLDModel fLDModel;
    
    private ICancelable cancelable;
    private boolean fCancelled;
    private String fTitle;
    
    
    /**
     * Default Constructor
     * @param title 
     */
    public UploadWizard(IReCourseLDModel ldModel, String title) {
		setWindowTitle(Messages.UploadWizard_0 + " - " + title); //$NON-NLS-1$
		fLDModel = ldModel;
		fTitle = title;
	}
	
    /**
	 * Add the page to the wizard
	 */
	@Override
    public void addPages() {
	    fPage = new UploadWizardPage(fLDModel);
		addPage(fPage);
	}
    
	@Override
    public boolean performFinish() {
	    upload();
        return true;
	}

    private void upload() {
        if(fLDModel == null) {
            return;
        }
        
        final String server = FedoraPlugin.getDefault().getPreferenceStore().getString(IFedoraPreferenceConstants.PREFS_FEDORA_SERVER_URL);
        final String user = ConnectionsPrefsManager.getDecryptedString(IFedoraPreferenceConstants.PREFS_FEDORA_USERNAME);
        final String password = ConnectionsPrefsManager.getDecryptedString(IFedoraPreferenceConstants.PREFS_FEDORA_PASSWORD);
             
        IRunnableWithProgress runnable = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                try {
                    IFedoraHandler handler = new FedoraHandlerImpl(server, user, password);
                    
                    monitor.beginTask(Messages.UploadWizard_1 + " " + fTitle + "...", IProgressMonitor.UNKNOWN);  //$NON-NLS-1$//$NON-NLS-2$
                    
                    handler.postLearningDesign(fLDModel, new ICancelMonitor() {
                        public void releaseOperation() {
                        }

                        public void setOperation(ICancelable method) {
                            cancelable = method;
                        }
                    });
                }
                catch(Exception ex) {
                    // Pass this on to the caller with this ruse...
                    throw new InterruptedException(ex.getMessage());
                }
            }
        };

        ProgressMonitorDialog progress = new ProgressMonitorDialog(Display.getCurrent().getActiveShell()) {
            @Override
            protected void cancelPressed() {
                fCancelled = true;
                if(cancelable != null) {
                    cancelable.cancel();
                }
                super.cancelPressed();
            }
        };
        
        try {
            progress.run(true, true, runnable);

            if(!fCancelled) {
                displaySuccessMessage();
            }
        }
        catch(Exception ex) {
            if(!fCancelled) {
                displayErrorMessage(ex.getMessage());
            }
        }
    }
    
    private void displayErrorMessage(final String message) {
        MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.UploadWizard_2, message);
    }
    
    private void displaySuccessMessage() {
        MessageDialog.openInformation(Display.getCurrent().getActiveShell(), Messages.UploadWizard_3, Messages.UploadWizard_4);
    }
    
	
}