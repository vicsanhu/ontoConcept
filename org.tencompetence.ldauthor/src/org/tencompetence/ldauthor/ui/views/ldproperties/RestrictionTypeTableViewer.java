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
package org.tencompetence.ldauthor.ui.views.ldproperties;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.imsldmodel.properties.IRestrictionType;
import org.tencompetence.ldauthor.ui.common.StringComboBoxCellEditor;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * RestrictionTypeTableViewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: RestrictionTypeTableViewer.java,v 1.3 2009/05/19 18:21:04 phillipus Exp $
 */
public class RestrictionTypeTableViewer extends TableViewer {

    private IPropertyTypeModel fProperty;
    
    /**
     * The Column Names
     */
    private static String[] columnNames = {
            Messages.RestrictionTypeTableViewer_0,  
            Messages.RestrictionTypeTableViewer_1
    };

    
    public RestrictionTypeTableViewer(Composite parent, int style) {
        /*
         * SWT.FULL_SELECTION is VERY important, otherwise you can't edit columns
         */
        super(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER);

        setColumns();
        setEditors();
        setCellModifier(new RestrictionTypeEditorCellModifier());
        
        setContentProvider(new RestrictionTypeTableContentProvider());
        setLabelProvider(new RestrictionTypeTableLabelProvider());
    }

    /**
     * Set up the tree columns
     */
    private void setColumns() {
        Table table = getTable();
        
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        // Use layout from parent container
        TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();

        TableColumn[] columns = new TableColumn[columnNames.length];
        
        for(int i = 0; i < columnNames.length; i++) {
            columns[i] = new TableColumn(table, SWT.NONE);
            columns[i].setText(columnNames[i]);
        }
        
        layout.setColumnData(columns[0], new ColumnWeightData(50, true));
        layout.setColumnData(columns[1], new ColumnWeightData(50, true));
        
        // Column names are properties
        setColumnProperties(columnNames);
    }
    
    /**
     * Set up the Editors
     */
    private void setEditors() {
        Table table = getTable();
        CellEditor[] editors = new CellEditor[columnNames.length];
        editors[0] = new StringComboBoxCellEditor(table, IRestrictionType.RESTRICTION_TYPES, false);
        editors[1] = new TextCellEditor(table);
        setCellEditors(editors);
    }

    public void setProperty(IPropertyTypeModel property) {
        fProperty = property;
        setInput(property);
    }
    
    
    // =============================================================================================
    //
    //                                   CONTENT PROVIDER
    //
    // =============================================================================================

    class RestrictionTypeTableContentProvider
    implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(parent instanceof IPropertyTypeModel) {
                return ((IPropertyTypeModel)parent).getRestrictionTypes().toArray();
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

    class RestrictionTypeTableLabelProvider
    extends LabelProvider
    implements ITableLabelProvider
    {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            IRestrictionType restrictionType = (IRestrictionType)element;
            
            switch(columnIndex) {
                // Type
                case 0:
                    return StringUtils.safeString(restrictionType.getType());
                    
                // Value
                case 1:
                    return StringUtils.safeString(restrictionType.getValue());
            }

            return ""; //$NON-NLS-1$
        }
    }
    
    // =============================================================================================
    //
    //                                   CELL EDITOR
    //
    // =============================================================================================
    
    class RestrictionTypeEditorCellModifier
    implements ICellModifier {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
         */
        public boolean canModify(Object element, String property) {
            return true;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
         */
        public Object getValue(Object element, String property) {
            IRestrictionType restrictionType = (IRestrictionType)element;
            
            if(property == columnNames[0]) {
                return StringUtils.safeString(restrictionType.getType());
            }
            
            else if(property == columnNames[1]){
                return StringUtils.safeString(restrictionType.getValue());
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
            
            TableItem item = (TableItem)element;
            IRestrictionType restrictionType = (IRestrictionType)item.getData();

            if(property == columnNames[0]) {
                String text = (String)newValue;
                String oldValue = StringUtils.safeString(restrictionType.getType());
                // If different
                if((!text.equals(oldValue))) {
                    restrictionType.setType(text);
                    fProperty.getLDModel().setDirty();
                    update(restrictionType, null);
                }
            }

            else if(property == columnNames[1]) {
                String text = (String)newValue;
                String oldValue = StringUtils.safeString(restrictionType.getValue());
                // If different
                if(!text.equals(oldValue)) {
                    restrictionType.setValue(text);
                    fProperty.getLDModel().setDirty();
                    update(restrictionType, null);
                }
            }
        }
    }

}
