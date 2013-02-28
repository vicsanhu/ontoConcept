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
package org.tencompetence.ldauthor.ui.views.inspector.ldproperties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ITitle;
import org.tencompetence.imsldmodel.properties.ILocalPropertyModel;
import org.tencompetence.imsldmodel.types.ITimeLimitType;
import org.tencompetence.ldauthor.ui.common.AppFormToolkit;
import org.tencompetence.ldauthor.ui.common.DurationWidget;


/**
 * Composite to show Duration widget and optional Property Ref
 * 
 * @author Phillip Beauvoir
 * @version $Id: TimeLimitComposite.java,v 1.2 2009/05/19 18:21:03 phillipus Exp $
 */
public class TimeLimitComposite extends Composite {
    
    private ILDModel fLDModel;
    
    private ITimeLimitType fTimeLimitType;
    
    private DurationWidget fDurationWidget;
    
    private ComboViewer fComboProperties;
    
    private final String NONE_STRING = Messages.TimeLimitComposite_0;
    
    private boolean fIsUpdating;

    public TimeLimitComposite(Composite parent, int style) {
        super(parent, style);
        
        AppFormToolkit.getInstance().adapt(this);
        
        GridLayout layout = new GridLayout();
        setLayout(layout);
        
        fDurationWidget = new DurationWidget(this, SWT.NULL);
        new Label(this, SWT.NULL);
        AppFormToolkit.getInstance().createLabel(this, Messages.TimeLimitComposite_1);
        
        fComboProperties = new ComboViewer(this, SWT.READ_ONLY);
        fComboProperties.getControl().setLayoutData(new GridData(GridData.FILL, SWT.NULL, false, false));
        ((Combo)fComboProperties.getControl()).setVisibleItemCount(12);
        fComboProperties.setContentProvider(new PropertyComboViewerProvider());
        fComboProperties.setLabelProvider(new PropertyComboViewerLabelProvider());
        
        fComboProperties.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if(fIsUpdating) {
                    return;
                }
                Object object = ((StructuredSelection)event.getSelection()).getFirstElement();
                if(object == NONE_STRING) {
                    fTimeLimitType.setPropertyRef(null);
                    fDurationWidget.setEnabled(true);
                }
                else if(object instanceof ILocalPropertyModel) {
                    fTimeLimitType.setPropertyRef((ILocalPropertyModel)object);
                    fDurationWidget.setEnabled(false);
                }
                
                fLDModel.setDirty();
            }
        });
    }

    public void setTimeLimitType(ITimeLimitType timeLimitType) {
        fIsUpdating = true;
        
        fTimeLimitType = timeLimitType;
        fLDModel = timeLimitType.getOwner().getLDModel();
        
        fDurationWidget.setDurationType(timeLimitType.getDurationType(), fLDModel);
        fDurationWidget.setEnabled(timeLimitType.getPropertyRef() == null);
        
        fComboProperties.setInput(fLDModel);
        
        // No Property Ref
        if(timeLimitType.getPropertyRef() == null) {
            fComboProperties.setSelection(new StructuredSelection(NONE_STRING));
        }
        else {
            fComboProperties.setSelection(new StructuredSelection(timeLimitType.getPropertyRef()));
        }
        
        fIsUpdating = false;
    }
    
    // ===================================================================================
    //                           Combo Viewer Provider
    // ===================================================================================
    
    private class PropertyComboViewerProvider
    implements IStructuredContentProvider {
        
        public Object[] getElements(Object input) {
            List<Object> list = new ArrayList<Object>();
            
            // Add "None" Object
            list.add(NONE_STRING);
            
            for(ILDModelObject property : fLDModel.getPropertiesModel().getChildren()) {
                if(property instanceof ILocalPropertyModel) {
                    list.add(property);
                }
            }
            
            return list.toArray();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

    }
    
    private class PropertyComboViewerLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            if(element instanceof String) {
                return (String)element;
            }
            return ((ITitle)element).getTitle();
        }
    }
}
