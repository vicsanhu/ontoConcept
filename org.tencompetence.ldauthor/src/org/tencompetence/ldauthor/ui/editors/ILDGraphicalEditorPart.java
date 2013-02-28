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
package org.tencompetence.ldauthor.ui.editors;

import org.eclipse.gef.GraphicalViewer;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObjectContainer;


/**
 * Graphical Editor interface
 * 
 * @author Phillip Beauvoir
 * @version $Id: ILDGraphicalEditorPart.java,v 1.6 2009/05/19 18:21:09 phillipus Exp $
 */
public interface ILDGraphicalEditorPart
extends ILDEditorPart {
    
    /**
     * @return The GraphicalViewer
     */
    GraphicalViewer getGraphicalViewer();

    /**
     * Add a new Model object to the top-level domain object and show in the Editor
     * @param model
     */
    void addModelObject(IGraphicalModelObject model);

    /**
     * @return The top-level domain model being edited
     */
    IGraphicalModelObjectContainer getModel();

    /**
     * Set the top-level domain model being edited
     * 
     * @param model
     */
    void setModel(IGraphicalModelObjectContainer model);
    
    /**
     * Select an Edit Part given an LD Object
     * @param object
     */
    void selectEditPart(ILDModelObject object);
}
