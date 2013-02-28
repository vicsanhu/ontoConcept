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
package org.tencompetence.ldauthor.ui.views.inspector.ldproperties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.expressions.IPropertyRefValuePairOwner;
import org.tencompetence.imsldmodel.expressions.IPropertyValueType;
import org.tencompetence.imsldmodel.properties.IPropertyGroupModel;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.imsldmodel.types.ICompleteType;
import org.tencompetence.imsldmodel.types.IOnCompletionType;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Table for editing Property Values.
 * Can be used for:
 * 1). Complete UOL/Play/Act/Activity When a Property Value is Set
 * 2). On Completion of UOL/Play/Act/Activity Set a Property Value
 * 
 * @author Phillip Beauvoir
 * @version $Id: PropertyValueTableViewer.java,v 1.8 2009/07/02 11:33:56 phillipus Exp $
 */
public class PropertyValueTableViewer
extends TableViewer implements PropertyChangeListener {

    private ILDModel fCurrentLDModel;
    
    private List<IPropertyTypeModel> fLookUp = new ArrayList<IPropertyTypeModel>();
    
    /**
     * The Column Names
     */
    private static String[] columnNames = {
            Messages.PropertyValueTableViewer_0,
            Messages.PropertyValueTableViewer_1,
            Messages.PropertyValueTableViewer_2
    };

    /**
     * Constructor
     * 
     * @param parent
     */
    public PropertyValueTableViewer(Composite parent) {
        /*
         * SWT.FULL_SELECTION is VERY important, otherwise you can't edit columns
         */
        super(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER);
        
        setEditors();
        setCellModifier(new PropertyValueTableCellModifier());
        setLabelProvider(new PropertyValueTableLabelProvider());
        setContentProvider(new PropertyValueTableContentProvider());
        
        setColumns();
        
        // Ensure we remove any model listener on dispose of this
        getTable().addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if(fCurrentLDModel != null) {
                    fCurrentLDModel.removePropertyChangeListener(PropertyValueTableViewer.this);
                }
            }
        });
    }
    
    /**
     * Set the Property Ref/Value Pairs Owner
     * @param completeType
     */
    public void setOwner(Object owner) {
        ILDModel newLDModel = null;
        
        // Complete owner when a property is set
        if(owner instanceof ICompleteType) {
            newLDModel = ((ICompleteType)owner).getOwner().getLDModel();
        }
        // Set a property on completion
        else if(owner instanceof IOnCompletionType) {
            newLDModel = ((IOnCompletionType)owner).getParent().getLDModel();
        }
        else {
            throw new RuntimeException("Wrong input on Property Value Table"); //$NON-NLS-1$
        }
        
        // Remove previous Model listener if any
        if(fCurrentLDModel != newLDModel) {
            if(fCurrentLDModel != null) {
                fCurrentLDModel.removePropertyChangeListener(this);
            }
            
            // Add new one
            newLDModel.addPropertyChangeListener(this);

            // New LD Model
            fCurrentLDModel = newLDModel;
        }
        
        populatePropertiesCombo();
        setInput(owner);
    }
    
    /**
     * Set up the columns
     */
    private void setColumns() {
        Table table = getTable();
        
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        // Use layout from parent container
        TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();

        TableColumn[] columns = new TableColumn[columnNames.length];
        
        for(int i = 0; i < columnNames.length; i++) {
            columns[i] = new TableColumn(table, SWT.NONE, i);
            columns[i].setText(columnNames[i]);
        }
        
        layout.setColumnData(columns[0], new ColumnWeightData(50, true));
        layout.setColumnData(columns[1], new ColumnWeightData(50, true));
        layout.setColumnData(columns[2], new ColumnWeightData(15, true));
        
        // Column names are properties
        setColumnProperties(columnNames);
    }
    
    /**
     * Set up the Editors
     */
    private void setEditors() {
        Table table = getTable();
        
        CellEditor[] editors = new CellEditor[columnNames.length];
        
        // Combo box for Property Ref
        editors[0] = new ComboBoxCellEditor(table, new String[] {}, SWT.READ_ONLY);
        ((CCombo)((ComboBoxCellEditor)editors[0]).getControl()).setVisibleItemCount(15);
        
        // Text cell for value
        editors[1] = new TextCellEditor(table);
        
        // Tick for advanced editor
        editors[2] = new CheckboxCellEditor(table);
        
        setCellEditors(editors);
    }

    /**
     * Populate the combo with Properties Titles
     */
    private void populatePropertiesCombo() {
        fLookUp.clear();
        
        List<String> string_list = new ArrayList<String>();
        
        for(ILDModelObject object : fCurrentLDModel.getPropertiesModel().getChildren()) {
            if(object instanceof IPropertyTypeModel) {
                IPropertyTypeModel property = (IPropertyTypeModel)object;
                string_list.add(property.getTitle());
                fLookUp.add(property);
            }
        }
        
        String[] s = new String[string_list.size()];
        string_list.toArray(s);
        
        ((ComboBoxCellEditor)getCellEditors()[0]).setItems(s);
    }
    
    /* 
     * When a Resource's identifer changes update the identifierrefs
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // When a Property changes update the combo
        if(evt.getSource() instanceof IPropertyTypeModel || evt.getNewValue() instanceof IPropertyTypeModel ||
                evt.getSource() instanceof IPropertyGroupModel || evt.getNewValue() instanceof IPropertyGroupModel) {
            populatePropertiesCombo();
            refresh();
        }
    }
    
    /**
     * Edit using the advanced Property editor
     */
    private void editAdvancedPropertyValueEditor(IPropertyRefValuePairOwner propertyRefValuePairOwner) {
        AdvancedPropertyValueEditorDialog dialog = new AdvancedPropertyValueEditorDialog(getControl().getShell(), propertyRefValuePairOwner);
        dialog.open();
        refresh();
    }
    
    // =============================================================================================
    //
    //                                   CONTENT PROVIDER
    //
    // =============================================================================================

    class PropertyValueTableContentProvider implements IStructuredContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(parent instanceof ICompleteType) {
                return ((ICompleteType)parent).getWhenPropertyValueIsSetTypes().toArray();
            }
            else if(parent instanceof IOnCompletionType) {
                return ((IOnCompletionType)parent).getChangePropertyValueTypes().toArray();
            }
            else {
                return new Object[0];
            }
        }
    }

    // =============================================================================================
    //
    //                                   LABEL PROVIDER
    //
    // =============================================================================================

    class PropertyValueTableLabelProvider
    extends LabelProvider
    implements ITableLabelProvider
    {
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            IPropertyRefValuePairOwner propertyRefOwner = (IPropertyRefValuePairOwner)element;
            
            switch(columnIndex) {
                case 0:
                    return ImageFactory.getImage(ImageFactory.ICON_PROPERTY);
                    
                case 2:
                    // Show if it uses advanced Properties
                    boolean usesAdvanced = propertyRefOwner.getPropertyRefValuePair().getPropertyValue().getChoice() != IPropertyValueType.CHOICE_NONE;
                    return usesAdvanced ? ImageFactory.getImage(ImageFactory.ICON_TICK) : null;
            }
            
            return null;
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            IPropertyRefValuePairOwner propertyRefOwner = (IPropertyRefValuePairOwner)element;

            switch(columnIndex) {
                // Property Ref Title
                case 0:
                    IPropertyTypeModel property = (IPropertyTypeModel)propertyRefOwner.getPropertyRefValuePair().getPropertyRef().getLDModelObject();
                    return property == null ? "" : property.getTitle(); //$NON-NLS-1$

                // Property Value
                case 1:
                    return StringUtils.safeString(propertyRefOwner.getPropertyRefValuePair().getPropertyValue().getValue());

                // Advanced
                case 2:
                    boolean isSimple = propertyRefOwner.getPropertyRefValuePair().getPropertyValue().getChoice() == IPropertyValueType.CHOICE_NONE;
                    return isSimple ? "..." : ""; //$NON-NLS-1$ //$NON-NLS-2$
            }
            
            return ""; //$NON-NLS-1$
        }
    }

    // =============================================================================================
    //
    //                                   CELL EDITOR
    //
    // =============================================================================================
    
    class PropertyValueTableCellModifier
    implements ICellModifier {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
         */
        public boolean canModify(Object element, String property) {
            // Property Value
            if(property == columnNames[1]) {
                IPropertyRefValuePairOwner propertyRefOwner = (IPropertyRefValuePairOwner)element;
                boolean isSimple = propertyRefOwner.getPropertyRefValuePair().getPropertyValue().getChoice() == IPropertyValueType.CHOICE_NONE;
                return isSimple;
            }
            
            return true;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
         */
        public Object getValue(Object element, String property) {
            IPropertyRefValuePairOwner propertyRefOwner = (IPropertyRefValuePairOwner)element;

            // Property Ref Title
            if(property == columnNames[0]) {
                IPropertyTypeModel propertyType = (IPropertyTypeModel)propertyRefOwner.getPropertyRefValuePair().getPropertyRef().getLDModelObject();
                if(propertyType != null) {
                    return new Integer(fLookUp.indexOf(propertyType));
                }
                return new Integer(-1);
            }
            // Property Value
            else if(property == columnNames[1]) {
                return StringUtils.safeString(propertyRefOwner.getPropertyRefValuePair().getPropertyValue().getValue());
            }
            // Advanced
            else if(property == columnNames[2]) {
                boolean isSimple = propertyRefOwner.getPropertyRefValuePair().getPropertyValue().getChoice() == IPropertyValueType.CHOICE_NONE;
                return new Boolean(isSimple);
            }
            
            return ""; //$NON-NLS-1$
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
         */
        public void modify(Object element, String property, Object newValue) {
            // null means user rejected the value
            if(newValue == null) {
                return;
            }
            
            Item item = (Item)element;
            IPropertyRefValuePairOwner itemType = (IPropertyRefValuePairOwner)item.getData();

            // Property Ref Title
            if(property == columnNames[0]) {
                Integer newIndex = (Integer)newValue;
                IPropertyTypeModel previousProperty = (IPropertyTypeModel)itemType.getPropertyRefValuePair().getPropertyRef().getLDModelObject();
                Integer oldIndex = fLookUp.indexOf(previousProperty);
                // If different
                if(!oldIndex.equals(newIndex)) {
                    // Set Property Ref
                    IPropertyTypeModel newProperty = fLookUp.get(newIndex);
                    if(newProperty != null) {
                        itemType.getPropertyRefValuePair().setPropertyRef(newProperty);
                        fCurrentLDModel.setDirty();
                        update(itemType, null);
                    }
                }
            }
            
            // Property Value
            else if(property == columnNames[1]) {
                String text = (String)newValue;
                String oldval = StringUtils.safeString(itemType.getPropertyRefValuePair().getPropertyValue().getValue());
                // If different
                if(!text.equals(oldval)) {
                    itemType.getPropertyRefValuePair().getPropertyValue().setValue(text);
                    fCurrentLDModel.setDirty();
                    update(itemType, null);
                }
            }
            
            // Advanced
            else if(property == columnNames[2]) {
                ISelection selection = getSelection();
                IPropertyRefValuePairOwner owner = (IPropertyRefValuePairOwner)((IStructuredSelection)selection).getFirstElement();
                editAdvancedPropertyValueEditor(owner);
            }
        }
    }
}
