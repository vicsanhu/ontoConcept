package org.tencompetence.qtieditor.ui;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.tencompetence.qtieditor.elements.*;

public class AssociationExtensionsTableViewer extends TableViewer {
        
    private static String[] columnNames = {
    	"Source", "Target", "Score"
    };
    private BlockInteraction fInteraction;

    public AssociationExtensionsTableViewer(Composite parent, BlockInteraction aInteraction, AbstractQuestionEditPanel aEditor) {
        super(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        fInteraction  = aInteraction;

        setColumns();
        setEditors();
        
        AssociationExtensionsTableCellModifier aTableCellModifier = new AssociationExtensionsTableCellModifier();
        aTableCellModifier.setTableViewer(this);
        aTableCellModifier.setEditor(aEditor);
        setCellModifier(aTableCellModifier);
        
        setContentProvider(new AssociationExtensionsTableContentProvider());
        setLabelProvider(new AssociationExtensionsTableLabelProvider());
    }
    
    /**
     * Set up the tree columns
     */
    private void setColumns() {
        Table table = getTable();
        
        table.setHeaderVisible(true);
        
        // Use layout from parent container
        TableColumnLayout layout = new TableColumnLayout();

        TableColumn[] columns = new TableColumn[columnNames.length];
        
        for(int i = 0; i < columnNames.length; i++) {
            columns[i] = new TableColumn(table, SWT.NONE);
            columns[i].setText(columnNames[i]);
        }
        
        layout.setColumnData(columns[0], new ColumnWeightData(30, true));
        layout.setColumnData(columns[1], new ColumnWeightData(30, true));
        layout.setColumnData(columns[2], new ColumnWeightData(8, true));
        
        // Column names are properties
        setColumnProperties(columnNames);
    }

    /**
     * Set up the Editors
     */
    private void setEditors() {
        Table table = getTable();
        CellEditor[] editors = new CellEditor[columnNames.length];
        editors[0] = null;
        editors[1] = null;
        editors[2] = new TextCellEditor(table);
        setCellEditors(editors);
    }


    private class AssociationExtensionsTableContentProvider implements IStructuredContentProvider {
        
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
    }
    
    private class AssociationExtensionsTableLabelProvider
    extends LabelProvider
    implements ITableLabelProvider
    {
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
    	
        public Image getColumnImage(Object element, int columnIndex) {

            return null;
        }
        
        @Override
        public String getText(Object element) {
            // Have to implement this for Sorter comparator
            if(element instanceof AssociationExtension) {
                return ((AssociationExtension)element).toString();
            }
            return super.getText(element);
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            if(element instanceof AssociationExtension) {
            	AssociationExtension aAssociationExtension = (AssociationExtension)element;

                switch(columnIndex) {
                    // Source
                    case 0:
                    	if (fInteraction instanceof MatchInteraction) {
                    		return ((MatchInteraction)fInteraction).getSourceSimpleMatchSet()
                    		.getSimpleAssociableChoiceByID(aAssociationExtension.getSource())
                    		.getData();
                    	} else if (fInteraction instanceof AssociateInteraction) {
                    		return ((AssociateInteraction)fInteraction)
                    		.getSimpleAssociableChoiceByID(aAssociationExtension.getSource())
                    		.getData();
                    	} else if (fInteraction instanceof GapMatchInteraction) {
                    		return ((GapMatchInteraction)fInteraction)
                    		.getGapTextByID(aAssociationExtension.getSource())
                    		.getData();
                    	}
                    // Target
                    case 1:
                    	if (fInteraction instanceof MatchInteraction) {
                    		return ((MatchInteraction)fInteraction).getTargetSimpleMatchSet()
                    		.getSimpleAssociableChoiceByID(aAssociationExtension.getTarget())
                    		.getData();
                    	} else if (fInteraction instanceof AssociateInteraction) {
                    		return ((AssociateInteraction)fInteraction)
                    		.getSimpleAssociableChoiceByID(aAssociationExtension.getTarget())
                    		.getData();
                    	} else if (fInteraction instanceof GapMatchInteraction) {
                    		return ((GapMatchInteraction)fInteraction)
                    		.getGapByID(aAssociationExtension.getTarget())
                    		.getData();
                    	}
                    // Score
                    case 2:
                        return String.valueOf(aAssociationExtension.getMappedValue());
                    default:
                    	return "";
                }
            } else
            	return "";
        }
    }
    
    private class AssociationExtensionsTableCellModifier
    implements ICellModifier {
    	
    	public AssociationExtensionsTableViewer viewer;
        private AbstractQuestionEditPanel parentEditor;

    	public void setTableViewer(AssociationExtensionsTableViewer aViewer){
    		viewer = aViewer;
    	}
    	
    	public void setEditor(AbstractQuestionEditPanel aEditor){
    		parentEditor = aEditor;
    	}

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

            if(property == columnNames[2]) {
            	return String.valueOf(aAssociationExtension.getMappedValue());
            }
            
            return ""; 
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
         */
        public void modify(Object element, String property, Object newValue) {
            // null means user rejected the value
            if(newValue == null) {
                return;
            }
                        
            if(property == columnNames[2]) {
            	Item item = (Item)element;
                AssociationExtension aAssociationExtension = (AssociationExtension)item.getData();

                String score = (String)newValue;
                double oldval = aAssociationExtension.getMappedValue();
				try {
					
					double value = Double.valueOf(score).doubleValue();
					
					if(oldval != value) {
	                    aAssociationExtension.setMappedValue(value);
	                    parentEditor.setDirty(true);
	                    viewer.refresh(aAssociationExtension);
	                }					
										
				} catch (Exception event) {
					MessageBox mDialog = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);					
					mDialog.setText("Warning: Wrong input!");
					mDialog.setMessage("You input a string '"+score+ "' for defining a score. Please input a number!");
					mDialog.open();
				}
            }               
        }
    }

}
