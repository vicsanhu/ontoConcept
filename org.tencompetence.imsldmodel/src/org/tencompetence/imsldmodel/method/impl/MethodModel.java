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
package org.tencompetence.imsldmodel.method.impl;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.expressions.IConditionsType;
import org.tencompetence.imsldmodel.expressions.impl.ConditionsType;
import org.tencompetence.imsldmodel.method.ICompleteUOLType;
import org.tencompetence.imsldmodel.method.IConditionsModel;
import org.tencompetence.imsldmodel.method.IMethodModel;
import org.tencompetence.imsldmodel.method.IPlaysModel;
import org.tencompetence.imsldmodel.types.IOnCompletionType;
import org.tencompetence.imsldmodel.types.impl.OnCompletionType;

/**
 * Method Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: MethodModel.java,v 1.6 2009/11/26 09:16:04 phillipus Exp $
 */
public class MethodModel
implements IMethodModel {
    
    private IPlaysModel fPlays;
    
    private ICompleteUOLType fCompleteUOLType;
    
    private IOnCompletionType fCompletionType;
    
    private IConditionsModel fConditions;
    
    private ILDModel fLDModel;
    
    /**
     * Default Constructor
     */
    public MethodModel(ILDModel ldModel) {
        fLDModel = ldModel;
        fPlays = new PlaysModel(ldModel);
    }
    
    public ILDModel getLDModel() {
        return fLDModel;
    }

    public IPlaysModel getPlaysModel() {
        return fPlays;
    }

    public ICompleteUOLType getCompleteUOLType() {
        if(fCompleteUOLType == null) {
            fCompleteUOLType = new CompleteUOLType(this);
        }
        return fCompleteUOLType;
    }

    public IOnCompletionType getOnCompletionType() {
        if(fCompletionType == null) {
            fCompletionType = new OnCompletionType(this);
        }
        return fCompletionType;
    }
    

    public IConditionsModel getConditionsModel() {
        if(fConditions == null) {
            fConditions = new ConditionsModel(fLDModel);
        }
        return fConditions;
    }

    // ====================================== JDOM SUPPORT ===========================================

    public void fromJDOM(Element element) {
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            // Play
            if(tag.equals(LDModelFactory.PLAY)) {
                ILDModelObject play = LDModelFactory.createModelObject(LDModelFactory.PLAY, fLDModel);
                play.fromJDOM(child); // This first, in order to set ID
                fPlays.addChild(play);
            }
            
            // Complete UOL
            else if(tag.equals(LDModelFactory.COMPLETE_UOL)) {
                getCompleteUOLType().fromJDOM(child);
            }
            
            // Completion
            else if(tag.equals(LDModelFactory.ON_COMPLETION)) {
                getOnCompletionType().fromJDOM(child);
            }

            // Conditions
            else if(tag.equals(LDModelFactory.CONDITIONS)) {
                IConditionsType conditions = new ConditionsType(fLDModel);
                conditions.fromJDOM(child);
                getConditionsModel().addChild(conditions);
            }
        }
    }

    public Element toJDOM() {
        Element method = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        
        // Plays
        for(ILDModelObject play : fPlays.getChildren()) {
            Element child = play.toJDOM();
            method.addContent(child);
        }

        if(fCompleteUOLType != null) {
            Element child = fCompleteUOLType.toJDOM();
            if(child != null) {
                method.addContent(child);
            }
        }
        
        if(fCompletionType != null) {
            Element child = fCompletionType.toJDOM();
            if(child != null) {
                method.addContent(child);
            }
        }
        
        // Conditions
        if(fConditions != null) {
            for(ILDModelObject condition : fConditions.getChildren()) {
                Element child = condition.toJDOM();
                if(child != null) {
                    method.addContent(child);
                }
            }
        }
        
        return method;
    }

    public String getTagName() {
        return LDModelFactory.METHOD;
    }
}
