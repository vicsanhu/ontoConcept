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
package org.tencompetence.ldauthor.ui.views.organiser.environments;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.environments.IEnvironmentsModel;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.ui.dialogs.RenameObjectDialog;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.views.IOrganiserView;
import org.tencompetence.ldauthor.ui.views.ViewManager;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;
import org.tencompetence.ldauthor.ui.views.organiser.IOrganiserComposite;
import org.tencompetence.ldauthor.ui.views.preview.PreviewView;


/**
 * EnvironmentsComposite
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentsComposite.java,v 1.26 2009/06/07 14:05:59 phillipus Exp $
 */
public class EnvironmentsComposite extends Composite implements IOrganiserComposite, PropertyChangeListener {
    
    private IOrganiserView fViewPart;
    
    private IAction fActionRename;
    private IAction fActionShowInspector;
    private IAction fActionShowPreview;
    
    private ILDModel fCurrentLDModel;

    private EnvironmentsTableViewer fTableViewer;
    
    public EnvironmentsComposite(IOrganiserView viewPart, Composite parent, int style) {
        super(parent, style);
        
        setLayout(new TableColumnLayout());
        
        fViewPart = viewPart;
        
        makeActions();
        
        fTableViewer = new EnvironmentsTableViewer(this, SWT.MULTI);
        fTableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Pass on selections to the Inspector
                Object o = ((StructuredSelection)event.getSelection()).getFirstElement();
                InspectorManager.getInstance().setInput(fViewPart, o);
                
                // And the selection provider
                fViewPart.getSite().getSelectionProvider().setSelection(event.getSelection());
                
                // Update actions
                updateActions(event.getSelection());
            }
        });
        
        // Double-click opens the Properties View
        fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                InspectorManager.getInstance().showInspector();
            }
        });

        hookContextMenu();
        
        new EnvironmentsTableViewerDragDropHandler(fTableViewer);
    }
    
    /**
     * Make local actions
     */
    private void makeActions() {
        // Inspector
        fActionShowInspector = new Action(Messages.EnvironmentsComposite_0) {
            @Override
            public void run() {
                InspectorManager.getInstance().showInspector();
            }
        };
        
        // Preview
        fActionShowPreview = new Action(Messages.EnvironmentsComposite_1) {
            @Override
            public void run() {
                ViewManager.showViewPart(PreviewView.ID, false);
            }
        };
        
        // Rename
        fActionRename = new LDAuthorActionFactory.RenameAction() {
            @Override
            public void run() {
                renameEnvironment();
            }
        };
        fActionRename.setEnabled(false);
    }
    
    /**
     * Register Global Action Handlers
     */
    private void registerGlobalActions() {
        IActionBars actionBars = fViewPart.getViewSite().getActionBars();
        actionBars.clearGlobalActionHandlers();
        
        // Register our interest in the global menu actions
        actionBars.setGlobalActionHandler(ActionFactory.RENAME.getId(), fActionRename);
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
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#EnvironmentsOrganiserPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        
        Menu menu = menuMgr.createContextMenu(fTableViewer.getControl());
        fTableViewer.getControl().setMenu(menu);
        
        fViewPart.getSite().registerContextMenu(menuMgr, fTableViewer);
    }
    
    /**
     * Update the Local Actions depending on the selection 
     * @param selection
     */
    private void updateActions(ISelection selection) {
        boolean isEmpty = selection.isEmpty();
        fActionShowInspector.setEnabled(!isEmpty);
        fActionShowPreview.setEnabled(!isEmpty);
        fActionRename.setEnabled(!isEmpty);
    }

    /**
     * Fill the right-click menu for the Table
     * 
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        boolean isEmpty = fTableViewer.getSelection().isEmpty();
        
        if(!isEmpty) {
            manager.add(fActionRename);
            manager.add(new Separator());
            manager.add(fActionShowInspector);
            manager.add(fActionShowPreview);
        }

        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    public void updateFocus() {
        updateActions(fTableViewer.getSelection());
        registerGlobalActions();
        makeLocalToolBarActions();
        
        fTableViewer.getControl().setFocus();
        
        // Have to do this to update the Inspector
        Object o = ((StructuredSelection)fTableViewer.getSelection()).getFirstElement();
        InspectorManager.getInstance().setInput(fViewPart, o);
    }
    
    /**
     * Rename
     */
    private void renameEnvironment() {
        IEnvironmentModel selected = (IEnvironmentModel)((StructuredSelection)fTableViewer.getSelection()).getFirstElement();
        if(selected != null) {
            RenameObjectDialog dialog = new RenameObjectDialog(Display.getDefault().getActiveShell(), selected.getTitle());
            if(dialog.open()) {
                String name = dialog.getNewName();
                if(name != null) {
                    selected.setTitle(name);
                    fCurrentLDModel.setDirty();
                }
            }
        }
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
        // Environment updated - update the whole tree
        if(evt.getSource() instanceof IEnvironmentModel || evt.getSource() instanceof IEnvironmentsModel) {
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
