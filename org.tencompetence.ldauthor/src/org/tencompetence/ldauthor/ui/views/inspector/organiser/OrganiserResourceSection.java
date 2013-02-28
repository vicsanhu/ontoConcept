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
package org.tencompetence.ldauthor.ui.views.inspector.organiser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.ldauthor.organisermodel.IOrganiserResource;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserIndex;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;


/**
 * Inspector Section for an LD Organiser Entry
 * 
 * @author Phillip Beauvoir
 * @version $Id: OrganiserResourceSection.java,v 1.4 2009/06/10 10:16:49 phillipus Exp $
 */
public class OrganiserResourceSection extends AbstractScrolledInspectorSection {
    
    private Text fTextName, fTextLocation, fTextType;
    
    private IOrganiserResource fOrganiserResource;
    
    private boolean fIsUpdating;
    
    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(!fIsUpdating) {
                if(e.getSource() == fTextLocation) {
                    fOrganiserResource.setResourceLocation(fTextLocation.getText());
                }
                if(e.getSource() == fTextType) {
                    fOrganiserResource.setResourceType(fTextType.getText());
                }
                OrganiserIndex.getInstance().setDirty(true);
            }
        }
    };

    public OrganiserResourceSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        GridData gd;
        
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        setContent(composite);
        
        factory.createLabel(composite, Messages.OrganiserResourceEntryPropertySection_0);
        
        fTextName = factory.createText(composite, "", SWT.READ_ONLY | SWT.BORDER); //$NON-NLS-1$
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        fTextName.setLayoutData(gd);

        factory.createLabel(composite, Messages.OrganiserResourceEntryPropertySection_1);
        
        fTextLocation = factory.createText(composite, "", SWT.BORDER); //$NON-NLS-1$
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        fTextLocation.setLayoutData(gd);
        fTextLocation.addModifyListener(fModifyListener);
        
        factory.createLabel(composite, Messages.OrganiserResourceEntryPropertySection_2);
        
        fTextType = factory.createText(composite, "", SWT.BORDER); //$NON-NLS-1$
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        fTextType.setLayoutData(gd);
        fTextType.addModifyListener(fModifyListener);

        composite.layout();
        composite.pack();
    }

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IOrganiserResource) {
            fOrganiserResource = (IOrganiserResource)element;
        }
        else {
            throw new RuntimeException("Should have been a Resource Organiser Entry"); //$NON-NLS-1$
        }
        
        refresh();
    }
    
    public void refresh() {
        fIsUpdating = true;
        fTextName.setText(fOrganiserResource.getName());
        fTextLocation.setText(fOrganiserResource.getResourceLocation());
        fTextType.setText(fOrganiserResource.getResourceType());
        fIsUpdating = false;
    }
}
