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
package org.tencompetence.ldauthor.graphicsmodel;

import org.eclipse.draw2d.geometry.Rectangle;


/**
 * Core Model Object with graphical properties for the Editor
 * 
 * @author Phillip Beauvoir
 * @version $Id: IGraphicalModelObject.java,v 1.12 2008/04/24 10:15:21 phillipus Exp $
 */
public interface IGraphicalModelObject
extends IGraphicsObject
{
    /*
     * Properties
     */
    String PROPERTY_NAME = "Property.name"; //$NON-NLS-1$
    String PROPERTY_BOUNDS = "Property.bounds"; //$NON-NLS-1$
    String PROPERTY_XPOS = "Property.xpos"; //$NON-NLS-1$
    String PROPERTY_YPOS = "Property.ypos"; //$NON-NLS-1$
    String PROPERTY_HEIGHT = "Property.height"; //$NON-NLS-1$
    String PROPERTY_WIDTH = "Property.width"; //$NON-NLS-1$
    
    /**
     * @return The Name
     */
    String getName();
    
    /**
     * Set the name
     * @param name
     */
    void setName(String name);
    
    /**
     * Set the bounds of this object
     * @param bounds
     */
    void setBounds(Rectangle bounds);

    /**
     * @return The bounds of this object
     */
    Rectangle getBounds();
    


}
