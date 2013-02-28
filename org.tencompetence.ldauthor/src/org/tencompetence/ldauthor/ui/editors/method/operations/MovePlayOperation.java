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
package org.tencompetence.ldauthor.ui.editors.method.operations;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.method.IPlayModel;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;
import org.tencompetence.ldauthor.ui.editors.ILDEditorPart;
import org.tencompetence.ldauthor.ui.editors.method.PlayWidget;
import org.tencompetence.ldauthor.ui.editors.method.PlayWidgetsHandler;


/**
 * MovePlayOperation
 * 
 * @author Phillip Beauvoir
 * @version $Id: MovePlayOperation.java,v 1.8 2009/05/22 16:35:05 phillipus Exp $
 */
public class MovePlayOperation extends AbstractOperation {
    
    private PlayWidgetsHandler fHandler;
    private ILDEditorPart fEditor;
    private IPlayModel fPlay;
    
    private int fOffset;
    
    public MovePlayOperation(PlayWidgetsHandler handler, IPlayModel play, int offset) {
        super(Messages.MovePlayOperation_0 + " " + LDModelUtils.getUserObjectName(LDModelFactory.PLAY)); //$NON-NLS-1$
        
        fHandler = handler;
        fEditor = handler.getEditor();
        fPlay = play;
        fOffset = offset;
        
        addContext((IUndoContext)fEditor.getAdapter(IUndoContext.class));
    }
    
    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        return movePlay(fOffset);
    }

    @Override
    public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        return execute(monitor, info);
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        return movePlay(-fOffset);
    }
    
    private IStatus movePlay(int offset) {
        List<ILDModelObject> plays = fPlay.getLDModel().getMethodModel().getPlaysModel().getChildren();
        
        int newIndex = plays.indexOf(fPlay) + offset;
        
        fPlay.getLDModel().getMethodModel().getPlaysModel().removeChild(fPlay);
        fPlay.getLDModel().getMethodModel().getPlaysModel().addChildAt(fPlay, newIndex);
        
        // Should be a 1 to 1 mapping of LD Plays to Plays widgets, use a brute force approach...
        for(int i = 0; i < plays.size(); i++) {
            ILDModelObject play = plays.get(i);
            PlayWidget playWidget = (PlayWidget)fHandler.getComposite().getChildren()[i];
            if(play != playWidget.getPlay()) {
                playWidget.setPlay((IPlayModel)play);
            }
        }

        fHandler.layout();
        
        fPlay.getLDModel().setDirty();
        
        return Status.OK_STATUS;
    }
}
