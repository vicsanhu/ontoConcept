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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.ldauthor.graphicsmodel.other.IGraphicalNoteModel;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Inspector Section for a GEF Note
 * 
 * @author Phillip Beauvoir
 * @version $Id: GEFNoteSection.java,v 1.5 2009/06/10 10:16:49 phillipus Exp $
 */
public class GEFNoteSection extends AbstractInspectorSection {
    
    private Text fTextContent;
    
    private boolean fIsUpdating;
    
    private IGraphicalNoteModel fNoteModel;
    
    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fNoteModel == null) {
                return;
            }
            
            Object control = e.getSource();

            if(control == fTextContent) {
                String s = fTextContent.getText();
                fNoteModel.setContent(s);
            }
            
            fNoteModel.getLDModel().setDirty();
        }
    };


    public GEFNoteSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        GridData gd;
        
        GridLayout layout = new GridLayout(2, false);
        setLayout(layout);
        
        setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, true));
        
        Label label = InspectorWidgetFactory.getInstance().createLabel(this, Messages.NotePropertySection_0);
        label.setToolTipText(Messages.NotePropertySection_1);
        //gd = new GridData(SWT.FILL, SWT.TOP, false, true);
        //label.setLayoutData(gd);
        
        fTextContent = InspectorWidgetFactory.getInstance().createText(this, "", SWT.MULTI | SWT.V_SCROLL | SWT.WRAP); //$NON-NLS-1$
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 100;
        fTextContent.setLayoutData(gd);
        
        fTextContent.addModifyListener(fModifyListener);
    }

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IGraphicalNoteModel) {
            fNoteModel = (IGraphicalNoteModel)element;
            if(fNoteModel == null) {
                throw new RuntimeException("Note was null"); //$NON-NLS-1$
            }
        }
        else {
            throw new RuntimeException("Should have been a IGraphicalNoteModel"); //$NON-NLS-1$
        }
        
        refresh();
    }
    
    public void refresh() {
        if(fNoteModel == null) {
            return;
        }
        
        // Populate fields...
        fIsUpdating = true;
        
        String s = StringUtils.safeString(fNoteModel.getContent());
        fTextContent.setText(s);

        fIsUpdating = false;
    }
}
