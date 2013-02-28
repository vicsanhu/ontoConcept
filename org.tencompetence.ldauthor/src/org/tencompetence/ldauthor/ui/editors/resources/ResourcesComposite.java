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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.widgets.Section;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.resources.IResourcesModel;
import org.tencompetence.imsldmodel.util.ItemTypeUtils;
import org.tencompetence.ldauthor.ExtensionContentHandler;
import org.tencompetence.ldauthor.ILDContentHandler;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.qti.ui.NewQTITestResourceWizard;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.editors.EditorManager;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.LDEditorInput;
import org.tencompetence.ldauthor.ui.views.ViewManager;
import org.tencompetence.ldauthor.ui.views.preview.PreviewView;
import org.tencompetence.ldauthor.ui.wizards.resource.NewResourceWizard;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Resources Composite
 * 
 * @author Phillip Beauvoir
 * @version $Id: ResourcesComposite.java,v 1.46 2009/07/02 19:49:17 phillipus Exp $
 */
public class ResourcesComposite extends Composite implements PropertyChangeListener {

    private ResourcesTableViewer fResourcesTableViewer;
    
    private ResourceFilesComposite fResourceFilesComposite;
    
    private ILDEditorPart fEditor;
    
    private ILDModel fLDModel;
    
    private IAction fActionNewResource;
    private IAction fActionNewQTITest;
    
    private IAction fActionAssignFile;
    private IAction fActionDeleteResource;
    private IAction fActionEditResource;
    private IAction fActionViewResource;
    private IAction fActionSelectAll;
    private IAction fActionViewItemReferences;

    private IResourcesModel fResources;
    
    private File fRootFolder;

    
    public ResourcesComposite(ILDEditorPart editor, Composite parent, int style) {
        super(parent, style);
        
        fEditor = editor;
        
        fLDModel = ((LDEditorInput)editor.getEditorInput()).getModel();
        
        fResources = fLDModel.getResourcesModel();
        
        fRootFolder = fLDModel.getRootFolder();
        
        createActions();
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        
        Section section = AppFormToolkit.getInstance().createSection(this, Section.TITLE_BAR);
        section.setText(Messages.ResourcesComposite_0);
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Composite client = AppFormToolkit.getInstance().createComposite(section);
        layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);
        section.setClient(client);

        Composite toolBar = createToolBar(section);
        section.setTextClient(toolBar);

        SashForm sash = new SashForm(client, SWT.VERTICAL);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        AppFormToolkit.getInstance().adapt(sash);
        
        // Use a parent composite for the table with TableColumnLayout.
        // This allows for auto-resizing of table columns
        Composite c1 = AppFormToolkit.getInstance().createComposite(sash);
        c1.setLayout(new TableColumnLayout());
        
        fResourcesTableViewer = new ResourcesTableViewer(fEditor, c1, SWT.BORDER);
        fResourcesTableViewer.setResources(fResources);
        
