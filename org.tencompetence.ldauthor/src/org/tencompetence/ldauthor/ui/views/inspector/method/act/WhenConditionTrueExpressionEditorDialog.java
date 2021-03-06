/*
 * Copyright (c) 2009, Consortium Board TENCompetence
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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.tencompetence.imsldmodel.expressions.IExpressionType;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.expressions.ExpressionEditorComposite;



/**
 * Editor Window to show Act Completion When Condition True Expression Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: WhenConditionTrueExpressionEditorDialog.java,v 1.4 2009/06/12 16:36:21 phillipus Exp $
 */
public class WhenConditionTrueExpressionEditorDialog extends TitleAreaDialog {

    private static final String DIALOG_SETTINGS_SECTION = "WhenConditionTrueExpressionEditorDialogSettings"; //$NON-NLS-1$
    
    private ExpressionEditorComposite fExpressionEditorComposite;
    
    private IExpressionType fExpressionType;
    
    public WhenConditionTrueExpressionEditorDialog(Shell parentShell, IExpressionType expressionType) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE);
        
        fExpressionType = expressionType;
    }
    
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.WhenConditionTrueExpressionEditorDialog_0);
        shell.setImage(ImageFactory.getImage(ImageFactory.ICON_PROPERTY));
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(final Composite parent) {
        setTitle(Messages.WhenConditionTrueExpressionEditorDialog_1);
        setMessage(Messages.WhenConditionTrueExpressionEditorDialog_2);
        
        Composite composite = new Composite((Composite)super.createDialogArea(parent), SWT.NULL);
        composite.setLayout(new GridLayout());
        
        GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
        composite.setLayoutData(gd);
        
        // Expression editor Composite
        ScrolledComposite scrollContainer = new ScrolledComposite(composite, SWT.V_SCROLL | SWT.BORDER);
        scrollContainer.setExpandHorizontal(true);
        scrollContainer.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        AppFormToolkit.getInstance().adapt(scrollContainer);
        
        fExpressionEditorComposite = new ExpressionEditorComposite(scrollContainer, SWT.NULL);
        scrollContainer.setContent(fExpressionEditorComposite);
        
        /*
         * Set this (first) to be the Composite that needs laying out when children are added or removed
         */
        fExpressionEditorComposite.setData("_top_control_"); //$NON-NLS-1$

        fExpressionEditorComposite.setExpressionType(fExpressionType);
        
        doLayout();
        
        return composite;
    }

    private void doLayout() {
        fExpressionEditorComposite.layout();
        fExpressionEditorComposite.pack();
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, Messages.WhenConditionTrueExpressionEditorDialog_3, true);
    }
    
    @Override
    protected IDialogSettings getDialogBoundsSettings() {
        IDialogSettings settings = LDAuthorPlugin.getDefault().getDialogSettings();
        IDialogSettings section = settings.getSection(DIALOG_SETTINGS_SECTION);
        if(section == null) {
            section = settings.addNewSection(DIALOG_SETTINGS_SECTION);
        } 
        return section;
    }
}
