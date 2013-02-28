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
package org.tencompetence.ldauthor.graphicsmodel.environment.impl;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.ISendMailModel;
import org.tencompetence.ldauthor.graphicsmodel.environment.IGraphicalSendMailModel;
import org.tencompetence.ldauthor.graphicsmodel.impl.AbstractGraphicalModelObject;
import org.tencompetence.ldauthor.ldmodel.util.LDModelUtils;



/**
 * Send Mail Service class
 * 
 * @author Phillip Beauvoir
 * @version $Id: GraphicalSendMailModel.java,v 1.15 2009/05/22 16:35:05 phillipus Exp $
 */
public class GraphicalSendMailModel
extends AbstractGraphicalModelObject implements IGraphicalSendMailModel {
	
    private ISendMailModel fSendMailModel;
    
    public GraphicalSendMailModel(ILDModel ldModel) {
        super(ldModel);
    }
    
    public void createLDModelObject() {
        ISendMailModel sendmail = (ISendMailModel)LDModelFactory.createModelObject(LDModelFactory.SEND_MAIL, getLDModel());
        setLDModelObject(sendmail);
        LDModelUtils.setNewObjectDefaults(sendmail);
    }

    public ILDModelObject getLDModelObject() {
        return fSendMailModel;
    }
    
    public void setLDModelObject(ILDModelObject object) {
        fSendMailModel = (ISendMailModel)object;
    }
    
    public void setName(String name) {
        if(fSendMailModel != null) {
            fSendMailModel.setTitle(name);
        }
    }

    public String getName() {
        if(fSendMailModel == null) {
            return LDModelUtils.getUserObjectName(LDModelFactory.SEND_MAIL);
        }
        return fSendMailModel.getTitle();
    }

    public String getIdentifier() {
        if(fSendMailModel != null) {
            return fSendMailModel.getIdentifier();
        }
        return "no-id"; //$NON-NLS-1$
    }

    // ================================== XML JDOM PERSISTENCE =================================
    
    @Override
    public Element toJDOM() {
        Element element = super.toJDOM();
        element.setAttribute(LDModelFactory.REF, getIdentifier());
        return element;
    }
    
    @Override
    public void fromJDOM(Element element) {
        super.fromJDOM(element);
        
        // Add referenced LO object
        String refID = element.getAttributeValue(LDModelFactory.REF);
        ILDModelObject ldModelObject = getLDModel().getModelObject(refID);
        setLDModelObject(ldModelObject);
    }
    
    public String getTagName() {
        return LDModelFactory.SEND_MAIL;
    }
}
