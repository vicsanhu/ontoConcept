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

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.tencompetence.imsldmodel.environments.IIndexSearchModel;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;


/**
 * Inspector Section for Index Search Classes
 * 
 * @author Phillip Beauvoir
 * @version $Id: IndexSearchClassSection.java,v 1.6 2009/06/10 10:16:49 phillipus Exp $
 */
public class IndexSearchClassSection extends AbstractInspectorSection {
    
    private IIndexSearchModel fIndexSearch;
    
    private ItemsTableViewer fItemsTableViewer;
    
    private Listener fModifyListener = new Listener() {
        public void handleEvent(Event event) {
            if(fIndexSearch == null) {
                return;
            }
            
            fIndexSearch.setIndexClasses(fItemsTableViewer.getItems());
            
            fIndexSearch.getLDModel().setDirty();
        }
    };
    
    public IndexSearchClassSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        GridLayout layout = new GridLayout(2, false);
        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, true));
        
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        // Use a parent composite for the table with TableColumnLayout.
        // This allows for auto-resizing of table columns        
        Composite c1 = factory.createComposite(this);
        c1.setLayout(new TableColumnLayout());

        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        //gd.widthHint = 60;  // stops width creep
        c1.setLayoutData(gd);
        
        fItemsTableViewer = new ItemsTableViewer(c1, SWT.BORDER);
        fItemsTableViewer.getTable().addListener(SWT.Modify, fModifyListener);
        
        createButtonPanel(this);
    }

    private Composite createButtonPanel(Composite parent) {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite panel = factory.createComposite(parent);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        panel.setLayout(layout);
        GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        panel.setLayoutData(gd);
        
        Button btn = factory.createButton(panel, Messages.IndexSearchClassSection_0, SWT.PUSH);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        btn.setLayoutData(gd);
        btn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fItemsTableViewer.addNewEntry();
            }
        });
        
        btn = factory.createButton(panel, Messages.IndexSearchClassSection_1, SWT.PUSH);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        btn.setLayoutData(gd);
        btn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fItemsTableViewer.removeSelectedEntries();
            }
        });
        
        return panel;
    }

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IIndexSearchModel) {
            fIndexSearch = (IIndexSearchModel)element;
        }
        else {
            throw new RuntimeException("Should have been an Index Search Part"); //$NON-NLS-1$
        }
        
        refresh();
    }
    
    public void refresh() {
        if(fIndexSearch == null) {
            return;
        }
        
        List<String> list = fIndexSearch.getIndexClasses();
        fItemsTableViewer.setItems(list);
    }
}
