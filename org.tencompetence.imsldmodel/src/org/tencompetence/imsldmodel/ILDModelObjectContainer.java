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
package org.tencompetence.imsldmodel;

import java.util.List;




/**
 * Parent Container Model behavior
 * 
 * @author Phillip Beauvoir
 * @version $Id: ILDModelObjectContainer.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public interface ILDModelObjectContainer {
    
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
    List<ILDModelObject> getChildren();

    /**
     * Add child object
     * @param child
     */
    boolean addChild(ILDModelObject child);
    
    /**
     * Add a child object at given index position
     * @param child
     * @param index
     * @return
     */
    void addChildAt(ILDModelObject child, int index);
    
    /**
     * Remove child object
     * @param child
     */
    boolean removeChild(ILDModelObject child);
    
    /**
     * Move a child object to given index position
     * @param child
     * @param index
     */
    void moveChild(ILDModelObject child, int index);
    
    /**
     * Can delete a child object
     */
    boolean canDeleteChild(ILDModelObject child);
    
    /**
     * @param ref
     * @return True if this has a child given its ID
     */
    boolean hasChild(String id);

}
