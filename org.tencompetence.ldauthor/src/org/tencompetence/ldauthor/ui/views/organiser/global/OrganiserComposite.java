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
package org.tencompetence.ldauthor.ui.views.organiser.global;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.operations.RedoActionHandler;
import org.eclipse.ui.operations.UndoActionHandler;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.actions.NewLDAction;
import org.tencompetence.ldauthor.actions.NewOrganiserFolderAction;
import org.tencompetence.ldauthor.actions.NewResourceAction;
import org.tencompetence.ldauthor.organisermodel.IOrganiserContainer;
import org.tencompetence.ldauthor.organisermodel.IOrganiserFolder;
import org.tencompetence.ldauthor.organisermodel.IOrganiserIndex;
import org.tencompetence.ldauthor.organisermodel.IOrganiserLD;
import org.tencompetence.ldauthor.organisermodel.IOrganiserObject;
import org.tencompetence.ldauthor.organisermodel.IOrganiserResource;
import org.tencompetence.ldauthor.organisermodel.impl.OrganiserIndex;
import org.tencompetence.ldauthor.ui.common.DropDownAction;
import org.tencompetence.ldauthor.ui.editors.EditorManager;
import org.tencompetence.ldauthor.ui.editors.browser.BrowserEditor;
import org.tencompetence.ldauthor.ui.editors.browser.BrowserEditorOrganiserResourceInput;
import org.tencompetence.ldauthor.ui.views.IOrganiserView;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;
import org.tencompetence.ldauthor.ui.views.organiser.IOrganiserComposite;
import org.tencompetence.ldauthor.ui.views.organiser.global.dnd.OrganiserTreeViewerDragDropHandler;
import org.tencompetence.ldauthor.ui.views.organiser.global.operations.CopyOrganiserEntriesOperation;
import org.tencompetence.ldauthor.ui.views.organiser.global.operations.CopyOrganiserEntryOperation;
import org.tencompetence.ldauthor.ui.views.organiser.global.operations.DeleteOrganiserEntriesOperation;
import org.tencompetence.ldauthor.ui.views.organiser.global.operations.RenameOrganiserEntryOperation;


/**
 * OrganiserComposite
 * 
 * @author Phillip Beauvoir
 * @version $Id: OrganiserComposite.java,v 1.17 2008/11/21 18:57:14 phillipus Exp $
 */
public class OrganiserComposite extends Composite implements PropertyChangeListener, IOrganiserComposite {
    
    private IOrganiserView fViewPart;
    
    /*
     * Actions
     */
    private IAction fActionOpen;
    private IAction fActionRename;
    private IAction fActionShowInspector;
    private IAction fActionCut;
    private IAction fActionCopy;
    private IAction fActionPaste;
    private IAction fActionDelete;
    private IAction fActionSelectAll;
    
    private NewLDAction fActionNewLD;
    private NewOrganiserFolderAction fActionNewFolder;
    private NewResourceAction fActionNewResource;
    
    private DropDownAction fDropDownMenu;
    
    /*
     * Undo and redo stuff
     */
    private UndoActionHandler fUndoAction;
    private RedoActionHandler fRedoAction;
    
    // Have a static undo context in case the view is closed
    private static IUndoContext fUndoContext = new ObjectUndoContext("organiser"); //$NON-NLS-1$

    private Clipboard fClipboard = new Clipboard(null);
    
    /**
     * The Tree Viewer
     */
    private OrganiserTreeViewer fTreeViewer;
    
