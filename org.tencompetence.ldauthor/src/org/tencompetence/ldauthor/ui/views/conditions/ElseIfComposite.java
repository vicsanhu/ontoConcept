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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.expressions.IConditionType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.expressions.ExpressionEditorComposite;
import org.tencompetence.ldauthor.ui.expressions.ExpressionsAdapterFactory;


/**
 * Panel to edit Else If element
 * 
 * @author Phillip Beauvoir
 * @version $Id: ElseIfComposite.java,v 1.4 2009/06/17 00:23:56 phillipus Exp $
 */
public class ElseIfComposite extends Composite {
    
    private IConditionType fConditionType;
    
    private ElseOptionComposite fElseParentComposite;
    
    private ExpressionEditorComposite fIfComposite;
    private ThenComposite fThenComposite;
    private ElseOptionComposite fElseOptionComposite;

    public ElseIfComposite(ElseOptionComposite elseParentComposite, Composite parent) {
        super(parent, SWT.NULL);
        
        fElseParentComposite = elseParentComposite;
        
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        setLayout(layout);
        
        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        AppFormToolkit.getInstance().adapt(this);
        
        AppFormToolkit.getInstance().createHeaderLabel(this, Messages.ElseIfComposite_1, SWT.NULL);
        
        Button removeButton = AppFormToolkit.getInstance().createButton(this, null, SWT.NULL);
        removeButton.setImage(ImageFactory.getImage(ImageFactory.ICON_MINUS));
        GridData gd = new GridData(GridData.END, GridData.FILL_HORIZONTAL, true, true);
        gd.heightHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        gd.widthHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        removeButton.setLayoutData(gd);
        
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dispose();
                fConditionType.clearElseType();
                fConditionType.getParent().getLDModel().setDirty();
                fElseParentComposite.showElseComposite();
            }
        });
        
        Composite client = AppFormToolkit.getInstance().createComposite(this, SWT.NULL);
        GridLayout layout2 = new GridLayout();
        layout2.marginHeight = 0;
        layout2.marginWidth = 0;
        layout2.marginLeft = 10;
        client.setLayout(layout2);
        gd = new GridData(GridData.FILL, GridData.FILL, true, true);
        gd.horizontalSpan = 2;
        gd.minimumHeight = 40;
        client.setLayoutData(gd);
        
        // IF
        fIfComposite = new ExpressionEditorComposite(client, SWT.BORDER);
        AppFormToolkit.getInstance().createLabel(client, ""); //$NON-NLS-1$

        // THEN
        fThenComposite = new ThenComposite(client, SWT.NULL);
        AppFormToolkit.getInstance().createLabel(client, ""); //$NON-NLS-1$

        // ELSE
        fElseOptionComposite = new ElseOptionComposite(client);
    }
    
    public void setCondition(IConditionType condition) {
        if(condition == fConditionType) {
            return;
        }
        
        fConditionType = condition;
        
        IConditionType elseIfConditon = fConditionType.getElseIfType().getCondition();
        
        fIfComposite.setExpressionType(elseIfConditon.getIfType());
        fThenComposite.setThenType(elseIfConditon.getThenType());
        fElseOptionComposite.setCondition(elseIfConditon);
    }

}
