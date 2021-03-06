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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.ExtendedBrowser;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Learning Object Preview
 * 
 * @author Phillip Beauvoir
 * @version $Id: LearningObjectPreview.java,v 1.3 2009/06/16 10:01:00 phillipus Exp $
 */
public class LearningObjectPreview implements ILDPreview {
    
    protected ILearningObjectModel fLearningObject;
    
    protected Composite fClient;
    protected CLabel fTitleLabel;
    protected Browser fBrowser;
    protected Composite fLinksPanel;
    
    private List<Control> fControls = new ArrayList<Control>();
    
    public LearningObjectPreview() {
    }
    
    public void createControl(Composite parent) {
        fClient = AppFormToolkit.getInstance().createComposite(parent);
        fClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        fClient.setLayout(new GridLayout());

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
    
    private Composite createLinksPanel(Composite parent) {
        ScrolledComposite scrolledClient = new ScrolledComposite(parent, SWT.V_SCROLL);
        scrolledClient.setExpandHorizontal(true);
        scrolledClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        AppFormToolkit.getInstance().adapt(scrolledClient);
        
        Composite client = AppFormToolkit.getInstance().createComposite(scrolledClient, SWT.NULL);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, false, true);
        client.setLayoutData(gd);
        client.setLayout(new GridLayout());
        scrolledClient.setContent(client);
        
        PreviewFactory.createSectionLabel(client, Messages.LearningObjectPreview_0, ImageFactory.getImage(ImageFactory.ICON_VIEW));
        
        return client;
    }
    
    public void update(ILDEditorPart editorPart, Object object) {
        if(!(object instanceof ILearningObjectModel)) {
            throw new RuntimeException("Incorrect model type"); //$NON-NLS-1$
        }
        
        fLearningObject = (ILearningObjectModel)object;
        
        // Title
        fTitleLabel.setText(fLearningObject.getTitle());
        fTitleLabel.setImage(ImageFactory.getIconLDType(fLearningObject));
        
        // Dispose controls
        for(Control control : fControls) {
            if(!control.isDisposed()) {
                control.dispose();
            }
        }

        fControls.clear();
        
        // Resource Items
        for(final IItemType item : fLearningObject.getItems().getItemTypes()) {
            String title = StringUtils.isSet(item.getTitle()) ? item.getTitle() : Messages.LearningObjectPreview_1;
            Hyperlink link = AppFormToolkit.getInstance().createHyperlink(fLinksPanel, title, SWT.NULL);
            link.addHyperlinkListener(new HyperlinkAdapter() {
                @Override
                public void linkActivated(HyperlinkEvent e) {
                    PreviewFactory.displayBrowserItem(item, fBrowser);
                }
            });
            link.setToolTipText(title);
            fControls.add(link);
        }
        
        // Default
        if(!fLearningObject.getItems().getItemTypes().isEmpty()) {
            PreviewFactory.displayBrowserItem(fLearningObject.getItems().getItemTypes().get(0), fBrowser);
        }
        else {
            fBrowser.setText(""); //$NON-NLS-1$
        }
        
        fClient.layout();
        
        fLinksPanel.layout();
        fLinksPanel.pack();
    }
}

