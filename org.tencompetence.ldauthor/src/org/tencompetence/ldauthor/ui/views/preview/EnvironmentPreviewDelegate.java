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
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.environments.IConferenceModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentRefModel;
import org.tencompetence.imsldmodel.environments.IIndexSearchModel;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.environments.IMonitorModel;
import org.tencompetence.imsldmodel.environments.ISendMailModel;
import org.tencompetence.imsldmodel.environments.IServiceModel;
import org.tencompetence.imsldmodel.types.IEmailDataType;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;


/**
 * EnvironmentPreviewDelegate
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentPreviewDelegate.java,v 1.8 2009/05/19 18:21:04 phillipus Exp $
 */
public class EnvironmentPreviewDelegate {

    private List<Control> fControls = new ArrayList<Control>();

    private List<ILearningObjectModel> fLearningObjects = new ArrayList<ILearningObjectModel>();
    private List<IServiceModel> fServices = new ArrayList<IServiceModel>();
    
    private IEnvironmentOwnerPreview fPreview;
    
    public EnvironmentPreviewDelegate(IEnvironmentOwnerPreview preview) {
        fPreview = preview;
    }

    public void display(IEnvironmentModel environment, Composite parent) {
        List<IEnvironmentModel> envs = new ArrayList<IEnvironmentModel>();
        envs.add(environment);
        display(envs, parent);
    }
    
    /**
     * Display the contents of the Environments
     * 
     * @param envs
     */
    public void display(List<IEnvironmentModel> envs, Composite parent) {
        // Dispose controls
        for(Control control : fControls) {
            if(!control.isDisposed()) {
                control.dispose();
            }
        }

        fControls.clear();
        fLearningObjects.clear();
        fServices.clear();
        
        // Collect Environment components
        for(IEnvironmentModel environmentModel : envs) {
            parseEnvironment(environmentModel);
        }
        
        // Add Learning Objects
        if(!fLearningObjects.isEmpty()) {
            CLabel label = PreviewFactory.createSectionLabel(parent, Messages.EnvironmentPreviewDelegate_0, ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_16));
            fControls.add(label);
            
            for(ILearningObjectModel lo : fLearningObjects) {
                addLDObject(lo, parent);
            }
        }

        // Add Services
        if(!fServices.isEmpty()) {
            CLabel label = PreviewFactory.createSectionLabel(parent, Messages.EnvironmentPreviewDelegate_1, ImageFactory.getImage(ImageFactory.ICON_SERVICE));
            fControls.add(label);
            
            for(IServiceModel service : fServices) {
                addLDObject(service, parent);
            }
        }
    }
    
    private void parseEnvironment(IEnvironmentModel env) {
        List<ILDModelObject> children = env.getChildren();
        
        for(ILDModelObject modelObject : children) {
            if(modelObject instanceof ILearningObjectModel && !fLearningObjects.contains(modelObject)) {
                fLearningObjects.add((ILearningObjectModel)modelObject);
            }
            else if(modelObject instanceof IServiceModel && !fServices.contains(modelObject)) {
                fServices.add((IServiceModel)modelObject);
            }
            else if(modelObject instanceof IEnvironmentRefModel) {
                IEnvironmentModel subEnv = (IEnvironmentModel)((ILDModelObjectReference)modelObject).getLDModelObject();
                if(subEnv != null) {
                    parseEnvironment(subEnv);
                }
            }
        }
    }

    private void addLDObject(final ILDModelObject object, Composite parent) {
        String title = ((ITitle)object).getTitle();
        
        Hyperlink link = AppFormToolkit.getInstance().createHyperlink(parent, title, SWT.NULL);
        link.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent e) {
                displayObject(object);
            }
        });
        link.setToolTipText(title);
        fControls.add(link);
    }
    
    /**
     * Display the default (first) object in the Environment
     */
    public void displayDefaultObject() {
        if(!fLearningObjects.isEmpty()) {
            displayObject(fLearningObjects.get(0));
        }
        else if(!fServices.isEmpty()) {
            displayObject(fServices.get(0));
        }
        else {
            fPreview.getBrowser().setText(Messages.AbstractActivityPreview_2);
        }
    }
    
    /**
     * Dsiplay an object in the browser
     * @param object
     */
    private void displayObject(ILDModelObject object) {
        if(object instanceof ILearningObjectModel) {
            List<IItemType> items = ((ILearningObjectModel)object).getItems().getItemTypes();
            if(!items.isEmpty()) {
                PreviewFactory.displayBrowserItem(items.get(0), fPreview.getBrowser());
            }
        }
        else if(object instanceof IConferenceModel) {
            List<IItemType> items = ((IConferenceModel)object).getItem().getItemTypes();
            if(!items.isEmpty()) {
                PreviewFactory.displayBrowserItem(items.get(0), fPreview.getBrowser());
            }
        }
        else if(object instanceof IMonitorModel) {
            List<IItemType> items = ((IMonitorModel)object).getItems().getItemTypes();
            if(!items.isEmpty()) {
                PreviewFactory.displayBrowserItem(items.get(0), fPreview.getBrowser());
            }
        }
        else if(object instanceof ISendMailModel) {
            String text = "<html>" + //$NON-NLS-1$
                          Messages.EnvironmentPreviewDelegate_2 +
                          "<br><br>"; //$NON-NLS-1$
            for(IEmailDataType emailDataType : ((ISendMailModel)object).getEmailDataTypes()) {
                text += emailDataType.getRole().getTitle() + "<br>"; //$NON-NLS-1$
            }
            fPreview.getBrowser().setText(text);
        }
        else if(object instanceof IIndexSearchModel) {
            fPreview.getBrowser().setText(Messages.EnvironmentPreviewDelegate_3);
        }
    }
    
    public void dispose() {
        fControls.clear();
        fLearningObjects.clear();
        fServices.clear();
    }
}
