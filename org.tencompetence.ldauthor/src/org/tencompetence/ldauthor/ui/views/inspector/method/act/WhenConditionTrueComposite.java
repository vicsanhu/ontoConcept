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
package org.tencompetence.ldauthor.ui.views.inspector.method.act;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.expressions.IExpressionType;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;


/**
 * When Condition True Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: WhenConditionTrueComposite.java,v 1.3 2009/06/10 13:12:40 phillipus Exp $
 */
public class WhenConditionTrueComposite extends Composite {

    private ComboViewer fComboRoleViewer;
    
    private IActModel fActModel;

    public WhenConditionTrueComposite(Composite parent, int style) {
        super(parent, style);
        
        AppFormToolkit.getInstance().adapt(this);
        
        GridLayout layout = new GridLayout(2, false);
        setLayout(layout);
        
        AppFormToolkit.getInstance().createLabel(this, Messages.WhenConditionTrueComposite_0);
        
        fComboRoleViewer = new ComboViewer(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        
        fComboRoleViewer.setContentProvider(new IStructuredContentProvider() {
            public Object[] getElements(Object inputElement) {
                return fActModel.getLDModel().getRolesModel().getOrderedRoles().toArray();
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        });
        
        fComboRoleViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ((ITitle)element).getTitle();
            }
        });
        
        fComboRoleViewer.getCombo().addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                StructuredSelection selection = (StructuredSelection)fComboRoleViewer.getSelection();
                if(!selection.isEmpty()) {
                    IRoleModel role = (IRoleModel)selection.getFirstElement();
                    fActModel.getCompleteActType().getWhenConditionTrueType().setRoleRef(role);
                    fActModel.getLDModel().setDirty();
                }
            }
        });
        
        fComboRoleViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        AppFormToolkit.getInstance().createLabel(this, Messages.WhenConditionTrueComposite_2);
        
        Button button = AppFormToolkit.getInstance().createButton(this, Messages.WhenConditionTrueComposite_1, SWT.PUSH);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                IExpressionType expression = fActModel.getCompleteActType().getWhenConditionTrueType().getExpression();
                WhenConditionTrueExpressionEditorDialog dialog = new WhenConditionTrueExpressionEditorDialog(getShell(), expression);
                dialog.open();
            }
        });
    }

    public void setActModel(IActModel actModel) {
        fActModel = actModel;
        
        fComboRoleViewer.setInput(""); //$NON-NLS-1$
        
        // Select Role
        IRoleModel role = actModel.getCompleteActType().getWhenConditionTrueType().getRoleRef();
        
        if(role != null) {
            fComboRoleViewer.setSelection(new StructuredSelection(role));
        }
    }

}
