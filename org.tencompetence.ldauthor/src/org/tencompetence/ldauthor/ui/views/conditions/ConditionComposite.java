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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.expressions.IConditionType;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.expressions.ExpressionEditorComposite;


/**
 * Panel to edit Condition element
 * 
 * @author Phillip Beauvoir
 * @version $Id: ConditionComposite.java,v 1.7 2009/06/16 22:56:32 phillipus Exp $
 */
public class ConditionComposite extends Composite {
    
    private IConditionType fConditionType;
    
    private ExpressionEditorComposite fIfComposite;
    private ThenComposite fThenComposite;
    private ElseOptionComposite fElseOptionComposite;

    public ConditionComposite(Composite parent) {
        super(parent, SWT.NULL);
        
        /*
         * Set this to be the Composite that needs laying out when children are added or removed
         */
        setData("_top_control_"); //$NON-NLS-1$
        
        GridLayout layout = new GridLayout();
        setLayout(layout);
        
        AppFormToolkit.getInstance().adapt(this);
        
        // IF
        AppFormToolkit.getInstance().createHeaderLabel(this, Messages.ConditionComposite_0, SWT.NULL);
        fIfComposite = new ExpressionEditorComposite(this, SWT.BORDER);
        AppFormToolkit.getInstance().createLabel(this, ""); //$NON-NLS-1$

        // THEN
        fThenComposite = new ThenComposite(this, SWT.NULL);
        AppFormToolkit.getInstance().createLabel(this, ""); //$NON-NLS-1$

        // ELSE
        fElseOptionComposite = new ElseOptionComposite(this);
    }
    
    public void setConditionType(IConditionType condition) {
        if(fConditionType == condition) {
            return;
        }
        
    	fConditionType = condition;
        
    	fIfComposite.setExpressionType(condition.getIfType());
    	fThenComposite.setThenType(condition.getThenType());
    	fElseOptionComposite.setCondition(condition);
    }

}
