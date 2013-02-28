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
package org.tencompetence.ldauthor.opendock.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.xmlrpc.XmlRpcException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.tencompetence.ldauthor.opendock.IOpenDockConstants;
import org.tencompetence.ldauthor.opendock.OpenDockHandler;
import org.tencompetence.ldauthor.opendock.OpenDockPlugin;
import org.tencompetence.ldauthor.opendock.UOL_SearchQueryInfo;
import org.tencompetence.ldauthor.opendock.preferences.OpenDockPreferencePage;


/**
 * OpenDock Main/Search View
 * 
 * @author Phillip Beauvoir
 * @version $Id: OpenDockView.java,v 1.9 2008/06/23 21:36:49 phillipus Exp $
 */
public class OpenDockView extends ViewPart implements IOpenDockConstants {
    
    public static String ID = OpenDockPlugin.PLUGIN_ID + ".openDockView"; //$NON-NLS-1$
    
    private IAction fActionConfigure;
    private IAction fActionUpload;
    
    private Section fSection;
    private FormToolkit formToolKit;

    private Text fTextContent, fTextAuthor;

    private Combo fComboLicence;
    
    private Button fButtonAllTerms, fButtonAnyTerms, fButtonExact, fButtonLike, fButtonAllFields, fButtonAnyFields;
    
    private Button fSearchButton;
    
    private SearchResultsTreeViewer fTreeViewer;
    
    @Override
    public void createPartControl(Composite parent) {
        formToolKit = new FormToolkit(parent.getDisplay());
        
        formToolKit.adapt(parent);
        parent.setLayout(new GridLayout());
        
        fSection = formToolKit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        fSection.setText(Messages.OpenDockView_0);
        fSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        Composite client = createSearchParameterSection(fSection);
        fSection.setClient(client);
        
        // Tree Viewer
        // Use a parent composite for the tree with TreeColumnLayout.
        Composite treeClient = formToolKit.createComposite(parent);
        treeClient.setLayout(new TreeColumnLayout());
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        treeClient.setLayoutData(gd);
        fTreeViewer = new SearchResultsTreeViewer(treeClient, SWT.FULL_SELECTION | SWT.BORDER);
        
        // Actions
        makeActions();
        
        // Toolbar additions
        ToolBar toolBar = createToolBar(fSection);
        fSection.setTextClient(toolBar);
    }
    
    private Composite createSearchParameterSection(Section parent) {
        Composite client = formToolKit.createComposite(parent);
        GridLayout layout = new GridLayout(2, true);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);
        
        // Fields Group
        Group fields = new Group(client, SWT.NULL);
        fields.setText(Messages.OpenDockView_1);
        fields.setLayout(new GridLayout(2, false));
        fields.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Content
        formToolKit.createLabel(fields, Messages.OpenDockView_2, SWT.NULL);
        fTextContent = formToolKit.createText(fields, "", SWT.BORDER); //$NON-NLS-1$
        fTextContent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Author
        formToolKit.createLabel(fields, Messages.OpenDockView_3, SWT.NULL);
        fTextAuthor = formToolKit.createText(fields, "", SWT.BORDER); //$NON-NLS-1$
        fTextAuthor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Licence
        formToolKit.createLabel(fields, Messages.OpenDockView_4, SWT.NULL);
        fComboLicence = new Combo(fields, SWT.BORDER);
        fComboLicence.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fComboLicence.setItems(LICENCE_ITEMS);
        
        // Matching Group
        Group match = new Group(client, SWT.NULL);
        match.setText(Messages.OpenDockView_5);
        match.setLayout(new GridLayout(2, false));
        match.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        fButtonAllTerms = formToolKit.createButton(match, Messages.OpenDockView_6, SWT.RADIO);
        fButtonAnyTerms = formToolKit.createButton(match, Messages.OpenDockView_7, SWT.RADIO);
        fButtonAnyTerms.setSelection(true);
        
        // Exact / Like group
        Group like = new Group(client, SWT.NULL);
        like.setText(Messages.OpenDockView_8);
        like.setLayout(new GridLayout(2, false));
        like.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        fButtonExact = formToolKit.createButton(like, Messages.OpenDockView_9, SWT.RADIO);
        fButtonLike = formToolKit.createButton(like, Messages.OpenDockView_10, SWT.RADIO);
        fButtonLike.setSelection(true);
        
