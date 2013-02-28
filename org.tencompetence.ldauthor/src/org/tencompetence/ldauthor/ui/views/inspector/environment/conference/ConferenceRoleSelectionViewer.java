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
package org.tencompetence.ldauthor.ui.views.inspector.environment.conference;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.environments.IConferenceModel;
import org.tencompetence.imsldmodel.roles.ILearnerModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.CenterImageLabelProvider;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Role Selection Viewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: ConferenceRoleSelectionViewer.java,v 1.3 2009/05/19 18:21:09 phillipus Exp $
 */
public class ConferenceRoleSelectionViewer extends TableViewer {
    
    /**
     * The Column Names
     */
    private static String[] columnNames = {
            Messages.ConferenceRoleSelectionViewer_0,  
            Messages.ConferenceRoleSelectionViewer_1,
            Messages.ConferenceRoleSelectionViewer_2,
            Messages.ConferenceRoleSelectionViewer_3,
            Messages.ConferenceRoleSelectionViewer_4
    };

    
    private IConferenceModel fConference;
    
    public ConferenceRoleSelectionViewer(Composite parent, int style) {
        super(parent, style | SWT.FULL_SELECTION); // Have to have full selection!
        
        // Content Provider
        IStructuredContentProvider contentProvider = new IStructuredContentProvider() {
            public Object[] getElements(Object inputElement) {
                // Display all Roles
                return getFlatList().toArray();
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        };

        setContentProvider(contentProvider);
        
        init();
    }
    
    private void init() {
        Table table = getTable();

        // Set up the columns
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        // Use layout from parent container
        TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();

        TableViewerColumn[] columns = new TableViewerColumn[columnNames.length];
        
        // Custom cell label providers so we can centre the images
        
        for(int i = 0; i < columnNames.length; i++) {
            columns[i] = new TableViewerColumn(this, SWT.NONE);
            columns[i].getColumn().setText(columnNames[i]);
            if(i == 0) {
                columns[i].setLabelProvider(new RoleLabelProvider());
            }
            else {
                columns[i].setLabelProvider(new CentredTickLabelProvider(i));
            }
        }
        
        layout.setColumnData(columns[0].getColumn(), new ColumnWeightData(60, true));
        layout.setColumnData(columns[1].getColumn(), new ColumnWeightData(10, true));
        layout.setColumnData(columns[2].getColumn(), new ColumnWeightData(10, true));
        layout.setColumnData(columns[3].getColumn(), new ColumnWeightData(10, true));
        layout.setColumnData(columns[4].getColumn(), new ColumnWeightData(10, true));
        
        // Set Column properties in order for cell editors to work
        setColumnProperties(columnNames);
        
        // Editors
        CellEditor[] editors = new CellEditor[columnNames.length];
        editors[0] = null;
        editors[1] = new CheckboxCellEditor(table);
        editors[2] = new CheckboxCellEditor(table);
        editors[3] = new CheckboxCellEditor(table);
        editors[4] = new CheckboxCellEditor(table);
        setCellEditors(editors);
        
        // Cell modifier
        setCellModifier(new TableCellModifier());
        
        // To have owner draw Cells we have to do this for Eclipse 3.3
        // Eclipse 3.4 might not need it
        // OwnerDrawLabelProvider.setUpOwnerDraw(this);
    }

    /**
     * Set the Conference Model
     * @param model
     */
    public void setConference(IConferenceModel model) {
        fConference = model;
        setInput(model.getLDModel().getRolesModel());
    }
    
    /**
     * @return A flattened list of Roles
     */
    private List<IRoleModel> getFlatList() {
        List<IRoleModel> list = new ArrayList<IRoleModel>();
        _getFlatList(fConference.getLDModel().getRolesModel(), list);
        return list;
    }
    // internal
    private void _getFlatList(ILDModelObjectContainer model, List<IRoleModel> list) {
        for(ILDModelObject o : model.getChildren()) {
            list.add((IRoleModel)o);
            _getFlatList((IRoleModel)o, list);
        }
    }
    
    /**
     * Return true if role is a conference participant
     * @param role
     * @return
     */
    private boolean isParticipant(IRoleModel role) {
        return fConference.getParticipants().contains(role);
    }
    
    /**
     * Return true if role is a conference observer
     * @param role
     * @return
     */
    private boolean isObserver(IRoleModel role) {
        return fConference.getObservers().contains(role);
    }
    
    private boolean isManager(IRoleModel role) {
        return fConference.getManager() == role;
    }
    
    private boolean isModerator(IRoleModel role) {
        return fConference.getModerator() == role;
    }



    /**
     * Label Provider for first columm cells
     */
    private class RoleLabelProvider extends CellLabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            if(element instanceof ILearnerModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_LEARNER_16);
            }
            else {
                return ImageFactory.getImage(ImageFactory.IMAGE_STAFF_16);
            }
        }

        public String getColumnText(Object element, int columnIndex) {
            return ((ITitle)element).getTitle();
        }

        @Override
        public void update(ViewerCell cell) {
            cell.setText(getColumnText(cell.getElement(), cell.getColumnIndex()));
            cell.setImage(getColumnImage(cell.getElement(), cell.getColumnIndex()));
        } 
    }

    
    /**
     * Label Provider for Centred Ticks cells
     */
    private class CentredTickLabelProvider extends CenterImageLabelProvider {
        private int fColumnIndex;
        
        public CentredTickLabelProvider(int columnIndex) {
            fColumnIndex = columnIndex;
        }
        
        @Override
        protected Image getImage(Object element) {
            switch(fColumnIndex) {
                case 1:
                    return isParticipant((IRoleModel)element) ? ImageFactory.getImage(ImageFactory.ICON_TICK) : null;

                case 2:
                    return isObserver((IRoleModel)element) ? ImageFactory.getImage(ImageFactory.ICON_TICK) : null;
                    
                case 3:
                    return isManager((IRoleModel)element) ? ImageFactory.getImage(ImageFactory.ICON_TICK) : null;
                    
                case 4:
                    return isModerator((IRoleModel)element) ? ImageFactory.getImage(ImageFactory.ICON_TICK) : null;
                    
            }
            
            return null;
        }
    }
    
    // =============================================================================================
    //
    //                                   CELL EDITOR
    //
    // =============================================================================================
    
    class TableCellModifier
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
            IRoleModel role = (IRoleModel)element;

            // Title
            if(property == columnNames[0]) {
                return StringUtils.safeString(role.getTitle());
            }
            // Participant
            else if(property == columnNames[1]) {
                return new Boolean(isParticipant(role));
            }
            // Observer
            else if(property == columnNames[2]) {
                return new Boolean(isObserver(role));
            }
            // Manager
            else if(property == columnNames[3]) {
                return new Boolean(isManager(role));
            }
            // Moderator
            else if(property == columnNames[4]) {
                return new Boolean(isModerator(role));
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

            // Participant
            if(property == columnNames[1]) {
                Boolean value = (Boolean)newValue;
                if(value) {
                    fConference.addParticipant(role);
                }
                else {
                    fConference.removeParticipant(role);
                }
                fConference.getLDModel().setDirty();
                update(role, null);
            }
            
            // Observer
            else if(property == columnNames[2]) {
                Boolean value = (Boolean)newValue;
                if(value) {
                    fConference.addObserver(role);
                }
                else {
                    fConference.removeObserver(role);
                }
                fConference.getLDModel().setDirty();
                update(role, null);
            }

            // Manager
            else if(property == columnNames[3]) {
                Boolean value = (Boolean)newValue;
                if(value) {
                    fConference.setManager(role);
                }
                else {
                    fConference.setManager(null);
                }
                fConference.getLDModel().setDirty();
                refresh();
            }

            // Moderator
            else if(property == columnNames[4]) {
                Boolean value = (Boolean)newValue;
                if(value) {
                    fConference.setModerator(role);
                }
                else {
                    fConference.setModerator(null);
                }
                fConference.getLDModel().setDirty();
                refresh();
            }
            
        }
    }
}
