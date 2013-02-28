/*
 * Copyright (c) 2008, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.ui.views.inspector.item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;
import org.tencompetence.ldauthor.LDAuthorActionFactory;
import org.tencompetence.ldauthor.qti.model.QTIUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.BrowserWidgetNotSupportedException;
import org.tencompetence.ldauthor.ui.common.ExtendedBrowser;
import org.tencompetence.ldauthor.ui.common.StackComposite;
import org.tencompetence.ldauthor.ui.editors.EditorManager;
import org.tencompetence.ldauthor.utils.BrowserUtils;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * ItemModelViewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemModelViewer.java,v 1.12 2009/07/22 15:29:02 phillipus Exp $
 */
public class ItemModelViewer extends Composite {
    
    /**
     * Item Type Owner Model
     */
    private IItemTypeContainer fItemModel;
    
    private ILDModel fLDModel;
    
    /**
     * Flattened list of items to display
     */
    private List<IItemType> fFlatList;
    
    /**
     * Track the currently selected item
     */
    private IItemType fCurrentItem;

    
    private IAction fActionEditItem, fActionShowItemEditor, fActionPreviousItem, fActionNextItem, fActionRefresh;
    
    private StackComposite fStackComposite;
    
    private Browser fBrowser;
    
    private Composite fEmptyItemsMessageComposite;
    
    private Composite fExternalContentMessageComposite;

    private Hyperlink fLinkLabel;

    
    public ItemModelViewer(ItemModelComposite parent, int style) {
        super(parent, style);
        setup();
    }

