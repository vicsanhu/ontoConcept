/*
 * Copyright (c) 2008, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.ui.views.inspector.environment;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Inspector Section for Environment
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentSection.java,v 1.7 2009/06/10 10:16:49 phillipus Exp $
 */
public class EnvironmentSection extends AbstractScrolledInspectorSection {
    
    private Text fTextTitle;
    
    private Text fTextID;
    
    private boolean fIsUpdating;
    
    private IEnvironmentModel fEnvironment;
    
    private EnvironmentTableViewer fTableViewer;
    
    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fEnvironment == null) {
                return;
            }
            
            Object control = e.getSource();
            if(control == fTextTitle) {
                String s = fTextTitle.getText();
                fEnvironment.setTitle(s);
            }
            
            fEnvironment.getLDModel().setDirty();
        }
    };

    public EnvironmentSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        setContent(composite);
        
        Label label = factory.createLabel(composite, Messages.EnvironmentSection_0);
        label.setToolTipText(Messages.EnvironmentSection_1);
        fTextTitle = factory.createText(composite, ""); //$NON-NLS-1$
        GridData gd = new GridData(SWT.FILL, SWT.NULL, true, false);
        fTextTitle.setLayoutData(gd);
        fTextTitle.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.EnvironmentSection_2);
        label.setToolTipText(Messages.EnvironmentSection_3);
        
        Composite tableComposite = factory.createComposite(composite);
        tableComposite.setLayout(new TableColumnLayout());
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 160;
        //gd.widthHint = 60;  // stops width creep
        tableComposite.setLayoutData(gd);
        fTableViewer = new EnvironmentTableViewer(tableComposite, SWT.BORDER);
        
        label = factory.createLabel(composite, Messages.EnvironmentSection_4);
        label.setToolTipText(Messages.EnvironmentSection_5);
        fTextID = factory.createText(composite, "", SWT.READ_ONLY); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextID.setLayoutData(gd);
        
        composite.layout();
        composite.pack();
    }

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IEnvironmentModel) {  
            fEnvironment = (IEnvironmentModel)element;
        }
        if(fEnvironment == null) {
            throw new RuntimeException("Environment was null"); //$NON-NLS-1$
        }

        refresh();
    }
    
    public void refresh() {
        if(fEnvironment == null) {
            return;
        }
        
        // Populate fields...
        fIsUpdating = true;
        
        String s = StringUtils.safeString(fEnvironment.getTitle());
        fTextTitle.setText(s);
        
        fTableViewer.setInput(fEnvironment);
        
        fTextID.setText(StringUtils.safeString(fEnvironment.getIdentifier()));

        fIsUpdating = false;
    }
}
