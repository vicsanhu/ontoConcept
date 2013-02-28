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
package org.tencompetence.qtieditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.ILDContentHandler;
import org.tencompetence.ldauthor.Logger;
import org.tencompetence.qtieditor.serialization.QTIEditorInput;
import org.tencompetence.qtieditor.ui.AssessmentTestEditor;

public class QTITestContentHandler implements ILDContentHandler {
    
    /*
     * Content ID of IMS QTI Item Content to be used in Eclipse org.eclipse.core.runtime.contentType extension
     */
	public static final String IMS_QTI_TEST_CONTENT = "org.tencompetence.ldauthor.content.QTI_Test"; //$NON-NLS-1$
    
    public static final String IMS_QTI_TYPE = "imsqti_test_xmlv2p1";

    public String getType() {
        return IMS_QTI_TYPE;
    }

    public boolean isHandler(File file) {
        if(file == null || !file.exists() || file.isDirectory()) {
            return false;
        }
        
        try {
            IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
            FileInputStream fi = new FileInputStream(file);
            IContentType contentType = contentTypeManager.findContentTypeFor(fi, file.getName());
            fi.close();
            return contentType != null ? IMS_QTI_TEST_CONTENT.equals(contentType.getId()) : false;
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }

    public void editFile(File file, ILDModel ldModel) {
        if(isHandler(file)) {
            QTIEditorInput input = new QTIEditorInput(file);
            /*
            IResourcesModel resources = ldModel.getResourcesModel();
            IResourceModel testResource = resources.getResourceByHref(file.getName());
            input.setResources(resources);                    
            input.setTestResource(testResource);                    
            */
            try {
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(); 
                page.openEditor(input, AssessmentTestEditor.ID);
            }
            catch(PartInitException ex) {
            	Logger.logError("QTITestContentHandler: Could not open the editor: " + AssessmentTestEditor.ID); //$NON-NLS-1$
            }
        }
    }

    /*
	public void editFile(File file) {
		// TODO Auto-generated method stub
		if (isHandler(file)) {
			QTIEditorInput input = new QTIEditorInput(file);

			IEditorPart ldEditor = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();

			if (ldEditor instanceof ILDMultiPageEditor) {
				IResourcesModel resources = ((ILDModel) ldEditor
						.getAdapter(ILDModel.class)).getResourcesModel();
				IResourceModel testResource = resources.getResourceByHref(file
						.getName());
				input.setResources(resources);
				input.setTestResource(testResource);

				try {
					IWorkbenchPage page = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage();
					page.openEditor(input, AssessmentTestEditor.ID);
				} catch (PartInitException ex) {
					Logger.logError("QTIItemContentHandler: Could not open the editor: " + AssessmentTestEditor.ID); //$NON-NLS-1$
				}
			}
		}
	}
	*/
}
