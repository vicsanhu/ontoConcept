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

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;
import org.tencompetence.ldauthor.ui.editors.common.actions.DefaultEditPartSizeAction;
import org.tencompetence.ldauthor.ui.editors.common.actions.PreviewAction;
import org.tencompetence.ldauthor.ui.editors.common.actions.PropertiesAction;

/**
 * Provides a context menu.
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDGraphicalEditorContextMenuProvider.java,v 1.6 2008/11/22 11:52:33 phillipus Exp $
 */
public class LDGraphicalEditorContextMenuProvider extends ContextMenuProvider {

    public static final String ID = "LDEditorContextMenuProvider"; //$NON-NLS-1$
    
    private ActionRegistry actionRegistry;

    /**
     * Creates a new FlowContextMenuProvider assoicated with the given viewer
     * and action registry.
     * 
     * @param viewer the viewer
     * @param registry the action registry
     */
    public LDGraphicalEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer);
        setActionRegistry(registry);
    }

    /**
     * @see ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    public void buildContextMenu(IMenuManager menu) {
        GEFActionConstants.addStandardActionGroups(menu);

        IAction action;
        action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
        menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

        action = getActionRegistry().getAction(ActionFactory.REDO.getId());
        menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

        action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
        if(action.isEnabled()) {
            menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
        }
        
        menu.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
        menu.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));
        
        menu.add(new Separator());
        
        menu.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_LEFT));
        menu.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_CENTER));
        menu.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_RIGHT));
        
        menu.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_TOP));
        menu.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_MIDDLE));
        menu.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_BOTTOM));
        
        menu.add(new Separator());

        menu.add(getActionRegistry().getAction(GEFActionConstants.MATCH_WIDTH));
        menu.add(getActionRegistry().getAction(GEFActionConstants.MATCH_HEIGHT));
        
        action = getActionRegistry().getAction(DefaultEditPartSizeAction.ID);
        if(action.isEnabled()) {
            menu.add(action);
        }
        
        menu.add(new Separator());
        
        action = getActionRegistry().getAction(PropertiesAction.ID);
        menu.add(action);
        
        action = getActionRegistry().getAction(PreviewAction.ID);
        menu.add(action);
    }

    private ActionRegistry getActionRegistry() {
        return actionRegistry;
    }

    /**
     * Sets the action registry
     * 
     * @param registry the action registry
     */
    public void setActionRegistry(ActionRegistry registry) {
        actionRegistry = registry;
    }

}