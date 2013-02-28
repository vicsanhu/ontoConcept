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
package org.tencompetence.ldauthor.ui.views.inspector.environment;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.environments.IConferenceModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentRefModel;
import org.tencompetence.imsldmodel.environments.IIndexSearchModel;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.imsldmodel.environments.IMonitorModel;
import org.tencompetence.imsldmodel.environments.ISendMailModel;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Table for Environments' contents
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentTableViewer.java,v 1.3 2009/05/19 18:21:06 phillipus Exp $
 */
public class EnvironmentTableViewer
extends TableViewer {
    
    /**
     * Constructor
     * @param parent
     * @param style
     */
    public EnvironmentTableViewer(Composite parent, int style) {
        super(parent, style | SWT.FULL_SELECTION);
        
        setColumns();
        
        setContentProvider(new EnvironmentsTableContentProvider());
        setLabelProvider(new EnvironmentsTableLabelProvider());
        
        setSorter(new ViewerSorter());
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

    
    private class EnvironmentsTableContentProvider implements IStructuredContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(parent instanceof IEnvironmentModel) {
                return ((IEnvironmentModel)parent).getChildren().toArray();
            }
            else {
                return new Object[0];
            }
        }
    }

    /**
     * View Label Provider
     */
    private class EnvironmentsTableLabelProvider extends LabelProvider {

        public EnvironmentsTableLabelProvider() {
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
            if(obj instanceof ILearningObjectModel) {
                switch(((ILearningObjectModel)obj).getType()) {
                    case ILearningObjectModel.TYPE_KNOWLEDGE_OBJECT:
                        return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_24);
                    case ILearningObjectModel.TYPE_TEST_OBJECT:
                        return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_TEST_OBJECT_24);
                    case ILearningObjectModel.TYPE_TOOL_OBJECT:
                        return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_TOOL_OBJECT_24);
                    default:
                        return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_24);
                }
            }
            if(obj instanceof IConferenceModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_CONFERENCE_24);
            }
            if(obj instanceof IIndexSearchModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_INDEXSEARCH_24);
            }
            if(obj instanceof ISendMailModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_SENDMAIL_24);
            }
            if(obj instanceof IMonitorModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_MONITOR_24);
            }
            if(obj instanceof IEnvironmentRefModel) {
                return ImageFactory.getImage(ImageFactory.IMAGE_ENVIRONMENT_24);
            }

            return null;
        }
    }
}
