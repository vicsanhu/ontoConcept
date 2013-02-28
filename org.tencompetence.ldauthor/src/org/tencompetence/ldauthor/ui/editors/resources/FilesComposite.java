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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.menus.IMenuService;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.dialogs.NewFileDialog;
import org.tencompetence.ldauthor.ui.dialogs.NewFolderDialog;
import org.tencompetence.ldauthor.ui.dialogs.RenameFileDialog;
import org.tencompetence.ldauthor.ui.editors.EditorManager;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.LDEditorInput;
import org.tencompetence.ldauthor.ui.views.ViewManager;
import org.tencompetence.ldauthor.ui.views.preview.PreviewView;
import org.tencompetence.ldauthor.utils.FileUtils;


/**
 * FilesComposite
 * 
 * @author Phillip Beauvoir
 * @version $Id: FilesComposite.java,v 1.33 2009/06/07 14:05:59 phillipus Exp $
 */
public class FilesComposite extends Composite implements IFilesComposite {

    private FileTreeViewer fFileTreeViewer;
    
    private ILDEditorPart fEditor;
    
    private ILDModel fLDModel;
    
    private IAction fActionNewFolder;
    private IAction fActionImport;
    private IAction fActionEditFile;
    private IAction fActionViewFile;
    private IAction fActionNewFile;
    private IAction fActionRefresh;
    private IAction fActionDelete;
    private IAction fActionRename;
    private IAction fActionSelectAll;
    private IAction fActionCut;
    private IAction fActionCopy;
    private IAction fActionPaste;
    
    private Clipboard fClipboard = new Clipboard(null);
    
    private boolean fCutOperation = false;
    
    private File fRootFolder;
    
    public FilesComposite(ILDEditorPart editor, Composite parent, int style) {
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
        section.setText(Messages.FilesComposite_0);
        section.setToolTipText(fRootFolder.toString());
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Composite client = AppFormToolkit.getInstance().createComposite(section);
        layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);
        section.setClient(client);

        ToolBar toolBar = createToolBar(section);
        section.setTextClient(toolBar);
        
        fFileTreeViewer = new FileTreeViewer(client, SWT.BORDER, editor);
        fFileTreeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        fFileTreeViewer.setRootFolder(fRootFolder);
        
