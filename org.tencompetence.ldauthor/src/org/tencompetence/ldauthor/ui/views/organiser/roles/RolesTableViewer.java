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
package org.tencompetence.ldauthor.ui.views.organiser.roles;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.roles.ILearnerModel;
import org.tencompetence.imsldmodel.roles.IStaffModel;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Table for Roles
 * 
 * @author Phillip Beauvoir
 * @version $Id: RolesTableViewer.java,v 1.6 2009/05/19 18:21:05 phillipus Exp $
 */
public class RolesTableViewer
extends TableViewer {
    
    /**
     * Constructor
     * @param parent
     * @param style
     */
    public RolesTableViewer(Composite parent, int style) {
        super(parent, style | SWT.FULL_SELECTION);
        
        setColumns();
        
        setContentProvider(new RolesTableContentProvider());
        setLabelProvider(new RolesTableLabelProvider());
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
        layout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
    }

    
    private class RolesTableContentProvider implements IStructuredContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(parent instanceof ILDModel) {
                return ((ILDModel)parent).getRolesModel().getOrderedRoles().toArray();
            }
            else {
                return new Object[0];
            }
        }
    }

    /**
     * View Label Provider
     */
    private class RolesTableLabelProvider extends LabelProvider {

        public RolesTableLabelProvider() {
        }
        
        @Override
        public String getText(Object element) {
            if(element instanceof ITitle) {
                return ((ITitle)element).getTitle();
            }
            return element.toString();
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
         */
        @Override
        public Image getImage(Object obj) {
            if(obj instanceof ILearnerModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_LEARNER_24);
            }
            if(obj instanceof IStaffModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_STAFF_24);
            }

            return null;
        }
    }
}
