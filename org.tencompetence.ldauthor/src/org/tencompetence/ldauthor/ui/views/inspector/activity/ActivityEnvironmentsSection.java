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
package org.tencompetence.ldauthor.ui.views.inspector.activity;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.environments.IEnvironmentRefOwner;
import org.tencompetence.ldauthor.ui.editors.IEnvironmentEditorPage;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;


/**
 * Inspector Section for Environments in an Activity
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActivityEnvironmentsSection.java,v 1.8 2009/06/10 10:16:49 phillipus Exp $
 */
public class ActivityEnvironmentsSection extends AbstractInspectorSection {
    
    private IEnvironmentRefOwner fEnvironmentRefOwner;
    
    private EnvironmentListViewer fTableViewer;

    public ActivityEnvironmentsSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        GridLayout layout = new GridLayout();
        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, true));
        
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Label label = factory.createLabel(this, Messages.ActivityEnvironmentsSection_0);
        label.setToolTipText(Messages.ActivityEnvironmentsSection_1);
        
        Hyperlink link = factory.createHyperlink(this, Messages.ActivityEnvironmentsSection_2, SWT.NULL);
        link.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent e) {
                ILDMultiPageEditor editor = (ILDMultiPageEditor)getInspectorProvider().getAdapter(ILDMultiPageEditor.class);
                if(editor != null) {
                    editor.setActivePage(ILDMultiPageEditor.ENVIRONMENTS_TAB);
                }
            }
        });
        link.setToolTipText(Messages.ActivityEnvironmentsSection_3);
        
        fTableViewer = new EnvironmentListViewer(this, SWT.BORDER);
        fTableViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        /*
         * Double-click on an Environment jumps to the Editor
         */
        fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                ILDMultiPageEditor editor = (ILDMultiPageEditor)getInspectorProvider().getAdapter(ILDMultiPageEditor.class);
                if(editor != null) {
                    editor.setActivePage(ILDMultiPageEditor.ENVIRONMENTS_TAB);
                    IEnvironmentEditorPage page = editor.getEnvironmentEditorPage();
                    ILDModelObject object = (ILDModelObject)((StructuredSelection)fTableViewer.getSelection()).getFirstElement();
                    page.selectEditPart(object);
                }
            }
        });
    }
    
    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IEnvironmentRefOwner) {
            fEnvironmentRefOwner = (IEnvironmentRefOwner)element;
        }
        else {
            throw new RuntimeException("Should have been an Activity Part"); //$NON-NLS-1$
        }
        
        refresh();
    }
    
    public void refresh() {
        if(fEnvironmentRefOwner != null) {
            fTableViewer.setModel(fEnvironmentRefOwner);
        }
    }
}
