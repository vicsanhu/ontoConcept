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
package org.tencompetence.ldauthor.ui.views.conditions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.tencompetence.imsldmodel.expressions.IConditionType;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.StackComposite;
import org.tencompetence.ldauthor.ui.expressions.ExpressionsAdapterFactory;


/**
 * Composite to show Else or Else-If option
 * 
 * @author Phillip Beauvoir
 * @version $Id: ElseOptionComposite.java,v 1.6 2009/06/17 00:23:56 phillipus Exp $
 */
public class ElseOptionComposite extends Composite {
    
    private IConditionType fConditionType;
    
    private Button fAddButton;
    private StackComposite fStackComposite;
    private Composite fButtonComposite;
    
    private Composite fClient;
    
    private Object[] fMenuItems;
    
    /**
     * The top ancestor that will need laying out when children are added or removed. Could be null.
     */
    private Composite fLayoutComposite;

    
    public ElseOptionComposite(Composite parent) {
        super(parent, SWT.NULL);
        
        // Do this now
        fLayoutComposite = getLayoutParentComposite(parent);
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing = ExpressionsAdapterFactory.HORIZONTAL_SPACING;
        setLayout(layout);
        
        AppFormToolkit.getInstance().adapt(this);
        
        setLayoutData(new GridData(GridData.FILL_BOTH));
        
        fStackComposite = new StackComposite(this, SWT.NULL);
        fStackComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        fClient = AppFormToolkit.getInstance().createComposite(fStackComposite, SWT.NULL);
        AppFormToolkit.getInstance().paintBordersFor(fClient);
        GridLayout layout2 = new GridLayout();
        layout2.marginHeight = 0;
        layout2.marginWidth = 0;
        fClient.setLayout(layout2);
        
        // Button
        fButtonComposite = AppFormToolkit.getInstance().createComposite(fStackComposite, SWT.NULL);
        GridLayout layout3 = new GridLayout();
        layout3.marginHeight = 0;
        layout3.marginWidth = 0;
        fButtonComposite.setLayout(layout3);
        
        fAddButton = AppFormToolkit.getInstance().createButton(fButtonComposite, Messages.ElseOptionComposite_0, SWT.NULL);
        fAddButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                showMenuItems();
            }
        });
        
        createMenuItems();
    }

    public void setCondition(IConditionType condition) {
        if(fConditionType == condition) {
            return;
        }
        
        fConditionType = condition;
        
        clearContents();
        
        showElseComposite();
    }
    
    void showElseComposite() {
        if(fConditionType.hasElseType()) {
            ElseComposite composite = new ElseComposite(this, fClient);
            composite.setConditionType(fConditionType);
            showAddButton(false);
        }
        else if(fConditionType.hasElseIfType()) {
            ElseIfComposite composite = new ElseIfComposite(this, fClient);
            composite.setCondition(fConditionType);
            showAddButton(false);
        }
        else {
            showAddButton(true);
        }
        
        doLayout();
    }
    
    private void clearContents() {
        for(Control control : fClient.getChildren()) {
            if(!control.isDisposed()) {
                control.dispose();
            }
        }
        
        //fClient.layout(); 
    }

    private void showAddButton(boolean show) {
        if(show) {
            fStackComposite.setControl(fButtonComposite);
        }
        else {
            fStackComposite.setControl(fClient);
            fClient.layout(); // Need this!
        }
    }
    
    private void addNewChild(String type) {
        if(type.equals("else")) { //$NON-NLS-1$
            fConditionType.getElseType();
        }
        else {
            fConditionType.getElseIfType();
        }
        
        fConditionType.getParent().getLDModel().setDirty();
        
        showElseComposite();
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
        fMenuItems = new Object[2];
        
        String[] choices = new String[] {
                "else", //$NON-NLS-1$
                "elseif" //$NON-NLS-1$
        };
        
        for(int i = 0; i < fMenuItems.length; i++) {
            final String choice = choices[i];
            fMenuItems[i] = new Action(ConditionsAdapterFactory.getFriendlyName(choice)) {
                @Override
                public void run() {
                    addNewChild(choice);
                }
            };
        }
    }

    /**
     * Re-layout
     */
    private void doLayout() {
        if(fLayoutComposite != null) {
            fLayoutComposite.layout();
            fLayoutComposite.pack();
        }
    }
    
    private Composite getLayoutParentComposite(Composite parent) {
        while(parent != null && !"_top_control_".equals(parent.getData())) { //$NON-NLS-1$
            parent = parent.getParent();
        }
        return parent;
    }
}
