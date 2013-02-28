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
package org.tencompetence.ldauthor.ui.editors.resources;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.widgets.Section;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.resources.IResourceFileModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.LDEditorInput;
import org.tencompetence.ldauthor.ui.views.ViewManager;
import org.tencompetence.ldauthor.ui.views.preview.PreviewView;
import org.tencompetence.ldauthor.utils.FileUtils;


/**
 * Resource Files Composite
 * 
 * @author Phillip Beauvoir
 * @version $Id: ResourceFilesComposite.java,v 1.22 2009/05/19 18:21:04 phillipus Exp $
 */
public class ResourceFilesComposite extends Composite implements PropertyChangeListener {

    private ResourceFilesTableViewer fResourceFilesTableViewer;
    
    private ILDEditorPart fEditor;
    
    private ILDModel fLDModel;
    
    private IAction fActionNewFile;
    private IAction fActionDeleteFile;
    private IAction fActionSelectAll;

    private IResourceModel fResource;
    
    private File fRootFolder;
    
    public ResourceFilesComposite(ILDEditorPart editor, Composite parent, int style) {
        super(parent, style);
        
        fEditor = editor;
        
        fLDModel = ((LDEditorInput)editor.getEditorInput()).getModel();
        
        fRootFolder = fLDModel.getRootFolder();
        
        createActions();
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        
        Section section = AppFormToolkit.getInstance().createSection(this, Section.TITLE_BAR);
        section.setText(Messages.ResourceFilesComposite_0);
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        ToolBar toolBar = createToolBar(section);
        section.setTextClient(toolBar);
        
        // Use a parent composite for the table with TableColumnLayout.
        // This allows for auto-resizing of table columns
        Composite client = AppFormToolkit.getInstance().createComposite(section);
        client.setLayout(new TableColumnLayout());
        section.setClient(client);
        
        fResourceFilesTableViewer = new ResourceFilesTableViewer(editor, client, SWT.BORDER);
        fResourceFilesTableViewer.getControl().setEnabled(false);
        
        /*
         * Listen to selections
         */
        fResourceFilesTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                updateActions(event.getSelection());
                fEditor.getSite().getSelectionProvider().setSelection(event.getSelection());
            }
        });
        
        /*
         * Listen to double-clicks
         */
        fResourceFilesTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                ViewManager.showViewPart(PreviewView.ID, false);
            }
        });
        
        hookContextMenu();
        registerGlobalActions();
        
        fLDModel.addPropertyChangeListener(this);
    }

    public void setResource(IResourceModel resource) {
        fResource = resource;
        fResourceFilesTableViewer.setResource(resource);
        fActionNewFile.setEnabled(fResource != null);
    }
    
    /**
     * @return The Table Viewer
     */
    public ResourceFilesTableViewer getViewer() {
        return fResourceFilesTableViewer;
    }

    /**
     * Actions
     */
    private void createActions() {
        fActionDeleteFile = new LDAuthorActionFactory.DeleteAction() {
            @Override
            public void run() {
                deleteSelected();
            }
        };
        
        fActionDeleteFile.setEnabled(false);
        
        fActionNewFile = new Action(Messages.ResourceFilesComposite_1) {
            @Override
            public void run() {
                newResourceFiles();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_NEWFILE);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionNewFile.setEnabled(false);
        
        // Select All
        fActionSelectAll = new LDAuthorActionFactory.SelectAllAction() {
            @Override
            public void run() {
                fResourceFilesTableViewer.getTable().selectAll();
                updateActions(fResourceFilesTableViewer.getSelection());
            }
        };
    }

    /**
     * Delete the selected resources
     */
    private void deleteSelected() {
        List<?> selected = ((IStructuredSelection)fResourceFilesTableViewer.getSelection()).toList();
        
        if(!askUserDeleteFiles(selected.size() > 1)) {
            return;
        }
        
        for(Object o : selected) {
            if(o instanceof IResourceFileModel) {
                IResourceFileModel file = (IResourceFileModel)o;
                fResource.getFiles().remove(file);
            }
        }
        
        fResourceFilesTableViewer.refresh();
        fLDModel.setDirty();
    }

    /**
     * Ask the user whether they wish to delete the given objects
     * 
     * @param objects The objects to delete
     * @return True if the user said yes, false if user says no or cancels
     */
    private boolean askUserDeleteFiles(boolean plural) {
        // Confirmation dialog
        return MessageDialog.openQuestion(
                Display.getDefault().getActiveShell(),
                Messages.ResourceFilesComposite_2,
                plural ?
                        Messages.ResourceFilesComposite_3 
                        : 
                        Messages.ResourceFilesComposite_4);
    }

    /**
     * Add a new Resource File
     */
    private void newResourceFiles() {
        File[] files = FileUtils.selectFilesFromRootFolder(fRootFolder, fResourceFilesTableViewer.getControl().getShell());
        if(files == null) {
            return;
        }
        
        for(File file : files) {
            String href = FileUtils.getRelativePath(file, fRootFolder);
            if(fResource.getResourceFileByHref(href) == null) {
                IResourceFileModel resourceFile = (IResourceFileModel)LDModelFactory.createModelObject(LDModelFactory.FILE, fLDModel);
                resourceFile.setHref(href);
                fResource.getFiles().add(resourceFile);
            }
        }
        
        fResourceFilesTableViewer.refresh();
        fLDModel.setDirty();
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);

        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });

        Menu menu = menuMgr.createContextMenu(fResourceFilesTableViewer.getControl());
        fResourceFilesTableViewer.getControl().setMenu(menu);

        fEditor.getSite().registerContextMenu(menuMgr, fResourceFilesTableViewer);
    }
    
    /**
     * Fill the right-click menu
     * 
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        boolean isEmpty = fResourceFilesTableViewer.getSelection().isEmpty();
        
        manager.add(fActionNewFile);

        if(!isEmpty) {
            manager.add(new Separator());
            manager.add(fActionDeleteFile);
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_END));
        }
        
        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    /**
     * Register Global Actions on focus events
     */
    private void registerGlobalActions() {
        final IActionBars bars = fEditor.getEditorSite().getActionBars();
        
        fResourceFilesTableViewer.getControl().addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), fActionDeleteFile);
                bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), fActionSelectAll);
                bars.updateActionBars();
            }
            
            public void focusLost(FocusEvent e) {
                bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
                bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), null);
                bars.updateActionBars();
            }
        });
    }
    
    private void updateActions(ISelection selection) {
        IResourceFileModel file = (IResourceFileModel)((IStructuredSelection)selection).getFirstElement();
        fActionDeleteFile.setEnabled(file != null);
        fActionNewFile.setEnabled(fResource != null);
    }

    /**
     * Create the toolbar
     * 
     * @param parent
     * @return
     */
    private ToolBar createToolBar(Composite parent) {
        ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
        toolBar.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
        //fFormToolkit.adapt(toolBar);
        
        ToolBarManager toolBarmanager = new ToolBarManager(toolBar);
        toolBarmanager.add(fActionNewFile);
        
        toolBarmanager.update(true);
        
        return toolBar;
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        
        if(IResourceFileModel.PROPERTY_RESOURCEFILE_HREF.equals(propertyName)) {
            fLDModel.setDirty();
            fResourceFilesTableViewer.refresh();
        }
    }
}
