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
package org.tencompetence.ldauthor.templates.impl.ld;

import java.io.File;

import org.jdom.Element;
import org.tencompetence.ldauthor.LDAuthorException;
import org.tencompetence.ldauthor.serialization.IJDOMPersistence;
import org.tencompetence.ldauthor.serialization.LDModelSerializer;
import org.tencompetence.ldauthor.templates.ILDTemplateXMLTags;
import org.tencompetence.ldauthor.utils.FileUtils;


/**
 * Abstract LD Template mad by copying from folder
 * @author Phillip Beauvoir
 * @version $Id: AbstractCopiedLDTemplate.java,v 1.5 2010/01/06 15:34:25 phillipus Exp $
 */
public abstract class AbstractCopiedLDTemplate extends AbstractLDTemplate
implements IJDOMPersistence, ILDTemplateXMLTags {
    
    private String fLocation;
    
    public AbstractCopiedLDTemplate() {
        super();
    }

    @Override
    public void create(File manifestFile) throws LDAuthorException {
        super.create(manifestFile);
        
        LDModelSerializer serialiser = new LDModelSerializer(fLDModel);

        try {
            File srcFolder = getSourceFolder();
            
            // Copy Folder
            if(srcFolder != null && srcFolder.exists()) {
                FileUtils.copyFolder(srcFolder, fLDModel.getRootFolder());
            }

            // Load Manifest
            serialiser.loadModel();

            // Change IDs
            fLDModel.resetIDs();

            // Add user stuff
            addDefaultTitleLOsAndPrereqs();

            // Save again
            serialiser.saveModel();
        }
        catch(Exception ex) {
            throw new LDAuthorException(ex);
        }
    }
    
    /**
     * @return The relative path name for the location of this template, relative to Templates folder
     */
    public String getLocation() {
        return fLocation;
    }
    
    public void setLocation(String path) {
        fLocation = path;
    }

    /**
     * @return The Source Folder of the template files
     */
    public abstract File getSourceFolder();
    
    public void fromJDOM(Element element) {
        setName(element.getAttributeValue(XMLTAG_NAME));
        setLocation(element.getAttributeValue(XMLTAG_LOCATION));
    }

    public Element toJDOM() {
        Element element = new Element(getTagName());
        element.setAttribute(XMLTAG_NAME, getName());
        element.setAttribute(XMLTAG_LOCATION, getLocation());
        return element;
    }

}
