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
package org.tencompetence.ldauthor.ui.views.publisher;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.preferences.CopperCorePrefsManager;
import org.tencompetence.ldauthor.preferences.ILDAuthorPreferenceConstants;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.views.IPublisherView;
import org.tencompetence.ldauthor.ui.views.ViewManager;
import org.tencompetence.ldauthor.ui.wizards.zip.UploadZipCopperCoreWizard;
import org.tencompetence.ldauthor.utils.StringUtils;
import org.tencompetence.ldpublisher.IPublishHandler;
import org.tencompetence.ldpublisher.PublishHandler;
import org.tencompetence.ldpublisher.model.IRole;
import org.tencompetence.ldpublisher.model.IRun;
import org.tencompetence.ldpublisher.model.IUnitOfLearning;
import org.tencompetence.ldpublisher.model.IUser;


/**
 * Publisher View
 * 
 * @author Phillip Beauvoir
 * @version $Id: PublisherView.java,v 1.31 2009/02/26 13:38:15 phillipus Exp $
 */
public class PublisherView
extends ViewPart
implements IContextProvider, IPublisherView
{

    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".publisherView"; //$NON-NLS-1$
    public static String HELP_ID = LDAuthorPlugin.PLUGIN_ID + ".publisherViewHelp"; //$NON-NLS-1$
    
    /*
     * Actions
     */
    private IAction fActionUploadZip;
    private IAction fActionConnect;
    private IAction fActionAddRun;
    //private IAction fActionAddUser; // Can't add users from client without authentication
    private IAction fActionDelete;
    private IAction fActionSelectAll;
    private IAction fActionLaunchBrowser;

    private CCUoLTableViewer fUoLTableViewer;
    private CCRunsTableViewer fRunsTableViewer;
    private CCRolesTableViewer fRolesTableViewer;
    private CCUsersTableViewer fUsersTableViewer;
    
    private Label fConnectionLabel;
    
    private IPublishHandler fPublishHandler;
    
    private boolean fConnected;
    
    @Override
    public void createPartControl(Composite parent) {
        AppFormToolkit.getInstance().adapt(parent);
        parent.setLayout(new GridLayout());
        
        createSection(parent);
        
        fConnectionLabel = AppFormToolkit.getInstance().createLabel(parent, Messages.PublisherView_0);
        GridData gd = new GridData(SWT.FILL, SWT.NULL, true, false);
        fConnectionLabel.setLayoutData(gd);

        makeActions();
        registerGlobalActions();
        makeLocalToolBarActions();
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    /**
     * Create the UoLs Section
     * 
     * @param parent
     * @return
     */
    private Section createSection(Composite parent) {
        Section section = AppFormToolkit.getInstance().createSection(parent, Section.TITLE_BAR);
        section.setText(Messages.PublisherView_1);
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        Composite client = AppFormToolkit.getInstance().createComposite(section);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);
        section.setClient(client);
        
        SashForm sash = new SashForm(client, SWT.HORIZONTAL);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        Composite clientUOLs = AppFormToolkit.getInstance().createComposite(sash);
        clientUOLs.setLayout(new TableColumnLayout());
        createCCUoLTableViewer(clientUOLs);

        Composite clientRuns = AppFormToolkit.getInstance().createComposite(sash);
        clientRuns.setLayout(new TableColumnLayout());
        createCCRunsTableViewer(clientRuns);
        
        Composite clientRoles = AppFormToolkit.getInstance().createComposite(sash);
        clientRoles.setLayout(new TableColumnLayout());
        createCCRolesTableViewer(clientRoles);
        
        Composite clientUsers = AppFormToolkit.getInstance().createComposite(sash);
        clientUsers.setLayout(new TableColumnLayout());
        createCCUsersTableViewer(clientUsers);
        
        return section;
    }
    
    /**
     * Create the UoL Table Viewer
     * @param parent
     */
    private void createCCUoLTableViewer(Composite parent) {
        fUoLTableViewer = new CCUoLTableViewer(parent, SWT.BORDER);
        
        hookContextMenu(fUoLTableViewer);
        
        fUoLTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IUnitOfLearning uol = (IUnitOfLearning)((IStructuredSelection)event.getSelection()).getFirstElement();
                fRunsTableViewer.setInput(uol);
                fActionAddRun.setEnabled(uol != null);
                fActionDelete.setEnabled(uol != null);
            }
        });
    }
    
    /**
     * Create the Runs Table Viewer
     * @param parent
     */
    private void createCCRunsTableViewer(Composite parent) {
        fRunsTableViewer = new CCRunsTableViewer(parent, SWT.BORDER);
        
        hookContextMenu(fRunsTableViewer);
        
        fRunsTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IRun run = (IRun)((IStructuredSelection)event.getSelection()).getFirstElement();
                fRolesTableViewer.setInput(run);
                fActionDelete.setEnabled(run != null);
            }
        });

        fRunsTableViewer.getControl().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                IRun run = (IRun)((IStructuredSelection)fRunsTableViewer.getSelection()).getFirstElement();
                fActionDelete.setEnabled(run != null);
            }
        });
    }
    
    /**
     * Create the Roles Table Viewer
     * @param parent
     */
    private void createCCRolesTableViewer(Composite parent) {
        fRolesTableViewer = new CCRolesTableViewer(parent, SWT.BORDER);
        
        fRolesTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IRole role = (IRole)((IStructuredSelection)event.getSelection()).getFirstElement();
                fUsersTableViewer.setInput(role);
                //fActionAddUser.setEnabled(role != null);
            }
        });
        
        fRolesTableViewer.getControl().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                fActionDelete.setEnabled(false);
            }
        });
    }
    
    /**
     * Create the Users Table Viewer
     * @param parent
     */
    private void createCCUsersTableViewer(Composite parent) {
        fUsersTableViewer = new CCUsersTableViewer(parent, SWT.BORDER);
        
        hookContextMenu(fUsersTableViewer);
        
        fUsersTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                //IUser user = (IUser)((IStructuredSelection)event.getSelection()).getFirstElement();
                //fActionDelete.setEnabled(user != null);
                fActionDelete.setEnabled(false);
            }
        });

        fUsersTableViewer.getControl().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                //IUser user = (IUser)((IStructuredSelection)fUsersTableViewer.getSelection()).getFirstElement();
                //fActionDelete.setEnabled(user != null);
                fActionDelete.setEnabled(false);
            }
        });
    }
    
    /**
     * Make local actions
     */
    private void makeActions() {
        fActionUploadZip = new Action(Messages.PublisherView_2) {
            @Override
            public void run() {
                uploadZip();
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_UPLOAD_ZIP);
            }
        };
        
        fActionConnect = new Action(Messages.PublisherView_3) {
            @Override
            public void run() {
                connect();
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_GO);
            }
        };
        
        fActionAddRun = new Action(Messages.PublisherView_4) {
            @Override
            public void run() {
                IUnitOfLearning uol = (IUnitOfLearning)((IStructuredSelection)fUoLTableViewer.getSelection()).getFirstElement();
                if(uol != null) {
                    addRun(uol);
                }
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_NEWRUN);
            }
        };
        
