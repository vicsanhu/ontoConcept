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
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IExpressionChoice;
import org.tencompetence.imsldmodel.expressions.IShowHideType;
import org.tencompetence.imsldmodel.expressions.IThenType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.expressions.ExpressionsAdapterFactory;


/**
 * Show/Hide Group Composite
 * 
 * @author Phillip Beauvoir
 * @version $Id: ShowHideGroupComposite.java,v 1.12 2009/06/16 22:56:32 phillipus Exp $
 */
public class ShowHideGroupComposite extends Composite {
    
    private ComboViewer fTypeCombo;
    private Button fAddButton;
    
    private IThenType fThenType;
    private IShowHideType fShowHideType;
    
    private Object[] fMenuItems;
    
    private boolean fIsUpdating;
    
    /**
     * The top ancestor that will need laying out when children are added or removed. Could be null.
     */
    private Composite fLayoutComposite;
    
    
    private PropertyChangeListener fPropertyChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            Object source = evt.getSource();
            
            if(source == fShowHideType) {
                if(IExpressionChoice.PROPERTY_MEMBER_ADDED.equals(propertyName)) {
                    updateAddButton();
                }
                
                if(IExpressionChoice.PROPERTY_MEMBER_REMOVED.equals(propertyName)) {
                    updateAddButton();
                }
            }
        }
    };
    

    public ShowHideGroupComposite(Composite parent) {
        super(parent, SWT.NULL);
        
        // Do this now
        fLayoutComposite = getLayoutParentComposite(parent);
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 5;
        layout.marginWidth = 0;
        setLayout(layout);
        
        AppFormToolkit.getInstance().adapt(this);
        
        GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
        setLayoutData(gd);
        
        Composite topComposite = AppFormToolkit.getInstance().createComposite(this, SWT.NULL);
        GridLayout layout2 = new GridLayout(3, false);
        layout2.marginWidth = 0;
        layout2.marginHeight = 0;
        layout2.horizontalSpacing = ExpressionsAdapterFactory.HORIZONTAL_SPACING;
        topComposite.setLayout(layout2);
        topComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        fTypeCombo = new ComboViewer(topComposite, SWT.READ_ONLY);
        gd = new GridData(GridData.FILL, GridData.CENTER, false, true);
        fTypeCombo.getCombo().setLayoutData(gd);
        AppFormToolkit.getInstance().adapt(fTypeCombo.getCombo());
        
        fTypeCombo.setContentProvider(new IStructuredContentProvider() {
            public Object[] getElements(Object inputElement) {
                return new String[] { 
                        LDModelFactory.SHOW,
                        LDModelFactory.HIDE,
                };
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        });
        
        fTypeCombo.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                String s = (String)element;
                return ConditionsAdapterFactory.getFriendlyName(s);
            }
        });
        
        fTypeCombo.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if(!fIsUpdating) {
                    String type = (String)((StructuredSelection)event.getSelection()).getFirstElement();
                    changeShowHideType(type);
                }
            }
        });
        
        fTypeCombo.setInput(""); //$NON-NLS-1$
        fTypeCombo.setSelection(new StructuredSelection("")); //$NON-NLS-1$
        
        Button removeButton = AppFormToolkit.getInstance().createButton(topComposite, null, SWT.NULL);
        removeButton.setImage(ImageFactory.getImage(ImageFactory.ICON_MINUS));
        gd = new GridData(GridData.END, SWT.CENTER, true, false);
        gd.heightHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        gd.widthHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        removeButton.setLayoutData(gd);
        
        /*
         * Remove this group
         */
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dispose(); // This first
                fThenType.removeMember(fShowHideType);
                fThenType.getLDModel().setDirty();
            }
        });
        
        fAddButton = AppFormToolkit.getInstance().createButton(topComposite, null, SWT.NULL);
        fAddButton.setImage(ImageFactory.getImage(ImageFactory.ICON_PLUS));
        gd = new GridData();
        gd.heightHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        gd.widthHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        fAddButton.setLayoutData(gd);
        
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
                if(fShowHideType != null) {
                    fShowHideType.getLDModel().removePropertyChangeListener(fPropertyChangeListener);
                }
            }
        });
        
        createMenuItems();
    }

    public void setShowHideType(IThenType thenType, IShowHideType showHideType) {
        fIsUpdating = true;
        
        /*
         * Property Change Listener swapout
         */
        if(fThenType != null && fThenType.getLDModel() != thenType.getLDModel()) {
            fThenType.getLDModel().removePropertyChangeListener(fPropertyChangeListener);
        }
        thenType.getLDModel().addPropertyChangeListener(fPropertyChangeListener);
        
        fThenType = thenType;
        fShowHideType = showHideType;
        
        fTypeCombo.setSelection(new StructuredSelection(fShowHideType.getType()));
        
        updateAddButton();
        
        List<ILDModelObject> members = fShowHideType.getMemberReferences();
        
        if(members.isEmpty()) {
            fIsUpdating = false;
            return;
        }
        
        for(ILDModelObject member : members) {
            addChildComposite(member);
        }
        
        fIsUpdating = false;
    }
    
    private void changeShowHideType(String type) {
        if(type != null) {
            fShowHideType.setType(type);
            fThenType.getLDModel().setDirty();
        }
    }
    
    private void addChildComposite(ILDModelObject child) {
        ShowHideMemberComposite composite = new ShowHideMemberComposite(this, fShowHideType);
        composite.setElement(child);
    }
    
    private void addNewChild(String type) {
        ShowHideMemberComposite composite = new ShowHideMemberComposite(this, fShowHideType);

        if(LDModelFactory.CLASS.equals(type)) {
            ILDModelObject element = LDModelFactory.createModelObject(type, fShowHideType.getLDModel());
            fShowHideType.addMember(element);
            fShowHideType.getLDModel().setDirty();
            composite.setElement(element);
        }
        else if(LDModelFactory.UOL_HREF.equals(type)) {
            ILDModelObject element = LDModelFactory.createModelObject(type, fShowHideType.getLDModel());
            fShowHideType.addMember(element);
            fShowHideType.getLDModel().setDirty();
            composite.setElement(element);
        }
        else {
            // yet to be selected object reference...
            composite.setType(type);
        }

        doLayout(); // Need to show it though
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
    
    private void updateAddButton() {
        boolean hasMinimumRequired = fShowHideType.getMemberReferences().size() > 0;
        fAddButton.setImage(hasMinimumRequired ? ImageFactory.getImage(ImageFactory.ICON_PLUS) : 
            ImageFactory.getImage(ImageFactory.ICON_PLUS_RED));
        fAddButton.setToolTipText(hasMinimumRequired ? "" : Messages.ShowHideGroupComposite_0); //$NON-NLS-1$
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
        fMenuItems = new Object[ConditionsAdapterFactory.SHOW_HIDE_CHOICES.length];
        
        for(int i = 0; i < ConditionsAdapterFactory.SHOW_HIDE_CHOICES.length; i++) {
            final String choice = ConditionsAdapterFactory.SHOW_HIDE_CHOICES[i];
            if(choice == null) {
                fMenuItems[i] = new Separator();
            }
            else {
                fMenuItems[i] = new Action(ConditionsAdapterFactory.getFriendlyName(choice)) {
                    @Override
                    public void run() {
                        addNewChild(choice);
                    }
                };
            }
        }
    }

}
