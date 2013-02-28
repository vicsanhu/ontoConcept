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
package org.tencompetence.ldauthor.ui.editors.environment.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObjectContainer;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;

/**
 * Orphan Environment Child Command
 * 
 * @author Phillip Beauvoir
 * @version $Id: OrphanEnvironmentChildCommand.java,v 1.5 2008/04/24 10:15:30 phillipus Exp $
 */
public class OrphanEnvironmentChildCommand extends Command {

    private Rectangle fOldBounds;

    private IGraphicalModelObjectContainer fParent;

    private IGraphicalModelObject fChild;

    private int fIndex;

    public OrphanEnvironmentChildCommand() {
        super(Messages.OrphanEnvironmentChildCommand_0);
    }

    @Override
    public void execute() {
        fOldBounds = fChild.getBounds();
        fIndex = fParent.getChildren().indexOf(fChild); // Ensure this is stored just before execute
        fParent.removeChild(fChild, true);
    }

    @Override
    public void redo() {
        fParent.removeChild(fChild, true);
    }

    @Override
    public void undo() {
        fChild.setBounds(fOldBounds);
        fParent.addChildAt(fChild, fIndex, true);
    }

    public void setChild(IGraphicalModelObject child) {
        fChild = child;
    }

    public void setParent(IGraphicalModelObjectContainer parent) {
        fParent = parent;
    }

}
