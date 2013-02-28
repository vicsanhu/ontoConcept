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
package org.tencompetence.ldauthor.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.preferences.CopperCorePrefsManager;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;
import org.tencompetence.ldauthor.serialization.LDModelSerializer;
import org.tencompetence.ldpublisher.IPublishHandler;
import org.tencompetence.ldpublisher.PublishHandler;
import org.tencompetence.ldpublisher.upload.ICancelMonitor;
import org.tencompetence.ldpublisher.upload.ICancelable;


/**
 * CopperCoreUtils
 * 
 * @author Phillip Beauvoir
 * @version $Id: CopperCoreUtils.java,v 1.3 2009/05/19 18:21:09 phillipus Exp $
 */
public class CopperCoreUtils {
    
    private static boolean fResult;
    private static boolean fCancelled;
    private static ICancelable cancelable;


    /**
     * Upload (Publish) a UOL to CC server
     * @param ldModel
     */
    public static void publish(IReCourseLDModel ldModel) {
        if(ldModel == null) {
            return;
        }
        
        IPreferenceStore store = LDAuthorPlugin.getDefault().getPreferenceStore();
        boolean includeReCourseInfo = store.getBoolean(ILDAuthorPreferenceConstants.PREFS_CC_INCLUDE_RECOURSE_INFO);
        
        // Make a temporary zip file of the LD
        File tmpFile = null;
        try {
            LDModelSerializer serialiser = new LDModelSerializer(ldModel);
            tmpFile = File.createTempFile("lds", ".zip"); //$NON-NLS-1$ //$NON-NLS-2$
            tmpFile.deleteOnExit();
            serialiser.createZipFile(tmpFile, includeReCourseInfo);
            
            publish(tmpFile);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if(tmpFile != null) {
                tmpFile.delete();
            }
        }
    }
    
    /**
     * Upload (Publish) a zipped UOL to CC server
     * @param ldModel
     * @param title
     */
    public static void publish(final File file) {
        final String server = LDAuthorPlugin.getDefault().getPreferenceStore().getString(ILDAuthorPreferenceConstants.PREFS_CC_SERVER);
        final String user = CopperCorePrefsManager.getDecryptedString(ILDAuthorPreferenceConstants.PREFS_CC_USERNAME);
        final String password = CopperCorePrefsManager.getDecryptedString(ILDAuthorPreferenceConstants.PREFS_CC_PASSWORD);
        
        IRunnableWithProgress runnable = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                try {
                    IPublishHandler handler = new PublishHandler(server, user, password);
                    
                    monitor.beginTask(Messages.CopperCoreUtils_0, IProgressMonitor.UNKNOWN);
                    
                    fResult = handler.publishUol(file, new ICancelMonitor() {
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
                if(fResult) {
                    displaySuccessMessage();
                }
                else {
                    displayErrorMessage(Messages.CopperCoreUtils_1);
                }
            }
        }
        catch(Exception ex) {
            if(!fCancelled) {
                displayErrorMessage(ex.getMessage());
            }
        }
    }
    
    private static void displayErrorMessage(final String message) {
        MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.CopperCoreUtils_2, message);
    }
    
    private static void displaySuccessMessage() {
        MessageDialog.openInformation(Display.getCurrent().getActiveShell(), Messages.CopperCoreUtils_2, Messages.CopperCoreUtils_3);
    }

}