    private void setup() {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 3;
        setLayout(layout);
        
        // Toolbar
        ToolBar toolBar = new ToolBar(this, SWT.FLAT);
        toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        AppFormToolkit.getInstance().adapt(toolBar);
        
        ToolBarManager toolBarmanager = new ToolBarManager(toolBar);

        fActionEditItem = new Action(Messages.ItemModelViewer_0) {
            @Override
            public void run() {
                // Edit Item if possible
                editSelectedItem();
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

        fActionPreviousItem = new Action(Messages.ItemModelViewer_1) {
            @Override
            public void run() {
                stepItemBack();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
                return sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_BACK);
            }

            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionNextItem = new Action(Messages.ItemModelViewer_2) {
            @Override
            public void run() {
                stepItemForward();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
                return sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD);
            }

            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionRefresh = new LDAuthorActionFactory.RefreshAction() {
            @Override
            public void run() {
                fBrowser.refresh();
            }
        };
        
        fActionShowItemEditor = new Action(Messages.ItemModelViewer_3) {
            @Override
            public void run() {
                ((ItemModelComposite)getParent()).showEditor();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ImageFactory.getImageDescriptor(ImageFactory.ICON_ITEMS);
            }

            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionEditItem.setEnabled(false);
        fActionNextItem.setEnabled(false);
        fActionPreviousItem.setEnabled(false);
        fActionRefresh.setEnabled(false);
        
        toolBarmanager.add(fActionEditItem);
        toolBarmanager.add(new Separator());
        toolBarmanager.add(fActionPreviousItem);
        toolBarmanager.add(fActionNextItem);
        toolBarmanager.add(fActionRefresh);
        toolBarmanager.add(new Separator());
        toolBarmanager.add(fActionShowItemEditor);
        
        toolBarmanager.update(true);
        
        fStackComposite = new StackComposite(this, SWT.BORDER);
        fStackComposite.setLayoutData(new GridData(GridData.FILL, SWT.FILL, true, true));

        try {
            fBrowser = createBrowser(fStackComposite);
        }
        catch(BrowserWidgetNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Set the Items to display
     * 
     * @param model
     */
    public void setModel(IItemTypeContainer model) {
        fItemModel = model;
        fLDModel = model.getLDModel();
        fCurrentItem = null;
        refresh();
    }
    
    /**
     * Refresh the List and controls
     */
    public void refresh() {
        if(fItemModel == null) {
            return;
        }
        
        // Get the list of Items, flattened
        fFlatList = getFlatList();
        boolean hasItems = !fFlatList.isEmpty();
        
        // If there are items...
        if(hasItems) {
            // And we already had an item being viewed and which is still valid
            if(fCurrentItem != null && fFlatList.contains(fCurrentItem)) {
                displayItem(fCurrentItem);
            }
            else {
                displayItem(fFlatList.get(0));
            }
        }
        else {
            fStackComposite.setControl(getEmptyItemsMessageComposite());
            fActionEditItem.setEnabled(false);
        }
        
        fActionNextItem.setEnabled(fFlatList.size() > 1);
        fActionPreviousItem.setEnabled(fFlatList.size() > 1);
        fActionRefresh.setEnabled(hasItems);
    }
    
    /**
     * @return A flattened list of Items in the hierarchy so we can step through them
     */
    private List<IItemType> getFlatList() {
        List<IItemType> list = new ArrayList<IItemType>();
        _getFlatList(fItemModel, list);
        return list;
    }
    // internal
    private void _getFlatList(IItemTypeContainer model, List<IItemType> list) {
        List<IItemType> l = model.getItemTypes();
        for(IItemType itemType : l) {
            list.add(itemType);
            _getFlatList(itemType, list);
        }
    }
    
    /**
     * Composite displaying Message that there are no Items to View with link to add one
     * @return
     */
    private Composite getEmptyItemsMessageComposite() {
        if(fEmptyItemsMessageComposite == null) {
            fEmptyItemsMessageComposite = AppFormToolkit.getInstance().createComposite(fStackComposite);
            fEmptyItemsMessageComposite.setLayout(new GridLayout());
            fEmptyItemsMessageComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
            AppFormToolkit.getInstance().createLabel(fEmptyItemsMessageComposite, Messages.ItemModelViewer_4);
            Hyperlink link = AppFormToolkit.getInstance().createHyperlink(fEmptyItemsMessageComposite, Messages.ItemModelViewer_5, SWT.NULL);
            link.addHyperlinkListener(new HyperlinkAdapter() {
                @Override
                public void linkActivated(HyperlinkEvent e) {
                    if(fItemModel != null) {
                        if(((ItemModelComposite)getParent()).getItemEditor().addNewItem(fItemModel)) {
                            refresh();
                        }
                    }
                }
            });
        }
        
        return fEmptyItemsMessageComposite;
    }
    
    /**
     * Composite displaying link to non-displayable content (PDF)
     * @return
     */
    private Composite getExternalContentMessageComposite() {
        if(fExternalContentMessageComposite == null) {
            fExternalContentMessageComposite = AppFormToolkit.getInstance().createComposite(fStackComposite);
            fExternalContentMessageComposite.setLayout(new GridLayout());
            fExternalContentMessageComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
            fLinkLabel = AppFormToolkit.getInstance().createHyperlink(fExternalContentMessageComposite, "", SWT.NULL); //$NON-NLS-1$
            fLinkLabel.addHyperlinkListener(new HyperlinkAdapter() {
                @Override
                public void linkActivated(HyperlinkEvent e) {
                    editSelectedItem();
                }
            });
        }
        
        return fExternalContentMessageComposite;
    }

    /**
     * Display an item in the Browser component
     * 
     * @param itemType
     */
    private void displayItem(IItemType itemType) {
        fCurrentItem = itemType;
        
        IResourceModel resource = fLDModel.getResourcesModel().getResource(itemType);
        if(resource == null) {
            fStackComposite.setControl(fBrowser);
            fBrowser.setText(""); //$NON-NLS-1$
            return;
        }
        
        String href = resource.getHref();
        
        if(href == null || "".equals(href)) { //$NON-NLS-1$
            fStackComposite.setControl(fBrowser);
            fBrowser.setText(""); //$NON-NLS-1$
        }
        // Special display cases such as QTI Tests
        else if(QTIUtils.isQTITestResource(resource)) {
            fStackComposite.setControl(getExternalContentMessageComposite());
            fLinkLabel.setText(QTIUtils.getFriendlyDisplayForQTITest());
            fLinkLabel.getParent().layout();
        }
        else if(BrowserUtils.isBrowserURL(href)) {
            fStackComposite.setControl(fBrowser);
            // See if it's a local file
            File rootFolder = fLDModel.getRootFolder();
            File file = new File(rootFolder, href);
            if(file.exists()) {
                fBrowser.setUrl("file:///" + file.toString()); //$NON-NLS-1$
            }
            // Or a web page
            else {
                fBrowser.setUrl(href);
            }
        }
        else {
            fStackComposite.setControl(getExternalContentMessageComposite());
            fLinkLabel.setText(href);
            fLinkLabel.getParent().layout();
        }
        
        fActionEditItem.setEnabled(isEditableItem(itemType));
    }
    
    /**
     * Step the Item back
     */
    private void stepItemBack() {
        if(fFlatList.size() <= 1) {
            return;
        }
        
        int index = 0;
        
        if(fCurrentItem != null) {
            index = fFlatList.indexOf(fCurrentItem);
            index--;
            if(index < 0) {
                index = fFlatList.size() - 1;
            }
        }

        displayItem(fFlatList.get(index));
    }
    
    /**
     * Step the Item forward
     */
    private void stepItemForward() {
        if(fFlatList.size() <= 1) {
            return;
        }
        
        int index = 0;
        
        if(fCurrentItem != null) {
            index = fFlatList.indexOf(fCurrentItem);
            index++;
            if(index >= fFlatList.size()) {
                index = 0;
            }
        }

        displayItem(fFlatList.get(index));
    }


    /**
     * Create the Browser component
     * 
     * @param parent
     * @return
     * @throws BrowserWidgetNotSupportedException
     */
    private Browser createBrowser(Composite parent) throws BrowserWidgetNotSupportedException {
        Browser browser;

        try {
            browser = new ExtendedBrowser(parent, SWT.NONE);
            browser.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        }
        catch(SWTError ex) {
            throw new BrowserWidgetNotSupportedException(ex);
        }
        
        return browser;
    }

    private boolean isEditableItem(IItemType itemType) {
        if(itemType == null) {
            return false;
        }
        
        String href = getItemHREF(itemType);
        
        if(!StringUtils.isSet(href)) {
            return false;
        }
        
        File file = new File(fLDModel.getRootFolder(), href);
        
        return file.exists();
    }

    private String getItemHREF(IItemType itemType) {
        if(itemType != null) {
            IResourceModel resource = fLDModel.getResourcesModel().getResource(itemType);
            if(resource != null) {
                return resource.getHref();
            }
        }
        return null;
    }

    private void editSelectedItem() {
        if(fCurrentItem == null) {
            return;
        }
        
        String href = getItemHREF(fCurrentItem);

        if(!StringUtils.isSet(href)) {
            return;
        }

        // Open in an editor if possible
        File file = new File(fLDModel.getRootFolder(), href);
        EditorManager.editFile(file, fLDModel);
    }
}
