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
package org.tencompetence.ldauthor.ui.editors.method;

import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.service.environment.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TreeColumn;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.activities.IActivityRefModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureModel;
import org.tencompetence.imsldmodel.activities.IActivityStructureRefModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityModel;
import org.tencompetence.imsldmodel.activities.ILearningActivityRefModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityModel;
import org.tencompetence.imsldmodel.activities.ISupportActivityRefModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.expressions.IUOLHrefType;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;
import org.tencompetence.ldauthor.qti.model.QTIUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.CenterImageLabelProvider;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.LDEditorInput;
import org.tencompetence.ldauthor.ui.editors.method.RolePartsTreeModelAdapter.ASChildTreeItem;
import org.tencompetence.ldauthor.ui.editors.method.RolePartsTreeModelAdapter.FoldedRolePart;


/**
 * RoleParts TreeTableViewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: RolePartsTreeTableViewer.java,v 1.38 2009/05/19 18:21:01 phillipus Exp $
 */
public class RolePartsTreeTableViewer extends TreeViewer {
    
    private static Color NOTE_COLOR = new Color(null, 255, 255, 205);
    
    private ILDModel fLDModel;
    
    private RolePartsTreeTableViewerDragDropHandler fDragDropHandler;
    
    public RolePartsTreeTableViewer(ILDEditorPart editor, Composite parent, int style) {
        super(parent, style | SWT.FULL_SELECTION | SWT.MULTI);
        
        fLDModel = ((LDEditorInput)editor.getEditorInput()).getModel();
        
        addColumns();
        
        // Mac does it's own table colouring thing
        if(!Platform.getOS().equals(Constants.OS_MACOSX)) {
            getTree().setBackground(NOTE_COLOR);
        }
        
        setContentProvider(new RolePartsTableContentProvider());
        
        fDragDropHandler = new RolePartsTreeTableViewerDragDropHandler(editor, this);
        
        // Cell modifier
        setCellModifier(new TreeCellModifier());
        
        // For custom tooltips
        ColumnViewerToolTipSupport.enableFor(this);

        getTree().setHeaderVisible(true);
        getTree().setLinesVisible(true);
    }
    
    public void setInput(RolePartsTreeModelAdapter adapter) {
        super.setInput(adapter);
        fDragDropHandler.setAct(adapter);
    }
    
    /**
     * Set up the columns
     */
    public void addColumns() {
        // Dispose old columns and editors
        for(TreeColumn column : getTree().getColumns()) {
            column.dispose();
        }
        
        if(getCellEditors() != null) {
            for(CellEditor editor : getCellEditors()) {
                if(editor != null) {
                    editor.dispose();
                }
            }
        }
        
        // Use layout from parent container
        TreeColumnLayout layout = (TreeColumnLayout)getControl().getParent().getLayout();
        
        // Roles
        List<IRoleModel> roles = fLDModel.getRolesModel().getOrderedRoles();
        
        // Number of columns
        int columnSize = roles.size() + 1;
        
        TreeViewerColumn[] columns = new TreeViewerColumn[columnSize];
        CellEditor[] editors = new CellEditor[columnSize];
        String[] columnProperties = new String[columnSize];
        
        // First Column and Cell Editor
        columnProperties[0] = "0"; // Set column property to column index //$NON-NLS-1$
        columns[0] = new TreeViewerColumn(this, SWT.NONE);
        columns[0].getColumn().setText(Messages.RolePartsTreeTableViewer_0);
        columns[0].setLabelProvider(new FirstColumnLabelProvider());
        layout.setColumnData(columns[0].getColumn(), new ColumnWeightData(50, true));
        
        editors[0] = null; // cannot edit first column
        
        // Roles
        for(int i = 0; i < roles.size(); i++) {
            IRoleModel role = roles.get(i);
            
            columnProperties[i + 1] = String.valueOf(i + 1); // Set column property to column index
            columns[i + 1] = new TreeViewerColumn(this, SWT.NONE);
            columns[i + 1].getColumn().setData("role", role); //$NON-NLS-1$
            columns[i + 1].getColumn().setText(role.getTitle());
            columns[i + 1].getColumn().setToolTipText(role.getTitle());
            columns[i + 1].setLabelProvider(new CentredTickLabelProvider(role));
            layout.setColumnData(columns[i + 1].getColumn(), new ColumnWeightData(10, true));
            
            // Cell Editor
            editors[i + 1] = new CheckboxCellEditor(getTree());
        }
        
        // Cell Editors
        setCellEditors(editors);
        
        // Set Column properties in order for cell editors to work
        setColumnProperties(columnProperties);
    }
    
