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
package org.tencompetence.ldauthor.organisermodel.impl;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.tencompetence.ldauthor.organisermodel.IOrganiserContainer;
import org.tencompetence.ldauthor.organisermodel.IOrganiserObject;
import org.tencompetence.ldauthor.organisermodel.OrganiserModelFactory;


/**
 * Abstract Organiser Model container
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractOrganiserContainer.java,v 1.5 2007/10/01 22:18:34 phillipus Exp $
 */
public abstract class AbstractOrganiserContainer
extends AbstractOrganiserModelObject
implements IOrganiserContainer {

    /**
     * Children
     */
    private List<IOrganiserObject> fChildren = new ArrayList<IOrganiserObject>();

    protected AbstractOrganiserContainer() {
        super();
    }
    
    protected AbstractOrganiserContainer(String name) {
        super(name);
    }

    public List<IOrganiserObject> getChildren() {
        return fChildren;
    }
    
    public boolean addChild(IOrganiserObject child) {
        if(child != null && child != this && !fChildren.contains(child) && fChildren.add(child)) {
            child.setParent(this);
            OrganiserIndex.getInstance().fireStructureChange(PROPERTY_CHILD_ADDED, child);
            return true;
        }
        return false;
    }
    
    public boolean removeChild(IOrganiserObject child) {
        if(fChildren.remove(child)) {
            OrganiserIndex.getInstance().fireStructureChange(PROPERTY_CHILD_REMOVED, child);
            return true;
        }
        return false;
    }
    
    @Override
    public IOrganiserObject clone() {
        AbstractOrganiserContainer clone = (AbstractOrganiserContainer)super.clone();
        
        // New children and clone them
        clone.fChildren = new ArrayList<IOrganiserObject>();
        for(IOrganiserObject child : fChildren) {
            clone.addChild(child.clone());
        }

        return clone;
    }
    
    // ================================== XML JDOM PERSISTENCE =================================
    
    @Override
    public Element toJDOM() {
        Element element = super.toJDOM();
        
        // Children
        for(IOrganiserObject object : getChildren()) {
            Element child = object.toJDOM();
            element.addContent(child);
        }

        return element;
    }
    
    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);
        
        for(Object childElement : element.getChildren()) {
            IOrganiserObject childObject = OrganiserModelFactory.createModelObject(((Element)childElement).getName());
            if(childObject != null) {
                addChild(childObject);
                childObject.fromJDOM((Element)childElement);
            }
        }
    }
 
}
