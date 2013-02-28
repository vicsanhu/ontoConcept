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

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.ICompleteActType;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;


/**
 * Acts TableViewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActsTableViewer.java,v 1.7 2009/05/19 18:21:01 phillipus Exp $
 */
public class ActsTableViewer extends TableViewer {

    public ActsTableViewer(ILDEditorPart editor, Composite parent, int style) {
        super(parent, style | SWT.FULL_SELECTION);
        
        setColumns();
        
        setContentProvider(new ActsTableContentProvider());
        
        new ActsTableViewerDragDropHandler(editor, this);
    }

    
    /**
     * Set up the columns
     */
    private void setColumns() {
        Table table = getTable();
        
        table.setHeaderVisible(false);
        
        //table.setLinesVisible(true);
        
        // Use layout from parent container
        TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();
        TableViewerColumn column = new TableViewerColumn(this, SWT.NONE);
        column.setLabelProvider(new ActsTableLabelCellProvider());
        layout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
        
        // To have owner draw Cells we have to do this for Eclipse 3.3
        // Eclipse 3.4 might not need it
        // OwnerDrawLabelProvider.setUpOwnerDraw(this);
        
        // For custom tooltips
        ColumnViewerToolTipSupport.enableFor(this);
    }
    
    // =============================================================================================
    //
    //                                   CONTENT PROVIDER
    //
    // =============================================================================================

    private class ActsTableContentProvider implements IStructuredContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(parent instanceof IPlayModel) {
                return ((IPlayModel)parent).getActsModel().getChildren().toArray();
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

    private class ActsTableLabelCellProvider
    extends OwnerDrawLabelProvider {

        @Override
        protected void measure(Event event, Object element) {
        }

        @Override
        protected void paint(Event event, Object element) {
            Image img = getImage(element);

            if(img != null) {
                Rectangle bounds = ((TableItem)event.item).getBounds(event.index);
                Rectangle imgBounds = img.getBounds();
                
                bounds.height /= 2;
                bounds.height -= imgBounds.height / 2;
                
                event.gc.drawImage(img, bounds.x + (bounds.width - imgBounds.width - 2), bounds.y + bounds.height);
            }
        }
        
        @Override
        protected void erase(Event event, Object element) {
            // Don't want this behaviour
        }

        private Image getImage(Object element) {
            IActModel act = (IActModel)element;
            
            switch(act.getCompleteActType().getChoice()) {
                case ICompleteActType.COMPLETE_NONE:
                    return null;
                    
                case ICompleteActType.COMPLETE_WHEN_ROLE_PART_COMPLETED:
                    return ImageFactory.getImage(ImageFactory.IMAGE_TASK_16);
                
                case ICompleteActType.COMPLETE_TIME_LIMIT:
                    return ImageFactory.getImage(ImageFactory.ICON_DATE);
                
                case ICompleteActType.COMPLETE_WHEN_PROPERTY_SET:
                    return ImageFactory.getImage(ImageFactory.ICON_PROPERTY);

                case ICompleteActType.COMPLETE_WHEN_CONDITION_TRUE:
                    return ImageFactory.getImage(ImageFactory.ICON_CONDITION);
            }
            
            return null;
        }

        @Override
        public void update(ViewerCell cell) {
            super.update(cell);
            cell.setImage(ImageFactory.getImage(ImageFactory.IMAGE_ACT_32));
            cell.setText(getText(cell.getElement()));
        }
        
        @Override
        public String getToolTipText(Object element) {
            return getText(element);
        }
        
        @Override
        public boolean useNativeToolTip(Object object) {
            return true;
        }

        private String getText(Object element) {
            return ((IActModel)element).getTitle();
        }
    }
}