    public OrganiserComposite(IOrganiserView viewPart, Composite parent, int style) {
        super(parent, style);
        
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        setLayout(layout);
        
        fViewPart = viewPart;
        
        initializeOperationHistory();
        
        // Create the Tree Viewer first    
        fTreeViewer = new OrganiserTreeViewer(this, SWT.MULTI);
        fTreeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        
        fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Update the Inspector
                Object o = ((StructuredSelection)event.getSelection()).getFirstElement();
                InspectorManager.getInstance().setInput(fViewPart, o);
            }
        });

        makeActions();
        hookContextMenu();
        
        /*
         * Listen to Selections to update local Actions
         */
        getViewer().addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                updateActions(event.getSelection());
            }
        });

        /*
         * Listen to Double-click Action
         */
        getViewer().addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                handleOpenAction();
            }
        });
        
        // Register us as a Organiser Model Listener
        OrganiserIndex.getInstance().addPropertyChangeListener(this);

        // Drag and Drop support
        new OrganiserTreeViewerDragDropHandler(fTreeViewer, fUndoContext);
        
        // Set Data Model Domain
        getViewer().setInput(OrganiserIndex.getInstance());
    }
    
    public void updateFocus() {
        registerGlobalActions();
        makeLocalToolBarActions();
        
        getViewer().getControl().setFocus();

        // Have to do this to update Inspector
        Object o = ((StructuredSelection)getViewer().getSelection()).getFirstElement();
        InspectorManager.getInstance().setInput(fViewPart, o);
    }

    /**
     * Make local actions
     */
    private void makeActions() {
        IWorkbenchWindow window = fViewPart.getViewSite().getWorkbenchWindow();
        
        // New LD
        fActionNewLD = new NewLDAction(window);
        
        // New Folder
        fActionNewFolder = new NewOrganiserFolderAction(window);

        // New Resource
        fActionNewResource = new NewResourceAction(window);
        
        // Drop Down Menu
        fDropDownMenu = new DropDownAction(Messages.OrganiserComposite_0) {
            @Override
            public void run() {
                Menu menu = getMenu(getShell());
                menu.setVisible(true);
            }

            @Override
            protected void addActions(Menu menu) {
                addActionToMenu(menu, fActionNewLD);
                addActionToMenu(menu, fActionNewFolder);
                addActionToMenu(menu, fActionNewResource);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD);
            }
        };

        // Delete
        fActionDelete = new LDAuthorActionFactory.DeleteAction() {
            @Override
            public void run() {
                handleDeleteAction();
            }
        };
        fActionDelete.setEnabled(false);
        
        // Rename
        fActionRename = new LDAuthorActionFactory.RenameAction() {
            @Override
            public void run() {
                handleRenameAction();
            }
        };
        fActionRename.setEnabled(false);
        
        // Cut
        fActionCut = new LDAuthorActionFactory.CutAction() {
            @Override
            public void run() {
                handleCutAction();
            }
        };
        fActionCut.setEnabled(false);

        // Copy
        fActionCopy = new LDAuthorActionFactory.CopyAction() {
            @Override
            public void run() {
                handleCopyAction();
            }
        };
        fActionCopy.setEnabled(false);

        // Paste
        fActionPaste = new LDAuthorActionFactory.PasteAction() {
            @Override
            public void run() {
                handlePasteAction();
            }
        };
        fActionPaste.setEnabled(false);

        // Open
        fActionOpen = new LDAuthorActionFactory.OpenAction() {
            @Override
            public void run() {
                handleOpenAction();
            }
        };
        fActionOpen.setEnabled(false);
        
        // Select All
        fActionSelectAll = new LDAuthorActionFactory.SelectAllAction() {
            @Override
            public void run() {
                getViewer().getTree().selectAll();
                updateActions(getViewer().getSelection());
            }
        };
        
        // Inspector
        fActionShowInspector = new Action(Messages.OrganiserComposite_1) {
            @Override
            public void run() {
                InspectorManager.getInstance().showInspector();
            }
        };

        // Register the Keybinding for F3 Open action
        //IHandlerService service = (IHandlerService)fViewPart.getViewSite().getService(IHandlerService.class);
        //service.activateHandler(fActionOpen.getActionDefinitionId(), new ActionHandler(fActionOpen));
        
        // Undo
        fUndoAction = new UndoActionHandler(fViewPart.getSite(), fUndoContext);
        fRedoAction = new RedoActionHandler(fViewPart.getSite(), fUndoContext);
    }
    
    /**
     * Register Global Action Handlers
     */
    private void registerGlobalActions() {
        IActionBars actionBars = fViewPart.getViewSite().getActionBars();
        actionBars.clearGlobalActionHandlers();
        
        // Register our interest in the global menu actions
        actionBars.setGlobalActionHandler(ActionFactory.RENAME.getId(), fActionRename);
        actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), fActionCut);
        actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), fActionCopy);
        actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), fActionPaste);
        actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), fActionDelete);
        actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), fActionSelectAll);
        actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), fUndoAction);
        actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), fRedoAction);
    }

    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#OrganiserPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        
        Menu menu = menuMgr.createContextMenu(getViewer().getControl());
        getViewer().getControl().setMenu(menu);
        
        fViewPart.getSite().registerContextMenu(menuMgr, getViewer());
    }
    
    /**
     * Make Local Toolbar items
     */
    private void makeLocalToolBarActions() {
        IActionBars bars = fViewPart.getViewSite().getActionBars();
        IToolBarManager manager = bars.getToolBarManager();
        manager.removeAll();

        manager.add(new Separator(IWorkbenchActionConstants.NEW_GROUP));
        manager.add(fDropDownMenu);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        
        bars.updateActionBars();
    }
    
    /**
     * Update the Local Actions depending on the selection 
     * @param selection
     */
    private void updateActions(ISelection selection) {
        boolean isEmpty = selection.isEmpty();
        
        IOrganiserObject obj = (IOrganiserObject)((IStructuredSelection)selection).getFirstElement();
        
        /*
         * Ascertain a non-null parent for a new object
         */
        IOrganiserContainer parentForNewObject = null;
        if(obj instanceof IOrganiserContainer) {
            parentForNewObject = (IOrganiserContainer)obj;
        }
        else if(obj != null) {
            parentForNewObject = obj.getParent();
        }
        else {
            parentForNewObject = OrganiserIndex.getInstance();
        }
        
        fActionDelete.setEnabled(!isEmpty);
        fActionCut.setEnabled(!isEmpty);
        fActionCopy.setEnabled(!isEmpty);
        
        fActionPaste.setEnabled(false);
        
        if(obj instanceof IOrganiserFolder) {
            Object o = fClipboard.getContents(LocalSelectionTransfer.getTransfer());
            if(o instanceof IStructuredSelection) {
                fActionPaste.setEnabled(((IStructuredSelection)o).getFirstElement() instanceof IOrganiserObject);
            }
        }
        
        fActionRename.setEnabled(!isEmpty);
        fActionOpen.setEnabled(!(obj instanceof IOrganiserFolder));
        fActionNewLD.setParent(parentForNewObject);
        fActionNewFolder.setParent(parentForNewObject);
        fActionNewResource.setParent(parentForNewObject);
        fActionShowInspector.setEnabled(!isEmpty);
    }
    
    private void fillContextMenu(IMenuManager manager) {
        boolean isEmpty = getViewer().getSelection().isEmpty();

        IMenuManager newMenu = new MenuManager(Messages.OrganiserComposite_0, "new"); //$NON-NLS-1$
        manager.add(newMenu);

        newMenu.add(fActionNewLD);
        newMenu.add(fActionNewResource);
        newMenu.add(fActionNewFolder);
        manager.add(new Separator());

        if(!isEmpty) {
            manager.add(fActionOpen);
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_START));
            manager.add(fActionCut);
            manager.add(fActionCopy);
            manager.add(fActionPaste);
            manager.add(fActionDelete);
            manager.add(new Separator(IWorkbenchActionConstants.EDIT_END));
            manager.add(fActionRename);
            manager.add(new Separator());
            manager.add(fActionShowInspector);
        }
        
        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /**
     * Edit event happened
     */
    private void handleOpenAction() {
        IOrganiserObject obj = (IOrganiserObject)((IStructuredSelection)getViewer().getSelection()).getFirstElement();
        
        if(obj instanceof IOrganiserLD) {
            IOrganiserLD org = (IOrganiserLD)obj;
            File file = org.getFile();
            if(!file.exists()) {
                MessageDialog.openError(getShell(), Messages.OrganiserComposite_2, Messages.OrganiserComposite_3 + file);
            }
            else {
                EditorManager.openLDEditor(org.getName(), file);
            }
        }

        else if(obj instanceof IOrganiserResource) {
            IOrganiserResource org = (IOrganiserResource)obj;
            String name = org.getName();
            String location = org.getResourceLocation();
            IEditorInput input =  new BrowserEditorOrganiserResourceInput(name, location);
            EditorManager.openEditor(input, BrowserEditor.ID);
        }
    }
    
    /**
     * Paste event happened
     */
    private void handlePasteAction() {
        IOrganiserObject obj = (IOrganiserObject)((IStructuredSelection)getViewer().getSelection()).getFirstElement();
        if(!(obj instanceof IOrganiserFolder)) {
            return;
        }
        
        IStructuredSelection selection = (IStructuredSelection)fClipboard.getContents(LocalSelectionTransfer.getTransfer());
        if(selection != null && !selection.isEmpty()) {
            IOrganiserFolder folder = (IOrganiserFolder)obj;
            List<CopyOrganiserEntryOperation> copyOperationsList = new ArrayList<CopyOrganiserEntryOperation>();
            
            for(Object o : selection.toList()) {
                // Source Object
                IOrganiserObject object = (IOrganiserObject)o;

                // If can paste it and not already in target parent
                if(!folder.getChildren().contains(object)) {
                    // Add to list of Copy operations
                    copyOperationsList.add(new CopyOrganiserEntryOperation(folder, object, true));
                }
            }
            
            // Execute as undoable operation
            if(copyOperationsList.size() > 0) {
                try {
                    getOperationHistory().execute(
                            new CopyOrganiserEntriesOperation(fUndoContext, copyOperationsList),
                            null,
                            null);
                }
                catch(ExecutionException e) {
                    e.printStackTrace();
                }
                
                // Refresh, open, and select target folder
                getViewer().refresh(folder);
                getViewer().expandToLevel(folder, AbstractTreeViewer.ALL_LEVELS);
                getViewer().setSelection(new StructuredSelection(folder));
            }
        }
    }

    /**
     * Copy event happened
     */
    private void handleCopyAction() {
        ISelection selection = getViewer().getSelection();
        if(selection != null && !selection.isEmpty()) {
            LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
            transfer.setSelection(selection);
            fClipboard.setContents(new ISelection[] { selection }, new Transfer[] { transfer } );
        }
    }

    /**
     * Cut event happened
     */
    private void handleCutAction() {
        handleCopyAction();
        handleDeleteAction();
    }

    /**
     * Rename event happened
     */
    private void handleRenameAction() {
        IOrganiserObject obj = (IOrganiserObject)((IStructuredSelection)getViewer().getSelection()).getFirstElement();
        if(obj == null) {
            return;
        }
        
        try {
            getOperationHistory().execute(
                    new RenameOrganiserEntryOperation(fUndoContext, obj),
                    null,
                    null);
        }
        catch(ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete event happened
     */
    private void handleDeleteAction() {
        try {
            getOperationHistory().execute(
                    new DeleteOrganiserEntriesOperation(fUndoContext, fTreeViewer),
                    null,
                    null);
        }
        catch(ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @return The Tree Viewer
     */
    public TreeViewer getViewer() {
        return fTreeViewer;
    }
    
    /**
     * @return The Undo Context
     */
    public static IUndoContext getUndoContext() {
        return fUndoContext;
    }

    /**
     * Dispose of all objects
     */
    @Override
    public void dispose() {
        super.dispose();
        
        // Dispose of undo stuff
        //getOperationHistory().dispose(fUndoContext, true, true, true);
        
        // UnRegister us as a Organiser Model Listener
        OrganiserIndex.getInstance().removePropertyChangeListener(this);
        
        // Clipboard
        fClipboard.dispose();
    }

    
    // =================================================================================
    //                       Property Change Model Listener
    // =================================================================================

    public void propertyChange(PropertyChangeEvent evt) {
        getViewer().refresh();
        
        // Select new one
        if(IOrganiserContainer.PROPERTY_CHILD_ADDED == evt.getPropertyName()) {
            getViewer().setSelection(new StructuredSelection(evt.getSource()), true);
        }
        // Organiser data folder changed
        else if(IOrganiserIndex.PROPERTY_ORGANISER_CHANGED == evt.getPropertyName()) {
            getViewer().setInput(OrganiserIndex.getInstance());
        }
    }

    // =================================================================================
    //                       Undo/Redo stuff
    // =================================================================================

    /*
     * Initialize the workbench operation history for our undo context.
     */
    private void initializeOperationHistory() {
        // Set the undo limit for this context (could put this in Prefs)
        int limit = 99;
        getOperationHistory().setLimit(fUndoContext, limit);
    }

    /*
     * Get the operation history from the workbench.
     */
    private IOperationHistory getOperationHistory() {
        return PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
    }
}
