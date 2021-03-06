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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.tencompetence.ldauthor.graphicsmodel.impl.GraphicalEmptyModel;
import org.tencompetence.ldauthor.graphicsmodel.other.IGraphicalNoteModel;

/**
 * Factory for creating common Edit Parts
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractLDEditPartFactory.java,v 1.1 2008/11/20 13:46:36 phillipus Exp $
 */
public abstract class AbstractLDEditPartFactory
implements EditPartFactory {

    /* (non-Javadoc)
     * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
     */
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart child = null;
        
        if(model == null) {
            return null;
        }
        
        // Note
        else if(model instanceof IGraphicalNoteModel) {
            child = new NoteEditPart();
        }
        
        // Empty Model
        else if(model instanceof GraphicalEmptyModel) {
            child = new FreeFormDiagramPart();
        }
        
        return child;
    }
    
    /**
     * Add the model to the EditPart
     * @param child
     * @param model
     */
    protected void setModel(EditPart child, Object model) {
        if(child != null) {
            child.setModel(model);
        }
        else {
            System.out.println("Requested a part for " + model + " but was null."); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

}
