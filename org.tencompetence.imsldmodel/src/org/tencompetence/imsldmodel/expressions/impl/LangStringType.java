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
import org.tencompetence.imsldmodel.expressions.ILangStringType;


/**
 * LangStringType
 * 
 * @author Phillip Beauvoir
 * @version $Id: LangStringType.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class LangStringType implements ILangStringType {
    
    private ILDModel fLDModel;
    
    private String fValue = ""; //$NON-NLS-1$
    
    public LangStringType(ILDModel ldModel) {
        fLDModel = ldModel;
    }
    
    public ILDModel getLDModel() {
        return fLDModel;
    }

    public String getValue() {
        return fValue;
    }

    public void setValue(String value) {
        fValue = value;
    }

    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        fValue = element.getText();
    }

    public String getTagName() {
        return LDModelFactory.LANGSTRING;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        element.setText(fValue);
        return element;
    }

}
