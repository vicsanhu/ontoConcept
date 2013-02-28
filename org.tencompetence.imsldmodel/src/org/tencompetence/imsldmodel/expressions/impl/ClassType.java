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
package org.tencompetence.imsldmodel.expressions.impl;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IClassType;
import org.tencompetence.imsldmodel.internal.StringUtils;


/**
 * Class Type
 * 
 * @author Phillip Beauvoir
 * @version $Id: ClassType.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class ClassType implements IClassType {
    
    private ILDModel fLDModel;
    
    private String fClass = ""; //$NON-NLS-1$
    
    private String fTitle = ""; //$NON-NLS-1$
    
    private boolean fWithControl;

    public ClassType(ILDModel ldModel) {
        fLDModel = ldModel;
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public String getClassString() {
        return fClass;
    }

    public String getTitle() {
        return fTitle;
    }

    public boolean isWithControl() {
        return fWithControl;
    }

    public void setClassString(String s) {
        fClass = s;
    }

    public void setTitle(String title) {
        fTitle = title;
    }

    public void setWithControl(boolean set) {
        fWithControl = set;
    }

    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        fClass = element.getAttributeValue(LDModelFactory.CLASS);
        fTitle = element.getAttributeValue(LDModelFactory.TITLE);
        fWithControl = "true".equals(element.getAttributeValue(LDModelFactory.WITH_CONTROL)); //$NON-NLS-1$
    }

    public String getTagName() {
        return LDModelFactory.CLASS;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);

        if(StringUtils.isSet(fClass)) {
            element.setAttribute(LDModelFactory.CLASS, fClass);
        }
        
        element.setAttribute(LDModelFactory.WITH_CONTROL, fWithControl ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$
        
        if(StringUtils.isSet(fTitle)) {
            element.setAttribute(LDModelFactory.TITLE, fTitle);
        }
        
        return element;
    }
}
