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
import org.tencompetence.imsldmodel.expressions.ICalculateType;
import org.tencompetence.imsldmodel.expressions.IExpressionChoice;
import org.tencompetence.imsldmodel.expressions.INotType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;


/**
 * Calculate Composite
 * 
 * @author Phillip Beauvoir
 * @version $Id: CalculateComposite.java,v 1.11 2009/06/14 08:06:45 phillipus Exp $
 */
public class CalculateComposite extends Composite
implements IExpressionComposite {

    private ComboViewer fTypeCombo;
    private Button fAddButton;
    
    private IExpressionChoice fParentType;
    private INotType fNotType;
    private ICalculateType fCalculateType;
    
    private Object[] fMenuItems;
    
    private boolean fIsUpdating;
    
    /**
     * The top ancestor that will need laying out when children added or removed. Could be null.
     */
    private Composite fLayoutComposite;

    
    private PropertyChangeListener fPropertyChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            if(IExpressionChoice.PROPERTY_MEMBER_ADDED.equals(propertyName) ||
                    IExpressionChoice.PROPERTY_MEMBER_REMOVED.equals(propertyName)) {
                if(fCalculateType != null) {
                    updateAddButton();
                }
            }
        }
    };
    
    public CalculateComposite(Composite parent) {
        super(parent, SWT.NULL);
        
        // Do this now
        fLayoutComposite = getLayoutParentComposite(parent);
        
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        //layout.marginBottom = 5;
        setLayout(layout);
        
        AppFormToolkit.getInstance().adapt(this);
        
        GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
        gd.horizontalIndent = 25;
        setLayoutData(gd);
        
        Composite topComposite = AppFormToolkit.getInstance().createComposite(this, SWT.NULL);
        GridLayout layout2 = new GridLayout(4, false);
        layout2.marginWidth = 0;
        layout2.marginHeight = 0;
        layout2.horizontalSpacing = ExpressionsAdapterFactory.HORIZONTAL_SPACING;
        topComposite.setLayout(layout2);
        topComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        fTypeCombo = new ComboViewer(topComposite, SWT.READ_ONLY);
        gd = new GridData(GridData.FILL, GridData.CENTER, false, true);
        fTypeCombo.getCombo().setLayoutData(gd);
        fTypeCombo.getCombo().setVisibleItemCount(10);
        AppFormToolkit.getInstance().adapt(fTypeCombo.getCombo());
        
        AppFormToolkit.getInstance().createLabel(topComposite, Messages.CalculateComposite_0, SWT.NULL);
        
        fTypeCombo.setContentProvider(new IStructuredContentProvider() {
            public Object[] getElements(Object inputElement) {
                return new String[] { 
                        LDModelFactory.IS,
                        LDModelFactory.IS_NOT,
                        LDModelFactory.SUM,
                        LDModelFactory.SUBTRACT,
                        LDModelFactory.MULTIPLY,
                        LDModelFactory.DIVIDE,
                        LDModelFactory.GREATER_THAN,
                        LDModelFactory.LESS_THAN
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
                return ExpressionsAdapterFactory.getFriendlyName((String)element);
            }
        });
        
        fTypeCombo.setInput(""); //$NON-NLS-1$
        
        fTypeCombo.setSelection(new StructuredSelection(LDModelFactory.SUM));
        
        fTypeCombo.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if(!fIsUpdating) {
                    String type = (String)((StructuredSelection)event.getSelection()).getFirstElement();
                    changeCalculateType(type);
                }
            }
        });
        
        Button removeButton = AppFormToolkit.getInstance().createButton(topComposite, null, SWT.NULL);
        removeButton.setImage(ImageFactory.getImage(ImageFactory.ICON_MINUS));
        gd = new GridData(GridData.END, GridData.CENTER, true, false);
        gd.heightHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        gd.widthHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        removeButton.setLayoutData(gd);
        
        /*
         * Remove this Operator type
         */
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(fNotType != null) {
                    fParentType.removeExpressionMemberType(fNotType);
                }
                else {
                    fParentType.removeExpressionMemberType(fCalculateType);
                }
                
                fParentType.getLDModel().setDirty();
                
                dispose();
                
                doLayout();
            }
        });
        
        fAddButton = AppFormToolkit.getInstance().createButton(topComposite, null, SWT.NULL);
        fAddButton.setImage(ImageFactory.getImage(ImageFactory.ICON_PLUS));
        fAddButton.setToolTipText(Messages.CalculateComposite_1);
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
        
        createMenuItems();
        
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if(fCalculateType != null) {
                    fCalculateType.getLDModel().removePropertyChangeListener(fPropertyChangeListener);
                }
            }
        });
    }
    
    public void setExpressionType(IExpressionChoice parentType, ILDModelObject memberType) {
        fIsUpdating = true;

        /*
         * Property Change Listener swapout
         */
        if(fParentType != null && fParentType.getLDModel() != parentType.getLDModel()) {
            fParentType.getLDModel().removePropertyChangeListener(fPropertyChangeListener);
        }
        parentType.getLDModel().addPropertyChangeListener(fPropertyChangeListener);
        
        fParentType = parentType;
        
        // We don't really support the Not type
        
        if(memberType instanceof INotType) {
            fNotType = (INotType)memberType;
            List<ILDModelObject> members = fNotType.getExpressionMemberTypes();
            fCalculateType = (ICalculateType)members.get(0);
        }
        else if(memberType instanceof ICalculateType){
            fCalculateType = (ICalculateType)memberType;
        }
        else {
            throw new RuntimeException("Wrong type in setExpressionType: " + memberType); //$NON-NLS-1$
        }
        
        fTypeCombo.setSelection(new StructuredSelection(fCalculateType.getTagName()));

        updateAddButton();
        
        List<ILDModelObject> members = fCalculateType.getExpressionMemberTypes();
        
        if(members.isEmpty()) {
            fIsUpdating = false;
            return;
        }
        
        for(ILDModelObject operatorChild : members) {
            addChildComposite(operatorChild);
        }
        
        // doLayout();
        
        fIsUpdating = false;
    }
    
    private void changeCalculateType(String type) {
        if(type == null) {
            return;
        }
        
        // Ignoring any possible "Not" parent...
        
        // Same one, ignore
        if(!type.equals(fCalculateType.getType())) {
            fCalculateType.setType(type);
            fCalculateType.getLDModel().setDirty();
        }
    }
    
    private void addChildComposite(ILDModelObject child) {
        ExpressionsAdapterFactory.createExpressionComposite(fCalculateType, child, this);
    }
    
    private void updateAddButton() {
        boolean hasMinimumRequired = fCalculateType.hasCorrectMemberAmount();
        fAddButton.setEnabled(!hasMinimumRequired);
        fAddButton.setToolTipText(hasMinimumRequired ? "" : Messages.CalculateComposite_1); //$NON-NLS-1$
        fAddButton.setImage(hasMinimumRequired ? ImageFactory.getImage(ImageFactory.ICON_PLUS)
        		: ImageFactory.getImage(ImageFactory.ICON_PLUS_RED));
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
        fMenuItems = new Object[ExpressionsAdapterFactory.CALCULATE_CHOICES.length];
        
        for(int i = 0; i < ExpressionsAdapterFactory.CALCULATE_CHOICES.length; i++) {
            final String choice = ExpressionsAdapterFactory.CALCULATE_CHOICES[i];
            if(choice == null) {
                fMenuItems[i] = new Separator();
            }
            else {
                fMenuItems[i] = new Action(ExpressionsAdapterFactory.getFriendlyName(choice)) {
                    @Override
                    public void run() {
                        ILDModelObject child = ExpressionsAdapterFactory.addNewMemberType(fCalculateType, choice);
                        addChildComposite(child);
                        doLayout();
                    }
                };
            }
        }
    }
}
