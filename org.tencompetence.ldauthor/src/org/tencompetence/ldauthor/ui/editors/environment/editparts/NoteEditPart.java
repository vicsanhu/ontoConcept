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
package org.tencompetence.ldauthor.ui.editors.environment.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.ldauthor.graphicsmodel.other.IGraphicalNoteModel;
import org.tencompetence.ldauthor.ui.editors.common.directedit.MultiLineCellEditor;
import org.tencompetence.ldauthor.ui.editors.common.policies.PartEditPolicy;
import org.tencompetence.ldauthor.ui.editors.environment.figures.NoteFigure;

/**
 * Note Edit Part
 * 
 * @author Phillip Beauvoir
 * @version $Id: NoteEditPart.java,v 1.1 2008/11/20 13:46:36 phillipus Exp $
 */
public class NoteEditPart extends AbstractLDEditPart {

    private DirectEditManager fDirectManager;

    @Override
    protected void createEditPolicies() {
        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new NoteDirectEditPolicy());
    }

    @Override
    protected IFigure createFigure() {
        NoteFigure figure = new NoteFigure();
        return figure;
    }

    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        ((NoteFigure)figure).setText(((IGraphicalNoteModel)getModel()).getContent());
    }

    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_OPEN) {
            performDirectEdit();
        }
    }

    private void performDirectEdit() {
        if(fDirectManager == null) {
            fDirectManager = new NoteDirectEditManager();
        }
        fDirectManager.show();
    }

    /**
     * DirectEditManager
     */
    private class NoteDirectEditManager extends DirectEditManager {

        public NoteDirectEditManager() {
            super(NoteEditPart.this, MultiLineCellEditor.class, new NoteCellEditorLocator());
        }

        @Override
        protected void initCellEditor() {
            getCellEditor().setValue(((IGraphicalNoteModel)getModel()).getContent());
            Text text = (Text)getCellEditor().getControl();
            text.selectAll();
        }
    }

    /**
     * CellEditorLocator
     */
    private class NoteCellEditorLocator implements CellEditorLocator {

        public void relocate(CellEditor celleditor) {
            IFigure figure = getFigure();
            Text text = (Text)celleditor.getControl();
            Rectangle rect = figure.getBounds().getCopy();
            figure.translateToAbsolute(rect);
            text.setBounds(rect.x + 5, rect.y + 5, rect.width - 5, rect.height - 5);
        }
    }

    /**
     * DirectEditCommand
     */
    private class DirectEditCommand extends Command {

        private String oldName;
        private String newName;

        @Override
        public void execute() {
            IGraphicalNoteModel model = (IGraphicalNoteModel)getModel();
            oldName = model.getContent();
            model.setContent(newName);
        }
        
        @Override
        public String getLabel() {
            return Messages.NoteEditPart_0;
        }

        public void setName(String name) {
            newName = name;
        }

        @Override
        public void undo() {
            IGraphicalNoteModel model = (IGraphicalNoteModel)getModel();
            model.setContent(oldName);
        }
    }

    /**
     * DirectEditPolicy
     */
    private class NoteDirectEditPolicy extends DirectEditPolicy {

        @Override
        protected Command getDirectEditCommand(DirectEditRequest request) {
            DirectEditCommand command = new DirectEditCommand();
            command.setName((String)request.getCellEditor().getValue());
            return command;
        }

        @Override
        protected void showCurrentEditValue(DirectEditRequest request) {
        }
    }
}
