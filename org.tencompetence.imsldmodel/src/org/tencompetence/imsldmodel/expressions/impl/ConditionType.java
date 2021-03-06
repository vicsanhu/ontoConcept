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

import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.expressions.IConditionType;
import org.tencompetence.imsldmodel.expressions.IElseIfType;
import org.tencompetence.imsldmodel.expressions.IElseType;
import org.tencompetence.imsldmodel.expressions.IIfType;
import org.tencompetence.imsldmodel.expressions.IThenType;


/**
 * Container for If-Then-Else Type
 * 
 * @author Phillip Beauvoir
 * @version $Id: ConditionType.java,v 1.5 2009/11/26 09:16:04 phillipus Exp $
 */
public class ConditionType implements IConditionType {
    
    private IIfType fIfType;
    
    private IThenType fThenType;

    private IElseType fElseType;
    
    private IElseIfType fElseIfType;
    
    private ILDModelObject fParent;
    
    public ConditionType(ILDModelObject parent) {
        fParent = parent;
        fIfType = new IfType(fParent.getLDModel());
        fThenType = new ThenType(fParent.getLDModel());
    }

    public IIfType getIfType() {
        return fIfType;
    }

    public IThenType getThenType() {
        return fThenType;
    }

    public IElseType getElseType() {
        if(fElseType == null) {
            fElseType = new ElseType(fParent.getLDModel());
            fElseIfType = null; // Mutually exclusive
        }
        return fElseType;
    }
    
    public IElseIfType getElseIfType() {
        if(fElseIfType == null) {
            fElseIfType = new ElseIfType(fParent.getLDModel());
            fElseType = null; // Mutually exclusive
        }
        return fElseIfType;
    }

    public boolean hasElseType() {
        return fElseType != null;
    }
    
    public boolean hasElseIfType() {
        return fElseIfType != null;
    }
    
    public void clearElseType() {
        fElseType = null;
        fElseIfType = null;
    }

    public ILDModelObject getParent() {
        return fParent;
    }
}
