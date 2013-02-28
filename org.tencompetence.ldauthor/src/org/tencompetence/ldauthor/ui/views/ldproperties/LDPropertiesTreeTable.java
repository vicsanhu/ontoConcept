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
package org.tencompetence.ldauthor.ui.views.ldproperties;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IDecoration;
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
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.properties.IGlobalPropertyTypeModel;
import org.tencompetence.imsldmodel.properties.IPropertyGroupModel;
import org.tencompetence.imsldmodel.properties.IPropertyGroupRefModel;
import org.tencompetence.imsldmodel.properties.IPropertyRefModel;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * LD Level B Properties TreeTable
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDPropertiesTreeTable.java,v 1.7 2009/06/29 20:49:37 phillipus Exp $
 */
public class LDPropertiesTreeTable
extends TreeViewer {
    
    /**
     * The Column Names
     */
    private static String[] columnNames = {
        Messages.LDPropertiesTreeTable_0,
        Messages.LDPropertiesTreeTable_1,
        Messages.LDPropertiesTreeTable_2
    };
    
    /**
     * Parent Editor
     */
    private ILDEditorPart fEditor;
    
    private ILDModel fLDModel;


    public LDPropertiesTreeTable(Composite parent) {
        /*
         * SWT.FULL_SELECTION is VERY important, otherwise you can't edit columns
         */
        super(parent, SWT.FULL_SELECTION | SWT.MULTI);

        setColumns();
        
        setContentProvider(new LDPropertiesTreeTableContentProvider());
        setLabelProvider(new LDPropertiesTreeTableLabelProvider());
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
        
        layout.setColumnData(columns[0], new ColumnWeightData(35, true));
        layout.setColumnData(columns[1], new ColumnWeightData(15, true));
        layout.setColumnData(columns[2], new ColumnWeightData(25, true));
        
        // Column names are properties
        setColumnProperties(columnNames);
    }
    
    public void setEditor(ILDEditorPart editor) {
        fEditor = editor;
        fLDModel = (ILDModel)fEditor.getAdapter(ILDModel.class);
        setInput(fLDModel);
    }

    
    // =============================================================================================
    //
    //                                   CONTENT PROVIDER
    //
    // =============================================================================================

    class LDPropertiesTreeTableContentProvider implements ITreeContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(parent instanceof ILDModel) {
                return ((ILDModel)parent).getPropertiesModel().getChildren().toArray();
            }
            else {
                return new Object[0];
            }
        }
        
        public Object getParent(Object child) {
            return null;
        }
        
        public Object [] getChildren(Object parent) {
            if(parent instanceof ILDModelObjectReference) {
                parent = ((ILDModelObjectReference)parent).getLDModelObject();
            }
            
            if(parent instanceof IPropertyGroupModel) {
                return ((IPropertyGroupModel)parent).getPropertyRefs().toArray();
            }
            
            return new Object[0];
        }
        
        public boolean hasChildren(Object parent) {
            if(parent instanceof ILDModelObjectReference) {
                parent = ((ILDModelObjectReference)parent).getLDModelObject();
            }
            
            if(parent instanceof IPropertyGroupModel) {
                return ((IPropertyGroupModel)parent).getPropertyRefs().size() > 0;
            }
            return false;
        }
    }

    /**
     * View Label Provider
     */
    private class LDPropertiesTreeTableLabelProvider extends LabelProvider
    implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            if(columnIndex != 0) {
                return null;
            }
            
            Image image = null;
            
            if(element instanceof IPropertyTypeModel) {
                image = ImageFactory.getImage(ImageFactory.ICON_PROPERTY);
            }
            
            else if(element instanceof IPropertyGroupModel) {
                image = ImageFactory.getImage(ImageFactory.ECLIPSE_IMAGE_FOLDER);
            }
            
            // Check for IPropertyGroupRefModel before IPropertyRefModel because of class hierarchy
            else if(element instanceof IPropertyGroupRefModel) {
                image = ImageFactory.getOverlayImage(ImageFactory.ECLIPSE_IMAGE_FOLDER, ImageFactory.ICON_SHORTCUT_OVERLAY, IDecoration.BOTTOM_LEFT);
            }
            
            else if(element instanceof IPropertyRefModel) {
                image = ImageFactory.getOverlayImage(ImageFactory.ICON_PROPERTY, ImageFactory.ICON_SHORTCUT_OVERLAY, IDecoration.BOTTOM_LEFT);
            }
            
            return image;
        }

        public String getColumnText(Object element, int columnIndex) {
            if(element instanceof ILDModelObjectReference) {
                element = ((ILDModelObjectReference)element).getLDModelObject();
            }
            
            if(element instanceof IPropertyTypeModel) {
                IPropertyTypeModel property = (IPropertyTypeModel)element;
                
                // Check if Global type and "Exiting Choice" type
                boolean isGlobalAndExistingChoice = false;
                IGlobalPropertyTypeModel glob = null;
                if(element instanceof IGlobalPropertyTypeModel) {
                    glob = (IGlobalPropertyTypeModel)element;
                    isGlobalAndExistingChoice = glob.isExistingChoice();
                }

                switch(columnIndex) {
                    // Title
                    case 0:
                        // If a Global Property with type "Existing definition" use the Href as a title
                        if(isGlobalAndExistingChoice) {
                            // Use path part of href
                            String href = glob.getExistingHREF();
                            try {
                                URI uri = new URI(href);
                                String s = uri.getPath();
                                if(!StringUtils.isSet(s) || s.length() == 1) {
                                    // Jump out to the catch clause...
                                    throw new URISyntaxException("", ""); //$NON-NLS-1$ //$NON-NLS-2$
                                }
                                else {
                                    return s;
                                }
                            }
                            // Failing that, the URL without http://
                            catch(URISyntaxException ex) {
                                // Ignore
                                int index = href.indexOf("http://"); //$NON-NLS-1$
                                if(index != -1) {
                                    href = href.substring(index + 7);
                                }
                            }
                            return href;
                        }
                        // Fall thru to here
                        return StringUtils.safeString(property.getTitle());
                        
                    // Type
                    case 1:
                        if(isGlobalAndExistingChoice) {
                            return ""; //$NON-NLS-1$
                        }
                        return StringUtils.safeString(property.getDataType().getType());
                        
                    // Initial Value
                    case 2:
                        if(isGlobalAndExistingChoice) {
                            return ""; //$NON-NLS-1$
                        }
                        return StringUtils.safeString(property.getInitialValue());
                }
            }
            
            if(element instanceof IPropertyGroupModel) {
                IPropertyGroupModel group = (IPropertyGroupModel)element;

                switch(columnIndex) {
                    // Title
                    case 0:
                        return StringUtils.safeString(group.getTitle());
                }
            }

            return ""; //$NON-NLS-1$
        }
        
    }

}
