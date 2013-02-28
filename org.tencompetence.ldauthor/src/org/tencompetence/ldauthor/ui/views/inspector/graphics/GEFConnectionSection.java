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
package org.tencompetence.ldauthor.ui.views.inspector.graphics;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelConnection;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Inspector Section for a GEF Connection
 * 
 * @author Phillip Beauvoir
 * @version $Id: GEFConnectionSection.java,v 1.5 2009/06/10 10:16:49 phillipus Exp $
 */
public class GEFConnectionSection extends AbstractScrolledInspectorSection {
    
    private IGraphicalModelConnection fConnection;
    
    private Text fTextRelationship, fTextName;
    
    private boolean fIsUpdating;
    
    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fTextRelationship) {
                String s = fTextRelationship.getText();
                fConnection.setRelationshipText(s);
            }
            
            fConnection.getLDModel().setDirty();
        }
    };

    public GEFConnectionSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        setContent(composite);
        
        Label label = factory.createLabel(composite, Messages.ConnectionPropertySection_0);
        label.setToolTipText(Messages.ConnectionPropertySection_1);
        fTextName = factory.createText(composite, ""); //$NON-NLS-1$
        GridData gd = new GridData(GridData.FILL_BOTH);
        fTextName.setLayoutData(gd);
        fTextName.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.ConnectionPropertySection_2);
        label.setToolTipText(Messages.ConnectionPropertySection_3);
        fTextRelationship = factory.createText(composite, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextRelationship.setLayoutData(gd);
        fTextRelationship.addModifyListener(fModifyListener);
        
        composite.layout();
        composite.pack();
    }

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IGraphicalModelConnection) {
            fConnection = (IGraphicalModelConnection)element;
        }
        else {
            throw new RuntimeException("Connection was null"); //$NON-NLS-1$
        }
        
        refresh();
    }
    
    public void refresh() {
        // Populate fields...
        fIsUpdating = true;
        
        String s = StringUtils.safeString(fConnection.getRelationshipText());
        fTextRelationship.setText(s);
        
        fTextName.setEnabled(false);
        fTextName.setText(fConnection.toString());
        
        fIsUpdating = false;
    }
}
