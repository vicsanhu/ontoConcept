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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.tencompetence.imsldmodel.environments.ILearningObjectModel;
import org.tencompetence.ldauthor.graphicsmodel.IGraphicalModelObject;
import org.tencompetence.ldauthor.graphicsmodel.ILDModelObjectOwner;
import org.tencompetence.ldauthor.ui.ImageFactory;

/**
 * Figure for a Learning Object
 * 
 * @author Phillip Beauvoir
 * @version $Id: LearningObjectFigure.java,v 1.16 2009/05/19 18:21:05 phillipus Exp $
 */
public class LearningObjectFigure
extends AbstractIconTypeFigure implements IEnvironmentChildFigure {
    
    public static Color bgcolor = new Color(null, 81, 207, 105);

    private int fType;
    
    public LearningObjectFigure(){
    }

    public Image getDefaultImage() {
        switch(fType) {
            case ILearningObjectModel.TYPE_KNOWLEDGE_OBJECT:
                return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_32);
            case ILearningObjectModel.TYPE_TEST_OBJECT:
                return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_TEST_OBJECT_32);
            case ILearningObjectModel.TYPE_TOOL_OBJECT:
                return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_TOOL_OBJECT_32);
            default:
                return ImageFactory.getImage(ImageFactory.IMAGE_LEARNING_OBJECT_32);
        }
    }
    
    @Override
    public void refreshVisuals(IGraphicalModelObject object) {
        if(object != null) {
            ILearningObjectModel lo = (ILearningObjectModel)((ILDModelObjectOwner)object).getLDModelObject();
            fType = lo.getType();
            setImage(getDefaultImage());
        }
        
        super.refreshVisuals(object);
    }
    
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        return getDefaultSize();
    }

    public Dimension getDefaultSize() {
        return new Dimension(Math.max(getTextLabel().getPreferredSize().width, DEFAULT_WIDTH), DEFAULT_HEIGHT);
    }
}