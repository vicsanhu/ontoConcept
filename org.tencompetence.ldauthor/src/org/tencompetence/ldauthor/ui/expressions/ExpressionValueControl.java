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

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelJDOMPersistence;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.method.IRolePartModel;
import org.tencompetence.imsldmodel.resources.IResourceModel;
import org.tencompetence.imsldmodel.types.IItemType;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Expression Value Control for editing and selecting Values
 * 
 * @author Phillip Beauvoir
 * @version $Id: ExpressionValueControl.java,v 1.11 2009/07/09 15:27:04 phillipus Exp $
 */
public class ExpressionValueControl extends Composite {
    private Label fImageLabel;
    private Label fTextLabel;

    private Object fValue;
    
    private ISelectionChangedListener fListener;
    
    private Cursor fHandCursor;
    
    public ExpressionValueControl(final Composite parent) {
        super(parent, SWT.BORDER);
        
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        
        GridData gd = new GridData(GridData.FILL, GridData.CENTER, true, true);
        gd.heightHint = Platform.getOS().equals(Platform.OS_WIN32) ? 18 : 20;
        setLayoutData(gd);
        
        fImageLabel = new Label(this, SWT.NULL);
        
        fTextLabel = new Label(this, SWT.NULL);
        fTextLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true));
        fHandCursor = new Cursor(parent.getDisplay(), SWT.CURSOR_HAND);
        fTextLabel.setCursor(fHandCursor);
        
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                fHandCursor.dispose();
            }
        });
    }
    
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        fListener = listener;
    }
    
    public void setItems(final Object items[], Object selectedValue) {
        fTextLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                ComponentChooserPopupWindow window = new ComponentChooserPopupWindow(ExpressionValueControl.this);
                window.create();
                window.open();
                window.setItems(items, fValue);
                window.addSelectionChangedListener(new ISelectionChangedListener() {
                    public void selectionChanged(SelectionChangedEvent event) {
                        fValue = ((StructuredSelection)event.getSelection()).getFirstElement();
                        updateControl();
                        // Pass event on to listener
                        if(fListener != null) {
                            fListener.selectionChanged(event);
                        }
                    }
                });
            }
        });
        
        if(selectedValue instanceof ILDModelObjectReference) {
            fValue = ((ILDModelObjectReference)selectedValue).getLDModelObject();
        }
        else {
            fValue = selectedValue;
        }
        
        updateControl();
    }
    
    /**
     * Show all the Items in the LD
     * @param ldModel
     */
    public void setLDModelItems(final ILDModel ldModel, Object selectedValue) {
        fTextLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                ItemChooserPopupWindow window = new ItemChooserPopupWindow(ExpressionValueControl.this);
                window.create();
                window.open();
                window.setLDModel(ldModel, fValue);
                window.addSelectionChangedListener(new ISelectionChangedListener() {
                    public void selectionChanged(SelectionChangedEvent event) {
                        fValue = ((StructuredSelection)event.getSelection()).getFirstElement();
                        updateControl();
                        // Pass event on to listener
                        if(fListener != null) {
                            fListener.selectionChanged(event);
                        }
                    }
                });
            }
        });
        
        if(selectedValue instanceof ILDModelObjectReference) {
            fValue = ((ILDModelObjectReference)selectedValue).getLDModelObject();
        }
        else {
            fValue = selectedValue;
        }
        
        updateControl();
    }
    
    private void updateControl() {
        String selected = null;
        fTextLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
        
        if(fValue instanceof IRolePartModel) {
            selected = LDModelUtils.createRolePartTitle((IRolePartModel)fValue);
        }
        else if(fValue instanceof ITitle) {
            selected = ((ITitle)fValue).getTitle();
        }
        else if(fValue instanceof IItemType) {
            IItemType itemType = (IItemType)fValue;
            String title = itemType.getTitle();
            if(StringUtils.isSet(title)) {
                selected = title;
            }
            else {
                IResourceModel resource = itemType.getLDModel().getResourcesModel().getResource(itemType);
                selected = (resource == null) ? Messages.ExpressionValueControl_1 : resource.getIdentifier();
            }
        }
        else if(fValue instanceof String) {
            selected = (String)fValue;
        }
        else {
            selected = Messages.ExpressionValueControl_0;
            fTextLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        }
        
        String tag = null;
        if(fValue instanceof ILDModelJDOMPersistence) {
            tag = ((ILDModelJDOMPersistence)fValue).getTagName();
        }
        
        fImageLabel.setImage(ImageFactory.getIconLDType(tag));
        fTextLabel.setText(selected);
        layout(); // Shows Icon if not one before
    }
}

