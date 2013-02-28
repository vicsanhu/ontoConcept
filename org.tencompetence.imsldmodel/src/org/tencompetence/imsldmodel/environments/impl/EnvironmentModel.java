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

import java.util.List;
import java.util.UUID;

import org.jdom.Element;
import org.tencompetence.imsldmodel.ILDModel;
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.IEnvironmentModel;
import org.tencompetence.imsldmodel.impl.LDModelObjectContainer;

/**
 * Environment Model
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentModel.java,v 1.4 2009/11/26 09:16:04 phillipus Exp $
 */
public class EnvironmentModel extends LDModelObjectContainer
implements IEnvironmentModel {
    
    private String fID;
    private String fTitle;

    public EnvironmentModel(ILDModel ldModel) {
        super(ldModel);
    }

    public String getIdentifier() {
        if(fID == null) {
            fID = "env-" + UUID.randomUUID().toString(); //$NON-NLS-1$
        }
        return fID;
    }

    public void setIdentifier(String id) {
        fID = id;
    }
    
    public String getTitle() {
        return fTitle == null ? getLDModel().getObjectName(LDModelFactory.ENVIRONMENT) : fTitle;
    }

    public void setTitle(String title) {
        String old = fTitle;
        fTitle = title;
        getLDModel().firePropertyChange(this, ILDModelObject.PROPERTY_NAME, old, title);
    }

    public void fromJDOM(Element element) {
        fID = element.getAttributeValue(LDModelFactory.IDENTIFIER);
        
        for(Object o : element.getChildren()) {
            Element child = (Element)o;
            String tag = child.getName();
            
            if(tag.equals(LDModelFactory.TITLE)) {
                fTitle = child.getText();
                continue;
            }
            
            if(tag.equals(LDModelFactory.SERVICE)) {
                // Dig in to the "service" element to get the actual element tag name
                List<?> children = child.getChildren();
                
                // First child should be the actual object...
                if(!children.isEmpty()) {
                    Element service = (Element)children.get(0);
                    if(service != null) {
                        tag = service.getName();
                    }
                }
            }

            ILDModelObject object = LDModelFactory.createModelObject(tag, getLDModel());
            if(object != null) {
                object.fromJDOM((Element)child); // This first, in order to set ID
                addChild(object);
            }
        }
    }

    public Element toJDOM() {
        Element env = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);
        env.setAttribute(LDModelFactory.IDENTIFIER, getIdentifier());

        // Title
        Element title = new Element(LDModelFactory.TITLE, IMSLD_NAMESPACE_100_EMBEDDED);
        title.setText(getTitle());
        env.addContent(title);
        
        // Children
        for(ILDModelObject object : getChildren()) {
            Element child = object.toJDOM();
            env.addContent(child);
        }
        
        return env;
    }

    public String getTagName() {
        return LDModelFactory.ENVIRONMENT;
    }
}
