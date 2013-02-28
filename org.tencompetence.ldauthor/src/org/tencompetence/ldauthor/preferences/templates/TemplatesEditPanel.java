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
package org.tencompetence.ldauthor.preferences.templates;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.ldauthor.templates.ITemplate;
import org.tencompetence.ldauthor.templates.ITemplateGroup;
import org.tencompetence.ldauthor.templates.LDTemplateManager;
import org.tencompetence.ldauthor.templates.UserTemplateGroup;
import org.tencompetence.ldauthor.templates.impl.ld.UserLDTemplate;


/**
 * TemplatesEditPanel
 * 
 * @author Phillip Beauvoir
 * @version $Id: TemplatesEditPanel.java,v 1.4 2008/12/15 15:00:35 phillipus Exp $
 */
public class TemplatesEditPanel extends Composite {
    
    private Text fNameTextField;
    private Text fDescriptionTextField;
    
    private ComboViewer fComboViewer;
    
    boolean fModifying;
    
    private Object fUserSelected;
    
    private LDTemplateManager fLDTemplateManager;
    
    private TemplatesTreeViewer fTreeViewer;
    
    private boolean fIsDirty;
    
    public TemplatesEditPanel(Composite parent, int style, LDTemplateManager templateManager, TemplatesTreeViewer treeViewer) {
        super(parent, style);
        
        fLDTemplateManager = templateManager;
        
        fTreeViewer = treeViewer;
        
        GridLayout layout = new GridLayout(2, false);
        setLayout(layout);
        
        GridData gd;
        Label label;
        
        label = new Label(this, SWT.NULL);
        label.setText(Messages.TemplatesEditPanel_0);
        
        fNameTextField = new Text(this, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fNameTextField.setLayoutData(gd);
        fNameTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if(fModifying) {
                    return;
                }
                
                if(fUserSelected instanceof UserLDTemplate) {
                    ((ITemplate)fUserSelected).setName(fNameTextField.getText());
                    fTreeViewer.refresh(fUserSelected);
                    fIsDirty = true;
                }
                
                if(fUserSelected instanceof UserTemplateGroup) {
                    ((ITemplateGroup)fUserSelected).setName(fNameTextField.getText());
                    fTreeViewer.refresh(fUserSelected);
                    fIsDirty = true;
                }
            }
        });
        fNameTextField.setEnabled(false);
        
        label = new Label(this, SWT.NULL);
        label.setText(Messages.TemplatesEditPanel_1);
        
        fDescriptionTextField = new Text(this, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        gd = new GridData(GridData.FILL_BOTH);
        fDescriptionTextField.setLayoutData(gd);
        fDescriptionTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if(fModifying) {
                    return;
                }
                
                if(fUserSelected instanceof UserLDTemplate) {
                    ((ITemplate)fUserSelected).setDescription(fDescriptionTextField.getText());
                    fIsDirty = true;
                }
            }
        });
        
        fDescriptionTextField.setEnabled(false);
        
        label = new Label(this, SWT.NULL);
        label.setText(Messages.TemplatesEditPanel_2);
        
        fComboViewer = new ComboViewer(this, SWT.READ_ONLY);
        fComboViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fComboViewer.getControl().setEnabled(false);
        fComboViewer.setContentProvider(new ComboViewerProvider());
        fComboViewer.setLabelProvider(new ComboViewerLabelProvider());
        
        fComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if(fModifying) {
                    return;
                }
                
                if(fUserSelected instanceof UserLDTemplate) {
                    ITemplate template = (ITemplate)fUserSelected;
                    ITemplateGroup newGroup = (ITemplateGroup)((StructuredSelection)event.getSelection()).getFirstElement();
                    if(newGroup != null) {
                        ITemplateGroup oldGroup = fLDTemplateManager.getParentGroup(template);
                        if(oldGroup != null && (oldGroup != newGroup)) {
                            oldGroup.removeTemplate(template);
                            newGroup.addTemplate(template);
                            fTreeViewer.refresh();
                            fIsDirty = true;
                        }
                    }
                }
            }
        });
        
        fComboViewer.setInput(fLDTemplateManager);
    }

    public boolean isDirty() {
        return fIsDirty;
    }
    
    public void updateCombo() {
        fComboViewer.refresh();
    }

    public void setDisplay(Object o) {
        fModifying = true;
        
        fNameTextField.setEnabled((o instanceof UserLDTemplate) || (o instanceof UserTemplateGroup));
        fDescriptionTextField.setEnabled(o instanceof UserLDTemplate);
        fComboViewer.getControl().setEnabled(o instanceof UserLDTemplate);
        
        if(o == null) {
            fNameTextField.setText(""); //$NON-NLS-1$
            fDescriptionTextField.setText(""); //$NON-NLS-1$
            fComboViewer.setSelection(StructuredSelection.EMPTY);
        }
        else if(o instanceof ITemplateGroup) {
            ITemplateGroup group = (ITemplateGroup)o;
            fNameTextField.setText(group.getName());
            fDescriptionTextField.setText(""); //$NON-NLS-1$
            fComboViewer.setSelection(StructuredSelection.EMPTY);
        }
        else if(o instanceof ITemplate) {
            ITemplate template = (ITemplate)o;
            fNameTextField.setText(template.getName());
            fDescriptionTextField.setText(template.getDescription());
            fComboViewer.setSelection(new StructuredSelection(fLDTemplateManager.getParentGroup(template)));
        }
        
        if(o instanceof UserLDTemplate || o instanceof UserTemplateGroup) {
            fUserSelected = o;
        }
        else {
            fUserSelected = null;
        }
        
        fModifying = false;
    }
    
    // ===================================================================================
    //                           Combo Viewer Provider
    // ===================================================================================
    
    private class ComboViewerProvider
    implements IStructuredContentProvider {
        
        public Object[] getElements(Object input) {
            if(input instanceof LDTemplateManager) {
                return ((LDTemplateManager)input).getTemplateGroups().toArray();
            }
            else {
                return new Object[0];
            }
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

    }
    
    private class ComboViewerLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            if(element instanceof ITemplateGroup) {
                return ((ITemplateGroup)element).getName();
            }
            return super.getText(element);
        }
    }
}