        /*
         * Listen to selections
         */
        fResourcesTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                fEditor.getSite().getSelectionProvider().setSelection(event.getSelection());
                updateActions(event.getSelection());
            }
        });
        
        /*
         * Listen to double-clicks
         */
        fResourcesTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                Object object = ((IStructuredSelection)event.getSelection()).getFirstElement();
                if(object instanceof IResourceModel) {
                    IResourceModel resource = (IResourceModel)object;
                    File file = new File(fRootFolder, resource.getHref());
                    if(file.exists() && file.getName().endsWith(".xml")) { //$NON-NLS-1$
                        EditorManager.editFile(file, fLDModel);
                    }
                    else {
                        ViewManager.showViewPart(PreviewView.ID, false);
                    }
                }
            }
        });
        
        fResourceFilesComposite = new ResourceFilesComposite(editor, sash, SWT.NULL);
        
        sash.setWeights(new int[] { 75, 25 });
        
        hookContextMenu();
        registerGlobalActions();
        
        fLDModel.addPropertyChangeListener(this);
    }
    
    public TableViewer getTableViewer() {
        return fResourcesTableViewer;
    }

    /**
     * Actions
     */
    private void createActions() {
        fActionViewItemReferences = new Action(Messages.ResourcesComposite_1) {
            @Override
            public void run() {
                Object object = ((IStructuredSelection)fResourcesTableViewer.getSelection()).getFirstElement();
                if(object instanceof IResourceModel) {
                    ItemViewerDialog dialog = new ItemViewerDialog((IResourceModel)object, getShell());
                    dialog.open();
                }
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_NODE);
            }
        };
        
        fActionViewItemReferences.setEnabled(false);
        
        fActionDeleteResource = new LDAuthorActionFactory.DeleteAction() {
            @Override
            public void run() {
                deleteSelected();
            }
        };
        
        fActionDeleteResource.setEnabled(false);
        
        fActionAssignFile = new Action(Messages.ResourcesComposite_2) {
            @Override
            public void run() {
                setResourceHREF();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor("file"); //$NON-NLS-1$
            }
        };
        
        fActionAssignFile.setEnabled(false);
        
        fActionNewResource = new Action(Messages.ResourcesComposite_3) {
            @Override
            public void run() {
                newResource();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_NEWRESOURCE);
            }
        };
        
        fActionNewQTITest = new Action(Messages.ResourcesComposite_6) {
            @Override
            public void run() {
                newQTITestResource();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_TEST_OBJECT_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        // Edit Resource
        fActionEditResource = new Action(Messages.ResourcesComposite_4) {
            @Override
            public void run() {
                Object object = ((IStructuredSelection)fResourcesTableViewer.getSelection()).getFirstElement();
                if(object instanceof IResourceModel) {
                    editHref(((IResourceModel)object).getHref());
                }
            }

            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_EDIT);
            }
        };
        
        fActionEditResource.setEnabled(false);
        
        // View Resource
        fActionViewResource = new Action(Messages.ResourcesComposite_5) {
            @Override
            public void run() {
                Object object = ((IStructuredSelection)fResourcesTableViewer.getSelection()).getFirstElement();
                if(object instanceof IResourceModel) {
                    ViewManager.showViewPart(PreviewView.ID, false);
                }
            }

            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_VIEW);
            }
        };
        
        fActionViewResource.setEnabled(false);
        
        // Select All
        fActionSelectAll = new LDAuthorActionFactory.SelectAllAction() {
            @Override
            public void run() {
                fResourcesTableViewer.getTable().selectAll();
                updateActions(fResourcesTableViewer.getSelection());
            }
        };
    }

    /**
     * Add a new Resource
     */
    private void newResource() {
        NewResourceWizard wizard = new NewResourceWizard(fLDModel);
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
        if(dialog.open() == IDialogConstants.OK_ID) {
            IResourceModel resource = wizard.getResource();
            fResources.addResource(resource);
        }
    }
    
    /**
     * Add a new QTI Test Resource
     */
    private void newQTITestResource() {
        NewQTITestResourceWizard wizard = new NewQTITestResourceWizard(fLDModel);
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
        dialog.open();
    }

    /**
     * Set the File HREF for the selected Resource
     */
    private void setResourceHREF() {
        Object object = ((IStructuredSelection)fResourcesTableViewer.getSelection()).getFirstElement();
        
        if(!(object instanceof IResourceModel)) {
            return;
        }
        
        IResourceModel resource = (IResourceModel)object;
        
        File file = FileUtils.selectFileFromRootFolder(fRootFolder, fResourcesTableViewer.getControl().getShell());
        if(file != null) {
            String href = FileUtils.getRelativePath(file, fRootFolder);
            resource.setHrefAndResourceFile(href);
            
            // If this is a content type added by an extension, set its type
            ILDContentHandler handler = ExtensionContentHandler.getInstance().getHandler(file);
            if(handler != null) {
                resource.setType(handler.getType());
            }
        }
        // Update is via event message from IResource to this class...
    }
    
    /**
     * Delete the selected resources
     */
    private void deleteSelected() {
        List<?> selected = ((IStructuredSelection)fResourcesTableViewer.getSelection()).toList();
        
        List<IResourceModel> referenced = new ArrayList<IResourceModel>();
        
        // Check if any Resources are referenced by Items
        for(Object o : selected) {
            if(o instanceof IResourceModel) {
                IResourceModel resource = (IResourceModel)o;
                if(ItemTypeUtils.isReferencedByItem(resource)) {
                    referenced.add(resource);
                }
            }
        }
        
        if(!askUserDeleteResources(selected.size() > 1, referenced)) {
            return;
        }
        
        for(Object o : selected) {
            if(o instanceof IResourceModel) {
                IResourceModel resource = (IResourceModel)o;
                fResources.removeResource(resource);
            }
        }
        
        fResourcesTableViewer.refresh();
        fLDModel.setDirty();
    }

    /**
     * Ask the user whether they wish to delete the given objects
     * @param referenced Any Resources that are referenced by Items
     * @param objects The objects to delete
     * @return True if the user said yes, false if user says no or cancels
     */
    private boolean askUserDeleteResources(boolean plural, List<IResourceModel> referenced) {
        String message = plural ?
                Messages.ResourcesComposite_10 
                : 
                Messages.ResourcesComposite_11;
        
        if(!referenced.isEmpty()) {
            message += "\n\n" + //$NON-NLS-1$
            Messages.ResourcesComposite_12 +
            ":\n\n"; //$NON-NLS-1$
            for(IResourceModel resourceModel : referenced) {
                message += resourceModel.getIdentifier() + "\n"; //$NON-NLS-1$
            }
        }
        
        // Confirmation dialog
        return MessageDialog.openQuestion(
                Display.getDefault().getActiveShell(),
                Messages.ResourcesComposite_9,
                message);
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

        Menu menu = menuMgr.createContextMenu(fResourcesTableViewer.getControl());
        fResourcesTableViewer.getControl().setMenu(menu);

        fEditor.getSite().registerContextMenu(menuMgr, fResourcesTableViewer);
    }

    /**
     * Fill the right-click menu
     * 
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        boolean isEmpty = fResourcesTableViewer.getSelection().isEmpty();
        
        IMenuManager newMenu = new MenuManager(Messages.ResourcesComposite_7, "new"); //$NON-NLS-1$
        manager.add(newMenu);
        
        newMenu.add(fActionNewResource);
        newMenu.add(fActionNewQTITest);

        if(!isEmpty) {
            manager.add(fActionAssignFile);
            manager.add(new Separator());
            manager.add(fActionEditResource);
            manager.add(fActionViewResource);
            manager.add(fActionViewItemReferences);
            manager.add(new Separator());
            manager.add(fActionDeleteResource);
        }
        
        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    /**
     * Register Global Actions on focus events
     */
    private void registerGlobalActions() {
        final IActionBars bars = fEditor.getEditorSite().getActionBars();
        
        fResourcesTableViewer.getControl().addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), fActionDeleteResource);
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
        IResourceModel resource = (IResourceModel)((IStructuredSelection)selection).getFirstElement();
        
        fResourceFilesComposite.setResource(resource);

        fActionEditResource.setEnabled(resource != null && isEditableResource(resource));
        fActionViewResource.setEnabled(resource != null);
        fActionAssignFile.setEnabled(resource != null);
        fActionDeleteResource.setEnabled(resource != null);
        fActionViewItemReferences.setEnabled(resource != null);
    }


    private Composite createToolBar(Composite parent) {
        Composite c = new Composite(parent, SWT.NULL);
        
        FillLayout layout = new FillLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        c.setLayout(layout);
        
        final Button newButton = new Button(c, SWT.PUSH);
        newButton.setText(Messages.ResourcesComposite_13);
        newButton.setToolTipText(Messages.ResourcesComposite_3);
        newButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD).createImage());
        newButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                MenuManager menuManager = new MenuManager();
                menuManager.add(fActionNewResource);
                menuManager.add(fActionNewQTITest);
                Menu menu = menuManager.createContextMenu(newButton.getShell());
                menu.setVisible(true);
            }
        });
        
        
        ToolBar toolBar = new ToolBar(c, SWT.FLAT);
        ToolBarManager toolBarmanager = new ToolBarManager(toolBar);

        //toolBarmanager.add(fActionNewResource);
        //toolBarmanager.add(fActionAssignFile);
        toolBarmanager.add(new Separator());
        toolBarmanager.add(fActionEditResource);
        toolBarmanager.add(fActionViewResource);
        
        toolBarmanager.update(true);
        
        return c;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getSource() instanceof IResourcesModel || evt.getSource() instanceof IResourceModel) {
            fLDModel.setDirty();
            fResourcesTableViewer.refresh();
            fResourceFilesComposite.getViewer().refresh();
        }
    }

    /**
     * Edit a href
     * @param href
     */
    private void editHref(String href) {
        if(!StringUtils.isSet(href)) {
            return;
        }

        // Open in an editor if possible
        File file = new File(fRootFolder, href);
        EditorManager.editFile(file, fLDModel);
    }
    
    private boolean isEditableResource(IResourceModel resource) {
        if(resource == null) {
            return false;
        }
        
        String href = resource.getHref();
        
        if(!StringUtils.isSet(href)) {
            return false;
        }
        
        File file = new File(fRootFolder, href);
        
        return file.exists();
    }

}
