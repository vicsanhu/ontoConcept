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
package org.tencompetence.imsldmodel.environments.impl;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.IServiceModel;
import org.tencompetence.imsldmodel.internal.StringUtils;


/**
 * AbstractServiceModel
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractServiceModel.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public abstract class AbstractServiceModel implements IServiceModel {

    private ILDModel fLDModel;
    
    protected String fID;
    private boolean fIsVisible = true;
    private String fClass, fParameters;

    
    protected AbstractServiceModel(ILDModel ldModel) {
        fLDModel = ldModel;
    }
    
    public void setIdentifier(String id) {
        fID = id;
    }

    public String getClassString() {
        return fClass;
    }

    public String getParameters() {
        return fParameters;
    }

    public boolean isVisible() {
        return fIsVisible;
    }

    public void setIsVisible(boolean set) {
        fIsVisible = set;
    }

    public void setClassString(String s) {
        fClass = s;
    }

    public void setParameters(String s) {
        fParameters = s;
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public void fromJDOM(Element element) {
        fID = element.getAttributeValue(LDModelFactory.IDENTIFIER);
        fIsVisible = !"false".equals(element.getAttributeValue(LDModelFactory.ISVISIBLE)); //$NON-NLS-1$
        fClass = element.getAttributeValue(LDModelFactory.CLASS);
        fParameters = element.getAttributeValue(LDModelFactory.PARAMETERS);
    }

    public Element toJDOM() {
        Element service = new Element(LDModelFactory.SERVICE, IMSLD_NAMESPACE_100_EMBEDDED);
        
        service.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());
        service.setAttribute(LDModelFactory.ISVISIBLE, "" + isVisible()); //$NON-NLS-1$
        
        if(StringUtils.isSet(fClass)) {
            service.setAttribute(LDModelFactory.CLASS, fClass);
        }

        if(StringUtils.isSet(fParameters)) {
            service.setAttribute(LDModelFactory.PARAMETERS, fParameters);
        }
        
        return service;
    }

}
