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
package org.tencompetence.ldauthor.ui.views.conditions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.expressions.IConditionsType;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Panel to edit Conditions element
 * 
 * @author Phillip Beauvoir
 * @version $Id: ConditionsComposite.java,v 1.2 2009/06/16 19:56:43 phillipus Exp $
 */
public class ConditionsComposite extends Composite {
    
    private Text fTitleText;

    private IConditionsType fConditions;
    
    private boolean fIsUpdating;

    public ConditionsComposite(Composite parent) {
        super(parent, SWT.NULL);
        
        AppFormToolkit.getInstance().adapt(this);
        
        GridLayout layout = new GridLayout(2, false);
        setLayout(layout);
        
        AppFormToolkit.getInstance().createLabel(this, Messages.ConditionsComposite_0);
        
        fTitleText =  AppFormToolkit.getInstance().createText(this, "", SWT.BORDER); //$NON-NLS-1$
        fTitleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fTitleText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if(!fIsUpdating && fConditions != null) {
                    fConditions.setTitle(fTitleText.getText());
                    fConditions.getLDModel().setDirty();
                }
            }
        });
    }
    
    public void setConditions(IConditionsType conditions) {
        fConditions = conditions;
        
        fIsUpdating = true;
        
        String title = ""; //$NON-NLS-1$
        
        if(fConditions != null) {
            title = fConditions.getTitle();
            if(!StringUtils.isSet(title)) {
                title = Messages.ConditionsComposite_1;
            }
        }

        fTitleText.setText(title);
        
        fIsUpdating = false;
    }
}
