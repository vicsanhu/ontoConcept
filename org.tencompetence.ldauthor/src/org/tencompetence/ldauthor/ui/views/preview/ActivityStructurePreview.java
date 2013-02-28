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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureRefModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.types.IItemModelType;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.ExtendedBrowser;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;


/**
 * ActivityStructurePreview
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActivityStructurePreview.java,v 1.7 2009/06/16 10:01:00 phillipus Exp $
 */
public class ActivityStructurePreview
implements IEnvironmentOwnerPreview {
    
    protected IActivityStructureModel fActivityStructure;
    
    protected Composite fClient;
    protected CLabel fTitleLabel;
    protected Browser fBrowser;
    protected Composite fLinksPanel;
    
    protected Label fSummaryLabel;
    
    private EnvironmentPreviewDelegate fEnvironmentPreviewDelegate;
    
    public ActivityStructurePreview() {
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
        
        fSummaryLabel = AppFormToolkit.getInstance().createLabel(fClient, ""); //$NON-NLS-1$
        
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
        ScrolledComposite scrolledClient = new ScrolledComposite(parent, SWT.V_SCROLL);
        scrolledClient.setExpandHorizontal(true);
        scrolledClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        AppFormToolkit.getInstance().adapt(scrolledClient);
        
        Composite client = AppFormToolkit.getInstance().createComposite(scrolledClient, SWT.NULL);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, false, true);
        client.setLayoutData(gd);
        client.setLayout(new GridLayout());
        scrolledClient.setContent(client);
        
        PreviewFactory.createSectionLabel(client, Messages.AbstractActivityPreview_0, ImageFactory.getImage(ImageFactory.ICON_VIEW));
        
        Hyperlink link = AppFormToolkit.getInstance().createHyperlink(client, Messages.ActivityStructurePreview_0, SWT.NULL);
        link.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent e) {
                displayItemModelType(fActivityStructure.getInformationModel());
            }
        });
        link.setToolTipText(Messages.ActivityStructurePreview_0);
        
        return client;
    }
    
    /**
     * Display the Item Model type in a Browser
     * @param activity
     */
    protected void displayItemModelType(IItemModelType itemModel) {
        List<IItemType> items = itemModel.getItemTypes();
        if(items.isEmpty()) {
            fBrowser.setText(Messages.AbstractActivityPreview_2);
        }
        else {
            PreviewFactory.displayBrowserItem(items.get(0), fBrowser);
        }
    }
    
    public void update(ILDEditorPart editorPart, Object object) {
        if(object instanceof IActivityStructureRefModel) {
            object = ((IActivityStructureRefModel)object).getLDModelObject();
        }

        if(!(object instanceof IActivityStructureModel)) {
            throw new RuntimeException("Incorrect model type"); //$NON-NLS-1$
        }
        
        fActivityStructure = (IActivityStructureModel)object;
        
        // Title
        fTitleLabel.setText(fActivityStructure.getTitle());
        fTitleLabel.setImage(ImageFactory.getIconLDType(fActivityStructure));
        
        // Summary
        String text = null;
        switch(fActivityStructure.getStructureType()) {
            case IActivityStructureModel.TYPE_SELECTION:
                int num = fActivityStructure.getNumberToSelect();
                if(num == 0) {
                    num = fActivityStructure.getActivityRefs().size();
                }
                text = Messages.ActivityStructurePreview_1 +
                    " " + //$NON-NLS-1$
                    num +
                    " " + //$NON-NLS-1$
                    Messages.ActivityStructurePreview_2; 
                break;

            case IActivityStructureModel.TYPE_SEQUENCE:
                text = Messages.ActivityStructurePreview_3 +
                    " " + //$NON-NLS-1$
                    fActivityStructure.getActivityRefs().size() +
                    " " + //$NON-NLS-1$
                    Messages.ActivityStructurePreview_4;
                break;
        }

        fSummaryLabel.setText(text);
        
        // Display Information as default
        displayItemModelType(fActivityStructure.getInformationModel());
        
        // Collect Environments
        
        List<IEnvironmentModel> envs = new ArrayList<IEnvironmentModel>();
        
        for(ILDModelObjectReference ref : fActivityStructure.getEnvironmentRefs()) {
            IEnvironmentModel env =  (IEnvironmentModel)ref.getLDModelObject();
            if(env != null) {
                envs.add(env);
            }
        }
        
        fEnvironmentPreviewDelegate.display(envs, fLinksPanel);
        
        fClient.layout();
        
        fLinksPanel.layout();
        fLinksPanel.pack();
    }

    public void dispose() {
        fEnvironmentPreviewDelegate.dispose();
    }
}

