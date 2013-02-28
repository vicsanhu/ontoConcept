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
package org.tencompetence.ldauthor.ui.views.preview;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.resources.IResourceFileModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.BrowserWidgetNotSupportedException;
import org.tencompetence.ldauthor.ui.common.ExtendedBrowser;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.LDEditorInput;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * BrowserPreview
 * 
 * @author Phillip Beauvoir
 * @version $Id: BrowserPreview.java,v 1.3 2009/05/19 18:21:04 phillipus Exp $
 */
public class BrowserPreview implements ILDPreview {
    
    private Composite fClient;
    private Browser fBrowser;

    public void createControl(Composite parent) {
        fClient = AppFormToolkit.getInstance().createComposite(parent);
        fClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        fClient.setLayout(new GridLayout());
        fClient.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                dispose();
            }
        });

        try {
            fBrowser = createBrowser(fClient);
        }
        catch(BrowserWidgetNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    public Composite getClientArea() {
        return fClient;
    }

    public void update(ILDEditorPart editorPart, Object object) {
        if(object instanceof IResourceModel) {
            showResource(editorPart, (IResourceModel)object);
        }
        
        else if(object instanceof IResourceFileModel) {
            showResourceFile(editorPart, (IResourceFileModel)object);
        }
        
        else if(object instanceof File) {
            showFile((File)object);
        }
        
        else {
            throw new RuntimeException("Incorrect model type"); //$NON-NLS-1$
        }
    }
    
    private void showResource(ILDEditorPart editorPart, IResourceModel resource) {
        String href = resource.getHref();
        if(!StringUtils.isSet(href)) {
            return;
        }
        
        ILDModel ldModel = ((LDEditorInput)editorPart.getEditorInput()).getModel();
        File rootFolder = ldModel.getRootFolder();
        
        // See if it's a local file
        File file = new File(rootFolder, href);
        if(file.exists()) {
            href = "file:///" + file.toString(); //$NON-NLS-1$
        }
        
        //if(BrowserUtils.isBrowserURL(href)) {
            fBrowser.setUrl(href);
        //}
        //else {
        //    Program.launch(href);
        //}
    }
    
    private void showResourceFile(ILDEditorPart editorPart, IResourceFileModel resourcefile) {
        String href = resourcefile.getHref();
        if(!StringUtils.isSet(href)) {
            return;
        }
        
        ILDModel ldModel = ((LDEditorInput)editorPart.getEditorInput()).getModel();
        File rootFolder = ldModel.getRootFolder();
        
        // See if it's a local file
        File file = new File(rootFolder, href);
        if(file.exists()) {
            href = "file:///" + file.toString(); //$NON-NLS-1$
        }
        
        //if(BrowserUtils.isBrowserURL(href)) {
            fBrowser.setUrl(href);
        //}
        //else {
        //    Program.launch(href);
        //}
    }

    private void showFile(File file) {
        if(file != null && !file.isDirectory()) {
            String href = "file:///" + file.toString(); //$NON-NLS-1$
            
            //if(BrowserUtils.isBrowserURL(href)) {
                fBrowser.setUrl(href);
            //}
            //else {
            //    Program.launch(file.toString());
            //}
        }
    }

    /**
     * Create the Browser component
     * 
     * @param parent
     * @return
     * @throws BrowserWidgetNotSupportedException
     */
    private Browser createBrowser(Composite parent) throws BrowserWidgetNotSupportedException {
        Browser browser;

        try {
            browser = new ExtendedBrowser(parent, SWT.NONE);
            browser.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        }
        catch(SWTError ex) {
            throw new BrowserWidgetNotSupportedException(ex);
        }
        
        return browser;
    }
    
    public void dispose() {
        
    }
}
