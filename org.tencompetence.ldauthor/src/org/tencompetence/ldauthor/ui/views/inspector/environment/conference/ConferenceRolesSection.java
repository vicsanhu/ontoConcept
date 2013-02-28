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
package org.tencompetence.ldauthor.ui.views.inspector.environment.conference;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.tencompetence.imsldmodel.environments.IConferenceModel;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;


/**
 * Inspector Section for Roles in Conference
 * 
 * @author Phillip Beauvoir
 * @version $Id: ConferenceRolesSection.java,v 1.6 2009/06/10 10:16:49 phillipus Exp $
 */
public class ConferenceRolesSection extends AbstractInspectorSection {
    
    private IConferenceModel fConference;
    
    private ConferenceRoleSelectionViewer fTableViewer;

    public ConferenceRolesSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        GridLayout layout = new GridLayout();
        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, true));
        
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Label label = factory.createLabel(this, Messages.ConferenceRolesSection_0);
        label.setToolTipText(Messages.ConferenceRolesSection_1);
        
        // Use a parent composite for the table with TableColumnLayout.
        // This allows for auto-resizing of table columns        
        Composite c1 = factory.createComposite(this);
        c1.setLayout(new TableColumnLayout());
        
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        //gd.widthHint = 60;  // stops width creep
        c1.setLayoutData(gd);
        
        fTableViewer = new ConferenceRoleSelectionViewer(c1, SWT.BORDER);
    }

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IConferenceModel) {
            fConference = (IConferenceModel)element;
        }
        else {
            throw new RuntimeException("Should have been a Conference Part"); //$NON-NLS-1$
        }

        refresh();
    }
    
    public void refresh() {
        if(fConference == null) {
            return;
        }
        
        fTableViewer.setConference(fConference);
    }
}
