/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.tencompetence.ldauthor.ui.editors.environment.policies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.swt.graphics.Color;

/**
 */
/**
 * Taken from the GEF Logic Example.
 * 
 * Highlights an Edit Part when you hover a mouse over it
 * 
 * @author Unknown
 * @version $Id: ContainerHighlightEditPolicy.java,v 1.1 2007/10/13 21:18:10 phillipus Exp $
 */
public class ContainerHighlightEditPolicy extends GraphicalEditPolicy {

    public final static Color BACKGROUND_COLOR = new Color(null, 200, 200, 240);

    private Color revertColor;

    @Override
    public void eraseTargetFeedback(Request request) {
        if(revertColor != null) {
            setContainerBackground(revertColor);
            revertColor = null;
        }
    }

    private Color getContainerBackground() {
        return getContainerFigure().getBackgroundColor();
    }

    private IFigure getContainerFigure() {
        return ((GraphicalEditPart)getHost()).getFigure();
    }

    @Override
    public EditPart getTargetEditPart(Request request) {
        if(request == null || request.getType() == null) {
            return null;
        }
        
        return request.getType().equals(RequestConstants.REQ_SELECTION_HOVER) ? getHost() : null;
    }

    private void setContainerBackground(Color c) {
        getContainerFigure().setBackgroundColor(c);
    }

    protected void showHighlight() {
        if(revertColor == null) {
            revertColor = getContainerBackground();
            setContainerBackground(BACKGROUND_COLOR);
        }
    }

    @Override
    public void showTargetFeedback(Request request) {
        if(request.getType().equals(RequestConstants.REQ_MOVE) || request.getType().equals(RequestConstants.REQ_ADD)
                || request.getType().equals(RequestConstants.REQ_CLONE) || request.getType().equals(RequestConstants.REQ_CONNECTION_START)
                || request.getType().equals(RequestConstants.REQ_CONNECTION_END) || request.getType().equals(RequestConstants.REQ_CREATE))
            showHighlight();
    }

}
