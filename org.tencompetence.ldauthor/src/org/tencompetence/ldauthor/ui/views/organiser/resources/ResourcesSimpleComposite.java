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
package org.tencompetence.ldauthor.ui.views.organiser.resources;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.resources.IResourcesModel;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.views.ViewManager;
import org.tencompetence.ldauthor.ui.views.organiser.IOrganiserComposite;
import org.tencompetence.ldauthor.ui.views.preview.PreviewView;


/**
 * ResourcesComposite
 * 
 * @author Phillip Beauvoir
 * @version $Id: ResourcesSimpleComposite.java,v 1.13 2009/05/19 18:21:09 phillipus Exp $
 */
public class ResourcesSimpleComposite extends Composite implements IOrganiserComposite, PropertyChangeListener {
    
    private ViewPart fViewPart;
    
    private ILDModel fCurrentLDModel;

    private ResourcesSimpleTableViewer fTableViewer;
        
    public ResourcesSimpleComposite(ViewPart viewPart, Composite parent, int style) {
        super(parent, style);
        
        fViewPart = viewPart;
        
        setLayout(new TableColumnLayout());
        
        fTableViewer = new ResourcesSimpleTableViewer(this, SWT.MULTI);
        fTableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Tell the selection provider
                fViewPart.getSite().getSelectionProvider().setSelection(event.getSelection());
            }
        });
        
        // Double-click opens the Viewer
        fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                ViewManager.showViewPart(PreviewView.ID, false);
            }
        });

        new ResourcesSimpleTableViewerDragDropHandler(fTableViewer);
    }
    
    /**
     * Register Global Action Handlers
     */
    private void registerGlobalActions() {
        IActionBars actionBars = fViewPart.getViewSite().getActionBars();
        actionBars.clearGlobalActionHandlers();
    }
    
    /**
     * Make Local Toolbar items
     */
    private void makeLocalToolBarActions() {
        IActionBars bars = fViewPart.getViewSite().getActionBars();
        IToolBarManager manager = bars.getToolBarManager();
        manager.removeAll();
        bars.updateActionBars();
    }
    
    public void updateFocus() {
        fTableViewer.getControl().setFocus();
        registerGlobalActions();
        makeLocalToolBarActions();
    }
    
    public void setActiveEditor(ILDMultiPageEditor editor) {
        if(fCurrentLDModel != null) {
            fCurrentLDModel.removePropertyChangeListener(this);
        }
        
        if(editor != null) {
            fCurrentLDModel = (ILDModel)editor.getAdapter(ILDModel.class);
            fCurrentLDModel.addPropertyChangeListener(this);
            fTableViewer.setInput(fCurrentLDModel);
        }
        else {
            fCurrentLDModel = null;
            fTableViewer.setInput(null);
        }
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        // Resource updated - update the whole tree
        if(evt.getSource() instanceof IResourceModel || evt.getSource() instanceof IResourcesModel) {
            fTableViewer.refresh();
        }
    }
    
    /**
     * Dispose of all objects
     */
    @Override
    public void dispose() {
        super.dispose();
        
        if(fCurrentLDModel != null) {
            fCurrentLDModel.removePropertyChangeListener(this);
            fCurrentLDModel = null;
        }
    }

}