        // Find Results group
        Group find_results = new Group(client, SWT.NULL);
        find_results.setText(Messages.OpenDockView_11);
        find_results.setLayout(new GridLayout(2, false));
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        //gd.horizontalSpan = 2;
        find_results.setLayoutData(gd);

        fButtonAllFields = formToolKit.createButton(find_results, Messages.OpenDockView_12, SWT.RADIO);
        fButtonAnyFields = formToolKit.createButton(find_results, Messages.OpenDockView_13, SWT.RADIO);
        fButtonAnyFields.setSelection(true);
        
        // Search Button
        fSearchButton = formToolKit.createButton(client, Messages.OpenDockView_14, SWT.PUSH);
        fSearchButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                doSearch();
            }
        });
        
        return client;
    }

    /**
     * Do the Search
     */
    private void doSearch() {
        fTreeViewer.setInput(null);
        fTreeViewer.refresh();
        
        if(!OpenDockHandler.getInstance().isConnected()){
            boolean result = OpenDockHandler.getInstance().loadRepositoryParameters();
            if(!result){
                MessageDialog.openError(Display.getCurrent().getActiveShell(),
                        Messages.OpenDockView_15,
                        Messages.OpenDockView_16);
                return;
            }
        }
        
        try {
            UOL_SearchQueryInfo queryInfo = new UOL_SearchQueryInfo();
            queryInfo.content = fTextContent.getText();
            queryInfo.author = fTextAuthor.getText();
            queryInfo.licence = fComboLicence.getText();
            queryInfo.matchAllTerms = fButtonAllTerms.getSelection();
            queryInfo.matchExact = fButtonExact.getSelection();
            queryInfo.matchAllFields = fButtonAllFields.getSelection();
            
            ArrayList<HashMap<String, String>> resultList = OpenDockHandler.getInstance().doSearch(queryInfo);
            
            if(resultList != null) {
                fTreeViewer.setInput(resultList);
                fTreeViewer.refresh();
            }
        }
        catch(XmlRpcException ex) {
            ex.printStackTrace();
            MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.OpenDockView_17, ex.getMessage());
            OpenDockHandler.getInstance().setConnected(false);
        }
    }

    /**
     * Make Actions
     */
    private void makeActions() {
        fActionConfigure = new Action(Messages.OpenDockView_18) {
            @Override
            public void run() {
                // Open Prefs
                Dialog dlg = PreferencesUtil.createPreferenceDialogOn(getSite().getShell(), OpenDockPreferencePage.ID, null, null);
                if(dlg != null) {
                    dlg.open();
                }
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return AbstractUIPlugin.imageDescriptorFromPlugin(OpenDockPlugin.PLUGIN_ID, IImages.ICON_CONFIGURE);
            }
        };
    
        fActionUpload = new Action(Messages.OpenDockView_19) {
            @Override
            public void run() {
                if(!OpenDockHandler.getInstance().isConnected()){
                    boolean result = OpenDockHandler.getInstance().loadRepositoryParameters();
                    if(!result){
                        MessageDialog.openError(Display.getCurrent().getActiveShell(),
                                Messages.OpenDockView_20,
                                Messages.OpenDockView_21);
                        return;
                    }
                }
                
                OpenDockHandler.getInstance().uploadUOLWithWizard();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return AbstractUIPlugin.imageDescriptorFromPlugin(OpenDockPlugin.PLUGIN_ID, IImages.ICON_UPLOADZIP);
            }
        };
    }
    
    private ToolBar createToolBar(Composite parent) {
        ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
        toolBar.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
        //fFormToolkit.adapt(toolBar);
        
        ToolBarManager toolBarmanager = new ToolBarManager(toolBar);

        toolBarmanager.add(fActionUpload);
        toolBarmanager.add(fActionConfigure);
        
        toolBarmanager.update(true);
        
        return toolBar;
    }

    
    @Override
    public void setFocus() {
        if(fTextContent != null) {
            fTextContent.setFocus();
        }
    }

}
