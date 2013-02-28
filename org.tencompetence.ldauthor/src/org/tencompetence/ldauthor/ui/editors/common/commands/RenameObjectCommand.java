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

/**
 * Command for renaming an Object directly in the EditPart
 * 
 * @author Phillip Beauvoir
 * @version $Id: RenameObjectCommand.java,v 1.7 2008/04/24 10:15:26 phillipus Exp $
 */
public class RenameObjectCommand extends Command {

    private IGraphicalModelObject fObject;

    private String fNewName, fOldName;
    
    public RenameObjectCommand(IGraphicalModelObject object, String name) {
        fObject = object;
        fNewName = name;
    }

    @Override
    public String getLabel() {
        return Messages.RenameObjectCommand_0 + fNewName;
    }

    @Override
    public void execute() {
        fOldName = fObject.getName();
        fObject.setName(fNewName);
    }

    @Override
    public void undo() {
        fObject.setName(fOldName);
    }

    @Override
    public void redo() {
        fObject.setName(fNewName);
    }
}