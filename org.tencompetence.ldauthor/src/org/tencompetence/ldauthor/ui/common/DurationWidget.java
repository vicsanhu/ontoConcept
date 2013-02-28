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
package org.tencompetence.ldauthor.ui.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.types.IDurationType;


/**
 * Duration Widget
 * 
 * @author Phillip Beauvoir
 * @version $Id: DurationWidget.java,v 1.7 2009/05/19 18:21:09 phillipus Exp $
 */
public class DurationWidget extends Composite {
    
    private Spinner fYears, fMonths, fDays, fHours, fMins;
    
    private ILDModel fLDModel;

    private boolean fIsUpdating;
    
    private IDurationType fDurationType;

    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            Object control = e.getSource();
            if(control == fYears) {
                fDurationType.setYears(fYears.getSelection());
            }
            else if(control == fMonths) {
                fDurationType.setMonths(fMonths.getSelection());
            }
            else if(control == fDays) {
                fDurationType.setDays(fDays.getSelection());
            }
            else if(control == fHours) {
                fDurationType.setHours(fHours.getSelection());
            }
            else if(control == fMins) {
                fDurationType.setMinutes(fMins.getSelection());
            }

            fLDModel.setDirty();
        }
    };
    
    public DurationWidget(Composite parent, int style) {
        super(parent, style);
        
        AppFormToolkit.getInstance().adapt(this);
        
        GridLayout layout = new GridLayout(5, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        layout.horizontalSpacing = 0;
        setLayout(layout);

        AppFormToolkit.getInstance().createLabel(this, Messages.DurationWidget_0);
        AppFormToolkit.getInstance().createLabel(this, Messages.DurationWidget_1);
        AppFormToolkit.getInstance().createLabel(this, Messages.DurationWidget_2);
        AppFormToolkit.getInstance().createLabel(this, Messages.DurationWidget_3);
        AppFormToolkit.getInstance().createLabel(this, Messages.DurationWidget_4);

        fYears = new Spinner(this, SWT.BORDER);
        fYears.addModifyListener(fModifyListener);
        
        fMonths = new Spinner(this, SWT.BORDER);
        fMonths.setMaximum(11);
        fMonths.addModifyListener(fModifyListener);
        
        fDays = new Spinner(this, SWT.BORDER);
        fDays.addModifyListener(fModifyListener);
        
        fHours = new Spinner(this, SWT.BORDER);
        fHours.setMaximum(23);
        fHours.addModifyListener(fModifyListener);
        
        fMins = new Spinner(this, SWT.BORDER);
        fMins.setMaximum(59);
        fMins.addModifyListener(fModifyListener);
    }
    
    public void setDurationType(IDurationType durationType, ILDModel model) {
        fLDModel = model;
        fDurationType = durationType;
        
        fIsUpdating = true;
        
        fYears.setSelection(durationType.getYears());
        fMonths.setSelection(durationType.getMonths());
        fDays.setSelection(durationType.getDays());
        fHours.setSelection(durationType.getHours());
        fMins.setSelection(durationType.getMinutes());
        
        fIsUpdating = false;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for(Control control : getChildren()) {
            control.setEnabled(enabled);
        }
    }
}
