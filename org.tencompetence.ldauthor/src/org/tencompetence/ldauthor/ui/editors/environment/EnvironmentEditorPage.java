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
package org.tencompetence.ldauthor.ui.editors.environment;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ldmodel.IReCourseLDModel;
import org.tencompetence.ldauthor.ui.editors.IEnvironmentEditorPage;
import org.tencompetence.ldauthor.ui.editors.ILDMultiPageEditor;
import org.tencompetence.ldauthor.ui.editors.common.AbstractLDGraphicalEditorPage;
import org.tencompetence.ldauthor.ui.editors.environment.dnd.EnvironmentEditorTransferDropTargetListener;
import org.tencompetence.ldauthor.ui.editors.ld.LDMultiPageEditor;
import org.tencompetence.ldauthor.ui.views.inspector.InspectorManager;



/**
 * Environment Editor as embedded in LD MultiPage editor.
 * 
 * This is the Environment Editor that also acts as Host for sub-composite panels
 * for Environments.
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentEditorPage.java,v 1.37 2010/02/01 10:45:09 phillipus Exp $
 */
public class EnvironmentEditorPage
extends AbstractLDGraphicalEditorPage implements IEnvironmentEditorPage {

    public static final String ID = LDAuthorPlugin.PLUGIN_ID + ".environmentEditorPage"; //$NON-NLS-1$
    
    /**
     * Palette Root
     */
    private EnvironmentEditorPalette fPalette;

    /*
     * LD Model
     */
    private IReCourseLDModel fLDModel;
    
    
    /**
     * Default Constructor
     */
    public EnvironmentEditorPage() {
    }
    
    @Override
    public void setInput(IEditorInput input) {
        super.setInput(input);
        
        // This first
        fLDModel = (IReCourseLDModel)input.getAdapter(ILDModel.class);
        setModel(fLDModel.getReCourseInfoModel().getGraphicalEnvironmentsModel());
        
        // Edit Domain before init
        setEditDomain(new DefaultEditDomain(this));
    }
    
    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        
        // Drag and Drop
        //new EnvironmentEditorDragDropHandler(this);
                
        // Experimental DnD
        getGraphicalViewer().addDropTargetListener(new EnvironmentEditorTransferDropTargetListener(getGraphicalViewer()));

        // Tell Inspector
        getGraphicalViewer().addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                updateInspector((StructuredSelection)event.getSelection());
            }
        });
    }
    
    /*
     * Have to over-ride this to re-select object when re-focussed on Editor
     */ 
    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        super.selectionChanged(part, selection);
        
        if(part instanceof LDMultiPageEditor && ((LDMultiPageEditor)part).getActiveEditor() == this) {
            updateInspector((StructuredSelection)selection);
        }
    }
    
    /**
     * Update the Inspector with new selection
     * @param selection
     */
    private void updateInspector(StructuredSelection selection) {
        Object o = selection.getFirstElement();
        if(InspectorManager.getInstance().getInput() != o) {
            InspectorManager.getInstance().setInput(EnvironmentEditorPage.this, o);
        }
    }

    
    @Override
    protected EditPartFactory getEditPartFactory() {
        return new EnvironmentEditPartFactory();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot()
     */
    @Override
    public PaletteRoot getPaletteRoot() {
        if(fPalette == null) {
            fPalette = new EnvironmentEditorPalette(fLDModel);
        }
        return fPalette;
    }
    
    @Override
    protected TransferDropTargetListener getTransferDropTargetListener() {
        return new TemplateTransferDropTargetListener(getGraphicalViewer());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        if(adapter == ILDModel.class) {
            return fLDModel;
        }
        
        /*
         * Return the multi-page editor
         */
        if(adapter == ILDMultiPageEditor.class) {
            return ((MultiPageEditorSite)getSite()).getMultiPageEditor();
        }

        return super.getAdapter(adapter);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        // Other cleanup
        if(fPalette != null) {
            fPalette.dispose();
        }
    }

}