        /*
         * Listen to selections
         */
        fFileTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                updateActions(event.getSelection());
                fEditor.getSite().getSelectionProvider().setSelection(event.getSelection());
            }
        });
        
        /*
         * Listen to double-clicks
         */
        fFileTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                File file = (File)((IStructuredSelection)event.getSelection()).getFirstElement();
                if(file != null && file.exists() && file.getName().endsWith(".xml")) { //$NON-NLS-1$
                    EditorManager.editFile(file, fLDModel);
                }
                else {
                    ViewManager.showViewPart(PreviewView.ID, false);
                }
            }
        });

        hookContextMenu();
        registerGlobalActions();
        
        new FileTreeViewerDragDropHandler(fFileTreeViewer, editor);
    }

    /* (non-Javadoc)
     * @see org.tencompetence.ldauthor.ui.editors.resources.IFilesComposite#getTreeViewer()
     */
    public TreeViewer getTreeViewer() {
        return fFileTreeViewer;
    }
    
    /**
     * Actions
     */
    private void createActions() {
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

        // Delete
        fActionDelete = new LDAuthorActionFactory.DeleteAction() {
            @Override
            public void run() {
                deleteSelected();
            }
        };
        
        fActionDelete.setEnabled(false);
        
        // View File
        fActionViewFile = new Action(Messages.FilesComposite_1) {
            @Override
            public void run() {
                ViewManager.showViewPart(PreviewView.ID, false);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_VIEW);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionViewFile.setEnabled(false);
        
        // Edit File
        fActionEditFile = new Action(Messages.FilesComposite_2) {
            @Override
            public void run() {
                File file = (File)((IStructuredSelection)fFileTreeViewer.getSelection()).getFirstElement();
                if(file != null && !file.isDirectory()) {
                    EditorManager.editFile(file, fLDModel);
                }
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_EDIT);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionEditFile.setEnabled(false);
        
        // New File
        fActionNewFile = new Action(Messages.FilesComposite_3) {
            @Override
            public void run() {
                handleNewFile();
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
        
        // New Folder
        fActionNewFolder = new Action(Messages.FilesComposite_4) {
            @Override
            public void run() {
                handleNewFolder();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_NEWFOLDER);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        // Refresh
        fActionRefresh = new LDAuthorActionFactory.RefreshAction() {
            @Override
            public void run() {
                fFileTreeViewer.refresh();
            }
        };
        
        // Select All
        fActionSelectAll = new LDAuthorActionFactory.SelectAllAction() {
            @Override
            public void run() {
                fFileTreeViewer.getTree().selectAll();
                updateActions(fFileTreeViewer.getSelection());
            }
        };
        
        // Rename
        fActionRename = new LDAuthorActionFactory.RenameAction() {
            @Override
            public void run() {
                renameFile();
            }
        };
        fActionRename.setEnabled(false);
        
        // Import
        fActionImport = new Action(Messages.FilesComposite_10) {
            @Override
            public void run() {
                try {
                    handleImport();
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                    MessageDialog.openError(getShell(),
                            "Import", //$NON-NLS-1$
                            ex.getMessage());
                }
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
    }

    private void handleCutAction() {
        handleCopyAction();
        fCutOperation = true;
    }

    private void handleCopyAction() {
        ISelection selection = getTreeViewer().getSelection();
        if(selection != null && !selection.isEmpty()) {
            LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
            transfer.setSelection(selection);
            fClipboard.setContents(new ISelection[] { selection }, new Transfer[] { transfer } );
        }
        fCutOperation = false;
    }

    private void handlePasteAction() {
        Object o = fClipboard.getContents(LocalSelectionTransfer.getTransfer());
        
        if(o instanceof IStructuredSelection && !((IStructuredSelection)o).isEmpty()) {
            File parent = (File)((IStructuredSelection)fFileTreeViewer.getSelection()).getFirstElement();

            if(parent == null) {
                parent = fRootFolder;
            }
            else if(!parent.isDirectory()) {
                parent = parent.getParentFile();
            }
            
            IStructuredSelection selection = (IStructuredSelection)o;
            List<?> list = selection.toList();
            File[] files = new File[list.size()];
            list.toArray(files);
            
            if(fCutOperation) {
                    FileChangeHelper fileHelper = new FileChangeHelper(fLDModel);
                    try {
                        fileHelper.moveFiles(files, parent, null);
                    }
                    catch(IOException ex) {
                        MessageDialog.openError(getShell(), Messages.FilesComposite_5, ex.getMessage());
                    }
                    LocalSelectionTransfer.getTransfer().setSelection(null);
            }
            
            else {
                try {
                    FileUtils.copyFiles(files, parent, null);
                }
                catch(IOException ex) {
                    MessageDialog.openError(getShell(), Messages.FilesComposite_6, ex.getMessage());
                }
            }
            
            fFileTreeViewer.refresh();
        }
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

        Menu menu = menuMgr.createContextMenu(fFileTreeViewer.getControl());
        fFileTreeViewer.getControl().setMenu(menu);

        // Register the Popup for other contributions
        fEditor.getSite().registerContextMenu(POPUP_ID, menuMgr, fFileTreeViewer);
    }

    /**
     * Fill the right-click menu
     * 
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        boolean isEmpty = fFileTreeViewer.getSelection().isEmpty();
        
        manager.add(new Separator(IWorkbenchActionConstants.FILE_START));
        manager.add(fActionNewFile);
        manager.add(fActionNewFolder);
        manager.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));

        manager.add(new Separator());
        manager.add(fActionImport);
        
        if(!isEmpty) {
            manager.add(new Separator());
            manager.add(fActionEditFile);
            manager.add(fActionViewFile);
            manager.add(new Separator());
            manager.add(fActionRename);
            manager.add(new Separator());
            manager.add(fActionCut);
            manager.add(fActionCopy);
            manager.add(fActionPaste);
            manager.add(fActionDelete);
        }
        
        manager.add(new Separator());
        manager.add(fActionRefresh);

        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    /**
     * Register Global Actions on focus events
     */
    private void registerGlobalActions() {
        final IActionBars bars = fEditor.getEditorSite().getActionBars();
        
        fFileTreeViewer.getControl().addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                bars.setGlobalActionHandler(ActionFactory.CUT.getId(), fActionCut);
                bars.setGlobalActionHandler(ActionFactory.COPY.getId(), fActionCopy);
                bars.setGlobalActionHandler(ActionFactory.PASTE.getId(), fActionPaste);
                bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), fActionDelete);
                bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), fActionSelectAll);
                bars.setGlobalActionHandler(ActionFactory.RENAME.getId(), fActionRename);
                bars.setGlobalActionHandler(ActionFactory.REFRESH.getId(), fActionRefresh);
                bars.updateActionBars();
            }
            
            public void focusLost(FocusEvent e) {
                bars.setGlobalActionHandler(ActionFactory.CUT.getId(), null);
                bars.setGlobalActionHandler(ActionFactory.COPY.getId(), null);
                bars.setGlobalActionHandler(ActionFactory.PASTE.getId(), null);
                bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
                bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), null);
                bars.setGlobalActionHandler(ActionFactory.RENAME.getId(), null);
                bars.setGlobalActionHandler(ActionFactory.REFRESH.getId(), null);
                bars.updateActionBars();
            }
        });
    }
    

    private void updateActions(ISelection selection) {
        File file = (File)((IStructuredSelection)selection).getFirstElement();
        fActionRename.setEnabled(file != null);
        fActionDelete.setEnabled(file != null);
        fActionViewFile.setEnabled(file != null);
        fActionEditFile.setEnabled(file != null);
        fActionCut.setEnabled(file != null);
        fActionCopy.setEnabled(file != null);
        
        fActionPaste.setEnabled(false);
        Object o = fClipboard.getContents(LocalSelectionTransfer.getTransfer());
        if(o instanceof IStructuredSelection) {
            Object selected = ((IStructuredSelection)o).getFirstElement();
            fActionPaste.setEnabled(selected instanceof File && ((File)selected).exists());
        }
    }

    private ToolBar createToolBar(Composite parent) {
        ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
        toolBar.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
        
        final ToolBarManager toolBarmanager = new ToolBarManager(toolBar);

        toolBarmanager.add(fActionNewFile);
        toolBarmanager.add(fActionNewFolder);
        toolBarmanager.add(new Separator(IWorkbenchActionConstants.FILE_END));
        toolBarmanager.add(fActionEditFile);
        toolBarmanager.add(fActionViewFile);
        
        // Register the toolbar for other contributions
        final IMenuService service = (IMenuService)fEditor.getEditorSite().getService(IMenuService.class);
        service.populateContributionManager(toolBarmanager, TOOLBAR_ID);
        
        toolBarmanager.update(true);
        
        // Dispose of toolbar contributions
        toolBar.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                service.releaseContributions(toolBarmanager);
            }
        });
        
        return toolBar;
    }
    
    /**
     * New File Action
     */
    private void handleNewFile() {
        File parent = (File)((IStructuredSelection)fFileTreeViewer.getSelection()).getFirstElement();

        if(parent == null) {
            parent = fRootFolder;
        }
        else if(!parent.isDirectory()) {
            parent = parent.getParentFile();
        }
        
        if(parent.exists()) {
            NewFileDialog dialog = new NewFileDialog(parent, getShell());
            if(dialog.open() == Dialog.OK) {
                File newFile = dialog.getFile();
                if(newFile != null) {
                    try {
                        newFile.createNewFile();
                    }
                    catch(IOException ex) {
                        ex.printStackTrace();
                    }
                    fFileTreeViewer.expandToLevel(parent, 1);
                    fFileTreeViewer.refresh();
                    
                    // Open File for editing
                    EditorManager.editFile(newFile, fLDModel);
                }
            }
        }
    }

    /**
     * New Folder Action
     */
    private void handleNewFolder() {
        File parent = (File)((IStructuredSelection)fFileTreeViewer.getSelection()).getFirstElement();

        if(parent == null) {
            parent = fRootFolder;
        }
        else if(!parent.isDirectory()) {
            parent = parent.getParentFile();
        }
        
        if(parent.exists()) {
            NewFolderDialog dialog = new NewFolderDialog(parent, getShell());
            if(dialog.open() == Dialog.OK) {
                File newFolder = dialog.getFolder();
                if(newFolder != null) { 
                    newFolder.mkdirs();
                    fFileTreeViewer.expandToLevel(parent, 1);
                    fFileTreeViewer.refresh();
                }
            }
        }
    }
    
    /**
     * Delete the selected files
     */
    private void deleteSelected() {
        List<?> selected = ((IStructuredSelection)fFileTreeViewer.getSelection()).toList();
        
        if(!askUserDeleteFiles(selected.size() > 1)) {
            return;
        }
        
        for(Object o : selected) {
            if(o instanceof File) {
                File file = (File)o;
                try {
                    if(file.isDirectory()) {
                        FileUtils.deleteFolder(file);
                    }
                    else if(file.exists()) {
                        file.delete();
                    }
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        fFileTreeViewer.refresh();
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
                Messages.FilesComposite_7,
                plural ?
                        Messages.FilesComposite_8 
                        : 
                        Messages.FilesComposite_9);
    }
    
    /**
     * Rename selected file
     */
    private void renameFile() {
        File file = (File)((IStructuredSelection)fFileTreeViewer.getSelection()).getFirstElement();

        if(file != null && file.exists()) {
            RenameFileDialog dialog = new RenameFileDialog(file, getShell());
            if(dialog.open() == IDialogConstants.OK_ID) {
                File newFile = dialog.getRenamedFile();
                if(newFile != null) {
                    FileChangeHelper fileHelper = new FileChangeHelper(fLDModel);
                    fileHelper.renameFile(file, newFile);
                    fFileTreeViewer.refresh();
                    fFileTreeViewer.setSelection(new StructuredSelection(newFile));
                }
            }
        }
    }
    
    private void handleImport() throws IOException {
        File parent = (File)((IStructuredSelection)fFileTreeViewer.getSelection()).getFirstElement();

        if(parent == null) {
            parent = fRootFolder;
        }
        else if(!parent.isDirectory()) {
            parent = parent.getParentFile();
        }
        
        FileDialog dialog = new FileDialog(getShell(), SWT.MULTI | SWT.OPEN);
        dialog.setText(Messages.FilesComposite_11);
        String str = dialog.open();
        if(str != null) {
            String folder = dialog.getFilterPath();
            String[] filenames = dialog.getFileNames();

            // Make full paths
            File[] files = new File[filenames.length];
            for(int i = 0; i < filenames.length; i++) {
                files[i] = new File(folder, filenames[i]);
            }

            // Copy them
            FileUtils.copyFiles(files, parent, null);

            fFileTreeViewer.refresh();
        }
    }
}
