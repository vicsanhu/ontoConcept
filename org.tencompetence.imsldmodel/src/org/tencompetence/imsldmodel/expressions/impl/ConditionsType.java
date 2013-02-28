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

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IConditionType;
import org.tencompetence.imsldmodel.expressions.IConditionsType;
import org.tencompetence.imsldmodel.internal.StringUtils;


/**
 * ConditionsType
 * 
 * @author Phillip Beauvoir
 * @version $Id: ConditionsType.java,v 1.9 2009/11/26 09:16:04 phillipus Exp $
 */
public class ConditionsType implements IConditionsType {
    
    private ILDModel fLDModel;
    
    private String fTitle;
    
    private List<IConditionType> fConditions;

    public ConditionsType(ILDModel model) {
        fLDModel = model;
        fConditions = new ArrayList<IConditionType>();
    }

    public ILDModel getLDModel() {
        return fLDModel;
    }

    public String getTitle() {
        return fTitle;
    }

    public void setTitle(String title) {
        String old = fTitle;
        fTitle = title;
        getLDModel().firePropertyChange(this, PROPERTY_NAME, old, title);
    }
    
    public List<IConditionType> getConditions() {
        return fConditions;
    }
    
	public IConditionType addNewCondition() {
		IConditionType condition = new ConditionType(this);
		fConditions.add(condition);
		return condition;
	}

	public void deleteCondition(IConditionType condition) {
		if(condition != null) {
			fConditions.remove(condition);
		}
	}


    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        IConditionType condition = null;
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.TITLE)) {
                fTitle = child.getText();
            }
            else if(tag.equals(LDModelFactory.IF)) {
            	condition = new ConditionType(this);
            	condition.getIfType().fromJDOM(child);
                fConditions.add(condition);
            }
            else if(tag.equals(LDModelFactory.THEN) && condition != null) {
            	condition.getThenType().fromJDOM(child);
            }
            // Found an "Else" type. It could be one of two types...
            // A "Show/Hide" type (Else) or an "If" type (Else If)
            else if(tag.equals(LDModelFactory.ELSE) && condition != null) {
                // See if it has an "If" child element...
                if(child.getChild(LDModelFactory.IF, IMSLD_NAMESPACE_100_EMBEDDED) != null) {
                	condition.getElseIfType().fromJDOM(child);
                }
                else {
                	condition.getElseType().fromJDOM(child);
                }
            }
            else {
            	condition = null;
            }
        }
    }

    public String getTagName() {
        return LDModelFactory.CONDITIONS;
    }

    public Element toJDOM() {
        if(fConditions.isEmpty()) {
            return null;
        }
        
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);

        // Title
        if(StringUtils.isSet(getTitle())) {
            Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
            title.setText(getTitle());
            element.addContent(title);
        }
        
        // If-Then-Else
        for(IConditionType condition : fConditions) {
            Element e = condition.getIfType().toJDOM();
            element.addContent(e);
            
            e = condition.getThenType().toJDOM();
            element.addContent(e);
            
            if(condition.hasElseType()) {
                e = condition.getElseType().toJDOM();
                element.addContent(e);
            }
            else if(condition.hasElseIfType()) {
                e = condition.getElseIfType().toJDOM();
                element.addContent(e);
            }
        }
        
        return element;
    }

}
