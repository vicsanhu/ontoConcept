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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;

/**
 * Command for moving an Object
 * 
 * @author Phillip Beauvoir
 * @version $Id: SetConstraintObjectCommand.java,v 1.10 2008/04/24 10:15:25 phillipus Exp $
 */
public class SetConstraintObjectCommand extends Command {

    private IGraphicalModelObject fObject;
    private Rectangle fNewPos, fOldPos;
    private ChangeBoundsRequest fRequest;
    
    public SetConstraintObjectCommand(IGraphicalModelObject object, ChangeBoundsRequest req, Rectangle bounds) {
        fObject = object;
        fRequest = req;
        fNewPos = bounds;
    }

    @Override
    public String getLabel() {
        return Messages.SetConstraintObjectCommand_0 + fObject.getName();
    }

    @Override
    public void execute() {
        fOldPos = fObject.getBounds();
        redo();
    }

    @Override
    public void undo() {
        fObject.setBounds(fOldPos);
    }

    @Override
    public void redo() {
        fObject.setBounds(fNewPos);
    }
    
    @Override
    public boolean canExecute() {
        Object type = fRequest.getType();
        // make sure the Request is of a type we support:
        return (RequestConstants.REQ_MOVE.equals(type)
                || RequestConstants.REQ_ALIGN_CHILDREN.equals(type)
                || RequestConstants.REQ_MOVE_CHILDREN.equals(type) 
                || RequestConstants.REQ_RESIZE.equals(type)
                || RequestConstants.REQ_RESIZE_CHILDREN.equals(type));
    }
}