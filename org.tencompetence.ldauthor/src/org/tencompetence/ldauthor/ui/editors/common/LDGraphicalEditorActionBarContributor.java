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

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightRetargetAction;
import org.eclipse.gef.ui.actions.MatchWidthRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.tencompetence.ldauthor.ui.editors.common.actions.DefaultEditPartSizeRetargetAction;
import org.tencompetence.ldauthor.ui.editors.common.actions.PropertiesRetargetAction;

/**
 * Manages the installation/deinstallation of global actions for the editor.
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDGraphicalEditorActionBarContributor.java,v 1.7 2009/01/19 11:53:57 phillipus Exp $
 */
public class LDGraphicalEditorActionBarContributor
extends ActionBarContributor
{
    //private ZoomComboContributionItem fZoomCombo;
    
	/**
	 * Constructor
	 */
	public LDGraphicalEditorActionBarContributor() {
	}
	
//    @Override
//    public void contributeToToolBar(IToolBarManager toolBarManager) {
//        // Add the Zoom Manager Combo
//        fZoomCombo = new ZoomComboContributionItem(getPage());
//        toolBarManager.add(fZoomCombo);
//        
//        toolBarManager.add(new Separator());
//        toolBarManager.add(getAction(GEFActionConstants.ALIGN_LEFT));
//        toolBarManager.add(getAction(GEFActionConstants.ALIGN_CENTER));
//        toolBarManager.add(getAction(GEFActionConstants.ALIGN_RIGHT));
//        toolBarManager.add(new Separator());
//        toolBarManager.add(getAction(GEFActionConstants.ALIGN_TOP));
//        toolBarManager.add(getAction(GEFActionConstants.ALIGN_MIDDLE));
//        toolBarManager.add(getAction(GEFActionConstants.ALIGN_BOTTOM));
//        toolBarManager.add(new Separator());   
//        toolBarManager.add(getAction(GEFActionConstants.MATCH_WIDTH));
//        toolBarManager.add(getAction(GEFActionConstants.MATCH_HEIGHT));
//        toolBarManager.add(new Separator());
//        toolBarManager.add(getAction(DefaultEditPartSizeRetargetAction.ID));
//    }
    
    @Override
    protected void buildActions() {
        // Not sure whether to do it this way or just by addGlobalActionKey(id);
        addRetargetAction(new DeleteRetargetAction());
        addRetargetAction(new UndoRetargetAction());
        addRetargetAction(new RedoRetargetAction());
        
        addRetargetAction(new ZoomInRetargetAction());
        addRetargetAction(new ZoomOutRetargetAction());
        
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.MIDDLE));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));
        
        addRetargetAction(new MatchWidthRetargetAction());
        addRetargetAction(new MatchHeightRetargetAction());

        addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY, 
                Messages.LDGraphicalEditorActionBarContributor_0, IAction.AS_CHECK_BOX));
        
        addRetargetAction(new DefaultEditPartSizeRetargetAction());
        
        addRetargetAction(new PropertiesRetargetAction());
    }
    
    @Override
    public void contributeToMenu(IMenuManager menuManager) {
        MenuManager viewMenu = new MenuManager(Messages.LDGraphicalEditorActionBarContributor_1);
        
        viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
        viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
        viewMenu.add(new Separator());
        viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
        viewMenu.add(new Separator());
        viewMenu.add(getAction(GEFActionConstants.MATCH_WIDTH));
        viewMenu.add(getAction(GEFActionConstants.MATCH_HEIGHT));
        viewMenu.add(new Separator());
        viewMenu.add(getAction(DefaultEditPartSizeRetargetAction.ID));
        
        menuManager.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
    }

    @Override
    protected void declareGlobalActionKeys() {
        addGlobalActionKey(ActionFactory.SELECT_ALL.getId());
        addGlobalActionKey(ActionFactory.PRINT.getId());
    }

    @Override
    public void setActiveEditor(IEditorPart editor) {
        if(editor instanceof GraphicalEditor) {
            // Activate the zoom manager
            super.setActiveEditor(editor);
            // fZoomCombo.setZoomManager((ZoomManager)editor.getAdapter(ZoomManager.class));
        }
        else {
            // De-activate the zoom manager
            // fZoomCombo.setZoomManager(null);
        }
    }
}
