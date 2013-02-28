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
package org.tencompetence.ldauthor.graphicsmodel.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.ldauthor.graphicsmodel.IImageBank;
import org.tencompetence.ldauthor.utils.FileUtils;
import org.tencompetence.ldauthor.utils.StringUtils;


/**
 * ImageBank
 * 
 * @author Phillip Beauvoir
 * @version $Id: ImageBank.java,v 1.6 2009/05/19 18:21:05 phillipus Exp $
 */
public class ImageBank implements IImageBank {
    
    private HashMap<String, String> fMap = new HashMap<String, String>();
    
    private ILDModel fLDModel;
    
    public ImageBank(ILDModel ldModel) {
        fLDModel = ldModel;
    }
    
    public void fromJDOM(Element element) {
        for(Object o : element.getChildren("image", LDAUTHOR_NAMESPACE_EMBEDDED)) { //$NON-NLS-1$
            Element child = (Element)o;
            String id = child.getAttributeValue(LDModelFactory.IDENTIFIER_REF);
            String path = child.getAttributeValue("path"); //$NON-NLS-1$
            if(StringUtils.isSet(id) && StringUtils.isSet(path)) {
                // Use this opportunity to check that the object actually exists
                if(fLDModel.getModelObject(id) != null) {
                    addImage(id, getAbsolutePath(path));  // convert to absolute path (relative to LD root)
                }
            }
        }
    }

    public String getTagName() {
        return "images"; //$NON-NLS-1$
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), LDAUTHOR_NAMESPACE_EMBEDDED);
        
        for(String id : fMap.keySet()) {
            String path = fMap.get(id);
            if(StringUtils.isSet(path)) {
                Element child = new Element("image", LDAUTHOR_NAMESPACE_EMBEDDED); //$NON-NLS-1$
                child.setAttribute(LDModelFactory.IDENTIFIER_REF, id);
                child.setAttribute("path", getRelativePath(path));  // write relative path //$NON-NLS-1$
                element.addContent(child);
            }
        }
        
        return element;
    }

    public void addImage(String identifier, String iconPath) {
        fMap.put(identifier, iconPath);
    }

    public String getImage(String identifier) {
        return fMap.get(identifier);
    }
    
    private String getRelativePath(String path) {
        return FileUtils.getRelativePath(new File(path), fLDModel.getRootFolder());
    }
    
    private String getAbsolutePath(String path) {
        try {
            return new File(fLDModel.getRootFolder(), path).getCanonicalPath();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        return ""; //$NON-NLS-1$
    }
}
