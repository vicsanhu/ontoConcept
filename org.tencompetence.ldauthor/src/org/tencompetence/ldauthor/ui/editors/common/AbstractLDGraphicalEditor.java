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
package org.tencompetence.ldauthor.ui.editors.common;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObjectContainer;
import org.tencompetence.ldauthor.ui.editors.ILDGraphicalEditorPart;
import org.tencompetence.ldauthor.ui.editors.common.actions.DefaultEditPartSizeAction;
import org.tencompetence.ldauthor.ui.editors.common.actions.PreviewAction;
import org.tencompetence.ldauthor.ui.editors.common.actions.PropertiesAction;
import org.tencompetence.ldauthor.ui.editors.common.commands.CreateObjectCommand;


/**
 * Abstract Graphical standalone Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractLDGraphicalEditor.java,v 1.33 2010/02/01 10:45:09 phillipus Exp $
 */
public abstract class AbstractLDGraphicalEditor
extends GraphicalEditorWithFlyoutPalette
implements ILDGraphicalEditorPart {
    
    /**
     * Dirty flag
     */
    protected boolean isDirty;
    
    /**
     * The Domain Model
     */
    private IGraphicalModelObjectContainer fModel;
    
    /**
     * Default Constructor
     */
    protected AbstractLDGraphicalEditor() {
    }
    
    @Override
    public DefaultEditDomain getEditDomain() {
        return super.getEditDomain();
    }
    
    @Override
    public ActionRegistry getActionRegistry() {
        return super.getActionRegistry();
    }
    
    @Override
    public GraphicalViewer getGraphicalViewer() {
        return super.getGraphicalViewer();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#commandStackChanged(java.util.EventObject)
     */
    @Override
    public void commandStackChanged(EventObject event) {
        super.commandStackChanged(event);
        
        //setDirty(getCommandStack().isDirty());
        // We have to set it to dirty whatever happens since a Property Editor may have made a change outside
        // of this Command Stack
        setDirty(true);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#createPaletteViewerProvider()
     */
    @Override
    protected PaletteViewerProvider createPaletteViewerProvider() {
        return new PaletteViewerProvider(getEditDomain()) {
            @Override
            protected void configurePaletteViewer(PaletteViewer viewer) {
                super.configurePaletteViewer(viewer);
                // create a drag source listener for the palette viewer
                // together with an appropriate transfer drop target listener, this will enable
                // model element creation by dragging a CombinatedTemplateCreationEntries 
                // from the palette into the editor
                // @see ShapesEditor#createTransferDropTargetListener()
                viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
            }
        };
    }
    
    /**
     * Set up and register the context menu
     */
    protected void registerContextMenu() {
        GraphicalViewer viewer = getGraphicalViewer();
        LDGraphicalEditorContextMenuProvider provider = new LDGraphicalEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        getSite().registerContextMenu(LDGraphicalEditorContextMenuProvider.ID, provider, viewer);
    }
    
    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();

        GraphicalViewer viewer = getGraphicalViewer();
        
        /*
         * We'll have a Zoom Manager 
         */
        ScalableFreeformRootEditPart rootPart = new ScalableFreeformRootEditPart();
        viewer.setRootEditPart(rootPart);
        
        // Key handler
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        
        // create a drop target listener for this palette viewer
        // this will enable model element creation by dragging a CombinatedTemplateCreationEntries 
        // from the palette into the editor
        viewer.addDropTargetListener(getTransferDropTargetListener());
        
        // Context menu
        registerContextMenu();
        
        // Other Actions
        createAdditionalActions();
        
        // Register Part Factory before setting contents
        viewer.setEditPartFactory(getEditPartFactory());
        
        // Set Contents
        if(getModel() != null) {
            viewer.setContents(getModel());
        }
    }
    
    /**
     * @return The Drop target listener to be used
     */
    protected abstract TransferDropTargetListener getTransferDropTargetListener();
    
    /**
     * @return The EditPartFactory to use
     */
    protected abstract EditPartFactory getEditPartFactory();
    
    public IGraphicalModelObjectContainer getModel() {
        return fModel;
    }

    public void setModel(IGraphicalModelObjectContainer model) {
        fModel = model;
    }

    public void addModelObject(IGraphicalModelObject model) {
        /*
         * Add to the Host's Command Stack and execute the Command
         */
        Command command = new CreateObjectCommand(getModel(), model);
        getCommandStack().execute(command);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPalettePreferences()
     */
    @Override
    protected FlyoutPreferences getPalettePreferences() {
        return new LDGraphicalEditorFlyoutPreferences();
    }
    
    public void selectEditPart(ILDModelObject object) {
        if(object == null) {
            return;
        }
        IGraphicalModelObjectContainer model = getModel();
        IGraphicalModelObject gfxObject = model.getGraphicalLDChild(object);
        if(gfxObject != null) {
            getGraphicalViewer().getControl().forceFocus();
            Object editpart = getGraphicalViewer().getEditPartRegistry().get(gfxObject);
            if(editpart instanceof EditPart) {
                //Force a layout first.
                getGraphicalViewer().flush();
                getGraphicalViewer().select((EditPart)editpart);
            }
        }
    }
    
    /* (non-Javadoc)
     * @see org.tencompetence.ldauthor.ui.editors.ILDGraphicalEditor#setDirty(boolean)
     */
    public void setDirty(boolean dirty) {
        if(isDirty != dirty) {
            isDirty = dirty;
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }
    
    /**
     * @see org.eclipse.ui.ISaveablePart#doSaveAs()
     */
    @Override
    public void doSaveAs() {
    }

    /**
     * @see org.eclipse.ui.ISaveablePart#isDirty()
     */
    @Override
    public boolean isDirty() {
        return isDirty;
    }

    /**
     * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
     */
    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    public IAction getGlobalActionHandler(String actionId) {
        // Return null for this type of GEF type Editor, since GEF has its own way of dealing with Actions
        return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected void createActions() {
        super.createActions();
        
        ActionRegistry registry = getActionRegistry();
        IAction action;
        
        action = new MatchWidthAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
        action = new MatchHeightAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.LEFT);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.RIGHT);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.TOP);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.BOTTOM);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.CENTER);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.MIDDLE);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
        action = new DefaultEditPartSizeAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
        action = new PropertiesAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
        action = new PreviewAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
    }
    
    /**
     * Add some extra Actions - after the graphical viewer has been created
     */
    protected void createAdditionalActions() {
        ZoomManager zoomManager = (ZoomManager)getAdapter(ZoomManager.class);
        
        List<String> zoomLevels = new ArrayList<String>(3);
        zoomLevels.add(ZoomManager.FIT_ALL);
        zoomLevels.add(ZoomManager.FIT_WIDTH);
        zoomLevels.add(ZoomManager.FIT_HEIGHT);
        zoomManager.setZoomLevelContributions(zoomLevels);
        
        IAction zoomIn = new ZoomInAction(zoomManager);
        IAction zoomOut = new ZoomOutAction(zoomManager);
        getActionRegistry().registerAction(zoomIn);
        getActionRegistry().registerAction(zoomOut);
        
        // Add these zoom actions to key binding service
        
        // This is deprecated...
        //getSite().getKeyBindingService().registerAction(zoomIn);
        //getSite().getKeyBindingService().registerAction(zoomOut);
        
        // So do this...
        
        IHandlerService service = (IHandlerService)getEditorSite().getService(IHandlerService.class);
        service.activateHandler(zoomIn.getActionDefinitionId(), new ActionHandler(zoomIn));
        service.activateHandler(zoomOut.getActionDefinitionId(), new ActionHandler(zoomOut));
        
        // Show Grid Action
        IAction showGrid = new ToggleGridAction(getGraphicalViewer());
        getActionRegistry().registerAction(showGrid);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        /*
         * Return the Zoom Manager
         */
        if(adapter == ZoomManager.class) {
            return getGraphicalViewer().getProperty(ZoomManager.class.toString());
        }

        /*
         * Return the Outline Page
         */
        if(adapter == IContentOutlinePage.class) {
            return new OverviewOutlinePage();
        }

        /*
         * Return the Model
         */
        if(adapter == IGraphicalModelObject.class) {
            return getModel();
        }
        
        return super.getAdapter(adapter);
    }
}
