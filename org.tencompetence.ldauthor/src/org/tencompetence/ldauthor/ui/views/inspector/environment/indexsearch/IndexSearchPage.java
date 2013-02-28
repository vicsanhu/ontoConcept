/*
 * Copyright (c) 2008, Consortium Board TENCompetence
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
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractTabbedInspectorPage;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;


/**
 * Inspector Index Search Page
 * 
 * @author Phillip Beauvoir
 * @version $Id: IndexSearchPage.java,v 1.4 2009/06/10 10:16:49 phillipus Exp $
 */
public class IndexSearchPage extends AbstractTabbedInspectorPage {
    
    private IndexSearchSettingsSection fSettingsSection;
    private IndexSearchClassSection fClassesSection;
    private IndexSearchElementsSection fElementsSection;
    private IndexSearchTypesSection fTypesSection;
    
    private static final int TAB_SETTINGS = 0;
    private static final int TAB_CLASSES = 1;
    private static final int TAB_ELEMENTS = 2;
    private static final int TAB_TYPES = 3;

    public IndexSearchPage(Composite parent) {
        super(parent);
        
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        CTabItem item = factory.createTabItem(getFolder(), SWT.NULL);
        item.setText(Messages.IndexSearchPage_0);
        
        fSettingsSection = new IndexSearchSettingsSection(getFolder());
        fSettingsSection.createControls();
        item.setControl(fSettingsSection);
        
        item = factory.createTabItem(getFolder(), SWT.NULL);
        item.setText(Messages.IndexSearchPage_1);
        
        item = factory.createTabItem(getFolder(), SWT.NULL);
        item.setText(Messages.IndexSearchPage_2);
        
        item = factory.createTabItem(getFolder(), SWT.NULL);
        item.setText(Messages.IndexSearchPage_3);
        
        getFolder().setSelection(TAB_SETTINGS);
    }

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(fSettingsSection != null) {
            fSettingsSection.setInput(provider, element);
        }
        
        if(fClassesSection != null) {
            fClassesSection.setInput(provider, element);
        }
        
        if(fElementsSection != null) {
            fElementsSection.setInput(provider, element);
        }
        
        if(fTypesSection != null) {
            fTypesSection.setInput(provider, element);
        }
    }

    @Override
    protected void createTabSection(CTabItem tabItem) {
        // Lazily create the other tabs
        switch(getFolder().indexOf(tabItem)) {
            case TAB_CLASSES:
                if(fClassesSection == null) {
                    fClassesSection = new IndexSearchClassSection(getFolder());
                    fClassesSection.createControls();
                    tabItem.setControl(fClassesSection);
                    fClassesSection.setInput(getInspectorProvider(), getElement());
                }
                break;

            case TAB_ELEMENTS:
                if(fElementsSection == null) {
                    fElementsSection = new IndexSearchElementsSection(getFolder());
                    fElementsSection.createControls();
                    tabItem.setControl(fElementsSection);
                    fElementsSection.setInput(getInspectorProvider(), getElement());
                }
                break;
                
            case TAB_TYPES:
                if(fTypesSection == null) {
                    fTypesSection = new IndexSearchTypesSection(getFolder());
                    fTypesSection.createControls();
                    tabItem.setControl(fTypesSection);
                    fTypesSection.setInput(getInspectorProvider(), getElement());
                }
                break;

            default:
                break;
        }
        
    }
}
