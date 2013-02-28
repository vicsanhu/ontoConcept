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
package org.tencompetence.imsldmodel.resources.impl;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.IMSNamespaces;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.internal.StringUtils;
import org.tencompetence.imsldmodel.resources.IResourceFileModel;


/**
 * CP Resource File
 * 
 * @author Phillip Beauvoir
 * @version $Id: ResourceFile.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class ResourceFile implements IResourceFileModel {
    
    private ILDModel fLDModel;
    
    private String fHref;
    
    public ResourceFile(ILDModel ldModel) {
        fLDModel = ldModel;
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public String getHref() {
        return fHref;
    }

    public void setHref(String href) {
        String old = fHref;
        fHref = href;
        fLDModel.firePropertyChange(this, PROPERTY_RESOURCEFILE_HREF, old, href);
    }

    public void fromJDOM(Element element) {
        fHref =  element.getAttributeValue(LDModelFactory.HREF);
        if(fHref != null) {
            fHref = fHref.replaceAll("%20", " "); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    public String getTagName() {
        return LDModelFactory.FILE;
    }

    public Element toJDOM() {
        Element file = new Element(getTagName(), IMSNamespaces.IMSCP_NAMESPACE_114);
        
        if(StringUtils.isSet(fHref)) {
            // Convert spaces to %20 
            String href = fHref.replaceAll(" ", "%20"); //$NON-NLS-1$ //$NON-NLS-2$
            file.setAttribute(LDModelFactory.HREF, href);
        }
        
        return file;
    }
}
