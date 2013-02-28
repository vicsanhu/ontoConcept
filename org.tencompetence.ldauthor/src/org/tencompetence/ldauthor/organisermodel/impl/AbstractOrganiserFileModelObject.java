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

import java.io.File;
import java.io.IOException;

import org.jdom.Element;
import org.tencompetence.ldauthor.organisermodel.IOrganiserBackingFile;
import org.tencompetence.ldauthor.organisermodel.IOrganiserObject;
import org.tencompetence.ldauthor.utils.FileUtils;


/**
 * Abstract Organiser Model object with backing File
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractOrganiserFileModelObject.java,v 1.9 2008/06/02 22:15:30 phillipus Exp $
 */
public abstract class AbstractOrganiserFileModelObject
extends AbstractOrganiserModelObject
implements IOrganiserBackingFile {
    
    /**
     * Store only the name portion, since the Organiser folder may change.
     * This fileName has to be generated here since this entry is
     * added straight away to the OrganiserIndex using the fileName as key.
     */
    protected String fileName = generateFileName();
    
    /**
     * Default constructor
     */
    protected AbstractOrganiserFileModelObject() {
        super();
    }

    protected AbstractOrganiserFileModelObject(String name) {
        super(name);
    }

    /**
     * @return A generated file name for the model object
     */
    protected abstract String generateFileName();
    
    /** 
     * This operation also copies the backing file, so don't call it unless necessary.
     */
    @Override
    public IOrganiserObject clone() {
        AbstractOrganiserFileModelObject clone = (AbstractOrganiserFileModelObject)super.clone();
        
        // Copy backing file as well
        clone.fileName = generateFileName();
        
        try {
            File file = getFile();
            if(file.exists()) {
                FileUtils.copyFile(file, clone.getFile(), false);
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        return clone;
    }
    
    @Override
    public Element toJDOM() {
        Element element = super.toJDOM();
        element.setAttribute("file", fileName); //$NON-NLS-1$
        return element;
    }

    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);
        fileName = element.getAttributeValue("file"); //$NON-NLS-1$
    }
}
