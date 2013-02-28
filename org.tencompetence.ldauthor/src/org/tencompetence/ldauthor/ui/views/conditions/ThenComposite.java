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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
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
import org.tencompetence.imsldmodel.expressions.IChangePropertyValueType;
import org.tencompetence.imsldmodel.expressions.IExpressionChoice;
import org.tencompetence.imsldmodel.expressions.IShowHideType;
import org.tencompetence.imsldmodel.expressions.IThenType;
import org.tencompetence.imsldmodel.types.INotificationType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.expressions.ExpressionsAdapterFactory;


/**
 * Panel to edit "Then" condition element
 * 
 * @author Phillip Beauvoir
 * @version $Id: ThenComposite.java,v 1.15 2009/06/19 10:14:48 phillipus Exp $
 */
public class ThenComposite extends Composite {
    
    private Composite fClient;
    
    private Button fAddButton;
    
    private Object[] fMenuItems;

    private IThenType fThenType;
    
    /**
     * The top ancestor that will need laying out when children are added or removed. Could be null.
     */
    private Composite fLayoutComposite;

    
    private PropertyChangeListener fPropertyChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            Object object = evt.getNewValue();
            Object source = evt.getSource();
            
            if(source == fThenType) {
                if(IExpressionChoice.PROPERTY_MEMBER_ADDED.equals(propertyName)) {
                    addChildComposite((ILDModelObject)object);
                    fClient.layout(); // Yes. we need it
                    updateAddButton();
                }
                if(IExpressionChoice.PROPERTY_MEMBER_REMOVED.equals(propertyName)) {
                    if(fThenType.getMembers().isEmpty()) {
                        updateAddButton();
                    }
                }
                
                doLayout();
            }
        }
    };

    
    public ThenComposite(Composite parent, int style) {
        super(parent, style);
        
        // Do this now
        fLayoutComposite = getLayoutParentComposite(parent);
        
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        
        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        AppFormToolkit.getInstance().adapt(this);
        
        AppFormToolkit.getInstance().createHeaderLabel(this, Messages.ThenComposite_1, SWT.NULL);

        // Button
        fAddButton = AppFormToolkit.getInstance().createButton(this, null, SWT.NULL);
        fAddButton.setImage(ImageFactory.getImage(ImageFactory.ICON_PLUS));
        GridData gd = new GridData(GridData.END, GridData.FILL_HORIZONTAL, true, true);
        gd.heightHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        gd.widthHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        fAddButton.setLayoutData(gd);

        fClient = AppFormToolkit.getInstance().createComposite(this, SWT.BORDER);
        AppFormToolkit.getInstance().paintBordersFor(fClient);
        GridLayout layout2 = new GridLayout();
        layout2.marginHeight = 0;
        layout2.marginWidth = 5;
        layout2.marginTop = 5;
        layout2.marginBottom = 5;
        fClient.setLayout(layout2);
        gd = new GridData(GridData.FILL, GridData.FILL, true, true);
        gd.horizontalSpan = 2;
        gd.minimumHeight = 40;
        fClient.setLayoutData(gd);
        
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
                if(fThenType != null) {
                    fThenType.getLDModel().removePropertyChangeListener(fPropertyChangeListener);
                }
            }
        });
        
        createMenuItems();
    }
    
    public void setThenType(IThenType thenType) {
        if(fThenType == thenType) {
            return;
        }
        
        if(fThenType != null) {
            fThenType.getLDModel().removePropertyChangeListener(fPropertyChangeListener);
        }

        fThenType = thenType;

        clearContents();
        
        /*
         * Property Change Listener swapout
         */
        fThenType.getLDModel().addPropertyChangeListener(fPropertyChangeListener);
        
        updateAddButton();
        
        List<ILDModelObject> members = fThenType.getMembers();
        
        if(members.isEmpty()) {
            return;
        }
        
        // Add sub-composites
        for(ILDModelObject memberType : members) {
            addChildComposite(memberType);
        }
        
        // Need this under some circumstances!
        fClient.layout();
    }

    private void clearContents() {
        for(Control control : fClient.getChildren()) {
            if(!control.isDisposed()) {
                control.dispose();
            }
        }
        
        fClient.layout(); 
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
        fMenuItems = new Object[IThenType.CHOICES.length];
        
        for(int i = 0; i < IThenType.CHOICES.length; i++) {
            final String choice = IThenType.CHOICES[i];
            fMenuItems[i] = new Action(ConditionsAdapterFactory.getFriendlyName(choice)) {
                @Override
                public void run() {
                    fThenType.addMember(choice);
                    fThenType.getLDModel().setDirty();
                }
            };
        }
    }

    private void addChildComposite(ILDModelObject memberType) {
        if(memberType instanceof IShowHideType) {
            ShowHideGroupComposite composite = new ShowHideGroupComposite(fClient);
            composite.setShowHideType(fThenType, (IShowHideType)memberType);
        }
        else if(memberType instanceof IChangePropertyValueType) {
            ChangePropertyValueComposite composite = new ChangePropertyValueComposite(fClient);
            composite.setChangePropertyValueType(fThenType, (IChangePropertyValueType)memberType);
        }
        else if(memberType instanceof INotificationType) {
            NotificationComposite composite = new NotificationComposite(fClient);
            composite.setNotificatioType(fThenType, (INotificationType)memberType);
        }
    }
    
    private void updateAddButton() {
        boolean hasMinimumRequired = fThenType.hasCorrectMemberAmount();
        fAddButton.setImage(hasMinimumRequired ? ImageFactory.getImage(ImageFactory.ICON_PLUS) : 
            ImageFactory.getImage(ImageFactory.ICON_PLUS_RED));
        fAddButton.setToolTipText(hasMinimumRequired ? "" : Messages.ShowHideGroupComposite_0); //$NON-NLS-1$
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
