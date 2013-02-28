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

import org.tencompetence.imsldmodel.IIdentifier;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.ILDModelObjectReference;


/**
 * A list that treats LD model objects and references as equal if they reference the same object.
 * We use this rather than over-riding the equals() method of the objects themselves since trees
 * and tables rely on non-equality of these objects.
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDModelObjectArrayList.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class LDModelObjectArrayList extends ArrayList<ILDModelObject> {

    public LDModelObjectArrayList() {
    }

    @Override
    public boolean contains(Object elem) {
        elem = getObjectRef(elem);
        return super.contains(elem);
    }
    
    @Override
    public boolean remove(Object o) {
        o = getObjectRef(o);
        return super.remove(o);
    }
    
    @Override
    public int indexOf(Object elem) {
        elem = getObjectRef(elem);
        return super.indexOf(elem);
    }
    
    @Override
    public int lastIndexOf(Object elem) {
        elem = getObjectRef(elem);
        return super.lastIndexOf(elem);
    }
    
    
    /**
     * Find the real object
     * @param elem
     * @return
     */
    protected Object getObjectRef(Object elem) {
        String id = null;
        
        if(elem instanceof ILDModelObjectReference) {
            id = ((ILDModelObjectReference)elem).getReferenceIdentifier();
        }
        else if(elem instanceof IIdentifier) {
            id = ((IIdentifier)elem).getIdentifier();
        }
        
        if(id != null) {
            for(int i = 0; i < size(); i++) {
                ILDModelObject child = get(i);
                if(child instanceof ILDModelObjectReference && ((ILDModelObjectReference)child).getReferenceIdentifier().equals(id)) {
                    return child;
                }
                if(child instanceof IIdentifier && ((IIdentifier)child).getIdentifier().equals(id)) {
                    return child;
                }
            }
        }
        
        return elem;
    }
}
