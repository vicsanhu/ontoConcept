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
package org.tencompetence.ldauthor.opendock.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.tencompetence.ldauthor.opendock.IOpenDockConstants;
import org.tencompetence.ldauthor.opendock.OpenDockHandler;
import org.tencompetence.ldauthor.opendock.OpenDockPlugin;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * TreeViewer for OpenDock Search Results
 * 
 * TODO - Why is this a Tree and not a Table?
 * 
 * @author Phillip Beauvoir
 * @author Roy Cherian
 * @version $Id: SearchResultsTreeViewer.java,v 1.11 2010/02/01 10:45:14 phillipus Exp $
 */
public class SearchResultsTreeViewer extends TreeViewer implements IOpenDockConstants {
    
    private static String[] columnNames = {
        Messages.SearchResultsTreeViewer_0,
        Messages.SearchResultsTreeViewer_1,
        Messages.SearchResultsTreeViewer_2,
        Messages.SearchResultsTreeViewer_3
    };
    
    /**
     * Actions
     */
    private IAction fActionViewDetails, fActionDownload;

    public SearchResultsTreeViewer(Composite parent, int style) {
        super(parent, style);
        
        setColumns();
        
        makeActions();
        hookContextMenu();
        
        /*
         * Double-click handler
         */
        addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                doViewSelectedItem();
            }
        });
        
        setContentProvider(new TreeContentProvider());
        setLabelProvider(new TreeLabelProvider());
    }

    private void makeActions() {
        fActionViewDetails = new Action(Messages.SearchResultsTreeViewer_4) {
            @Override
            public void run() {
                doViewSelectedItem();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_LD);
            }
        };
        
        fActionDownload = new Action(Messages.SearchResultsTreeViewer_5) {
            @Override
            public void run() {
                doDownloadSelectedItem();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return OpenDockPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        };
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);

        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });

        Menu menu = menuMgr.createContextMenu(getControl());
        getControl().setMenu(menu);
    }

    /**
     * Fill the right-click menu
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        ISelection selection = getSelection();
        if(!selection.isEmpty()) {
            manager.add(fActionViewDetails);
            manager.add(fActionDownload);
        }
    }
    
    /**
     * View the Selected Item
     */
    @SuppressWarnings("unchecked")
    private void doViewSelectedItem() {
        Object selected = ((IStructuredSelection) getSelection()).getFirstElement();
        if(selected instanceof HashMap) {
            ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>)getInput();
            
            Dialog dlg = new UoLDetailsDialog(getControl().getShell(), list, (HashMap<String, String>)selected);
            dlg.open();
        }
    }

    /**
     * Download the Selected Item
     */
    @SuppressWarnings("unchecked")
    private void doDownloadSelectedItem() {
        Object selected = ((IStructuredSelection) getSelection()).getFirstElement();
        if(selected instanceof HashMap) {
            OpenDockHandler.getInstance().downloadItemWithDialog((HashMap<String, String>)selected);
        }
    }

    /**
     * Set up the tree columns
     */
    private void setColumns() {
        Tree tree = getTree();
        
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        
        // Use layout from parent container
        TreeColumnLayout layout = (TreeColumnLayout)getControl().getParent().getLayout();

        TreeColumn[] columns = new TreeColumn[columnNames.length];
        
        for(int i = 0; i < columnNames.length; i++) {
            columns[i] = new TreeColumn(tree, SWT.NONE);
            columns[i].setText(columnNames[i]);
        }
        
        layout.setColumnData(columns[0], new ColumnWeightData(20, true));
        layout.setColumnData(columns[1], new ColumnWeightData(10, true));
        layout.setColumnData(columns[2], new ColumnWeightData(10, true));
        layout.setColumnData(columns[3], new ColumnWeightData(10, true));

        // Column names are properties
        setColumnProperties(columnNames);
    }

    // =============================================================================================
    //
    //                                   CONTENT PROVIDER
    //
    // =============================================================================================

    private class TreeContentProvider implements ITreeContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        @SuppressWarnings({"unchecked"})
        public Object[] getElements(Object parent) {
            if(parent instanceof List) {
                return ((List)parent).toArray();
            }
            
            if(parent instanceof Object[]) {
                return (Object[])parent;
            }
            
            else if(parent instanceof HashMap) {
                Object containers = ((HashMap)parent).get(QUERY_CONTAINERS);
                if(containers instanceof Object[]) {
                    return (Object[])containers;
                }
            }
            
            return new Object[0];
        }

        public Object getParent(Object child) {
            return null;
        }

        public Object[] getChildren(Object parent) {
            return getElements(parent);
        }

        public boolean hasChildren(Object parent) {
            if(parent instanceof Object[]) {
                return ((Object[])parent).length > 0;
            }
            return false;
        }
    }
    
    // =============================================================================================
    //
    //                                   LABEL PROVIDER
    //
    // =============================================================================================

    private class TreeLabelProvider extends LabelProvider implements ITableLabelProvider {
        
        private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy"); //$NON-NLS-1$

        public String getColumnText(Object element, int columnIndex) {
            if(element instanceof HashMap<?, ?>) {
                HashMap<?, ?> itemType = (HashMap<?, ?>)element;
                // element_downloads
                // container_type uol
                // element_owner_name Mark Barrett-Baxendale
                // element_views - Value - 29
                // element_releasedate - Value - 1169099479
                // meta_title - Value - PBL
                // container_size - Value - 78482
                // container_item_count - Value - 31
                // meta_shortdesc - Value - Computing PBL
                switch(columnIndex) {
                    // short desc
                    case 0: {
                        String s = (String)itemType.get(QUERY_META_TITLE);
                        return StringUtils.safeString(s);
                    }
                    // Author
                    case 1: {
                        String s = (String)itemType.get(QUERY_ELEMENT_OWNER_NAME);
                        return StringUtils.safeString(s);
                    }
                    // Licence
                    case 2: {
                        String s = (String)itemType.get(QUERY_CC_LICENSE);
                        return StringUtils.safeString(s);
                    }
                    // Published date
                    case 3: {
                        String pub = (String)itemType.get(QUERY_ELEMENT_LASTUPDATE);
                        if(StringUtils.isSet(pub)) {
                            // php date to be multiplied with 1000 to make it java
                            Date date = new Date(Long.parseLong(pub) * 1000);
                            return formatter.format(date);
                        }
                    }

                }
            }

            return ""; //$NON-NLS-1$
        }

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }
    }

}
