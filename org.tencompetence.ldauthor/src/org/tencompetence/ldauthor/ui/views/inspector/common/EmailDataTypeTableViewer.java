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
package org.tencompetence.ldauthor.ui.views.inspector.common;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
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
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.properties.IGlobalPropertyTypeModel;
import org.tencompetence.imsldmodel.properties.IPropertyGroupModel;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.imsldmodel.roles.ILearnerModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.imsldmodel.roles.IRolesModel;
import org.tencompetence.imsldmodel.types.IEmailDataType;
import org.tencompetence.imsldmodel.types.IEmailDataTypeOwner;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Email DataType Table Viewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: EmailDataTypeTableViewer.java,v 1.2 2009/06/30 18:03:47 phillipus Exp $
 */
public class EmailDataTypeTableViewer extends CheckboxTableViewer
implements PropertyChangeListener {
    
    /**
     * The Column Names
     */
    private static String[] columnNames = {
            Messages.EmailDataTypeTableViewer_0,  
            Messages.EmailDataTypeTableViewer_1,
            Messages.EmailDataTypeTableViewer_2,
    };

    private IEmailDataTypeOwner fEmailDataTypeOwner;
    
    private ILDModel fCurrentLDModel;
    
    private List<IGlobalPropertyTypeModel> fLookUp = new ArrayList<IGlobalPropertyTypeModel>();
    
    private CCombo fCombo1, fCombo2;
    
    public EmailDataTypeTableViewer(Composite parent) {
        super(new Table(parent, SWT.CHECK | SWT.FULL_SELECTION | SWT.BORDER));
        
        setColumns();
        
        setEditors();
        setCellModifier(new PropertyValueTableCellModifier());
        
        setContentProvider(new RoleTableContentProvider());
        setLabelProvider(new RoleTableLabelProvider());
        
        // Listen to selections
        addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                IRoleModel role = (IRoleModel)event.getElement();
                selectRole(role, event.getChecked());
            }
        });
        
        // Dispose listener
        getTable().addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if(fCurrentLDModel != null) {
                    fCurrentLDModel.removePropertyChangeListener(EmailDataTypeTableViewer.this);
                }
            }
        });
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
        layout.setColumnData(columns[1], new ColumnWeightData(40, true));
        layout.setColumnData(columns[2], new ColumnWeightData(40, true));
        
        // Column names are properties
        setColumnProperties(columnNames);
    }
    
    /**
     * Set up the Editors
     */
    private void setEditors() {
        Table table = getTable();
        
        CellEditor[] editors = new CellEditor[columnNames.length];
        editors[0] = null;
        
        editors[1] = new ComboBoxCellEditor(table, new String[] {}, SWT.READ_ONLY);
        fCombo1 = (CCombo)((ComboBoxCellEditor)editors[1]).getControl();
        fCombo1.setVisibleItemCount(15);
        
        editors[2] = new ComboBoxCellEditor(table, new String[] {}, SWT.READ_ONLY);
        fCombo2 = (CCombo)((ComboBoxCellEditor)editors[2]).getControl();
        fCombo2.setVisibleItemCount(15);
        
        setCellEditors(editors);
    }


    /**
     * Set the EmailDataType Model
     * @param model
     */
    public void setEmailDataTypeOwner(IEmailDataTypeOwner model) {
        ILDModel newLDModel = model.getLDModel();
        
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
        
        fEmailDataTypeOwner = model;
        
        populatePropertiesCombos();
        
        setInput(model.getLDModel().getRolesModel());
        setCheckedElements();
    }
    
    /**
     * Populate the combos with Properties Titles
     */
    private void populatePropertiesCombos() {
        fLookUp.clear();
        
        List<String> string_list = new ArrayList<String>();
        
        // (none) option
        fLookUp.add(null);
        string_list.add(Messages.EmailDataTypeTableViewer_3);
        
        // Global Properties
        for(ILDModelObject object : fEmailDataTypeOwner.getLDModel().getPropertiesModel().getChildren()) {
            if(object instanceof IGlobalPropertyTypeModel) {
                IGlobalPropertyTypeModel property = (IGlobalPropertyTypeModel)object;
                string_list.add(property.getTitle());
                fLookUp.add(property);
            }
        }
        
        String[] s = new String[string_list.size()];
        string_list.toArray(s);
        
        ((ComboBoxCellEditor)getCellEditors()[1]).setItems(s);
        ((ComboBoxCellEditor)getCellEditors()[2]).setItems(s);
    }

    
    /**
     * Set the check marks on selected Roles
     */
    private void setCheckedElements() {
        // Clear ticked elements
        setCheckedElements(new Object[] {});
        
        for(IEmailDataType emailData : fEmailDataTypeOwner.getEmailDataTypes()) {
            IRoleModel role = emailData.getRole();
            if(role != null) {
                setChecked(role, true);
            }
        }
    }

    private void selectRole(IRoleModel role, boolean checked) {
        if(checked) {
            fEmailDataTypeOwner.addEmailDataType(role);
        }
        else {
            IEmailDataType emailData = fEmailDataTypeOwner.getEmailDataType(role);
            if(emailData != null) {
                fEmailDataTypeOwner.getEmailDataTypes().remove(emailData);
            }
        }

        refresh();
        fEmailDataTypeOwner.getLDModel().setDirty();
    }
    
    private IGlobalPropertyTypeModel getEmailPropertyRef(IRoleModel role) {
        IEmailDataType emailData = fEmailDataTypeOwner.getEmailDataType(role);
        return emailData == null ? null : emailData.getEmailPropertyRef();
    }
    
    private IGlobalPropertyTypeModel getUserNamePropertyRef(IRoleModel role) {
        IEmailDataType emailData = fEmailDataTypeOwner.getEmailDataType(role);
        return emailData == null ? null : emailData.getUserNamePropertyRef();
    }
    
    /* 
     * When a Property changes update the identifierrefs
     */
    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        Object newValue = evt.getNewValue();
        
        // When a Property changes update the combo
        if(source instanceof IPropertyTypeModel || newValue instanceof IPropertyTypeModel ||
                source instanceof IPropertyGroupModel || newValue instanceof IPropertyGroupModel) {
            populatePropertiesCombos();
            refresh();
        }
        
        // Roles change
        if(source instanceof IRoleModel || source instanceof IRolesModel) {
            refresh();
        }
    }


    // =============================================================================================
    //
    //                                   CONTENT PROVIDER
    //
    // =============================================================================================

    class RoleTableContentProvider implements IStructuredContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            // Display all Roles
            return fEmailDataTypeOwner.getLDModel().getRolesModel().getOrderedRoles().toArray();
        }
    }

    // =============================================================================================
    //
    //                                   LABEL PROVIDER
    //
    // =============================================================================================

    class RoleTableLabelProvider
    extends LabelProvider
    implements ITableLabelProvider
    {
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    if(element instanceof ILearnerModel) {
                        return ImageFactory.getImage(ImageFactory.IMAGE_LEARNER_16);
                    }
                    else {
                        return ImageFactory.getImage(ImageFactory.IMAGE_STAFF_16);
                    }
                    
            }
            
            return null;
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            IRoleModel role = (IRoleModel)element;

            switch(columnIndex) {
                // Role Title
                case 0:
                    return ((ITitle)element).getTitle();

                // E-mail Property
                case 1:
                    IGlobalPropertyTypeModel globalproperty = getEmailPropertyRef(role);
                    return globalproperty == null ? Messages.EmailDataTypeTableViewer_3 : globalproperty.getTitle(); 

                // Username Property
                case 2:
                    globalproperty = getUserNamePropertyRef(role);
                    return globalproperty == null ? Messages.EmailDataTypeTableViewer_3 : globalproperty.getTitle(); 
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
            if(property == columnNames[0]) {
                return false;
            }
            return true;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
         */
        public Object getValue(Object element, String property) {
            IRoleModel role = (IRoleModel)element;
            
            // E-mail Property
            if(property == columnNames[1]) {
                IGlobalPropertyTypeModel globalproperty = getEmailPropertyRef(role);
                return new Integer(fLookUp.indexOf(globalproperty));
            }
            
            // Username Property
            else if(property == columnNames[2]) {
                IGlobalPropertyTypeModel globalproperty = getUserNamePropertyRef(role);
                return new Integer(fLookUp.indexOf(globalproperty));
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
            IRoleModel role = (IRoleModel)item.getData();
            
            IEmailDataType emailData = fEmailDataTypeOwner.getEmailDataType(role);
            
            // Not ticked, so tick it for the user
            if(emailData == null) {
                selectRole(role, true);
                setChecked(role, true);
                emailData = fEmailDataTypeOwner.getEmailDataType(role);
            }
            // Oops, something still went wrong!
            if(emailData == null) {
                return;
            }
            
            if(property == columnNames[1]) {
                Integer newIndex = (Integer)newValue;
                IGlobalPropertyTypeModel previous_globalproperty = emailData.getEmailPropertyRef();
                Integer oldIndex = fLookUp.indexOf(previous_globalproperty);
                // If different
                if(!oldIndex.equals(newIndex)) {
                    IGlobalPropertyTypeModel newProperty = fLookUp.get(newIndex); // allowed to be null
                    emailData.setEmailPropertyRef(newProperty);
                    fCurrentLDModel.setDirty();
                    update(role, null);
                }
            }
            
            else if(property == columnNames[2]) {
                Integer newIndex = (Integer)newValue;
                IGlobalPropertyTypeModel previous_globalproperty = emailData.getUserNamePropertyRef();
                Integer oldIndex = fLookUp.indexOf(previous_globalproperty);
                // If different
                if(!oldIndex.equals(newIndex)) {
                    IGlobalPropertyTypeModel newProperty = fLookUp.get(newIndex); // allowed to be null
                    emailData.setUsernamePropertyRef(newProperty);
                    fCurrentLDModel.setDirty();
                    update(role, null);
                }
            }
        }
    }

}