    /**
     * Update a column name from the Role's title
     * @param role
     */
    public void updateColumnName(IRoleModel role) {
        for(TreeColumn column : getTree().getColumns()) {
            if(column.getData("role") == role) { //$NON-NLS-1$
                column.setText(role.getTitle());
                column.setToolTipText(role.getTitle());
            }
        }
    }
    
    /**
     * @return The currently selected ActivityStructure.
     *         If the selected node is an Activity Reference then return itself.
     *         Return null if there isn't an ActivityStructure selected or it doesn't have one as a parent.
     */
    public IActivityStructureModel getSelectedActivityStructure() {
        Object element = ((IStructuredSelection)getSelection()).getFirstElement();
        
        // Folded Role Part containing AS
        if(element instanceof FoldedRolePart && ((FoldedRolePart)element).getComponent() instanceof IActivityStructureModel) {
            return (IActivityStructureModel)((FoldedRolePart)element).getComponent();
        }
        
        // Child Tree item, get ref
        if(element instanceof ASChildTreeItem) {
            element = ((ASChildTreeItem)element).getComponentRef();
        }
        
        // Child other ref, find parent
        if((element instanceof ILearningActivityRefModel) || (element instanceof ISupportActivityRefModel)) {
            return (IActivityStructureModel)((IActivityRefModel)element).getParent();
        }

        // AS Ref
        if(element instanceof IActivityStructureRefModel) {
            return (IActivityStructureModel)((IActivityStructureRefModel)element).getLDModelObject();
        }
        
        return null;
    }
      
    // =============================================================================================
    //
    //                                   CONTENT PROVIDER
    //
    // =============================================================================================

