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
package org.tencompetence.ldauthor.graphicsmodel.impl;

import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicsObject;



/**
 * Model class that acts as an empty container used for empty editors
 * 
 * @author Phillip Beauvoir
 * @version $Id: GraphicalEmptyModel.java,v 1.14 2009/05/19 18:21:05 phillipus Exp $
 */
public class GraphicalEmptyModel
extends AbstractGraphicalModelObjectContainer {

    public static final String DEFAULT_NAME = "Empty"; //$NON-NLS-1$
    
    public GraphicalEmptyModel(ILDModel ldModel) {
        super(ldModel);
    }
	
    @Override
    protected void setPropertyDescriptors() {
        // none
    }
    
    public String getTagName() {
        return "empty-model"; //$NON-NLS-1$
    }

    public void reconcile() {
    }

    public String getName() {
        return DEFAULT_NAME;
    }

    public void setName(String name) {
    }

    @Override
    protected IGraphicsObject createChildObject(String name) {
        return null;
    }

}
