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
package org.tencompetence.ldauthor.ui.editors.common.actions;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.tencompetence.ldauthor.ui.ImageFactory;
import org.tencompetence.ldauthor.ui.editors.common.commands.SetConstraintObjectCommand;
import org.tencompetence.ldauthor.ui.editors.environment.figures.IDefaultSizedFigure;

/**
 * Action to set the size of an Edit Part to default.
 * 
 * This is based on the org.eclipse.gef.ui.actions.MatchSizeAction
 * 
 * @author Phillip Beauvoir
 * @version $Id: DefaultEditPartSizeAction.java,v 1.5 2008/11/20 13:46:36 phillipus Exp $
 */
public class DefaultEditPartSizeAction extends SelectionAction {

    public static final String ID = "org.tencompetence.ldauthor.defaultEditPartSizeAction"; //$NON-NLS-1$

    public DefaultEditPartSizeAction(IWorkbenchPart part) {
        super(part);
        setText(Messages.DefaultEditPartSizeAction_0);
        setId(ID);
        setToolTipText(Messages.DefaultEditPartSizeAction_1);
        setImageDescriptor(ImageFactory.getImageDescriptor(ImageFactory.ICON_DEFAULT_SIZE));
    }

    @Override
    public void run() {
        execute(createDefaultSizeCommand(getSelectedObjects()));
    }

    @Override
    protected boolean calculateEnabled() {
        Command command = createDefaultSizeCommand(getSelectedObjects());
        if(command == null) {
            return false;
        }
        return command.canExecute();
    }

    private Command createDefaultSizeCommand(List<?> objects) {
        if(objects.isEmpty()) {
            return null;
        }
        
        for(Object object : objects) {
            if(!(object instanceof GraphicalEditPart)) {
                return null;
            }
            
            /*
             * Hack - the EditPart may have been removed programatically on another page.
             *        This means that the Editpart's parent is null which will throw a NPE
             */
            if(((EditPart)object).getParent() == null) {
                return null;
            }
        }

        CompoundCommand command = new CompoundCommand();
        
        for(Object object : objects) {
            GraphicalEditPart part = (GraphicalEditPart)object;

            ChangeBoundsRequest request = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE);
            
            Dimension preferredSize = part.getFigure().getPreferredSize().getCopy();
            
            // If we can change the default size then do so...
            if(part.getFigure() instanceof IDefaultSizedFigure) {
                Dimension optimalSize = ((IDefaultSizedFigure)part.getFigure()).getDefaultSize().getCopy();
                //if(preferredSize.width > optimalSize.width) { // Not too wide!
                    preferredSize.width = optimalSize.width; 
                    preferredSize.height = optimalSize.height; 
                //}
            }
            
            Dimension existingSize = part.getFigure().getSize();
            request.setSizeDelta(preferredSize.getDifference(existingSize));
            
            Command cmd = part.getCommand(request);
            if(cmd instanceof SetConstraintObjectCommand) {
                command.add(cmd);
            }
        }

        return command;
    }
}
