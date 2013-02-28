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
package org.tencompetence.ldauthor.ui.editors.resources;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;
import org.tencompetence.ldauthor.ldmodel.util.ItemTypeDescriptionEnumerator;
import org.tencompetence.ldauthor.ldmodel.util.ItemTypeDescriptionEnumerator.ItemTypeContainerDetail;
import org.tencompetence.ldauthor.ldmodel.util.ItemTypeDescriptionEnumerator.ItemTypeOwnerDetail;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Table Viewer to view Items in a LD
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemsTreeViewer.java,v 1.12 2009/06/16 10:01:00 phillipus Exp $
 */
public class ItemsTreeViewer extends TreeViewer {

    private IResourceModel fResource;
    
    /**
     * The Column Names
     */
    private static String[] columnNames = {
            Messages.ItemsTreeViewer_0,
            Messages.ItemsTreeViewer_1
    };
    
    public ItemsTreeViewer(IResourceModel resource, Composite parent, int style) {
        super(parent, style | SWT.FULL_SELECTION | SWT.MULTI);
        
        setColumns();
        
        setContentProvider(new ItemsContentProvider());
        
        setLabelProvider(new ItemsTreeLabelProvider());
        
        fResource = resource;
        
        setInput(new ItemTypeDescriptionEnumerator(resource.getLDModel()));
        
        expandAll();
    }
    
    /**
     * Set up the tree columns
     */
    private void setColumns() {
        Tree tree = getTree();
        
        tree.setHeaderVisible(true);
        //table.setLinesVisible(true);
        
        // Use layout from parent container
        TreeColumnLayout layout = (TreeColumnLayout)getControl().getParent().getLayout();

        TreeColumn[] columns = new TreeColumn[columnNames.length];
        
        for(int i = 0; i < columnNames.length; i++) {
            columns[i] = new TreeColumn(tree, SWT.NONE);
            columns[i].setText(columnNames[i]);
        }
        
        layout.setColumnData(columns[0], new ColumnWeightData(35, true));
        layout.setColumnData(columns[1], new ColumnWeightData(5, true));

        // Column names are properties
        setColumnProperties(columnNames);
    }
    
    // =============================================================================================
    //
    //                                   CONTENT PROVIDER
    //
    // =============================================================================================

    
    private class ItemsContentProvider implements ITreeContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(parent instanceof ItemTypeDescriptionEnumerator) {
                return ((ItemTypeDescriptionEnumerator)parent).getAllItemTypeDetails(fResource).toArray();
            }
            else {
                return new Object[0];
            }
        }
        
        public Object getParent(Object child) {
            return null;
        }
        
        public Object [] getChildren(Object parent) {
            if(parent instanceof ItemTypeOwnerDetail) {
                return ((ItemTypeOwnerDetail)parent).getItemTypeContainerDetails().toArray();
            }
            if(parent instanceof ItemTypeContainerDetail) {
                return ((ItemTypeContainerDetail)parent).getItemTypeContainer().getItemTypes().toArray();
            }
            if(parent instanceof IItemTypeContainer) {
                return ((IItemTypeContainer)parent).getItemTypes().toArray();
            }
            return new Object[0];
        }
        
        public boolean hasChildren(Object parent) {
            if(parent instanceof ItemTypeOwnerDetail) {
                return true;
            }
            if(parent instanceof ItemTypeContainerDetail) {
                return ((ItemTypeContainerDetail)parent).getItemTypeContainer().getItemTypes().size() > 0;
            }
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

    private class ItemsTreeLabelProvider
    extends LabelProvider
    implements ITableLabelProvider
    {
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            if(columnIndex == 0) {
                if(element instanceof ItemTypeOwnerDetail) {
                    ItemTypeOwnerDetail itemDetail = (ItemTypeOwnerDetail)element;
                    
                    if(itemDetail.getOwner() instanceof ILDModelObject) {
                        return ImageFactory.getIconLDType(((ILDModelObject)itemDetail.getOwner()));
                    }

                    return ImageFactory.getImage(ImageFactory.ICON_NODE);
                }
                
                if(element instanceof ItemTypeContainerDetail) {
                    //return ImageFactory.getImage(ImageFactory.ICON_NODE);
                }
                
                if(element instanceof IItemType) {
                    return ImageFactory.getImage(ImageFactory.ICON_ITEM);
                }
            }
            
            if(columnIndex == 1 && fResource != null) {
                if(element instanceof IItemType) {
                    IItemType itemType = (IItemType)element;
                    if(itemType.getIdentifierRef() != null && itemType.getIdentifierRef().equalsIgnoreCase(fResource.getIdentifier())) {
                        return ImageFactory.getImage(ImageFactory.ICON_TICK);
                    }
                }
            }

            return null;
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            if(element instanceof ItemTypeOwnerDetail) {
                ItemTypeOwnerDetail itemDetail = (ItemTypeOwnerDetail)element;
                
                switch(columnIndex) {
                    // Owner
                    case 0:
                        return StringUtils.safeString(itemDetail.getOwnerName());
                }
            }
            
            if(element instanceof ItemTypeContainerDetail) {
                ItemTypeContainerDetail itemDetail = (ItemTypeContainerDetail)element;
                
                switch(columnIndex) {
                    // Owner
                    case 0:
                        return StringUtils.safeString(itemDetail.getName());
                }
            }
            
            if(element instanceof IItemType) {
                IItemType itemType = (IItemType)element;

                switch(columnIndex) {
                    // Title
                    case 0:
                        String title = itemType.getTitle();
                        if(StringUtils.isSet(title)) {
                            return title;
                        }
                        else {
                            IResourceModel resource = itemType.getLDModel().getResourcesModel().getResource(itemType);
                            return resource == null ? Messages.ItemsTreeViewer_2 : resource.getIdentifier();
                        }
                    // Referenced
                    case 1:
                        return ""; //$NON-NLS-1$
                }
            }

            return ""; //$NON-NLS-1$
        }
    }


}
