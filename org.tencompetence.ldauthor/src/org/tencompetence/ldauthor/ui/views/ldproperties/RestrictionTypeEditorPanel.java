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
package org.tencompetence.ldauthor.ui.views.ldproperties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.properties.IPropertyTypeModel;
import org.tencompetence.imsldmodel.properties.IRestrictionType;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;


/**
 * RestrictionTypeEditorPanel
 * 
 * @author Phillip Beauvoir
 * @version $Id: RestrictionTypeEditorPanel.java,v 1.4 2009/06/29 08:30:34 phillipus Exp $
 */
public class RestrictionTypeEditorPanel extends Composite {
    
    private RestrictionTypeTableViewer fTableViewer;
    
    /*
     * Buttons
     */
    private Button fButtonNew, fButtonRemove, fButtonUp, fButtonDown;
    
    
    private IPropertyTypeModel fProperty;


    public RestrictionTypeEditorPanel(Composite parent, int style) {
        super(parent, style);
        
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        
        Composite c = new Composite(this, SWT.NULL);
        c.setLayout(new TableColumnLayout());
        c.setLayoutData(new GridData(GridData.FILL_BOTH));
        fTableViewer = new RestrictionTypeTableViewer(c, SWT.NULL);
        
        createButtonBar(this);
    }

    /**
     * Create the Button bar
     * @param parent
     * @return The Bar
     */
    private Composite createButtonBar(Composite parent) {
        Composite client = AppFormToolkit.getInstance().createComposite(parent, SWT.NULL);
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);
        
        GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        client.setLayoutData(gd);

        fButtonNew = new Button(client, SWT.FLAT);
        fButtonNew.setText(Messages.RestrictionTypeEditorPanel_0);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fButtonNew.setLayoutData(gd);
        fButtonNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addNewRestrictionType();
            }
        });
        
        fButtonRemove = new Button(client, SWT.FLAT);
        fButtonRemove.setText(Messages.RestrictionTypeEditorPanel_1);
        fButtonRemove.setEnabled(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fButtonRemove.setLayoutData(gd);
        fButtonRemove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                deleteSelected();
            }
        });
        
        fButtonUp = new Button(client, SWT.FLAT);
        fButtonUp.setText(Messages.RestrictionTypeEditorPanel_2);
        fButtonUp.setEnabled(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fButtonUp.setLayoutData(gd);
        fButtonUp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                moveSelectedUp();
            }
        });

        fButtonDown = new Button(client, SWT.FLAT);
        fButtonDown.setText(Messages.RestrictionTypeEditorPanel_3);
        fButtonDown.setEnabled(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fButtonDown.setLayoutData(gd);
        fButtonDown.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                moveSelectedDown();
            }
        });

        // Listen to table selections to enable buttons
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection)event.getSelection();
                IRestrictionType restrictionType = (IRestrictionType)selection.getFirstElement();
                fButtonRemove.setEnabled(!selection.isEmpty());
                fButtonUp.setEnabled(restrictionType != null && fProperty.getRestrictionTypes().indexOf(restrictionType) > 0);
                fButtonDown.setEnabled(restrictionType != null && fProperty.getRestrictionTypes().indexOf(restrictionType) < fProperty.getRestrictionTypes().size() - 1);
            }
        });

        return client;
    }

    
    /**
     * Add a new RestrictionType to the end of the list
     */
    public void addNewRestrictionType() {
        if(fProperty != null) {
            IRestrictionType restrictionType = (IRestrictionType)LDModelFactory.createModelObject(LDModelFactory.RESTRICTION, fProperty.getLDModel());
            fProperty.getRestrictionTypes().add(restrictionType);
            fTableViewer.refresh();
            fProperty.getLDModel().setDirty();
            fTableViewer.editElement(restrictionType, 0);
        }
    }
    
    /**
     * Delete Selected Restrictions
     */
    public void deleteSelected() {
        if(fProperty == null) {
            return;
        }
        
        StructuredSelection selection = (StructuredSelection)fTableViewer.getSelection();
        Object[] objects = selection.toArray();
        
        // Make sure we didn't get the empty selection
        if(objects.length == 0) {
            return;
        }
        
        // Confirmation dialog
        boolean ok = MessageDialog.openQuestion(
                getShell(),
                Messages.RestrictionTypeEditorPanel_4,
                objects.length == 1 ?
                        Messages.RestrictionTypeEditorPanel_5 
                        : 
                        Messages.RestrictionTypeEditorPanel_6);
        
        if(!ok) {
            return;
        }
        
        // Delete
        for(int i = 0; i < objects.length; i++) {
            fProperty.getRestrictionTypes().remove((IRestrictionType)objects[i]);
        }
        
        fTableViewer.refresh();
        fProperty.getLDModel().setDirty();
    }
   
    /**
     * Move Selected up
     */
    public void moveSelectedUp() {
        StructuredSelection selection = (StructuredSelection)fTableViewer.getSelection();
        if(selection.isEmpty()) {
            return;
        }
        
        IRestrictionType restrictionType = (IRestrictionType)selection.getFirstElement();
        int index = fProperty.getRestrictionTypes().indexOf(restrictionType);
        if(index > 0) {
            fProperty.getRestrictionTypes().remove(restrictionType);
            fProperty.getRestrictionTypes().add(index - 1, restrictionType);
            fTableViewer.refresh();
            fProperty.getLDModel().setDirty();
        }
    }
   
    /**
     * Move Selected down
     */
    public void moveSelectedDown() {
        StructuredSelection selection = (StructuredSelection)fTableViewer.getSelection();
        if(selection.isEmpty()) {
            return;
        }
        
        IRestrictionType restrictionType = (IRestrictionType)selection.getFirstElement();
        int index = fProperty.getRestrictionTypes().indexOf(restrictionType);
        if(index < fProperty.getRestrictionTypes().size() - 1) {
            fProperty.getRestrictionTypes().remove(restrictionType);
            fProperty.getRestrictionTypes().add(index + 1, restrictionType);
            fTableViewer.refresh();
            fProperty.getLDModel().setDirty();
        }
    }

    public void setProperty(IPropertyTypeModel property) {
        fProperty = property;
        fTableViewer.setProperty(property);
    }

    public void setPanelEnabled(boolean enabled) {
        fTableViewer.setSelection(null);
        fTableViewer.getControl().setEnabled(enabled);
        
        fButtonNew.setEnabled(enabled);

        enabled = enabled && !fTableViewer.getSelection().isEmpty();
        fButtonRemove.setEnabled(enabled);
        fButtonUp.setEnabled(enabled);
        fButtonDown.setEnabled(enabled);
    }
    
    public RestrictionTypeTableViewer getRestrictionTypeTableViewer() {
        return fTableViewer;
    }
}
