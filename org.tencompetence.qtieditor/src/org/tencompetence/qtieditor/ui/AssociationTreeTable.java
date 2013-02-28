package org.tencompetence.qtieditor.ui;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
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
import org.tencompetence.qtieditor.elements.AssociationExtension;
import org.tencompetence.qtieditor.elements.AssociationExtensionList;
import org.tencompetence.qtieditor.elements.MatchInteraction;

public class AssociationTreeTable extends TreeViewer {
    
    /**
     * The Column Names
     */
    private static String[] columnNames = {
    	"Source", "Target", "Correct", "Score"
    };
    
    /**
     * Parent Editor and edited interaction
     */
    private EditMatchInteractionBlockPanel parentEditor;
    private MatchInteraction fMatchInteraction;
    

    public AssociationTreeTable(Composite parent, MatchInteraction aMatchInteraction) {
        /*
         * SWT.FULL_SELECTION is VERY important, otherwise you can't edit columns
         */
        super(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER);
        fMatchInteraction  = aMatchInteraction;
        setColumns();  
        setCellModifier(new AssociationTreeTableCellModifier());
        setContentProvider(new AssociationTreeTableContentProvider());
        setLabelProvider(new AssociationTreeTableLabelProvider());
    }

    /**
     * Set up the tree columns
     */
    private void setColumns() {
        Tree tree = getTree();
        
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        
        // Use layout from parent container
        TreeColumnLayout layout = new TreeColumnLayout();

        TreeColumn[] columns = new TreeColumn[columnNames.length];
        
        for(int i = 0; i < columnNames.length; i++) {
            columns[i] = new TreeColumn(tree, SWT.NONE, i);
            columns[i].setText(columnNames[i]);
        }
        
        layout.setColumnData(columns[0], new ColumnWeightData(35, true));
        layout.setColumnData(columns[1], new ColumnWeightData(35, true));
        layout.setColumnData(columns[2], new ColumnWeightData(8, true));
        layout.setColumnData(columns[3], new ColumnWeightData(8, true));
        
        // Column names are properties
        setColumnProperties(columnNames);
    }
    
    public void setEditors() {
    	/*
        Table table = getTable();
        CellEditor[] editors = new CellEditor[columnNames.length];
        editors[0] = null;
        editors[1] = new TextCellEditor(table);
        setCellEditors(editors);
        */
    }

    
    // CONTENT PROVIDER
    class AssociationTreeTableContentProvider implements ITreeContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object content) {
            if (content instanceof AssociationExtensionList) {
            	AssociationExtensionList aAssociationExtensionList = (AssociationExtensionList)content;
            	int associationCount = aAssociationExtensionList.size();
                Object[] rows = new Object[associationCount];
                for (int i=0; i<associationCount; i++) {
                	rows[i] = aAssociationExtensionList.getAssociationExtensionAt(i);
                }
                return rows;
            }
            else {
                return new Object[0];
            }
        }
        
        public Object getParent(Object child) {
            return null;
        }

        public Object [] getChildren(Object parent) {
            return new Object[0];
        }
        
        public boolean hasChildren(Object parent) {
            return false;
        }
    }

    //View Label Provider
    private class AssociationTreeTableLabelProvider extends LabelProvider
    implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            if(element instanceof AssociationExtension) {
            	AssociationExtension aAssociationExtension = (AssociationExtension)element;

                switch(columnIndex) {
                    // Source
                    case 0:
                        return fMatchInteraction.getSourceSimpleMatchSet()
                        .getSimpleAssociableChoiceByID(aAssociationExtension.getSource())
                        .getData();
                    // Target
                    case 1:
                        return fMatchInteraction.getTargetSimpleMatchSet()
                        .getSimpleAssociableChoiceByID(aAssociationExtension.getTarget())
                        .getData();
                    // Correct
                    case 2:
                        return aAssociationExtension.getCorrect()? "true":"false";
                    // Score
                    case 3:
                        return String.valueOf(aAssociationExtension.getMappedValue());
                    default:
                    	return "";
                }
            } else
            	return "";
        }
    }
    
    //                                   CELL EDITOR
    //
    // =============================================================================================
    
    private class AssociationTreeTableCellModifier
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
        	AssociationExtension aAssociationExtension = (AssociationExtension)element;

            // HREF
            if(property == columnNames[1]) {
                return (aAssociationExtension.getCorrect());
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
            /*
            Item item = (Item)element;
            IResourceFileModel fileModel = (IResourceFileModel)item.getData();

            // HREF
            if(property == columnNames[1]) {
                String text = (String)newValue;
                String oldval = StringUtils.safeString(fileModel.getHref());
                // If different
                if(!text.equals(oldval)) {
                    fileModel.setHref(text);
                }
            }
            */
        }
    }

}
