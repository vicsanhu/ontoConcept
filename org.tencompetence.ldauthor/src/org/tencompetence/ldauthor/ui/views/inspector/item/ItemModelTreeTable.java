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
package org.tencompetence.ldauthor.ui.views.inspector.item;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.StringComboBoxCellEditor;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Tree Table for adding Items
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemModelTreeTable.java,v 1.9 2009/06/15 12:58:41 phillipus Exp $
 */
public class ItemModelTreeTable
extends TreeViewer implements PropertyChangeListener {

    private ILDModel fCurrentLDModel;
    
    private ItemModelEditor fItemEditor;
    
    /**
     * The Column Names
     */
    private static String[] columnNames = {
            Messages.ItemModelTreeTable_0,
            Messages.ItemModelTreeTable_1,
            Messages.ItemModelTreeTable_2,
            Messages.ItemModelTreeTable_3
    };

    /**
     * Constructor
     * 
     * @param parent
     */
    public ItemModelTreeTable(ItemModelEditor itemEditor, Composite parent) {
        /*
         * SWT.FULL_SELECTION is VERY important, otherwise you can't edit columns
         */
        super(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER);
        
        fItemEditor = itemEditor;
        
        // For custom tooltips
        ColumnViewerToolTipSupport.enableFor(this);

        setEditors();
        setCellModifier(new ItemTypeTreeCellModifier());
        setLabelProvider(new ItemTypeTreeLabelProvider());
        setContentProvider(new ItemTypeTreeContentProvider());
        
        setColumns();
        
        // Drag and Drop support
        new ItemTreeTableDragDropHandler(this);
        
        // Ensure we remove any model listener on dispose of this
        getTree().addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if(fCurrentLDModel != null) {
                    fCurrentLDModel.removePropertyChangeListener(ItemModelTreeTable.this);
                }
            }
        });
    }
    
    /**
     * Set the Item model to display
     * @param model
     */
    public void setItemModel(IItemTypeContainer itemModel) {
        ILDModel newLDModel = itemModel.getLDModel();
        
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
        
        populateResourceCombo();
        setInput(itemModel);
    }
    
    /**
     * @return The model
     */
    public IItemTypeContainer getItemModel() {
        return (IItemTypeContainer)getInput();
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
            columns[i] = new TreeColumn(tree, SWT.NONE, i);
            columns[i].setText(columnNames[i]);
        }
        
        layout.setColumnData(columns[0], new ColumnWeightData(25, true));
        layout.setColumnData(columns[1], new ColumnWeightData(25, true));
        layout.setColumnData(columns[2], new ColumnWeightData(25, true));
        layout.setColumnData(columns[3], new ColumnWeightData(10, true));
        
        // Allows us to use custom tooltip for Item Resource
        TreeViewerColumn vCol = new TreeViewerColumn(this, columns[1]);
        vCol.setLabelProvider(new ItemTypeTreeLabelProvider());
        
        // Column names are properties
        setColumnProperties(columnNames);
    }
    
    /**
     * Set up the Editors
     */
    private void setEditors() {
        Tree tree = getTree();
        CellEditor[] editors = new CellEditor[columnNames.length];
        editors[0] = new TextCellEditor(tree);
        editors[1] = new StringComboBoxCellEditor(tree, new String[] {}, false);
        editors[2] = new TextCellEditor(tree);
        editors[3] = new CheckboxCellEditor(tree);
        setCellEditors(editors);
    }

    /**
     * Populate the Resource combo with Resource identifiers
     */
    private void populateResourceCombo() {
        List<String> list = new ArrayList<String>();
        
        for(IResourceModel resource : fCurrentLDModel.getResourcesModel().getResources()) {
            if(StringUtils.isSet(resource.getIdentifier())) {
                list.add(resource.getIdentifier());
            }
        }
        
        String[] s = new String[list.size()];
        list.toArray(s);
        
        ((StringComboBoxCellEditor)getCellEditors()[1]).setItems(s);
    }
    
    /* 
     * When a Resource's identifer changes update the identifierrefs
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        
        // When a Resource's identifer changes update the identifierrefs
        if(IResourceModel.PROPERTY_RESOURCE_IDENTIFIER.equals(propertyName)) {
            populateResourceCombo();
            refresh();
        }
        
        // Resource added or removed
        if(IResourceModel.PROPERTY_RESOURCE_ADDED.equals(propertyName) || IResourceModel.PROPERTY_RESOURCE_REMOVED.equals(propertyName)) {
            populateResourceCombo();
        }
    }
    
    
    // =============================================================================================
    //
    //                                   CONTENT PROVIDER
    //
    // =============================================================================================

    class ItemTypeTreeContentProvider implements ITreeContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(parent instanceof IItemTypeContainer) {
                return ((IItemTypeContainer)parent).getItemTypes().toArray();
            }
            else {
                return new Object[0];
            }
        }
        
        public Object getParent(Object child) {
            if(child instanceof IItemType) {
                return ((IItemType)child).getParent();
            }
            return null;
        }
        
        public Object [] getChildren(Object parent) {
            if(parent instanceof IItemTypeContainer) {
                return ((IItemTypeContainer)parent).getItemTypes().toArray();
            }
            return new Object[0];
        }
        
        public boolean hasChildren(Object parent) {
            if(parent instanceof IItemTypeContainer) {
                return ((IItemTypeContainer)parent).getItemTypes().size() > 0;
            }
            return false;
        }
    }

    // =============================================================================================
    //
    //                                   LABEL PROVIDER
    //
    // =============================================================================================

    class ItemTypeTreeLabelProvider
    extends CellLabelProvider
    implements ITableLabelProvider
    {
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            if(element instanceof IItemType) {
                switch(columnIndex) {
                    case 0:
                        return ImageFactory.getImage(ImageFactory.ICON_ITEM);
                        
                    case 3:
                        IItemType itemType = (IItemType)element;
                        return itemType.isVisible() ? ImageFactory.getImage(ImageFactory.ICON_TICK) : null;
                }
            }
            
            return null;
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            if(element instanceof IItemType) {
                IItemType itemType = (IItemType)element;

                switch(columnIndex) {
                    // Title
                    case 0:
                        return itemType.getTitle();

                    // Resource
                    case 1:
                        return StringUtils.safeString(itemType.getIdentifierRef());
                        
                    // Parameters
                    case 2:
                        return itemType.getParameters();
                }
            }
            
            return ""; //$NON-NLS-1$
        }
        
        @Override
        public String getToolTipText(Object element) {
            if(element instanceof IItemType) {
                IItemType itemType = (IItemType)element;
                IResourceModel resource = fCurrentLDModel.getResourcesModel().getResource(itemType);
                if(resource != null) {
                    return resource.getHref();
                }
            }
            return null;
        }
        
        @Override
        public void update(ViewerCell cell) {
            cell.setText(getColumnText(cell.getElement(), cell.getColumnIndex()));
        }
        
        @Override
        public int getToolTipTimeDisplayed(Object object) {
            return 10000;
        }
        
        @Override
        public int getToolTipDisplayDelayTime(Object object) {
            return 0;
        }
    }

    // =============================================================================================
    //
    //                                   CELL EDITOR
    //
    // =============================================================================================
    
    class ItemTypeTreeCellModifier
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
            IItemType itemType = (IItemType)element;

            // Title
            if(property == columnNames[0]) {
                return StringUtils.safeString(itemType.getTitle());
            }
            // Resource
            else if(property == columnNames[1]) {
                return StringUtils.safeString(itemType.getIdentifierRef());
            }
            // Parameters
            else if(property == columnNames[2]) {
                return StringUtils.safeString(itemType.getParameters());
            }
            // Is Visible
            else if(property == columnNames[3]) {
                return new Boolean(itemType.isVisible());
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
            IItemType itemType = (IItemType)item.getData();

            // Title
            if(property == columnNames[0]) {
                String text = (String)newValue;
                String oldval = StringUtils.safeString(itemType.getTitle());
                // If different
                if(!text.equals(oldval)) {
                    itemType.setTitle(text);
                    fCurrentLDModel.setDirty();
                    update(itemType, null);
                }
            }
            
            // Resource
            else if(property == columnNames[1]) {
                String text = (String)newValue;
                String oldval = StringUtils.safeString(itemType.getIdentifierRef());
                // If different
                if(!text.equals(oldval)) {
                    itemType.setIdentifierRef(text);
                    fCurrentLDModel.setDirty();
                    update(itemType, null);
                    fItemEditor.handleSelectionChanged(getSelection()); // Update "edit" menu item
                }
            }
            
            // Parameters
            else if(property == columnNames[2]) {
                String text = (String)newValue;
                String oldval = StringUtils.safeString(itemType.getParameters());
                // If different
                if(!text.equals(oldval)) {
                    itemType.setParameters(text);
                    fCurrentLDModel.setDirty();
                    update(itemType, null);
                }
            }

            // Is Visible
            else if(property == columnNames[3]) {
                Boolean value = (Boolean)newValue;
                itemType.setIsVisible(value.booleanValue());
                fCurrentLDModel.setDirty();
                update(itemType, null);
            }
            
        }
    }
}
