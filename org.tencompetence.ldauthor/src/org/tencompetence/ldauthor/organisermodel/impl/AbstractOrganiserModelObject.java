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

import org.jdom.Element;
import org.tencompetence.ldauthor.organisermodel.IOrganiserContainer;
import org.tencompetence.ldauthor.organisermodel.IOrganiserObject;


/**
 * Abstract Organiser Model object
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractOrganiserModelObject.java,v 1.7 2008/06/02 22:15:30 phillipus Exp $
 */
public abstract class AbstractOrganiserModelObject
implements IOrganiserObject {
    
    private String fName = ""; //$NON-NLS-1$
    
    private IOrganiserContainer fParent;
    
    /**
     * Default constructor
     */
    protected AbstractOrganiserModelObject() {
    }

    protected AbstractOrganiserModelObject(String name) {
        this();
        setName(name);
    }

    public void setName(String name) {
        String old = fName;
        fName = name;
        OrganiserIndex.getInstance().firePropertyChange(this, PROPERTY_NAME, old, name);
    }
    
    public String getName() {
        return fName;
    }

    public IOrganiserContainer getParent() {
        return fParent;
    }

    public void setParent(IOrganiserContainer parent) {
        fParent = parent;
    }

    public Object getEditableValue() {
        return null;
    }

    @Override
    public IOrganiserObject clone() {
        try {
            Object o = super.clone();
            return (IOrganiserObject)o;
        }
        catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
    
    // ================================== XML JDOM PERSISTENCE =================================
    
    public Element toJDOM() {
        Element element = new Element(getTagName());
        element.setAttribute("name", getName()); //$NON-NLS-1$
        return element;
    }
    
    public void fromJDOM(Element element) {
        // Name
        String name = element.getAttributeValue("name"); //$NON-NLS-1$
        if(name != null) {
            setName(name);
        }
    }
    
}
