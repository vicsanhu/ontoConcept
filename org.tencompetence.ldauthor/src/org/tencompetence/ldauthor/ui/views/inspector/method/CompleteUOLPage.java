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
package org.tencompetence.ldauthor.ui.views.inspector.method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.method.ICompleteUOLType;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractTabbedInspectorPage;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;


/**
 * CompleteUOLPage
 * 
 * @author Phillip Beauvoir
 * @version $Id: CompleteUOLPage.java,v 1.5 2009/06/10 10:16:49 phillipus Exp $
 */
public class CompleteUOLPage extends AbstractTabbedInspectorPage {
    
    private CompleteUOLSection fMainSection;
    
    private OnCompleteUOLFeedbackSection fFeedbackSection;
    
    private static final int TAB_MAIN = 0;
    private static final int TAB_FEEDBACK = 1;

    public CompleteUOLPage(Composite parent) {
        super(parent);
        
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        CTabItem item = factory.createTabItem(getFolder(), SWT.NULL);
        item.setText(Messages.CompleteUOLPage_0);
        
        fMainSection = new CompleteUOLSection(getFolder());
        fMainSection.createControls();
        item.setControl(fMainSection);
        
        item = factory.createTabItem(getFolder(), SWT.NULL);
        item.setText(Messages.CompleteUOLPage_1);
        
        getFolder().setSelection(TAB_MAIN);
    }

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        if(!(element instanceof ICompleteUOLType)) {
            throw new RuntimeException("Wrong input type"); //$NON-NLS-1$
        }
        
        super.setInput(provider, element);
        
        if(fMainSection != null) {
            fMainSection.setInput(provider, element);
        }
        
        if(fFeedbackSection != null) {
            fFeedbackSection.setInput(provider, element);
        }
    }

    @Override
    protected void createTabSection(CTabItem tabItem) {
        // Lazily create the other tabs
        switch(getFolder().indexOf(tabItem)) {
            case TAB_FEEDBACK:
                if(fFeedbackSection == null) {
                    fFeedbackSection = new OnCompleteUOLFeedbackSection(getFolder());
                    fFeedbackSection.createControls();
                    tabItem.setControl(fFeedbackSection);
                    fFeedbackSection.setInput(getInspectorProvider(), getElement());
                }
                break;

            default:
                break;
        }
        
    }
}
