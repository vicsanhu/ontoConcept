/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.tencompetence.ldauthor.ui.editors.common;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.ld.LDMultiPageEditor;

/**
 * This is a sample implementation of an outline page showing an
 * overview of a graphical editor.
 * 
 * @author Gunnar Wagenknecht
 */
public class OverviewOutlinePage extends Page implements IContentOutlinePage, ISelectionListener {

    /** the control of the overview */
    private Canvas overview;

    /** the thumbnail */
    private ScrollableThumbnail thumbnail;
    
    private LightweightSystem lws;

    /**
     * Creates a new OverviewOutlinePage instance.
     */
    public OverviewOutlinePage() {
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        // create canvas and lws
        overview = new Canvas(parent, SWT.NONE);
        lws = new LightweightSystem(overview);
        
        getSite().getPage().addSelectionListener(this);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.IPage#dispose()
     */
    @Override
    public void dispose() {
        if(thumbnail != null) {
            thumbnail.deactivate();
            thumbnail = null;
        }
        
        getSite().getPage().removeSelectionListener(this);

        super.dispose();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.IPage#getControl()
     */
    @Override
    public Control getControl() {
        return overview;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
     */
    public ISelection getSelection() {
        return StructuredSelection.EMPTY;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
     */
    public void setSelection(ISelection selection) {
    }
    
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        /*
         * Listen to selections to update the thumbnail
         */
        if(part instanceof LDMultiPageEditor) {
            part = ((LDMultiPageEditor)part).getActiveEditor();
        }
        
        if(part instanceof ILDEditorPart) {
            EditPart editPart = (EditPart)part.getAdapter(EditPart.class);
            setEditPart(editPart);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.IPage#setFocus()
     */
    @Override
    public void setFocus() {
        if(getControl() != null) {
            getControl().setFocus();
        }
    }

    /**
     * Set the EditPart to diplay an overview of
     * @param editPart
     */
    public void setEditPart(EditPart editPart) {
        // Hide the thumbnail if Edit part is null or not a ScalableFreeformRootEditPart
        if(editPart == null || !(editPart instanceof ScalableFreeformRootEditPart)) {
            if(thumbnail != null) {
                thumbnail.setVisible(false);
            }
            return;
        }
        
        ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart)editPart;
        
        // Have to create this here
        if(thumbnail == null) {
            thumbnail = new ScrollableThumbnail();
            thumbnail.setBorder(new MarginBorder(3));
            lws.setContents(thumbnail);
        }
        
        thumbnail.setVisible(true);
        thumbnail.setViewport((Viewport)rootEditPart.getFigure());
        thumbnail.setSource(rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS));
        
        // Force an update
        rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS).repaint();
    }
}
