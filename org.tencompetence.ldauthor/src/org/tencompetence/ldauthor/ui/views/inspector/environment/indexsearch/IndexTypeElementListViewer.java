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
package org.tencompetence.ldauthor.ui.views.inspector.environment.indexsearch;

import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * List Viewer for LD Types
 * 
 * @author Phillip Beauvoir
 * @version $Id: IndexTypeElementListViewer.java,v 1.2 2009/05/19 18:21:02 phillipus Exp $
 */
public class IndexTypeElementListViewer extends CheckboxTableViewer {
    
    private List<String> fSelected;
    
    private String[] fElements = {
            LDModelFactory.LEARNING_ACTIVITY,
            LDModelFactory.SUPPORT_ACTIVITY,
            LDModelFactory.ACTIVITY_STRUCTURE,
            LDModelFactory.ENVIRONMENT,
            LDModelFactory.LEARNING_OBJECT,
            LDModelFactory.SEND_MAIL,
            LDModelFactory.CONFERENCE,
            LDModelFactory.INDEX_SEARCH,
            LDModelFactory.MONITOR,
    };

    public IndexTypeElementListViewer(Composite parent, int style) {
        super(new Table(parent, SWT.CHECK | SWT.FULL_SELECTION | style));
        
        // Content Provider
        IStructuredContentProvider contentProvider = new IStructuredContentProvider() {
            public Object[] getElements(Object inputElement) {
                // Display all Elements
                return fElements;
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        };

        setContentProvider(contentProvider);
        setInput(""); //$NON-NLS-1$
        
        // Label Provider
        LabelProvider labelProvider = new LabelProvider() {
            @Override
            public String getText(Object element) {
                return element.toString();
            }
            
            @Override
            public Image getImage(Object element) {
                return ImageFactory.getIconLDType((String)element);
            }
        };
        
        setLabelProvider(labelProvider);

        // Listen to selections
        addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                fSelected.clear();
                
                for(Object object : getCheckedElements()) {
                    fSelected.add((String)object);
                }
                
                getTable().notifyListeners(SWT.Modify, null);
            }
        });
    }

    public List<String> getItems() {
        return fSelected;
    }

    public void setItems(List<String> list) {
        fSelected = list;
        setCheckedElements(fSelected.toArray());
        refresh();
    }

}
