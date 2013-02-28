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
package org.tencompetence.ldauthor.ui.views.inspector.environment.monitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.environments.IMonitorModel;
import org.tencompetence.imsldmodel.roles.IRoleModel;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.ui.views.inspector.AbstractScrolledInspectorSection;
import org.tencompetence.ldauthor.ui.views.inspector.IInspectorProvider;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorWidgetFactory;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * Inspector Section for Monitor Settings
 * 
 * @author Phillip Beauvoir
 * @version $Id: MonitorSettingsSection.java,v 1.7 2009/06/10 10:16:49 phillipus Exp $
 */
public class MonitorSettingsSection extends AbstractScrolledInspectorSection {
    
    // "Self" Role - bogus role signifying "self" option
    private static String SELF_ROLE = Messages.MonitorSettingsSection_0;
    
    private Text fTextTitle, fTextClass, fTextParameters;
    
    private Text fTextID;
    
    private Button fButtonVisible;
    
    private ComboViewer fComboRoleViewer;
    
    private boolean fIsUpdating;
    
    private IGraphicalModelObject fGraphicalModelObject;
    
    private IMonitorModel fMonitor;
    
    private ModifyListener fModifyListener = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fMonitor == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fTextTitle) {
                String s = fTextTitle.getText();
                fMonitor.setTitle(s);
                if(fGraphicalModelObject != null) {
                    fGraphicalModelObject.setName(s);
                }
            }
            else if(control == fTextClass) {
                fMonitor.setClassString(fTextClass.getText());
            }
            else if(control == fTextParameters) {
                fMonitor.setParameters(fTextParameters.getText());
            }
            
            fMonitor.getLDModel().setDirty();
        }
    };
    
    private SelectionAdapter fSelectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(fIsUpdating) {
                return;
            }
            
            if(fMonitor == null) {
                return;
            }
            
            Object control = e.getSource();
            
            if(control == fButtonVisible) {
                fMonitor.setIsVisible(fButtonVisible.getSelection());
            }
            
            else if(control == fComboRoleViewer.getControl()) {
                StructuredSelection selection = (StructuredSelection)fComboRoleViewer.getSelection();
                if(!selection.isEmpty()) {
                    Object role = selection.getFirstElement();
                    if(role == SELF_ROLE) {
                        fMonitor.setRole(null);
                    }
                    else {
                        fMonitor.setRole((IRoleModel)role);
                    }
                }
            }
            
            fMonitor.getLDModel().setDirty();
        }
    };

    
    public MonitorSettingsSection(Composite parent) {
        super(parent);
    }

    public void createControls() {
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        Composite composite = factory.createComposite(this);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        setContent(composite);
        
        Label label = factory.createLabel(composite, Messages.MonitorSettingsSection_1);
        label.setToolTipText(Messages.MonitorSettingsSection_2);
        fTextTitle = factory.createText(composite, ""); //$NON-NLS-1$
        GridData gd = new GridData(GridData.FILL_BOTH);
        fTextTitle.setLayoutData(gd);
        fTextTitle.addModifyListener(fModifyListener);

        label = factory.createLabel(composite, Messages.MonitorSettingsSection_3);
        label.setToolTipText(Messages.MonitorSettingsSection_4);
        fComboRoleViewer = new ComboViewer(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        fComboRoleViewer.getCombo().addSelectionListener(fSelectionListener);
        fComboRoleViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Combo Content Provider
        IStructuredContentProvider contentProvider = new IStructuredContentProvider() {
            public Object[] getElements(Object inputElement) {
                List<Object> list = new ArrayList<Object>();
                list.add(0, SELF_ROLE);
                for(IRoleModel roleModel : fMonitor.getLDModel().getRolesModel().getOrderedRoles()) {
                    list.add(roleModel);
                }
                return list.toArray();
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        };
        
        // Combo Label Provider
        ILabelProvider labelProvider = new LabelProvider() {
            @Override
            public String getText(Object element) {
                if(element instanceof String) {
                    return (String)element;
                }
                return ((ITitle)element).getTitle();
            }
        };
        
        fComboRoleViewer.setContentProvider(contentProvider);
        fComboRoleViewer.setLabelProvider(labelProvider);

        fButtonVisible = factory.createButton(composite, Messages.MonitorSettingsSection_5, SWT.CHECK);
        fButtonVisible.setToolTipText(Messages.MonitorSettingsSection_6);
        fButtonVisible.addSelectionListener(fSelectionListener);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        fButtonVisible.setLayoutData(gd);

        label = factory.createLabel(composite, Messages.MonitorSettingsSection_7);
        label.setToolTipText(Messages.MonitorSettingsSection_8);
        fTextClass = factory.createText(composite, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextClass.setLayoutData(gd);
        fTextClass.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.MonitorSettingsSection_9);
        label.setToolTipText(Messages.MonitorSettingsSection_10);
        fTextParameters = factory.createText(composite, ""); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextParameters.setLayoutData(gd);
        fTextParameters.addModifyListener(fModifyListener);
        
        label = factory.createLabel(composite, Messages.MonitorSettingsSection_11);
        label.setToolTipText(Messages.MonitorSettingsSection_12);
        fTextID = factory.createText(composite, "", SWT.READ_ONLY); //$NON-NLS-1$
        gd = new GridData(GridData.FILL_BOTH);
        fTextID.setLayoutData(gd);
        
        composite.layout();
        composite.pack();
    }
    

    @Override
    public void setInput(IInspectorProvider provider, Object element) {
        super.setInput(provider, element);
        
        if(element instanceof IMonitorModel) {
            fMonitor = (IMonitorModel)element;
        }
        else {
            throw new RuntimeException("Should have been a Monitor Part"); //$NON-NLS-1$
        }

        refresh();
    }
    
    public void refresh() {
        if(fMonitor == null) {
            return;
        }
        
        // Populate fields...
        fIsUpdating = true;
        
        String s = StringUtils.safeString(fMonitor.getTitle());
        fTextTitle.setText(s);

        s = StringUtils.safeString(fMonitor.getClassString());
        fTextClass.setText(s);

        s = StringUtils.safeString(fMonitor.getParameters());
        fTextParameters.setText(s);
        
        fComboRoleViewer.setInput(""); //$NON-NLS-1$

        Object role = fMonitor.getRole();
        if(role == null) {
            role = SELF_ROLE;
        }
        fComboRoleViewer.setSelection(new StructuredSelection(role));
        
        fButtonVisible.setSelection(fMonitor.isVisible());
        
        fTextID.setText(StringUtils.safeString(fMonitor.getIdentifier()));

        fIsUpdating = false;
    }
}
