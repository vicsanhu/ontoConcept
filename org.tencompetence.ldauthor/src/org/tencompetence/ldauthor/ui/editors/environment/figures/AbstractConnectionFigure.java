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
package org.tencompetence.ldauthor.ui.editors.environment.figures;

import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;
import org.tencompetence.ldauthor.ui.editors.common.directedit.EditableLabel;

/**
 * Abstract implementation of a connection figure.  Subclasses should
 * decide how to draw the line
 * 
 * @author Paul Sharples
 * @version $Id: AbstractConnectionFigure.java,v 1.1 2008/11/20 13:46:36 phillipus Exp $
 */
public abstract class AbstractConnectionFigure
extends PolylineConnection {

    private EditableLabel fRelationshipLabel;
    
	public AbstractConnectionFigure(){
		setFigureProperties();
	}
	
	protected void setFigureProperties(){
		setTargetDecoration(new PolygonDecoration()); // arrow at target endpoint
	}
	
    /**
     * Place the relationship text on the connection
     */
    public void setRelationshipText(String text){
        getRelationshipLabel().setText(text);
    }

    /**
     * @param copy
     * @return True if the user clicked on the Relationship edit label
     */
    public boolean didClickRelationshipLabel(Point requestLoc) {
        EditableLabel label = getRelationshipLabel();
        label.translateToRelative(requestLoc);
        return label.containsPoint(requestLoc);
    }

    public EditableLabel getRelationshipLabel() {
        if(fRelationshipLabel == null) {
            ConnectionEndpointLocator locator = new ConnectionEndpointLocator(this, true);
            fRelationshipLabel = new EditableLabel(""); //$NON-NLS-1$
            Font font = JFaceResources.getDefaultFont();
            fRelationshipLabel.setFont(font);
            add(fRelationshipLabel, locator);
        }
        return fRelationshipLabel;
    }

}
