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

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentRefModel;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.ExtendedBrowser;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;


/**
 * EnvironmentPreview
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentPreview.java,v 1.10 2009/06/16 10:01:00 phillipus Exp $
 */
public class EnvironmentPreview
implements IEnvironmentOwnerPreview {
    
    private IEnvironmentModel fEnvironment;
    
    private EnvironmentPreviewDelegate fEnvironmentPreviewDelegate;
    
    protected Composite fClient;
    protected CLabel fTitleLabel;
    protected Browser fBrowser;
    protected Composite fLinksPanel;
    
    public EnvironmentPreview() {
        fEnvironmentPreviewDelegate = new EnvironmentPreviewDelegate(this);
    }
    
    public void createControl(Composite parent) {
        fClient = AppFormToolkit.getInstance().createComposite(parent);
        fClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        fClient.setLayout(new GridLayout());
        fClient.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                dispose();
            }
        });

        fTitleLabel = PreviewFactory.createTitleLabel(fClient);
        GridData gd = new GridData(SWT.FILL, SWT.NULL, true, false);
        fTitleLabel.setLayoutData(gd);
        
        SashForm sash = new SashForm(fClient, SWT.HORIZONTAL);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        //PreviewFactory.getFormToolkit().adapt(sash);
    
        fLinksPanel = createLinksPanel(sash);

        fBrowser = new ExtendedBrowser(sash, SWT.NULL);
        
        sash.setWeights(new int[] { 20, 80 });
    }

    public Composite getClientArea() {
        return fClient;
    }
    
    public Browser getBrowser() {
        return fBrowser;
    }
    
    protected Composite createLinksPanel(Composite parent) {
        Composite client = AppFormToolkit.getInstance().createComposite(parent, SWT.NULL);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, false, true);
        client.setLayoutData(gd);
        client.setLayout(new GridLayout());
        
        return client;
    }
    
    public void update(ILDEditorPart editorPart, Object object) {
        if(object instanceof IEnvironmentRefModel) {
            object = ((IEnvironmentRefModel)object).getLDModelObject();
        }
        
        if(!(object instanceof IEnvironmentModel)) {
            throw new RuntimeException("Incorrect model type"); //$NON-NLS-1$
        }
        
        fEnvironment = (IEnvironmentModel)object;
        
        // Title
        fTitleLabel.setText(fEnvironment.getTitle());
        fTitleLabel.setImage(ImageFactory.getIconLDType(fEnvironment));
        
        fEnvironmentPreviewDelegate.display(fEnvironment, fLinksPanel);
        
        // Default view
        fEnvironmentPreviewDelegate.displayDefaultObject();
        
        fClient.layout();
        fLinksPanel.layout();
    }

    public void dispose() {
        fEnvironmentPreviewDelegate.dispose();
    }
}
