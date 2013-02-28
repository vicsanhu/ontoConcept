/*
 * Copyright (c) 2009, Consortium Board TENCompetence
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
package org.tencompetence.ldauthor.ui.expressions;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.imsldmodel.types.IItemTypeContainer;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.util.ItemTypeDescriptionEnumerator;
import org.tencompetence.ldauthor.ldmodel.util.ItemTypeDescriptionEnumerator.ItemTypeContainerDetail;
import org.tencompetence.ldauthor.ldmodel.util.ItemTypeDescriptionEnumerator.ItemTypeOwnerDetail;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Chooser to select an LD Item
 * 
 * @author Phillip Beauvoir
 * @version $Id: ItemChooserPopupWindow.java,v 1.3 2009/06/16 10:01:00 phillipus Exp $
 */
public class ItemChooserPopupWindow extends PopupDialog {
    
    private static final String DIALOG_SETTINGS_SECTION = "ComponentChooserPopupWindowSettings"; //$NON-NLS-1$
    
    private Composite fParent;
    
    private Composite fComposite;
    
    private TreeViewer fTreeViewer;
    
    private ISelectionChangedListener fSelectionListener;
    
    private List<ItemTypeOwnerDetail> fItemDetails;
    
    private boolean fIsUpdating;
    
    public ItemChooserPopupWindow(Composite parent) {
        super(parent.getShell(), SWT.RESIZE, true, true, false, false, false, null, null);
        fParent = parent;
    }
    
    public void setLDModel(ILDModel ldModel, Object selected) {
        ItemTypeDescriptionEnumerator itemEnumerator = new ItemTypeDescriptionEnumerator(ldModel);
        fItemDetails = itemEnumerator.getAllItemTypeDetails();
        fTreeViewer.setInput(fItemDetails);
        
        if(selected != null) {
            fIsUpdating = true;
            fTreeViewer.setSelection(new StructuredSelection(selected));
            fIsUpdating = false;
        }
    }
    
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        fSelectionListener = listener;
        
