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

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormColors;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureRefModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityRefModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityRefModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentRefModel;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.resources.IResourceFileModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.qti.model.QTIUtils;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.utils.BrowserUtils;


/**
 * Factory for returning preview views
 * 
 * @author Phillip Beauvoir
 * @version $Id: PreviewFactory.java,v 1.14 2009/06/16 10:01:00 phillipus Exp $
 */
public class PreviewFactory {
    
    private ActivityStructurePreview fActivityStructurePreview;
    private LearningActivityPreview fLearningActivityPreview;
    private SupportActivityPreview fSupportActivityPreview;
    
    private EnvironmentPreview fEnvironmentPreview;
    private LearningObjectPreview fLearningObjectPreview;
    
    private BrowserPreview fBrowserPreview;

    public ILDPreview getPreview(Object object, Composite parent) {
        // Learning Activity
        if(object instanceof ILearningActivityModel || object instanceof ILearningActivityRefModel) {
            if(fLearningActivityPreview == null) {
                fLearningActivityPreview = new LearningActivityPreview();
                fLearningActivityPreview.createControl(parent);
            }
            return fLearningActivityPreview;
        }
        
        // Support Activity
        if(object instanceof ISupportActivityModel || object instanceof ISupportActivityRefModel) {
            if(fSupportActivityPreview == null) {
                fSupportActivityPreview = new SupportActivityPreview();
                fSupportActivityPreview.createControl(parent);
            }
            return fSupportActivityPreview;
        }

        // Activity Structure
        if(object instanceof IActivityStructureModel || object instanceof IActivityStructureRefModel) {
            if(fActivityStructurePreview == null) {
                fActivityStructurePreview = new ActivityStructurePreview();
                fActivityStructurePreview.createControl(parent);
            }
            return fActivityStructurePreview;
        }

        // Environment
        if(object instanceof IEnvironmentModel || object instanceof IEnvironmentRefModel) {
            if(fEnvironmentPreview == null) {
                fEnvironmentPreview = new EnvironmentPreview();
                fEnvironmentPreview.createControl(parent);
            }
            return fEnvironmentPreview;
        }
        
        // Learning Object
        if(object instanceof ILearningObjectModel) {
            if(fLearningObjectPreview == null) {
                fLearningObjectPreview = new LearningObjectPreview();
                fLearningObjectPreview.createControl(parent);
            }
            return fLearningObjectPreview;
        }
        
        // Resource
        if(object instanceof IResourceModel || object instanceof IResourceFileModel || object instanceof File) {
            if(fBrowserPreview == null) {
                fBrowserPreview = new BrowserPreview();
                fBrowserPreview.createControl(parent);
            }
            return fBrowserPreview;
        }
        
        return null;
    }
    

    /**
     * Display an item in a Browser component
     * 
     * @param itemType
     */
    public static void displayBrowserItem(IItemType itemType, Browser browser) {
        IResourceModel resource = itemType.getLDModel().getResourcesModel().getResource(itemType);
        if(resource == null) {
            browser.setText(""); //$NON-NLS-1$
            return;
        }
        
        // Special display cases such as QTI Tests
        if(QTIUtils.isQTITestResource(resource)) {
            browser.setText(QTIUtils.getFriendlyDisplayForQTITest());
            return;
        }
        
        String href = resource.getHref();
        
        if(href == null || "".equals(href)) { //$NON-NLS-1$
            browser.setText(""); //$NON-NLS-1$
        }
        
        else if(BrowserUtils.isBrowserURL(href)) {
            // See if it's a local file
            File homeFolder = itemType.getLDModel().getRootFolder();
            File file = new File(homeFolder, href);
            if(file.exists()) {
                browser.setUrl("file:///" + file.toString()); //$NON-NLS-1$
            }
            // Or a web page
            else {
                browser.setUrl(href);
            }
        }
        else {
            browser.setText(href);
        }
    }

    public static CLabel createTitleLabel(Composite parent) {
        CLabel label = new CLabel(parent, SWT.NULL);
        label.setFont(JFaceResources.getHeaderFont());
        label.setForeground(AppFormToolkit.getInstance().getColors().getColor(IFormColors.TITLE));
        label.setBackground(new Color[] {
                AppFormToolkit.getInstance().getColors().getColor(IFormColors.H_GRADIENT_START),
                AppFormToolkit.getInstance().getColors().getColor(IFormColors.H_GRADIENT_END) },
                new int[] { 100 }, false);
        
        return label;
    }
    
    public static CLabel createSectionLabel(Composite parent, String text, Image image) {
        CLabel label = new CLabel(parent, SWT.NULL); 
        label.setFont(JFaceResources.getBannerFont());
        label.setForeground(AppFormToolkit.getInstance().getColors().getColor(IFormColors.TITLE));
        label.setBackground(new Color[] {
                AppFormToolkit.getInstance().getColors().getColor(IFormColors.H_GRADIENT_START),
                AppFormToolkit.getInstance().getColors().getColor(IFormColors.H_GRADIENT_END) },
                new int[] { 100 }, false);

        label.setText(text);
        label.setImage(image);
        label.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false));
        
        return label;
    }
    
}
