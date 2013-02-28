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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.xmlrpc.XmlRpcException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.tencompetence.ldauthor.opendock.IOpenDockConstants;
import org.tencompetence.ldauthor.opendock.OpenDockHandler;
import org.tencompetence.ldauthor.opendock.OpenDockPlugin;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Dialog to show details of UoL
 * 
 * @author Phillip Beauvoir
 * @author Roy Cherian
 * @version $Id: UoLDetailsDialog.java,v 1.5 2008/04/25 11:46:13 phillipus Exp $
 */
public class UoLDetailsDialog extends TitleAreaDialog implements IOpenDockConstants {
    
    // For persisting dialog position and size
    private static final String DIALOG_SETTINGS_SECTION = "ViewUoLDialogSettings"; //$NON-NLS-1$

    private IAction fActionNextItem;
    private IAction fActionPreviousItem;

    private Text fTextDescription, fTextLanguage, fTextAuthor, fTextLicence, fTextReleaseDate, fTextFileSize;
    private Text fTextContainerItemCount, fTextNumActivities, fTextLDStructure, fTextLDLevel, fTextLDAgeGroup;

    private SimpleDateFormat fDateFormatter = new SimpleDateFormat("dd.MM.yyyy"); //$NON-NLS-1$
    
    private ArrayList<HashMap<String, String>> fList;

    private HashMap<String, String> fSelectedItem;
    
