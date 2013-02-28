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
import org.tencompetence.imsldmodel.expressions.IElseIfType;
import org.tencompetence.imsldmodel.expressions.IConditionType;


/**
 * Else If Type
 * 
 * Contains 1 If-Then-Else construct
 * 
 * @author Phillip Beauvoir
 * @version $Id: ElseIfType.java,v 1.6 2009/11/26 09:16:04 phillipus Exp $
 */
public class ElseIfType implements IElseIfType {
    
    private ILDModel fLDModel;
    
    /**
     * Has one Condition
     */
    private IConditionType fCondition;

    public ElseIfType(ILDModel model) {
        fLDModel = model;
        fCondition = new ConditionType(this);
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public IConditionType getCondition() {
        return fCondition;
    }
    
    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.IF)) {
                fCondition.getIfType().fromJDOM(child);
            }
            else if(tag.equals(LDModelFactory.THEN)) {
                fCondition.getThenType().fromJDOM(child);
            }
            // Found an "Else" type. It could be one of two types...
            // A "Show/Hide" type (Else) or an "If" type (Else If)
            else if(tag.equals(LDModelFactory.ELSE)) {
                // See if it has an "If" child element...
                if(child.getChild(LDModelFactory.IF, IMSLD_NAMESPACE_100_EMBEDDED) != null) {
                    fCondition.getElseIfType().fromJDOM(child);
                }
                else {
                    fCondition.getElseType().fromJDOM(child);
                }
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.ELSE;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        Element e = fCondition.getIfType().toJDOM();
        element.addContent(e);

        e = fCondition.getThenType().toJDOM();
        element.addContent(e);

        if(fCondition.hasElseType()) {
            e = fCondition.getElseType().toJDOM();
            element.addContent(e);
        }
        else if(fCondition.hasElseIfType()) {
            e = fCondition.getElseIfType().toJDOM();
            element.addContent(e);
        }
        
        return element;
    }

}
