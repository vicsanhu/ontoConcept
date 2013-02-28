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
package org.tencompetence.ldauthor.ui.editors.method;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.services.IDisposable;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.dialogs.RenameObjectDialog;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.method.operations.DeleteActOperation;
import org.tencompetence.ldauthor.ui.editors.method.operations.NewActOperation;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;
import org.tencompetence.ldauthor.ui.wizards.method.NewActWizard;

/**
 * Play Widget
 * 
 * @author Phillip Beauvoir
 * @version $Id: PlayWidget.java,v 1.40 2009/06/10 22:35:07 phillipus Exp $
 */
public class PlayWidget extends Composite
implements IDisposable, PropertyChangeListener {
    
    private IPlayModel fPlay;
    
    private PlayWidgetsHandler fPlayWidgetsHandler;
    
    private Composite fTableComposite;
    
    private CLabel fLabel;
    private ActsTableViewer fTableViewer;
    
    private ILDEditorPart fEditor;
    
    private IAction fActionNewPlay;
    private IAction fActionNewAct;
    private IAction fActionDeleteAct;
    private IAction fActionDeletePlay;
    private IAction fActionMovePlayUp;
    private IAction fActionMovePlayDown;
    private IAction fActionShowActProperties;
    private IAction fActionShowPlayProperties;
    private IAction fActionRenamePlay;
    private IAction fActionRenameAct;
    
    /*
     * Track and de-select other Tables in other Play Widgets so that only one
     * has focus at a time
     */
    static TableViewer fLastTableFocus;
    
    public PlayWidget(PlayWidgetsHandler handler, IPlayModel play) {
        this(handler, play, -1);
    }
    
    /**
     * @param handler
     * @param play
     * @param index index position.  If -1, add to end of list
     */
    public PlayWidget(PlayWidgetsHandler handler, IPlayModel play, int index) {
        super(handler.getComposite(), SWT.NULL);
        
        // Insert at index position
        if(index != -1 && index < getParent().getChildren().length - 1) {
            //System.out.println("Requested index " + index + "  :  Children size is " + getParent().getChildren().length);
            moveAbove(getParent().getChildren()[index]);
        }
        
        fPlayWidgetsHandler = handler;
        fEditor = handler.getEditor();
        
        createActions();
        setup();
        hookContextMenu();
        registerGlobalActions();
        registerListeners();
        
        setPlay(play);
    }
    
    private void setup() {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        fLabel = createTitleLabel(this);
        fLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        fLabel.setImage(ImageFactory.getImage(ImageFactory.IMAGE_PLAY_16));
        
        fTableComposite = new Composite(this, SWT.NULL);
        TableColumnLayout tableLayout = new TableColumnLayout();
        fTableComposite.setLayout(tableLayout);
        fTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        fTableViewer = new ActsTableViewer(fEditor, fTableComposite, SWT.NO_SCROLL);  // No Scroll gutters on Mac
        
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Pass on Act table selections to the Inspector
                Object o = ((StructuredSelection)event.getSelection()).getFirstElement();
                InspectorManager.getInstance().setInput(fEditor, o);
                
                // And tell the local selection provider
                ActSelectionProvider selectionHandler = (ActSelectionProvider)fEditor.getAdapter(ActSelectionProvider.class);
                selectionHandler.setSelection(event.getSelection());
                
                // Update actions
                updateActions(event.getSelection());
                
                // Deselect previous table
                if(fLastTableFocus != null && !fLastTableFocus.getTable().isDisposed()
                        && fLastTableFocus != fTableViewer) {
                    fLastTableFocus.getTable().deselectAll();
                }
                
                fLastTableFocus = fTableViewer;
            }
        });
        
        // Double-click opens the Properties View
        fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                InspectorManager.getInstance().showInspector();
            }
        });
    }
    
    private CLabel createTitleLabel(Composite parent) {
        final CLabel label = new CLabel(parent, SWT.NULL);
        //label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
        label.setBackground(new Color[] {
                Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND),
                Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW) },
                new int[] { 100 }, false);
        
        
        final Color oldColor = label.getForeground();
        
        label.addMouseTrackListener(new MouseTrackListener() {
            public void mouseEnter(MouseEvent e) {
                label.setForeground(JFaceColors.getErrorText(Display.getDefault()));
            }

            public void mouseExit(MouseEvent e) {
                label.setForeground(oldColor);
            }

            public void mouseHover(MouseEvent e) {
            }
        });

        
        return label;
    }
    
    /**
     * Actions
     */
    private void createActions() {
        fActionNewPlay = new Action(Messages.PlayWidget_0 + " " + LDModelUtils.getUserObjectName(LDModelFactory.PLAY)) { //$NON-NLS-1$
            @Override
            public void run() {
                fPlayWidgetsHandler.addNewPlay();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_PLAY_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        fActionNewAct = new Action(Messages.PlayWidget_0 + " " + LDModelUtils.getUserObjectName(LDModelFactory.ACT)) { //$NON-NLS-1$
            @Override
            public void run() {
                NewActWizard wizard = new NewActWizard();
                WizardDialog dialog = new WizardDialog(fEditor.getSite().getShell(), wizard);
                if(dialog.open() == IDialogConstants.OK_ID) {
                    int index;
                    
                    // Find selected Act for new position
                    IActModel selectedAct = (IActModel)((StructuredSelection)fTableViewer.getSelection()).getFirstElement();
                    if(selectedAct != null) {
                        index = fPlay.getActsModel().getChildren().indexOf(selectedAct) + 1;
                    }
                    else {
                        index = fPlay.getActsModel().getChildren().size();
                    }

                    try {
                        // Undo Operation
                        NewActOperation operation = new NewActOperation(fEditor, fPlay, wizard.getActTitle(), index);
                        getOperationHistory().execute(operation, null, null);
                        // Select Act
                        fTableViewer.setSelection(new StructuredSelection(operation.getAct()));
                    }
                    catch(ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return new DecorationOverlayIcon(ImageFactory.getImage(ImageFactory.IMAGE_ACT_16),
                        ImageFactory.getImageDescriptor(ImageFactory.ICON_NEW_OVERLAY), IDecoration.TOP_LEFT);
            }
        };
        
        fActionDeleteAct = new LDAuthorActionFactory.DeleteAction() {
            @Override
            public void run() {
                IActModel selectedAct = (IActModel)((StructuredSelection)fTableViewer.getSelection()).getFirstElement();
                if(selectedAct != null) {
                    try {
                        getOperationHistory().execute(
                                new DeleteActOperation(fEditor, selectedAct),
                                null,
                                null);
                    }
                    catch(ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        fActionDeleteAct.setText(Messages.PlayWidget_1 + " " + LDModelUtils.getUserObjectName(LDModelFactory.ACT)); //$NON-NLS-1$
        fActionDeleteAct.setEnabled(false);
        
        fActionDeletePlay = new Action(Messages.PlayWidget_1 + " " + LDModelUtils.getUserObjectName(LDModelFactory.PLAY)) { //$NON-NLS-1$
            @Override
            public void run() {
                fPlayWidgetsHandler.deletePlay(fPlay);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE);
            }
        };
        
        fActionShowActProperties = new Action(Messages.PlayWidget_2) { 
            @Override
            public void run() {
                InspectorManager.getInstance().showInspector();
            }
        };
        
        fActionShowPlayProperties = new Action(Messages.PlayWidget_2) { 
            @Override
            public void run() {
                // Force selection to the Play
                fTableViewer.getTable().deselectAll();
                fEditor.getEditorSite().getSelectionProvider().setSelection(new StructuredSelection(fPlay));
                updateActions(StructuredSelection.EMPTY);
                InspectorManager.getInstance().showInspector();
            }
        };
        
        fActionMovePlayUp = new Action(Messages.PlayWidget_3 + " " + LDModelUtils.getUserObjectName(LDModelFactory.PLAY) + " " + Messages.PlayWidget_4) { //$NON-NLS-1$ //$NON-NLS-2$
            @Override
            public void run() {
                fPlayWidgetsHandler.movePlay(fPlay, -1);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_UP);
            }
        };
        
        fActionMovePlayDown = new Action(Messages.PlayWidget_3 + " " + LDModelUtils.getUserObjectName(LDModelFactory.PLAY) + " " + Messages.PlayWidget_6) { //$NON-NLS-1$ //$NON-NLS-2$
            @Override
            public void run() {
                fPlayWidgetsHandler.movePlay(fPlay, 1);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_DOWN);
            }
        };
        
        // Rename Play
        fActionRenamePlay = new LDAuthorActionFactory.RenameAction() {
            @Override
            public void run() {
                renamePlay();
            }
        };
        
        // Rename Act
        fActionRenameAct = new LDAuthorActionFactory.RenameAction() {
            @Override
            public void run() {
                renameAct();
            }
        };
        fActionRenameAct.setEnabled(false);
    }
    
    /**
     * Update the Local Actions depending on the selection 
     * @param selection
     */
    private void updateActions(ISelection selection) {
        IActModel selectedAct = (IActModel)((StructuredSelection)selection).getFirstElement();
        
        fActionDeleteAct.setEnabled(selectedAct != null);
        fActionShowActProperties.setEnabled(selectedAct != null);
        fActionRenameAct.setEnabled(selectedAct != null);
    }
    
    /**
     * @return The TableViewer
     */
    public TableViewer getTableViewer() {
        return fTableViewer;
    }
    
    /**
     * @return The Play for this widget
     */
    public IPlayModel getPlay() {
        return fPlay;
    }
    
    /**
     * Set the Play in this widget
     * @param play
     */
    public void setPlay(IPlayModel play) {
        fPlay = play;
        fTableViewer.setInput(fPlay);
        fLabel.setText(fPlay.getTitle());
        computeTableHeight();
        fPlay.getLDModel().addPropertyChangeListener(this);
    }
    
    /**
     * Hook into a right-click menu on the Table
     */
    private void hookContextMenu() {
        // Play
        MenuManager menuMgr1 = new MenuManager("#ActsPopupMenu"); //$NON-NLS-1$
        menuMgr1.setRemoveAllWhenShown(true);
        
        menuMgr1.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillPlayContextMenu(manager);
            }
        });
        
        Menu menu1 = menuMgr1.createContextMenu(fLabel);
        fLabel.setMenu(menu1);
        
        // Acts Table
        MenuManager menuMgr2 = new MenuManager("#ActsPopupMenu"); //$NON-NLS-1$
        menuMgr2.setRemoveAllWhenShown(true);

        menuMgr2.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillActsTableContextMenu(manager);
            }
        });

        Menu menu2 = menuMgr2.createContextMenu(fTableViewer.getControl());
        fTableViewer.getControl().setMenu(menu2);
        
        fEditor.getSite().registerContextMenu(menuMgr2, fTableViewer);
    }
    
    /**
     * Fill the right-click menu for the Play
     * 
     * @param manager
     */
    private void fillPlayContextMenu(IMenuManager manager) {
        int indexOfPlay = fPlay.getLDModel().getMethodModel().getPlaysModel().getChildren().indexOf(fPlay);
        fActionMovePlayUp.setEnabled(indexOfPlay > 0);
        fActionMovePlayDown.setEnabled(indexOfPlay < fPlay.getLDModel().getMethodModel().getPlaysModel().getChildren().size() - 1);

        manager.add(fActionNewPlay);
        manager.add(new Separator());

        manager.add(fActionMovePlayUp);
        manager.add(fActionMovePlayDown);
        
        manager.add(new Separator());
        manager.add(fActionDeletePlay);
        
        manager.add(new Separator());
        manager.add(fActionRenamePlay);
        
        manager.add(new Separator());
        manager.add(fActionShowPlayProperties);
    }
    
    /**
     * Fill the right-click menu for the Table
     * 
     * @param manager
     */
    private void fillActsTableContextMenu(IMenuManager manager) {
        boolean isEmpty = fTableViewer.getSelection().isEmpty();
        
        manager.add(fActionNewAct);
        
        manager.add(new Separator());

        if(!isEmpty) {
            manager.add(fActionDeleteAct);
            manager.add(new Separator());
            manager.add(fActionRenameAct);
            manager.add(new Separator());
        }

        manager.add(fActionShowActProperties);

        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        // Play Title updated from elsewhere...
        if(evt.getSource() == fPlay && evt.getPropertyName() == ILDModelObject.PROPERTY_NAME) {
            fLabel.setText(fPlay.getTitle());
        }
        
        // Act updated from elsewhere...
        else if(evt.getSource() instanceof IActModel) {
            fTableViewer.update(evt.getSource(), null);
        }
        
        // Acts children changed by moving/adding/deleting them from elsewhere...
        else if(evt.getSource() == fPlay.getActsModel() && evt.getPropertyName().startsWith(ILDModelObjectContainer.PROPERTY_CHILD)) {
            // Need to do this...
            layoutTable();
        }
    }
    
    /**
     * Re-layout the table
     */
    private void layoutTable() {
        fTableViewer.refresh();
        
        // This extra command is a hack to get it to layout properly on Ubuntu
        // To Test this, start with an empty Play. Then add 3 Plays. Then delete them. Then Undo/Redo.
        if(Platform.getOS().equals(Platform.OS_LINUX)) {
        	fPlayWidgetsHandler.layout();
        }
        computeTableHeight();
        fPlayWidgetsHandler.layout();
    }
    
    /**
     * Compute the table height so that empty tables are not too big and that
     * there are a few pixels at the bottom for dragging onto.
     */
    private void computeTableHeight() {
        GridData gd = (GridData)fTableComposite.getLayoutData();
        
        if(fPlay.getActsModel().getChildren().isEmpty()) {
            gd.heightHint = 40;
            gd.minimumHeight = 40;
        }
        else {
            Point pt = fTableComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            gd.minimumHeight = pt.y + 8;
        }
    }
    
    /**
     * Register Global Actions on focus events
     */
    private void registerGlobalActions() {
        final IActionBars bars = fEditor.getEditorSite().getActionBars();
        
        fTableViewer.getControl().addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                register(true);
            }
            
            public void focusLost(FocusEvent e) {
                register(false);
            }
            
            // Only de-register specific actions (not undo/redo!)
            private void register(boolean hasFocus) {
                bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), hasFocus ? fActionDeleteAct : null);
                bars.setGlobalActionHandler(ActionFactory.PROPERTIES.getId(), hasFocus ? fActionShowActProperties : null);
                bars.setGlobalActionHandler(ActionFactory.RENAME.getId(), hasFocus ? fActionRenameAct : null);
                bars.updateActionBars();
            }
        });
    }
    
    /**
     * Rename Play
     */
    private void renamePlay() {
        RenameObjectDialog dialog = new RenameObjectDialog(Display.getDefault().getActiveShell(), fPlay.getTitle());
        if(dialog.open()) {
            String name = dialog.getNewName();
            if(name != null) {
                fPlay.setTitle(name);
                fPlay.getLDModel().setDirty();
            }
        }
    }
    
    /**
     * Rename Act
     */
    private void renameAct() {
        IActModel selectedAct = (IActModel)((StructuredSelection)fTableViewer.getSelection()).getFirstElement();
        if(selectedAct != null) {
            RenameObjectDialog dialog = new RenameObjectDialog(Display.getDefault().getActiveShell(), selectedAct.getTitle());
            if(dialog.open()) {
                String name = dialog.getNewName();
                if(name != null) {
                    selectedAct.setTitle(name);
                    selectedAct.getLDModel().setDirty();
                }
            }
        }
    }
    
    /**
     * Register Listeners
     */
    private void registerListeners() {
        // Show Inspector for the Play
        fLabel.addListener(SWT.MouseDoubleClick, new Listener() {
            public void handleEvent(Event event) {
                InspectorManager.getInstance().showInspector();
            }
        });
        
        // Pass on Play selections to the Inspector
        fLabel.addListener(SWT.MouseUp, new Listener() {
            public void handleEvent(Event event) {
                InspectorManager.getInstance().setInput(fEditor, getPlay());
            }
        });
    }
    
    /*
     * Get the undo/redo operation history from the workbench.
     */
    private IOperationHistory getOperationHistory() {
        return PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
    }

    @Override
    public void dispose() {
        super.dispose();
        
        if(fPlay != null) {
            fPlay.getLDModel().removePropertyChangeListener(this);
        }
    }
}