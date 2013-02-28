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
package org.tencompetence.ldauthor.ui.editors.browser;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.part.EditorPart;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.BrowserWidgetNotSupportedException;
import org.tencompetence.ldauthor.ui.common.ExtendedBrowser;
import org.tencompetence.ldauthor.ui.editors.IBrowserEditor;
import org.tencompetence.ldauthor.ui.views.ViewManager;

/**
 * A pseudo-editor containing a Browser component
 * 
 * @author Phillip Beauvoir
 * @version $Id: BrowserEditor.java,v 1.3 2008/07/29 17:19:05 phillipus Exp $
 */
public class BrowserEditor
extends EditorPart
implements IBrowserEditor {

    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".browserEditor"; //$NON-NLS-1$
    
    /**
     * The URL to display
     */
    private String fURL;
    
    /**
     * Form
     */
    private Form fForm;
    
    /**
     * The Browser component
     */
    private Browser fBrowser;
    


    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        if(!(input instanceof BrowserEditorInput)) {
            throw new IllegalArgumentException("Editor Input has to be type BrowserEditorInput"); //$NON-NLS-1$
        }

        setSite(site);
        setInput(input);
        
        fURL = ((BrowserEditorInput)input).getURL();
        
        setPartName(input.getName());
    }

    @Override
    public void createPartControl(Composite parent) {
        fForm = AppFormToolkit.getInstance().createForm(parent);
        
        Composite body = fForm.getBody();
        body.setLayout(new GridLayout());
        AppFormToolkit.getInstance().paintBordersFor(body);
        
        Text label = AppFormToolkit.getInstance().createText(body, "", SWT.BORDER | SWT.READ_ONLY); //$NON-NLS-1$
        label.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        label.setText(fURL == null ? Messages.BrowserEditor_1 : fURL);
        
        // Check preference to show in external browser
        boolean useExternalBrowser = LDAuthorPlugin.getDefault()
                                     .getPreferenceStore().getBoolean(ILDAuthorPreferenceConstants.PREFS_BROWSER_USE_EXTERNAL);
        
        if(useExternalBrowser && fURL != null) {
            ViewManager.showInExternalBrowser(fURL);
        }
        
        try {
            fBrowser = createBrowser(body);
            if(fURL != null) {
                fBrowser.setUrl(fURL);
            }
        }
        catch(BrowserWidgetNotSupportedException ex) {
            ex.printStackTrace();
            doNoBrowser(body);  // Show error message and show in external browser
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

    /**
     * Display label and display URL in external Browser if system
     * does not support the Browser component
     * @param parent 
     */
    private void doNoBrowser(Composite parent) {
        Label label = AppFormToolkit.getInstance().createLabel(parent, Messages.BrowserEditor_0);
        label.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        if(fURL != null) {
            ViewManager.showInExternalBrowser(fURL);
        }
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void setFocus() {
        if(fBrowser != null) {
            fBrowser.setFocus();
        }
    }

    /* (non-Javadoc)
     * @see org.tencompetence.client.ui.editors.IPCMEditor#setDirty(boolean)
     */
    public void setDirty(boolean dirty) {
    }
}
