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
package org.tencompetence.imsldmodel.impl;

import java.util.ArrayList;
import java.util.List;

import org.tencompetence.imsldmodel.IIdentifier;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectContainer;
import org.tencompetence.imsldmodel.ILDModelObjectReference;
import org.tencompetence.imsldmodel.IParentable;


/**
 * LD Model Object Container
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDModelObjectContainer.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class LDModelObjectContainer
implements ILDModelObjectContainer {

    /**
     * Children
     */
    private List<ILDModelObject> fChildren = new ArrayList<ILDModelObject>();

    private ILDModel fLDModel;
    
    public LDModelObjectContainer(ILDModel ldModel) {
        fLDModel = ldModel;
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public boolean addChild(ILDModelObject child) {
        if(child != null && child != this && !fChildren.contains(child) && fChildren.add(child)) {
            addToIDMap(child);             // Store in ID Map table
            
            if(child instanceof IParentable) {  // Set Parent
                ((IParentable)child).setParent(this);
            }
        
            fLDModel.firePropertyChange(this, ILDModelObjectContainer.PROPERTY_CHILD_ADDED, null, child);
            
            return true;
        }
        
        return false;
    }

    public void addChildAt(ILDModelObject child, int index) {
        if(child != null && child != this && !fChildren.contains(child)) {
            // Sanity check
            if(index < 0) {
                index = 0;
            }
            
            // This can happen if the child object was previously removed - child count is reduced
            if(index > fChildren.size()) {
                index = fChildren.size();
            }
            
            fChildren.add(index, child);
            
            addToIDMap(child);             // Store in ID Map table
            
            if(child instanceof IParentable) {  // Set Parent
                ((IParentable)child).setParent(this);
            }
            
            fLDModel.firePropertyChange(this, ILDModelObjectContainer.PROPERTY_CHILD_ADDED, null, child);
        }
    }

    public boolean removeChild(ILDModelObject child) {
        if(child != null && fChildren.remove(child)) {
            if(child instanceof IParentable) {  // Set Parent
                ((IParentable)child).setParent(null);
            }
            
            fLDModel.firePropertyChange(this, ILDModelObjectContainer.PROPERTY_CHILD_REMOVED, null, child);
            
            /*
             * If we remove the child from the ID Map table this will cause a problem for model listeners
             * who react to the event and attempt to find the object from ILDModel.getModelObject(String id)
             * or via ILDModelObjectReference.getLDModelObject().
             * So, id we do remove the child from the ID Map table at least remove it after firing the event...
             */
            // removeFromIDMap(child);
            
            return true;
        }
        return false;
    }
    
    public void moveChild(ILDModelObject child, int index) {
        if(child != null && fChildren.remove(child)) {
            // Sanity check
            if(index < 0) {
                index = 0;
            }
            // This can happen if the child object was previously removed - child count is reduced
            if(index > fChildren.size()) {
                index = fChildren.size();
            }
            
            fChildren.add(index, child);
            
            fLDModel.firePropertyChange(this, ILDModelObjectContainer.PROPERTY_CHILD_MOVED, null, child);
        }
    }
    
    public List<ILDModelObject> getChildren() {
        return fChildren;
    }

    public boolean canDeleteChild(ILDModelObject child) {
        return true;
    }
    
    public boolean hasChild(String id) {
        if(id == null) {
            return false;
        }
        
        for(ILDModelObject child : getChildren()) {
            if((child instanceof IIdentifier) && id.equals(((IIdentifier)child).getIdentifier())) {
                return true;
            }
            else if((child instanceof ILDModelObjectReference) && id.equals(((ILDModelObjectReference)child).getReferenceIdentifier())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add the child to the grand object ID map
     * @param child
     */
    private void addToIDMap(ILDModelObject child) {
        if(child instanceof IIdentifier) {
            fLDModel.putModelObject(((IIdentifier)child).getIdentifier(), child);
        }
    }
    
    /**
     * Remove the child from the grand object ID map
     * @param child
     */
    @SuppressWarnings("unused")
    private void removeFromIDMap(ILDModelObject child) {
        if(child instanceof IIdentifier) {
            fLDModel.removeModelObject(((IIdentifier)child).getIdentifier());
        }
    }
}
