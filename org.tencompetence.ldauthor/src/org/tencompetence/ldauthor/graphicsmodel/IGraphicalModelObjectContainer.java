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

import java.util.List;

import org.tencompetence.imsldmodel.ILDModelObject;



/**
 * Parent Container Model behavior
 * 
 * @author Phillip Beauvoir
 * @version $Id: IGraphicalModelObjectContainer.java,v 1.4 2009/05/19 18:21:05 phillipus Exp $
 */
public interface IGraphicalModelObjectContainer 
extends IGraphicalModelObject {
    
    /*
     * This group of Property names should all start with "Property.child" in order
     * that receivers can test the type of change.
     */
    String PROPERTY_CHILD = "Property.child"; //$NON-NLS-1$
    String PROPERTY_CHILD_ADDED = "Property.child_added"; //$NON-NLS-1$
    String PROPERTY_CHILD_REMOVED = "Property.child_removed"; //$NON-NLS-1$
    String PROPERTY_CHILD_MOVED = "Property.child_moved"; //$NON-NLS-1$
    
    /**
     * @return Children objects
     */
    List<IGraphicalModelObject> getChildren();

    /**
     * Add child object
     * @param child
     * @param notifyLDModel If this is true notify the LD Model
     */
    boolean addChild(IGraphicalModelObject child, boolean notifyLDModel);
    
    /**
     * Add a child object at given index position
     * @param child
     * @param index
     * @param notifyLDModel If this is true notify the LD Model
     */
    void addChildAt(IGraphicalModelObject child, int index, boolean notifyLDModel);
    
    /**
     * Remove child object
     * @param child
     * @param notifyLDModel If this is true notify the LD Model
     */
    boolean removeChild(IGraphicalModelObject child, boolean notifyLDModel);
    
    /**
     * Move child to a specific index
     * @param child
     * @param index
     * @param notifyLDModel If this is true notify the LD Model
     */
    boolean moveChild(IGraphicalModelObject child, int index, boolean notifyLDModel);
    
    /**
     * Can delete a child object
     */
    boolean canDeleteChild(IGraphicalModelObject child);
    
    /**
     * Return true if this has a child with LD object of given id
     * 
     * @param id The ID of the LD object
     * @return
     */
    boolean hasLDObjectChild(String id);
    
    /**
     * Get a graphical child object given the LD object it wraps
     * @param object
     * @return
     */
    IGraphicalModelObject getGraphicalLDChild(ILDModelObject object);
    
    /**
     * Add graphical child entries matching the LD model
     */
    void reconcile();
}