    public UoLDetailsDialog(Shell parentShell, ArrayList<HashMap<String, String>> list, HashMap<String, String> selectedItem) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE);
        //setTitleImage(OpenDockPlugin.imageDescriptorFromPlugin(OpenDockPlugin.PLUGIN_ID, IImages.ICON_ODN).createImage());
        
        fList = list;
        fSelectedItem = selectedItem;
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(Messages.UoLDetailsDialog_0);

        Composite client = (Composite)super.createDialogArea(parent);
        
        Composite topComposite = new Composite(client, SWT.NONE);
        topComposite.setLayout(new GridLayout(2, false));
        topComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        // Actions
        createActions();
        
        // Toolbar
        ToolBar toolBar = new ToolBar(topComposite, SWT.FLAT);
        ToolBarManager toolBarmanager = new ToolBarManager(toolBar);
        toolBarmanager.add(fActionPreviousItem);
        toolBarmanager.add(fActionNextItem);
        toolBarmanager.update(true);
        
        Label label = new Label(topComposite, SWT.NULL);
        label.setText(Messages.UoLDetailsDialog_1);

        Composite bottomComposite = new Composite(client, SWT.NONE);
        bottomComposite.setLayout(new GridLayout(2, false));
        bottomComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // Long Description
        Label lbl = new Label(bottomComposite, SWT.NULL);
        lbl.setText(Messages.UoLDetailsDialog_2);
        fTextDescription = new Text(bottomComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER | SWT.READ_ONLY);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 100;
        fTextDescription.setLayoutData(gd);
        
        // Author
        fTextAuthor = createTextField(bottomComposite, Messages.UoLDetailsDialog_3);
        
        // Language
        fTextLanguage = createTextField(bottomComposite, Messages.UoLDetailsDialog_4);
        
        // Licence
        fTextLicence = createTextField(bottomComposite, Messages.UoLDetailsDialog_5);
        
        // Release Date
        fTextReleaseDate = createTextField(bottomComposite, Messages.UoLDetailsDialog_6);
        
        // File Size
        fTextFileSize = createTextField(bottomComposite, Messages.UoLDetailsDialog_7);
        
        // Container item count
        fTextContainerItemCount = createTextField(bottomComposite, Messages.UoLDetailsDialog_8);
        
        // Number of Activities
        fTextNumActivities = createTextField(bottomComposite, Messages.UoLDetailsDialog_9);
        
        // LD Structure
        fTextLDStructure = createTextField(bottomComposite, Messages.UoLDetailsDialog_10);
        
        // LD Level 
        fTextLDLevel = createTextField(bottomComposite, Messages.UoLDetailsDialog_11);
        
        // LD Age
        fTextLDAgeGroup = createTextField(bottomComposite, Messages.UoLDetailsDialog_12);
        
        updateFields();
        
        return topComposite;
    }
    
    private Text createTextField(Composite parent, String lblText) {
        Label lbl = new Label(parent, SWT.NULL);
        lbl.setText(lblText);
        Text text = new Text(parent, SWT.BORDER | SWT.READ_ONLY);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return text;
    }
    
    /**
     * Update fields with UoL info
     */
    private void updateFields() {
        if(fSelectedItem == null || fList == null) {
            return;
        }
        
        // Query the server for more item details
        try {
            OpenDockHandler.getInstance().addItemDetails(fSelectedItem);
        }
        catch(XmlRpcException ex) {
            ex.printStackTrace();
        }
        
        // Title
        String text = StringUtils.safeString(fSelectedItem.get(QUERY_META_TITLE));
        setTitle(text);
        
        // Short Description
        text = StringUtils.safeString(fSelectedItem.get(QUERY_META_SHORTDESC));
        setMessage(text);
        
        // Long Description
        text = StringUtils.safeString(fSelectedItem.get(QUERY_META_DESCRIPTION));
        fTextDescription.setText(text);
        
        // Author
        text = StringUtils.safeString(fSelectedItem.get(QUERY_ELEMENT_OWNER_NAME));
        fTextAuthor.setText(text);
        
        // Language
        text = StringUtils.safeString(fSelectedItem.get(QUERY_CC_LANGUAGE));
        fTextLanguage.setText(text);

        // Licence
        text = StringUtils.safeString(fSelectedItem.get(QUERY_CC_LICENSE));
        fTextLicence.setText(text);

        // Release Date
        text = StringUtils.safeString(fSelectedItem.get(QUERY_ELEMENT_RELEASEDATE));
        if(StringUtils.isSet(text)) {
            Date date = new Date(Long.parseLong(text) * 1000);  // php date to be multiplied with 1000 to make it java
            text = fDateFormatter.format(date);
        }
        fTextReleaseDate.setText(text);
        
        // File Size
        text = StringUtils.safeString(fSelectedItem.get(QUERY_COMPRESSED_SIZE));
        if(StringUtils.isSet(text)) {
            int val = (int)Float.parseFloat(text);
            text = "" + val; //$NON-NLS-1$
        }
        fTextFileSize.setText(text);
        
        // Container item count
        text = StringUtils.safeString(fSelectedItem.get(QUERY_CONTAINER_ITEM_COUNT));
        fTextContainerItemCount.setText(text);
        
        // Number of Activities
        text = StringUtils.safeString(fSelectedItem.get(QUERY_LD_NR_ACTIVITIES));
        fTextNumActivities.setText(text);

        // LD Structure
        text = StringUtils.safeString(fSelectedItem.get(QUERY_LD_STRUCTURE_TYPE));
        fTextLDStructure.setText(text);

        // LD Level 
        text = StringUtils.safeString(fSelectedItem.get(QUERY_LD_LEVEL));
        fTextLDLevel.setText(text);

        // LD Age
        text = StringUtils.safeString(fSelectedItem.get(QUERY_LD_AGE));
        fTextLDAgeGroup.setText(text);
     }
    
    /**
     * Create local actions
     */
    private void createActions() {
        fActionNextItem = new Action(Messages.UoLDetailsDialog_13) {
            @Override
            public void run() {
                int index = fList.indexOf(fSelectedItem);
                index++;
                if(index == fList.size()) {
                    index = 0;
                }
                fSelectedItem = fList.get(index);
                updateFields();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return OpenDockPlugin.imageDescriptorFromPlugin(OpenDockPlugin.PLUGIN_ID, IImages.ICON_NEXT);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionPreviousItem = new Action(Messages.UoLDetailsDialog_14) {
            @Override
            public void run() {
                int index = fList.indexOf(fSelectedItem);
                index--;
                if(index < 0) {
                    index = fList.size() - 1;
                }
                fSelectedItem = fList.get(index);
                updateFields();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return OpenDockPlugin.imageDescriptorFromPlugin(OpenDockPlugin.PLUGIN_ID, IImages.ICON_PREV);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };

    }

    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, true);
        Button downloadButton = createButton(parent, IDialogConstants.CLIENT_ID, Messages.UoLDetailsDialog_15, false);
        downloadButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                doDownloadItem();
            }
        });
    }
    
    /**
     * Download Item
     */
    private void doDownloadItem() {
        if(fSelectedItem == null || fList == null) {
            return;
        }
        
        OpenDockHandler.getInstance().downloadItemWithDialog(fSelectedItem);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setImage(OpenDockPlugin.imageDescriptorFromPlugin(OpenDockPlugin.PLUGIN_ID, IImages.ICON_APP).createImage());
        shell.setText(Messages.UoLDetailsDialog_16);
    }
    
    @Override
    protected IDialogSettings getDialogBoundsSettings() {
        IDialogSettings settings = OpenDockPlugin.getDefault().getDialogSettings();
        IDialogSettings section = settings.getSection(DIALOG_SETTINGS_SECTION);
        if(section == null) {
            section = settings.addNewSection(DIALOG_SETTINGS_SECTION);
        } 
        return section;
    }
}