    private class RolePartsTableContentProvider implements ITreeContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }
        
        public Object getParent(Object child) {
            return null;
        }
        
        public Object [] getChildren(Object parent) {
            if(parent instanceof RolePartsTreeModelAdapter) {
                return ((RolePartsTreeModelAdapter)parent).getFoldedRoleParts().toArray();
            }
            
            if(parent instanceof FoldedRolePart) {
                return ((FoldedRolePart)parent).getChildItems().toArray();
            }
            
            if(parent instanceof ASChildTreeItem) {
                return ((ASChildTreeItem)parent).getChildItems().toArray();
            }
            
            return new Object[0];
        }
        
        public boolean hasChildren(Object element) {
            if(element instanceof FoldedRolePart) {
                FoldedRolePart foldedRolePart = (FoldedRolePart)element;
                if(foldedRolePart.getComponent() instanceof IActivityStructureModel) {
                    return ((IActivityStructureModel)foldedRolePart.getComponent()).getActivityRefs().size() > 0;
                }
            }
            
            if(element instanceof ASChildTreeItem) {
                return ((ASChildTreeItem)element).getChildItems().size() > 0;
            }
            
            return false;
        }
    }
    
    // =============================================================================================
    //
    //                                   FIRST COLUMN LABEL PROVIDER
    //
    // =============================================================================================

    private class FirstColumnLabelProvider
    extends ColumnLabelProvider
    {
        // On Vista Column 1 text is clipped
        boolean VISTA_HACK_NEEDED = "Windows Vista".equals(System.getProperty("os.name"));  //$NON-NLS-1$//$NON-NLS-2$
        
        @Override
        public Image getImage(Object element) {
            Object component = getComponent(element);
            
            if(component instanceof ILearningActivityModel) {
                // If activity has Item that points to QTI Resource
                ILearningActivityModel activity = (ILearningActivityModel)component;
                if(QTIUtils.isQTITestActivity(activity)) {
                    return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_TEST_OBJECT_24);
                }
                return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_ACTIVITY_24);
            }
            if(component instanceof ISupportActivityModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_SUPPORT_ACTIVITY_24);
            }
            if(component instanceof IActivityStructureModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_ACTIVITY_STRUCTURE_24);
            }
            if(component instanceof IEnvironmentModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_ENVIRONMENT_24);
            }
            if(component instanceof IUOLHrefType) {
                return ImageFactory.getImage(ImageFactory.ICON_LD);
            }

            return null;
        }
        
        @Override
        public String getText(Object element) {
            Object component = getComponent(element);
            
            String text = ""; //$NON-NLS-1$
            
            if(component instanceof ITitle) {
                text = ((ITitle)component).getTitle();
            }
            if(component instanceof IUOLHrefType) {
                text = ((IUOLHrefType)component).getHref();
            }
            
            if(VISTA_HACK_NEEDED) {
                text += " "; //$NON-NLS-1$
            }

            return text; 
        }
        
        private Object getComponent(Object element) {
            if(element instanceof FoldedRolePart) {
                return ((FoldedRolePart)element).getComponent();
            }
            if(element instanceof ASChildTreeItem) {
                return ((ASChildTreeItem)element).getComponentRef().getLDModelObject();
            }
            return element;
        }
        
        @Override
        public String getToolTipText(Object element) {
            // Don't use tooltips if we are using Preview Popups
            if(LDAuthorPlugin.getDefault().getPreferenceStore().getBoolean(ILDAuthorPreferenceConstants.PREFS_PREVIEW_POPUP)) {
                return null;
            }
            
            String tip = getText(element);
            
            Object component = getComponent(element);
            
            // If an Activity Structure Ref...
            if(component instanceof IActivityStructureModel) {
                IActivityStructureModel as = (IActivityStructureModel)component;
                
                switch(as.getStructureType()) {
                    case IActivityStructureModel.TYPE_SELECTION:
                        int num = as.getNumberToSelect();
                        if(num == 0) {
                            num = as.getActivityRefs().size();
                        }
                        tip += "\n" + //$NON-NLS-1$
                        Messages.RolePartsTreeTableViewer_1 +
                        " " + //$NON-NLS-1$
                        num +
                        " " + //$NON-NLS-1$
                        Messages.RolePartsTreeTableViewer_2;
                        break;

                    case IActivityStructureModel.TYPE_SEQUENCE:
                        tip += "\n" + //$NON-NLS-1$
                        Messages.RolePartsTreeTableViewer_3 +
                        " " + //$NON-NLS-1$
                        as.getActivityRefs().size() +
                        " " + //$NON-NLS-1$
                        Messages.RolePartsTreeTableViewer_4;
                        break;
                }
                
                return tip;
            }
            
            return tip;
        }
        
        @Override
        public int getToolTipTimeDisplayed(Object object) {
            return 10000;
        }
        
        @Override
        public int getToolTipDisplayDelayTime(Object object) {
            return 200;
        }
    }
    
    // =============================================================================================
    //
    //                                   ROLE COLUMN LABEL PROVIDER
    //
    // =============================================================================================

    private class CentredTickLabelProvider extends CenterImageLabelProvider {
        
        private IRoleModel fRoleModel;
        
        public CentredTickLabelProvider(IRoleModel roleModel) {
            fRoleModel = roleModel;
        }
        
        @Override
        protected Image getImage(Object element) {
            // If it's a top level FoldedRolePart we can do a solid tick it or not depending on Role.
            if(element instanceof FoldedRolePart) {
                boolean ticked = ((FoldedRolePart)element).hasRole(fRoleModel);
                return ticked ? ImageFactory.getImage(ImageFactory.ICON_TICK) : null;
            }
            
            // If it's a LD reference then it's part of an Activity Structure and might be ghost ticked
            if(element instanceof ASChildTreeItem) {
                boolean ticked = ((ASChildTreeItem)element).getFoldedRolePart().hasRole(fRoleModel);
                return ticked ? ImageFactory.getImage(ImageFactory.ICON_TICK_FAINT) : null;
            }
            
            return null;
        }
    }

    // =============================================================================================
    //
    //                                   CELL EDITOR
    //
    // =============================================================================================
    
    class TreeCellModifier
    implements ICellModifier {

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
         */
        public boolean canModify(Object element, String property) {
            // Can only tick top-level rows
            return element instanceof FoldedRolePart;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
         */
        public Object getValue(Object element, String property) {
            FoldedRolePart foldedRolePart = (FoldedRolePart)element;
            
            int columnindex = Integer.parseInt(property);
            TreeColumn column = getTree().getColumn(columnindex);
            if(column == null) {
                throw new RuntimeException("TreeColumn was null"); //$NON-NLS-1$
            }
            
            IRoleModel roleModel = (IRoleModel)column.getData("role"); //$NON-NLS-1$
            
            return new Boolean(foldedRolePart.hasRole(roleModel));
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
            FoldedRolePart foldedRolePart = (FoldedRolePart)item.getData();
            Boolean value = (Boolean)newValue;
            
            // Only allow untick if at least one other Role is ticked
            if(value == false && foldedRolePart.getRoles().size() < 2) {
                return;
            }
            
            int columnindex = Integer.parseInt(property);
            TreeColumn column = getTree().getColumn(columnindex);
            if(column == null) {
                throw new RuntimeException("TreeColumn was null"); //$NON-NLS-1$
            }
            
            IRoleModel roleModel = (IRoleModel)column.getData("role"); //$NON-NLS-1$
            
            if(value) {
                foldedRolePart.addNewRole(roleModel);
            }
            else {
                foldedRolePart.removeRole(roleModel);
            }

            fLDModel.setDirty();
            refresh(foldedRolePart);
        }
    }
}
