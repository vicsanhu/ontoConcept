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
package org.tencompetence.ldauthor.ui.editors.ld;

import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.common.LDGraphicalEditorActionBarContributor;


/**
 * Action Bar Contributor for MultiPage
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDMultiPageEditorActionBarContributor.java,v 1.14 2009/01/19 11:54:18 phillipus Exp $
 */
public class LDMultiPageEditorActionBarContributor
extends MultiPageEditorActionBarContributor
{
    /*
     * List of Global Actions that a (non-GEF) Editor may wish to register interest in
     */
    private final static String[] ACTIONS = {
        ActionFactory.UNDO.getId(),
        ActionFactory.REDO.getId(),
        ActionFactory.DELETE.getId(),
        ActionFactory.RENAME.getId(),
        ActionFactory.PROPERTIES.getId(),
    };

    private IEditorPart fActiveEditor;
    
    /**
     * Delegate to the GEF Action Handler
     */
    public LDGraphicalEditorActionBarContributor fGEFContributor = new LDGraphicalEditorActionBarContributor();
    
    @Override
    public void setActivePage(IEditorPart activeEditor) {
        if(activeEditor == fActiveEditor) {
            return;
        }
        
        getActionBars().clearGlobalActionHandlers();

        /*
         * If it's a GEF Editor then delegate to the GEF Action Handler 
         */
        if(activeEditor instanceof GraphicalEditor) {
            fGEFContributor.setActiveEditor(activeEditor);
        }
        /*
         * Else update the Global Actions
         */
        else {
            setEditorPartActions((ILDEditorPart)activeEditor);
            fGEFContributor.setActiveEditor(null);
        }
        
        getActionBars().updateActionBars();
        
        fActiveEditor = activeEditor;
    }
    
    @Override
    public void init(IActionBars actionBars, IWorkbenchPage page) {
        super.init(actionBars, page);
        
        // Pass on to delegate
        fGEFContributor.init(actionBars, page);
    }
    
    /**
     * Set the Actions
     * @param part
     */
    private void setEditorPartActions(ILDEditorPart part) {
        IActionBars actionBars = getActionBars();
        
        // Ask the given part if it wants to contribute an Action Handler 
        for(int i= 0; i < ACTIONS.length; i++) {
            actionBars.setGlobalActionHandler(ACTIONS[i], part.getGlobalActionHandler(ACTIONS[i]));
        }
    }

    @Override
    public void dispose() {
        fGEFContributor.dispose();
        fGEFContributor = null;
    }
}