        /*
         * Only pass on selection events that select an Item type
         */
        fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                Object o = ((StructuredSelection)event.getSelection()).getFirstElement();
                if(o instanceof IItemType) {
                    fSelectionListener.selectionChanged(event);
                }
            }
        });
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
        fComposite = (Composite) super.createDialogArea(parent);
        
        fTreeViewer = new TreeViewer(fComposite, SWT.NULL);
        fTreeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        fTreeViewer.setContentProvider(new ITreeContentProvider() {
            public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            }
            
            public void dispose() {
            }
            
            public Object[] getElements(Object parent) {
                return fItemDetails.toArray();
            }
            
            public Object getParent(Object child) {
                // This first...
                if(child instanceof IItemType) {
                    return ((IItemType)child).getParent();
                }
                
                // Then this...
                if(child instanceof IItemTypeContainer) {
                    for(ItemTypeOwnerDetail detail : fItemDetails) {
                        for(ItemTypeContainerDetail containerDetail : detail.getItemTypeContainerDetails()) {
                            if(containerDetail.getItemTypeContainer() == child) {
                                return containerDetail.getParentOwnerDetail();
                            }
                        }
                    }
                    return null;
                }
                
                return null;
            }
            
            public Object [] getChildren(Object parent) {
                if(parent instanceof ItemTypeOwnerDetail) {
                    return ((ItemTypeOwnerDetail)parent).getItemTypeContainerDetails().toArray();
                }
                if(parent instanceof ItemTypeContainerDetail) {
                    return ((ItemTypeContainerDetail)parent).getItemTypeContainer().getItemTypes().toArray();
                }
                if(parent instanceof IItemTypeContainer) {
                    return ((IItemTypeContainer)parent).getItemTypes().toArray();
                }
                return new Object[0];
            }
            
            public boolean hasChildren(Object parent) {
                if(parent instanceof ItemTypeOwnerDetail) {
                    return true;
                }
                if(parent instanceof ItemTypeContainerDetail) {
                    return ((ItemTypeContainerDetail)parent).getItemTypeContainer().getItemTypes().size() > 0;
                }
                if(parent instanceof IItemTypeContainer) {
                    return ((IItemTypeContainer)parent).getItemTypes().size() > 0;
                }
                return false;
            }
            
        });
        
        fTreeViewer.setLabelProvider(new ItemTreeLabelProvider());
        
        fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if(fIsUpdating) {
                    return;
                }
                
                // Only close if we selected an Item
                Object o = ((StructuredSelection)event.getSelection()).getFirstElement();
                if(o instanceof IItemType) {
                    if(fSelectionListener != null) {
                        fTreeViewer.removeSelectionChangedListener(fSelectionListener);
                    }
                    close();
                }
            }
        });
        
        return fComposite;
    }
    
    @Override
    protected Point getInitialLocation(Point size) {
        Point pt = fParent.getLocation();
        pt.y += fParent.getBounds().height;
        return fParent.getParent().toDisplay(pt);
    }

    @Override
    protected Point getDefaultSize() {
        return new Point(300, 200);
    }

    @Override
    protected Control getFocusControl() {
        return fTreeViewer.getControl();
    }

    @Override
    protected IDialogSettings getDialogSettings() {
        IDialogSettings settings = LDAuthorPlugin.getDefault().getDialogSettings();
        IDialogSettings section = settings.getSection(DIALOG_SETTINGS_SECTION);
        if(section == null) {
            section = settings.addNewSection(DIALOG_SETTINGS_SECTION);
        } 
        return section;
    }
    
    /**
     * Label Provider
     */
    private class ItemTreeLabelProvider extends LabelProvider implements IFontProvider {
        
        @Override
        public String getText(Object element) {
            if(element instanceof ItemTypeOwnerDetail) {
                ItemTypeOwnerDetail itemDetail = (ItemTypeOwnerDetail)element;
                return StringUtils.safeString(itemDetail.getOwnerName());
            }
            if(element instanceof ItemTypeContainerDetail) {
                ItemTypeContainerDetail itemDetail = (ItemTypeContainerDetail)element;
                return StringUtils.safeString(itemDetail.getName());
            }
            if(element instanceof IItemType) {
                IItemType itemType = (IItemType)element;
                String title = itemType.getTitle();
                if(StringUtils.isSet(title)) {
                    return title;
                }
                else {
                    IResourceModel resource = itemType.getLDModel().getResourcesModel().getResource(itemType);
                    return resource == null ? Messages.ItemChooserPopupWindow_0 : resource.getIdentifier();
                }
            }
            return element.toString();
        }
        
        @Override
        public Image getImage(Object obj) {
            if(obj instanceof ItemTypeOwnerDetail) {
                ItemTypeOwnerDetail itemDetail = (ItemTypeOwnerDetail)obj;
                
                if(itemDetail.getOwner() instanceof ILDModelObject) {
                    return ImageFactory.getIconLDType(((ILDModelObject)itemDetail.getOwner()));
                }
                
                if(obj instanceof ItemTypeContainerDetail) {
                    //return ImageFactory.getImage(ImageFactory.ICON_NODE);
                }

                return ImageFactory.getImage(ImageFactory.ICON_NODE);
            }
            
            if(obj instanceof IItemType) {
                return ImageFactory.getImage(ImageFactory.ICON_ITEM);
            }

            return null;
        }

        
        /*
         * Show a bold font
         */
        public Font getFont(Object element) {
            String fontName = "treeboldFont"; //$NON-NLS-1$
            
            if(!JFaceResources.getFontRegistry().hasValueFor(fontName)) {
                Font font = fTreeViewer.getTree().getFont();
                FontData[] fontdata = font.getFontData();
                fontdata[0].setStyle(SWT.BOLD);
                JFaceResources.getFontRegistry().put(fontName, fontdata);
            }
            
            if(element instanceof IItemType) {
                return JFaceResources.getFont(fontName);
            }
            
            return null;
        }
        
    }
}
