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
package org.tencompetence.ldauthor.ui.views.ldproperties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;
import org.tencompetence.imsldmodel.properties.IPropertyGroupModel;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Composite for editing Level B Properties
 * 
 * @author Phillip Beauvoir
 * @version $Id: PropertyGroupEditorComposite.java,v 1.4 2009/05/19 18:21:04 phillipus Exp $
 */
public class PropertyGroupEditorComposite extends Composite {
    
    private Text fTextTitle;
    private Text fTextIdentifier;
    
    private boolean fIsUpdating;
    
    private IPropertyGroupModel fLDPropertyGroup;
    
    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fLDPropertyGroup == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fTextTitle) {
                String s = fTextTitle.getText();
                fLDPropertyGroup.setTitle(s);
            }
            
            fLDPropertyGroup.getLDModel().setDirty();
        }
    };
    

    public PropertyGroupEditorComposite(Composite parent, int style) {
        super(parent, style);
        
        setLayout(new GridLayout());
        setLayoutData(new GridData(GridData.FILL_BOTH));
        
        Section section = AppFormToolkit.getInstance().createSection(this, Section.TITLE_BAR);
        section.setText(getTitle());
        section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        section.setLayout(new GridLayout());
        section.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        Composite fieldComposite = AppFormToolkit.getInstance().createComposite(section, SWT.NULL);
        fieldComposite.setLayout(new GridLayout(2, false));
        fieldComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Title
        AppFormToolkit.getInstance().createLabel(fieldComposite, Messages.PropertyGroupEditorComposite_0);
        fTextTitle = new Text(fieldComposite, SWT.BORDER);
        fTextTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fTextTitle.addModifyListener(fModifyListener);
        
        // Identifier
        AppFormToolkit.getInstance().createLabel(fieldComposite, Messages.PropertyGroupEditorComposite_1);
        fTextIdentifier = new Text(fieldComposite, SWT.BORDER | SWT.READ_ONLY);
        fTextIdentifier.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        section.setClient(fieldComposite);
    }

    public void setPropertyGroup(IPropertyGroupModel propertyGroup) {
        fLDPropertyGroup = propertyGroup;
        
        fIsUpdating = true;
        
        fTextTitle.setText(StringUtils.safeString(propertyGroup.getTitle()));
        fTextIdentifier.setText(StringUtils.safeString(propertyGroup.getIdentifier()));
        
        fIsUpdating = false;
    }
    
    protected String getTitle() {
        return Messages.PropertyGroupEditorComposite_2;
    }
}
