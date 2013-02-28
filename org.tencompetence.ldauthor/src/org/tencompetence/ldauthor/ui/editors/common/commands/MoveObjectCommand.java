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
package org.tencompetence.ldauthor.ui.editors.common.commands;

import org.eclipse.gef.commands.Command;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObjectContainer;

/**
 * Command for moving an Object
 * 
 * @author Phillip Beauvoir
 * @version $Id: MoveObjectCommand.java,v 1.10 2008/04/24 10:15:26 phillipus Exp $
 */
public class MoveObjectCommand extends Command {

    private IGraphicalModelObjectContainer fParent;
    private IGraphicalModelObject fObject;
    private int fOldIndex, fNewIndex;
    
    /**
     * @param parent
     * @param object
     * @param index The new index to move the child to
     */
    public MoveObjectCommand(IGraphicalModelObjectContainer parent, IGraphicalModelObject object, int index) {
        if(parent == null || object == null) {
            throw new IllegalArgumentException("Arguments cannot be null"); //$NON-NLS-1$
        }
        if(index < 0 || index > parent.getChildren().size() - 1) {
            throw new IllegalArgumentException("Illegal index position: " + index); //$NON-NLS-1$
        }
        
        fParent = parent;
        fObject = object;
        fOldIndex = fParent.getChildren().indexOf(fObject);
        fNewIndex = index;
    }

    @Override
    public String getLabel() {
        return Messages.MoveObjectCommand_0 + fObject.getName();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        fParent.moveChild(fObject, fNewIndex, true);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.gef.commands.Command#redo()
     */
    @Override
    public void redo() {
        execute();
    }
    
    @Override
    public void undo() {
        // Add the Child at old index position
        fParent.moveChild(fObject, fOldIndex, true);
    }
}