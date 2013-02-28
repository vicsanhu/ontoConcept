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
package org.tencompetence.ldauthor.ui.expressions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.expressions.IExpressionChoice;
import org.tencompetence.imsldmodel.expressions.IExpressionType;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.StackComposite;


/**
 * Expression Editor Composite
 * 
 * @author Phillip Beauvoir
 * @version $Id: ExpressionEditorComposite.java,v 1.13 2009/06/17 00:23:56 phillipus Exp $
 */
public class ExpressionEditorComposite extends Composite {
    
    private Composite fClient;
   
    private Button fAddButton;
    
    private StackComposite fStackComposite;
    
    private Composite fButtonComposite;
    
    private Object[] fMenuItems;
    
    private IExpressionType fExpressionType;
    
    private PropertyChangeListener fPropertyChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            Object o = evt.getNewValue();
            
            if(IExpressionChoice.PROPERTY_MEMBER_ADDED.equals(propertyName)) {
                if(fExpressionType != null && fExpressionType.getExpressionMemberTypes().contains(o)) {
                    createMainComposite();
                    showAddButton(false);
                }
            }
            if(IExpressionChoice.PROPERTY_MEMBER_REMOVED.equals(propertyName)) {
                if(fExpressionType != null && fExpressionType.getExpressionMemberTypes().isEmpty()) {
                    showAddButton(true);
                }
            }
        }
    };
    
    public ExpressionEditorComposite(Composite parent, int style) {
        super(parent, style);

        AppFormToolkit.getInstance().adapt(this);
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        
        setLayoutData(new GridData(GridData.FILL, SWT.NULL, true, false));
        
        fStackComposite = new StackComposite(this, SWT.NULL);
        fStackComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        fClient = AppFormToolkit.getInstance().createComposite(fStackComposite, SWT.NULL);
        AppFormToolkit.getInstance().paintBordersFor(fClient);
        GridLayout layout2 = new GridLayout();
        layout2.marginHeight = 0;
        layout2.marginWidth = 5;
        layout2.marginTop = 5;
        layout2.marginBottom = 5;
        fClient.setLayout(layout2);
        
        // Button
        fButtonComposite = AppFormToolkit.getInstance().createComposite(fStackComposite, SWT.NULL);
        fButtonComposite.setLayout(new GridLayout());
        fAddButton = AppFormToolkit.getInstance().createButton(fButtonComposite, Messages.ExpressionEditorComposite_0, SWT.NULL);
        //fAddButton.setLayoutData(new GridData(GridData.END, SWT.NULL, true, false));
        fAddButton.setLayoutData(new GridData(SWT.NULL, SWT.NULL, true, false));
        
        /*
         * Add a new child member type
         */
        fAddButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                showMenuItems();
            }
        });
        
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if(fExpressionType != null) {
                    fExpressionType.getLDModel().removePropertyChangeListener(fPropertyChangeListener);
                }
            }
        });
        
        createMenuItems();
    }

    public void setExpressionType(IExpressionType expressionType) {
        if(fExpressionType == expressionType) {
            return;
        }
        
        if(fExpressionType != null) {
            fExpressionType.getLDModel().removePropertyChangeListener(fPropertyChangeListener);
        }
                
        clearContents();
        
        if(expressionType == null) {
            fExpressionType = null;
            return;
        }
        
        expressionType.getLDModel().addPropertyChangeListener(fPropertyChangeListener);
        
        fExpressionType = expressionType;
        
        List<ILDModelObject> members = expressionType.getExpressionMemberTypes();
        showAddButton(members.isEmpty());
        
        createMainComposite();
    }
    
    private void clearContents() {
        for(Control control : fClient.getChildren()) {
            if(!control.isDisposed()) {
                control.dispose();
            }
        }
        
        fClient.layout(); 
    }
    
    private void showAddButton(boolean show) {
        if(show) {
            fStackComposite.setControl(fButtonComposite);
        }
        else {
            fStackComposite.setControl(fClient);
            fClient.layout(); // Need this on Mac!
        }
    }
    
    private void showMenuItems() {
        MenuManager menuManager = new MenuManager();
        for(Object item : fMenuItems) {
            if(item instanceof IContributionItem) {
                menuManager.add((IContributionItem)item);
            }
            else {
                menuManager.add((IAction)item);
            }
        }
        Menu menu = menuManager.createContextMenu(fAddButton.getShell());
        menu.setVisible(true);
    }
    
    private void createMenuItems() {
        fMenuItems = new Object[ExpressionsAdapterFactory.EXPRESSION_CHOICES.length];
        
        for(int i = 0; i < ExpressionsAdapterFactory.EXPRESSION_CHOICES.length; i++) {
            final String choice = ExpressionsAdapterFactory.EXPRESSION_CHOICES[i];
            if(choice == null) {
                fMenuItems[i] = new Separator();
            }
            else {
                fMenuItems[i] = new Action(ExpressionsAdapterFactory.getFriendlyName(choice)) {
                    @Override
                    public void run() {
                        ExpressionsAdapterFactory.addNewMemberType(fExpressionType, choice);
                    }
                };
            }
        }
    }
    
    private void createMainComposite() {
        List<ILDModelObject> members = fExpressionType.getExpressionMemberTypes();
        
        if(members.isEmpty()) {
            return;
        }
        
        // One child member
        ILDModelObject child = members.get(0);
        
        // Kludge to outdent top control
        Control c = (Control)ExpressionsAdapterFactory.createExpressionComposite(fExpressionType, child, fClient);
        ((GridData)c.getLayoutData()).horizontalIndent = 0;
        
        fClient.layout(); 
    }
}
