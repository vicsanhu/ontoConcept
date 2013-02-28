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
package org.tencompetence.ldauthor.ui.views.inspector.environment.indexsearch;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.tencompetence.ldauthor.ui.ImageFactory;

/**
 * Table Viewer for Item class in Index Search
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemsTableViewer.java,v 1.1 2008/09/24 13:08:35 phillipus Exp $
 */
public class ItemsTableViewer
extends TableViewer {
    /**
     * The Column Names
     */
    private static String[] columnNames = {
        "", //$NON-NLS-1$
        Messages.ItemsTableViewer_0
    };
    
    private List<ItemString> fItems = new ArrayList<ItemString>();
    
    /**
     * Constructor
     * @param parent
     * @param style
     */
    public ItemsTableViewer(Composite parent, int style) {
        super(parent, style | SWT.FULL_SELECTION | SWT.MULTI);
        setupTable();
    }

    /**
     * Set up the table
     */
    private void setupTable() {
        setCellEditors();
        setCellModifier(new ItemsTableCellModifier());
        setupColumns();
        
        setContentProvider(new ItemsTableContentProvider());
        setLabelProvider(new ItemsTableLabelProvider());

        setInput(""); //$NON-NLS-1$
    }

    /**
     * Set up the table columns
     */
    private void setupColumns() {
        Table table = getTable();

        // Set up the columns
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        // Use layout from parent container
        TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();
        
        TableViewerColumn[] columns = new TableViewerColumn[columnNames.length];
        
        for(int i = 0; i < columnNames.length; i++) {
            columns[i] = new TableViewerColumn(this, SWT.NONE);
            columns[i].getColumn().setText(columnNames[i]);
        }
        
        layout.setColumnData(columns[0].getColumn(), new ColumnWeightData(5, true));
        layout.setColumnData(columns[1].getColumn(), new ColumnWeightData(95, true));
        
        // Column properties for editors
        setColumnProperties(columnNames);
    }
    
    /**
     * Set up the Cell Editors
     */
    private void setCellEditors() {
        Table table = getTable();
        CellEditor[] editors = new CellEditor[2];  // One for each column
        editors[0] = null;
        editors[1] = new TextCellEditor(table);
        setCellEditors(editors);
    }
    
    /**
     * Set the String list of items
     * @param list
     */
    public void setItems(List<String> list) {
        fItems.clear();
        
        for(String string : list) {
            ItemString element = new ItemString(string);
            fItems.add(element);
        }

        refresh();
    }
    
    /**
     * @return The String list of items
     */
    public List<String> getItems() {
        List<String> list = new ArrayList<String>();
        for(ItemString item : fItems) {
            list.add(item.value);
        }
        return list;
    }
    
    /**
     * Add a new blank entry
     */
    public void addNewEntry() {
        ItemString s = new ItemString(Messages.ItemsTableViewer_1);
        fItems.add(s);
        refresh();
        editElement(s, 1);
        getTable().notifyListeners(SWT.Modify, null);
    }

    /**
     * Remove selected entries
     */
    public void removeSelectedEntries() {
        IStructuredSelection selection = (IStructuredSelection)getSelection();
        Object[] objects = selection.toArray();
        
        // Make sure we didn't get the empty selection
        if(objects.length == 0) {
            return;
        }
        
        // Delete
        for(int i = 0; i < objects.length; i++) {
            fItems.remove(objects[i]);
        }
        
        refresh();
        getTable().notifyListeners(SWT.Modify, null);
    }
    
    public class ItemsTableContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(fItems != null) {
                return fItems.toArray();
            }
            return new Object[0];
        }
    }
    
    /**
     * View Label Provider
     */
    class ItemsTableLabelProvider extends LabelProvider
    implements ITableLabelProvider {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            return columnIndex == 0 ? ImageFactory.getImage(ImageFactory.ICON_NODE) : null;
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            if(element == null) {
                return ""; //$NON-NLS-1$
            }
            
            return columnIndex == 0 ? null : element.toString();
        }
    }

    /**
     * Cell Modifier
     */
    class ItemsTableCellModifier
    implements ICellModifier {

        public boolean canModify(Object element, String property) {
            return true;
        }

        public Object getValue(Object element, String property) {
            return element.toString();
        }

        public void modify(Object element, String property, Object newValue) {
            Item item = (Item)element;
            ItemString s = (ItemString)item.getData();

            // Same value, ignore
            if(newValue.equals(s.value)) {
                return;
            }
            
            s.value = (String)newValue;
            
            update(s, null);
            getTable().notifyListeners(SWT.Modify, null);
        }
        
    }
    
    /*
     * Convenience class to wrap a String.
     * This ensures we have unique objects in the Table Content Provider
     */
    class ItemString {
        String value = ""; //$NON-NLS-1$
        
        ItemString(String value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }
}
