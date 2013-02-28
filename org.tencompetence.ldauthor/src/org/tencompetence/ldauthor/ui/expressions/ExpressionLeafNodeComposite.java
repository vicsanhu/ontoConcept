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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.ICompleteExpressionType;
import org.tencompetence.imsldmodel.expressions.IExpressionChoice;
import org.tencompetence.imsldmodel.expressions.IExpressionType;
import org.tencompetence.imsldmodel.expressions.INotType;
import org.tencompetence.imsldmodel.expressions.IPropertyValueType;
import org.tencompetence.imsldmodel.expressions.IUOLHrefType;
import org.tencompetence.imsldmodel.expressions.IUsersInRoleType;
import org.tencompetence.imsldmodel.properties.IPropertyRefModel;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;


/**
 * Composite for Expression Leaf node type
 * 
 * @author Phillip Beauvoir
 * @version $Id: ExpressionLeafNodeComposite.java,v 1.14 2009/06/29 06:50:19 phillipus Exp $
 */
public class ExpressionLeafNodeComposite extends Composite
implements IExpressionComposite {
    
    private ComboViewer fTypeCombo;
    private Label fTypeLabel;
    private Button fAddButton;
    private Composite fSubComposite;
    
    private Object[] fMenuItems;
    
    private IExpressionChoice fParentType;
    private INotType fNotType;
    private ILDModelObject fMemberType;
    private Object fValue;
    
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
                if(fMemberType != null) {
                    updateAddButton();
                }
            }
        }
    };
    
    /**
     * Constructor
     * @param parent
     */
    public ExpressionLeafNodeComposite(Composite parent) {
        super(parent, SWT.NULL);
        
        // Do this now
        fLayoutComposite = getLayoutParentComposite(parent);
        
        GridLayout layout = new GridLayout(4, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing = ExpressionsAdapterFactory.HORIZONTAL_SPACING;
        setLayout(layout);
        
        AppFormToolkit.getInstance().adapt(this);
        
        // Indent this
        GridData gd = new GridData(GridData.FILL, GridData.CENTER, true, true);
        gd.horizontalIndent = 25;
        setLayoutData(gd);
        
        fTypeCombo = new ComboViewer(this, SWT.READ_ONLY);
        gd = new GridData(GridData.FILL, GridData.CENTER, false, true);
        fTypeCombo.getCombo().setLayoutData(gd);
        AppFormToolkit.getInstance().adapt(fTypeCombo.getCombo());
        
        fTypeCombo.setContentProvider(new IStructuredContentProvider() {
            public Object[] getElements(Object inputElement) {
                return new String[] { 
                        "", //$NON-NLS-1$
                        LDModelFactory.NOT,
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
                if(s == LDModelFactory.NOT) {
                    return Messages.ExpressionLeafNodeComposite_0;
                }
                else {
                    return Messages.ExpressionLeafNodeComposite_1;
                }
            }
        });
        
        fTypeCombo.setInput(""); //$NON-NLS-1$
        fTypeCombo.setSelection(new StructuredSelection("")); //$NON-NLS-1$
        
        fTypeCombo.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if(!fIsUpdating) {
                    String type = (String)((StructuredSelection)event.getSelection()).getFirstElement();
                    changeNotType(type);
                }
            }
        });
        
        fTypeLabel = AppFormToolkit.getInstance().createLabel(this, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL, GridData.CENTER, false, true);
        gd.widthHint = 150;
        fTypeLabel.setLayoutData(gd);
        
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if(fMemberType != null) {
                    fMemberType.getLDModel().removePropertyChangeListener(fPropertyChangeListener);
                }
            }
        });
    }
    
    /**
     * Set the composite to show the Expression child member type and value
     * @param memberType 
     */
    public void setExpressionType(IExpressionChoice parentType, ILDModelObject memberType) {
        if(parentType == null || memberType == null) {
            throw new RuntimeException("Null values in setExpressionType"); //$NON-NLS-1$
        }

        fIsUpdating = true;
        
        fParentType = parentType;
        
        if(memberType instanceof INotType) {
            fNotType = (INotType)memberType;
            List<ILDModelObject> members = fNotType.getExpressionMemberTypes();
            fMemberType = members.get(0);
            fTypeCombo.setSelection(new StructuredSelection(LDModelFactory.NOT));
            
            // Sanity check in case someone added "not" type to Property Ref and Property Value types in previous version
            if(fMemberType instanceof IPropertyRefModel || fMemberType instanceof IPropertyValueType) {
                changeNotType(""); //$NON-NLS-1$
                fTypeCombo.getControl().setVisible(false);
            }
        }
        // Property Ref and Property Value types not allowed to have "not" in front of them
        else if(memberType instanceof IPropertyRefModel || memberType instanceof IPropertyValueType) {
            fMemberType = memberType;
            fTypeCombo.getControl().setVisible(false);
        }
        else {
            fMemberType = memberType;
            fTypeCombo.setSelection(new StructuredSelection("")); //$NON-NLS-1$
        }
        
        fValue = ExpressionsAdapterFactory.getValue(fMemberType);
        
        showControls();
        
        fIsUpdating = false;
    }
    
    /**
     * Update the value control based on value from model
     */
    private void showControls() {
        String tag = fMemberType.getTagName();
        
        // Special case for Complete UOL type
        if(fMemberType instanceof ICompleteExpressionType &&
                ((ICompleteExpressionType)fMemberType).getComponentReference() instanceof IUOLHrefType) {
            tag = LDModelFactory.COMPLETE_UOL;
        }
        
        fTypeLabel.setText(ExpressionsAdapterFactory.getFriendlyName(tag));
        
        switch(ExpressionsAdapterFactory.getValueType(fMemberType)) {
            case ExpressionsAdapterFactory.VALUE_TYPE_NONE:
                Label l =  AppFormToolkit.getInstance().createLabel(this, ""); //$NON-NLS-1$
                l.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, true));
                break;
                
            case ExpressionsAdapterFactory.VALUE_TYPE_LIST:
                ExpressionValueControl expressionValueControl = createExpressionValueControl();
                Object[] items = ExpressionsAdapterFactory.getValueList(fParentType.getLDModel(), tag);
                expressionValueControl.setItems(items, fValue);
                break;

            case ExpressionsAdapterFactory.VALUE_TYPE_TEXT:
                Text text = createTextControl();
                if(fValue instanceof String) {
                    text.setText((String)fValue);
                }
                else {
                    text.setText(""); //$NON-NLS-1$
                }
                break;

            default:
                break;
        }
        
        
        // Remove Button
        Button removeButton =  AppFormToolkit.getInstance().createButton(this, null, SWT.NULL);
        removeButton.setImage(ImageFactory.getImage(ImageFactory.ICON_MINUS));
        GridData gd = new GridData();
        gd.heightHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        gd.widthHint = ExpressionsAdapterFactory.BUTTON_SIZE;
        removeButton.setLayoutData(gd);
        
        /*
         * Remove this child member type
         */
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(fNotType != null) {
                    fParentType.removeExpressionMemberType(fNotType);
                }
                else {
                    fParentType.removeExpressionMemberType(fMemberType);
                }
                
                fParentType.getLDModel().setDirty();
                
                dispose();
                
                doLayout();
            }
        });
        
        // If the type is users-in-role we need an add button and some children...
        if(fMemberType instanceof IUsersInRoleType) {
            ((GridLayout)getLayout()).numColumns = 5;
                        
            fAddButton = AppFormToolkit.getInstance().createButton(this, null, SWT.NULL);
            fAddButton.setImage(ImageFactory.getImage(ImageFactory.ICON_PLUS));
            gd = new GridData();
            gd.heightHint = ExpressionsAdapterFactory.BUTTON_SIZE;
            gd.widthHint = ExpressionsAdapterFactory.BUTTON_SIZE;
            fAddButton.setLayoutData(gd);
            
            fAddButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    showMenuItems();
                }
            });
            
            fSubComposite = AppFormToolkit.getInstance().createComposite(this, SWT.NULL);
            GridLayout layout = new GridLayout();
            layout.marginHeight = 0;
            layout.marginWidth = 0;
            fSubComposite.setLayout(layout);
            
            gd = new GridData(GridData.FILL, GridData.FILL, true, true);
            gd.horizontalSpan = 5;
            fSubComposite.setLayoutData(gd);
            
            IExpressionType expressionType = ((IUsersInRoleType)fMemberType).getExpression();
            List<ILDModelObject> members = expressionType.getExpressionMemberTypes();
            
            updateAddButton();
            
            if(!members.isEmpty()) {
                for(ILDModelObject child : members) {
                    ExpressionsAdapterFactory.createExpressionComposite(expressionType, child, fSubComposite);
                }
            }
            
            // Listen to changes
            fMemberType.getLDModel().addPropertyChangeListener(fPropertyChangeListener);
            
            createMenuItems();
            
            doLayout();
        }
    }
    
    private void setValue(Object value) {
        fValue = value;
        updateExpressionValue();
    }
    
    /**
     * Update the Expression value in the LD Model
     */
    private void updateExpressionValue() {
        // Add additional value
        ExpressionsAdapterFactory.updateExpressionValue(fMemberType, fValue);
        fParentType.getLDModel().setDirty();
    }
    
    /**
     * Change type to Not or not Not
     * @param type
     */
    private void changeNotType(String type) {
        if(type == null) {
            return;
        }
        
        // Need a new Not Type added via replacing it...
        if(type.equals(LDModelFactory.NOT) && fNotType == null) {
            fNotType = (INotType)LDModelFactory.createModelObject(LDModelFactory.NOT, fParentType.getLDModel());
            fParentType.replaceExpressionMemberType(fMemberType, fNotType);
            fNotType.addExpressionMemberType(fMemberType);
        }
        else {
            // If we have a Not type, replace it
            if(fNotType != null) {
                fParentType.replaceExpressionMemberType(fNotType, fMemberType);
                fNotType = null;
            }
        }
        
        fParentType.getLDModel().setDirty();
    }

    private void updateAddButton() {
        if(fMemberType instanceof IUsersInRoleType) {
            IExpressionType expressionType = ((IUsersInRoleType)fMemberType).getExpression();
            boolean hasMinimumRequired = !expressionType.getExpressionMemberTypes().isEmpty();
            fAddButton.setImage(hasMinimumRequired ? ImageFactory.getImage(ImageFactory.ICON_PLUS) : 
                ImageFactory.getImage(ImageFactory.ICON_PLUS_RED));
            fAddButton.setEnabled(!hasMinimumRequired);
            fAddButton.setToolTipText(hasMinimumRequired ? "" : Messages.ExpressionLeafNodeComposite_2); //$NON-NLS-1$
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
                        if(fMemberType instanceof IUsersInRoleType) {
                            IExpressionType expressionType = ((IUsersInRoleType)fMemberType).getExpression();
                            ILDModelObject child = ExpressionsAdapterFactory.addNewMemberType(expressionType, choice);
                            ExpressionsAdapterFactory.createExpressionComposite(expressionType, child, fSubComposite);
                            doLayout();
                        }
                    }
                };
            }
        }
    }

    
    /**
     * Create Text Control
     */
    private Text createTextControl() {
        final Text text = AppFormToolkit.getInstance().createText(this, "", SWT.BORDER); //$NON-NLS-1$
        text.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, true));
        
        text.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                // Set in LD Model
                if(!fIsUpdating) {
                    fValue = text.getText();
                    updateExpressionValue();
                }
            }
        });
        
        return text;
    }
    
    /**
     * Create Control
     */
    private ExpressionValueControl createExpressionValueControl() {
        ExpressionValueControl expressionValueControl = new ExpressionValueControl(this);
        
        expressionValueControl.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                Object value = ((StructuredSelection)event.getSelection()).getFirstElement();
                setValue(value);
            }
        });
        
        return expressionValueControl;
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
