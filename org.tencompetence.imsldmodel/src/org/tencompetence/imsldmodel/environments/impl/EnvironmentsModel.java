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
import org.tencompetence.imsldmodel.ILDModelObject;
import org.tencompetence.imsldmodel.LDModelFactory;
import org.tencompetence.imsldmodel.environments.IEnvironmentsModel;
import org.tencompetence.imsldmodel.impl.LDModelObjectContainer;


/**
 * Description
 * 
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentsModel.java,v 1.3 2009/11/26 09:16:04 phillipus Exp $
 */
public class EnvironmentsModel extends LDModelObjectContainer
implements IEnvironmentsModel {

    public EnvironmentsModel(ILDModel model) {
        super(model);
    }

    public String getTagName() {
        return LDModelFactory.ENVIRONMENTS;
    }

    public Element toJDOM() {
        Element element = new Element(getTagName(), IMSLD_NAMESPACE_100_EMBEDDED);

        // Children
        for(ILDModelObject object : getChildren()) {
            Element child = object.toJDOM();
            element.addContent(child);
        }

        return element.getChildren().size() == 0 ? null : element;
    }

    public void fromJDOM(Element element) {
        // Dynamically create objects from children
        for(Object child : element.getChildren()) {
            ILDModelObject object = LDModelFactory.createModelObject(((Element)child).getName(), getLDModel());
            if(object != null) {
                object.fromJDOM((Element)child); // This first, in order to set ID
                addChild(object);
            }
        }
    }

    @Override
    public String toString() {
        return getLDModel().getObjectName(LDModelFactory.ENVIRONMENTS);
    }
}
