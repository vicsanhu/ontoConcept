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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.tencompetence.imsldmodel.environments.IIndexSearchModel;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;


/**
 * Inspector Section for Index Search Elements
 * 
 * @author Phillip Beauvoir
 * @version $Id: IndexSearchElementsSection.java,v 1.6 2009/06/10 10:16:49 phillipus Exp $
 */
public class IndexSearchElementsSection extends AbstractInspectorSection {
    
    private IIndexSearchModel fIndexSearch;
    
    private IndexElementTreeViewer fIndexElementTreeViewer;
    
    private Listener fModifyListener = new Listener() {
        public void handleEvent(Event event) {
            if(fIndexSearch != null) {
                fIndexSearch.setIndexElements(fIndexElementTreeViewer.getItems());
                fIndexSearch.getLDModel().setDirty();
            }
        }
    };
    
    public IndexSearchElementsSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        GridLayout layout = new GridLayout(2, false);
        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, true));
        
        fIndexElementTreeViewer = new IndexElementTreeViewer(this, SWT.BORDER);
        fIndexElementTreeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        fIndexElementTreeViewer.getTree().addListener(SWT.Modify, fModifyListener);
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
        if(fIndexSearch != null) {
            fIndexElementTreeViewer.setIndexSearch(fIndexSearch);
        }
    }
}
