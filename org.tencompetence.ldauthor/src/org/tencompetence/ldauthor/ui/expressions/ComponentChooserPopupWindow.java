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
package org.tencompetence.ldauthor.ui.expressions;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.method.IActModel;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;


/**
 * Chooser to select an LD Component
 * 
 * @author Phillip Beauvoir
 * @version $Id: ComponentChooserPopupWindow.java,v 1.7 2009/06/16 10:01:00 phillipus Exp $
 */
public class ComponentChooserPopupWindow extends PopupDialog {
    
    private static final String DIALOG_SETTINGS_SECTION = "ComponentChooserPopupWindowSettings"; //$NON-NLS-1$
    
    private Composite fParent;
    
    private Composite fComposite;
    
    private TableViewer fTableViewer;
    
    private Object[] fItems = new Object[0];
    
    private ISelectionChangedListener fSelectionListener;
    
    private boolean fIsUpdating;
    
    public ComponentChooserPopupWindow(Composite parent) {
        super(parent.getShell(), SWT.RESIZE, true, true, false, false, false, null, null);
        fParent = parent;
    }
    
    public void setItems(Object[] items, Object selectedItem) {
        fItems = items;
        fTableViewer.setInput(fItems);
        
        if(selectedItem != null) {
            fIsUpdating = true;
            fTableViewer.setSelection(new StructuredSelection(selectedItem), true);
            fIsUpdating = false;
        }
    }
    
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        fSelectionListener = listener;
        fTableViewer.addSelectionChangedListener(listener);
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
        fComposite = (Composite) super.createDialogArea(parent);
        
        Composite tableComposite = new Composite(fComposite, SWT.NULL);
        TableColumnLayout tableLayout = new TableColumnLayout();
        tableComposite.setLayout(tableLayout);
        tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        fTableViewer = new TableViewer(tableComposite, SWT.NULL);
        
        Table table = fTableViewer.getTable();
        table.setHeaderVisible(false);
        
        TableViewerColumn column = new TableViewerColumn(fTableViewer, SWT.NONE);
        tableLayout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
        
        fTableViewer.setContentProvider(new IStructuredContentProvider() {

            public Object[] getElements(Object inputElement) {
                return fItems;
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
            
        });
        
        fTableViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                if(element instanceof IActModel) {
                    return "   " + ((IActModel)element).getTitle(); //$NON-NLS-1$
                }
                if(element instanceof IRolePartModel) {
                    return "     " + LDModelUtils.createRolePartTitle((IRolePartModel)element); //$NON-NLS-1$
                }
                if(element instanceof ITitle) {
                    return ((ITitle)element).getTitle();
                }
                return element.toString();
            }
            
            @Override
            public Image getImage(Object obj) {
                if(obj instanceof ILDModelObject) {
                    return ImageFactory.getIconLDType((ILDModelObject)obj);
                }

                return null;
            }
        });
        
        /*
         * Use a double-click listener because if we use a selection listener it's basically
         * a mouse-down and this can act on the parent dialog's mouse actions
         */
        fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                if(fSelectionListener != null) {
                    fTableViewer.removeSelectionChangedListener(fSelectionListener);
                }
                close();
            }
        });
        
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if(fIsUpdating) {
                    return;
                }
                
                if(fSelectionListener != null) {
                    fTableViewer.removeSelectionChangedListener(fSelectionListener);
                }
                
                close();
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
        return fTableViewer.getControl();
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
}
