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
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.activities.IActivityModel;
import org.tencompetence.imsldmodel.activities.IActivityRefModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.types.IItemModelType;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.ExtendedBrowser;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;


/**
 * AbstractActivityPreview
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractActivityPreview.java,v 1.14 2009/06/16 10:01:00 phillipus Exp $
 */
public abstract class AbstractActivityPreview
implements IEnvironmentOwnerPreview {
    
    protected IActivityModel fActivity;
    
    protected Composite fClient;
    protected CLabel fTitleLabel;
    protected Browser fBrowser;
    protected Composite fLinksPanel;
    
    
    private EnvironmentPreviewDelegate fEnvironmentPreviewDelegate;
    
    public AbstractActivityPreview() {
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
        
        Hyperlink link = AppFormToolkit.getInstance().createHyperlink(client, Messages.AbstractActivityPreview_1, SWT.NULL);
        link.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent e) {
                displayItemModelType(fActivity.getDescriptionModel());
            }
        });
        link.setToolTipText(Messages.AbstractActivityPreview_1);
        
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
        IActivityRefModel activityRef = null;
        
        if(object instanceof IActivityRefModel) {
            activityRef = (IActivityRefModel)object;
            object = ((IActivityRefModel)object).getLDModelObject();
        }
        
        if(!(object instanceof IActivityModel)) {
            throw new RuntimeException("Incorrect model type"); //$NON-NLS-1$
        }
        
        fActivity = (IActivityModel)object;
        
        // Title
        fTitleLabel.setText(fActivity.getTitle());
        fTitleLabel.setImage(ImageFactory.getIconLDType(fActivity));
        
        // Display Description as default
        displayItemModelType(fActivity.getDescriptionModel());
        
        /*
         * Collect Environments.
         * If we are previewing an LA or SA Reference and it has an ActivityStructure parent with 1 or more Environment Refs
         * Then the parent's Environments over-ride the ones in the Activity Ref.
         * 
         * "Like the single Activities, an Activity-structure may reference one or more environments. This allows for learning
         * design models where a series of different activities are performed within the same environment. When an
         * activity-structure references one or more environments, then these will overrule the environments specified within the
         * referenced activities.
         * The environments will not be inherited between hierarchical levels of the Activity-structure, allowing environments
         * to be omitted as well. As a consequence, for each hierarchical level of the Activity-structure the appropriate reference
         * to an environment has to be made and possibly repeated."
         * IMS Information Model - p.16
         */
        
        List<IEnvironmentModel> envs = new ArrayList<IEnvironmentModel>();
        
        // This is an Activity Ref, it has a parent which has Environment refs
        if(activityRef != null && activityRef.getParent() != null && !activityRef.getParent().getEnvironmentRefs().isEmpty()) {
            for(ILDModelObjectReference ref : activityRef.getParent().getEnvironmentRefs()) {
                IEnvironmentModel env =  (IEnvironmentModel)ref.getLDModelObject();
                if(env != null) {
                    envs.add(env);
                }
            }
        }
        // This is an Activity
        else {
            for(ILDModelObjectReference ref : fActivity.getEnvironmentRefs()) {
                IEnvironmentModel env =  (IEnvironmentModel)ref.getLDModelObject();
                if(env != null) {
                    envs.add(env);
                }
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
