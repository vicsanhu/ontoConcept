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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Section;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.method.operations.NewActOperation;
import org.tencompetence.ldauthor.ui.wizards.method.NewActWizard;


/**
 * Composite holding Plays and UOL Links
 * 
 * @author Phillip Beauvoir
 * @version $Id: PlaysComposite.java,v 1.24 2009/05/22 16:35:05 phillipus Exp $
 */
public class PlaysComposite extends Composite {
    
    private ILDEditorPart fEditor;
    
    private ILDModel fLDModel;
    
    private PlayWidgetsHandler fPlayWidgetsHandler;
    
    private Button fNewButton;
    
    private IAction fActionNewPlay;
    private IAction fActionNewAct;
    
    public PlaysComposite(ILDEditorPart editor, Composite parent, int style) {
        super(parent, style);
        
        fEditor = editor;
        fLDModel = (ILDModel)editor.getAdapter(ILDModel.class);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        
        Section section1 = AppFormToolkit.getInstance().createSection(this, Section.TITLE_BAR);
        section1.setText(LDModelUtils.getUserObjectName(LDModelFactory.PLAYS));
        section1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        // Toolbar
        Composite toolBar = createToolBar(section1);
        section1.setTextClient(toolBar);
        
        // Plays
        fPlayWidgetsHandler = new PlayWidgetsHandler(fEditor, section1);
        section1.setClient(fPlayWidgetsHandler.getComposite().getParent());
        
        fLDModel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().startsWith(ILDModelObjectContainer.PROPERTY_CHILD) && evt.getNewValue() instanceof IPlayModel) {
                    // Enable Act Action if there are Plays
                    fActionNewAct.setEnabled(!fLDModel.getMethodModel().getPlaysModel().getChildren().isEmpty());
                }
            }
        });
        
        hookContextMenu();
    }
    
    /**
     * @return PlayWidgetsHandler
     */
    public PlayWidgetsHandler getPlayWidgetsHandler() {
        return fPlayWidgetsHandler;
    }
    
    private Composite createToolBar(Composite parent) {
        createActions();
        
        Composite c = new Composite(parent, SWT.NULL);
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        c.setLayout(layout);

        fNewButton = new Button(c, SWT.PUSH);
        fNewButton.setText(Messages.PlaysComposite_1);
        
        fNewButton.setToolTipText(Messages.PlaysComposite_2 +
                " " + //$NON-NLS-1$
                LDModelUtils.getUserObjectName(LDModelFactory.PLAY) +
                " " + //$NON-NLS-1$
                Messages.PlaysComposite_3 +
                " " + //$NON-NLS-1$
                LDModelUtils.getUserObjectName(LDModelFactory.ACT));
        
        fNewButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD).createImage());
        
        fNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                MenuManager menuManager = new MenuManager();
                menuManager.add(fActionNewPlay);
                menuManager.add(fActionNewAct);
                menuManager.add(new Separator());
                Menu menu = menuManager.createContextMenu(fNewButton.getShell());
                menu.setVisible(true);
            }
        });
        
        // Enable Act Action if there are Plays
        fActionNewAct.setEnabled(!fLDModel.getMethodModel().getPlaysModel().getChildren().isEmpty());
        
        return c;
    }
    
    /**
     * Actions
     */
    private void createActions() {
        fActionNewPlay = new Action(LDModelUtils.getUserObjectName(LDModelFactory.PLAY)) {
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
        
        fActionNewAct = new Action(LDModelUtils.getUserObjectName(LDModelFactory.ACT)) {
            @Override
            public void run() {
                NewActWizard wizard = new NewActWizard(fLDModel.getMethodModel().getPlaysModel());
                WizardDialog dialog = new WizardDialog(fEditor.getSite().getShell(), wizard);
                if(dialog.open() == IDialogConstants.OK_ID) {
                    
                    // Parent Play
                    IPlayModel play = wizard.getParentPlay();
                    if(play == null) {
                        return;
                    }
                    
                    int index = play.getActsModel().getChildren().size();
                    
                    try {
                        // Undo Operation
                        NewActOperation operation = new NewActOperation(fEditor, play, wizard.getActTitle(), index);
                        getOperationHistory().execute(operation, null, null);
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
    }
    
    /*
     * Get the undo/redo operation history from the workbench.
     */
    private IOperationHistory getOperationHistory() {
        return PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
    }
    
    /**
     * Hook into a right-click menu on the Table
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);

        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });

        Menu menu = menuMgr.createContextMenu(fPlayWidgetsHandler.getComposite().getParent());
        fPlayWidgetsHandler.getComposite().getParent().setMenu(menu);
    }
    
    /**
     * Fill the right-click menu for the Table
     * 
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        IMenuManager newMenu = new MenuManager(Messages.PlaysComposite_4, "new"); //$NON-NLS-1$ 
        manager.add(newMenu);
        
        newMenu.add(fActionNewPlay);
        newMenu.add(fActionNewAct);
    }


}
