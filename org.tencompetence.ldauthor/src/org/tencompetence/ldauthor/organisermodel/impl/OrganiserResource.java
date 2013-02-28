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
import org.tencompetence.ldauthor.organisermodel.IOrganiserResource;
import org.tencompetence.ldauthor.organisermodel.OrganiserModelFactory;



/**
 * Resource in Organiser View
 * 
 * @author Phillip Beauvoir
 * @version $Id: OrganiserResource.java,v 1.9 2008/06/02 22:15:30 phillipus Exp $
 */
public class OrganiserResource
extends AbstractOrganiserModelObject
implements IOrganiserResource {

    private String fLocation = ""; //$NON-NLS-1$
    private String fType = ""; //$NON-NLS-1$

    /**
     * Default constructor
     */
    public OrganiserResource() {
        super();
    }

    public OrganiserResource(String name) {
        super(name);
    }
    
    public String getResourceLocation() {
        return fLocation;
    }
    
    public void setResourceLocation(String location) {
        fLocation = location;
    }
    
    public String getResourceType() {
        return fType;
    }
    
    public void setResourceType(String type) {
        fType = type;
    }
    
    @Override
    public Element toJDOM() {
        Element element = super.toJDOM();
        
        element.setAttribute("type", fType); //$NON-NLS-1$
        element.setAttribute("location", fLocation); //$NON-NLS-1$
        
        return element;
    }

    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);
        fType = element.getAttributeValue("type"); //$NON-NLS-1$
        fLocation = element.getAttributeValue("location"); //$NON-NLS-1$
    }

    public String getTagName() {
        return OrganiserModelFactory.RESOURCE;
    }

}