//        fActionAddUser = new Action(Messages.PublisherView_5) {
//            @Override
//            public void run() {
//                IRole role = (IRole)((IStructuredSelection)fRolesTableViewer.getSelection()).getFirstElement();
//                if(role != null) {
//                    addUser(role);
//                }
//            }
//            
//            @Override
//            public String getToolTipText() {
//                return getText();
//            }
//            
//            @Override
//            public ImageDescriptor getImageDescriptor() {
//                return ImageFactory.getImageDescriptor(ImageFactory.ICON_NEWUSER);
//            }
//        };

        // Select All
        fActionSelectAll = new LDAuthorActionFactory.SelectAllAction() {
            @Override
            public void run() {
                handleSelectAllAction();
            }
        };
        
        // Delete
        fActionDelete = new LDAuthorActionFactory.DeleteAction() {
            @Override
            public void run() {
                handleDeleteAction();
            }
        };
        
        // Launch in Browser
        fActionLaunchBrowser = new Action(Messages.PublisherView_28) {
            @Override
            public void run() {
                String server = LDAuthorPlugin.getDefault().getPreferenceStore().getString(ILDAuthorPreferenceConstants.PREFS_CC_SERVER);
                String url = server + "/sled3/"; //$NON-NLS-1$

                // Get Run ID from currently selected Run in Run Table
                IRun run = (IRun)((IStructuredSelection)fRunsTableViewer.getSelection()).getFirstElement();
                if(run != null) {
                    // TODO - show the actual Run with username and pw from prefs like: "http://localhost:8080/sled3/AutoLogin.do?runid=0&username=adam&password=letmein"
                    // int runID = run.getCopperCoreKey();
                    // String userName = "paul";
                    // String pw = "paul";
                    // url += "AutoLogin.do?runid=" + runID + "&username=" + userName + "&password=" + pw;
                }
                
                ViewManager.showInExternalBrowser(url);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_BROWSER);
            }
        };

        fActionDelete.setEnabled(false);
        fActionAddRun.setEnabled(false);
        //fActionAddUser.setEnabled(false);
    }
    
    /**
     * Delete Action
     */
    private void handleDeleteAction() {
        if(fPublishHandler == null) {
            return;
        }
        
        // Delete UoLs
        if(fUoLTableViewer.getControl().isFocusControl()) {
            IStructuredSelection selection = (IStructuredSelection)fUoLTableViewer.getSelection();
            if(selection != null && askConfirmationOfDelete()) {
                
                for(Object o : selection.toList()) {
                    IUnitOfLearning uol = (IUnitOfLearning)o;
                    try {
                        fPublishHandler.removeUnitOfLearning(uol);
                    }
                    catch(Exception ex) {
                        MessageDialog.openError(getSite().getShell(), Messages.PublisherView_6, Messages.PublisherView_7 + ex.getMessage());
                    }
                }

                // Refresh
                fUoLTableViewer.refresh();
            }
        }
        
        // Delete Runs
        else if(fRunsTableViewer.getControl().isFocusControl()) {
            IStructuredSelection selection = (IStructuredSelection)fRunsTableViewer.getSelection();
            IUnitOfLearning selectedUoL = (IUnitOfLearning)((IStructuredSelection)fUoLTableViewer.getSelection()).getFirstElement();
            if(selection != null && selectedUoL != null && askConfirmationOfDelete()) {
                
                for(Object o : selection.toList()) {
                    IRun run = (IRun)o;
                    try {
                        selectedUoL.removeRun(run);
                    }
                    catch(Exception ex) {
                        MessageDialog.openError(getSite().getShell(), Messages.PublisherView_8, Messages.PublisherView_9 + ex.getMessage());
                    }
                }

                // Refresh
                fRunsTableViewer.refresh();
            }
        }
        
        // Delete Users
        else if(fUsersTableViewer.getControl().isFocusControl()) {
            IStructuredSelection selection = (IStructuredSelection)fUsersTableViewer.getSelection();
            IRole selectedRole = (IRole)((IStructuredSelection)fRolesTableViewer.getSelection()).getFirstElement();
            if(selection != null && selectedRole != null && askConfirmationOfDelete()) {
                
                for(Object o : selection.toList()) {
                    IUser user = (IUser)o;
                    try {
                        selectedRole.removeUserFromRole(user);
                    }
                    catch(Exception ex) {
                        MessageDialog.openError(getSite().getShell(), Messages.PublisherView_10, Messages.PublisherView_11 + ex.getMessage());
                    }
                }
                
                // Refresh
                fUsersTableViewer.refresh();
            }
        }
    }
    
    /**
     * Ask for confirmation of Delete action
     * @return
     */
    private boolean askConfirmationOfDelete() {
        return MessageDialog.openQuestion(
                Display.getDefault().getActiveShell(),
                Messages.PublisherView_12,
                Messages.PublisherView_13);
    }
    
    /**
     * Select All Action
     */
    private void handleSelectAllAction() {
        if(fUoLTableViewer.getControl().isFocusControl()) {
            fUoLTableViewer.getTable().selectAll();
        }
        else if(fRunsTableViewer.getControl().isFocusControl()) {
            fRunsTableViewer.getTable().selectAll();
        }
        else if(fUsersTableViewer.getControl().isFocusControl()) {
            fUsersTableViewer.getTable().selectAll();
        }
    }

    /**
     * Add a Run to a UoL
     * @param uol
     */
    private void addRun(IUnitOfLearning uol) {
        if(fPublishHandler == null) {
            return;
        }
        
        InputDialog dialog = new InputDialog(getSite().getShell(),
                Messages.PublisherView_14,
                Messages.PublisherView_15,
                null,
                new IInputValidator() {
            public String isValid(String newText) {
                return StringUtils.isSetAfterTrim(newText) ? null : ""; //$NON-NLS-1$
            }
        });

        if(dialog.open() == Window.OK) {
            try {
                String title = dialog.getValue();
                uol.createRun(title);
                fRunsTableViewer.refresh();
            }
            catch(Exception ex) {
                MessageDialog.openError(getSite().getShell(), Messages.PublisherView_16, Messages.PublisherView_17);
            }
        }
    }
    
    /**
     * Add a user to a Role
     * @param role
     */
    @SuppressWarnings("unused")
    private void addUser(IRole role) {
        if(fPublishHandler == null) {
            return;
        }

        InputDialog dialog = new InputDialog(getSite().getShell(),
                Messages.PublisherView_18,
                Messages.PublisherView_19,
                null,
                new IInputValidator() {
            public String isValid(String newText) {
                return StringUtils.isSetAfterTrim(newText) ? null : ""; //$NON-NLS-1$
            }
        });

        if(dialog.open() == Window.OK) {
            try {
                String title = dialog.getValue();
                IUser user = role.getUser(title);
                if(user != null) {
                    role.addUserToRole(user);
                }                
                fUsersTableViewer.refresh();
            }
            catch(Exception ex) {
                MessageDialog.openError(getSite().getShell(), Messages.PublisherView_20, Messages.PublisherView_21);
            }
        }
    }

    /**
     * Connect to a CC server
     * @param server
     */
    private void connect() {
        final String server = LDAuthorPlugin.getDefault().getPreferenceStore().getString(ILDAuthorPreferenceConstants.PREFS_CC_SERVER);
        final String user = CopperCorePrefsManager.getDecryptedString(ILDAuthorPreferenceConstants.PREFS_CC_USERNAME);
        final String password = CopperCorePrefsManager.getDecryptedString(ILDAuthorPreferenceConstants.PREFS_CC_PASSWORD);
        
        fConnected = false;
        
        fConnectionLabel.setText(Messages.PublisherView_22);

        IRunnableWithProgress runnable = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                try {
                    monitor.beginTask(Messages.PublisherView_23, IProgressMonitor.UNKNOWN);
                    fPublishHandler = new PublishHandler(server, user, password);
                    fConnected = true;
                }
                catch(Exception ex) {
                    // Pass this on to the caller with this ruse...
                    throw new InterruptedException(ex.getMessage());
                }
            }
        };
        
        ProgressMonitorDialog progress = new ProgressMonitorDialog(getSite().getShell()) {
            @Override
            protected void cancelPressed() {
                fConnected = false;
                decrementNestingDepth(); // have to do this if something goes wrong in order to close()
                super.cancelPressed();
            }
        };

        try {
            progress.run(true, true, runnable);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        if(!fConnected) {
            MessageDialog.openError(getSite().getShell(), Messages.PublisherView_24, Messages.PublisherView_25 + server);
            return;
        }
        
        fConnectionLabel.setText(Messages.PublisherView_26 + server);
        
        // Retrieve UoLs
        fUoLTableViewer.setInput(fPublishHandler);
    }

    /**
     * Select and upload a zip file to the server
     */
    private void uploadZip() {
        UploadZipCopperCoreWizard wizard = new UploadZipCopperCoreWizard();
        
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
        if(dialog.open() == Window.OK && fPublishHandler != null) {
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    try {
                        fUoLTableViewer.refresh();
                    }
                    catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * Make Local Toolbar items
     */
    private void makeLocalToolBarActions() {
        IActionBars bars = getViewSite().getActionBars();
        IToolBarManager manager = bars.getToolBarManager();

        manager.add(new Separator(IWorkbenchActionConstants.NEW_GROUP));
        
        manager.add(fActionConnect);
        manager.add(new Separator());
        
        manager.add(fActionUploadZip);
        manager.add(fActionLaunchBrowser);
        manager.add(new Separator());
        
        manager.add(fActionAddRun);
        //manager.add(fActionAddUser);
        
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /**
     * Register Global Action Handlers
     */
    private void registerGlobalActions() {
        IActionBars actionBars = getViewSite().getActionBars();
        
        // Register our interest in the global menu actions
        actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), fActionDelete);
        actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), fActionSelectAll);
    }
    
    @Override
    public void setFocus() {
        if(fUoLTableViewer != null) {
            fUoLTableViewer.getControl().setFocus();
        }
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu(final StructuredViewer viewer) {
        MenuManager menuMgr = new MenuManager("#Popup"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(viewer, manager);
            }
        });
        
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        
        getSite().registerContextMenu(menuMgr, viewer);
    }
    
    private void fillContextMenu(StructuredViewer viewer, IMenuManager manager) {
        //boolean isEmpty = viewer.getSelection().isEmpty();
        
        if(viewer == fUoLTableViewer) {
            manager.add(fActionLaunchBrowser);
        }
        
        if(viewer == fRunsTableViewer) {
            manager.add(fActionAddRun);
        }
        
//        if(viewer == fUsersTableViewer) {
//            manager.add(fActionAddUser);
//        }

        manager.add(new Separator());
        manager.add(fActionDelete);
        
        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    // =================================================================================
    //                       Contextual Help support
    // =================================================================================
    
    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getContextChangeMask()
     */
    public int getContextChangeMask() {
        return NONE;
    }

    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getContext(java.lang.Object)
     */
    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getSearchExpression(java.lang.Object)
     */
    public String getSearchExpression(Object target) {
        return Messages.PublisherView_27;
    }
}
