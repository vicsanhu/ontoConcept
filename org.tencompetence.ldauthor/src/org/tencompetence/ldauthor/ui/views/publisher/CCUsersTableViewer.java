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
package org.tencompetence.ldauthor.ui.views.publisher;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.TableColumnSorter;
import org.tencompetence.ldauthor.utils.StringUtils;
import org.tencompetence.ldpublisher.model.IRole;
import org.tencompetence.ldpublisher.model.IUser;


/**
 * Users Viewer for CC server
 * 
 * @author Phillip Beauvoir
 * @version $Id: CCUsersTableViewer.java,v 1.4 2008/10/16 12:29:06 phillipus Exp $
 */
public class CCUsersTableViewer extends TableViewer {
    
    /**
     * The Column Names
     */
    private static String[] columnNames = {
            "", //$NON-NLS-1$
            Messages.CCUsersTableViewer_0
    };
    
    private Listener sortListener = new Listener() {
        public void handleEvent(Event e) {
            TableColumn sortColumn = getTable().getSortColumn();
            TableColumn currentColumn = (TableColumn)e.widget;
            int direction = getTable().getSortDirection();

            if(sortColumn == currentColumn) {
                direction = (direction == SWT.UP ? SWT.DOWN : SWT.UP);
            }
            else {
                getTable().setSortColumn(currentColumn);
                direction = SWT.UP;
            }
            getTable().setSortDirection(direction);
            refresh(false);
        }
    };

    public CCUsersTableViewer(Composite parent, int style) {
        /*
         * SWT.FULL_SELECTION is VERY important, otherwise you can't edit columns
         */
        super(parent, style | SWT.FULL_SELECTION | SWT.MULTI);
        
        setColumns();
        
        setContentProvider(new UsersContentProvider());
        
        setLabelProvider(new UsersLabelProvider());
    }
    
    /**
     * Set up the tree columns
     */
    private void setColumns() {
        Table table = getTable();
        
        table.setHeaderVisible(true);
        //table.setLinesVisible(true);
        
        // Use layout from parent container
        TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();

        TableColumn[] columns = new TableColumn[columnNames.length];
        
        for(int i = 0; i < columnNames.length; i++) {
            columns[i] = new TableColumn(table, SWT.NONE);
            columns[i].setText(columnNames[i]);
            
            // Needed for sorting
            columns[i].setData("index", new Integer(i)); //$NON-NLS-1$
            columns[i].addListener(SWT.Selection, sortListener);
        }
        
        layout.setColumnData(columns[0], new ColumnWeightData(5, true));
        layout.setColumnData(columns[1], new ColumnWeightData(95, true));
        
        setSorter(new Sorter());

        // Column names are properties
        setColumnProperties(columnNames);
    }

    // =============================================================================================
    //
    //                                   CONTENT PROVIDER
    //
    // =============================================================================================

    private class UsersContentProvider implements IStructuredContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(parent instanceof IRole) {
                try {
                    return ((IRole)parent).getUsers().toArray();
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
                
                return new Object[0];
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

    private class UsersLabelProvider
    extends LabelProvider
    implements ITableLabelProvider
    {
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            if(element instanceof IUser && columnIndex == 0) {
                return ImageFactory.getImage(ImageFactory.ICON_USER);
            }

            return null;
        }
        
        @Override
        public String getText(Object element) {
            // Have to implement this for Sorter comparator
            if(element instanceof IUser) {
                return ((IUser)element).getTitle();
            }
            return super.getText(element);
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            if(element instanceof IUser) {
                IUser user = (IUser)element;

                switch(columnIndex) {
                    // Title
                    case 1:
                        return StringUtils.safeString(user.getTitle());
                }
            }

            return ""; //$NON-NLS-1$
        }
    }

    // =============================================================================================
    //
    //                                   SORTER
    //
    // =============================================================================================

    private class Sorter extends TableColumnSorter {

        @Override
        protected Object getValue(Object o, int index) {
            if(o instanceof IRole) {
                IUser user = (IUser)o;

                switch(index) {
                    case 1:
                        return StringUtils.safeString(user.getTitle());
                }
            }
            return null;
        }
    }
}
