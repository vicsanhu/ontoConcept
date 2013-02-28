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
package org.tencompetence.ldauthor.preferences.editors;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;



/**
 * Table Viewer for Editing External Editors
 * 
 * @author Phillip Beauvoir
 * @version $Id: EditorsTableViewer.java,v 1.4 2009/06/27 15:01:38 phillipus Exp $
 */
public class EditorsTableViewer extends TableViewer {
    
    /**
     * Table Data
     */
    private ArrayList<TableEntry> fEntries = new ArrayList<TableEntry>();
    
    /**
     * Constructor
     * 
     * @param parent
     * @param style
     */
    public EditorsTableViewer(Composite parent, int style) {
        super(parent, style);
        setupTable();
    }

    /**
     * Set up the table
     */
    protected void setupTable() {
        Table table = getTable();
        
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        TableLayout layout = new TableLayout();
        table.setLayout(layout);

        TableColumn column = new TableColumn(table, SWT.NONE);
        column.setText(Messages.EditorsTableViewer_0);
        layout.addColumnData(new ColumnWeightData(20, true));
        
        column = new TableColumn(table, SWT.NONE);
        column.setText(Messages.EditorsTableViewer_1);
        layout.addColumnData(new ColumnWeightData(80, true));
        
        setContentProvider(new EditorsTableViewerContentProvider());
        setLabelProvider(new EditorsTableViewerLabelProvider());
    }

    /**
     * Set the data to that from the Preferences Store
     * @param data
     */
    public void setPreferencesData(String data) {
        fEntries.clear();
        
        StringTokenizer t = new StringTokenizer(data, ";"); //$NON-NLS-1$
        
        try {
            while(t.hasMoreElements()) {
                String extension = t.nextToken();
                String programPath = t.nextToken();
                TableEntry tableEntry = new TableEntry(extension, programPath);
                fEntries.add(tableEntry);
            }
        }
        catch(NoSuchElementException ex) {
        }
        
        setInput(fEntries);
    }
    
    /**
     * @return The Preferences data string
     */
    public String getPreferencesData() {
        String s = ""; //$NON-NLS-1$
        
        for(int i = 0; i < fEntries.size(); i++) {
            TableEntry tableEntry = (TableEntry)fEntries.get(i);
            s += tableEntry.extension + ";" + tableEntry.programPath + ";"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        return s;
    }
    
    /**
     * Add a new Entry
     */
    public void addNewEntry() {
        EditorEntryDialog dialog = new EditorEntryDialog(getControl().getShell());
        int code = dialog.open();
        if(code == Window.OK) {
            String extension = dialog.fExtension.trim();
            String programPath = dialog.fProgramPath.trim();
            if(extension.length() > 0 && programPath.length() > 0) {
                if(!extension.startsWith(".")) { //$NON-NLS-1$
                    extension = "." + extension; //$NON-NLS-1$
                }
                fEntries.add(new TableEntry(extension, programPath));
                refresh();
            }
        }
    }
    
    /**
     * Edit a selected Entry
     */
    public void editSelectedEntry() {
        IStructuredSelection selection = (IStructuredSelection)getSelection();
        if(!selection.isEmpty()) {
            TableEntry tableEntry = (TableEntry)selection.getFirstElement();
            EditorEntryDialog dialog = new EditorEntryDialog(getControl().getShell(),
                    tableEntry.extension, tableEntry.programPath);
            int code = dialog.open();
            if(code == Window.OK) {
                String extension = dialog.fExtension.trim();
                String programPath = dialog.fProgramPath.trim();
                if(extension.length() > 0 && programPath.length() > 0) {
                    if(!extension.startsWith(".")) { //$NON-NLS-1$
                        extension = "." + extension; //$NON-NLS-1$
                    }
                    tableEntry.extension = extension;
                    tableEntry.programPath = programPath;
                    refresh();
                }
            }
        }
    }
    
    /**
     * Remove selected entries
     */
    public void removeSelectedEntries() {
        StructuredSelection selection = (StructuredSelection)getSelection();
        Object[] objects = selection.toArray();
        
        // Make sure we didn't get the empty selection
        if(objects.length == 0) {
            return;
        }
        
        // Remove
        for(int i = 0; i < objects.length; i++) {
            fEntries.remove(objects[i]);
        }
        
        refresh();
    }
    
    // =============================================================================================
    //
    //                                   CONTENT PROVIDER
    //
    // =============================================================================================

    class EditorsTableViewerContentProvider
    implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(parent instanceof ArrayList<?>) {
                return ((ArrayList<?>)parent).toArray();
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

    class EditorsTableViewerLabelProvider
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
            if(element == null) {
                return ""; //$NON-NLS-1$
            }
            
            TableEntry tableEntry = (TableEntry)element;
            
            switch(columnIndex) {
                // Extension
                case 0:
                    return tableEntry.extension == null ? "" : tableEntry.extension; //$NON-NLS-1$
                    
                // Program name
                case 1:
                    return tableEntry.programPath == null ? "" : tableEntry.programPath; //$NON-NLS-1$
            }
            
            return ""; //$NON-NLS-1$
        }
    }
    
    /**
     * Simple class to bind two Strings for a table Entry
     */
    class TableEntry {
        public String extension = ""; //$NON-NLS-1$
        public String programPath = ""; //$NON-NLS-1$

        /**
         * @param extension
         * @param path
         */
        public TableEntry(String extension, String programPath) {
            this.extension = extension;
            this.programPath = programPath;
        }
    }
}
